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

function showNOUFiltersBlock() {
	nouFiltersBlock.style.display = "block";
}
function hideNOUFiltersBlock() {
	nouFiltersBlock.style.display = "none";
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

const nouFiltersDisplayButton = document.getElementById("matrix-rooms-nou-filters-display-button");
const maxNOUFilterCheckbox = document.getElementById("matrix-rooms-max-nou-filter-checkbox");
const maxNOUFilterTextField = document.getElementById("matrix-rooms-max-nou-filter-textfield");
const minNOUFilterCheckbox = document.getElementById("matrix-rooms-min-nou-filter-checkbox");
const minNOUFilterTextField = document.getElementById("matrix-rooms-min-nou-filter-textfield");
const gaFilterDisplayButton = document.getElementById("matrix-rooms-ga-filter-display-button");
const wrFilterDisplayButton = document.getElementById("matrix-rooms-wr-filter-display-button");
const nouFiltersBlock = document.getElementById("matrix-rooms-nou-header-filters-block");
const gaFilterBlock = document.getElementById("matrix-rooms-ga-header-filter-block");
const wrFilterBlock = document.getElementById("matrix-rooms-wr-header-filter-block");

const urlParams = new URLSearchParams(window.location.search);
const maxNOUFilterUrlParam = urlParams.get(MATRIX_ROOMS_PAGE_MAX_NOU_FILTER_REQ_NAME);
const minNOUFilterUrlParam = urlParams.get(MATRIX_ROOMS_PAGE_MIN_NOU_FILTER_REQ_NAME);
const gaFilterUrlParam = urlParams.get(MATRIX_ROOMS_PAGE_GA_FILTER_REQ_NAME);
const wrFilterUrlParam = urlParams.get(MATRIX_ROOMS_PAGE_WR_FILTER_REQ_NAME);


maxNOUFilterCheckbox.addEventListener('change', (event) => {
	if (maxNOUFilterCheckbox.checked)
	{
		maxNOUFilterTextField.disabled = false;
		
		if(!minNOUFilterTextField.disabled)
		{
			if (maxNOUFilterTextField.value != null)
				minNOUFilterTextField.max = maxNOUFilterTextField.value;
			if (minNOUFilterTextField.value != null)
				maxNOUFilterTextField.min = minNOUFilterTextField.value;
		}
	}
	else
	{
		maxNOUFilterTextField.disabled = true;
		minNOUFilterTextField.max = null;
	}
});
maxNOUFilterTextField.addEventListener('change', (event) => {
	minNOUFilterTextField.max = maxNOUFilterTextField.value;
});
minNOUFilterCheckbox.addEventListener('change', (event) => {
	if (minNOUFilterCheckbox.checked)
	{
		minNOUFilterTextField.disabled = false;
		
		if(!maxNOUFilterTextField.disabled)
		{
			if (minNOUFilterTextField.value != null)
				maxNOUFilterTextField.min = minNOUFilterTextField.value;
			if (maxNOUFilterTextField.value != null)
				minNOUFilterTextField.max = maxNOUFilterTextField.value;
		}
	}
	else
	{
		minNOUFilterTextField.disabled = true;
		maxNOUFilterTextField.min = null;
	}
});
minNOUFilterTextField.addEventListener('change', (event) => {
	maxNOUFilterTextField.min = minNOUFilterTextField.value;
});
nouFiltersDisplayButton.addEventListener('click', (event) => {
	if (!nouFiltersBlock.style.display || nouFiltersBlock.style.display == "none")
		showNOUFiltersBlock();
	else
		hideNOUFiltersBlock();
});
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

if (maxNOUFilterUrlParam != null || minNOUFilterUrlParam != null)
	showNOUFiltersBlock();
if (gaFilterUrlParam != null && gaFilterUrlParam != 'NO_FILTER')
	showGAFilterBlock();
if (wrFilterUrlParam != null && wrFilterUrlParam != 'NO_FILTER')
	showWRFilterBlock();
