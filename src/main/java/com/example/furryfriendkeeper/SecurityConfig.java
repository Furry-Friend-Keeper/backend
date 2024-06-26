package com.example.furryfriendkeeper;


import com.example.furryfriendkeeper.jwt.JwtAuthenticationEntryPoint;
import com.example.furryfriendkeeper.jwt.JwtRequestFilter;
import com.example.furryfriendkeeper.jwt.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtUserDetailsService userDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    public SecurityConfig(JwtUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Argon2PasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(new JwtAuthenticationEntryPoint()).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
//                .antMatchers("**").permitAll()
                .antMatchers("/api/users/login").permitAll()
                .antMatchers(HttpMethod.GET, "/api/keepers/all").permitAll()
                .antMatchers(HttpMethod.GET, "/api/keepers/{keeperId}").permitAll()
                .antMatchers(HttpMethod.GET, "/api/keepers/categories/all").permitAll()
                .antMatchers(HttpMethod.PATCH, "/api/keepers/{keeperId}").hasRole("PetKeeper")
                .antMatchers(HttpMethod.PATCH, "/api/keepers/{keeperId}/profile-img").hasRole("PetKeeper")
                .antMatchers(HttpMethod.POST, "/api/keepers/{keeperId}/gallery").hasRole("PetKeeper")
                .antMatchers(HttpMethod.PATCH, "/api/keepers/{keeperId}/gallery").hasRole("PetKeeper")
                .antMatchers(HttpMethod.GET, "/api/files/{keeperId}/{filename:.+}").permitAll()
                .antMatchers(HttpMethod.GET, "/api/files/{keeperId}/gallery/{filename:.+}").permitAll()
                .antMatchers("/api/users/sign-up/keeper").permitAll()
                .antMatchers("/api/users/all-roles").permitAll()
                .antMatchers("/api/users/sign-up/owner").permitAll()
                .antMatchers(HttpMethod.GET, "/api/users/owner/all").permitAll()
                .antMatchers(HttpMethod.GET, "/api/users/refreshToken").permitAll()
                .antMatchers(HttpMethod.POST, "/api/review/save").hasAnyRole("Owner")
                .antMatchers(HttpMethod.PATCH, "/api/review/edit/{reviewId}").hasAnyRole("Owner")
                .antMatchers(HttpMethod.PATCH, "/api/review/delete/{reviewId}").hasAnyRole("Owner")
                .antMatchers(HttpMethod.GET, "/api/files/{keeperId}/{filename:.+}").permitAll()
                .antMatchers(HttpMethod.GET, "/api/files/owner/{ownerId}/{filename:.+}").permitAll()
                .antMatchers(HttpMethod.GET, "/{keeperId}/gallery/{filename:.+}").permitAll()
                .antMatchers("/api/appointment/create").hasRole("Owner")
                .antMatchers(HttpMethod.GET, "/api/appointment//owner/{petownerId}").hasRole("Owner")
                .antMatchers(HttpMethod.GET, "/api/appointment/keeper/{petkeeperId}").hasRole("PetKeeper")
                .antMatchers(HttpMethod.PATCH, "/api/appointment/confirm/{appointmentId}").hasRole("PetKeeper")
                .antMatchers(HttpMethod.PATCH, "/api/appointment/cancel/{appointmentId}").hasAnyRole("PetKeeper", "Owner")
                .antMatchers(HttpMethod.PATCH, "/api/appointment/in-care/{appointmentId}").hasRole("PetKeeper")
                .antMatchers(HttpMethod.PATCH, "/api/appointment/keeper-completed/{appointmentId}").hasRole("PetKeeper")
                .antMatchers(HttpMethod.PATCH, "/api/appointment/owner-completed/{appointmentId}").hasRole("Owner")
                .antMatchers(HttpMethod.GET, "/api/owner/{petOwnerId}").hasRole("Owner")
                .antMatchers(HttpMethod.PATCH, "/api/owner/{petOwnerId}").hasRole("Owner")
                .antMatchers(HttpMethod.PATCH, "/api/owner/{ownerId}/profile-img").hasRole("Owner")
                .antMatchers(HttpMethod.PUT, "/api/owner/favorite/{ownerId}").hasRole("Owner")
                .antMatchers(HttpMethod.PATCH, "/api/keepers/closed/{keeperId}").hasRole("PetKeeper")
                .antMatchers(HttpMethod.POST, "/api/appointment/disable-schedule/{petKeeperId}").hasRole("PetKeeper")
                .antMatchers(HttpMethod.PATCH, "/api/keepers/available/{keeperId}").hasRole("PetKeeper")
                .antMatchers(HttpMethod.GET, "/api/review/check-review/{ownerId}").hasRole("Owner")
                .antMatchers("/send-message").permitAll()
                .antMatchers("/web-s/**").permitAll()
                .antMatchers("/topic/**").permitAll()
                .antMatchers("/ws/**").permitAll()
                .antMatchers("/api/notification/keeper/{notiId}").hasRole("PetKeeper")
                .antMatchers("/api/notification/owner/{notiId}").hasRole("Owner")
                .antMatchers("/api/notification/keeper/all/{keeperId}").hasRole("PetKeeper")
                .antMatchers("/api/notification/owner/all/{ownerId}").hasRole("Owner")
                .anyRequest().authenticated().and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
