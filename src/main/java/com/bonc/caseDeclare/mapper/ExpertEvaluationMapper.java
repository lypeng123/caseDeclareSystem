package com.bonc.caseDeclare.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ExpertEvaluationMapper {

    List<Map<String,Object>> getSimpleCaseList(Map<String,Object> param);

    long getSimpleCaseListNum(Map<String,Object> param);

    void insertExportScore(Map<String,Object> param);

    void insertExportAverage(Map<String,Object> param);

    Map<String,Object> getExportAverage(Map<String,Object> param);

    void updateExportAverage(Map<String,Object> param);

    void insertCompositeScore(Map<String,Object> param);

    Map<String,Object> getCompositeScore(Map<String,Object> param);

    void updateCompositeScore(Map<String,Object> param);

    List<Map<String,Object>> getExportScoreByUser(Map<String,Object> param);

    void updateExportScore(Map<String,Object> param);
}
