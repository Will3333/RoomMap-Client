    <div>
	    <h1>${globalData.websiteName}</h1>
		<p>${texts.getString('intro_txt')?no_esc}</p>
	</div>
    <nav class="matrix-rooms-page-management">
      <span class="matrix-rooms-current-page-management">
        <#if queryParameters.page gt 1>
		<a class="link-button link-button-enabled" href="?${queryParameters.copyWithNewPage(1).toUrlFormat()}">&lt;&lt;</a>
		<a class="link-button link-button-enabled" href="?${queryParameters.copyWithNewPage(queryParameters.page-1).toUrlFormat()}">&lt;</a>
		<#else>
		<span class="link-button link-button-disabled">&lt;&lt;</span>
		<span class="link-button link-button-disabled">&lt;</span>
		</#if>
        ${texts.getString('current_page_management_page_txt')?no_esc} <form method="get">
		  <input type="number" class="matrix-rooms-current-page-textfield" name="${queryParameters.pageReqName}" value="${queryParameters.page?string.computer}" min="1" max="${matrixRoomsData.pageMaxNum?string.computer}">
		  ${queryParameters.copyDeleting(true, false, false, false, false, false).toOwnFormat('<input type="hidden" name="£{name}£" value="£{value}£">')?no_esc}
		</form> ${texts.getString('current_page_management_of_txt')?no_esc} ${matrixRoomsData.pageMaxNum}
        <#if queryParameters.page lt matrixRoomsData.pageMaxNum>
		<a class="link-button link-button-enabled" href="?${queryParameters.copyWithNewPage(queryParameters.page+1).toUrlFormat()}">&gt;</a>
		<a class="link-button link-button-enabled" href="?${queryParameters.copyWithNewPage(matrixRoomsData.pageMaxNum).toUrlFormat()}">&gt;&gt;</a>
		<#else>
		<span class="link-button link-button-disabled">&gt;</span>
		<span class="link-button link-button-disabled">&gt;&gt;</span>
		</#if>
      </span>
      <span class="matrix-rooms-per-page-management"><form method="get">
        ${texts.getString('rooms_per_page_management_display_txt')?no_esc} <input type="number" class="matrix-rooms-per-page-textfield" name="${queryParameters.roomsPerPageReqName}" list="matrix-rooms-per-page-datalist" value="${queryParameters.roomsPerPage?string.computer}" min="1"> ${texts.getString('rooms_per_page_management_rooms_per_page_txt')?no_esc}
        ${queryParameters.copyDeleting(true, true, false, false, false, false).toOwnFormat('<input type="hidden" name="£{name}£" value="£{value}£">')?no_esc}
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
    </nav>
    <div id="matrix-rooms-container">
	  <div class="matrix-rooms-name-header">
	    <#if !queryParameters.sorter?? || (queryParameters.sorter?? && queryParameters.sorter != "ROOM_NAME") || (queryParameters.sorterDirection?? && !(queryParameters.sorterDirection))><a class="link-button link-button-enabled" href="?${queryParameters.copyWithNewSorter('ROOM_NAME', true).toUrlFormat()}">${texts.getString('name_elm_header_title')?no_esc}<#if queryParameters.sorter?? && queryParameters.sorter == "ROOM_NAME"> &#x25BC;</#if></a>
		<#else><a class="link-button link-button-enabled" href="?${queryParameters.copyWithNewSorter('ROOM_NAME', false).toUrlFormat()}">${texts.getString('name_elm_header_title')?no_esc} &#x25B2;</a></#if>
	  </div>
	  <div class="matrix-rooms-nou-header">
	    <div class="matrix-rooms-nou-header-title-container">
	      <#if (queryParameters.sorter?? && queryParameters.sorter != "NUM_JOINED_MEMBERS") || (queryParameters.sorterDirection?? && queryParameters.sorterDirection)><a class="link-button link-button-enabled" href="?${queryParameters.copyWithNewSorter('NUM_JOINED_MEMBERS', false).toUrlFormat()}">${texts.getString('users_elm_header_title')?no_esc}<#if !queryParameters.sorter?? || queryParameters.sorter == "NUM_JOINED_MEMBERS"> &#x25B2;</#if></a>
	      <#else><a class="link-button link-button-enabled" href="?${queryParameters.copyWithNewSorter('NUM_JOINED_MEMBERS', true).toUrlFormat()}">${texts.getString('users_elm_header_title')?no_esc} &#x25BC;</a></#if>
		  <button id="${htmlElmIds.NOU_FILTERS_DISPLAY_BUTTON}" class="button">&#x22EE;</button>
	    </div>
		<form method="get" id="${htmlElmIds.NOU_FILTERS_BLOCK}" class="matrix-rooms-nou-header-filters <#if queryParameters.maxNOUFilter?? || queryParameters.minNOUFilter??>${cssClasses.NOU_FILTERS_BLOCK_DISPLAYED}<#else>${cssClasses.NOU_FILTERS_BLOCK_HIDDEN}</#if>">
		  <div>
		    <input type="checkbox" id="${htmlElmIds.MAX_NOU_FILTER_CHECKBOX}"<#if queryParameters.maxNOUFilter??> checked</#if>>
			<label for="${htmlElmIds.MAX_NOU_FILTER_CHECKBOX}">max : </label>
			<input type="number" id="${htmlElmIds.MAX_NOU_FILTER_TEXTFIELD}" class="matrix-rooms-current-page-textfield" name="${queryParameters.maxNOUFilterReqName}"<#if queryParameters.maxNOUFilter??> value=${queryParameters.maxNOUFilter?string.computer}</#if><#if queryParameters.minNOUFilter??>  min=${queryParameters.minNOUFilter?string.computer}</#if><#if !queryParameters.maxNOUFilter??> disabled</#if> required>
		  </div>
		  <div>
		    <input type="checkbox" id="${htmlElmIds.MIN_NOU_FILTER_CHECKBOX}"<#if queryParameters.minNOUFilter??> checked</#if>>
			<label for="${htmlElmIds.MIN_NOU_FILTER_CHECKBOX}">min : </label>
			<input type="number" id="${htmlElmIds.MIN_NOU_FILTER_TEXTFIELD}" class="matrix-rooms-current-page-textfield" name="${queryParameters.minNOUFilterReqName}"<#if queryParameters.minNOUFilter??> value=${queryParameters.minNOUFilter?string.computer}</#if> min=1<#if queryParameters.maxNOUFilter??>  max=${queryParameters.maxNOUFilter?string.computer}</#if><#if !queryParameters.minNOUFilter??> disabled</#if> required>
		  </div>
		  ${queryParameters.copyDeleting(true, false, true, false, false, false).toOwnFormat('<input type="hidden" name="£{name}£" value="£{value}£">')?no_esc}
		  <button type="submit" class="button" id="matrix-rooms-nou-filters-validation-button">${texts.getString('filter_button_name')?no_esc}</button>
		</form>
	  </div>
	  <div class="matrix-rooms-ga-header">
	    <div class="matrix-rooms-ga-header-title-container"><span class="matrix-rooms-ga-header-title">${texts.getString('ga_elm_header_title')?no_esc}</span><span id="${htmlElmIds.GA_TOOLTIP_CONTAINER}" class="tooltip-container"><span class="tooltip-question-mark">?</span><span id="${htmlElmIds.GA_TOOLTIP}" class="tooltip ${globalData.cssClasses.TOOLTIP_BLOCK_HIDDEN}">${texts.getString('ga_elm_header_tooltip')?no_esc}</span></span><button id="${htmlElmIds.GA_FILTER_DISPLAY_BUTTON}" class="button">&#x22EE;</button></div>
		<form method="get" id="${htmlElmIds.GA_FILTER_BLOCK}" class="matrix-rooms-ga-header-filter <#if queryParameters.gaFilter??>${cssClasses.GA_FILTER_BLOCK_DISPLAYED}<#else>${cssClasses.GA_FILTER_BLOCK_HIDDEN}</#if>">
		  <div>
		    <input type="radio" id="matrix-rooms-ga-filter-all-radio" name="${queryParameters.gaFilterReqName}" value="NO_FILTER"<#if !queryParameters.gaFilter?? || (queryParameters.gaFilter?? && queryParameters.gaFilter == "NO_FILTER")> checked</#if>>
			<label for="matrix-rooms-ga-filter-all-radio">${texts.getString('filter_selection_no_filter_txt')?no_esc}</label>
		  </div>
		  <div>
		    <input type="radio" id="matrix-rooms-ga-filter-yes-radio" name="${queryParameters.gaFilterReqName}" value="CAN_JOIN"<#if queryParameters.gaFilter?? && queryParameters.gaFilter == "CAN_JOIN"> checked</#if>>
			<label for="matrix-rooms-ga-filter-yes-radio">${texts.getString('filter_selection_allowed_txt')?no_esc}</label>
		  </div>
		  <div>
		    <input type="radio" id="matrix-rooms-ga-filter-no-radio" name="${queryParameters.gaFilterReqName}" value="CAN_NOT_JOIN"<#if queryParameters.gaFilter?? && queryParameters.gaFilter == "CAN_NOT_JOIN"> checked</#if>>
			<label for="matrix-rooms-ga-filter-no-radio">${texts.getString('filter_selection_disallowed_txt')?no_esc}</label>
		  </div>
		  ${queryParameters.copyDeleting(true, false, false, true, false, false).toOwnFormat('<input type="hidden" name="£{name}£" value="£{value}£">')?no_esc}
		  <button type="submit" class="button">${texts.getString('filter_button_name')?no_esc}</button>
		</form>
	  </div>
	  <div class="matrix-rooms-wr-header">
	    <div class="matrix-rooms-wr-header-title-container"><span class="matrix-rooms-wr-header-title">${texts.getString('wr_elm_header_title')?no_esc}</span><span id="${htmlElmIds.WR_TOOLTIP_CONTAINER}"><span class="tooltip-question-mark">?</span><span id="${htmlElmIds.WR_TOOLTIP}" class="tooltip ${globalData.cssClasses.TOOLTIP_BLOCK_HIDDEN}">${texts.getString('wr_elm_header_tooltip')?no_esc}</span></span><button id="${htmlElmIds.WR_FILTER_DISPLAY_BUTTON}" class="button">&#x22EE;</button></div>
	    <form method="get" id="${htmlElmIds.WR_FILTER_BLOCK}" class="matrix-rooms-wr-header-filter <#if queryParameters.wrFilter??>${cssClasses.WR_FILTER_BLOCK_DISPLAYED}<#else>${cssClasses.WR_FILTER_BLOCK_HIDDEN}</#if>">
		  <div>
		    <input type="radio" id="matrix-rooms-wr-filter-all-radio" name="${queryParameters.wrFilterReqName}" value="NO_FILTER"<#if !queryParameters.wrFilter?? || (queryParameters.wrFilter?? && queryParameters.wrFilter == "NO_FILTER")> checked</#if>>
			<label for="matrix-rooms-wr-filter-all-radio">${texts.getString('filter_selection_no_filter_txt')?no_esc}</label>
		  </div>
		  <div>
		    <input type="radio" id="matrix-rooms-wr-filter-yes-radio" name="${queryParameters.wrFilterReqName}" value="IS_WORLD_READABLE"<#if queryParameters.wrFilter?? && queryParameters.wrFilter == "IS_WORLD_READABLE"> checked</#if>>
			<label for="matrix-rooms-wr-filter-yes-radio">${texts.getString('filter_selection_yes_txt')?no_esc}</label>
		  </div>
		  <div>
		    <input type="radio" id="matrix-rooms-wr-filter-no-radio" name="${queryParameters.wrFilterReqName}" value="IS_NOT_WORLD_READABLE"<#if queryParameters.wrFilter?? && queryParameters.wrFilter == "IS_NOT_WORLD_READABLE"> checked</#if>>
			<label for="matrix-rooms-wr-filter-no-radio">${texts.getString('filter_selection_no_txt')?no_esc}</label>
		  </div>
		  ${queryParameters.copyDeleting(true, false, false, false, true, false).toOwnFormat('<input type="hidden" name="£{name}£" value="£{value}£">')?no_esc}
		  <button type="submit" class="button">${texts.getString('filter_button_name')?no_esc}</button>
		</form>
	  </div>
	  <div class="matrix-rooms-server-header">
	    <div class="matrix-rooms-server-header-title-container">
	      <#if !queryParameters.sorter?? || (queryParameters.sorter?? && queryParameters.sorter != "SERVER_NAME") || (queryParameters.sorterDirection?? && !(queryParameters.sorterDirection))><a class="link-button link-button-enabled" href="?${queryParameters.copyWithNewSorter('SERVER_NAME', true).toUrlFormat()}">${texts.getString('server_elm_header_title')?no_esc}<#if queryParameters.sorter?? && queryParameters.sorter == "SERVER_NAME"> &#x25BC;</#if></a>
		  <#else><a class="link-button link-button-enabled" href="?${queryParameters.copyWithNewSorter('SERVER_NAME', false).toUrlFormat()}">${texts.getString('server_elm_header_title')?no_esc} &#x25B2;</a></#if>
		  <button id="${htmlElmIds.SERVER_FILTER_DISPLAY_BUTTON}" class="button">&#x22EE;</button>
	    </div>
		<form method="get" id="${htmlElmIds.SERVER_FILTER_BLOCK}" class="matrix-rooms-server-header-filter <#if queryParameters.serverFilter??>${cssClasses.SERVER_FILTER_BLOCK_DISPLAYED}<#else>${cssClasses.SERVER_FILTER_BLOCK_HIDDEN}</#if>">
		  <select name="${queryParameters.serverFilterReqName}" multiple>
		    <#list matrixRoomsData.serverList as serverId, server>
			<option value="${serverId}"<#if queryParameters.serverFilter?? && queryParameters.serverFilter?seq_contains(serverId)> selected</#if>>${server.name}</option>
			</#list>
		  </select>
		  ${queryParameters.copyDeleting(true, false, false, false, false, true).toOwnFormat('<input type="hidden" name="£{name}£" value="£{value}£">')?no_esc}
		  <button type="submit" class="button" id="matrix-rooms-server-filter-validation-button">${texts.getString('filter_button_name')?no_esc}</button>
		</form>
	  </div>
	  <#list matrixRoomsData.roomList as room>
	  <#if room?index % 2 == 0><#assign lineVariationClass = "matrix-room-elm-line-var1"><#else><#assign lineVariationClass = "matrix-room-elm-line-var2"></#if>
	  <div class="matrix-room-name-elm-container ${lineVariationClass}">
		<img id="matrix-room-elm-arrow-${room?index}" class="matrix-room-name-arrow" src="/static/img/icon-collapsed.svg" width="17.5em" height="17.5em" onclick="handleMatrixRoomArrowClickEvent('matrix-room-elm-arrow-${room?index}')">
		<span class="matrix-room-name-link"><#if room.canonicalAlias??><a href="https://matrix.to/#/${room.canonicalAlias}">${room.canonicalAlias}</a><#else><a href="https://matrix.to/#/${room.roomId}">${room.roomId}</a></#if></span>
		<span class="matrix-room-name-elm"><#if room.name?has_content>${room.name}<#else>${room.roomId}</#if></span>
	  </div>
	  <div class="matrix-room-nou-elm ${lineVariationClass}">${room.numJoinedMembers}</div>
	  <div class="matrix-room-ga-elm ${lineVariationClass}"><#if room.guestCanJoin>${texts.getString('ga_elm_allowed_value_txt')?no_esc}<#else>${texts.getString('ga_elm_disallowed_value_txt')?no_esc}</#if></div>
	  <div class="matrix-room-wr-elm ${lineVariationClass}"><#if room.worldReadable>${texts.getString('wr_elm_yes_value_txt')?no_esc}<#else>${texts.getString('wr_elm_no_value_txt')?no_esc}</#if></div>
	  <div class="matrix-room-server-elm ${lineVariationClass}"><a href="${matrixRoomsData.serverList[room.serverId].apiURL.toString()}">${matrixRoomsData.serverList[room.serverId].name}</a></div>
	  <div id="matrix-room-details-${room?index}" class="matrix-room-details ${cssClasses.MATRIX_ROOM_DETAILS_HIDDEN}">
	    <#if room.canonicalAlias??><span class="matrix-room-details-detail-title">${texts.getString('details_elm_id_title')?no_esc} :</span><span class="matrix-room-details-detail">${room.roomId}</span></#if>
		<span class="matrix-room-details-server-title">${texts.getString('details_elm_server_title')?no_esc} :</span><span class="matrix-room-details-server"><a href="${matrixRoomsData.serverList[room.serverId].apiURL.toString()}">${matrixRoomsData.serverList[room.serverId].name}</a></span>
		<#if room.languages??><span class="matrix-room-details-detail-title">${texts.getString('details_elm_languages_title')?no_esc} :</span><span class="matrix-room-details-detail"><#list room.languages as lang><span>${lang.nativeName}</span></#list></span></#if>
		<#if room.topic??><span class="matrix-room-details-detail-title">${texts.getString('details_elm_topic_title')?no_esc} :</span><span class="matrix-room-details-detail" lang="">${room.topic}</span></#if>
	  </div>
	  </#list>
	</div>
	<script>
	  const GA_TOOLTIP_CONTAINER_ID = '${htmlElmIds.GA_TOOLTIP_CONTAINER}';
	  const GA_TOOLTIP_ID = '${htmlElmIds.GA_TOOLTIP}';
	  const WR_TOOLTIP_CONTAINER_ID = '${htmlElmIds.WR_TOOLTIP_CONTAINER}';
	  const WR_TOOLTIP_ID = '${htmlElmIds.WR_TOOLTIP}';
	  const MATRIX_ROOM_DETAILS_DISPLAYED_CLASS = '${cssClasses.MATRIX_ROOM_DETAILS_DISPLAYED}';
	  const MATRIX_ROOM_DETAILS_HIDDEN_CLASS = '${cssClasses.MATRIX_ROOM_DETAILS_HIDDEN}';
	</script>

	