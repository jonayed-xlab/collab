package com.jbtech.collab.resource;

import com.jbtech.collab.dto.response.ApiResponse;
import com.jbtech.collab.model.Notification;
import com.jbtech.collab.service.INotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${app.base.url}/notification")
@RequiredArgsConstructor
public class NotificationResource {

    private final INotificationService notificationService;

    @GetMapping("/unread")
    public ApiResponse<List<Notification>> getUnreadNotifications(@RequestParam Long userId) {
        return ApiResponse.success(
                notificationService.getUnreadNotifications(userId)
        );
    }

    @GetMapping("/read")
    public ApiResponse<List<Notification>> getReadNotifications(@RequestParam Long userId) {
        List<Notification> read = notificationService.getReadNotifications(userId);
        return ApiResponse.success(read);
    }

    @PutMapping("/{id}/read")
    public ApiResponse<Void> markNotificationAsRead(@PathVariable Long id) {
        notificationService.markNotificationAsRead(id);
        return ApiResponse.success(null);
    }
}
