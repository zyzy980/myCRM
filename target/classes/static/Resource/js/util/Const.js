/**
 * 初始化前半部分
 */

var CONST = {
	URL : {
		RELATIVE : "../../",
		SERVICE : "service",
		MODELNAME : {
			ORDER : "/order/",
			PLANEXEC : "/bussiness/planExec/",
			DICTIONARY : "/dictionary/"
		},
		OPTION : {
			PLANEXEC : {
				FINDBYPOJO : "findByPOJO",
				UPDATEPLANEXEC : "updatePlanExec"
			},
			ORDER : {
				SAVE : "save",
				DELETEORDERDETAIL : "deleteOrderDetail"
			},
			DICTIONARY : {
				FINDTRUCKTYPE : "findTruckType"
			}
		}
	},
	HTTP : {
		METHOD : {
			POST : "POST",
			GET : "GET",
			PUT : "PUT",
			DELETE : "DELETE"
		}
	},
	JS : {
		EVENT : {
			CLICK : "click",
			CHANGE : "change",
			DBLCLICK: "dblclick",
			INPUT  : "input",
			FOCUS : "focus"
		}
	}
}

var getRequest = function(modelName, option) {
	var requestURL = CONST.URL.RELATIVE + CONST.URL.SERVICE + modelName;
	return requestURL + option + "?t=" + Math.random();
}
