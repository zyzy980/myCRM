//需要传入地址转经纬度
function geocoder(address) {
	var geocoder;
    AMap.plugin('AMap.Geocoder',function(){//异步
    	geocoder = new AMap.Geocoder({});
    });
    
    //地理编码,返回地理编码结果
   	var resultPosition;
    geocoder.getLocation(address, function(status, result) {
        if (status === 'complete' && result.info === 'OK') {
            resultPosition = geocoder_CallBack(result);
            //定义全局变量接受经纬度
            positionList.push(resultPosition);
        }
	    return positionList;;
    });
}
//地理编码返回结果展示
function geocoder_CallBack(data) {
    //地理编码结果数组
    var geocode = data.geocodes;
	var positionList = "";
    for (var i = 0; i < geocode.length; i++) {
    	positionList = geocode[i].location.getLng()+","+geocode[i].location.getLat();
    }
	return positionList;
}

