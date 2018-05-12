package com.cmic.hbservice.service.impl;

import com.cmic.hbservice.domain.AnalysisResult;
import com.cmic.hbservice.mapper.AnalysisResultMapper;
import com.cmic.hbservice.service.AnalysisResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AnalysisResultServiceImpl implements AnalysisResultService {
    @Autowired
    private AnalysisResultMapper analysisResultMapper;

    @Override
    public List<AnalysisResult> getCurrentList() {
        var analysisResultList =  analysisResultMapper.selectAnalysisResult();
        return analysisResultList;
    }
}
