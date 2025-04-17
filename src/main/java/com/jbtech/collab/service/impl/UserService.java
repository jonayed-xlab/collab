package com.jbtech.collab.service.impl;

import com.jbtech.collab.exception.ApiException;
import com.jbtech.collab.model.User;
import com.jbtech.collab.repository.UserRepository;
import com.jbtech.collab.service.BaseService;
import com.jbtech.collab.service.IUserService;
import com.jbtech.collab.utils.JwtUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService extends BaseService implements IUserService {

    private final UserRepository userRepo;

    public UserService(JwtUtil jwtUtil, UserRepository userRepo) {
        super(jwtUtil);
        this.userRepo = userRepo;
    }

    @Override
    public User create(User user) {
        return userRepo.save(user);
    }

    @Override
    public User get(Long id) {
        return userRepo.findById(id).orElseThrow(() -> new ApiException("E404", "User not found"));
    }

    @Override
    public List<User> getAll() {
        return userRepo.findAll();
    }

    @Override
    public User update(Long id, User user) {
        User existing = get(id);
        existing.setName(user.getName());
        existing.setEmail(user.getEmail());
        return userRepo.save(existing);
    }

    @Override
    public void delete(Long id) {
        userRepo.deleteById(id);
    }
}