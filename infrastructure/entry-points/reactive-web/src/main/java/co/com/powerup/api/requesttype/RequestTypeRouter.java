package co.com.powerup.api.requesttype;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RequestTypeRouter {
    @Bean
    public RouterFunction<ServerResponse> requestTypeRouterFunction(RequestTypeHandler handler) {
        return route(GET("/api/v1/request-type"), handler::find)
                .andRoute(POST("/api/v1/request-type"), handler::save);
    }
}
