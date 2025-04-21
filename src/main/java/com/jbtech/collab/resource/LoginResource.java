package com.jbtech.collab.resource;


import com.jbtech.collab.dto.request.LoginRequest;
import com.jbtech.collab.dto.response.ApiResponse;
import com.jbtech.collab.dto.response.LoginResponse;
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


    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        return ApiResponse.success(
                loginService.login(request)
        );
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout() {
        loginService.logout();
        return ApiResponse.success(null);
    }
}
