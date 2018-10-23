/**
 * Created by Administrator on 2017/9/11.
 */
$(function () {
    //获取34省
    var area_id; //区域ID
    var userFirm = $("#userFirm");
    var userPeople = $("#userPeople");
    var userAddre = $("#userAddre");
    var userName = $("#userName");
    var userPhone = $("#userPhone");
    var userEmail = $("#userEmail");
    var userCode = $("#userCode");
    var registerAddre = $('#register_addre');

    var password = $("#password");
    var password2 = $("#password2");

    getAllProvince();
    function getAllProvince() {

        $.ajax({
            type: "post",
            url: getAllProvinceUrl,
            data: {},
            xhrFields: {
                withCredentials: true
            },
            crossDomain: true,
            success: function (data) {
                if (data.state == 2) {

                    new Dialog().init({
                        mask: true,
                        title: '',
                        content: data.message,
                        isAlert: true,
                        hideView: true,
                        callback: function (boolean) {
                            if (boolean) {
                                top.location.href = loginUrl;
                            }
                        }
                    });
                }
                var list = data.data;
                //Start-将北京放置在第一位

                (function () {
                    for (var i = 0; i < list.length; i++) {
                        if (list[i].area_name == '北京') {
                            var deleteItem = list.splice(i, 1)
                            list.unshift(deleteItem[0]);
                        }
                    }
                    console.log(list)
                })();


                //End-将北京放置在第一位

                var ProvinceHtml = "";
                for (var i = 0; i < list.length; i++) {
                    if (i == 0) {
                        ProvinceHtml += '<option selected="selected" value=' + list[0].area_id + '>' + list[0].area_name + '</option>'
                    } else {
                        ProvinceHtml += '<option value=' + list[i].area_id + '>' + list[i].area_name + '</option>'
                    }
                }
                $("#register_addre").html(ProvinceHtml);
                area_id = $("#register_addre").find("option:selected").val()
            },
            error: function (err) {
                console.log('请求省份失败', JSON.stringify(err, null, 4))
            }
        });
    }

    $("#register_addre").on("change", function () {
        area_id = $(this).val();
    });

    //所在单位验证

    function checkRegisterAddre() {
        if (registerAddre.val() == '' || registerAddre.val() == null) {
            registerAddre.next().html("请选择所在省市!");
            return false;
        }
        return true;
    }

    //邮箱验证
    function emailCheck() {
        userEmail.val($.trim(userEmail.val()));
        var reg = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
        if (!$.trim(userEmail.val())) {
            userEmail.next().html("邮箱不能为空!");
            return false;
        } else if (reg.test(userEmail.val())) {
            userEmail.next().html("");
            return true;
        } else {
            userEmail.next().html("邮箱格式不正确!");
            return false;
        }
    }
    userEmail.blur(emailCheck);

    //密码验证
    function passCheck() {
        password.val($.trim(password.val()));
        var pass_val = password.val();
        var regPhone = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,20}$/;
        if (regPhone.test(pass_val)) {
            password.next().html("");
            return true;
        } else if (!$.trim(pass_val)) {
            password.next().html("密码不能为空!（不允许空格）");
            return false;
        } else {
            password.next().html("密码为8-20位数字与字母组合（不允许空格）!");
            return false;
        }
    }
    password.blur(passCheck);

    //重新输入新密码验证
    function passCheck2() {
        password2.val($.trim(password2.val()));
        var pwdRepeat = ((password.val()) === (password2.val()) ? true : false);
        if (pwdRepeat) {
            password2.next().html("");
            return true;
        } else {
            password2.next().html("两次密码输入不同！");
            return false;
        }
    }
    password2.blur(passCheck2);

    //单位名称验证
    function firmCheck() {
        userFirm.val($.trim(userFirm.val()));
        var firm_val = userFirm.val();
        if (firm_val == "") {
            userFirm.next().html("单位名称不能为空!");
            return false;
        } else {
            userFirm.next().html("");
            return true;
        }
    }
    userFirm.blur(firmCheck);

    //单位负责人验证
    function propleCheck() {
        userPeople.val($.trim(userPeople.val()));
        var people_val = userPeople.val();
        if (people_val == "") {
            userPeople.next().html("单位负责人不能为空!");
            return false;
        } else {
            userPeople.next().html("");
            return true;
        }
    }
    userPeople.blur(propleCheck);

    //单位注册地址验证
    function addreCheck() {
        userAddre.val($.trim(userAddre.val()));
        var addre_val = userAddre.val();
        if (addre_val == "") {
            userAddre.next().html("单位注册地址不能为空!");
            return false;
        } else {
            userAddre.next().html("");
            return true;
        }
    }
    userAddre.blur(addreCheck);

    //联系人姓名验证
    function nameCheck() {
        userName.val($.trim(userName.val()));
        var mane_val = userName.val();
        if (mane_val == "") {
            userName.next().html("申报联系人不能为空!");
            return false;
        } else {
            userName.next().html("");
            return true;
        }
    }
    userName.blur(nameCheck);

    // 手机号验证
    function phoneCheck() {
        userPhone.val($.trim(userPhone.val()));
        var phone_val = userPhone.val();
        var regPhone = /(^(0[0-9]{2,3}\-)?([2-9][0-9]{6,7})+(\-[0-9]{1,4})?$)|(^((\+86)|0|86|17951)?(1[3|4|5|7|8]\d{9}$))/;
        if (regPhone.test(phone_val)) {
            userPhone.next().html("");
            return true;
        } else if (!$.trim(phone_val)) {
            userPhone.next().html("联系电话不能为空!");
            return false;
        } else {
            userPhone.next().html("请输入正确的联系电话!");
            return false;
        }
    }
    userPhone.blur(phoneCheck);

    //社会统一代码验证
    function codeCheck() {
        userCode.val($.trim(userCode.val()));
        var code_val = userCode.val();
        // var codePhone = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{18}$/;
        var codePhone = /^[a-zA-Z0-9]{18}$/;
        if (codePhone.test(code_val)) {
            userCode.next().html("");
            return true;
        } else if (!$.trim(code_val)) {
            userCode.next().html("统一社会信用代码不能为空!");
            return false;
        } else {
            userCode.next().html("统一社会信用代码为18位数字与字母组合!");
            return false;
        }
    }
    userCode.blur(codeCheck);


    var submitingMask = {
        set: function () {
            $('.submiting-mask').css('display', 'block');
            $('#user_apply').text('提交中...');
        },
        remove: function () {
            $('.submiting-mask').css('display', 'none');
            $('#user_apply').text('提交');
        }
    }

    $("#user_apply").on("click", function () {

        var firm = firmCheck();
        var people = propleCheck();
        var registerAddre = checkRegisterAddre();
        var addre = addreCheck();
        var name = nameCheck();
        var phone = phoneCheck();
        var email = emailCheck();
        var code = codeCheck();
        var pass = passCheck();
        var pass2 = passCheck2();

        if (firm && people && registerAddre && addre && name && phone && email && code && pass && pass2) {
            submitingMask.set();
            $.ajax({
                type: 'post',
                url: register,
                data: {
                    company_name: userFirm.val(),
                    responsible_person: userPeople.val(),
                    company_register_address: userAddre.val(),
                    declare_person: userName.val(),
                    declare_phone: userPhone.val(),
                    declare_mail: userEmail.val(),
                    credit_code: userCode.val(),
                    password: password.val(),
                    area_id: area_id,
                },
                xhrFields: {
                    withCredentials: true
                },
                crossDomain: true,
                success: function (data) {

                    console.log(data);
                    if (data.state == 1) {
                        submitingMask.remove();
                        $(".modal").css("display", "block");
                    } else if (data.state == 2) {

                        new Dialog().init({
                            mask: true,
                            title: '',
                            content: data.message,
                            isAlert: true,
                            hideView: true,
                            callback: function (boolean) {
                                if (boolean) {
                                    top.location.href = loginUrl;
                                }
                            }
                        });
                    } else {
                        submitingMask.remove();
                        $(".apply_info").html(data.message);
                    }
                }
            });
        }
    });

    $(".modal .close").on("click", function () {
        $(".modal").css("display", "none");
    });
});