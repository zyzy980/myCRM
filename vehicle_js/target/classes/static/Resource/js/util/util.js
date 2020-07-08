/**
 * 校验只要是数字（包含正负整数，0以及正负浮点数）就返回true
 * 
 */
var isNumber = function(val) {
	var regPos = /^\d+(\.\d+)?$/; // 非负浮点数
	var regNeg = /^(-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*)))$/; // 负浮点数
	if (regPos.test(val) || regNeg.test(val)) {
		return true;
	} else {
		return false;
	}
}

/**
 * 校验正负正数就返回true
 * 
 */
var isInteger = function(val) {
	var regPos = / ^\d+$/; // 非负整数
	var regNeg = /^\-[1-9][0-9]*$/; // 负整数
	if (regPos.test(val) || regNeg.test(val)) {
		return true;
	} else {
		return false;
	}
}

/**
 * 将页面表单字段转换为对象
 * 
 */
var fieldToObj = function() {
	var $fields = $(".field");

	var obj = {};

	$.each($fields, function() {
		var name = $(this).attr("name");
		var val = $(this).val();
		obj[name] = val;
	});
	return obj;
}

/**
 * 对象值转换
 * 
 */
var copyObjField = function(oldObj, newObj) {
	for ( var key in newObj) {
		oldObj[key] = newObj[key];
	}
	return oldObj;
}

/**
 * 对象值转换转换时间格式
 * 
 */
var dateFormat = function(date) {
	var day = date.getDate() > 9 ? date.getDate() : "0" + date.getDate();
	var month = (date.getMonth() + 1) > 9 ? (date.getMonth() + 1) : "0"
			+ (date.getMonth() + 1);
	
	var hour  = date.getHours()>9? date.getHours() : "0"+ date.getHours();
	
	var minute = date.getMinutes()>9? date.getMinutes() : "0"+ date.getMinutes();
	
	var second = date.getSeconds()>9? date.getSeconds() : "0"+ date.getSeconds();
	
	var time = date.getFullYear() + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
	
	return  time;
}

/**
 * 表单字段转换对象
 */
var formFieldToObj = function() {

}

/**
 * 赋值
 * 
 * @param i
 *            当前字段索引
 * @param field
 *            所有字段数组
 * @param value
 *            值
 * @param obj
 *            上级对象
 */
var assignment = function(i, field, value, obj) {

	if (i == (field.length - 1)) {
		if (isEmpty(value)) {
			var key = field[i];
			obj[key] = value;
		}
		return;
	}

	var key = field[i];

	if (!obj[key]) {
		obj[key] = {};
	}

	assignment(i + 1, field, value, obj[key]);
}

var isEmpty = function(val) {
	if (val != null && val != "" && val != undefined) {
		return true;
	}
	return false;
}