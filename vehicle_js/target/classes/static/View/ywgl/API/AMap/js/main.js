window.onload = function () {
    var map = new AMap.Map('container', {
        resizeEnable: true,
        zoom: 12,
        center: [118.756376, 32.052573]
    });
//地图内容
    map.setFeatures(['bg', 'building', 'road', 'point'])
//地图空间
    AMap.plugin(['AMap.ToolBar', 'AMap.Scale', 'AMap.MapType'],
        function () {
            map.addControl(new AMap.ToolBar());

            map.addControl(new AMap.Scale());
        });
//覆盖物
    var marker = new AMap.Marker({
        position: [118.756376, 32.052573]
    });
    marker.setMap(map);
    var circle = new AMap.Circle({
        center: [118.756376, 32.052573],
        radius: 100,
        fillOpacity: 0.2,
        strokeWeight: 1
    })
    circle.setMap(map);
//自定义窗体
    var infowindow;
    map.plugin('AMap.AdvancedInfoWindow', function () {
        infowindow = new AMap.AdvancedInfoWindow({
            panel: 'panel',
            placeSearch: true,
            asOrigin: true,
            asDestination: true
        });
    });

    function clearMarker() {
        if (marker) {
            marker.setMap(null);
            marker = null;
        }
        if (infowindow) {
            infowindow.close()
        }
    }

//输入提示
    var autoOptions = new AMap.Autocomplete({
        input: "tipinput"
    });
//城市搜索
    var auto = new AMap.Autocomplete(autoOptions);
    var placeSearch = new AMap.PlaceSearch({
        map: map
    });  //构造地点查询类
    AMap.event.addListener(auto, "select", select);//注册监听，当选中某条记录时会触发
    function select(e) {
        placeSearch.setCity(e.poi.adcode);
        placeSearch.search(e.poi.name);  //关键字查询查询
    }

    $('#show').on('click', function () {
        clearMarker()
        $('.pageShow').slideToggle();
    })

    $('#confirm').on('click', function () {
    	var address = $("#tipinput").val();
    	var name = location.search.substr(1).split("=")[1];
    	var $parent = $(window.parent.document)
    	$parent.find("[id="+name+"]").prepend(generalOption(address,address,true));
    	window.parent.model.obj.formField.detail.orderMostly.location[name]=address;
    	$parent.find("#mask").show();
    	$parent.find("#maps").remove();
    });
}
