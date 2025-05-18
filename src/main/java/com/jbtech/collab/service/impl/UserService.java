package com.jbtech.collab.service.impl;

import com.jbtech.collab.dto.request.ChangePasswordRequest;
import com.jbtech.collab.dto.request.UserRequest;
import com.jbtech.collab.exception.ApiException;
import com.jbtech.collab.model.User;
import com.jbtech.collab.repository.UserRepository;
import com.jbtech.collab.service.BaseService;
import com.jbtech.collab.service.IUserService;
import com.jbtech.collab.utils.JwtUtil;
import com.jbtech.collab.utils.UserRoleEnum;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class UserService extends BaseService implements IUserService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(JwtUtil jwtUtil, UserRepository userRepo, PasswordEncoder passwordEncoder) {
        super(jwtUtil);
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User create(UserRequest request) {

        userRepo.findByEmail(request.getEmail())
                .ifPresent(user -> {
                    throw new ApiException("E400", "User already exists");
                });

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );
        if ("admin@gmail.com".equalsIgnoreCase(request.getEmail())) {
            user.setRole(UserRoleEnum.ADMIN);
            user.setActive(Boolean.TRUE);
        } else {
            user.setActive(Boolean.FALSE);
        }
        return userRepo.save(user);
    }

    @Override
    public User get(Long id) {
        return userRepo.findById(id).orElseThrow(() -> new ApiException("E404", "User not found"));
    }

    @Override
    public List<User> getAll(String name, String email) {

        if ((name == null || name.isEmpty()) && (email == null || email.isEmpty())) {
            return userRepo.findAll();
        }

        return userRepo.findByNameContainingIgnoreCaseAndEmailContainingIgnoreCase(
                name != null ? name : "",
                email != null ? email : ""
        );
    }


    @Override
    public User update(Long id, User user) {
        User existing = get(id);

        if (user.getName() != null) {
            existing.setName(user.getName());
        }
        if (user.getEmail() != null) {
            existing.setEmail(user.getEmail());
        }
        if (user.getRole() != null) {
            existing.setRole(user.getRole());
        }
        if (!Objects.equals(existing.isActive(),user.isActive())) {
            existing.setActive(user.isActive());
        }

        return userRepo.save(existing);
    }

    @Override
    public void delete(Long id) {
        if (!Objects.equals(UserRoleEnum.ADMIN, getCurrentUser().getRole())) {
            throw new ApiException("E401", "Admin only delete user");
        }
        userRepo.deleteById(id);
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        User user = getCurrentUser();
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new ApiException("E401", "Invalid old password");
        }
        user.setPassword(
                passwordEncoder.encode(request.getNewPassword())
        );
        userRepo.save(user);
    }

    @Override
    public User getMe() {
        User currentUser = getCurrentUser();
        return userRepo.findByEmail(currentUser.getEmail())
                .orElseThrow(() -> new ApiException("E404", "User not found"));
    }
}