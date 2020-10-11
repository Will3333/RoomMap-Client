package pro.wsmi.roommap.client.js.matrix_rooms_page

import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event

fun getMaxNOUFilterCheckboxChangeEventHandler(maxNOUFilterTextField: HTMLInputElement, minNOUFilterTextField: HTMLInputElement) : (Event) -> Unit = { event ->
    if ((event.currentTarget as HTMLInputElement).checked)
    {
        maxNOUFilterTextField.disabled = false

        if (maxNOUFilterTextField.value.isNotBlank())
            minNOUFilterTextField.max = maxNOUFilterTextField.value

        if (!minNOUFilterTextField.disabled && minNOUFilterTextField.value.isNotBlank())
            maxNOUFilterTextField.min = minNOUFilterTextField.value
    }
    else
    {
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
    else
    {
        minNOUFilterTextField.disabled = true
        maxNOUFilterTextField.min = ""
    }
}

fun getMinNOUFilterTextFieldChangeEventHandler(maxNOUFilterTextField: HTMLInputElement) : (Event) -> Unit = { event ->
    maxNOUFilterTextField.min = (event.currentTarget as HTMLInputElement).value
}