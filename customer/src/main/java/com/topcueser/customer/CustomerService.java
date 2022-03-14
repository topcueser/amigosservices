package com.topcueser.customer;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    // constructor olmadan @AllArgsConstructor ile dependency sağlanmış olur.

    public void registerCustomer(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();

        // todo: check if email valid
        // TODO: check if email not taken

        customerRepository.save(customer);
    }
}
