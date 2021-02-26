package com.changgou.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author pengmin
 * @date 2020/11/20 17:42
 */
@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {

    private static final String AUTHORIZE_TOKEN = "Authorization";

    /**
     * 全局过滤器进行登陆验证; 拦截所有经过网关的请求;
     *
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        if (request.getURI().getPath().startsWith("/api/user/login")) {
            //请求为登陆,放行;
            return chain.filter(exchange);
        }
        //String token = request.getHeaders().getFirst(AUTHORIZE_TOKEN);
       /* if (StringUtils.isEmpty(token)){
            token = request.getQueryParams().getFirst(AUTHORIZE_TOKEN);
        }*/
        //if (StringUtils.isEmpty(token)){
        String token = null;
        HttpCookie cookie = request.getCookies().getFirst(AUTHORIZE_TOKEN);
        if (cookie != null) {
            token = cookie.getValue();
        }
        //}
        if (StringUtils.isEmpty(token)) {
            // 没有权限,让其重定向到登陆页面; 并将原来的请求路径设置到参数url中,用于后续登陆成功后的跳转;
            String url = request.getURI().toString();
            response.getHeaders().set("Location", "http://localhost:9001/oauth/login?url=" + url);
            response.setStatusCode(HttpStatus.SEE_OTHER);
            return response.setComplete();
        }
        /*try {
            JwtUtil.parseJWT(token);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }*/
        // 将令牌设置到请求的头部中,放行至对应的微服务;
        request.mutate().header(AUTHORIZE_TOKEN, "bearer " + token);
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
