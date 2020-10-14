/*
 * Copyright 2020 William Smith
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 */

package pro.wsmi.roommap.client.backend

import freemarker.template.Configuration
import freemarker.template.Template
import kotlinx.serialization.ExperimentalSerializationApi
import org.http4k.core.*
import org.http4k.core.cookie.Cookie
import org.http4k.core.cookie.cookie
import pro.wsmi.kwsmilib.language.Language
import pro.wsmi.kwsmilib.net.http.convertRawAcceptLanguageHeaderToBCP47LanguageTags
import pro.wsmi.roommap.client.backend.config.ClientConfiguration
import pro.wsmi.roommap.client.backend.matrix_rooms_page.handleMatrixRoomsPageReq
import pro.wsmi.roommap.client.lib.MAIN_LANG_COOKIE_NAME
import java.io.File
import java.io.StringWriter
import java.util.*
import java.util.concurrent.locks.ReentrantLock


@ExperimentalSerializationApi
val availableLang = setOf(
    Language.ENG,
    Language.FRA
)
@ExperimentalSerializationApi
val defaultLang = Language.ENG

private const val PAGE_404_TEMPLATE_FILE_NAME = "404_page.ftlh"
private const val PAGE_404_CSS_FILE_NAME = "404_page.css"

@ExperimentalSerializationApi
fun get404HttpResponse(debugMode: Boolean, clientCfg: ClientConfiguration, pageMainLang: Language, globalBundle: ResourceBundle, freemarkerTemplate: Template) : Response
{
    val freemarkerModel = mapOf(
        "debug_mode" to debugMode,
        "website_info" to mapOf(
            "name" to clientCfg.websiteName,
            "texts" to globalBundle,
        ),
        "page_info" to mapOf(
            "path_name" to "",
            "main_lang" to pageMainLang,
            "css_files" to listOf(PAGE_404_CSS_FILE_NAME),
            "template_file" to PAGE_404_TEMPLATE_FILE_NAME,
            "texts" to ResourceBundle.getBundle("pro.wsmi.roommap.client.backend.Page404UITexts", Locale(pageMainLang.bcp47)),
        ),
        "query_parameters" to Page404QueryParameters()
    )

    val stringWriter = StringWriter()
    freemarkerTemplate.process(freemarkerModel, stringWriter)

    return Response(Status.NOT_FOUND)
        .body(stringWriter.toString())
        .header("Content-Type", ContentType.TEXT_HTML.toHeaderValue())
}

@ExperimentalSerializationApi
fun handleMainHttpRequest(debugMode: Boolean, clientCfg: ClientConfiguration, freemarkerCfg: Configuration, globalTemplateFile: File, businessData: BusinessData, businessDataLock: ReentrantLock) : HttpHandler = { req ->

    val httpReqAcceptLanguageHeader = req.header("Accept-Language")
    val httpReqAcceptedLanguageTags = if (httpReqAcceptLanguageHeader != null)
        convertRawAcceptLanguageHeaderToBCP47LanguageTags(httpReqAcceptLanguageHeader)
    else null

    val mainLangReqByHttp = if (httpReqAcceptedLanguageTags != null)
    {
        var lang : Language? = null
        for (tag in httpReqAcceptedLanguageTags)
        {
            if (tag.lang == Language.ENG || tag.lang == Language.FRA)
            {
                lang = tag.lang
                break
            }
        }
        lang
    }
    else null

    val mainLangReqByCookie = req.cookie(MAIN_LANG_COOKIE_NAME)?.value?.toLowerCase().let {
        if (it != null) {
            val lang = Language.getFromISO639_3(it)
            if (availableLang.contains(lang)) lang
            else null
        }
        else null
    }

    val mainLangReqByPath = req.uri.path.let { path ->
        Regex("^\\/([a-z]{3})((\\/.*)?)$").find(path)?.groupValues?.get(1)?.let {
            val lang = Language.getFromISO639_3(it)
            if (availableLang.contains(lang)) lang
            else null
        }
    }

    val pageMainLang = when {
        mainLangReqByPath != null -> mainLangReqByPath
        mainLangReqByCookie != null -> mainLangReqByCookie
        mainLangReqByHttp != null -> mainLangReqByHttp
        else -> defaultLang
    }


    val requestedPagePath = if (mainLangReqByPath != null)
        req.uri.path.replace(Regex("^\\/${mainLangReqByPath.iso639_3}\\/?"), "/")
    else
        req.uri.path


    businessDataLock.lock()
    val frozenMatrixServerList = businessData.matrixServers
    val frozenMatrixRoomList = businessData.matrixRooms
    businessDataLock.unlock()

    val pageMainLocale = Locale(pageMainLang.bcp47)
    val globalBundle = ResourceBundle.getBundle("pro.wsmi.roommap.client.backend.GlobalUITexts", pageMainLocale)
    freemarkerCfg.locale = pageMainLocale
    val freemarkerTemplate = freemarkerCfg.getTemplate(globalTemplateFile.name)

    when {
        requestedPagePath == "/" -> handleMatrixRoomsPageReq(req, debugMode, clientCfg, pageMainLang, globalBundle, freemarkerTemplate, frozenMatrixServerList, frozenMatrixRoomList)
        else -> get404HttpResponse(debugMode, clientCfg, pageMainLang, globalBundle, freemarkerTemplate)
    }.let { response: Response ->
        if (pageMainLang == mainLangReqByPath && pageMainLang != mainLangReqByCookie)
            response.cookie(Cookie(name = MAIN_LANG_COOKIE_NAME, value = pageMainLang.iso639_3, maxAge = 16329600L, path = "/"))
        else response
    }
}