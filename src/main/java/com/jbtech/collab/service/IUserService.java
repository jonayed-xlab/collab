package com.jbtech.collab.service;

import com.jbtech.collab.model.User;

import java.util.List;

public interface IUserService {
    User create(User user);
    User get(Long id);
    List<User> getAll();
    User update(Long id, User user);
    void delete(Long id);
}
