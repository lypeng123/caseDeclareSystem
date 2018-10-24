//var path = 'http://san.511860.com/casedeclare_system'; //线上
 var path = 'http://127.0.0.1:8080';//本地
var pathExt = '/static';
//通用页面
var loginUrl = pathExt + '/login.html';
var logoutUrl = path + '/login/logout'; //退出
var templetUrl = pathExt + '/templet/index.html'; //申报模板页面
var downLoadProductUrl = path + '/load/downLoadProduct';
var downLoadPlanUrl = path + '/load/downLoadPlan';
var html_action = path + '/caseHtml/look';
var JSErrorLogUrl = path + '/error/errorLog';
//申报填写页面
var saveUrl = path + '/declareInfo/opeationDatas'; //填写完毕，保存
var propertyUrl = path + '/declareInfo/propertyDatas'; //获取单位性质列表
var businessUrl = path + '/declareInfo/businessDatas'; //获取主营业务列表
var honorUrl = path + '/declareInfo/honorDatas'; //获取相关荣誉
var allData = path + '/declareInfo/allDatas';
var businessInfoDatas = path + '/declareInfo/businessInfoDatas'; //登陆后获取页面主营业务数据
var honorInfoDatas = path + '/declareInfo/honorInfoDatas'; //登陆后获取相关荣誉数据

//申报书上传页面
var getProductUrl = path + '/load/selectProductFile'; //获取已上传产品
var upLoadProductWordUrl = path + '/load/upLoadProductWord'; //上传产品
var getPlanUrl = path + '/load/selectPlanFile'; //获取已上传方案
var upLoadPlanWordUrl = path + '/load/upLoadPlanWord'; //上传产品
var applicationField = path + '/load/selectField'; //应用领域
var logoutUrl = path + '/login/logout'; //退出
var deleteProductFileUrl = path + '/load/deleteProductFile'; //删除上传的产品
var deletePlanFileUrl = path + '/load/deletePlanFile'; //删除上传的方案

//登录页面
var login = path + '/login/login'; //登录

//注册页面
var register = path + '/login/regist'; //注册
var getAllProvinceUrl = path + '/login/getAllProvince'; //获取省份

//安全设置页面
var password_cUrl = path + "/safeSettings/editPassword"; //修改密码

//案例查询页面
var selectFirstInfoUrl = path + "/caseSelect/selectFirstInfo"; //原始案例查询
var downLoadWordUrl = path + '/caseSelect/downLoadWord'; //文件下载
var getFinalScoreUrl = path + '/caseSelect/getFinalScore'; //最终评分
//专家评审
var expertReviewtFirstUrl = path + '/evaluation/caseList'; //文件下载
var expertScoreUrl = path + '/evaluation/expertScore'; //提交评审
var getScoreUrl = path + '/evaluation/getScore'; //获取评审分数
var caseScreenResultUrl = path + "/caseScreen/caseScreenResult";
//通过与不通过
var editCaseScreenUrl = path + "/caseScreen/editCaseScreen";
//数据导出
var enterpriseInformationUrl = path + '/info/enterpriseInformation';//企业信息
var scoringResultsUrl = path + '/info/scoringResults';//打分结果

var exportEnterpriseInfoUrl = path + '/exportExcel/exportEnterpriseInfo';//导出企业信息
var exportScoreExcelUrl = path + '/exportExcel/exportScoreExcel';//导出打分结果

//历史数据浏览
var historicalDataBrowseUrl = path+'/historicalCase/getDeclareInfo';//获取历史数据

//账户管理
var userManageUrl = path + '/userManage/getUserInfo';//获取用户信息

//修改用户账号时获取用户所有信息
var userAllData=path+'/userManage/allDatas';

//用户id传入request域
var setUserIdUrl = path+'/userManage/setUserIdUrl';

var modifyBusinessInfoDatas = path + '/userManage/businessInfoDatas'; //登陆后获取页面主营业务数据
var modifyHonorInfoDatas = path + '/userManage/honorInfoDatas'; //登陆后获取相关荣誉数据

//管理员修改用户信息后保存
var modifySaveUrl = path +'/userManage/opeationDatas';

//管理员根据用户id查看对应案例
var checkCaseByUserIdUrl = path + '/userManage/findCaseByUserId';

//网页JS错误日志上报:仅上报登录页,注册页,以及其他已登录站内页面

if (sessionStorage.getItem('userInfo') || document.documentElement.className == 'login' || document.documentElement.className == 'register') {

    window.onerror = function (errorMessage, scriptURI, lineNumber, columnNumber, errorObj) {
        try {
            var ajaxData = {
                user_id: sessionStorage.getItem('userInfo') ? JSON.parse(sessionStorage.getItem('userInfo')).user_id : '未登录',//用户
                user_agent: window.navigator.userAgent,//浏览器
                errorPageUrl: window.location.href,//错误页面
                errorMessage: errorMessage || '',//"错误信息"
                scriptURI: scriptURI || '',//出错文件
                lineNumber: lineNumber || '',//出错行号
                columnNumber: columnNumber || '',//出错列号
                errorStack: errorObj.stack ? errorObj.stack : '' //错误详情
            };

            //async send

            setTimeout(function () {

                $.ajax({
                    url: JSErrorLogUrl,
                    type: 'post',
                    xhrFields: {
                        withCredentials: true
                    },
                    crossDomain: true,
                    data: ajaxData
                })

            }, 0)
        } catch (err) {

        }


    }
}