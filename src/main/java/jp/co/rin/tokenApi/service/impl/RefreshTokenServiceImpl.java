package jp.co.rin.tokenApi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.rin.tokenApi.jwt.TokenProvider;
import jp.co.rin.tokenApi.object.dto.TokenResponse;
import jp.co.rin.tokenApi.service.RefreshTokenService;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

	@Autowired
	private TokenProvider provider;

	public TokenResponse refresh(String refreshToken) {

		// 1.refreshToken の有効チェック
		if (!provider.validateToken(refreshToken)) {
			return new TokenResponse("dummyAccess","dummyRefresh");
		}

		// 2. refreshToken から user_id と roles を取得
		String userId = provider.getUserId(refreshToken);
		List<String> roles = provider.getRoles(refreshToken);

		// 3. refreshToken から取得した user_id と roles でアクセストークンを再取得
		String accessToken = provider.createAccessToken(userId, roles);

		// 4. アクセストークンのみ再発行 (refreshToken はそのまま返却)
		return new TokenResponse(accessToken, refreshToken);
	}
}
