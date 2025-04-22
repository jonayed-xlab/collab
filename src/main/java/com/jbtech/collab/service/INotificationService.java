package com.jbtech.collab.service;

import com.jbtech.collab.dto.request.NotificationRequest;
import com.jbtech.collab.model.Notification;

import java.util.List;

public interface INotificationService {
    void notifyUser(NotificationRequest notificationRequest);
    void notifyUsers(List<NotificationRequest> notificationRequests);
    List<Notification> getUnreadNotifications(Long userId);
    List<Notification> getAllNotifications(Long userId);
    void markNotificationAsRead(Long id);
}
