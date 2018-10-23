/**
 * Created by Administrator on 2017/9/19.
 */
$(function () {
    var pwdOld = $("#pwdOld");
    var pwdNew = $("#pwdNew");
    var pwdNew2 = $("#pwdNew2");

    //原密码验证
    function oldCheck() {
        var people_val = pwdOld.val();
        if (people_val == "") {
            pwdOld.next().html("原密码不能为空!");
            return false;
        } else {
            pwdOld.next().html("");
            return true;
        }
    }
    pwdOld.blur(oldCheck);

    function newCheck() {
        var pass_val = pwdNew.val();
        var regPhone = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,20}$/;
        if (regPhone.test(pass_val)) {
            pwdNew.next().html("");
            return true;
        } else if (!$.trim(pass_val)) {
            pwdNew.next().html("密码不能为空!");
            return false;
        } else {
            pwdNew.next().html("密码为8-20位数字与字母组合!");
            return false;
        }
    }
    pwdNew.blur(newCheck);

    //重新输入新密码验证
    function newCheck2() {
        var pwdRepeat = ((pwdNew.val()) === (pwdNew2.val()) ? true : false);
        if (pwdRepeat) {
            pwdNew2.next().html("");
            return true;
        } else {
            pwdNew2.next().html("两次密码输入不同！");
            return false;
        }
    }
    pwdNew2.blur(newCheck2);

    //未登录判断
    if (!sessionStorage.getItem('userInfo')) {
        new Dialog().init({  
            mask: true,
            title: '',
            content: '未登录或登录失效！',
            isAlert: true,
            hideView:true,
            callback: function (boolean) {
                if (boolean) {
                    top.location.href = loginUrl;
                }
            }
        });
    } else {
        $("#con_change").on("click", function () {
            var old = oldCheck();
            var connewCheck = newCheck();
            var connewCheck2 = newCheck2();
            if (old && connewCheck && connewCheck2) {
                $.ajax({
                    type: 'post',
                    url: password_cUrl,
                    data: {
                        oldPassword: pwdOld.val(),
                        newPassword: pwdNew.val()
                    },
                    xhrFields: {
                        withCredentials: true
                    },
                    crossDomain: true,
                    success: function (data) {
                        //console.log(data);
                        if (data.state == 1) {
                            $(".change_info").html("密码修改成功，3秒后自动跳到登录页！");
                            pwdOld.val("");
                            pwdNew.val("");
                            pwdNew2.val("");
                            var timer = setTimeout(function () {
                                top.location.href = loginUrl;
                                clearTimeout(timer);
                            }, 3000);
                        } else if (data.state == 2) {

                            new Dialog().init({
                                mask: true,
                                title: '',
                                content: data.message,
                                isAlert: true,
                                hideView:true,
                                callback: function (boolean) {
                                    if (boolean) {
                                        top.location.href = loginUrl;
                                    }
                                }
                            });
                        } else {
                            $(".change_info").html(data.message);
                        }
                    }
                })
            }
        });
    }

});