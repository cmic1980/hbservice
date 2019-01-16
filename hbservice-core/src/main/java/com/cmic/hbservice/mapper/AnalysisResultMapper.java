package com.cmic.hbservice.mapper;

import com.cmic.hbservice.domain.AnalysisResult;
import com.cmic.hbservice.domain.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AnalysisResultMapper {
    @Select("select analysis_result_id as analysisResultId, " +
            "analysis_time as analysisTime, symbol, s_hour as shour, " +
            "t,income,scale,current,price,vol,amount,days " +
            "from analysis_result " +
            "where current = 1 order by vol desc")
    List<AnalysisResult> selectAnalysisResult();


}
