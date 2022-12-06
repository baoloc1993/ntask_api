package io.github.ntask_api.web.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.ntask_api.domain.Authority;
import io.github.ntask_api.domain.User;
import io.github.ntask_api.repository.UserRepository;
import io.github.ntask_api.security.DomainUserDetailsService;
import io.github.ntask_api.security.jwt.JWTFilter;
import io.github.ntask_api.security.jwt.TokenProvider;
import io.github.ntask_api.service.dto.UserDTO;
import io.github.ntask_api.web.rest.vm.LoginVM;
import javax.validation.Valid;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserJWTController {

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;


    @PostMapping("/authenticate")
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginVM loginVM,
                                              @RequestHeader(value = "registrationToken") String registrationToken) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            loginVM.getUsername(),
            loginVM.getPassword()
        );

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication, loginVM.isRememberMe());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        User user = userRepository.findOneWithAuthoritiesByLogin(loginVM.getUsername()).orElse(null);
        if (user != null){
            user.setNotificationKey(registrationToken);
            userRepository.save(user);
        }
        return new ResponseEntity<>(new JWTToken(jwt, (DomainUserDetailsService.CustomUser) authentication.getPrincipal()), httpHeaders, HttpStatus.OK);
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken extends UserDTO {

        private String idToken;

        JWTToken(String idToken, DomainUserDetailsService.CustomUser user) {
            this.idToken = idToken;
            id = user.getId();
            // Customize it here if you need, or not, firstName/lastName/etc
            login = user.getLogin();
            name = user.getName();
            email = user.getEmail();
            bio = user.getBio();
            avatarUrl = user.getAvatarUrl();
            roles = user.getRoles();
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }
}