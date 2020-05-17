package org.springframework.cloud.gateway.handler.predicate;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.tuple.Tuple;
import org.springframework.web.server.ServerWebExchange;

/**
 * @author Spencer Gibb
 */
//声明了范型，即使用到的配置类为 AfterRoutePredicateFactory 中定义的内部类 Config
public class AfterRoutePredicateFactory implements RoutePredicateFactory {

	public static final String DATETIME_KEY = "datetime";

	@Override
	public List<String> argNames() {
		return Collections.singletonList(DATETIME_KEY);
	}

	@Override
	public Predicate<ServerWebExchange> apply(Tuple args) {
		//生产 Predicate 对象，逻辑是判断当前时间（执行时）是否在 Config 中指定的 datetime之后

		//todo 关键疑问是PredicateDefinition 对象又是如何转换成 AfterRoutePredicateFactory.args
		Object value = args.getValue(DATETIME_KEY);
		final ZonedDateTime dateTime = BetweenRoutePredicateFactory.getZonedDateTime(value);

		return exchange -> {
			final ZonedDateTime now = ZonedDateTime.now();
			return now.isAfter(dateTime);
		};
	}

}
