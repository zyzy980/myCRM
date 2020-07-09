/**
 * Created by weekeyanYear on 2019/6/25.
 */
var afterSave = "close";
var savedRow = null;
var savedCol = null;
var id="";
var code = "";
var callerId = "";
var callerType = "";
var callbackFlag = "";
var globalStateArray = "";
var originalFormData = "";
var originalgridData = "";
$(function () {
    var parms = getUrlParms();
    id = parms["id"];
    //code = parms["code"];
    callerId = parms["callerId"];
    callerType = parms["callerType"];
    callbackFlag = parms["callbackFlag"];
    initScript();
    initData();
    initStyle();
});

$(window).load(function () {
    hideLoading();
});

function initStyle() {
    if (id != "") {
        $("input", $("#id").next("span")).attr("readonly", "readonly");
    }
}

function initScript() {
    /*  loadform(); */
}


function initData() {
    initRadioCheckboxData();
    getDates();
    if (id != "") {
        loadDetail();
    }
    else {
        $("#id").val("0");
        originalFormData = $('#addForm').serialize();
    }
}

/**分类状态单选框*/
function initRadioCheckboxData() {
    //分类
    getDictionaryData({
        dicTypeCode : Global_DicType.Global_DicType_divtype,
        async : false,
        callback : function(callbackData) {
            initTypeControl(callbackData);
            globalKindArray = callbackData;
        }
    });
    //状态
    getDictionaryData({
        dicTypeCode : Global_DicType.Global_DicType_CusStatus,
        async : false,
        callback : function(callbackData) {
            initStatusControl(callbackData);
            globalStateArray = callbackData;
        }
    });
}

function initTypeControl (jsonData){
    jsonData.unshift({
        dicValue : "",
        dicText : $.i18n.prop('all')
    });
    $('#type').combobox({
        data : jsonData,
        valueField : 'dicValue',
        textField : 'dicText',
        panelHeight : 120,
        editable : true,
        formatter : function(row) {
            if (row.dicValue == "") {
                return '<table style=\"width:100%;\"><tr><td style=\"text-align:left;width:80%;\"><span >'
                    + row.dicText
                    + '</span></td><td style=\"text-align:right;width:20%;\"></td></tr></table>';
            } else {
                return '<table style=\"width:100%;\"><tr><td style=\"text-align:left;width:80%;\"><span >'
                    +row.dicValue+'-'+row.dicText
                    + '</span></td><td style=\"text-align:right;width:20%;\"></td></tr></table>';
            }
        },
        onLoadSuccess : function(e) {
        }
    });
}


function initStatusControl (jsonData){
    jsonData.unshift({
        dicValue : "",
        dicText : $.i18n.prop('all')
    });
    $('#status').combobox({
        data : jsonData,
        valueField : 'dicValue',
        textField : 'dicText',
        panelHeight : 120,
        editable : true,
        formatter : function(row) {
            if (row.dicValue == "") {
                return '<table style=\"width:100%;\"><tr><td style=\"text-align:left;width:80%;\"><span >'
                    + row.dicText
                    + '</span></td><td style=\"text-align:right;width:20%;\"></td></tr></table>';
            } else {
                return '<table style=\"width:100%;\"><tr><td style=\"text-align:left;width:80%;\"><span >'
                    +row.dicValue+'-'+row.dicText
                    + '</span></td><td style=\"text-align:right;width:20%;\"></td></tr></table>';
            }
        },
        onLoadSuccess : function(e) {
        }
    });
}
/**聚焦*/
function loseGridFocus() {
    if (savedRow && savedCol) {
        $('#gridTable').jqGrid('saveCell', savedRow, savedCol);
    }
}
/**删除行*/
function remove() {
    var jsonList = [];
    var selectRowItems = $("#gridTable").jqGrid("getGridParam", "selarrrow");
    if (selectRowItems.length == 0) {
        errorNotification({
            SimpleMessage: "请至少选择一行数据行进行操作"
        });
        return;
    }

    for (var i = 0; i < selectRowItems.length; i++) {
        var rowData = $("#gridTable").jqGrid('getRowData', selectRowItems[i]);
        jsonList.push({sn: rowData.sn});
    }

    $.messager.confirm('提示', '确定删除选中的数据项吗?', function (r) {
        if (r) {
            showLoading();
            var addcusAddressArray = [];
            for (var i = 0; i < jsonList.length; i++) {
                var curRowData = $("#gridTable").jqGrid("getRowData", jsonList[i]);
                var operatetype = curRowData["operatetype"];
                // 此处不能用sn为0来判断，因为sn为0的话，将无法查询到任何行
                if (operatetype == "add") {
                    //说明是临时增加的，可删除
                    $("#gridTable").jqGrid("delRowData", jsonList[i]);
                } else {
                    addcusAddressArray.push(jsonList[i]);
                }
            }
            if (addcusAddressArray.length > 0) {
                $.ajax({
                    url : "../../jcda/ZdCus/delete?t="+ Math.random(),
                    type : "POST",
                    data : "jsonData="+ JSON.stringify(addcusAddressArray),
                    success : function(dataObj) {
                        if (isServerResultDataPass(dataObj)) {
                            correctNotification(dataObj.resultDataFull);
                            searchData(); // 删除时需要重新搜索
                        } else {
                            FailResultDataToTip(dataObj);
                        }
                        hideLoading();
                    },
                    error : function(message) {
                        console.log(message);
                        hideLoading();
                    }
                });
            }

            hideLoading();
        }
    });
}
/**关闭*/
function close() {
    closeDialog("Cr_CusDetail");
}

/**重置*/
function reset() {
    loseGridFocus();
    var newFormData = $('#addForm').serialize();
    var newgidArry = $("#gridTable").jqGrid("getRowData");
    var newgridData = JSON.stringify(newgidArry);
    if (newFormData != originalFormData || newgridData != originalgridData) {
        $.messager.confirm('提示', '您已经修改部分数据，确定放弃修改并且重置吗?', function (r) {
            if (r) {
                if (id != "") {
                    loadDetail();
                    searchData();
                } else {
                    $('form').form('clear');
                    initRadioCheckboxData();
                    searchData();
                }
            }
        });
    }
}
/**验证*/
function validate() {
    var validated = $("#addForm").form('enableValidation').form('validate');
    if (!validated) {
        errorNotification({
            SimpleMessage: "保存操作中部分数据验证不通过"
        });
        return false;
    }

    var newFormData = $('#addForm').serialize();
    loseGridFocus();
    var newgidArry = $("#gridTable").jqGrid("getRowData");
    var newgridData = JSON.stringify(newgidArry);
    if (newFormData == originalFormData && newgridData == originalgridData) {
        errorNotification({
            SimpleMessage: "您未做任何修改,不需要保存"
        });
        return false;
    }
    var errorMessage = "";
    var gridAllData = $("#gridTable").jqGrid("getRowData");
    for (var i = 0; i < gridAllData.length; i++) {
        var regNotEmpty = new RegExp(regexEnum.notempty);
        var regMobile = new RegExp(regexEnum.mobile);
        var regQQ = new RegExp(regexEnum.qq);
        var regEmail = new RegExp(regexEnum.email);

        if (!regNotEmpty.test(gridAllData[i]["realname"])) {
            errorMessage += "<p>第" + (i + 1) + "行 联系人 不能为空</p>";
        }

        if (!regNotEmpty.test(gridAllData[i]["mobileno"])) {
            errorMessage += "<p>第" + (i + 1) + "行 手机号 不能为空</p>";
        }

        if (regEmail.test(gridAllData[i]["mail"])) {
            errorMessage += "<p>第" + (i + 1) + "行 电子邮件 格式不正确</p>";
        }

    }
    if (errorMessage.length != "") {
        errorNotification({
            SimpleMessage: "验证不通过",
            MoreMessage: errorMessage
        });
        return false;
    }
    return true;
}
/**新增行明细*/
function add() {
    loseGridFocus();
    var ids = $("#gridTable").jqGrid('getDataIDs');
    var rowid = Math.max.apply(Math, ids);
    if (rowid == "-Infinity")
        rowid = 0;
    var newrowid = rowid + 1;
    var dataRow = {
        operatetype: "add",
        /* sn: newrowid, */
        state: "1",
        cus_id: id,
        realname: "",
        mobileno: "",
        type: "",
        pisition: "",
        mail: "",
        wechat:"",
        dui_flag:"",
        remark:"",
        create_by:"",
        create_date:""
    };
    $("#gridTable").jqGrid("addRowData", newrowid, dataRow, "last");
}

/**保存*/
function save() {
    if (!validate()) {
        return;
    }
    // 判断jqgrid行
    $.messager.confirm('提示', '确定保存吗?', function (r) {
        if (r) {
            showLoading();
            afterSave = "close";
            /*  $("#addForm").submit(); */
            //id = $("#id").val();
            saveChild();
        }
    });
}

function saveChild() {
    loseGridFocus();
    var crCusVO={};
    crCusVO= FormUtils.toJSONObject($("#addForm"));
    var gridAllData = $("#gridTable").jqGrid("getRowData");
    //处理特殊数据[1、state数据格式的转换；2、cus_id列的动态添值]
    for (var i = 0; i < gridAllData.length; i++) {
        gridAllData[i].state = gridAllData[i].state == "Yes" ? BaseStateValid : BaseStateInValid;
        gridAllData[i].cus_id = id;
    }
    /*  var gridAllDataJson = JSON.stringify(gridAllData); */
    crCusVO.crCusLinks=gridAllData;
    $.ajax({
        url : "../../jcda/customer/SaveCus?t="+ Math.random(),
        contentType : 'application/json;charset=utf-8',
        dataType:"json",
        type : "POST",
        data : JSON.stringify(crCusVO),
        success : function(dataObj) {
            if (isServerResultDataPass_java(dataObj)) {
                correctNotification(dataObj.resultDataFull);
                hideLoading();
                afterSaveOperate();
            } else {
                hideLoading();
                FailResultDataToTip(dataObj);
            }
        }
    });
}

function saveAndAddClear() {
    if (!validate()) {
        return;
    }

    $.messager.confirm('提示', '确定保存吗?', function (r) {
        if (r) {
            showLoading();
            afterSave = "clear";
            $("#addForm").submit();
        }
    });
}

var saveAndAddCopy = function () {
    if (!validate()) {
        return;
    }

    $.messager.confirm('提示', '确定保存吗?', function (r) {
        if (r) {
            showLoading();
            afterSave = "copy";
            $("#addForm").submit();
        }
    });
}

function afterSaveOperate() {
    switch (afterSave) {
        case "close":
            refreshCallerData();
            close();
            break;
        case "clear":
            $('form').form('clear');
            $("#id").val("0");
            $("input", $("#txtCODE").next("span")).removeAttr("readonly");
            $("#gridTable").jqGrid("clearGridData");
            refreshCallerData();
            break;
        case "copy":
            $("#id").val("0");
            $("input", $("#txtCODE").next("span")).removeAttr("readonly");
            $("#txtCODE").textbox('setValue', '');
            refreshCallerData();
            break;
    }
}

function refreshCallerData() {
    if (callerType == Global_Constant.Global_Constant_CallerType_Dialog) {
        //此callerId此时为dialog的id
        getDialog(callerId).refreshCallerData_ZdCus(callbackFlag);
    }
    else {
        //此callerId此时为frame的id
        getCurrentTab(callerId).refreshCallerData_ZdCus(callbackFlag);
    }
}

function loadDetail() {
    showLoading();
    var cr_cusVO = {};
    //cr_cusVO.code = code;
    cr_cusVO.id = id;
    var serverUrl = "../../jcda/customer/getDetail?t="+ Math.random();
    $.ajax({
        type : "POST",
        url : serverUrl,
        data : JSON.stringify(cr_cusVO),
        contentType : 'application/json;charset=utf-8', // 设置请求头信息
        success : function(dataObj) {
            if (isServerResultDataPass(dataObj)) {
                $("#id").val(dataObj.resultDataFull.id);
                $("#code").textbox('setValue', dataObj.resultDataFull.code);
                $("#name").textbox('setValue', dataObj.resultDataFull.name);
                $("#tax").textbox('setValue', dataObj.resultDataFull.tax);
                $("#in_address").textbox('setValue', dataObj.resultDataFull.in_address);
                $("#bank").textbox('setValue', dataObj.resultDataFull.bank);
                $("#bank_no").textbox('setValue', dataObj.resultDataFull.bank_no);
                $("#in_tel").textbox('setValue', dataObj.resultDataFull.in_tel);
                $("#pri").textbox('setValue', dataObj.resultDataFull.pri);
                $("#city").textbox('setValue', dataObj.resultDataFull.city);
                $("#address").textbox('setValue', dataObj.resultDataFull.address);
                $("[name='type'][value='" + dataObj.resultDataFull.type + "']").prop("checked", true);
                $("[name='status'][value='" + dataObj.resultDataFull.status + "']").prop("checked", true);
                $("#remark").textbox('setValue', dataObj.resultDataFull.remark);
                originalFormData = $('#addForm').serialize();
            } else {
                FailResultDataToTip(dataObj);
            }
            hideLoading();
        },
    });
}

function getSearchGridUrl() {
    return '../../jcda/customer/findzdCusLinkPageVO?t=' + Math.random()+'&customSearchFilters=' + encodeURI(getSearchFilters());;
}

function getSearchFilters() {
    var parmsArray = [
        { name: 'cus_id', value: id == "0" ? "1=0" : code }
    ];

    return formatSearchParames(parmsArray);
};

function searchData() {
    $("#gridTable").jqGrid('setGridParam', {
        url: getSearchGridUrl(),
        datatype: 'json',
        postData: { "filters": "" },
        page: 1
    }).trigger("reloadGrid");
}

function getDates() {
    $("#gridTable").jqGrid({
        url: getSearchGridUrl(),
        datatype: "json",
        width: "none",
        colNames: ['操作', '主键','客户id', '状态', '是否接收邮件','联系人', '手机号', '类型', '职位', '电邮', '微信号','备注', '创建人', '创建时间'],
        colModel: [
            { name: 'operatetype', index: 'operatetype', width: 0, hidden: true },
            { name: 'id', index: 'id', key: true, align: 'center', sorttype: 'int', search: false, sortable: false, width: 40, hidden: true },
            { name: 'cus_id', index: 'cus_id', align: 'center', sorttype: 'int', search: false, sortable: false, width: 40, hidden: true },
            { name: 'state', index: 'state', align: 'center', fixed: true, width: 70, search: true, stype: 'select', searchoptions: { value: formatGridCombobox_Local(globalStateArray, true), sopt: ['eq'] },
                formatter: 'checkbox', formatoptions: { value: BaseStateValid + ':True;' + BaseStateInValid + ':False', disabled: false }
            },
            { name: 'dui_flag', index: 'dui_flag', align: 'center', fixed: true, width: 80, search: true, stype: 'select', searchoptions: { value: formatGridCombobox_Local(globalStateArray, true), sopt: ['eq'] },
                formatter: 'checkbox', formatoptions: { value: BaseStateValid + ':True;' + BaseStateInValid + ':False', disabled: false }
            },
            { name: 'realname', index: 'realname', editable: true, hidden: false, align: 'center', isSort: false, width: 120, type: 'string', search: true, searchoptions: { sopt: ['cn', 'eq', 'ne'] },
                editoptions: {
                    dataEvents: [{ type: 'focus', data: {}, fn: function (e) {
                        $(this).select();
                    }
                    }]
                }
            },
            { name: 'mobileno', index: 'mobileno', editable: true, align: 'center', isSort: false, width: 150, type: 'string', search: true, searchoptions: { sopt: ['cn', 'eq', 'ne'] },
                editoptions: {
                    dataEvents: [{ type: 'focus', data: {}, fn: function (e) {
                        $(this).select();
                    }
                    }]
                }
            },
            { name: 'type', index: 'type', editable: true, align: 'center', isSort: false, width: 70, type: 'string', search: true, searchoptions: { sopt: ['cn', 'eq', 'ne'] },
                editoptions: {
                    dataEvents: [{ type: 'focus', data: {}, fn: function (e) {
                        $(this).select();
                    }
                    }]
                }
            },
            { name: 'position', index: 'position', editable: true, align: 'center', isSort: false, width: 100, type: 'string', search: true, searchoptions: { sopt: ['cn', 'eq', 'ne'] },
                editoptions: {
                    dataEvents: [{ type: 'focus', data: {}, fn: function (e) {
                        $(this).select();
                    }
                    }]
                }
            },
            { name: 'mail', index: 'mail', editable: true, align: 'center', isSort: false, width: 120, type: 'string', search: true, searchoptions: { sopt: ['cn', 'eq', 'ne'] },
                editoptions: {
                    dataEvents: [{ type: 'focus', data: {}, fn: function (e) {
                        $(this).select();
                    }
                    }]
                }
            },
            { name: 'wechat', index: 'wechat', editable: true, align: 'center', isSort: false, width: 120, type: 'string', search: true, searchoptions: { sopt: ['cn', 'eq', 'ne'] },
                editoptions: {
                    dataEvents: [{ type: 'focus', data: {}, fn: function (e) {
                        $(this).select();
                    }
                    }]
                }
            },
            { name: 'remark', index: 'remark', editable: true, align: 'center', isSort: false, width: 120, type: 'string', search: true, searchoptions: { sopt: ['cn', 'eq', 'ne'] },
                editoptions: {
                    dataEvents: [{ type: 'focus', data: {}, fn: function (e) {
                        $(this).select();
                    }
                    }]
                }
            },
            { name: 'create_by', index: 'create_by', editable: false, align: 'center', isSort: false, width: 60, type: 'string', search: true, searchoptions: { sopt: ['cn', 'eq', 'ne']} },
            { name: 'create_date', index: 'create_date', editable: false, align: 'center', width: 120, isSort: false, type: 'string', search: true, searchoptions: { sopt: ['eq', 'ne', 'lt', 'le', 'gt', 'ge'], dataEvents: [{ type: 'click', data: {}, fn: function () { WdatePicker(); } }]} }

        ],
        cellEdit: true,
        cellsubmit: 'clientArray',
        shrinkToFit: false,
        altRows: true,
        altclass: 'gridSpacingClass',
        forceFit: true,
        cellLayout: 0,
        scroll: false,
        autowidth: true,
        sortname: 'id',
        sortorder: "asc",
        loadonce: false,
        mtype: "POST",
        viewrecords: true,
        rownumbers: true,
        multiselect: true,
        rowNum: 15,
        height: 378,
        rowList: [15, 20, 30, 50],
        pager: "#gridPager",
        jsonReader: {
            root: "rows",
            total: "total",
            page: "page",
            records: "records",
            repeatitems: false
        },
        gridComplete: function () {
            $(".cbox").shiftSelect();
        },
        loadComplete: function (xhr) {
            var gidArry = $("#gridTable").jqGrid("getRowData");
            originalgridData = JSON.stringify(gidArry);
            FailResultDataToTip(xhr);
            hideLoading();
        },
        onSelectRow: function (id) {
        },
        beforeRequest: function () {
            $("#gridTable").jqGrid("clearGridData");
        }
    });
    $("#gridTable").jqGrid('navGrid', '#gridPager', { add: false, edit: false, del: false, refresh: true }, {}, {}, {}, { multipleSearch: true, closeOnEscape: true, closeAfterSearch: true });
    $("#gridTable").jqGrid('navButtonAdd', '#gridPager', {
        caption: "设置列",
        title: "Reorder Columns",
        onClickButton: toggleGridColumns
    });
    $("#gridTable").jqGrid('navButtonAdd', '#gridPager', {
        caption: "快捷搜索",
        title: "",
        onClickButton: toggleGridSearchToolbar
    });

    $('#gridTable').setGridParam({
        beforeEditCell: function (rowid, cellname, value, iRow, iCol) {
            savedRow = iRow;
            savedCol = iCol;
        }
    });

    setGridHeightWidth();
}