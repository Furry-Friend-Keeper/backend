package com.example.furryfriendkeeper;

import com.example.furryfriendkeeper.jwt.JwtTokenUtil;
import com.example.furryfriendkeeper.repositories.UserRepository;
import com.sun.security.auth.UserPrincipal;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.slf4j.Logger;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;
import java.util.UUID;


public class UserHandshakeHandler extends DefaultHandshakeHandler {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private UserRepository userRepository;

    private final Logger LOG =  LoggerFactory.getLogger(UserHandshakeHandler.class);


    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
//        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");
//        String userId = request.getHeaders().getFirst("userId-for-handshake");
        MultiValueMap<String, String> queryParams = UriComponentsBuilder.fromUri(request.getURI()).build().getQueryParams();
        String userId = queryParams.getFirst("userId");

        if (userId != null) {
            // Perform any necessary operations with the email
            LOG.info("User guest id: {}", userId);
            return new UserPrincipal(userId);
        }
        String email = "test4@example.com";
        LOG.info("User guest id: {}", email);
        return new UserPrincipal(email);




//            String email = jwtTokenUtil.getUsernameFromToken(token);
//            LOG.info("User email: {}", emailCheck);
//            return new UserPrincipal(email);

    }
}
