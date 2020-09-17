<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8">
    <title>Hello World</title>
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
	    <td <#if (room.room_id)??>title="${room.room_id}"</#if>><#if (room.name)??>${room.name}<#else>${room.room_id}</#if></td>
		<td>${room.num_joined_members}</td>
		<td><#if room.guest_can_join>yes<#else>no</#if></td>
	  </tr>
	  <tr>
	    <td></td>
	    <td colspan="3"><#if (room.topic)??>${room.topic}</#if></td>
	  </tr>
	</#list>
	</table>
	<h2>Page</h2>
	<span><#if page_info.current_page gt 1 >1..</#if><#if (page_info.current_page-1) gt 1>${page_info.current_page-1}..</#if>${page_info.current_page}<#if (page_info.current_page+1) lt page_info.max_page>..${page_info.current_page+1}</#if><#if page_info.current_page lt page_info.max_page>..${page_info.max_page}</#if></span>
  </body>
</html>