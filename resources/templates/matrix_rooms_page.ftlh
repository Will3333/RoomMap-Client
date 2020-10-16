    <div>
	    <h1>${website_info.name}</h1>
		<p>${page_info.texts.getString('intro_txt')?no_esc}</p>
	</div>
    <nav class="matrix-rooms-page-management">
      <span class="matrix-rooms-current-page-management">
        <#if query_parameters.page gt 1>
		<a class="link-button link-button-enabled" href="?${query_parameters.copyWithNewPage(1).toUrlFormat()}">&lt;&lt;</a>
		<a class="link-button link-button-enabled" href="?${query_parameters.copyWithNewPage(query_parameters.page-1).toUrlFormat()}">&lt;</a>
		<#else>
		<span class="link-button link-button-disabled">&lt;&lt;</span>
		<span class="link-button link-button-disabled">&lt;</span>
		</#if>
        ${page_info.texts.getString('current_page_management_page_txt')?no_esc} <form method="get">
		  <input type="number" class="matrix-rooms-current-page-textfield" name="${query_parameters.pageReqName}" value="${query_parameters.page?string.computer}" min="1" max="${page_info.max_page?string.computer}">
		  ${query_parameters.copyDeleting(true, false, false, false, false, false).toOwnFormat('<input type="hidden" name="£{name}£" value="£{value}£">')?no_esc}
		</form> ${page_info.texts.getString('current_page_management_of_txt')?no_esc} ${page_info.max_page}
        <#if query_parameters.page lt page_info.max_page>
		<a class="link-button link-button-enabled" href="?${query_parameters.copyWithNewPage(query_parameters.page+1).toUrlFormat()}">&gt;</a>
		<a class="link-button link-button-enabled" href="?${query_parameters.copyWithNewPage(page_info.max_page).toUrlFormat()}">&gt;&gt;</a>
		<#else>
		<span class="link-button link-button-disabled">&gt;</span>
		<span class="link-button link-button-disabled">&gt;&gt;</span>
		</#if>
      </span>
      <span class="matrix-rooms-per-page-management"><form method="get">
        ${page_info.texts.getString('rooms_per_page_management_display_txt')?no_esc} <input type="number" class="matrix-rooms-per-page-textfield" name="${query_parameters.roomsPerPageReqName}" list="matrix-rooms-per-page-datalist" value="${query_parameters.roomsPerPage?string.computer}" min="1"> ${page_info.texts.getString('rooms_per_page_management_rooms_per_page_txt')?no_esc}
        ${query_parameters.copyDeleting(true, true, false, false, false, false).toOwnFormat('<input type="hidden" name="£{name}£" value="£{value}£">')?no_esc}
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
	    <#if !query_parameters.sorter?? || (query_parameters.sorter?? && query_parameters.sorter != "ROOM_NAME") || (query_parameters.sorterDirection?? && !(query_parameters.sorterDirection))><a class="link-button link-button-enabled" href="?${query_parameters.copyWithNewSorter('ROOM_NAME', true).toUrlFormat()}">${page_info.texts.getString('name_elm_header_title')?no_esc}<#if query_parameters.sorter?? && query_parameters.sorter == "ROOM_NAME"> &#x25BC;</#if></a>
		<#else><a class="link-button link-button-enabled" href="?${query_parameters.copyWithNewSorter('ROOM_NAME', false).toUrlFormat()}">${page_info.texts.getString('name_elm_header_title')?no_esc} &#x25B2;</a></#if>
	  </div>
	  <div class="matrix-rooms-nou-header">
	    <div class="matrix-rooms-nou-header-title-container">
	      <#if (query_parameters.sorter?? && query_parameters.sorter != "NUM_JOINED_MEMBERS") || (query_parameters.sorterDirection?? && query_parameters.sorterDirection)><a class="link-button link-button-enabled" href="?${query_parameters.copyWithNewSorter('NUM_JOINED_MEMBERS', false).toUrlFormat()}">${page_info.texts.getString('users_elm_header_title')?no_esc}<#if !query_parameters.sorter?? || query_parameters.sorter == "NUM_JOINED_MEMBERS"> &#x25B2;</#if></a>
	      <#else><a class="link-button link-button-enabled" href="?${query_parameters.copyWithNewSorter('NUM_JOINED_MEMBERS', true).toUrlFormat()}">${page_info.texts.getString('users_elm_header_title')?no_esc} &#x25BC;</a></#if>
		  <button id="${html_element_ids.NOU_FILTERS_DISPLAY_BUTTON_ID}" class="button">&#x22EE;</button>
	    </div>
		<form method="get" id="${html_element_ids.NOU_FILTERS_BLOCK_ID}" class="matrix-rooms-nou-header-filters <#if query_parameters.maxNOUFilter?? || query_parameters.minNOUFilter??>${html_element_classes.NOU_FILTERS_BLOCK_DISPLAYED_CLASS}<#else>${html_element_classes.NOU_FILTERS_BLOCK_HIDDEN_CLASS}</#if>">
		  <div>
		    <input type="checkbox" id="${html_element_ids.MAX_NOU_FILTER_CHECKBOX_ID}"<#if query_parameters.maxNOUFilter??> checked</#if>>
			<label for="${html_element_ids.MAX_NOU_FILTER_CHECKBOX_ID}">max : </label>
			<input type="number" id="${html_element_ids.MAX_NOU_FILTER_TEXTFIELD_ID}" class="matrix-rooms-current-page-textfield" name="${query_parameters.maxNOUFilterReqName}"<#if query_parameters.maxNOUFilter??> value=${query_parameters.maxNOUFilter?string.computer}</#if><#if query_parameters.minNOUFilter??>  min=${query_parameters.minNOUFilter?string.computer}</#if><#if !query_parameters.maxNOUFilter??> disabled</#if> required>
		  </div>
		  <div>
		    <input type="checkbox" id="${html_element_ids.MIN_NOU_FILTER_CHECKBOX_ID}"<#if query_parameters.minNOUFilter??> checked</#if>>
			<label for="${html_element_ids.MIN_NOU_FILTER_CHECKBOX_ID}">min : </label>
			<input type="number" id="${html_element_ids.MIN_NOU_FILTER_TEXTFIELD_ID}" class="matrix-rooms-current-page-textfield" name="${query_parameters.minNOUFilterReqName}"<#if query_parameters.minNOUFilter??> value=${query_parameters.minNOUFilter?string.computer}</#if> min=1<#if query_parameters.maxNOUFilter??>  max=${query_parameters.maxNOUFilter?string.computer}</#if><#if !query_parameters.minNOUFilter??> disabled</#if> required>
		  </div>
		  ${query_parameters.copyDeleting(true, false, true, false, false, false).toOwnFormat('<input type="hidden" name="£{name}£" value="£{value}£">')?no_esc}
		  <button type="submit" class="button" id="matrix-rooms-nou-filters-validation-button">${page_info.texts.getString('filter_button_name')?no_esc}</button>
		</form>
	  </div>
	  <div class="matrix-rooms-ga-header">
	    <div class="matrix-rooms-ga-header-title-container"><span class="matrix-rooms-ga-header-title" title="${page_info.texts.getString('ga_elm_header_tooltip')?no_esc}">${page_info.texts.getString('ga_elm_header_title')?no_esc}</span><button id="${html_element_ids.GA_FILTER_DISPLAY_BUTTON_ID}" class="button">&#x22EE;</button></div>
		<form method="get" id="${html_element_ids.GA_FILTER_BLOCK_ID}" class="matrix-rooms-ga-header-filter <#if query_parameters.gaFilter??>${html_element_classes.GA_FILTER_BLOCK_DISPLAYED_CLASS}<#else>${html_element_classes.GA_FILTER_BLOCK_HIDDEN_CLASS}</#if>">
		  <div>
		    <input type="radio" id="matrix-rooms-ga-filter-all-radio" name="${query_parameters.gaFilterReqName}" value="NO_FILTER"<#if !query_parameters.gaFilter?? || (query_parameters.gaFilter?? && query_parameters.gaFilter == "NO_FILTER")> checked</#if>>
			<label for="matrix-rooms-ga-filter-all-radio">${page_info.texts.getString('filter_selection_no_filter_txt')?no_esc}</label>
		  </div>
		  <div>
		    <input type="radio" id="matrix-rooms-ga-filter-yes-radio" name="${query_parameters.gaFilterReqName}" value="CAN_JOIN"<#if query_parameters.gaFilter?? && query_parameters.gaFilter == "CAN_JOIN"> checked</#if>>
			<label for="matrix-rooms-ga-filter-yes-radio">${page_info.texts.getString('filter_selection_allowed_txt')?no_esc}</label>
		  </div>
		  <div>
		    <input type="radio" id="matrix-rooms-ga-filter-no-radio" name="${query_parameters.gaFilterReqName}" value="CAN_NOT_JOIN"<#if query_parameters.gaFilter?? && query_parameters.gaFilter == "CAN_NOT_JOIN"> checked</#if>>
			<label for="matrix-rooms-ga-filter-no-radio">${page_info.texts.getString('filter_selection_disallowed_txt')?no_esc}</label>
		  </div>
		  ${query_parameters.copyDeleting(true, false, false, true, false, false).toOwnFormat('<input type="hidden" name="£{name}£" value="£{value}£">')?no_esc}
		  <button type="submit" class="button">${page_info.texts.getString('filter_button_name')?no_esc}</button>
		</form>
	  </div>
	  <div class="matrix-rooms-wr-header">
	    <div class="matrix-rooms-wr-header-title-container"><span class="matrix-rooms-wr-header-title">${page_info.texts.getString('wr_elm_header_title')?no_esc}</span><button id="${html_element_ids.WR_FILTER_DISPLAY_BUTTON_ID}" class="button">&#x22EE;</button></div>
	    <form method="get" id="${html_element_ids.WR_FILTER_BLOCK_ID}" class="matrix-rooms-wr-header-filter <#if query_parameters.wrFilter??>${html_element_classes.WR_FILTER_BLOCK_DISPLAYED_CLASS}<#else>${html_element_classes.WR_FILTER_BLOCK_HIDDEN_CLASS}</#if>">
		  <div>
		    <input type="radio" id="matrix-rooms-wr-filter-all-radio" name="${query_parameters.wrFilterReqName}" value="NO_FILTER"<#if !query_parameters.wrFilter?? || (query_parameters.wrFilter?? && query_parameters.wrFilter == "NO_FILTER")> checked</#if>>
			<label for="matrix-rooms-wr-filter-all-radio">${page_info.texts.getString('filter_selection_no_filter_txt')?no_esc}</label>
		  </div>
		  <div>
		    <input type="radio" id="matrix-rooms-wr-filter-yes-radio" name="${query_parameters.wrFilterReqName}" value="IS_WORLD_READABLE"<#if query_parameters.wrFilter?? && query_parameters.wrFilter == "IS_WORLD_READABLE"> checked</#if>>
			<label for="matrix-rooms-wr-filter-yes-radio">${page_info.texts.getString('filter_selection_yes_txt')?no_esc}</label>
		  </div>
		  <div>
		    <input type="radio" id="matrix-rooms-wr-filter-no-radio" name="${query_parameters.wrFilterReqName}" value="IS_NOT_WORLD_READABLE"<#if query_parameters.wrFilter?? && query_parameters.wrFilter == "IS_NOT_WORLD_READABLE"> checked</#if>>
			<label for="matrix-rooms-wr-filter-no-radio">${page_info.texts.getString('filter_selection_no_txt')?no_esc}</label>
		  </div>
		  ${query_parameters.copyDeleting(true, false, false, false, true, false).toOwnFormat('<input type="hidden" name="£{name}£" value="£{value}£">')?no_esc}
		  <button type="submit" class="button">${page_info.texts.getString('filter_button_name')?no_esc}</button>
		</form>
	  </div>
	  <div class="matrix-rooms-server-header">
	    <div class="matrix-rooms-server-header-title-container">
	      <#if !query_parameters.sorter?? || (query_parameters.sorter?? && query_parameters.sorter != "SERVER_NAME") || (query_parameters.sorterDirection?? && !(query_parameters.sorterDirection))><a class="link-button link-button-enabled" href="?${query_parameters.copyWithNewSorter('SERVER_NAME', true).toUrlFormat()}">${page_info.texts.getString('server_elm_header_title')?no_esc}<#if query_parameters.sorter?? && query_parameters.sorter == "SERVER_NAME"> &#x25BC;</#if></a>
		  <#else><a class="link-button link-button-enabled" href="?${query_parameters.copyWithNewSorter('SERVER_NAME', false).toUrlFormat()}">${page_info.texts.getString('server_elm_header_title')?no_esc} &#x25B2;</a></#if>
		  <button id="${html_element_ids.SERVER_FILTER_DISPLAY_BUTTON_ID}" class="button">&#x22EE;</button>
	    </div>
		<form method="get" id="${html_element_ids.SERVER_FILTER_BLOCK_ID}" class="matrix-rooms-server-header-filter <#if query_parameters.serverFilter??>${html_element_classes.SERVER_FILTER_BLOCK_DISPLAYED_CLASS}<#else>${html_element_classes.SERVER_FILTER_BLOCK_HIDDEN_CLASS}</#if>">
		  <select name="${query_parameters.serverFilterReqName}" multiple>
		    <#list serverList as serverId, server>
			<option value="${serverId}"<#if query_parameters.serverFilter?? && query_parameters.serverFilter?seq_contains(serverId)> selected</#if>>${server.name}</option>
			</#list>
		  </select>
		  ${query_parameters.copyDeleting(true, false, false, false, false, true).toOwnFormat('<input type="hidden" name="£{name}£" value="£{value}£">')?no_esc}
		  <button type="submit" class="button" id="matrix-rooms-server-filter-validation-button">${page_info.texts.getString('filter_button_name')?no_esc}</button>
		</form>
	  </div>
	  <#list roomList as room>
	  <#if room?index % 2 == 0><#assign lineVariationClass = "matrix-room-elm-line-var1"><#else><#assign lineVariationClass = "matrix-room-elm-line-var2"></#if>
	  <div class="matrix-room-name-elm-container ${lineVariationClass}">
		<img id="matrix-room-elm-arrow-${room?index}" class="matrix-room-name-arrow" src="/static/img/icon-collapsed.svg" width="17.5em" height="17.5em" onclick="handleMatrixRoomArrowClickEvent('matrix-room-elm-arrow-${room?index}')">
		<span class="matrix-room-name-elm"><#if room.name?has_content>${room.name}<#else>${room.roomId}</#if></span>
	  </div>
	  <div class="matrix-room-nou-elm ${lineVariationClass}">${room.numJoinedMembers}</div>
	  <div class="matrix-room-ga-elm ${lineVariationClass}"><#if room.guestCanJoin>${page_info.texts.getString('ga_elm_allowed_value_txt')?no_esc}<#else>${page_info.texts.getString('ga_elm_disallowed_value_txt')?no_esc}</#if></div>
	  <div class="matrix-room-wr-elm ${lineVariationClass}"><#if room.worldReadable>${page_info.texts.getString('wr_elm_yes_value_txt')?no_esc}<#else>${page_info.texts.getString('wr_elm_no_value_txt')?no_esc}</#if></div>
	  <div class="matrix-room-server-elm ${lineVariationClass}"><a href="${serverList[room.serverId].apiUrl}">${serverList[room.serverId].name}</a></div>
	  <div id="matrix-room-details-${room?index}" class="matrix-room-details ${html_element_classes.MATRIX_ROOM_DETAILS_HIDDEN_CLASS}">
	    <#if room.canonicalAlias??><span class="matrix-room-details-alias-title">${page_info.texts.getString('details_elm_alias_title')?no_esc} :</span><span class="matrix-room-details-alias">${room.canonicalAlias}</span>
	    <#else><span class="matrix-room-details-room-id-title">${page_info.texts.getString('details_elm_room_id_title')?no_esc} :</span><span class="matrix-room-details-room-id">${room.roomId}</span></#if>
		<span class="matrix-room-details-server-title">${page_info.texts.getString('details_elm_server_title')?no_esc} :</span><span class="matrix-room-details-server"><a href="${serverList[room.serverId].apiUrl}">${serverList[room.serverId].name}</a></span>
		<#if room.topic??><span class="matrix-room-details-topic-title">${page_info.texts.getString('details_elm_topic_title')?no_esc} :</span><span class="matrix-room-details-topic" lang="">${room.topic}</span></#if>
	  </div>
	  </#list>
	</div>
	<script>
	  const MATRIX_ROOM_DETAILS_DISPLAYED_CLASS = '${html_element_classes.MATRIX_ROOM_DETAILS_DISPLAYED_CLASS}';
	  const MATRIX_ROOM_DETAILS_HIDDEN_CLASS = '${html_element_classes.MATRIX_ROOM_DETAILS_HIDDEN_CLASS}';
	</script>
    <script src="/static/js/roommap-client-js-matrix-rooms-page-native.js"></script>
	<script src="/static/js/roommap-client-js-matrix-rooms-page.js"></script>
	