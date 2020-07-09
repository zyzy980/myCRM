// 导入
var importFile = function(fromUrl,fileTypeFilter,remark,downTemplateUrl) {
	// showLoading();
	var url = fromUrl+"?t="
			+ Math.random();
	$("#fileUpload")
			.zyUpload(
					{
						itemWidth : "60px", // 文件项的宽度
						itemHeight : "60px", // 文件项的高度
						url : url,
						multiple : false, // 是否可以多个文件上传
						dragDrop : true, // 是否可以拖动上传文件
						del : true, // 是否可以删除文件
						finishDel : false, // 是否在上传文件完成后删除预览
						close : true,
						fileTypeFilter : fileTypeFilter,
						remark : remark,
						isDownModuleFile : true,
						downModuleFileUrl : downTemplateUrl,
						/* 外部获得的回调接口 */
						onSelect : function(files, allFiles) { // 选择文件的回调方法
						},
						onDelete : function(file, surplusFiles) { // 删除一个文件的回调方法
						},
						onFailure : function(file) { // 文件上传失败的回调方法
						},
						onComplete : function(responseInfo) { // 上传完成的回调方法
							hideLoading();
							var dataObj = JSON.parse(responseInfo);
							if (isServerResultDataPass(dataObj)) {
								correctNotification(dataObj.resultDataFull);
								afterComplete();
							} else {
								FailResultDataToTip(dataObj);
							}
							$("#fileUpload").empty();
							$("#fileUpload").hide();
							
						}
					});
};