package com.example.furryfriendkeeper.services;

import com.example.furryfriendkeeper.dtos.*;
import com.example.furryfriendkeeper.entities.*;
import com.example.furryfriendkeeper.jwt.JwtTokenUtil;
import com.example.furryfriendkeeper.jwt.JwtUserDetailsService;
import com.example.furryfriendkeeper.repositories.*;
import com.example.furryfriendkeeper.utils.ListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.modelmapper.ModelMapper;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;


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
    private AddressRepository addressRepository;
    @Autowired
    private PetkeeperRepository petkeeperRepository;
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CategoriesRepository categoriesRepository;
    @Autowired
    private OwnerRepository ownerRepository;


    public ResponseEntity<JwtDTO> match(MatchUserDTO user) throws ResponseStatusException {
        User user1 = userRepository.findEmail(user.getEmail());
        if (user1 == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Email does not exist");
        }
        if (!matchPassword(user)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Password not match >:(");
        }
        String userRole = user1.getRole().getRole();
        Integer id = null;
        String name = "";
        Integer userId = null;
        String img = "";
        if (userRole.equals("PetKeeper")) {
            id = petkeeperRepository.getPetkeepersIdByEmail(user.getEmail());
            Petkeepers keeper = petkeeperRepository.getById(id);
            name = keeper.getName();
            img = keeper.getImg();
            userId = keeper.getEmail().getId();

        } else if (userRole.equals("Owner")) {
            id = ownerRepository.getPetownerIdByEmail(user.getEmail());
            Petowner owner = ownerRepository.getById(id);
            name = owner.getFirstname();
            img = owner.getImg();
            userId = owner.getEmail().getId();
        }
        JwtDTO jwtDTO = new JwtDTO(generateToken(user), generateRefreshToken(user), userRole, id, name, userId, img);
        return ResponseEntity.ok(jwtDTO);
    }

    public boolean matchPassword(MatchUserDTO user) {
        User user1 = modelMapper.map(user, User.class);
        User match = userRepository.findEmail(user.getEmail());
        if (match == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Email does not exist");
        }
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

    public JwtDTO generateNewToken(String refreshToken) {
        UserDetails userDetail = userDetailsService.loadUserByUsername(jwtTokenUtil.getUsernameFromToken(refreshToken));
        User user = userRepository.findEmail(userDetail.getUsername());
        String userRole = user.getRole().getRole();
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Token is from email that does not exist!!");
        }
        if (!jwtTokenUtil.tokenExpired(refreshToken)) {
            return new JwtDTO(jwtTokenUtil.generateToken(userDetail), refreshToken, userRole, null, null, null, null);
        } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token has expired");
    }


    public List<Role> AllRole() {
        return roleRepository.findAll();
    }

    @Transactional(rollbackOn = Exception.class)
    public SignUpPetkeeperDTO signUpPetkeeper(SignUpPetkeeperDTO signUpPetkeeperDTO) {

        User user = modelMapper.map(signUpPetkeeperDTO, User.class);
        user.setEmail(signUpPetkeeperDTO.getEmail().trim());
        user.setPassword(passwordEncoder.encode(signUpPetkeeperDTO.getPassword()));
        Role role = roleRepository.findById(signUpPetkeeperDTO.getRole())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Role Exist"));

        user.setRole(role);
        List<User> email = userRepository.uniqueUserEmail(signUpPetkeeperDTO.getEmail().trim().toLowerCase());
        for (User user1 : email) {
            System.out.println("User{id=" + user1.getId() + ", email='" + user1.getEmail());
        }
        if (email.size() != 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This email is already used!.");
        }
        try {

            User savedUser = userRepository.saveAndFlush(user);

            Address address = modelMapper.map(signUpPetkeeperDTO.getAddress(), Address.class);
            Address savedAddress = addressRepository.saveAndFlush(address);

            Petkeepers petkeepers = modelMapper.map(signUpPetkeeperDTO, Petkeepers.class);

            petkeepers.setEmail(savedUser);
            petkeepers.setAddress(savedAddress);
            petkeepers.setAvailable(1);
            petkeepers.setClosedDay("");
            Petkeepers savedPetkeepers = petkeeperRepository.saveAndFlush(petkeepers);

            Set<Petcategory> petcategories = signUpPetkeeperDTO.getCategoryId().stream()
                    .map(categoryId -> new Petcategory(signUpPetkeeperDTO.getPetkeeperId(), petRepository.getById(categoryId)))
                    .collect(Collectors.toSet());

            petcategories.forEach(petcategory -> petcategory.setPetKeeper(savedPetkeepers));
            categoriesRepository.saveAll(petcategories);

            signUpPetkeeperDTO.setPassword("Protected Field");
            return signUpPetkeeperDTO;
        } catch (Exception e) {
            throw new RuntimeException("Cannot Sign Up, Please Try again!", e);
        }

    }

    @Transactional(rollbackOn = Exception.class)
    public OwnerDTO sighUpPetOwner(OwnerDTO newOwner) {
        User user = modelMapper.map(newOwner, User.class);
        user.setEmail(newOwner.getEmail().trim());
        user.setPassword(passwordEncoder.encode(newOwner.getPassword()));
        Role role = roleRepository.findById(newOwner.getRole())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Role Exist"));

        user.setRole(role);
        List<User> email = userRepository.uniqueUserEmail(newOwner.getEmail().trim().toLowerCase());
        for (User user1 : email) {
            System.out.println("User{id=" + user1.getId() + ", email='" + user1.getEmail());
        }
        if (email.size() != 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This email is already used!.");
        }
        try {
            User savedUser = userRepository.saveAndFlush(user);

            Petowner petowner = modelMapper.map(newOwner, Petowner.class);
            petowner.setEmail(savedUser);
            ownerRepository.saveAndFlush(petowner);

            newOwner.setPassword("Protected Field");

            return newOwner;
        } catch (Exception e) {
            throw new RuntimeException("Cannot Sign Up, Please Try again!", e);
        }
    }

}
