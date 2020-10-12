function handleMatrixRoomArrowClickEvent(arrowElmId)
{
	const arrowBlock = document.getElementById(arrowElmId);
	const detailsBlock = arrowBlock != null ? document.getElementById(arrowBlock.id.replace('-elm-arrow-', '-details-')) : null;
	
	if (detailsBlock != null)
	{
		if (arrowBlock.src.endsWith("icon-arrows-expand.svg"))
		{
			arrowBlock.src = "/static/img/icon-arrows-collapse.svg";
			detailsBlock.classList.replace(MATRIX_ROOM_DETAILS_DISPLAYED_CLASS, MATRIX_ROOM_DETAILS_HIDDEN_CLASS);
		} 
		else 
		{
			arrowBlock.src = "/static/img/icon-arrows-expand.svg";
			detailsBlock.classList.replace(MATRIX_ROOM_DETAILS_HIDDEN_CLASS, MATRIX_ROOM_DETAILS_DISPLAYED_CLASS);
		}
	}
}