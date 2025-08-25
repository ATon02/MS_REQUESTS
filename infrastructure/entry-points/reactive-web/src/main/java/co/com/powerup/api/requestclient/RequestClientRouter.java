package co.com.powerup.api.requestclient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RequestClientRouter {
    @Bean
    public RouterFunction<ServerResponse> requestClientRouterFunction(RequestClientHandler handler) {
        return route(GET("/api/v1/request"), handler::find)
                .andRoute(POST("/api/v1/request"), handler::save);
    }
}
