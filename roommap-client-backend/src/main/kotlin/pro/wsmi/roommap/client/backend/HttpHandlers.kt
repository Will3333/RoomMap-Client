package pro.wsmi.roommap.client.backend

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
import java.io.StringWriter
import java.util.*


@ExperimentalSerializationApi
val availableLang = setOf(
    Language.ENG,
    Language.FRA
)
@ExperimentalSerializationApi
val defaultLang = Language.ENG

private const val PAGE_404_TEMPLATE_FILE_NAME = "404_page.ftlh"

@ExperimentalSerializationApi
fun get404HttpResponse(debugMode: Boolean, clientCfg: ClientConfiguration, pageMainLang: Language, freemarkerTemplate: Template) : Response
{
    val freemarkerModel = mapOf(
        "debug_mode" to debugMode,
        "texts" to ResourceBundle.getBundle("pro.wsmi.roommap.client.backend.Page404Texts", Locale(pageMainLang.bcp47)),
        "website_info" to mapOf(
            "name" to clientCfg.websiteName
        ),
        "page_info" to mapOf(
            "path_name" to "",
            "main_lang" to pageMainLang,
            "css_files" to listOf<String>(),
            "template_file" to PAGE_404_TEMPLATE_FILE_NAME
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
fun handleMainHttpRequest(debugMode: Boolean, clientCfg: ClientConfiguration, freemarkerTemplate: Template, businessData: BusinessData) : HttpHandler = { req ->

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


    val frozenMatrixServerList = businessData.matrixServers
    val frozenMatrixRoomList = businessData.matrixRooms

    when {
        requestedPagePath == "/" -> handleMatrixRoomsPageReq(req, debugMode, clientCfg, pageMainLang, freemarkerTemplate, frozenMatrixServerList, frozenMatrixRoomList)
        else -> get404HttpResponse(debugMode, clientCfg, pageMainLang, freemarkerTemplate)
    }.let { response: Response ->
        if (pageMainLang == mainLangReqByPath && pageMainLang != mainLangReqByCookie)
            response.cookie(Cookie(name = MAIN_LANG_COOKIE_NAME, value = pageMainLang.iso639_3, maxAge = 16329600L, path = "/"))
        else response
    }
}