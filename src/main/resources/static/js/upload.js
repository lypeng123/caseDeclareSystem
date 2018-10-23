$(function () {

	/************************产品************************/

	//获取已上传产品列表
	getProductList();

	function getProductList() {

		$.ajax({
			type: 'post',
			url: getProductUrl,
			success: function (res) {
				if (res.state == 1) {
					renderProductList(res.data)
				} else if (res.state == 0) {
					new Dialog().init({
						mask: true,
						title: '',
						content: res.message,
						isAlert: true,
						callback: function (boolean) {

						}
					})
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
				declare_id: JSON.parse(sessionStorage.getItem('all_data')).declare_id
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

	var uploadState = {

		pending: function (selector) { //上传中
			$('#upload-mask').css('display', 'block');
			$('.' + selector).find('a.upload').css({
				background: 'silver',
				color: 'white'
			}).text('上传中...');

		},
		complete: function (selector) { //上传完成（success & error）
			$('#upload-mask').css('display', 'none');
			$('.' + selector).find('a.upload').css({
				background: '#076bce',
				color: 'white'
			}).text('开始上传');

		}
	};

	function deleteProductWord(span) {

		var file_path = $(span).data('file_path');
		var product_id = $(span).data('product_id');
		var old_file_name = $(span).data('old_file_name');
		var file_name = $(span).data('file_name');
		var html_path = $(span).data('html_path');
		var file_code = $(span).data('file_code');

		new Dialog().init({
			mask: true,
			title: '警告',
			content: '确定要删除文件"' + old_file_name + '"吗？',
			isAlert: false,
			callback: function (boolean) {

				if (boolean) { //确定
					var data = {
						declare_id: JSON.parse(sessionStorage.getItem('all_data')).declare_id,
						company_name: JSON.parse(sessionStorage.getItem('all_data')).company_name,
						file_name: file_name,
						file_path: file_path,
						product_id: product_id,
						html_path: html_path,
						file_code: file_code

					};
					$.ajax({
						type: 'post',
						url: deleteProductFileUrl,
						success: function (res) {
							if (res.state == 1) {
								getProductList(res.data);

								new Dialog().init({
									mask: true,
									title: '',
									content: '删除成功！',
									isAlert: true,
									callback: function (boolean) {

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
									hideView:true,
									callback: function (boolean) {
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
						error: function (err) {
							console.log('失败，错误原因：', err.message)
						}
					});
				}

			}
		});

	};

	//改动：
	function renderProductList(data) {
		var box = document.querySelector('.product-list');
		box.innerHTML = '';
		for (var i = 0, len = data.baseDatas.length; i < len; i++) {
			var li = document.createElement('li');
			var span = document.createElement('span');

			span.innerHTML = "×";
			span.title = '删除';
			span.onclick = function () {
				deleteProductWord(this)
			};
			
			span.setAttribute('data-file_code',data.baseDatas[i]['file_code']);
			span.setAttribute('data-file_path',data.baseDatas[i]['file_path']);
			span.setAttribute('data-old_file_name',data.baseDatas[i]['old_file_name']);
			span.setAttribute('data-file_name',data.baseDatas[i]['file_name']);
			span.setAttribute('data-html_path',data.baseDatas[i]['html_path']);
			span.setAttribute('data-product_id',data.baseDatas[i]['product_id']);

			li.innerText = i + 1 + '，' + data.baseDatas[i]['old_file_name'];
			li.appendChild(span);
			box.appendChild(li);
		}
	}
	//事件委托
	$('.product img.upload').on('click', function () {
		$('.product input.fileUpload').click();
	});
	//选择文件 
	$('.product input.fileUpload').change(function () {

		var fakeFilePath = $(this).val();
		var fileExtendName = fakeFilePath.substr(fakeFilePath.lastIndexOf('.'));

		if (fakeFilePath == '') {
			$('.product input.fileUpload').val('');
			$('.product .file-info').html('&nbsp;').attr('title', '');
		} else {
			if (fileExtendName == '.doc' || fileExtendName == '.docx') {
				if (this.files) {
					if (this.files[0].size <= 31457280) {
						$('.product .file-info').attr('title', this.files[0].name)
						$('.product .file-info').text('已选择：' + this.files[0].name);
					} else {
						new Dialog().init({
							mask: true,
							title: '',
							content: '文件大小超出限制，仅限上传小于30M的Word文件！',
							isAlert: true,
							callback: function (boolean) {

							}
						});
						$('.product input.fileUpload').val('');
						$('.product .file-info').html('&nbsp;').attr('title', '');
					}
				}
			} else {
				new Dialog().init({
					mask: true,
					title: '',
					content: '文件类型错误，仅限上传Word文件！',
					isAlert: true,
					callback: function (boolean) {

					}
				});
				$('.product input.fileUpload').val('');
				$('.product .file-info').html('&nbsp;').attr('title', '');
			}
		}

	});

	//产品上传
	$('.product a.upload').on('click', function () {

		if ($('.product input.fileUpload').val() == '') {
			new Dialog().init({
				mask: true,
				title: '',
				content: '请选择您要上传的文件！',
				isAlert: true,
				callback: function (boolean) {

				}
			});
		} else {
			uploadState.pending('product');
			var data = {
				declare_id: JSON.parse(sessionStorage.getItem('all_data')).declare_id,
				company_name: JSON.parse(sessionStorage.getItem('all_data')).company_name,
				property_id: JSON.parse(sessionStorage.getItem('all_data')).property_id == undefined ? 'other' : JSON.parse(sessionStorage.getItem('all_data')).property_id,
				user_id: JSON.parse(sessionStorage.getItem('all_data')).user_id,
				user_name: JSON.parse(sessionStorage.getItem('all_data')).user_name,
				area_id: JSON.parse(sessionStorage.getItem('all_data')).area_id,
				quoted_state: JSON.parse(sessionStorage.getItem('all_data')).quoted_state
			};
			$('.product #submitFormProduct').ajaxSubmit({
				type: 'post',
				url: upLoadProductWordUrl,
				dataType: 'json',
				data: data,
				xhrFields: {
					withCredentials: true
				},
				crossDomain: true,
				success: function (res) {
					if (res.state == 1) {
						//更新列表
						uploadState.complete('product');
						getProductList();
						$('.product input.fileUpload').val('');
						$('.product .file-info').html('&nbsp;').attr('title', '');


						new Dialog().init({
							mask: true,
							title: '',
							content: '上传成功！',
							isAlert: true,
							callback: function (boolean) {

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
							hideView:true,
							callback: function (boolean) {
								if (boolean) {
									top.location.href = loginUrl;
								}
							}
						});
					}

				},
				complete: function () {
					uploadState.complete('product');
				},
				error: function (res) {
					$('.product input.fileUpload').val('');
					$('.product .file-info').html('&nbsp;').attr('title', '');
					new Dialog().init({
						mask: true,
						title: '',
						content: res.message,
						isAlert: true,
						callback: function (boolean) {

						}
					});
				}
			});
		}
	});

	/************************方案************************/

	function getPlanList() {
		$.ajax({
			type: 'post',
			url: getPlanUrl,
			success: function (res) {
				if (res.state == 1) {
					renderPlanList(res.data)
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
				declare_id: JSON.parse(sessionStorage.getItem('all_data')).declare_id
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

	function deletePlanWord(span) {

		var file_path = $(span).data('file_path');
		var plan_id = $(span).data('plan_id');
		var old_file_name = $(span).data('old_file_name');
		var file_name = $(span).data('file_name');
		var html_path = $(span).data('html_path');
		var file_code = $(span).data('file_code');


		new Dialog().init({
			mask: true,
			title: '警告',
			content: '确定要删除文件"' + old_file_name + '"吗？',
			isAlert: false,
			callback: function (boolean) {

				if (boolean) { //确定
					var data = {
						declare_id: JSON.parse(sessionStorage.getItem('all_data')).declare_id,
						company_name: JSON.parse(sessionStorage.getItem('all_data')).company_name,
						file_name: file_name,
						file_path: file_path,
						plan_id: plan_id,
						html_path: html_path,
						file_code: file_code
					};
					console.log(data);
					$.ajax({
						type: 'post',
						url: deletePlanFileUrl,
						success: function (res) {
							if (res.state == 1) {
								getPlanList(res.data);

								new Dialog().init({
									mask: true,
									title: '',
									content: '删除成功！',
									isAlert: true,
									callback: function (boolean) {

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
									hideView:true,
									callback: function (boolean) {
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
						error: function (err) {
							console.log('失败，错误原因：', err.message)
						}
					});
				}

			}
		});
	};

	function getFieldName(fieldId) {
		return $('.plan-field option[value="' + fieldId + '"]').text()
	};

	//改动：
	function renderPlanList(data) {
		var box = document.querySelector('.plan-list');
		box.innerHTML = '';
		for (var i = 0, len = data.baseDatas.length; i < len; i++) {
			var li = document.createElement('li');
			var span = document.createElement('span');

			span.innerHTML = "×";
			span.title = '删除';
			span.onclick = function () {
				deletePlanWord(this)
			};
			span.setAttribute('data-file_code',data.baseDatas[i]['file_code']);
			span.setAttribute('data-file_path',data.baseDatas[i]['file_path']);
			span.setAttribute('data-old_file_name',data.baseDatas[i]['old_file_name']);
			span.setAttribute('data-file_name',data.baseDatas[i]['file_name']);
			span.setAttribute('data-html_path',data.baseDatas[i]['html_path']);
			span.setAttribute('data-plan_id',data.baseDatas[i]['plan_id']);
			
			li.innerText = i + 1 + '，' + data.baseDatas[i]['old_file_name'] + '（' + getFieldName(data.baseDatas[i]['field_id']) + ')';
			li.appendChild(span);
			box.appendChild(li);
		}
	}
	//事件委托
	$('.plan img.upload').on('click', function () {
		$('.plan input.fileUpload').click();
	});

	//选择方案
	$('.plan input.fileUpload').change(function () {

		var fakeFilePath = $(this).val();
		var fileExtendName = fakeFilePath.substr(fakeFilePath.lastIndexOf('.'));

		if (fakeFilePath == '') {
			$('.plan input.fileUpload').val('');
			$('.plan .file-info').html('&nbsp;').attr('title', '');
		} else {
			if (fileExtendName == '.doc' || fileExtendName == '.docx') {
				if (this.files) {
					if (this.files[0].size <= 31457280) {
						$('.plan .file-info').attr('title', this.files[0].name)
						$('.plan .file-info').text('已选择：' + this.files[0].name);
					} else {
						new Dialog().init({
							mask: true,
							title: '',
							content: '文件大小超出限制，仅限上传小于30M的Word文件！',
							isAlert: true,
							callback: function (boolean) {

							}
						});
						$('.plan input.fileUpload').val('');
						$('.plan .file-info').html('&nbsp;').attr('title', '');
					}
				}
			} else {
				new Dialog().init({
					mask: true,
					title: '',
					content: '文件类型错误，仅限上传Word文件！',
					isAlert: true,
					callback: function (boolean) {

					}
				});
				$('.plan input.fileUpload').val('');
				$('.plan .file-info').html('&nbsp;').attr('title', '');
			}
		}

	});
	//方案上传
	$('.plan a.upload').on('click', function () {
		if ($('.plan input.fileUpload').val() == '') {

			new Dialog().init({
				mask: true,
				title: '',
				content: '请选择您要上传的文件！',
				isAlert: true,
				callback: function (boolean) {

				}
			});
		} else {
			$('.plan-field select').addClass('highlight');
			new Dialog().init({
				mask: true,
				title: '',
				content: '提示：请再次确认“行业应用领域”选项与申报书材料填写一致。',
				isAlert: false,
				callback: function (boolean) {

					if (boolean) {
						$('.plan-field select').removeClass('highlight');
						uploadState.pending('plan');
						var data = {
							declare_id: JSON.parse(sessionStorage.getItem('all_data')).declare_id,
							field_id: $('#file-box').val(),
							company_name: JSON.parse(sessionStorage.getItem('all_data')).company_name,
							property_id: JSON.parse(sessionStorage.getItem('all_data')).property_id == undefined ? 'other' : JSON.parse(sessionStorage.getItem('all_data')).property_id,
							user_id: JSON.parse(sessionStorage.getItem('all_data')).user_id,
							user_name: JSON.parse(sessionStorage.getItem('all_data')).user_name,
							area_id: JSON.parse(sessionStorage.getItem('all_data')).area_id,
							quoted_state: JSON.parse(sessionStorage.getItem('all_data')).quoted_state
						};
						$('.plan #submitFormPlan').ajaxSubmit({
							type: 'post',
							url: upLoadPlanWordUrl,
							dataType: 'json',
							data: data,
							xhrFields: {
								withCredentials: true
							},
							crossDomain: true,
							success: function (res) {
								if (res.state == 1) {
									//更新列表
									uploadState.complete('plan');
									getPlanList();
									$('.plan input.fileUpload').val('');
									$('.plan .file-info').html('&nbsp;').attr('title', '');
									//重置行业领域
									// $('#file-box').val('1');

									new Dialog().init({
										mask: true,
										title: '',
										content: '上传成功！',
										isAlert: true,
										callback: function (boolean) {

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
										hideView:true,
										callback: function (boolean) {
											if (boolean) {
												top.location.href = loginUrl;
											}
										}
									});
								}
							},
							complete: function () {
								uploadState.complete('plan');
							},
							error: function (res) {
								$('.plan input.fileUpload').val('');
								$('.plan .file-info').html('&nbsp;').attr('title', '');
								new Dialog().init({
									mask: true,
									title: '',
									content: res.message,
									isAlert: true,
									callback: function (boolean) {

									}
								});
							}
						});
					} else {
						$('.plan-field select').removeClass('highlight');
					}
				}
			});

		}
	});
	//获取应用领域
	$.ajax({
		type: 'post',
		url: applicationField,
		success: function (res) {
			if (res.state == 1) {
				renderField(res.data);
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
			declare_id: JSON.parse(sessionStorage.getItem('all_data')).declare_id
		},
		xhrFields: {
			withCredentials: true
		},
		crossDomain: true,
		error: function (err) {
			console.log('失败，错误原因：', err.message)
		}
	});

	function renderField(data) {
		var box = document.getElementById('file-box');
		for (var i = 0; i < data.length; i++) {
			var option = document.createElement('option');
			option.value = data[i].field_id;
			option.innerText = data[i].field_name;
			box.appendChild(option);
		};

		getPlanList();
	};
	//返回上一步
	$('a.prew').on('click', function () {
		history.back()
	});

	//提交
	$('a.submit').on('click', function () {

		if ($('.product-list li,.plan-list li').length > 0) {
			location.href = "thanks.html";

		} else {

			new Dialog().init({
				mask: true,
				title: '',
				content: '请先上传文件！',
				isAlert: true,
				callback: function (boolean) {

				}
			});
		}

		//提交
	})

	new Header({
		ele: $('.header')[0],
		showNav: true
	});
	//end
})