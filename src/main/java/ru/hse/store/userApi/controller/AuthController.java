package ru.hse.store.userApi.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.hse.store.restApi.dto.auth.AuthRequest;
import ru.hse.store.restApi.dto.auth.AuthResponse;
//import com.tinkoffedu.dto.auth.RefreshTokenRequest;
//import com.tinkoffedu.dto.auth.RefreshTokenResponse;
import ru.hse.store.restApi.dto.auth.RefreshTokenRequest;
import ru.hse.store.restApi.dto.auth.RefreshTokenResponse;
import ru.hse.store.restApi.endpoints.AuthApi;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.store.restApi.exceptions.InvalidArgumentException;
import ru.hse.store.userApi.dto.UserAuthDetails;
import ru.hse.store.userApi.entity.User;
import ru.hse.store.userApi.entity.UserRefreshToken;
import ru.hse.store.userApi.mapper.UserAuthDetailsMapper;
import ru.hse.store.userApi.service.UserRefreshTokenService;
import ru.hse.store.userApi.utils.UserJwtTokenUtils;

import javax.xml.datatype.Duration;

import static ru.hse.store.userApi.utils.UserPermissionUtils.getAllowedAuthorities;


@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthenticationManager authenticationManager;
    private final UserRefreshTokenService userRefreshTokenService;
    private final UserAuthDetailsMapper userAuthDetailsMapper;
    private final UserJwtTokenUtils tokenUtils;
    private final SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

    @Value("${security.jwt.expiration-time}")
    private Long expirationMinutes;

    private final SecurityContextHolderStrategy securityContextHolderStrategy =
            SecurityContextHolder.getContextHolderStrategy();

    @Override
    public ResponseEntity<?> login(AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email().toLowerCase(), request.password())
            );
            UserAuthDetails userDetails = (UserAuthDetails) authentication.getPrincipal();
//            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String accessToken = tokenUtils.createToken(userDetails);
            UserRefreshToken refreshToken = userRefreshTokenService.createRefreshToken(userDetails.getId());

//            var securityContext = securityContextHolderStrategy.createEmptyContext();
//            securityContext.authentication = authentication;
//            securityContextHolderStrategy.context = securityContext;
//            securityContextRepository.saveContext(securityContext, request, response);


//            // Cookies
//            SecurityContext context = securityContextHolderStrategy.createEmptyContext();
//            context.setAuthentication(authentication);
//            securityContextHolderStrategy.setContext(context);
//
//            // Setting new SecurityContext to the current session
//            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
//            HttpSession session = attr.getRequest().getSession(true);
//            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContextHolderStrategy.getContext());

            ResponseCookie springCookie = getResponseCookie(refreshToken);


            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, springCookie.toString())
                    .body(new AuthResponse(accessToken));

//            return ; //refreshToken.getRefreshToken()
        } catch (BadCredentialsException e) {
            throw new InvalidArgumentException("invalid username or password");
        } catch (Exception e) {
            throw new InvalidArgumentException("login request is invalid; " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> refreshToken(String refreshToken) {
        UserRefreshToken refreshTokenObj = userRefreshTokenService.updateRefreshToken(refreshToken);
        User user = refreshTokenObj.getUser();
        String accessToken = tokenUtils.createToken(
                userAuthDetailsMapper.map(user, getAllowedAuthorities(user.getRoles(), user.getPermissions()))
        );
        ResponseCookie springCookie = getResponseCookie(refreshTokenObj);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, springCookie.toString())
                .body(new AuthResponse(accessToken));
    }

    @Override
    public ResponseEntity<?> refreshToken(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        this.logoutHandler.logout(request, response, authentication);
        response.addCookie(new Cookie("refreshToken", null));
        return ResponseEntity.ok().build();
    }

    private @NotNull ResponseCookie getResponseCookie(UserRefreshToken refreshToken) {
        ResponseCookie springCookie = ResponseCookie.from("refreshToken", refreshToken.getRefreshToken())
                .httpOnly(true)
//                    .secure(true)
//                    .path("/")
                .maxAge(expirationMinutes * 60)
//                    .domain("http://localhost:3000")
                .build();
        return springCookie;
    }
}

