1、Grid采用的是jqgrid（jquery.jqGrid-3.8.2）版本（并没有用最新版本），导致的缺陷是很多其他功能需要单独引用grid的扩展插件，比如（grid.grouping.js、grid.setcolumns.js）
	jqgrid下载地址：http://www.trirand.com/blog/?page_id=6
	中文帮助文档网址：http://blog.mn886.net/jqGrid/
	主题定制网址：http://jqueryui.com/themeroller
		主题定制时下载请勾选jquery-ui-1.10.4.custom稳定版本

2、智能联想下拉框jquerUI的下拉控件（jquery-ui-widget-combobox.js），因为
	jqgrid也属于jqueryUI的一部分，所以他们共享theme主题样式，在引用jquery-ui-widget-combobox.js时，需要引用
	jquery-ui-1.8.17.custom.min.js脚本

3、plugins是扩展功能类，主要是因为新版本的jqgrid，不再支持老版本的一些插件功能，但是为了保证用户在使用中新版本的jqgrid的同时又可以
使用老版本的插件功能，可以单独引用这些类

4、初始化页面选择风格css时采用的是asp.net服务端ruant=server方式，使用这种方式的原因是easyui框架如果在初始化时没有指定样式
，然后在脚本中设置风格样式时会造成渲染失效