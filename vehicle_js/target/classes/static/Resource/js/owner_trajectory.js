$(function(){
	// 初始化函数
    // 注：使用该函数获得经纬度集合
    function initFunction(){
    	//修改页面部分样式
    	var guid = getUrlParms().guid;
    	$.ajax({
    	    url:"/oxplatformtms/owner/order/inRoadTracking?detail_guid=" + guid,
    	    data: {},
    	    dataType:"json",
    	    contentType : 'application/json',
    	    type:"GET",
    	    success:function(res){
    	    	var _trajectory_set = [];
    	    	var innerHTML = "";
    	    	if(res.resultCode === '1'){
        	    	innerHTML += "<div class=\"content state-con\">";
        	    	innerHTML += "<article>";
        	    	innerHTML += "<section>";
        	    	innerHTML += "<span class=\"circle-time circle-color\"><em>" 
        	    		+ (res.resultDataFull[0].action_name ===  "交付目的地" ? '收' : '运') + "</em></span>";
        	    	innerHTML += "<time datetime=\"2013-03\">";
        	    	var date = res.resultDataFull[0].action_date.split(" ")[0];
        	    	var time = res.resultDataFull[0].action_date.split(" ")[1];
        	    	innerHTML += "<p>" + date.substring(date.indexOf("-") + 1,date.length) + "</p>";
        	    	innerHTML += "<span>" + time.substring(0 , time.lastIndexOf(":")) + "</span>";
        	    	innerHTML += "</time>";
        	    	innerHTML += "<aside>";
        	    	innerHTML += "<p class=\"brief brief2\">" 
        	    		+ (res.resultDataFull[0].action_name ===  "交付目的地" ? "收货地址：" : "当前地址：") 
        	    		+ res.resultDataFull[0].address + "</p>";
        	    	innerHTML += "</aside>";
        	    	innerHTML += "</section>";
        	    	innerHTML += "<p class=\"flip\" style=\"cursor:pointer; color:#999; font-size:1.2em; text-align:center;\">" 
        	    		+ "<span class=\"zhanshi\"><em>点击查看更多物流详情</em>" 
        	    		+ "<img src=\"/oxplatform/Resource/TMS/images/tms_trajectory/arrow.png\" /></span></p>";
        	    	innerHTML += "<div class=\"panel\">";
        	    	for(var index in res.resultDataFull){
        	    		
        	    		_trajectory_set.push(new BMap.Point(res.resultDataFull[index].longitude
        	    				, res.resultDataFull[index].latitude));
        	    		if(index == 0) continue;
        	    		innerHTML += "<section>";
            	    	innerHTML += "<span class=\"circle-time\"><img src=\"" 
            	    		+ "/oxplatform/Resource/TMS/images/tms_trajectory/icon1.png" 
            	    		+ "\" /></span>";
            	    	date = res.resultDataFull[index].action_date.split(" ")[0];
            	    	time = res.resultDataFull[index].action_date.split(" ")[1];
            	    	innerHTML += "<time datetime=\"" + date.substring(0, date.lastIndexOf("-")) + "\">";
            	    	innerHTML += "<p>" + date.substring(date.indexOf("-") + 1,date.length) + "</p>";
            	    	innerHTML += "<span>" + time.substring(0 , time.lastIndexOf(":")) + "</span>";
            	    	innerHTML += "</time>";
            	    	innerHTML += "<aside>";
            	    	innerHTML += "<p class=\"things\">" + res.resultDataFull[index].action_name + "</p>";
            	    	innerHTML += "<p class=\"brief\">" + res.resultDataFull[index].address + "</p>";
            	    	innerHTML += "</aside>";
            	    	innerHTML += "</section>";
        	    	}
        	    	innerHTML += "</div>";
        	    	innerHTML += "</article>";
        	    	innerHTML += "</div>";
        	    	$("body").append(innerHTML);
        	    	
        	    	// 绑定onclick事件
        	        $(".flip").bind("click",function(){
        	        	$(".panel").slideToggle("slow");
        	    	    $(".zhanshi").toggle();
        	    	    $(".yincang").toggle();
        	    	    //  查看详情后，如果内容超出则滚动条显示
        	    	    $(".state-con").css({"height":"400px","overflow-y":"scroll"});
        	        });
        	        // 调用方法（绘制地图轨迹）
        	    	getTransportTrajectory(_trajectory_set);
    	    	}else{
    	    		innerHTML += "<div class=\"content state-con\" style=\"padding-bottom: 30px;\">";
        	    	innerHTML += "<article>";
        	    	innerHTML += "<p class=\"flip\" style=\"cursor:pointer; color:#999; font-size:1.2em; text-align:center;\">" 
        	    		+ "<span class=\"zhanshi\"><em>暂无在途信息</em></span></p>";
        	    	innerHTML += "<div class=\"panel\">";
        	    	innerHTML += "</div>";
        	    	innerHTML += "</article>";
        	    	innerHTML += "</div>";
        	    	$("body").append(innerHTML);
        	    	
    	    		// 否则展开当前地理位置
    	    		gainLatitudeAndLongitude();
    	    	};
    	    },
    	    error:function(res){
    	        //请求出错处理
    	    	alert("系统异常，请联系管理员");
    	    },
    	});
        
    };

    // 获取轨迹中间点
    // 参数一：轨迹起始位置
    // 参数二：轨迹结束位置
    // 注：如果你只传了一个参数，则默认以该点为中间点
    function getCenterPoint(_starting_point, _ending_point){
        // 判断是否只有一个参数传入
        if( _starting_point === undefined || _starting_point === null || _starting_point === "" ){ return _ending_point; }
        if( _ending_point === undefined || _ending_point === null || _ending_point === "" ){ return _starting_point; }

        // 取得经纬度平均值
        var lng = (parseFloat(_starting_point.lng) + parseFloat(_ending_point.lng)) / 2;      // 经度
        var lat = (parseFloat(_starting_point.lat) + parseFloat(_ending_point.lat)) / 2;     // 纬度
        return new BMap.Point(lng,  lat);
    };

    // 轨迹集参数是否合法
    // 参数一：轨迹集合
    // 参数二：指定合法集合长度
    function trackSetIsCorrect(_trajectory_set, legal_length){
        if(_trajectory_set === undefined || _trajectory_set === null || _trajectory_set.length < legal_length){
            return false;
        }else {
            return true;
        }
    };

    // 获取轨迹信息(距离，花费时间)
    // 参数一：轨迹集合
    function getMapZoomRatio(_trajectory_set){
        if(!trackSetIsCorrect(_trajectory_set, 2)){ return; }
        var _starting_point = _trajectory_set[0];                           // 起始经纬度
        var _ending_point = _trajectory_set[_trajectory_set.length - 1];   // 结束经纬度
        var _trajectory_information = null;                                // 轨迹信息
        var map = new BMap.Map("allmap");
        map.centerAndZoom(getCenterPoint(_starting_point, _ending_point));
        var searchComplete = function (results){
            if (transit.getStatus() != BMAP_STATUS_SUCCESS){
                return ;
            }
            _trajectory_information = results.getPlan(0);
//            var plan = results.getPlan(0);
//            plan.getDuration(true);             // 返回方案总时间。当format参数为true时，返回描述时间的字符串（包含单位），当format为false时，仅返回数值（单位为秒）信息。默认参数为true
//            plan.getDistance(true);             // 返回方案总距离。当format参数为true时，返回方案距离字符串（包含单位），当format为false时，仅返回数值（单位为米）信息。默认参数为true
        };
        var transit = new BMap.DrivingRoute(map, {renderOptions: {map: map},
            onSearchComplete: searchComplete,
            onPolylinesSet: function(){
                console.log(_trajectory_information.getDuration(true));
                return _trajectory_information;
            }
        });
        transit.search(_starting_point, _ending_point, _trajectory_set); //_trajectory_set表示途经点
    };

    // 获得运输轨迹
    // 参数一：轨迹集合
    function getTransportTrajectory(_trajectory_set){
        if(!trackSetIsCorrect(_trajectory_set, 1)){ return; }
        var map = new BMap.Map("allmap");
        var _starting_point = _trajectory_set[0];                           // 起始经纬度
        var _ending_point = _trajectory_set[_trajectory_set.length - 1];   // 结束经纬度
        map.centerAndZoom(getCenterPoint(_starting_point, _ending_point));  // 设初始化地图。参数值（初始坐标，地图初始大小，可赋值范围为3-18级）
        map.enableKeyboard(true);                                          // 启用键盘操作，默认禁用。
        map.enableScrollWheelZoom(true);                                   // 开启鼠标滚轮缩放功能。仅对PC上有效

        var driving = new BMap.DrivingRoute(map, {renderOptions:{map: map, autoViewport: true}});
        var _passing_point = _trajectory_set.slice(1, _trajectory_set.length - 1);
        driving.search(_starting_point, _ending_point, {waypoints: _passing_point});    // waypoints表示途经点
    };

    // 浏览器是否支持定位
    // 返回值：true支持，false不支持
    function isItPossibleToPosition(){
        if(navigator.geolocation) {
            console.log("正在获取位置信息...");
            return true;
        } else {
            console.log("浏览器不支持地理定位。");
            return false;
        }
    };

    // 获得当前地理位置
    function getGeographicLocation(){
        // 浏览器是否支持定位
        if(!isItPossibleToPosition()) return;
        //创建百度地图控件
        var geolocation = new BMap.Geolocation();
        geolocation.getCurrentPosition(function(result){
            if(this.getStatus() == BMAP_STATUS_SUCCESS){
                //以指定的经度与纬度创建一个坐标点
                var position = new BMap.Point(result.point.lng, result.point.lat);
                //创建一个地理位置解析器
                var geoc = new BMap.Geocoder();
                geoc.getLocation(position, function(res){//解析格式：城市，区县，街道
                    var addComp = res.addressComponents;
                    console.log(addComp.city + ", " + addComp.district + ", " + addComp.street);
                });
            }else {
                console.log('定位失败');
            }
        },{enableHighAccuracy: true});//指示浏览器获取高精度的位置，默认false
    };
    
    // 获得当前经纬度及绘制地图信息
    function gainLatitudeAndLongitude(){
    	var geolocation = new BMap.Geolocation();
    	geolocation.getCurrentPosition(function(r){
    		if(this.getStatus() == BMAP_STATUS_SUCCESS){
    			console.log(r.point.lng, r.point.lat);
    			// 绘制当前经纬度地图
    			var map = new BMap.Map("allmap");
    			var point = new BMap.Point(r.point.lng, r.point.lat);
    			map.centerAndZoom(point,12);
    		}else {
    			alert('failed' + this.getStatus());
    		}        
    	},{enableHighAccuracy: true});
    	//关于状态码
    	//BMAP_STATUS_SUCCESS	检索成功。对应数值“0”。
    	//BMAP_STATUS_CITY_LIST	城市列表。对应数值“1”。
    	//BMAP_STATUS_UNKNOWN_LOCATION	位置结果未知。对应数值“2”。
    	//BMAP_STATUS_UNKNOWN_ROUTE	导航结果未知。对应数值“3”。
    	//BMAP_STATUS_INVALID_KEY	非法密钥。对应数值“4”。
    	//BMAP_STATUS_INVALID_REQUEST	非法请求。对应数值“5”。
    	//BMAP_STATUS_PERMISSION_DENIED	没有权限。对应数值“6”。(自 1.1 新增)
    	//BMAP_STATUS_SERVICE_UNAVAILABLE	服务不可用。对应数值“7”。(自 1.1 新增)
    	//BMAP_STATUS_TIMEOUT	超时。对应数值“8”。(自 1.1 新增)
    };

//    getGeographicLocation();
    initFunction();
    
});