/**
 * Created by Administrator on 2017/9/11.
 */
$(function () {
    //    回车键实现登录
    $(document).on('keydown', function (event) {
        if (event.keyCode == 13) {
            document.getElementById("login_btn").click();
        }
    });

    //  用户名、密码验证
    var user_name = $("#user_name");
    var user_password = $("#user_password");

    function nameCheck() {
        user_name.val($.trim(user_name.val()));
        var firm_val = user_name.val();
        if (firm_val == "") {
            user_name.next().html("用户名不能为空!");
            return false;
        } else {
            user_name.next().html("");
            return true;
        }
    }
    user_name.blur(nameCheck);

    function passwordCheck() {
        user_password.val($.trim(user_password.val()));
        var firm_val = user_password.val();
        if (firm_val == "") {
            user_password.next().html("密码不能为空!");
            return false;
        } else {
            user_password.next().html("");
            return true;
        }
    }
    user_password.blur(passwordCheck);


    $("#login_btn").on("click", function () {
        var name = nameCheck();
        var password = passwordCheck();
        var userName = user_name.val();
        var userPassword = user_password.val();
		
        if (name && password) {
            $.ajax({
                type: 'post',
                url: login,
                data: {
                    user_name: userName,
                    password: userPassword
                },
                xhrFields: {
                    withCredentials: true
                },
                crossDomain: true,
                success: function (data) {
                    
                    if (data.state == 1) {
                        sessionStorage.setItem('userInfo', JSON.stringify(data.data.userInfo));
                        /* 测试数据导出开始 */
                        // data.data.nav_data.data.push({ name: "数据导出", className: "data-export", menu_id: "F_00", url: "data-export.html" })
                        /* 测试数据导出结束 */
                        sessionStorage.setItem('nav_data', JSON.stringify(data.data.nav_data.data));
                        switch (data.data.userInfo.type) {
                            case '1': //管理员
                                window.location.href = "case_select.html";
                                break;
                            case '2': //专家
                                window.location.href = "expert-review.html";
                                break;
                            case '3': //普通
                                window.location.href = "index.html";
                                break;
                        };

                    } else {
                        $(".user_password .info").html(data.message);
                    }
                }
            });
        }
    });

    //显示/隐藏密码
    $('.show-password').on('mousedown',function(){
        $('#user_password').attr('type','text')
    }).on('mouseup',function(){
        $('#user_password').attr('type','password')
    })
});