package com.meme;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Component
public class TomcatContainerCustomizer  implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    @Value("${swagger.port}")
    private int swaggerPort;
    
    @Value("${server.port}")
    private int serverPort;


    @Value("${swagger.paths}")
    private List<String> swaggerPaths;
    
    
    @Override
    public void customize(TomcatServletWebServerFactory factory) {

        Connector swaggerConnector = new Connector();
        swaggerConnector.setPort(swaggerPort);
        factory.addAdditionalTomcatConnectors(swaggerConnector);
    }

    @Bean
    public FilterRegistrationBean<SwaggerFilter> swaggerFilterRegistrationBean() {

        FilterRegistrationBean<SwaggerFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new SwaggerFilter());
        filterRegistrationBean.setOrder(-100);
        filterRegistrationBean.setName("SwaggerFilter");

        return filterRegistrationBean;
    }
    
    private class SwaggerFilter extends OncePerRequestFilter {

        private AntPathMatcher pathMatcher = new AntPathMatcher();

        @Override
        protected void doFilterInternal(final HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        FilterChain filterChain) throws ServletException, IOException {

            boolean isSwaggerPath = swaggerPaths.stream()
                    .anyMatch(new Predicate<String>() {
						@Override
						public boolean test(String path) {
							return pathMatcher.match(path, httpServletRequest.getServletPath());
						}
					});
            boolean isSwaggerPort = httpServletRequest.getLocalPort() == swaggerPort;

            if(isSwaggerPath == isSwaggerPort) {
                filterChain.doFilter(httpServletRequest, httpServletResponse);
            } else {
                httpServletResponse.sendError(404);
            }
        }
    }
    
    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .host("localhost:" + serverPort).apiInfo(apiDetails());
    }
    private ApiInfo apiDetails() {
    	return new ApiInfo("XMeme Api",
    			"API for Xmeme",
    			"1.0",
    			"Free to Use",
    			new springfox.documentation.service.Contact("Ishika Agarwal","https://www.linkedin.com/in/ishika-agarwal-683199175/","ishuagarwal2210@gmail.com"), "Api License", "ishuagarwal@gmail.com", Collections.emptyList());
    }

}
