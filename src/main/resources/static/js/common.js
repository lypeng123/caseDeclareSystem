/**************************生成头部**************************/
function Header(opt) {

    this.settings = { //默认参数
        ele: null, //容器元素
        showNav: true, //是否显示导航栏
        showExit: true, //是否显示退出键
        callback: function() {
            //渲染完头部后需要做的事
        }
    };
    this.cfg = $.extend({}, this.settings, opt);

    this.init();
};
Header.prototype = {
    constructor: Header,
    init: function() {
        this.renderUI();
    },
    renderUI: function() {
        var current_nav = document.documentElement.className;
        if (this.cfg.showExit) {
            var headerTitle = '<link rel="stylesheet" href="./fonts/iconfont.css"><div class="title-wrap"><div class="title"><h2>工业和信息化部2017大数据优秀产品和应用解决方案案例申报系统</h2><a class="logout iconfont icon-tuichu" href="javascript:;">&nbsp;&nbsp;退出</a></div></div>';
        } else {
            var headerTitle = '<div class="title-wrap"><div class="title"><h2>工业和信息化部2017大数据优秀产品和应用解决方案案例申报系统</h2></div></div>';
        }

        if (this.cfg.showNav) {
            var navArray = JSON.parse(sessionStorage.getItem('nav_data'));

            //开始-将<安全设置>栏目放置最后
            if (navArray[navArray.length - 1].className != 'security-set') {

                (function() {
                    for (var i = 0; i < navArray.length; i++) {
                        if (navArray[i].className == 'security-set') {
                            var deleteItem = navArray.splice(i, 1);
                            navArray.push(deleteItem[0]);
                        }
                    }
                })();

            }

            //结束-将<安全设置>栏目放置最后

            var navList = navArray.map(function(item) {
                if (item.className == current_nav) {
                    return '<li class="active"><a href="' + item.url + '">' + item.name + '</a></li>'
                } else {
                    return '<li><a href="' + item.url + '">' + item.name + '</a></li>'
                }

            }).join('');
            var headerNav = '<div class="nav clear"><ul class="nav-list">' + navList + '</ul></div>'
            this.cfg.ele.innerHTML = headerTitle + headerNav;
        } else {
            this.cfg.ele.innerHTML = headerTitle;
        }

        this.bindEvt();
    },
    bindEvt: function() {
        var container = this.cfg.ele;
        //退出登录
        $(container).find('.logout').on('click', function() {
            $.ajax({
                type: 'post',
                url: logoutUrl,
                success: function(res) {
                    if (res.state == 1) {
                        top.location.href = loginUrl;
                    } else if (res.state == 0) {
                        new Dialog().init({
                            mask: true,
                            title: '',
                            content: res.message,
                            isAlert: true,
                            callback: function(boolean) {

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
                error: function(err) {
                    console.log('失败，错误原因：', err.message)
                }
            });
        });

        //导航栏变色
        $(container).find('.nav-list li').on('click', function() {
            sessionStorage.setItem('current_nav', this.innerText.trim());
        })
    }
};

window.Header = Header;

/**************************拖拽组件**************************/

$(function() {
    function Drag(ele) {
        this.obj = ele;
        this.disX = 0;
        this.disY = 0;
        this.settings = { //默认参数
            callback: function() {}
        }
    }

    Drag.prototype = {
        constructor: Drag,
        init: function(options) {

            options = options || {};

            var This = this;

            $.extend(this.settings, options);

            this.obj.onmousedown = function(ev) {

                var ev = ev || window.ev;


                This.fnDown(ev); //计算拖拽点相对于元素左上角的坐标

                top.document.onmousemove = function(ev) {
                    var ev = ev || window.ev;
                    This.fnMove(ev);
                }

                top.document.onmouseup = function(ev) {
                    This.fnUp(ev);
                }

                return false;
            }

        },
        fnDown: function(ev) {

            this.disX = ev.clientX - this.obj.offsetLeft;
            this.disY = ev.clientY - this.obj.offsetTop;

        },
        fnMove: function(ev) {

            var L = ev.clientX - this.disX;
            var T = ev.clientY - this.disY;

            if (L < 0) {
                L = 0;
            }
            if (L > top.document.documentElement.clientWidth - this.obj.offsetWidth) {
                L = top.document.documentElement.clientWidth - this.obj.offsetWidth;
            }
            if (T < 0) {
                T = 0;
            }
            if (T > top.document.documentElement.clientHeight - this.obj.offsetHeight) {
                T = top.document.documentElement.clientHeight - this.obj.offsetHeight;
            }
            this.obj.style.left = L + "px";
            this.obj.style.top = T + "px";

        },

        fnUp: function() {
            top.document.onmousemove = null;
            top.document.onmouseup = null;
        }
    };

    /******************************弹窗（可选是否依赖拖拽）******************************/
    function Dialog() {

        this.obonc_dialog = null;

        this.settings = { //默认参数
            w: 330,
            h: 200,
            dir: "center",
            title: "消息",
            content: "",
            mask: true,
            isAlert: false,
            hideView: false,
            callback: function() {}
        }
    }

    Dialog.prototype = {
        constructor: Dialog,
        init: function(opt) {

            $.extend(this.settings, opt);

            this.create(); //结构
            this.setData(); //样式
            this.fnClose(); //事件
            this.yes();
            this.no();

        },
        create: function() {
            this.obonc_dialog = top.document.createElement('div');
            this.obonc_dialog.id = "bonc_dialog";
            this.obonc_dialog.innerHTML = '<div class="title"><span>' + this.settings.title + '</span><span class="close" style="display:none">X</span></div><div class="dialog_content"><em>' + this.settings.content + '</em></div><div class="handle"><span class="yes">确定</span><span class="no">取消</span></div>';

            top.document.body.appendChild(this.obonc_dialog);

            if (this.settings.isAlert) {

                $(this.obonc_dialog).find('.no').css("display", "none")
            };

            // new Drag(this.obonc_dialog).init();
        },
        setData: function() {

            this.obonc_dialog.style.width = this.settings.w + "px";
            this.obonc_dialog.style.height = this.settings.h + "px";

            if (this.settings.dir === "center") {

                this.obonc_dialog.style.left = (viewWidth() - this.obonc_dialog.offsetWidth) / 2 + "px";
                this.obonc_dialog.style.top = (viewHeight() - this.obonc_dialog.offsetHeight) / 2 + "px";
            }

            if (this.settings.dir === "right") {
                this.obonc_dialog.style.left = viewWidth() - this.obonc_dialog.offsetWidth + "px";
                this.obonc_dialog.style.top = viewHeight() - this.obonc_dialog.offsetHeight + "px";
            }

            if (this.settings.mask) {

                this.createMask();
            }

        },
        fnClose: function() {
            var oClose = this.obonc_dialog.getElementsByTagName('span')[1];
            var This = this;
            oClose.onclick = function() {
                top.document.body.removeChild(This.obonc_dialog);

                if (This.settings.mask) {
                    top.document.body.removeChild(This.oMask);
                }

            }

        },
        createMask: function() {
            var oMask = top.document.createElement('div');

            if (this.settings.hideView) {
                oMask.id = "bonc_mask_hideView";
            } else {
                oMask.id = "bonc_mask";
            }
            top.document.body.appendChild(oMask);
            this.oMask = oMask;
        },
        yes: function() {
            var oYes = $(this.obonc_dialog).find('.yes')[0];
            var This = this;
            oYes.onclick = function() {
                This.settings.callback && This.settings.callback(true);
                This.obonc_dialog.getElementsByTagName('span')[1].click();
            }
        },
        no: function() {
            var oNo = $(this.obonc_dialog).find('.no')[0];
            var This = this;
            oNo.onclick = function() {
                This.settings.callback && This.settings.callback(false);
                This.obonc_dialog.getElementsByTagName('span')[1].click();
            }
        }
    }

    function viewWidth() {
        return top.document.documentElement.clientWidth;
    }

    function viewHeight() {
        return top.document.documentElement.clientHeight;
    };

    window.Dialog = Dialog;

    /******************************输入验证规则******************************/
    var check = {

        //验证提示框
        checkInfo: function(input, aErrMsg) {
            //输入框的坐标
            $(input).parent().css('position', 'relative').find('.info').remove();
            var centerStar = input.offsetLeft + input.clientWidth / 2;
            var info = document.createElement('b');
            info.className = 'info';
            info.innerText = aErrMsg.join('且');

            input.parentNode.appendChild(info);

            info.style.left = centerStar - info.clientWidth / 2 + 'px'; //13
            info.style.left = centerStar - info.clientWidth / 2 + 'px'; //52
            info.style.top = input.offsetTop - info.clientHeight + 'px';
        },
        //开始验证
        formCheck: function(ele, patterCallback, maxLength, minLength) {

            var str = ele.value;
            if (patterCallback(str) == false || str.length > maxLength || (minLength && str.length < minLength)) {
                var aErrMsg = [];
                if (patterCallback(str) == false) {
                    aErrMsg.push('格式错误');
                }
                if (str.length > maxLength) {

                    aErrMsg.push('长度超出');

                }

                if (str.length < minLength) {

                    aErrMsg.push('长度不足');

                }
                checkInfo(ele, aErrMsg);
            } else {
                $(ele).parent().find('.info').remove();
            }

        },
        /*以下是具体验证规则*/

        //一般验证
        normalCheck: function(str) {
            return /^[a-zA-Z0-9_\u4e00-\u9fa5@\.。,，\-？；！!“”：《》【】、（）()]*$/.test(str)
        },
        //人名
        peopleNameCheck: function(str) {
            return /^[a-zA-Z\u4e00-\u9fa5]*$/.test(str);
        },
        //单位名称
        companyCheck: function(str) {
            return /^[a-zA-Z0-9\u4e00-\u9fa5()（）]*$/.test(str)
        },
        //验证手机号
        phoneCheck: function(str) {
            return /(^(0[0-9]{2,3}\-)?([2-9][0-9]{6,7})+(\-[0-9]{1,4})?$)|(^((\+86)|0|86|17951)?(1[3|4|5|7|8]\d{9}$))/.test(str);
        },
        //验证邮箱
        emailCheck: function(str) {
            return /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/.test(str);
        },

        //统一社会信用代码
        numberLetter: function(str) {
            return /^[a-zA-Z0-9]*$/.test(str);
        },
        //验证年份
        yearCheck: function(str) {
            return /^(19||20)\d{2}$/.test(str) && parseInt(str) <= new Date().getFullYear()
        },
        //纯数字验证
        numberCheck: function(str) {
            return /^[0-9]*$/.test(str)
        },
        richNumberCheck: function(str) {

            //对于负号-
            if (str.match(/-/g)) {

                if (str.match(/-/g).length == 1 && str.indexOf('-') != 0) {
                    return false;
                }
                if (str.match(/-/g).length > 1) {
                    return false;
                }

            }
            //对于.
            if (str.match(/\./g)) {

                if (str.match(/\./g).length == 1) {

                    if ((str.match(/-/g) && str.match(/-/g).length == 1) && str.indexOf('.') == 1) {
                        return false;
                    }

                }
                if (str.match(/\./g).length > 1) {
                    return false;
                }

            }
            return /^[\.\-\d]*$/.test(str);
        },
        //数字+保留2位小数
        numberFixedTwo: function(obj) {

            /*  obj.value = obj.value.replace(/[^\d.]/g, ""); //清除“数字”和“.”以外的字符
             obj.value = obj.value.replace(/\.{2,}/g, "."); //只保留第一个. 清除多余的
             obj.value = obj.value.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");
             obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3'); //只能输入两个小数
             if (obj.value.indexOf(".") < 0 && obj.value != "") { //以上已经过滤，此处控制的是如果没有小数点，首位不能为类似于 01、02的金额
                 obj.value = parseFloat(obj.value);
             } */
        },
        numberFixedTwoMinus: function(obj) {

            /*  obj.value = obj.value.replace(/[^\d.\-]/g, ""); //清除“数字”和“.”以外的字符
             obj.value = obj.value.replace(/\.{2,}/g, "."); //只保留第一个. 清除多余的
             obj.value = obj.value.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");
             obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3'); //只能输入两个小数 */

            /* if (obj.value.indexOf(".") < 0 && obj.value != "") { //以上已经过滤，此处控制的是如果没有小数点，首位不能为类似于 01、02的金额
                obj.value = parseFloat(obj.value);
            } */
        }
    };

    /* 动态设置自定义Data-*属性 */
    function setDataString(data) {

        var aKey = Object.keys(data).map(function(key) {
            return ' data-' + key + '="' + data[key] + '" ';
        });
        return aKey.join('');
    };

    window.setDataString = setDataString;

    /* 去重 */
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
    };

    window.uniq = uniq;

    $.extend(window, check);

    //未登录，到登录页
    if (!sessionStorage.getItem('userInfo') && document.documentElement.className != 'login' && document.documentElement.className != 'register') {

        new Dialog().init({
            mask: true,
            title: '',
            content: '未登录或登陆失效',
            isAlert: true,
            hideView: true,
            callback: function(boolean) {
                if (boolean) {
                    top.location.href = loginUrl;
                }
            }
        });
    }

    //end
})