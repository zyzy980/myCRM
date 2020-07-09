;(function () {
	var getAddress = true;
 	var address = {
 			shi:[],//市
 			qu:[],//区
 			jiedao:[],//街道
 			zhandian:[]//站点
 	}
 	var addressChecked = []; //已选地址
 	var targetAddress = '';//操作地址目标	

	//获取出发地。目的地
	getPlace = function(target,name,layer,code){
		var cusId = $("#cus_id").combobox("getValues");
		if(cusId == "--请选择--") {
			cusId = "";
		}
		if(typeof target == "object"){
			//点击文本进入弹出框
			$('#win').window('open');
			if(targetAddress == ""){
				getAddressZhanDian(cusId);
				searchAddress(name,layer,code);
				readLetter();
				$(".world-link1 li:first").click();
			}
			
			var json = decodeURIComponent($.trim($(target).attr("data-json")));
			if(!json){
				json = "[]";
			}
			addressChecked = JSON.parse(json);
			targetAddress = target;
			resetAddress();
			resetSuccessHTML();
		}else{
			searchAddress(name,layer,code,cusId);
		}
	}
	
	resetSuccessHTML = function(){
		for(var i = 0 ; i < addressChecked.length; i++){
			$("#field input[data-code="+addressChecked[i].code+"]").attr("checked",true);
		}
	}
	
	readLetter = function(){
		for(var i=0;i<26;i++){//字母搜索链接
			var word = String.fromCharCode(65+i);
			$(".world-link1 ul").append("<li onclick='worldLink(2,\""+word+"\")'>"+word+"</li>");
		}
	}
	
	checkAddr = function (target, type){
		$("#checked").html('');
		var code = $(target).attr("data-code");
		if(target.checked == true){//选中
			addressChecked.push({
				type : type,
				code : code,
				name : $(target).val()
			});
			resetAddress();
		}else{
			deleteChecked(code);
		}
	}
	resetAddress = function(){
		$("#checked").html("");
		for(var i = 0; i < addressChecked.length; i++){
			var obj = addressChecked[i];
			$("#checked").append("&nbsp;<span>"+obj.name+"<img src='css/cancel.png' onclick='deleteChecked(\""+obj.code+"\")' style='width:10px;height:10px;'/></span>");
		}
	}
	//地址选择弹框删除地址
	deleteChecked = function (code){
		for(var i = 0 ; i < addressChecked.length; i++){
			if(addressChecked[i].code == code){
				addressChecked.splice(i,1);
				break;
			}
		}
		 $("#field input[data-code="+code+"]").attr("checked",false);
		 resetAddress();
	}
	//地址选择弹框字母搜索
	 worldLink = function (option,value){
		$("#shi").val('');
		$(".field"+option+" ul").html('');
		var index = option;
		if(option = 2){
			index += 1;
			var datas = address.shi;
		}else {
			var datas = address.zhandian;
		}
		var result = [];
		for(var i in datas){
			if(value == '*' ||datas[i].ACRONYM.substr(0,1) == value){//相匹配
				result.push(datas[i]);
			}
		}
		loadAddressList($(".field"+option+" ul"), index, result);
	}
	 
	loadAddressList = function(obj, layer, result){
		obj.html('');
		for(var i in result){
			obj.append(
				"<li>"+
				"<input type='checkbox' value='"+result[i].NAME+"' onchange=\"checkAddr(this, 'Q')\" data-code='"+result[i].ID+"'/>"+
				"<label onclick='getPlace(\"child\",null,"+layer+","+result[i].ID+")'>"+result[i].NAME+"</label>"+
				"</li>"
			);
		}
		resetSuccessHTML();
	}
	 
 	//初始化市级数据
	var searchAddress = function(name,layer,code,cusId){
		var index = layer;
		$.ajax({
			url:"../..costplan/getaddress",
			type:'post',
			async : false,
			// data:{'layer':layer,'name':name,'code':code,"cusId":cusId[0]},
            data:{'layer':layer,'name':name,'code':code,"cusId":cusId},
			beforeSend: function () {
				$.messager.progress({
		            text: 'loading....'
		        });
		    },
			success:function(result){
				$.messager.progress("close");
				if(result == null && result.length<1){
					$.messager.alert('#{error}','#{no-data}');
				}else{
					var layer = index;
					switch(index){
						case 2:
							layer = 3;
							break;
						case 3:
							layer = 4;
							break;
						case 4:
							layer = '';
							break;
					}
					loadAddressList($(".field"+index+" ul"), layer, result);
					switch(index){
						case 2:
							address.shi = result;
							break;
						case 3:
							address.qu = result;
							break;
						case 4:
							address.jiedao = result;
							break;
						default:
							address.zhandian = result;
					}
				}
			},
			error:function(){
				$.messager.progress("close");
				$.messager.alert('#{error}','#{network-error}');
			}
		});
	}
	//初始化站点数据
	var getAddressZhanDian = function(cusId){
		$.ajax({
			url:"../..costplan/getaddress",
			type:'post',
			async : false,
			data: "cusId=" + cusId,
			success:function(result){
				$.messager.progress("close");
				if(result == null && result.length<1){
					$.messager.alert('#{error}','#{no-data}');
				}else{
					loadAddressZhanDian($(".field ul"),result);
					address.zhandian = result;
				}
			},
			error:function(){
				$.messager.progress("close");
				$.messager.alert('#{error}','#{network-error}');
			}
		});
	}
	
	loadAddressZhanDian = function(obj, result){
		$(".field ul").html('');
		for(var i in result){
			$(".field ul").append(
				"<li>"+
				"<input type='checkbox' value='"+result[i].NAME+"' onchange=\"checkAddr(this, 'Z')\" data-code='"+result[i].CODE+"'/>"+
				"<label>"+result[i].NAME+"</label>"+
				"</li>"
			);
		}
		resetSuccessHTML();
	}
	
	//地址选择弹框汉字搜索框搜索
	searByWord = function(option,target){
		var value = $(target).val();
		if(option == 2){
			var result = [];
			var datas = address.shi;
			for(var i in datas){
				if(value.trim() == '' || datas[i].NAME.substr(0,value.length) == value){//相匹配
					result.push(datas[i]);
				}
			}
			loadAddressList($(".field"+option+" ul"), (option+1), result);
		}else{
			var result = [];
			var datas = address.zhandian;
			for(var i in datas){
				if(value.trim() == '' || datas[i].NAME.substr(0,value.length) == value){//相匹配
					result.push(datas[i]);
				}
			}
			loadAddressZhanDian($(".field ul"), result);
		}
	}
	//清除地址数据
   clearAddr = function(target){
		$("#"+target).val('');
		$("#"+target).attr('data-json', "");
	}
	//关闭地址选择弹框
	closeWindow= function(){
		$("#field input[type=checkbox]").each(function(){
			$(this).attr("checked",false);
		});

		$('#win').window('close');
		
	}
	//地址选择弹框确定
	addressOk = function(){
		$(targetAddress).attr("data-json", encodeURIComponent(JSON.stringify(addressChecked)));
		var arr = [];
		for(var i = 0; i < addressChecked.length; i++){
			arr.push(addressChecked[i].name);
		}
		$(targetAddress).val(arr.join(","));
		closeWindow();
	}
})(window)