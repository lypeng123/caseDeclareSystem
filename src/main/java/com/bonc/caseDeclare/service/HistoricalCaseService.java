package com.bonc.caseDeclare.service;

import com.bonc.util.JsonResult;

import javax.servlet.http.HttpServletRequest;

public interface HistoricalCaseService {
    /**
     * 查询历史申报数据
     * @param req
     * @return
     */
    JsonResult<Object> selectDeclareInfoByDeclareTime(HttpServletRequest request, Integer year);
}
