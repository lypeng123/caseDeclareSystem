package com.bonc.caseDeclare.service.impl;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.bonc.caseDeclare.mapper.ExpertEvaluationMapper;
import com.bonc.caseDeclare.service.ExpertEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sound.midi.Soundbank;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service("expertEvaluationService")
public class ExpertEvaluationServiceImpl implements ExpertEvaluationService {

    @Autowired
    private ExpertEvaluationMapper mapper;

    @Override
    public Map<String, Object> getSimpleCaseList(Map<String, Object> param) {
        List<Map<String, Object>> list = mapper.getSimpleCaseList(param);
        long total = mapper.getSimpleCaseListNum(param);
        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        return result;
    }

    @Override
    @Transactional
    public String exporScore(Map<String, Object> param) {
        Integer sum = (Integer) param.get("first") + (Integer) param.get("second") + (Integer) param.get("third")
                + (Integer) param.get("fourth");
        param.put("sum", sum);
        param.put("count", 1);
        param.put("expertId", getUUID());
        System.out.println("参数为：" + param);

        //判断专家是否已经暂存过
        List<Map<String, Object>> exportScore = mapper.getExportScoreByUser(param);
        if (exportScore == null || exportScore.size() == 0) {
            mapper.insertExportScore(param);
        } else {
            Map<String, Object> score = exportScore.get(0);
            //已评审的不再评审
            if (score.get("reviewState") == null) {
                throw new RuntimeException("评审状态错误");
            } else if ("1".equals(score.get("reviewState"))) {
                return "重复评审";
            } else if ("-1".equals(score.get("reviewState")) || "0".equals(score.get("reviewState"))) {
                mapper.updateExportScore(param);
            }
        }

        //暂存不再插入汇总表
        String reviewState = (String) param.get("reviewState");
        if (reviewState == null || "-1".equals(reviewState) || "0".equals(reviewState)) {
            return "暂存成功";
        }

        Map<String, Object> exportAvg = mapper.getExportAverage(param);
        System.out.println(exportAvg);
        if (exportAvg == null) {
            System.out.println("插入exportAvg");
            param.put("avgId", getUUID());
            mapper.insertExportAverage(param);
        } else {
            System.out.println("更新exportAvg");
            mapper.updateExportAverage(param);
        }

        //计算统计表的数据
        getCompositeParam(param);

        Map<String, Object> compositeScore = mapper.getCompositeScore(param);
        if (compositeScore == null) {
            System.out.println("插入compositeScore");
            param.put("scoreId", getUUID());
            mapper.insertCompositeScore(param);
        } else {
            System.out.println("更新compositeScore");
            mapper.updateCompositeScore(param);
        }

        System.out.println("最终参数：" + param);
        return "评审成功";
    }




    @Override
    public List<Map<String, Object>> getExportScoreByUser(Map<String, Object> param) {
        System.out.println(param);
        List<Map<String, Object>> result = mapper.getExportScoreByUser(param);
        return result;
    }

    private void getCompositeParam(Map<String, Object> param) {
        String auditState = (String) param.get("auditState");
        if ("1".equals(auditState)) {
            param.put("firstSum", param.get("sum"));
            param.put("firstCount", 1);
            param.put("secondSum", 0);
            param.put("secondCount", 0);
        } else if ("2".equals(auditState)) {
            param.put("firstSum", 0);
            param.put("firstCount", 0);
            param.put("secondSum", param.get("sum"));
            param.put("secondCount", 1);
        }
    }

    private static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
