function handleMatrixRoomArrowClickEvent(arrowElmId)
{
	const arrowBlock = document.getElementById(arrowElmId);
	const detailsBlock = arrowBlock != null ? document.getElementById(arrowBlock.id.replace('-elm-arrow-', '-details-')) : null;
	
	if (detailsBlock != null)
	{
		if (arrowBlock.src.endsWith("icon-expanded.svg"))
		{
			arrowBlock.src = "/static/img/icon-collapsed.svg";
			detailsBlock.classList.replace(MATRIX_ROOM_DETAILS_DISPLAYED_CLASS, MATRIX_ROOM_DETAILS_HIDDEN_CLASS);
		} 
		else 
		{
			arrowBlock.src = "/static/img/icon-expanded.svg";
			detailsBlock.classList.replace(MATRIX_ROOM_DETAILS_HIDDEN_CLASS, MATRIX_ROOM_DETAILS_DISPLAYED_CLASS);
		}
	}
}

const gaTooltipContainer = document.getElementById(GA_TOOLTIP_CONTAINER_ID);
const gaTooltipBlock = document.getElementById(GA_TOOLTIP_ID);
const wrTooltipContainer = document.getElementById(WR_TOOLTIP_CONTAINER_ID);
const wrTooltipBlock = document.getElementById(WR_TOOLTIP_ID);

gaTooltipContainer.addEventListener('mouseenter', () => {
    if (gaTooltipBlock != null && gaTooltipBlock.classList.contains(TOOLTIP_BLOCK_HIDDEN_CLASS))
        gaTooltipBlock.classList.replace(TOOLTIP_BLOCK_HIDDEN_CLASS, TOOLTIP_BLOCK_DISPLAYED_CLASS);
});
gaTooltipContainer.addEventListener('mouseleave', () => {
    if (gaTooltipBlock != null && gaTooltipBlock.classList.contains(TOOLTIP_BLOCK_DISPLAYED_CLASS))
        gaTooltipBlock.classList.replace(TOOLTIP_BLOCK_DISPLAYED_CLASS, TOOLTIP_BLOCK_HIDDEN_CLASS);
});

wrTooltipContainer.addEventListener('mouseenter', () => {
    if (wrTooltipBlock != null && wrTooltipBlock.classList.contains(TOOLTIP_BLOCK_HIDDEN_CLASS))
        wrTooltipBlock.classList.replace(TOOLTIP_BLOCK_HIDDEN_CLASS, TOOLTIP_BLOCK_DISPLAYED_CLASS);
});
wrTooltipContainer.addEventListener('mouseleave', () => {
    if (wrTooltipBlock != null && wrTooltipBlock.classList.contains(TOOLTIP_BLOCK_DISPLAYED_CLASS))
        wrTooltipBlock.classList.replace(TOOLTIP_BLOCK_DISPLAYED_CLASS, TOOLTIP_BLOCK_HIDDEN_CLASS);
});