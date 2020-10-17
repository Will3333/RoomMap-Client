<!DOCTYPE html>
<html lang="${globalData.mainLang.bcp47}">
  <head>
    <meta charset="UTF-8">
    <title>${globalData.websiteName}<#if texts.containsKey('page_title')> - ${texts.getString('page_title')?no_esc}</#if></title>
	<#if texts.containsKey('meta_page_description')><meta name="description" content="${texts.getString('meta_page_description')?no_esc}"></#if>
	<#if robotsMeta??>${robotsMeta.toHTMLFormat()?no_esc}</#if>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="/static/css/global.css">
	<#if cssFileNames??><#list cssFileNames as css_file>
	<link rel="stylesheet" href="/static/css/${css_file}">
	</#list></#if>
  </head>
  <body>
    <nav class="lang-menu">
	  <#if globalData.mainLang == "ENG"><span class="link-button link-button-disabled lang-item">Eng</span><#else><a class="link-button link-button-enabled lang-item" href="/eng${urlPath}?${queryParameters.toUrlFormat()}">Eng</a></#if>
	  <#if globalData.mainLang == "FRA"><span class="link-button link-button-disabled lang-item">Fra</span><#else><a class="link-button link-button-enabled lang-item" href="/fra${urlPath}?${queryParameters.toUrlFormat()}">Fra</a></#if>
	</nav>
    <#include templateFileName>
	<hr class="page-footer-separator">
	<footer class="footer">
	  <p class="footer-copyright">${globalData.texts.getString('copyright_txt')?no_esc}</p>
	  <p class="footer-license-details">${globalData.texts.getString('copyright_details_txt')?no_esc}</p>
	</footer>
    <script>
      const TOOLTIP_BLOCK_DISPLAYED_CLASS = '${globalData.cssClasses.TOOLTIP_BLOCK_DISPLAYED}';
      const TOOLTIP_BLOCK_HIDDEN_CLASS = '${globalData.cssClasses.TOOLTIP_BLOCK_HIDDEN}';
    </script>
    <#if jsFileNames??><#list jsFileNames as js_file>
    <script src="/static/js/${js_file}"></script>
    </#list></#if>
  </body>
</html>