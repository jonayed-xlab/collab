package com.jbtech.collab.resource;


import com.jbtech.collab.dto.request.UserRequest;
import com.jbtech.collab.dto.request.LoginRequest;
import com.jbtech.collab.dto.request.LogoutRequest;
import com.jbtech.collab.dto.response.ApiResponse;
import com.jbtech.collab.dto.response.LoginResponse;
import com.jbtech.collab.model.User;
import com.jbtech.collab.service.ILoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${app.base.url}/auth")
@RequiredArgsConstructor
public class LoginResource extends BaseResource {

    private final ILoginService loginService;

    @PostMapping("/register")
    public ApiResponse<User> register(@RequestBody UserRequest request) {
        loginService.register(request);
        return ApiResponse.success(null);
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        return ApiResponse.success(
                loginService.login(request)
        );
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody LogoutRequest request) {
        loginService.logout(request);
        return ApiResponse.success(null);
    }
}
