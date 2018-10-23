/**
 * Created by Administrator on 2017/9/22.
 */
$(function () {
    var company_name = "";
    if (!sessionStorage.getItem('userInfo')) {
        new Dialog().init({
            mask: true,
            title: '',
            content: '未登录或登录失效！',
            isAlert: true,
            hideView: true,
            callback: function (boolean) {
                if (boolean) {
                    top.location.href = loginUrl;
                }
            }
        });
    } else {
        var user_id = JSON.parse(sessionStorage.getItem('userInfo')).user_id;
        all();
    }

    //预览文件
    $("#top_mark,#bottom_mark").on("click", "a.preview_file", function (e) {

        e.preventDefault();
        $(".dialog-review").css("transform", "scale(1)");
        $(".dialog-title").html($(this).data("file"));

        //获取远程html

        var html_url = $(this).data("html");
        var file_type = $(this).data("file_type");
        var base_action = html_action + '?file_type=' + file_type + '&html_path=';

        $.ajax({
            type: 'get',
            url: base_action + html_url,
            dataType: 'html',
            success: function (data) {

                var reg = /src="([^"]*)"/g;
                var result;
                var imgSrcs = [];
                while (result = reg.exec(data)) {
                    imgSrcs.push(result[1]);
                };
                // console.log('原始', imgSrcs);
                imgSrcs = uniq(imgSrcs);
                // console.log('去重', imgSrcs);
                imgSrcs.forEach(function (imgSrc) {
                    var reg = new RegExp(imgSrc, 'g');
                    data = data.replace(reg, base_action + imgSrc)
                })

                $('.left-content').html(data);
            },
            xhrFields: {
                withCredentials: true
            },
            crossDomain: true,
            error: function (err) {
                console.log('失败，错误原因：', err)
            }
        });

    });

    $(".dialog-close").on("click", function () {
        $(".dialog-review").css("transform", "scale(0)");
    });


    //申报表其他信息-申报单位
    function all() {
        $.ajax({
            type: 'post',
            url: allData,
            data: { user_id: user_id },
            xhrFields: {
                withCredentials: true
            },
            crossDomain: true,
            success: function (res) {

                if (res.state == 1) {

                    var data = res.data;
                    company_name = data.company_name;

                    if (JSON.parse(sessionStorage.getItem('all_data')).declare_id) {
                        getProduct();
                        getPlan();
                    }
                } else if (res.state == 0) {
                    new Dialog().init({
                        mask: true,
                        title: '',
                        content: res.message,
                        isAlert: true,
                        callback: function (boolean) {

                        }
                    });
                } else if (res.state == 2) {

                    new Dialog().init({
                        mask: true,
                        title: '',
                        content: res.message,
                        isAlert: true,
                        hideView: true,
                        callback: function (boolean) {
                            if (boolean) {
                                top.location.href = loginUrl;
                            }
                        }
                    });
                }

            }
        });
    };

    //获取已上传产品文件
    function getProduct() {
        $.ajax({
            type: 'post',
            url: getProductUrl,
            data: { declare_id: JSON.parse(sessionStorage.getItem('all_data')).declare_id },
            xhrFields: {
                withCredentials: true
            },
            crossDomain: true,
            success: function (res) {

                var data = res.data.baseDatas;
                var getProductHtml = "";
                if (res.state == 1) {
                    for (var i = 0; i < data.length; i++) {
                        var upload_time = new Date(data[i].upload_time);
                        var uploadTime = infoDateChange(upload_time);
                        var audit_state;
                        if (data[i].audit_state == '0') {
                            audit_state = '进入初审';
                        } else if (data[i].audit_state == '1') {
                            audit_state = '进入中审';
                        } else if (data[i].audit_state == '2') {
                            audit_state = '进入终审';
                        } else if (data[i].audit_state == '3') {
                            audit_state = '成功入选';
                        } else if (data[i].audit_state == '-1') {
                            audit_state = '未入选';
                        } else if (data[i].audit_state == '-2') {
                            audit_state = '初审补充材料';
                        }
                        getProductHtml += ' <tr ' + setDataString(data[i]) + '> <td>' + (i + 1) + '</td><td>' + data[i].file_code + '</td><td>' + company_name + '</td> <td>' + data[i].old_file_name + '</td><td>' + audit_state + '</td> <td>' + uploadTime + '</td> <td><a href="javascript:;" class="preview_file" data-file=' + data[i].old_file_name + ' data-html=' + data[i].html_path + ' data-file_type="0">预览</a></td> </tr>'
                        // getProductHtml += ' <tr ' + setDataString(data[i]) + '> <td>' + (i + 1) + '</td><td>' + data[i].file_code + '</td><td>' + company_name + '</td> <td>' + data[i].old_file_name + '</td><td>' + audit_state + '</td> <td>' + uploadTime + '</td> <td><a href="javascript:;" class="preview_file" data-file=' + data[i].old_file_name + ' data-html=' + data[i].html_path + ' data-file_type="0">预览</a><a class="delete_file" data-cat="product" onclick="deleteFile(this)">删除</a></td> </tr>'
                    }
                    $("#top_mark tbody").html(getProductHtml);
                } else if (res.state == 2) {

                    new Dialog().init({
                        mask: true,
                        title: '',
                        content: res.message,
                        isAlert: true,
                        hideView: true,
                        callback: function (boolean) {
                            if (boolean) {
                                top.location.href = loginUrl;
                            }
                        }
                    });
                }
            }
        });
    };

    window.deleteFile = function (deleteBtn) {

        var cat = $(deleteBtn).data('cat');
        var fileData = $(deleteBtn).parents('tr').data();
        var old_file_name = fileData['old_file_name'];

        new Dialog().init({
            mask: true,
            title: '警告',
            content: '确定要删除文件"' + old_file_name + '"吗？',
            isAlert: false,
            callback: function (boolean) {

                if (boolean) { //确定
                    //整理数据
                    var ajaxData = {
                        declare_id: JSON.parse(sessionStorage.getItem('all_data')).declare_id,
                        company_name: JSON.parse(sessionStorage.getItem('all_data')).company_name,
                        file_name: fileData['file_name'],
                        file_path: fileData['file_path'],
                        html_path: fileData['html_path'],
                        file_code: fileData['file_code']

                    };

                    if (cat == 'product') {
                        ajaxData.product_id = fileData['product_id'];
                        var deleteFileUrl = deleteProductFileUrl;
                    } else if (cat == 'plan') {
                        ajaxData.plan_id = fileData['plan_id'];
                        var deleteFileUrl = deletePlanFileUrl;
                    }

                    console.log(ajaxData);
                    //开始发送
                    $.ajax({
                        type: 'post',
                        url: deleteFileUrl,
                        data: ajaxData,
                        success: function (res) {
                            if (res.state == 1) {
                                new Dialog().init({
                                    mask: true,
                                    title: '',
                                    content: '删除成功！',
                                    isAlert: true,
                                    callback: function (boolean) {
                                        if (cat == 'product') {
                                            getProduct();
                                        } else if (cat == 'plan') {
                                            getPlan();
                                        }
                                    }
                                });
                            } else if (res.state == 0) {
                                new Dialog().init({
                                    mask: true,
                                    title: '',
                                    content: res.message,
                                    isAlert: true,
                                    callback: function (boolean) {

                                    }
                                });
                            } else if (res.state == 2) {
                                new Dialog().init({
                                    mask: true,
                                    title: '',
                                    content: res.message,
                                    isAlert: true,
                                    hideView: true,
                                    callback: function (boolean) {
                                        if (boolean) {
                                            top.location.href = loginUrl;
                                        }
                                    }
                                });
                            }
                        },
                        xhrFields: {
                            withCredentials: true
                        },
                        crossDomain: true,
                        error: function (err) {
                            console.log('失败，错误原因：', err.message)
                        }
                    });
                }

            }
        });

    };
    //获取已上传方案文件
    function getPlan() {
        $.ajax({
            type: 'post',
            url: getPlanUrl,
            data: { declare_id: JSON.parse(sessionStorage.getItem('all_data')).declare_id },
            xhrFields: {
                withCredentials: true
            },
            crossDomain: true,
            success: function (res) {
                //console.log(res);
                if (res.state == 2) {

                    new Dialog().init({
                        mask: true,
                        title: '',
                        content: res.message,
                        isAlert: true,
                        hideView: true,
                        callback: function (boolean) {
                            if (boolean) {
                                top.location.href = loginUrl;
                            }
                        }
                    });
                }
                var data = res.data.baseDatas;
                var getPlantHtml = "";
                for (var i = 0; i < data.length; i++) {

                    var upload_time = new Date(data[i].upload_time);
                    var uploadTime = infoDateChange(upload_time);
                    var audit_state;

                    if (data[i].audit_state == '0') {
                        audit_state = '进入初审';
                    } else if (data[i].audit_state == '1') {
                        audit_state = '进入中审';
                    } else if (data[i].audit_state == '2') {
                        audit_state = '进入终审';
                    } else if (data[i].audit_state == '3') {
                        audit_state = '成功入选';
                    } else if (data[i].audit_state == '-1') {
                        audit_state = '未入选';
                    } else if (data[i].audit_state == '-2') {
                        audit_state = '初审补充材料';
                    }

                    getPlantHtml += ' <tr ' + setDataString(data[i]) + '> <td>' + (i + 1) + '</td><td>' + data[i].file_code + '</td><td>' + company_name + '</td> <td>' + data[i].old_file_name + '</td><td>' + audit_state + '</td><td>' + uploadTime + '</td> <td><a href="javascript:;" class="preview_file" data-file=' + data[i].old_file_name + '  data-html=' + data[i].html_path + '  data-file_type="1">预览</a></td> </tr>'
                    // getPlantHtml += ' <tr ' + setDataString(data[i]) + '> <td>' + (i + 1) + '</td><td>' + data[i].file_code + '</td><td>' + company_name + '</td> <td>' + data[i].old_file_name + '</td><td>' + audit_state + '</td><td>' + uploadTime + '</td> <td><a href="javascript:;" class="preview_file" data-file=' + data[i].old_file_name + '  data-html=' + data[i].html_path + '  data-file_type="1">预览</a><a class="delete_file" data-cat="plan" onclick="deleteFile(this)">删除</a></td> </tr>'
                }
                $("#bottom_mark tbody").html(getPlantHtml);
            }
        });
    };

    //后台时间转换函数
    function infoDateChange(date) {
        var year = 1900 + date.getYear();  //年
        var month = date.getMonth() + 1; //月
        var strDate = date.getDate();    //日
        return year + "/" + month + "/" + strDate;
    }
});



