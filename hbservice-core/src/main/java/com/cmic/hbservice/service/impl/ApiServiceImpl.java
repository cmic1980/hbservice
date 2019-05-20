package com.cmic.hbservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cmic.hbservice.mapper.ConfigMapper;
import com.cmic.hbservice.service.ApiService;
import com.huobi.api.ApiClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

@Service
public class ApiServiceImpl implements ApiService {

    @Value("${huobi.key.path}")
    private String accessKeyPath;

    @Autowired
    private ConfigMapper configMapper;

    private ApiClient client;

    private String accessKeyId;
    private String accessKeySecret;

    @Override
    public ApiClient getApiClient() {

        if (StringUtils.isEmpty(this.accessKeyId) || StringUtils.isEmpty(this.accessKeySecret)) {
            var config = this.configMapper.selectConfig();
            this.accessKeyId = config.getAccessKey();
            this.accessKeySecret = config.getSecretKey();
        }

        this.client = new ApiClient(this.accessKeyId, this.accessKeySecret);
        return this.client;
    }
}
