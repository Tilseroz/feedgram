package cz.tilseroz.feedgramapigateway.config;

import cz.tilseroz.feedgramapigateway.enums.RoutesEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Autowired
    AuthenticationFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(RoutesEnum.FEED_SERVICE.getServiceId(), r -> r.path(RoutesEnum.FEED_SERVICE.getPathPattern())
                        .filters(f -> f.filter(filter))
                        .uri(RoutesEnum.FEED_SERVICE.getUri()))

                .route(RoutesEnum.AUTH_SERVICE.getServiceId(), r -> r.path(RoutesEnum.AUTH_SERVICE.getPathPattern())
                        .filters(f -> f.filter(filter))
                        .uri(RoutesEnum.AUTH_SERVICE.getUri()))

                .route(RoutesEnum.GRAPH_SERVICE.getServiceId(), r -> r.path(RoutesEnum.GRAPH_SERVICE.getPathPattern())
                        .filters(f -> f.filter(filter))
                        .uri(RoutesEnum.GRAPH_SERVICE.getUri()))

                .route(RoutesEnum.POST_SERVICE.getServiceId(), r -> r.path(RoutesEnum.POST_SERVICE.getPathPattern())
                        .filters(f -> f.filter(filter))
                        .uri(RoutesEnum.POST_SERVICE.getUri()))

                .build();
    }
}
