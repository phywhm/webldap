package phy.test.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.LdapUserDetailsService;

@Configuration
@EnableWebSecurity
public class MultiHttpSecurityConfig {
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        DefaultSpringSecurityContextSource contextSource = new DefaultSpringSecurityContextSource("ldap://192.168.109.128:389/dc=haima,dc=com");
        contextSource.setUserDn("cn=root,dc=haima,dc=com");
        contextSource.setPassword("1234");
        contextSource.afterPropertiesSet();

        BindAuthenticator authenticator = new BindAuthenticator(contextSource);
        authenticator.setUserDnPatterns(new String[] { "uid={0},ou=研发中心","uid={0},ou=行政部","uid={0},ou=财务部","uid={0},ou=总经办" });

        DefaultLdapAuthoritiesPopulator populator = new DefaultLdapAuthoritiesPopulator(
                contextSource, "ou=角色");
        populator.setGroupRoleAttribute("cn");
        populator.setGroupSearchFilter("uniqueMember={0}");

        AuthenticationProvider authProvider = new LdapAuthenticationProvider(
                authenticator, populator);
        auth.authenticationProvider(authProvider);

        FilterBasedLdapUserSearch userSearch = new FilterBasedLdapUserSearch("ou=研发中心","(uid={0})",contextSource);
        LdapUserDetailsService userDetailsService = new LdapUserDetailsService(userSearch, populator);
//        //auth.authenticationProvider(authProvider);
//        //Will use DaoAuthenticationProvider.
//        auth.userDetailsService(userDetailsService);
    }
    
    @Configuration
    @Order(1)
    public static class IndexSecurityConfig extends WebSecurityConfigurerAdapter {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/index.jsp").anonymous();
        }
    }
    
    @Configuration
    @Order(2)
    public static class HtmlSecurityConfig extends WebSecurityConfigurerAdapter {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/html/**")
                .authorizeRequests()
                    .antMatchers("/html/submit.jsp").hasAnyRole("测试工程师", "研发工程师")
                    .antMatchers("/html/forbidden.html").authenticated()
                .and().formLogin()
                    .loginPage("/html/login.jsp")
                    .loginProcessingUrl("/html/login")
                    .defaultSuccessUrl("/index.jsp")
                    .failureForwardUrl("/html/login.jsp?login_error=true")
                    .permitAll()
                .and().logout().logoutUrl("/html/logout")
                    .logoutSuccessUrl("/index.jsp")
                .and().exceptionHandling().accessDeniedPage("/html/403.jsp")
                .and().rememberMe().tokenValiditySeconds(14*24*60*60);
        }

        @Override
        public void configure(WebSecurity web) {
//            web.ignoring().antMatchers("/html/forbidden.html");
        }
    }
    
    @Configuration
    @Order(3)
    public static class AjaxSecurityConfig extends WebSecurityConfigurerAdapter {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            http
               .antMatcher("/ajax/**")
               .authorizeRequests().anyRequest().hasRole("RED")
               .and()
               .httpBasic();
        }
    }
}