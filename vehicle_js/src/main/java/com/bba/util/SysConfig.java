package com.bba.util;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 系统配置注入
 */
@Component
@Data
public class SysConfig {

    /**
     * oto SHA256 key8
     */
    @Value("${tmsKet}")
    private String tmsKet;

    /**
     * oto SHA256 密钥
     */
    @Value("${tmsSecret}")
    private String tmsSecret;

    public String getSecretByKey(final String key) {
        if (key.equals(tmsKet)){
            return tmsSecret;
        }
        throw new RuntimeException("鉴权的appKey不存在");
    }
}
