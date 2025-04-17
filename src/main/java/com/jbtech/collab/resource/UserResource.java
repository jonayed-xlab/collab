package com.jbtech.collab.resource;

import com.jbtech.collab.dto.response.ApiResponse;
import com.jbtech.collab.model.User;
import com.jbtech.collab.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${app.base.url}/user")
@RequiredArgsConstructor
public class UserResource extends BaseResource {

    private final IUserService userService;

    @PostMapping
    public ApiResponse<User> create(@RequestBody User user) {
        userService.create(user);
        return ApiResponse.success(null);
    }

    @GetMapping("/{id}")
    public ApiResponse<User> get(@PathVariable Long id) {
        return ApiResponse.success(
                userService.get(id)
        );
    }

    @GetMapping
    public ApiResponse<List<User>> getAll() {
        return ApiResponse.success(
                userService.getAll()
        );
    }

    @PutMapping("/{id}")
    public ApiResponse<User> update(@PathVariable Long id, @RequestBody User user) {
        return ApiResponse.success(
                userService.update(id, user)
        );
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ApiResponse.success(null);
    }
}
