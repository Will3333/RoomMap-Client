package pro.wsmi.roommap.client.js.matrix_rooms_page

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.url.URLSearchParams
import pro.wsmi.roommap.client.lib.matrix_rooms_page.*



fun main()
{
    val nouFiltersDisplayButton = document.getElementById(NOU_FILTERS_DISPLAY_BUTTON_ID)
    val maxNOUFilterCheckbox = document.getElementById(MAX_NOU_FILTER_CHECKBOX_ID) as HTMLInputElement
    val maxNOUFilterTextField = document.getElementById(MAX_NOU_FILTER_TEXTFIELD_ID) as HTMLInputElement
    val minNOUFilterCheckbox = document.getElementById(MIN_NOU_FILTER_CHECKBOX_ID) as HTMLInputElement
    val minNOUFilterTextField = document.getElementById(MIN_NOU_FILTER_TEXTFIELD_ID) as HTMLInputElement
    val gaFilterDisplayButton = document.getElementById(GA_FILTER_DISPLAY_BUTTON_ID)
    val wrFilterDisplayButton = document.getElementById(WR_FILTER_DISPLAY_BUTTON_ID)
    val serverFilterDisplayButton = document.getElementById(SERVER_FILTER_DISPLAY_BUTTON_ID)
    val nouFiltersBlock = document.getElementById(NOU_FILTERS_BLOCK_ID)
    val gaFilterBlock = document.getElementById(GA_FILTER_BLOCK_ID)
    val wrFilterBlock = document.getElementById(WR_FILTER_BLOCK_ID)
    val serverFilterBlock = document.getElementById(SERVER_FILTER_BLOCK_ID)

    val urlParams = URLSearchParams(window.location.search)
    val maxNOUFilterUrlParam = urlParams.get(MATRIX_ROOMS_PAGE_MAX_NOU_FILTER_REQ_NAME)
    val minNOUFilterUrlParam = urlParams.get(MATRIX_ROOMS_PAGE_MIN_NOU_FILTER_REQ_NAME)
    val gaFilterUrlParam = urlParams.get(MATRIX_ROOMS_PAGE_GA_FILTER_REQ_NAME)
    val wrFilterUrlParam = urlParams.get(MATRIX_ROOMS_PAGE_WR_FILTER_REQ_NAME)
    val serverFilterUrlParam = urlParams.getAll(MATRIX_ROOMS_PAGE_SERVER_FILTER_REQ_NAME)


    maxNOUFilterCheckbox.addEventListener("change", getMaxNOUFilterCheckboxChangeEventHandler(maxNOUFilterTextField = maxNOUFilterTextField, minNOUFilterTextField = minNOUFilterTextField))
    maxNOUFilterTextField.addEventListener("change", getMaxNOUFilterTextFieldChangeEventHandler(minNOUFilterTextField = minNOUFilterTextField))
    minNOUFilterCheckbox.addEventListener("change", getMinNOUFilterCheckboxChangeEventHandler(maxNOUFilterTextField = maxNOUFilterTextField, minNOUFilterTextField = minNOUFilterTextField))
    minNOUFilterTextField.addEventListener("change", getMinNOUFilterTextFieldChangeEventHandler(maxNOUFilterTextField = maxNOUFilterTextField))
}