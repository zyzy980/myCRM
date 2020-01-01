(function ($) {
    jQuery.bindLocationsChoose = {
        open: function (options) {
            var jqueryVariable = {};
            var locationArray = null;
            var currentAreaInfo = {
                "currentId": -1,
                "currentProvinceId": -1, "currentProvinceName": "", "currentProvincePosition": -1,
                "currentCityId": -1, "currentCityName": "", "currentCityPosition": -1, "currentAreaId": -1, "currentAreaName": "", "currentAreaPosition": -1,
                "currentTownId": -1, "currentTownName": "", "currentTownPosition": -1, "currentSiteId": -1, "currentSiteName": "", "currentSitePosition": -1
            };

            jqueryVariable.locationArray = locationArray;
            jqueryVariable.currentAreaInfo = currentAreaInfo;
            var defaluts = {
                needProvince: false,
                needSite: true,
                enableAddressSelect: true,
                enableSiteSelect: true,
                singleSelect: false,
                selectedLocationArray: [],
                afterChoosedEvent: function () {
                },
                afterClosedEvent: function () {
                }
            };

            var opt = $.extend({}, defaluts, options);
            jqueryVariable.selectedLocationArray = opt.selectedLocationArray;
            jqueryVariable.needProvince = opt.needProvince;
            jqueryVariable.needSite = opt.needSite;
            jqueryVariable.enableAddressSelect = opt.enableAddressSelect;
            jqueryVariable.enableSiteSelect = opt.enableSiteSelect;
            jqueryVariable.singleSelect = opt.singleSelect;
            var box = "";
            box += "<div class=\"maxBox\">";
            box += "<div class=\"maxBoxSure\">确定";
            box += "</div>";
            box += "<div class=\"maxBoxClose\">关闭";
            box += "</div>";
            box += "<div class=\"LocationStorage\">";
            box += "<ul id=\"selectLocationBox\" class=\"baseUl\">";
            box += "</ul>";
            box += "</div>";
            box += "<div class=\"LocationChooseMaxBox\">";
            box += "<div id=\"locationChooseBox_Province\" class=\"LocationChooseBox\">";
            box += "<div class=\"ContentTitle ContentTitle_Province\">";
            box += "省份";
            box += "</div>";
            box += "<div class=\"LocationChooseFirstLetterBox\">";
            box += "<ul class=\"baseUl\">";
            box += " <li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_Province\">*</li>";
            box += "  <li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_Province\">A</li>";
            box += " <li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_Province\">B</li>";
            box += "<li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_Province\">C</li>";
            box += "<li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_Province\">D</li>";
            box += " <li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_Province\">E</li>";
            box += "<li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_Province\">F</li>";
            box += "<li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_Province\">";
            box += "  G</li>";
            box += "<li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_Province\">H</li>";
            box += "<li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_Province\">I</li>";
            box += "<li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_Province\">J</li>";
            box += " <li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_Province\">K</li>";
            box += "<li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_Province\">L</li>";
            box += "<li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_Province\">M</li>";
            box += "<li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_Province\">N</li>";
            box += "<li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_Province\">O</li>";
            box += "<li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_Province\">P</li>";
            box += "<li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_Province\">Q</li>";
            box += "<li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_Province\">R</li>";
            box += "<li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_Province\">S</li>";
            box += "<li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_Province\">T</li>";
            box += "<li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_Province\">U</li>";
            box += "<li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_Province\">V</li>";
            box += "<li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_Province\">W</li>";
            box += "<li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_Province\">X</li>";
            box += "<li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_Province\">Y</li>";
            box += "<li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_Province\">Z</li>";
            box += "</ul>";
            box += "</div>";
            box += "<div class=\"LocationChooseContentBox\">";
            box += "<ul id=\"provincesListBox\" class=\"baseUl\">";
            box += "</ul>";
            box += "</div>";
            box += "</div>";
            box += "<div class=\"LocationChooseBox\">";
            box += "<div class=\"ContentTitle ContentTitle_City\">";
            box += "市";
            box += "</div>";
            box += "<div class=\"LocationChooseFirstLetterBox\">";
            box += "<ul class=\"baseUl\">";
            box += "<li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_City\">*</li>";
            box += "<li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_City\">A</li>";
            box += "<li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_City\">B</li>";
            box += "<li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_City\">C</li>";
            box += "<li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_City\">D</li>";
            box += "<li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_City\">E</li>";
            box += "<li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_City\">F</li>";
            box += "<li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_City\">G</li>";
            box += "<li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_City\">H</li>";
            box += "<li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_City\">I</li>";
            box += "<li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_City\">J</li>";
            box += "<li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_City\">K</li>";
            box += "<li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_City\">L</li>";
            box += "<li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_City\">M</li>";
            box += "<li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_City\">N</li>";
            box += "<li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_City\">O</li>";
            box += "<li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_City\">P</li>";
            box += "<li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_City\">Q</li>";
            box += "<li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_City\">R</li>";
            box += "<li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_City\">S</li>";
            box += "<li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_City\">T</li>";
            box += "<li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_City\">U</li>";
            box += "<li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_City\">V</li>";
            box += "<li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_City\">W</li>";
            box += "<li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_City\">X</li>";
            box += "<li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_City\">Y</li>";
            box += "<li class=\"baseli LocationChooseFirstLetterLi LocationChooseFirstLetterLi_City\">Z</li>";
            box += "</ul>";
            box += "</div>";
            box += "<div class=\"LocationChooseContentBox\">";
            box += "<ul id=\"citysListBox\" class=\"baseUl\">";
            box += "</ul>";
            box += "</div>";
            box += "</div>";
            box += "<div class=\"LocationChooseBox\">";
            box += "<div class=\"ContentTitle ContentTitle_Area\">";
            box += "区";
            box += "</div>";
            box += "<div class=\"LocationChooseContentBox\">";
            box += "<ul id=\"areasListBox\" class=\"baseUl\">";
            box += "</ul>";
            box += "</div>";
            box += "</div>";
            box += "<div class=\"LocationChooseBox\">";
            box += "<div class=\"ContentTitle ContentTitle_Towna\">";
            box += "街道";
            box += "</div>";
            box += "<div class=\"LocationChooseContentBox\">";
            box += "<ul id=\"townasListBox\" class=\"baseUl\">";
            box += "</ul>";
            box += "</div>";
            box += "</div>";
            box += "<div id=\"locationChooseBox_Site\" class=\"LocationChooseBox\">";
            box += "<div class=\"ContentTitle ContentTitle_Site\">";
            box += "站点";
            box += "</div>";
            box += "<div class=\"LocationChooseContentBox\">";
            box += "<ul id=\"siteListBox\" class=\"baseUl\">";
            box += "</ul>";
            box += "</div>";
            box += "</div>";
            box += " </div>";
            box += "</div>";
            $(document.body).append(box);

            var num = 0;
            if (!jqueryVariable.needProvince) {
                $("#locationChooseBox_Province").css("display", "none");
                num++;
            }

            if (!jqueryVariable.needSite) {
                $("#locationChooseBox_Site").css("display", "none");
                num++;
            }

            if (num == 0) {
                $(".LocationChooseBox").css("width", "19%");
            }
            else if (num == 1) {
                $(".LocationChooseBox").css("width", "24%");
            } else if (num == 2) {
                $(".LocationChooseBox").css("width", "33%");
            }

            initSelectLocation(jqueryVariable);
            getProvinceCityData(jqueryVariable);

            //            if (!jqueryVariable.enableAddressSelect) {
            //                $(".LocationChooseContentBoxLiSelect[data-type='Q'] .LocationChooseContentBoxLiSelected[data-type='Q']").css("display", "none");
            //            }

            //            if (!jqueryVariable.enableSiteSelect) {
            //                $(".LocationChooseContentBoxLiSelect[data-type='Z'] .LocationChooseContentBoxLiSelected[data-type='Z']").css("display", "none");
            //            }
            
            $(".LocationChooseFirstLetterLi").unbind("click").click(function () {
                if ($(this).hasClass("LocationChooseFirstLetterLi_Province")) {
                    loadProvince(jqueryVariable, { "FirstLetter": $.trim($(this).text()) });
                } else {
                    loadCity(jqueryVariable, $.trim($(this).text()));
                }
            });
            $(document).off('click', '.LocationChooseContentBoxLiTitle').on('click', '.LocationChooseContentBoxLiTitle', function () {
                if ($(this).parent().hasClass("LocationChooseContentBoxLi_Province")) {
                    $(".LocationChooseContentBoxLi_Province").removeClass("LocationChooseContentBoxLi_Linked");
                    $(this).parent().addClass("LocationChooseContentBoxLi_Linked");
                    jqueryVariable.currentAreaInfo.currentId = $(this).attr("data-id");
                    jqueryVariable.currentAreaInfo.currentProvinceId = $(this).attr("data-id");
                    jqueryVariable.currentAreaInfo.currentProvinceName = $(this).text();
                    jqueryVariable.currentAreaInfo.currentProvincePosition = $(this).next().attr("data-position");
                    jqueryVariable.currentAreaInfo.currentCityId = -1;
                    jqueryVariable.currentAreaInfo.currentCityName = "";
                    jqueryVariable.currentAreaInfo.currentCityPosition = -1;
                    jqueryVariable.currentAreaInfo.currentAreaId = -1;
                    jqueryVariable.currentAreaInfo.currentAreaName = "";
                    jqueryVariable.currentAreaInfo.currentAreaPosition = -1;
                    jqueryVariable.currentAreaInfo.currentTownaId = -1;
                    jqueryVariable.currentAreaInfo.currentTownaName = "";
                    jqueryVariable.currentAreaInfo.currentTownaPosition = -1;
                    jqueryVariable.currentAreaInfo.currentSiteId = -1;
                    jqueryVariable.currentAreaInfo.currentSiteName = "";
                    jqueryVariable.currentAreaInfo.currentSitePosition = -1;
                    loadCity(jqueryVariable);
                    loadArea(jqueryVariable);
                    loadTowna(jqueryVariable);
                    loadSite(jqueryVariable);
                } else if ($(this).parent().hasClass("LocationChooseContentBoxLi_City")) {
                    $(".LocationChooseContentBoxLi_City").removeClass("LocationChooseContentBoxLi_Linked");
                    $(this).parent().addClass("LocationChooseContentBoxLi_Linked");
                    jqueryVariable.currentAreaInfo.currentId = $(this).attr("data-id");
                    jqueryVariable.currentAreaInfo.currentCityId = $(this).attr("data-id");
                    jqueryVariable.currentAreaInfo.currentCityName = $(this).text();
                    jqueryVariable.currentAreaInfo.currentCityPosition = $(this).next().attr("data-position");
                    jqueryVariable.currentAreaInfo.currentAreaId = -1;
                    jqueryVariable.currentAreaInfo.currentAreaName = "";
                    jqueryVariable.currentAreaInfo.currentAreaPosition = -1;
                    jqueryVariable.currentAreaInfo.currentTownaId = -1;
                    jqueryVariable.currentAreaInfo.currentTownaName = "";
                    jqueryVariable.currentAreaInfo.currentTownaPosition = -1;
                    jqueryVariable.currentAreaInfo.currentSiteId = -1;
                    jqueryVariable.currentAreaInfo.currentSiteName = "";
                    jqueryVariable.currentAreaInfo.currentSitePosition = -1;
                    loadArea(jqueryVariable);
                    loadTowna(jqueryVariable);
                    loadSite(jqueryVariable);
                } else if ($(this).parent().hasClass("LocationChooseContentBoxLi_Area")) {
                    $(".LocationChooseContentBoxLi_Area").removeClass("LocationChooseContentBoxLi_Linked");
                    $(this).parent().addClass("LocationChooseContentBoxLi_Linked");
                    jqueryVariable.currentAreaInfo.currentId = $(this).attr("data-id");
                    jqueryVariable.currentAreaInfo.currentAreaId = $(this).attr("data-id");
                    jqueryVariable.currentAreaInfo.currentAreaName = $(this).text();
                    jqueryVariable.currentAreaInfo.currentAreaPosition = $(this).next().attr("data-position");
                    jqueryVariable.currentAreaInfo.currentTownaId = -1;
                    jqueryVariable.currentAreaInfo.currentTownaName = "";
                    jqueryVariable.currentAreaInfo.currentTownaPosition = -1;
                    jqueryVariable.currentAreaInfo.currentSiteId = -1;
                    jqueryVariable.currentAreaInfo.currentSiteName = "";
                    jqueryVariable.currentAreaInfo.currentSitePosition = -1;
                    loadTowna(jqueryVariable);
                    loadSite(jqueryVariable);
                } else if ($(this).parent().hasClass("LocationChooseContentBoxLi_Towna")) {
                    $(".LocationChooseContentBoxLi_Towna").removeClass("LocationChooseContentBoxLi_Linked");
                    $(this).parent().addClass("LocationChooseContentBoxLi_Linked");
                    jqueryVariable.currentAreaInfo.currentId = $(this).attr("data-id");
                    jqueryVariable.currentAreaInfo.currentTownaId = $(this).attr("data-id");
                    jqueryVariable.currentAreaInfo.currentTownaName = $(this).text();
                    jqueryVariable.currentAreaInfo.currentTownaPosition = $(this).next().attr("data-position");
                    jqueryVariable.currentAreaInfo.currentSiteId = -1;
                    jqueryVariable.currentAreaInfo.currentSiteName = "";
                    jqueryVariable.currentAreaInfo.currentSitePosition = -1;
                    loadSite(jqueryVariable);
                }
            });

            $(document).off('click', '.LocationChooseContentBoxLiSelect,.LocationChooseContentBoxLiSelected');
            $(document).on('click', '.LocationChooseContentBoxLiSelect,.LocationChooseContentBoxLiSelected', function () {
                if (!jqueryVariable.singleSelect) {
                    if ($(this).attr("class") == "LocationChooseContentBoxLiSelected") {
                        $(this).attr("class", "LocationChooseContentBoxLiSelect");
                        deleteSelectLocation(jqueryVariable, $(this).attr("data-id"));
                    } else {
                        $(this).attr("class", "LocationChooseContentBoxLiSelected");
                        selectLocation(jqueryVariable, { "ID": $(this).attr("data-id"), "POSITION": $(this).attr("data-position"), "SHORTNAME": $(this).attr("data-shortname"), "FULLNAME": $(this).attr("data-fullname"), "TYPE": $(this).attr("data-type") });
                    }
                } else {
                    $(".LocationChooseContentBoxLiSelect,.LocationChooseContentBoxLiSelected").attr("class", "LocationChooseContentBoxLiSelect");
                    $(this).attr("class", "LocationChooseContentBoxLiSelected");
                    selectLocation(jqueryVariable, { "ID": $(this).attr("data-id"), "POSITION": $(this).attr("data-position"), "SHORTNAME": $(this).attr("data-shortname"), "FULLNAME": $(this).attr("data-fullname"), "TYPE": $(this).attr("data-type") });
                }
            });

            $(document).off('click', '.LocationStorageLiClose');
            $(document).on('click', '.LocationStorageLiClose', function () {
                deleteSelectLocation(jqueryVariable, $(this).attr("data-id"));
            });

            $(".ContentTitle_Site").unbind("click").click(function () {
                loadSite(jqueryVariable, true);
            });

            $(".maxBoxSure").unbind("click").click(function () {
                //读取position全称,选择在此处读取选择项的全称是为了访问效率考虑
                var selectedLocationArray = jqueryVariable.selectedLocationArray;
                var positionArray = [];
                for (var i = 0; i < selectedLocationArray.length; i++) {
                    positionArray.push(selectedLocationArray[i].POSITION);
                }

                $.getJSON("/ServiceHandler/SysInfo/SysAddress.ashx" + "?" + Math.random(), { action: "GetPositionName", positionArray: JSON.stringify(positionArray) },
                    function (data) {
                        if (data != null && data.ResultCode == 0 && data.ResultDataFull != null) {
                            var positionObjArray = data.ResultDataFull;
                            for (var j = 0; j < selectedLocationArray.length; j++) {
                                var pointArray = Enumerable.From(positionObjArray).Where(function (k) { return k.POSITION == selectedLocationArray[j].POSITION; }).ToArray();
                                if (pointArray && pointArray.length > 0) {
                                    selectedLocationArray[j].FULLNAME = pointArray[0].POSITIONNAME;
                                }
                            }
                            $(".maxBox").remove();
                            opt.afterChoosedEvent(selectedLocationArray);
                        }
                    });
                $(".maxBox").remove();
            });

            $(".maxBoxClose").unbind("click").click(function () {
                $(".maxBox").remove();
                opt.afterClosedEvent();
            });

            $(".maxBox").css("top", ($(window).height() - $(".maxBox").height()) / 2);
            $(".maxBox").css("left", ($(window).width() - $(".maxBox").width()) / 2);
        }
    };

    var deleteSelectLocation = function (jqueryVariable, id) {
        for (var i = 0; i < jqueryVariable.selectedLocationArray.length; i++) {
            if (id == jqueryVariable.selectedLocationArray[i].ID) {
                jqueryVariable.selectedLocationArray[i] = "undefined";
            }
        }

        var selectedLocationArrayTemp = [];
        for (var i = 0; i < jqueryVariable.selectedLocationArray.length; i++) {
            if (jqueryVariable.selectedLocationArray[i] != "undefined") {
                selectedLocationArrayTemp.push(jqueryVariable.selectedLocationArray[i]);
            }
        }

        jqueryVariable.selectedLocationArray = selectedLocationArrayTemp;
        initSelectLocation(jqueryVariable);
        // 取消关联复选框
        $('.LocationChooseContentBoxLiSelected').each(function () {
            var $this = $(this);
            var existArray = Enumerable.From(jqueryVariable.selectedLocationArray).Where(function (k) { return k.ID == $this.attr("data-id") && k.TYPE == $this.attr("data-type"); }).ToArray();
            if (existArray.length > 0) {
                $(this).attr("class", "LocationChooseContentBoxLiSelected");
            } else {
                $(this).attr("class", "LocationChooseContentBoxLiSelect");
            }
        });
    }

    var selectLocation = function (jqueryVariable, locationItem) {
        if (!locationItem || !locationItem.ID) {
            return;
        }

        if (jqueryVariable.singleSelect) {
            jqueryVariable.selectedLocationArray = [];
            jqueryVariable.selectedLocationArray.push(locationItem);
        } else {
            if (jqueryVariable.selectedLocationArray.length <= 0) {
                jqueryVariable.selectedLocationArray.push(locationItem);
            } else {
                var existArray = Enumerable.From(jqueryVariable.selectedLocationArray).Where(function (i) { return i.ID == locationItem.ID && i.TYPE == locationItem.TYPE; }).ToArray();
                if (existArray.length <= 0) {
                    jqueryVariable.selectedLocationArray.push(locationItem);
                }
            }
        }

        initSelectLocation(jqueryVariable);
    }

    var initSelectLocation = function (jqueryVariable) {
        $("#selectLocationBox").empty();
        var selectedLocationArray = jqueryVariable.selectedLocationArray;
        if (selectedLocationArray.length > 0) {
            var selectedHtml = "";
            for (var i = 0; i < selectedLocationArray.length; i++) {
                if (selectedLocationArray[i].ID == "" || selectedLocationArray[i].SHORTNAME == "") {
                    continue;
                }

                var liClass = "";
                if (selectedLocationArray[i].TYPE == "Z") {
                    liClass = "LocationStorageLi_Site";
                } else {
                    // eg:0-110-1101-11234-122334(国家-省-市-区-街道)
                    if (selectedLocationArray[i].POSITION == "" || selectedLocationArray[i].POSITION == null) {
                        liClass = "LocationStorageLi_Province";
                    } else {
                        var splitPosition = selectedLocationArray[i].POSITION.split("-");
                        switch (splitPosition.length) {
                            case 2:
                                liClass = "LocationStorageLi_Province";
                                break;
                            case 3:
                                liClass = "LocationStorageLi_City";
                                break;
                            case 4:
                                liClass = "LocationStorageLi_Area";
                                break;
                            case 5:
                                liClass = "LocationStorageLi_Towna";
                                break;
                        }
                    }
                }
                selectedHtml += "<li class=\"baseLi LocationStorageLi " + liClass + "\"><span class=\"LocationStorageLiTitle\">" + selectedLocationArray[i].SHORTNAME + "</span><span class=\"LocationStorageLiClose\" data-id=\"" + selectedLocationArray[i].ID + "\" data-position=\"" + selectedLocationArray[i].POSITION + "\" >×</span></li>";
            }

            $("#selectLocationBox").append(selectedHtml);
        }
    }

    var loadProvince = function (jqueryVariable, params) {
        var locationArray = jqueryVariable.locationArray;
        if (locationArray != null) {
            $("#provincesListBox").empty();
            var provinceArray = [];
            if (params && params.FirstLetter != "*") {
                if (params.FirstLetter) {
                    provinceArray = Enumerable.From(locationArray).Where(function (i) { if (i.ACRONYM) { return i.PARENTID == 0 && i.ACRONYM.substring(0, 1) == params.FirstLetter; } else { return false; } }).ToArray();
                }
            } else {
                provinceArray = Enumerable.From(locationArray).Where(function (i) { return i.PARENTID == 0; }).ToArray();
            }

            var provinceHtml = "";
            for (var i = 0; i < provinceArray.length; i++) {
                var locationChooseContentBoxLiSelect_Class = "LocationChooseContentBoxLiSelect";
                var existSelected = Enumerable.From(jqueryVariable.selectedLocationArray).Where(function (i) { return i.ID == provinceArray[i].ID; }).ToArray();
                if (existSelected.length > 0) {
                    locationChooseContentBoxLiSelect_Class = "LocationChooseContentBoxLiSelected";
                }

                if (!jqueryVariable.enableAddressSelect) {
                    provinceHtml += "<li class=\"baseli LocationChooseContentBoxLi LocationChooseContentBoxLi_Province\" ><span class=\"LocationChooseContentBoxLiTitle\" data-id=\"" + provinceArray[i].ID + "\">" + provinceArray[i].SHORTNAME + "</span>";
                } else {
                    provinceHtml += "<li class=\"baseli LocationChooseContentBoxLi LocationChooseContentBoxLi_Province\" ><span class=\"LocationChooseContentBoxLiTitle\" data-id=\"" + provinceArray[i].ID + "\">" + provinceArray[i].SHORTNAME + "</span><span class=\"" + locationChooseContentBoxLiSelect_Class + "\" data-id=\"" + provinceArray[i].ID + "\" data-position=\"" + provinceArray[i].POSITION + "-" + provinceArray[i].ID + "\" data-shortname=\"" + provinceArray[i].SHORTNAME + "\" data-fullname=\"" + provinceArray[i].POSITIONNAME + "\" data-type=\"Q\"></span>";
                }
            }
            $("#provincesListBox").append(provinceHtml);
        }
    }

    var loadCity = function (jqueryVariable, firstLetter) {
        // 两种模式（1、需要父节点省 2、不需要父节点省）
        $("#citysListBox").empty();
        var locationArray = jqueryVariable.locationArray;
        var cityArray = [];
        if (locationArray == null) {
            return;
        }
        var currentAreaInfo = jqueryVariable.currentAreaInfo;
        var locationArray = jqueryVariable.locationArray;
        if (jqueryVariable.needProvince) {
            if (currentAreaInfo.currentProvinceId == -1) {
                return;
            }

            if (firstLetter && firstLetter != "*") {
                cityArray = Enumerable.From(locationArray).Where(function (i) { if (i.ACRONYM) { return i.LAYER == 2 && i.PARENTID == currentAreaInfo.currentProvinceId && i.ACRONYM.substring(0, 1) == firstLetter; } else { return false; } }).ToArray();
            } else {
                cityArray = Enumerable.From(locationArray).Where(function (i) { return i.LAYER == 2 && i.PARENTID == currentAreaInfo.currentProvinceId; }).ToArray();
            }
        } else {
            if (firstLetter && firstLetter != "*") {
                cityArray = Enumerable.From(locationArray).Where(function (i) { if (i.ACRONYM) { return i.LAYER == 2 && i.ACRONYM.substring(0, 1) == firstLetter; } }).ToArray();
            } else {
                cityArray = Enumerable.From(locationArray).Where(function (i) { return i.LAYER == 2; }).ToArray();
            }
        }

        var cityHtml = "";
        for (var i = 0; i < cityArray.length; i++) {
            var locationChooseContentBoxLiSelect_Class = "LocationChooseContentBoxLiSelect";
            var existSelected = Enumerable.From(jqueryVariable.selectedLocationArray).Where(function (k) { return k.ID == cityArray[i].ID; }).ToArray();
            if (existSelected.length > 0) {
                locationChooseContentBoxLiSelect_Class = "LocationChooseContentBoxLiSelected";
            }
            if (!jqueryVariable.enableAddressSelect) {
                cityHtml += "<li class=\"baseli LocationChooseContentBoxLi LocationChooseContentBoxLi_City\" ><span class=\"LocationChooseContentBoxLiTitle\" data-id=\"" + cityArray[i].ID + "\">" + cityArray[i].SHORTNAME + "</span>";
            } else {
                cityHtml += "<li class=\"baseli LocationChooseContentBoxLi LocationChooseContentBoxLi_City\" ><span class=\"LocationChooseContentBoxLiTitle\" data-id=\"" + cityArray[i].ID + "\">" + cityArray[i].SHORTNAME + "</span><span class=\"" + locationChooseContentBoxLiSelect_Class + "\" data-id=\"" + cityArray[i].ID + "\" data-position=\"" + cityArray[i].POSITION + "-" + cityArray[i].ID + "\" data-shortname=\"" + cityArray[i].SHORTNAME + "\" data-fullname=\"" + cityArray[i].POSITIONNAME + "\" data-type=\"Q\"></span>";
            }
        }
        $("#citysListBox").append(cityHtml);
    }

    var loadArea = function (jqueryVariable) {
        $("#areasListBox").empty();
        var currentAreaInfo = jqueryVariable.currentAreaInfo;
        if (currentAreaInfo.currentCityId == -1) {
            return;
        }

        var areaArray = [];
        var parmsArray = [
              { name: 'LAYER', value: 3 },
              { name: 'parentid', value: currentAreaInfo.currentCityId }
            ];

        var customSearchFilters = formatSearchParames(parmsArray);
        $.getJSON("/ServiceHandler/SysInfo/SysAddress.ashx" + "?" + Math.random(), { action: "GetList", customSearchFilters: customSearchFilters },
                    function (data) {
                        if (data != null && data.ResultCode == 0 && data.ResultDataFull != null) {
                            areaArray = data.ResultDataFull;
                            var areaHtml = "";
                            for (var i = 0; i < areaArray.length; i++) {
                                var locationChooseContentBoxLiSelect_Class = "LocationChooseContentBoxLiSelect";
                                var existSelected = Enumerable.From(jqueryVariable.selectedLocationArray).Where(function (k) { return k.ID == areaArray[i].ID; }).ToArray();
                                if (existSelected.length > 0) {
                                    locationChooseContentBoxLiSelect_Class = "LocationChooseContentBoxLiSelected";
                                }
                                if (!jqueryVariable.enableAddressSelect) {
                                    areaHtml += "<li class=\"baseli LocationChooseContentBoxLi LocationChooseContentBoxLi_Area\" ><span class=\"LocationChooseContentBoxLiTitle\" data-id=\"" + areaArray[i].ID + "\">" + areaArray[i].SHORTNAME + "</span>";
                                } else {
                                    areaHtml += "<li class=\"baseli LocationChooseContentBoxLi LocationChooseContentBoxLi_Area\" ><span class=\"LocationChooseContentBoxLiTitle\" data-id=\"" + areaArray[i].ID + "\">" + areaArray[i].SHORTNAME + "</span><span class=\"" + locationChooseContentBoxLiSelect_Class + "\" data-id=\"" + areaArray[i].ID + "\" data-position=\"" + areaArray[i].POSITION + "-" + areaArray[i].ID + "\" data-shortname=\"" + areaArray[i].SHORTNAME + "\" data-fullname=\"" + areaArray[i].POSITIONNAME + "\" data-type=\"Q\"></span>";
                                }
                            }
                            $("#areasListBox").append(areaHtml);
                        }
                    });
    }

    var loadTowna = function (jqueryVariable) {
        $("#townasListBox").empty();
        var currentAreaInfo = jqueryVariable.currentAreaInfo;
        if (currentAreaInfo.currentAreaId == -1) {
            return;
        }

        var townaArray = [];
        var parmsArray = [
              { name: 'LAYER', value: 4 },
              { name: 'parentid', value: currentAreaInfo.currentAreaId }
            ];

        var customSearchFilters = formatSearchParames(parmsArray);
        $.getJSON("/ServiceHandler/SysInfo/SysAddress.ashx" + "?" + Math.random(), { action: "GetList", customSearchFilters: customSearchFilters },
                    function (data) {
                        if (data != null && data.ResultCode == 0 && data.ResultDataFull != null) {
                            townaArray = data.ResultDataFull;
                            var townaHtml = "";
                            for (var i = 0; i < townaArray.length; i++) {
                                var locationChooseContentBoxLiSelect_Class = "LocationChooseContentBoxLiSelect";
                                var existSelected = Enumerable.From(jqueryVariable.selectedLocationArray).Where(function (k) { return k.ID == townaArray[i].ID; }).ToArray();
                                if (existSelected.length > 0) {
                                    locationChooseContentBoxLiSelect_Class = "LocationChooseContentBoxLiSelected";
                                }
                                if (!jqueryVariable.enableAddressSelect) {
                                    townaHtml += "<li class=\"baseli LocationChooseContentBoxLi LocationChooseContentBoxLi_Towna\" ><span class=\"LocationChooseContentBoxLiTitle\" data-id=\"" + townaArray[i].ID + "\">" + townaArray[i].SHORTNAME + "</span>";
                                } else {
                                    townaHtml += "<li class=\"baseli LocationChooseContentBoxLi LocationChooseContentBoxLi_Towna\" ><span class=\"LocationChooseContentBoxLiTitle\" data-id=\"" + townaArray[i].ID + "\">" + townaArray[i].SHORTNAME + "</span><span class=\"" + locationChooseContentBoxLiSelect_Class + "\" data-id=\"" + townaArray[i].ID + "\" data-position=\"" + townaArray[i].POSITION + "-" + townaArray[i].ID + "\" data-shortname=\"" + townaArray[i].SHORTNAME + "\" data-fullname=\"" + townaArray[i].POSITIONNAME + "\" data-type=\"Q\"></span>";
                                }
                            }
                            $("#townasListBox").append(townaHtml);
                        }
                    });
    }

    var loadSite = function (jqueryVariable, isShowAll) {
        $("#siteListBox").empty();
        var currentAreaInfo = jqueryVariable.currentAreaInfo;
        if (!isShowAll) {
            if (currentAreaInfo.currentId == -1) {
                return;
            }
        }

        var siteArray = [];
        var parmsArray = [];
        if (!isShowAll) {
            parmsArray = [
              { name: 'STREET', value: currentAreaInfo.currentId, op: "cn" }
            ];
        }

        var customSearchFilters = formatSearchParames(parmsArray);
        $.getJSON("/ServiceHandler/BaseInfo/Zd_Site.ashx" + "?" + Math.random(), { action: "GetList", customSearchFilters: customSearchFilters },
                    function (data) {
                        if (data != null && data.ResultCode == 0 && data.ResultDataFull != null) {
                            siteArray = data.ResultDataFull;
                            var siteHtml = "";
                            for (var i = 0; i < siteArray.length; i++) {
                                var locationChooseContentBoxLiSelect_Class = "LocationChooseContentBoxLiSelect";
                                var existSelected = Enumerable.From(jqueryVariable.selectedLocationArray).Where(function (k) { return k.ID == siteArray[i].CODE; }).ToArray();
                                if (existSelected.length > 0) {
                                    locationChooseContentBoxLiSelect_Class = "LocationChooseContentBoxLiSelected";
                                }
                                siteHtml += "<li class=\"baseli LocationChooseContentBoxLi LocationChooseContentBoxLi_Site\" ><span class=\"LocationChooseContentBoxLiTitle\" data-id=\"" + siteArray[i].CODE + "\">" + siteArray[i].NAME + "</span><span class=\"" + locationChooseContentBoxLiSelect_Class + "\" data-id=\"" + siteArray[i].CODE + "\" data-position=\"" + siteArray[i].STREET + "\" data-shortname=\"" + siteArray[i].NAME + "\" data-fullname=\"" + siteArray[i].NAME + "\" data-type=\"Z\"></span>";
                            }
                            $("#siteListBox").append(siteHtml);
                        }
                    });
    }

    var getProvinceCityData = function (jqueryVariable) {
        var parmsArray = [
                    { name: 'LAYER', value: "1,2", op: "in" }
                ];
        var customSearchFilters = formatSearchParames(parmsArray);
        $.getJSON("/ServiceHandler/SysInfo/SysAddress.ashx" + "?" + Math.random(), { action: "GetList", customSearchFilters: customSearchFilters },
                    function (data) {
                        if (data != null && data.ResultCode == 0 && data.ResultDataFull != null) {
                            jqueryVariable.locationArray = data.ResultDataFull;
                            if (jqueryVariable.needProvince) {
                                loadProvince(jqueryVariable);
                                loadCity(jqueryVariable);
                                loadArea(jqueryVariable);
                                loadTowna(jqueryVariable);
                            }
                            else {
                                loadCity(jqueryVariable);
                                loadArea(jqueryVariable);
                                loadTowna(jqueryVariable);
                            }
                        }
                    });
    }
})(jQuery);