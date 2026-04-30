package jp.co.rin.tokenApi.object.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenResponse {

	private final String accessToken;
	private final String refreshToken;

}

/*
 * TokenAPI のレスポンスはトークンしかないので、
 * アクセストークンとリフレッシュトークンの両方格納対応できるように両方を定義
 * ★
 * ログイン時					-- アクセストークン ＆ リフレッシュトークンが格納される
 * アクセストークンのリフレッシュ時		-- アクセストークンのみ格納される（リフレッシュトークンの更新ロジックを入れない場合）
 *
 * @AllArgsConstructor で全ての要素をコンストラクタで設定扱いにすることで、
 * final 装飾子のついたフィールドはその後 immutable(不変) にできる。
 * token は処理途中で書き換えられるべきデータではないので、 immutable にする。
 */