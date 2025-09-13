package co.com.powerup.api.requestclient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.powerup.api.config.JwtAuthenticationFilter;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import java.util.List;

@Configuration
public class RequestClientRouter {

    @Bean
    public RouterFunction<ServerResponse> requestClientRouterFunction(RequestClientHandler handler, JwtAuthenticationFilter filter) {
        RouterFunction<ServerResponse> find = route(GET("/api/v1/request/all"), handler::find)
                .filter(filter.requireRole(List.of("admin","asesor")));
        RouterFunction<ServerResponse> save = route(POST("/api/v1/request"), handler::save)
                .filter(filter.requireRole(List.of("cliente")));
        RouterFunction<ServerResponse> findFilter = route(GET("/api/v1/request"), handler::findByFilter)
                .filter(filter.requireRole(List.of("asesor")));
        RouterFunction<ServerResponse> updateStatus = route(PUT("/api/v1/request/{id}"), handler::updateStatus)
                .filter(filter.requireRole(List.of("asesor")));
        RouterFunction<ServerResponse> totalByStatus = route(GET("/api/v1/request/total"), handler::getTotal)
                .filter(filter.requireRole(List.of("admin")));
        return find.and(save).and(findFilter).and(updateStatus).and(totalByStatus);
    }
}

