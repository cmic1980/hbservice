package com.cmic.hbservice.mapper;

import com.cmic.hbservice.domain.Config;
import com.cmic.hbservice.domain.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ConfigMapper {
    @Select("select access_key as accessKey, secret_key as secretKey " +
            "from config ")
    Config selectConfig();
}
