package com.jbtech.collab.service.impl;


import com.jbtech.collab.dto.request.LoginRequest;
import com.jbtech.collab.dto.response.LoginResponse;
import com.jbtech.collab.exception.ApiException;
import com.jbtech.collab.model.LoginHistory;
import com.jbtech.collab.model.User;
import com.jbtech.collab.repository.LoginHistoryRepository;
import com.jbtech.collab.repository.UserRepository;
import com.jbtech.collab.service.BaseService;
import com.jbtech.collab.service.ILoginService;
import com.jbtech.collab.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class LoginService extends BaseService implements ILoginService {

    private final UserRepository userRepo;
    private final LoginHistoryRepository loginHistoryRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final HttpServletRequest servletRequest;

    public LoginService(JwtUtil jwtUtil, UserRepository userRepo, LoginHistoryRepository loginHistoryRepo, PasswordEncoder passwordEncoder, HttpServletRequest servletRequest) {
        super(jwtUtil);
        this.userRepo = userRepo;
        this.loginHistoryRepo = loginHistoryRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.servletRequest = servletRequest;
    }

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {

        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApiException("E401", "Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new ApiException("E401", "Invalid username or password");

        if (Boolean.FALSE.equals(user.isActive()))
            throw new ApiException("E401", "Need to activate account. Contact admin");

        LoginHistory loginHistory = new LoginHistory();
        loginHistory.setUserId(user.getId());
        loginHistory.setName(user.getName());
        loginHistory.setEmail(user.getEmail());
        loginHistory.setLoginTime(LocalDateTime.now());
        loginHistory.setIpAddress(getClientIp(servletRequest));

        loginHistoryRepo.save(loginHistory);

        return LoginResponse.builder()
                .token(generateToken(user))
                .build();

    }

    private String getClientIp(HttpServletRequest request) {

        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

    @Override
    public void logout() {

        LoginHistory latest = loginHistoryRepo
                .findTopByUserIdOrderByLoginTimeDesc(getCurrentUser().getId())
                .orElse(null);

        if (latest != null) {
            latest.setLogoutTime(LocalDateTime.now());
            loginHistoryRepo.save(latest);
        }
    }

    private String generateToken(User user) {
        return jwtUtil.generateToken(user.getEmail());
    }
}
