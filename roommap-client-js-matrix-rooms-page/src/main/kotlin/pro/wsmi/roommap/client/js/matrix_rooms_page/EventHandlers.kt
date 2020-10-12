package pro.wsmi.roommap.client.js.matrix_rooms_page

import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import pro.wsmi.roommap.client.lib.matrix_rooms_page.*

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
    if (nouFiltersBlock.classList.contains(NOU_FILTERS_BLOCK_DISPLAYED_CLASS))
        nouFiltersBlock.classList.replace(NOU_FILTERS_BLOCK_DISPLAYED_CLASS, NOU_FILTERS_BLOCK_HIDDEN_CLASS)
    else
        nouFiltersBlock.classList.replace(NOU_FILTERS_BLOCK_HIDDEN_CLASS, NOU_FILTERS_BLOCK_DISPLAYED_CLASS)
}

fun getGAFilterDisplayButtonClickEventHandler(gaFilterBlock: HTMLElement) : (Event) -> Unit = {
    if (gaFilterBlock.classList.contains(GA_FILTER_BLOCK_DISPLAYED_CLASS))
        gaFilterBlock.classList.replace(GA_FILTER_BLOCK_DISPLAYED_CLASS, GA_FILTER_BLOCK_HIDDEN_CLASS)
    else
        gaFilterBlock.classList.replace(GA_FILTER_BLOCK_HIDDEN_CLASS, GA_FILTER_BLOCK_DISPLAYED_CLASS)
}

fun getWRFilterDisplayButtonClickEventHandler(wrFilterBlock: HTMLElement) : (Event) -> Unit = {
    if (wrFilterBlock.classList.contains(WR_FILTER_BLOCK_DISPLAYED_CLASS))
        wrFilterBlock.classList.replace(WR_FILTER_BLOCK_DISPLAYED_CLASS, WR_FILTER_BLOCK_HIDDEN_CLASS)
    else
        wrFilterBlock.classList.replace(WR_FILTER_BLOCK_HIDDEN_CLASS, WR_FILTER_BLOCK_DISPLAYED_CLASS)
}

fun getServerFilterDisplayButtonClickEventHandler(serverFilterBlock: HTMLElement) : (Event) -> Unit = {
    if (serverFilterBlock.classList.contains(SERVER_FILTER_BLOCK_DISPLAYED_CLASS))
        serverFilterBlock.classList.replace(SERVER_FILTER_BLOCK_DISPLAYED_CLASS, SERVER_FILTER_BLOCK_HIDDEN_CLASS)
    else
        serverFilterBlock.classList.replace(SERVER_FILTER_BLOCK_HIDDEN_CLASS, SERVER_FILTER_BLOCK_DISPLAYED_CLASS)
}