package com.aaron.ren.sample

import org.springframework.cloud.gateway.filter.factory.GatewayFilters.addResponseHeader
import org.springframework.cloud.gateway.handler.predicate.RoutePredicates.host
import org.springframework.cloud.gateway.handler.predicate.RoutePredicates.path
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.gateway
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AdditionalRoutes {

	@Bean
	fun additionalRouteLocator(): RouteLocator = gateway {
		route(id = "test-kotlin") {
			uri("http://httpbin.org:80") // Route.Builder#uri(uri)
			predicate(host("kotlin.abc.org") and path("/image/png")) // Route.Builder#predicate(predicate)
			add(addResponseHeader("X-TestHeader", "foobar")) // Route.Builder#add(webFilter)
		}
	}

}