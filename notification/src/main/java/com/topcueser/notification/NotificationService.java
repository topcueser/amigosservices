package com.topcueser.notification;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void send(NotificationRequest notificationRequest) {
        notificationRepository.save(
                Notification.builder()
                    .toCustomerId(notificationRequest.toCustomerId())
                    .toCustomerEmail(notificationRequest.toCustomerName())
                    .sender("topcueser")
                    .message(notificationRequest.message())
                    .sentAt(LocalDateTime.now())
                    .build()
        );
    }
}
