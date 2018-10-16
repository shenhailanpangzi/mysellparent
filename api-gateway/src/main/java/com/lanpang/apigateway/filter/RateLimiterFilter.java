package com.lanpang.apigateway.filter;

import com.google.common.util.concurrent.RateLimiter;
import com.lanpang.apigateway.exception.RateLimitException;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.constants.ZuulConstants;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

/**
 * @program: api-gateway
 * @description: 限流拦截器
 * @author: yanghao
 * @create: 2018-10-08 14:55
 **/
public class RateLimiterFilter extends ZuulFilter {

    //谷歌令牌桶限流算法
    private static final RateLimiter RATE_LIMITER = RateLimiter.create(100);//每秒钟要向里面放多少个令牌

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.SERVLET_DETECTION_FILTER_ORDER-1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        //如果未取到令牌
        if (!RATE_LIMITER.tryAcquire()){
            throw new RateLimitException();
        }
        return null;
    }
}
