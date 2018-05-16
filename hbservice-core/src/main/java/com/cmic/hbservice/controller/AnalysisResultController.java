package com.cmic.hbservice.controller;

import com.cmic.hbservice.domain.AnalysisResult;
import com.cmic.hbservice.mapper.AnalysisResultMapper;
import com.cmic.hbservice.service.AnalysisResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController("")
public class AnalysisResultController {

    @Autowired
    private AnalysisResultService analysisResultService;

    @RequestMapping("/analysis/list")
    @ResponseBody
    public List<AnalysisResult> list() {
        var analysisResultList =  analysisResultService.getCurrentList();
        return analysisResultList;
    }
}