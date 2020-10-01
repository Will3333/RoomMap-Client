<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8">
    <title>Hello World</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="/static/css/matrix_rooms_page.css">
  </head>
  <body>
    <h1>Rooms</h1>
    <menu class="matrix-rooms-page-management" type="toolbar">
      <span class="matrix-rooms-current-page-management">
        <span class="button button-disabled"><#if page_info.current_page_number gt 1><a href="?${queries.rooms_per_page_req_name}=${page_info.matrix_rooms_per_page?string.computer}&${queries.page_req_name}=1"></#if>&lt;&lt;<#if page_info.current_page_number gt 1></a></#if></span>
        <#if page_info.current_page_number gt 1><a href="?${queries.rooms_per_page_req_name}=${page_info.matrix_rooms_per_page?string.computer}&${queries.page_req_name}=${(page_info.current_page_number-1)?string.computer}"></#if>&lt;<#if page_info.current_page_number gt 1></a></#if>
        <form method="get">
		  Page <input type="number" name="${queries.page_req_name}" value="${page_info.current_page_number?string.computer}" min="1" max="${page_info.max_page?string.computer}">
		  <input type="hidden" name="${queries.rooms_per_page_req_name}" value="${page_info.matrix_rooms_per_page?string.computer}">
		</form> of ${page_info.max_page}
        <#if page_info.current_page_number lt page_info.max_page><a href="?${queries.rooms_per_page_req_name}=${page_info.matrix_rooms_per_page?string.computer}&${queries.page_req_name}=${(page_info.current_page_number+1)?string.computer}"></#if>&gt;<#if page_info.current_page_number lt page_info.max_page></a></#if>
        <#if page_info.current_page_number lt page_info.max_page><a href="?${queries.rooms_per_page_req_name}=${page_info.matrix_rooms_per_page?string.computer}&${queries.page_req_name}=${page_info.max_page?string.computer}"></#if>&gt;&gt;<#if page_info.current_page_number lt page_info.max_page></a></#if>
      </span>
      <span class="matrix-rooms-per-page-management"><form method="get">
        Display <input type="number" name="${queries.rooms_per_page_req_name}" list="matrix-rooms-per-page-datalist" value="${page_info.matrix_rooms_per_page?string.computer}" min="1"> rooms per page
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
	  <div class="matrix-rooms-name-header">Name</div>
	  <div class="matrix-rooms-nou-header">Users</div>
	  <div class="matrix-rooms-ga-header">Guest access</div>
	  <div class="matrix-rooms-wr-header">World readable</div>
	  <div class="matrix-rooms-server-header">Server</div>
	  <#list roomList as room>
	  <#if room?index % 2 == 0><#assign lineVariationClass = "matrix-room-elm-line-var1"><#else><#assign lineVariationClass = "matrix-room-elm-line-var2"></#if>
	  <div class="matrix-room-name-elm-container ${lineVariationClass}">
	    <#if room.topic??><div class="matrix-room-name-arrow-container" onclick="handleMatrixRoomArrowClickEvent('matrix-room-elm-arrow-${room?index}')"><span id="matrix-room-elm-arrow-${room?index}" class="matrix-room-name-arrow matrix-room-name-right-arrow"></span></div>
		<#else><div class="matrix-room-name-arrow-container"><span hidden></span></div></#if>
		<div class="<#if room.topic??>matrix-room-name-elm<#else>matrix-room-name-elm-without-arrow</#if>"><#if room.name??>${room.name}<#else>${room.roomId}</#if></div>
	  </div>
	  <div class="matrix-room-nou-elm ${lineVariationClass}">${room.numJoinedMembers}</div>
	  <div class="matrix-room-ga-elm ${lineVariationClass}"><#if room.guestCanJoin>yes<#else>no</#if></div>
	  <div class="matrix-room-wr-elm ${lineVariationClass}"><#if room.worldReadable>yes<#else>no</#if></div>
	  <div class="matrix-room-server-elm ${lineVariationClass}"><a href="${serverList[room.serverId].apiUrl}">${serverList[room.serverId].name}</a></div>
	  <#if room.topic??><div id="matrix-room-topic-elm-${room?index}" class="matrix-room-topic-elm ${lineVariationClass}">${room.topic}</div></#if>
	  </#list>
	</div>
    <script src="/static/js/roommap-client-js-matrix-rooms-page-native.js"></script>
  </body>
</html>