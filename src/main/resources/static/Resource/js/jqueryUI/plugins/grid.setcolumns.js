;(function($){
/**
 * jqGrid extension for manipulating columns properties
 * Piotr Roznicki roznicki@o2.pl
 * http://www.roznicki.prv.pl
 * Dual licensed under the MIT and GPL licenses:
 * http://www.opensource.org/licenses/mit-license.php
 * http://www.gnu.org/licenses/gpl-2.0.html
**/
$.jgrid.extend({
	setColumns : function(p) {
		p = $.extend({
			top : 0,
			left: 0,
			width: 200,
			height: 'auto',
			dataheight: 'auto',
			modal: false,
			drag: true,
			beforeShowForm: null,
			afterShowForm: null,
			afterSubmitForm: null,
			closeOnEscape : true,
			ShrinkToFit : false,
			jqModal : false,
			saveicon: [true,"left","ui-icon-disk"],
			closeicon: [true,"left","ui-icon-close"],
			onClose : null,
			colnameview : true,
			closeAfterSubmit : true,
			updateAfterCheck : false,
			recreateForm : false
		}, $.jgrid.col, p ||{});
		return this.each(function(){
			var $t = this;
			if (!$t.grid ) { return; }
			var onBeforeShow = typeof p.beforeShowForm === 'function' ? true: false;
			var onAfterShow = typeof p.afterShowForm === 'function' ? true: false;
			var onAfterSubmit = typeof p.afterSubmitForm === 'function' ? true: false;			
			var gID = $t.p.id,
			dtbl = "ColTbl_"+gID,
			IDs = {themodal:'colmod'+gID,modalhead:'colhd'+gID,modalcontent:'colcnt'+gID, scrollelm: dtbl};
			if(p.recreateForm===true && $("#"+IDs.themodal).html() != null) {
				$("#"+IDs.themodal).remove();
			}
			if ( $("#"+IDs.themodal).html() != null ) {
				if(onBeforeShow) { p.beforeShowForm($("#"+dtbl)); }
				$.jgrid.viewModal("#"+IDs.themodal,{gbox:"#gbox_"+gID,jqm:p.jqModal, jqM:false, modal:p.modal});
				if(onAfterShow) { p.afterShowForm($("#"+dtbl)); }
			} else {
				var dh = isNaN(p.dataheight) ? p.dataheight : p.dataheight+"px";
				var formdata = "<div id='"+dtbl+"' class='formdata' style='width:100%;overflow:auto;position:relative;height:"+dh+";'>";
				formdata += "<table class='ColTable' cellspacing='1' cellpading='2' border='0'><tbody>";
				for(i=0;i<this.p.colNames.length;i++){
					if(!$t.p.colModel[i].hidedlg) { // added from T. Tomov
						formdata += "<tr><td style='white-space: pre;'><input type='checkbox' style='margin-right:5px;' id='col_" + this.p.colModel[i].name + "' class='cbox' value='T' " + 
						((this.p.colModel[i].hidden===false)?"checked":"") + "/>" +  "<label for='col_" + this.p.colModel[i].name + "'>" + this.p.colNames[i] + ((p.colnameview) ? " (" + this.p.colModel[i].name + ")" : "" )+ "</label></td></tr>";
					}
				}
				formdata += "</tbody></table></div>"
				var bA  ="<a href='javascript:void(0)' id='aData' class='fm-button ui-state-default ui-corner-all fm-button-icon-left'><span>"+p.bAll+"</span><span class='ui-icon ui-icon-check'></span></a>";
				var bS  = !p.updateAfterCheck ? "<a href='javascript:void(0)' id='dData' class='fm-button ui-state-default ui-corner-all'>"+p.bSubmit+"</a>" : "",
				bC  ="<a href='javascript:void(0)' id='eData' class='fm-button ui-state-default ui-corner-all'>"+p.bCancel+"</a>";
				formdata += "<table border='0' class='EditTable' id='"+dtbl+"_2'><tbody><tr style='display:block;height:3px;'><td></td></tr><tr><td class='DataTD ui-widget-content'></td></tr><tr><td class='ColButton EditButton'>"+bA+bS+"&#160;"+bC+"</td></tr></tbody></table>";
				p.gbox = "#gbox_"+gID;
				$.jgrid.createModal(IDs,formdata,p,"#gview_"+$t.p.id,$("#gview_"+$t.p.id)[0]);
				if(p.saveicon[0]==true) {
					$("#dData","#"+dtbl+"_2").addClass(p.saveicon[1] == "right" ? 'fm-button-icon-right' : 'fm-button-icon-left')
					.append("<span class='ui-icon "+p.saveicon[2]+"'></span>");
				}
				if(p.closeicon[0]==true) {
					$("#eData","#"+dtbl+"_2").addClass(p.closeicon[1] == "right" ? 'fm-button-icon-right' : 'fm-button-icon-left')
					.append("<span class='ui-icon "+p.closeicon[2]+"'></span>");
				}
				if(!p.updateAfterCheck) {
					$("#dData","#"+dtbl+"_2").click(function(e){
						for(i=0;i<$t.p.colModel.length;i++){
							if(!$t.p.colModel[i].hidedlg) { // added from T. Tomov
								var nm = $t.p.colModel[i].name.replace(/\./g, "\\.");
								if($("#col_" + nm,"#"+dtbl).attr("checked")) {
									$($t).jqGrid("showCol",$t.p.colModel[i].name);
									$("#col_" + nm,"#"+dtbl).attr("defaultChecked",true); // Added from T. Tomov IE BUG
								} else {
									$($t).jqGrid("hideCol",$t.p.colModel[i].name);
									$("#col_" + nm,"#"+dtbl).attr("defaultChecked",""); // Added from T. Tomov IE BUG
								}
							}
						}
						if(p.ShrinkToFit===true) {
							$($t).jqGrid("setGridWidth",$t.grid.width-0.001,true);
						}
						if(p.closeAfterSubmit) $.jgrid.hideModal("#"+IDs.themodal,{gb:"#gbox_"+gID,jqm:p.jqModal, onClose: p.onClose});
						if (onAfterSubmit) { p.afterSubmitForm($("#"+dtbl)); }
						return false;
					});
				} else {
					$(":input","#"+dtbl).click(function(e){
						var cn = this.id.substr(4);
						if(cn){
							if(this.checked) {
								$($t).jqGrid("showCol",cn);
							} else {
								$($t).jqGrid("hideCol",cn);
							}
							if(p.ShrinkToFit===true) {
								$($t).jqGrid("setGridWidth",$t.grid.width-0.001,true);
							}
						}
						return this;
					});
				}
				$("#eData", "#"+dtbl+"_2").click(function(e){
					$.jgrid.hideModal("#"+IDs.themodal,{gb:"#gbox_"+gID,jqm:p.jqModal, onClose: p.onClose});
					return false;
				});
				
				$("#aData").click(function(e){
					console.log('$t.p.colModel');
					console.log($t.p.colModel);
					if($("#ColTbl_"+gID+" table input[type='checkbox']").not("input:checked").length>0)
					{
						$(this).find("span").eq(0).html(p.bAllno);
						$("#ColTbl_"+gID+" table input[type='checkbox']").attr("checked",true);
						for(var i=0,ilen=$t.p.colModel.length;i<ilen;i++)
						{
							if($t.p.colModel[i].name!="rn")
								$("#"+gID).jqGrid("showCol", $t.p.colModel[i].name);
						}
							
					}
					else {
						$(this).find("span").eq(0).html(p.bAll);
						$("#ColTbl_"+gID+" table input[type='checkbox']").attr("checked",false); 
						for(var i=0,ilen=$t.p.colModel.length;i<ilen;i++)
						{
							if($t.p.colModel[i].name!="rn")
								$("#"+gID).jqGrid("hideCol", $t.p.colModel[i].name);
						}
							
					}
				});
				
				$("#dData, #eData","#"+dtbl+"_2").hover(
				   function(){$(this).addClass('ui-state-hover');}, 
				   function(){$(this).removeClass('ui-state-hover');}
				);
				
				if(onBeforeShow) { p.beforeShowForm($("#"+dtbl)); }
				$.jgrid.viewModal("#"+IDs.themodal,{gbox:"#gbox_"+gID,jqm:p.jqModal, jqM: true, modal:p.modal});
				if(onAfterShow) { p.afterShowForm($("#"+dtbl)); }
			}
		});
	}
});

    $.jgrid.extend({


        //取消所有选中的行
        unSelectRowItems : function(){
            var _this = $(this);
            var selectRowItems = _this.jqGrid("getGridParam", "selarrrow");
            for(var i = selectRowItems.length - 1; i >= 0 ; i--){
                _this.jqGrid("setSelection", selectRowItems[i]);
            }
            return _this;
        },
        //该rowid是否被选中
        isSelectRowItems : function(rowid){
            var trObject = $(this).find("tr[id="+rowid+"]");
            return trObject.attr("aria-selected") == "true";
        },
        //滚动到最后,或者指定位置
        scrollToLast : function(rowid){
            var _this = $(this);
            var div = _this.closest('.ui-jqgrid-bdiv')[0];
            if(rowid == null){
                div.scrollTop = div.scrollHeight;
            }else{
                var index = _this.find('[id='+rowid+']').index();
                var len = $('#gridTable tr[id]').length;
                if(len > 0){
                    var size = div.scrollHeight/len * (index - 1);
                    if(size < 0 ){
                        size = 0;
                    }
                    div.scrollTop = size;
                }else{
                    div.scrollTop = div.scrollHeight;
                }
            }
            return _this;
        },
        loseGridFocus : function(){
            $(this).find(".edit-cell:has(input[type=text])").each(function(){
                var saveTr = $(this).parents("tr").index();
                var saveTd = $(this).index();
                $(this).parents(".ui-jqgrid-btable").jqGrid('saveCell', saveTr, saveTd);
                $(this).removeClass("edit-cell").removeClass("ui-state-highlight");
            });
        },
        //默认新增
        common_add : function(obj){
            var key = null;
            var dataObj = null;
            var isFirstClick = false;
            if(typeof obj == "object"){
                if(obj.key){
                    key = obj.key;
                }
                if(typeof obj.data == "object"){
                    dataObj = obj.data;
                }
                if(obj.isFirstClick == false){
                    isFirstClick = false;
                }
            }


            var $this = $(this);
            $this.loseGridFocus();
            if(!key){
                var colModels = $this.jqGrid("getGridParam")["colModel"];
                for(var i = 2; i < colModels.length; i++){
                    if(!colModels[i].hidden){
                        key = colModels[i].name;
                        break;
                    }
                }
            }

            var ids = $this.jqGrid('getDataIDs');
            var newIds = [];
            var re = /^[0-9]+$/;
            for(var i = 0; i < ids.length; i++){
                if(re.test(ids[i])){
                    newIds.push(ids[i]);
                }
            }
            var rowid = Math.max.apply(Math, newIds);
            if (rowid == "-Infinity"){
                rowid = 0;
            }else{
                rowid += 1;
            }

            var dataRow = null;
            if(dataObj){
                dataRow = dataObj;
            }else{
                dataRow = {};
            }
            dataRow.operator = "add";
            $this.jqGrid("addRowData", rowid, dataRow, "last");
            $this.unSelectRowItems();
            $this.jqGrid("setSelection", rowid);
            $this.scrollToLast();
            if(isFirstClick){
                $this.find("#"+rowid+" td[aria-describedby="+$this.attr("id")+"_"+key+"]").click();
            }
        }
    });
})(jQuery);