package jp.co.rin.tokenApi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.rin.tokenApi.jwt.TokenProvider;
import jp.co.rin.tokenApi.object.dto.LoginRequest;
import jp.co.rin.tokenApi.object.dto.TokenResponse;
import jp.co.rin.tokenApi.object.dto.UserEntry;
import jp.co.rin.tokenApi.repository.LoginRepository;
import jp.co.rin.tokenApi.service.AccessTokenService;

@Service
public class AccessTokenServiceImpl implements AccessTokenService {

	@Autowired
	private LoginRepository repository;

	@Autowired
	private TokenProvider provider;

	@Override
	public TokenResponse issue(LoginRequest request) {

		// 1. ユーザ検索
		UserEntry user = repository.searchUser(request.getUserId());

		// 2. ユーザ存在チェック
		if (user == null) {
			// ユーザが存在しない場合ダミートークンを返却
			return new TokenResponse("dummyAccess", "dummyRefresh");
		}

		// 3. パスワード照合
		if (!request.getPassword().equals(user.getPassword())) {
			// パスワードが一致しない場合ダミートークンを返却
			return new TokenResponse("dummyAccess", "dummyRefresh");
		}

		// 4. JWT組立 ＆ 返却
		String accessToken = provider.createAccessToken(user.getUserId(), user.getRoles());
		String refreshToken = provider.createRefreshToken(user.getUserId());

		return new TokenResponse(accessToken, refreshToken);

	}

}
