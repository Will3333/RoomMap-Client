function handleMatrixRoomArrowClickEvent(arrowElmId)
{
	const arrowBlock = document.getElementById(arrowElmId);
	const topicLabel = arrowBlock != null ? document.getElementById(arrowBlock.id.replace('-elm-arrow-', '-topic-elm-')) : null;
	
	if (topicLabel != null)
	{
		if (arrowBlock.classList.contains('matrix-room-name-down-arrow'))
		{
			arrowBlock.classList.remove('matrix-room-name-down-arrow');
			arrowBlock.classList.add('matrix-room-name-right-arrow');
			topicLabel.style.display = 'none';
		} 
		else 
		{
			arrowBlock.classList.remove('matrix-room-name-right-arrow');
			arrowBlock.classList.add('matrix-room-name-down-arrow');
			topicLabel.style.display = 'block';
		}
	}
}