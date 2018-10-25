package com.bonc.caseDeclare.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bonc.caseDeclare.mapper.DeclareInfoMapper;
import com.bonc.caseDeclare.mapper.LoginMapper;
import com.bonc.caseDeclare.mapper.UserManageMapper;
import com.bonc.caseDeclare.service.DeclareInfoService;
import com.bonc.caseDeclare.service.UserManageService;
import com.bonc.util.JsonResult;
import com.bonc.util.Md5Util;
import com.bonc.util.PageBeanMysql;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class UserManageServiceImpl implements UserManageService {
    private static final Logger logger = LoggerFactory.getLogger(UserManageServiceImpl.class);

    @Autowired
    private UserManageMapper userManageMapper;

    @Autowired
    private LoginMapper loginMapper;

    @Autowired
    private DeclareInfoService declareInfoService;

    @Autowired
    private DeclareInfoMapper declareInfoMapper;

    /**
     * 查询所有用户信息
     *
     * @return
     */
    @Override
    public JsonResult<Object> getAllUser(HttpServletRequest request) {
//        String audit_state = request.getParameter("audit_state");
//        String file_type = request.getParameter("file_type");
//        String area_id = request.getParameter("area_id");
//        String property_id = request.getParameter("property_id");
//        String quoted_state = request.getParameter("quoted_state");
//        String company_name = request.getParameter("company_name");
//        String field_id = request.getParameter("field_id");
//        String business_id = request.getParameter("business_id");
        String pageNum = request.getParameter("pageNum");

        Integer nums;
        try {
            nums = Integer.parseInt(pageNum);
            if (nums == 0) {
                logger.info("页码无效！");
                return new JsonResult<>("页码无效！");
            }
        } catch (NumberFormatException e) {
            logger.info("页码无效！");
            return new JsonResult<>("页码无效！");
        }


        PageBeanMysql<Object> pageBeanMysql = new PageBeanMysql<Object>(nums);
        Integer start = pageBeanMysql.getStart();
        Integer end = pageBeanMysql.getEnd();
        if (start < 0 || end < 0) {
            logger.info("页码无效！");
            return new JsonResult<>("页码无效！");
        }


        Map<String, Object> map = new HashMap<String, Object>();
        map.put("start", start);
        map.put("end", end);

        List<Map<String, Object>> userData = userManageMapper.getAllUser(map);
        //模拟数据库查询到的总条数，用于分页上线需更改
        Integer count = 20;
        pageBeanMysql.setCount(count);
        pageBeanMysql.setPageNum(nums);
        pageBeanMysql.setData(userData);

        return new JsonResult<>(JsonResult.SUCCESS, "ok", pageBeanMysql);
    }

    @Override
    public JsonResult<Object> allDeclareCompanyAccount(HttpServletRequest request) {
        String audit_state = request.getParameter("audit_state");
		/*String file_type = request.getParameter("file_type");
		String area_id = request.getParameter("area_id");
		String property_id = request.getParameter("property_id");
		String quoted_state = request.getParameter("quoted_state");
		String company_name = request.getParameter("company_name");
		String field_id = request.getParameter("field_id");
		String business_id = request.getParameter("business_id");*/
        String pageNum = request.getParameter("pageNum");
        //用户注册时间
        String register_time = request.getParameter("register_time");
        if (pageNum == null || pageNum.equals("")
                || audit_state == null || audit_state.equals("")) {
            logger.info("参数有误");
            return new JsonResult<>("参数有误！");
        }

        Integer nums;
        try {
            nums = Integer.parseInt(pageNum);
            if (nums == 0) {
                logger.info("页码无效！");
                return new JsonResult<>("页码无效！");
            }
        } catch (NumberFormatException e) {
            logger.info("页码无效！");
            return new JsonResult<>("页码无效！");
        }


        PageBeanMysql<Object> pageBeanMysql = new PageBeanMysql<Object>(nums);
        Integer start = pageBeanMysql.getStart();
        Integer end = pageBeanMysql.getEnd();
        if (start < 0 || end < 0) {
            logger.info("页码无效！");
            return new JsonResult<>("页码无效！");
        }


        Map<String, Object> map = new HashMap<String, Object>();
        map.put("audit_state", audit_state);
		/*map.put("fiel_type", file_type);
		map.put("area_id", area_id);
		map.put("property_id", property_id);
		map.put("quoted_state", quoted_state);
		map.put("company_name", company_name);
		map.put("field_id", field_id);
		map.put("business_id", business_id);*/
        map.put("register_time", register_time);
        map.put("start", start);
        map.put("end", end);

        List<Map<String, Object>> selectFirstInfo = userManageMapper.allDeclareCompanyAccount(map);

        Integer count = userManageMapper.selectCount(map);

        pageBeanMysql.setPageNum(nums);
        pageBeanMysql.setCount(count);
        pageBeanMysql.setData(selectFirstInfo);

        logger.info("查询结果：" + pageBeanMysql);
        return new JsonResult<>(JsonResult.SUCCESS, "ok", pageBeanMysql);
    }

    @Override
    @Transactional
    public JsonResult<Object> deleteUserById(String user_id) {
        boolean deleteUserById = userManageMapper.deleteUserById(user_id);
        if (!deleteUserById) {
            return new JsonResult<>(JsonResult.ERROR, "删除失败");
        }
        return new JsonResult<>(JsonResult.SUCCESS, "删除成功");
    }

    @Transactional
    public JsonResult<Object> saveUser(HttpServletRequest req, String IP) {

        String company_name = req.getParameter("company_name");
        String responsible_person = req.getParameter("responsible_person");
        String company_register_address = req.getParameter("company_register_address");
        String area_id = req.getParameter("area_id");
        String declare_phone = req.getParameter("declare_phone");
        String declare_mail = req.getParameter("declare_mail");
        String credit_code = req.getParameter("credit_code");
//		String recommend = req.getParameter("recommend");
        String declare_person = req.getParameter("declare_person");
        String password = req.getParameter("password");

        if (company_name == null || company_name == "" || responsible_person == null || responsible_person == ""
                || company_register_address == null || company_register_address == "" || declare_phone == null
                || declare_phone == "" || declare_mail == null || declare_mail == "" || credit_code == null
                || credit_code == "" || password == null || password == ""
//				|| recommend == null || recommend == ""
                || declare_person == null
                || declare_person == "" || area_id == null || area_id == "") {
            logger.info("修改参数错误！");
            return new JsonResult<>("修改参数错误！");
        }

        String randomUUID = UUID.randomUUID().toString();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("user_id", randomUUID);
        map.put("company_name", company_name);
        map.put("responsible_person", responsible_person);
        map.put("company_register_address", company_register_address);
        map.put("area_id", area_id);
        map.put("declare_person", declare_person);
        map.put("register_time,", new Date());

        map.put("declare_phone", declare_phone);
        map.put("declare_mail", declare_mail);
        map.put("credit_code", credit_code);
        map.put("state", 1);
        map.put("ip", IP);
        map.put("type", 3);


        // 根据email添加用户名和密码 QY10001
//		HashMap<String, Object> hashMap = new HashMap<String, Object>();
//		selectUser = loginMapper.selectUser(hashMap);
//		String user_name = null;
//		Integer maxUserCode;
//		if (selectUser.isEmpty()) {
//			user_name = "QY10001";
//		} else {
//			maxUserCode = loginMapper.selectMaxUserCode();
//			user_name = "QY" + maxUserCode;
//		}
//
//		String password = GeneratorPassword.getPassword(6, 2);

        String passwordMd5 = Md5Util.encoding(password);

        map.put("user_name", declare_mail);
        map.put("password", passwordMd5);

        logger.info("用户注册相关信息" + map);

        boolean insertUser = loginMapper.insertUser(map);
        if (!insertUser) {
            logger.info("【未知错误导致修改失败，请尽快解决！！】");
            return new JsonResult<>("修改失败");
        }

//		SendEmailUtils.sendEmailConfigure((String) map.get("declare_person"), user_name,
//				(String) map.get("declare_mail"), password);

        return new JsonResult<>(JsonResult.SUCCESS, "修改成功！");
    }

    @Override
    public JsonResult<Object> findCaseByUserId(String user_id) {
        if (user_id == null || "".equals(user_id)) {
            return new JsonResult<>(JsonResult.ERROR, "用户id为空");
        }

        //调用userManageMapper查询该用户对应的所有案例
        List<Map<String, Object>> caseByUserId = userManageMapper.findCaseByUserId(user_id);
        logger.info("查询结果：" + caseByUserId);
        if (caseByUserId.isEmpty()) {
            return new JsonResult<>(JsonResult.ERROR, "没有查询到该用户对应案例");
        }

        return new JsonResult<>(JsonResult.SUCCESS, "ok", caseByUserId);
    }

    @Override
    public JsonResult<Object> updateAuditState(String screen_id, String audit_state) {
        if (screen_id == null || "".equals(screen_id)) {
            return new JsonResult<>(JsonResult.ERROR, "案例id为空");
        }
        //0:初筛，1：复筛，2:终筛，3：入选
        //1.第一轮通过
        //2.第二轮通过
        //3.第三轮通过

        // boolean updateAuditState = userManageMapper.updateAuditState(screen_id,audit_state);
        return null;
    }

    @Override
    public Object allDatas(String user_id) {
        //根据用户id查询该用户下所有的申报信息
        String declare_id = userManageMapper.declareId(user_id);
        Object userData = "";
        if (declare_id == null || declare_id.equals("")) {
            //只查找用户信息
            userData = userManageMapper.getUserInfo(user_id);
            logger.info("申报页返回信息1：" + userData);
            return userData;
        } else {
            String property_id = userManageMapper.selectPropertyId(declare_id);
            userData = userManageMapper.getUserInfo2(user_id, property_id);
            logger.info("申报页返回信息2：" + userData);
            return userData;
        }
    }

    @Override
    public JsonResult<Object> getBusinessInfo(String user_id) {
        if (user_id == null || user_id.equals("")) {
            logger.info("获取主营业务数据：参数有误");
            return new JsonResult<>("参数有误！");
        }

        String declare_id = userManageMapper.declareId(user_id);
//		if (declare_id == null || declare_id.equals("")) {
//			return new JsonResult<>("未进行申报！");
//		}

        List<Map<String, Object>> businessDatas = userManageMapper.getbusinessInfo(declare_id);

        logger.info("关联的主营业务数据返回结果：" + businessDatas);

        return new JsonResult<>(businessDatas);
    }

    @Override
    public JsonResult<Object> getHonorInfo(String user_id) {
        if (user_id == null || user_id.equals("")) {
            logger.info("获取相关荣誉数据：参数有误");
            return new JsonResult<>("参数有误！");
        }

        String declare_id = userManageMapper.declareId(user_id);
//		if (declare_id == null || declare_id.equals("")) {
//			return new JsonResult<>("未进行申报！");
//		}

        List<Map<String, Object>> honorDatas = userManageMapper.getHonorInfo(declare_id);

        logger.info("关联的荣誉返回结果：" + honorDatas);

        return new JsonResult<>(honorDatas);
    }

    @Override
    @Transactional
    public JsonResult<Object> updateAllDatas(HttpServletRequest request) {
        String user_id = (String) request.getSession().getAttribute("userId");

        String company_name = request.getParameter("company_name");
        String declare_person = request.getParameter("declare_person");
        String declare_mail = request.getParameter("declare_mail");
        String declare_phone = request.getParameter("declare_phone");
        String responsible_person = request.getParameter("responsible_person");
        String responsible_phone = request.getParameter("responsible_phone");
        String responsible_mail = request.getParameter("responsible_mail");
        String corporate_jurisdical_person = request.getParameter("corporate_jurisdical_person");
        String company_register_address = request.getParameter("company_register_address");
        String company_office_address = request.getParameter("company_office_address");
        String register_capital = request.getParameter("register_capital");

        String property_describe = request.getParameter("property_describe");
        String business_describe = request.getParameter("business_describe");
        String quoted_state = request.getParameter("quoted_state");
        String quoted_time = request.getParameter("quoted_time");
        String quoted_address = request.getParameter("quoted_address");
        String shares_code = request.getParameter("shares_code");
        String export_state = request.getParameter("export_state");
        String export_address = request.getParameter("export_address");
        String honor_describe = request.getParameter("honor_describe");
        String development_ability = request.getParameter("development_ability");
        String business_income = request.getParameter("business_income");
        String development_investment = request.getParameter("development_investment");
        String tax_nums = request.getParameter("tax_nums");
        String profit = request.getParameter("profit");
        String company_person_nums = request.getParameter("company_person_nums");
        String development_person_nums = request.getParameter("development_person_nums");

        String product_incom = request.getParameter("product_incom");
        String information_income = request.getParameter("information_income");
        String software_income = request.getParameter("software_income");
        String data_income = request.getParameter("data_income");

        String property_id = request.getParameter("property_id");
        String business_data = request.getParameter("business_data");
        String honor_data = request.getParameter("honor_data");

        if (user_id == null || user_id.equals("") || company_name == null || company_name.equals("")
                || declare_person == null || declare_person.equals("") || declare_mail == null
                || declare_mail.equals("") || declare_phone == null || declare_phone.equals("")
                || responsible_person == null || responsible_person.equals("") || responsible_phone == null
                || responsible_phone.equals("") || responsible_mail == null || responsible_mail.equals("")
                || corporate_jurisdical_person == null || corporate_jurisdical_person.equals("")
                || company_office_address == null || company_office_address.equals("")
                || company_register_address == null || company_register_address.equals("") || register_capital == null
                || register_capital.equals("") || quoted_state == null || quoted_state.equals("")
                || export_state == null || export_state.equals("") || business_income == null
                || business_income.equals("") || development_investment == null || development_investment.equals("")
                || tax_nums == null || tax_nums.equals("") || profit == null || profit.equals("")
                || company_person_nums == null || company_person_nums.equals("") || development_person_nums == null
                || development_person_nums.equals("") || business_data == null || business_data.equals("")
                || data_income == null || data_income.equals("")) {
            logger.info("修改数据：参数有误");
            return new JsonResult<>("参数有误！");
        }
        if ((property_id == null || property_id.equals(""))
                && (property_describe == null || property_describe.equals(""))) {
            logger.info("修改数据：公司性质未获取到");
            return new JsonResult<>("公司性质未获取到！");
        }
        if (
                declare_person.length() > 16 ||
                        declare_phone.length() > 16 ||
                        responsible_person.length() > 16 ||
                        responsible_phone.length() > 16 ||
                        corporate_jurisdical_person.length() > 16) {
            logger.info("修改数据：数据长度太长,超过16字");
            return new JsonResult<>("输入内容超出规定长度，限制16字以内！");
        }
        if (
                company_name.length() > 33 ||
                        declare_mail.length() > 33 ||
                        responsible_mail.length() > 33 ||
                        shares_code.length() > 33) {
            logger.info("修改数据：数据长度太长,超过33字");
            return new JsonResult<>("输入内容超出规定长度，限制33字以内！");
        }
        if (
                company_register_address.length() > 100 ||
                        company_office_address.length() > 100 ||
                        quoted_address.length() > 100 ||
                        export_address.length() > 100) {
            logger.info("修改数据：数据长度太长,超过100字");
            return new JsonResult<>("输入内容超出规定长度，限制100字以内！");
        }
        if (
                property_describe.length() > 333 ||
                        business_describe.length() > 333 ||
                        honor_describe.length() > 333) {
            logger.info("修改数据：数据长度太长,超过333字");
            return new JsonResult<>("输入内容超出规定长度，限制333字以内！");
        }
        if (
                development_ability.length() > 1000) {
            logger.info("修改数据：数据长度太长,超过1000字");
            return new JsonResult<>("输入内容超出规定长度，限制1000字以内！");
        }

        Double register_capital2;
        Double business_income2;
        Double development_investment2;
        Double tax_nums2;
        Double profit2;
        Integer company_person_nums2;
        Integer development_person_nums2;

        Double product_incom2 = null;
        Double information_income2 = null;
        Double software_income2 = null;
        Double data_income2;

        // 将字符串转换为数字
        try {
            register_capital2 = Double.parseDouble(register_capital);
            business_income2 = Double.parseDouble(business_income);
            development_investment2 = Double.parseDouble(development_investment);
            profit2 = Double.parseDouble(profit);
            tax_nums2 = Double.parseDouble(tax_nums);
            company_person_nums2 = Integer.parseInt(company_person_nums);
            development_person_nums2 = Integer.parseInt(development_person_nums);
            data_income2 = Double.parseDouble(data_income);
            if (product_incom != null && !product_incom.equals("") && !information_income.equals("")
                    && !information_income.equals("") && !software_income.equals("") && !software_income.equals("")) {
                product_incom2 = Double.parseDouble(product_incom);
                information_income2 = Double.parseDouble(information_income);
                software_income2 = Double.parseDouble(software_income);
            }
        } catch (Exception e) {
            // e.printStackTrace();
            logger.info("修改数据：存在不合法数据参数");
            return new JsonResult<>("存在不合法数据参数！");
        }

        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> map2 = new HashMap<String, Object>();

        map.put("user_id", user_id);
        map.put("company_name", company_name);
        map.put("declare_person", declare_person);
        map.put("declare_mail", declare_mail);
        map.put("declare_phone", declare_phone);
        map.put("responsible_person", responsible_person);
        map.put("responsible_phone", responsible_phone);
        map.put("responsible_mail", responsible_mail);
        map.put("corporate_jurisdical_person", corporate_jurisdical_person);
        map.put("company_register_address", company_register_address);
        map.put("company_office_address", company_office_address);
        map.put("register_capital", register_capital2);

        declareInfoService.updateUserInfo(map);

        String declare_id = declareInfoMapper.declareId(user_id);

        if (quoted_state.equals("1")) {
            if (quoted_time == null || quoted_time.equals("")
                    || quoted_address == null || quoted_address.equals("")
                    || shares_code == null || shares_code.equals("")) {
                logger.info("修改数据：上市信息不全");
                return new JsonResult<>("上市信息不全！");
            }
        }

        if (export_state.equals("1")) {
            if (export_address == null || export_address.equals("")) {
                logger.info("获取主营业务数据：出口信息不全");
                return new JsonResult<>("出口信息不全！");
            }
        }

        map2.put("property_describe", property_describe);
        map2.put("business_describe", business_describe);
        map2.put("quoted_state", quoted_state);
        map2.put("quoted_time", quoted_time);
        map2.put("quoted_address", quoted_address);
        map2.put("shares_code", shares_code);
        map2.put("export_state", export_state);
        map2.put("export_address", export_address);
        map2.put("honor_describe", honor_describe);
        map2.put("development_ability", development_ability);
        map2.put("business_income", business_income2);
        map2.put("development_investment", development_investment2);
        map2.put("tax_nums", tax_nums2);
        map2.put("profit", profit2);
        map2.put("company_person_nums", company_person_nums2);
        map2.put("development_person_nums", development_person_nums2);
        map2.put("product_incom", product_incom2);
        map2.put("information_income", information_income2);
        map2.put("software_income", software_income2);
        map2.put("data_income", data_income2);

        // 如果申报ID为空就调插入接口，否则就修改
        if (declare_id == null || declare_id.equals("")) {
            // 生成新的申报ID
            declare_id = UUID.randomUUID().toString();
            map2.put("declare_id", declare_id);
            declareInfoService.insertDeclareInfo(map2);
            declareInfoService.insertUserDeclareRel(user_id, declare_id);
            if (property_id == null || property_id.equals("")) {
                declareInfoMapper.deleteProperty(declare_id);
            } else {
                declareInfoService.insertDeclarePropertyRel(property_id, declare_id);
            }

            // 修改主营业务
            JSONObject parseObject = null;
            try {
                parseObject = JSONObject.parseObject(business_data);
            } catch (Exception e) {
                logger.info("修改数据：主营业务数据格式有误");
                return new JsonResult<>("主营业务数据格式有误");
            }
            List<Map<String, Object>> object = (List<Map<String, Object>>) parseObject.get("data");
            if (object != null && object.size() > 0) {
                for (Map<String, Object> map3 : object) {
                    String business_id = (String) map3.get("business_id");
                    declareInfoService.insertDeclareBusinessRel(business_id, declare_id);
                }
            }

            // 修改相关荣誉
            JSONObject parseObject2 = null;
            if (honor_data != null && !honor_data.equals("")) {
                try {
                    parseObject2 = JSONObject.parseObject(honor_data);
                } catch (Exception e) {
                    logger.info("修改数据：相关荣誉数据格式有误");
                    return new JsonResult<>("相关荣誉数据格式有误");
                }
                List<Map<String, Object>> object2 = (List<Map<String, Object>>) parseObject2.get("data");
                if (object2 != null && object2.size() > 0) {
                    for (Map<String, Object> map3 : object2) {
                        String honor_id = (String) map3.get("honor_id");
                        String honor_level = (String) map3.get("honor_level");
                        String get_year = (String) map3.get("get_year");
                        try {
                            Integer yesr_change = Integer.parseInt(get_year);
                        } catch (Exception e) {
                            logger.info("修改数据：相关荣誉数据中年份格式有误");
                            return new JsonResult<>("相关荣誉数据中年份格式有误");
                        }
                        declareInfoService.insertDeclareHonorRel(honor_id, honor_level, get_year, declare_id);
                    }
                }
            }
        } else {
            map2.put("declare_id", declare_id);
            declareInfoService.updateDeclareInfo(map2);
            if (property_id == null || property_id.equals("")) {
                declareInfoMapper.deleteProperty(declare_id);
            } else {
                declareInfoService.updateDeclarePropertyRel(property_id, declare_id);
            }
            declareInfoMapper.deletebusiness(declare_id);

            // 修改主营业务
            // 修改成功之后该用户id下对应的所有申报id-主营业务需要修改
            JSONObject parseObject = null;
            try {
                parseObject = JSONObject.parseObject(business_data);
            } catch (Exception e) {
                logger.info("修改数据：主营业务数据格式有误");
                return new JsonResult<>("主营业务数据格式有误");
            }
            List<Map<String, Object>> object = (List<Map<String, Object>>) parseObject.get("data");
            if (object != null && object.size() > 0) {
                for (Map<String, Object> map3 : object) {
                    String business_id = (String) map3.get("business_id");
                    declareInfoService.insertDeclareBusinessRel(business_id, declare_id);
                }
            }

            // 修改相关荣誉
            JSONObject parseObject2 = null;
            if (honor_data != null && !honor_data.equals("")) {
                declareInfoMapper.deleteHonor(declare_id);
                try {
                    parseObject2 = JSONObject.parseObject(honor_data);
                } catch (Exception e) {
                    logger.info("修改数据：相关荣誉数据格式有误");
                    return new JsonResult<>("相关荣誉数据格式有误");
                }
                List<Map<String, Object>> object2 = (List<Map<String, Object>>) parseObject2.get("data");
                if (object2 != null && object2.size() > 0) {
                    for (Map<String, Object> map3 : object2) {
                        String honor_id = (String) map3.get("honor_id");
                        String honor_level = (String) map3.get("honor_level");
                        String get_year = (String) map3.get("get_year");
                        try {
                            Integer yesr_change = Integer.parseInt(get_year);
                        } catch (Exception e) {
                            logger.info("修改数据：相关荣誉数据中年份格式有误");
                            return new JsonResult<>("相关荣誉数据中年份格式有误");
                        }
                        declareInfoService.insertDeclareHonorRel(honor_id, honor_level, get_year, declare_id);
                    }
                }
            }

        }
        Object userData = declareInfoMapper.getUserInfo2(user_id, property_id);
        //logger.info("所有结果"+userData);
        return new JsonResult<>(JsonResult.SUCCESS, "ok", userData);
    }

}
