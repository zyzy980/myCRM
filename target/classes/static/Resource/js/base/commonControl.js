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
			        return '<table style=\"width:100%;\"><tr><td  style=\"text-align:left;width:80%;\"><span >' + row[opts.textField] + '</span></td><td  style=\"text-align:right;width:20%;\"><img src=\"../../Resource/images/icons/refresh.gif\" style=\"display:none;\" onclick=\"reloadComboboxData(\''+id+'\')\"/></td></tr></table>';
			    } else {
			       return '<table style=\"width:100%;\"><tr><td  style=\"text-align:left;width:100%;\"><span >' + row[opts.textField] + '</span></td></tr></table>';
			    }			    
			}
			,filter: function(q, row){
				var opts = $(this).combobox('options');
				return row[opts.textField].toLowerCase().indexOf(q.toLowerCase()) != -1;
			}
		});		
	});
});


var getContentWindow = function(domObj){
	if(domObj == null){
		return null;
	}
	return domObj.contentWindow || (domObj.frameElement && domObj.frameElement.contentWindow) || null;
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
	var export_columnneed="";
	if(_options && _options.columnNeed) {
		export_columnneed=_options.columnNeed;
	}
	var url = _options.url;
    var httpUrl = "";
    var _this = this;
    url = location.pathname.substring(0,location.pathname.substring(1).indexOf("/") +1)+"/base/Base_DataController/exportExcelData";
    //加载 form 表单
    var html = '<form id="formExportFile" class="formExportFile" target="_blank" action="'+url+'" method="post" style="display: none">';
    html += '<input type="text" id="export_title" name="export_title" value="'+export_title+'" />'; //标题 -> 文件名
    html += '<input type="text" id="url" name="url" value="" />'; //标题 -> 文件名
    html += '<input type="text" id="FiledData" name="FiledData" value="" />'; //列英文名
    html += '<input type="text" id="Export_ColumnCode" name="Export_ColumnCode" value="" />'; //列英文名
    html += '<input type="text" id="Export_ColumnName" name="Export_ColumnName" value="" />';//列中文名
    html += '<input type="text" id="Export_ColumnWidth" name="Export_ColumnWidth" value="" />';//列宽度
    html += '<input type="text" id="Export_ColumnAlign" name="Export_ColumnAlign" value="" />';//列对齐方式
	html += '<input type="text" id="Export_ColumnNeed" name="Export_ColumnNeed" value="'+export_columnneed+'" />';//必填字段设置为红色字体
    html += '<input type="text" id="Export_customSearchFilters" name="Export_customSearchFilters" value="" />'; //查询条件
    html += '<input type="text" id="Export_KIND" name="Export_KIND" value="0" />';  // 0.以查询条件导出数据, 1.导出当前界面数据, 2.导出列表选中数据
    html += '<input type="text" id="Export_CurrentPageData" name="Export_CurrentPageData" value="" />'; //当前页数据
    html += '</form>';
    $("body").append(html);
    var html = "<table width='100%'>";
    
    
    var urlParms = getUrlParms();
    var exportTitle = "";
    var msg = "";
    var p1 = "";
    var p2 = "";
    var p3 = "";
    
    if(urlParms.lang == "en"){
    	exportTitle = "Export data";
    	msg = "<div style='margin-left:25px;margin-top:7px'>Are you sure about exporting data？（<font color='red'>Excel can export up to 65530 lines of data</font>）<div><br>";
    	p1 = "Export for query";
    	p2 = "Export for current page";
    	p3 = "Export for select";
    }else{
    	exportTitle = "导出数据";
    	msg = "<div style='margin-left:25px;margin-top:7px'>确定导出数据吗？（<font color='red'>Excel最大只能导出65530行数据</font>）<div><br>";
    	p1 = "以查询条件导出数据";
    	p2 = "导出当前页面的数据";
    	p3 = "导出列表选中的数据";
    }
    if(_options.type == "0"){
    	html += "<tr><td align='left' style='padding:4px 4px 4px 90px;'><input type='radio' name='ExportKind' id='ExportKind2' value='1' checked/><label for='ExportKind2'>"+p2+"</label></td></tr>";
    	html += "<tr><td align='left' style='padding:4px 4px 4px 90px;'><input type='radio' name='ExportKind' id='ExportKind3' value='2' /><label for='ExportKind3'>"+p3+"</label></td></tr>";

    }else{
    	html += "<tr><td align='left' style='padding:4px 4px 4px 90px;'><input type='radio' name='ExportKind' id='ExportKind1' value='0' checked /><label for='ExportKind1'>"+p1+"</label></td></tr>";
    	html += "<tr><td align='left' style='padding:4px 4px 4px 90px;'><input type='radio' name='ExportKind' id='ExportKind2' value='1' /><label for='ExportKind2'>"+p2+"</label></td></tr>";
    	html += "<tr><td align='left' style='padding:4px 4px 4px 90px;'><input type='radio' name='ExportKind' id='ExportKind3' value='2' /><label for='ExportKind3'>"+p3+"</label></td></tr>";
        httpUrl = location.origin + location.pathname.substring(0,location.pathname.substring(1).indexOf("/") +1);
        var url = $(_this).jqGrid("getGridParam", "url");

        if(!url){
            alert("先进行查询操作后再导出");
            return;
		}
        while(/\.\.\//.test(url)){
			url = url.substring(3);
		}
        httpUrl = httpUrl + "/" + url;

        if(!(typeof _options.getSearchFilters == "function")){
            if(window.getSearchFilters){
                _options.getSearchFilters = window.getSearchFilters;
            }else{
                alert("必须指定getSearchFilters属性方法,且返回值类型为对象");
                return;
            }
        }

    }
    html += "</table>";
    

    $.messager.confirm({
    	title: exportTitle,
        msg: msg+html,
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
                

                var topWindow = window.top.frames[0];
                if (KIND == "1") {
                    //导出当前列表数据
                    var dataList = $(_this).jqGrid("getRowData");
                    if (dataList.length <= 0){
                        errorNotification({ SimpleMessage: topWindow.exportMessageFail , MoreMessage: topWindow.exportMessageEmptyValid });
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
                        errorNotification({ SimpleMessage: topWindow.exportMessageFail , MoreMessage: topWindow.exportMessageSelectValid });
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
                    
                	
                	var requestParam = JSON.parse(_options.getSearchFilters());//$(_this).jqGrid("getGridParam", "postData");
                   /* if(!(typeof requestParam ==  "object")){
                    	alert("查询条件返回值必须为对象类型");
                    	return;
                    }*/
                	
                    requestParam.sidx = $(_this).jqGrid("getGridParam", "sortname");
                    requestParam.sord = $(_this).jqGrid("getGridParam", "sortorder");
                    
                    
                    requestParam.page = 1;
                    requestParam.rows = 65530;
                   
                    //delete requestParam._search;
                    //delete requestParam.nd;
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
};

var reloadComboboxData=function(t) {
	$('#'+t).combobox('reload');
};
var enterTriggerEvent = function(rangeBoxName, eventButtonName) {
	var obj;
	if(typeof rangeBoxName == "string"){
		obj = $("#" + rangeBoxName + " input[type='text']");
	}else{
		obj = rangeBoxName.find("input[type='text']");
	}
	obj.each(function() {
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

var checkboxJqgrid = function(rowid,iCol,cellcontent,e, tableName){
	var gridTable = null;
	if(tableName == null){
		gridTable = $("#gridTable");
	}else{
		gridTable = $(tableName);
	}
	
	var v = gridTable.jqGrid("getGridParam","colModel")[iCol];
	if(v && v.editable){
		return;
	}
	
	/*if(e.toElement.localName!="input"&&e.toElement.childNodes[0].type!='checkbox'){
		   var classStr = "#jqg_gridTable_"+rowid;
		   var flag = $(classStr).get(0).checked;
		   $("#gridTable").jqGrid("resetSelection");
     	if(flag)
     	$("#gridTable").jqGrid('setSelection',rowid);
	   } */
	if(!e.ctrlKey && !e.shiftKey){
		  if(iCol!=1){
     		gridTable.jqGrid('resetSelection');
     		gridTable.jqGrid('setSelection',rowid);
     	 } 
		}
  	if(e.shiftKey){
  	var selectRowItems = gridTable.jqGrid("getGridParam", "selarrrow"); 
  	 var rowData = gridTable.jqGrid('getDataIDs'); 
  	 
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
				gridTable.jqGrid('setSelection',rowData[i]);
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
			
			var currentLocation = location.href;
			
			if(currentLocation.indexOf("Login.html")!=-1){
				url_login = "/vehicle_js/Index/Login.html?lang="+lang;
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
	
	if (serverResultData.resultCode == 6) {
			var lang=GetLang();
			var alert_tit="操作提示";
			var alert_text="你没有权限访问此页面，请联系管理员。";
			if(lang=="en")
			{
				alert_tit="alert";
				alert_text="You do not have permission to access this Page.";
			}

			try
			{
				window.top.$.messager.alert(alert_tit, alert_text, "error", function () {});
			} 
			catch (e)
			{
				alert(alert_text);
			}
			
			try
			{
				closeCurrentTab();
			}catch (e2)
			{
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


        if(typeof paramesList[i] == "object" && paramesList[i].length){
            //or操作
            var arr = [];
            for(var j = 0; j < paramesList[i].length; j++){

                var vo = paramesList[i][j];
                var name = vo.name;
                var value = $.trim(vo.value);
                var op = vo.op;
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
                arr.push(parameObj);
            }
            if(arr.length){
                paramesJson.rules.push(arr);
            }
        }else{
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
var toggleGridSearchToolbar = function(id,options) {
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
					if(null!=options)
					{
						if ($.isFunction(options.callback)) 
						{
			                   options.callback();
			            }
					}
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
		$("#gbox_"+id+" .ui-search-toolbar select,#gbox_"+id+" .ui-search-toolbar input[type='text']").val("");
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
	//var h=$("#gridTable").height()+40;
	var h=$("#gridTable").jqGrid('getGridParam','height')-40;
	$('#ColTbl_gridTable').css('max-height' , h+"px");//$(window).height() - 160);
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

/** 
 * 显示日志功能列表界面 
 * options={id:"如果为null,默认toolbar",operateModuleGlobal:"模块管理-查询列表数据",moduleId:"模块ID"}
 * **/
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
	//message.AutoHide=true;
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


var close = function () {
    if(/^iframe/.test($(window.frameElement).parent().find(">iframe").attr("id"))){
        closeDialog($(window.frameElement).parent().attr("id"));
    }else{
        closeCurrentTab();
    }
};

/** (top frame)全屏遮罩层加载loading * */
var showLoading = function(message) {
	window.top.startLoading(message);
}

/** (top frame)全屏遮罩层隐藏loading * */

var hideLoading = function() {
	try
	{
		window.top.hideLoading();
	} 
	catch (e)
	{
		// TODO: handle exception
	}
	
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
			dicText_zh_en=formatParam.data[i].dictext;
		
		listHtml += "<input type=\"radio\" data-default=\""
				+ formatParam.data[i].isDefault + "\" name=\""
				+ formatParam.bindControlPrefix + "\" value='"
				+ formatParam.data[i].dicvalue + "' id=\""
				+ formatParam.bindControlPrefix + "_"
				+ formatParam.data[i].dicvalue + "\"/>&nbsp;<label for=\""
				+ formatParam.bindControlPrefix + "_"
				+ formatParam.data[i].dicvalue + "\">"
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
 
/**
 * 1、引用样式			<link type="text/css" rel="stylesheet" href="../../Resource/css/index/timeline.css" />
 * 2、列表:			{ name:  "log_date", index:  "log_date", align: "left", width: 120, type: "string", search: true, frozen: false, searchoptions: { sopt: ["cn", "eq", "ne"]}
 * 	                 ,formatter: function (value, grid, rows) {
 * 	                     var detail = ShowTipDetail(rows);
 * 	                     return "<a href=\"javascript:void(0)\" class=\"gridViewPicLink\"  title=\"" + detail + "\" style=\"background:none;width:100%;line-height:23px;color:blue;\">"+rows.log_date+"</a>";
 * 	                 }
 *                  },
 * 3、json=[{log_date:'2018-07-19',log_time:'10:20',log_subject:'1602311订车',log_body:'名称XXXX内容'}]
 * */
var ShowTipDetail = function (json) {
	var html = "<div class=\'gridDetailTable\'>";
	html+="<div class=\'content\'>";
	html+="<article>";
	var point_green=" point-green";
	var firstColor=" firstColor";
	for(var i=0,ilen=json.length;i<ilen;i++)
	{
		if(i!=0)
		{
			point_green="";
			firstColor="";
		}
 
		html+="<section>";
		html+="<span class=\'point-time"+point_green+"\'></span>";
		html+="<time datetime=\'\'>";
		html+="		<span class=\'"+firstColor+"\'>"+json[i].log_date+"</span>";
		html+="		<span class=\'"+firstColor+"\'>"+json[i].log_time+"</span>";
		html+="	</time>";
		html+="	<aside>";
		html+="		<p class=\'things"+firstColor+"\'>"+json[i].log_subject+"</p>";
		html+="		<p class=\'brief\'><span class=\'text-green"+firstColor+"\'>"+json[i].log_body+"</span></p>";
		html+="	</aside>";
		html+="</section>";

	}
	html += "</article>";
	html += "</div>";
	html += "<div class=\'clearfix\'></div>";
	html += "</div>";
	return html;
	
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

// 操作日志
// var operateModuleGlobal = '';// 操作模块名称（全局）
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




//城市选择框显示
var inCity = new Object();
inCity.cssParents = function() { //城市显示
	inCity.top = placeThis.offset().top + 27; //城市选择框  top位置
	inCity.left = placeThis.offset().left; //城市选择框  left位置
	$(inCity.id).css({
		"top" : inCity.top + "px",
		"left" : inCity.left + "px",
		"width" : inCity.width + "px",
		"height" : inCity.height + "px"
	});
}
inCity.place = function(e) {
	e.click(function() {
		placeThis = $(this);
		//城市显示
		inCity.cssParents();
		$(inCity.id).show();
		return false;
	})
}
inCity.destination = function(e) {
	e.click(function() {
		placeThis = $(this);
		//城市显示
		inCity.cssParents();
		$(inCity.id).show();
		return false;
	})
}

/* 城市HTML模板 */
inCity._template = [ '<h1>请选择城市</h1>', '<div class="screen">',
		'<a href="javascript:void(0)" class="shar">热门</a>',
		'<a href="javascript:void(0)">ABCDEFG</a>',
		'<a href="javascript:void(0)">HIJKL</a>',
		'<a href="javascript:void(0)">MNOPQRST</a>',
		'<a href="javascript:void(0)">WXYZ</a>', '</div>',
		'<div class="city_pos">', '<div class="city_a_le1">', '</div>',
		'<div class="city_a_le1" style="display: none">', '</div>',
		'<div class="city_a_le1" style="display: none">', '</div>',
		'<div class="city_a_le1" style="display: none">', '</div>',
		'<div class="city_a_le1" style="display: none">', '</div>', '</div>' ];
/* 所有城市数据,可以按照格式自行添加（北京|beijing|bj），前16条为热门城市 */

inCity.allCity = ['所有|all|a', '北京|beijing|bj', '上海|shanghai|sh', '重庆|chongqing|cq',
		'深圳|shenzhen|sz', '广州|guangzhou|gz', '杭州|hangzhou|hz', '南京|nanjing|nj',
		'苏州|shuzhou|sz', '天津|tianjin|tj', '成都|chengdu|cd', '南昌|nanchang|nc',
		'三亚|sanya|sy', '青岛|qingdao|qd', '厦门|xiamen|xm', '西安|xian|xa',
		'长沙|changsha|cs', '合肥|hefei|hf', '西藏|xizang|xz', '内蒙古|neimenggu|nmg',
		'安庆|anqing|aq', '阿泰勒|ataile|atl', '安康|ankang|ak', '阿克苏|akesu|aks',
		'包头|baotou|bt', '北海|beihai|bh', '百色|baise|bs', '保山|baoshan|bs',
		'长治|changzhi|cz', '长春|changchun|cc', '常州|changzhou|cz',
		'昌都|changdu|cd', '朝阳|chaoyang|cy', '常德|changde|cd',
		'长白山|changbaishan|cbs', '赤峰|chifeng|cf', '大同|datong|dt',
		'大连|dalian|dl', '达县|daxian|dx', '东营|dongying|dy', '大庆|daqing|dq',
		'丹东|dandong|dd', '大理|dali|dl', '敦煌|dunhuang|dh', '鄂尔多斯|eerduosi|eeds',
		'恩施|enshi|es', '福州|fuzhou|fz', '阜阳|fuyang|fy', '贵阳|guiyang|gy',
		'桂林|guilin|gl', '广元|guangyuan|gy', '格尔木|geermu|gem',
		'呼和浩特|huhehaote|hhht', '哈密|hami|hm', '黑河|heihe|hh', '海拉尔|hailaer|hle',
		'哈尔滨|haerbin|heb', '海口|haikou|hk', '黄山|huangshan|hs', '邯郸|handan|hd',
		'汉中|hanzhong|hz', '和田|hetian|ht', '晋江|jinjiang|jj', '锦州|jinzhou|jz',
		'景德镇|jingdezhen|jdz', '嘉峪关|jiayuguan|jyg', '井冈山|jinggangshan|jgs',
		'济宁|jining|jn', '九江|jiujiang|jj', '佳木斯|jiamusi|jms', '济南|jinan|jn',
		'喀什|kashi|ks', '昆明|kunming|km', '康定|kangding|kd', '克拉玛依|kelamayi|klmy',
		'库尔勒|kuerle|kel', '库车|kuche|kc', '兰州|lanzhou|lz', '洛阳|luoyang|ly',
		'丽江|lijiang|lj', '林芝|linzhi|lz', '柳州|liuzhou|lz', '泸州|luzhou|lz',
		'连云港|lianyungang|lyg', '黎平|liping|lp', '连成|liancheng|lc', '拉萨|lasa|ls',
		'临沧|lincang|lc', '临沂|linyi|ly', '芒市|mangshi|ms', '牡丹江|mudanjiang|mdj',
		'满洲里|manzhouli|mzl', '绵阳|mianyang|my', '梅县|meixian|mx', '漠河|mohe|mh',
		'南充|nanchong|nc', '南宁|nanning|nn', '南阳|nanyang|ny', '南通|nantong|nt',
		'那拉提|nalati|nlt', '宁波|ningbo|nb', '攀枝花|panzhihua|pzh', '衢州|quzhou|qz',
		'秦皇岛|qinhuangdao|qhd', '庆阳|qingyang|qy', '齐齐哈尔|qiqihaer|qqhe',
		'石家庄|shijiazhuang|sjz', '沈阳|shenyang|sy', '思茅|simao|sm',
		'铜仁|tongren|tr', '塔城|tacheng|tc', '腾冲|tengchong|tc', '台州|taizhou|tz',
		'通辽|tongliao|tl', '太原|taiyuan|ty', '威海|weihai|wh', '梧州|wuzhou|wz',
		'文山|wenshan|ws', '无锡|wuxi|wx', '潍坊|weifang|wf', '武夷山|wuyishan|wys',
		'乌兰浩特|wulanhaote|wlht', '温州|wenzhou|wz', '乌鲁木齐|wulumuqi|wlmq',
		'万州|wanzhou|wz', '乌海|wuhai|wh', '兴义|xingyi|xy', '西昌|xichang|xc',
		'襄樊|xiangfan|xf', '西宁|xining|xn', '锡林浩特|xilinhaote|xlht',
		'西双版纳|xishuangbanna|xsbn', '徐州|xuzhou|xz', '义乌|yiwu|yw',
		'永州|yongzhou|yz', '榆林|yulin|yl', '延安|yanan|ya', '运城|yuncheng|yc',
		'烟台|yantai|yt', '银川|yinchuan|yc', '宜昌|yichang|yc', '宜宾|yibin|yb',
		'盐城|yancheng|yc', '延吉|yanji|yj', '玉树|yushu|ys', '伊宁|yining|yn',
		'珠海|zhuhai|zh', '昭通|zhaotong|zt', '张家界|zhangjiajie|zjj',
		'舟山|zhoushan|zs', '郑州|zhengzhou|zz', '中卫|zhongwei|zw',
		'芷江|zhijiang|zj', '湛江|zhanjiang|zj' ];

/* 正则表达式 筛选中文城市名、拼音、首字母 */

inCity.regEx = /^([\u4E00-\u9FA5\uf900-\ufa2d]+)\|(\w+)\|(\w)\w*$/i;
inCity.regExChiese = /([\u4E00-\u9FA5\uf900-\ufa2d]+)/;

(function() {
	var citys = inCity.allCity, match, letter, regEx = inCity.regEx, reg2 = /^[a-g]$/i, reg3 = /^[h-l]$/i, reg4 = /^[m-t]$/i, reg5 = /^[w-z]$/i;
	if (!inCity.oCity) {
		inCity.oCity = {
			hot : {},
			ABCDEFG : {},
			HIJKL : {},
			MNOPQRST : {},
			WXYZ : {}
		};
		//console.log(citys.length);
		for (var i = 0, n = citys.length; i < n; i++) {
			match = regEx.exec(citys[i]); //exec
			letter = match[3].toUpperCase(); //转换字母为大写

			if (reg2.test(letter)) { //test检测一个字符串是否匹配某个模式
				if (!inCity.oCity.ABCDEFG[letter])
					inCity.oCity.ABCDEFG[letter] = [];
				inCity.oCity.ABCDEFG[letter].push(match[1]);
			} else if (reg3.test(letter)) {
				if (!inCity.oCity.HIJKL[letter])
					inCity.oCity.HIJKL[letter] = [];
				inCity.oCity.HIJKL[letter].push(match[1]);
			} else if (reg4.test(letter)) {
				if (!inCity.oCity.MNOPQRST[letter])
					inCity.oCity.MNOPQRST[letter] = [];
				inCity.oCity.MNOPQRST[letter].push(match[1]);
			} else if (reg5.test(letter)) {
				if (!inCity.oCity.WXYZ[letter])
					inCity.oCity.WXYZ[letter] = [];
				inCity.oCity.WXYZ[letter].push(match[1]);
			}
			/* 热门城市 前16条 */
			if (i < 16) {
				if (!inCity.oCity.hot['hot'])
					inCity.oCity.hot['hot'] = [];
				inCity.oCity.hot['hot'].push(match[1]);
			}
		}
	}
})();
//热门城市
inCity.Hot = function(cityA) {
	var ckey, odda, sortKey, str, odda = [], abc = [], key, regEx = inCity.regEx, oCity = inCity.oCity, len, leni;
	for (key in oCity) {
		sortKey = [];
		for (ckey in oCity[key]) {
			sortKey.push(ckey);
			// ckey按照ABCDEDG顺序排序
			sortKey.sort();
		}
		for (var j = 0, k = sortKey.length; j < k; j++) {
			odda = [];
			abc = [];
			for (var i = 0, n = oCity[key][sortKey[j]].length; i < n; i++) {

				if (key == 'hot') {
					$(inCity.id).find(inCity.Children).eq(0).append(
							'<a href="javascript:void(0)">'
									+ oCity[key][sortKey[j]][i] + '</a>');
					odda.push(str);
				} else {
					str = '<a href="javascript:void(0)">'
							+ oCity[key][sortKey[j]][i] + '</a>';
					inCity.arrRepeat(abc, sortKey, j); //获取字母
					odda.push(str);
					len = n;
					leni = i;
				}

			}
			inCity.cityPinyin(leni, len, key, abc, odda);
		}
	}
}

//按拼音排序
inCity.cityPinyin = function(leni, len, key, abc, odda) {
	if (leni != undefined && key != 'hot') {
		if (len - 1 == leni) {
			var one;
			switch (key) {
			case 'ABCDEFG':
				one = 1;
				break;
			case 'HIJKL':
				one = 2;
				break;
			case 'MNOPQRST':
				one = 3;
				break;
			case 'WXYZ':
				one = 4;
				break;
			}
			$(inCity.id).find(inCity.Children).eq(one).append(
					'<div class="Letter">' + '<span>' + abc[0] + '</span>'
							+ '<div>' + odda.join('') + '</div>' + '</div>');
		}
	}
}

//数组去重
inCity.arrRepeat = function(abc, sortKey, j) {
	var nab = sortKey[j];
	for ( var i in abc) {
		if (abc[i] == nab) {
			return nab = 1;
		}
	}
	if (nab != 1) {
		abc.push(sortKey[j])
	}
}
//城市切换
inCity.payment = function($this) {
	var ind = $this.index();
	$this.siblings().removeClass("shar");
	$this.addClass("shar");
	$this.parent().next().children().hide();
	$this.parent().next().children().eq(ind).show();
}
//给input赋值
inCity.cityClick = function($this) {
	$(".city_a_le1 a").click(function() {
		var a_city = $(this).text(); //当前选择的城市
		$(inCity.id).hide(); //隐藏城市选择框 
		
		if($(placeThis).is("[role=textbox]")){
			$(placeThis).val(a_city);//赋值

		}else{
			$(placeThis).parent().prev().textbox("setValue", a_city);//赋值
			//.val(a_city);  
			searchData();
			
		}
		return false;
	})
}
var loseGridFocus = function(){
    $(".ui-jqgrid-btable").loseGridFocus();
}

// 物料号输入框获取光标事件
var commonFocus = function(myid,title,remark) {
    // 获取原输入框数据并解析显示
	var myids = $("#" + myid);
    var oldVal = myids.val();
    var showVal = '';
    if (oldVal && oldVal.length > 0) {
        var oldVals = oldVal.split(';');
        if (oldVals && oldVals.length > 0) {
            for (var i = 0; i < oldVals.length; i++) {
                if ($.trim(oldVals[i]) && $.trim(oldVals[i]).length > 0) {
                    showVal += ($.trim(oldVals[i]) + '\n')
                }
            }
        }
    }
    // 显示弹出层（填充解析后的原输入框数据）
    var html = '<span style="color:#666;display:inline-block;margin-bottom:5px;">'+remark+'</span>';
    html += '<textarea id="textareaCommon" name="textareaCommon" rows="9" cols="3" style="width:calc(99% - 2px);border:1px solid #C2D3E9;">' + showVal + '</textarea>';
    $.messager.defaults = {
        ok: "确认",
        cancel: "取消" ,
        draggable:true,
        modal:true,
        width:'250px',
        height:'150px',
        overflow:'none',
        left: '30%',
        right: '30%',
    };
    $.messager.confirm({
        title: title,
        msg: html,
        width: 380,
        height: 256,
        fn: function (r) {
            if (r) {
                // 将新填写的数据解析后填入原输入框数
                var newVal = $('#textareaCommon').val();
                var matnrVal = '';
                if (newVal && newVal.length > 0) {
                    matnrVal = newVal.split('\n').join(';');
                }
                var sub=matnrVal.substr(matnrVal.length-1,1)
                var sub1=matnrVal.substr(matnrVal.length-2,1)
                var sub2=matnrVal.split(';')[1]
                if(sub==";" && sub1==";"&& sub2==""){
                    matnrVal =matnrVal.split(';')[0];
                }else if(sub==";"&& sub2!=";"){
                    matnrVal =matnrVal.substring(0,matnrVal.lastIndexOf(";"));
                }else{
                    matnrVal
                }
                myids.textbox('setValue', matnrVal);
                searchData();// 查询
            }else{
                myids.textbox('setValue','');
                searchData();
            }
        }
    });
    $('.messager-icon').hide();
    $("#textareaCommon").focus();
};
