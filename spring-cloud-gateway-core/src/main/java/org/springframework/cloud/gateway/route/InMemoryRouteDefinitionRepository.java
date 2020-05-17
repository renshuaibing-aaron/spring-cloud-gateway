
package org.springframework.cloud.gateway.route;

import org.springframework.cloud.gateway.support.NotFoundException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Collections.synchronizedMap;

/**
 * 基于内存为存储器的 RouteDefinitionLocator
 *
 * @author Spencer Gibb
 */
public class InMemoryRouteDefinitionRepository implements RouteDefinitionRepository {

    /**
     * 路由配置映射
     * key ：路由编号 {@link RouteDefinition#id}
     */
	private final Map<String, RouteDefinition> routes = synchronizedMap(new LinkedHashMap<String, RouteDefinition>());

	@Override
	public Mono<Void> save(Mono<RouteDefinition> route) {
        System.out.println(Thread.currentThread() + ":#save()");
        return route.flatMap( r -> {
            System.out.println(Thread.currentThread() + ":#save2()");
            routes.put(r.getId(), r);
			return Mono.empty();
		});
	}

	@Override
	public Mono<Void> delete(Mono<String> routeId) {
		return routeId.flatMap(id -> {
			if (routes.containsKey(id)) {
				routes.remove(id);
				return Mono.empty();
			}
			return Mono.error(new NotFoundException("RouteDefinition not found: "+routeId));
		});
	}

	@Override
	public Flux<RouteDefinition> getRouteDefinitions() {
		return Flux.fromIterable(routes.values());
	}
}
