package com.example.jwt.userdetails;

import com.example.entity.User;
import com.example.jwt.models.Token;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import com.example.service.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("jwtPreAuthService")
@Slf4j
@RequiredArgsConstructor
public class JwtTokenAuthentificationUserDetailsService implements
        AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {
    private final UserService userService;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken authenticationToken)
            throws UsernameNotFoundException {

        if (authenticationToken.getPrincipal() instanceof Token token) {

            User user = userService.findByUsername(token.subject());
            JwtUser jwtUser = JwtUserFactory.create(user);
            return jwtUser;
        }
        throw new UsernameNotFoundException("Invalid token type");
    }
}
