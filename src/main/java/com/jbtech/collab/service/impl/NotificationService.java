package com.jbtech.collab.service.impl;

import com.jbtech.collab.dto.request.NotificationRequest;
import com.jbtech.collab.model.Notification;
import com.jbtech.collab.repository.NotificationRepository;
import com.jbtech.collab.service.INotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService implements INotificationService {

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void notifyUser(NotificationRequest request) {

        Notification notification = Notification.builder()
                .userId(request.getUserId())
                .projectId(request.getProjectId())
                .workPackageId(request.getWorkPackageId())
                .title(request.getTitle())
                .timestamp(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);

        messagingTemplate.convertAndSend(
                "/topic/notifications/" + request.getUserId(),
                notification
        );
    }

    @Override
    public void notifyUsers(List<NotificationRequest> requests) {
        requests.forEach(this::notifyUser);
    }

    @Override
    public List<Notification> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUserIdAndReadFalse(userId);
    }

    @Override
    public List<Notification> getAllNotifications(Long userId) {
        return notificationRepository.findByUserId(userId);
    }

    @Override
    public void markNotificationAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }
}
