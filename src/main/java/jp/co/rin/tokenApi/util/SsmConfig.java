package jp.co.rin.tokenApi.util;

import org.springframework.stereotype.Component;

import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParameterRequest;

@Component
public class SsmConfig {

    private final SsmClient ssm = SsmClient.builder().build();

    public String get(String key) {
        return ssm.getParameter(
            GetParameterRequest.builder()
                .name(key)
                .withDecryption(true)
                .build()
        ).parameter().value();
    }
}

/*
 * SSM から SecureString を取得するユーティリティ。
 * ParameterLoader から呼び出され、Spring Boot が application.yml のプレースホルダを解決する前に
 * 必要な環境変数を System プロパティとして注入するために使用する。
 */