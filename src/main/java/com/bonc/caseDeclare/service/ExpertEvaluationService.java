package com.bonc.caseDeclare.service;

import java.util.List;
import java.util.Map;

public interface ExpertEvaluationService {

    Map<String,Object> getSimpleCaseList(Map<String,Object> param);

    String exporScore(Map<String,Object> param);

    List<Map<String,Object>> getExportScoreByUser(Map<String ,Object> param);
}
