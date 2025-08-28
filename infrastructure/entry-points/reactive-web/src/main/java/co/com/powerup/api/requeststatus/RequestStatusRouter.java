package co.com.powerup.api.requeststatus;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.powerup.api.config.JwtAuthenticationFilter;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import java.util.List;

@Configuration
public class RequestStatusRouter {
    @Bean
    public RouterFunction<ServerResponse> requestStatusRouterFunction(RequestStatusHandler handler, JwtAuthenticationFilter filter) {
        RouterFunction<ServerResponse> find = route(GET("/api/v1/status-request"), handler::find)
                .filter(filter.requireRole(List.of("admin")));
        RouterFunction<ServerResponse> save = route(POST("/api/v1/status-request"), handler::save)
                .filter(filter.requireRole(List.of("admin")));
        return find.and(save);
    }
}
