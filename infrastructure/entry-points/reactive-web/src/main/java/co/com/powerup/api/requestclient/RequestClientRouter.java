package co.com.powerup.api.requestclient;

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
public class RequestClientRouter {

    @Bean
    public RouterFunction<ServerResponse> requestClientRouterFunction(RequestClientHandler handler, JwtAuthenticationFilter filter) {
        RouterFunction<ServerResponse> find = route(GET("/api/v1/request"), handler::find)
                .filter(filter.requireRole(List.of("admin","asesor")));
        RouterFunction<ServerResponse> save = route(POST("/api/v1/request"), handler::save)
                .filter(filter.requireRole(List.of("cliente")));
        return find.and(save);
    }
}

