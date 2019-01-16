package com.cmic.hbservice.service.impl;

import com.cmic.hbservice.domain.AnalysisResult;
import com.cmic.hbservice.mapper.AnalysisResultMapper;
import com.cmic.hbservice.service.AnalysisResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AnalysisResultServiceImpl implements AnalysisResultService {
    @Autowired
    private AnalysisResultMapper analysisResultMapper;

    @Value("${huobi.exchange.url}")
    private String exchangeUrl;

    @Override
    public List<AnalysisResult> getCurrentList() {
        var analysisResultList =  analysisResultMapper.selectAnalysisResult();

        analysisResultList.stream().forEach(s->{
            String symbolForExchange = s.getSymbol().replace("eth","_eth");
            s.setExchangeUrl(this.exchangeUrl.concat(symbolForExchange));
        });

        return analysisResultList;
    }
}
