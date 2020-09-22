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
    <table>
	  <tr>
	    <th>Server</th>
	    <th>Name</th>
		<th>Number of users</th>
		<th>Guest access</th>
	  </tr>
	<#list rooms as room>
	  <tr>
	    <td><a href="${servers[room.serverId].apiUrl}">${servers[room.serverId].name}</a></td>
	    <td <#if (room.roomId)??>title="${room.roomId}"</#if>><details>
	      <summary><#if (room.name)??>${room.name}<#else>${room.roomId}</#if></summary>
	      <#if (room.topic)??>${room.topic}</#if>
	    </details></td>
		<td>${room.numJoinedMembers}</td>
		<td><#if room.guestCanJoin>yes<#else>no</#if></td>
	  </tr>
	</#list>
	</table>
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
    <script>
      const debugMode = ${debug_mode?c};
      const initialRoomsPerPage = ${page_info.matrix_rooms_per_page?string.computer};
      const initialPageNumber = ${page_info.current_page_number?string.computer};
    </script>
    <script src="/static/js/roommap-client-js-matrix-rooms-page.js"></script>
  </body>
</html>