var enterTriggerEvent = function (rangeBoxName, eventButtonName) {//进入的触发事件 trigger 触发
    $("#" + rangeBoxName + " input[type='text']").each(function () {
        $(this).bind('keyup', function (event) {
            if (event.keyCode == "13") {
                eval(eventButtonName + "()");
            }
        });
    });
}

/*复选框联动*/
var setCheckBoxLinkage = function (ckbAllId, ckbTargetName, callback) {
    $("#" + ckbAllId).click(function () {
        var checkAllObj = $(this);
        $("[name='" + ckbTargetName + "']").each(function () {
            $(this).attr("checked", checkAllObj.is(':checked'));
        });
        if (typeof callback === "function") {
            callback();
        }
    });

    $("[name='" + ckbTargetName + "']").click(function () {
        var allNum = $("[name='" + ckbTargetName + "']").length;
        var checkedNum = $("[name='" + ckbTargetName + "']:checked").length;
        if (checkedNum == allNum) {
            $("#" + ckbAllId).attr("checked", true);
        } else {
            $("#" + ckbAllId).attr("checked", false);
        }
        if (typeof callback === "function") {
            callback();
        }
    });
}

/*获取url参数:使用方式 var parame = args["id"]*/
var getUrlParms = function () {
    var args = new Object();
    var query = location.search.substring(1); //获取查询串   
    var pairs = query.split("&"); //在逗号处断开   
    for (var i = 0; i < pairs.length; i++) {
        var pos = pairs[i].indexOf('='); //查找=在数组pairs[i]中的索引位置  
        if (pos == -1) continue; //如果为-1就是没有找到就跳过   
        var argname = pairs[i].substring(0, pos); //提取name   
        var value = pairs[i].substring(pos + 1); //提取value   
        args[argname] = decodeURIComponent(value); //存为属性(解码)   
    }
    return args;
}

/*判断undefined、null或者null字符串转换成空字符*/
var formatStr = function (data) {
    // 判断undefined、null
    if (data == null || data == undefined || data == "null") {
        return "";
    }

    return data;
}

/*loading主题 :preLevel路径层级*/
//var loadTheme = function (preLevelPath) {
//    var themeCustom = "customBlack";
//    var themeEasyUi = "easyuiBlack";
//    var themeJqgrid = "themeGray";
//    if (preLevelPath == undefined) {
//        preLevelPath = "../";
//    }

//    // 根据cookie读取主题
//    var theme = $.cookie('abisTheme');
//    if (theme != null && theme != "") {
//        themeCustom = "custom" + theme;
//        themeEasyUi = "easyui" + theme;
//        themeJqgrid = "theme" + theme;
//    }

//    themeCustom = themeCustom + ".css";
//    themeEasyUi = themeEasyUi + ".css";
//    $("#stylelink_custom").attr("href", preLevelPath + "Resource/css/index/" + themeCustom);
//    $("#stylelink_easyui").attr("href", preLevelPath + "Resource/js/easyUI/themes/" + themeEasyUi);
//    $("#stylelink_jqgrid").attr("href", preLevelPath + "Resource/js/jqueryUI/" + themeJqgrid + "/jquery-ui-1.10.4.custom.css");
//}

/*判断服务器返回的ServerResultData是否有错误、为空、null、列表是否大于0等*/
var isServerResultDataPass = function (serverResultData) {
    if (serverResultData != null && serverResultData.ResultCode == 0 && serverResultData.ResultDataFull != null) {
        return true;
    }

    return false;
}

/*根据服务器返回的ServerResultData中的错误级别操作弹出不同的提示*/
/* 1：应用级别提示；2：应用级别警告；3:应用级别错误,4:系统级别错误,5：session过期 */
var FailResultDataToTip = function (serverResultData) {
    if (serverResultData.ResultCode == 1 || serverResultData.ResultCode == 2 || serverResultData.ResultCode == 3 || serverResultData.ResultCode == 4) {
        window.top.showNotification({
            SimpleMessage: serverResultData.ResultDataFull.SimpleMessage,
            MoreMessage: serverResultData.ResultDataFull.MoreMessage,
            ShowMoreMessage: serverResultData.ResultDataFull.ShowMoreMessage,
            AutoHide: serverResultData.ResultDataFull.AutoHide,
            Type: serverResultData.ResultCode
        });
    }

    if (serverResultData.ResultCode == 5) {
        window.top.showNotification({
            SimpleMessage: "由于系统更新或者您长时间未操作,为了保证系统安全您需要重新登录</br>系统将会在5秒内切换到登录页面",
            MoreMessage: "",
            Type: 3
        });
        setInterval(function () { window.top.location = "/Index/Login.aspx"; }, 5000);
    }
}


/**将自定义查询参数格式化成后台需要的格式并转换成JSON字符串**/
//var formatSearchParames = function (paramesList) {
//    var paramesJson = {};
//    paramesJson.groupOp = "AND";
//    paramesJson.rules = [];
//    for (var i = 0; i < paramesList.length; i++) {
//        var name = paramesList[i].name;
//        var value = $.trim(paramesList[i].value);
//        var op = paramesList[i].op;

//        if (name == null || value == "") {
//            continue;
//        }

//        var parameObj = {};
//        parameObj.field = name;

//        if (op == null || op == "") {
//            parameObj.op = "eq";
//        } else {
//            parameObj.op = op;
//        }

//        parameObj.data = value;
//        paramesJson.rules.push(parameObj);
//    }

//    return JSON.stringify(paramesJson);
//};

/*将参数格式化服务端统一的json字符串*/
var formatSearchParames = function (paramesList, groupOp) {
    var paramesJson = {};
    if (!groupOp) {
        paramesJson.groupOp = "AND";
    } else {
        paramesJson.groupOp = groupOp;
    }

    paramesJson.rules = [];
    for (var i = 0; i < paramesList.length; i++) {
        var name = paramesList[i].name;
        var value = $.trim(paramesList[i].value);
        var op = paramesList[i].op;

        if (name == null || value == "") {
            continue;
        }

        var parameObj = {};
        parameObj.field = name;

        if (op == null || op == "") {
            parameObj.op = "eq";
        } else {
            parameObj.op = op;
        }

        parameObj.data = value;
        paramesJson.rules.push(parameObj);
    }

    return JSON.stringify(paramesJson);
};

/*将参数格式化服务端统一的json格式对象*/
var formatSearchParamesJson = function (paramesList, groupOp) {
    var paramesJson = {};
    if (!groupOp) {
        paramesJson.groupOp = "AND"; //给paramesJson添加属性并赋值
    } else {
        paramesJson.groupOp = groupOp;
    }

    paramesJson.rules = [];
    for (var i = 0; i < paramesList.length; i++) {
        var name = paramesList[i].name;
        var value = $.trim(paramesList[i].value);
        var op = paramesList[i].op;

        if (name == null || value == "") {
            continue;
        }

        var parameObj = {};
        parameObj.field = name;

        if (op == null || op == "") {
            parameObj.op = "eq";
        } else {
            parameObj.op = op;
        }

        parameObj.data = value;
        paramesJson.rules.push(parameObj);
    }

    return paramesJson;
};

/** 查询清单页面切换显示和隐藏更多的查询条件 **/
function toggleParame(obj) {
    if ($(obj).attr("pointType") == "pointDown") {
        $(obj).attr("class", "toggleLinkTop");
        $(obj).attr("pointType", "pointUp");
        $("#searchParamesTable tr:gt(0)").show();
    } else {
        $(obj).attr("class", "toggleLinkDown");
        $(obj).attr("pointType", "pointDown");
        $("#searchParamesTable tr:gt(0)").hide();
    };
    setGridHeightWidth();
}

function toggleParamePlan(obj) {
    if ($(obj).attr("pointType") == "pointDown") {
        $(obj).attr("class", "toggleLinkTop");
        $(obj).attr("pointType", "pointUp");
        $("#searchParamesTable tr:gt(0)").show();
        setGridHeightWidthCount(230, 200);
    } else {
        $(obj).attr("class", "toggleLinkDown");
        $(obj).attr("pointType", "pointDown");
        $("#searchParamesTable tr:gt(0)").hide();
        setGridHeightWidthCount(230, 130);
    };
}

function toggleParameDespatch(obj) {
    if ($(obj).attr("pointType") == "pointDown") {
        $(obj).attr("class", "toggleLinkTop");
        $(obj).attr("pointType", "pointUp");
        $("#searchParamesTable tr:gt(0)").show();
        setGridHeightWidthCount(0, 160);
    } else {
        $(obj).attr("class", "toggleLinkDown");
        $(obj).attr("pointType", "pointDown");
        $("#searchParamesTable tr:gt(0)").hide();
        setGridHeightWidthCount(0, 130);
    };
}

/**动态设置jqGrid的高度和宽度，因为该控件无自适应机制，需要在窗口改变大小事件中触发**/
/**subtractWidth：需要页面可见宽度减去的宽度值**/
/**subtractHeight：需要页面可见高度减去的高度值**/
/**如果不指定减去高度和宽度值，则系统自动计算**/
var setGridHeightWidth = function (subtractWidth, subtractHeight, gridTableId) {
    var gridTableObj = $("#gridTable");
    if (gridTableId) {
        gridTableObj = $("#" + gridTableId);
    }
    if (subtractWidth == "" || subtractWidth == null) {
        subtractWidth = 0;
    }

    if (subtractHeight == "" || subtractHeight == null) {
        subtractHeight = 0;
        $("#gridControl").prevAll().each(function () {
            subtractHeight += $(this).height();
        });

        subtractHeight += 63;
    }

    gridTableObj.setGridWidth($(window).width() - subtractWidth);
    var moreHight = 0;
    if ($(".ui-search-toolbar").css("display") == "table-row") {
        moreHight = 25;
    }

    gridTableObj.setGridHeight($(window).height() - subtractHeight - moreHight);
}
/**动态设置jqGrid的高度和宽度，因为该控件无自适应机制，需要在窗口改变大小事件中触发**/
/**subtractWidth：需要页面可见宽度减去的宽度值**/
/**subtractHeight：需要页面可见高度减去的高度值**/
/**如果不指定减去高度和宽度值，则系统自动计算**/
/**适应汇总统计列表**/
var setGridHeightWidthCount = function (subtractWidth, subtractHeight, gridTableId) {
    var gridTableObj = $("#gridTable");
    if (gridTableId) {
        gridTableObj = $("#" + gridTableId);
    }
    if (subtractWidth == "" || subtractWidth == null) {
        subtractWidth = 0;
    }

    if (subtractHeight == "" || subtractHeight == null) {
        subtractHeight = 0;
        $("#gridControl").prevAll().each(function () {
            subtractHeight += $(this).height();
        });

        subtractHeight += 110;
    }

    gridTableObj.setGridWidth($(window).width() - subtractWidth);
    var moreHight = 0;
    if ($(".ui-search-toolbar").css("display") == "table-row") {
        moreHight = 25;
    }

    gridTableObj.setGridHeight($(window).height() - subtractHeight - moreHight - 25);
}

var setGridHeightWidthCountShou = function (subtractWidth, subtractHeight, gridTableId) {
    var gridTableObj = $("#gridTable");
    if (gridTableId) {
        gridTableObj = $("#" + gridTableId);
    }
    if (subtractWidth == "" || subtractWidth == null) {
        subtractWidth = 0;
    }

    if (subtractHeight == "" || subtractHeight == null) {
        subtractHeight = 0;
        $("#gridControl").prevAll().each(function () {
            subtractHeight += $(this).height();
        });

        subtractHeight += 63;
    }

    gridTableObj.setGridWidth($(window).width() - subtractWidth - 8);
    var moreHight = 0;
    if ($(".ui-search-toolbar").css("display") == "table-row") {
        moreHight = 25;
    }

    gridTableObj.setGridHeight($(window).height() - subtractHeight - moreHight - 70);
}

var setToolbarHeightWidth = function (toolbarId) {
    if (toolbarId) {
        $("#" + toolbarId).css("width", "100%");
    } else {
        $("#toolbar").css("width", "100%");
    }
}


/**切换展示jqgrid的表头下的搜索条**/
/**如果不传递最新url，则无法和自定义查询条件取交集**/
var toggleGridSearchToolbar = function () {
    if ($(".ui-search-toolbar").css("display") != "table-row") {
        if ($(".ui-search-toolbar").length <= 0) {
            $("#gridTable").jqGrid('filterToolbar', { stringResult: true, searchOperators: true, beforeSearch: function () {
                $("#gridTable").jqGrid('setGridParam', {
                    url: getSearchGridUrl()
                });
            }
            });
        } else {
            $(".ui-search-toolbar").show();
        }
        setGridHeightWidth();
    } else {
        $(".ui-search-toolbar").hide();
        setGridHeightWidth();
    }
}

/**切换展示jqgrid的表头下的搜索条**/
/**如果不传递最新url，则无法和自定义查询条件取交集**/
/**合计使用**/
var toggleGridSearchToolbarCount = function () {
    if ($(".ui-search-toolbar").css("display") != "table-row") {
        if ($(".ui-search-toolbar").length <= 0) {
            $("#gridTable").jqGrid('filterToolbar', { stringResult: true, searchOperators: true, beforeSearch: function () {
                $("#gridTable").jqGrid('setGridParam', {
                    url: getSearchGridUrl()
                });
            }
            });
        } else {
            $(".ui-search-toolbar").show();
        }
        setGridHeightWidthCount();
    } else {
        $(".ui-search-toolbar").hide();
        setGridHeightWidthCount(230, 130, "gridTable");
    }
}

/**切换展示jqgrid的表头下的搜索条**/
var toggleGridColumns = function () {
    var gridColumnOptions = { updateAfterCheck: true }
    jQuery("#gridTable").setColumns(gridColumnOptions);
}


/**用json值生成jqgrid的searchToolbar下的html默认select下拉框所需要的值**/
var formatGridCombobox_Local = function (jsonData, showChooseAll) {
    var initData = "";
    initData += "{";
    if (showChooseAll) {
        initData += "\"\":\"--所有--\",";
    }

    for (var i = 0; i < jsonData.length; i++) {
        if (i == jsonData.length - 1) {
            initData += "\"" + jsonData[i].Value + "\":\"" + jsonData[i].Text + "\"";
        } else {
            initData += "\"" + jsonData[i].Value + "\":\"" + jsonData[i].Text + "\",";
        }
    }
    initData += "}";
    return JSON.parse(initData);
}

var formatGridCombobox_Server = function (jsonData, showChooseAll) {
    var initData = "";
    if (jsonData != undefined) {
        initData += "{";
        if (showChooseAll) {
            initData += "\"\":\"--所有--\",";
        }


        for (var i = 0; i < jsonData.length; i++) {
            if (i == jsonData.length - 1) {
                initData += "\"" + jsonData[i].CODE + "\":\"" + jsonData[i].NAME + "\"";
            } else {
                initData += "\"" + jsonData[i].CODE + "\":\"" + jsonData[i].NAME + "\",";
            }
        }
        initData += "}";
    }
    else {
        initData = "";
        return;
    }
    return JSON.parse(initData);
}

var formatGridCombobox_GstmServer = function (jsonData, showChooseAll) {
    var initData = "";
    if (jsonData != undefined) {
        initData += "{";
        if (showChooseAll) {
            initData += "\"\":\"--所有--\",";
        }


        for (var i = 0; i < jsonData.length; i++) {
            if (i == jsonData.length - 1) {
                initData += "\"" + jsonData[i].SITECODE + "\":\"" + jsonData[i].SITENAME + "\"";
            } else {
                initData += "\"" + jsonData[i].SITECODE + "\":\"" + jsonData[i].SITENAME + "\",";
            }
        }
        initData += "}";
    }
    else {
        initData = "";
        return;
    }
    return JSON.parse(initData);
}

/**用json值生成jqueryUi的autocomplete 下拉框所需要的值**/
var generateAutocompleteSelectData = function (jsonData) {
    var initDataArray = [{ "label": "", "value": ""}];
    if (isServerResultDataPass(jsonData)) {
        for (var i = 0; i < jsonData.ResultDataFull.length; i++) {
            var item = {};
            item.label = jsonData.ResultDataFull[i].Name;
            item.value = jsonData.ResultDataFull[i].Value;
            initDataArray.push(item);
        }
    }

    return initDataArray;
}

/** 通过状态值映射状态显示 **/
var getStateName = function (stateJsonData, stateValue) {
    var stateName = stateValue;
    if (isServerResultDataPass(stateJsonData)) {
        for (var i = 0; i < stateJsonData.ResultDataFull.length; i++) {
            if (stateValue == stateJsonData.ResultDataFull[i].Value) {
                stateName = stateJsonData.ResultDataFull[i].Name;
                break;
            }
        }
    }

    return stateName;
}

/** 通过标示值映射标示显示 **/
/** flagValue:标示类型（如：Y/N） **/
var getFlagName = function (flagValue) {
    if (flagValue == "Y") {
        return "是";
    } else if (flagValue == "N") {
        return "否";
    }

    return flagValue;
}

/**用指定值绑定jqueryUI的autocomplete下拉框(通用的下拉框样式-特殊的请自定义)**/
/** bindControl:需要被绑定的控件对象 ；initJsonData 绑定数据源;showSplitChar:显示列表是否有分隔符；selectedSplitChar：选择后的值是否有分隔符；width：宽度,addFunc  :添加按钮需要执行的方法；editFunc:编辑按钮需要执行的方法；deleteFunc:删除按钮需要执行的方法**/
var bindAutocompleteControlSelect = function (params) {
    if (isServerResultDataPass(params.initJsonData)) {
        var initData = generateAutocompleteSelectData(params.initJsonData);
        params.bindControl.autocomplete({
            autoFocus: false,
            minLength: 0,
            source:
                        function (request, response) {
                            var matcher = new RegExp(".*" + $.ui.autocomplete.escapeRegex(request.term) + ".*", "i");
                            var length = 10;
                            response($.grep(initData, function (item) {
                                if (length > 0) {
                                    var matchResult = matcher.test(item.label) || matcher.test(item.value);
                                    if (matchResult) {
                                        length = length - 1;
                                    }

                                    return matchResult;
                                }
                            }));
                        },
            focus: function (event, ui) {
                event.preventDefault();
                if (params.selectedSplitChar) {
                    params.bindControl.val(ui.item.value + "-" + ui.item.label);
                } else {
                    params.bindControl.val(ui.item.value);
                }
            },
            select: function (event, ui) {
                if ($.trim(ui.item.value) == "" && $.trim(ui.item.label) == "") {
                    params.bindControl.val("");
                } else {
                    if (params.selectedSplitChar) {
                        params.bindControl.val(ui.item.value + "-" + ui.item.label);
                    } else {
                        params.bindControl.val(ui.item.value);
                    }
                }
                return false;
            }
        })
                .focus(function () {
                    if ($(this).attr("readOnly") == true) {

                    } else {
                        $(this).autocomplete("search");
                    }
                })
                .data("ui-autocomplete")._renderItem = function (ul, item) {
                    //                    if (ul.children().length == 0) {
                    //                        $("<li></li>").append("<img style=\"position:absolute;right:10px; \" src=\"../images/icons/add.gif\" onclick=\"test(event)\"><span style=\"display:block;\">&nbsp;</span>").appendTo(ul);
                    //                    }

                    var aa = $("<li></li>").data("item.autocomplete", item);
                    if ($.trim(item.value) == "" && $.trim(item.label) == "") {
                        if (params.addFunc != "" && params.addFunc != undefined) {
                            aa.append("<a>" + "&nbsp;" + "<img style=\"position:absolute;right:10px;\" src=\"../images/icons/add.gif\" onclick=\"" + params.addFunc + "(event,'" + params.addFuncCallBackFunc + "')\"></a>").appendTo(ul);
                        }
                    } else {
                        var editStr = "";
                        var deleteStr = "";
                        if (params.editFunc != "" && params.editFunc != undefined) {
                            editStr = "<img style=\"position:absolute;right:10px;\" src=\"../images/icons/edit.gif\" onclick=\"" + params.editFunc + "(event,'" + params.editFuncCallBackFunc + "','" + item.value + "')\">";
                        }

                        //                        if (params.deleteFunc != "" && params.deleteFunc != undefined) {
                        //                            deleteStr = "<img style=\"position:absolute;right:10px;\" src=\"../images/icons/delete.gif\" onclick=\"" + params.deleteFunc + "(event)\">";
                        //                        }
                        if (params.showSplitChar) {
                            aa.append("<a>" + item.value + "-" + item.label + editStr + deleteStr + "</a>").appendTo(ul);
                        } else {
                            aa.append("<a>" + item.label + editStr + deleteStr + "</a>").appendTo(ul);
                        }
                    }

                    return aa;
                };

        params.bindControl.data("ui-autocomplete")._resizeMenu = function () {
            this.menu.element.outerWidth(params.width);
        }
    }
}

/** 自定义提示弹出 **/
/** 0:成功 1：应用级别提示 2：应用级别警告 3：应用级别错误4：系统级别错误5：session过期6:loading **/
var alertNotification = function (message) {
    message.Type = 1;
    window.top.showNotification(message);
}

/** 自定义警告弹出 **/
/** 0:成功 1：应用级别提示 2：应用级别警告 3：应用级别错误4：系统级别错误5：session过期6:loading **/
var warningNotification = function (message) {
    message.Type = 2;
    window.top.showNotification(message);
}

/** 自定义错误弹出 **/
/** 0:成功 1：应用级别提示 2：应用级别警告 3：应用级别错误4：系统级别错误5：session过期6:loading **/
var errorNotification = function (message) {
    message.Type = 3;
    window.top.showNotification(message);
}

/** 自定义正确弹出 **/
/** 0:成功 1：应用级别提示 2：应用级别警告 3：应用级别错误4：系统级别错误5：session过期6:loading **/
var correctNotification = function (message) {
    message.Type = 0;
    window.top.showNotification(message);
}

/** 自定义loadng **/
/** 0:成功 1：应用级别提示 2：应用级别警告 3：应用级别错误4：系统级别错误5：session过期6:loading **/
var loadingNotification = function (message) {
    if (message.SimpleMessage == "" || message.SimpleMessage == undefined) {
        message.SimpleMessage = "正在加载中...";
    }

    message.Type = 6;
    window.top.showNotification(message);
}

/** 关闭提示 **/
/** 0:成功 1：应用级别提示 2：应用级别警告 3：应用级别错误4：系统级别错误5：session过期6:loading **/
var closeNotification = function () {
    window.top.hideNotificationTips();
}

/** (top frame)全屏遮罩层加载牛货运loading **/
var showLoading = function (message) {
    window.top.startLoading(message);
}

/** (top frame)全屏遮罩层隐藏牛货运loading **/
var hideLoading = function () {
    window.top.hideLoading();
}

/** 通用的在顶层打开窗体 **/
var openDialog = function (dialogParamsTemp) {
    window.top.open(dialogParamsTemp);
}

/** 根据dialogId关闭弹出框对象 **/
var closeDialog = function (dialogId) {
    if (dialogId == undefined) {
        return;
    }

    window.top.close(dialogId);
}

/** 关闭所有弹出框对象-如果只有一个弹出框的话也是可以调用的和closeSlefDialog效果一样 **/
var closeAllDialog = function () {
    window.top.$.ligerDialog.close();
}

/** 打开新的tab中的框架窗体 **/
var createNewTab = function (title, url) {
    window.top.frames["maniFrame"].contentWindow.frames["rightFrame"].TabCreate(title, url);
}

/** 关闭当前打开的tab中的框架窗体 **/
var closeCurrentTab = function () {
    var tabA = $(window.top.frames["maniFrame"].contentWindow.frames["rightFrame"].document).find(".selected");
    var divTab = tabA.prev();
    var tabId = divTab.attr("lang");
    window.top.frames["maniFrame"].contentWindow.frames["rightFrame"].TabClose(divTab[0], tabId);
}

/** 根据dialogId获取弹出框父对象 **/
/** 1、父对象本身是弹出框 **/
/** 2、父对象本身不是弹出框,则获取打开的tab窗体 **/
var getDialog = function (dialogId) {
    var topWindowDoc = $("#" + dialogId, window.top.document);
    if (topWindowDoc.length > 0) {
        var iframeName = $(topWindowDoc).find("iframe").attr("name");
        return window.top.frames[iframeName];
    }
}

/** 获取当前打开的tab中的框架窗体 **/
/** 有一种情况是tab中的frame中再嵌入frame(目前只支持tab中的frame再嵌入一层frame（暂时不支持嵌入多层）) **/
var getCurrentTab = function (iframeId) {
    var tabContent;
    if (iframeId) {
        //        tabContentArray = $(window.top.frames["maniFrame"].contentWindow.frames["rightFrame"].document).find("div iframe[id='" + iframeId + "']");
        tabContentArray = $(window.top.frames["maniFrame"].contentWindow.frames["rightFrame"].document).find("div iframe");
        tabContentArray.each(function () {
            //首先判断自身是否符合id
            if ($(this).attr("id") == iframeId) {
                tabContent = $(this);
                return false;
            }

            var childFrameArray = $($(this)[0].contentWindow.document).find("iframe")
            childFrameArray.each(function () {
                if ($(this).attr("id") == iframeId) {
                    tabContent = $(this);
                    return false;
                }
            });
        });
    } else {
        var tabDivContent = $(window.top.frames["maniFrame"].contentWindow.frames["rightFrame"].document).find("div [class='tabson']");
        tabDivContent.each(function () {
            if ($(this).css("display") == "block") {
                tabContent = $(this).find("iframe");
                return false;
            }
        });
    }

    return tabContent[0].contentWindow;
}

var formatDefaultCheckbox_Local = function (formatParam) {
    // var formatData = { data: null, needChooseAll: true, chooseAllValue: "", defaultValue:"",bindBoxName:"",bindControlPrefix:""};
    if (!formatParam.data) {
        return "";
    }

    if (!formatParam.bindBoxName) {
        return "";
    }

    if (!formatParam.bindControlPrefix) {
        return "";
    }

    if (formatParam.needChooseAll == undefined) {
        formatParam.needChooseAll = true;
    }

    if (formatParam.chooseAllValue == undefined) {
        formatParam.chooseAllValue = "";
    }

    if (formatParam.defaultValue == undefined) {
        formatParam.defaultValue = "undefined";
    }

    var listHtml = "";
    if (formatParam.needChooseAll) {
        listHtml += "&nbsp;&nbsp;<input type=\"checkbox\" name=\"" + formatParam.bindControlPrefix + "_all\" id=\"" + formatParam.bindControlPrefix + "_all\" value=\"" + formatParam.chooseAllValue + "\" /><label for=\"" + formatParam.bindControlPrefix + "_all\">全部</label>&nbsp;&nbsp;";
    }

    for (var i = 0; i < formatParam.data.length; i++) {
        listHtml += "<input type=\"checkbox\" data-default=\"" + formatParam.data[i].Default + "\" data-name=\"" + formatParam.bindControlPrefix + "\" name=\"" + formatParam.bindControlPrefix + "_" + formatParam.data[i].Value + "\" value='" + formatParam.data[i].Value + "' id=\"" + formatParam.bindControlPrefix + "_" + formatParam.data[i].Value + "\"/>&nbsp;<label for=\"" + formatParam.bindControlPrefix + "_" + formatParam.data[i].Value + "\">" + formatParam.data[i].Text + "</label>&nbsp;&nbsp;";
    }

    $("#" + formatParam.bindBoxName).append(listHtml);

    $("#" + formatParam.bindControlPrefix + "_all").click(function () {
        var checkAllObj = $(this);
        //        $("#" + formatParam.bindBoxName).find("input[type='checkbox']:not(#" + formatParam.bindControlPrefix + "_all)").each(function () {
        //            $(this).attr("checked", checkAllObj.is(':checked'));
        //        });
        $("#" + formatParam.bindBoxName).find("input[type='checkbox']:not(#" + formatParam.bindControlPrefix + "_all)").each(function () {
            if (checkAllObj.is(':checked')) {
                $(this).attr("checked", false);
            }
        });
    });

    $("[data-name='" + formatParam.bindControlPrefix + "']").click(function () {
        var allNum = $("[data-name='" + formatParam.bindControlPrefix + "']").length;
        var checkedNum = $("[data-name='" + formatParam.bindControlPrefix + "']:checked").length;
        //        if (checkedNum == allNum) {
        //            $("#" + formatParam.bindControlPrefix + "_all").attr("checked", true);
        //        } else {
        //            $("#" + formatParam.bindControlPrefix + "_all").attr("checked", false);
        //        }
        if (checkedNum > 0) {
            $("#" + formatParam.bindControlPrefix + "_all").attr("checked", false);
        }
    });

    // 加载默认值
    if (formatParam.defaultValue == "undefined") {
        $("#" + formatParam.bindBoxName).find("input[type='checkbox']").each(function () {
            if ($(this).attr("data-default") == "1") {
                $(this).trigger("click");
            }
        });
    } else {
        $("#" + formatParam.bindBoxName).find("input[type='checkbox']").each(function () {
            var defaultValuesArray = formatParam.defaultValue.split(',');
            for (var j = 0; j < defaultValuesArray.length; j++) {
                if ($(this).attr("value") == new String(defaultValuesArray[j])) {
                    $(this).trigger("click");
                }
            }
        });
    }
}

var formatDefaultRadio_Local = function (formatParam) {
    // var formatData = { data: null, defaultValue:"",bindBoxName:"",bindControlPrefix:""};
    if (!formatParam.data) {
        return "";
    }

    if (!formatParam.bindBoxName) {
        return "";
    }

    if (!formatParam.bindControlPrefix) {
        return "";
    }

    if (formatParam.defaultValue == undefined) {
        formatParam.defaultValue = "undefined";
    }

    var listHtml = "";
    if (formatParam.needChooseAll) {
        listHtml += "&nbsp;&nbsp;<input type=\"radio\" name=\"" + formatParam.bindControlPrefix + "\" id=\"" + formatParam.bindControlPrefix + "_all\" value=\"" + formatParam.chooseAllValue + "\" /><label for=\"" + formatParam.bindControlPrefix + "_all\">全部</label>&nbsp;&nbsp;";
    }

    for (var i = 0; i < formatParam.data.length; i++) {
        listHtml += "<input type=\"radio\" data-default=\"" + formatParam.data[i].Default + "\" name=\"" + formatParam.bindControlPrefix + "\" value='" + formatParam.data[i].Value + "' id=\"" + formatParam.bindControlPrefix + "_" + formatParam.data[i].Value + "\"/>&nbsp;<label for=\"" + formatParam.bindControlPrefix + "_" + formatParam.data[i].Value + "\">" + formatParam.data[i].Text + "</label>&nbsp;&nbsp;";
    }

    $("#" + formatParam.bindBoxName).empty();

    $("#" + formatParam.bindBoxName).append(listHtml);
    if (formatParam.defaultValue == "undefined") {
        $("#" + formatParam.bindBoxName + " [name='" + formatParam.bindControlPrefix + "']").each(function () {
            if ($(this).attr("data-default") == "1") {
                $(this).attr("checked", "checked");
            }
        });
    } else {
        $("#" + formatParam.bindBoxName + " [name='" + formatParam.bindControlPrefix + "']").each(function () {
            if ($(this).attr("value") == new String(formatParam.defaultValue)) {
                $(this).attr("checked", "checked");
            }
        });
    }
}

var formatEasyuiCombobox_Local = function (formatParam) {
    if (formatParam.needChooseAll == undefined) {
        formatParam.needChooseAll = true;
    }

    if (formatParam.chooseAllValue == undefined) {
        formatParam.chooseAllValue = "";
    }

    var initDataArray = [];
    if (formatParam.needChooseAll) {
        initDataArray = [{ "Value": formatParam.chooseAllValue, "Text": "--所有--"}];
    }

    if (formatParam.data) {
        for (var i = 0; i < formatParam.data.length; i++) {
            initDataArray.push(formatParam.data[i]);
        }
    }

    return initDataArray;
}

var formatEasyuiCombobox_Driver = function (formatParam) {
    if (formatParam.needChooseAll == undefined) {
        formatParam.needChooseAll = true;
    }

    if (formatParam.chooseAllValue == undefined) {
        formatParam.chooseAllValue = "";
    }

    var initDataArray = [];
    if (formatParam.needChooseAll) {
        initDataArray = [{ "Value": formatParam.chooseAllValue, "Text": "--所有--"}];
    }
    if (formatParam.data) {
        for (var i = 0; i < formatParam.data.length; i++) {
            var item = {};
            item.Text = formatParam.data[i].NAME;
            item.Value = formatParam.data[i].CODE;
            initDataArray.push(item);
        }
    }

    return initDataArray;
}

var formatEasyuiCombobox_TruckDriver = function (formatParam) {
    if (formatParam.needChooseAll == undefined) {
        formatParam.needChooseAll = true;
    }

    if (formatParam.chooseAllValue == undefined) {
        formatParam.chooseAllValue = "";
    }

    var initDataArray = [];
    if (formatParam.needChooseAll) {
        initDataArray = [{ "Value": formatParam.chooseAllValue, "Text": "--所有--"}];
    }
    if (formatParam.data) {
        for (var i = 0; i < formatParam.data.length; i++) {
            var item = {};
            item.Text = formatParam.data[i].SHORTNAME;
            item.Value = formatParam.data[i].CODE;
            initDataArray.push(item);
        }
    }

    return initDataArray;
}

var formatEasyuiCombobox_Site = function (formatParam) {
    if (formatParam.needChooseAll == undefined) {
        formatParam.needChooseAll = true;
    }

    if (formatParam.chooseAllValue == undefined) {
        formatParam.chooseAllValue = "";
    }

    var initDataArray = [];
    if (formatParam.needChooseAll) {
        initDataArray = [{ "Value": formatParam.chooseAllValue, "Text": "--所有--"}];
    }
    if (formatParam.data) {
        for (var i = 0; i < formatParam.data.length; i++) {
            var item = {};
            item.Text = formatParam.data[i].SITENAME;
            item.Value = formatParam.data[i].SITECODE;
            initDataArray.push(item);
        }
    }

    return initDataArray;
}

var formatEasyuiCombobox_DealerForShort = function (formatParam) {
    if (formatParam.needChooseAll == undefined) {
        formatParam.needChooseAll = true;
    }

    if (formatParam.chooseAllValue == undefined) {
        formatParam.chooseAllValue = "";
    }

    var initDataArray = [];
    if (formatParam.needChooseAll) {
        initDataArray = [{ "Value": formatParam.chooseAllValue, "Text": "-经销商-"}];
    }
    if (formatParam.data) {
        for (var i = 0; i < formatParam.data.length; i++) {
            var item = {};
            item.Text = formatParam.data[i].CODE + "-" + formatParam.data[i].NAMESHORT;
            item.Value = formatParam.data[i].CODE;
            initDataArray.push(item);
        }
    }

    return initDataArray;
}

var formatEasyuiCombobox_ContorForShort = function (formatParam) {
    if (formatParam.needChooseAll == undefined) {
        formatParam.needChooseAll = true;
    }

    if (formatParam.chooseAllValue == undefined) {
        formatParam.chooseAllValue = "";
    }

    var initDataArray = [];
    if (formatParam.needChooseAll) {
        initDataArray = [{ "Value": formatParam.chooseAllValue, "Text": "-承运商-"}];
    }
    if (formatParam.data) {
        for (var i = 0; i < formatParam.data.length; i++) {
            var item = {};
            //item.Text = formatParam.data[i].CODE + "-" + formatParam.data[i].SHORTNAME;
            item.Text = formatParam.data[i].CODE;
            item.Value = formatParam.data[i].CODE;
            initDataArray.push(item);
        }
    }

    return initDataArray;
}

var formatEasyuiCombobox_DriverForCY = function (formatParam) {
    if (formatParam.needChooseAll == undefined) {
        formatParam.needChooseAll = true;
    }

    if (formatParam.chooseAllValue == undefined) {
        formatParam.chooseAllValue = "";
    }

    var initDataArray = [];
    if (formatParam.needChooseAll) {
        initDataArray = [{ "Value": formatParam.chooseAllValue, "Text": "--所有--"}];
    }
    if (formatParam.data) {
        for (var i = 0; i < formatParam.data.length; i++) {
            var item = {};
            item.Text = formatParam.data[i].NAME;
            item.Value = formatParam.data[i].CODE;
            item.SN = formatParam.data[i].SN;
            initDataArray.push(item);
        }
    }

    return initDataArray;
}

//var formatEasyuiCombobox_DriverByInfo = function (formatParam) {
//    if (formatParam.needChooseAll == undefined) {
//        formatParam.needChooseAll = true;
//    }

//    if (formatParam.chooseAllValue == undefined) {
//        formatParam.chooseAllValue = "";
//    }

//    var initDataArray = [];
//    if (formatParam.needChooseAll) {
//        initDataArray = [{ "Value": formatParam.chooseAllValue, "Text": "--所有--"}];
//    }
//    if (formatParam.data) {
//        for (var i = 0; i < formatParam.data.length; i++) {
//            var item = {};
//            item.Text = formatParam.data[i].drname;
//            item.Value = formatParam.data[i].TRUCK_OWNER;
//            initDataArray.push(item);
//        }
//    }

//    return initDataArray;
//}

//var formatEasyuiCombobox_Truck = function (formatParam) {
//    if (formatParam.needChooseAll == undefined) {
//        formatParam.needChooseAll = true;
//    }

//    if (formatParam.chooseAllValue == undefined) {
//        formatParam.chooseAllValue = "";
//    }

//    var initDataArray = [];
//    if (formatParam.needChooseAll) {
//        initDataArray = [{ "Value": formatParam.chooseAllValue, "Text": "--所有--"}];
//    }
//    if (formatParam.data) {
//        for (var i = 0; i < formatParam.data.length; i++) {
//            var item = {};
//            item.Text = formatParam.data[i].TRUCK_NO;
//            item.Value = formatParam.data[i].TRUCK_NO;
//            initDataArray.push(item);
//        }
//    }

//    return initDataArray;
//}

var formatEasyuiCombobox_TruckNo = function (formatParam) {
    if (formatParam.needChooseAll == undefined) {
        formatParam.needChooseAll = true;
    }

    if (formatParam.chooseAllValue == undefined) {
        formatParam.chooseAllValue = "";
    }

    var initDataArray = [];
    if (formatParam.needChooseAll) {
        initDataArray = [{ "Value": formatParam.chooseAllValue, "Text": "--所有--"}];
    }
    if (formatParam.data) {
        for (var i = 0; i < formatParam.data.length; i++) {
            var item = {};
            item.Text = formatParam.data[i].TRUCKNO;
            item.Value = formatParam.data[i].TRUCKNO;
            item.TRUCK_OWNER = formatParam.data[i].TRUCK_OWNER;
            item.DICG = formatParam.data[i].DICG;
            item.SN = formatParam.data[i].SN;
            item.TRUCK_OWNER_TEL = formatParam.data[i].TRUCK_OWNER_TEL;
            initDataArray.push(item);
        }
    }

    return initDataArray;
}

var formatEasyuiCombobox_TruckNoG = function (formatParam) {
    if (formatParam.needChooseAll == undefined) {
        formatParam.needChooseAll = true;
    }

    if (formatParam.chooseAllValue == undefined) {
        formatParam.chooseAllValue = "";
    }

    var initDataArray = [];
    if (formatParam.needChooseAll) {
        initDataArray = [{ "Value": formatParam.chooseAllValue, "Text": "--所有--"}];
    }
    if (formatParam.data) {
        for (var i = 0; i < formatParam.data.length; i++) {
            var item = {};
            item.Text = formatParam.data[i].DICG;
            item.Value = formatParam.data[i].DICG;
            initDataArray.push(item);
        }
    }

    return initDataArray;
}

var formatEasyuiCombobox_TranSportRoute = function (formatParam) {
    if (formatParam.needChooseAll == undefined) {
        formatParam.needChooseAll = true;
    }

    if (formatParam.chooseAllValue == undefined) {
        formatParam.chooseAllValue = "";
    }

    var initDataArray = [];
    if (formatParam.needChooseAll) {
        initDataArray = [{ "Value": formatParam.chooseAllValue, "Text": "--所有--"}];
    }
    if (formatParam.data) {
        for (var i = 0; i < formatParam.data.length; i++) {
            var item = {};
            item.Text = formatParam.data[i].LINE_CODENAME;
            item.Value = formatParam.data[i].LINE_CODE;
            initDataArray.push(item);
        }
    }

    return initDataArray;
}

var formatEasyuiCombobox_RouteList = function (formatParam) {
    if (formatParam.needChooseAll == undefined) {
        formatParam.needChooseAll = true;
    }

    if (formatParam.chooseAllValue == undefined) {
        formatParam.chooseAllValue = "";
    }

    var initDataArray = [];
    if (formatParam.needChooseAll) {
        initDataArray = [{ "Value": formatParam.chooseAllValue, "Text": "--线路号--"}];
    }
    if (formatParam.data) {
        for (var i = 0; i < formatParam.data.length; i++) {
            var item = {};
            item.Text = formatParam.data[i].CODE;
            item.Value = formatParam.data[i].CODE;
            initDataArray.push(item);
        }
    }

    return initDataArray;
}

/*通用的格式化成easyui combobox数据*/
var formatEasyuiCombobox_Server = function (formatParam, isSplit) {
    if (formatParam.needChooseAll == undefined) {
        formatParam.needChooseAll = true;
    }

    if (formatParam.chooseAllValue == undefined) {
        formatParam.chooseAllValue = "";
    }

    if (isSplit == undefined) {
        isSplit = true;
    }

    var initDataArray = [];
    var cookieOrderArray = [];
    var topShowData = [];
    if (formatParam.cookieOrderKey) {
        var cookieOrderValue = $.cookie(formatParam.cookieOrderKey);
        var cookieOrderArray = JSON.parse(cookieOrderValue);
        if (!cookieOrderArray) {
            cookieOrderArray = [];
        }
    }

    if (formatParam.data) {
        for (var i = 0; i < formatParam.data.length; i++) {
            var item = {};
            item.Text = formatParam.data[i].SeacherName;
            if (isSplit) {
                item.Text = formatParam.data[i].CODE + "-" + formatParam.data[i].NAME;
            } else {
                item.Text = formatParam.data[i].NAME;
            }
            item.Value = formatParam.data[i].CODE;
            if (cookieOrderArray.length > 0) {
                var insertTopShow = false;
                $.each(cookieOrderArray, function (index, content) {
                    if (content.Value == item.Value) {
                        item.Order = content.Order;
                        topShowData.push(item);
                        insertTopShow = true;
                        return false;
                    }
                });
                if (!insertTopShow) {
                    initDataArray.push(item);
                }
            } else {
                initDataArray.push(item);
            }
        }

        // 根据order排序
        if (topShowData.length > 0) {
            var topShowDataList = Enumerable.From(topShowData)
                      .OrderByDescending(function (x) { return x.Order; })
                      .ToArray();
            $.each(topShowDataList, function (index, content) {
                initDataArray.unshift(content);
            });
        }
    }

    if (formatParam.needChooseAll) {
        initDataArray.unshift({ "Value": formatParam.chooseAllValue, "Text": "-所有-" });
    }

    return initDataArray;
}

var formatEasyuiCombobox_ErrorKind = function (formatParam, isSplit) {
    if (formatParam.needChooseAll == undefined) {
        formatParam.needChooseAll = true;
    }

    if (formatParam.chooseAllValue == undefined) {
        formatParam.chooseAllValue = "";
    }

    if (isSplit == undefined) {
        isSplit = true;
    }

    var initDataArray = [];
    var cookieOrderArray = [];
    var topShowData = [];
    if (formatParam.cookieOrderKey) {
        var cookieOrderValue = $.cookie(formatParam.cookieOrderKey);
        var cookieOrderArray = JSON.parse(cookieOrderValue);
        if (!cookieOrderArray) {
            cookieOrderArray = [];
        }
    }

    if (formatParam.data) {
        for (var i = 0; i < formatParam.data.length; i++) {
            var item = {};
            item.Text = formatParam.data[i].SeacherName;
            item.Text = formatParam.data[i].NAME;
            item.Value = formatParam.data[i].NAME;
            if (cookieOrderArray.length > 0) {
                var insertTopShow = false;
                $.each(cookieOrderArray, function (index, content) {
                    if (content.Value == item.Value) {
                        item.Order = content.Order;
                        topShowData.push(item);
                        insertTopShow = true;
                        return false;
                    }
                });
                if (!insertTopShow) {
                    initDataArray.push(item);
                }
            } else {
                initDataArray.push(item);
            }
        }

        // 根据order排序
        if (topShowData.length > 0) {
            var topShowDataList = Enumerable.From(topShowData)
                      .OrderByDescending(function (x) { return x.Order; })
                      .ToArray();
            $.each(topShowDataList, function (index, content) {
                initDataArray.unshift(content);
            });
        }
    }

    if (formatParam.needChooseAll) {
        initDataArray.unshift({ "Value": formatParam.chooseAllValue, "Text": "-所有-" });
    }

    return initDataArray;
}

var formatEasyuiCombobox_ServerContractor = function (formatParam, isSplit) {
    if (formatParam.needChooseAll == undefined) {
        formatParam.needChooseAll = true;
    }

    if (formatParam.chooseAllValue == undefined) {
        formatParam.chooseAllValue = "";
    }

    if (isSplit == undefined) {
        isSplit = true;
    }

    var initDataArray = [];
    var cookieOrderArray = [];
    var topShowData = [];
    if (formatParam.cookieOrderKey) {
        var cookieOrderValue = $.cookie(formatParam.cookieOrderKey);
        var cookieOrderArray = JSON.parse(cookieOrderValue);
        if (!cookieOrderArray) {
            cookieOrderArray = [];
        }
    }

    if (formatParam.data) {
        for (var i = 0; i < formatParam.data.length; i++) {
            var item = {};
            item.Text = formatParam.data[i].SeacherName;
            if (isSplit) {
                item.Text = formatParam.data[i].SHORTNAME;
            } else {
                item.Text = formatParam.data[i].SHORTNAME;
            }
            item.Value = formatParam.data[i].CODE;
            if (cookieOrderArray.length > 0) {
                var insertTopShow = false;
                $.each(cookieOrderArray, function (index, content) {
                    if (content.Value == item.Value) {
                        item.Order = content.Order;
                        topShowData.push(item);
                        insertTopShow = true;
                        return false;
                    }
                });
                if (!insertTopShow) {
                    initDataArray.push(item);
                }
            } else {
                initDataArray.push(item);
            }
        }

        // 根据order排序
        if (topShowData.length > 0) {
            var topShowDataList = Enumerable.From(topShowData)
                      .OrderByDescending(function (x) { return x.Order; })
                      .ToArray();
            $.each(topShowDataList, function (index, content) {
                initDataArray.unshift(content);
            });
        }
    }

    if (formatParam.needChooseAll) {
        initDataArray.unshift({ "Value": formatParam.chooseAllValue, "Text": "--所有--" });
    }

    return initDataArray;
}

var formatEasyuiCombobox_Server_City = function (formatParam) {
    if (formatParam.needChooseAll == undefined) {
        formatParam.needChooseAll = true;
    }

    if (formatParam.chooseAllValue == undefined) {
        formatParam.chooseAllValue = "";
    }

    var initDataArray = [];
    var cookieOrderArray = [];
    var topShowData = [];
    if (formatParam.cookieOrderKey) {
        var cookieOrderValue = $.cookie(formatParam.cookieOrderKey);
        var cookieOrderArray = JSON.parse(cookieOrderValue);
        if (!cookieOrderArray) {
            cookieOrderArray = [];
        }
    }

    if (formatParam.data) {
        for (var i = 0; i < formatParam.data.length; i++) {
            var item = {};
            item.Text = formatParam.data[i].SeacherName;
            item.Text = formatParam.data[i].ACRONYM + "-" + formatParam.data[i].NAME; //formatParam.data[i].ID + "-" +
            item.Value = formatParam.data[i].NAME;
            if (cookieOrderArray.length > 0) {
                var insertTopShow = false;
                $.each(cookieOrderArray, function (index, content) {
                    if (content.Value == item.Value) {
                        item.Order = content.Order;
                        topShowData.push(item);
                        insertTopShow = true;
                        return false;
                    }
                });
                if (!insertTopShow) {
                    initDataArray.push(item);
                }
            } else {
                initDataArray.push(item);
            }
        }

        // 根据order排序
        if (topShowData.length > 0) {
            var topShowDataList = Enumerable.From(topShowData)
                      .OrderByDescending(function (x) { return x.Order })
                      .ToArray();
            $.each(topShowDataList, function (index, content) {
                initDataArray.unshift(content);
            });
        }
    }

    if (formatParam.needChooseAll) {
        initDataArray.unshift({ "Value": formatParam.chooseAllValue, "Text": "--所有--" });
    }

    return initDataArray;
}

var formatEasyuiCombobox_Server_CityTwo = function (formatParam) {
    if (formatParam.needChooseAll == undefined) {
        formatParam.needChooseAll = true;
    }

    if (formatParam.chooseAllValue == undefined) {
        formatParam.chooseAllValue = "";
    }

    var initDataArray = [];
    var cookieOrderArray = [];
    var topShowData = [];
    if (formatParam.cookieOrderKey) {
        var cookieOrderValue = $.cookie(formatParam.cookieOrderKey);
        var cookieOrderArray = JSON.parse(cookieOrderValue);
        if (!cookieOrderArray) {
            cookieOrderArray = [];
        }
    }

    if (formatParam.data) {
        for (var i = 0; i < formatParam.data.length; i++) {
            var item = {};
            item.Text = formatParam.data[i].SeacherName;
            item.Text = formatParam.data[i].NAME; //formatParam.data[i].ID + "-" +
            item.Value = formatParam.data[i].NAME;
            if (cookieOrderArray.length > 0) {
                var insertTopShow = false;
                $.each(cookieOrderArray, function (index, content) {
                    if (content.Value == item.Value) {
                        item.Order = content.Order;
                        topShowData.push(item);
                        insertTopShow = true;
                        return false;
                    }
                });
                if (!insertTopShow) {
                    initDataArray.push(item);
                }
            } else {
                initDataArray.push(item);
            }
        }

        // 根据order排序
        if (topShowData.length > 0) {
            var topShowDataList = Enumerable.From(topShowData)
                      .OrderByDescending(function (x) { return x.Order })
                      .ToArray();
            $.each(topShowDataList, function (index, content) {
                initDataArray.unshift(content);
            });
        }
    }

    if (formatParam.needChooseAll) {
        initDataArray.unshift({ "Value": formatParam.chooseAllValue, "Text": "--所有--" });
    }

    return initDataArray;
}

var formatEasyuiCombobox_Server_CityByDealer = function (formatParam) {
    if (formatParam.needChooseAll == undefined) {
        formatParam.needChooseAll = true;
    }

    if (formatParam.chooseAllValue == undefined) {
        formatParam.chooseAllValue = "";
    }

    var initDataArray = [];
    var cookieOrderArray = [];
    var topShowData = [];
    if (formatParam.cookieOrderKey) {
        var cookieOrderValue = $.cookie(formatParam.cookieOrderKey);
        var cookieOrderArray = JSON.parse(cookieOrderValue);
        if (!cookieOrderArray) {
            cookieOrderArray = [];
        }
    }

    if (formatParam.data) {
        for (var i = 0; i < formatParam.data.length; i++) {
            var item = {};
            item.Text = formatParam.data[i].SeacherName;
            item.Text = formatParam.data[i].CITYNAME; //formatParam.data[i].ID + "-" +
            item.Value = formatParam.data[i].CITYNAME;
            if (cookieOrderArray.length > 0) {
                var insertTopShow = false;
                $.each(cookieOrderArray, function (index, content) {
                    if (content.Value == item.Value) {
                        item.Order = content.Order;
                        topShowData.push(item);
                        insertTopShow = true;
                        return false;
                    }
                });
                if (!insertTopShow) {
                    initDataArray.push(item);
                }
            } else {
                initDataArray.push(item);
            }
        }

        // 根据order排序
        if (topShowData.length > 0) {
            var topShowDataList = Enumerable.From(topShowData)
                      .OrderByDescending(function (x) { return x.Order })
                      .ToArray();
            $.each(topShowDataList, function (index, content) {
                initDataArray.unshift(content);
            });
        }
    }

    if (formatParam.needChooseAll) {
        initDataArray.unshift({ "Value": formatParam.chooseAllValue, "Text": "--所有--" });
    }

    return initDataArray;
}

var formatEasyuiCombobox_Server_Dealer = function (formatParam) {
    if (formatParam.needChooseAll == undefined) {
        formatParam.needChooseAll = true;
    }

    if (formatParam.chooseAllValue == undefined) {
        formatParam.chooseAllValue = "";
    }

    var initDataArray = [];
    var cookieOrderArray = [];
    var topShowData = [];
    if (formatParam.cookieOrderKey) {
        var cookieOrderValue = $.cookie(formatParam.cookieOrderKey);
        var cookieOrderArray = JSON.parse(cookieOrderValue);
        if (!cookieOrderArray) {
            cookieOrderArray = [];
        }
    }

    if (formatParam.data) {
        for (var i = 0; i < formatParam.data.length; i++) {
            var item = {};
            //            item.Text = formatParam.data[i].Name;
            item.Text = formatParam.data[i].CODE + "-" + formatParam.data[i].NAME; //formatParam.data[i].ID + "-" +
            item.Value = formatParam.data[i].CODE;
            if (cookieOrderArray.length > 0) {
                var insertTopShow = false;
                $.each(cookieOrderArray, function (index, content) {
                    if (content.Value == item.Value) {
                        item.Order = content.Order;
                        topShowData.push(item);
                        insertTopShow = true;
                        return false;
                    }
                });
                if (!insertTopShow) {
                    initDataArray.push(item);
                }
            } else {
                initDataArray.push(item);
            }
        }

        // 根据order排序
        if (topShowData.length > 0) {
            var topShowDataList = Enumerable.From(topShowData)
                      .OrderByDescending(function (x) { return x.Order })
                      .ToArray();
            $.each(topShowDataList, function (index, content) {
                initDataArray.unshift(content);
            });
        }
    }

    if (formatParam.needChooseAll) {
        initDataArray.unshift({ "Value": formatParam.chooseAllValue, "Text": "-经销商-" });
    }

    return initDataArray;
}

var formatEasyuiCombobox_ServerQianNo = function (formatParam) {
    if (formatParam.needChooseAll == undefined) {
        formatParam.needChooseAll = true;
    }

    if (formatParam.chooseAllValue == undefined) {
        formatParam.chooseAllValue = "";
    }

    var initDataArray = [];
    var cookieOrderArray = [];
    var topShowData = [];
    if (formatParam.cookieOrderKey) {
        var cookieOrderValue = $.cookie(formatParam.cookieOrderKey);
        var cookieOrderArray = JSON.parse(cookieOrderValue);
        if (!cookieOrderArray) {
            cookieOrderArray = [];
        }
    }

    if (formatParam.data) {
        for (var i = 0; i < formatParam.data.length; i++) {
            var item = {};
            //            item.Text = formatParam.data[i].SeacherName;
            item.Text = formatParam.data[i].TRACTORNO_CODE;
            item.Value = formatParam.data[i].TRACTORNO_CODE;
            if (cookieOrderArray.length > 0) {
                var insertTopShow = false;
                $.each(cookieOrderArray, function (index, content) {
                    if (content.Value == item.Value) {
                        item.Order = content.Order
                        topShowData.push(item);
                        insertTopShow = true;
                        return false;
                    }
                });
                if (!insertTopShow) {
                    initDataArray.push(item);
                }
            } else {
                initDataArray.push(item);
            }
        }

        // 根据order排序
        if (topShowData.length > 0) {
            var topShowDataList = Enumerable.From(topShowData)
                      .OrderByDescending(function (x) { return x.Order })
                      .ToArray();
            $.each(topShowDataList, function (index, content) {
                initDataArray.unshift(content);
            });
        }
    }

    if (formatParam.needChooseAll) {
        initDataArray.unshift({ "Value": formatParam.chooseAllValue, "Text": "--所有--" });
    }

    return initDataArray;
}

var formatEasyuiCombobox_ServerGuaNo = function (formatParam) {
    if (formatParam.needChooseAll == undefined) {
        formatParam.needChooseAll = true;
    }

    if (formatParam.chooseAllValue == undefined) {
        formatParam.chooseAllValue = "";
    }

    var initDataArray = [];
    var cookieOrderArray = [];
    var topShowData = [];
    if (formatParam.cookieOrderKey) {
        var cookieOrderValue = $.cookie(formatParam.cookieOrderKey);
        var cookieOrderArray = JSON.parse(cookieOrderValue);
        if (!cookieOrderArray) {
            cookieOrderArray = [];
        }
    }

    if (formatParam.data) {
        for (var i = 0; i < formatParam.data.length; i++) {
            var item = {};
            //            item.Text = formatParam.data[i].SeacherName;
            item.Text = formatParam.data[i].TRAILERNO_CODE;
            item.Value = formatParam.data[i].TRAILERNO_CODE;
            if (cookieOrderArray.length > 0) {
                var insertTopShow = false;
                $.each(cookieOrderArray, function (index, content) {
                    if (content.Value == item.Value) {
                        item.Order = content.Order
                        topShowData.push(item);
                        insertTopShow = true;
                        return false;
                    }
                });
                if (!insertTopShow) {
                    initDataArray.push(item);
                }
            } else {
                initDataArray.push(item);
            }
        }

        // 根据order排序
        if (topShowData.length > 0) {
            var topShowDataList = Enumerable.From(topShowData)
                      .OrderByDescending(function (x) { return x.Order })
                      .ToArray();
            $.each(topShowDataList, function (index, content) {
                initDataArray.unshift(content);
            });
        }
    }

    if (formatParam.needChooseAll) {
        initDataArray.unshift({ "Value": formatParam.chooseAllValue, "Text": "--所有--" });
    }

    return initDataArray;
}

/*格式化成easyui combobox数据-特殊业务地点*/
var formatEasyuiCombobox_Server_YwLocation = function (formatParam) {
    if (!formatParam.data) {
        return "";
    }

    if (formatParam.needChooseAll == undefined) {
        formatParam.needChooseAll = true;
    }

    if (formatParam.chooseAllValue == undefined) {
        formatParam.chooseAllValue = "";
    }

    var initDataArray = [];
    if (formatParam.needChooseAll) {
        initDataArray = [{ "Value": formatParam.chooseAllValue, "Text": "--所有--", "Desc1": ""}];
    }

    for (var i = 0; i < formatParam.data.length; i++) {
        var item = {};
        item.Text = formatParam.data[i].NAME;
        item.Value = formatParam.data[i].CODE;
        item.Desc1 = formatParam.data[i].ICO;
        initDataArray.push(item);
    }

    return initDataArray;
}

var imgView = function (imgUrl) {
    $("#viewBox").remove();
    var maxViewHtml = ' <div id="viewBox" class="viewBox">';
    maxViewHtml += '<div class="closeViewBox"></div>';
    maxViewHtml += '<img class="viewImg" src="' + imgUrl + '"/>';
    maxViewHtml += '</div>';
    $(document.body).append(maxViewHtml);
    var maxViewImgWidth = parseInt($("#viewBox").css("width")) - 50;
    $(".viewImg").css({ "width": maxViewImgWidth + "px" });
    $("#viewBox").css({ "display": "block" });
    $(".closeViewBox").unbind("click").click(function () {
        $("#viewBox").css("display", "none");
        $("#viewBox").remove();
    });
}

/*向cookie中添加置顶元素，并更新cookie值*/
var CookieOrderAdd = function (cookieName, addValue) {
    var cookieValue = $.cookie(cookieName);
    var cookieValueArray = JSON.parse(cookieValue);
    if (!cookieValueArray) {
        cookieValueArray = [];
    }
    // 删除cookie中已经存在的
    var needDeleteCount = 0;
    Enumerable.From(cookieValueArray).ToArray().forEach(function (i) {
        if (i.CODE == addValue) {
            cookieValueArray.splice(needDeleteCount, 1);
        }

        needDeleteCount++;
    });

    var newInsert = { Order: 1, Value: addValue };
    // 其他的Order+1
    $.each(cookieValueArray, function (index, content) {
        content.Order = parseInt(content.Order) + 1;
    });
    // 插入第一位
    cookieValueArray.unshift(newInsert);
    // 删除超过部分
    if (cookieValueArray.length > 5) {
        cookieValueArray.splice(5, 1);
    }
    $.cookie(cookieName, JSON.stringify(cookieValueArray), { path: '/' });
}

jQuery.fn.shiftSelect = function () {
    var checkboxes = this;
    var lastSelected;
    var executing = false;
    jQuery(this).click(function (event) {

        if (executing)
            return;

        if (!lastSelected) {
            lastSelected = this;
            return;
        }

        if (event.shiftKey) {
            var selIndex = checkboxes.index(this);
            var lastIndex = checkboxes.index(lastSelected);
            /*
            * if you find the "select/unselect" behavior unseemly,
            * remove this assignment and replace 'checkValue'
            * with 'true' below.
            */
            var checkValue = lastSelected.checked;
            if (selIndex == lastIndex) {
                return true;
            }
            executing = true;
            var end = Math.max(selIndex, lastIndex);
            var start = Math.min(selIndex, lastIndex);
            for (i = start; i <= end; i++) {
                if (checkboxes[i].checked != checkValue)
                    $(checkboxes[i]).click();
            }
            executing = false;
        }
        lastSelected = this;
    });
}