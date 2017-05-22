package phy.test.web;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by phy on 2017/4/26.
 */
@Configuration
@ComponentScan(basePackages = {"phy.test"})
//        excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value = EnableWebMvc.class)})
public class RootConfig {
}
