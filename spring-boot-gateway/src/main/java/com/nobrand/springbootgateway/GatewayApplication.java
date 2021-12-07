package com.nobrand.springbootgateway;

import com.nobrand.springbootgateway.config.UriConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder, UriConfiguration configuration) {
		String httpUri = configuration.getHttpBin();
		return builder.routes()
				.route(p -> p.path("/get")
							 .filters(f -> f.addRequestHeader("Hello", "World"))
						     .uri(httpUri))
				.route(p -> p.host("*.circuitbreaker.com")
							 .filters(f -> f.circuitBreaker(config ->
									 config.setName("mycmd")
									 	   .setFallbackUri("forward:/fallback")))
				             .uri(httpUri))
				.build();
	}

}
