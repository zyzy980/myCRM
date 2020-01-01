/**获取当前session对象相关值**/
var getSession = function (levelPath) {
    var resultJson;
    var serverUrl = levelPath + "ServiceHandler/SysInfo/SysUsers.ashx?" + Math.random();
    $.ajax({
        type: "POST",
        url: serverUrl,
        data: { action: "GetSession" },
        async: false,
        dataType: "json",
        success: function (json) {
            if (isServerResultDataPass(json)) {
                resultJson = json.ResultDataFull;
            }
        }
    });

    return resultJson;
}

var AuthorityData;
/**通过moduleName获取用于读取权限设置EasyUI框架toolbar按钮的隐藏和显示**/
var getAuthorityByName = function (moduleName, bindControlId) {
    var serverUrl = "/ServiceHandler/SysInfo/SysAuthorith.ashx?" + Math.random();
    $.getJSON(serverUrl, { action: "GetAuthorityListByName", moduleName: moduleName }, function (dataObj) {
        if (isServerResultDataPass(dataObj)) {
            AuthorityData = dataObj.ResultDataFull;
            for (var i = 0; i < dataObj.ResultDataFull.length; i++) {
                $("#" + bindControlId + " a").each(function () {
                    if ($(this).attr("id") == dataObj.ResultDataFull[i].ButtonUse) {
                        $(this).css("display", "inline-block");
                        return false;
                    }
                    return true;
                });
            }
        }
    });
}

/**通过moduleid获取用于读取权限设置EasyUI框架toolbar按钮的隐藏和显示**/
var getAuthorityById = function (moduleId, bindControlId) {//模块id和增删改查按钮外大的
    var serverUrl = "/ServiceHandler/SysInfo/SysAuthorith.ashx?" + Math.random(); //防止有相同的
    $.getJSON(serverUrl, { action: "GetAuthorityListById", moduleId: moduleId }, function (dataObj) {//方法参数事件
        if (isServerResultDataPass(dataObj)) {
            AuthorityData = dataObj.ResultDataFull;
            for (var i = 0; i < dataObj.ResultDataFull.length; i++) {
                $("#" + bindControlId + " a").each(function () {
                    if ($(this).attr("id") == dataObj.ResultDataFull[i].ButtonUse) {
                        $(this).css("display", "inline-block");
                        return false;
                    }
                    return true;
                });
            }
        }
    });
}
/*读取系统字典数据（1：直接从数据库中读取-database；2：直接从静态变量的缓存中读取-cache）*/
var getDictionaryData = function (p) {
    if (!p) {
        return;
    }
    $.ajax({
        type: "POST",
        url: "/ServiceHandler/SysInfo/SysDictionary.ashx?" + Math.random(),
        data: { action: "GetDictionaryDataListCache", dicKey: p.dicTypeCode },
        async: p.async,
        dataType: "json",
        success: function (json) {
            if (isServerResultDataPass(json)) {
                // 返回的格式 [{ 'Value': '0', 'Text': '正常','Default':'0' }
                var callback = p ? p.callback : null;
                if ($.isFunction(p.callback)) {
                    callback(json.ResultDataFull);
                }
            }
        }
    });
}

var getData_Dealer = function (customSearchFilters, async_, callbackFunc) {
    var async = false;
    if (async_) {
        async = async_;
    }
    var resultJson;
    $.ajax({
        type: "POST",
        url: "/ServiceHandler/BaseInfo/Zd_Dealer.ashx?" + Math.random() + "&customSearchFilters=" + customSearchFilters,
        data: { action: "GetList" },
        async: async,
        dataType: "json",
        success: function (json) {
            if (isServerResultDataPass(json)) {
                resultJson = json.ResultDataFull;
                if (callbackFunc) {
                    callbackFunc(resultJson);
                }
            }
        }
    });

    return resultJson;
}

var getData_ErrorKind = function (customSearchFilters, async_, callbackFunc) {
    var async = false;
    if (async_) {
        async = async_;
    }
    var resultJson;
    $.ajax({
        type: "POST",
        url: "/ServiceHandler/BaseInfo/Zd_ErrorKind.ashx?" + Math.random() + "&customSearchFilters=" + customSearchFilters,
        data: { action: "GetList" },
        async: async,
        dataType: "json",
        success: function (json) {
            if (isServerResultDataPass(json)) {
                resultJson = json.ResultDataFull;
                if (callbackFunc) {
                    callbackFunc(resultJson);
                }
            }
        }
    });

    return resultJson;
}

var getData_Provinces = function (customSearchFilters, async_, callbackFunc) {
    var async = false;
    if (async_) {
        async = async_;
    }
    var resultJson;
    $.ajax({
        type: "POST",
        url: "/ServiceHandler/BaseInfo/Zd_Express.ashx?" + Math.random() + "&customSearchFilters=" + customSearchFilters,
        data: { action: "GetListProvinces" },
        async: async,
        dataType: "json",
        success: function (json) {
            if (isServerResultDataPass(json)) {
                resultJson = json.ResultDataFull;
                if (callbackFunc) {
                    callbackFunc(resultJson);
                }
            }
        }
    });

    return resultJson;
}

var get_Citys = function (customSearchFilters, async_, callbackFunc) {
    var async = false;
    if (async_) {
        async = async_;
    }
    var resultJson;
    $.ajax({
        type: "POST",
        url: "/ServiceHandler/SysInfo/SysAddress.ashx?" + Math.random() + "&customSearchFilters=" + customSearchFilters,
        data: { action: "GetCitys" },
        async: async,
        dataType: "json",
        success: function (json) {
            if (isServerResultDataPass(json)) {
                resultJson = json.ResultDataFull;
                if (callbackFunc) {
                    callbackFunc(resultJson);
                }
            }
        }
    });

    return resultJson;
}

var get_CitysByDealer = function (customSearchFilters, async_, callbackFunc) {
    var async = false;
    if (async_) {
        async = async_;
    }
    var resultJson;
    $.ajax({
        type: "POST",
        url: "/ServiceHandler/BaseInfo/Zd_Dealer.ashx?" + Math.random() + "&customSearchFilters=" + customSearchFilters,
        data: { action: "GetCity" },
        async: async,
        dataType: "json",
        success: function (json) {
            if (isServerResultDataPass(json)) {
                resultJson = json.ResultDataFull;
                if (callbackFunc) {
                    callbackFunc(resultJson);
                }
            }
        }
    });

    return resultJson;
}

var get_RouteList = function (customSearchFilters, async_, callbackFunc) {
    var async = false;
    if (async_) {
        async = async_;
    }
    var resultJson;
    $.ajax({
        type: "POST",
        url: "/ServiceHandler/BaseInfo/Zd_Route.ashx?" + Math.random() + "&customSearchFilters=" + customSearchFilters,
        data: { action: "GetRouteList" },
        async: async,
        dataType: "json",
        success: function (json) {
            if (isServerResultDataPass(json)) {
                resultJson = json.ResultDataFull;
                if (callbackFunc) {
                    callbackFunc(resultJson);
                }
            }
        }
    });

    return resultJson;
}

var getData_Citys = function (customSearchFilters, async_, callbackFunc) {
    var async = false;
    if (async_) {
        async = async_;
    }
    var resultJson;
    $.ajax({
        type: "POST",
        url: "/ServiceHandler/BaseInfo/Zd_Express.ashx?" + Math.random() + "&customSearchFilters=" + customSearchFilters,
        data: { action: "GetListCitys" },
        async: async,
        dataType: "json",
        success: function (json) {
            if (isServerResultDataPass(json)) {
                resultJson = json.ResultDataFull;
                if (callbackFunc) {
                    callbackFunc(resultJson);
                }
            }
        }
    });

    return resultJson;
}

var getData_Contractor = function (customSearchFilters, async_, callbackFunc) {
    var async = false;
    if (async_) {
        async = async_;
    }
    var resultJson;
    $.ajax({
        type: "POST",
        url: "/ServiceHandler/BaseInfo/Zd_Contractor.ashx?" + Math.random() + "&customSearchFilters=" + customSearchFilters,
        data: { action: "GetList" },
        async: async,
        dataType: "json",
        success: function (json) {
            if (isServerResultDataPass(json)) {
                resultJson = json.ResultDataFull;
                if (callbackFunc) {
                    callbackFunc(resultJson);
                }
            }
        }
    });

    return resultJson;
}

var getData_TwoContractor = function (customSearchFilters, async_, callbackFunc) {
    var async = false;
    if (async_) {
        async = async_;
    }
    var resultJson;
    $.ajax({
        type: "POST",
        url: "/ServiceHandler/BaseInfo/Zd_Contractor.ashx?" + Math.random() + "&customSearchFilters=" + customSearchFilters,
        data: { action: "GetListTwo" },
        async: async,
        dataType: "json",
        success: function (json) {
            if (isServerResultDataPass(json)) {
                resultJson = json.ResultDataFull;
                if (callbackFunc) {
                    callbackFunc(resultJson);
                }
            }
        }
    });

    return resultJson;
}

var getData_DICG = function (customSearchFilters, async_, callbackFunc) {
    var async = false;
    if (async_) {
        async = async_;
    }
    var resultJson;
    $.ajax({
        type: "POST",
        url: "/ServiceHandler/BaseInfo/Zd_Truck.ashx?" + Math.random() + "&customSearchFilters=" + customSearchFilters,
        data: { action: "GetDICGList" },
        async: async,
        dataType: "json",
        success: function (json) {
            if (isServerResultDataPass(json)) {
                resultJson = json.ResultDataFull;
                if (callbackFunc) {
                    callbackFunc(resultJson);
                }
            }
        }
    });

    return resultJson;
}

var getData_ExpressCom = function (customSearchFilters, async_, callbackFunc) {
    var async = false;
    if (async_) {
        async = async_;
    }
    var resultJson;
    $.ajax({
        type: "POST",
        url: "/ServiceHandler/BaseInfo/Zd_ExpressCom.ashx?" + Math.random() + "&customSearchFilters=" + customSearchFilters,
        data: { action: "GetList" },
        async: async,
        dataType: "json",
        success: function (json) {
            if (isServerResultDataPass(json)) {
                resultJson = json.ResultDataFull;
                if (callbackFunc) {
                    callbackFunc(resultJson);
                }
            }
        }
    });

    return resultJson;
}

var getData_Driver = function (customSearchFilters, async_, callbackFunc) {
    var async = false;
    if (async_) {
        async = async_;
    }
    var resultJson;
    $.ajax({
        type: "POST",
        url: "/ServiceHandler/BaseInfo/Zd_Driver.ashx?" + Math.random() + "&customSearchFilters=" + customSearchFilters,
        data: { action: "GetList" },
        async: async,
        dataType: "json",
        success: function (json) {
            if (isServerResultDataPass(json)) {
                resultJson = json.ResultDataFull;
                if (callbackFunc) {
                    callbackFunc(resultJson);
                }
            }
        }
    });

    return resultJson;
}

//后期会被弃用
//var getData_DriverCodeByInfo = function (customSearchFilters, code, truckno, async_) {
//    var async = false;
//    if (async_) {
//        async = async_;
//    }
//    var resultJson;
//    $.ajax({
//        type: "POST",
//        url: "/ServiceHandler/BaseInfo/Zd_Truck.ashx?" + Math.random() + "&customSearchFilters=" + customSearchFilters + "&code=" + code + "&truckno=" + truckno,
//        data: { action: "GetListByInfo" },
//        async: async,
//        dataType: "json",
//        success: function (json) {
//            if (isServerResultDataPass(json)) {
//                resultJson = json.ResultDataFull;
//            }
//        }
//    });

//    return resultJson;
//}

//var getData_DriverCodeByInfo = function (customSearchFilters, async_, callbackFunc) {
//    var async = false;
//    if (async_) {
//        async = async_;
//    }
//    var resultJson;
//    $.ajax({
//        type: "POST",
//        url: "/ServiceHandler/BaseInfo/Zd_Truck.ashx?" + Math.random() + "&customSearchFilters=" + customSearchFilters,
//        data: { action: "GetListByInfo" },
//        async: async,
//        dataType: "json",
//        success: function (json) {
//            if (isServerResultDataPass(json)) {
//                resultJson = json.ResultDataFull;
//            }
//        }
//    });

//    return resultJson;
//}

var getData_YwLocation = function (customSearchFilters, async_, callbackFunc) {
    var async = false;
    if (async_) {
        async = async_;
    }
    var resultJson;
    $.ajax({
        type: "POST",
        url: "/ServiceHandler/BaseInfo/Zd_YwLocation.ashx?" + Math.random() + "&customSearchFilters=" + customSearchFilters,
        data: { action: "GetList" },
        async: async,
        dataType: "json",
        success: function (json) {
            if (isServerResultDataPass(json)) {
                resultJson = json.ResultDataFull;
                if (callbackFunc) {
                    callbackFunc(resultJson);
                }
            }
        }
    });

    return resultJson;
}

var getData_KdType = function (customSearchFilters, async_, callbackFunc) {
    var async = false;
    if (async_) {
        async = async_;
    }
    var resultJson;
    $.ajax({
        type: "POST",
        //        url: "/ServiceHandler/BaseInfo/Zd_Package.ashx?" + Math.random() + "&customSearchFilters=" + customSearchFilters,
        url: "/ServiceHandler/BaseInfo/Zd_PartType.ashx?" + Math.random() + "&customSearchFilters=" + customSearchFilters,
        data: { action: "GetKdTypeList" },
        async: async,
        dataType: "json",
        success: function (json) {
            if (isServerResultDataPass(json)) {
                resultJson = json.ResultDataFull;
                if (callbackFunc) {
                    callbackFunc(resultJson);
                }
            }
        }
    });

    return resultJson;
}

var getData_ParType = function (customSearchFilters, async_, callbackFunc) {
    var async = false;
    if (async_) {
        async = async_;
    }
    var resultJson;
    $.ajax({
        type: "POST",
        //        url: "/ServiceHandler/BaseInfo/Zd_Package.ashx?" + Math.random() + "&customSearchFilters=" + customSearchFilters,
        url: "/ServiceHandler/BaseInfo/Zd_PartType.ashx?" + Math.random() + "&customSearchFilters=" + customSearchFilters,
        data: { action: "GetParTypeList" },
        async: async,
        dataType: "json",
        success: function (json) {
            if (isServerResultDataPass(json)) {
                resultJson = json.ResultDataFull;
                if (callbackFunc) {
                    callbackFunc(resultJson);
                }
            }
        }
    });

    return resultJson;
}

var getData_Part = function (customSearchFilters, async_, callbackFunc) {
    var async = false;
    if (async_) {
        async = async_;
    }
    var resultJson;
    $.ajax({
        type: "POST",
        url: "/ServiceHandler/BaseInfo/Zd_PartType.ashx?" + Math.random() + "&customSearchFilters=" + customSearchFilters,
        data: { action: "GetList" },
        async: async,
        dataType: "json",
        success: function (json) {
            if (isServerResultDataPass(json)) {
                resultJson = json.ResultDataFull;
                if (callbackFunc) {
                    callbackFunc(resultJson);
                }
            }
        }
    });

    return resultJson;
}

var getData_PartList = function (customSearchFilters, async_, callbackFunc) {
    var async = false;
    if (async_) {
        async = async_;
    }
    var resultJson;
    $.ajax({
        type: "POST",
        url: "/ServiceHandler/BaseInfo/Zd_Part.ashx?" + Math.random() + "&customSearchFilters=" + customSearchFilters,
        data: { action: "GetList" },
        async: async,
        dataType: "json",
        success: function (json) {
            if (isServerResultDataPass(json)) {
                resultJson = json.ResultDataFull;
                if (callbackFunc) {
                    callbackFunc(resultJson);
                }
            }
        }
    });

    return resultJson;
}

var getData_OrderType = function (customSearchFilters, async_, callbackFunc) {
    var async = false;
    if (async_) {
        async = async_;
    }
    var resultJson;
    $.ajax({
        type: "POST",
        url: "/ServiceHandler/BaseInfo/Zd_TransportMode.ashx?" + Math.random() + "&customSearchFilters=" + customSearchFilters,
        data: { action: "GetList" },
        async: async,
        dataType: "json",
        success: function (json) {
            if (isServerResultDataPass(json)) {
                resultJson = json.ResultDataFull;
                if (callbackFunc) {
                    callbackFunc(resultJson);
                }
            }
        }
    });

    return resultJson;
}

var getData_Order = function (customSearchFilters, async_, callbackFunc) {
    var async = false;
    if (async_) {
        async = async_;
    }
    var resultJson;
    $.ajax({
        type: "POST",
        url: "/ServiceHandler/BaseInfo/Zd_OrderType.ashx?" + Math.random() + "&customSearchFilters=" + customSearchFilters,
        data: { action: "GetList" },
        async: async,
        dataType: "json",
        success: function (json) {
            if (isServerResultDataPass(json)) {
                resultJson = json.ResultDataFull;
                if (callbackFunc) {
                    callbackFunc(resultJson);
                }
            }
        }
    });

    return resultJson;
}

var getData_Cus = function (customSearchFilters, async_, callbackFunc) {
    var async = false;
    if (async_) {
        async = async_;
    }
    var resultJson;
    $.ajax({
        type: "POST",
        url: "/ServiceHandler/BaseInfo/Zd_Cus.ashx?" + Math.random() + "&customSearchFilters=" + customSearchFilters,
        data: { action: "GetList" },
        async: async,
        dataType: "json",
        success: function (json) {
            if (isServerResultDataPass(json)) {
                resultJson = json.ResultDataFull;
                if (callbackFunc) {
                    callbackFunc(resultJson);
                }
            }
        }
    });

    return resultJson;
}

var getData_SiteAddress = function (customSearchFilters, async_, callbackFunc) {

    var async = false;
    if (async_) {
        async = async_;
    }

    var resultJson;
    $.ajax({
        type: "POST",
        url: "/ServiceHandler/BaseInfo/Zd_Location.ashx?" + Math.random() + "&customSearchFilters=" + customSearchFilters,
        data: { action: "GetSiteAdderssList" },
        async: async,
        dataType: "json",
        success: function (json) {
            if (isServerResultDataPass(json)) {
                resultJson = json.ResultDataFull;
                if (callbackFunc) {
                    callbackFunc(resultJson);
                }
            }
        }
    });

    return resultJson;
}

var getData_TruckType = function (customSearchFilters, async_, callbackFunc) {
    var async = false;
    if (async_) {
        async = async_;
    }
    var resultJson;
    $.ajax({
        type: "POST",
        url: "/ServiceHandler/BaseInfo/Zd_TruckType.ashx?" + Math.random() + "&customSearchFilters=" + customSearchFilters,
        data: { action: "GetList" }, //GetListByCon
        async: async,
        dataType: "json",
        success: function (json) {
            if (isServerResultDataPass(json)) {
                resultJson = json.ResultDataFull;
                if (callbackFunc) {
                    callbackFunc(resultJson);
                }
            }
        }
    });

    return resultJson;
}

var getData_TruckTypeByCon = function (customSearchFilters, async_, callbackFunc) {
    var async = false;
    if (async_) {
        async = async_;
    }
    var resultJson;
    $.ajax({
        type: "POST",
        url: "/ServiceHandler/BaseInfo/Zd_TruckType.ashx?" + Math.random() + "&customSearchFilters=" + customSearchFilters,
        data: { action: "GetListByCon" },
        async: async,
        dataType: "json",
        success: function (json) {
            if (isServerResultDataPass(json)) {
                resultJson = json.ResultDataFull;
                if (callbackFunc) {
                    callbackFunc(resultJson);
                }
            }
        }
    });

    return resultJson;
}



//var getData_QianNo = function (customSearchFilters, async_, callbackFunc) {
//    var async = false;
//    if (async_) {
//        async = async_;
//    }
//    var resultJson;
//    $.ajax({
//        type: "POST",
//        url: "/ServiceHandler/BaseInfo/Zd_Truck.ashx?" + Math.random() + "&customSearchFilters=" + customSearchFilters,
//        data: { action: "GetListQian" },
//        async: async,
//        dataType: "json",
//        success: function (json) {
//            if (isServerResultDataPass(json)) {
//                resultJson = json.ResultDataFull;
//            }
//        }
//    });

//    return resultJson;
//}

//var getData_QianNoByCode = function (customSearchFilters, code, async_) {
//    var async = false;
//    if (async_) {
//        async = async_;
//    }
//    var resultJson;
//    $.ajax({
//        type: "POST",
//        url: "/ServiceHandler/BaseInfo/Zd_Truck.ashx?" + Math.random() + "&customSearchFilters=" + customSearchFilters + "&code=" + code,
//        data: { action: "GetListByAppLicontrCode" },
//        async: async,
//        dataType: "json",
//        success: function (json) {
//            if (isServerResultDataPass(json)) {
//                resultJson = json.ResultDataFull;
//            }
//        }
//    });

//    return resultJson;
//}

//var getData_GuaNo = function (customSearchFilters, async_, callbackFunc) {
//    var async = false;
//    if (async_) {
//        async = async_;
//    }
//    var resultJson;
//    $.ajax({
//        type: "POST",
//        url: "/ServiceHandler/BaseInfo/Zd_Truck.ashx?" + Math.random() + "&customSearchFilters=" + customSearchFilters,
//        data: { action: "GetListGua" },
//        async: async,
//        dataType: "json",
//        success: function (json) {
//            if (isServerResultDataPass(json)) {
//                resultJson = json.ResultDataFull;
//            }
//        }
//    });

//    return resultJson;
//}

var getData_Truck = function (customSearchFilters, async_, callbackFunc) {
    var async = false;
    if (async_) {
        async = async_;
    }
    var resultJson;
    $.ajax({
        type: "POST",
        url: "/ServiceHandler/BaseInfo/Zd_Truck.ashx?" + Math.random() + "&customSearchFilters=" + customSearchFilters,
        data: { action: "GetList" },
        async: async,
        dataType: "json",
        success: function (json) {
            if (isServerResultDataPass(json)) {
                resultJson = json.ResultDataFull;
                if (callbackFunc) {
                    callbackFunc(resultJson);
                }
            }
        }
    });

    return resultJson;
}

var getData_TruckG = function (customSearchFilters, async_, callbackFunc) {
    var async = false;
    if (async_) {
        async = async_;
    }
    var resultJson;
    $.ajax({
        type: "POST",
        url: "/ServiceHandler/BaseInfo/Zd_Truck.ashx?" + Math.random() + "&customSearchFilters=" + customSearchFilters,
        data: { action: "GetDICGList" },
        async: async,
        dataType: "json",
        success: function (json) {
            if (isServerResultDataPass(json)) {
                resultJson = json.ResultDataFull;
                if (callbackFunc) {
                    callbackFunc(resultJson);
                }
            }
        }
    });

    return resultJson;
}

var getData_TruckKind = function (customSearchFilters, async_, callbackFunc) {
    var async = false;
    if (async_) {
        async = async_;
    }
    var resultJson;
    $.ajax({
        type: "POST",
        url: "/ServiceHandler/BaseInfo/Zd_TruckType.ashx?" + Math.random() + "&customSearchFilters=" + customSearchFilters,
        data: { action: "GetList" },
        async: async,
        dataType: "json",
        success: function (json) {
            if (isServerResultDataPass(json)) {
                resultJson = json.ResultDataFull;
                if (callbackFunc) {
                    callbackFunc(resultJson);
                }
            }
        }
    });

    return resultJson;
}

var getData_YWLocation = function (customSearchFilters, async_, callbackFunc) {
    var async = false;
    if (async_) {
        async = async_;
    }
    var resultJson;
    $.ajax({
        type: "POST",
        url: "/ServiceHandler/BaseInfo/Zd_YwLocation.ashx?" + Math.random() + "&customSearchFilters=" + customSearchFilters,
        data: { action: "GetList" },
        async: async,
        dataType: "json",
        success: function (json) {
            if (isServerResultDataPass(json)) {
                resultJson = json.ResultDataFull;
                if (callbackFunc) {
                    callbackFunc(resultJson);
                }
            }
        }
    });

    return resultJson;
}


var getData_Location = function (customSearchFilters, async_, callbackFunc) {
    var async = false;
    if (async_) {
        async = async_;
    }
    //    alert("000---"+customSearchFilters);
    var resultJson;
    var resultJson;
    $.ajax({
        type: "POST",
        url: "/ServiceHandler/BaseInfo/Zd_Location.ashx?" + Math.random() + "&customSearchFilters=" + customSearchFilters,
        data: { action: "GetList" },
        async: async,
        dataType: "json",
        success: function (json) {
            if (isServerResultDataPass(json)) {
                resultJson = json.ResultDataFull;
                if (callbackFunc) {
                    callbackFunc(resultJson);
                }
            }
        }
    });

    return resultJson;
}

var getData_GstmSite = function (customSearchFilters, async_, callbackFunc) {
    var async = false;
    if (async_) {
        async = async_;
    }
    var resultJson;
    var resultJson;
    $.ajax({
        type: "POST",
        url: "/ServiceHandler/BaseInfo/Zd_GstmSite.ashx?" + Math.random() + "&customSearchFilters=" + customSearchFilters,
        data: { action: "GetListForSelect" },
        async: async,
        dataType: "json",
        success: function (json) {
            if (isServerResultDataPass(json)) {
                resultJson = json.ResultDataFull;
                if (callbackFunc) {
                    callbackFunc(resultJson);
                }
            }
        }
    });

    return resultJson;
}

//var getData_GstmSite = function (customSearchFilters, async_, callbackFunc) {
//    var async = false;
//    if (async_) {
//        async = async_;
//    }
//    var resultJson;
//    var resultJson;
//    $.ajax({
//        type: "POST",
//        url: "/ServiceHandler/BaseInfo/Zd_GstmSite.ashx?" + Math.random() + "&customSearchFilters=" + customSearchFilters,
//        data: { action: "GetList" },
//        async: async,
//        dataType: "json",
//        success: function (json) {
//            if (isServerResultDataPass(json)) {
//                resultJson = json.ResultDataFull;
//            }
//        }
//    });

//    return resultJson;
//}

var getData_Route = function (customSearchFilters, async_, callbackFunc) {
    var async = false;
    if (async_) {
        async = async_;
    }
    var resultJson;
    var resultJson;
    $.ajax({
        type: "POST",
        url: "/ServiceHandler/BaseInfo/Zd_Route.ashx?" + Math.random() + "&customSearchFilters=" + customSearchFilters,
        data: { action: "GetListGroup" },
        async: async,
        dataType: "json",
        success: function (json) {
            if (isServerResultDataPass(json)) {
                resultJson = json.ResultDataFull;
                if (callbackFunc) {
                    callbackFunc(resultJson);
                }
            }
        }
    });

    return resultJson;
}
var getData_SuoShuPDC = function (customSearchFilters, async_, callbackFunc) {
    var async = false;
    if (async_) {
        async = async_;
    }
    var resultJson;
    $.ajax({
        type: "POST",
        url: "/ServiceHandler/BaseInfo/Zd_Location.ashx?" + Math.random() + "&customSearchFilters=" + customSearchFilters,
        data: { action: "GetListSuoShuPDC" },
        async: async,
        dataType: "json",
        success: function (json) {
            if (isServerResultDataPass(json)) {
                resultJson = json.ResultDataFull;
                if (callbackFunc) {
                    callbackFunc(resultJson);
                }
            }
        }
    });
    return resultJson;
}

var getData_TranSportRoute = function (customSearchFilters, async_, callbackFunc) {
    var async = false;
    if (async_) {
        async = async_;
    }
    var resultJson;
    $.ajax({
        type: "POST",
        url: "/ServiceHandler/BaseInfo/Zd_TranSportRoute.ashx?" + Math.random() + "&customSearchFilters=" + customSearchFilters,
        data: { action: "GetListGroup" },
        async: async,
        dataType: "json",
        success: function (json) {
            if (isServerResultDataPass(json)) {
                resultJson = json.ResultDataFull;
                if (callbackFunc) {
                    callbackFunc(resultJson);
                }
            }
        }
    });

    return resultJson;
}

var getData_Site = function (customSearchFilters, async_, callbackFunc) {
    var async = false;
    if (async_) {
        async = async_;
    }
    var resultJson;
    $.ajax({
        type: "POST",
        url: "/ServiceHandler/BaseInfo/Zd_GstmSite.ashx?" + Math.random() + "&customSearchFilters=" + customSearchFilters,
        data: { action: "GetList" },
        async: async,
        dataType: "json",
        success: function (json) {
            if (isServerResultDataPass(json)) {
                resultJson = json.ResultDataFull;
                if (callbackFunc) {
                    callbackFunc(resultJson);
                }
            }
        }
    });

    return resultJson;
}

//var getDictionaryData = function (typeCode) {
//    var resultJson;
//    var parmsArray = [
//          { name: 'TYPECODE', value: typeCode }
//        ];

//    var customSearchFilters = formatSearchParames(parmsArray);
//    $.ajax({
//        type: "POST",
//        url: "/ServiceHandler/SysInfo/SysDictionary.ashx?" + Math.random(),
//        data: { action: "GetDictionaryDataList", customSearchFilters: customSearchFilters },
//        async: false,
//        dataType: "json",
//        success: function (json) {
//            if (isServerResultDataPass(json)) {
//                // 返回的格式 [{ 'Value': '0', 'Text': '正常' }
//                var initDataArray = [];
//                for (var i = 0; i < json.ResultDataFull.length; i++) {
//                    var item = {};
//                    item.Text = json.ResultDataFull[i].DicText;
//                    item.Value = json.ResultDataFull[i].DicValue;
//                    initDataArray.push(item);
//                }
//                resultJson = initDataArray;
//            }
//        }
//    });

//    return resultJson;
//}

///**用于读取状态值,此处需要同步执行**/
///** stateKind:状态类型，即表名 **/
//var getStateData = function (stateKind) {
//    var resultJson;
//    $.ajax({
//        type: "POST",
//        url: "../BasicInformation/ItCombbox.ashx?" + Math.random(),
//        data: { action: "GetItCombboxListForControl", kind: stateKind },
//        async: false,
//        dataType: "json",
//        success: function (json) {
//            resultJson = json;
//        }
//    });

//    return resultJson;
//}

///** 获取客户列表用于绑定到控件（一般为下拉框） **/
///** ajax请求为同步 **/
//var getCusData = function () {
//    var resultJson;
//    $.ajax({
//        type: "POST",
//        url: "../BasicInformation/ZdCus.ashx?" + Math.random(),
//        data: { action: "GetListForControl" },
//        async: false,
//        dataType: "json",
//        success: function (json) {
//            resultJson = json;
//        }
//    });

//    return resultJson;
//}

///** 绑定客户列表到Autocomplete控件 **/
///** ajax请求为异步 **/
//var bindCusDataForAutocomplete = function (params) {
//    $.ajax({
//        type: "POST",
//        url: "../BasicInformation/ZdCus.ashx?" + Math.random(),
//        data: { action: "GetListForControl" },
//        async: true,
//        dataType: "json",
//        success: function (json) {
//            for (var i = 0; i < params.length; i++) {
//                params[i].initJsonData = json;
//                bindAutocompleteControlSelect(params[i]);
//            }
//        }
//    });
//};

///** 获取承运商列表用于绑定到控件（一般为下拉框） **/
///** ajax请求为同步 **/
//var getSubcontractorData = function () {
//    var resultJson;
//    $.ajax({
//        type: "POST",
//        url: "../BasicInformation/ZdContractor.ashx?" + Math.random(),
//        data: { action: "GetListForControl" },
//        async: false,
//        dataType: "json",
//        success: function (json) {
//            resultJson = json;
//        }
//    });

//    return resultJson;
//}

///** 绑定客户列表到Autocomplete控件 **/
///** ajax请求为异步 **/
//var bindSubcontractorDataForAutocomplete = function (params) {
//    $.ajax({
//        type: "POST",
//        url: "../BasicInformation/ZdContractor.ashx?" + Math.random(),
//        data: { action: "GetListForControl" },
//        async: true,
//        dataType: "json",
//        success: function (json) {
//            for (var i = 0; i < params.length; i++) {
//                params[i].initJsonData = json;
//                bindAutocompleteControlSelect(params[i]);
//            }
//        }
//    });
//};

///** 获取起运地目的地列表用于绑定到控件（一般为下拉框） **/
///** ajax请求为同步 **/
//var getLocationData = function () {
//    var resultJson;
//    $.ajax({
//        type: "POST",
//        url: "../BasicInformation/ZdLocation.ashx?" + Math.random(),
//        data: { action: "GetListForControl" },
//        async: false,
//        dataType: "json",
//        success: function (json) {
//            resultJson = json;
//        }
//    });

//    return resultJson;
//}

///** 绑定客户列表到Autocomplete控件 **/
///** ajax请求为异步 **/
//var bindLocationDataForAutocomplete = function (params) {
//    $.ajax({
//        type: "POST",
//        url: "../BasicInformation/ZdLocation.ashx?" + Math.random(),
//        data: { action: "GetListForControl" },
//        async: true,
//        dataType: "json",
//        success: function (json) {
//            for (var i = 0; i < params.length; i++) {
//                params[i].initJsonData = json;
//                bindAutocompleteControlSelect(params[i]);
//            }
//        }
//    });
//};

///** 获取车型列表用于绑定到控件（一般为下拉框） **/
///** ajax请求为同步 **/
//var getTruckKindData = function () {
//    var resultJson;
//    $.ajax({
//        type: "POST",
//        url: "../BasicInformation/ZdTruckType.ashx?" + Math.random(),
//        data: { action: "GetListForControl" },
//        async: false,
//        dataType: "json",
//        success: function (json) {
//            resultJson = json;
//        }
//    });

//    return resultJson;
//}

///** 绑定客户列表到Autocomplete控件 **/
///** ajax请求为异步 **/
//var bindTruckKindDataForAutocomplete = function (params) {
//    $.ajax({
//        type: "POST",
//        url: "../BasicInformation/ZdTruckType.ashx?" + Math.random(),
//        data: { action: "GetListForControl" },
//        async: true,
//        dataType: "json",
//        success: function (json) {
//            for (var i = 0; i < params.length; i++) {
//                params[i].initJsonData = json;
//                bindAutocompleteControlSelect(params[i]);
//            }
//        }
//    });
//};

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

var loadLifnr = function(options) {
	var requestParam = {
		  valueField: 'code',  
		  textField: 'name',
		  panelWidth: 300,
		  loader:function(param,success,error){  
          $.ajax({  
              url:"../../scts/supplier/findAllSupplier?t=" + Math.random(),
              method:"GET",
              //async : false,
              success: function(data){
              	var supArr = [];
              	supArr.unshift({code:'',name:lspInnerHTML[""]});
              	var i = 0;
              	data.forEach(function(item){
              		supArr.push({code:item.lifnr,name:(item.lifnr +"-"+ item.name1)});
                 });
              	success(supArr);
              	$("#supplier").combobox("setValue","");
              	$("#supplier").textbox("textbox").css("padding-top", "0px");
              } 
          }); 
      }
	};
	if(options.onSelect){
		requestParam.onSelect = options.onSelect;
	}
	
	$("#supplier").combobox(requestParam);
};

var loadLsp = function(options) {
	var requestParam = {
		  valueField: 'code',  
		  textField: 'name',
		  panelWidth: 300,
		  loader:function(param,success,error){  
          $.ajax({  
              url:"../../jcda/contractor/findAllListWithLocation?&t=" + Math.random(),
              method:"GET",
              //async : false,
              success: function(data){
              	var supArr = [];
              	supArr.unshift({code:'',name:lspInnerHTML[""]});
              	var i = 0;
              	data.forEach(function(item){
              		supArr.push({code:item.code,name:(item.code +"-"+ item.name)});
                 });
              	success(supArr);
              	$("#lsp").combobox("setValue","");
              	$("#lsp").textbox("textbox").css("padding-top", "0px");
              } 
          }); 
      }
	};
	if(options.onSelect){
		requestParam.onSelect = options.onSelect;
	}
	$("#lsp").combobox(requestParam);
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
