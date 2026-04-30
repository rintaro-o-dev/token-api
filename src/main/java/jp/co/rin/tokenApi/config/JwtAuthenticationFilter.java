package jp.co.rin.tokenApi.config;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jp.co.rin.tokenApi.jwt.TokenProvider;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final TokenProvider provider;

	// コンストラクタで TokenProvider を設定
	public JwtAuthenticationFilter(TokenProvider provider) {
		this.provider = provider;
	}

	@Override
    protected void doFilterInternal(
            HttpServletRequest request,		// リクエストの格納
            HttpServletResponse response,	// レスポンスの格納（前処理から受け継がれたもの）
            FilterChain filterChain)		// フィルターの順番データ
            throws ServletException, IOException {		// 例外はスロー

    	String path = request.getServletPath();

        // ★ Swagger と認証不要 API をフィルタから除外
        if (path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Authorization ヘッダから JWT を取得
    	// (Authorization: Bearer xxxxx.yyyyy.zzzzz から Bearer xxxxx.yyyyy.zzzzz　を取り出し)
        String authHeader = request.getHeader("Authorization");

        // ヘッダが無い or Bearer で始まらない場合 → 後続のフィルターへ
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // "Bearer " を除去してトークン本体を取得
        // (Bearer xxxxx.yyyyy.zzzzz から xxxxx.yyyyy.zzzzz　を取り出し)
        String token = authHeader.substring(7); // "Bearer " の7文字インデックス以降を取得

        // トークンの有効チェック
        if (provider.validateToken(token)) {

            // トークンから userId と roles を取得
            String userId = provider.getUserId(token);
            List<String> roles = provider.getRoles(token);

            /*
             * ■ roles を Spring Security　専用の権限オブジェクトのリストに変換
             * Spring Security には専用の権限オブジェクトがあるため、roles　はそのまま使えない。
             * roles の中身を、Spring Security の専用権限オブジェクト型　GrantedAuthority にそれぞれ変換
             *
             * ["ROLE_USER", "ROLE_ADMIN"]  ← ただの文字列
             *  ↓
             * new SimpleGrantedAuthority("ROLE_USER"))  ← Spring Security で使える形に変換
             * new SimpleGrantedAuthority("ROLE_ADMIN"))  ← Spring Security で使える形に変換
             */
            var authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList();

            /*
             * ■ 認証情報を作成して SecurityContext にセット
             * 認証済みユーザーを表すために、以下3点の要素を内包した認証済オブジェクトを作成
             * [認証されたユーザー名]
             * [パスワード] (JWT認証ではいらないので null)
             * [authorities] (ORLE_USER 等の権限情報)
             *
             * 「この userId は、これらの権限を持つ認証済みユーザーです」
             */
            var authentication =
                    new UsernamePasswordAuthenticationToken(userId, null, authorities);

            /*
             * ■ SecurityContext に作成した認証情報をセット
             * Spring Security に対して 「このリクエストは認証済みです」 と登録
             * 登録することで、これ以降の処理は「認証済みユーザー」として扱われる
             *  ↓
             * SecurityContextHolder.getContext().getAuthentication() から user_id や roles が取れるようになる(認可で利用)
             */
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 後続のフィルターへ
        filterChain.doFilter(request, response);
    }
}