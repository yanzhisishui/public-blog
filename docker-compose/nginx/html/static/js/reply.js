/**
 * 点击表情
 * */
function pick(obj){
    var parent = $(obj).parent();
    var prev = $(obj).parent().prev().parent().prev();
    $(obj).removeAttr("onclick");//放到div里面移除点击事件
    $(prev).append(obj);
    $(parent).hide();
}

/**
 * 模板填充
 * */
function formatTemplate(dta, tmpl) {
    var format = {
        name: function(x) {
            return x ;
        }
    };
    return tmpl.replace(/{(\w+)}/g, function(m1, m2) {
        if (!m2)
            return "";
        return (format && format[m2]) ? format[m2](dta[m2]) : dta[m2];
    });
}

/**
 * 展开回复楼层
 * */
function show(obj) {
    var next = $(obj).parent().parent().next();
    var nextTwo = $(obj).parent().parent().next().next();
    var right=$(obj).parent().next().children()[0];
    var text = $(obj).html();
    if(text == "展开"){
        $(next).slideDown();
        $(obj).html("关闭");
    }else{
        $(next).slideUp();
        $(obj).html("展开");
        $(right).html("回复");
        $(nextTwo).slideUp();//关闭二级回复列表的时候，关闭评论框
    }
}


/**
 * 打开一级评论
 * */
function openReply(obj){
    var next = $(obj).parent().parent().parent().find(".item-child-smbx");
    //点击一级回复关闭子回复按钮
    $(obj).parent().parent().next().find(".operate .muted a").each(function () {
        if($(this).html() == "取消回复"){
            $(this).html("回复");
        }
    });

    var text = $(obj).html();
    if(text == "回复"){
        $(next).slideDown();
        $(obj).html("取消回复");
    }else{
        $(next).slideUp();
        $(obj).html("回复");
    }

    var receiver = $(obj).parent().parent().parent().find(".receiver");
    var willReply = $(obj).parent().parent().prev().prev().find(".floor");
    var username = $(willReply).html();
    $(receiver).html(username);//更新要回复的对象名
}


/**
 * 打开二级评论
 * */
function openChildReply(obj){
    var next = $(obj).parent().parent().parent().parent().parent().next();
    var prev = $(obj).parent().parent().parent().parent().parent().prev();
    var text = $(obj).html();
    //关闭当前楼层其他回复按钮
    $(next).prev().find(".msg-history .muted a").each(function () {
        if($(this).html() == "取消回复"){
            $(this).html("回复");
        }
    });
    //关闭父楼层回复
    $(prev).find(".muted a").each(function () {
        if($(this).html() == "取消回复"){
            $(this).html("回复");
        }
    });

    if(text == "回复"){
        $(next).slideDown();
        $(obj).html("取消回复");
    }else{
        $(next).slideUp();
        $(obj).html("回复");
    }

    var willReply = $(obj).parent().parent().parent().find(".comment");
    var willReplyUserId = $(obj).parent().parent().parent().find(".userId");

    var username = $(willReply).html();
    var userId = willReplyUserId.val();//要回复的用户id
    var receiver = next.find(".receiver");
    var receiverUserId = next.find(".receiverUserId");
    $(receiver).html(username);//更新要回复的对象名
    $(receiverUserId).val(userId);//更新要回复的对象id

}