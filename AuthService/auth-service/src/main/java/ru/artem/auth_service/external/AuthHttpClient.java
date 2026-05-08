package ru.artem.auth_service.external;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import ru.artem.auth_service.dto.request.BusinessUserRequest;
import ru.artem.auth_service.dto.response.UserResponse;

@HttpExchange(accept = "application/json", contentType = "application/json", url = "/user")
public interface AuthHttpClient {

    @PostExchange
    UserResponse createUser(@RequestBody BusinessUserRequest request);

}