//package com.lanpang.apigateway.filter;
//
//import com.lanpang.apigateway.consant.RedisCons;
//import com.lanpang.apigateway.utils.CookieUtil;
//import com.netflix.zuul.ZuulFilter;
//import com.netflix.zuul.context.RequestContext;
//import com.netflix.zuul.exception.ZuulException;
//import com.sun.javafx.binding.StringFormatter;
//import org.apache.http.HttpStatus;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletRequest;
//
///**
// * @program: api-gateway
// * @description: 权限拦截（区分买家和卖家）过滤器
// * @author: yanghao
// * @create: 2018-10-08 11:22
// **/
//@Component
//public class AuthFilter extends ZuulFilter {
//    @Autowired
//    private StringRedisTemplate redisTemplate;
//    @Override
//    public String filterType() {
//        //配置过滤器类型   前置拦截器
//        return FilterConstants.PRE_TYPE;
//    }
//
//    @Override
//    public int filterOrder() {
//        //过滤器加载顺序 放在这个过滤器之前
//        return FilterConstants.PRE_DECORATION_FILTER_ORDER-1;
//    }
//
//    @Override
//    public boolean shouldFilter() {
//        return true;
//    }
//
//    @Override
//    public Object run() throws ZuulException {
//        //获取上下文
//        RequestContext currentContext = RequestContext.getCurrentContext();
//        HttpServletRequest request = currentContext.getRequest();
//        /**
//         * /order/create 只能买家访问(cookie里有openid)
//         * /order/finish 只能卖家访问(cookie里有token，redis中有值)
//         * /product/list 都可访问
//         */
//        if ("/order/order/create".equals(request.getRequestURI())) {
//            Cookie cookie = CookieUtil.get(request,"openid");
//            if (cookie==null || StringUtils.isEmpty(cookie.getValue())){
//                currentContext.setSendZuulResponse(false);
//                currentContext.setResponseStatusCode(HttpStatus.SC_UNAUTHORIZED);
//            }
//        }
//        if ("/order/order/finish".equals(request.getRequestURI())) {
//            Cookie cookie = CookieUtil.get(request,"token");
//            if (cookie==null
//                    || StringUtils.isEmpty(cookie.getValue())
//                    || StringUtils.isEmpty(redisTemplate.opsForValue().get(String.format(RedisCons.TOKEN_TEMPLATE,cookie.getValue())))){
//                currentContext.setSendZuulResponse(false);
//                currentContext.setResponseStatusCode(HttpStatus.SC_UNAUTHORIZED);
//            }
//        }
//        return null;
//    }
//}
