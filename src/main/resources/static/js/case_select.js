/**
 * Created by Administrator on 2017/10/9.
 */
$(function () {
    //查询案例筛选
    $.ajax({
        type: 'post',
        url: propertyUrl,
        data: {},
        xhrFields: {
            withCredentials: true
        },
        crossDomain: true,
        success: function (res) {
            //console.log(res);
            if (res.state == 2) {
                $("#table_mark").hide();
                $(".page").hide();
                $(".table-file .data-none").show();
                $(".table-file .data-none").html(res.message);

                new Dialog().init({
                    mask: true,
                    title: '',
                    content: res.message,
                    isAlert: true,
                    hideView:true,
                    callback: function (boolean) {
                        if (boolean) {
                            top.location.href = loginUrl;
                        }
                    }
                });

            }
        }
    });


});










