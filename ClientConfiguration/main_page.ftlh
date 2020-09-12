<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8">
    <title>Hello World</title>
  </head>
  <body>
    <table>
	  <tr>
	    <th>Name</th>
		<th>Number of users</th>
		<th>Guest access</th>
	  </tr>
	<#list rooms as room>
	  <tr>
	    <td <#if (room.topic)??>title="${room.topic}"</#if>><#if (room.name)??>${room.name}<#else>${room.room_id}</#if></td>
		<td>${room.num_joined_members}</td>
		<td><#if room.guest_can_join>yes<#else>no</#if></td>
	  </tr>
	</#list>
	</table>
  </body>
</html>