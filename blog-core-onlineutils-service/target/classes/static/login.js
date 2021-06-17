
var layer=layui.layer;

function getAppropriateWindowSize(){
    var innerWidth = window.innerWidth;//褰撳墠绐楀彛瀹藉害
    var widthPercent='30%';
    var heightPercent='50%';
    if(innerWidth >=1300 && innerWidth <= 1400){ //绗旇鏈�
        widthPercent='40%';
        heightPercent='70%';
    }else if(innerWidth >= 1900 && innerWidth <= 2000){//鍙板紡鏈�
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
 * 鐧诲綍
 * */
$("#util-login").click(function () {

    layer.open({
        type: 2,
        title:'登录',
        area: getAppropriateWindowSize(), //瀹介珮
        content: 'https://www.sunyuchao.com/login/login'
    });
});

/**
 * 鐧诲綍
 * */
$("#util-regist").click(function () {
    layer.open({
        type: 2,
        title:'注册',
        area: getAppropriateWindowSize(), //瀹介珮
        content: 'https://www.sunyuchao.com/login/regist'
    });
});



/**
 * 鍙嶉
 * */
$("#util-feedback").click(function () {

    layer.open({
        type: 2,
        title:'在线反馈',
        area: getAppropriateWindowSize(), //瀹介珮
        content: 'https://www.sunyuchao.com/feedback'
    });
});

/*淇敼瀵嗙爜*/
$("#util-modifyPass").click(function () {
    layer.open({
        type: 2,
        title:'蹇樿瀵嗙爜',
        area: getAppropriateWindowSize(), //瀹介珮
        content: 'https://www.sunyuchao.com/login/forgotPassword'
    });
});
