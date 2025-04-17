package com.jbtech.collab.service;

import com.jbtech.collab.dto.request.UserRequest;
import com.jbtech.collab.dto.request.LoginRequest;
import com.jbtech.collab.dto.request.LogoutRequest;
import com.jbtech.collab.dto.response.LoginResponse;
import com.jbtech.collab.model.User;

public interface ILoginService {

    User register(UserRequest request);

    LoginResponse login(LoginRequest request);

    void logout(LogoutRequest request);
}
