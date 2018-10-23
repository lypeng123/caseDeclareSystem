window.onload = function () {

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


	//获取用户的申报书数据，填充页面（在单位性质和主营业务同时加载完毕后执行）

	function getDeclare() {

		//获取k-v形式的数据
		$.ajax({
			type: 'post',
			url: allData,
			success: function (res) {
				if (res.state == 1) {
					sessionStorage.setItem('all_data', JSON.stringify(res.data));
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
						hideView: true,
						callback: function (boolean) {
							if (boolean) {
								top.location.href = loginUrl;
							}
						}
					});

				}
			},
			data: {
				user_id: JSON.parse(sessionStorage.getItem('userInfo')).user_id
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
						hideView: true,
						callback: function (boolean) {
							if (boolean) {
								top.location.href = loginUrl;
							}
						}
					});
				}
			},
			data: {
				user_id: JSON.parse(sessionStorage.getItem('userInfo')).user_id
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
						hideView: true,
						callback: function (boolean) {
							if (boolean) {
								top.location.href = loginUrl;
							}
						}
					});
				}
			},
			data: {
				user_id: JSON.parse(sessionStorage.getItem('userInfo')).user_id
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

	//如果该用户下载过word，默认展开word框
	// (function () {
	// 	var showWordList = localStorage.getItem('showWordList');
	// 	var user_id = JSON.parse(sessionStorage.userInfo).user_id;
	// 	if (showWordList) {
	// 		var showWordList = JSON.parse(showWordList);
	// 		showWordList.forEach(function (local_user_id) {
	// 			if (user_id == local_user_id) {
	// 				$('.word').css('display', 'block');
	// 			}
	// 		})
	// 	}
	// })();

	//进入案例上传
	// $('.enter-scheme').on('click', function () {
	// 	top.location.href = 'upload.html';
	// });

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
				li.innerHTML = '<label><input name="' + name + '" type="' + type + '" value="' + list[i][valueKey] + '">' + list[i][nameKey] + '</label>';
			} else {

				li.innerHTML = '其他（请注明）：<input name="' + series + '_describe"  placeholder="（选填）" class="nomust" type="text"><span class="waring"></span> </label>';
			}

			box.appendChild(li);
			li = null;
		}

		//绑定事件
		bindEvent(series);

		//DOM完全渲染完毕
		DomTreePrecess.push(series);
		if (DomTreePrecess.length >= 2) {
			getDeclare();
			bingCheckEvent();
		}

	};

	//上市公司或者业务出口的单选按钮为否时，输入框禁止输入
	bindEvent('.quoted');
	bindEvent('.export');

	function bindEvent(series) {
		if (series == 'property') {
			$('.property-box input[type="text"]').on('focus', function () {
				$('.property-box input[type="radio"]').prop('checked', false)
			});
			$('.property-box input[type="radio"]').on('change', function () {
				if (this.checked == true) {
					$('.property-box input[type="text"]').val('');
				}
			});
		}
		if (series == '.quoted' || series == '.export') {
			$(series + ' ' + 'input[type="radio"]').on('change', function () {
				if (this.checked == true && this.value == '0') {
					$(series + ' ' + 'input[type="text"]').prop({
						'disabled': true,
						'title': '仅在选择"是"的情况下可填写'
					}).css('cursor', 'not-allowed').prev('span').css('color', '#333');
					$(series + ' ' + 'input[type="text"]').val('');
				} else {
					$(series + ' ' + 'input[type="text"]').prop({
						'disabled': false,
						'title': ''
					}).css('cursor', 'initial')
				}
			})
		}

	};

	function clearErrorElementsStyle() {

		errorElements.forEach(function (node) {
			node.style.color = '#333';
		})

	};


	//保存 
	$('.save button').on('click', function () {

		clearErrorElementsStyle();
		if (checkForm()) {
			DataTemplet['business_data'] = JSON.stringify(DataTemplet['business_data']);
			DataTemplet['honor_data'] = JSON.stringify(DataTemplet['honor_data']);
			DataTemplet['user_id'] = JSON.parse(sessionStorage.getItem('userInfo')).user_id;
			$.ajax({
				type: 'post',
				url: saveUrl,
				success: function (res) {
					if (res.state == 1) {

						new Dialog().init({
							mask: true,
							title: '',
							// content: '保存成功！请下载并填写页面底部的Word文件！',
							content: '保存成功！',
							isAlert: true,
							callback: function (boolean) {


							}
						});

						saveDone(res);
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
					} else {
						new Dialog().init({
							mask: true,
							title: '',
							content: res.message,
							isAlert: true,
							callback: function (boolean) {

							}
						});
					}
				},
				data: DataTemplet,
				xhrFields: {
					withCredentials: true
				},
				crossDomain: true,
				error: function (err) {
					new Dialog().init({
						mask: true,
						title: '',
						content: '提交失败：' + err.statusText,
						isAlert: true,
						callback: function (boolean) {

						}
					});

				},
				complete: function () {
					//深拷贝初始对象
					DataTemplet = $.extend(true, {}, DataTempletInit);
				}
			});
		} else {
			DataTemplet = $.extend(true, {}, DataTempletInit);
			new Dialog().init({
				mask: true,
				title: '',
				content: '填写有误，请检查表单中标注的<span style="color:red;">红色</span>字段！',
				isAlert: true,
				callback: function (boolean) {

				}
			});

		}
	});

	//检查表单
	function checkForm() {
		var bool = true;

		function callback() {
			bool = false;
		};
		errorElements = [];
		for (var key in DataTemplet) {
			switch (key) {
				case 'property_id': //单位性质
					checkProperty(key, callback);
					break;
				case 'business_data': //主营业务
					checkBusiness(key, callback);
					break;
				case 'quoted_state': //是否上市公司
					checkQuotedState(key, callback);
					break;
				case 'export_state': //是否有业务出口
					checkExportState(key, callback);
					break;
				case 'honor_data': //相关荣誉
					checkHonor(key, callback);
					break;
				default: //其他input输入框
					checkInputText(key, callback);
			}
		};
		if ($(".info").length > 0) {
			callback()
		};
		return bool;

	};

	//相关荣誉
	function checkHonor(key, callback) {
		//key == honor_data
		var inputs = document.querySelectorAll('.honor-box li input');

		for (var i = 0, len = inputs.length; i < len - 1; i++) {
			var input = inputs[i];
			if (input.value != '' && yearCheck(input.value)) {
				DataTemplet[key].data.push({
					"honor_id": $(input.parentNode.parentNode).data('honor_id').toString(),
					"honor_level": $(input.parentNode).data('honor_level').toString(),
					"get_year": input.value
				})
			};
		};

	};

	//input输入框
	function checkInputText(key, callback) {

		if ($('[name="' + key + '"]').val() == '' && $('[name="' + key + '"]').prop('disabled') == false && $('[name="' + key + '"]').prop('className') != 'nomust') {

			//是否上市公司
			if (key == 'quoted_time' || key == 'quoted_address' || key == 'shares_code') {
				if ($('input[name="quoted_state"]:checked').length == 0) {

					$('[name="' + key + '"]').parents('td').prev().css('color', 'red');
					errorElements.push($('[name="' + key + '"]').parents('td').prev()[0]);
					callback()
					return;
				} else if ($('input[name="quoted_state"][value="1"]').prop('checked') == true) {

					$('[name="' + key + '"]').prev('span').css('color', 'red');
					errorElements.push($('[name="' + key + '"]').prev('span')[0]);
					callback()
					return;
				} else if ($('input[name="quoted_state"][value="0"]').prop('checked') == true) {
					return;
				}
			};
			//是否有业务出口
			if (key == 'export_address') {

				if ($('input[name="export_state"]:checked').length == 0) {

					$('[name="' + key + '"]').parents('td').prev().css('color', 'red');
					errorElements.push($('[name="' + key + '"]').parents('td').prev()[0])
					callback()
					return;
				} else if ($('input[name="export_state"][value="1"]').prop('checked') == true) {

					$('[name="' + key + '"]').prev('span').css('color', 'red');
					errorElements.push($('[name="' + key + '"]').prev('span')[0]);
					callback()
					return;
				} else if ($('input[name="export_state"][value="0"]').prop('checked') == true) {
					return;
				}
			};

			$('[name="' + key + '"]').parents('td').prev().css('color', 'red');
			errorElements.push($('[name="' + key + '"]').parents('td').prev()[0]);
			callback();
		}
		DataTemplet[key] = $('[name="' + key + '"]').val();
	};
	//单位性质
	function checkProperty(key, callback) {

		var checked = $('input[name="' + key + '"]:checked');

		if (checked.length > 0) {
			DataTemplet[key] = checked.val();
		} else if ($('input[name="property_describe"]').val() == '') {
			// $('.property-box').css('outline', '2px dashed red');
			$('.property-box').prev().css('color', 'red');
			errorElements.push($('.property-box').prev()[0])
			callback();
		}
	};
	//主营业务
	function checkBusiness(key, callback) {

		var checked = $('input[name="business_id"]:checked');
		// console.log(DataTemplet[key])
		if (checked.length > 0) {
			checked.each(function () {
				DataTemplet[key].data.push({
					business_id: $(this).val()
				})
			})
		} else if ($('input[name="business_describe"]').val() == '') {
			// $('.business-box').css('outline', '2px dashed red');
			$('.business-box').prev().css('color', 'red');
			errorElements.push($('.business-box').prev()[0])
			callback();
		};

	};
	//上市公司
	function checkQuotedState(key, callback) {
		var checked = $('input[name="' + key + '"]:checked');

		DataTemplet[key] = checked.val();

	};
	//业务出口
	function checkExportState(key, callback) {
		var checked = $('input[name="' + key + '"]:checked');
		DataTemplet[key] = checked.val();
	};
	//保存成功后续操作
	function saveDone(res) {

		//存储allData
		sessionStorage.setItem('all_data', JSON.stringify(res.data));
		//展开Word下载
		// document.querySelector('.word').style.display = 'block';
		//打开Word预览开关

		// if (localStorage.getItem('showWordList')) {
		// 	var user_id = JSON.parse(sessionStorage.userInfo).user_id;
		// 	var showWordList = JSON.parse(localStorage.getItem('showWordList'));
		// 	showWordList.push(user_id);
		// 	localStorage.setItem('showWordList', JSON.stringify(showWordList));
		// } else {
		// 	var user_id = JSON.parse(sessionStorage.userInfo).user_id;
		// 	var showWordList = [];
		// 	showWordList.push(user_id);
		// 	localStorage.setItem('showWordList', JSON.stringify(showWordList));
		// }
		//视线回到最底部
		// document.body.scrollTop += 500;
	};

	//退出
	$('.logout').on('click', function () {
		$.ajax({
			type: 'post',
			url: logoutUrl,
			success: function (res) {
				if (res.state == 1) {
					top.location.href = loginUrl;
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
			data: {
				user_id: JSON.parse(sessionStorage.getItem('userInfo')).user_id
			},
			xhrFields: {
				withCredentials: true
			},
			crossDomain: true,
			error: function (err) {
				console.log('失败，错误原因：', err.message)
			}
		});
	});
	$('input[name="quoted_time"],input[name="quoted_address"],input[name="shares_code"]').on('focus', function () {
		$('input[name="quoted_state"]').prop('checked', true);
	});

	$('input[name="export_address"]').on('focus', function () {
		$('input[name="export_state"]').prop('checked', true);
	});

	/**************************表单验证开始**************************/


	function bingCheckEvent() {

		$('input[type="text"],textarea').on('keyup', function () {

			var name = this.name;

			if (this.value != '') {

				//暂时不验证上市时间（已限制输入）
				if (name == 'quoted_time') {
					return;
				}

				//单位名称
				if (name == 'company_name') {
					formCheck(this, companyCheck, 33);

				}
				//人名
				if (name == 'responsible_person' || name == 'declare_person' || name == 'corporate_jurisdical_person') {
					formCheck(this, peopleNameCheck, 16);
				}

				//邮箱验证

				if (name == 'responsible_mail' || name == 'declare_mail') {
					formCheck(this, emailCheck, 33);

				}

				//联系电话
				if (name == 'responsible_phone' || name == 'declare_phone') {
					formCheck(this, phoneCheck, 16);
				}

				//其他（描述）
				if (name == 'property_describe' || name == 'business_describe' || name == 'honor_describe') {
					formCheck(this, normalCheck, 333);
				}

				//地址

				if (name == 'company_register_address' || name == 'company_office_address' || name == 'quoted_address' || name == 'export_address') {
					formCheck(this, normalCheck, 100);
				}

				//授予年份
				if (name == 'get_year') {
					formCheck(this, yearCheck, 4);
				}

				//统一社会信用代码
				if (name == 'credit_code') {
					formCheck(this, numberLetter, 18, 18);
				}
				//股票代码
				if (name == 'shares_code') {
					formCheck(this, numberLetter, 9999);
				}
				//单位总人数+研发人员规模
				if (name == 'company_person_nums' || name == 'development_person_nums') {
					formCheck(this, numberCheck, 9999);
				}

				//研发能力
				/* if (name == 'development_ability') {

					formCheck(this, normalCheck, 333);
				} */

				//验证财富数字，允许负数
				if (name == 'register_capital' || name == 'business_income' || name == 'development_investment' || name == 'data_income' || name == 'tax_nums' || name == 'profit' || name == 'company_person_nums' || name == 'development_person_nums' || name == 'product_incom' || name == 'information_income' || name == 'software_income') {

					formCheck(this, richNumberCheck, 999999);
				}

			} else { //如果输入框为空，认定初始状态，尝试去除输入异常提示框
				$(this).parent().find('.info').remove();
			}

		});
	}

	/**************************表单验证结束**************************/

	//word下载链接
	$('a.project-download-link').attr('href', downLoadProductUrl);
	$('a.plan-download-link').attr('href', downLoadPlanUrl);
	new Header({
		ele: $('.header')[0],
		showNav: true
	});

	//end	
};
