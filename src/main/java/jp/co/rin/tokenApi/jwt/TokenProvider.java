package jp.co.rin.tokenApi.jwt;

import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class TokenProvider {

	// ※import は yml と紐づけられる依存を選択
    @Value("${jwt.secret}")
    private String secretKey; // 署名キー

    @Value("${jwt.access-token-expire}")
    private long accessTokenExpire; // アクセストークン 有効時間

    @Value("${jwt.refresh-token-expire}")
    private long refreshTokenExpire; // リフレッシュトークン 有効時間

    // 署名キー返却メソッド
    private SecretKey getSigningKey() {
    	return Keys.hmacShaKeyFor(secretKey.getBytes());
	}


    /*
     * ■ トークン発行メソッド
     * yml に設定した有効期限を参照してトークン作成
     */
    // AccessToken 生成
    public String createAccessToken(String userId, List<String> roles) {

        Date now = new Date(); // 現在日時
        Date expiry = new Date(now.getTime() + accessTokenExpire); // 有効期限（終了）

        // JWT を組み立てて返却
        return Jwts.builder()						// JWT のビルダー
                .setSubject(userId)					// payload の sub(誰) を設定
                .claim("roles", roles)				// payload に独自の属性 roles を追加して設定
                .setIssuedAt(now)					// payload にトークン発行時刻 iat を設定
                .setExpiration(expiry)				// payload にトークン有効期限 exp を設定
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)	// signeture に署名キーを設定
                .compact();							// 最後に JWT を文字列変換
    }

	// RefreshToken 生成
    public String createRefreshToken(String userId) {

        Date now = new Date(); // 現在日時
        Date expiry = new Date(now.getTime() + refreshTokenExpire);	// 有効期限（終了）

        return Jwts.builder()						// JWT のビルダー
                .setSubject(userId)					// payload の sub(誰) を設定
                .setIssuedAt(now)					// payload にトークン再発行時刻 iat を設定
                .setExpiration(expiry)				// payload にトークン有効期限 exp を設定
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)	// signeture に署名キーを設定
                .compact();							// 最後に JWT を文字列変換
    }


    /*
     * ■ トークンリフレッシュ前の検証用メソッド
     * リフレッシュ時に存在チェック、トークンの再発行用に解析データの取得を担当
     * token は xxxxx.yyyyy.zzzzz の形
     */
    // validation 確認 (トークンの有効チェック)
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()					// JWT の解析用ビルダー
                .setSigningKey(getSigningKey())		// 解析用に署名キーを再計算しセット
                .build()							// 解析データを構築
                .parseClaimsJws(token);				// JWT の解析（署名・有効期限・形式）
            return true;							// 解析が成功で有効であることを返却

        } catch (Exception e) {
            return false;							// 解析できなかった場合、無効とみなす
        }
    }

    // トークンから user_id を取得
    public String getUserId(String token) {
        return Jwts.parserBuilder()					// JWT の解析用ビルダー
                .setSigningKey(getSigningKey())		// 解析用に署名キーを再計算しセット
                .build()							// 解析データを構築
                .parseClaimsJws(token)				// JWT の解析（署名・有効期限・形式）
                .getBody()							// 解析結果から body(payload) を抜き出し
                .getSubject();   					// sub を返す
    }

    // トークンから roles を取得
    public List<String> getRoles(String token) {
        return Jwts.parserBuilder()					// JWT の解析用ビルダー
                .setSigningKey(getSigningKey())		// 解析用に署名キーを再計算しセット
                .build()							// 解析データを構築
                .parseClaimsJws(token)				// JWT の解析（署名・有効期限・形式）
                .getBody()							// 解析結果から body(payload) を抜き出し
                .get("roles", List.class);			// 独自属性 roles を List<String> で返す
    }
}
