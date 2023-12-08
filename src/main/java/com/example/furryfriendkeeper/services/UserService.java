package com.example.furryfriendkeeper.services;

import com.example.furryfriendkeeper.dtos.JwtDTO;
import com.example.furryfriendkeeper.dtos.MatchUserDTO;
import com.example.furryfriendkeeper.dtos.UserDTO;
import com.example.furryfriendkeeper.entities.Role;
import com.example.furryfriendkeeper.entities.User;
import com.example.furryfriendkeeper.jwt.JwtTokenUtil;
import com.example.furryfriendkeeper.jwt.JwtUserDetailsService;
import com.example.furryfriendkeeper.repositories.RoleRepository;
import com.example.furryfriendkeeper.repositories.UserRepository;
import com.example.furryfriendkeeper.utils.ListMapper;
import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.modelmapper.ModelMapper;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private PasswordService passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private RoleRepository roleRepository;

    public ResponseEntity<JwtDTO> match(MatchUserDTO user) throws ResponseStatusException {
        User user1 = userRepository.findEmail(user.getEmail());
        if (user1 == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Email does not exist");
        }
        if (!matchPassword(user)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Password not match >:(");
        }
//        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Testttetst");
        String userRole = user1.getRole().getRole();
        JwtDTO jwtDTO = new JwtDTO(generateToken(user), generateRefreshToken(user), userRole);
//        return new JwtDTO(generateToken(user), generateRefreshToken(user), userRole);
        return ResponseEntity.ok(jwtDTO);
    }

    public boolean matchPassword(MatchUserDTO user) {
        User user1 = modelMapper.map(user, User.class);
        User match = userRepository.findEmail(user.getEmail());
        if (match == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Email does not exist");
        }
        match.setPassword(passwordEncoder.encode(match.getPassword()));
        return passwordEncoder.matches(user1.getPassword(), match.getPassword());

    }


    private String generateToken(MatchUserDTO user) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        UserDetails userDetail = userDetailsService.loadUserByUsername(user.getEmail());
        return jwtTokenUtil.generateToken(userDetail);

    }

    public String generateRefreshToken(MatchUserDTO user) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        UserDetails userDetail = userDetailsService.loadUserByUsername(user.getEmail());
        return jwtTokenUtil.generateRefreshToken(userDetail);
    }

    public JwtDTO generateNewToken(String refreshToken){
        UserDetails userDetail = userDetailsService.loadUserByUsername(jwtTokenUtil.getUsernameFromToken(refreshToken));
        User user = userRepository.findEmail(userDetail.getUsername());
        String userRole = user.getRole().getRole();
        if(user == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Token is from email that does not exist!!");
        }
        if(!jwtTokenUtil.tokenExpired(refreshToken)){
            return new JwtDTO(jwtTokenUtil.generateToken(userDetail),refreshToken, userRole);
        }else return null;
    }


    public User save(UserDTO newUser) {
        User user = modelMapper.map(newUser, User.class);
        user.setEmail(newUser.getEmail().trim());
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        Role role = roleRepository.findById(newUser.getRole())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Role Exist"));

        user.setRole(role);
        List<User> email = userRepository.uniqueUserEmail(newUser.getEmail().trim().toLowerCase());
        if (email.size() != 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This email is already used!.");
        }
//       if (!(enumContains(newUser.getRole().toLowerCase()))) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is no Role.");
//        }

        User savedUser = userRepository.saveAndFlush(user);
        user.setPassword("Protected Field");
        return savedUser;
    }

}
