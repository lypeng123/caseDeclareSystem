package com.bonc.caseDeclare.service.impl;

import com.bonc.caseDeclare.mapper.LoginMapper;
import com.bonc.caseDeclare.mapper.UserManageMapper;
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
    private static final Logger logger=LoggerFactory.getLogger(UserManageServiceImpl.class);

    @Autowired
    private UserManageMapper userManageMapper;

    @Autowired
    private LoginMapper loginMapper;

    /**
     * 查询所有用户信息
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
        try{
            nums = Integer.parseInt(pageNum);
            if(nums == 0){
                logger.info("页码无效！");
                return new JsonResult<>("页码无效！");
            }
        }catch(NumberFormatException e){
            logger.info("页码无效！");
            return new JsonResult<>("页码无效！");
        }


        PageBeanMysql<Object> pageBeanMysql = new PageBeanMysql<Object>(nums);
        Integer start = pageBeanMysql.getStart();
        Integer end = pageBeanMysql.getEnd();
        if(start < 0 || end < 0){
            logger.info("页码无效！");
            return new JsonResult<>("页码无效！");
        }


        Map<String,Object> map = new HashMap<String,Object>();
        map.put("start", start);
        map.put("end", end);

        List<Map<String, Object>> userData = userManageMapper.getAllUser(map);
        //模拟数据库查询到的总条数，用于分页上线需更改
        Integer count=20;
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
        String register_time=request.getParameter("register_time");
        if(pageNum == null || pageNum.equals("")
                || audit_state == null || audit_state.equals("")){
            logger.info("参数有误");
            return new JsonResult<>("参数有误！");
        }

        Integer nums;
        try{
            nums = Integer.parseInt(pageNum);
            if(nums == 0){
                logger.info("页码无效！");
                return new JsonResult<>("页码无效！");
            }
        }catch(NumberFormatException e){
            logger.info("页码无效！");
            return new JsonResult<>("页码无效！");
        }


        PageBeanMysql<Object> pageBeanMysql = new PageBeanMysql<Object>(nums);
        Integer start = pageBeanMysql.getStart();
        Integer end = pageBeanMysql.getEnd();
        if(start < 0 || end < 0){
            logger.info("页码无效！");
            return new JsonResult<>("页码无效！");
        }


        Map<String,Object> map = new HashMap<String,Object>();
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

        List<Map<String,Object>> selectFirstInfo = userManageMapper.allDeclareCompanyAccount(map);

        Integer count = userManageMapper.selectCount(map);

        pageBeanMysql.setPageNum(nums);
        pageBeanMysql.setCount(count);
        pageBeanMysql.setData(selectFirstInfo);

        logger.info("查询结果："+ pageBeanMysql);
        return new JsonResult<>(JsonResult.SUCCESS,"ok",pageBeanMysql);
    }

    @Override
    public JsonResult<Object> findUserById(String user_id) {
        if(user_id==null || "".equals(user_id)){
            return new JsonResult<>(JsonResult.ERROR, "用户id为空");
        }
        //调用userManageMapper查询该用户信息
        Map<String,Object> userById = userManageMapper.findUserById(user_id);
        logger.info("查询结果："+userById);
        if(userById.isEmpty()){
           return new JsonResult<>(JsonResult.ERROR, "没有查询到用户");
        }

        return new JsonResult<>(JsonResult.SUCCESS,"ok" , userById);
    }

    @Override
    @Transactional
    public JsonResult<Object> updateUserById(HttpServletRequest request,String user_id,String ipAddr) {
        if(user_id==null || "".equals(user_id)){
            return new JsonResult<>(0,"用户id为空");
        }
        JsonResult<Object> deleteUserById = deleteUserById(user_id);
        if(deleteUserById.getState()==1){
            logger.info("用户"+user_id+"被删除");
        }
        //更新用户信息
        saveUser(request,ipAddr);
        return new JsonResult<>(JsonResult.SUCCESS, "修改成功");
    }

    @Override
    @Transactional
    public JsonResult<Object> deleteUserById(String user_id) {
        boolean deleteUserById = userManageMapper.deleteUserById(user_id);
        if(!deleteUserById){
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
        if(user_id==null || "".equals(user_id)){
            return new JsonResult<>(JsonResult.ERROR, "用户id为空");
        }
        //调用userManageMapper查询该用户对应的所有案例
        List<Map<String,Object>> caseByUserId = userManageMapper.findCaseByUserId(user_id);
        logger.info("查询结果："+caseByUserId);
        if(caseByUserId.isEmpty()){
            return new JsonResult<>(JsonResult.ERROR, "没有查询到该用户对应案例");
        }

        return new JsonResult<>(JsonResult.SUCCESS,"ok" , caseByUserId);
    }

    @Override
    public JsonResult<Object> updateAuditState(String screen_id,String audit_state) {
        if(screen_id==null || "".equals(screen_id)){
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
            userData = userManageMapper.getUserInfo2(user_id,property_id);
            logger.info("申报页返回信息2：" + userData);
            return userData;
        }
    }

}
