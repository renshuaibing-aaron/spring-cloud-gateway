package org.springframework.cloud.gateway.route;

import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.springframework.util.StringUtils.tokenizeToStringArray;

/**
 * 路由定义
 *
 * @author Spencer Gibb
 */
@Validated
public class RouteDefinition {

	@NotEmpty
	private String id = UUID.randomUUID().toString();
    /**
     * 谓语定义数组
     */
	@NotEmpty
	@Valid
	private List<PredicateDefinition> predicates = new ArrayList<>();
    /**
     * 过滤器定义数组
     */
	@Valid
	private List<FilterDefinition> filters = new ArrayList<>();
    /**
     * 路由向的 URI
     */
	@NotNull
	private URI uri;
    /**
     * 顺序 顺序。当请求匹配到多个路由时，使用顺序小的。
     */
	private int order = 0;

	public RouteDefinition() {}

    /**
     * 根据 text 创建 RouteDefinition
     *
     * @param text 格式 ${id}=${uri},${predicates[0]},${predicates[1]}...${predicates[n]}
     *             例如 route001=http://127.0.0.1,Host=**.addrequestparameter.org,Path=/get
     */
	public RouteDefinition(String text) {
		int eqIdx = text.indexOf("=");
		if (eqIdx <= 0) {
			throw new ValidationException("Unable to parse RouteDefinition text '" + text + "'" +
					", must be of the form name=value");
		}
        // id
		setId(text.substring(0, eqIdx));
        // predicates
		String[] args = tokenizeToStringArray(text.substring(eqIdx+1), ",");
        // uri
		setUri(URI.create(args[0]));

		for (int i=1; i < args.length; i++) {
			this.predicates.add(new PredicateDefinition(args[i]));
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<PredicateDefinition> getPredicates() {
		return predicates;
	}

	public void setPredicates(List<PredicateDefinition> predicates) {
		this.predicates = predicates;
	}

	public List<FilterDefinition> getFilters() {
		return filters;
	}

	public void setFilters(List<FilterDefinition> filters) {
		this.filters = filters;
	}

	public URI getUri() {
		return uri;
	}

	public void setUri(URI uri) {
		this.uri = uri;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		RouteDefinition routeDefinition = (RouteDefinition) o;
		return Objects.equals(id, routeDefinition.id) &&
				Objects.equals(predicates, routeDefinition.predicates) &&
				Objects.equals(order, routeDefinition.order) &&
				Objects.equals(uri, routeDefinition.uri);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, predicates, uri);
	}

	@Override
	public String toString() {
		return "RouteDefinition{" +
				"id='" + id + '\'' +
				", predicates=" + predicates +
				", filters=" + filters +
				", uri=" + uri +
				", order=" + order +
				'}';
	}

    public static void main(String[] args) {
        new RouteDefinition("route001=http://127.0.0.1,Host=**.addrequestparameter.org,Path=/get");
    }
}
