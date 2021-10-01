package com.appsdeveloperblog.photoapp.api.gateway;

import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.Jwts;
import reactor.core.publisher.Mono;

@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.config> {
   @Autowired
	Environment env;
   
   public AuthorizationHeaderFilter() {
	   super(config.class);
   }
	public static class config {
		// put config properties here

	}

     @Override
	public GatewayFilter apply(config config) {
		return (exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();
			if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
				return onError(exchange, "No Authorization Error", HttpStatus.UNAUTHORIZED);
			}
			String authorizationHeader=request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
			String jwt=authorizationHeader.replace("Bearer", "");
			if(!isJwtValid(jwt)) {
				return onError(exchange, "Jwt token is not valid", HttpStatus.UNAUTHORIZED);
			}
			return chain.filter(exchange);
		};
	}

	private Mono<Void> onError(ServerWebExchange exchange, String string, HttpStatus unauthorized) {
		// TODO Auto-generated method stub
		ServerHttpResponse response = exchange.getResponse();
		return response.setComplete();
	}
	
	private boolean isJwtValid(String jwt) {
		boolean returnValue = true;
		String subject = Jwts.parser()
				.setSigningKey(env.getProperty("token.secret")).parseClaimsJws(jwt).getBody()
				.getSubject();

		if (subject == null || subject.isEmpty()) {
			return false;
		}
		return returnValue;
	}

}
