
package org.springframework.cloud.gateway.route;

import reactor.core.publisher.Flux;

/**
 * Route 定位器接口
 *
 * @author Spencer Gibb
 */
//TODO: rename to Routes?
public interface RouteLocator {

	//定义获得路由数组的方法
	Flux<Route> getRoutes();

}
