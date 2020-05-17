package org.springframework.cloud.gateway.filter.factory;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.tuple.Tuple;

import java.util.Arrays;
import java.util.List;

/**
 * User Request Rate Limiter filter. See https://stripe.com/blog/rate-limiters and
 */
public class RequestRateLimiterGatewayFilterFactory implements GatewayFilterFactory {

	public static final String KEY_RESOLVER_KEY = "keyResolver";

	private final RateLimiter rateLimiter;
	private final KeyResolver defaultKeyResolver;

	public RequestRateLimiterGatewayFilterFactory(RateLimiter rateLimiter,
			KeyResolver defaultKeyResolver) {
		this.rateLimiter = rateLimiter;
		this.defaultKeyResolver = defaultKeyResolver;
	}

    @Override
    public List<String> argNames() {
        return Arrays.asList(
                RedisRateLimiter.REPLENISH_RATE_KEY,
                RedisRateLimiter.BURST_CAPACITY_KEY,
                KEY_RESOLVER_KEY
        );
    }

    @Override
    public boolean validateArgs() {
        return false;
    }

    @SuppressWarnings("unchecked")
	@Override
	public GatewayFilter apply(Tuple args) {
        validateMin(2, args);

        // 获得 KeyResolver
		KeyResolver keyResolver;
		if (args.hasFieldName(KEY_RESOLVER_KEY)) {
			keyResolver = args.getValue(KEY_RESOLVER_KEY, KeyResolver.class);
		} else {
			keyResolver = defaultKeyResolver;
		}

		return (exchange, chain) -> keyResolver.resolve(exchange).flatMap(key ->
            // TODO: if key is empty?
            rateLimiter.isAllowed(key, args).flatMap(response -> {
                // TODO: set some headers for rate, tokens left

                // 允许访问
                if (response.isAllowed()) {
                    return chain.filter(exchange);
                }

                // 被限流，不允许访问
                exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                return exchange.getResponse().setComplete();
            }));
	}

}
