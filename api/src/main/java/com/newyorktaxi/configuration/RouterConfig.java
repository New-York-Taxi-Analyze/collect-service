package com.newyorktaxi.configuration;

import com.newyorktaxi.controller.ReportController;
import com.newyorktaxi.controller.UserController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterConfig {

    @Bean
    RouterFunction<ServerResponse> userRoutes(UserController handler) {
        return route((POST("/api/v1/createUser")), handler::createUser)
                .andRoute((POST("/api/v1/login")), handler::login);
    }

    @Bean
    RouterFunction<ServerResponse> reportRoutes(ReportController handler) {
        return route((POST("/api/v1/message")), handler::message)
                .andRoute((GET("/api/v1/total")), handler::total)
                .andRoute((GET("/api/v1/hello")), handler::getHello);
    }
}
