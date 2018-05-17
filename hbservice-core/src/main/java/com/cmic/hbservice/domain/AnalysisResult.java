package com.cmic.hbservice.domain;

import java.util.Date;

public class AnalysisResult {
    private Integer analysisResultId;

    private Date analysisTime;

    private String symbol;

    private Integer sHour;

    private Float t;

    private Float income;

    private Float scale;

    private Boolean current;

    private Float price;
    
    public Integer getAnalysisResultId() {
        return analysisResultId;
    }

    public void setAnalysisResultId(Integer analysisResultId) {
        this.analysisResultId = analysisResultId;
    }

    public Date getAnalysisTime() {
        return analysisTime;
    }

    public void setAnalysisTime(Date analysisTime) {
        this.analysisTime = analysisTime;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol == null ? null : symbol.trim();
    }

    public Integer getsHour() {
        return sHour;
    }

    public void setsHour(Integer sHour) {
        this.sHour = sHour;
    }

    public Float getT() {
        return t;
    }

    public void setT(Float t) {
        this.t = t;
    }

    public Float getIncome() {
        return income;
    }

    public void setIncome(Float income) {
        this.income = income;
    }

    public Float getScale() {
        return scale;
    }

    public void setScale(Float scale) {
        this.scale = scale;
    }

    public Boolean getCurrent() {
        return current;
    }

    public void setCurrent(Boolean current) {
        this.current = current;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}