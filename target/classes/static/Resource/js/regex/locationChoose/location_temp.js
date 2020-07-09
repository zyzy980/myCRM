!function ($) {
    $.extend({
        _jsonp: {
            scripts: {},
            counter: 1,
            charset: "gb2312",
            head: document.getElementsByTagName("head")[0],
            name: function (callback) {
                var name = "_jsonp_" + (new Date).getTime() + "_" + this.counter;
                this.counter++;
                var cb = function (json) {
                    eval("delete " + name),
					callback(json),
					$._jsonp.head.removeChild($._jsonp.scripts[name]),
					delete $._jsonp.scripts[name]
                };
                return eval(name + " = cb"),
				name
            },
            load: function (a, b) {
                var c = document.createElement("script");
                c.type = "text/javascript",
				c.charset = this.charset,
				c.src = a,
				this.head.appendChild(c),
				this.scripts[b] = c
            }
        },
        getJSONP: function (a, b) {
            var c = $._jsonp.name(b),
			a = a.replace(/{callback};/, c);
            return $._jsonp.load(a, c),
			this
        }
    })
}
(jQuery);

// 首次只读取省和市，区和街道动态读取
//var iplocation = [
//{ sn: "16452", id: "110000", parentid: "0", name: "北京", position: "0" },
//{ sn: "16453", id: "110100", parentid: "110000", name: "北京市", position: "0-110000" },
//{ sn: "16454", id: "110101", parentid: "110100", name: "东城区", position: "0-110000-110100" },
//{ sn: "16488", id: "130000", parentid: "0", name: "河北省", position: "0" },
//{ sn: "16489", id: "130100", parentid: "130000", name: "石家庄市", position: "0-130000" },
//{ sn: "16490", id: "130102", parentid: "130100", name: "长安区", position: "0-130000-130100" },
//{ sn: "20741", id: "130102001", parentid: "130102", name: "建北街道", position: "0-130000-130100-130102" }
//];

//var locationArray = Enumerable.From(iplocation);

//var cName = "ipLocation";
//var currentLocation = "北京";
//var currentProvinceId = 1;

//根据省份ID获取名称
//function getNameById(provinceId) {
//    for (var o in iplocation) {
//        if (iplocation[o] && iplocation[o].id == provinceId) {
//            return o;
//        }
//    }
//}

//var isUseServiceLoc = true; //是否默认使用服务端地址

function getAreaList(sourceArray, parentid) {
    var result = Enumerable.From(sourceArray).Where(function (i) { return i.PARENTID == parentid; }).ToArray()
    var html = ["<ul class='area-list'>"];
    var longhtml = [];
    var longerhtml = [];
    if (result && result.length > 0) {
        for (var i = 0, j = result.length; i < j; i++) {
            result[i].SHORTNAME = result[i].SHORTNAME.replace(" ", "");
            if (result[i].SHORTNAME.length > 12) {
                longerhtml.push("<li class='longer-area'><a href='#none' data-position='" + result[i].POSITION + "'  data-value='" + result[i].ID + "'>" + result[i].SHORTNAME + "</a></li>");
            }
            else if (result[i].SHORTNAME.length > 5) {
                longhtml.push("<li class='long-area'><a href='#none' data-position='" + result[i].POSITION + "'  data-value='" + result[i].ID + "'>" + result[i].SHORTNAME + "</a></li>");
            }
            else {
                html.push("<li><a href='#none' data-position='" + result[i].POSITION + "'  data-value='" + result[i].ID + "'>" + result[i].SHORTNAME + "</a></li>");
            }
        }
    }

    html.push(longhtml.join(""));
    html.push(longerhtml.join(""));
    html.push("</ul>");
    return html.join("");
}

//function cleanKuohao(str) {
//    if (str && str.indexOf("(") > 0) {
//        str = str.substring(0, str.indexOf("("));
//    }
//    if (str && str.indexOf("（") > 0) {
//        str = str.substring(0, str.indexOf("（"));
//    }
//    return str;
//}

//function getStockOpt(id, name) {
//    if (currentAreaInfo.currentLevel == 3) {
//        currentAreaInfo.currentAreaId = id;
//        currentAreaInfo.currentAreaName = name;
//        if (!page_load) {
//            currentAreaInfo.currentTownId = 0;
//            currentAreaInfo.currentTownName = "";
//        }
//    }
//    else if (currentAreaInfo.currentLevel == 4) {
//        currentAreaInfo.currentTownId = id;
//        currentAreaInfo.currentTownName = name;
//    }
//    //添加20140224
//    $('#store-selector').removeClass('hover');
//    //setCommonCookies(currentAreaInfo.currentProvinceId,currentLocation,currentAreaInfo.currentCityId,currentAreaInfo.currentAreaId,currentAreaInfo.currentTownId,!page_load);
//    if (page_load) {
//        page_load = false;
//    }
//    //替换gSC
//    var address = currentAreaInfo.currentProvinceName + currentAreaInfo.currentCityName + currentAreaInfo.currentAreaName + currentAreaInfo.currentTownName;
//    $("#store-selector .text div").html(currentAreaInfo.currentProvinceName + cleanKuohao(currentAreaInfo.currentCityName) + cleanKuohao(currentAreaInfo.currentAreaName) + cleanKuohao(currentAreaInfo.currentTownName)).attr("title", address);
//}

//function getAreaListcallback(r) {
//    currentDom.html(getAreaList(r));
//    if (currentAreaInfo.currentLevel >= 2) {
//        currentDom.find("a").click(function () {
//            if (page_load) {
//                page_load = false;
//            }
//            if (currentDom.attr("id") == "stock_area_item") {
//                currentAreaInfo.currentLevel = 3;
//            }
//            else if (currentDom.attr("id") == "stock_town_item") {
//                currentAreaInfo.currentLevel = 4;
//            }
//            getStockOpt($(this).attr("data-value"), $(this).html());
//        });
//        if (page_load) { //初始化加载
//            currentAreaInfo.currentLevel = currentAreaInfo.currentLevel == 2 ? 3 : 4;
//            if (currentAreaInfo.currentAreaId && new Number(currentAreaInfo.currentAreaId) > 0) {
//                getStockOpt(currentAreaInfo.currentAreaId, currentDom.find("a[data-value='" + currentAreaInfo.currentAreaId + "']").html());
//            }
//            else {
//                getStockOpt(currentDom.find("a").eq(0).attr("data-value"), currentDom.find("a").eq(0).html());
//            }
//        }
//    }
//}

function chooseProvince(sourceArray, provinceId, provinceName, position) {
    sourceArray = Enumerable.From(sourceArray).Where(function (i) { return i.LAYER == 2; }).ToArray();
    provinceContainer.hide();
    currentAreaInfo.currentProvinceId = provinceId;
    currentAreaInfo.currentProvinceName = provinceName;
    currentAreaInfo.currentPosition = position;
    currentAreaInfo.currentProvincePosition = position;
    areaTabContainer.eq(0).removeClass("curr").find("em").html(provinceName);
    areaTabContainer.eq(1).addClass("curr").show().find("em").html("请选择");
    areaTabContainer.eq(2).hide();
    areaTabContainer.eq(3).hide();
    cityContainer.show();
    areaContainer.hide();
    townaContainer.hide();
    if (provinceId) {
        cityContainer.html(getAreaList(sourceArray, provinceId));
        cityContainer.find("a").click(function () {
            if (page_load) {
                page_load = false;
            }

            //$("#txtSearchAddress").unbind("click");
            chooseCity($(this).attr("data-value"), $(this).html(), $(this).attr("data-position"));
        });
    }

    getChooseArea();
}

function chooseCity(cityId, cityName, position) {
    provinceContainer.hide();
    cityContainer.hide();
    currentAreaInfo.currentCityId = cityId;
    currentAreaInfo.currentCityName = cityName;
    currentAreaInfo.currentPosition = position;
    currentAreaInfo.currentCityPosition = position;
    areaTabContainer.eq(1).removeClass("curr").find("em").html(cityName);
    areaTabContainer.eq(2).addClass("curr").show().find("em").html("请选择");
    areaTabContainer.eq(3).hide();
    areaContainer.show().html("<div class='iloading'>正在加载中，请稍候...</div>");
    townaContainer.hide();

    var parmsArray = [
          { name: 'LAYER', value: 3 },
          { name: 'parentid', value: cityId }
        ];
    var customSearchFilters = formatSearchParames(parmsArray);
    $.getJSON("/ServiceHandler/SysInfo/SysAddress.ashx?" + Math.random(), { action: "GetList", customSearchFilters: customSearchFilters },
                    function (data) {
                        if (isServerResultDataPass(data)) {
                            areaContainer.html(getAreaList(data.ResultDataFull, cityId));
                            areaContainer.find("a").click(function () {
                                if (page_load) {
                                    page_load = false;
                                }

                                //$("#txtSearchAddress").unbind("click");
                                chooseArea($(this).attr("data-value"), $(this).html(), $(this).attr("data-position"));
                            });
                        }
                    });

    getChooseArea();
}

function chooseArea(areaId, areaName, position) {
    provinceContainer.hide();
    cityContainer.hide();
    areaContainer.hide();
    currentAreaInfo.currentAreaId = areaId;
    currentAreaInfo.currentAreaName = areaName;
    currentAreaInfo.currentPosition = position;
    currentAreaInfo.currentAreaPosition = position;
    areaTabContainer.eq(2).removeClass("curr").find("em").html(areaName);
    areaTabContainer.eq(3).addClass("curr").show().find("em").html("请选择");
    townaContainer.show().html("<div class='iloading'>正在加载中，请稍候...</div>");
    var parmsArray = [
          { name: 'LAYER', value: 4 },
          { name: 'parentid', value: areaId }
        ];
    var customSearchFilters = formatSearchParames(parmsArray);
    $.getJSON("/ServiceHandler/SysInfo/SysAddress.ashx?" + Math.random(), { action: "GetList", customSearchFilters: customSearchFilters },
                    function (data) {
                        if (isServerResultDataPass(data)) {
                            townaContainer.html(getAreaList(data.ResultDataFull, areaId));
                            townaContainer.find("a").click(function () {
                                if (page_load) {
                                    page_load = false;
                                }

                                //$("#txtSearchAddress").unbind("click");
                                chooseTowna($(this).attr("data-value"), $(this).html(), $(this).attr("data-position"));
                            });
                        }
                    });

    getChooseArea();
}

function chooseTowna(townaId, townaName, position) {
    // 直接关闭层
    //    provinceContainer.hide();
    //    cityContainer.hide();
    //    areaContainer.hide();
    //    areaTabContainer.hide();
    currentAreaInfo.currentTownId = townaId;
    currentAreaInfo.currentTownName = townaName;
    currentAreaInfo.currentPosition = position;
    currentAreaInfo.currentTownPosition = position;
    areaTabContainer.eq(3).addClass("curr").show().find("em").html(townaName);
    $('#store-selector').hide();
    getChooseArea();
}

function getChooseArea() {
    if (currentAreaInfo) {
        var address = currentAreaInfo.currentProvinceName + " " + currentAreaInfo.currentCityName + " " + currentAreaInfo.currentAreaName + " " + currentAreaInfo.currentTownName;
        $("#txtSearchAddress").val("");
        $("#txtSearchAddress").val(address);
        $("#hidSearchAddress").val(currentAreaInfo.currentPosition);
    }
}


var page_load = true;
var areaTabContainer = null;
var provinceContainer = null;
var cityContainer = null;
var areaContainer = null;
var townaContainer = null;
// 当前选择的区域
var currentAreaInfo = { "currentPosition": -1, "currentProvinceId": -1, "currentProvinceName": "", "currentProvincePosition": -1,
    "currentCityId": -1, "currentCityName": "", "currentCityPosition": -1, "currentAreaId": -1, "currentAreaName": "", "currentAreaPosition": -1,
    "currentTownId": -1, "currentTownName": "", "currentTownPosition": -1
};
(function () {
    var locationArray = null;
    var parmsArray = [
          { name: 'LAYER', value: "1,2", op: "in" }
        ];
    var customSearchFilters = formatSearchParames(parmsArray);
    $.getJSON("/ServiceHandler/SysInfo/SysAddress.ashx?" + Math.random(), { action: "GetList", customSearchFilters: customSearchFilters },
                    function (data) {
                        if (isServerResultDataPass(data)) {
                            locationArray = data.ResultDataFull;
                            var provinceHtml = '<div class="content"><div data-widget="tabs" class="m JD-stock" id="JD-stock">'
								+ '<div class="mt">'
								+ '    <ul class="tab">'
								+ '        <li data-index="0" data-widget="tab-item" class="curr"><a href="#none" class="hover"><em>请选择</em><i></i></a></li>'
								+ '        <li data-index="1" data-widget="tab-item" style="display:none;"><a href="#none" class=""><em>请选择</em><i></i></a></li>'
								+ '        <li data-index="2" data-widget="tab-item" style="display:none;"><a href="#none" class=""><em>请选择</em><i></i></a></li>'
								+ '        <li data-index="3" data-widget="tab-item" style="display:none;"><a href="#none" class=""><em>请选择</em><i></i></a></li>'
								+ '    </ul>'
								+ '    <div class="stock-line"></div>'
								+ '</div>'
								+ '<div class="mc" data-area="0" data-widget="tab-content" id="stock_province_item">'
								+ '    <ul class="area-list">';

                            var provinceArray = Enumerable.From(locationArray).Where(function (i) { return i.PARENTID == 0; }).ToArray();
                            for (var i = 0; i < provinceArray.length; i++) {
                                if (provinceArray[i].SHORTNAME.length > 12) {
                                    provinceHtml += "<li class='longer-area'><a href='#none' data-position='" + provinceArray[i].POSITION + "' data-value='" + provinceArray[i].ID + "'>" + provinceArray[i].SHORTNAME + "</a></li>";
                                }
                                else if (provinceArray[i].SHORTNAME.length > 5) {
                                    provinceHtml += "<li class='long-area'><a href='#none' data-position='" + provinceArray[i].POSITION + "' data-value='" + provinceArray[i].ID + "'>" + provinceArray[i].SHORTNAME + "</a></li>";
                                }
                                else {
                                    provinceHtml += "<li><a href='#none' data-position='" + provinceArray[i].POSITION + "' data-value='" + provinceArray[i].ID + "'>" + provinceArray[i].SHORTNAME + "</a></li>";
                                }
                            }

                            provinceHtml += '    </ul>'
								+ '</div>'
								+ '<div class="mc" data-area="1" data-widget="tab-content" id="stock_city_item"></div>'
								+ '<div class="mc" data-area="2" data-widget="tab-content" id="stock_area_item"></div>'
								+ '<div class="mc" data-area="3" data-widget="tab-content" id="stock_town_item"></div>'
								+ '</div></div>';

                            $("#store-selector").append(provinceHtml);
                            areaTabContainer = $("#JD-stock .tab li");
                            provinceContainer = $("#stock_province_item");
                            cityContainer = $("#stock_city_item");
                            areaContainer = $("#stock_area_item");
                            townaContainer = $("#stock_town_item");

                            $("#txtSearchAddress").unbind("click").bind("click", function () {
                                var x = $(this).position().top + $(this).height()+3;
                                var y = $(this).position().left;
                                $("#store-selector").css("top", x);
                                $("#store-selector").css("left", y);
                                $("#store-selector").show();
                                $("#store-selector .content,#JD-stock").show();
                            });
                            $(document).bind("click", function (e) {
                                var target = $(e.target);
                                if (target.closest("#store-selector,#txtSearchAddress").length == 0) {
                                    $("#store-selector").hide();
                                };
                                e.stopPropagation();
                            })

                            areaTabContainer.eq(0).find("a").click(function () {
                                areaTabContainer.removeClass("curr");
                                areaTabContainer.eq(0).addClass("curr").show();
                                provinceContainer.show();
                                cityContainer.hide();
                                areaContainer.hide();
                                townaContainer.hide();
                                areaTabContainer.eq(1).hide();
                                areaTabContainer.eq(2).hide();
                                areaTabContainer.eq(3).hide();

                                currentAreaInfo.currentPosition = currentAreaInfo.currentProvincePosition;
                                currentAreaInfo.currentCityId = -1;
                                currentAreaInfo.currentCityName = "";
                                currentAreaInfo.currentCityPosition = -1;

                                currentAreaInfo.currentAreaId = -1;
                                currentAreaInfo.currentAreaName = "";
                                currentAreaInfo.currentAreaPosition = -1;

                                currentAreaInfo.currentTownId = -1;
                                currentAreaInfo.currentTownName = "";
                                currentAreaInfo.currentTownPosition = -1;

                                getChooseArea();
                            });
                            areaTabContainer.eq(1).find("a").click(function () {
                                areaTabContainer.removeClass("curr");
                                areaTabContainer.eq(1).addClass("curr").show();
                                provinceContainer.hide();
                                cityContainer.show();
                                areaContainer.hide();
                                townaContainer.hide();
                                areaTabContainer.eq(2).hide();
                                areaTabContainer.eq(3).hide();

                                currentAreaInfo.currentPosition = currentAreaInfo.currentCityPosition;
                                currentAreaInfo.currentAreaId = -1;
                                currentAreaInfo.currentAreaName = "";
                                currentAreaInfo.currentAreaPosition = -1;

                                currentAreaInfo.currentTownId = -1;
                                currentAreaInfo.currentTownName = "";
                                currentAreaInfo.currentTownPosition = -1;

                                getChooseArea();
                            });
                            areaTabContainer.eq(2).find("a").click(function () {
                                areaTabContainer.removeClass("curr");
                                areaTabContainer.eq(2).addClass("curr").show();
                                provinceContainer.hide();
                                cityContainer.hide();
                                areaContainer.show();
                                townaContainer.hide();
                                areaTabContainer.eq(3).hide();

                                currentAreaInfo.currentPosition = currentAreaInfo.currentAreaPosition;
                                currentAreaInfo.currentTownId = -1;
                                currentAreaInfo.currentTownName = "";
                                currentAreaInfo.currentTownPosition = -1;

                                getChooseArea();
                            });
                            provinceContainer.find("a").click(function () {
                                if (page_load) {
                                    page_load = false;
                                }
                                //$("#txtSearchAddress").unbind("click");
                                chooseProvince(locationArray, $(this).attr("data-value"), $(this).html(), $(this).attr("data-position"));
                            }).end();

                        }
                    });
})();