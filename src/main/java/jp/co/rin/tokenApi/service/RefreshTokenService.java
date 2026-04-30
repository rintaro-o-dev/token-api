package jp.co.rin.tokenApi.service;

import jp.co.rin.tokenApi.object.dto.TokenResponse;

public interface RefreshTokenService {
	public TokenResponse refresh(String refreshToken);
}

/*
 * controller で request.getRefreshToken() を設定して
 * 文字列を引数にされているため String refreshToken が引数になる
 *
 * 戻り値は refreshToken を内包する TokenResponse
 *
 */
