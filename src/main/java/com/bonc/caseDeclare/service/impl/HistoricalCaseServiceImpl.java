package com.bonc.caseDeclare.service.impl;

import com.bonc.caseDeclare.mapper.HistoricalCaseMapper;
import com.bonc.caseDeclare.service.HistoricalCaseService;
import com.bonc.util.JsonResult;
import com.bonc.util.PageBeanMysql;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HistoricalCaseServiceImpl implements HistoricalCaseService {
    private static final Logger logger=LoggerFactory.getLogger(HistoricalCaseServiceImpl.class);

    @Autowired
    private HistoricalCaseMapper historicalCaseMapper;

    /**
     * 根据申报时间查询历史申报记录
     * @param year 所要查询的年限
     * @return
     */
    @Override
    public JsonResult<Object> selectDeclareInfoByDeclareTime(HttpServletRequest request,Integer declare_time) {
        String audit_state = request.getParameter("audit_state");
        String file_type = request.getParameter("file_type");
        String area_id = request.getParameter("area_id");
        String property_id = request.getParameter("property_id");
        String quoted_state = request.getParameter("quoted_state");
        String company_name = request.getParameter("company_name");
        String field_id = request.getParameter("field_id");
        String business_id = request.getParameter("business_id");
        String pageNum = request.getParameter("pageNum");
        String upload_time=request.getParameter("upload_time");
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
        map.put("fiel_type", file_type);
        map.put("area_id", area_id);
        map.put("property_id", property_id);
        map.put("quoted_state", quoted_state);
        map.put("company_name", company_name);
        map.put("field_id", field_id);
        map.put("business_id", business_id);
        map.put("start", start);
        map.put("end", end);
        map.put("upload_time", upload_time);


        int next_declare_time=declare_time+1;
        //map.put("declare_time", declare_time);
        List<Map<String, Object>> hisData = historicalCaseMapper.selectDeclareInfoByDeclareTime(map);
        //模拟数据库查询到的总条数，用于分页
        Integer count=20;
        pageBeanMysql.setCount(count);
        pageBeanMysql.setPageNum(nums);
        pageBeanMysql.setData(hisData);

        return new JsonResult<>(JsonResult.SUCCESS, "ok", pageBeanMysql);
    }
}
