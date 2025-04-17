package com.jbtech.collab.service;

import com.jbtech.collab.exception.ApiException;
import com.jbtech.collab.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@Slf4j
public class BaseService {
    private final JwtUtil jwtUtil;

    public BaseService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public String getCurrentUserContextHeaderValue() {
        Optional<String> userTokenOpt = getHeaderValue("Authorization");
        if (userTokenOpt.isEmpty()) {
            throw new ApiException("E401", "Unauthorized");
        }
        return userTokenOpt.get();
    }

    public Optional<String> getHeaderValue(String headerName) {
        try {
            HttpServletRequest request =
                    ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            return Optional.ofNullable(request.getHeader(headerName));

        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage(), ex);
        }

        return Optional.empty();
    }

    public String getCurrentUser() {

        String token = getCurrentUserContextHeaderValue();

        if (token == null || !token.startsWith("Bearer ")) {
            return null;
        }

        // Extract token without "Bearer " prefix
        String jwt = token.substring(7);

        if (jwtUtil.validateToken(jwt)) {
            return jwtUtil.getUserNameFromToken(jwt);
        }

        throw new ApiException("E401", "Unauthorized");
    }
}
