// 定义天气图标
var pWeatherTypeIcon;
// 定义脱敏权限
var pSensitive_authority;
// 定义Market集合
var pMarkerArrayList = [];
var pKeyMilestoneList = [];
var waterIcosMap = {"大暴雨":"Resource\\images\\weather\\rain.png","阵雨":"Resource\\images\\weather\\rain.png","雷阵雨":"Resource\\images\\weather\\rain.png","小雪-中雪":"Resource\\images\\weather\\snow.png","小雨-中雨":"Resource\\images\\weather\\rain.png","阵雪":"Resource\\images\\weather\\snow.png","冻雨":"Resource\\images\\weather\\rain.png","弱高吹雪":"Resource\\images\\weather\\snow.png","中雨":"Resource\\images\\weather\\rain.png","飑":"Resource\\images\\weather\\sunny.png","沙尘暴":"Resource\\images\\weather\\sunny.png","中雪":"Resource\\images\\weather\\snow.png","强沙尘暴":"Resource\\images\\weather\\sunny.png","大雨-暴雨":"Resource\\images\\weather\\rain.png","大雪-暴雪":"Resource\\images\\weather\\snow.png","中雪-大雪":"Resource\\images\\weather\\snow.png","中雨-大雨":"Resource\\images\\weather\\rain.png","大暴雨-特大暴雨":"Resource\\images\\weather\\rain.png","扬沙":"Resource\\images\\weather\\sunny.png","雷阵雨并伴有冰雹":"Resource\\images\\weather\\rain.png","特大暴雨":"Resource\\images\\weather\\rain.png","浮尘":"Resource\\images\\weather\\sunny.png","大雪":"Resource\\images\\weather\\snow.png","大雨":"Resource\\images\\weather\\rain.png","小雪":"Resource\\images\\weather\\snow.png","暴雨-大暴雨":"Resource\\images\\weather\\rain.png","龙卷风":"Resource\\images\\weather\\sunny.png","小雨":"Resource\\images\\weather\\rain.png","轻雾":"Resource\\images\\weather\\mist.png","晴":"Resource\\images\\weather\\sunny.png","阴":"Resource\\images\\weather\\sunny.png","雨夹雪":"Resource\\images\\weather\\rain.png","暴雨":"Resource\\images\\weather\\rain.png","暴雪":"Resource\\images\\weather\\snow.png","多云":"Resource\\images\\weather\\sunny.png","雾":"Resource\\images\\weather\\mist.png","霾":"Resource\\images\\weather\\mist.png"};


var map = window.map;

if(window.AMapUI == null){
    $("body").append("<script src=\"https://webapi.amap.com/ui/1.0/main.js?v=1.0.11\"></script>");
}

var searchNEW = function(arr,grasp,DG_ALL){
    var data = [];
    for(var i = 0; i < arr.length; i++){
        if(arr[i]!=null){
            data.push([arr[i].x, arr[i].y]);
        }
    }

    AMapUI.load(['ui/misc/PathSimplifier', 'lib/$'], function(PathSimplifier, $) {

        if (!PathSimplifier.supportCanvas) {
            alert('当前环境不支持 Canvas！');
            return;
        }
        /*var pathSimplifierIns = null;
        if(window.pathSimplifierIns){
            pathSimplifierIns = window.pathSimplifierIns;
        }else{*/
        pathSimplifierIns = //window.pathSimplifierIns =
            new PathSimplifier({
                zIndex: 100,
                clickToSelectPath : false,
                autoSetFitView : false, //是否在绘制后自动调整地图视野以适合全部轨迹
                map: map, //所属的地图实例
                getPath: function(pathData, pathIndex) {
                    return pathData.path;
                },
                getHoverTitle: function(pathData, pathIndex, pointIndex) {

                    return "";
                },
                renderOptions: {
                    //轨迹线的样式
                    pathLineStyle: {
                        strokeStyle: '#0091EA',
                        lineWidth: 7,
                        dirArrowStyle: false
                    },
                    pathLineHoverStyle : false,
                    eventSupportInvisible : false
                }
            });
        //}
        //设置数据
        pathSimplifierIns.setData([{
            name: '',
            path: data
        }]);

        Recursion(grasp,DG_ALL);

    });
}


var Recursion = function(grasp,arr,tempTruck){

    var tempAll = [];
    var tempAllInt = 0;
    var DG_ALL = [];
    if(arr != null ){
        var conall = [];
        for(var i = 0; i <= arr.length; i++){
            if(arr[i] != null && (arr[i].truck == null || arr[i].truck == '') ){
                arr.splice($.inArray(arr[i],arr),1);
            }
        }
        for(var i = 0; i <= arr.length; i++){

            if(i == 0 && arr[0] != null){
                tempAllInt++;
                conall[0] = arr[0];
            }
            for(var j = i+1; j <= arr.length; j++){
                if(arr[i] != null && arr[j] != null){
                    if(arr[i].x.toFixed(3) == arr[j].x.toFixed(3) && arr[i].y.toFixed(3) == arr[j].y.toFixed(3)){

                    }else{
                        if(arr[i].x.longitude != 0 && arr[i].y.longitude != 0){
                            conall[tempAllInt++] = arr[j];
                        }
                    }
                }
                break;
            }
        }
        arr = conall;
        tempAllInt = 0;
        for(var i = 0; i <= arr.length; i++){
            if(arr[i] != null){
                arr[i].tm = 500+i;
            }
        }
        if(arr.length > 0){
            arr[0].tm = 1478031031;
        }

    }
    var type = "";
    if(arr == null || arr.length  <= 0){
        return;
    }else if(arr.length <= 400){

        for (var i = 0; i <= arr.length; i++) {
            if(arr[i] != null){
                if(type == arr[i].truck || type == ""){
                    type = arr[i].truck;
                    tempAll[tempAllInt++] = arr[i];
                    continue;
                }
                DG_ALL[DG_ALL_Int++] = arr[i];
            }
        }


    }else{
        for (var i = 0; i <= arr.length; i++) {
            if((i != 0 && i % 400 == 0) || (type != arr[i].truck && type != "")){
                var DG_ALL_Int = 0;
                for(i = i - 1; i <= arr.length ;  i++) {
                    DG_ALL[DG_ALL_Int++] = arr[i];
                }
                break;
            }
            if(type == arr[i].truck || type == ""){
                type = arr[i].truck;
                tempAll[tempAllInt++] = arr[i];
            }
        }
    }
    //grasp 地图对象，arr 定位集合， tempTruck 车牌号

    var grasspAll = [];

    // 去掉 车型
    for (var i = 0; i <= tempAll.length; i++) {
        if(tempAll[i] != null){
            grasspAll[i] = {
                x : tempAll[i].x,
                y : tempAll[i].y,
                sp : tempAll[i].sp,
                ag : tempAll[i].ag,
                tm : tempAll[i].tm
            }
        }
    }

    grasp.driving(grasspAll,function(error,result){
        if(!error){

            var newPath = result.data.points;//纠偏后的轨迹
            var distance = result.data.distance;//里程
            for(var i = 0; i < newPath.length; i++){
                newPath[i] = [newPath[i].x, newPath[i].y];
            }
            var newLine = new AMap.Polyline({
                path:newPath,
                strokeWeight:8,
                strokeOpacity:1,
                strokeColor:'#0091EA',
                showDir:true
            })
            map.add(newLine);
            map.setFitView();

            Recursion(grasp,DG_ALL);
        }else{
            console.log(error);
            console.log(grasspAll);
            searchNEW(grasspAll,grasp,DG_ALL);

        }
    });
}


var My_MapUtils = {
    /**
     * Map地图放大
     */
    amplification : function amplification(){
        var map = window.map;
        if(map){

            /*if(map.F == null){
                var currentZoom = map.F.zoom;
                if(currentZoom == 18){
                    //已经最低限制了，无须更新
                    return;
                }
                map.F.zoom++;
                map.setZoomAndCenter(++currentZoom);
            }else{*/
            var currentZoom = map.getZoom();
            if(currentZoom == 18){
                //已经最低限制了，无须更新
                return;
            }
            map.setZoom(++currentZoom);
            /*}*/

        }

    },
    /**
     * Map地图缩小
     */
    narrow : function(){
        var map = window.map;
        if(map){


            /*if(map.F == null){
                var currentZoom = map.F.zoom;
                if(currentZoom == 3){
                    //已经最低限制了，无须更新
                    return;
                }
                map.F.zoom--;
                map.setZoomAndCenter(++currentZoom);
            }else{*/
            var currentZoom = map.getZoom();
            if(currentZoom == 3){
                //已经最低限制了，无须更新
                return;
            }
            map.setZoom(--currentZoom);
            /*}*/
        }
    },
    /**
     * 刷新
     */
    refresh : function(){
        location.reload();
    },
    /**
     * 全屏
     */
    fullScreen : function(){
        window.open(window.location)
    },
    /**
     * 定位到当前位置
     */
    currentPosition : function(){
        if($(".monitorListItemSelect").length == 0){
            if(CommonMsg){
                errorNotification({
                    simpleMessage : CommonMsg.selectTruckAfterOp
                });
            }
            return;
        }

        var longitude = MarkerArrayList[0].getPosition().P;
        var latitude = MarkerArrayList[0].getPosition().O;

        map.setZoomAndCenter(10, [longitude, latitude]);
        /***************************************
         由于Chrome、IOS10等已不再支持非安全域的浏览器定位请求，为保证定位成功率和精度，请尽快升级您的站点到HTTPS。
         ***************************************/
        /* var geolocation;
     map.plugin('AMap.Geolocation', function() {
         geolocation = new AMap.Geolocation({
             enableHighAccuracy: true, //是否使用高精度定位，默认:true
             timeout: 10000, //超过10秒后停止定位，默认：无穷大
             buttonOffset: new AMap.Pixel(10, 20), //定位按钮与设置的停靠位置的偏移量，默认：Pixel(10, 20)
             zoomToAccuracy: true, //定位成功后调整地图视野范围使定位位置及精度范围视野内可见，默认：false
             buttonPosition: 'RB'
         });
         map.addControl(geolocation);
         geolocation.getCurrentPosition();
         AMap.event.addListener(geolocation, 'complete', onComplete); //返回定位信息
         AMap.event.addListener(geolocation, 'error', onError); //返回定位出错信息
     });
     //解析定位结果
     function onComplete(data) {
         var str = [];
         str.push(data.position.getLat());
         str.push(data.position.getLng());
         //document.getElementById('tip').innerHTML = str;
         console.log(data);
     }

     //解析定位错误信息
     function onError(data) {
         console.log("ERROR;");
         console.log(data);
         document.getElementById('tip').innerHTML = '定位失败';
     }*/

    },
    realTimeSituation : function(){
        if(map.my_traffic){
            map.remove(map.my_traffic) //需要时可以移除
            map.my_traffic = null;
            delete map.my_traffic;
            $("._realTimeWeather").hide(300);
        }else{
            map.my_traffic = new AMap.TileLayer.Traffic({
                'autoRefresh': true,     //是否自动刷新，默认为false
                'interval': 30,         //刷新间隔，默认180s
            });
            map.add(map.my_traffic); //通过add方法添加图层
            $("._realTimeWeather").show(300);
        }
    },
    /**
     * 实时天气
     */
    realTimeWeather : function(){

        var tempObj = $("#WeatherMain:visible");
        if(tempObj.length){
            if(window.tipInfoWin){
                map.remove(tipInfoWin);
                window.tipInfoWin = null;
            }
            return;
        }

        AMap.service('AMap.Weather', function() {
            var weather = new AMap.Weather();
            map.plugin(["AMap.Geocoder"], function() {});
            var geocoder = new AMap.Geocoder({
                radius: 1000,
                extensions: "all"
            });
            //查询实时天气信息, 查询的城市到行政级别的城市，如朝阳区、杭州市
            for(var i = 0; i < MarkerArrayList.length && i < 1; i++){
                var positionVO = MarkerArrayList[i].getPosition();
                geocoder.getAddress([positionVO.P,positionVO.O], function(status, result) {
                    if (status === 'complete' && result.info === 'OK') {
                        var response = result;
                        weather.getLive(response.regeocode.addressComponent.city, function(err, data) {
                            var liveData = data;
                            if (!err) {
                                weather.getForecast(response.regeocode.addressComponent.city, function(err, data) {
                                    if (err) {return;}
                                    //console.log(data.forecasts);
                                    var html = '';
                                    html += '<div id="WeatherMain" style="width:330px;height:180px;padding:10px; -moz-box-shadow:2px 2px 8px #808080; -moz-box-shadow:2px 2px 5px #B8B8B8; -webkit-box-shadow:2px 2px 5px #B8B8B8; box-shadow:2px 2px 5px #B8B8B8; ">';
                                    html += '<div style="border-bottom:1px solid #F5F5F5;height:25px;line-height:25px;padding-bottom:5px;">';
                                    html += liveData.city+'<span style="margin-left:5px;font-size:11px;">'+DateUtils.dateFormat(new Date(liveData.reportTime.replace(/\-/g,"/")), "yyyy-MM-dd")+'</span>';
                                    html += '</div>';
                                    var dayWeather = data.forecasts[0];
                                    data.imgSrc = "../../" + waterIcosMap[dayWeather.dayWeather];
                                    html += '<div style="margin-top:5px;">';
                                    html += '<div style="float: left; width: 105px;overflow: hidden; text-align:center; ">';
                                    html += '<p style="margin:0;padding:0; text-align: center; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; color: #666; font-size: 12px;    height: 25px; line-height: 25px; width: 100%;">'+dayWeather.date+'</p>';
                                    html += '<img style="width: 48px;height: 48px;margin: 0 auto 4px;border: none;" src="'+data.imgSrc+'"/>';
                                    html += '<p style="margin:0;padding:0;text-align: center;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;color: #666;font-size: 12px;">'+dayWeather.nightTemp + ' ~ ' + dayWeather.dayTemp + '℃'+'</p>';
                                    html += '<p style="margin:0;padding:0;text-align: center;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;color: #666;font-size: 12px;">'+dayWeather.dayWeather+'</p>';
                                    html += '<p style="margin:0;padding:0;text-align: center;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;color: #666;font-size: 12px;">'+dayWeather.dayWindDir +'</p>';  // dayWeather.dayWindPower

                                    html += '</div>';

                                    dayWeather = data.forecasts[1];
                                    data.imgSrc = "../../" + waterIcosMap[dayWeather.dayWeather];
                                    html += '<div style="float: left; width: 105px;overflow: hidden; text-align:center; ">';
                                    html += '<p style="margin:0;padding:0; text-align: center; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; color: #666; font-size: 12px;    height: 25px; line-height: 25px; width: 100%;">'+dayWeather.date+'</p>';
                                    html += '<img style="width: 48px;height: 48px;margin: 0 auto 4px;border: none;" src="'+data.imgSrc+'"/>';
                                    html += '<p style="margin:0;padding:0;text-align: center;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;color: #666;font-size: 12px;">'+dayWeather.nightTemp + ' ~ ' + dayWeather.dayTemp + '℃'+'</p>';
                                    html += '<p style="margin:0;padding:0;text-align: center;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;color: #666;font-size: 12px;">'+dayWeather.dayWeather+'</p>';
                                    html += '<p style="margin:0;padding:0;text-align: center;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;color: #666;font-size: 12px;">'+dayWeather.dayWindDir + ' ' + dayWeather.dayWindPower + '</p>';
                                    html += '</div>';

                                    dayWeather = data.forecasts[2];
                                    data.imgSrc = "../../" + waterIcosMap[dayWeather.dayWeather];
                                    html += '<div style="float: left; width: 105px;overflow: hidden; text-align:center; ">';
                                    html += '<p style="margin:0;padding:0; text-align: center; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; color: #666; font-size: 12px;    height: 25px; line-height: 25px; width: 100%;">'+dayWeather.date+'</p>';
                                    html += '<img style="width: 48px;height: 48px;margin: 0 auto 4px;border: none;" src="'+data.imgSrc+'"/>';
                                    html += '<p style="margin:0;padding:0;text-align: center;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;color: #666;font-size: 12px;">'+dayWeather.nightTemp + ' ~ ' + dayWeather.dayTemp + '℃'+'</p>';
                                    html += '<p style="margin:0;padding:0;text-align: center;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;color: #666;font-size: 12px;">'+dayWeather.dayWeather+'</p>';
                                    html += '<p style="margin:0;padding:0;text-align: center;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;color: #666;font-size: 12px;">'+dayWeather.dayWindDir + ' ' + dayWeather.dayWindPower + '</p>';
                                    html += '</div>';
                                    html += '</div>';
                                    html += '</div>';
                                    tipInfoWin = new AMap.InfoWindow({
                                        content: html,
                                        offset: new AMap.Pixel(0, -20)
                                    });
                                    tipInfoWin.open(map, positionVO);
                                    //$("#WeatherMain").parents(".amap-info-content:first").addClass("Weather");

                                });

                            }
                        });
                    }
                });
            }
        });
    },

    /**
     * Map地图样式图层,不支持英文改变，因为英文地图也是一个图层。暂时未找到英文的样式图层，若有再加入。
     */
    mapTheme : function amplification(){
        var map = window.map;
        if(map.getMapStyle()=="normal"){
            map.setMapStyle("amap://styles/whitesmoke")
        }else{
            map.setMapStyle("normal")
        }
    },

    switchLang : function(){
        if(map.getLang() != "zh_cn"){
            map.setLang("zh_cn");
        }else{
            map.setLang("en");
        }
    },

    theSpeedLimit : function(){
        var content = "";
    },

    /**
     * 导出轨迹
     */
    exportTrajetory : function(url){
        var trainno = $(".monitorListItemSelect").attr("data-trainno");
        if(!trainno){
            errorNotification({
                simpleMessage : CommonMsg.selectTruckAfterOp
            });
            return;
        }
        showLoading('');
        var href;
        if(url == null || typeof url !='string'){
            href = '../View/ztgl/CommonExportTrajetory.html?trainno='+trainno+'&t='+Math.random();
        }else{
            href = url+'?trainno='+trainno+'&t='+Math.random();
        }
        openDialog({ title : CommonMsg.gjsj_title, id : 'CommonExportTrajetory', width : 950, height : 550, isResize : true, href : href, closable : true });


    },

    /**
     * 导出线路轨迹
     */
    exportTrajetoryByGuid : function(){
        //showLoading('');
        var href = '../View/ztgl/CommonExportTrajetoryByGuid.html?detail_guid='+detail_guid+'&t='+Math.random();
        openDialog({ title : CommonMsg.gjsj_title, id : 'CommonExportTrajetoryByGuid', width : 950, height : 550, isResize : true, href : href, closable : true });


    },

    /**
     * 自定义图标列表
     */
    loadWeatherTypeInfo : function() {
        $.ajax({
            url : "../../ztgl/WeatherForecastController/getWeatherTypeList?t=" + Math.random(),
            data : {},
            type : 'POST',
            async : true,
            contentType : 'application/json;charset=utf-8',
            success : function(datas) {
                pWeatherTypeIcon = datas;
            }
        });
    },

    /**
     * 根据经纬度获取实时&后2天预报天气
     */
    liveForecastWeather: function () {
        if(window.tipInfoWin){
            map.remove(tipInfoWin);
            window.tipInfoWin = null;
            return;
        }
        AMap.service('AMap.Weather', function() {
            var weather = new AMap.Weather();
            map.plugin(["AMap.Geocoder"], function() {});
            var geocoder = new AMap.Geocoder({
                radius: 15000,
                extensions: "all"
            });
            //查询实时天气信息, 查询的城市到行政级别的城市，如朝阳区、杭州市
            for(var i = 0; i < pMarkerArrayList.length && i < 1; i++){
                var positionVO = pMarkerArrayList[i].getPosition();
                geocoder.getAddress([positionVO.O,positionVO.P], function(status, result) {
                    if (status === 'complete' && result.info === 'OK') {
                        var response = result;
                        weather.getLive(response.regeocode.addressComponent.city, function(err, data) {
                            var liveData = data;
                            if (!err) {
                                var html = '';
                                html += '<div id="WeatherMain" style="width:330px;height:180px;padding:10px; ">';
                                html += '<div title='+response.regeocode.formattedAddress+' style="border-bottom:1px solid #F5F5F5;height:25px;line-height:25px;padding-bottom:5px;">';
                                html += response.regeocode.formattedAddress+'<span style="margin-left:5px;font-size:11px;"></span>';
                                html += '</div>';
                                var imgSrc = "../../" + pWeatherTypeIcon[liveData.weather];
                                var date= new Date(Date.parse(liveData.reportTime.replace(/-/g,  "/")));
                                var year = date.getFullYear();
                                var month = date.getMonth() + 1;
                                var day = date.getDate();
                                if (month < 10) {
                                    month = "0" + month;
                                }
                                if (day < 10) {
                                    day = "0" + day;
                                }
                                var nowDate = year + "-" + month + "-" + day;
                                html += '<div style="margin-top:5px;">';
                                html += '<div style="float: left; width: 105px;overflow: hidden; text-align:center; ">';
                                html += '<p style="margin:0;padding:0; text-align: center; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; color: #666; font-size: 12px;    height: 25px; line-height: 25px; width: 100%;">'+ nowDate +'</p>';
                                html += '<img style="width: 48px;height: 48px;margin: 0 auto 4px;border: none;" src="'+imgSrc+'"/>';
                                html += '<p style="margin:0;padding:0;text-align: center;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;color: #666;font-size: 12px;">'+ liveData.temperature + '℃'+'</p>';
                                html += '<p style="margin:0;padding:0;text-align: center;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;color: #666;font-size: 12px;">' + liveData.weather + '</p>';
                                html += '<p style="margin:0;padding:0;text-align: center;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;color: #666;font-size: 12px;">' + liveData.windDirection + ' ' + ((!liveData.windPower) ? '' : liveData.windPower) + '</p>';
                                html += '</div>';
                                weather.getForecast(response.regeocode.addressComponent.city, function(err, data) {
                                    if (err) {return;}
                                    dayWeather = data.forecasts[1];
                                    imgSrc = "../../" + pWeatherTypeIcon[dayWeather.dayWeather];
                                    html += '<div style="float: left; width: 105px;overflow: hidden; text-align:center; ">';
                                    html += '<p style="margin:0;padding:0; text-align: center; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; color: #666; font-size: 12px;    height: 25px; line-height: 25px; width: 100%;">'+dayWeather.date+'</p>';
                                    html += '<img style="width: 48px;height: 48px;margin: 0 auto 4px;border: none;" src="'+imgSrc+'"/>';
                                    html += '<p style="margin:0;padding:0;text-align: center;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;color: #666;font-size: 12px;">'+dayWeather.nightTemp + ' ~ ' + dayWeather.dayTemp + '℃'+'</p>';
                                    html += '<p style="margin:0;padding:0;text-align: center;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;color: #666;font-size: 12px;">'+dayWeather.dayWeather+'</p>';
                                    html += '<p style="margin:0;padding:0;text-align: center;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;color: #666;font-size: 12px;">'+dayWeather.dayWindDir + ' ' +  dayWeather.dayWindPower + '</p>';
                                    html += '</div>';

                                    dayWeather = data.forecasts[2];
                                    imgSrc = "../../" + pWeatherTypeIcon[dayWeather.dayWeather];
                                    html += '<div style="float: left; width: 105px;overflow: hidden; text-align:center; ">';
                                    html += '<p style="margin:0;padding:0; text-align: center; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; color: #666; font-size: 12px;    height: 25px; line-height: 25px; width: 100%;">'+dayWeather.date+'</p>';
                                    html += '<img style="width: 48px;height: 48px;margin: 0 auto 4px;border: none;" src="'+imgSrc+'"/>';
                                    html += '<p style="margin:0;padding:0;text-align: center;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;color: #666;font-size: 12px;">'+dayWeather.nightTemp + ' ~ ' + dayWeather.dayTemp + '℃'+'</p>';
                                    html += '<p style="margin:0;padding:0;text-align: center;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;color: #666;font-size: 12px;">'+dayWeather.dayWeather+'</p>';
                                    html += '<p style="margin:0;padding:0;text-align: center;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;color: #666;font-size: 12px;">'+dayWeather.dayWindDir + ' ' +  dayWeather.dayWindPower + '</p>';
                                    html += '</div>';
                                    html += '</div>';
                                    html += '</div>';
                                    //var marker = new AMap.Marker({map: map,visible: false,position: [positionVO.O,positionVO.P]});
                                    tipInfoWin = new AMap.InfoWindow({
                                        content: html,
                                        offset: new AMap.Pixel(0, -20)
                                    });
                                    tipInfoWin.open(map, positionVO);

                                    $("#WeatherMain").parents(".amap-info-content:first").addClass("Weather");

                                });

                            }
                        });
                    }
                });
            }
        });
    },

    /**
     * 根据地址反解析后 展示 key Milestone
     */
    keyMilestoneByAddress : function(){
        map.plugin(["AMap.Geocoder"], function() {});
        var geocoder = new AMap.Geocoder({
            radius: 15000,
            extensions: "all"
        });
        var markers = [];
        for(var i = 0; i < pKeyMilestoneList.length; i++){
            geocoder.getLocation(pKeyMilestoneList[i].address, function(status, result) {
                if (status === 'complete' && result.info === 'OK') {
                    var geocode = result.geocodes;
                    var marker = new AMap.Marker({
                        map: map,
                        position: [geocode[0].location.O ,geocode[0].location.P ]
                    });
                    marker.on('click', markerClick);
                    marker.emit('click', {target: marker});
                }
                markers.push(marker);
            });
        }
        map.setFitView();
    },

    /**
     * 根据经纬度展示 key Milestone
     */
    keyMilestoneByll : function(){
        map.clearMap();
        var markers = [];
        /*var phone;
        var truckNo;
        var truckName;*/
        var pathArray = [];

        for(var i = 0; i < pKeyMilestoneList.length; i++){
            if(!(pKeyMilestoneList[i].longitude != 0 && pKeyMilestoneList[i].latitude != 0 && pKeyMilestoneList[i].longitude != "" && pKeyMilestoneList[i].latitude != "")){
                continue;
            }
            pathArray.push({
                lnglat : [pKeyMilestoneList[i].longitude,pKeyMilestoneList[i].latitude]
            });
        }
        /*	My_MapUtils.search(pathArray, {
                isLoadBeginEnd : false
            });*/

        for(var i = 0; i < pKeyMilestoneList.length; i++){
            if(!(pKeyMilestoneList[i].longitude != 0 && pKeyMilestoneList[i].latitude != 0 && pKeyMilestoneList[i].longitude != "" && pKeyMilestoneList[i].latitude != "")){
                continue;
            }
            var marker = new AMap.Marker({map: map, position: [pKeyMilestoneList[i].longitude ,pKeyMilestoneList[i].latitude]});
            var phone = pKeyMilestoneList[i].driver_tel == null ? "" : pKeyMilestoneList[i].driver_tel;
            var truckNo = pKeyMilestoneList[i].truck_no == null ? "" : pKeyMilestoneList[i].truck_no;
            var truckName = pKeyMilestoneList[i].driver_name == null ? "" : pKeyMilestoneList[i].driver_name;
            var createtime = pKeyMilestoneList[i].action_date.substr(0,16);
            var html = "";
            html += '<div style="width:230px;height:100px;padding:10px; -moz-box-shadow:2px 2px 8px #808080; -moz-box-shadow:2px 2px 5px #B8B8B8; -webkit-box-shadow:2px 2px 5px #B8B8B8; box-shadow:2px 2px 5px #B8B8B8; ">';
            html += '<div style="height:22px; border-bottom:1px solid #F5F5F5; font-size:11px;color:#00a65a; line-height:22px;font-weight:bold;  ">';
            html += "<div style='width:50%;float:left;font-size:11px;color:#00a65a;font-weight:bold;'>" + (parms["lang"] != "zh" ? pKeyMilestoneList[i].action_name_en : pKeyMilestoneList[i].action_name) + "</div>";
            html += "<div style='width:50%;float:right;font-size:11px;color:#999; text-align:right;'>" + createtime + "</div>";
            html += "</div>";
            html += "<div style='font-size:10px; margin-top:5px;height:60px;color:#666'>";
            html += "<li style='padding:0;margin:0;list-style:none'>" + CommonMsg.truckno + ":" + truckNo + "</li>";
            html += "<li style='padding:0;margin:0;list-style:none'>" + CommonMsg.driver + ":" + truckName + " - " +  phone + "</li>";
            html += "<li style='padding:0;margin:0;list-style:none'>" + CommonMsg.currentPosition + ":" + pKeyMilestoneList[i].address + "</li>";
            html += "</div>";
            html += "<div style='height:22px; font-size:11px;color:#999; line-height:22px;'>";
            html += "<div style='width:70%;float:left;'></div>";
            html += "<div style='width:30%;float:right;   '>";
            if(pKeyMilestoneList[i].electricfence_check == "Y"){
                html += "<div style='width:50px;height:15px;line-height:15px; float:right;background-color:#66CC33;color:white; font-size:9px;  font-weight:bold; margin-right:5px; text-align:center; '>";
                html += "围栏check通过";
                html += "</div>";
            }else if(pKeyMilestoneList[i].electricfence_check == "N"){
                html += "<div style='width:50px;height:15px;line-height:15px; float:right;background-color:#FFCC00;color:white; font-size:9px;  font-weight:bold; margin-right:5px; text-align:center; '>";
                html += "围栏check不通过";
                html += "</div>";
            }else{
                html += "<div'>";
                html += "";
                html += "</div>";
            }

            html += "</div>";
            html += "</div>";
            html += "</div>";
            marker.content = html;
            marker.on('click', markerClick);
            marker.emit('click', {target: marker});
            markers.push(marker);
        }
        map.setFitView();
    },


    /**
     * 公共增加线路
     *
     * [{lnglat : []}]
     */
    search : function(arr, options){
        var notCfNumber = 0;
        var prevObj = {};
        var newList = [];
        for(var i = 0; i < arr.length; i++){
            if(arr[i].lnglat[0] != null && arr[i].lnglat[1] != null){
                if(prevObj.x == arr[i].lnglat[0] && prevObj.y == arr[i].lnglat[1]){
                    //
                }else{
                    notCfNumber++;
                    newList.push(arr[i]);
                }
                prevObj.x = arr[i].lnglat[0];
                prevObj.y = arr[i].lnglat[1];
            }
        }
        arr = newList;




        /*if(notCfNumber <= 30){
            this.search2(arr, options);
            return;
        }*/


        for(var i = 0; i < arr.length; i++){
            arr[i] = {
                x : Number(arr[i].lnglat[0]),
                y : Number(arr[i].lnglat[1]),
                sp : 10,
                ag : 0,
                tm : 500+i,
                truck : arr[i].lnglat[2]
            }
        }
        /*if(arr.length > 0){
            arr[0].tm = 1478031031;
        }*/

        AMap.plugin('AMap.GraspRoad',function(){
            var grasp = new AMap.GraspRoad();
            Recursion(grasp,arr);
        });
        if(options){
            var pixel = new AMap.Pixel(-16,-33);
            if(options.begin_local && options.begin_local.longitude && options.begin_local.latitude){
                var address = options.begin_local.address || "";
                if(address){
                    var content = "<div class = 'colorA' style='margin-left: 30px;width: auto;border: inherit;background-color: inherit;'><div style='text-align:center'></div><div style='font-size:12px;  font-weight:bold; color:#00a7d0; padding:5px 10px; background:#fff;border-radius:2px;box-shadow: 1px 1px 4px 1px #999; display:block;text-align:center;width:150px;white-space:normal;'>"+address+"</div></div>";
                    var marker = new AMap.Marker({
                        position: new AMap.LngLat(options.begin_local.longitude, options.begin_local.latitude),
                        offset: pixel,
                        icon: '../../Resource/images/truckImages/start.png'
                    });
                    marker.setLabel({
                        //offset: new AMap.Pixel(15, 15),
                        content: content
                    });
                }else{
                    marker = new AMap.Marker({
                        offset: pixel,
                        position: new AMap.LngLat(options.begin_local.longitude, options.begin_local.latitude),
                        icon: '../../Resource/images/truckImages/start.png'
                    });
                }
                map.add(marker);
            }

            if(options.end_local && options.end_local.longitude && options.end_local.latitude){
                var address = options.end_local.address || "";
                if(address){
                    var content = "<div class = 'colorA' style='margin-left: 30px;width: auto;border: inherit;background-color: inherit;'><div style='text-align:center'></div><div style='font-size:12px;  font-weight:bold; color:#00a7d0; padding:5px 10px; background:#fff;border-radius:2px;box-shadow: 1px 1px 4px 1px #999; display:block;text-align:center;width:150px;white-space:normal;'>"+address+"</div></div>";
                    var marker = new AMap.Marker({
                        offset: pixel,
                        position: new AMap.LngLat(options.end_local.longitude, options.end_local.latitude),
                        icon: '../../Resource/images/truckImages/end.png'
                    });
                    marker.setLabel({
                        content: content
                    });
                }else{
                    marker = new AMap.Marker({
                        offset: pixel,
                        position: new AMap.LngLat(options.end_local.longitude, options.end_local.latitude),
                        icon: '../../Resource/images/truckImages/end.png'
                    });

                }
                map.add(marker);
            }
        }
    },
    search2 : function(arr, options,policy){
        var map = window.map;
        var truckOptions = {
            map : map,
            policy : policy,

            outlineColor : "#0091FF",
                size : 4,
            hideMarkers : true,		//设置隐藏路径规划的起始点图标，设置为true：隐藏图标；设置false：
            showTraffic : false,	//设置是否显示实时路况信息，默认设置为true。 显示绿色代表畅通，黄色代表轻微拥堵，红色代表比较拥堵，灰色表示无路况信息。
            autoFitView : false 	//用于控制在路径规划结束后，是否自动调整地图视野使绘制的路线处于视口的可见范围
        };

        function fun(path){
				var driving = new AMap.TruckDriving(truckOptions);
            driving.search(path,function(status, result) {
                if(status == "complete" && result.info == "OK"){
                }else{
                    fun(path);		//如果失败，重新再次操作
                }
            });
        }
        var newArray = [];
        for(var i = 0; i < arr.length; i++){
            newArray.push(arr[i]);
            if(((i+1)%15) == 0 || (i+1) == arr.length){
                fun(JSON.parse(JSON.stringify(newArray)));
                newArray = [arr[i]];
            }
        }
        if(options){
            var pixel = new AMap.Pixel(-16,-33);
            if(options.begin_local && options.begin_local.longitude && options.begin_local.latitude){
                var address = options.begin_local.address || "";
                if(address){
                    var content = "<div class = 'colorA' style='margin-left: 30px;width: auto;border: inherit;background-color: inherit;'><div style='text-align:center'></div><div style='font-size:12px;  font-weight:bold; color:#00a7d0; padding:5px 10px; background:#fff;border-radius:2px;box-shadow: 1px 1px 4px 1px #999; display:block;text-align:center;width:150px;white-space:normal;'>"+address+"</div></div>";
                    var marker = new AMap.Marker({
                        position: new AMap.LngLat(options.begin_local.longitude, options.begin_local.latitude),
                        offset: pixel,
                        icon: '../../Resource/images/truckImages/start.png'
                    });
                    marker.setLabel({
                        //offset: new AMap.Pixel(15, 15),
                        content: content
                    });
                }else{
                    marker = new AMap.Marker({
                        offset: pixel,
                        position: new AMap.LngLat(options.begin_local.longitude, options.begin_local.latitude),
                        icon: '../../Resource/images/truckImages/start.png'
                    });
                }
                map.add(marker);
            }

            if(options.end_local && options.end_local.longitude && options.end_local.latitude){
                var address = options.end_local.address || "";
                if(address){
                    var content = "<div class = 'colorA' style='margin-left: 30px;width: auto;border: inherit;background-color: inherit;'><div style='text-align:center'></div><div style='font-size:12px;  font-weight:bold; color:#00a7d0; padding:5px 10px; background:#fff;border-radius:2px;box-shadow: 1px 1px 4px 1px #999; display:block;text-align:center;width:150px;white-space:normal;'>"+address+"</div></div>";
                    var marker = new AMap.Marker({
                        offset: pixel,
                        position: new AMap.LngLat(options.end_local.longitude, options.end_local.latitude),
                        icon: '../../Resource/images/truckImages/end.png'
                    });
                    marker.setLabel({
                        content: content
                    });
                }else{
                    marker = new AMap.Marker({
                        offset: pixel,
                        position: new AMap.LngLat(options.end_local.longitude, options.end_local.latitude),
                        icon: '../../Resource/images/truckImages/end.png'
                    });

                }
                map.add(marker);
            }
        }

    },

    speedLine : function(data){
        var getXFormat = function(dataArray){
            var xFormatter = function(){return ""};

            //初始化时生成X轴时间格式
            if(dataArray!=null && dataArray!=undefined && dataArray.length>0){

                //时间默认前三位,长度大于3则比较0和2位置的时间
                if(dataArray.length>2){
                    xFormatter = getFormatWithYear(dataArray[0],dataArray[dataArray.length-1]);
                } else if(dataArray.length==2){
                    xFormatter = getFormatWithYear(dataArray[1],dataArray[0]);
                } else {
                    xFormatter = getFormatWithYear(dataArray[0],dataArray[0]);
                }
            }
            return xFormatter;
        }

        var getFormatWithYear = function(obj1,obj2){

            var xFormatter = function(){return ""};
            //获取年份
            var year1 = "";
            var year2 = "";
            //获取年份
            try{
                year1 = obj1.substring(0,5);
                year2 = obj2.substring(0,5);
            }catch(e){
                return xFormatter;
            }

            //比较年份
            if(year1==year2){

                //截取日期
                var day1 = obj1.substring(8,10);
                var day2 = obj2.substring(8,10);

                //比较日期
                if(day1==day2){

                    //日期相同则显示时分
                    xFormatter = function(value){return value.substring(11);}

                } else {

                    //日期不同年份相同则显示月日
                    xFormatter = function(value){return value.substring(5,10);}

                }
            } else {

                //年份不同则显示年份
                xFormatter = function(value){return value.substring(0,10);}
            }

            return xFormatter;
        }

        var xFormatter = null;
        var truck_id = data.truck_id;
        var title = data.title;
        var overSpeed = data.overSpeed;
        var seriesArray = data.seriesArray;
        var dataArray = data.dataArray;

        xFormatter = getXFormat(dataArray);
        var option = {
            calculable: true,
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'line',

                },
                formatter: function(param){
                    console.log(arguments);
                    param = param[0];
                    if(!param.name){
                        return "";
                    }
                    return "Date:"+param.name.substring(0,10)+"<br/>"+"Time:"+param.name.substring(11)+"<br/>Speed:"+Number(param.data)+"km/h";
                }
            },
            dataZoom : [{
                show : true,
                realtime : true,
                startValue: dataArray==null||dataArray==undefined||dataArray.length<=0?"":dataArray[0],
                endValue : dataArray==null||dataArray==undefined||dataArray.length<=0?"":dataArray[dataArray.length-1],
                textStyle: {
                    fontSize: 13,
                    fontWeight: 'bolder',
                    color: '#333'
                }
            },{
                type: 'inside',
                realtime : true,
            }],
            xAxis: {
                type: 'category',
                data: dataArray,
                axisLabel: {
                    formatter: xFormatter,
                    inerval : 0,
                    rotate : -30
                }
            },
            grid : {
                y2 : 100
            },
            yAxis: {
                type: 'value'
            },
            series: [{
                data: seriesArray,
                type: 'line',
                markLine: {
                    data: [
                        { yAxis : overSpeed}
                    ],lineStyle: {
                        normal: {
                            color: 'red'
                        }
                    }
                },
                lineStyle: {
                    normal: {
                        color: '#24C4C6'
                    }
                },
                symbolSize: 0.1,
                itemStyle: {
                    normal: {
                        color: 'RGB(121, 151, 117)'
                    }
                }
            }]
        };
        var html = "";
        html += '<div id="container" class="echarts_container" style="height: 347px;width: 420px;"></div>';
        html += '<div class="echarts_container_top_title"><font style="color:rgb(10, 140, 255);">'+title+'</font>	<font size="2">'+truck_id+'</font><a class="amap-info-close" href="javascript: void(0)" style="right: 3%;top: -20%;">×</a></div>';
        var containerMainObj = $("#containerMain");
        if(containerMainObj.length == 0){
            $("body").append('<div style="width: 100%;height: 100%;" id="containerMain"></div>');
            containerMainObj = $("#containerMain");
        }
        $("#containerMain").html(html);
        var myChart = echarts.init(document.getElementById("container"));

        myChart.on('datazoom', function (params){

            //设置end始终在底部
            option.dataZoom[0].end=100;

            //设置start为当前位置
            option.dataZoom[0].start=params.start==undefined?params.batch[0].start:params.start;

            //计算startValue
            var start = option.dataZoom[0].start;
            var startValues = option.xAxis.data;
            var starValue = startValues[Math.round(start/100*startValues.length)];

            // 根据时间计算当前显示文本
            var XFormat = getFormatWithYear(starValue,option.dataZoom[0].endValue);

            //设置X轴显示文本
            option.xAxis.axisLabel.formatter = XFormat;

            //刷新
            myChart.setOption(option);
        });
        myChart.setOption(option, true);
        $(".echarts_container").css("background", "white");
        $("#containerMain .amap-info-close").bind("click", function(){
            //关闭超速折线图
            $("#containerMain").remove();
            var theSpeedLimitArray = window.theSpeedLimitArray || [];
            for(var i = 0; i < theSpeedLimitArray.length; i++){
                //清除上次的超速标记
                map.remove(theSpeedLimitArray[i]);
            }
        });
    },
    onTrack : function(){
        if($(".monitorListItemSelect").length == 0){
            if(CommonMsg){
                errorNotification({
                    simpleMessage : CommonMsg.selectTruckAfterOp
                });
            }
            return;
        }

        var longitude = MarkerArrayList[0].getPosition().O;
        var latitude = MarkerArrayList[0].getPosition().P;

        map.setZoomAndCenter(5, [longitude, latitude]);

    },
    routeStartEnd : function(guid){
        var markers = [];
        var requestParam =
            {
                detail_guid : guid
            };
        $.ajax({
            url : "../../ztgl/AFCInboundTracking/getYwOrderMostly?t="+ Math.random(),
            data : requestParam,
            type : "POST",
            success : function(dataObj)
            {
                if (isServerResultDataPass(dataObj))
                {
                    var data = dataObj.resultDataFull;
                    if(data == null || data[0] == null){
                        errorNotification({
                            simpleMessage : CommonMsg.startendIsEmpty
                        });
                        return;
                    }
                    if(data.length > 1){
                        var start = [data[i].longitude, data[i].latitude];
                        var marker = new AMap.Marker({
                            position: new AMap.LngLat(data[0].longitude, data[0].latitude),
                            icon: '../../Resource/images/truckImages/start.png'
                        });
                        map.add(marker);
                        marker = new AMap.Marker({
                            position: new AMap.LngLat(data[data.length-1].longitude, data[data.length-1].latitude),
                            icon: '../../Resource/images/truckImages/end.png'
                        });
                        map.add(marker);
                    }

                }else
                {
                    FailResultDataToTip(dataObj);
                }
            }
        });
    }

};

