package com.cmic.hbservice.mapper;

import com.cmic.hbservice.domain.AnalysisResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AnalysisResultMapper {
    @Select("select analysis_result_id as analysisResultId, " +
            "analysis_time as analysisTime, symbol, s_hour as shour, " +
            "t,income,scale,current " +
            "from analysis_result " +
            "where current = 1 order by s_hour")
    List<AnalysisResult> selectAnalysisResult();
}
