$(function () {

	/****************************Tab切换部分****************************/

	$('.tab li').on('click', function () {
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
	/****************************条件筛选部分****************************/

	getOptionData('area_id', getAllProvinceUrl); //获取省份
	getOptionData('property_id', propertyUrl); //获取单位性质
	getOptionData('business_id', businessUrl); //获取主要业务
	getOptionData('field_id', applicationField); //应用领域

	//获取Option
	function getOptionData(cat, url) {
		$.ajax({
			type: 'post',
			url: url,
			xhrFields: {
				withCredentials: true
			},
			crossDomain: true,
			success: function (res) {
				if (res.state == 1) {
					renderOption(cat, res.data);
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
			error: function (err) {
				console.log('单位性质失败，错误原因：', err.message)
			}
		});
	}

	//生成Option
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

	}


	//应用领域选项切换

	$('select[name="file_type"]').on('change', function () {
		if ($(this).val() == 1) {
			$('select[name="field_id"]').parents('td').show()
		} else {
			$('select[name="field_id"]').val('');
			$('select[name="field_id"]').parents('td').hide()
		}
	});

	/****************************查询结果部分****************************/

	// 加载页面初始请求第一页
	getCaseData(1);

	function getCaseData(num) {

		if ($('.info').length <= 0) {
			var data = {
				audit_state: $('.tab .active').data('audit_state'), //必选
				pageNum: num, //必选
				file_type: $('[name="file_type"]').val(),
				area_id: $('[name="area_id"]').val(),
				property_id: $('[name="property_id"]').val(),
				quoted_state: $('[name="quoted_state"]').val(),
				company_name: $('input[name="company_name"]').val(),
				field_id: $('[name="field_id"]').val(),
				business_id: $('[name="business_id"]').val(),
                upload_time:$('input[name="upload_time"]').val()
			};
			$.ajax({
				type: 'post',
				url: historicalDataBrowseUrl,
				xhrFields: {
					withCredentials: true
				},
				data: data,
				crossDomain: true,
				success: function (res) {
					if (res.state == 1) {

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
							callback: function (boolean) {
								if (boolean) {
									top.location.href = loginUrl;
								}
							}
						});
					}
				},
				error: function (err) {
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
	$('.submit').on('click', function () {
		getCaseData(1);
	});

	//生成案例列表
	function renderCaseList(data) {
		var audit_state = $('.tab .active').data('audit_state'); //第几轮

		//清空表头

		$('thead.result-title').html('');
		$('tbody.result-list').html('');

		//生成表头
		var theadInnerHtml = '';
		if (audit_state == 0) {
			theadInnerHtml = '<tr><td>文件编号</td><td>申报文件</td><td>申报单位</td><td>评审状态</td><td>提交时间</td><td>案例操作</td></tr>';
		} else if (audit_state == 1) {
			theadInnerHtml = '<tr><td>文件编号</td><td>申报文件</td><td>申报单位</td><td>提交时间</td><td>第一轮评审得分</td><td>案例操作</td></tr>';
		} else if (audit_state == 2) {
			theadInnerHtml = '<tr><td>文件编号</td><td>申报文件</td><td>申报单位</td><td>提交时间</td><td>第二轮评审得分</td><td>案例操作</td></tr>';
		} else if (audit_state == 3) {
			theadInnerHtml = '<tr><td>文件编号</td><td>申报文件</td><td>申报单位</td><td>提交时间</td><td>第一轮评审得分</td><td>第二轮评审得分</td><td>两轮综合得分</td><td>案例操作</td></tr>';
		};

		$('thead.result-title').html(theadInnerHtml);

		//生成表体  --报错Uncaught TypeError: Cannot read property 'data' of undefined
		var list = data.data;

		if (list.length == 0) { //无符合结果
			var p = document.createElement('p');
			p.style.cssText = 'padding:20px 0;text-algin:center;width:100%';
			p.innerText = '没有您要查询的结果!';
			document.querySelector('.result-list').appendChild(p);
			$('#paging').html('');

		} else { //有符合结果,生成table

			var tbodyInnerHtml = '';

			for (var i = 0; i < list.length; i++) {
				var previewSwitch;
				var item = list[i];

				//格式化参数
				format_file_path(item); //下载链接
				format_upload_time(item); //上传时间

				if (audit_state == 0) { //原始案例查询

					previewSwitch = 'on';

					if (item.audit_state == '0') {
						item.audit_state = '进入初审';
					} else if (item.audit_state == '1') {
						item.audit_state = '进入中审';
					} else if (item.audit_state == '2') {
						item.audit_state = '进入终审';
					} else if (item.audit_state == '3') {
						item.audit_state = '成功入选';
					} else if (item.audit_state == '-1') {
						item.audit_state = '未入选';
					} else if (item.audit_state == '-2') {
						item.audit_state = '初审补充材料';
					}

					tbodyInnerHtml += '<tr' + setDataString(item) + '><td>' + item.file_code + '</td><td>' + item.old_file_name + '</td><td>' + item.company_name + '</td><td>' + item.audit_state + '</td><td>' + item.upload_time + '</td><td><a href="javascript:;" class="preview ' + previewSwitch + '">查看</a><span class="split">|</span><a href="' + item.file_path + '" class="download">下载</a></td></tr>';

				} else if (audit_state == 1) { //第一轮评审结果查询
					if (parseInt(item.first_score) == -1 || item.first_count == 0) {
						item.first_score = '暂无评分';
						previewSwitch = 'off';
					} else {
						item.first_score = (Number(item.first_score) / item.first_count).toFixed(2);
						previewSwitch = 'on';
					}

					tbodyInnerHtml += '<tr' + setDataString(item) + '><td>' + item.file_code + '</td><td>' + item.old_file_name + '</td><td>' + item.company_name + '</td><td>' + item.upload_time + '</td><td>' + item.first_score + '</td><td><a href="javascript:;" class="preview ' + previewSwitch + '">查看</a><span class="split">|</span><a href="' + item.file_path + '" class="download">下载</a></td></tr>';
				} else if (audit_state == 2) { //第二轮评审结果查询
					if (parseInt(item.second_score) == -1 || item.second_count == 0) {
						item.second_score = '暂无评分';
						previewSwitch = 'off';
					} else {
						item.second_score = (Number(item.second_score) / item.second_count).toFixed(2);
						previewSwitch = 'on';
					}
					tbodyInnerHtml += '<tr' + setDataString(item) + '><td>' + item.file_code + '</td><td>' + item.old_file_name + '</td><td>' + item.company_name + '</td><td>' + item.upload_time + '</td><td>' + item.second_score + '</td><td><a href="javascript:;" class="preview ' + previewSwitch + '">查看</a><span class="split">|</span><a href="' + item.file_path + '" class="download">下载</a></td></tr>';
				} else if (audit_state == 3) { //最终入选案例查询
					previewSwitch = 'on';
					//第一轮得分
					item.first_score = Number((Number(item.first_score) / item.first_count).toFixed(2));
					//第二轮得分
					item.second_score = Number((Number(item.second_score) / item.second_count).toFixed(2));
					//第三轮得分
					item.third_score = ((item.first_score + item.second_score) / 2).toFixed(2);
					tbodyInnerHtml += '<tr' + setDataString(item) + '><td>' + item.file_code + '</td><td>' + item.old_file_name + '</td><td>' + item.company_name + '</td><td>' + item.upload_time + '</td><td>' + item.first_score + '</td><td>' + item.second_score + '</td><td>' + item.third_score + '</td><td><a href="javascript:;" class="preview ' + previewSwitch + '">查看</a><span class="split">|</span><a href="' + item.file_path + '" class="download">下载</a></td></tr>';
				}
			};

			$('tbody.result-list').html(tbodyInnerHtml);

			//开始分页
			try {
				//生成分页导航

				$('#paging').jqPaginator({
					totalCounts: data.count,
					pageSize: data.pageSize,
					currentPage: data.pageNum,
					first: '<li class="first"><a href="javascript:void(0);">首页</a></li>',
					prev: '<li class="prev"><a href="javascript:void(0);">上一页</a></li>',
					next: '<li class="next"><a href="javascript:void(0);">下一页</a></li>',
					last: '<li class="last"><a href="javascript:void(0);">末页</a></li>',
					page: '<li class="page"><a href="javascript:void(0);">{{page}}</a></li>',
					onPageChange: function (num) {
						getCaseData(num);
					}
				});
			} catch (err) {

			}

		}
	};

	function format_file_path(item) {
		item.file_path = downLoadWordUrl + '?old_file_name=' + item.old_file_name + '&file_path=' + item.file_path;
	};


	function format_upload_time(item) {
		item.upload_time = new Date(item.upload_time).toLocaleDateString();
	};

	function setDataString(data) {

		var aKey = Object.keys(data).map(function (key) {
			return ' data-' + key + '="' + data[key] + '" ';
		});
		return aKey.join('');
	};
	/****************************案例查看部分****************************/

	//进入查看
	$('.result-list').on('click', function (e) {
		if (e.target.className == 'preview on') {
			renderDialog(e);
		}
	});

	function renderDialog(e) {
		$('.dialog-review').css('transform', 'scale(1)');
		//整理数据源
		var audit_state = $('.tab .active').data('audit_state');
		var customData = $(e.target).parents('tr').data();

		var topData = {
			audit_state: audit_state,
			old_file_name: customData.old_file_name
		};
		var leftData = {
			audit_state: audit_state,
			templetUrl: templetUrl,
			user_id: customData.user_id,
			html_path: customData.html_path,
			file_type: customData.file_type
		};
		var rightData = {
			audit_state: audit_state,
			file_type: customData.file_type
		};
		//生成对话框顶部
		renderDialogTop(topData);
		//生成对话框左侧
		renderDialogLeft(leftData);
		//生成对话框右侧（如果不是原始案例页）
		if (audit_state != 0) {
			if (audit_state == 3) {
				$('.right-content').removeClass('audit_state_12');
				$('.right-content').addClass('audit_state_3');
			} else {
				$('.right-content').removeClass('audit_state_3');
				$('.right-content').addClass('audit_state_12');
			}

			var sendData = {
				audit_state: rightData.audit_state,
				screen_id: customData.screen_id
			};
			$.ajax({
				type: 'post',
				url: getFinalScoreUrl,
				success: function (res) {
					if (res.state == 1 && res.data != null) {
						//生成评分栏
						rightData.list = res.data;
						renderDialogRight(rightData);
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
							callback: function (boolean) {
								if (boolean) {
									top.location.href = loginUrl;
								}
							}
						});
					}
				},
				data: sendData,
				xhrFields: {
					withCredentials: true
				},
				crossDomain: true,
				error: function (err) {
					console.log('失败，错误原因：', err.message)
				}
			});
		}

	};
	//顶部
	function renderDialogTop(data) {
		$('.dialog-title', top.document).text(data.old_file_name);
	};
	//左侧
	function renderDialogLeft(data) {

		top.user_id = data.user_id;
		top.html_url = data.html_path;
		top.file_type = data.file_type;

		$('.dialog-review', top.document).find('#case-preview').attr('src', data.templetUrl);
		//如果位于原始案例查询页，隐藏右侧评分栏
		if (data.audit_state == 0) {
			//左侧全屏
			$('.left-content', top.document).addClass('left-content-max');
			//右侧隐藏
			$('.right-content', top.document).addClass('right-content-hide right-content-border-none');

		} else {
			//左侧非全屏
			$('.left-content', top.document).removeClass('left-content-max');
			//右侧显示
			$('.right-content', top.document).removeClass('right-content-hide right-content-border-none');

		}
	};

	//获取第n轮数据
	function getItem(data, audit_state) {
		for (var i = 0; i < data.list.length; i++) {
			if (Number(data.list[i].audit_state) == audit_state) {
				return data.list[i];
			}
		}
	};
	//右侧
	function renderDialogRight(data) {
		
		var table_title;
		var tableInnerHtml;
		//标题
		if (data.audit_state == 1) {
			table_title = '第一轮';
		} else if (data.audit_state == 2) {
			table_title = '第二轮';
		} else if (data.audit_state == 3) {
			table_title = '最终';
		}

		if (data.audit_state != 3) { //第一轮或者第二轮
			var third_td1;
			var third_td2;
			var third_td3;
			var third_td4;
			var item = getItem(data, data.audit_state);
			
			var first_score = (item.first_score / item.count).toFixed(2);
			var second_score = (item.second_score / item.count).toFixed(2);
			var third_score = (item.third_score / item.count).toFixed(2);
			var fourth_score = (item.fourth_score / item.count).toFixed(2);
			var fifth_score = (item.fifth_score / item.count).toFixed(2);
			var sum_score = (item.sum_score / item.count).toFixed(2);
			//Table

			if (data.file_type == 0) { //产品
				$('.table-wrap h2').text('产品申报书' + table_title + '评审得分');
				third_td1 = '3.大数据产品数据处理及安全性评分指标（40分）';
				third_td2 = '1.处理数据量规模。2.具备处理多元异构数据的能力。3.处理数据的效率。4.安全性、可扩展性、移植性。';

				third_td3 = '4.大数据产品功能和技术评分指标（20分）';
				third_td4 = '1.产品功能先进性。2.产品技术创新能力（第三方测试评估报告）';

			} else if (data.file_type == 1) { //方案
				$('.table-wrap h2').text('方案申报书' + table_title + '评审得分');
				third_td1 = '3.大数据应用解决方案技术及架构评分指标（40分）';
				third_td2 = '1.云存储、大数据分析、挖掘等先进技术的运用能力。2.系统架构完整性、可靠性及可扩展性';

				third_td3 = '4.大数据应用解决方案数据采集能力及推广评分指标（20分）';
				third_td4 = '1.垂直行业数据采集能力及覆盖广度，跨行业融合创新能力。2.可复制性及可推广性。';
			}

			tableInnerHtml = '<thead>' +
				'<tr>' +
				'<td>评审内容</td>' +
				'<td>评审事项</td>' +
				'<td>得分</td>' +
				'</tr>' +
				'</thead>' +
				'<tbody>' +
				'<tr>' +
				'<td>1.企业综合实力（10分）</td>' +
				'<td>申报单位的综合实力情况，包括注册资本、上市情况、人才团队、荣誉、软件著作权、专利、行业影响力等。</td>' +
				'<td>' + first_score + '</td>' +
				'</tr>' +
				'<tr>' +
				'<td>2.材料内容完备性（10分）</td>' +
				'<td>申报材料结构合理性及描述清晰度，结构合理，描述清晰</td>' +
				'<td>' + second_score + '</td>' +
				'</tr>' +
				'<tr>' +
				'<td>' + third_td1 + '</td>' +
				'<td>' + third_td2 + '</td>' +
				'<td>' + third_score + '</td>' +
				'</tr>' +
				'<tr>' +
				'<td>' + third_td3 + '</td>' +
				'<td>' + third_td4 + '</td>' +
				'<td>' + fourth_score + '</td>' +
				'</tr>' +
				'<tr>' +
				'<td>5.经济社会效益（20分）</td>' +
				'<td>1.市场价值。2.盈利状况。3.应用效果（第三方用户报告）。4.社会效益。</td>' +
				'<td>' + fifth_score + '</td>' +
				'</tr>' +
				'<tr>' +
				'<td>综合得分</td>' +
				'<td></td>' +
				'<td class="total-score">' + sum_score + '</td>' +
				'</tr>' +
				'</tbody>';


		} else if (data.audit_state == 3) { //第三轮
			var item1 = getItem(data, 1);
			var item2 = getItem(data, 2);
			console.log(data.list)
			console.log(item1, item2);
			debugger;
			//第一轮得分
			var item1_first_score = (item1.first_score / item1.count).toFixed(2);
			var item1_second_score = (item1.second_score / item1.count).toFixed(2);
			var item1_third_score = (item1.third_score / item1.count).toFixed(2);
			var item1_fourth_score = (item1.fourth_score / item1.count).toFixed(2);
			var item1_fifth_score = (item1.fifth_score / item1.count).toFixed(2);
			var item1_sum_score = (item1.sum_score / item1.count).toFixed(2);
			//第二轮得分
			var item2_first_score = (item2.first_score / item2.count).toFixed(2);
			var item2_second_score = (item2.second_score / item2.count).toFixed(2);
			var item2_third_score = (item2.third_score / item2.count).toFixed(2);
			var item2_fourth_score = (item2.fourth_score / item2.count).toFixed(2);
			var item2_fifth_score = (item2.fifth_score / item2.count).toFixed(2);
			var item2_sum_score = (item2.sum_score / item2.count).toFixed(2);
			//综合得分
			var first_sum_score = ((Number(item1_first_score) + Number(item2_first_score)) / 2).toFixed(2);
			var second_sum_score = ((Number(item1_second_score) + Number(item2_second_score)) / 2).toFixed(2);
			var third_sum_score = ((Number(item1_third_score) + Number(item2_third_score)) / 2).toFixed(2);
			var fourth_sum_score = ((Number(item1_fourth_score) + Number(item2_fourth_score)) / 2).toFixed(2);
			var fifth_sum_score = ((Number(item1_fifth_score) + Number(item2_fifth_score)) / 2).toFixed(2);
			var final_sum_score = ((Number(item1_sum_score) + Number(item2_sum_score)) / 2).toFixed(2);
			//Table
			if (data.file_type == 0) { //产品
				$('.table-wrap h2').text('产品申报书' + table_title + '评审得分');
				third_td1 = '3.大数据产品数据处理及安全性评分指标（40分）';
				third_td2 = '4.大数据产品功能和技术评分指标（20分）';
			} else if (data.file_type == 1) { //方案
				$('.table-wrap h2').text('方案申报书' + table_title + '评审得分');
				third_td1 = '3.大数据应用解决方案技术及架构评分指标（40分）';
				third_td2 = '4.大数据应用解决方案数据采集能力及推广评分指标（20分）';
			}
			tableInnerHtml = '<thead>' +
				'<tr>' +
				'<td>评审内容</td>' +
				'<td>第一轮得分</td>' +
				'<td>第二轮得分</td>' +
				'<td>综合得分</td>' +
				'</tr>' +
				'</thead>' +
				'<tbody>' +
				'<tr>' +
				'<td>1.企业综合实力（10分）</td>' +
				'<td>' + item1_first_score + '</td>' +
				'<td>' + item2_first_score + '</td>' +
				'<td>' + first_sum_score + '</td>' +
				'</tr>' +
				'<tr>' +
				'<td>2.材料内容完备性（10分）</td>' +
				'<td>' + item1_second_score + '</td>' +
				'<td>' + item2_second_score + '</td>' +
				'<td>' + second_sum_score + '</td>' +
				'</tr>' +
				'<tr>' +
				'<td>' + third_td1 + '</td>' +
				'<td>' + item1_third_score + '</td>' +
				'<td>' + item2_third_score + '</td>' +
				'<td>' + third_sum_score + '</td>' +
				'</tr>' +
				'<tr>' +
				'<td>' + third_td2 + '</td>' +
				'<td>' + item1_fourth_score + '</td>' +
				'<td>' + item2_fourth_score + '</td>' +
				'<td>' + fourth_sum_score + '</td>' +
				'</tr>' +
				'<tr>' +
				'<td>5.经济社会效益（20分）</td>' +
				'<td>' + item1_fifth_score + '</td>' +
				'<td>' + item2_fifth_score + '</td>' +
				'<td>' + fifth_sum_score + '</td>' +
				'</tr>' +
				'<tr>' +
				'<td>综合得分</td>' +
				'<td>' + item1_sum_score + '</td>' +
				'<td>' + item2_sum_score + '</td>' +
				'<td class="total-score">' + final_sum_score + '</td>' +
				'</tr>' +
				'</tbody>';
		}
		$('.right-content table').html(tableInnerHtml);
	};

	//退出查看
	$('.dialog-close', top.document).on('click', function () {
		destroyDialog();
	});

	function destroyDialog() {
		/* 整体 */
		$('.dialog-review', top.document).css('transform', 'scale(0)'); //隐藏对话框
		/* 顶部 */
		$('.dialog-title', top.document).text(''); //清空顶部标题
		/* 左侧 */
		$('#case-preview', top.document).attr('src', ''); //清空左侧iframe
		/* 右侧 */
		$('.table-wrap h2', top.document).text('');

	};

	/****************************输入验证****************************/
	$('input[name="company_name"]').on('keyup', function () {
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
	};

	new Header({
		ele: $('.header')[0]
	});
	//end
})