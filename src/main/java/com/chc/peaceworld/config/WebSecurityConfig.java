package com.chc.peaceworld.config;

import com.chc.peaceworld.security.JwtAuthenticationEntryPoint;
import com.chc.peaceworld.security.JwtAuthorizationTokenFilter;
import com.chc.peaceworld.security.service.impl.JwtUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private JwtUserDetailsServiceImpl jwtUserDetailsService;

    // Custom JWT based security filter
//    @Autowired
//    JwtAuthorizationTokenFilter authenticationTokenFilter;

    @Bean
    public JwtAuthorizationTokenFilter authorizationTokenFilter()throws Exception{
        return new JwtAuthorizationTokenFilter();
    }
    @Value("${jwt.tokenHead}")
    private String tokenHeader;

//    @Value("${jwt.route.authentication.path}")
//    private String authenticationPath;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(jwtUserDetailsService)
                .passwordEncoder(passwordEncoderBean());
    }

    @Bean
    public PasswordEncoder passwordEncoderBean() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // we don't need CSRF because our token is invulnerable
                .csrf().disable()

                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()

                // don't create session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                .authorizeRequests()

                // 允许对于网站静态资源的无授权访问
                .antMatchers(
                        HttpMethod.GET,
                        "/",
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js"
                ).permitAll()
                // 对于获取token的rest api要允许匿名访问
                .antMatchers("/user/login").permitAll()
                .antMatchers("/user/register").permitAll()
                .antMatchers("/user/refresh").permitAll()
                .antMatchers("/user/info").permitAll()
                .antMatchers("/redis/**").permitAll()
                .antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources/**",
                        "/configuration/security", "/swagger-ui.html", "/webjars/**").permitAll()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated();

        httpSecurity
                .addFilterBefore(authorizationTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        // disable page caching
        httpSecurity
                .headers()
                .frameOptions().sameOrigin()  // required to set for H2 else H2 Console will be blank.
                .cacheControl();
    }

//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        // AuthenticationTokenFilter will ignore the below paths
//        web
//            .ignoring()
//            .antMatchers(
//                HttpMethod.POST,
//                "/auth"
//            )
//
//            // allow anonymous resource requests
//            .and()
//            .ignoring()
//            .antMatchers(
//                HttpMethod.GET,
//                "/",
//                "/*.html",
//                "/favicon.ico",
//                "/**/*.html",
//                "/**/*.css",
//                "/**/*.js"
//            )
//
//            // Un-secure H2 Database (for testing purposes, H2 console shouldn't be unprotected in production)
//            .and()
//            .ignoring()
//            .antMatchers("/h2-console/**/**")
//                .antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources/**",
//                        "/configuration/security", "/swagger-ui.html", "/webjars/**");
//    }
}
