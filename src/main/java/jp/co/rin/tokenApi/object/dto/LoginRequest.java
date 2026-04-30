package jp.co.rin.tokenApi.object.dto;

import lombok.Data;

@Data
public class LoginRequest {

	private String userId;
	private String password;
}

/*
 * ログイン時には userId と password　で正規のユーザか判別
 * userDB で検索
 *
 */
