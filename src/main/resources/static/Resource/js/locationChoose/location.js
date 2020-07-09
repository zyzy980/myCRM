(function ($) {
    $.fn.extend({
        "bindLocationChoose": function (options) {
            var areaTabContainer = null;
            var provinceContainer = null;
            var cityContainer = null;
            var areaContainer = null;
            var townaContainer = null;
            var oftenUseContainer = null;
            var currentAreaInfo = { "currentPosition": -1, "currentPositionDisplay": "", "currentId": -1, "currentIdDisplay": "", "currentProvinceId": -1, "currentProvinceName": "", "currentProvincePosition": -1,
                "currentCityId": -1, "currentCityName": "", "currentCityPosition": -1, "currentAreaId": -1, "currentAreaName": "", "currentAreaPosition": -1,
                "currentTownId": -1, "currentTownName": "", "currentTownPosition": -1
            };
            var defaluts = {
                url: '/ServiceHandler/SysInfo/SysAddress.ashx',
                locationChooseBoxId: 'store-selector',
                lastChooseLevel: 4,
                useCache: false,
                cacheValue: [],
                cacheCookieName: 'othenUseArea',
                // 1:省，2：市；3：区；4：街道
                //                leastLevel: 2,
                afterChooseForPositionEvent: function (chooseLocationPositionCode, chooseLocationPositionName, chooseLocationId, chooseLocationIdName) {
                }
            };
            
            var opt = $.extend({}, defaluts, options);

            //将控件绑定给textbox类型控件
            $(this).each(function () {
                var $this = $(this);
                var locationArray = null;
                var parmsArray = [
                    { name: 'LAYER', value: "1,2", op: "in" }
                ];
                var customSearchFilters = formatSearchParames(parmsArray);
                //opt.cacheValue = getOthenUseArea(opt);
                $.getJSON(opt.url + "?" + Math.random(), { action: "GetList", customSearchFilters: customSearchFilters },
                    function (data) {
                        if (data != null && data.ResultCode == 0 && data.ResultDataFull != null) {
                            locationArray = data.ResultDataFull;
                            var provinceHtml = '<div class="content"><div data-widget="tabs" class="JD-stock" id="' + opt.locationChooseBoxId + '_JD-stock">'
								+ '<div class="mt">'
								+ '    <ul class="tab">';
                            if (opt.useCache) {
                                provinceHtml += '        <li data-index="-1" data-widget="tab-item" class="curr"><a href="#none" class="hover"><em>常用地区</em><i></i></a></li>';
                                provinceHtml += '        <li data-index="0" data-widget="tab-item" ><a href="#none" class=""><em>请选择</em><i></i></a></li>';
                            } else {
                                provinceHtml += '        <li data-index="0" data-widget="tab-item" class="curr"><a href="#none" class="hover"><em>请选择</em><i></i></a></li>';
                            }

                            provinceHtml += '        <li data-index="1" data-widget="tab-item" style="display:none;"><a href="#none" class=""><em>请选择</em><i></i></a></li>'
								+ '        <li data-index="2" data-widget="tab-item" style="display:none;"><a href="#none" class=""><em>请选择</em><i></i></a></li>'
								+ '        <li data-index="3" data-widget="tab-item" style="display:none;"><a href="#none" class=""><em>请选择</em><i></i></a></li>'
								+ '    </ul>'
								+ '    <div class="stock-line"></div>'
								+ '</div>';

                            //                            if (opt.useCache) {
                            //                                // 常用地址
                            //                                provinceHtml += '<div class="mc" data-area="-1" data-widget="tab-content" id="' + opt.locationChooseBoxId + '_oftenUse">';
                            //                                provinceHtml += '    <ul class="area-list">';
                            //                                if (opt.cacheValue) {
                            //                                    for (var i = 0; i < opt.cacheValue.length; i++) {
                            //                                        if (opt.cacheValue[i].chooseLocationIdName.length > 12) {
                            //                                            provinceHtml += "<li class='longer-area'><a href='#none' data-position-code='" + opt.cacheValue[i].chooseLocationPositionCode + "' data-position-name='" + opt.cacheValue[i].chooseLocationPositionName + "'  data-location-id='" + opt.cacheValue[i].chooseLocationId + "' data-location-name='" + opt.cacheValue[i].chooseLocationIdName + "'>" + opt.cacheValue[i].chooseLocationIdName + "</a></li>";
                            //                                        } else if (opt.cacheValue[i].chooseLocationIdName.length > 5) {
                            //                                            provinceHtml += "<li class='long-area'><a href='#none' data-position-code='" + opt.cacheValue[i].chooseLocationPositionCode + "' data-position-name='" + opt.cacheValue[i].chooseLocationPositionName + "'  data-location-id='" + opt.cacheValue[i].chooseLocationId + "' data-location-name='" + opt.cacheValue[i].chooseLocationIdName + "'>" + opt.cacheValue[i].chooseLocationIdName + "</a></li>";
                            //                                        } else {
                            //                                            provinceHtml += "<li><a href='#none' data-position-code='" + opt.cacheValue[i].chooseLocationPositionCode + "' data-position-name='" + opt.cacheValue[i].chooseLocationPositionName + "'  data-location-id='" + opt.cacheValue[i].chooseLocationId + "' data-location-name='" + opt.cacheValue[i].chooseLocationIdName + "'>" + opt.cacheValue[i].chooseLocationIdName + "</a></li>";
                            //                                        }
                            //                                    }
                            //                                }

                            //                                provinceHtml += '    </ul>';
                            //                                provinceHtml += '    </div>';
                            //                            }

                            if (opt.useCache) {


                                // 常用地址
                                provinceHtml += '<div class="mc" data-area="-1" data-widget="tab-content" id="' + opt.locationChooseBoxId + '_oftenUse">';
                                provinceHtml += '    <ul class="area-list">';
                                if (opt.cacheValue) {
                                    for (var i = 0; i < opt.cacheValue.length; i++) {
                                        if (opt.cacheValue[i].chooseLocationIdName.length > 12) {
                                            provinceHtml += "<li class='longer-area'><a href='#none' data-position-code='" + opt.cacheValue[i].chooseLocationPositionCode + "' data-position-name='" + opt.cacheValue[i].chooseLocationPositionName + "'  data-location-id='" + opt.cacheValue[i].chooseLocationId + "' data-location-name='" + opt.cacheValue[i].chooseLocationIdName + "'>" + opt.cacheValue[i].chooseLocationIdName + "</a></li>";
                                        } else if (opt.cacheValue[i].chooseLocationIdName.length > 5) {
                                            provinceHtml += "<li class='long-area'><a href='#none' data-position-code='" + opt.cacheValue[i].chooseLocationPositionCode + "' data-position-name='" + opt.cacheValue[i].chooseLocationPositionName + "'  data-location-id='" + opt.cacheValue[i].chooseLocationId + "' data-location-name='" + opt.cacheValue[i].chooseLocationIdName + "'>" + opt.cacheValue[i].chooseLocationIdName + "</a></li>";
                                        } else {
                                            provinceHtml += "<li><a href='#none' data-position-code='" + opt.cacheValue[i].chooseLocationPositionCode + "' data-position-name='" + opt.cacheValue[i].chooseLocationPositionName + "'  data-location-id='" + opt.cacheValue[i].chooseLocationId + "' data-location-name='" + opt.cacheValue[i].chooseLocationIdName + "'>" + opt.cacheValue[i].chooseLocationIdName + "</a></li>";
                                        }
                                    }
                                }

                                provinceHtml += '    </ul>';
                                provinceHtml += '    </div>';

                                // 常用地址
                                provinceHtml += '<div class="mc" data-area="-1" data-widget="tab-content" id="' + opt.locationChooseBoxId + '_oftenUse">';
                                provinceHtml += '    <ul class="area-list">';
                                if (opt.cacheValue) {
                                    for (var i = 0; i < opt.cacheValue.length; i++) {
                                        if (opt.cacheValue[i].chooseLocationIdName.length > 12) {
                                            provinceHtml += "<li class='longer-area'><a href='#none' data-position-code='" + opt.cacheValue[i].chooseLocationPositionCode + "' data-position-name='" + opt.cacheValue[i].chooseLocationPositionName + "'  data-location-id='" + opt.cacheValue[i].chooseLocationId + "' data-location-name='" + opt.cacheValue[i].chooseLocationIdName + "'>" + opt.cacheValue[i].chooseLocationIdName + "</a></li>";
                                        } else if (opt.cacheValue[i].chooseLocationIdName.length > 5) {
                                            provinceHtml += "<li class='long-area'><a href='#none' data-position-code='" + opt.cacheValue[i].chooseLocationPositionCode + "' data-position-name='" + opt.cacheValue[i].chooseLocationPositionName + "'  data-location-id='" + opt.cacheValue[i].chooseLocationId + "' data-location-name='" + opt.cacheValue[i].chooseLocationIdName + "'>" + opt.cacheValue[i].chooseLocationIdName + "</a></li>";
                                        } else {
                                            provinceHtml += "<li><a href='#none' data-position-code='" + opt.cacheValue[i].chooseLocationPositionCode + "' data-position-name='" + opt.cacheValue[i].chooseLocationPositionName + "'  data-location-id='" + opt.cacheValue[i].chooseLocationId + "' data-location-name='" + opt.cacheValue[i].chooseLocationIdName + "'>" + opt.cacheValue[i].chooseLocationIdName + "</a></li>";
                                        }
                                    }
                                }

                                provinceHtml += '    </ul>';
                                provinceHtml += '    </div>';
                                provinceHtml += '<div class="mc" style="display:none;" data-area="0" data-widget="tab-content" id="' + opt.locationChooseBoxId + '_stock_province_item">';
                            } else {
                                provinceHtml += '<div class="mc" data-area="0" data-widget="tab-content" id="' + opt.locationChooseBoxId + '_stock_province_item">';
                            }
                            provinceHtml += '    <ul class="area-list" >';

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
								+ '</div>';


                            provinceHtml += '<div class="mc" data-area="1" data-widget="tab-content" id="' + opt.locationChooseBoxId + '_stock_city_item"></div>'
								+ '<div class="mc" data-area="2" data-widget="tab-content" id="' + opt.locationChooseBoxId + '_stock_area_item"></div>'
								+ '<div class="mc" data-area="3" data-widget="tab-content" id="' + opt.locationChooseBoxId + '_stock_town_item"></div>'
								+ '</div></div>';

                            $("#" + opt.locationChooseBoxId).append(provinceHtml);
                            areaTabContainer = $("#" + opt.locationChooseBoxId + "_JD-stock .tab li");
                            if (opt.useCache) {
                                oftenUseContainer = $("#" + opt.locationChooseBoxId + "_oftenUse");
                            }
                            provinceContainer = $("#" + opt.locationChooseBoxId + "_stock_province_item");
                            cityContainer = $("#" + opt.locationChooseBoxId + "_stock_city_item");
                            areaContainer = $("#" + opt.locationChooseBoxId + "_stock_area_item");
                            townaContainer = $("#" + opt.locationChooseBoxId + "_stock_town_item");

                            $this.unbind("click").bind("click", function () {
                                // 如果需要从cookie中获取常用地点，则需要重新渲染
                                opt.cacheValue = getOthenUseArea(opt);
                                var othenHtml = "";
                                if (opt.useCache) {
                                    // 常用地址
                                    othenHtml += '<div class="mc" data-area="-1" data-widget="tab-content" id="' + opt.locationChooseBoxId + '_oftenUse">';
                                    othenHtml += '    <ul class="area-list">';
                                    if (opt.cacheValue) {
                                        for (var i = 0; i < opt.cacheValue.length; i++) {
                                            if (opt.cacheValue[i].chooseLocationIdName.length > 12) {
                                                othenHtml += "<li class='longer-area'><a href='#none' data-position-code='" + opt.cacheValue[i].chooseLocationPositionCode + "' data-position-name='" + opt.cacheValue[i].chooseLocationPositionName + "'  data-location-id='" + opt.cacheValue[i].chooseLocationId + "' data-location-name='" + opt.cacheValue[i].chooseLocationIdName + "'>" + opt.cacheValue[i].chooseLocationIdName + "</a></li>";
                                            } else if (opt.cacheValue[i].chooseLocationIdName.length > 5) {
                                                othenHtml += "<li class='long-area'><a href='#none' data-position-code='" + opt.cacheValue[i].chooseLocationPositionCode + "' data-position-name='" + opt.cacheValue[i].chooseLocationPositionName + "'  data-location-id='" + opt.cacheValue[i].chooseLocationId + "' data-location-name='" + opt.cacheValue[i].chooseLocationIdName + "'>" + opt.cacheValue[i].chooseLocationIdName + "</a></li>";
                                            } else {
                                                othenHtml += "<li><a href='#none' data-position-code='" + opt.cacheValue[i].chooseLocationPositionCode + "' data-position-name='" + opt.cacheValue[i].chooseLocationPositionName + "'  data-location-id='" + opt.cacheValue[i].chooseLocationId + "' data-location-name='" + opt.cacheValue[i].chooseLocationIdName + "'>" + opt.cacheValue[i].chooseLocationIdName + "</a></li>";
                                            }
                                        }
                                    }

                                    othenHtml += '    </ul>';
                                    othenHtml += '    </div>';

                                    $("#" + opt.locationChooseBoxId + "_JD-stock [data-area='-1']").remove();
                                    $("#" + opt.locationChooseBoxId + "_JD-stock .mt").append(othenHtml);
                                    oftenUseContainer = $("#" + opt.locationChooseBoxId + "_oftenUse");
                                    oftenUseContainer.find("a").click(function () {
                                        chooseOftenUse($this, opt, currentAreaInfo, areaTabContainer, $(this));
                                    }).end();
                                }



                                var x = $this.position().top + $this.height() + 3;
                                var y = $this.position().left;
                                $("#" + opt.locationChooseBoxId).css("top", x);
                                $("#" + opt.locationChooseBoxId).css("left", y);
                                $("#" + opt.locationChooseBoxId).show();
                                $("#" + opt.locationChooseBoxId + " .content,#" + opt.locationChooseBoxId + "_JD-stock").show();
                            });

                            $(document).bind("click", function (e) {
                                //                                if (currentAreaInfo.currentPosition != -1) {
                                //                                    var currentLevel = currentAreaInfo.currentPosition.split("-");
                                //                                    if (opt.leastLevel < currentLevel.length) {
                                //                                        var target = $(e.target);
                                //                                        if (target.closest("#" + opt.locationChooseBoxId + ",#" + $this.attr("id")).length == 0) {
                                //                                            $("#" + opt.locationChooseBoxId).hide();
                                //                                        };
                                //                                        e.stopPropagation();
                                //                                    }
                                //                                }
                                var target = $(e.target);
                                if (target.closest("#" + opt.locationChooseBoxId + ",#" + $this.attr("id")).length == 0) {
                                    $("#" + opt.locationChooseBoxId).hide();
                                };
                                e.stopPropagation();
                            });

                            if (opt.useCache) {
                                areaTabContainer.eq(0).find("a").click(function () {
                                    areaTabContainer.removeClass("curr");
                                    areaTabContainer.eq(0).addClass("curr").show();
                                    oftenUseContainer.show();
                                    provinceContainer.hide();
                                    cityContainer.hide();
                                    areaContainer.hide();
                                    townaContainer.hide();
                                });

                                areaTabContainer.eq(1).find("a").click(function () {
                                    areaTabContainer.removeClass("curr");
                                    areaTabContainer.eq(1).addClass("curr").show();
                                    oftenUseContainer.hide();
                                    provinceContainer.show();
                                    cityContainer.hide();
                                    areaContainer.hide();
                                    townaContainer.hide();
                                    areaTabContainer.eq(2).hide();
                                    areaTabContainer.eq(3).hide();
                                    areaTabContainer.eq(4).hide();

                                    currentAreaInfo.currentId = currentAreaInfo.currentProvinceId;
                                    currentAreaInfo.currentIdDisplay = currentAreaInfo.currentProvinceName;
                                    currentAreaInfo.currentPosition = currentAreaInfo.currentProvincePosition + "-" + currentAreaInfo.currentProvinceId;
                                    currentAreaInfo.currentPositionDisplay = currentAreaInfo.currentProvinceName;
                                    currentAreaInfo.currentCityId = -1;
                                    currentAreaInfo.currentCityName = "";
                                    currentAreaInfo.currentCityPosition = -1;

                                    currentAreaInfo.currentAreaId = -1;
                                    currentAreaInfo.currentAreaName = "";
                                    currentAreaInfo.currentAreaPosition = -1;

                                    currentAreaInfo.currentTownId = -1;
                                    currentAreaInfo.currentTownName = "";
                                    currentAreaInfo.currentTownPosition = -1;

                                    opt.afterChooseForPositionEvent.apply($this[0], [currentAreaInfo.currentPosition, currentAreaInfo.currentPositionDisplay, currentAreaInfo.currentId, currentAreaInfo.currentIdDisplay]);
                                    setCookieCache(opt, [currentAreaInfo.currentPosition, currentAreaInfo.currentPositionDisplay, currentAreaInfo.currentId, currentAreaInfo.currentIdDisplay]);
                                });
                                areaTabContainer.eq(2).find("a").click(function () {
                                    areaTabContainer.removeClass("curr");
                                    areaTabContainer.eq(2).addClass("curr").show();
                                    oftenUseContainer.hide();
                                    provinceContainer.hide();
                                    cityContainer.show();
                                    areaContainer.hide();
                                    townaContainer.hide();
                                    areaTabContainer.eq(3).hide();
                                    areaTabContainer.eq(4).hide();

                                    currentAreaInfo.currentId = currentAreaInfo.currentCityId;
                                    currentAreaInfo.currentIdDisplay = currentAreaInfo.currentCityName;
                                    currentAreaInfo.currentPosition = currentAreaInfo.currentCityPosition + "-" + currentAreaInfo.currentCityId;
                                    currentAreaInfo.currentPositionDisplay = currentAreaInfo.currentProvinceName + " " + currentAreaInfo.currentCityName;
                                    currentAreaInfo.currentAreaId = -1;
                                    currentAreaInfo.currentAreaName = "";
                                    currentAreaInfo.currentAreaPosition = -1;

                                    currentAreaInfo.currentTownId = -1;
                                    currentAreaInfo.currentTownName = "";
                                    currentAreaInfo.currentTownPosition = -1;

                                    opt.afterChooseForPositionEvent.apply($this[0], [currentAreaInfo.currentPosition, currentAreaInfo.currentPositionDisplay, currentAreaInfo.currentId, currentAreaInfo.currentIdDisplay]);
                                    setCookieCache(opt, [currentAreaInfo.currentPosition, currentAreaInfo.currentPositionDisplay, currentAreaInfo.currentId, currentAreaInfo.currentIdDisplay]);
                                });
                                areaTabContainer.eq(3).find("a").click(function () {
                                    areaTabContainer.removeClass("curr");
                                    areaTabContainer.eq(3).addClass("curr").show();
                                    oftenUseContainer.hide();
                                    provinceContainer.hide();
                                    cityContainer.hide();
                                    areaContainer.show();
                                    townaContainer.hide();
                                    areaTabContainer.eq(4).hide();

                                    currentAreaInfo.currentId = currentAreaInfo.currentAreaId;
                                    currentAreaInfo.currentIdDisplay = currentAreaInfo.currentAreaName;
                                    currentAreaInfo.currentPosition = currentAreaInfo.currentAreaPosition + currentAreaInfo.currentAreaId;
                                    currentAreaInfo.currentPositionDisplay = currentAreaInfo.currentProvinceName + " " + currentAreaInfo.currentCityName + " " + currentAreaInfo.currentAreaName;
                                    currentAreaInfo.currentTownId = -1;
                                    currentAreaInfo.currentTownName = "";
                                    currentAreaInfo.currentTownPosition = -1;

                                    opt.afterChooseForPositionEvent.apply($this[0], [currentAreaInfo.currentPosition, currentAreaInfo.currentPositionDisplay, currentAreaInfo.currentId, currentAreaInfo.currentIdDisplay]);
                                    setCookieCache(opt, [currentAreaInfo.currentPosition, currentAreaInfo.currentPositionDisplay, currentAreaInfo.currentId, currentAreaInfo.currentIdDisplay]);
                                });

                                areaTabContainer.eq(4).find("a").click(function () {
                                    areaTabContainer.removeClass("curr");
                                    areaTabContainer.eq(4).addClass("curr").show();
                                    oftenUseContainer.hide();
                                    provinceContainer.hide();
                                    cityContainer.hide();
                                    areaContainer.hide();
                                    townaContainer.show();

                                    currentAreaInfo.currentId = currentAreaInfo.currentTownaId;
                                    currentAreaInfo.currentIdDisplay = currentAreaInfo.currentTownaName;
                                    currentAreaInfo.currentPosition = currentAreaInfo.currentTownaPosition + currentAreaInfo.currentTownaId;
                                    currentAreaInfo.currentPositionDisplay = currentAreaInfo.currentProvinceName + " " + currentAreaInfo.currentTownaName + " " + currentAreaInfo.currentAreaName;
                                    opt.afterChooseForPositionEvent.apply($this[0], [currentAreaInfo.currentPosition, currentAreaInfo.currentPositionDisplay, currentAreaInfo.currentId, currentAreaInfo.currentIdDisplay]);
                                    setCookieCache(opt, [currentAreaInfo.currentPosition, currentAreaInfo.currentPositionDisplay, currentAreaInfo.currentId, currentAreaInfo.currentIdDisplay]);
                                });

                                oftenUseContainer.find("a").click(function () {
                                    chooseOftenUse($this, opt, currentAreaInfo, areaTabContainer, $(this));
                                }).end();
                            } else {
                                areaTabContainer.eq(0).find("a").click(function () {
                                    areaTabContainer.removeClass("curr");
                                    areaTabContainer.eq(0).addClass("curr").show();
                                    //oftenUseContainer.hide();
                                    provinceContainer.show();
                                    cityContainer.hide();
                                    areaContainer.hide();
                                    townaContainer.hide();
                                    areaTabContainer.eq(1).hide();
                                    areaTabContainer.eq(2).hide();
                                    areaTabContainer.eq(3).hide();

                                    currentAreaInfo.currentId = currentAreaInfo.currentProvinceId;
                                    currentAreaInfo.currentIdDisplay = currentAreaInfo.currentProvinceName;
                                    currentAreaInfo.currentPosition = currentAreaInfo.currentProvincePosition + "-" + currentAreaInfo.currentProvinceId;
                                    currentAreaInfo.currentPositionDisplay = currentAreaInfo.currentProvinceName;
                                    currentAreaInfo.currentCityId = -1;
                                    currentAreaInfo.currentCityName = "";
                                    currentAreaInfo.currentCityPosition = -1;

                                    currentAreaInfo.currentAreaId = -1;
                                    currentAreaInfo.currentAreaName = "";
                                    currentAreaInfo.currentAreaPosition = -1;

                                    currentAreaInfo.currentTownId = -1;
                                    currentAreaInfo.currentTownName = "";
                                    currentAreaInfo.currentTownPosition = -1;

                                    opt.afterChooseForPositionEvent.apply($this[0], [currentAreaInfo.currentPosition, currentAreaInfo.currentPositionDisplay, currentAreaInfo.currentId, currentAreaInfo.currentIdDisplay]);
                                    //setCookieCache(opt, [currentAreaInfo.currentPosition, currentAreaInfo.currentPositionDisplay, currentAreaInfo.currentId, currentAreaInfo.currentIdDisplay]);
                                });
                                areaTabContainer.eq(1).find("a").click(function () {
                                    areaTabContainer.removeClass("curr");
                                    areaTabContainer.eq(1).addClass("curr").show();
                                    //oftenUseContainer.hide();
                                    provinceContainer.hide();
                                    cityContainer.show();
                                    areaContainer.hide();
                                    townaContainer.hide();
                                    areaTabContainer.eq(2).hide();
                                    areaTabContainer.eq(3).hide();

                                    currentAreaInfo.currentId = currentAreaInfo.currentCityId;
                                    currentAreaInfo.currentIdDisplay = currentAreaInfo.currentCityName;
                                    currentAreaInfo.currentPosition = currentAreaInfo.currentCityPosition + "-" + currentAreaInfo.currentCityId;
                                    currentAreaInfo.currentPositionDisplay = currentAreaInfo.currentProvinceName + " " + currentAreaInfo.currentCityName;
                                    currentAreaInfo.currentAreaId = -1;
                                    currentAreaInfo.currentAreaName = "";
                                    currentAreaInfo.currentAreaPosition = -1;

                                    currentAreaInfo.currentTownId = -1;
                                    currentAreaInfo.currentTownName = "";
                                    currentAreaInfo.currentTownPosition = -1;

                                    opt.afterChooseForPositionEvent.apply($this[0], [currentAreaInfo.currentPosition, currentAreaInfo.currentPositionDisplay, currentAreaInfo.currentId, currentAreaInfo.currentIdDisplay]);
                                    //setCookieCache(opt, [currentAreaInfo.currentPosition, currentAreaInfo.currentPositionDisplay, currentAreaInfo.currentId, currentAreaInfo.currentIdDisplay]);
                                });
                                areaTabContainer.eq(2).find("a").click(function () {
                                    areaTabContainer.removeClass("curr");
                                    areaTabContainer.eq(2).addClass("curr").show();
                                    //oftenUseContainer.hide();
                                    provinceContainer.hide();
                                    cityContainer.hide();
                                    areaContainer.show();
                                    townaContainer.hide();
                                    areaTabContainer.eq(3).hide();

                                    currentAreaInfo.currentId = currentAreaInfo.currentAreaId;
                                    currentAreaInfo.currentIdDisplay = currentAreaInfo.currentAreaName;
                                    currentAreaInfo.currentPosition = currentAreaInfo.currentAreaPosition + currentAreaInfo.currentAreaId;
                                    currentAreaInfo.currentPositionDisplay = currentAreaInfo.currentProvinceName + " " + currentAreaInfo.currentCityName + " " + currentAreaInfo.currentAreaName;
                                    currentAreaInfo.currentTownId = -1;
                                    currentAreaInfo.currentTownName = "";
                                    currentAreaInfo.currentTownPosition = -1;

                                    opt.afterChooseForPositionEvent.apply($this[0], [currentAreaInfo.currentPosition, currentAreaInfo.currentPositionDisplay, currentAreaInfo.currentId, currentAreaInfo.currentIdDisplay]);
                                    //setCookieCache(opt, [currentAreaInfo.currentPosition, currentAreaInfo.currentPositionDisplay, currentAreaInfo.currentId, currentAreaInfo.currentIdDisplay]);
                                });

                                areaTabContainer.eq(3).find("a").click(function () {
                                    areaTabContainer.removeClass("curr");
                                    areaTabContainer.eq(3).addClass("curr").show();
                                    //oftenUseContainer.hide();
                                    provinceContainer.hide();
                                    cityContainer.hide();
                                    areaContainer.hide();
                                    townaContainer.show();

                                    currentAreaInfo.currentId = currentAreaInfo.currentTownaId;
                                    currentAreaInfo.currentIdDisplay = currentAreaInfo.currentTownaName;
                                    currentAreaInfo.currentPosition = currentAreaInfo.currentTownaPosition + currentAreaInfo.currentTownaId;
                                    currentAreaInfo.currentPositionDisplay = currentAreaInfo.currentProvinceName + " " + currentAreaInfo.currentTownaName + " " + currentAreaInfo.currentAreaName;
                                    opt.afterChooseForPositionEvent.apply($this[0], [currentAreaInfo.currentPosition, currentAreaInfo.currentPositionDisplay, currentAreaInfo.currentId, currentAreaInfo.currentIdDisplay]);
                                    //setCookieCache(opt, [currentAreaInfo.currentPosition, currentAreaInfo.currentPositionDisplay, currentAreaInfo.currentId, currentAreaInfo.currentIdDisplay]);
                                });
                            }

                            provinceContainer.find("a").click(function () {
                                chooseProvince($this, opt, currentAreaInfo, areaTabContainer, oftenUseContainer, provinceContainer, cityContainer, areaContainer, townaContainer, locationArray, $(this).attr("data-value"), $(this).html(), $(this).attr("data-position"), opt.lastChooseLevel);
                            }).end();
                        }
                    });
            });

            //            this.updateUseCache = function (cacheValue) {
            //                opt.cacheValue = cacheValue;
            //                var cacheUseBox = $("#" + opt.locationChooseBoxId + "_oftenUse");
            //                cacheUseBox.find("ul").empty();
            //                var tempLi = "";
            //                if (opt.cacheValue) {
            //                    for (var i = 0; i < opt.cacheValue.length; i++) {
            //                        tempLi += "<li class='long-area'><a href='#none' data-position-code='" + opt.cacheValue[i].chooseLocationPositionCode + "' data-position-name='" + opt.cacheValue[i].chooseLocationPositionName + "'  data-location-id='" + opt.cacheValue[i].chooseLocationId + "' data-location-name='" + opt.cacheValue[i].chooseLocationIdName + "'>" + opt.cacheValue[i].chooseLocationIdName + "</a></li>";
            //                    }

            //                    cacheUseBox.find("ul").append(tempLi);
            //                }
            //            }
        }
    });

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

    function chooseProvince(objControl, opt, currentAreaInfo, areaTabContainer, oftenUseContainer, provinceContainer, cityContainer, areaContainer, townaContainer, sourceArray, provinceId, provinceName, position, lastChooseLevel) {
        var selectorBoxId = opt.locationChooseBoxId;
        sourceArray = Enumerable.From(sourceArray).Where(function (i) { return i.LAYER == 2; }).ToArray();
        if (opt.useCache) {
            oftenUseContainer.hide();
        }
        provinceContainer.hide();
        currentAreaInfo.currentProvinceId = provinceId;
        currentAreaInfo.currentProvinceName = provinceName;
        currentAreaInfo.currentProvincePosition = position;
        currentAreaInfo.currentId = provinceId;
        currentAreaInfo.currentIdDisplay = currentAreaInfo.currentProvinceName;
        currentAreaInfo.currentPosition = position + "-" + provinceId;
        currentAreaInfo.currentPositionDisplay = currentAreaInfo.currentProvinceName;
        areaTabContainer.eq(1).removeClass("curr").find("em").html(provinceName);
        areaTabContainer.eq(2).addClass("curr").show().find("em").html("请选择");
        areaTabContainer.eq(3).hide();
        areaTabContainer.eq(4).hide();
        cityContainer.show();
        areaContainer.hide();
        townaContainer.hide();
        if (provinceId) {
            cityContainer.html(getAreaList(sourceArray, provinceId));
            cityContainer.find("a").click(function () {
                if (lastChooseLevel == 1) {
                    $('#' + selectorBoxId).hide();
                    opt.afterChooseForPositionEvent.apply(objControl[0], [currentAreaInfo.currentPosition, currentAreaInfo.currentPositionDisplay, currentAreaInfo.currentId, currentAreaInfo.currentIdDisplay]);
                    setCookieCache(opt, [currentAreaInfo.currentPosition, currentAreaInfo.currentPositionDisplay, currentAreaInfo.currentId, currentAreaInfo.currentIdDisplay]);
                } else {
                    chooseCity(objControl, opt, currentAreaInfo, areaTabContainer, oftenUseContainer, provinceContainer, cityContainer, areaContainer, townaContainer, $(this).attr("data-value"), $(this).html(), $(this).attr("data-position"), lastChooseLevel);
                }
            });
        }
        opt.afterChooseForPositionEvent.apply(objControl[0], [currentAreaInfo.currentPosition, currentAreaInfo.currentPositionDisplay, currentAreaInfo.currentId, currentAreaInfo.currentIdDisplay]);
        setCookieCache(opt, [currentAreaInfo.currentPosition, currentAreaInfo.currentPositionDisplay, currentAreaInfo.currentId, currentAreaInfo.currentIdDisplay]);
    }

    function chooseCity(objControl, opt, currentAreaInfo, areaTabContainer, oftenUseContainer, provinceContainer, cityContainer, areaContainer, townaContainer, cityId, cityName, position, lastChooseLevel) {
        var selectorBoxId = opt.locationChooseBoxId;
        if (opt.useCache) {
            oftenUseContainer.hide();
        }
        provinceContainer.hide();

        currentAreaInfo.currentCityId = cityId;
        currentAreaInfo.currentCityName = cityName;
        currentAreaInfo.currentCityPosition = position;
        currentAreaInfo.currentId = cityId;
        currentAreaInfo.currentIdDisplay = cityName;
        currentAreaInfo.currentPosition = position + "-" + cityId;
        currentAreaInfo.currentPositionDisplay = currentAreaInfo.currentProvinceName + " " + cityName;

        if (lastChooseLevel == 2) {
            $('#' + selectorBoxId).hide();
            opt.afterChooseForPositionEvent.apply(objControl[0], [currentAreaInfo.currentPosition, currentAreaInfo.currentPositionDisplay, currentAreaInfo.currentId, currentAreaInfo.currentIdDisplay]);
            setCookieCache(opt, [currentAreaInfo.currentPosition, currentAreaInfo.currentPositionDisplay, currentAreaInfo.currentId, currentAreaInfo.currentIdDisplay]);
        } else {
            cityContainer.hide();
            areaTabContainer.eq(2).removeClass("curr").find("em").html(cityName);
            areaTabContainer.eq(3).addClass("curr").show().find("em").html("请选择");
            areaTabContainer.eq(4).hide();
            areaContainer.show().html("<div class='iloading'>正在加载中，请稍候...</div>");
            townaContainer.hide();
            var parmsArray = [
          { name: 'LAYER', value: 3 },
          { name: 'parentid', value: cityId }
        ];
            var customSearchFilters = formatSearchParames(parmsArray);
            $.getJSON(opt.url + "?" + Math.random(), { action: "GetList", customSearchFilters: customSearchFilters },
                    function (data) {
                        if (isServerResultDataPass(data)) {
                            areaContainer.html(getAreaList(data.ResultDataFull, cityId));
                            areaContainer.find("a").click(function () {
                                chooseArea(objControl, opt, currentAreaInfo, areaTabContainer, oftenUseContainer, provinceContainer, cityContainer, areaContainer, townaContainer, $(this).attr("data-value"), $(this).html(), $(this).attr("data-position"), lastChooseLevel);
                            });
                        }
                    });
            opt.afterChooseForPositionEvent.apply(objControl[0], [currentAreaInfo.currentPosition, currentAreaInfo.currentPositionDisplay, currentAreaInfo.currentId, currentAreaInfo.currentIdDisplay]);
            setCookieCache(opt, [currentAreaInfo.currentPosition, currentAreaInfo.currentPositionDisplay, currentAreaInfo.currentId, currentAreaInfo.currentIdDisplay]);
        }
    }

    function chooseArea(objControl, opt, currentAreaInfo, areaTabContainer, oftenUseContainer, provinceContainer, cityContainer, areaContainer, townaContainer, areaId, areaName, position, lastChooseLevel) {
        var selectorBoxId = opt.locationChooseBoxId;
        if (opt.useCache) {
            oftenUseContainer.hide();
        }
        provinceContainer.hide();
        cityContainer.hide();
        areaContainer.hide();
        currentAreaInfo.currentAreaId = areaId;
        currentAreaInfo.currentAreaName = areaName;
        currentAreaInfo.currentAreaPosition = position;
        currentAreaInfo.currentId = areaId;
        currentAreaInfo.currentIdDisplay = areaName;
        currentAreaInfo.currentPosition = position + "-" + areaId;
        currentAreaInfo.currentPositionDisplay = currentAreaInfo.currentProvinceName + " " + currentAreaInfo.currentCityName + " " + currentAreaInfo.currentAreaName;
        areaTabContainer.eq(3).removeClass("curr").find("em").html(areaName);
        if (lastChooseLevel == 3) {
            $('#' + selectorBoxId).hide();
            opt.afterChooseForPositionEvent.apply(objControl[0], [currentAreaInfo.currentPosition, currentAreaInfo.currentPositionDisplay, currentAreaInfo.currentId, currentAreaInfo.currentIdDisplay]);
            setCookieCache(opt, [currentAreaInfo.currentPosition, currentAreaInfo.currentPositionDisplay, currentAreaInfo.currentId, currentAreaInfo.currentIdDisplay]);
        } else {
            areaTabContainer.eq(4).addClass("curr").show().find("em").html("请选择");
            townaContainer.show().html("<div class='iloading'>正在加载中，请稍候...</div>");
            var parmsArray = [
          { name: 'LAYER', value: 4 },
          { name: 'parentid', value: areaId }
        ];
            var customSearchFilters = formatSearchParames(parmsArray);
            $.getJSON(opt.url + "?" + Math.random(), { action: "GetList", customSearchFilters: customSearchFilters },
                    function (data) {
                        if (isServerResultDataPass(data)) {
                            townaContainer.html(getAreaList(data.ResultDataFull, areaId));
                            townaContainer.find("a").click(function () {
                                chooseTowna(objControl, opt, currentAreaInfo, areaTabContainer, oftenUseContainer, provinceContainer, cityContainer, areaContainer, townaContainer, $(this).attr("data-value"), $(this).html(), $(this).attr("data-position"), lastChooseLevel);
                            });
                        }
                    });
            opt.afterChooseForPositionEvent.apply(objControl[0], [currentAreaInfo.currentPosition, currentAreaInfo.currentPositionDisplay, currentAreaInfo.currentId, currentAreaInfo.currentIdDisplay]);
            setCookieCache(opt, [currentAreaInfo.currentPosition, currentAreaInfo.currentPositionDisplay, currentAreaInfo.currentId, currentAreaInfo.currentIdDisplay]);
        }
    }

    function chooseTowna(objControl, opt, currentAreaInfo, areaTabContainer, oftenUseContainer, provinceContainer, cityContainer, areaContainer, townaContainer, townaId, townaName, position, lastChooseLevel) {
        var selectorBoxId = opt.locationChooseBoxId;
        currentAreaInfo.currentTownId = townaId;
        currentAreaInfo.currentTownName = townaName;
        currentAreaInfo.currentTownPosition = position;
        currentAreaInfo.currentId = townaId;
        currentAreaInfo.currentIdDisplay = townaName;
        currentAreaInfo.currentPosition = position + "-" + townaId;
        currentAreaInfo.currentPositionDisplay = currentAreaInfo.currentProvinceName + " " + currentAreaInfo.currentCityName + " " + currentAreaInfo.currentAreaName + " " + currentAreaInfo.currentTownName;
        areaTabContainer.eq(4).addClass("curr").show().find("em").html(townaName);
        $('#' + selectorBoxId).hide();
        opt.afterChooseForPositionEvent.apply(objControl[0], [currentAreaInfo.currentPosition, currentAreaInfo.currentPositionDisplay, currentAreaInfo.currentId, currentAreaInfo.currentIdDisplay]);
        setCookieCache(opt, [currentAreaInfo.currentPosition, currentAreaInfo.currentPositionDisplay, currentAreaInfo.currentId, currentAreaInfo.currentIdDisplay]);
    }

    function chooseOftenUse(objControl, opt, currentAreaInfo, areaTabContainer, clickItemObj) {
        var selectorBoxId = opt.locationChooseBoxId;
        //        currentAreaInfo.currentProvinceId = provinceId;
        //        currentAreaInfo.currentProvinceName = provinceName;
        //        currentAreaInfo.currentProvincePosition = position;
        //        currentAreaInfo.currentId = provinceId;
        //        currentAreaInfo.currentIdDisplay = currentAreaInfo.currentProvinceName;
        //        currentAreaInfo.currentPosition = position + "-" + provinceId;
        //        currentAreaInfo.currentPositionDisplay = currentAreaInfo.currentProvinceName;
        areaTabContainer.eq(0).addClass("curr").show().find("em").html(clickItemObj.attr("data-location-name"));
        $('#' + selectorBoxId).hide();
        opt.afterChooseForPositionEvent.apply(objControl[0], [clickItemObj.attr("data-position-code"), clickItemObj.attr("data-position-name"), clickItemObj.attr("data-location-id"), clickItemObj.attr("data-location-name")]);
        setCookieCache(opt, [clickItemObj.attr("data-position-code"), clickItemObj.attr("data-position-name"), clickItemObj.attr("data-location-id"), clickItemObj.attr("data-location-name")]);
    }

    function setCookieCache(opt, positionArray) {
        var chooseLocationPositionCode = positionArray[0];
        if (opt.useCache && opt.lastChooseLevel == chooseLocationPositionCode.split('-').length - 1) {
            var chooseLocationPositionName = positionArray[1];

            var chooseLocationId = positionArray[2];
            var chooseLocationIdName = positionArray[3];
            // 存入缓存(只存最近使用的20个常用地点)
            var locationsValue = decodeURIComponent($.cookie(opt.cacheCookieName));
            var locationsArray = JSON.parse(locationsValue);
            // 判断地址是否存在，存在则删除，并插入到最顶端
            var needDeleteCount = 0;
            Enumerable.From(locationsArray).ToArray().forEach(function (i) {
                if (i.chooseLocationId == chooseLocationId) {
                    locationsArray.splice(needDeleteCount, 1);
                }

                needDeleteCount++;
            });

            var newInsertArea = { chooseLocationPositionCode: chooseLocationPositionCode, chooseLocationPositionName: chooseLocationPositionName, chooseLocationId: chooseLocationId, chooseLocationIdName: chooseLocationIdName };
            if (!locationsArray) {
                locationsArray = [];
            }

            if (newInsertArea.chooseLocationId != -1) {
                locationsArray.unshift(newInsertArea);
            }

            var newAreasStr = encodeURIComponent(JSON.stringify(locationsArray))
            $.cookie(opt.cacheCookieName, newAreasStr, { path: '/' });
            opt.cacheValue = locationsArray;
        }
    }

    var getOthenUseArea = function (opt) {
        var locationsValue = decodeURIComponent($.cookie(opt.cacheCookieName));
        locationsValue = locationsValue.replace(/\+/g, " ");
        return JSON.parse(locationsValue);
    }
})(jQuery);