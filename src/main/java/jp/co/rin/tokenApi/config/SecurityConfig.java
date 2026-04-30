package jp.co.rin.tokenApi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	// JWT のフィルター
    private final JwtAuthenticationFilter jwtFilter;

    // コンストラクタで JWT のフィルターを設定
    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    // CORS 設定の bean を管理
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();	// CORS の設定を new
        config.addAllowedOriginPattern("*");	// 全てのオリジン (URL)　を許可する
        config.addAllowedMethod("*");			// 全ての HTTPメソッド (GET や POST 等) を許可する
        config.addAllowedHeader("*");			// 全てのリクエストヘッダ (Authorization, Content-Type, X-Requested-With 等) を許可する
        config.setAllowCredentials(true);		// Cookie や Authorization ヘッダを送ることを許可する (Authorization ヘッダは JWT で使用するため有効)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); // ここで URLパターン ごとに CORS 設定するオブジェクト生成
        source.registerCorsConfiguration("/**", config);	// 全ての APIパス(/) に対して CORS 設定を適用する
        return source;	// Spring Security に CORS 設定を返却
    }


    // セキュリティフィルター順序にまつわる設定(http)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
        	// CORS を有効化(DEBUGの ため)
        	.cors(cors -> {})
            // CSRF は REST API では不要のため無効化 (Cookie に依存しない)
            .csrf(csrf -> csrf.disable())

            /*
             * JWT 認証は サーバー側にセッションを持たず、毎回 JWT を送ってもらう方式のため、
             * セッションは使わない (stateless　化する)　ことを設定
             */
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            /*
             * ■ 認可設定
             * JWT の作成や懸賞に必要な login や refresh に対するアクセスには認証不要 (permitAll()) を設定
             * それ以外のリクエスト (anyRequest()) には認証要 (authenticated()) を設定
             */
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/login").permitAll()
                .requestMatchers("/api/auth/refresh").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs", "/v3/api-docs/**").permitAll() //swagger
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()   // ★ これが必須
                .anyRequest().authenticated()
            )

            /*
             * ■ フィルター順序の設定
             * Spring Security が認証処理をする前に必ず JWT 検証を差し込むため、
             * JWT フィルターを SecurityContext　(SecurityContextHolderFilter) が初期化された直後に挿入
             */
            .addFilterAfter(jwtFilter, SecurityContextHolderFilter.class);

        return http.build(); // http 通信のセキュリティ設定を構築して返却
    }
}