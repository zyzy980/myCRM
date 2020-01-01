﻿
$(function(){
	//处理键盘事件 禁止后退键（Backspace）密码或单行、多行文本框除外  
	function forbidBackSpace(e) {  
	    var ev = e || window.event; //获取event对象   
	   var obj = ev.target || ev.srcElement; //获取事件源   
	   var t = obj.type || obj.getAttribute('type'); //获取事件源类型   
	   //获取作为判断条件的事件类型   
	   var vReadOnly = obj.readOnly;  
	    var vDisabled = obj.disabled;  
	    //处理undefined值情况   
	   vReadOnly = (vReadOnly == undefined) ? false : vReadOnly;  
	    vDisabled = (vDisabled == undefined) ? true : vDisabled;  
	    //当敲Backspace键时，事件源类型为密码或单行、多行文本的，   
	   //并且readOnly属性为true或disabled属性为true的，则退格键失效   
	   var flag1 = ev.keyCode == 8 && (t == "password" || t == "text" || t == "textarea") && (vReadOnly == true || vDisabled == true);  
	    //当敲Backspace键时，事件源类型非密码或单行、多行文本的，则退格键失效   
	   var flag2 = ev.keyCode == 8 && t != "password" && t != "text" && t != "textarea";  
	    //判断   
	   if (flag2 || flag1) return false;  
	}  
	//禁止后退键 作用于Firefox、Opera  
	document.onkeypress = forbidBackSpace;  
	//禁止后退键  作用于IE、Chrome  
	document.onkeydown = forbidBackSpace; 
	
	$(window.document).find(".easyui-combobox").each(function(i){		
		var cid=$(this).attr("id");
		var opts = $('#'+cid).combobox("options");
		//根据属性comformatter来格式化输出formatter
		var cindex=0;
		$('#'+cid).combobox({
			formatter: function (row) {					
				var id=$(this).attr("id");
				cindex++;
			    if ((row[opts.valueField] == "" || cindex==1) && opts.isReflush!=false) {
			        return '<table style=\"width:100%;\"><tr><td  style=\"text-align:left;width:80%;\"><span >' + row[opts.textField] + '</span></td><td  style=\"text-align:right;width:20%;\"><img src=\"../../Resource/images/icons/refresh.gif\" style=\"display:inline-block;\" onclick=\"reloadComboboxData(\''+id+'\')\"/></td></tr></table>';
			    } else {
			       return '<table style=\"width:100%;\"><tr><td  style=\"text-align:left;width:100%;\"><span >' + row[opts.textField] + '</span></td></tr></table>';
			    }			    
			}
		});		
	});
});

var showLog=function(options)
{
	try
	{
		if(options.id==null || options.id=="")
			options.id="toolbar";
		if($("#"+options.id).length<=0)
			return;
		var logBtnName=(GetLang()=="en"?"log":"日志");
		var html='<a href="javascript:OperateLogs(\''+options.operateModuleGlobal+'\',\''+options.moduleId+'\');" id="showLog180709" class="easyui-linkbutton l-btn l-btn-small l-btn-plain" plain="true" iconcls="icon-add" style="float: right;display: inline-block;margin-right:15px;" plain="true" iconcls="icon-help">';
		html+='<span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">'+logBtnName+'</span><span class="l-btn-icon icon-help">&nbsp;</span></span>';
		html+="</a>";
		$("#"+options.id).append(html);
		
	} catch (e)
	{
		// TODO: handle exception
	}
}

var GetLang=function()
{
	var lang="zh";//中文
	try
	{
		var parms = getUrlParms();
		lang = parms["lang"];
	} 
	catch (e)
	{
		// {: handle exception
	}
	return lang;
}

var ExportToExcel = function (_options) {
	
	if(_options.loseGridFocus){
		_options.loseGridFocus();
	}else if(window.loseGridFocus){
		window.loseGridFocus();
	}
	$("#formExportFile").remove();
   
	var export_title = "";
	if(_options && _options.title){
		export_title = _options.title;
	}else{
		var list = $("#gridPager_left .ui-pg-div");
		for(var i = 0; i < list.length; i++){
			if(list.eq(i).text() == window.parent.parent.exportTitle){
				var title = $(this).parents(".ui-pg-button").attr("title");
				if(title){
					export_title = tile;
				}
			}
		}
	}
    var url = _options.url;
    var httpUrl = "";
    var _this = this;
    url = location.pathname.substring(0,location.pathname.substring(1).indexOf("/") +1)+"/service/base/Base_DataController/exportExcelData";
    //加载 form 表单
    var html = '<form id="formExportFile" class="formExportFile" target="_blank" action="'+url+'" method="post" style="display: none">';
    html += '<input type="text" id="export_title" name="export_title" value="'+export_title+'" />'; //标题 -> 文件名
    html += '<input type="text" id="url" name="url" value="" />'; //标题 -> 文件名
    html += '<input type="text" id="FiledData" name="FiledData" value="" />'; //列英文名
    html += '<input type="text" id="Export_ColumnCode" name="Export_ColumnCode" value="" />'; //列英文名
    html += '<input type="text" id="Export_ColumnName" name="Export_ColumnName" value="" />';//列中文名
    html += '<input type="text" id="Export_ColumnWidth" name="Export_ColumnWidth" value="" />';//列宽度
    html += '<input type="text" id="Export_ColumnAlign" name="Export_ColumnAlign" value="" />';//列对齐方式
    html += '<input type="text" id="Export_customSearchFilters" name="Export_customSearchFilters" value="" />'; //查询条件
    html += '<input type="text" id="Export_KIND" name="Export_KIND" value="0" />';  // 0.以查询条件导出数据, 1.导出当前界面数据, 2.导出列表选中数据
    html += '<input type="text" id="Export_CurrentPageData" name="Export_CurrentPageData" value="" />'; //当前页数据
    html += '</form>';
    $("body").append(html);
    var html = "<table width='100%'>";
    if(_options.type == "0"){
    	html += "<tr><td align='left' style='padding:4px 4px 4px 90px;'><input type='radio' name='ExportKind' id='ExportKind2' value='1' checked/><label for='ExportKind2'>导出当前页面的数据</label></td></tr>";
    	html += "<tr><td align='left' style='padding:4px 4px 4px 90px;'><input type='radio' name='ExportKind' id='ExportKind3' value='2' /><label for='ExportKind3'>导出列表选中的数据</label></td></tr>";

    }else{
    	html += "<tr><td align='left' style='padding:4px 4px 4px 90px;'><input type='radio' name='ExportKind' id='ExportKind1' value='0' checked /><label for='ExportKind1'>以查询条件导出数据</label></td></tr>";
    	html += "<tr><td align='left' style='padding:4px 4px 4px 90px;'><input type='radio' name='ExportKind' id='ExportKind2' value='1' /><label for='ExportKind2'>导出当前页面的数据</label></td></tr>";
    	html += "<tr><td align='left' style='padding:4px 4px 4px 90px;'><input type='radio' name='ExportKind' id='ExportKind3' value='2' /><label for='ExportKind3'>导出列表选中的数据</label></td></tr>";
  

        httpUrl = location.origin + location.pathname.substring(0,location.pathname.substring(1).indexOf("/") +1);
        var url = $(_this).jqGrid("getGridParam", "url");
        if(url){
        	httpUrl = httpUrl + url.substring(url.indexOf("/service/"));
        }else{
        	alert("先进行查询操作后再导出");
        	return;
        }
    }
    html += "</table>";
    
    
    
    if(!(typeof _options.getSearchFilters == "function")){
		if(window.getSearchFilters){
			_options.getSearchFilters = window.getSearchFilters; 			
		}else{
			alert("必须指定getSearchFilters属性方法,且返回值类型为对象");
			return;
		}
	}
    $.messager.confirm({
    	title: window.parent.parent.exportTitle,
        msg: window.parent.parent.exportHTMLHead + window.parent.parent.exportHTMLBody,
        width:380,
        height: 240,
        fn: function (r) {
            if (r) {
                var KIND = $("input[type='radio'][name='ExportKind']:checked").val();
                $("#Export_KIND").val(KIND);

                $("#formExportFile #url").val(httpUrl);
                
                
                var colModelPageList = $(_this).jqGrid("getGridParam", "colModel") || [];
                var colNamePageList = $(_this).jqGrid("getGridParam", "colNames") || [];

                var Export_ColumnCode = [];
                var Export_ColumnName = [];
                var Export_ColumnWidth = [];//列宽度
                var Export_ColumnAlign = [];//列对齐方式
                for (var i = 0, ilen = colNamePageList.length; i < ilen; i++){
                    if (!colModelPageList[i].hidden){
                    	if(colModelPageList[i].name == "rn" || colModelPageList[i].name == "cb"){
                    		continue;
                    	}
                        Export_ColumnCode.push(colModelPageList[i].name);
                        Export_ColumnWidth.push(colModelPageList[i].width);
                        Export_ColumnAlign.push(colModelPageList[i].align);
                        Export_ColumnName.push(colNamePageList[i]);
                    }
                }
            
                $("#Export_ColumnCode").val(Export_ColumnCode.join(","));
                $("#Export_ColumnName").val(Export_ColumnName.join(","));
                $("#Export_ColumnWidth").val(Export_ColumnWidth.join(","));
                $("#Export_ColumnAlign").val(Export_ColumnAlign.join(","));
                if (KIND == "1") {
                    //导出当前列表数据
                    var dataList = $(_this).jqGrid("getRowData");
                    if (dataList.length <= 0){
                        errorNotification({ SimpleMessage: window.parent.parent.exportMessageFail , MoreMessage: window.parent.parent.exportMessageEmptyValid });
                        return;
                    }
                    
                    var newDataList = [];
                    for(var i = 0; i < dataList.length; i++){
                    	//删除无必要的字段
                    	var obj = {};
                    	for(var j=0; j <Export_ColumnCode.length; j++){
                    		obj[Export_ColumnCode[j]] = dataList[i][Export_ColumnCode[j]];
                    	}
	                	delete obj.ViewDetail;
	                	
	                	if(_options.beforeRequestRow){
	                		_options.beforeRequestRow(obj);
	                	}
                    	newDataList.push(obj);
                    }
                    $("#Export_CurrentPageData").val(JSON.stringify(newDataList));
                }else if (KIND == "2") {
                    //导出选中列表的数据
                    var selectRowItems = $(_this).jqGrid("getGridParam", "selarrrow");
                    if (selectRowItems.length <= 0) {
                        errorNotification({ SimpleMessage: window.parent.parent.exportMessageFail , MoreMessage: window.parent.parent.exportMessageSelectValid });
                        return;
                    }
                    var dataList = [];
                    var rowData = null;
                    for (var i = 0, ilen = selectRowItems.length; i < ilen; i++){
                        rowData = $(_this).jqGrid('getRowData', selectRowItems[i]);
	                	var obj = {};
	                	for(var j=0; j <Export_ColumnCode.length; j++){
	                		obj[Export_ColumnCode[j]] = rowData[Export_ColumnCode[j]];
	                	}
	                	delete obj.ViewDetail;
	                	if(_options.beforeRequestRow){
	                		_options.beforeRequestRow(obj);
	                	}
	                	dataList.push(obj);
                    }
                    $("#Export_CurrentPageData").val(JSON.stringify(dataList));
                }else {
                    
                	
                	var requestParam = _options.getSearchFilters();//$(_this).jqGrid("getGridParam", "postData");
                   /* if(!(typeof requestParam ==  "object")){
                    	alert("查询条件返回值必须为对象类型");
                    	return;
                    }*/
                	
                    requestParam.sidx = $(_this).jqGrid("getGridParam", "sortname");
                    requestParam.sord = $(_this).jqGrid("getGridParam", "sortorder");
                    
                    
                    requestParam.page = 1;
                    requestParam.rows = 65530;
                   
                    delete requestParam._search;
                    delete requestParam.nd;
                    if(_options.beforeRequestRow){
                		_options.beforeRequestRow(requestParam);
                	}
                    if(_options.FiledData){
                    	$("#formExportFile #FiledData").val(JSON.stringify(_options.FiledData));
                    }
                    $("#Export_customSearchFilters").val($.param(requestParam));
                }
                $("#formExportFile").submit();
            }
        }
    });
}

var reloadComboboxData=function(t) {
	$('#'+t).combobox('reload');
};
var enterTriggerEvent = function(rangeBoxName, eventButtonName) {
	$("#" + rangeBoxName + " input[type='text']").each(function() {
		$(this).bind('keyup', function(event) {
			if (event.keyCode == "13") {
				eval(eventButtonName + "()");
			}
		});
	});

};
function addDate(date,days){ 
    var d=new Date(date); 
    d.setDate(d.getDate()+days); 
    var m=d.getMonth()+1; 
    return d.getFullYear()+'-'+m+'-'+d.getDate(); 
}

var IECheck=function()
{
	var bResult = false;
	if(window.top.frames["maniFrame"].contentWindow!=null && window.top.frames["maniFrame"].contentWindow!=undefined)
	{
		bResult = true;
	}
	return bResult;
}

var checkboxJqgrid = function(rowid,iCol,cellcontent,e){
	 /*if(e.toElement.localName!="input"&&e.toElement.childNodes[0].type!='checkbox'){
		   var classStr = "#jqg_gridTable_"+rowid;
		   var flag = $(classStr).get(0).checked;
		   $("#gridTable").jqGrid("resetSelection");
     	if(flag)
     	$("#gridTable").jqGrid('setSelection',rowid);
	   } */
	if(!e.ctrlKey && !e.shiftKey){
		  if(iCol!=1){
     		$("#gridTable").jqGrid('resetSelection');
     		$("#gridTable").jqGrid('setSelection',rowid);
     	 } 
		}
  	if(e.shiftKey){
  	var selectRowItems = $("#gridTable").jqGrid("getGridParam", "selarrrow"); 
  	 var rowData = $("#gridTable").jqGrid('getDataIDs'); 
  	 
		var start=0;
		var end="";
		for ( var i = 0; i < rowData.length; i++) {
			if(selectRowItems.length>1){
  				if(selectRowItems[selectRowItems.length-2]==rowData[i]){
  					start=i;
  				}	
  			}
			if(rowid==rowData[i]){
				end=i;
			}
		}
		if(start>end){
			var middle=start;
			start=end;
			end=middle;
		}
		var k=0;
		for ( var i = start; i < end; i++) {
			k=0;
			for ( var j = 0; j < selectRowItems.length; j++) {
				if(selectRowItems[j]==rowData[i]){
					k=1;
				}
			}
			if(k==0){
				$("#gridTable").jqGrid('setSelection',rowData[i]);
			}
		}
  	}
};



var checkboxJqgrid_id = function(rowid,iCol,cellcontent,e,id){
	if(!e.ctrlKey && !e.shiftKey){
		  if(iCol!=1){
     		$("#"+id).jqGrid('resetSelection');
     		$("#"+id).jqGrid('setSelection',rowid);
     	 } 
		}
  	if(e.shiftKey){
  	var selectRowItems = $("#"+id).jqGrid("getGridParam", "selarrrow"); 
  	 var rowData = $("#"+id).jqGrid('getDataIDs'); 
  	 
		var start=0;
		var end="";
		for ( var i = 0; i < rowData.length; i++) {
			if(selectRowItems.length>1){
  				if(selectRowItems[selectRowItems.length-2]==rowData[i]){
  					start=i;
  				}	
  			}
			if(rowid==rowData[i]){
				end=i;
			}
		}
		if(start>end){
			var middle=start;
			start=end;
			end=middle;
		}
		var k=0;
		for ( var i = start; i < end; i++) {
			k=0;
			for ( var j = 0; j < selectRowItems.length; j++) {
				if(selectRowItems[j]==rowData[i]){
					k=1;
				}
			}
			if(k==0){
				$("#"+id).jqGrid('setSelection',rowData[i]);
			}
		}
  	}
};


/* 获取url参数:使用方式 var parame = args["id"] */
var getUrlParms = function() {
	// var Obj = function () { };
	// var c = new Obj();
	var args = new Object();
	var query = location.search.substring(1); // 获取查询串
	var pairs = query.split("&"); // 在逗号处断开
	for (var i = 0; i < pairs.length; i++) {
		var pos = pairs[i].indexOf('='); // 查找name=value
		if (pos == -1)
			continue; // 如果没有找到就跳过
		var argname = pairs[i].substring(0, pos); // 提取name
		var value = pairs[i].substring(pos + 1); // 提取value
		args[argname] = decodeURIComponent(value); // 存为属性(解码)
		// c[argname] = decodeURIComponent(value);
	}
	return args;
}

/* 判断undefined、null或者null字符串转换成空字符 */
var formatStr = function(data) {
	// 判断undefined、null
	if (data == null || data == undefined || data == "null") {
		return "";
	}

	return data;
}

/* 判断服务器返回的ServerResultData是否有错误、为空、null、列表是否大于0等 */
var isServerResultDataPass = function(serverResultData) {
	if (serverResultData != null && serverResultData.resultCode == 0 && serverResultData.resultDataFull != null) 
	{
		return true;
	}
	return false;
}

var isServerResultListDataPass = function(serverResultData) {
	if (serverResultData != null && serverResultData.length != null
			&& serverResultData.resultDataFull == null) {
		// list不会返回结果状态,出现了说明报错
		return true;
	}
	return false;
}

/* 根据服务器返回的ServerResultData中的错误级别操作弹出不同的提示 */
/* 1：应用级别提示；2：应用级别警告；3:应用级别错误,4:系统级别错误,5：session过期 */
var FailResultDataToTip = function(serverResultData) {
	if (serverResultData.resultCode == 1 || serverResultData.resultCode == 2
			|| serverResultData.resultCode == 3
			|| serverResultData.resultCode == 4) {
		window.top.showNotification({
			SimpleMessage : serverResultData.resultDataFull.simpleMessage,
			MoreMessage : serverResultData.resultDataFull.moreMessage,
			ShowMoreMessage : serverResultData.resultDataFull.showMoreMessage,
			AutoHide : serverResultData.resultDataFull.autoHide,
			Type : serverResultData.resultCode
		});
	}

	if (serverResultData.resultCode == 5) {
		if(window.OSUN_login == null){
			window.OSUN_login = "no";
			
			var lang=GetLang();
			var url_login="/vehicle_js/Index/Login.html";
			var alert_tit="操作提示";
			var alert_text="由于系统更新或者您长时间未操作,为了保证系统安全您需要重新登录";
			if(lang=="en")
			{
				url_login="/vehicle_js/Index/Login.html?lang="+lang;
				alert_tit="alert";
				alert_text="Login timeout, please Login again";
			}

            var urlPath = location.href;
            if(urlPath.indexOf('www.niutms.com') != -1) {
                // 如果不为 -1 的话，就代表url里面已经有/BBA了。就在url_login去掉 /BBA
                url_login = "/Index/Login.html";
            }

			try
			{
				window.top.$.messager.alert(alert_tit, alert_text, "error", function () {
					window.top.location = url_login;
		        });
			} 
			catch (e)
			{
				alert(alert_text);
				window.top.location = url_login; 
			}
		}
	}
}

var formatSearchParames = function(paramesList, groupOp) {
	var paramesJson = {};
	if (!groupOp) {
		paramesJson.groupOp = "AND";
	} else {
		paramesJson.groupOp = groupOp;
	}

	paramesJson.rules = [];
	for (var i = 0; i < paramesList.length; i++) {
		var name = paramesList[i].name;
		var value = $.trim(paramesList[i].value);
		var op = paramesList[i].op;

		if (name == null || value == "") {
			continue;
		}

		var parameObj = {};
		parameObj.field = name;

		if (op == null || op == "") {
			parameObj.op = "eq";
		} else {
			parameObj.op = op;
		}

		parameObj.data = value;
		paramesJson.rules.push(parameObj);
	}

	return JSON.stringify(paramesJson);
};
/* 将参数格式化服务端统一的json格式对象 */
var formatSearchParamesJson = function(paramesList, groupOp) {
	var paramesJson = {};
	if (!groupOp) {
		paramesJson.groupOp = "AND";
	} else {
		paramesJson.groupOp = groupOp;
	}

	paramesJson.rules = [];
	for (var i = 0; i < paramesList.length; i++) {
		var name = paramesList[i].name;
		var value = $.trim(paramesList[i].value);
		var op = paramesList[i].op;

		if (name == null || value == "") {
			continue;
		}

		var parameObj = {};
		parameObj.field = name;

		if (op == null || op == "") {
			parameObj.op = "eq";
		} else {
			parameObj.op = op;
		}

		parameObj.data = value;
		paramesJson.rules.push(parameObj);
	}

	return paramesJson;
};

/** 查询清单页面切换显示和隐藏更多的查询条件 * */
function toggleParame(obj) {
	if ($(obj).attr("pointType") == "pointDown") {
		$(obj).attr("class", "toggleLinkTop");
		$(obj).attr("pointType", "pointUp");
		$("#searchParamesTable tr:gt(0)").show();
	} else {
		$(obj).attr("class", "toggleLinkDown");
		$(obj).attr("pointType", "pointDown");
		$("#searchParamesTable tr:gt(0)").hide();
	}
	;
	setGridHeightWidth();
}
// 按钮失效
var myDisabled = function(jq, message) {
	var doMessage = (message != null) ? function() {
		errorNotification({
			SimpleMessage : message
		});
		return false;
	} : function() {
		return false;
	};
	if (jq.attr("id") == "save") {
		$(".icon-save").removeClass("icon-save").addClass("icon-save-disabled");
		$("#moreSave *").click(doMessage);
		$("#save *").click(doMessage);
	} else {
		$("input", jq.next("span")).attr("readonly", "readonly");
		$("input", jq.next("span")).prev().addClass("text_dis");
		
		jq.next("span").find("a").click(doMessage);
		jq.combobox("textbox").unbind("focus");
	}
}
// 按钮恢复
var myDisabledun = function(jq) {
	if (jq.attr("id") == "save") {
		$(".icon-save-disabled").removeClass("icon-save-disabled").addClass(
				"icon-save");
		$("#moreSave *").unbind("click");
		$("#save *").unbind("click");
	} else {
		jq.next("span").removeAttr("readonly")
		jq.next("span").find("a").unbind("click");
		$("input", jq.next("span")).attr("readonly", false);
		var val = jq.combobox("getValue");
		jq.combobox();
		jq.combobox("setValue", val);
		
		$("input", jq.next("span")).prev().removeClass("text_dis");

		
	}
}

/** 动态设置jqGrid的高度和宽度，因为该控件无自适应机制，需要在窗口改变大小事件中触发* */
/** subtractWidth：需要页面可见宽度减去的宽度值* */
/** subtractHeight：需要页面可见高度减去的高度值* */
/** 如果不指定减去高度和宽度值，则系统自动计算* */
var setGridHeightWidth = function(subtractWidth, subtractHeight, gridTableId) {
	var gridTableObj = $("#gridTable");
	if (gridTableId) {
		gridTableObj = $("#" + gridTableId);
	}
	if (subtractWidth == "" || subtractWidth == null) {
		subtractWidth = 0;
	}

	if (subtractHeight == "" || subtractHeight == null) {
		subtractHeight = 0;
		$("#gridControl").prevAll().each(function() {
			subtractHeight += $(this).height();
		});

		subtractHeight += 63;
	}

	gridTableObj.setGridWidth($(window).width() - subtractWidth);
	var moreHight = 0; 
	if (gridTableObj.closest("#gbox_gridTable").find(".ui-search-toolbar").css("display") == "table-row") {
		moreHight = gridTableObj.closest("#gbox_gridTable").find(".ui-search-toolbar").height();
	}
	gridTableObj.setGridHeight($(window).height() - subtractHeight - moreHight);

	// 火狐点快速搜索
	if (window.navigator.userAgent.indexOf("Chrome") == -1) {
		gridTableObj.find(".ui-search-toolbar").height();
	}
}

var setToolbarHeightWidth = function(toolbarId) {
	if (toolbarId) {
		$("#" + toolbarId).css("width", "100%");
	} else {
		$("#toolbar").css("width", "100%");
	}
}

/** 切换展示jqgrid的表头下的搜索条* */
/** 如果不传递最新url，则无法和自定义查询条件取交集* */
var toggleGridSearchToolbar = function(id) {
	if(id==''|| id==null|| id== undefined || typeof(id)=="object")
	{
		id="gridTable";
	}
	if ($("#gbox_"+id+" .ui-search-toolbar").css("display") != "table-row") {
		if ($("#gbox_"+id+" .ui-search-toolbar").length <= 0) {
			$("#"+id).jqGrid('filterToolbar', {
				stringResult : true,
				searchOperators : true,
				beforeSearch : function() {
					
					$("#"+id).jqGrid('setGridParam', {
						url : getSearchGridUrl()
					});
				}
			});
		} else {
			$("#gbox_"+id+" .ui-search-toolbar").show();
		}
		setGridHeightWidth(null,null,id);
	} else {
		$("#gbox_"+id+" .ui-search-toolbar").hide();
		setGridHeightWidth(null,null,id);
	}
}

var toggleGridSearchToolbarById = function(width, height, id, _search) {
 
		var toolbar = $("#" + id).closest("#gridControl").find(".ui-search-toolbar");
		if (toolbar.css("display") != "table-row") {
			if (toolbar.length <= 0) {
				$("#" + id).jqGrid('filterToolbar', {
					stringResult : true,
					searchOperators : true,
					beforeSearch : function() {
						var search = {};
						if (!_search) {
							search = getSearchGridUrl;
						}
						$("#" + id).jqGrid('setGridParam', {
							url : search()
						});
					}
				});
			} else {
				toolbar.show();
			}
			if (width != null && height != null) {
				setGridHeightWidth(width + 20, height + 10, id);
				setGridHeightWidth(width, height, id);
			}
		} else {
			toolbar.hide();
			if (width != null && height != null) {
				setGridHeightWidth(width, height, id);
			}
		}
 

}

/** 切换展示jqgrid的表头下的搜索条* */
var toggleGridColumns = function() {	 
	var gridColumnOptions = {
		updateAfterCheck : true
	}
	jQuery("#gridTable").setColumns(gridColumnOptions);
	$('#ColTbl_gridTable').css('max-height' , $(window).height() - 160);
}

/** 切换展示jqgrid的表头下的搜索条* */
var toggleGridColumnsById = function(id) {
	//return function() {
		var gridColumnOptions = {
			updateAfterCheck : true
		}
		jQuery(id).setColumns(gridColumnOptions);
	//}
}

// 根据状态 获取字符值
var toStateValue = function(code) {
	if (code == "0") {
		return "正常";
	} else if (code == "1") {
		return "结算";
	} else if (code == "2") {
		return "货代审核";
	} else if (code == "3") {
		return "商务审核";
	} else if (code == "5") {
		return "客户审核";
	} else if (code == "6") {
		return "财务审核";
	} else if (code == "7") {
		return "台帐审核";
	} else if (code == "9") {
		return "付款审核";
	} else {
		return "";
	}

}

/** 用json值生成jqgrid的searchToolbar下的html默认select下拉框所需要的值* */
var formatGridCombobox_Local = function(jsonData, showChooseAll) {
	jsonData = jsonData || []; // 防止空时报错
	var initData = "{";
	if (showChooseAll) {
		initData += "\"\":\"--所有--\",";
	}

	for (var i = 0; i < jsonData.length; i++) {
		initData += "\"" + jsonData[i].dicvalue + "\":\"" + jsonData[i].dictext
				+ "\",";
	}
	if (/,$/.test(initData)) {
		initData = initData.substring(0, initData.length - 1);
	}

	initData += "}";
	return JSON.parse(initData);
}

var formatGridCombobox_ServerSelect = function(jsonData, showChooseAll, key,
		value) {
	var arr = [];
	
	if(showChooseAll){
		arr.push(":");
	}	
	
	for (var i = 0; i < jsonData.length; i++) {
		arr.push("" + jsonData[i][key || "code"] + ":"
				+ jsonData[i][value || "name"]);
	}
	return arr.join(";");
}
var formatGridCombobox_Server = function(jsonData, showChooseAll, key, value) {
	var initData = "";
	if (jsonData != undefined) {
		initData += "{";
		if (showChooseAll) {
			initData += "\"\":\"--所有--\",";
		}

		for (var i = 0; i < jsonData.length; i++) {
			if (i == jsonData.length - 1) {
				initData += "\"" + jsonData[i][key || "code"] + "\":\""
						+ jsonData[i][value || "name"] + "\"";
			} else {
				initData += "\"" + jsonData[i][key || "code"] + "\":\""
						+ jsonData[i][value || "name"] + "\",";
			}
		}
		
		if(/,$/.test(initData)){
			initData = initData.substring(0, initData.length-1);
		}
		
		initData += "}";
	} else {
		initData = "";
		return;
	}
	return JSON.parse(initData);
}

/** 用json值生成jqueryUi的autocomplete 下拉框所需要的值* */
var generateAutocompleteSelectData = function(jsonData) {
	console.log("generateAutocompleteSelectData-------");
	console.log(jsonData);
	var initDataArray = [ {
		"label" : "",
		"value" : ""
	} ];
	if (isServerResultDataPass(jsonData)) {
		for (var i = 0; i < jsonData.ResultDataFull.length; i++) {
			var item = {};
			item.label = jsonData.ResultDataFull[i].Name;
			item.value = jsonData.ResultDataFull[i].Value;
			initDataArray.push(item);
		}
	}

	return initDataArray;
}

/** 通过状态值映射状态显示 * */
var getStateName = function(stateJsonData, stateValue) {
	console.log("getStateName=======");
	console.log(stateValue);
	var stateName = stateValue;
	if (isServerResultDataPass(stateJsonData)) {
		for (var i = 0; i < stateJsonData.ResultDataFull.length; i++) {
			if (stateValue == stateJsonData.ResultDataFull[i].value) {
				stateName = stateJsonData.ResultDataFull[i].name;
				break;
			}
		}
	}

	return stateName;
}


/** 用指定值绑定jqueryUI的autocomplete下拉框(通用的下拉框样式-特殊的请自定义)* */
/**
 * bindControl:需要被绑定的控件对象 ；initJsonData
 * 绑定数据源;showSplitChar:显示列表是否有分隔符；selectedSplitChar：选择后的值是否有分隔符；width：宽度,addFunc
 * :添加按钮需要执行的方法；editFunc:编辑按钮需要执行的方法；deleteFunc:删除按钮需要执行的方法*
 */
var bindAutocompleteControlSelect = function(params) {
	if (isServerResultDataPass(params.initJsonData)) {
		var initData = generateAutocompleteSelectData(params.initJsonData);
		params.bindControl.autocomplete(
				{
					autoFocus : false,
					minLength : 0,
					source : function(request, response) {
						var matcher = new RegExp(".*"
								+ $.ui.autocomplete.escapeRegex(request.term)
								+ ".*", "i");
						var length = 10;
						response($.grep(initData, function(item) {
							if (length > 0) {
								var matchResult = matcher.test(item.label)
										|| matcher.test(item.value);
								if (matchResult) {
									length = length - 1;
								}

								return matchResult;
							}
						}));
					},
					focus : function(event, ui) {
						event.preventDefault();
						if (params.selectedSplitChar) {
							params.bindControl.val(ui.item.value + "-"
									+ ui.item.label);
						} else {
							params.bindControl.val(ui.item.value);
						}
					},
					select : function(event, ui) {
						if ($.trim(ui.item.value) == ""
								&& $.trim(ui.item.label) == "") {
							params.bindControl.val("");
						} else {
							if (params.selectedSplitChar) {
								params.bindControl.val(ui.item.value + "-"
										+ ui.item.label);
							} else {
								params.bindControl.val(ui.item.value);
							}
						}
						return false;
					}
				}).focus(function() {
			if ($(this).attr("readOnly") == true) {

			} else {
				$(this).autocomplete("search");
			}
		}).data("ui-autocomplete")._renderItem = function(ul, item) {
			// if (ul.children().length == 0) {
			// $("<li></li>").append("<img style=\"position:absolute;right:10px;
			// \" src=\"../images/icons/add.gif\" onclick=\"test(event)\"><span
			// style=\"display:block;\">&nbsp;</span>").appendTo(ul);
			// }

			var aa = $("<li></li>").data("item.autocomplete", item);
			if ($.trim(item.value) == "" && $.trim(item.label) == "") {
				if (params.addFunc != "" && params.addFunc != undefined) {
					aa
							.append(
									"<a>"
											+ "&nbsp;"
											+ "<img style=\"position:absolute;right:10px;\" src=\"../images/icons/add.gif\" onclick=\""
											+ params.addFunc + "(event,'"
											+ params.addFuncCallBackFunc
											+ "')\"></a>").appendTo(ul);
				}
			} else {
				var editStr = "";
				var deleteStr = "";
				if (params.editFunc != "" && params.editFunc != undefined) {
					editStr = "<img style=\"position:absolute;right:10px;\" src=\"../images/icons/edit.gif\" onclick=\""
							+ params.editFunc
							+ "(event,'"
							+ params.editFuncCallBackFunc
							+ "','"
							+ item.value
							+ "')\">";
				}

				// if (params.deleteFunc != "" && params.deleteFunc !=
				// undefined) {
				// deleteStr = "<img style=\"position:absolute;right:10px;\"
				// src=\"../images/icons/delete.gif\" onclick=\"" +
				// params.deleteFunc + "(event)\">";
				// }
				if (params.showSplitChar) {
					aa.append(
							"<a>" + item.value + "-" + item.label + editStr
									+ deleteStr + "</a>").appendTo(ul);
				} else {
					aa
							.append(
									"<a>" + item.label + editStr + deleteStr
											+ "</a>").appendTo(ul);
				}
			}

			return aa;
		};

		params.bindControl.data("ui-autocomplete")._resizeMenu = function() {
			this.menu.element.outerWidth(params.width);
		}
	}
}

/** 自定义提示弹出 * */
/** 0:成功 1：应用级别提示 2：应用级别警告 3：应用级别错误4：系统级别错误5：session过期6:loading * */
var alertNotification = function(message) {
	message.Type = 1;
	window.top.showNotification(message);
}

/** 自定义警告弹出 * */
/** 0:成功 1：应用级别提示 2：应用级别警告 3：应用级别错误4：系统级别错误5：session过期6:loading * */
var warningNotification = function(message) {
	message.Type = 2;
	window.top.showNotification(message);
}

/** 自定义错误弹出 * */
/** 0:成功 1：应用级别提示 2：应用级别警告 3：应用级别错误4：系统级别错误5：session过期6:loading * */
var errorNotification = function(message) {
	message.Type = 3;
	window.top.showNotification(message);
}

/** 自定义正确弹出 * */
/** 0:成功 1：应用级别提示 2：应用级别警告 3：应用级别错误4：系统级别错误5：session过期6:loading * */
var correctNotification = function(message) {
	message.Type = 0;
	window.top.showNotification(message);
}

/** 自定义loadng * */
/** 0:成功 1：应用级别提示 2：应用级别警告 3：应用级别错误4：系统级别错误5：session过期6:loading * */
var loadingNotification = function(message) {
	if (message.SimpleMessage == "" || message.SimpleMessage == undefined) {
		message.SimpleMessage = "正在玩命加载中...";
	}

	message.Type = 6;
	window.top.showNotification(message);
}

/** 关闭提示 * */
/** 0:成功 1：应用级别提示 2：应用级别警告 3：应用级别错误4：系统级别错误5：session过期6:loading * */
var closeNotification = function() {
	window.top.hideNotificationTips();
}

/** (top frame)全屏遮罩层加载loading * */
var showLoading = function(message) {
	window.top.startLoading(message);
}

/** (top frame)全屏遮罩层隐藏loading * */

var hideLoading = function() {
	window.top.hideLoading();
}

/** 通用的在顶层打开窗体 * */
var openDialog = function(dialogParamsTemp) {
	if(dialogParamsTemp.href){
		var url = dialogParamsTemp.href;
		var index = url.indexOf("?");
		if(index != -1){
			var left_str = url.substring(0, index);
			var right_str = url.substring(index + 1);
			
			var arr = right_str.split("&");
			for(var i = 0; i < arr.length; i++){
				if(arr[i].length){
					var filed = arr[i].split("=")[0];
					var value = arr[i].split("=")[1];
					value = encodeURIComponent(decodeURIComponent(value));
					arr[i] = filed+"="+value;
				}
			}
			url = left_str + "?"+arr.join("&");
			dialogParamsTemp.href = url;
		}
	}
	window.top.open(dialogParamsTemp);
}

/** 根据dialogId关闭弹出框对象 * */
var closeDialog = function(dialogId) {
	if (dialogId == undefined) {
		return;
	}

	window.top.close(dialogId);
}

/** 关闭所有弹出框对象-如果只有一个弹出框的话也是可以调用的和closeSlefDialog效果一样 * */
var closeAllDialog = function() {
	window.top.$.ligerDialog.close();
}

/** 打开新的tab中的框架窗体 * */
var createNewTab = function(title, url) {
	if(IECheck())
		window.top.frames["maniFrame"].contentWindow.frames["rightFrame"].TabCreate(title, url);
	else
		window.top.frames["maniFrame"].frames["rightFrame"].TabCreate(title, url);
}

/** 关闭当前打开的tab中的框架窗体 * */
var closeCurrentTab = function() {
	if(IECheck())
	{
		var tabA = $(window.top.frames["maniFrame"].contentWindow.frames["rightFrame"].document).find(".selected");
		var divTab = tabA.prev();
		var tabId = divTab.attr("lang");
		window.top.frames["maniFrame"].contentWindow.frames["rightFrame"].TabClose(divTab[0], tabId);
	}
	else
	{
		var tabA = $(window.top.frames["maniFrame"].frames["rightFrame"].document).find(".selected");
		var divTab = tabA.prev();
		var tabId = divTab.attr("lang");
		window.top.frames["maniFrame"].frames["rightFrame"].TabClose(divTab[0], tabId);
	}
}

/** 根据dialogId获取弹出框父对象 * */
/** 1、父对象本身是弹出框 * */
/** 2、父对象本身不是弹出框,则获取打开的tab窗体 * */
var getDialog = function(dialogId) {
	var topWindowDoc = $("#" + dialogId, window.top.document);
	if (topWindowDoc.length > 0) {
		var iframeName = $(topWindowDoc).find("iframe").attr("name");
		return window.top.frames[iframeName];
	}
}

/** 获取当前打开的tab中的框架窗体 * */
/** 有一种情况是tab中的frame中再嵌入frame(目前只支持tab中的frame再嵌入一层frame（暂时不支持嵌入多层）) * */
var getCurrentTab = function(iframeId) {
	var tabContent;
	var b_ie=IECheck();
	var tabIframeId=iframeId;
	if (iframeId) {
		var tabContentArray = null;
		if(b_ie)
			tabContentArray = $(window.top.frames["maniFrame"].contentWindow.frames["rightFrame"].document).find("div iframe");
		else 
			tabContentArray = $(window.top.frames["maniFrame"].frames["rightFrame"].document).find("div iframe");
		
		tabContentArray.each(function() {
			// 首先判断自身是否符合id
			if ($(this).attr("id") == iframeId) {
				tabContent = $(this);
				return false;
			}

			var childFrameArray =null;
			if(b_ie)
				childFrameArray =$($(this)[0].contentWindow.document).find("iframe");
			else 
				childFrameArray =$($(this)[0].document).find("iframe");
			
			childFrameArray.each(function() {
				if ($(this).attr("id") == iframeId) {
					tabContent = $(this);
					return false;
				}
			});
		});
	} else {
		var tabDivContent = null;
		if(b_ie)
		{
			tabDivContent =$(window.top.frames["maniFrame"].contentWindow.frames["rightFrame"].document).find("div [class='tabson']");
		}
		else
		{
			tabDivContent =$(window.top.frames["maniFrame"].frames["rightFrame"].document).find("div [class='tabson']");
		}
		tabDivContent.each(function() {
			if ($(this).css("display") == "block") {
				tabContent = $(this).find("iframe");
				tabIframeId=$(this).find("iframe").attr("name");
			}
		});
	}

	if(b_ie)
	{
		return tabContent[0].contentWindow;
	}
	else
	{
		return window.top.frames["maniFrame"].frames["rightFrame"].frames[tabIframeId].window;
		//return tabContent[0];	
	}
}

var formatDefaultCheckbox_Local = function(formatParam) {
	if (!formatParam.data) {
		return "";
	}
	if (!formatParam.bindBoxName) {
		return "";
	}

	if (!formatParam.bindControlPrefix) {
		return "";
	}

	if (formatParam.needChooseAll == undefined) {
		formatParam.needChooseAll = true;
	}

	if (formatParam.chooseAllValue == undefined) {
		formatParam.chooseAllValue = "";
	}

	if (formatParam.defaultValue == undefined) {
		formatParam.defaultValue = "undefined";
	}

	var listHtml = "";
	if (formatParam.needChooseAll) {
		listHtml += "&nbsp;&nbsp;<input type=\"checkbox\" name=\""
				+ formatParam.bindControlPrefix + "_all\" id=\""
				+ formatParam.bindControlPrefix + "_all\" value=\""
				+ formatParam.chooseAllValue + "\" /><label for=\""
				+ formatParam.bindControlPrefix
				+ "_all\">全部</label>&nbsp;&nbsp;";
	}
	for (var i = 0; i < formatParam.data.length; i++) {
		listHtml += "<input type=\"checkbox\" data-default=\""
				+ formatParam.data[i].isDefault + "\" data-name=\""
				+ formatParam.bindControlPrefix + "\" name=\""
				+ formatParam.bindControlPrefix + "_"
				+ formatParam.data[i].dicValue + "\" value='"
				+ formatParam.data[i].dicValue + "' id=\""
				+ formatParam.bindControlPrefix + "_"
				+ formatParam.data[i].dicValue + "\"/>&nbsp;<label for=\""
				+ formatParam.bindControlPrefix + "_"
				+ formatParam.data[i].dicValue + "\">"
				+ formatParam.data[i].dicText + "</label>&nbsp;&nbsp;";
	}

	$("#" + formatParam.bindBoxName).append(listHtml);

	$("#" + formatParam.bindControlPrefix + "_all").click(
			function() {
				var checkAllObj = $(this);
				$("#" + formatParam.bindBoxName).find(
						"input[type='checkbox']:not(#"
								+ formatParam.bindControlPrefix + "_all)")
						.each(
								function() {
									$(this).attr("checked",
											checkAllObj.is(':checked'));
								});
			});

	$("[data-name='" + formatParam.bindControlPrefix + "']").click(
			function() {
				var allNum = $("[data-name='" + formatParam.bindControlPrefix
						+ "']").length;
				var checkedNum = $("[data-name='"
						+ formatParam.bindControlPrefix + "']:checked").length;
				if (checkedNum == allNum) {
					$("#" + formatParam.bindControlPrefix + "_all").attr(
							"checked", true);
				} else {
					$("#" + formatParam.bindControlPrefix + "_all").attr(
							"checked", false);
				}
			});

	// 加载默认值
	if (formatParam.defaultValue == "undefined") {
		$("#" + formatParam.bindBoxName).find("input[type='checkbox']").each(
				function() {
					if ($(this).attr("data-default") == "1") {
						$(this).trigger("click");
					}
				});
	} else {
		$("#" + formatParam.bindBoxName).find("input[type='checkbox']").each(
				function() {
					if ($(this).attr("value") == new String(
							formatParam.defaultValue)) {
						$(this).trigger("click");
					}
				});
	}
}

// 为状态
var formatStateRadio_Local = function(formatParam) {
	// var formatData = { data: null,
	// defaultValue:"",bindBoxName:"",bindControlPrefix:""};
	if (!formatParam.data) {
		return "";
	}

	if (!formatParam.bindBoxName) {
		return "";
	}

	if (!formatParam.bindControlPrefix) {
		return "";
	}

	if (formatParam.defaultValue == undefined) {
		formatParam.defaultValue = "undefined";
	}

	var listHtml = "";
	for (var i = 0; i < formatParam.data.length; i++) {
		listHtml += "<input type=\"radio\" data-default=\""
				+ formatParam.data[i].isDefault + "\" name=\""
				+ formatParam.bindControlPrefix + "\" value='"
				+ formatParam.data[i].dicValue + "' id=\""
				+ formatParam.bindControlPrefix + "_"
				+ formatParam.data[i].dicValue + "\"/>&nbsp;<label for=\""
				+ formatParam.bindControlPrefix + "_"
				+ formatParam.data[i].dicValue + "\">"
				+ formatParam.data[i].dicText + "</label>&nbsp;&nbsp;";
	}

	$("#" + formatParam.bindBoxName).empty();
	$("#" + formatParam.bindBoxName).append(listHtml);
	if (formatParam.defaultValue == "undefined") {
		$(
				"#" + formatParam.bindBoxName + " [name='"
						+ formatParam.bindControlPrefix + "']").each(
				function() {
					if ($(this).attr("data-default") == "1") {
						$(this).attr("checked", "checked");
						var cs = "";
						if (formatParam.data.length == 3) {
							if ($(this).val() == "0") {
								cs = "state_green";
							} else if ($(this).val() == "1") {
								cs = "state_blue";
							} else if ($(this).val() == "2") {
								cs = "btn_bg_orange";
							}
						} else if (formatParam.data.length == 2) {
							if ($(this).val() == 0) {
								cs = "state_green";
							} else if ($(this).val() == 1) {
								cs = "state_orange";
							}
						}
						$(this).next("label").addClass(cs);
					} else {
						$(this).next("label").addClass("state_gray");
					}
				});
	} else {
		$(
				"#" + formatParam.bindBoxName + " [name='"
						+ formatParam.bindControlPrefix + "']")
				.each(
						function() {
							if ($(this).attr("value") == new String(
									formatParam.defaultValue)) {
								$(this).attr("checked", "checked");
								if (formatParam.data.length == 3) {
									// 说明 有 正常,审核,失效
									if (new String(formatParam.defaultValue) == "0") {
										cs = "state_green";
									} else if (new String(
											formatParam.defaultValue) == "1") {
										cs = "state_blue";
									} else if (new String(
											formatParam.defaultValue) == "2") {
										cs = "btn_bg_orange";
									}
								} else if (formatParam.data.length == 2) {
									if (new String(formatParam.defaultValue) == 0) {
										cs = "state_green";
									} else if (new String(
											formatParam.defaultValue) == 1) {
										cs = "state_orange";
									}
								}
								if (cs != '') {
									$(this).next("label").addClass(cs);
								} else {
									$(this).next("label")
											.addClass("state_gray");
								}
							}
						});
	}

	$(
			"#" + formatParam.bindBoxName + " [name='"
					+ formatParam.bindControlPrefix + "']").wrapAll(
			"<span></span>");
	$(
			"#" + formatParam.bindBoxName + " [name='"
					+ formatParam.bindControlPrefix + "']").each(function() {
		$(this).parent().hide();
	});

	// 重写jquery 方法
	var _prop = $.fn.prop;
	$.fn.extend({
		prop : function() {
			if ($(this).attr("name") == "state") {
				$("[name=state]").removeAttr("checked");
				$(this).attr("checked", "checked");
				// 获取状态值
				var stateValue = $(this).val();
				var len = $(this).parent().children().length;

				var cs = "";
				if (len == 3) {
					// 说明 有 正常,审核,失效
					if (stateValue == "0") {
						cs = "state_green";
					} else if (stateValue == "1") {
						cs = "state_blue";
					} else if (stateValue == "2") {
						cs = "state_orange";
					}
				} else if (len == 2) {
					if (stateValue == 0) {
						cs = "state_green";
					} else if (stateValue == 1) {
						cs = "state_orange";
					}
				}
				$("[for=" + $(this).attr("id") + "]");
				$("[for^=state_]").each(function() {
					if ($(this).attr("class") != "state_gray") {
						$(this).attr("class", "state_gray");
					}
				});
				$("[for^=state_" + stateValue + "]").attr("class", cs);
				// 移除状态按钮以前绑定的事件
				$("[name=state]").unbind("click");
				$("[name=state]").click(function() {
					$("[name=state]").removeAttr("checked");
					$(this).attr("checked", "checked");

					// 获取状态值
					var stateValue = $(this).val();
					var len = $(this).parent().children().length;

					var cs = "";
					if (len == 3) {
						// 说明 有 正常,审核,失效
						if (stateValue == "0") {
							cs = "state_green";
						} else if (stateValue == "1") {
							cs = "state_blue";
						} else if (stateValue == "2") {
							cs = "state_orange";
						}
					} else if (len == 2) {
						if (stateValue == 0) {
							cs = "state_green";
						} else if (stateValue == 1) {
							cs = "state_orange";
						}
					}
					$("[for=" + $(this).attr("id") + "]");
					$("[for^=state_]").each(function() {
						if ($(this).attr("class") != "state_gray") {
							$(this).attr("class", "state_gray");
						}
					});
					$("[for^=state_" + stateValue + "]").attr("class", cs);
				});
			} else {
				_prop.apply(this, arguments);
			}
		}
	});

	if (formatParam.isClick) {
		// 可以对状态区域点击
	} else {
		// 禁止用户点击状态区域
		$("#divState").click(function() {
			return false;
		});
	}
}


var formatDefaultCommbobox_Local = function(formatParam){
	
	if (formatParam.needChooseAll == undefined) {
		formatParam.needChooseAll = true;
	}

	if (formatParam.chooseAllValue == undefined) {
		formatParam.chooseAllValue = "";
	}

	var initDataArray = [];
	if (formatParam.needChooseAll) {
		initDataArray = [ {
			"dicValue" : formatParam.chooseAllValue,
			"dicText" : "--所有--"
		} ];
	}

	if (formatParam.data) {
		for (var i = 0; i < formatParam.data.length; i++) {
			initDataArray.push(formatParam.data[i]);
		}
	}

	$('#' + formatParam.bindControlPrefix).combobox({
		 data: initDataArray,
		 valueField: 'dicValue',
		 textField: 'dicText',
		 panelHeight: 150,
		 editable: true,
		 formatter: function (row) {
		 	if(row.dicValue == ""){
	 			return '<table style=\"width:100%;\"><tr><td style=\"text-align:left;width:80%;\"><span>--所有--</span></td><td style=\"text-align:right;width:20%;\"></td></tr></table>';
		 	}else{
		 		return '<table style=\"width:100%;\"><tr><td style=\"text-align:left;width:80%;\"><span>'+row.dicText + '</span></td><td style=\"text-align:right;width:20%;\"></td></tr></table>';
	        }	            
		 },onSelect : function(row){
	     
		 },onLoadSuccess: function(){
		 }
	 });
	$('#' + formatParam.bindControlPrefix).combobox("setValue", formatParam.defaultValue);
}


var close = function () {
    closeDialog($(window.frameElement).parent().attr("id"));
};

var formatDefaultRadio_Local = function(formatParam) {

	// var formatData = { data: null,
	// defaultValue:"",bindBoxName:"",bindControlPrefix:""};
	if (!formatParam.data) {
		return "";
	}

	if (!formatParam.bindBoxName) {
		return "";
	}

	if (!formatParam.bindControlPrefix) {
		return "";
	}

	if (formatParam.defaultValue == undefined) {
		formatParam.defaultValue = "undefined";
	}
	var lang=GetLang();
	 

	var listHtml = "";
	for (var i = 0; i < formatParam.data.length; i++) {
		var dicText_zh_en="";
		if(lang=="en")
			dicText_zh_en=formatParam.data[i].dicText_en;
		else 
			dicText_zh_en=formatParam.data[i].dicText;
		
		listHtml += "<input type=\"radio\" data-default=\""
				+ formatParam.data[i].isDefault + "\" name=\""
				+ formatParam.bindControlPrefix + "\" value='"
				+ formatParam.data[i].dicValue + "' id=\""
				+ formatParam.bindControlPrefix + "_"
				+ formatParam.data[i].dicValue + "\"/>&nbsp;<label for=\""
				+ formatParam.bindControlPrefix + "_"
				+ formatParam.data[i].dicValue + "\">"
				+ dicText_zh_en + "</label>&nbsp;&nbsp;";
	}
	$("#" + formatParam.bindBoxName).empty();
	$("#" + formatParam.bindBoxName).append(listHtml);
	if (formatParam.defaultValue == "undefined") {
		$(
				"#" + formatParam.bindBoxName + " [name='"
						+ formatParam.bindControlPrefix + "']").each(
				function() {
					if ($(this).attr("data-default") == "1") {
						$(this).attr("checked", "checked");
					}
				});
	} else {
		$(
				"#" + formatParam.bindBoxName + " [name='"
						+ formatParam.bindControlPrefix + "']").each(
				function() {
					if ($(this).attr("value") == new String(
							formatParam.defaultValue)) {
						$(this).attr("checked", "checked");
					}
				});
	}
	
	if(formatParam.onClick){
		$("#" + formatParam.bindBoxName + " [name='" + formatParam.bindControlPrefix + "']").bind("click", function(){
			formatParam.onClick.call(this);
		});
	}
}

var formatEasyuiCombobox_Local = function(formatParam) {
	if (formatParam.needChooseAll == undefined) {
		formatParam.needChooseAll = true;
	}

	if (formatParam.chooseAllValue == undefined) {
		formatParam.chooseAllValue = "";
	}

	var initDataArray = [];
	if (formatParam.needChooseAll) {
		initDataArray = [ {
			"Value" : formatParam.chooseAllValue,
			"Text" : "--所有--"
		} ];
	}

	if (formatParam.data) {
		for (var i = 0; i < formatParam.data.length; i++) {
			initDataArray.push(formatParam.data[i]);
		}
	}

	return initDataArray;
}

var formatEasyuiCombobox_LocalNew = function(formatParam) {
	if (formatParam.needChooseAll == undefined) {
		formatParam.needChooseAll = true;
	}

	if (formatParam.chooseAllValue == undefined) {
		formatParam.chooseAllValue = "";
	}

	if (formatParam.needChooseAll) {
		formatParam.data.unshift({
			"dicValue" : formatParam.chooseAllValue,
			"dicText" : "--所有--"
		});
	}
	return formatParam.data;
}

var formatEasyuiCombobox_Driver = function(formatParam) {
	console.log("formatEasyuiCombobox_Driver---------");
	if (formatParam.needChooseAll == undefined) {
		formatParam.needChooseAll = true;
	}

	if (formatParam.chooseAllValue == undefined) {
		formatParam.chooseAllValue = "";
	}

	var initDataArray = [];
	if (formatParam.needChooseAll) {
		initDataArray = [ {
			"Value" : formatParam.chooseAllValue,
			"Text" : "--所有--"
		} ];
	}
	if (formatParam.data) {
		for (var i = 0; i < formatParam.data.length; i++) {
			var item = {};
			item.Text = formatParam.data[i].DRIVER_NAME;
			item.Value = formatParam.data[i].DRIVER_NAME;
			initDataArray.push(item);
		}
	}

	return initDataArray;
}

var formatEasyuiCombobox_XJH = function(formatParam) {

	if (formatParam.needChooseAll == undefined) {
		formatParam.needChooseAll = true;
	}

	if (formatParam.chooseAllValue == undefined) {
		formatParam.chooseAllValue = "";
	}

	var initDataArray = [];
	var cookieOrderArray = [];
	var topShowData = [];
	if (formatParam.cookieOrderKey) {
		var cookieOrderValue = $.cookie(formatParam.cookieOrderKey);
		var cookieOrderArray = JSON.parse(cookieOrderValue);
		if (!cookieOrderArray) {
			cookieOrderArray = [];
		}
	}

	if (formatParam.data) {
		for (var i = 0; i < formatParam.data.length; i++) {
			var item = {};
			item.Text = formatParam.data[i].code;
			item.Value = formatParam.data[i].sn;
			if (cookieOrderArray.length > 0) {
				var insertTopShow = false;
				$.each(cookieOrderArray, function(index, content) {
					if (content.Value == item.Value) {
						item.Order = content.Order;
						topShowData.push(item);
						insertTopShow = true;
						return false;
					}
				});
				if (!insertTopShow) {
					initDataArray.push(item);
				}
			} else {
				initDataArray.push(item);
			}
		}

		// 根据order排序
		if (topShowData.length > 0) {
			var topShowDataList = Enumerable.From(topShowData)
					.OrderByDescending(function(x) {
						return x.Order
					}).ToArray();
			$.each(topShowDataList, function(index, content) {
				initDataArray.unshift(content);
			});
		}
	}

	if (formatParam.needChooseAll) {
		initDataArray.unshift({
			"Value" : formatParam.chooseAllValue,
			"Text" : "--所有--"
		});
	}

	return initDataArray;
}

var formatEasyuiCombobox_JSQY = function(formatParam) {

	if (formatParam.needChooseAll == undefined) {
		formatParam.needChooseAll = true;
	}

	if (formatParam.chooseAllValue == undefined) {
		formatParam.chooseAllValue = "";
	}

	var initDataArray = [];
	var cookieOrderArray = [];
	var topShowData = [];
	if (formatParam.cookieOrderKey) {
		var cookieOrderValue = $.cookie(formatParam.cookieOrderKey);
		var cookieOrderArray = JSON.parse(cookieOrderValue);
		if (!cookieOrderArray) {
			cookieOrderArray = [];
		}
	}

	if (formatParam.data) {
		for (var i = 0; i < formatParam.data.length; i++) {
			var item = {};
			item.Text = formatParam.data[i].code;
			item.Value = formatParam.data[i].sn;
			if (cookieOrderArray.length > 0) {
				var insertTopShow = false;
				$.each(cookieOrderArray, function(index, content) {
					if (content.Value == item.Value) {
						item.Order = content.Order;
						topShowData.push(item);
						insertTopShow = true;
						return false;
					}
				});
				if (!insertTopShow) {
					initDataArray.push(item);
				}
			} else {
				initDataArray.push(item);
			}
		}

		// 根据order排序
		if (topShowData.length > 0) {
			var topShowDataList = Enumerable.From(topShowData)
					.OrderByDescending(function(x) {
						return x.Order
					}).ToArray();
			$.each(topShowDataList, function(index, content) {
				initDataArray.unshift(content);
			});
		}
	}

	if (formatParam.needChooseAll) {
		initDataArray.unshift({
			"Value" : formatParam.chooseAllValue,
			"Text" : "--所有--"
		});
	}

	return initDataArray;
}

/* 通用的格式化成easyui combobox数据 */
var formatEasyuiCombobox_Server = function(formatParam) {
	if (formatParam.needChooseAll == undefined) {
		formatParam.needChooseAll = true;
	}

	if (formatParam.chooseAllValue == undefined) {
		formatParam.chooseAllValue = "";
	}

	var initDataArray = [];
	var cookieOrderArray = [];
	var topShowData = [];
	if (formatParam.cookieOrderKey) {
		var cookieOrderValue = $.cookie(formatParam.cookieOrderKey);
		var cookieOrderArray = JSON.parse(cookieOrderValue);
		if (!cookieOrderArray) {
			cookieOrderArray = [];
		}
	}

	if (formatParam.data) {
		for (var i = 0; i < formatParam.data.length; i++) {
			var item = {};
			item.Text = formatParam.data[i].SeacherName;
			item.Text = formatParam.data[i].code + "-"
					+ formatParam.data[i].name;
			item.Value = formatParam.data[i].code;
			if (cookieOrderArray.length > 0) {
				var insertTopShow = false;
				$.each(cookieOrderArray, function(index, content) {
					if (content.Value == item.Value) {
						item.Order = content.Order
						topShowData.push(item);
						insertTopShow = true;
						return false;
					}
				});
				if (!insertTopShow) {
					initDataArray.push(item);
				}
			} else {
				initDataArray.push(item);
			}
		}

		// 根据order排序
		if (topShowData.length > 0) {
			var topShowDataList = Enumerable.From(topShowData)
					.OrderByDescending(function(x) {
						return x.Order
					}).ToArray();
			$.each(topShowDataList, function(index, content) {
				initDataArray.unshift(content);
			});
		}
	}

	if (formatParam.needChooseAll) {
		initDataArray.unshift({
			"Value" : formatParam.chooseAllValue,
			"Text" : "--所有--"
		});
	}

	return initDataArray;
}

/* 通用的格式化成easyui combobox数据 */
var javaFormatEasyuiCombobox_Server = function(formatParam) {
	if (formatParam.needChooseAll == undefined) {
		formatParam.needChooseAll = true;
	}
	if (formatParam.chooseAllValue == undefined) {
		formatParam.chooseAllValue = "";
	}
	var initDataArray = [];
	var cookieOrderArray = [];
	var topShowData = [];
	if (formatParam.cookieOrderKey) {
		var cookieOrderValue = $.cookie(formatParam.cookieOrderKey);
		var cookieOrderArray = JSON.parse(cookieOrderValue);
		if (!cookieOrderArray) {
			cookieOrderArray = [];
		}
	}

	if (formatParam.data) {
		for (var i = 0; i < formatParam.data.length; i++) {
			var item = {};
			item.Text = formatParam.data[i].code + "-"
					+ formatParam.data[i].NAME;
			item.Value = formatParam.data[i].CODE;
			if (cookieOrderArray.length > 0) {
				var insertTopShow = false;
				$.each(cookieOrderArray, function(index, content) {
					if (content.Value == item.Value) {
						item.Order = content.Order
						topShowData.push(item);
						insertTopShow = true;
						return false;
					}
				});
				if (!insertTopShow) {
					initDataArray.push(item);
				}
			} else {
				initDataArray.push(item);
			}
		}

		// 根据order排序
		if (topShowData.length > 0) {
			var topShowDataList = Enumerable.From(topShowData)
					.OrderByDescending(function(x) {
						return x.Order
					}).ToArray();
			$.each(topShowDataList, function(index, content) {
				initDataArray.unshift(content);
			});
		}
	}

	if (formatParam.needChooseAll) {
		initDataArray.unshift({
			"Value" : formatParam.chooseAllValue,
			"Text" : "--所有--"
		});
	}

	return initDataArray;
}

/* 格式化成easyui combobox数据-特殊业务地点 */
var formatEasyuiCombobox_Server_YwLocation = function(formatParam) {
	if (!formatParam.data) {
		return "";
	}

	if (formatParam.needChooseAll == undefined) {
		formatParam.needChooseAll = true;
	}

	if (formatParam.chooseAllValue == undefined) {
		formatParam.chooseAllValue = "";
	}

	var initDataArray = [];
	if (formatParam.needChooseAll) {
		initDataArray = [ {
			"Value" : formatParam.chooseAllValue,
			"Text" : "--所有--",
			"Desc1" : ""
		} ];
	}

	for (var i = 0; i < formatParam.data.length; i++) {
		var item = {};
		item.Text = formatParam.data[i].name;
		item.Value = formatParam.data[i].code;
		item.Desc1 = formatParam.data[i].ico;
		initDataArray.push(item);
	}

	return initDataArray;
}

var imgView = function(imgUrl) {
	$("#viewBox").remove();
	var maxViewHtml = ' <div id="viewBox" class="viewBox">';
	maxViewHtml += '<div class="closeViewBox"></div>';
	maxViewHtml += '<img class="viewImg" src="' + imgUrl + '"/>';
	maxViewHtml += '</div>';
	$(document.body).append(maxViewHtml);
	var maxViewImgWidth = parseInt($("#viewBox").css("width")) - 50;
	$(".viewImg").css({
		"width" : maxViewImgWidth + "px"
	});
	$("#viewBox").css({
		"display" : "block"
	});
	$(".closeViewBox").unbind("click").click(function() {
		$("#viewBox").css("display", "none");
		$("#viewBox").remove();
	});
}

/* 向cookie中添加置顶元素，并更新cookie值 */
var CookieOrderAdd = function(cookieName, addValue) {
	var cookieValue = $.cookie(cookieName);
	var cookieValueArray = JSON.parse(cookieValue);
	if (!cookieValueArray) {
		cookieValueArray = [];
	}
	// 删除cookie中已经存在的
	var needDeleteCount = 0;
	Enumerable.From(cookieValueArray).ToArray().forEach(function(i) {
		if (i.CODE == addValue) {
			cookieValueArray.splice(needDeleteCount, 1);
		}

		needDeleteCount++;
	});

	var newInsert = {
		Order : 1,
		Value : addValue
	};
	// 其他的Order+1
	$.each(cookieValueArray, function(index, content) {
		content.Order = parseInt(content.Order) + 1;
	});
	// 插入第一位
	cookieValueArray.unshift(newInsert);
	// 删除超过部分
	if (cookieValueArray.length > 5) {
		cookieValueArray.splice(5, 1);
	}
	$.cookie(cookieName, JSON.stringify(cookieValueArray), {
		path : '/'
	});
}

jQuery.fn.shiftSelect = function() {
	var checkboxes = this;
	var lastSelected;
	var executing = false;
	jQuery(this).click(function(event) {

		if (executing)
			return;

		if (!lastSelected) {
			lastSelected = this;
			return;
		}

		if (event.shiftKey) {
			var selIndex = checkboxes.index(this);
			var lastIndex = checkboxes.index(lastSelected);
			/*
			 * if you find the "select/unselect" behavior unseemly, remove this
			 * assignment and replace 'checkValue' with 'true' below.
			 */
			var checkValue = lastSelected.checked;
			if (selIndex == lastIndex) {
				return true;
			}
			executing = true;
			var end = Math.max(selIndex, lastIndex);
			var start = Math.min(selIndex, lastIndex);
			for (i = start; i <= end; i++) {
				if (checkboxes[i].checked != checkValue)
					$(checkboxes[i]).click();
			}
			executing = false;
		}
		lastSelected = this;
	});
}
 
var ExportToExcel = function (_options) {
	
	if(_options.loseGridFocus){
		_options.loseGridFocus();
	}else if(window.loseGridFocus){
		window.loseGridFocus();
	}
	
	$("#formExportFile").remove();
    var export_title = $(document).attr("title");
    var url = _options.url;
    //加载 form 表单
    var html = '<form id="formExportFile" class="formExportFile" target="_blank" action="'+url+'" method="post" style="display: none">';
    html += '<input type="text" id="export_title" name="export_title" value="" />'; //标题 -> 文件名
    html += '<input type="text" id="Export_ColumnCode" name="Export_ColumnCode" value="" />'; //列英文名
    html += '<input type="text" id="Export_ColumnName" name="Export_ColumnName" value="" />';//列中文名
    html += '<input type="text" id="Export_ColumnWidth" name="Export_ColumnWidth" value="" />';//列宽度
    html += '<input type="text" id="Export_ColumnAlign" name="Export_ColumnAlign" value="" />';//列对齐方式
    html += '<input type="text" id="Export_customSearchFilters" name="Export_customSearchFilters" value="" />'; //查询条件
    html += '<input type="text" id="Export_jqGridParamModel" name="Export_jqGridParamModel" value="" />'; //JqGridParamModel
    html += '<input type="text" id="Export_KIND" name="Export_KIND" value="0" />';  // 0.以查询条件导出数据, 1.导出当前界面数据, 2.导出列表选中数据
    html += '<input type="text" id="Export_CurrentPageData" name="Export_CurrentPageData" value="" />'; //当前页数据
    html += '</form>';
    $("body").append(html);

    var _this = this;
    
    if(!(typeof _options.getSearchFilters == "function")){
		if(window.getSearchFilters){
			_options.getSearchFilters = window.getSearchFilters; 			
		}else{
			alert("必须指定getSearchFilters属性方法,且返回值类型为对象");
			return;
		}
	}
    $.messager.confirm({
        title: window.parent.parent.exportTitle,
        msg: window.parent.parent.exportHTMLHead + window.parent.parent.exportHTMLBody,
        width:380,
        height: 240,
        fn: function (r) {
            if (r) {
                $("#export_title").val("");
                var KIND = $("input[type='radio'][name='ExportKind']:checked").val();
                $("#Export_KIND").val(KIND);
                
                
                var colModelPageList = $(_this).jqGrid("getGridParam", "colModel") || [];
                var colNamePageList = $(_this).jqGrid("getGridParam", "colNames") || [];

                var Export_ColumnCode = [];
                var Export_ColumnName = [];
                var Export_ColumnWidth = [];//列宽度
                var Export_ColumnAlign = [];//列对齐方式
                for (var i = 0, ilen = colNamePageList.length; i < ilen; i++){
                    if (!colModelPageList[i].hidden){
                    	if(colModelPageList[i].name == "rn" || colModelPageList[i].name == "cb"){
                    		continue;
                    	}
                        Export_ColumnCode.push(colModelPageList[i].name);
                        Export_ColumnWidth.push(colModelPageList[i].width);
                        Export_ColumnAlign.push(colModelPageList[i].align);
                        Export_ColumnName.push(colNamePageList[i]);
                    }
                }
            
                
                
                var jqGridParamModel={};
            	jqGridParamModel.sidx = $(_this).jqGrid("getGridParam", "sortname");
            	jqGridParamModel.sord = $(_this).jqGrid("getGridParam", "sortorder");
            	jqGridParamModel.page = 1;
            	jqGridParamModel.rows = 65530;
                if (KIND == "1") {
                    //导出当前列表数据
                    var dataList = $(_this).jqGrid("getRowData");
                    if (dataList.length <= 0){
                        errorNotification({ SimpleMessage: window.parent.parent.exportMessageFail , MoreMessage: window.parent.parent.exportMessageEmptyValid });
                        return;
                    }
                    
                    var newDataList = "";
                    for(var i = 0; i < dataList.length; i++){
                    	//删除无必要的字段
//                    	var obj = {};
//                    	for(var j=0; j <Export_ColumnCode.length; j++){
//                    		obj[Export_ColumnCode[j]] = dataList[i][Export_ColumnCode[j]];
//                    	}
//	                	delete obj.ViewDetail;
//	                	
//	                	if(_options.beforeRequestRow){
//	                		_options.beforeRequestRow(obj);
//	                	}
	                	newDataList+="'"+dataList[i].werks+","+dataList[i].lifnr+","+dataList[i].name1+","+dataList[i].trans_mode+","+dataList[i].scts_create_by+"'";
	                	if(i!=dataList.length-1){
	                		newDataList+=",";
	                	}
                    }
                    var parmsArray = [{ name: "werks||','||lifnr||','||name1||','||trans_mode||','||scts_create_by", value: newDataList, op: "in" }];
                    jqGridParamModel.filters=formatSearchParames(parmsArray);
                }else if (KIND == "2") {
                    //导出选中列表的数据
                    var selectRowItems = $(_this).jqGrid("getGridParam", "selarrrow");
                    if (selectRowItems.length <= 0) {
                        errorNotification({ SimpleMessage: window.parent.parent.exportMessageFail , MoreMessage: window.parent.parent.exportMessageSelectValid });
                        return;
                    }
                    var dataList = "";
                    var rowData = null;
                    for (var i = 0, ilen = selectRowItems.length; i < ilen; i++){
                        rowData = $(_this).jqGrid('getRowData', selectRowItems[i]);
	                	dataList+="'"+rowData.werks+","+rowData.lifnr+","+rowData.name1+","+rowData.trans_mode+","+rowData.scts_create_by+"'";
	                	if(i!=selectRowItems.length-1){
	                		dataList+=",";
	                	}
                    }
                    var parmsArray = [{ name: "werks||','||lifnr||','||name1||','||trans_mode||','||scts_create_by", value: dataList, op: "in" }];
                    jqGridParamModel.filters=formatSearchParames(parmsArray);
                }else {
                	jqGridParamModel.filters= _options.getSearchFilters();
                	
                	var obj = JSON.parse(jqGridParamModel.filters);
                	
                	for (var i = 0, ilen = colNamePageList.length; i < ilen; i++){
                        if (!colModelPageList[i].hidden){
                        	if(colModelPageList[i].name == "rn" || colModelPageList[i].name == "cb"){
                        		continue;
                        	}
                        	
                        	var $item = $("#gs_"+colModelPageList[i].name);
                        	
                        	if($item==undefined || $item.val()==""){
                        		continue;
                        	}
                        	
                        	var data = $item.val();
                        	var field = colModelPageList[i].index;
                        	var op = "";
                        	if(data==undefined || data==null || data==undefined){
                        		continue;
                        	}
                        	if(colModelPageList[i].searchoptions!=undefined){
                        		if(colModelPageList[i].searchoptions.sopt!=undefined){
                        			if(colModelPageList[i].searchoptions.sopt.length>0){
                        				op = colModelPageList[i].searchoptions.sopt[0];
                        			}
                        		}
                        	}
                        	if(op==""){
                        		op = "eq";
                        	}
                        	obj.rules.push({"field":field , "op" : op , "data" : data});
                        }
                    }
                	
                	jqGridParamModel.filters = JSON.stringify(obj);
                }
                jqGridParamModel.filters="{"+jqGridParamModel.filters.substring(jqGridParamModel.filters.indexOf("AND")+5);
                $("#Export_CurrentPageData").val(JSON.stringify(jqGridParamModel));
                $("#Export_ColumnCode").val(Export_ColumnCode.join(","));
                $("#Export_ColumnName").val(Export_ColumnName.join(","));
                $("#Export_ColumnWidth").val(Export_ColumnWidth.join(","));
                $("#Export_ColumnAlign").val(Export_ColumnAlign.join(","));
                $("#formExportFile").submit();
            }
        }
    });
}

var ExportToExcel = function (_options) {
	if(_options.loseGridFocus){
		_options.loseGridFocus();
	}else if(window.loseGridFocus){
		window.loseGridFocus();
	}
	
	$("#formExportFile").remove();
    var export_title = $(document).attr("title");
    var url = _options.url;
    //加载 form 表单
    var html = '<form id="formExportFile" class="formExportFile" target="_blank" action="'+url+'" method="post" style="display: none">';
    html += '<input type="text" id="export_title" name="export_title" value="" />'; //标题 -> 文件名
    html += '<input type="text" id="Export_ColumnCode" name="Export_ColumnCode" value="" />'; //列英文名
    html += '<input type="text" id="Export_ColumnName" name="Export_ColumnName" value="" />';//列中文名
    html += '<input type="text" id="Export_ColumnWidth" name="Export_ColumnWidth" value="" />';//列宽度
    html += '<input type="text" id="Export_ColumnAlign" name="Export_ColumnAlign" value="" />';//列对齐方式
    html += '<input type="text" id="Export_customSearchFilters" name="Export_customSearchFilters" value="" />'; //查询条件
    html += '<input type="text" id="Export_jqGridParamModel" name="Export_jqGridParamModel" value="" />'; //JqGridParamModel
    html += '<input type="text" id="Export_KIND" name="Export_KIND" value="0" />';  // 0.以查询条件导出数据, 1.导出当前界面数据, 2.导出列表选中数据
    html += '<input type="text" id="Export_CurrentPageData" name="Export_CurrentPageData" value="" />'; //当前页数据
    html += '</form>';
    $("body").append(html);
   
    var _this = this;
    
    if(!(typeof _options.getSearchFilters == "function")){
		if(window.getSearchFilters){
			_options.getSearchFilters = window.getSearchFilters; 			
		}else{
			alert("必须指定getSearchFilters属性方法,且返回值类型为对象");
			return;
		}
	}
    $.messager.confirm({
        title: window.parent.parent.exportTitle,
        msg: window.parent.parent.exportHTMLHead + window.parent.parent.exportHTMLBody,
        width:380,
        height: 240,
        fn: function (r) {
            if (r) {
                $("#export_title").val("");
                var KIND = $("input[type='radio'][name='ExportKind']:checked").val();
                $("#Export_KIND").val(KIND);
                
                
                var colModelPageList = $(_this).jqGrid("getGridParam", "colModel") || [];
                var colNamePageList = $(_this).jqGrid("getGridParam", "colNames") || [];

                var Export_ColumnCode = [];
                var Export_ColumnName = [];
                var Export_ColumnWidth = [];//列宽度
                var Export_ColumnAlign = [];//列对齐方式
                for (var i = 0, ilen = colNamePageList.length; i < ilen; i++){
                    if (!colModelPageList[i].hidden){
                    	if(colModelPageList[i].name == "rn" || colModelPageList[i].name == "cb"){
                    		continue;
                    	}
                        Export_ColumnCode.push(colModelPageList[i].name);
                        Export_ColumnWidth.push(colModelPageList[i].width);
                        Export_ColumnAlign.push(colModelPageList[i].align);
                        Export_ColumnName.push(colNamePageList[i]);
                    }
                }
            
                
                
                var jqGridParamModel={};
            	jqGridParamModel.sidx = $(_this).jqGrid("getGridParam", "sortname");
            	jqGridParamModel.sord = $(_this).jqGrid("getGridParam", "sortorder");
            	jqGridParamModel.page = 1;
            	jqGridParamModel.rows = 65530;
                if (KIND == "1") {
                    //导出当前列表数据
                    var dataList = $(_this).jqGrid("getRowData");
                    if (dataList.length <= 0){
                        errorNotification({ SimpleMessage: "列表没有数据不能导出" });
                        return;
                    }
                    
                    var newDataList = "";
                    for(var i = 0; i < dataList.length; i++){
                    	//删除无必要的字段
//                    	var obj = {};
//                    	for(var j=0; j <Export_ColumnCode.length; j++){
//                    		obj[Export_ColumnCode[j]] = dataList[i][Export_ColumnCode[j]];
//                    	}
//	                	delete obj.ViewDetail;
//	                	
//	                	if(_options.beforeRequestRow){
//	                		_options.beforeRequestRow(obj);
//	                	}
	                	newDataList+=dataList[i].sn;
	                	if(i!=dataList.length-1){
	                		newDataList+=",";
	                	}
                    }
                    var parmsArray = [{ name: 'r.sn', value: newDataList, op: "in" }];
                    jqGridParamModel.filters=formatSearchParames(parmsArray);
                }else if (KIND == "2") {
                    //导出选中列表的数据
                    var selectRowItems = $(_this).jqGrid("getGridParam", "selarrrow");
                    if (selectRowItems.length <= 0) {
                        errorNotification({ SimpleMessage: "没有选中列表任何数据，不能导出" });
                        return;
                    }
                    var dataList = "";
                    var rowData = null;
                    for (var i = 0, ilen = selectRowItems.length; i < ilen; i++){
                        rowData = $(_this).jqGrid('getRowData', selectRowItems[i]);
	                	dataList+=rowData.sn;
	                	if(i!=selectRowItems.length-1){
	                		dataList+=",";
	                	}
                    }
                    var parmsArray = [{ name: 'r.sn', value: dataList, op: "in" }];
                    jqGridParamModel.filters=formatSearchParames(parmsArray);
                }else {
                	jqGridParamModel.filters= _options.getSearchFilters();
                	
                	var obj = JSON.parse(jqGridParamModel.filters);
                	
                	for (var i = 0, ilen = colNamePageList.length; i < ilen; i++){
                        if (!colModelPageList[i].hidden){
                        	if(colModelPageList[i].name == "rn" || colModelPageList[i].name == "cb"){
                        		continue;
                        	}
                        	
                        	var $item = $("#gs_"+colModelPageList[i].name);
                        	
                        	if($item==undefined || $item.val()==""){
                        		continue;
                        	}
                        	
                        	var data = $item.val();
                        	var field = colModelPageList[i].index;
                        	var op = "";
                        	if(data==undefined || data==null || data==undefined){
                        		continue;
                        	}
                        	if(colModelPageList[i].searchoptions!=undefined){
                        		if(colModelPageList[i].searchoptions.sopt!=undefined){
                        			if(colModelPageList[i].searchoptions.sopt.length>0){
                        				op = colModelPageList[i].searchoptions.sopt[0];
                        			}
                        		}
                        	}
                        	if(op==""){
                        		op = "eq";
                        	}
                        	obj.rules.push({"field":field , "op" : op , "data" : data});
                        }
                    }
                	
                	jqGridParamModel.filters = JSON.stringify(obj);
                }
                jqGridParamModel.filters="{"+jqGridParamModel.filters.substring(jqGridParamModel.filters.indexOf("AND")+5);
                $("#Export_CurrentPageData").val(JSON.stringify(jqGridParamModel));
                $("#Export_ColumnCode").val(Export_ColumnCode.join(","));
                $("#Export_ColumnName").val(Export_ColumnName.join(","));
                $("#Export_ColumnWidth").val(Export_ColumnWidth.join(","));
                $("#Export_ColumnAlign").val(Export_ColumnAlign.join(","));
                $("#formExportFile").submit();
            }
        }
    });
}

//司机
var ExportToExcel = function (_options) {
	if(_options.loseGridFocus){
		_options.loseGridFocus();
	}else if(window.loseGridFocus){
		window.loseGridFocus();
	}
	
	$("#formExportFile").remove();
    var export_title = $(document).attr("title");
    var url = _options.url;
    //加载 form 表单
    var html = '<form id="formExportFile" class="formExportFile" target="_blank" action="'+url+'" method="post" style="display: none">';
    html += '<input type="text" id="export_title" name="export_title" value="" />'; //标题 -> 文件名
    html += '<input type="text" id="Export_ColumnCode" name="Export_ColumnCode" value="" />'; //列英文名
    html += '<input type="text" id="Export_ColumnName" name="Export_ColumnName" value="" />';//列中文名
    html += '<input type="text" id="Export_ColumnWidth" name="Export_ColumnWidth" value="" />';//列宽度
    html += '<input type="text" id="Export_ColumnAlign" name="Export_ColumnAlign" value="" />';//列对齐方式
    html += '<input type="text" id="Export_customSearchFilters" name="Export_customSearchFilters" value="" />'; //查询条件
    html += '<input type="text" id="Export_jqGridParamModel" name="Export_jqGridParamModel" value="" />'; //JqGridParamModel
    html += '<input type="text" id="Export_KIND" name="Export_KIND" value="0" />';  // 0.以查询条件导出数据, 1.导出当前界面数据, 2.导出列表选中数据
    html += '<input type="text" id="Export_CurrentPageData" name="Export_CurrentPageData" value="" />'; //当前页数据
    html += '</form>';
    $("body").append(html);
   
    var _this = this;
    
    if(!(typeof _options.getSearchFilters == "function")){
		if(window.getSearchFilters){
			_options.getSearchFilters = window.getSearchFilters; 			
		}else{
			alert("必须指定getSearchFilters属性方法,且返回值类型为对象");
			return;
		}
	}
    $.messager.confirm({
        title: window.parent.parent.exportTitle,
        msg: window.parent.parent.exportHTMLHead + window.parent.parent.exportHTMLBody,
        width:380,
        height: 240,
        fn: function (r) {
            if (r) {
                $("#export_title").val("");
                var KIND = $("input[type='radio'][name='ExportKind']:checked").val();
                $("#Export_KIND").val(KIND);
                
                
                var colModelPageList = $(_this).jqGrid("getGridParam", "colModel") || [];
                var colNamePageList = $(_this).jqGrid("getGridParam", "colNames") || [];

                var Export_ColumnCode = [];
                var Export_ColumnName = [];
                var Export_ColumnWidth = [];//列宽度
                var Export_ColumnAlign = [];//列对齐方式
                for (var i = 0, ilen = colNamePageList.length; i < ilen; i++){
                    if (!colModelPageList[i].hidden){
                    	if(colModelPageList[i].name == "rn" || colModelPageList[i].name == "cb"){
                    		continue;
                    	}
                        Export_ColumnCode.push(colModelPageList[i].name);
                        Export_ColumnWidth.push(colModelPageList[i].width);
                        Export_ColumnAlign.push(colModelPageList[i].align);
                        Export_ColumnName.push(colNamePageList[i]);
                    }
                }
            
                
                
                var jqGridParamModel={};
            	jqGridParamModel.sidx = $(_this).jqGrid("getGridParam", "sortname");
            	jqGridParamModel.sord = $(_this).jqGrid("getGridParam", "sortorder");
            	jqGridParamModel.page = 1;
            	jqGridParamModel.rows = 65530;
                if (KIND == "1") {
                    //导出当前列表数据
                    var dataList = $(_this).jqGrid("getRowData");
                    if (dataList.length <= 0){
                        errorNotification({ SimpleMessage: "列表没有数据不能导出" });
                        return;
                    }
                    
                    var newDataList = "";
                    for(var i = 0; i < dataList.length; i++){
                    	//删除无必要的字段
//                    	var obj = {};
//                    	for(var j=0; j <Export_ColumnCode.length; j++){
//                    		obj[Export_ColumnCode[j]] = dataList[i][Export_ColumnCode[j]];
//                    	}
//	                	delete obj.ViewDetail;
//	                	
//	                	if(_options.beforeRequestRow){
//	                		_options.beforeRequestRow(obj);
//	                	}
	                	newDataList+=dataList[i].sn;
	                	if(i!=dataList.length-1){
	                		newDataList+=",";
	                	}
                    }
                    var parmsArray = [{ name: 't.sn', value: newDataList, op: "in" }];
                    jqGridParamModel.filters=formatSearchParames(parmsArray);
                }else if (KIND == "2") {
                    //导出选中列表的数据
                    var selectRowItems = $(_this).jqGrid("getGridParam", "selarrrow");
                    if (selectRowItems.length <= 0) {
                        errorNotification({ SimpleMessage: "没有选中列表任何数据，不能导出" });
                        return;
                    }
                    var dataList = "";
                    var rowData = null;
                    for (var i = 0, ilen = selectRowItems.length; i < ilen; i++){
                        rowData = $(_this).jqGrid('getRowData', selectRowItems[i]);
	                	dataList+=rowData.sn;
	                	if(i!=selectRowItems.length-1){
	                		dataList+=",";
	                	}
                    }
                    var parmsArray = [{ name: 't.sn', value: dataList, op: "in" }];
                    jqGridParamModel.filters=formatSearchParames(parmsArray);
                }else {
                	jqGridParamModel.filters= _options.getSearchFilters();
                	
                	var obj = JSON.parse(jqGridParamModel.filters);
                	
                	for (var i = 0, ilen = colNamePageList.length; i < ilen; i++){
                        if (!colModelPageList[i].hidden){
                        	if(colModelPageList[i].name == "rn" || colModelPageList[i].name == "cb"){
                        		continue;
                        	}
                        	
                        	var $item = $("#gs_"+colModelPageList[i].name);
                        	
                        	if($item==undefined || $item.val()==""){
                        		continue;
                        	}
                        	
                        	var data = $item.val();
                        	var field = colModelPageList[i].index;
                        	var op = "";
                        	if(data==undefined || data==null || data==undefined){
                        		continue;
                        	}
                        	if(colModelPageList[i].searchoptions!=undefined){
                        		if(colModelPageList[i].searchoptions.sopt!=undefined){
                        			if(colModelPageList[i].searchoptions.sopt.length>0){
                        				op = colModelPageList[i].searchoptions.sopt[0];
                        			}
                        		}
                        	}
                        	if(op==""){
                        		op = "eq";
                        	}
                        	obj.rules.push({"field":field , "op" : op , "data" : data});
                        }
                    }
                	
                	jqGridParamModel.filters = JSON.stringify(obj);
                }
                jqGridParamModel.filters="{"+jqGridParamModel.filters.substring(jqGridParamModel.filters.indexOf("AND")+5);
                $("#Export_CurrentPageData").val(JSON.stringify(jqGridParamModel));
                $("#Export_ColumnCode").val(Export_ColumnCode.join(","));
                $("#Export_ColumnName").val(Export_ColumnName.join(","));
                $("#Export_ColumnWidth").val(Export_ColumnWidth.join(","));
                $("#Export_ColumnAlign").val(Export_ColumnAlign.join(","));
                $("#formExportFile").submit();
            }
        }
    });
}


var refreshCurrentTab = function(){
	showLoading($(document).attr("title"));
	location.reload();
}


/*
1、引用
<link type="text/css" rel="stylesheet" href="../../../vehicle_js/Resource/js/fileUpload/css/fileUpload.css" />
<script type="text/javascript" src="../../../vehicle_js/Resource/js/fileUpload/js/upload.js"></script>
<script type="text/javascript" src="../../../vehicle_js/Resource/js/fileUpload/js/file.js"></script>
2、body标签最后加入如下HTML:
<div id="fileUpload" class="fileUpload" style="position: absolute; left: 50%; top: 50%; width: 600px; height: 400px; margin-left: -300px; margin-top: -200px; display: none;"></div>
参数说明：
Code=对应配置表的编号
downModuleFileUrl=上传模板文件
callBackFun=上传成功回调函数
例：
options={Code:"",fileTypeFilter:"",downModuleFileUrl:"",callBackFun:CallYOUFunction};
*/
var OperateLogs = function(operateModuleGlobal,moduleId) {
	var lang = GetLang();
	if (!operateModuleGlobal) {
        correctNotification({ SimpleMessage: (lang == 'en' ? 'Request params not valid' : '请求参数无效') });
	}
	showLoading();
    var href = '../../../vehicle_js/View/SysInfo/SysOperateLogLayer.html?operatemodule=' + operateModuleGlobal + '&moduleId=' + moduleId;
    openDialog({
    	id : 'SysOperateLogLayer',
    	title : (lang == 'en' ? 'Operate Logs' : '操作日志'),
    	width : 1050,
    	height : 625,
    	isResize : true,
    	href : href,
    	closable : true
    });
};
var UpLoadFile = function (options) {
    var id="fileUpload";
    $("#"+id).remove();
	var html='<div id="'+id+'" class="fileUpload" style="position: absolute; left: 50%; top: 50%; width: 600px; height: 400px; margin-left: -300px; margin-top: -200px; display: none;"></div>';
	$("body").append(html);
	if(undefined == options.fileTypeFilter || options.fileTypeFilter==null || options.fileTypeFilter=="")
		options.fileTypeFilter=['jpg', 'png', 'bmp', 'jpeg', 'gif', 'xls', 'xlsx', 'txt', 'dwg', 'rar', 'pdf', 'zip'];
	options.remark="";
	for(var i=0,ilen=options.fileTypeFilter.length;i<ilen;i++)
	{
		options.remark+=options.fileTypeFilter[i];
		if(i<ilen-1)
			options.remark+=",";
	}
	var lang=GetLang();
	if(lang=="en")
	{
		options.remark="<p>only upload \""+options.remark+"\" Type File</p>";
	}
	else
	{
		options.remark="<p>只能上传\""+options.remark+"\"格式文件</p>";
	}

	var url = '../../../vehicle_js/service/Common/UpLoadFiles/FileToServers?CODE=' + encodeURI(options.Code) + '&t=' + Math.random();
    $("#" + id).zyUpload({
        itemWidth: "60px", // 文件项的宽度
        itemHeight: "60px", // 文件项的高度
        url: url,
        multiple: false, // 是否可以多个文件上传
        dragDrop: true, // 是否可以拖动上传文件
        del: false, // 是否可以删除文件
        finishDel: false, // 是否在上传文件完成后删除预览
        close: true,
        fileTypeFilter: options.fileTypeFilter,
        remark:options.remark,
        isDownModuleFile: true,
        downModuleFileUrl: options.downModuleFileUrl,
        /* 外部获得的回调接口 */
        onSelect: function (selectFiles, files) {            
        },
        onSelect: function (selectFiles, files) {
        },
        onSuccess: function (file) {
        },
        onFailure: function (file) {
        },
        onComplete: function (responseInfo) { // 上传完成的回调方法
            var dataObj = JSON.parse(responseInfo);
            hideLoading(); 
            if (isServerResultDataPass(dataObj)) {
                correctNotification({ SimpleMessage: (lang=="en"?"Upload file Success":"上传成功") });
                var callback = options.callBackFun ? options.callBackFun : null;
                if ($.isFunction(callback)) {
                    callback(dataObj.resultDataFull);
                }
            } else {
            	FailResultDataToTip(dataObj);
            }
            $("#" + id).remove();
        }
    });
};
