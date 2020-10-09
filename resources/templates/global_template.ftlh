<!DOCTYPE html>
<html lang="${page_info.main_lang.bcp47}">
  <head>
    <meta charset="UTF-8">
    <title>${website_info.name} - ${page_info.texts.getString('page_title')?no_esc}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="/static/css/global.css">
	<#list page_info.css_files as css_file>
	<link rel="stylesheet" href="/static/css/${css_file}">
	</#list>
  </head>
  <body>
    <nav class="lang-menu">
	  <#if page_info.main_lang == "ENG"><span class="link-button link-button-disabled lang-item">Eng</span><#else><a class="link-button link-button-enabled lang-item" href="/eng${page_info.path_name}?${query_parameters.toUrlFormat()}">Eng</a></#if>
	  <#if page_info.main_lang == "FRA"><span class="link-button link-button-disabled lang-item">Fra</span><#else><a class="link-button link-button-enabled lang-item" href="/fra${page_info.path_name}?${query_parameters.toUrlFormat()}">Fra</a></#if>
	</nav>
    <#include page_info.template_file>
	<hr class="page-footer-separator">
	<footer class="footer">
	  <p class="footer-copyright">${website_info.texts.getString('copyright_txt')?no_esc}</p>
	  <p class="footer-license-details">${website_info.texts.getString('copyright_details_txt')?no_esc}</p>
	</footer>
  </body>
</html>