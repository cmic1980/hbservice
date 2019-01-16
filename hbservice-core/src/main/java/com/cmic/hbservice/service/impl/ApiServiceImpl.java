package com.cmic.hbservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cmic.hbservice.service.ApiService;
import com.huobi.api.ApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

@Service
public class ApiServiceImpl implements ApiService {

    @Value("${huobi.key.path}")
    private String accessKeyPath;

    private ApiClient client;

    @Override
    public ApiClient getApiClient() {
        if(this.client == null)
        {
            try {
                FileInputStream inputStream = new FileInputStream(this.accessKeyPath);
                Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
                String json = scanner.next();
                JSONObject jsonObject = (JSONObject)JSONObject.parse(json);
                String accessKeyId = jsonObject.getString("ACCESS_KEY");
                String accessKeySecret = jsonObject.getString("SECRET_KEY");
                this.client = new ApiClient(accessKeyId, accessKeySecret);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        return this.client;
    }
}
