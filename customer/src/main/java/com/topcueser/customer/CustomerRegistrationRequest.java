package com.topcueser.customer;

public record CustomerRegistrationRequest(
        String firstName,
        String lastName,
        String email ) {
}
