/*
 * Copyright 2020 William Smith
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 */

package pro.wsmi.roommap.client.js.matrix_rooms_page

import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import pro.wsmi.roommap.client.lib.matrix_rooms_page.MatrixRoomsPageCSSClasses

fun getMaxNOUFilterCheckboxChangeEventHandler(maxNOUFilterTextField: HTMLInputElement, minNOUFilterTextField: HTMLInputElement) : (Event) -> Unit = { event ->
    if ((event.currentTarget as HTMLInputElement).checked)
    {
        maxNOUFilterTextField.disabled = false

        if (maxNOUFilterTextField.value.isNotBlank())
            minNOUFilterTextField.max = maxNOUFilterTextField.value

        if (!minNOUFilterTextField.disabled && minNOUFilterTextField.value.isNotBlank())
            maxNOUFilterTextField.min = minNOUFilterTextField.value
    }
    else {
        maxNOUFilterTextField.disabled = true
        minNOUFilterTextField.max = ""
    }
}

fun getMaxNOUFilterTextFieldChangeEventHandler(minNOUFilterTextField: HTMLInputElement) : (Event) -> Unit = { event ->
    minNOUFilterTextField.max = (event.currentTarget as HTMLInputElement).value
}

fun getMinNOUFilterCheckboxChangeEventHandler(maxNOUFilterTextField: HTMLInputElement, minNOUFilterTextField: HTMLInputElement) : (Event) -> Unit = { event ->
    if ((event.currentTarget as HTMLInputElement).checked)
    {
        minNOUFilterTextField.disabled = false

        if (minNOUFilterTextField.value.isNotBlank())
            maxNOUFilterTextField.min = minNOUFilterTextField.value

        if (!maxNOUFilterTextField.disabled && maxNOUFilterTextField.value.isNotBlank())
            minNOUFilterTextField.max = maxNOUFilterTextField.value
    }
    else {
        minNOUFilterTextField.disabled = true
        maxNOUFilterTextField.min = ""
    }
}

fun getMinNOUFilterTextFieldChangeEventHandler(maxNOUFilterTextField: HTMLInputElement) : (Event) -> Unit = { event ->
    maxNOUFilterTextField.min = (event.currentTarget as HTMLInputElement).value
}

fun getNouFiltersDisplayButtonClickEventHandler(nouFiltersBlock: HTMLElement) : (Event) -> Unit = {
    if (nouFiltersBlock.classList.contains(MatrixRoomsPageCSSClasses.NOU_FILTERS_BLOCK_DISPLAYED))
        nouFiltersBlock.classList.replace(MatrixRoomsPageCSSClasses.NOU_FILTERS_BLOCK_DISPLAYED, MatrixRoomsPageCSSClasses.NOU_FILTERS_BLOCK_HIDDEN)
    else
        nouFiltersBlock.classList.replace(MatrixRoomsPageCSSClasses.NOU_FILTERS_BLOCK_HIDDEN, MatrixRoomsPageCSSClasses.NOU_FILTERS_BLOCK_DISPLAYED)
}

fun getGAFilterDisplayButtonClickEventHandler(gaFilterBlock: HTMLElement) : (Event) -> Unit = {
    if (gaFilterBlock.classList.contains(MatrixRoomsPageCSSClasses.GA_FILTER_BLOCK_DISPLAYED))
        gaFilterBlock.classList.replace(MatrixRoomsPageCSSClasses.GA_FILTER_BLOCK_DISPLAYED, MatrixRoomsPageCSSClasses.GA_FILTER_BLOCK_HIDDEN)
    else
        gaFilterBlock.classList.replace(MatrixRoomsPageCSSClasses.GA_FILTER_BLOCK_HIDDEN, MatrixRoomsPageCSSClasses.GA_FILTER_BLOCK_DISPLAYED)
}

fun getWRFilterDisplayButtonClickEventHandler(wrFilterBlock: HTMLElement) : (Event) -> Unit = {
    if (wrFilterBlock.classList.contains(MatrixRoomsPageCSSClasses.WR_FILTER_BLOCK_DISPLAYED))
        wrFilterBlock.classList.replace(MatrixRoomsPageCSSClasses.WR_FILTER_BLOCK_DISPLAYED, MatrixRoomsPageCSSClasses.WR_FILTER_BLOCK_HIDDEN)
    else
        wrFilterBlock.classList.replace(MatrixRoomsPageCSSClasses.WR_FILTER_BLOCK_HIDDEN, MatrixRoomsPageCSSClasses.WR_FILTER_BLOCK_DISPLAYED)
}

fun getServerFilterDisplayButtonClickEventHandler(serverFilterBlock: HTMLElement) : (Event) -> Unit = {
    if (serverFilterBlock.classList.contains(MatrixRoomsPageCSSClasses.SERVER_FILTER_BLOCK_DISPLAYED))
        serverFilterBlock.classList.replace(MatrixRoomsPageCSSClasses.SERVER_FILTER_BLOCK_DISPLAYED, MatrixRoomsPageCSSClasses.SERVER_FILTER_BLOCK_HIDDEN)
    else
        serverFilterBlock.classList.replace(MatrixRoomsPageCSSClasses.SERVER_FILTER_BLOCK_HIDDEN, MatrixRoomsPageCSSClasses.SERVER_FILTER_BLOCK_DISPLAYED)
}