package org.springframework.cloud.gateway.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.filter.LoadBalancerClientFilter;
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.DispatcherHandler;

/**
 * todo LoadBalancerClientFilter 初始化
 *
 * @author Spencer Gibb
 */
@Configuration
@ConditionalOnClass({LoadBalancerClient.class, RibbonAutoConfiguration.class, DispatcherHandler.class})
@AutoConfigureAfter(RibbonAutoConfiguration.class)
public class GatewayLoadBalancerClientAutoConfiguration {

	// GlobalFilter beans

	@Bean
	@ConditionalOnBean(LoadBalancerClient.class)
	public LoadBalancerClientFilter loadBalancerClientFilter(LoadBalancerClient client) {

		System.out.println("【LoadBalancerClientFilter 初始化】");
		return new LoadBalancerClientFilter(client);
	}

}
