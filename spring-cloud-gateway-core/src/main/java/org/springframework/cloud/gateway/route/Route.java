package org.springframework.cloud.gateway.route;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.core.Ordered;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;
import java.util.*;
import java.util.function.Predicate;

/**
 * 路由 gateway 中最基本的组件之一，表示一个具体的路由信息载体
 *
 * @author Spencer Gibb
 */
public class Route implements Ordered {

    /**
     * 路由编号 标识符，区别于其他 Route
     */
	private final String id;
    /**
     * 路由向的 URI 路由指向的目的地 uri，即客户端请求最终被转发的目的地
     */
	private final URI uri;
    /**
     * 顺序 用于多个 Route 之间的排序，数值越小排序越靠前，匹配优先级越高
     */
	private final int order;
    /**
     * 谓语数组 谓语，表示匹配该 Route 的前置条件，即满足相应的条件才会被路由到目的地 uri
     */
	private final Predicate<ServerWebExchange> predicate;
    /**
     * 过滤器数组 过滤器用于处理切面逻辑，如路由转发前修改请求头等
     */
	private final List<GatewayFilter> gatewayFilters;

	public static Builder builder() {
		return new Builder();
	}

	//predicate / gatewayFilters 属性，需要调用 Builder 相关方法进行设置
	public static Builder builder(RouteDefinition routeDefinition) {
		return new Builder()
				.id(routeDefinition.getId())
				.uri(routeDefinition.getUri())
				.order(routeDefinition.getOrder());
	}

	public Route(String id, URI uri, int order, Predicate<ServerWebExchange> predicate, List<GatewayFilter> gatewayFilters) {
		this.id = id;
		this.uri = uri;
		this.order = order;
		this.predicate = predicate;
		this.gatewayFilters = gatewayFilters;
	}

	public static class Builder {
		private String id;

		private URI uri;

		private int order = 0;

		private Predicate<ServerWebExchange> predicate;

		private List<GatewayFilter> gatewayFilters = new ArrayList<>();

		private Builder() {}

		public Builder id(String id) {
			this.id = id;
			return this;
		}

		public Builder uri(String uri) {
			this.uri = URI.create(uri);
			return this;
		}

		public Builder order(int order) {
			this.order = order;
			return this;
		}

		public Builder uri(URI uri) {
			this.uri = uri;
			return this;
		}

		public Builder predicate(Predicate<ServerWebExchange> predicate) {
			this.predicate = predicate;
			return this;
		}

		public Builder gatewayFilters(List<GatewayFilter> gatewayFilters) {
			this.gatewayFilters = gatewayFilters;
			return this;
		}

		public Builder add(GatewayFilter webFilter) {
			this.gatewayFilters.add(webFilter);
			return this;
		}

		public Builder addAll(Collection<GatewayFilter> gatewayFilters) {
			this.gatewayFilters.addAll(gatewayFilters);
			return this;
		}

		public Route build() {
			Assert.notNull(this.id, "id can not be null");
			Assert.notNull(this.uri, "uri can not be null");
			//TODO: Assert.notNull(this.predicate, "predicate can not be null");

			return new Route(this.id, this.uri, this.order, this.predicate, this.gatewayFilters);
		}
	}

	public String getId() {
		return this.id;
	}

	public URI getUri() {
		return this.uri;
	}

	public int getOrder() {
		return order;
	}

	public Predicate<ServerWebExchange> getPredicate() {
		return this.predicate;
	}

	public List<GatewayFilter> getFilters() {
		return Collections.unmodifiableList(this.gatewayFilters);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Route route = (Route) o;
		return Objects.equals(id, route.id) &&
				Objects.equals(uri, route.uri) &&
				Objects.equals(order, route.order) &&
				Objects.equals(predicate, route.predicate) &&
				Objects.equals(gatewayFilters, route.gatewayFilters);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, uri, predicate, gatewayFilters);
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("Route{");
		sb.append("id='").append(id).append('\'');
		sb.append(", uri=").append(uri);
		sb.append(", order=").append(order);
		sb.append(", predicate=").append(predicate);
		sb.append(", gatewayFilters=").append(gatewayFilters);
		sb.append('}');
		return sb.toString();
	}
}
