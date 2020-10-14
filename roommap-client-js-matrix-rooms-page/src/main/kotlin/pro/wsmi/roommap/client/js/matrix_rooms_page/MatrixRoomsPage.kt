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

import kotlinx.browser.document
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import pro.wsmi.roommap.client.lib.matrix_rooms_page.*


fun main()
{
    val nouFiltersDisplayButton = document.getElementById(NOU_FILTERS_DISPLAY_BUTTON_ID) as HTMLButtonElement
    val maxNOUFilterCheckbox = document.getElementById(MAX_NOU_FILTER_CHECKBOX_ID) as HTMLInputElement
    val maxNOUFilterTextField = document.getElementById(MAX_NOU_FILTER_TEXTFIELD_ID) as HTMLInputElement
    val minNOUFilterCheckbox = document.getElementById(MIN_NOU_FILTER_CHECKBOX_ID) as HTMLInputElement
    val minNOUFilterTextField = document.getElementById(MIN_NOU_FILTER_TEXTFIELD_ID) as HTMLInputElement
    val gaFilterDisplayButton = document.getElementById(GA_FILTER_DISPLAY_BUTTON_ID) as HTMLButtonElement
    val wrFilterDisplayButton = document.getElementById(WR_FILTER_DISPLAY_BUTTON_ID) as HTMLButtonElement
    val serverFilterDisplayButton = document.getElementById(SERVER_FILTER_DISPLAY_BUTTON_ID) as HTMLButtonElement
    val nouFiltersBlock = document.getElementById(NOU_FILTERS_BLOCK_ID) as HTMLElement
    val gaFilterBlock = document.getElementById(GA_FILTER_BLOCK_ID) as HTMLElement
    val wrFilterBlock = document.getElementById(WR_FILTER_BLOCK_ID) as HTMLElement
    val serverFilterBlock = document.getElementById(SERVER_FILTER_BLOCK_ID) as HTMLElement


    maxNOUFilterCheckbox.addEventListener("change", getMaxNOUFilterCheckboxChangeEventHandler(maxNOUFilterTextField = maxNOUFilterTextField, minNOUFilterTextField = minNOUFilterTextField))
    maxNOUFilterTextField.addEventListener("change", getMaxNOUFilterTextFieldChangeEventHandler(minNOUFilterTextField = minNOUFilterTextField))
    minNOUFilterCheckbox.addEventListener("change", getMinNOUFilterCheckboxChangeEventHandler(maxNOUFilterTextField = maxNOUFilterTextField, minNOUFilterTextField = minNOUFilterTextField))
    minNOUFilterTextField.addEventListener("change", getMinNOUFilterTextFieldChangeEventHandler(maxNOUFilterTextField = maxNOUFilterTextField))
    nouFiltersDisplayButton.addEventListener("click", getNouFiltersDisplayButtonClickEventHandler(nouFiltersBlock = nouFiltersBlock))
    gaFilterDisplayButton.addEventListener("click", getGAFilterDisplayButtonClickEventHandler(gaFilterBlock = gaFilterBlock))
    wrFilterDisplayButton.addEventListener("click", getWRFilterDisplayButtonClickEventHandler(wrFilterBlock = wrFilterBlock))
    serverFilterDisplayButton.addEventListener("click", getServerFilterDisplayButtonClickEventHandler(serverFilterBlock = serverFilterBlock))
}