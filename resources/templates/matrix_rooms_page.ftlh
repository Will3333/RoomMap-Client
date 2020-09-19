<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8">
    <title>Hello World</title>
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
	    <td><a href="${room.server.api_url}">${room.server.name}</a></td>
	    <td <#if (room.room_id)??>title="${room.room_id}"</#if>><details>
	      <summary><#if (room.name)??>${room.name}<#else>${room.room_id}</#if></summary>
	      <#if (room.topic)??>${room.topic}</#if>
	    </details></td>
		<td>${room.num_joined_members}</td>
		<td><#if room.guest_can_join>yes<#else>no</#if></td>
	  </tr>
	</#list>
	</table>
    <menu type="toolbar">
      <#if page_info.current_page gt 1><a href="?${queries.rooms_per_page_req_name}=${page_info.matrix_rooms_per_page?string.computer}&${queries.page_req_name}=1"></#if>&lt;&lt;<#if page_info.current_page gt 1></a></#if>
      <#if page_info.current_page gt 1><a href="?${queries.rooms_per_page_req_name}=${page_info.matrix_rooms_per_page?string.computer}&${queries.page_req_name}=${(page_info.current_page-1)?string.computer}"></#if>&lt;<#if page_info.current_page gt 1></a></#if>
      <input type="number" id="pageReqField" value="${page_info.current_page?string.computer}" min="1" max="${page_info.max_page?string.computer}"> of ${page_info.max_page}
      <#if page_info.current_page lt page_info.max_page><a href="?${queries.rooms_per_page_req_name}=${page_info.matrix_rooms_per_page?string.computer}&${queries.page_req_name}=${(page_info.current_page+1)?string.computer}"></#if>&gt;<#if page_info.current_page lt page_info.max_page></a></#if>
      <#if page_info.current_page lt page_info.max_page><a href="?${queries.rooms_per_page_req_name}=${page_info.matrix_rooms_per_page?string.computer}&${queries.page_req_name}=${page_info.max_page?string.computer}"></#if>&gt;&gt;<#if page_info.current_page lt page_info.max_page></a></#if>
      <select>
      <#list page_info.rooms_per_page_standard_num as standard_num>
        <option<#if standard_num == page_info.matrix_rooms_per_page> selected</#if> value=${standard_num?string.computer}>${standard_num} per page</option>
      </#list>
      </select>
    </menu>
    <script>
      const debugMode = ${debug_mode?c};
      const apiUrlStr = "${api_url}";
    </script>
    <script src="/static/js/roommap-client-js-matrix-rooms-page.js"></script>
  </body>
</html>