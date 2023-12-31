package com.nick.propws.controller;

import com.nick.propws.dto.JwtResponse;
import com.nick.propws.dto.OauthSignup;
import com.nick.propws.dto.SignInRequest;
import com.nick.propws.dto.SignUpRequest;
import com.nick.propws.entity.ERole;
import com.nick.propws.entity.Role;
import com.nick.propws.entity.User;
import com.nick.propws.repository.GroupRepository;
import com.nick.propws.repository.RoleRepository;
import com.nick.propws.repository.UserRepository;
import com.nick.propws.service.GroupService;
import com.nick.propws.service.UserDetailsImpl;
import com.nick.propws.util.JwtUtil;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    private GroupService groupService;
    private AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;

    @Value("${propsheet.oauth.pw}")
    private String oauthPw;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          RoleRepository roleRepository,
                          AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          GroupService groupService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.groupService = groupService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody SignInRequest signInRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        JwtResponse res = new JwtResponse();
        res.setToken(jwt);
        res.setId(userDetails.getId());
        res.setUsername(userDetails.getUsername());
        res.setRoles(roles);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("username is already taken");
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("email is already taken");
        }
        String hashedPassword = passwordEncoder.encode(signUpRequest.getPassword());
        Set<Role> roles = new HashSet<>();
        Optional<Role> userRole = roleRepository.findByName(ERole.ROLE_USER);
        if (userRole.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("role not found");
        }
        roles.add(userRole.get());
        User user = new User();
        user.setUsername(signUpRequest.getEmail());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(hashedPassword);
        user.setRoles(roles);
        user.setProvider("credentials");
        User us = userRepository.save(user);
        if(!StringUtils.isEmpty(signUpRequest.getGroupId()))
        {
            try {
                this.groupService.addUserToGroup(user, signUpRequest.getGroupId());
            } catch (Exception e) {
                System.out.println("Error adding user to group");
                return ResponseEntity.ok("Error adding group");
            }

        }
        JwtResponse res = new JwtResponse();
        return ResponseEntity.ok(res);
    }

    @PostMapping("/oauth-register")
    public ResponseEntity<?> oauthSignup(@RequestBody OauthSignup signUpRequest) {
        Authentication authentication;
        String jwt;
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            User user = userRepository.findUserByUsername(signUpRequest.getUsername());
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), oauthPw));
        } else {
            Set<Role> roles = new HashSet<>();
            Optional<Role> userRole = roleRepository.findByName(ERole.ROLE_USER);
            if (userRole.isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("role not found");
            }
            roles.add(userRole.get());
            User user = new User();
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
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> rolesReturn = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        JwtResponse res = new JwtResponse();
        res.setToken(jwt);
        res.setId(userDetails.getId());
        res.setUsername(userDetails.getUsername());
        res.setRoles(rolesReturn);
        return ResponseEntity.ok(res);
    }
}