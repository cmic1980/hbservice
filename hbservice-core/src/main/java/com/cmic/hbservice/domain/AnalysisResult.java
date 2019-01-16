package com.cmic.hbservice.domain;

import org.springframework.beans.factory.annotation.Value;

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

    private Float vol;

    private Float amount;

    private int days;

    private String exchangeUrl;

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

    public Float getVol() {
        return vol;
    }

    public void setVol(Float vol) {
        this.vol = vol;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }


    public String getExchangeUrl() {
        return exchangeUrl;
    }

    public void setExchangeUrl(String exchangeUrl) {
        this.exchangeUrl = exchangeUrl;
    }
}