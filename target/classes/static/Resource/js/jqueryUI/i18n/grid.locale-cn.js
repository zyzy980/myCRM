;(function($){
/**
 * jqGrid Chinese Translation for v3.6
 * waiting 2010.01.18
 * http://waiting.javaeye.com/
 * Dual licensed under the MIT and GPL licenses:
 * http://www.opensource.org/licenses/mit-license.php
 * http://www.gnu.org/licenses/gpl.html
 * 
 * update 2010.05.04
 *		add double u3000 SPACE for search:odata to fix SEARCH box display err when narrow width from only use of eq/ne/cn/in/lt/gt operator under IE6/7
**/
var lang = "zh";
var parms = getUrlParms();
if(null!=parms["lang"] && parms["lang"]!=undefined)
{
	lang = parms["lang"];
}

if(lang=="en")
{
//英文
	$.jgrid = {
			defaults : {
				recordtext: "{0} - {1}\u3000total {2} row",
				emptyrecords: "no data",
				loadtext: "reading...",
				pgtext : " {0} total {1} page"
			},
			search : {
				caption: "search...",
				Find: "find",
				Reset: "reset",
				odata : ['equal\u3000\u3000', 'no equal\u3000\u3000', 'less\u3000\u3000', 'less than or equal to','greater\u3000\u3000','greater than or equal to', 
					'begin','no begin','belong\u3000\u3000','no belong','end','no end','contain\u3000\u3000','no contain'],
				groupOps: [	{ op: "AND", text: "ALL" },	{ op: "OR",  text: "any" }	],
				matchText: " match",
				rulesText: " rule"
			},
			edit : {
				addCaption: "add record",
				editCaption: "edit record",
				bSubmit: "submit",
				bCancel: "cancel",
				bClose: "close",
				saveData: "data is change,is save?",
				bYes : "Yes",
				bNo : "No",
				bExit : "Cancel",
				msg: {
					required:"This column is need",
					number:"Please enter a valid number",
					minValue:"Value must be greater than or equal to",
					maxValue:"Value must be less than or equal to ",
					email: "E-mail invalid",
					integer: "Please enter a valid integer",
					date: "Please enter a valid time",
					url: "URL valid.Prefix must be('http://' 或 'https://')",
					nodefined : " undefined！",
					novalue : " need return value！",
					customarray : "The custom function needs to return to the array！",
					customfcheck : "Custom function should be present in case of custom checking!"
					
				}
			},
			view : {
				caption: "view log",
				bClose: "close"
			},
			del : {
				caption: "delete",
				msg: "delete selected record？",
				bSubmit: "delete",
				bCancel: "cancel"
			},
			nav : {
				edittext: "",
				edittitle: "edit selected record",
				addtext:"",
				addtitle: "Add new record",
				deltext: "",
				deltitle: "Delete selected record",
				searchtext: "",
				searchtitle: "find",
				refreshtext: "",
				refreshtitle: "reflush grid table",
				alertcap: "Attention",
				alerttext: "Plaese select record",
				viewtext: "",
				viewtitle: "View selected record"
			},
			col : {
				caption: "selec column",
				bSubmit: "Determine",
				bAll: "Check",
				bAllno: "Uncheck",
				bCancel: "Cancel"
			},
			errors : {
				errcap : "error",
				nourl : "no set url",
				norecords: "no deal with record",
				model : "colNames and colModel len no equal to！"
			},
			formatter : {
				integer : {thousandsSeparator: " ", defaultValue: '0'},
				number : {decimalSeparator:".", thousandsSeparator: " ", decimalPlaces: 2, defaultValue: '0.00'},
				currency : {decimalSeparator:".", thousandsSeparator: " ", decimalPlaces: 2, prefix: "", suffix:"", defaultValue: '0.00'},
				date : {
					dayNames:   [
						"Sun", "Mon", "Tue", "Wed", "Thr", "Fri", "Sat",
				         "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
					],
					monthNames: [
						"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",
						"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
					],
					AmPm : ["am","pm","AM","PM"],
					S: function (j) {return j < 11 || j > 13 ? ['st', 'nd', 'rd', 'th'][Math.min((j - 1) % 10, 3)] : 'th'},
					srcformat: 'Y-m-d',
					newformat: 'm-d-Y',
					masks : {
						ISO8601Long:"Y-m-d H:i:s",
						ISO8601Short:"Y-m-d",
						ShortDate: "Y/j/n",
						LongDate: "l, F d, Y",
						FullDateTime: "l, F d, Y g:i:s A",
						MonthDay: "F d",
						ShortTime: "g:i A",
						LongTime: "g:i:s A",
						SortableDateTime: "Y-m-d\\TH:i:s",
						UniversalSortableDateTime: "Y-m-d H:i:sO",
						YearMonth: "F, Y"
					},
					reformatAfterEdit : false
				},
				baseLinkUrl: '',
				showAction: '',
				target: '',
				checkbox : {disabled:true},
				idName : 'id'
			}
		};
	//英文结束
}
else 
{
//中文
$.jgrid = {
	defaults : {
		recordtext: "{0} - {1}\u3000共 {2} 条",	// 共字前是全角空格
		emptyrecords: "无数据显示",
		loadtext: "读取中...",
		pgtext : " {0} 共 {1} 页"
	},
	search : {
		caption: "搜索...",
		Find: "查找",
		Reset: "重置",
		odata : ['等于\u3000\u3000', '不等\u3000\u3000', '小于\u3000\u3000', '小于等于','大于\u3000\u3000','大于等于', 
			'开始于','不开始于','属于\u3000\u3000','不属于','结束于','不结束于','包含\u3000\u3000','不包含'],
		groupOps: [	{ op: "AND", text: "所有" },	{ op: "OR",  text: "任一" }	],
		matchText: " 匹配",
		rulesText: " 规则"
	},
	edit : {
		addCaption: "添加记录",
		editCaption: "编辑记录",
		bSubmit: "提交",
		bCancel: "取消",
		bClose: "关闭",
		saveData: "数据已改变，是否保存？",
		bYes : "是",
		bNo : "否",
		bExit : "取消",
		msg: {
			required:"此字段必需",
			number:"请输入有效数字",
			minValue:"输值必须大于等于 ",
			maxValue:"输值必须小于等于 ",
			email: "这不是有效的e-mail地址",
			integer: "请输入有效整数",
			date: "请输入有效时间",
			url: "无效网址。前缀必须为 ('http://' 或 'https://')",
			nodefined : " 未定义！",
			novalue : " 需要返回值！",
			customarray : "自定义函数需要返回数组！",
			customfcheck : "Custom function should be present in case of custom checking!"
			
		}
	},
	view : {
		caption: "查看记录",
		bClose: "关闭"
	},
	del : {
		caption: "删除",
		msg: "删除所选记录？",
		bSubmit: "删除",
		bCancel: "取消"
	},
	nav : {
		edittext: "",
		edittitle: "编辑所选记录",
		addtext:"",
		addtitle: "添加新记录",
		deltext: "",
		deltitle: "删除所选记录",
		searchtext: "",
		searchtitle: "查找",
		refreshtext: "",
		refreshtitle: "刷新表格",
		alertcap: "注意",
		alerttext: "请选择记录",
		viewtext: "",
		viewtitle: "查看所选记录"
	},
	col : {
		caption: "选择列",
		bSubmit: "确定",
		bAll: "全选",
		bAllno: "反选",
		bCancel: "取消"
	},
	errors : {
		errcap : "错误",
		nourl : "没有设置url",
		norecords: "没有要处理的记录",
		model : "colNames 和 colModel 长度不等！"
	},
	formatter : {
		integer : {thousandsSeparator: " ", defaultValue: '0'},
		number : {decimalSeparator:".", thousandsSeparator: " ", decimalPlaces: 2, defaultValue: '0.00'},
		currency : {decimalSeparator:".", thousandsSeparator: " ", decimalPlaces: 2, prefix: "", suffix:"", defaultValue: '0.00'},
		date : {
			dayNames:   [
				"Sun", "Mon", "Tue", "Wed", "Thr", "Fri", "Sat",
		         "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
			],
			monthNames: [
				"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",
				"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
			],
			AmPm : ["am","pm","AM","PM"],
			S: function (j) {return j < 11 || j > 13 ? ['st', 'nd', 'rd', 'th'][Math.min((j - 1) % 10, 3)] : 'th'},
			srcformat: 'Y-m-d',
			newformat: 'm-d-Y',
			masks : {
				ISO8601Long:"Y-m-d H:i:s",
				ISO8601Short:"Y-m-d",
				ShortDate: "Y/j/n",
				LongDate: "l, F d, Y",
				FullDateTime: "l, F d, Y g:i:s A",
				MonthDay: "F d",
				ShortTime: "g:i A",
				LongTime: "g:i:s A",
				SortableDateTime: "Y-m-d\\TH:i:s",
				UniversalSortableDateTime: "Y-m-d H:i:sO",
				YearMonth: "F, Y"
			},
			reformatAfterEdit : false
		},
		baseLinkUrl: '',
		showAction: '',
		target: '',
		checkbox : {disabled:true},
		idName : 'id'
	}
};
//中文结束
}
})(jQuery);
