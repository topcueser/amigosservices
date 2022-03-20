package com.topcueser.notification;

public record NotificationRequest(
        Integer toCustomerId,
        String toCustomerName,
        String message ) {
}
