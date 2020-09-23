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
    <menu type="toolbar">
      <#if page_info.current_page_number gt 1><a href="?${queries.rooms_per_page_req_name}=${page_info.matrix_rooms_per_page?string.computer}&${queries.page_req_name}=1"></#if>&lt;&lt;<#if page_info.current_page_number gt 1></a></#if>
      <#if page_info.current_page_number gt 1><a href="?${queries.rooms_per_page_req_name}=${page_info.matrix_rooms_per_page?string.computer}&${queries.page_req_name}=${(page_info.current_page_number-1)?string.computer}"></#if>&lt;<#if page_info.current_page_number gt 1></a></#if>
      <input type="number" id="pageReqField" value="${page_info.current_page_number?string.computer}" min="1" max="${page_info.max_page?string.computer}"> of ${page_info.max_page}
      <#if page_info.current_page_number lt page_info.max_page><a href="?${queries.rooms_per_page_req_name}=${page_info.matrix_rooms_per_page?string.computer}&${queries.page_req_name}=${(page_info.current_page_number+1)?string.computer}"></#if>&gt;<#if page_info.current_page_number lt page_info.max_page></a></#if>
      <#if page_info.current_page_number lt page_info.max_page><a href="?${queries.rooms_per_page_req_name}=${page_info.matrix_rooms_per_page?string.computer}&${queries.page_req_name}=${page_info.max_page?string.computer}"></#if>&gt;&gt;<#if page_info.current_page_number lt page_info.max_page></a></#if>
      <select>
      <#list page_info.rooms_per_page_standard_num as standard_num>
         <option<#if standard_num == page_info.matrix_rooms_per_page> selected</#if> value=${standard_num?string.computer}>${standard_num} per page</option>
      </#list>
      </select>
    </menu>
    <div id="matrix-rooms-container">
	  <div class="matrix-rooms-name-header">Name</div>
      <div class="matrix-rooms-nou-header">Number of users</div>
	  <div class="matrix-rooms-ga-header">Guest access</div>
	  <div class="matrix-rooms-wr-header">World readable</div>
	  <div class="matrix-rooms-server-header">Server</div>
	  <#list rooms as room>
	  <div id="matrix-room-name-elm-${room?index}" class="matrix-room-name-elm"><#if (room.name)??>${room.name}<#else>${room.roomId}</#if></div>
      <div class="matrix-room-nou-elm">${room.numJoinedMembers}</div>
	  <div class="matrix-room-ga-elm"><#if room.guestCanJoin>yes<#else>no</#if></div>
	  <div class="matrix-room-wr-elm"><#if room.worldReadable>yes<#else>no</#if></div>
	  <div class="matrix-room-server-elm"><a href="${servers[room.serverId].apiUrl}">${servers[room.serverId].name}</a></div>
	  <#if (room.topic)??><div id="matrix-room-topic-elm-${room?index}" class="matrix-room-topic-elm">${room.topic}</div></#if>
	  </#list>
	</div>
    <script>
      const debugMode = ${debug_mode?c};
      const initialRoomsPerPage = ${page_info.matrix_rooms_per_page?string.computer};
      const initialPageNumber = ${page_info.current_page_number?string.computer};
    </script>
    <script src="/static/js/roommap-client-js-matrix-rooms-page.js"></script>
  </body>
</html>