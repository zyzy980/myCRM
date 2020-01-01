/**
 * Created by Administrator on 2018/1/10.
 * form by �úú�����
 * email 1570302023@qq.com
 *
 *
 *
 */


function initImgData(){
    index = 0;
    $('.bot-img ul li').click(function(){
        var _this=$(this);
        _this.addClass('active').siblings('li').removeClass('active');
        var int=_this.index();
        $('.activeimg').animate({left:int*-500},"slow");
    });
    var list=$('.bot-img ul li').length;
    $('.activeimg').css({
        width:list*500,
    });
    $('.right').unbind("click").click(function(){
        next(list)

    })
    $('.left').unbind("click").click(function(){
        prev(list)
    });
}
$(function(){

})
var index=0;
function next(list){
    if(index<list-1){
        index++;
        $('.activeimg').animate({left:index*-500},"slow");
        $('.bot-img ul li').eq(index).addClass('active').siblings('li').removeClass('active')
    }else{
        index=0;
        $('.activeimg').animate({left:index*-522},"slow");
        $('.bot-img ul li').eq(index).addClass('active').siblings('li').removeClass('active')
    }
}
function prev(list){
    index--;
    if(index<0){
        index=list-1;
        $('.activeimg').animate({left:index*-500},"slow");
        $('.bot-img ul li').eq(index).addClass('active').siblings('li').removeClass('active')
    }else{
        $('.activeimg').animate({left:index*-500},"slow");
        $('.bot-img ul li').eq(index).addClass('active').siblings('li').removeClass('active')
    }
}


