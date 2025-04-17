package com.jbtech.collab.service.impl;


import com.jbtech.collab.dto.request.LoginRequest;
import com.jbtech.collab.dto.request.LogoutRequest;
import com.jbtech.collab.dto.request.UserRequest;
import com.jbtech.collab.dto.response.LoginResponse;
import com.jbtech.collab.exception.ApiException;
import com.jbtech.collab.model.User;
import com.jbtech.collab.repository.UserRepository;
import com.jbtech.collab.service.BaseService;
import com.jbtech.collab.service.ILoginService;
import com.jbtech.collab.utils.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoginService extends BaseService implements ILoginService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginService(JwtUtil jwtUtil, UserRepository userRepo, PasswordEncoder passwordEncoder) {
        super(jwtUtil);
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }


    @Override
    @Transactional
    public User register(UserRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );
        return userRepo.save(user);
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApiException("E401", "Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ApiException("E401", "Invalid username or password");
        }

        return LoginResponse.builder()
                .token(generateToken(user))
                .build();

    }

    @Override
    public void logout(LogoutRequest request) {
        // TODO: need implementation
    }

    private String generateToken(User user) {
        return jwtUtil.generateToken(user.getEmail());
    }
}
