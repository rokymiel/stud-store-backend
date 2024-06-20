package ru.hse.store.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import ru.hse.store.userApi.security.UserAuthDetailsService;
import ru.hse.store.userApi.security.permission.StaticRole;

import javax.sql.DataSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class StudStoreSecurityConfig {

    private final DataSource dataSource;
    private final StudStoreBasicAuthEntryPoint authEntryPoint;
    private final String ROOT_PATH = "/api/v1/";

    private final UserJwtAuthorizationFilter userJwtAuthorizationFilter;

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth)
//            throws Exception {
//        auth.jdbcAuthentication()
//                .dataSource(dataSource)
//                .usersByUsernameQuery("select phone_number, password, enabled "
//                        + "from tex_user "
//                        + "where phone_number = ?")
//                .authoritiesByUsernameQuery("select user_phone_number, name "
//                        + "from authority "
//                        + "where user_phone_number = ?");
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.addFilterBefore(new CustomCharacterEncodingFilter(), ChannelProcessingFilter.class);
        http.cors().and().csrf().disable()
                .authorizeHttpRequests((requests) -> requests
                        // All
                        .requestMatchers("/error/**", "/swagger/**", "/v1/api-docs/**",
                                ROOT_PATH + "project/image",
                                ROOT_PATH + "project/*",
                                ROOT_PATH + "user/register/*", ROOT_PATH + "user/*").permitAll()
                        .requestMatchers(ROOT_PATH + "auth/*")
                        .permitAll()


                        // Student only
                        .requestMatchers(ROOT_PATH + "project/new", ROOT_PATH + "project/*/update",
                                ROOT_PATH + "project/binary/upload", ROOT_PATH + "projects/my", ROOT_PATH + "project/image/**")
                        .hasRole(StaticRole.STUDENT.name())

                        .requestMatchers(ROOT_PATH + "project/binary/*")
                        .permitAll()

                        // Admin & EDU_OFFICER only
                        .requestMatchers(ROOT_PATH + "admin/*")
                        .hasAnyRole(StaticRole.ADMIN.name(), StaticRole.EDU_OFFICER.name())

                        // Public zone
                        .requestMatchers(ROOT_PATH + "public/*")
                        .permitAll()

                        // Student zone
                        .requestMatchers(ROOT_PATH + "student/*")
                        .hasRole(StaticRole.STUDENT.name())

                        .anyRequest()
                        .authenticated())

            .httpBasic(withDefaults())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
//                .authenticationProvider(userDaoAuthenticationProvider)
                .addFilterBefore(userJwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable);
//                .exceptionHandling(configurer -> configurer.authenticationEntryPoint(authEntryPoint()))
//                .build();


        return http.build();
    }

    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();

        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(false);
        filter.setAfterMessagePrefix("REQUEST DATA : ");

        return filter;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        System.out.println("kashjgfgdkjhasgfdkhjasgdljhsa");
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
//        configuration.setAllowedMethods(List.of("GET","PUT","POST","DELETE","OPTIONS"));
//        configuration.setAllowedHeaders(List.of("Content-Type","Authorization","true"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder,
                                                       UserAuthDetailsService userDetailsService) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
