/**
 * Created by Administrator on 2017/9/20.
 */
$(function () {
    var area_id = "";  //区域ID
    var file_type = "";  //案例分类
    var pageNum = 1;
    var area_id2 = "";  //区域ID
    var file_type2 = "";  //案例分类

    getAllProvince();
    caseScreenResult(pageNum, file_type, area_id);

    $("#table_mark ").on("click", "span.sele-icon", function () {
        var isSelect = $(this).hasClass("is-icon");
        this.className = isSelect ? "sele-icon no-icon" : "sele-icon is-icon";
    });

    //案例筛选与查看清单选择
    $(".review-list").on("click", "li a", function (e) {
        e.preventDefault();
        $(this).parent().addClass("current").siblings(".current").removeClass("current");
        var classN = $(this).attr("href");
        $(classN).show().siblings("div").hide();
        if (classN == "#case_select") {
            caseScreenResult(pageNum, file_type, area_id);
        } else {
            caseScreenResult2(pageNum, file_type2, area_id2);
        }
    });


    //案例条件与查看清单选择
    $("#sele_province").on("change", function () {
        area_id = $(this).val();
    });
    $("#sele_province2").on("change", function () {
        area_id2 = $(this).val();
    });
    $("#case_kind").on("change", function () {
        file_type = $(this).val();
    });
    $("#case_kind2").on("change", function () {
        file_type2 = $(this).val();
    });
    $(".select-is").on("click", "a", function (e) {
        e.preventDefault();
        caseScreenResult(pageNum, file_type, area_id);
    });
    $(".select-is2").on("click", "a", function (e) {
        e.preventDefault();
        caseScreenResult2(pageNum, file_type2, area_id2);
    });


    //查看
    var dialogReview = parent.document.getElementById("dialog-review");
    $("#table_pass,#table_mark").on("click", "span.preview_file", function () {
        //alert("点击查看");
        $(dialogReview).css("transform", "scale(1)");
        $(dialogReview).find(".dialog-title").html($(this).data("file"));

        top.user_id = $(this).data('user_id');
        top.html_url = $(this).parents('tr').data('html_path');
        top.file_type = $(this).parents('tr').data('file_type');

        $(dialogReview).find("#case-preview").attr('src', templetUrl);
    });

    $(dialogReview).find(".dialog-close").on("click", function () {
        $(dialogReview).css("transform", "scale(0)");
    });


    //获得省份
    function getAllProvince() {
        $.ajax({
            type: "post",
            url: getAllProvinceUrl,
            data: {},
            xhrFields: {
                withCredentials: true
            },
            crossDomain: true,
            success: function (res) {
                if (res.state == 2) {

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
                var data = res.data;
                var ProvinceHtml = "";
                for (var i = -1; i < data.length; i++) {
                    if (i == -1) {
                        ProvinceHtml += '<option selected="selected" value=""> 全部</option>';
                    } else {
                        ProvinceHtml += '<option value=' + data[i].area_id + '>' + data[i].area_name + '</option>'
                    }
                }
                $("#sele_province,#sele_province2").html(ProvinceHtml);
            }
        });
    }

    //查询案例筛选
    function caseScreenResult(pageNum, file_type, area_id) {
        $.ajax({
            type: 'post',
            url: checkCaseByUserIdUrl,
            data: { audit_state: 0, pageNum: pageNum, file_type: file_type, area_id: area_id, average: "", first_score: "" },
            xhrFields: {
                withCredentials: true
            },
            crossDomain: true,
            success: function (res) {
                //console.log(res);
                var getfileHtml = "";
                if (res.state == 2) {

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
                } else if (res.state == 1) {
                    if (res.data == null) {
                        $("#table_mark").hide();
                        $(".table-file .data-none").show();
                        $(".table-file .data-none").html(res.message);
                    } else {
                        $("#table_mark").show();
                        $(".table-file .data-none").hide();
                        //var list = res.data.pageBeanMysql.data;
                        var list = res.data;

                        getfileHtml = ' <thead> <tr> <th>文件号</th> <th>申报公司</th> <th>申报文件</th> <th>审核状态</th> <th>审核选项</th><th>提交时间</th> <th>操作</th> </tr> </thead> <tbody>';

                        for (var i = 0; i < list.length; i++) {
                            var upload_time = new Date(list[i].upload_time);
                            var uploadTime = infoDateChange(upload_time);
                            var file_path=list[i].file_path;
                            var old_file_name=list[i].old_file_name;
                            var declare_id=list[i].declare_id;
                            var plan_id = list[i].plan_id;
                            var file_name = list[i].file_name;
                            var company_name = list[i].company_name;

                            //格式化参数
                            var download_file_path = format_file_path(file_path,old_file_name); //下载链接
                            var return_file_path = format_file_return(file_path,old_file_name,declare_id,plan_id,file_name,company_name);//退回连接
                            format_upload_time(list[i]); //上传时间

                            if (list[i].audit_state == '0') {
                                list[i].audit_state = '进入初审';
                            } else if (list[i].audit_state == '1') {
                                list[i].audit_state = '进入中审';
                            } else if (list[i].audit_state == '2') {
                                list[i].audit_state = '进入终审';
                            } else if (list[i].audit_state == '3') {
                                list[i].audit_state = '成功入选';
                            } else if (list[i].audit_state == '-1') {
                                list[i].audit_state = '未入选';
                            } else if (list[i].audit_state == '-2') {
                                list[i].audit_state = '初审补充材料';
                            }

                            //getfileHtml += '<tr ' + setDataString(list[i]) + '> <td>' + list[i].file_code + '</td> <td>' + list[i].company_name + '</td> <td><span data-code="' + list[i].file_code + '" data-id="' + list[i].user_id + '" class="sele-icon no-icon"></span>' + list[i].old_file_name + '</td><td><span><button>通过</button><button>不通过</button></span></td><td>' + uploadTime + '</td> <td><span class="preview_file" data-file=' + list[i].old_file_name + ' data-user_id="' + list[i].user_id + '">查看</span></td> </tr>';
                            //getfileHtml += '<tr ' + setDataString(list[i]) + '> <td>' + list[i].file_code + '</td> <td>' + list[i].company_name + '</td> <td><span data-code="' + list[i].file_code + '" data-id="' + list[i].user_id + '" class="sele-icon no-icon"></span>' + list[i].old_file_name + '</td><td class="audit_state">' +
                            getfileHtml += '<tr ' + setDataString(list[i]) + '> <td>' + list[i].file_code + '</td> <td>' + list[i].company_name + '</td> ' +
                                '<td><span data-code="' + list[i].file_code + '" data-id="' + list[i].user_id + '" class="sele-icon no-icon"></span>' + list[i].old_file_name +
                                '<br><a href="'+download_file_path+'" class="download_file" ><font color="#6495ed">下载</font></a>'+
                                '<a href="'+return_file_path+'" class="return_file" ><font color="#6495ed">退回</font></a></td>' +
                                '<td>'+list[i].audit_state+'</td><td class="audit_state">' +
                                '<span>初审<input name="Fruit'+i+'" type="radio" value="1"/>通过</span>' +
                                '<span><input name="Fruit'+i+'" type="radio" value="0"/>淘汰</span><br>' +
                                '<span>中审<input name="Fruit'+i+'" type="radio" value="2"/>通过</span>' +
                                '<span><input name="Fruit'+i+'" type="radio" value="1"/>淘汰</span><br>' +
                                '<span>终审<input name="Fruit'+i+'" type="radio" value="3"/>通过</span>' +
                                '<span><input name="Fruit'+i+'" type="radio" value="2"/>淘汰</span>' +
                                '</td><td>' + uploadTime + '</td> <td><span class="preview_file" data-file=' + list[i].old_file_name + ' data-user_id="' + list[i].user_id + '">查看</span></td> </tr>';
                        }
                        getfileHtml += '</tbody>';
                        $("#table_mark ").html(getfileHtml);


                        $('#paging').jqPaginator({
                            totalCounts: res.data.count,
                            pageSize: res.data.pageSize,
                            currentPage: res.data.pageNum,
                            first: '<li class="first"><a href="javascript:void(0);">首页</a></li>',
                            prev: '<li class="prev"><a href="javascript:void(0);">上一页</a></li>',
                            next: '<li class="next"><a href="javascript:void(0);">下一页</a></li>',
                            last: '<li class="last"><a href="javascript:void(0);">末页</a></li>',
                            page: '<li class="page"><a href="javascript:void(0);">{{page}}</a></li>',
                            onPageChange: function (pageNum) {
                                caseScreenResult(pageNum, file_type, area_id);
                            }
                        });
                        //全选
                        $(".sele-all").on("click", function () {
                            $("#table_mark span.sele-icon").toggleClass("no-icon is-icon");
                        });

                    }
                }
            }
        });
    }

    //下载路径拼接
    function format_file_path(file_path,old_file_name) {
        file_path = downLoadWordUrl + '?old_file_name=' + old_file_name + '&file_path=' + file_path;
        return file_path;
    };

    //退回路劲拼接
    function format_file_return(file_path,old_file_name,declare_id,plan_id,file_name,company_name) {
        file_path = returnWordUrl + '?old_file_name=' + old_file_name + '&file_path=' + file_path +'&declare_id=' + declare_id +'&plan_id=' + plan_id +'&file_name=' + file_name +'&company_name=' + company_name;
        return file_path;
    };

    function format_upload_time(item) {
        item.upload_time = new Date(item.upload_time).toLocaleDateString();
    };

    var seleNone = parent.document.getElementById("seleNone");
    var seleNone1 = parent.document.getElementById("seleNone1");
    var pass_file = parent.document.getElementById("pass_file");
    var pass_list = parent.document.getElementById("pass_list");
    var nopass = parent.document.getElementById("nopass");

    //通过-选择文件
    $("#pass_is").on("click", function (e) {
        //alert("点击");
        e.preventDefault();
        var isSelect = $("#table_mark span.sele-icon").hasClass("is-icon");
        if (isSelect) {
            $(pass_file).css("display", "block");
        } else {
            $(seleNone).css("display", "block");
        }
    });

    //不通过-选择文件
    $("#pass_no").on("click", function (e) {
        e.preventDefault();
        var isSelect = $("#table_mark span.sele-icon").hasClass("is-icon");
        if (isSelect) {
            $(nopass).css("display", "block");
        } else {
            $(seleNone).css("display", "block");
        }
    });


    //确定通过
    $(pass_file).on("click", "button#pass_con", function () {
        //点击确认按钮
        //获取鼠标选中行文件id
        $(pass_file).css("display", "none");
        $(pass_list).css("display", "block");
        //var audit_state2 = 1;
        var audit_state2=$('.audit_state input[type="radio"]:checked').val();
        //alert(audit_state2);
        editCaseScreen(audit_state2);
    });


    //不通过
    $(nopass).on("click", "button#pass_con", function () {
        $(pass_file).css("display", "none");
        $(pass_list).css("display", "block");

    });

    //不通过发送信息
    $(nopass).on("click", "button#nopass_con", function () {
        var audit_state2 = $(nopass).find("input[name='radio']:checked").val();
        editCaseScreen(audit_state2);
        caseScreenResult(pageNum, file_type, area_id);
        $(nopass).hide();
    });

    //点击下载
    $(".download_file").on("click",  function (e) {
        alert("点击下载");
    });


    //查看清单
    $(pass_list).on("click", "button#view_list", function () {
        $(".review-list li:eq(1)").addClass("current").siblings("li").removeClass("current");
        $(pass_list).css("display", "none");

        $("#case_select").css("display", "none");
        $("#review_list").css("display", "block");
        pageNum = 1;
        caseScreenResult2(pageNum, file_type2, area_id2);
    });


    //通过-不通过文件函数
    function editCaseScreen(audit_state2) {
        var screen_param = {
            file_codes: [],
            user_ids: [],
        };
        $(".sele-icon.is-icon").each(function () {
            if(audit_state2==1){
               // alert(audit_state2);
            }else if(audit_state2==2){
               // alert(audit_state2);
            }else if(audit_state2==3){
               // alert(audit_state2);
            }
            screen_param.file_codes.push({ file_code: $(this).data("code") });
            screen_param.user_ids.push({ user_id: $(this).data("id") });
        });

        var screen_param2 = JSON.stringify(screen_param, null, 4);

        var state;
        var ajaxData;

        console.log('audit_state2', audit_state2, typeof audit_state2)

        if (audit_state2 == 1) {//初筛通过
            state = '0';
        } else if (audit_state2 == '-2') {//初筛资料不全
            state = '1';
        } else if (audit_state2 == '-1') {//初筛不通过
            state = '2';
        }

        ajaxData = {
            screen_param: screen_param2,
            audit_state: audit_state2,
            state: state
        };
        console.log(ajaxData);
        $.ajax({
            type: "post",
            url: editCaseScreenUrl,
            data: ajaxData,
            xhrFields: {
                withCredentials: true
            },
            crossDomain: true,
            success: function (res) {
                if (res.state == 1) {
                    caseScreenResult(pageNum, file_type, area_id);
                } else if (res.state == 2) {

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
    }

    //查看清单函数
    function caseScreenResult2(pageNum, file_type2, area_id2) {
        $.ajax({
            type: 'post',
            url: checkCaseByUserIdUrl,
            data: { audit_state: 1, pageNum: pageNum, file_type: file_type2, area_id: area_id2, average: "", first_score: "" },
            xhrFields: {
                withCredentials: true
            },
            crossDomain: true,
            success: function (res) {
                //console.log(res);
                var getfileHtml = "";
                if (res.state == 2) {

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
                if (res.data == null) {
                    $(".table-pass .data-none").show();
                    $(".table-pass .data-none").html(res.message);
                    $("#table_pass").hide();

                } else {
                    $(".table-pass .data-none").hide();
                    $("#table_pass").show();

                    var list = res.data.data;
                    getfileHtml = '<thead> <tr> <th>文件号</th> <th>申报公司</th> <th>申报文件</th><th>提交时间</th> <th >入选时间</th> <th>操作</th> </tr> </thead><tbody>';

                    for (var i = 0; i < list.length; i++) {
                        var upload_time = new Date(list[i].upload_time);
                        var uploadTime = infoDateChange(upload_time);

                        var selected_time = new Date(list[i].selected_time);
                        var selectedTime = infoDateChange(selected_time);

                        getfileHtml += ' <tr ' + setDataString(list[i]) + '> <td>' + list[i].file_code + '</td> <td>' + list[i].company_name + '</td> <td> ' + list[i].old_file_name + '</td> <td>' + uploadTime + '</td> <td>' + selectedTime + '</td> <td><span class="preview_file" data-file=' + list[i].old_file_name + ' data-user_id="' + list[i].user_id + '" >查看</span></td> </tr>';
                    }
                    getfileHtml += '</tbody>';
                    $("#table_pass ").html(getfileHtml);

                    $('#paging2').jqPaginator({
                        totalCounts: res.data.count,
                        pageSize: res.data.pageSize,
                        currentPage: res.data.pageNum,
                        first: '<li class="first"><a href="javascript:void(0);">首页</a></li>',
                        prev: '<li class="prev"><a href="javascript:void(0);">上一页</a></li>',
                        next: '<li class="next"><a href="javascript:void(0);">下一页</a></li>',
                        last: '<li class="last"><a href="javascript:void(0);">末页</a></li>',
                        page: '<li class="page"><a href="javascript:void(0);">{{page}}</a></li>',
                        onPageChange: function (pageNum) {
                            caseScreenResult2(pageNum, file_type2, area_id2);
                        }
                    });
                }
            }
        });
    }

    $(seleNone).on("click", "span.close", function () {
        $(seleNone).css("display", "none");
    });
    $(seleNone).on("click", "#select_none", function () {
        $(seleNone).css("display", "none");
    });

    $(seleNone).on("click", "span.close", function () {
        $(seleNone).css("display", "none");
    });
    $(seleNone).on("click", "#select_none1", function () {
        $(seleNone).css("display", "none");
    });

    $(pass_file).on("click", "span.close", function () {
        $(pass_file).css("display", "none");
    });
    $(pass_file).on("click", "#pass_can", function () {
        $(pass_file).css("display", "none");
    });

    $(pass_list).on("click", "span.close", function () {
        $(pass_list).css("display", "none");
    });

    $(nopass).on("click", "span.close", function () {
        $(nopass).css("display", "none");
    });
    $(nopass).on("click", "#nopass_can", function () {
        $(nopass).css("display", "none");
    });

    //后台时间转换函数
    function infoDateChange(date) {
        var year = 1900 + date.getYear();  //年
        var month = date.getMonth() + 1; //月
        var strDate = date.getDate();    //日
        return year + "/" + month + "/" + strDate;
    }
});









