package com.example.furryfriendkeeper.jwt;

import com.example.furryfriendkeeper.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;



@Service
public class JwtUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        com.example.furryfriendkeeper.entities.User user = userRepository.findEmail(email);
        if (user == null) {
            System.out.println(user);
            throw new UsernameNotFoundException("User not found");
        }

        user.getRole().getRole();
        return new User(user.getEmail(), user.getPassword(), getAuthority(user));
    }
    private List<GrantedAuthority> getAuthority(com.example.furryfriendkeeper.entities.User user){
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().getRole()));
        return authorities;
    }
}
