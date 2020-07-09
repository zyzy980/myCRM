$.extend($.fn.validatebox.defaults.rules, {  
	
	    la : {//手机号码校验  
	        validator: function(value, param){ 
	        	
	            return /^-?(([1-9]\d?)|(1[1-7]\d)|180)(\.\d{1,6})?$/.test(value);  
	        },  
	        message: '请输入正确的经度'  
	    },  
	    lo : {//手机号码校验  
	        validator: function(value, param){ 
	        	
	            return /^-?(([1-8]\d?)|([1-8]\d)|90)(\.\d{1,6})?$/.test(value);  
	        },  
	        message: '请输入正确的纬度'  
	    },  
	    myEmail : {//邮箱校验，避免使用email和默认的冲突  
	        validator: function(value, param){  
	            return checkEmail(value);  
	        },  
	        message: '请输入正确的邮箱'  
	    },  
	    loginName : {//登录名，数字、英文字母或者下划线  
	        validator: function(value, param){  
	            return checkLoginName(value);  
	        },  
	        message: '只能输入数字、英文字母或者下划线'  
	    },  
	    telePhone : {//座机，区号及分机号可有可无  
	        validator: function(value, param){  
	            return checkTelePhone(value);  
	        },  
	        message: '请输入正确的座机号码'  
	    },  
	    chinese : {//  
	        validator: function(value, param){  
	            return checkChinese(value);  
	        },  
	        message: '只能输入中文汉字'  
	    },  
	    number : {//正整数，包括0（00，01非数字）  
	        validator: function(value, param){  
	            return isNumber(value);  
	        },  
	        message: '只能输入数字（01非数字）'  
	    },  
	    numberText : {//数字组成的字符串，如000222，22220000，00000  
	        validator: function(value, param){  
	            return isNumberText(value);  
	        },  
	        message: '只能输入数字字符串'  
	    },  
	    idCardNo : {//身份证  
	        validator: function(value, param){  
	            return isIdCardNo(value);  
	        },  
	        message: '请输入正确的身份证号码'  
	    },  
	    money : {//金额  
	        validator: function(value, param){  
	            return isFloat(value);  
	        },  
	        message: '请输入正确的数字'  
	    },  
	    floatNumber : {//数字（包括正整数、0、浮点数）  
	        validator: function(value, param){  
	            return isFloat(value);  
	        },  
	        message: '请输入正确的数字'  
	    },  
	    minLength: {  
	        validator: function(value, param){  
	            return value.length >= param[0];  
	        },  
	        message: '请输入至少 {0}个字符。'  
	    },  
	    maxLength: {  
	        validator: function(value, param){  
	            return value.length <= param[0];  
	        },  
	        message: '不能输入超过{0}个字符。'  
	    }  
	});

function checkPhone(value){
}