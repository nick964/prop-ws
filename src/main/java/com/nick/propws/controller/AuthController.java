package com.nick.propws.controller;

import com.nick.propws.dto.*;
import com.nick.propws.entity.ERole;
import com.nick.propws.entity.Role;
import com.nick.propws.entity.User;
import com.nick.propws.repository.GroupRepository;
import com.nick.propws.repository.RoleRepository;
import com.nick.propws.repository.UserRepository;
import com.nick.propws.service.GroupService;
import com.nick.propws.service.StoreService;
import com.nick.propws.service.UserDetailsImpl;
import com.nick.propws.util.JwtUtil;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    Logger logger = LoggerFactory.getLogger(AuthController.class);
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    private GroupService groupService;
    private AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;

    private StoreService storageService;

    @Value("${propsheet.oauth.pw}")
    private String oauthPw;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          RoleRepository roleRepository,
                          AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          GroupService groupService,
                          StoreService storageService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.groupService = groupService;
        this.jwtUtil = jwtUtil;
        this.storageService = storageService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody SignInRequest signInRequest) {
        logger.info("Recieved sign in request for user: " + signInRequest.getUsername());
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        User user = userRepository.findUserByUsername(signInRequest.getUsername());
        JwtResponse res = new JwtResponse();
        res.setToken(jwt);
        res.setJwtExpiration(jwtUtil.getJwtExpiration());
        res.setId(userDetails.getId());
        res.setUsername(userDetails.getUsername());
        res.setRoles(roles);
        res.setIcon(user.getIcon());
        res.setName(user.getName());
        return ResponseEntity.ok(res);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam(value = "groupId", required = false) String groupId,
            @RequestPart(value = "picture", required = false) MultipartFile picture) {
        if (userRepository.existsByUsername(email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("username is already taken");
        }
        if (userRepository.existsByEmail(email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("email is already taken");
        }
        String hashedPassword = passwordEncoder.encode(password);
        Set<Role> roles = new HashSet<>();
        Optional<Role> userRole = roleRepository.findByName(ERole.ROLE_USER);
        if (userRole.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("role not found");
        }
        roles.add(userRole.get());
        User user = new User();
        user.setUsername(email);
        user.setEmail(email);
        user.setName(firstName + " " + lastName);
        user.setPassword(hashedPassword);
        user.setRoles(roles);
        user.setProvider("credentials");
        if(picture != null && !picture.isEmpty()) {
            UploadFileDto uploadFile = storageService.uploadFile(picture);
            user.setIcon(uploadFile.getIconUrl());
            user.setIconObject(uploadFile.getObjectName());
        }
        userRepository.save(user);
        if(!StringUtils.isEmpty(groupId))
        {
            try {
                this.groupService.addUserToGroup(user, groupId);
            } catch (Exception e) {
                System.out.println("Error adding user to group");
                return ResponseEntity.ok("Error adding group");
            }

        }
        JwtResponse res = new JwtResponse();
        res.setIcon(user.getIcon());
        return ResponseEntity.ok(res);
    }

    @PostMapping("/oauth-register")
    public ResponseEntity<?> oauthSignup(@RequestBody OauthSignup signUpRequest) {
        Authentication authentication;
        String jwt;
        User user;
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            user = userRepository.findUserByUsername(signUpRequest.getUsername());
            if(!user.getProvider().equals(signUpRequest.getProvider())) {
                JwtResponse response = new JwtResponse();
                response.setSuccess(false);
                response.setErrorMessage("Error - A user already exists with this username. Did you login with "+ user.getProvider() + "?");
                return ResponseEntity.ok(response);
            }
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), oauthPw));
        } else {
            Set<Role> roles = new HashSet<>();
            Optional<Role> userRole = roleRepository.findByName(ERole.ROLE_USER);
            if (userRole.isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("role not found");
            }
            roles.add(userRole.get());
            user = new User();
            user.setName(signUpRequest.getName());
            user.setUsername(signUpRequest.getUsername());
            user.setEmail(signUpRequest.getEmail());
            user.setProvider(signUpRequest.getProvider());
            user.setIcon(signUpRequest.getImg());
            user.setRoles(roles);
            String nonHashedPass = oauthPw;
            String userPass = passwordEncoder.encode(nonHashedPass);
            user.setPassword(userPass);
            userRepository.save(user);
            User saveduser = userRepository.save(user);
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(saveduser.getUsername(), nonHashedPass));
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        jwt = jwtUtil.generateJwtToken(authentication);
        Date expires = jwtUtil.getJwtExpiration();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> rolesReturn = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        JwtResponse res = new JwtResponse();
        res.setToken(jwt);
        res.setJwtExpiration(expires);
        res.setId(userDetails.getId());
        res.setUsername(userDetails.getUsername());
        res.setRoles(rolesReturn);
        res.setIcon(user.getIcon());
        res.setName(user.getName());
        return ResponseEntity.ok(res);
    }
}