
function getAbsPoint(e)
{
    var x = e.offsetLeft;
    var y = e.offsetTop;
    while(e = e.offsetParent)
    {
        x += e.offsetLeft;
        y += e.offsetTop;
    }
    return {'x': x, 'y': y};
};
CommonPageUtils = {
	
	bind : function(opt){
		if(!$(this).dropload){
			 $.ajax({  
			    url:"../js/dropload.min.js",  
			    async:false
		    });
		}
		var commonObj = opt.commonObj;

		if(commonObj.find(".dropload-down").length > 0){
			commonObj.unbind();
			$(opt.scrollArea).unbind();
			commonObj.find("*").unbind();

		}
		if(!opt.page){
			opt.page = 1;
		}
		if(!opt.rows){
			opt.rows = 15;
		}
		opt.getSearchFilters = opt.getSearchFilters || function(){return {};};
		if(opt.removeBeforeFun){
			$(opt.scrollArea).unbind();
			commonObj.unbind();
			commonObj.find("*").unbind();
			//删除
			opt.removeBeforeFun();
			commonObj.find(".dropload-down").remove();
			commonObj.unbind().find("*").unbind();
			
		}
		
		var _me = null;
		commonObj.dropload({
			scrollArea : opt.scrollArea,
			loadDownFn : function(me){
				_me = me;
				var param = opt.getSearchFilters();
				if(param){
					param["page"] = opt.page;
					param["rows"] = opt.rows;
				}
				var exist = false;
				if(opt.total != null){
					if(opt.page > opt.total){
            			me.lock();
	                    me.noData();
	                    me.resetload();
	                    exist = true;
	            	}
				}
				if(!exist){
					$.ajax({
			            type: 'POST',
						url : opt.url,
			            dataType: 'json',
			            page: opt.page,
			            data  : param,
			            success: function(dataObj){
			            	if(this.page == "1"){
			            		//先清空次，避免分页2次
			            		opt.removeBeforeFun();
			            		opt.page == 1;
			            	}
			            	opt.page++;
			            	opt.loadDownFn(dataObj);
			            	if(opt.isLastPage){
			            		if(opt.isLastPage(dataObj)){
			            			me.lock();
				                    me.noData();
			            		}
			            	}
			            	opt.total = dataObj.total;
			            	me.resetload();
			            },
			            error: function(xhr, type){
			                alert('Ajax error!');
			                // 即使加载出错，也得重置
			                me.resetload();
			            }
			        });
				}
				
				 
			}
		});
		return _me;
	}
}
//日期帮助类
var DateUtils = {
	YYYYMMDD : "yyyy-MM-dd",

	// 格式化日期,字符串货日期格式
	dateFormat : function(obj) {

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

			var fmt = this.YYYYMMDD;
			if (/(y+)/.test(fmt))
				fmt = fmt.replace(RegExp.$1, (obj.getFullYear() + "")
						.substr(4 - RegExp.$1.length));
			for ( var k in o) {
				if (new RegExp("(" + k + ")").test(fmt)) {
					fmt = fmt.replace(RegExp.$1,
							(RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k])
									.substr(("" + o[k]).length)));
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
	},
	dateToString : function(format) {
		
		var d = new Date();
		var o = {
		        "M+": d.getMonth() + 1,
		        "d+": d.getDate(),
		        "h+": d.getHours(),
		        "m+": d.getMinutes(),
		        "s+": d.getSeconds(),
		        "q+": Math.floor((d.getMonth() + 3) / 3),
		        "S": d.getMilliseconds()
		    }
		    if (/(y+)/.test(format)) {
		        format = format.replace(RegExp.$1, (d.getFullYear() + "").substr(4 - RegExp.$1.length));
		    }
		    for (var k in o) {
		        if (new RegExp("(" + k + ")").test(format)) {
		            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
		        }
		    }
		    return format;
	},

	//时间Str转日期Str格式
	datatimeStrToDateStr : function(dateStr){
		var reg = /^(\d+)-(\d{1,2})-(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/;
		if(reg.test(dateStr)){
			var fullDate = dateStr.match(reg);
		    return this.dateFormat(new Date(fullDate[1], fullDate[2]-1, fullDate[3], 0, 0, 0));
		}
		return "";		
	},	
	addDate : function(date, days) {
		var d = new Date(date);
		d.setDate(d.getDate() + days);
		var month = d.getMonth() + 1;
		var day = d.getDate();
		if (month < 10) {
			month = "0" + month;
		}
		if (day < 10) {
			day = "0" + day;
		}
		var val = d.getFullYear() + "-" + month + "-" + day;
		return val;
	}

};sss = window.innerHeight;

// 通用帮助类
var CommonUtils = {
	
	clickMulti : function(_options){
		var inputObjArray = _options.inputObj;
		var title = _options.title;
		
		var DomesticTruckTracking_Inquiry_box_prompt = _options.DomesticTruckTracking_Inquiry_box_prompt;
		var DomesticTruckTracking_Eetermine = _options.DomesticTruckTracking_Eetermine;
		var DomesticTruckTracking_Empty = _options.DomesticTruckTracking_Empty;
		
		
		for(var j = 0; j < inputObjArray.length; j++){
			var inputObj = inputObjArray[j];
			
			if(inputObj.hasClass("easyui-textbox")){
				inputObj = inputObj.textbox("textbox").removeClass("textbox-prompt");
			}
			
			//inputObj.attr("readonly", "readonly");
			var clickFun = function(input, title, ok_fun, cancel_fun){
				input.unbind("focus").bind("focus", function(){
					if($(this).hasClass("textbox-text")){
						$(this).parent().prev().textbox("setValue", $(this).val());
					}
					var showVal = $(this).val().trim().split(";").join("\n");
					var _this = $(this);
					var html = '<span style="color:#666;display:inline-block;margin-bottom:5px;">'+DomesticTruckTracking_Inquiry_box_prompt+'</span>';
					html += '<textarea id="_textareaInput" rows="9" cols="3" style="width:calc(99% - 2px);border:1px solid #C2D3E9;">' + showVal + '</textarea>';
					$.messager.defaults = {
						ok: DomesticTruckTracking_Eetermine,
						cancel: DomesticTruckTracking_Empty ,
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
								var arr = $("#_textareaInput").val().trim().split("\n");
								var newArray = [];
								for(var i = 0; i < arr.length; i++){
									arr[i] = arr[i].trim();
									if(arr[i] == ""){
										continue;
									}
									newArray.push(arr[i]);
								}
								if(_this.hasClass("textbox-text")){
									_this.next().val(newArray.join(";"));
								}
								_this.val(newArray.join(";")).removeClass("textbox-prompt");
								ok_fun();
							}else{	
								_this.val(''); 
								if(_this.hasClass("textbox-text")){
									_this.next().val("");
								}
								cancel_fun();
							}
						}
					});
					$('.messager-icon').hide();
					$("#_textareaInput").val("").focus().val(showVal);
				});
			}
			clickFun(inputObj, title[j], _options.ok_fun, _options.cancel_fun);
		}
	},
	bindFocus : function(_$Object){
		
		if(_$Object.parents(".mian-tk").length > 0){
			
			if(_$Object.parents(".zyck-tk").length > 0 || _$Object.parents(".top10-tk").length > 0){
				var height300 = "";
				var bool = false;
				_$Object.unbind().on('focus', function () {
					
					var temp = $(this).parents(".mian-tk").css("top");
					temp = Number(temp.substring(0, temp.length - 2));
					if(Math.round(temp/document.body.clientHeight*100) != 10){
						bool = true;
						return;
					}
					var _this = $(this);
					var count = 50;
			        var t = "";
			        var v = Math.round(10 - $(this).parents(".contnr-list").offset().top /document.body.clientHeight*100) + 15;
			        height300 = v;
			        _this = _this.parents(".mian-tk");
			        count = 10;
			        t = setInterval(function(){
		 	        	count-= 2;
		 	        	if(count < v){
		 	        		count = v;
		 	        	}
		 	        	_this.css("top", (count) +"%");
		 	        	if(count <= v){
		 	        		clearInterval(t);
		 	        	}
		 	        },0.01);
				}) .on("blur", function(){
					
					var _this = $(this).parents(".mian-tk");
					setTimeout(function(){
						if(bool == true){
							bool = false;
							return;
						}
						var count = height300;
				         var t = "";
			        	 t = setInterval(function(){
			 	        	count++;
			 	        	_this.css("top", ""+count +"%");
			 	        	if(count >= 10){
			 	        		clearInterval(t);
			 	        		height300 = "";
			 	        	}
			 	        },0.01);
						
					}, 100);
					 
			    }); 
			}else{
				
				var height300 = "";
				var bool = false;
				_$Object.unbind().on('focus', function () {

					var scrollTop = document.documentElement.scrollTop || window.pageYOffset || document.body.scrollTop;
					var temp = $(this).parents(".mian-tk").css("top");
					temp = Math.round(Number(temp.substring(0, temp.length - 2)));
					//console.log(Math.round(scrollTop + document.body.clientHeight*0.5) + ";"+temp);
					if(Math.round(scrollTop + document.body.clientHeight*0.5) != temp){
						bool = true;
						return;
					}
					var _this = $(this);
					var count = 50;
			        var t = "";
			        var _thisHeightObject = getAbsPoint(this);
			        height300 = document.body.clientHeight*0.2;
			        //console.log(_thisHeightObject.y + ";" + document.body.clientHeight/2);
			        if(_thisHeightObject.y - document.body.clientHeight/2 < height300){
			        	height300 = _thisHeightObject.y - document.body.clientHeight/2 + 50;
			        }else if(_thisHeightObject.y < document.body.clientHeight/2){
			        	height300 = 0;
			        } 
			        _this = _this.parents(".mian-tk");
			        height300 = _this.css("top");
			        count = Math.round(Number(height300.substring(0, height300.length - 2)));
			        var mainHeight = (scrollTop + document.body.clientHeight *0.5);
			        
			        var newH = _thisHeightObject.y - mainHeight;
			        if(newH > 0){
			        	newH = Math.round(mainHeight - newH-document.body.clientHeight*0.13);
			        	t = setInterval(function(){
			 	        	count-= 8;
			 	        	if(count <= newH){
			 	        		count = newH;
			 	        		clearInterval(t);
			 	        	}
			 	        	_this.css("top", count +"px");
			 	        },0.01);
			        }
			        
				}) .on("blur", function(){
					var _this = $(this);
			        _this = _this.parents(".mian-tk");
			        setTimeout(function(){
						if(bool == true){
							bool = false;
							return;
						}
						var oldTop = Math.round(Number(_this.css("top").substring(0, _this.css("top").length - 2)));
				        var t = "";
				        var t2 = Number(height300.substring(0, height300.length - 2));
				        t = setInterval(function(){
			 	        	oldTop += 8;
			 	        	if(oldTop >= t2){
			 	        		oldTop = t2;
			 	        		clearInterval(t);
			 	        	}
			 	        	_this.css("top", oldTop +"px");
			 	        },0.01);
			        }, 100);
			        

			    });
			}
			 
			return;
		}
		
		var height300 = "";
		var bool = false;
		var gs = "";
		_$Object.unbind().on('focus', function () {
			var temp = $("#header").css("margin-top");
			temp = Number(temp.substring(0, temp.length - 2));
			if(0 != temp){
				bool = true;
				return;
			}
			if(document.documentElement.scrollTop){
				gs = "0";
			}else if(window.pageYOffset){
				gs = "1";
			}else if(document.body.scrollTop){
				gs = "2";
			}
			var scrollTop = document.documentElement.scrollTop || window.pageYOffset || document.body.scrollTop;
			var count = 0;
	        var t = "";
	        var _thisHeightObject = getAbsPoint(this);
	        height300 = document.body.clientHeight*0.37;
	        if(_thisHeightObject.y - document.body.clientHeight/2 < height300){
	        	height300 = _thisHeightObject.y - document.body.clientHeight/2 + 50;
	        }else if(_thisHeightObject.y < document.body.clientHeight/2){
	        	height300 = 0;
	        }
	        t = setInterval(function(){
	        	count += 8;
	        	$("#header").css("margin-top", "-"+(scrollTop+count) +"px");
	        	if(count >=height300){
	        		clearInterval(t);
	        	}
	        },0.007);
	    }).on("blur", function(){
	    	setTimeout(function(){
				if(bool == true){
					bool = false;
					return;
				}
				var scrollTop = document.documentElement.scrollTop || window.pageYOffset || document.body.scrollTop;
				var top = $("#header").css("margin-top");
				var newTop = Number(top.substring(0, top.length - 2));
				var newTop2 = newTop + scrollTop;
				var count = newTop2;
		        var t = "";
		        t = setInterval(function(){
		        	count+= 8;
		        	if(count > height300){
		        		count = height300;
		        	}
		        	$("#header").css("margin-top", count +"px");
		        	if(count >= newTop+height300){
		        		clearInterval(t);
		       			$("#header").css("margin-top", "");
	       				window.scrollTo(0,-count);
		        	}
		        },0.008);
	    	}, 100);
	    	
	    });    
	},
		
	//克隆
	clone : function(dataObj){
	
		if(!(typeof dataObj == "object")){
			return dataObj;
		}
		var jsonStr = JSON.stringify(dataObj);
		return JSON.parse(jsonStr);		
	},
	//获取登陆人session对象
	getSystemSession : function(){
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
					return {};
				}
			}
		});
		return resultJson.resultDataFull;
	},
	getAPPSession : function(){
		var resultJson;
		var serverUrl = "../../sysInfo/user/getAPPSession?" + Math.random();
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
					return {};
				}
			}
		});
		return resultJson.resultDataFull;
	},
	isSystemRole : function(list){
		
		if(list == null || !list.length){
			return false;
		}
		for(var i=0;i<list.length;i++){
			if(list[i].roleId == "1"){
				return true;
			}
		}
		return false;
		
	},
	isSystemRoleJituan : function(list){
		
		if(list == null || !list.length){
			return false;
		}
		for(var i=0;i<list.length;i++){
			if(list[i].roleId == "100"){
				return true;
			}
		}
		return false;
		
	},
	/**
	 * 按钮失效
	 * @param jq 对象
	 * @param message 不为空,提示自定义消息
	 */
	buttonDisabled : function(jq, message){
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
	},
	/**
	 * 按钮恢复正常
	 */
	buttonUndisabled : function(jq){
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
	
}

// 数组帮助类
var ArrayUtils = {
	// 数组中包含某数据
	contains : function(arr, v) {
		if (v == null) {
			return true;
		}
		if (!arr || !arr.length) {
			return false;
		}
		if (typeof v == "object") {

			for ( var i = 0; i < arr.length; i++) {
				if (arr[i] == v) {
					return true;
				}
			}
		} else {
			for ( var i = 0; i < arr.length; i++) {
				if (arr[i] == null) {
					throw "调用ArrayUtils.contains[arr, object]时数组数据为空";
				}
				var exist = true;
				for ( var j in v) {
					// 有一个字段不匹配则false
					if (arr[i][j] != v[j]) {
						exist = false;
						break;
					}
				}
				if (exist) {
					return true;
				}
			}
		}
		return false;
	},
	/**
	 * 删除数组数据
	 * 
	 * @param arr
	 * @param v
	 *            对象数组，普通数组
	 *            值为对象，根据对象所有值进行删除，字符就匹配
	 */
	remove : function(arr, v) {
		if (v == null && arr) {
			arr.splice(0, arr.length);
			return;
		}
		if (typeof v == "object") {
			for ( var i = 0; i < arr.length; i++) {
				if (arr[i] == null) {
					throw "调用ArrayUtils.remove[arr, object]时数组数据为空";
				}
				var exist = true;
				for ( var j in v) {
					// 有一个字段不匹配则false
					if (arr[i][j] != v[j]) {
						exist = false;
						break;
					}
				}
				if (exist) {
					arr.splice(i, 1);
					i--;
				}
			}
		} else {
			for ( var i = 0; i < arr.length; i++) {
				if (arr[i] == v) {
					arr.splice(i, 1);
					i--;
				}
			}
		}
		return arr;
	},
	// 数组合并
	join : function(arr, separator) {
		if (!arr || !arr.length) {
			return "";
		}
		if (!separator) {
			// 默认分割符
			separator = ",";
		}
		var buf = "";
		for ( var i = 0; i < arr.length; i++) {
			buf += separator + "'" + arr[i] + "'";
		}
		return buf.substring(1);
	},	get : function(arr, v){
		if (v == null && !arr) {
			return null;
		};
		if (typeof v == "object") {
			for ( var i = 0; i < arr.length; i++) {
				if (arr[i] == null) {
					throw "调用ArrayUtils.get[arr, object]时数组数据为空";
				}
				var exist = true;
				for ( var j in v) {
					// 有一个字段不匹配则false
					if (arr[i][j] != v[j]) {
						exist = false;
						break;
					}
				}
				if (exist) {
					return arr[i];
				}
			}
			return null;
		} else {
			for ( var i = 0; i < arr.length; i++) {
				if (arr[i] == v) {
					return v;
				}
			}
			return null;
		}
	}, find : function(arr, v){
		if (v == null && !arr) {
			return null;
		};
		
		var newArray = [];
		if (typeof v == "object") {
			for ( var i = 0; i < arr.length; i++) {
				if (arr[i] == null) {
					throw "调用ArrayUtils.get[arr, object]时数组数据为空";
				}
				var exist = true;
				for ( var j in v) {
					// 有一个字段不匹配则false
					if (arr[i][j] != v[j]) {
						exist = false;
						break;
					}
				}
				if (exist) {
					newArray.push(arr[i]);
				}
			}
			return newArray;
		} else {
			for ( var i = 0; i < arr.length; i++) {
				if (arr[i] == v) {
					newArray.push(arr[i]);
				}
			}
			return newArray;
		}
	}
};

// 表单帮助类
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
	},removeValidatebox : function(form){
		form.find(".validatebox-tip").remove();
		form.find(".validatebox-invalid").removeClass("validatebox-invalid");
		form.find(".textbox-invalid").removeClass("textbox-invalid");
	}
}
//对象帮助类
var ObjectUtils = {
	/**
	 * 根据正则表达式删除元素
	 * @param obj
	 * @param regexp
	 */
	removeAttr : function(obj, regexp){
		var reg = new RegExp(regexp)
		for (vo in obj){
			if (reg.test(vo)){
				delete obj[vo];
			}
		}
	}
}

//字符帮助类
var StringUtils = {
	/**
	 * 判断值是否为空，如果是取默认值
	 * @param arr
	 * @param obj 值
	 * @param default_value 值为空取默认值
	 */
	nvl : function(obj, default_value){
		if(obj == null){
			obj = default_value || "";
		}
		return obj;
	},
	/**
	 * 非空返回true,否则false
	 * @param v 校验的字符串 
	 * @return boolean
	 */
	isNotBlank : function(v){
		if(v == null){
			return false;
		}
		return !this.isBlank(v);
	},
	/**
	 * 空返回true,否则false
	 * @param v 校验的字符串 
	 * @return boolean
	 */
	isBlank : function(v){
		if(v == null){
			return true;
		}
		return (/^\s{0,}$/.test(v));
	},join : function(arr, f){
		f = this.nvl(f, ",");
		if(!arr.length){
			return "";
		}
		var str = "";
		for(var i =0;i < arr.length; i++){
			str += f + "'"+arr[i]+"'";
		}
		return str.substring(1);
	}
};

//html帮助类
var HtmlUtils = {
		
	htmlToObject : function(selections, filed_name, text_value){
		var htmlObjList = [];
		if(filed_name == null && text_value == null){
			htmlObjList = $(selections);
		}else{
			var htmlObj = $(selections + ":has([name="+filed_name+"]:text("+text_value+"))");
			
			if(htmlObj.length > 0){
				htmlObjList.push(htmlObj);
			}
		}
		
		var dataList = [];
		for (var j = 0; j <htmlObjList.length; j++){
			var htmlObj = $(htmlObjList[j]);
			var obj = {};
			var filedArray = htmlObj.find("*[name]");
			for(var i = 0; i < filedArray.length; i++){
				
				if(filedArray.eq(i).is("input")){
					obj[filedArray.eq(i).attr("name")] = filedArray.eq(i).val();
				
				}else if(filedArray.eq(i).is("textarea")){
					obj[filedArray.eq(i).attr("name")] = filedArray.eq(i).val();
				}else{
					obj[filedArray.eq(i).attr("name")] = filedArray.eq(i).text();
				}
				
			}
			dataList.push(obj);
		}
		
		if(filed_name == null && text_value == null){
			return dataList;
		}else{
			if(dataList.length == 0){
				return null;
			}
			return dataList[0];
		}
	}
};

//app分页帮助类
var PageUtils = {
	showPageLoading :function(obj){
		obj.find(".loadingLogo").remove();
		obj.append("<div class=\"loadingLogo\"><p>&nbsp;</p>正在加载...</div>")
	},
	hidePageLoading :function(obj){
		
		if(obj.page){
			
		}
		
		obj.find(".loadingLogo").remove();
	},
	isLastPage : function(pageVO){
		if(!pageVO.records || pageVO.records<=pageVO.rows){
			return true;
		}
		var total = 0;
		if(pageVO.records%pageVO.rows == 0){
			total = pageVO.records / pageVO.rows;
		}else{
			total = Math.floor(pageVO.records /pageVO.rows)+1;
		}
		if(pageVO.page<=total){
			return false;
		}
		return true;
	},
	showLastPageLoading :function(obj){
		var loadingLogo = obj.find(".loadingLogo");
		if(loadingLogo.length == 0){
			obj.append("<div class=\"loadingLogo\"><p>&nbsp;</p>已经到最后一页了</div>")
		}else{
			obj.find(".loadingLogo").show();
		}
		obj.find(".loadingLogo").css("background-image","url()");
	}
}
var setStateText  = function(state){
	if(state == undefined){
		return '';
	}
	if(state == null||state==''){
		return '';
	}
	if(state=='0'){
		return '新单';
	}else if(state=='1'){
		return '审核';
	}else if(state=='2'){
		return '任务分配';
	}else if(state=='3'){
		return '已完成';
	}else if(state=='4'){
		return '注销';
	}
}

