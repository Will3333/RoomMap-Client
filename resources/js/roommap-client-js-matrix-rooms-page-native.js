function handleMatrixRoomArrowClickEvent(arrowElmId)
{
	const arrowBlock = document.getElementById(arrowElmId);
	const detailsBlock = arrowBlock != null ? document.getElementById(arrowBlock.id.replace('-elm-arrow-', '-details-')) : null;
	
	if (detailsBlock != null)
	{
		if (arrowBlock.classList.contains('matrix-room-name-down-arrow'))
		{
			arrowBlock.classList.remove('matrix-room-name-down-arrow');
			arrowBlock.classList.add('matrix-room-name-right-arrow');
			detailsBlock.style.display = 'none';
		} 
		else 
		{
			arrowBlock.classList.remove('matrix-room-name-right-arrow');
			arrowBlock.classList.add('matrix-room-name-down-arrow');
			detailsBlock.style.display = 'grid';
		}
	}
}

function showGAFilterBlock() {
	gaFilterBlock.style.display = "block";
}
function hideGAFilterBlock() {
	gaFilterBlock.style.display = "none";
}
function showWRFilterBlock() {
	wrFilterBlock.style.display = "block";
}
function hideWRFilterBlock() {
	wrFilterBlock.style.display = "none";
}

const gaFilterDisplayButton = document.getElementById("matrix-rooms-ga-filter-display-button");
const wrFilterDisplayButton = document.getElementById("matrix-rooms-wr-filter-display-button");
const gaFilterBlock = document.getElementById("matrix-rooms-ga-header-filter-block");
const wrFilterBlock = document.getElementById("matrix-rooms-wr-header-filter-block");

const urlParams = new URLSearchParams(window.location.search);
const gaFilterUrlParam = urlParams.get(MATRIX_ROOMS_PAGE_GA_FILTER_REQ_NAME);
const wrFilterUrlParam = urlParams.get(MATRIX_ROOMS_PAGE_WR_FILTER_REQ_NAME);


gaFilterDisplayButton.addEventListener('click', (event) => {
	if (!gaFilterBlock.style.display || gaFilterBlock.style.display == "none")
		showGAFilterBlock();
	else
		hideGAFilterBlock();
});
wrFilterDisplayButton.addEventListener('click', (event) => {
	if (!wrFilterBlock.style.display || wrFilterBlock.style.display == "none")
		showWRFilterBlock();
	else
		hideWRFilterBlock();
});

if (gaFilterUrlParam != null && gaFilterUrlParam != 'NO_FILTER')
	showGAFilterBlock();
if (wrFilterUrlParam != null && wrFilterUrlParam != 'NO_FILTER')
	showWRFilterBlock();
