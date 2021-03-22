/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss.agrolavka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ss.martin.platform.spring.config.PlatformConfiguration;

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
    private PlatformConfiguration configuration;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(configuration.getNavigation().getLoginPage())
                .addResourceLocations("classpath:/static/martin-platform-client/build/");
        registry.addResourceHandler(configuration.getNavigation().getRegistrationVerification())
                .addResourceLocations("classpath:/static/martin-platform-client/build/");
        registry.addResourceHandler("/assets/**").addResourceLocations("classpath:/static/assets/");
        registry.addResourceHandler("/favicon.svg").addResourceLocations("classpath:/static/favicon.svg");
        registry.addResourceHandler("/sitemap.xml").addResourceLocations("classpath:/static/sitemap.xml");
        registry.addResourceHandler("/robots.txt").addResourceLocations("classpath:/static/robots.txt");
//        registry.addResourceHandler("/.well-known/pki-validation/**")
//                .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/martin-platform-client/build/");     
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
                final String forward = "forward:/index.html";
                registry.addViewController("/admin").setViewName(forward);
                registry.addViewController(configuration.getNavigation().getLoginPage()).setViewName(forward);
                registry.addViewController(configuration.getNavigation().getRegistrationVerification() + "/**")
                        .setViewName(forward);
                registry.addViewController(configuration.getNavigation().getViews() + "/**").setViewName(forward);
            }
        };
    }
}
