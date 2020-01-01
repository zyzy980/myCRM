/** 获取当前session对象相关值* */
var getSession = function(levelPath) {
	var resultJson;
	var serverUrl = "../../sysInfo/user/getSession?" + Math.random();
	$.ajax({
		type : "POST",
		url : serverUrl,
		async : false,
		dataType : "json",
		success : function(json) {
			if (isServerResultDataPass(json)) {
				resultJson = json;
			}else{
				FailResultDataToTip(json);
			}
		}
	});

	return resultJson;
}

var FormUtils = {
		// 表单对象专json格式对象
		toJSONObject : function(queryForm) {
			var form = $(queryForm);
			var formObject = {};
			if (!form) {
				return formObject;
			}
			if (form.length > 0) {
				form = form.eq(0);
			}
			var dataArray = form.serializeArray();
			for ( var i = 0; i < dataArray.length; i++) {
				formObject[dataArray[i].name] = dataArray[i].value;
			}
			return formObject;
		}
}

/**
 * 克隆对象数据
 * 
 * @param data
 *            对象
 */
var clone = function(data) {;
	return JSON.parse(JSON.stringify(data));
}


function formatDate(date) {
	var seperator1 = "-";
	var year = date.getFullYear();
	var month = date.getMonth() + 1;
	var strDate = date.getDate();
	if (month >= 1 && month <= 9) {
		month = "0" + month;
	}
	if (strDate >= 0 && strDate <= 9) {
		strDate = "0" + strDate;
	}
	var currentdate = year + seperator1 + month + seperator1 + strDate;
	return currentdate;
}

/* 读取系统字典数据（1：直接从数据库中读取-database；2：直接从静态变量的缓存中读取-cache） */
var getDictionaryData = function(p) {
    if (!p) {
        return;
    }
    if((typeof p) != "object" || p.length == null) {
    	//如果单个对象，转换成数组
        p = [p];
    }
    var SYS_DICTIONARY_DATA = /*JSON.parse(*/window.top.SYS_DICTIONARY_DATA_JSON//);
	console.log(SYS_DICTIONARY_DATA);
	for(var i = 0; i < p.length; i++){
        var dataList = clone(SYS_DICTIONARY_DATA[p[i].dicTypeCode]);
        if(p[i].ADD_ALL){
            dataList.unshift({
				dicvalue: "",
				dictext: "所有"
			});
		}

		p[i].callback(dataList)
	}
}

var getBaseData = function(p) {
    if (!p) {
        return;
    }
    if((typeof p) != "object" || p.length == null) {
        p = [p];
    }
    var requestParam = [];
    for(var i = 0; i < p.length; i++){
        requestParam.push(p[i].dicTypeCode);
	}
    $.ajax({
        url: "../../base/Base_DataController/getBaseData",
		type: "POST",
		data: JSON.stringify(requestParam),
        contentType: "application/json",
        success: function (dataObj) {
            if (isServerResultDataPass(dataObj)) {
				var dataMap = dataObj.resultDataFull;
                for(var i = 0; i < p.length; i++){
                    var dataList = clone(dataMap[p[i].dicTypeCode]);
                    p[i].callback(dataList)
                }
            }else{
                FailResultDataToTip(dataObj);
            }
        }
    });
}


var getCookieValue = function(key) {
	if (window.top.settlement_basedata == null) {
		window.top.settlement_basedata = {};
	}

	if (window.top.settlement_basedata[key] == null) {
		return "";
	}
	var cookieValue = window.top.settlement_basedata[key];
	return decodeURIComponent(cookieValue);
}

var setCookieValue = function(key, cookieValue) {
	if (window.top.settlement_basedata == null) {
		window.top.settlement_basedata = {};
	}
	cookieValue = encodeURIComponent(cookieValue);
	window.top.settlement_basedata[key] = cookieValue;
}

var getData_Whcenter = function(p) {
	// 仓储中心
	var key = "getData_Whcenter";
	var cookieValue = getCookieValue(key);
	var jsonData = null;
	if (cookieValue == "") {
		$.ajax({
			type : "POST",
			url : "../../bzbhInfo/zd_whcenter/finWhcenter?state=Y&"
					+ Math.random(),
			async : false,
			dataType : "json",
			success : function(json) {
				if (json.resultDataFull) {
					FailResultDataToTip(json);
				}
				jsonData = json;
			}
		});
		cookieValue = JSON.stringify(jsonData);
		setCookieValue(key, cookieValue);
	} else {
		cookieValue = decodeURIComponent(cookieValue);
		jsonData = JSON.parse(cookieValue);
	}

	if (p.kind != null) {
		var arr = [];

		for (var i = 0; i < jsonData.length; i++) {
			if (jsonData[i].kind == p.kind) {
				arr.push(jsonData[i]);
			}
		}
		p.callback(arr);
	} else {
		p.callback(jsonData);
	}

}

var getData_Zd_Owner = function(p) {
	// 提供商
	var key = "getData_Zd_Owner";
	var cookieValue = getCookieValue(key);
	var jsonData = null;
	if (cookieValue == "") {
		$.ajax({
			type : "POST",
			url : "../../bzbhInfo/zd_owner/findZdOwnerList?state=Y&"
					+ Math.random(),
			async : false,
			dataType : "json",
			success : function(json) {
				if (json.resultDataFull) {
					FailResultDataToTip(json);
				}
				jsonData = json;
			}
		});
		cookieValue = JSON.stringify(jsonData);
		setCookieValue(key, cookieValue);
	} else {
		cookieValue = decodeURIComponent(cookieValue);
		jsonData = JSON.parse(cookieValue);
	}

	if (p.kind != null) {
		var arr = [];

		for (var i = 0; i < jsonData.length; i++) {
			if (jsonData[i].kind == p.kind) {
				arr.push(jsonData[i]);
			}
		}
		p.callback(arr);
	} else {
		p.callback(jsonData);
	}

}

var getData_Zd_Package = function(p) {
	// 提供商
	var key = "getData_Zd_Package";
	var cookieValue = getCookieValue(key);
	var jsonData = null;
	if (cookieValue == "") {
		$.ajax({
			type : "POST",
			url : "../../bzbhInfo/zd_package/findZd_package?state=Y&"
					+ Math.random(),
			async : false,
			dataType : "json",
			success : function(json) {
				if (json.resultDataFull) {
					FailResultDataToTip(json);
				}
				jsonData = json;
			}
		});
		cookieValue = JSON.stringify(jsonData);
		setCookieValue(key, cookieValue);
	} else {
		cookieValue = decodeURIComponent(cookieValue);
		jsonData = JSON.parse(cookieValue);
	}
	p.callback(jsonData);
}

var getData_Package_noAll = function(p) {
	// 仓储中心
	var key = "getData_Package_noAll";
	var cookieValue = getCookieValue(key);
	var jsonData = null;
	if (cookieValue == "") {
		$.ajax({
			type : "POST",
			url : "../../bzbhInfo/zd_package/findZd_package?state=Y&"
					+ Math.random(),
			async : false,
			dataType : "json",
			success : function(json) {
				if (json.resultDataFull) {
					FailResultDataToTip(json);
				}
				jsonData = json;
			}
		});
		cookieValue = JSON.stringify(jsonData);
		setCookieValue(key, cookieValue);
	} else {
		cookieValue = decodeURIComponent(cookieValue);
		jsonData = JSON.parse(cookieValue);
	}
	p.callback(jsonData);

}

var getData_Area = function(p) {
	// 仓储中心-库区
	var key = "getData_Area";
	var cookieValue = getCookieValue(key);
	var jsonData = null;

	p.async = p.async || false;
	if (cookieValue == "" || p.reset == true) {

		if (p.whcenter == null) {
			// 库存中心编号
			p.whcenter = "";
		}

		var requestParam = {};
		requestParam.whcenter = p.whcenter;
		if (p.whcenter != null) {
			requestParam.kind = p.kind;
		}
		requestParam.state = "Y";
		$.ajax({
			type : "POST",
			url : "../../repair/area/finArea?" + Math.random(),
			async : p.async,
			data : requestParam,
			dataType : "json",
			success : function(json) {
				if (json.resultDataFull) {
					FailResultDataToTip(json);
				}
				jsonData = json;
				if (p.async == true) {
					p.callback(jsonData);
				}
			}
		});
		// cookieValue = JSON.stringify(jsonData);
		// setCookieValue(key, cookieValue);

	} else {
		// cookieValue = decodeURIComponent(cookieValue);
		// jsonData = JSON.parse(cookieValue);
	}
	if (p.async == false) {
		p.callback(jsonData);
	}
}

var getData_YwLocation = function(p) {
	// 仓储中心-库位
	var key = "getData_YwLocation";
	var cookieValue = getCookieValue(key);
	var jsonData = null;

	p.async = p.async || false;
	if (cookieValue == "" || p.reset == true) {

		if (p.aear_id == null) {
			// 库存中心编号
			p.aear_id = "";
		}
		var requestParam = {};

		if (p.aear_id != null && p.aear_id != '') {
			requestParam.aear_id = p.aear_id;
		}
		if (p.whcenter != null) {
			requestParam.whcenter = p.whcenter;
		}
		if (p.kind != null) {
			requestParam.kind = p.kind;
		}

		requestParam.state = "Y";
		$.ajax({
			type : "POST",
			url : "../sysInfo/user/findZd_WhcenterListByUser?"
					+ Math.random(),
			async : p.async,
			data : requestParam,
			dataType : "json",
			success : function(json) {
				if (json.resultDataFull) {
					FailResultDataToTip(json);
				}
				jsonData = json.resultDataFull;
				if (p.async == true) {
					p.callback(jsonData);
				}
			}
		});
		// cookieValue = JSON.stringify(jsonData);
		// setCookieValue(key, cookieValue);

	} else {
		// cookieValue = decodeURIComponent(cookieValue);
		// jsonData = JSON.parse(cookieValue);
	}
	if (p.async == false) {
		p.callback(jsonData);
	}
}

var getData_Seat = function(p) {
	// 仓储中心-库位
	var key = "getData_Seat";
	var cookieValue = getCookieValue(key);
	var jsonData = null;

	p.async = p.async || false;
	if (cookieValue == "" || p.reset == true) {

		if (p.aear_id == null) {
			// 库存中心编号
			p.aear_id = "";
		}
		var requestParam = {};

		if (p.aear_id != null && p.aear_id != '') {
			requestParam.aear_id = p.aear_id;
		}
		if (p.whcenter != null) {
			requestParam.whcenter = p.whcenter;
		}
		if (p.kind != null) {
			requestParam.kind = p.kind;
		}

		requestParam.state = "Y";
		$.ajax({
			type : "POST",
			url : "../../seat/findSeatByKq?" + Math.random(),
			async : p.async,
			data : requestParam,
			dataType : "json",
			success : function(json) {
				if (json.resultDataFull) {
					FailResultDataToTip(json);
				}
				jsonData = json;
				if (p.async == true) {
					p.callback(jsonData);
				}
			}
		});
		// cookieValue = JSON.stringify(jsonData);
		// setCookieValue(key, cookieValue);

	} else {
		// cookieValue = decodeURIComponent(cookieValue);
		// jsonData = JSON.parse(cookieValue);
	}
	if (p.async == false) {
		p.callback(jsonData);
	}
}
var getTopTenKuWei = function(p){
	var key = "getTopTenKuWei"; 
	var cookieValue = getCookieValue(key);
	var jsonData = null;
	if (cookieValue == "") {
		 $.ajax({
		    type: "POST",
			url: "../../admin/Base/getTopTenKuWei",
			async: false,
			dataType: "json",
			    success: function (json) {
			    	if(json.resultDataFull){
			    		FailResultDataToTip(json);
			    		return;
			    	}
			    	jsonData = json;
			    	
			    }
			});
		 cookieValue = JSON.stringify(jsonData);
		 setCookieValue(key, cookieValue);
     }else{
         cookieValue = decodeURIComponent(cookieValue);
    	 jsonData = JSON.parse(cookieValue);
     } 
     p.callback(jsonData);
}

var getData_CostList = function(p){
	
	var key = "getData_CostList"; 
	var cookieValue = getCookieValue(key);
	var jsonData = null;
	if (cookieValue == "") {
		 $.ajax({
		    type: "POST",
			url: "../../admin/Base/getCostList",
			async: false,
			dataType: "json",
			    success: function (json) {
			    	if(json.resultDataFull){
			    		FailResultDataToTip(json);
			    		return;
			    	}
			    	jsonData = json;
			    	
			    }
			});
		 cookieValue = JSON.stringify(jsonData);
		 setCookieValue(key, cookieValue);
     }else{
         cookieValue = decodeURIComponent(cookieValue);
    	 jsonData = JSON.parse(cookieValue);
     } 
     p.callback(jsonData);
}

var loseGridFocus = function(){
	$(".ui-jqgrid-btable").find(".edit-cell:has(input[type=text]),.edit-cell:has(select)").each(function(){
		var saveTr = $(this).parents("tr").index();
		var saveTd = $(this).index();
		$(this).parents(".ui-jqgrid-btable").jqGrid('saveCell', saveTr, saveTd);
		$(this).removeClass("edit-cell").removeClass("ui-state-highlight");
	});
};

var ComboboxDataSource=function(id,type,selval,callBackFun,disabled,SelectAll)
{
	if(undefined==SelectAll || null==SelectAll)
	{
		if(GetLang()=="en")
			SelectAll="select";
		else
			SelectAll="请选择";
	}
	var	opts = [{Value:'',Text:'--'+SelectAll+'--'}];
	//findBaseByType
	$.ajax({
 		url : "../../scts/basic/findDictionaryByType?type="+type+"&t=" + Math.random(),
 		data : null,
 		type : 'get',
 		async : false,
 		contentType : 'application/json;charset=utf-8',
 		success : function(datas) {
 			if (datas && datas.length > 0) {
    			for (var i = 0; i < datas.length; i++) {
    				opts.push({ Value : datas[i].code, Text : datas[i].code });
       			}
            }

 			if(null!=id && undefined!=id && id!="")
 			{
	 	    	$('#'+id).combobox({ data : opts, valueField : 'Value', textField : 'Text', editable : false});
	 	    	
	 	    	 
		 	    $('#'+id).combobox({
		 	    		onSelect: function (n,o) 
		 	    		{
			                if ($.isFunction(callBackFun)) {
			                	callBackFun(this);
			                }
		 	    		}
		 	    });
		 	   $('#'+id).combobox('setValue', selval);
		 	   
		 	   if(undefined==disabled || disabled==null)
					disabled=false;
		 	   
		 	   if(disabled==true)
		 	   { 
		 		   $('#'+id).combobox('disable');
		 	   }
 		   }
	 
		}
	});
	return opts;
}

var Zd_LocationComboboxDataSource=function(options)
{
	var kind="PLANT";
	if(options.kind==undefined || options.kind==null || options.kind=="")
	{
		kind="PLANT";
	}
	else
	{
		kind=options.kind;
	}
	var vo={};//{"ex_code":options.code};
	$.ajax({
 		url : "../../jcda/Zd_Location/findLocationByKind?t=" + Math.random()+ "&kind="+kind,
 		data : null,
 		type : 'get',
 		async : false,
 		contentType : 'application/json;charset=utf-8',
 		success : function(dataObj) {
 			var datas=dataObj;//dataObj.resultDataFull;
 			if(undefined==options.SelectAll || null==options.SelectAll)
 			{
 				if(GetLang()=="en")
 					options.SelectAll="select";
 				else
 					options.SelectAll="请选择";
 			}
 			var opts =	opts = [ { Value : '', Text : '--'+options.SelectAll+'--'} ];
 			if (datas && datas.length > 0) {
    			for (var i = 0; i < datas.length; i++) {
    				opts.push({ Value : datas[i].ex_code, Text : datas[i].name });
       			}
            }
 	    	$('#'+options.id).combobox({ data : opts, valueField : 'Value', textField : 'Text', editable : false});
 	    	
	 	    	$('#'+options.id).combobox({
	 	    		onSelect: function (n,o)
	 	    		{
	 	    			var callback = options.callBackFun ? options.callBackFun : null;
		                if ($.isFunction(callback)) {
		                   callback(this);
		                }
	 	    		}
	 	    	});
	 	    	$('#'+options.id).combobox('setValue', options.selval);
		}
	});
}

var DirectoryDataListComboboxDataSource=function(options)
{
	
	$.ajax({
 		url : "../../sysInfo/dictionaryData/getDictionaryDataListCache?t=" + Math.random(),
 		data : {dicTypeCode : options.dicTypeCode},
 		async : false,
 		dataType : 'json',
 		contentType : 'application/json;charset=utf-8',
 		success : function(dataObj) {
 		 
 			if (isServerResultDataPass(dataObj)) {
 			
	 			var datas=dataObj.resultDataFull;
	 			if(undefined==options.SelectAll || null==options.SelectAll)
	 			{
	 				if(GetLang()=="en")
	 					options.SelectAll="select";
	 				else
	 					options.SelectAll="请选择";
	 			}
	 			var opts = [ { Value : '', Text : '--'+options.SelectAll+'--'} ];
	 			if (datas && datas.length > 0) {
	    			for (var i = 0; i < datas.length; i++) {
	    				opts.push({ Value : datas[i].dicValue, Text : datas[i].dicValue+"-"+datas[i].dicText});
	       			}
	            }
	 	    	$('#'+options.id).combobox({ data : opts, valueField : 'Value', textField : 'Text', editable : false});
	 	    	
	 	    	 
		 	    	$('#'+options.id).combobox({
		 	    		onSelect: function (n,o) 
		 	    		{
		 	    			var callback = options.callBackFun ? options.callBackFun : null;
			                if ($.isFunction(callback)) {
			                   callback(this);
			                }
		 	    		}
		 	    	});
		 	    	$('#'+options.id).combobox('setValue', options.selval);
 	    	
 			} else {
				FailResultDataToTip(dataObj);
			}
		}
	});
}


var Zd_LocationpoeComboboxDataSource=function(options)
{
	if(undefined==options.SelectAll || null==options.SelectAll)
	{
			if(GetLang()=="en")
				options.SelectAll="select";
			else
				options.SelectAll="请选择";
	}
	var opts =[ { Value : '', Text : '--'+options.SelectAll+'--'} ];
	var vo=null;//{"ex_code":options.code};
	var parm="";
	if(undefined!=options.kind && null!=options.kind)
	{
		parm="&kind="+options.kind;
	}
	else
	{
		parm="&kind=PORT";
	}
	
	$.ajax({
 		url : "../../jcda/Zd_Location/findLocationByKind?t=" + Math.random()+parm,
 		data : null,
 		type : 'get',
 		async : false,
 		contentType : 'application/json;charset=utf-8',
 		success : function(dataObj) {
 			var datas=dataObj;//dataObj.resultDataFull;
 			if (datas && datas.length > 0) {
    			for (var i = 0; i < datas.length; i++) {
    				opts.push({ Value : datas[i].ex_code, Text : datas[i].name });
       			}
            }
            if(null!=options.id && options.id!=undefined && options.id!="")
            {
	 	    	$('#'+options.id).combobox({ data : opts, valueField : 'Value', textField : 'Text', editable : false});
	 	    	
		 	    	$('#'+options.id).combobox({
		 	    		onSelect: function (n,o)
		 	    		{
		 	    			var callback = options.callBackFun ? options.callBackFun : null;
			                if ($.isFunction(callback)) {
			                   callback(this);
			                }
		 	    		}
		 	    	});
		 	    $('#'+options.id).combobox('setValue', options.selval);
	 	    }
 	      
		}
	});
	return opts;
}

DateUtils = {
	dateFormat : function(obj, format) {
		
		if (typeof obj == "object") {
			var o = "";
			try {
				o = {
						"M+" : obj.getMonth() + 1, // 月份
						"d+" : obj.getDate(), // 日
						"h+" : obj.getHours(), // 小时
						"m+" : obj.getMinutes(), // 分
						"s+" : obj.getSeconds(), // 秒
						"q+" : Math.floor((obj.getMonth() + 3) / 3), // 季度
						"S" : obj.getMilliseconds()
						// 毫秒
				};
			} catch (e) {
				console.error(e);
				return "";
			}
			if(!format){
				format = this.YYYYMMDD;
			}
			var fmt = format;
			if (/(y+)/.test(fmt))
				fmt = fmt.replace(RegExp.$1, (obj.getFullYear() + "")
						.substr(4 - RegExp.$1.length));
			
			for ( var k in o) {
				
				if (new RegExp("(" + k + ")").test(fmt)) {
					fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
				}
			}
			return fmt;
		}
		
		if(/^(\d+)-(\d{1,2})-(\d{1,2})$/.test(obj)){
			var reg = /^(\d+)-(\d{1,2})-(\d{1,2})$/; // (\d+)-(\d{1,2})-(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})
			var r = obj.match(reg);
			if (r == null)
				return "";
			r[2] = r[2] - 1;
			var d = new Date(r[1], r[2], r[3]);
			if (d.getFullYear() != r[1])
				return "";
			if (d.getMonth() != r[2])
				return "";
			if (d.getDate() != r[3])
				return "";
			// if(d.getHours()!=r[4]) return "";
			// if(d.getMinutes()!=r[5]) return "";
			// if(d.getSeconds()!=r[6]) return "";
			return obj;
		}else if(/^(\d+)-(\d{1,2})-(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/.test(obj)){
			var reg = /^(\d+)-(\d{1,2})-(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/; // (\d+)-(\d{1,2})-(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})
			var r = obj.match(reg);
			if (r == null)
				return "";
			r[2] = r[2] - 1;
			var d = new Date(r[1], r[2], r[3], r[4], r[5], r[6]);
			if (d.getFullYear() != r[1])
				return "";
			if (d.getMonth() != r[2])
				return "";
			if (d.getDate() != r[3])
				return "";
			if(d.getHours()!=r[4]) return "";
			if(d.getMinutes()!=r[5]) return "";
			if(d.getSeconds()!=r[6]) return "";
			return obj;
		}
		return "";
	}
}

var ComboboxUtils = {
    jqGrid_DefaulDataInit : function(elem){
        if(this == ComboboxUtils){
            return function(elemObj){
                var options = elem;
                var obj = {};
                if(options.data){
                    obj.data = options.data;
                }
                if(options.valueField){
                    obj.valueField = options.valueField;
                }
                if(options.textField){
                    obj.textField = options.textField;
                }
                $(elemObj).combobox(obj);
                setTimeout(function () {
                    var temp_obj = {
                        onSelect:function(row){
                            this.value = $(this).combobox("getValue");
                            if(options.onSelect){
                                options.onSelect.call(this,row);
                            }
                        }
                    };
                    if(options.width != null){
                        temp_obj.width = options["width"];
                    }
                    $(elemObj).combobox(temp_obj);

                    if(window.defaultCloseCommon == null){
                        $(elemObj).combobox('showPanel');
                        var obj = $(elemObj);
                        if(obj.is("select")){
                            obj = obj.next().find(".textbox-text");
                        }else{
                            obj = obj.prev();
                        }
                        obj.select();
                    }else{
                        window.defaultCloseCommon = null;
                    }
                    $(elemObj).next().find(".textbox-text")[0].onchange = function(){
                        var arr = $(elemObj).find("option");
                        for(var i =0; i < arr.length; i++){
                            if(arr.eq(i).text() == this.value){
                                elemObj.value = arr[i].value;
                                break;
                            }
                        }
                    };
                },0);
            };
        }
        $(elem).combobox({
        });
        setTimeout(function () {
            $(elem).combobox({
                onSelect:function(){
                    this.value = $(this).combobox("getValue");
                }
            });
            if(window.defaultCloseCommon == null){
                $(elem).combobox('showPanel');
                var obj = $(elem);
                if(obj.is("select")){
                    obj = obj.next().find(".textbox-text");
                }else{
                    obj = obj.prev();
                }
                obj.select();
            }else{
                window.defaultCloseCommon = null;
            }

            $(elem).next().find(".textbox-text")[0].onchange = function(){
                var arr = $(elem).find("option");
                for(var i =0; i < arr.length; i++){
                    if(arr.eq(i).text() == this.value){
                        elem.value = arr[i].value;
                        break;
                    }
                }
            };
        }, 0);
    }
};


function myformatter(date){
    var y = date.getFullYear();
    var m = date.getMonth()+1;
    var d = date.getDate();
    return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d);
}
        
function myparser(s){
    if (!s) return new Date();
    var ss = (s.split('-'));
    var y = parseInt(ss[0],10);
    var m = parseInt(ss[1],10);
    var d = parseInt(ss[2],10);
    if (!isNaN(y) && !isNaN(m) && !isNaN(d)){
        return new Date(y,m-1,d);
    } else {
        return new Date();
    }
}
var defaultSelect = function (){
    $(this).combobox("setValue", "");
}

function isInArray(arr,value){
    for(var i = 0; i < arr.length; i++){
        if(value === arr[i].lifnr){
            return true;
        }
    }
    return false;
}
/**下拉框多选-获取值
 * obj--combobox的id
 * index--后台字段
 * */
var getMultipleComboVal = function(obj,index) {
    var arrs = [];
    var vals = obj.combobox('getValues');
    if (vals && vals.length > 0) {
        for (var i = 0; i < vals.length ; i++) {
            arrs.push({ name: index, value: vals[i], op: 'cn'});
        }
    }
    return arrs;
};

/***
 * 获取两位小数，保留2位
 */
function keepTwoPointData(t) {
    return Math.round(t*100)/100;
}

function keepThreePointData(t) {
    return Math.round(t*1000)/1000;
}
