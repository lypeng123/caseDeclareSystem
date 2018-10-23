$(function () {

	/**********************Tab切换部分**********************/

	$('.tab li').on('click', function () {
		if ($(this).attr('class') == 'active') {
			return false;
		} else {
			$(this).addClass('active').siblings().removeClass('active'); //变色
			$('td.enterprise,td.scoring').css('display', 'none');
			$('td.' + $(this).data('cat') + '').css('display', 'table-cell');

			getDataList(1); //请求默认第一页
		}
	});


	getDataList(1);

	function getDataList(num) {

		var cat = $('.tab li.active').data('cat');

		if (cat == 'enterprise') { //查询企业信息
			var ajaxUrl = enterpriseInformationUrl;
			var ajaxData = {
				audit_state: $('td.enterprise select[name="audit_state"]').val(), //企业筛选
				pageNum: num //请求页数
			};
			var successCallback = function (data) {
				renderEnterpriseList(data);
			}
		} else if (cat == 'scoring') { //查询打分结果

			var ajaxUrl = scoringResultsUrl;
			var ajaxData = {
				audit_state: $('td.scoring select[name="audit_state"]').val(), //打分轮次
				file_type: $('td.scoring select[name="file_type"]').val(), //案例分类
				pageNum: num //请求页数
			};

			var successCallback = function (data) {
				renderScoringList(data);
			};

		};

		$.ajax({
			type: 'post',
			url: ajaxUrl,
			xhrFields: {
				withCredentials: true
			},
			data: ajaxData,
			crossDomain: true,
			success: function (res) {
				//清空容器
				$('.result-title').html('');
				$('.result-list').html('');

				if (res.state == 1) {
					successCallback(res.data.pageBeanMysql);
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
				$('.result-title').html('');
				$('.result-list').html('');

				console.log('请求失败，错误原因：', err)
			}
		});

	};
	$('.submit').on('click', function () {
		getDataList(1);
	});

	//生成企业信息
	function renderEnterpriseList(data) {

		var list = data.data;
		if (list.length == 0) {
			var p = document.createElement('p');
			p.style.cssText = 'padding:20px 0;text-algin:center;width:100%';
			p.innerText = '没有您要查询的结果!';
			document.querySelector('.result-list').appendChild(p);
			$('#paging').html('');
			return;
		};
		
		var theadInnerHtml = '<tr><td>申报单位</td><td>单位负责人姓名</td><td>单位所在省市</td><td>联系电话</td><td>注册资本（万元）</td><td>统一社会信用代码</td></tr>';
		var tbodyInnerHtml = '';
		for (var i = 0; i < list.length; i++) {
			if(list[i].register_capital == undefined){
				list[i].register_capital = '' ;
			}
			tbodyInnerHtml += '<tr><td>' + list[i].company_name + '</td><td>' + list[i].responsible_person + '</td><td>' + list[i].area_name + '</td><td>' + list[i].declare_phone + '</td><td>' + list[i].register_capital + '</td><td>' + list[i].credit_code + '</td></tr>';
		};

		$('.result-title').html(theadInnerHtml);
		$('.result-list').html(tbodyInnerHtml);

		//分页
		try {
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
					getDataList(num);
				}
			});
		} catch (err) {

		}

	};


	//生成打分结果的table
	function renderScoringList(data) {

		//获取后端返回的数据对象（默认10条）
		var list = data.data;

		//没有查询到结果情况
		if (list.length == 0) {
			var p = document.createElement('p');
			p.style.cssText = 'padding:20px 0;text-algin:center;width:100%';
			p.innerText = '没有您要查询的结果!';
			document.querySelector('.result-list').appendChild(p);
			$('#paging').html('');

		} else { //查询到结果

			//以第一条数据对象为基准，正则匹配出对象里所有包含"中文字符"的键放进数组expertNames，生成一个固定的专家名单;

			var expertNames = Object.keys(list[0]).filter(function (key) {
				return /[\u4E00-\u9FA5]/.test(key);
			});

			//生成表头td标签
			var theadInnerHtml = '<tr><td>文件编号</td><td>申报文件</td><td>申报单位</td><td>评审综合得分</td>' + getTheadTd(expertNames) + '</tr>';

			//生成表体td标签
			var tbodyInnerHtml = '';

			for (var i = 0; i < list.length; i++) {
				//生成当前行专家部分的td标签
				tbodyInnerHtml += '<tr><td>' + list[i].file_code + '</td><td>' + list[i].file_name + '</td><td>' + list[i].company_name + '</td><td>' + list[i].sum_score + '</td>' + getTbodyTd(list[i], expertNames) + '</tr>';
			};

			//填充容器：表头+表体
			$('.result-title').html(theadInnerHtml);
			$('.result-list').html(tbodyInnerHtml);

			//开始分页
			try {
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
						getDataList(num);
					}
				});
			} catch (err) {

			}
		}

	};

	//生成表头专家部分td标签
	function getTheadTd(expertNames) {

		//循环专家列表，将专家名字放在td里
		var theadTd = '';

		for (var i = 0; i < expertNames.length; i++) {
			theadTd += '<td>' + expertNames[i] + '</td>'
		};
		return theadTd;
	};

	//生成表体专家部分td标签，从当前行的数据对象里挨个获取专家们的分数
	function getTbodyTd(currentRowData, expertNames) {

		var tbodyTd = '';
		for (var i = 0; i < expertNames.length; i++) {
			var expertName = expertNames[i];

			if (currentRowData[expertName] == null) {
				currentRowData[expertName] = '';
			}
			tbodyTd += '<td>' + currentRowData[expertName] + '</td>'
		}

		return tbodyTd;
	}

	//导出到excel

	$('.table-export').on('click', function () {
		if ($('.result-list tr').length > 0) {

			var content, url, search;
			var cat = $('.tab li.active').data('cat');
			var dsc = $('.tab li.active').text();

			if (cat == 'enterprise') {//导出企业信息
				content = '<div style="text-align:left;width:70%;display:inline-block;line-height:1.5em;font-size:14px;"><b style="color:red;margin-bottom:6px;display:inline-block;">请确认导出条件：</b><br>导出栏目：' + dsc + '<br>企业筛选：' + $('td.enterprise select[name="audit_state"] option:selected').text() + '</div>';
				url = exportEnterpriseInfoUrl;
				search = '?audit_state=' + $('td.enterprise select[name="audit_state"]').val();

			} else if (cat == 'scoring') {//导出打分结果
				content = '<div style="text-align:left;width:70%;display:inline-block;line-height:1.5em;font-size:14px;"><b style="color:red;margin-bottom:6px;display:inline-block;">请确认导出条件：</b><br>导出栏目：' + dsc + '<br>打分轮次：' + $('td.scoring select[name="audit_state"] option:selected').text() + '<br>案例分类：' + $('td.scoring select[name="file_type"] option:selected').text() + '</div>';
				url = exportScoreExcelUrl;
				search = '?audit_state=' + $('td.scoring select[name="audit_state"]').val() + '&file_type=' + $('td.scoring select[name="file_type"]').val();

			};

			new Dialog().init({
				mask: true,
				title: '',
				content: content,
				isAlert: false,
				callback: function (boolean) {
					if (boolean) {
						var aTag = document.createElement('a');
						aTag.href = url + search;
						aTag.download = dsc;
						aTag.click();
					}

				}
			});

		} else {
			new Dialog().init({
				mask: true,
				title: '',
				content: '当前没有可导出的结果!',
				isAlert: true,
				callback: function (boolean) {

				}
			});
		}
	})


	new Header({
		ele: $('.header')[0]
	});

	//end
})