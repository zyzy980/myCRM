(function ($) {
    jQuery.fn.extend({
        uploadPreview: function (opts) {
            opts = jQuery.extend({
//                width: 0,
//                height: 0,
//                max_width: 0,
//                max_height: 0,
                imgPreview: null,
                imgType: ["gif", "jpeg", "jpg", "bmp", "png"],
                callback: function () { return false; }
            }, opts || {});

            var _self = this;
            var _this = $(this);
            var imgPreview = $(opts.imgPreview);
            //������ʽ 
            autoScaling = function () {
                //imgPreview.css({ "margin-left": 0, "margin-top": 0, "width": opts.width, "height": opts.height, "max-width": opts.max_width, "max-height": opts.max_height });
                //imgPreview.css({ "margin-left": 0, "margin-top": 0, "width": opts.width, "height": opts.height, "max-width": opts.max_width, "max-height": opts.max_height });
                imgPreview.show();
            }
            //file��ť�����¼� 
            _this.change(function () {
                if (this.value) {
                    if (!RegExp("\.(" + opts.imgType.join("|") + ")$", "i").test(this.value.toLowerCase())) {
                        alert("ͼƬ���ͱ�����" + opts.imgType.join("��") + "�е�һ��");
                        this.value = "";
                        return false;
                    }
                    if ($.browser.msie) {//�ж�ie 
                        var path = $(this).val();
                        if (/"\w\W"/.test(path)) {
                            path = path.slice(1, -1);
                        }
                        imgPreview.attr("src", path);
                        imgPreview.css({ "margin-left": 0, "margin-top": 0, "width": opts.width, "height": opts.height });
                        setTimeout("autoScaling()", 100);
                    }
                    else {
                        if ($.browser.version < 7) {
                            imgPreview.attr('src', this.files.item(0).getAsDataURL());
                        }
                        else {
                            oFReader = new FileReader(), rFilter = /^(?:image\/bmp|image\/cis\-cod|image\/gif|image\/ief|image\/jpeg|image\/jpeg|image\/jpeg|image\/pipeg|image\/png|image\/svg\+xml|image\/tiff|image\/x\-cmu\-raster|image\/x\-cmx|image\/x\-icon|image\/x\-portable\-anymap|image\/x\-portable\-bitmap|image\/x\-portable\-graymap|image\/x\-portable\-pixmap|image\/x\-rgb|image\/x\-xbitmap|image\/x\-xpixmap|image\/x\-xwindowdump)$/i;
                            oFReader.onload = function (oFREvent) {
                                imgPreview.attr('src', oFREvent.target.result);
                            };
                            var oFile = this.files[0];
                            oFReader.readAsDataURL(oFile);
                        }
                        imgPreview.css({ "margin-left": 0, "margin-top": 0, "width": opts.width, "height": opts.height });
                        setTimeout("autoScaling()", 100);
                    }
                }
                opts.callback();
            });
        }
    });
})(jQuery);