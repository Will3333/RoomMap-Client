<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8">
    <title>${website_info.name} - Matrix room list</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="/static/css/matrix_rooms_page.css">
  </head>
  <body>
    <h1>Rooms</h1>
    <menu class="matrix-rooms-page-management" type="toolbar">
      <span class="matrix-rooms-current-page-management">
        <#if query_parameters.page gt 1>
		<a class="link-button link-button-enabled" href="?${query_parameters.copyWithNewPage(1).toUrlFormat()}">&lt;&lt;</a>
		<a class="link-button link-button-enabled" href="?${query_parameters.copyWithNewPage(query_parameters.page-1).toUrlFormat()}">&lt;</a>
		<#else>
		<span class="link-button link-button-disabled">&lt;&lt;</span>
		<span class="link-button link-button-disabled">&lt;</span>
		</#if>
        <form method="get">
		  Page <input type="number" name="${query_parameters.pageReqName}" value="${query_parameters.page?string.computer}" min="1" max="${page_info.max_page?string.computer}">
		  <#if query_parameters.sorter??><input type="hidden" name="${query_parameters.sorterReqName}" value="${query_parameters.sorter}"></#if>
		  <#if query_parameters.sorterDirection??><input type="hidden" name="${query_parameters.sorterDirectionReqName}" value="${query_parameters.sorterDirection?c}"></#if>
		  <#if query_parameters.gaFilter??><input type="hidden" name="${query_parameters.gaFilterReqName}" value="${query_parameters.gaFilter}"></#if>
		  <#if query_parameters.wrFilter??><input type="hidden" name="${query_parameters.wrFilterReqName}" value="${query_parameters.wrFilter}"></#if>
		  <#if query_parameters.serverFilter??><input type="hidden" name="${query_parameters.serverFilterReqName}" value="${query_parameters.serverFilter}"></#if>
		  <#if query_parameters.maxNOUFilter??><input type="hidden" name="${query_parameters.maxNOUFilterReqName}" value="${query_parameters.maxNOUFilter?string.computer}"></#if>
		  <#if query_parameters.minNOUFilter??><input type="hidden" name="${query_parameters.minNOUFilterReqName}" value="${query_parameters.minNOUFilter?string.computer}"></#if>
		  <input type="hidden" name="${query_parameters.roomsPerPageReqName}" value="${query_parameters.roomsPerPage?string.computer}">
		</form> of ${page_info.max_page}
        <#if query_parameters.page lt page_info.max_page>
		<a class="link-button link-button-enabled" href="?${query_parameters.copyWithNewPage(query_parameters.page+1).toUrlFormat()}">&gt;</a>
		<a class="link-button link-button-enabled" href="?${query_parameters.copyWithNewPage(page_info.max_page).toUrlFormat()}">&gt;&gt;</a>
		<#else>
		<span class="link-button link-button-disabled">&gt;</span>
		<span class="link-button link-button-disabled">&gt;&gt;</span>
		</#if>
      </span>
      <span class="matrix-rooms-per-page-management"><form method="get">
        Display <input type="number" name="${query_parameters.roomsPerPageReqName}" list="matrix-rooms-per-page-datalist" value="${query_parameters.roomsPerPage?string.computer}" min="1"> rooms per page
        <#if query_parameters.sorter??><input type="hidden" name="${query_parameters.sorterReqName}" value="${query_parameters.sorter}"></#if>
		<#if query_parameters.sorterDirection??><input type="hidden" name="${query_parameters.sorterDirectionReqName}" value="${query_parameters.sorterDirection?c}"></#if>
	    <#if query_parameters.gaFilter??><input type="hidden" name="${query_parameters.gaFilterReqName}" value="${query_parameters.gaFilter}"></#if>
		<#if query_parameters.wrFilter??><input type="hidden" name="${query_parameters.wrFilterReqName}" value="${query_parameters.wrFilter}"></#if>
	    <#if query_parameters.serverFilter??><input type="hidden" name="${query_parameters.serverFilterReqName}" value="${query_parameters.serverFilter}"></#if>
		<#if query_parameters.maxNOUFilter??><input type="hidden" name="${query_parameters.maxNOUFilterReqName}" value="${query_parameters.maxNOUFilter?string.computer}"></#if>
		<#if query_parameters.minNOUFilter??><input type="hidden" name="${query_parameters.minNOUFilterReqName}" value="${query_parameters.minNOUFilter?string.computer}"></#if>
        <datalist id="matrix-rooms-per-page-datalist">
           <option value=5>
           <option value=20>
           <option value=50>
           <option value=100>
           <option value=200>
           <option value=500>
           <option value=1000>
           <option value=2000>
           <option value=5000>
           <option value=10000>
        </datalist>
      </form></span>
    </menu>
    <div id="matrix-rooms-container">
	  <div class="matrix-rooms-name-header">
	    <#if !query_parameters.sorter?? || (query_parameters.sorter?? && query_parameters.sorter != "ROOM_NAME") || (query_parameters.sorterDirection?? && !(query_parameters.sorterDirection))><a class="link-button link-button-enabled" href="?${query_parameters.copyWithNewSorter('ROOM_NAME', true).toUrlFormat()}">Name<#if query_parameters.sorter?? && query_parameters.sorter == "ROOM_NAME"> &#x25BC;</#if></a>
		<#else><a class="link-button link-button-enabled" href="?${query_parameters.copyWithNewSorter('ROOM_NAME', false).toUrlFormat()}">Name &#x25B2;</a></#if>
	  </div>
	  <div class="matrix-rooms-nou-header">
	    <#if (query_parameters.sorter?? && query_parameters.sorter != "NUM_JOINED_MEMBERS") || (query_parameters.sorterDirection?? && query_parameters.sorterDirection)><a class="link-button link-button-enabled" href="?${query_parameters.copyWithNewSorter('NUM_JOINED_MEMBERS', false).toUrlFormat()}">Users<#if query_parameters.sorter == "NUM_JOINED_MEMBERS"> &#x25B2;</#if></a>
	    <#else><a class="link-button link-button-enabled" href="?${query_parameters.copyWithNewSorter('NUM_JOINED_MEMBERS', true).toUrlFormat()}">Users &#x25BC;</a></#if>
	  </div>
	  <div class="matrix-rooms-ga-header">
	    <div class="matrix-rooms-ga-header-title-container"><span class="matrix-rooms-ga-header-title">Guest access</span><button id="matrix-rooms-ga-filter-display-button" class="button">&#x22EE;</button></div>
		<form method="get" id="matrix-rooms-ga-header-filter-block" class="matrix-rooms-ga-header-filter">
		  <div>
		    <input type="radio" id="matrix-rooms-ga-filter-all-radio" name="${query_parameters.gaFilterReqName}" value="NO_FILTER"<#if !query_parameters.gaFilter?? || (query_parameters.gaFilter?? && query_parameters.gaFilter == "NO_FILTER")> checked</#if>>
			<label for="matrix-rooms-ga-filter-all-radio">All</label>
		  </div>
		  <div>
		    <input type="radio" id="matrix-rooms-ga-filter-yes-radio" name="${query_parameters.gaFilterReqName}" value="CAN_JOIN"<#if query_parameters.gaFilter?? && query_parameters.gaFilter == "CAN_JOIN"> checked</#if>>
			<label for="matrix-rooms-ga-filter-yes-radio">Yes</label>
		  </div>
		  <div>
		    <input type="radio" id="matrix-rooms-ga-filter-no-radio" name="${query_parameters.gaFilterReqName}" value="CAN_NOT_JOIN"<#if query_parameters.gaFilter?? && query_parameters.gaFilter == "CAN_NOT_JOIN"> checked</#if>>
			<label for="matrix-rooms-ga-filter-no-radio">No</label>
		  </div>
		  <#if query_parameters.sorter??><input type="hidden" name="${query_parameters.sorterReqName}" value="${query_parameters.sorter}"></#if>
		  <#if query_parameters.sorterDirection??><input type="hidden" name="${query_parameters.sorterDirectionReqName}" value="${query_parameters.sorterDirection?c}"></#if>
		  <#if query_parameters.wrFilter??><input type="hidden" name="${query_parameters.wrFilterReqName}" value="${query_parameters.wrFilter}"></#if>
		  <#if query_parameters.serverFilter??><input type="hidden" name="${query_parameters.serverFilterReqName}" value="${query_parameters.serverFilter}"></#if>
		  <#if query_parameters.maxNOUFilter??><input type="hidden" name="${query_parameters.maxNOUFilterReqName}" value="${query_parameters.maxNOUFilter?string.computer}"></#if>
		  <#if query_parameters.minNOUFilter??><input type="hidden" name="${query_parameters.minNOUFilterReqName}" value="${query_parameters.minNOUFilter?string.computer}"></#if>
		  <input type="hidden" name="${query_parameters.roomsPerPageReqName}" value="${query_parameters.roomsPerPage?string.computer}">
		  <button type="submit" class="button">Filter</button>
		</form>
	  </div>
	  <div class="matrix-rooms-wr-header">World readable</div>
	  <div class="matrix-rooms-server-header">
	    <#if !query_parameters.sorter?? || (query_parameters.sorter?? && query_parameters.sorter != "SERVER_NAME") || (query_parameters.sorterDirection?? && !(query_parameters.sorterDirection))><a class="link-button link-button-enabled" href="?${query_parameters.copyWithNewSorter('SERVER_NAME', true).toUrlFormat()}">Server<#if query_parameters.sorter?? && query_parameters.sorter == "SERVER_NAME"> &#x25BC;</#if></a>
		<#else><a class="link-button link-button-enabled" href="?${query_parameters.copyWithNewSorter('SERVER_NAME', false).toUrlFormat()}">Server &#x25B2;</a></#if>
	  </div>
	  <#list roomList as room>
	  <#if room?index % 2 == 0><#assign lineVariationClass = "matrix-room-elm-line-var1"><#else><#assign lineVariationClass = "matrix-room-elm-line-var2"></#if>
	  <div class="matrix-room-name-elm-container ${lineVariationClass}">
		<span id="matrix-room-elm-arrow-${room?index}" class="matrix-room-name-arrow matrix-room-name-right-arrow" onclick="handleMatrixRoomArrowClickEvent('matrix-room-elm-arrow-${room?index}')">&#x251B;</span>
		<span class="matrix-room-name-elm"><#if room.name?has_content>${room.name}<#else>${room.roomId}</#if></span>
	  </div>
	  <div class="matrix-room-nou-elm ${lineVariationClass}">${room.numJoinedMembers}</div>
	  <div class="matrix-room-ga-elm ${lineVariationClass}"><#if room.guestCanJoin>yes<#else>no</#if></div>
	  <div class="matrix-room-wr-elm ${lineVariationClass}"><#if room.worldReadable>yes<#else>no</#if></div>
	  <div class="matrix-room-server-elm ${lineVariationClass}"><a href="${serverList[room.serverId].apiUrl}">${serverList[room.serverId].name}</a></div>
	  <div id="matrix-room-details-${room?index}" class="matrix-room-details">
	    <#if room.canonicalAlias??><span class="matrix-room-details-alias-title">Alias :</span><span class="matrix-room-details-alias">${room.canonicalAlias}</span></#if>
		<span class="matrix-room-details-server-title">Server :</span><span class="matrix-room-details-server"><a href="${serverList[room.serverId].apiUrl}">${serverList[room.serverId].name}</a></span>
		<#if room.topic??><span class="matrix-room-details-topic-title">Topic :</span><span class="matrix-room-details-topic" lang="">${room.topic}</span></#if>
	  </div>
	  </#list>
	</div>
	<script>
	  const MATRIX_ROOMS_PAGE_GA_FILTER_REQ_NAME = '${query_parameters.gaFilterReqName}';
	  const MATRIX_ROOMS_PAGE_WR_FILTER_REQ_NAME = '${query_parameters.wrFilterReqName}';
	</script>
    <script src="/static/js/roommap-client-js-matrix-rooms-page-native.js"></script>
  </body>
</html>
