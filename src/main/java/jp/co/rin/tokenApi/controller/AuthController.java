package jp.co.rin.tokenApi.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jp.co.rin.tokenApi.object.dto.LoginRequest;
import jp.co.rin.tokenApi.object.dto.RefreshRequest;
import jp.co.rin.tokenApi.object.dto.TokenResponse;
import jp.co.rin.tokenApi.service.AccessTokenService;
import jp.co.rin.tokenApi.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AccessTokenService accessTokenService;
	private final RefreshTokenService refreshTokenService;

	// アクセストークンとリフレッシュトークンの払出
	@PostMapping("/login")
	public TokenResponse login(@RequestBody LoginRequest request) {
		return accessTokenService.issue(request);
	}

	// アクセストークンのリフレッシュ
	@PostMapping("/refresh")
	public TokenResponse refresh(@RequestBody RefreshRequest request) {
		return refreshTokenService.refresh(request.getRefreshToken());
	}
}
/*
 * LoginRequest は userId(String) と password(String) を保持
 * RefreshRequest は refreshTeoken(String)　のみ保持
 */
