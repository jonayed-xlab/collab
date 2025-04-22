package com.jbtech.collab.service;

import com.jbtech.collab.dto.request.ChangePasswordRequest;
import com.jbtech.collab.dto.request.UserRequest;
import com.jbtech.collab.model.User;

import java.util.List;

public interface IUserService {
    User create(UserRequest user);
    User get(Long id);
    List<User> getAll(String name, String email);
    User update(Long id, User user);
    void delete(Long id);
    void changePassword(ChangePasswordRequest request);
}
