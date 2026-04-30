package jp.co.rin.tokenApi.service;

import jp.co.rin.tokenApi.object.dto.LoginRequest;
import jp.co.rin.tokenApi.object.dto.TokenResponse;

public interface AccessTokenService {
	public TokenResponse issue(LoginRequest request);

}

/*
 * 引数は LoginRequest (ここに userId と password が格納されている)
 * 戻り値は TokenResponse で、accessToken と refreshToken 両方を返す
 */
