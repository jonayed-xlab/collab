package com.jbtech.collab.service;

import com.jbtech.collab.dto.request.LoginRequest;
import com.jbtech.collab.dto.response.LoginResponse;

public interface ILoginService {

    LoginResponse login(LoginRequest request);

    void logout();
}
