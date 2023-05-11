/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka;

import java.util.concurrent.TimeUnit;
import org.apache.catalina.Context;
import org.apache.tomcat.util.scan.StandardJarScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;
import ss.martin.security.configuration.external.NavigationConfiguration;

/**
 * Spring configuration.
 * @author alex
 */
@Configuration
@EnableScheduling
@ComponentScan({"ss.agrolavka", "ss.martin"})
public class SpringConfig implements WebMvcConfigurer {
    /** Platform configuration. */
    @Autowired
    private NavigationConfiguration configuration;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(configuration.loginPage())
                .addResourceLocations("classpath:/static/admin/build/");
        registry.addResourceHandler(configuration.registrationVerification())
                .addResourceLocations("classpath:/static/admin/build/");
        registry.addResourceHandler("/assets/**").addResourceLocations("classpath:/static/assets/")
                .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS));
        if (configuration.getImagesStoragePath() != null) {
            registry.addResourceHandler("/media/**").setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS))
                    .addResourceLocations("file:" + configuration.getImagesStoragePath() + "/").resourceChain(true)
                    .addResolver(new PathResourceResolver());
        }
        registry.addResourceHandler("/favicon.svg").addResourceLocations("classpath:/static/favicon.svg");
        registry.addResourceHandler("/sitemap.xml").addResourceLocations("classpath:/static/sitemap.xml");
        registry.addResourceHandler("/robots.txt").addResourceLocations("classpath:/static/robots.txt");
        registry.addResourceHandler("/firebase-messaging-sw.js").addResourceLocations("classpath:/static/assets/js/firebase-messaging-sw.js");
        registry.addResourceHandler("/.well-known/acme-challenge/**")
                .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/admin/**").addResourceLocations("classpath:/static/admin/build/");
    }
    /**
     * Forward some URLs to index page.
     * Required for React app routing navigation.
     * @return configuration.
     */
    @Bean
    public WebMvcConfigurer forwardToIndex() {
        return new WebMvcConfigurer() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                final String forward = "forward:/admin/index.html";
                registry.addViewController("/admin").setViewName(forward);
                registry.addViewController(configuration.loginPage()).setViewName(forward);
                registry.addViewController(configuration.registrationVerification() + "/**")
                        .setViewName(forward);
                registry.addViewController(configuration.views() + "/**").setViewName(forward);
            }
        };
    }
    
    @Bean
    public TomcatServletWebServerFactory tomcatFactory() {
        return new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                ((StandardJarScanner) context.getJarScanner()).setScanManifest(false);
        }};
    }
    /**
     * Rest template bean.
     * @return bean.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
