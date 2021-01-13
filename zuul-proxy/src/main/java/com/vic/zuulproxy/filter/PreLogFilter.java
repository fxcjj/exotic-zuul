package com.vic.zuulproxy.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * @author victor
 * date: 2021/1/13 17:12
 */
@Component
public class PreLogFilter extends ZuulFilter {

    /**
     * 过滤器类型，有pre、routing、post、error四种。
     *
     * @return
     */
    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    /**
     * 过滤器执行顺序，数值越小优先级越高。
     *
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 是否进行过滤，返回true会执行过滤。
     *
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 自定义的过滤器逻辑，当shouldFilter()返回true时才会执行。
     *
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        String remoteHost = request.getRemoteHost();
        String method = request.getMethod();
        String requestURI = request.getRequestURI();

        // remoteHost:0:0:0:0:0:0:0:1, method:GET, requestURI:/auction/user-service/test/test1
        System.out.println("remoteHost:" + remoteHost + ", method:" + method + ", requestURI:" + requestURI);
        return null;
    }
}
