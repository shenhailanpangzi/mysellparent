package com.lanpang.apigateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_DECORATION_FILTER_ORDER;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * @program: api-gateway
 * @description: 验证token的过滤器
 * @author: yanghao
 * @create: 2018-10-08 11:22
 **/
@Component
public class TokenFilter extends ZuulFilter {

    @Override
    public String filterType() {
        //配置过滤器类型   前置拦截器
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        //过滤器加载顺序 放在这个过滤器之前
        return FilterConstants.PRE_DECORATION_FILTER_ORDER-1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        //获取上下文
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        //从url参数中获取，也可以从cookie和session中获取
        String token = request.getParameter("token");
        if (StringUtils.isEmpty(token)){
            //设置未通过zuul校验
//            currentContext.setSendZuulResponse(false);
//            currentContext.setResponseStatusCode(HttpStatus.SC_UNAUTHORIZED);
        }
        return null;
    }
}
