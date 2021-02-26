package com.changgou.order.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 *
 * @author ljh
 * @version 1.0
 * @date 2020/11/22 15:17
 * @description 标题
 * @package com.changgou.order.config
 */
@Component
public class MyFeignInterceptor  implements RequestInterceptor {
    //发送请求 调用微服务之前进行执行
    @Override
    public void apply(RequestTemplate template) {
        //1.获取当前线程请求对象ThreadLocal
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(requestAttributes!=null) {
            HttpServletRequest request = requestAttributes.getRequest();
            //2.获取请求对象中的头名为Authorization的信息 为了扩展性 可以将所有的头 往下游进行传递
            Enumeration<String> headerNames = request.getHeaderNames();
            while(headerNames.hasMoreElements()){
                String headerName = headerNames.nextElement();
                String headerValue = request.getHeader(headerName);
                //3.传递给下游的被调用方微服务 作为头信息 Authorization bearer -->
                template.header(headerName,headerValue);
            }
        }
    }
}
