
renderIframe(top.user_id, top.html_url, top.file_type);

function renderIframe(user_id, html_url, file_type) {

	var DataTemplet = {
		user_id: '',
		company_name: '', //单位名称
		responsible_person: '', //单位负责人
		responsible_phone: '', //负责人联系电话
		responsible_mail: '', //负责人邮箱
		declare_person: '', //申报人姓名
		declare_phone: '', //申报人联系电话
		declare_mail: '', //申报人邮箱
		register_capital: '', //注册资本
		corporate_jurisdical_person: '', //法定代表人
		company_register_address: '', //单位注册地址
		company_office_address: '', //单位办公地址
		credit_code: '', //统一社会信用代码
		property_id: '', //单位性质
		property_describe: '', //单位性质描述（其他）
		business_data: { //主营业务
			data: []
		},
		business_describe: '', //主营业务描述（其他）
		quoted_state: '', //是否上市
		quoted_time: '', //上市时间
		quoted_address: '', //上市地点
		shares_code: '', //股票代码
		export_state: '', //是否有业务出口
		export_address: '', //主要出口地点
		honor_data: {
			data: []
		}, //相关荣誉
		honor_describe: '', //荣誉描述（其他）
		development_ability: '', //研发能力
		business_income: '', //2016年主营业务收入
		development_investment: '', //2016年研发投入
		data_income: '', //2016大数据业务收入
		tax_nums: '', //2016年税金总额
		profit: '', //2016年净利润
		company_person_nums: '', //单位总人数
		development_person_nums: '', //研发人员规模
		product_incom: '', //产品收入总额
		information_income: '', //服务收入总额
		software_income: '' //软件收入总额
	};
	var DomTreePrecess = [];
	var errorElements = [];
	var submitState = true;
	var DataTempletInit = $.extend(true, {}, DataTemplet);

	function uniq(arr) {
		var res = [];
		var json = {};
		for (var i = 0, len = arr.length; i < len; i++) {
			if (!json[arr[i]]) {
				res.push(arr[i]);
				json[arr[i]] = 1;
			}
		}
		return res;
	}

	/******************************远程html******************************/
	
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

			$('.remote-html').html(data);
		},
		xhrFields: {
			withCredentials: true
		},
		crossDomain: true,
		error: function (err) {
			console.log('失败，错误原因：', err)
		}
	});

	/******************************本地html模板******************************/

	//获取用户的申报书数据，填充页面（在单位性质和主营业务同时加载完毕后执行）

	function getDeclare() {

		//获取k-v形式的数据
		$.ajax({
			type: 'post',
			url: allData,
			success: function (res) {
				if (res.state == 1) {

					renderDeclare(res.data);
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
						hideView:true,
						callback: function (boolean) {
							if (boolean) {
								top.location.href = loginUrl;
							}
						}
					});

				}
			},
			data: {
				user_id: user_id
			},
			xhrFields: {
				withCredentials: true
			},
			crossDomain: true,
			error: function (err) {
				console.log('主营业务失败，错误原因：', err.message)
			}
		});

		//登陆后获取页面主营业务数据
		$.ajax({
			type: 'post',
			url: businessInfoDatas,
			success: function (res) {
				if (res.state == 1) {
					res.data.forEach(function (item) {
						$('input[name="business_id"][value="' + item.business_id + '"]').prop('checked', true);
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
						hideView:true,
						callback: function (boolean) {
							if (boolean) {
								top.location.href = loginUrl;
							}
						}
					});
				}
			},
			data: {
				user_id: user_id
			},
			xhrFields: {
				withCredentials: true
			},
			crossDomain: true,
			error: function (err) {
				console.log('失败，错误原因：', err.message)
			}
		});

		//登陆后获取相关荣誉数据
		$.ajax({
			type: 'post',
			url: honorInfoDatas,
			success: function (res) {
				if (res.state == 1) {

					var inputs = document.querySelectorAll('.honor-box li input');
					for (var i = 0, len = inputs.length; i < len - 1; i++) {
						var input = inputs[i];
						var honor_id = $(input.parentNode.parentNode).data('honor_id').toString();
						var honor_level = $(input.parentNode).data('honor_level').toString();
						input.value = getYear(res.data, honor_id, honor_level);
					};
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
						hideView:true,
						callback: function (boolean) {
							if (boolean) {
								top.location.href = loginUrl;
							}
						}
					});
				}
			},
			data: {
				user_id: user_id
			},
			xhrFields: {
				withCredentials: true
			},
			crossDomain: true,
			error: function (err) {
				console.log('主营业务失败，错误原因：', err.message)
			}
		});

	};

	function getYear(data, honor_id, honor_level) {

		for (var i = 0; i < data.length; i++) {
			if (data[i].honor_id == honor_id && data[i].honor_level == honor_level) {
				return data[i].get_year;
			}
		}
		return '';
	}

	function renderDeclare(data) {

		sessionStorage.setItem('declareInfo', JSON.stringify(data));

		for (var key in data) {

			if (key == 'property_id' && data[key] != '') {
				$('input[name="' + key + '"][value="' + data[key] + '"]').prop('checked', true);
			} else if (key == 'business_data') { //引用类型

			} else if (key == 'honor_data') { //引用类型

			} else if ((key == 'quoted_state' || key == 'export_state') && data[key] != '') {
				var series = '.' + key.split('_')[0];
				$('input[name="' + key + '"][value="' + data[key] + '"]').prop('checked', true);
				if (data[key] == '0') {
					$(series + ' ' + 'input[type="text"]').prop({
						'disabled': true,
						'title': '仅在选择"是"的情况下可填写'
					}).css('cursor', 'not-allowed');
					$(series + ' ' + 'input[type="text"]').val('');
				}

			} else {
				$('[name="' + key + '"]').val(data[key]);
			}
		}
	}

	//远程获取[单位性质]列表，填充DOM树
	$.ajax({
		type: 'post',
		url: propertyUrl,
		xhrFields: {
			withCredentials: true
		},
		crossDomain: true,
		success: function (res) {
			if (res.state == 1) {
				renderRemoteData(res, 'property');
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
					hideView:true,
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

	//远程获取[主营业务]列表，填充DOM树
	$.ajax({
		type: 'post',
		url: businessUrl,
		success: function (res) {
			if (res.state == 1) {
				renderRemoteData(res, 'business');
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
					hideView:true,
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
			console.log('主营业务失败，错误原因：', err.message)
		}
	});

	//渲染远程数据 [单位性质]和[主营业务]
	function renderRemoteData(res, series) {
		var list = res.data;
		var name = series + '_id';
		var valueKey = series + '_id';
		var box = document.querySelector('.' + series + '-box');
		var nameKey = series + '_name';
		var type;

		if (series === 'property') {
			type = 'radio';
		} else if (series === 'business') {
			type = 'checkbox';
		};

		for (var i = 0, len = list.length; i <= len; i++) { //此处多遍历一次，为填充[其他]预留位置

			var li = document.createElement('li');

			if (i < len) {
				li.innerHTML = '<label><input  name="' + name + '" type="' + type + '" value="' + list[i][valueKey] + '">' + list[i][nameKey] + '</label>';
			} else {

				li.innerHTML = '其他（请注明）：<input name="' + series + '_describe"  placeholder="（选填）" class="nomust" type="text"><span class="waring"></span> </label>';
			}

			box.appendChild(li);
			li = null;
		}
		//DOM完全渲染完毕
		DomTreePrecess.push(series);
		if (DomTreePrecess.length >= 2) {
			getDeclare();
		}

	};

};