package com.topcueser.customer;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class CustomerService {

    // constructor olmadan @AllArgsConstructor ile dependency sağlanmış olur.
    private final CustomerRepository customerRepository;
    private final RestTemplate restTemplate;

    public void registerCustomer(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();

        // todo: check if email valid
        // TODO: check if email not taken

        customerRepository.saveAndFlush(customer);

        // todo: check if fraudster
        // url : http://localhost:8081/api/v1/fraud-check/{customerId}
        // eureka eklendiğinde server tanımlandığı için url aşağıdaki gibi düzenlenir.
        // application.name üzerinden uppercase ile tanınmış olur.
        FraudCheckResponse fraudCheckResponse = restTemplate.getForObject(
                "http://FRAUD/api/v1/fraud-check/{customerId}",
                FraudCheckResponse.class,
                customer.getId()
        );

        if (fraudCheckResponse.isFraudster()) {
            throw  new IllegalStateException("fraudster");
        }


    }
}
