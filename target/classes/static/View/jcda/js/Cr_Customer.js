/**
 * Created by weekeyanYear on 2019/6/24.
 */
var globalStatusArray; //客户状态
var globalTypeArray;//客户类别
var moduleId = 0;
$(function () {
    var parms = getUrlParms();
    moduleId = parms["moduleId"];
    initScript();
    initData();
    initStyle();
});

$(window).load(function () {
    hideLoading();
});

var initStyle = function () {
    $(":radio").click(function(){
        searchData();
    });
    $("#kind").combobox({
        onSelect:function(record){
            searchData();
        }
    })
    enterTriggerEvent("searchParamesTable", "searchData");
}
var oldSetGridHeightWidth = setGridHeightWidth;
var setGridHeightWidth = function() {
    oldSetGridHeightWidth(40,170);
    oldSetGridHeightWidth(5, 128);
};
var initScript = function () {
    var v = 0;
    $(window).resize(function() {
        if (v == 0) {
            setTimeout(function() {
                setGridHeightWidth();
                v = 0;
            }, 200)
            v = 1;
        }
        setGridHeightWidth();
        setToolbarHeightWidth();
    });
}
var initData = function () {

    //客户类别
    getDictionaryData({
        dicTypeCode : Global_DicType.Global_DicType_divtype,
        async : false,
        callback : function(callbackData) {
            initTypeControl(callbackData);
            globalTypeArray = callbackData;
        }
    });
    //客户状态
    getDictionaryData({
        dicTypeCode : Global_DicType.Global_DicType_CusStatus,
        async : false,
        callback : function(callbackData) {
            initStatusControl(callbackData);
            globalStatusArray = callbackData;
        }
    });

    loadList();
}


function initTypeControl (jsonData){
    jsonData.unshift({
        dicvalue : "",
        dictext : $.i18n.prop('all')
    });
    $('#type').combobox({
        data : jsonData,
        valueField : 'dicvalue',
        textField : 'dictext',
        panelHeight : 120,
        editable : true,
        formatter : function(row) {
            if (row.dicvalue == "") {
                return '<table style=\"width:100%;\"><tr><td style=\"text-align:left;width:80%;\"><span >'
                    + row.dictext
                    + '</span></td><td style=\"text-align:right;width:20%;\"></td></tr></table>';
            } else {
                return '<table style=\"width:100%;\"><tr><td style=\"text-align:left;width:80%;\"><span >'
                    +row.dicvalue+'-'+row.dictext
                    + '</span></td><td style=\"text-align:right;width:20%;\"></td></tr></table>';
            }
        },
        onLoadSuccess : function(e) {
        }
    });
}


function initStatusControl (jsonData){
    jsonData.unshift({
        dicvalue : "",
        dictext : $.i18n.prop('all')
    });
    $('#status').combobox({
        data : jsonData,
        valueField : 'dicvalue',
        textField : 'dictext',
        panelHeight : 120,
        editable : true,
            formatter : function(row) {
         if (row.dicvalue == "") {
         return '<table style=\"width:100%;\"><tr><td style=\"text-align:left;width:80%;\"><span >'
         + row.dictext
         + '</span></td><td style=\"text-align:right;width:20%;\"></td></tr></table>';
         } else {
         return '<table style=\"width:100%;\"><tr><td style=\"text-align:left;width:80%;\"><span >'
         +row.dicvalue+'-'+row.dictext
         + '</span></td><td style=\"text-align:right;width:20%;\"></td></tr></table>';
         }
         },
        onLoadSuccess : function(e) {
        }
    });
}

function searchData() {
    $("#gridTable").jqGrid('setGridParam', {
        url: getSearchGridUrl(),
        datatype: 'json',
        postData: { "filters": "" },
        page: 1
    }).trigger("reloadGrid");
}



 function getSearchGridUrl() {
    return '../../jcda/customer/findCustomerPageVO?t=' + Math.random() + '&customSearchFilters=' + encodeURI(getRequestParams());
}

function getRequestParams() {
    var parmsArray = [
        { name: 'code', value: $("#code").val(), op: "cn" },
        { name: 'name',value: $("#name").val(), op: "cn" },
        {name : 'status',value : $("[id=status]").combobox('getValue'),op : "eq"},
        {name : 'type',value : $("[id=type]").combobox('getValue'),op : "eq"}
    ];
    return formatSearchParames(parmsArray);
}

function add() {
    openDetail("","");
}

 function edit() {
    var selectRowItems = $("#gridTable").jqGrid("getGridParam", "selarrrow");
    if (selectRowItems.length > 1 || selectRowItems.length == 0) {
        errorNotification({
            SimpleMessage: $.i18n.prop('selectOneDataRowOperator')
        });
        return;
    }

    var rowData = $("#gridTable").jqGrid('getRowData', selectRowItems);
    openDetail(rowData.sn,rowData.code);
}

var openDetail = function (id,code) {
    showLoading();
    var href = "../View/jcda/Cr_CusDetail.html?moduleId=" + moduleId+"&id=" + id+"&code=" + code;
    openDialog({ title: "客户(供应商)明细", id: 'Cr_CusDetail', width:850, height: 600, isResize: true, href: href, closable: true });
}

/*     var remove = function () {
 var selectRowItems = $("#gridTable").jqGrid("getGridParam", "selarrrow");
 if (selectRowItems.length == 0) {
 errorNotification({
 SimpleMessage : "请至少选择一行数据行进行操作"
 });
 return;
 }
 //首先定义一个数组
 var ids = [];
 for (var i = 0; i < selectRowItems.length; i++) {
 var rowData = $("#gridTable").jqGrid('getRowData', selectRowItems[i]);
 ids.push(rowData.sn);
 }
 $.messager.confirm('提示', '确定删除选中的数据项吗?', function(r) {
 if (r) {
 showLoading();
 $.ajax({
 url :  "../../jcda/Scts_Set/delete?t=" + Math.random(),
 data : JSON.stringify(ids),
 type : "POST",
 contentType : 'application/json;charset=utf-8',
 success : function(dataObj) {
 if (isServerResultDataPass(dataObj)) {
 correctNotification({ SimpleMessage: "删除成功" });
 searchData();
 } else {
 FailResultDataToTip(dataObj);
 }
 hideLoading();
 }
 });
 }
 });
 } */

 function remove() {
    var jsonList = [];
    var selectRowItems = $("#gridTable").jqGrid("getGridParam", "selarrrow");
    if (selectRowItems.length == 0) {
        errorNotification({
            SimpleMessage : $.i18n.prop("selectLeastOneDataRowOperator")
        });
        return;
    }
    for ( var i = 0; i < selectRowItems.length; i++) {
        var rowData = $("#gridTable").jqGrid('getRowData',selectRowItems[i]);
        jsonList.push({sn : rowData.sn});
    }
    $.messager.confirm($.i18n.prop("prompt"),$.i18n.prop("deleteData"),
        function(r) {
            if (r) {
                showLoading();
                $.ajax({url : "../../jcda/ZdCus/deleteDetail?t="+ Math.random(),
                    type : "POST",
                    data : "jsonData="
                    + JSON.stringify(jsonList),
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
        });
}

function loadList() {
    $("#gridTable").jqGrid({
        url: getSearchGridUrl(),
        datatype: "json",
        width: "none",
        colNames: ['ID','状态','类型', '公司编号', '公司名称', '税号', '发票地址', '开户银行', '开户账号', '发票电话','省','市','地址','备注','创建人', '创建时间'],
        colModel: [
            { name: 'id', index: 'id', align: 'center', sorttype: 'int', search: false, sortable: false, key: true, width: 40, hidden: true },
            { name: 'status', index : 'status', align : 'center', isSort : false, width : 60, stype : 'select', search : true, searchoptions : { value : formatGridCombobox_Local( globalStatusArray, true),sopt : [ 'eq' ]},
                formatter : function(value, grid, rows) {
                var v = formatGridCombobox_Local(globalStatusArray, true);
                return v[rows.status]; }
                },
            { name: 'type', index : 'type', align : 'center', isSort : false, width : 100, stype : 'select', search : true, searchoptions : { value : { '' : '全部', '1': '重点客户', '2': '一般客户','3':'零散客户'},sopt : [ 'eq' ]}, formatter : function(value, grid, rows) { var v = formatGridCombobox_Local(globalTypeArray, true); return v[rows.type]; } },
            { name: 'code', index: 'code', align: 'center', isSort: false, width: 100, type: 'string',  search: true, searchoptions: { sopt: ['cn', 'eq', 'ne']} },
            { name: 'name', index: 'name', align: 'center', isSort: false, width: 120, type: 'string',  search: true, searchoptions: { sopt: ['cn', 'eq', 'ne']} },
            { name: 'tax', index: 'tax', align: 'center', isSort: false, width: 120, type: 'string',  search: true, searchoptions: { sopt: ['cn', 'eq', 'ne']} },
            { name: 'in_address', index: 'in_address', align: 'center', isSort: false, width: 120, type: 'string',  search: true, searchoptions: { sopt: ['cn', 'eq', 'ne']} },
            { name: 'bank', index: 'bank', align: 'center', isSort: false, width: 120, type: 'string',  search: true, searchoptions: { sopt: ['cn', 'eq', 'ne']} },
            { name: 'bank_no', index: 'bank_no', align: 'center', isSort: false, width: 120, type: 'string',  search: true, searchoptions: { sopt: ['cn', 'eq', 'ne']} },
            { name: 'in_tel', index: 'in_tel', align: 'center', isSort: false, width: 120, type: 'string',  search: true, searchoptions: { sopt: ['cn', 'eq', 'ne']} },
            { name: 'pri', index: 'pri', align: 'center', isSort: false, width: 120, type: 'string',  search: true, searchoptions: { sopt: ['cn', 'eq', 'ne']} },
            { name: 'city', index: 'city', align: 'center', isSort: false, width: 120, type: 'string',  search: true, searchoptions: { sopt: ['cn', 'eq', 'ne']} },
            { name: 'address', index: 'address', align: 'center', isSort: false, width: 120, type: 'string',  search: true, searchoptions: { sopt: ['cn', 'eq', 'ne']} },
            { name: 'remark', index: 'remark', align: 'center', isSort: false, width: 265, type: 'string',  search: true, searchoptions: { sopt: ['cn', 'eq', 'ne']} },
            { name: 'create_by', index: 'create_by', align: 'center', isSort: false, width: 120, type: 'string',  search: true, searchoptions: { sopt: ['cn', 'eq', 'ne']} },
            { name: 'create_date', index: 'create_date', align: 'center', width: 150, isSort: false, type: 'string',  search: true, searchoptions: { sopt: ['eq', 'ne', 'lt', 'le', 'gt', 'ge'], dataEvents: [{ type: 'click', data: {}, fn: function () { WdatePicker(); } }]} }
        ],

        shrinkToFit: false,
        altRows: true,
        altclass: 'gridSpacingClass',
        forceFit: true,
        cellLayout: 0,
        scroll: false,
        autowidth: true,
        sortname: 'id',
        sortorder: "desc",
        loadonce: false,
        mtype: "POST",
        viewrecords: true,
        rownumbers: true,
        multiselect: true,
        rowNum: 15,
        height: "100%",
        rowList: [100, 200, 300, 500],
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
        ondblClickRow: function (id) {
            showChildModule(id);
        },
        loadComplete: function (xhr) {
            $('.gridViewPicLink').tooltip({
                position: 'bottom',
                content: $(this).attr("title"),
                onShow: function () {
                    $(this).tooltip('tip').css({
                        backgroundColor: '#FCF8E3',
                        borderColor: '#FAEBCC'
                    });
                }
            });
            FailResultDataToTip(xhr);
        }
    });

    $("#gridTable").jqGrid('navGrid', '#gridPager', { add: false, edit: false, del: false, refresh: true.sear }, {}, {}, {}, { multipleSearch: true, closeOnEscape: true, closeAfterSearch: true });
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
    setGridHeightWidth();
}

function showChildModule(sn) {
    var rowData = $("#gridTable").jqGrid('getRowData', sn);
    openDetail(rowData.sn,rowData.code);
};

 function refreshCallerData_ZdCus() {
    searchData();
}

