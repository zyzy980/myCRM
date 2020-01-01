/* www.jq22.com */
(function ($, undefined) {
    $.fn.zyUpload = function (options, param) {
    	
    	/*if(options.onComplete){
    		var _onComplete = options.onComplete;
    		options.onComplete = function(){
    			console.log("开始");
    			_onComplete.apply(this, arguments);
    		}
    	}*/
    	
    	
        var otherArgs = Array.prototype.slice.call(arguments, 1);
        if (typeof options == 'string') {
            var fn = this[0][options];
            if ($.isFunction(fn)) {
                return fn.apply(this, otherArgs);
            } else {
                throw ("No such method: " + options);
            }
        }

        return this.each(function () {
            var para = {};    // 保留参数
            var self = this;  // 保存组件对象
            //		    
            var defaults = {
                //					width            : "700px",  					// 宽度
                //					height           : "400px",  					// 宽度
                itemWidth: "140px",                     // 文件项的宽度
                itemHeight: "120px",                     // 文件项的高度
                url: "/upload/UploadAction",  	// 上传文件的路径
                multiple: true,  						// 是否可以多个文件上传
                dragDrop: true,  						// 是否可以拖动上传文件
                del: true,  						// 是否可以删除文件
                finishDel: false,  						// 是否在上传文件完成后删除预览
                //onlyView         : false,                       // 是否只支持预览
                //imgViewId:"", // 预览大图片id
                close: true,                         // 是否需要关闭按钮
                fileTypeFilter: [],
                remark: "",
                isDownModuleFile: true,
                downModuleFileUrl: "",
                sourceFiles: [], //初始化
                /* 提供给外部的接口方法 */
                onSelect: function (selectFiles, files) { }, // 选择文件的回调方法  selectFile:当前选中的文件  allFiles:还没上传的全部文件
                onDelete: function (file, files) { },     // 删除一个文件的回调方法 file:当前删除的文件  files:删除之后的文件
                onSuccess: function (file) { },            // 文件上传成功的回调方法
                onFailure: function (file) { },            // 文件上传失败的回调方法
                onComplete: function (responseInfo) { },    // 上传完成的回调方法
                onDownModuleFile: function () { }
            };

            para = $.extend(defaults, options);

            this.init = function () {
                if ($.trim($(self).html()) == "") {
                    this.createHtml();  // 创建组件html
                    this.createCorePlug();  // 调用核心js
                }
            };

            /**
            * 功能：创建上传所使用的html
            * 参数: 无
            * 返回: 无
            */
            this.createHtml = function () {
                var multiple = "";  // 设置多选的参数
                para.multiple ? multiple = "multiple" : multiple = "";
                var html = '';
                //			    if (para.onlyView) {
                //			            html += '	<div class="upload_box">';
                //			            html += '	<div class="upload_box_close"></div>'; // 关闭按钮
                //			            html += '		<div class="upload_main">';
                //			            html += '			<div id="preview" class="upload_preview"></div>';
                //			            html += '		</div>';
                //			            html += '	</div>';
                //                        $(self).append(html).css("display","block");
                //                        if (para.sourceFiles.length <= 0) {
                //                            $(".upload_preview").css({ "height": "100px", "text-align": "center", "line-height": "100px", "font-size": "14px" }).html("没有任何附件");
                //                        } else {
                //                            
                //                        }
                //			    } else {
                if (para.dragDrop) {
                    // 创建带有拖动的html
                    html += '<form id="uploadForm" action="' + para.url + '" method="post" enctype="multipart/form-data">';
                    html += '   <div style="display:none"><input type="text" id="filenamelist" name="filenamelist" value="" /></div>';
                    html += '	<div class="upload_box">';
                    html += '	<div class="upload_box_close"></div>'; // 关闭按钮
                    html += '		<div class="upload_main">';
                    html += '			<div class="upload_choose">';
                    html += '				<div class="convent_choice">';
                    html += '					<div class="andArea">';
                    html += '						<div class="filePicker">点击选择文件</div>';
                    html += '						<input id="fileImage" type="file" size="30" name="fileselect[]" ' + multiple + '>';
                    html += '					</div>';
                    html += '				</div>';

                    if (para.remark) {
                        if (para.isDownModuleFile) {
                            html += '				<span id="fileDragArea" class="downLoadModuleSpan" style="padding-top:20px"><p>可将文件拖到此处</p><p>文件大小不要超过5M</p>' + para.remark + '<p class="downLoadModuleA" ">下载上传模板</p>' + '</span>';
                        } else {
                            html += '				<span id="fileDragArea" class="downLoadModuleSpan" style="padding-top:20px"><p>可将文件拖到此处</p><p>文件大小不要超过5M</p>' + para.remark + '</span>';
                        }
                    } else {
                        if (para.isDownModuleFile) {
                            html += '				<span id="fileDragArea" class="downLoadModuleSpan" style="padding-top:20px"><p>可将文件拖到此处</p><p>文件大小不要超过5M</p><p class="downLoadModuleA"">下载上传模板</p>' + '</span>';
                        } else {
                            html += '				<span id="fileDragArea" class="downLoadModuleSpan" style="padding-top:20px"><p>可将文件拖到此处</p><p>文件大小不要超过5M</p></span>';
                        }
                    }

                    html += '			</div>';
                    html += '			<div class="status_bar">';
                    html += '				<div id="status_info" class="info">选中0张文件，共0B。</div>';
                    html += '				<div class="btns">';
                    html += '					<div class="webuploader_pick">继续选择</div>';
                    html += '					<div class="upload_btn" onclick="overrideUploadClick()">开始上传</div>';
                    html += '				</div>';
                    html += '			</div>';
                    html += '			<div id="preview" class="upload_preview"></div>';
                    html += '		</div>';
                    html += '		<div class="upload_submit">';
                    html += '			<button type="button" id="fileSubmit" class="upload_submit_btn">确认上传文件</button>';
                    html += '		</div>';
                    html += '		<div id="uploadInf" class="upload_inf"></div>';
                    html += '	</div>';
                    html += '</form>';
                } else {
                    var imgWidth = parseInt(para.itemWidth.replace("px", "")) - 15;

                    // 创建不带有拖动的html
                    html += '<form id="uploadForm" action="' + para.url + '" method="post" enctype="multipart/form-data">';
                    html += '  <div style="display:none"><input type="text" id="filenamelist" name="filenamelist" value="" /></div>';
                    html += '	<div class="upload_box">';
                    html += '		<div class="upload_main single_main">';
                    html += '			<div class="status_bar">';
                    html += '				<div id="status_info" class="info">选中0张文件，共0B。</div>';
                    html += '				<div class="btns">';
                    html += '					<input id="fileImage" type="file" size="30" name="fileselect[]" ' + multiple + '>';
                    html += '					<div class="webuploader_pick">选择文件</div>';
                    html += '					<div class="upload_btn" onclick="overrideUploadClick()">开始上传</div>';
                    html += '				</div>';
                    html += '			</div>';
                    html += '			<div id="preview" class="upload_preview">';
                    html += '				<div class="add_upload">';
                    html += '					<a style="height:' + para.itemHeight + ';width:' + para.itemWidth + ';" title="点击添加文件" id="rapidAddImg" class="add_imgBox" href="javascript:void(0)">';
                    html += '						<div class="uploadImg" style="width:' + imgWidth + 'px">';
                    html += '							<img class="upload_image" src="../../Resource/js/fileUpload/images/add_img.png" style="width:expression(this.width > ' + imgWidth + ' ? ' + imgWidth + 'px : this.width)" />';
                    html += '						</div>';
                    html += '					</a>';
                    html += '				</div>';
                    html += '			</div>';
                    html += '		</div>';
                    html += '		<div class="upload_submit">';
                    html += '			<button type="button" id="fileSubmit" class="upload_submit_btn">确认上传文件</button>';
                    html += '		</div>';
                    html += '		<div id="uploadInf" class="upload_inf"></div>';
                    html += '	</div>';
                    html += '</form>';
                }

                $(self).append(html).css("display", "block");
                //			    }


                //$(self).append(html).css({"width":para.width,"height":para.height});

                // 初始化html之后绑定按钮的点击事件
                this.addEvent();
            };

            /**
            * 功能：点击下载模板事件
            * 参数: 无
            * 返回: 无
            */
            this.funDownModuleFile = function () {
                //                $("#downModuleForm").attr("action", para.downModuleFileUrl);
                //$("#downModuleForm").submit();
                if (para.downModuleFileUrl) {
                    window.location.href = para.downModuleFileUrl;
                } else {
                    para.onDownModuleFile();
                }
            }

            /**
            * 功能：显示统计信息和绑定继续上传和上传按钮的点击事件
            * 参数: 无
            * 返回: 无
            */
            this.funSetStatusInfo = function (files) {
                var size = 0;
                var num = files.length;
                $.each(files, function (k, v) {
                    // 计算得到文件总大小
                    size += v.size;
                });

                // 转化为kb和MB格式。文件的名字、大小、类型都是可以现实出来。
                if (size > 1024 * 1024) {
                    size = (Math.round(size * 100 / (1024 * 1024)) / 100).toString() + 'MB';
                } else {
                    size = (Math.round(size * 100 / 1024) / 100).toString() + 'KB';
                }

                // 设置内容
                $("#status_info").html("选中" + num + "张文件，共" + size + "。");
            };

            /**
            * 功能：过滤上传的文件格式等
            * 参数: files 本次选择的文件
            * 返回: 通过的文件
            */
            this.funFilterEligibleFile = function (files) {
                var arrFiles = [];  // 替换的文件数组
                for (var i = 0, file; file = files[i]; i++) {
                    if (file.size >= 51200000) {
                        alert('文件："' + file.name + '"文件大小过大');
                        return arrFiles;
                    }
                    if (para.fileTypeFilter.indexOf(file.name.split('.')[1]) < 0) {
                        alert('文件："' + file.name + '"不是有效上传格式');
                        return arrFiles;
                    }
                }

                for (var i = 0, file; file = files[i]; i++) {
                    // 在这里需要判断当前所有文件中
                    arrFiles.push(file);
                }

                return arrFiles;
            };

            /**
            * 功能： 处理参数和格式上的预览html
            * 参数: files 本次选择的文件
            * 返回: 预览的html
            */
            this.funDisposePreviewHtml = function (file, e) {
                var html = "";
                var imgWidth = parseInt(para.itemWidth.replace("px", "")) - 15;

                // 处理配置参数删除按钮
                var delHtml = "";
                if (para.del) {  // 显示删除按钮
                    delHtml = '<span class="file_del" data-index="' + file.index + '" title="删除"></span>';
                }

                // 处理不同类型文件代表的图标
                var fileImgSrc = "/Resource/js/fileUpload/images/fileType/";
                if (file.type.indexOf("rar") > 0) {
                    fileImgSrc = fileImgSrc + "rar.png";
                } else if (file.type.indexOf("zip") > 0) {
                    fileImgSrc = fileImgSrc + "zip.png";
                } else if (file.type.indexOf("text") > 0) {
                    fileImgSrc = fileImgSrc + "txt.png";
                } else {
                    fileImgSrc = fileImgSrc + "file.png";
                }


                // 图片上传的是图片还是其他类型文件
                if (file.type.indexOf("image") == 0) {
                    html += '<div id="uploadList_' + file.index + '" class="upload_append_list">';
                    html += '	<div class="file_bar">';
                    html += '		<div style="padding:5px;">';
                    html += '			<p class="file_name">' + file.name + '</p>';
                    html += delHtml;   // 删除按钮的html
                    html += '		</div>';
                    html += '	</div>';
                    html += '	<a style="height:' + para.itemHeight + ';width:' + para.itemWidth + ';" href="#" class="imgBox">';
                    html += '		<div class="uploadImg" style="width:' + imgWidth + 'px">';
                    html += '			<img id="uploadImage_' + file.index + '" class="upload_image" src="../..' + e.target.result + '" style="width:expression(this.width > ' + imgWidth + ' ? ' + imgWidth + 'px : this.width)" />';
                    html += '		</div>';
                    html += '	</a>';
                    html += '	<p id="uploadProgress_' + file.index + '" class="file_progress"></p>';
                    html += '	<p id="uploadFailure_' + file.index + '" class="file_failure">上传失败，请重试</p>';
                    html += '	<p id="uploadSuccess_' + file.index + '" class="file_success"></p>';
                    html += '</div>';

                } else {
                    html += '<div id="uploadList_' + file.index + '" class="upload_append_list">';
                    html += '	<div class="file_bar">';
                    html += '		<div style="padding:5px;">';
                    html += '			<p class="file_name">' + file.name + '</p>';
                    html += delHtml;   // 删除按钮的html
                    html += '		</div>';
                    html += '	</div>';
                    html += '	<a style="height:' + para.itemHeight + ';width:' + para.itemWidth + ';" href="#" class="imgBox">';
                    html += '		<div class="uploadImg" style="width:' + imgWidth + 'px">';
                    html += '			<img id="uploadImage_' + file.index + '" class="upload_image" src="../../' + fileImgSrc + '" style="width:expression(this.width > ' + imgWidth + ' ? ' + imgWidth + 'px : this.width)" />';
                    html += '		</div>';
                    html += '	</a>';
                    html += '	<p id="uploadProgress_' + file.index + '" class="file_progress"></p>';
                    html += '	<p id="uploadFailure_' + file.index + '" class="file_failure">上传失败，请重试</p>';
                    html += '	<p id="uploadSuccess_' + file.index + '" class="file_success"></p>';
                    html += '</div>';
                }

                return html;
            };

            //            this.funDisposePreviewHtmlSource = function(){
            //            var sourceFiles=para.sourceFiles;
            //				var html = "";
            //				var imgWidth = parseInt(para.itemWidth.replace("px", ""))-15;
            //				// 处理配置参数删除按钮
            //				var delHtml = "";
            //                for (var i = 0; i < sourceFiles.length; i++) {
            //                    
            //                }
            //				if(para.del){  // 显示删除按钮
            //					delHtml = '<span class="file_del" data-index="'+file.index+'" title="删除"></span>';
            //				}
            //				
            //				// 处理不同类型文件代表的图标
            //				var fileImgSrc = "/Resource/js/fileUpload/images/fileType/";
            //				if(file.type.indexOf("rar") > 0){
            //					fileImgSrc = fileImgSrc + "rar.png";
            //				}else if(file.type.indexOf("zip") > 0){
            //					fileImgSrc = fileImgSrc + "zip.png";
            //				}else if(file.type.indexOf("text") > 0){
            //					fileImgSrc = fileImgSrc + "txt.png";
            //				}else{
            //					fileImgSrc = fileImgSrc + "file.png";
            //				}
            //				
            //				
            //				// 图片上传的是图片还是其他类型文件
            //				if (file.type.indexOf("image") == 0) {
            //					html += '<div id="uploadList_'+ file.index +'" class="upload_append_list">';
            //					html += '	<div class="file_bar">';
            //					html += '		<div style="padding:5px;">';
            //					html += '			<p class="file_name">' + file.name + '</p>';
            //					html += delHtml;   // 删除按钮的html
            //					html += '		</div>';
            //					html += '	</div>';
            //					html += '	<a style="height:'+para.itemHeight+';width:'+para.itemWidth+';" href="#" class="imgBox">';
            //					html += '		<div class="uploadImg" style="width:'+imgWidth+'px">';				
            //					html += '			<img id="uploadImage_'+file.index+'" class="upload_image" src="' + e.target.result + '" style="width:expression(this.width > '+imgWidth+' ? '+imgWidth+'px : this.width)" />';                                                                 
            //					html += '		</div>';
            //					html += '	</a>';
            //					html += '	<p id="uploadProgress_'+file.index+'" class="file_progress"></p>';
            //					html += '	<p id="uploadFailure_'+file.index+'" class="file_failure">上传失败，请重试</p>';
            //					html += '	<p id="uploadSuccess_'+file.index+'" class="file_success"></p>';
            //					html += '</div>';
            //                	
            //				}else{
            //					html += '<div id="uploadList_'+ file.index +'" class="upload_append_list">';
            //					html += '	<div class="file_bar">';
            //					html += '		<div style="padding:5px;">';
            //					html += '			<p class="file_name">' + file.name + '</p>';
            //					html += delHtml;   // 删除按钮的html
            //					html += '		</div>';
            //					html += '	</div>';
            //					html += '	<a style="height:'+para.itemHeight+';width:'+para.itemWidth+';" href="#" class="imgBox">';
            //					html += '		<div class="uploadImg" style="width:'+imgWidth+'px">';				
            //					html += '			<img id="uploadImage_'+file.index+'" class="upload_image" src="' + fileImgSrc + '" style="width:expression(this.width > '+imgWidth+' ? '+imgWidth+'px : this.width)" />';                                                                 
            //					html += '		</div>';
            //					html += '	</a>';
            //					html += '	<p id="uploadProgress_'+file.index+'" class="file_progress"></p>';
            //					html += '	<p id="uploadFailure_'+file.index+'" class="file_failure">上传失败，请重试</p>';
            //					html += '	<p id="uploadSuccess_'+file.index+'" class="file_success"></p>';
            //					html += '</div>';
            //				}
            //				
            //				return html;
            //			};

            /**
            * 功能：调用核心插件
            * 参数: 无
            * 返回: 无
            */
            this.createCorePlug = function () {
                var params = {
                    fileInput: $("#fileImage").get(0),
                    uploadInput: $("#fileSubmit").get(0),
                    dragDrop: $("#fileDragArea").get(0),
                    url: $("#uploadForm").attr("action"),

                    filterFile: function (files) {
                        // 过滤合格的文件
                        return self.funFilterEligibleFile(files);
                    },
                    onSelect: function (selectFiles, allFiles) {
                        para.onSelect(selectFiles, allFiles);  // 回调方法
                        self.funSetStatusInfo(ZYFILE.funReturnNeedFiles());  // 显示统计信息
                        var html = '', i = 0;
                        // 组织预览html
                        var funDealtPreviewHtml = function () {
                            file = selectFiles[i];
                            if (file) {
                                var reader = new FileReader()
                                reader.onload = function (e) {
                                    // 处理下配置参数和格式的html
                                    html += self.funDisposePreviewHtml(file, e);

                                    i++;
                                    // 再接着调用此方法递归组成可以预览的html
                                    funDealtPreviewHtml();
                                }
                                reader.readAsDataURL(file);
                            } else {
                                // 走到这里说明文件html已经组织完毕，要把html添加到预览区
                                funAppendPreviewHtml(html);
                            }
                        };

                        // 添加预览html
                        var funAppendPreviewHtml = function (html) {
                            // 添加到添加按钮前
                            if (para.dragDrop) {
                                $("#preview").append(html);
                            } else {
                                $(".add_upload").before(html);
                            }
                            // 绑定删除按钮
                            funBindDelEvent();
                            funBindHoverEvent();
                            //						    funBindMaxViewEvent();
                        };

                        // 绑定删除按钮事件
                        var funBindDelEvent = function () {
                            if ($(".file_del").length > 0) {
                                // 删除方法
                                $(".file_del").click(function () {
                                    ZYFILE.funDeleteFile(parseInt($(this).attr("data-index")), true);
                                    return false;
                                });
                            }

                            if ($(".file_edit").length > 0) {
                                // 编辑方法
                                $(".file_edit").click(function () {
                                    // 调用编辑操作
                                    //ZYFILE.funEditFile(parseInt($(this).attr("data-index")), true);
                                    return false;
                                });
                            }
                        };

                        //                        // 绑定点击预览大图片
                        //						var funBindMaxViewEvent = function(){
                        //							$(".upload_append_list").click(
                        //								function (e) {
                        //                                // 查询子元素为img
                        //                                    $("#"+para.imgViewId).empty();
                        //								    var imgItem = $(this).find("img");
                        //                                    var maxViewImgWidth = parseInt($("#"+para.imgViewId).css("width").replace("px", ""))-50;
                        //                                    //<div maxView="wdith:' + para.maxViewWidth + ';height:' + para.maxViewHeight + '">';
                        //                                    var  maxViewHtml = '<div class="maxView">';
                        //                                    maxViewHtml += '<div class="closeMaxView"></div>';
                        //								    maxViewHtml += '<img class="maxViewImg" src="' + imgItem.attr("src") + '"/>';
                        //                                    maxViewHtml += '</div>';
                        //                                    $("#"+para.imgViewId).append(maxViewHtml).css({"display":"block"});
                        //                                    $(".maxViewImg").css({"width":maxViewImgWidth+"px"});
                        //                                    // 绑定关闭
                        //								    $(".closeMaxView").unbind("click").click(function() {
                        //								        $("#"+para.imgViewId).empty();
                        //								        $("#" + para.imgViewId).css("display","none");
                        //								    });
                        //								}
                        //							);
                        //						};


                        // 绑定显示操作栏事件
                        var funBindHoverEvent = function () {
                            $(".upload_append_list").hover(
								function (e) {
								    $(this).find(".file_bar").addClass("file_hover");
								}, function (e) {
								    $(this).find(".file_bar").removeClass("file_hover");
								}
							);
                        };

                        funDealtPreviewHtml();
                    },
                    onDelete: function (file, files) {
                        // 移除效果
                        $("#uploadList_" + file.index).fadeOut();
                        // 重新设置统计栏信息
                        self.funSetStatusInfo(files);
                        console.info("剩下的文件");
                        console.info(files);
                    },
                    onProgress: function (file, loaded, total) {
                        var eleProgress = $("#uploadProgress_" + file.index), percent = (loaded / total * 100).toFixed(2) + '%';
                        if (eleProgress.is(":hidden")) {
                            eleProgress.show();
                        }
                        eleProgress.css("width", percent);
                    },
                    onSuccess: function (file, response) {
                        $("#uploadProgress_" + file.index).hide();
                        $("#uploadSuccess_" + file.index).show();
                        // $("#uploadInf").append("<p>上传成功，文件地址是：" + response + "</p>");
                        // 根据配置参数确定隐不隐藏上传成功的文件
                        if (para.finishDel) {
                            // 移除效果
                            $("#uploadList_" + file.index).fadeOut();
                            // 重新设置统计栏信息
                            self.funSetStatusInfo(ZYFILE.funReturnNeedFiles());
                        }
                    },
                    onFailure: function (file) {
                        $("#uploadProgress_" + file.index).hide();
                        $("#uploadSuccess_" + file.index).show();
                        $("#uploadInf").append("<p>文件" + file.name + "上传失败！</p>");
                        //$("#uploadImage_" + file.index).css("opacity", 0.2);
                    },
                    onComplete: function (response) {
                        para.onComplete(response);
                    },
                    onDragOver: function () {
                        $(this).addClass("upload_drag_hover");
                    },
                    onDragLeave: function () {
                        $(this).removeClass("upload_drag_hover");
                    }

                };

                ZYFILE = $.extend(ZYFILE, params);
                ZYFILE.init();
            };

            /**
            * 功能：绑定事件
            * 参数: 无
            * 返回: 无
            */
            this.addEvent = function () {
                // 如果快捷添加文件按钮存在
                if ($(".filePicker").length > 0) {
                    // 绑定选择事件
                    $(".filePicker").bind("click", function (e) {
                        $("#fileImage").click();
                    });
                }

                // 绑定继续添加点击事件
                $(".webuploader_pick").bind("click", function (e) {
                    $("#fileImage").click();
                });

                // 如果快捷添加文件按钮存在
                if ($("#rapidAddImg").length > 0) {
                    // 绑定添加点击事件
                    $("#rapidAddImg").bind("click", function (e) {
                        $("#fileImage").click();
                    });
                }

                // 关闭按钮
                if (para.close) {
                    $(".upload_box_close").bind("click", function (e) {
                        ZYFILE.destroy();
                        $(self).empty();
                        $(self).css("display", "none");
                        if($(".window-mask").length>0)
                        	$(".window-mask").css("display", "none");
                    });
                }

                //点击下载事件
                $(".downLoadModuleA").bind("click", function (e) {
                    self.funDownModuleFile();
                });
            };
            // 初始化上传控制层插件
            this.init();
        });
    };
})(jQuery);

