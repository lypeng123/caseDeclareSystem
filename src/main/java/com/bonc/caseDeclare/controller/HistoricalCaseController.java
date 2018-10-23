package com.bonc.caseDeclare.controller;

import com.bonc.caseDeclare.service.DeclareInfoService;
import com.bonc.caseDeclare.service.HistoricalCaseService;
import com.bonc.util.IPvalidateUtil;
import com.bonc.util.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/historicalCase")
public class HistoricalCaseController {

    private static final Logger logger = LoggerFactory.getLogger(HistoricalCaseController.class);

    @Autowired
    private DeclareInfoService declareInfoService; //申报信息service

    @Autowired
    private HistoricalCaseService historicalCaseService;//历史申报信息service

    /**
     * 获取历史申报信息列表
     * @param req
     * @return
     */
    @RequestMapping("/getDeclareInfo")
    public JsonResult<Object> getCaseScreenResult(HttpServletRequest req){
        String ipAddr = IPvalidateUtil.getIpAddr(req).split(",")[0];// 访问的ip
        logger.info("IP为：【"+ipAddr+"】查看了历史信息列表");

        //模拟年份数据
        int year=2017;
        JsonResult<Object> caseScreenResult = historicalCaseService.selectDeclareInfoByDeclareTime(req,year);
        logger.info("历史信息查询结果："+caseScreenResult);

        return caseScreenResult;
    }

}
