package jp.co.rin.tokenApi.config;

import jp.co.rin.tokenApi.util.SsmConfig;

public class ParameterLoader {

    public static void load() {
        SsmConfig ssm = new SsmConfig();

        System.setProperty("TOKEN_DB_HOST", ssm.get("/token-api/TOKEN_DB_HOST"));
        System.setProperty("TOKEN_DB_PORT", ssm.get("/token-api/TOKEN_DB_PORT"));
        System.setProperty("TOKEN_DB_NAME", ssm.get("/token-api/TOKEN_DB_NAME"));
        System.setProperty("TOKEN_DB_USER", ssm.get("/token-api/TOKEN_DB_USER"));
        System.setProperty("TOKEN_DB_PASS", ssm.get("/token-api/TOKEN_DB_PASS"));
        System.setProperty("JWT_SECRET", ssm.get("/token-api/JWT_SECRET"));
        System.setProperty("BASE_URL", ssm.get("/token-api/BASE_URL"));
    }
}

/*
 * Spring Boot 起動前に application.yml のプレースホルダ解決のため、
 * 必要な値を SSM から取得して System プロパティへ注入する。
 */