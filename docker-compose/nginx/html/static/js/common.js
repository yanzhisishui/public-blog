
var layer=layui.layer;

function getAppropriateWindowSize(){
    var innerWidth = window.innerWidth;//当前窗口宽度
    var widthPercent='30%';
    var heightPercent='50%';
    if(innerWidth >=1300 && innerWidth <= 1400){ //笔记本
        widthPercent='40%';
        heightPercent='70%';
    }else if(innerWidth >= 1900 && innerWidth <= 2000){//台式机
        widthPercent='30%';
        heightPercent='50%';
    }else if(innerWidth >= 780 && innerWidth <= 1080){
        widthPercent='60%';
        heightPercent='50%';
        if(window.innerHeight >= 1100){
            heightPercent='30%';
        }
    }
    var arr =[];
    arr.push(widthPercent,heightPercent);
    return arr;
}
/**
 * 登录
 * */
$("#login").click(function () {

    layer.open({
        type: 2,
        title:'登录',
        area: getAppropriateWindowSize(), //宽高
        content: '/login/login'
    });
});

/**
 * 登录
 * */
$("#regist").click(function () {
    layer.open({
        type: 2,
        title:'注册',
        area: getAppropriateWindowSize(), //宽高
        content: '/login/regist'
    });
});

/**
 * 滚动到顶部
 * */
$("#toTop").hide();
$(window).scroll(function () {
    if ($(window).scrollTop() > 100) {
        $("#toTop").fadeIn(500);
    }
    else {
        $("#toTop").fadeOut(500);
    }
});

/**
 * 分享框
 * */
$("#share").click(function () {
    $("#share-div").css("position", "fixed");
    $("#share-div").css("top", $("#share").offset().top+$("#share").width()/4);
    $("#share-div").css("left", $("#share").offset().left-$("#share-div").width());
    if($("#share-div").is(":hidden")){
        $("#share-div").fadeIn();
    }else{
        $("#share-div").fadeOut();
    }
});

$("#toTop a").click(function () {
    $('body,html').animate({scrollTop: 0}, 1000);
    return false;
});
/**
 * 反馈
 * */
$("#feedback").click(function () {

    layer.open({
        type: 2,
        title:'在线反馈',
        area: getAppropriateWindowSize(), //宽高
        content: '/feedback'
    });
});

/*修改密码*/
$("#modifyPass").click(function () {
    layer.open({
        type: 2,
        title:'忘记密码',
        area: getAppropriateWindowSize(), //宽高
        content: '/login/forgotPassword'
    });
});

$("#myMessage,#personCenter").click(function () {
    layer.alert("抱歉。。。该功能暂未开放");
});


/**
 * 加载全部表情
 * */
function loadFace(obj){
    var faceBook = $(obj).next();
    if($(faceBook).children().length == 0){
        for(var i= 0;i<48;i++){
            var path='https://www.sunyuchao.com/static/layui/images/face/'+i+'.gif';
            $(faceBook).append("<img onclick='pick(this)' src='"+path+"'>");
        }
    }
    $(faceBook).is(":hidden") ?  $(faceBook).fadeIn() :  $(faceBook).fadeOut();
}
