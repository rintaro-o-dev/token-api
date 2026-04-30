package jp.co.rin.tokenApi.object.dto;

import lombok.Data;

@Data
public class RefreshRequest {

	private String refreshToken;

}
/*
 * user 情報などは refreshToken の文字列から復元可能なので、
 * リフレッシュリクエストの DTO では refreshToken の文字列のみの定義でOK
 */
