package com.bonc.caseDeclare.controller;

import com.bonc.caseDeclare.service.ExpertEvaluationService;
import com.bonc.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("evaluation")
public class ExpertEvaluationController {

    @Autowired
    private ExpertEvaluationService service;


    /**
     * 获取评审列表
     *
     * @param req
     * @return
     */
    @RequestMapping("caseList")
    public JsonResult<Object> getSimpleCaseList(HttpServletRequest req) {
        String area = req.getParameter("area");
        String fileType = req.getParameter("fileType");
        String auditState = req.getParameter("auditState");
        String userId = req.getParameter("userId");
        String page = req.getParameter("page");
        String pageSize = req.getParameter("pageSize");
        Map<String, Object> param = new HashMap<>();
        if (page != null && pageSize != null && isPositiveNumber(page) && isPositiveNumber(pageSize)) {
            param.put("page", (Integer.valueOf(page) - 1) * Integer.valueOf(pageSize));
            param.put("pageSize", Integer.valueOf(pageSize));
        }
        if (auditState == null || "".equals(auditState)) {
            return new JsonResult<>("专家评审状态错误");
        }
        if (userId == null || "".equals(userId)) {
            return new JsonResult<>("用户id错误");
        }
        param.put("userId", userId);
        param.put("area", area);
        param.put("fileType", fileType);
        param.put("auditState", auditState);
        System.out.println(param);
        Map<String, Object> result = service.getSimpleCaseList(param);
        return new JsonResult<>(result);
    }

    /**
     * 提交评审
     *
     * @param req
     * @return
     */
    @RequestMapping("expertScore")
    public JsonResult<Object> expertScore(HttpServletRequest req) {
        String screenId = req.getParameter("screenId");
        String userId = req.getParameter("userId");
        String userName = req.getParameter("userName");
        //轮次
        String auditState = req.getParameter("auditState");
        //是否暂存
        String reviewState = req.getParameter("reviewState");
        String firstScore = req.getParameter("firstScore");
        String secondScore = req.getParameter("secondScore");
        String thirdScore = req.getParameter("thirdScore");
        String fourthScore = req.getParameter("fourthScore");
        if (screenId == null || "".equals(screenId)) {
            return new JsonResult<>("筛选单号错误");
        }
        if (userId == null || "".equals(userId)) {
            return new JsonResult<>("用户错误");
        }
        if (auditState == null || "".equals(auditState)) {
            return new JsonResult<>("用户错误");
        }
        if (reviewState == null || "".equals(reviewState)) {
            return new JsonResult<>("轮数错误");
        }
        Map<String, Object> param = new HashMap<>();
        param.put("screenId", screenId);
        param.put("userId", userId);
        param.put("userName", userName);
        param.put("auditState", auditState);
        param.put("reviewState", reviewState);
        if ("1".equals(reviewState)) {
            if (!isPositiveNumber(firstScore)) {
                return new JsonResult<>("第一项评审成绩错误");
            }
            if (!isPositiveNumber(secondScore)) {
                return new JsonResult<>("第二项评审成绩错误");
            }
            if (!isPositiveNumber(thirdScore)) {
                return new JsonResult<>("第三项评审成绩错误");
            }
            if (!isPositiveNumber(fourthScore)) {
                return new JsonResult<>("第四项评审成绩错误");
            }
        }
        param.put("first", getIntNum(firstScore));
        param.put("second", getIntNum(secondScore));
        param.put("third", getIntNum(thirdScore));
        param.put("fourth", getIntNum(fourthScore));
        String msg = service.exporScore(param);
        return new JsonResult<>(JsonResult.SUCCESS, msg);
    }

    private Integer getIntNum(String str) {
        if (str == null || "".equals(str)) {
            return 0;
        }
        return Integer.valueOf(str);
    }

    private static boolean isPositiveNumber(String str) {
        if (str == null || "".equals(str)) {
            return false;
        }
        //负数：-?[0-9]+.?[0-9]*
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 查看评审人对目前的评审结果
     *
     * @param req
     * @return
     */
    @RequestMapping("getScore")
    public JsonResult<Object> getExportScoreByUser(HttpServletRequest req) {
        String screenId = req.getParameter("screenId");
        String userId = req.getParameter("userId");
        String reviewState = req.getParameter("reviewState");
        String auditState = req.getParameter("auditState");
        if (screenId == null || "".equals(screenId)) {
            return new JsonResult<>("筛选单号错误");
        }
        if (userId == null || "".equals(userId)) {
            return new JsonResult<>("用户错误");
        }
        if (reviewState == null || "".equals(reviewState)) {
            return new JsonResult<>("评审状态未知");
        }
        if (auditState == null || "".equals(auditState)) {
            return new JsonResult<>("评审级别未知");
        }
        Map<String, Object> param = new HashMap<>();
        param.put("screenId", screenId);
        param.put("userId", userId);
        param.put("reviewState", reviewState);
        param.put("auditState", auditState);

        List<Map<String, Object>> result = service.getExportScoreByUser(param);
        return new JsonResult<>(result);
    }


    public static void main(String[] args) {
        System.out.println(UUID.randomUUID().toString().replaceAll("-", ""));
        Object o = "1";
        System.out.println("1".equals(o));
    }
}
