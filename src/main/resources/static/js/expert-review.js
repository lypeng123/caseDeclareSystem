$(function() {

	/**********************Tab切换部分**********************/

	$('.tab li').on('click', function() {
		if ($(this).attr('class') == 'active') {
			return false;
		} else {
			$(this).addClass('active').siblings().removeClass('active'); //变色
			filterInit(); //清空筛选条件
			getCaseData(1); //请求默认第一页
		}
	});

	function filterInit() {
		$('.filter select,.filter input').val('');
		$('select[name="field_id"]').parents('td').hide()
	}
	/****************************获取数据****************************/

	getOptionData('area_id', getAllProvinceUrl); //获取省份

	//获取Option
	function getOptionData(cat, url) {
		$.ajax({
			type: 'post',
			url: url,
			xhrFields: {
				withCredentials: true
			},
			crossDomain: true,
			success: function(res) {
				if (res.state == 1) {
					renderOption(cat, res.data);
				} else if (res.state == 0) {
					new Dialog().init({
						mask: true,
						title: '',
						content: res.message,
						isAlert: true,
						callback: function(boolean) {

						}
					});
				} else if (res.state == 2) {
					new Dialog().init({
						mask: true,
						title: '',
						content: res.message,
						isAlert: true,
						hideView: true,
						callback: function(boolean) {
							if (boolean) {
								top.location.href = loginUrl;
							}
						}
					});
				}
			},
			error: function(err) {
				console.log('失败，错误原因：', err.message)
			}
		});
	}

	//

	/****************************渲染数据****************************/

	//渲染Option
	function renderOption(cat, data) {

		for (var i = -1; i < data.length; i++) {
			var option = document.createElement('option');

			if (i == -1) {
				option.value = '';
				option.innerText = '全部';
			} else {
				option.value = data[i][getContainStrKey('_id', data[i])];
				option.innerText = data[i][getContainStrKey('_name', data[i])];
			}
			document.querySelector('select[name="' + cat + '"]').appendChild(option);
		}

	};

	/****************************绑定事件****************************/

	//应用领域选项

	$('select[name="file_type"]').on('change', function() {
		if ($(this).val() == 1) {
			$('select[name="field_id"]').parents('td').show()
		} else {
			$('select[name="field_id"]').val('');
			$('select[name="field_id"]').parents('td').hide()
		}
	});

	//获取第一轮转接评审列表
	getCaseData(1);

	function getCaseData(num) {
		if ($('.info').length <= 0) {
			var userInfo = JSON.parse(sessionStorage.getItem('userInfo'));
			var data = {
				userId: userInfo ? userInfo.user_id : '',
				fileType: $('[name="file_type"]').val(),
				area: $('[name="area_id"]').val(),
				auditState: $('.tab .active').data('audit_state'),
				page: num,
				pageSize: 10
			};
			$.ajax({
				type: 'post',
				url: expertReviewtFirstUrl,
				xhrFields: {
					withCredentials: true
				},
				data: data,
				crossDomain: true,
				success: function(res) {
					$('.result-list').html('');
					if (res.state == 1) {
						res.data.pageNum = num;
						res.data.pageSize = 10;
						renderCaseList(res.data);
					} else if (res.state == 0) {
						new Dialog().init({
							mask: true,
							title: '',
							content: res.message,
							isAlert: true
						});
					} else if (res.state == 2) {
						new Dialog().init({
							mask: true,
							title: '',
							content: res.message,
							isAlert: true,
							hideView: true,
							callback: function(boolean) {
								if (boolean) {
									top.location.href = loginUrl;
								}
							}
						});
					}
				},
				error: function(err) {
					console.log('单位性质失败，错误原因：', err.message)
				}
			});
		} else {
			new Dialog().init({
				mask: true,
				title: '',
				content: '填写有误，请检查表单中标注的<span style="color:red;">红色</span>字段！',
				isAlert: true
			});
		}
	};
	$('.submit').on('click', function() {
		getCaseData(1);
	});

	function renderCaseList(data) {
		var list = data.list;
		if (list.length == 0) {
			var p = document.createElement('p');
			p.style.cssText = 'padding:20px 0;text-algin:center;width:100%';
			p.innerText = '没有您要查询的结果!';
			document.querySelector('.result-list').appendChild(p);
			$('#paging').html('');
			return;
		};
		var html = '';
		for (var i = 0; i < list.length; i++) {
			//文件评审状态
			var reviewState;
			var reviewSymbolBgColor;
			var operationText;
			var operationClassName;

			if (list[i].reviewState == '-1') {
				reviewState = '评审中';
				operationText = '继续评审';
				operationClassName = 'continue-review';
				reviewSymbolBgColor = 'yellow';
			} else if (list[i].reviewState == '0') {
				reviewState = '未评审';
				operationText = '进入评审';
				operationClassName = 'enter-review';
				reviewSymbolBgColor = 'red';
			} else {
				reviewState = '已评审';
				operationText = '评审结果';
				operationClassName = 'result-review';
				reviewSymbolBgColor = 'green';
			};
			//渲染模板
			html += '<tr data-fileType = "' + list[i].fileType + '" data-uploadUserId = "' + list[i].uploadUserId + '" data-htmlPath = "' + list[i].htmlPath + '" data-screenId = "' + list[i].screenId + '" data-fileName="' + list[i].fileName + '" data-uploadTime="' + list[i].uploadTime + '" data-companyName="' + list[i].companyName + '" data-reviewState="' + list[i].reviewState + '"><td>' + list[i].fileCode + '</td><td>' + list[i].fileName + '</td><td>' + list[i].companyName + '</td><td><span class="review-symbol" style="background:' + reviewSymbolBgColor + ';"></span>' + reviewState + '</td><td>' + list[i].uploadTime + '</td><td><a href="javascript:;" class="' + operationClassName + '">' + operationText + '</a></td></tr>';
		};

		$('.result-list').html(html);

		//分页
		try {
			$('#paging').jqPaginator({
				totalCounts: data.total,
				pageSize: data.pageSize,
				currentPage: data.pageNum,
				first: '<li class="first"><a href="javascript:void(0);">首页</a></li>',
				prev: '<li class="prev"><a href="javascript:void(0);">上一页</a></li>',
				next: '<li class="next"><a href="javascript:void(0);">下一页</a></li>',
				last: '<li class="last"><a href="javascript:void(0);">末页</a></li>',
				page: '<li class="page"><a href="javascript:void(0);">{{page}}</a></li>',
				onPageChange: function(num) {
					getCaseData(num);
				}
			});
		} catch (err) {

		}

	};

	function setDataString(data) {

		var aKey = Object.keys(data).map(function(key) {
			return ' data-' + key + '="' + data[key] + '" ';
		});
		return aKey.join('');
	};
	/****************************添加评审****************************/
	function checkParams(reviewState) {

		if (reviewState == '1') {
			var inputs = $('.right-content input').toArray();

			for (var i = 0; i < inputs.length; i++) {
				if (inputs[i].value == '') {
					return false;
				}
			}
		}

		return true;
	};

	function destroyDialog() {
		$('.dialog-review').css('transform', 'scale(0)');
		//刷新列表
		getCaseData($('.page li.active').text());
		//销毁iframe
		$('#case-preview').attr('src', '');
		$('.right-content input').attr('readonly', false).css('border', '1px solid gray');
		//得分清空
		$('.right-content input').val('');
		//标题清空
		$('.dialog-title').text('');
		//总分清空
		$('.total-score').text('');
	}
	//提交表单
	$('.right-content').on('click', function(e) {
		if (e.target.className == 'review-submit' || e.target.className == 'review-save') {
			var reviewState = $(e.target).data('review_state');
			if (checkParams(reviewState)) {
				var data = {
					screenId: $('.result-list tr.active').data('screenid'), //唯一标识
					userId: JSON.parse(sessionStorage.getItem('userInfo')).user_id, //用户ID
					userName: JSON.parse(sessionStorage.getItem('userInfo')).user_name, //用户名称
					auditState: $('.tab li.active').data('audit_state'), // 评审级别（ 1：： 一级， 2： 二级）
					firstScore: $('.right-content input')[0].value, //第一栏得分
					secondScore: $('.right-content input')[1].value, //第二栏得分
					thirdScore: $('.right-content input')[2].value, //第三栏得分
					fourthScore: $('.right-content input')[3].value, //第四栏得分
					fifthScore: $('.right-content input')[4].value, //第五栏得分
					reviewState: reviewState //评审状态(-1： 暂存， 0： 未评审， 1: 已评审)
				};
				$.ajax({
					type: 'post',
					url: expertScoreUrl,
					success: function(res) {
						if (res.state == 1) {
							new Dialog().init({
								mask: true,
								title: '',
								content: res.message,
								isAlert: true,
								callback: function(bool) {
									if (bool) {
										destroyDialog()
									}
								}
							});

						} else if (res.state == 0) {
							new Dialog().init({
								mask: true,
								title: '',
								content: res.message,
								isAlert: true
							});
						} else if (res.state == 2) {

							new Dialog().init({
								mask: true,
								title: '',
								content: res.message,
								isAlert: true,
								hideView: true,
								callback: function(boolean) {
									if (boolean) {
										top.location.href = loginUrl;
									}
								}
							});
						}
					},
					data: data,
					xhrFields: {
						withCredentials: true
					},
					crossDomain: true,
					error: function(err) {
						console.log('失败，错误原因：', err.message)
					}
				});
			} else {
				new Dialog().init({
					mask: true,
					title: '',
					content: '请检查遗漏的打分项！',
					isAlert: true
				});

			}
		}
	});

	function renderReviewScore(data) {

		$('.right-content input')[0].value = data.first || '';
		$('.right-content input')[1].value = data.second || '';
		$('.right-content input')[2].value = data.third || '';
		$('.right-content input')[3].value = data.fourth || '';
		$('.right-content input')[4].value = data.fifth || '';
		if (data.sum == 0) {
			data.sum = '';
		}
		$('.total-score').text(data.sum);
	};

	//进入评审
	$('.result-list').on('click', function(e) {

		$(e.target).parents('tr').addClass('active');
		var opt = $(e.target).parents('tr').data();

		if (e.target.className == 'continue-review' || e.target.className == 'result-review') {
			if (opt.reviewstate == 1) {
				$('.right-content').removeClass('reviewstate_no');
				$('.right-content').addClass('reviewstate_ok');
			} else {
				$('.right-content').removeClass('reviewstate_ok');
				$('.right-content').addClass('reviewstate_no');
			}
			renderUI(opt);
			var data = {
				screenId: opt.screenid,
				userId: JSON.parse(sessionStorage.getItem('userInfo')).user_id,
				auditState: $('.tab li.active').data('audit_state'),
				reviewState: opt.reviewstate
			};
			$.ajax({
				type: 'post',
				url: getScoreUrl,
				success: function(res) {
					if (res.state == 1) {
						renderReviewScore(res.data[0]);
					} else if (res.state == 0) {

						new Dialog().init({
							mask: true,
							title: '',
							content: res.message,
							isAlert: true
						});
					} else if (res.state == 2) {

						new Dialog().init({
							mask: true,
							title: '',
							content: res.message,
							isAlert: true,
							hideView: true,
							callback: function(boolean) {
								if (boolean) {
									top.location.href = loginUrl;
								}
							}
						});
					}
				},
				data: data,
				xhrFields: {
					withCredentials: true
				},
				crossDomain: true,
				error: function(err) {
					console.log('失败，错误原因：', err.message)
				}
			});
		} else if (e.target.className == 'enter-review') {
			if (opt.reviewstate == 1) {
				$('.right-content').removeClass('reviewstate_no');
				$('.right-content').addClass('reviewstate_ok');
			} else {
				$('.right-content').removeClass('reviewstate_ok');
				$('.right-content').addClass('reviewstate_no');
			}
			renderUI(opt);
		}
	});
	//退出评审
	$('.dialog-close').on('click', function() {
		destroyDialog();
	});

	function renderUI(opt) {
		$('.dialog-review').css('transform', 'scale(1)');
		//上面案例查看
		top.user_id = opt.uploaduserid;
		top.html_url = opt.htmlpath;
		top.file_type = opt.filetype;

		$('#case-preview').attr('src', templetUrl);

		//顶部标题
		$('.dialog-title').text(opt.filename);
		//下面表单
		renderReviewTable(opt);
		renderReviewTitle(opt);
	};

	function renderReviewTitle(opt) {

		var titleText;
		if (opt.reviewstate == '-1') {
			titleText = '继续评审';
		} else if (opt.reviewstate == '0') {
			titleText = '开始评审';
		} else {
			titleText = '评审已结束';

			$('.right-content input').attr('readonly', true).css('border', 'none');
		}
		$('.right-content h2').text(titleText);
	}

	function renderReviewTable(opt) {
		var tableProjectHTML;
		var tablePlanHTML;
		if (opt.reviewstate == 1) { //已评审（不可操作）
			tableProjectHTML = '<thead><tr><td>评审内容</td><td>评审事项</td><td>得分</td></tr></thead><tbody><tr><td>1.企业综合实力（10分）</td><td>申报单位的综合实力情况，包括注册资本、上市情况、人才团队、荣誉、软件著作权、专利、行业影响力等。</td><td><input  readonly="readonly" style="border: none;" data-max="10" name="firstScore " type="text"></td></tr><tr><td>2.材料内容完备性（10分）</td><td>申报材料结构合理性及描述清晰度，结构合理，描述清晰</td><td><input  readonly="readonly" style="border: none;" data-max="10" name="secondScore " type="text"></td></tr><tr><td>3.大数据产品数据处理及安全性评分指标（40分）</td><td>1.处理数据量规模。2.具备处理多元异构数据的能力。3.处理数据的效率。4.安全性、可扩展性、移植性。</td><td><input  readonly="readonly" style="border: none;" data-max="40" name="thirdScore " type="text"></td></tr><tr><td>4.大数据产品功能和技术评分指标（20分）</td><td>1.产品功能先进性。2.产品技术创新能力（第三方测试评估报告）。</td><td><input  readonly="readonly" style="border: none;" data-max="20" name="fourthScore " type="text"></td></tr><tr><td>5.经济社会效益（20分）</td><td>1.市场价值。2.盈利状况。3.应用效果（第三方用户报告）。4.社会效益。</td><td><input  readonly="readonly" style="border: none;" data-max="20" name="fifthScore " type="text"></td></tr><tr><td>综合得分</td><td></td><td class="total-score"></td></tr></tbody>';
			tablePlanHTML = '<thead><tr><td>评审内容</td><td>评审事项</td><td>得分</td></tr></thead><tbody><tr><td>1.企业综合实力（10分）</td><td>申报单位的综合实力情况，包括注册资本、上市情况、人才团队、荣誉、软件著作权、专利、行业影响力等。</td><td><input  readonly="readonly" style="border: none;" data-max="10" name="firstScore " type="text"></td></tr><tr><td>2.材料内容完备性（10分）</td><td>申报材料结构合理性及描述清晰度，结构合理，描述清晰</td><td><input  readonly="readonly" style="border: none;" data-max="10" name="secondScore " type="text"></td></tr><tr><td>3.大数据应用解决方案技术及架构评分指标（40分）</td><td>1.云存储、大数据分析、挖掘等先进技术的运用能力。2.系统架构完整性、可靠性及可扩展性。</td><td><input  readonly="readonly" style="border: none;" data-max="40" name="thirdScore " type="text"></td></tr><tr><td>4.大数据应用解决方案数据采集能力及推广评分指标（20分）</td><td>1.垂直行业数据采集能力及覆盖广度，跨行业融合创新能力。2.可复制性及可推广性。</td><td><input  readonly="readonly" style="border: none;" data-max="20" name="fourthScore " type="text"></td></tr><tr><td>5.经济社会效益（20分）</td><td>1.市场价值。2.盈利状况。3.应用效果（第三方用户报告）。4.社会效益。</td><td><input  readonly="readonly" style="border: none;" data-max="20" name="fifthScore " type="text"></td></tr><tr><td>综合得分</td><td></td><td class="total-score"></td></tr></tbody>';
		} else { //评审进行中（可操作）
			tableProjectHTML = '<thead><tr><td>评审内容</td><td>评审事项</td><td>得分</td><td>操作</td></tr></thead><tbody><tr><td>1.企业综合实力（10分）</td><td>申报单位的综合实力情况，包括注册资本、上市情况、人才团队、荣誉、软件著作权、专利、行业影响力等。</td><td><input data-max="10" name="firstScore " type="text"></td><td rowspan="6" style="position:relative"><span class="table-toolbar"><a href="javascript:;" data-review_state="-1" class="review-save">暂存</a><a href="javascript:;" data-review_state="1" class="review-submit">提交</a></span></td></tr><tr><td>2.材料内容完备性（10分）</td><td>申报材料结构合理性及描述清晰度，结构合理，描述清晰</td><td><input data-max="10" name="secondScore " type="text"></td></tr><tr><td>3.大数据产品数据处理及安全性评分指标（40分）</td><td>1.处理数据量规模。2.具备处理多元异构数据的能力。3.处理数据的效率。4.安全性、可扩展性、移植性。</td><td><input data-max="40" name="thirdScore " type="text"></td></tr><tr><td>4.大数据产品功能和技术评分指标（20分）</td><td>1.产品功能先进性。2.产品技术创新能力（第三方测试评估报告）。</td><td><input data-max="20" name="fourthScore " type="text"></td></tr><tr><td>5.经济社会效益（20分）</td><td>1.市场价值。2.盈利状况。3.应用效果（第三方用户报告）。4.社会效益。</td><td><input data-max="20" name="fifthScore " type="text"></td></tr><tr><td>综合得分</td><td></td><td class="total-score"></td></tr></tbody>';
			tablePlanHTML = '<thead><tr><td>评审内容</td><td>评审事项</td><td>得分</td><td>操作</td></tr></thead><tbody><tr><td>1.企业综合实力（10分）</td><td>申报单位的综合实力情况，包括注册资本、上市情况、人才团队、荣誉、软件著作权、专利、行业影响力等。</td><td><input data-max="10" name="firstScore " type="text"></td><td rowspan="6" style="position:relative"><span class="table-toolbar"><a href="javascript:;" data-review_state="-1" class="review-save">暂存</a><a href="javascript:;" data-review_state="1" class="review-submit">提交</a></span></td></tr><tr><td>2.材料内容完备性（10分）</td><td>申报材料结构合理性及描述清晰度，结构合理，描述清晰</td><td><input data-max="10" name="secondScore " type="text"></td></tr><tr><td>3.大数据应用解决方案技术及架构评分指标（40分）</td><td>1.云存储、大数据分析、挖掘等先进技术的运用能力。2.系统架构完整性、可靠性及可扩展性。</td><td><input data-max="40" name="thirdScore " type="text"></td></tr><tr><td>4.大数据应用解决方案数据采集能力及推广评分指标（20分）</td><td>1.垂直行业数据采集能力及覆盖广度，跨行业融合创新能力。2.可复制性及可推广性。</td><td><input data-max="20" name="fourthScore " type="text"></td></tr><tr><td>5.经济社会效益（20分）</td><td>1.市场价值。2.盈利状况。3.应用效果（第三方用户报告）。4.社会效益。</td><td><input data-max="20" name="fifthScore " type="text"></td></tr><tr><td>综合得分</td><td></td><td class="total-score"></td></tr></tbody>';
		}
		//生成表单
		if (opt.filetype == 0) { //产品申报书
			$('.right-content table').html(tableProjectHTML);
		} else if (opt.filetype == 1) { //方案申报书
			$('.right-content table').html(tablePlanHTML);
		}
		//验证输入 + 计算综合得分
		$('.right-content input').on('keyup', function() {
			numberFixedTwo(this, $(this).data('max'));
			var score = 0;
			$('.right-content input').toArray().map(function(input) {
				score += Number(input.value);
			});
			$('.total-score').text(score);
		});
	};

	function numberFixedTwo(input, max) {
		//数字+保留2位小数+限制大小
		input.value = input.value.replace(/[^\d.]/g, ""); //清除“数字”和“.”以外的字符
		input.value = input.value.replace(/\.{2,}/g, "."); //只保留第一个. 清除多余的
		input.value = input.value.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");
		input.value = input.value.replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3'); //只能输入两个小数
		if (input.value.indexOf(".") < 0 && input.value != "") { //以上已经过滤，此处控制的是如果没有小数点，首位不能为类似于 01、02的金额
			input.value = parseFloat(input.value);
		}

		if (Number(input.value) > Number(max)) {
			input.value = max;
		}
	}

	/****************************单位名称验证****************************/

	$('input[name="company_name"]').on('keyup', function() {
		if (this.value != '') {
			formCheck(this, companyCheck, 33);
		} else {
			$(this).parent().find('.info').remove();
		}
	});
	/****************************工具函数****************************/
	function getContainStrKey(str, obj) {
		for (var key in obj) {
			if (key.indexOf(str) != -1) {
				return key;
			}
		}
	}

	new Header({
		ele: $('.header')[0]
	});

	//end
})