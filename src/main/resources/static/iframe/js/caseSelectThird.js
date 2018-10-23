/**
 * Created by Administrator on 2017/9/20.
 */
$(function () {
    var area_id = ""; //区域ID
    var file_type = ""; //案例分类
    var first_score = "";
    var average = "";
    var pageNum = 1;

    var area_id2 = ""; //区域ID
    var file_type2 = ""; //案例分类
    var first_score2 = "";
    getAllProvince();
    caseScreenResult(pageNum, file_type, area_id, average, first_score);

    //选择文件
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
            caseScreenResult(pageNum, file_type, area_id, average, first_score);
        } else {
            caseScreenResult2(pageNum, area_id2, file_type2, first_score2);
        }
    });


    //案例筛选条件--确定
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

    $("#sele_score").on("change", function () {
        first_score = $(this).val();
    });
    $("#sele_score2").on("change", function () {
        first_score2 = $(this).val();
    });

    function scoreCheck() {
        if (!$("#sele_score").val()) {
            return true;
        } else if (/^100$|^(\d|[1-9]\d)$/.test($("#sele_score").val())) {
            $("#sele_score").next().html("");
            return true;
        } else {
            $("#sele_score").next().html("*格式错误");
            return false;
        }
    }
    $("#sele_score").blur(scoreCheck);

    function scoreCheck2() {
        if (!$("#sele_score2").val()) {
            return true;
        } else if (/^100$|^(\d|[1-9]\d)$/.test($("#sele_score2").val())) {
            $("#sele_score2").next().html("");
            return true;
        } else {
            $("#sele_score2").next().html("*格式错误");
            return false;
        }
    }
    $("#sele_score2").blur(scoreCheck2);

    $(".select-is").on("click", "a", function (e) {
        e.preventDefault();
        var check2 = scoreCheck();
        if (check2) {
            caseScreenResult(pageNum, file_type, area_id, average, first_score)
        }
    });

    $(".select-is2").on("click", "a", function (e) {
        e.preventDefault();
        var check2 = scoreCheck2();
        if (check2) {
            caseScreenResult2(pageNum, area_id2, file_type2, first_score2);
        }
    });


    //查看操作
    var dialogReview = parent.document.getElementById("dialog-review");
    $("#table_pass,#table_mark").on("click", "span.preview_file", function () {
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
                        hideView: true,
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
    function caseScreenResult(pageNum, file_type, area_id, average, first_score) {
        $.ajax({
            type: 'post',
            url: caseScreenResultUrl,
            data: {
                audit_state: 2,
                pageNum: pageNum,
                file_type: file_type,
                area_id: area_id,
                average: first_score,
                first_score: ""
            },
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
                        hideView: true,
                        callback: function (boolean) {
                            if (boolean) {
                                top.location.href = loginUrl;
                            }
                        }
                    });

                } else if (res.state == 1) {
                    if (res.data == null) {
                        $("#table_mark").hide();
                        $(".page").hide();
                        $(".table-file .data-none").show();
                        $(".table-file .data-none").html(res.message);
                    } else {
                        $(".table-file .data-none").hide();
                        $("#table_mark").show();

                        var list = res.data.pageBeanMysql.data;
                        getfileHtml = '<thead> <tr> <th>文件号</th> <th>申报公司</th> <th><span class="sele-all">全选</span>申报文件</th> <th>第一轮<br>评审得分</th> <th>第一轮<br>评审人数</th>  <th>第二轮<br>评审得分</th>  <th >第二轮<br>评审人数</th> <th>两轮评审<br>综合得分</th> <th>提交时间</th> <th>操作</th> </tr> </thead> <tbody>';

                        for (var i = 0; i < list.length; i++) {
                            var upload_time = new Date(list[i].upload_time);
                            var uploadTime = infoDateChange(upload_time);


                            if (list[i].first_score) {
                                var firstScore = (list[i].first_score / list[i].first_count).toFixed(2);
                                var first_count = list[i].first_count;
                            } else {
                                var firstScore = "暂无打分";
                                var first_count = '暂无专家';
                            }

                            if (list[i].second_score) {
                                var secondScore = (list[i].second_score / list[i].second_count).toFixed(2);
                                var second_count = list[i].second_count;
                            } else {
                                var secondScore = "暂无打分";
                                var second_count = '暂无专家';
                            }

                            if (list[i].first_score && list[i].second_score) {
                                var thirdScore = (((list[i].first_score / list[i].first_count) + (list[i].second_score / list[i].second_count)) / 2).toFixed(2);
                            } else {
                                var thirdScore = "暂无打分";
                            }


                            getfileHtml += ' <tr ' + setDataString(list[i]) + '> <td>' + list[i].file_code + '</td> <td>' + list[i].company_name + '</td> <td><span class="sele-icon no-icon" data-code="' + list[i].file_code + '" data-id="' + list[i].user_id +
                                '" data-score="' + thirdScore + '"></span>' + list[i].old_file_name + '</td> <td>' + firstScore + '</td> <td>' + first_count + '</td> <td>' + secondScore + '</td> <td>' + second_count + '</td>  <td>' + thirdScore + '</td> <td>' + uploadTime + '</td> <td><span class="preview_file" data-file=' + list[i].file_name + ' data-user_id="' + list[i].user_id + '" data-html_path="' + list[i].html_path + '">查看</span></td> </tr>';
                        }
                        getfileHtml += '</tbody>';
                        $("#table_mark ").html(getfileHtml);


                        $('#paging').jqPaginator({
                            totalCounts: res.data.pageBeanMysql.count,
                            pageSize: res.data.pageBeanMysql.pageSize,
                            currentPage: res.data.pageBeanMysql.pageNum,
                            first: '<li class="first"><a href="javascript:void(0);">首页</a></li>',
                            prev: '<li class="prev"><a href="javascript:void(0);">上一页</a></li>',
                            next: '<li class="next"><a href="javascript:void(0);">下一页</a></li>',
                            last: '<li class="last"><a href="javascript:void(0);">末页</a></li>',
                            page: '<li class="page"><a href="javascript:void(0);">{{page}}</a></li>',
                            onPageChange: function (pageNum) {
                                caseScreenResult(pageNum, file_type, area_id, average, first_score);
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

    var seleNone = parent.document.getElementById("seleNone");
    var scoreNone = parent.document.getElementById("scoreNone"); //暂无打分
    var pass_file = parent.document.getElementById("pass_file3");
    var pass_list = parent.document.getElementById("pass_list3");
    var nopass = parent.document.getElementById("nopass3");


    //通过-选择文件
    $("#pass_is").on("click", function (e) {
        e.preventDefault();
        var isSelect = $("#table_mark span.sele-icon").hasClass("is-icon");
        if (isSelect) {
            var scoreString = [];
            $(".sele-icon.is-icon").each(function () {
                var ss = $(this).data("score");
                scoreString.push(ss);
            });
            //var is = scoreString.indexOf("暂无打分");
            var is = $.inArray("暂无打分", scoreString);
            if (is == -1) {
                $(pass_file).css("display", "block");
            } else {
                $(scoreNone).css("display", "block");
            }
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
    $(pass_file).on("click", "button#pass_con3", function () {
        $(pass_file).css("display", "none");
        $(pass_list).css("display", "block");
        var audit_state2 = 3;
        editCaseScreen3(audit_state2);
    });

    //不通过
    $(nopass).on("click", "button#pass_con3", function () {
        $(pass_file).css("display", "none");
        $(pass_list).css("display", "block");
    });

    //不通过发送信息
    $(nopass).on("click", "button#nopass_con3", function () {
        var audit_state2 = -1;
        editCaseScreen3(audit_state2);
        caseScreenResult(pageNum, file_type, area_id, average, first_score);
        $(nopass).hide();
    });


    //通过-不通过文件函数
    function editCaseScreen3(audit_state2) {
        var screen_param = {
            file_codes: [],
            user_ids: [],
        };
        $(".sele-icon.is-icon").each(function () {
            screen_param.file_codes.push({
                file_code: $(this).data("code")
            });
            screen_param.user_ids.push({
                user_id: $(this).data("id")
            });
        });
        var screen_param2 = JSON.stringify(screen_param, null, 4);

        var state;
        var ajaxData;

        console.log('audit_state2', audit_state2, typeof audit_state2)

        if (audit_state2 == 3) { //终筛通过
            state = '5';
        } else if (audit_state2 == -1) { //终筛不通过
            state = '6';
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

                //console.log(res);
                if (res.state == 1) {
                    caseScreenResult(pageNum, file_type, area_id, average, first_score);
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
    }

    //查看清单
    $(pass_list).on("click", "button#view_list3", function () {
        $(".review-list li:eq(1)").addClass("current").siblings("li").removeClass("current");
        $(pass_list).css("display", "none");
        $("#case_select").css("display", "none");
        $("#review_list").css("display", "block");
        pageNum = 1;
        caseScreenResult2(pageNum, area_id2, file_type2, first_score2);
    });


    //查看清单函数
    function caseScreenResult2(pageNum, area_id2, file_type2, first_score2) {
        $.ajax({
            type: 'post',
            url: caseScreenResultUrl,
            data: {
                audit_state: 3,
                pageNum: pageNum,
                file_type: file_type2,
                area_id: area_id2,
                average: first_score2,
                first_score: ""
            },
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
                var getfileHtml = "";
                if (res.data == null) {
                    $(".table-pass .data-none").show();
                    $(".table-pass .data-none").html(res.message);
                    $("#table_pass").hide();
                } else {
                    $(".table-pass .data-none").hide();
                    $("#table_pass").show();

                    var list = res.data.pageBeanMysql.data;
                    getfileHtml = '<thead> <tr> <th>文件号</th> <th>申报公司</th> <th>申报文件</th> <th>第一轮专家评审得分</th> <th>第二轮专家评审得分</th> <th>两轮评审综合得分</th> <th>提交时间</th> <th>入选时间</th> <th >操作</th> </tr> </thead> <tbody>';

                    for (var i = 0; i < list.length; i++) {
                        var upload_time = new Date(list[i].upload_time);
                        var uploadTime = infoDateChange(upload_time);
                        var selected_time = new Date(list[i].selected_time);
                        var selectedTime = infoDateChange(selected_time);

                        if (list[i].first_score) {
                            var firstScore = (list[i].first_score / list[i].first_count).toFixed(2);
                        } else {
                            var firstScore = "暂无打分";
                        }

                        if (list[i].second_score) {
                            var secondScore = (list[i].second_score / list[i].second_count).toFixed(2);
                        } else {
                            var secondScore = "暂无打分";
                        }

                        if (list[i].first_score && list[i].second_score) {
                            var thirdScore = ((list[i].first_score / list[i].first_count) + (list[i].second_score / list[i].second_count)) / 2;
                        } else {
                            var thirdScore = "暂无打分";
                        }

                        getfileHtml += ' <tr ' + setDataString(list[i]) + '> <td>' + list[i].file_code + '</td> <td>' + list[i].company_name + '</td> <td>' + list[i].old_file_name + '</td> <td>' + firstScore + '</td> <td>' + secondScore + '</td> <td>' + thirdScore + '</td> <td>' + uploadTime + '</td> <td>' + selectedTime + '</td> <td><span class="preview_file" data-file=' + list[i].file_name + ' data-user_id="' + list[i].user_id + '" data-html_path="' + list[i].html_path + '">查看</span></td> </tr>';
                    }
                    getfileHtml += '</tbody>';
                    $("#table_pass ").html(getfileHtml);

                    $('#paging2').jqPaginator({
                        totalCounts: res.data.pageBeanMysql.count,
                        pageSize: res.data.pageBeanMysql.pageSize,
                        currentPage: res.data.pageBeanMysql.pageNum,
                        first: '<li class="first"><a href="javascript:void(0);">首页</a></li>',
                        prev: '<li class="prev"><a href="javascript:void(0);">上一页</a></li>',
                        next: '<li class="next"><a href="javascript:void(0);">下一页</a></li>',
                        last: '<li class="last"><a href="javascript:void(0);">末页</a></li>',
                        page: '<li class="page"><a href="javascript:void(0);">{{page}}</a></li>',
                        onPageChange: function (pageNum) {
                            caseScreenResult2(pageNum, area_id2, file_type2, first_score2);
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

    $(pass_file).on("click", "span.close", function () {
        $(pass_file).css("display", "none");
    });
    $(pass_file).on("click", "#pass_can3", function () {
        $(pass_file).css("display", "none");
    });

    $(pass_list).on("click", "span.close", function () {
        $(pass_list).css("display", "none");
    });

    $(nopass).on("click", "span.close", function () {
        $(nopass).css("display", "none");
    });
    $(nopass).on("click", "#nopass_can3", function () {
        $(nopass).css("display", "none");
    });
    $(scoreNone).on("click", "span.close", function () {
        $(scoreNone).css("display", "none");
    });
    $(scoreNone).on("click", "button#score_none", function () {
        $(scoreNone).css("display", "none");
    });

    //后台时间转换函数
    function infoDateChange(date) {
        var year = 1900 + date.getYear(); //年
        var month = date.getMonth() + 1; //月
        var strDate = date.getDate(); //日
        return year + "/" + month + "/" + strDate;
    }

});