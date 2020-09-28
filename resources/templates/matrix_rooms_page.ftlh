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
        Page <input type="number" id="pageReqField" value="${page_info.current_page_number?string.computer}" min="1" max="${page_info.max_page?string.computer}"> of ${page_info.max_page}
        <#if page_info.current_page_number lt page_info.max_page><a href="?${queries.rooms_per_page_req_name}=${page_info.matrix_rooms_per_page?string.computer}&${queries.page_req_name}=${(page_info.current_page_number+1)?string.computer}"></#if>&gt;<#if page_info.current_page_number lt page_info.max_page></a></#if>
        <#if page_info.current_page_number lt page_info.max_page><a href="?${queries.rooms_per_page_req_name}=${page_info.matrix_rooms_per_page?string.computer}&${queries.page_req_name}=${page_info.max_page?string.computer}"></#if>&gt;&gt;<#if page_info.current_page_number lt page_info.max_page></a></#if>
      </span>
      <span class="matrix-rooms-per-page-management">
        Display <input type="number" list="matrix-rooms-per-page-datalist" value="${page_info.matrix_rooms_per_page?string.computer}" min="1"> rooms per page
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
      </span>
    </menu>
    <div id="matrix-rooms-container">
	  ${room_elm_list?no_esc}
	</div>
    <script>
      const debugMode = ${debug_mode?c};
      const initialRoomsPerPage = ${page_info.matrix_rooms_per_page?string.computer};
      const initialPageNumber = ${page_info.current_page_number?string.computer};
    </script>
    <script src="/static/js/roommap-client-js-matrix-rooms-page-native.js"></script>
    <script src="/static/js/roommap-client-js-matrix-rooms-page.js"></script>
  </body>
</html>