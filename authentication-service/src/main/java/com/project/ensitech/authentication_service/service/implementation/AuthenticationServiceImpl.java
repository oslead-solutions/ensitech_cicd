package com.project.ensitech.authentication_service.service.implementation;

import com.project.ensitech.authentication_service.config.security.jwt.JwtUtil;
import com.project.ensitech.authentication_service.model.dto.*;
import com.project.ensitech.authentication_service.model.entity.User;
import com.project.ensitech.authentication_service.model.enumeration.Role;
import com.project.ensitech.authentication_service.model.mapper.UserMapper;
import com.project.ensitech.authentication_service.repository.UserRepository;
import com.project.ensitech.authentication_service.service.common.IAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements IAuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    @Override
   // public AuthenticationResponse register(RegisterRequest request) {
    public String register(RegisterRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email attribué à un autre utilisateur");

        }
        var user = User.builder()
                .email(request.getEmail())
                .role(Role.valueOf(request.getRole()))
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(user);
        /*var jwtToken = jwtUtil.generateToken(user);
        return AuthenticationResponse.builder()
                .id(r)
                .token(jwtToken)
                .build();*/
        return "Utilisateur enregistré avec succès";
    }

    @Override
    public AuthenticationResponseDto login(LoginRequestDto request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Email incorrect"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Mot de passe incorrect");
        }
        //user.get
        var jwtToken = jwtUtil.generateToken(user);
        user.setToken(jwtToken);
       /** return AuthenticationResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .token(jwtToken)
                .build();*/
       return userMapper.toUserDto(user);
    }

    public String updatePassword(UpdatePasswordRequestDto request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(()->  new RuntimeException("Utilisateur introuvable"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            // return "Ancien mot de passe incorrect";
            throw new RuntimeException( "Ancien mot de passe incorrect");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return "Mot de passe mis à jour";
    }
    public String updateEmail(Integer userId, UpdateEmailRequestDto request) {
        User user = userRepository.findById(userId).orElseThrow(()->  new RuntimeException("Utilisateur introuvable"));
        if (userRepository.existsByEmail(request.getNewEmail()))  throw new RuntimeException("Email déjà attribué à un utilisateur");// return "Email déjà pris";


        user.setEmail(request.getNewEmail());
        userRepository.save(user);
        return "Email mis à jour";
    }
}
