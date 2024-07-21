package com.luv2code.springboot.cruddemo.security;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class DemoSecurityConfig {

        // add support to JDBC
        @Bean
        public UserDetailsManager userDetailsManager(DataSource dataSource) {
                JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
                jdbcUserDetailsManager.setUsersByUsernameQuery(
                                "select user_id, pw, active from members where user_id=?");
                jdbcUserDetailsManager
                                .setAuthoritiesByUsernameQuery("select user_id, role from roles where user_id=?");
                return new JdbcUserDetailsManager(dataSource);
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

                httpSecurity.authorizeHttpRequests(
                                configure -> configure
                                                .requestMatchers(HttpMethod.GET, "/api/employees").hasRole("EMPLOYEE")
                                                .requestMatchers(HttpMethod.GET, "/api/employees/**")
                                                .hasRole("EMPLOYEE")
                                                .requestMatchers(HttpMethod.POST, "/api/employees").hasRole("MANAGER")
                                                .requestMatchers(HttpMethod.PUT, "/api/employees").hasRole("MANAGER")
                                                .requestMatchers(HttpMethod.DELETE, "/api/employees/**")
                                                .hasRole("ADMIN"));

                httpSecurity.httpBasic(Customizer.withDefaults());

                httpSecurity.csrf(csrf -> csrf.disable());

                return httpSecurity.build();

        }

        /*
         * @Bean
         * public InMemoryUserDetailsManager getInMemoryUserDetailsManager() {
         * 
         * UserDetails john = User
         * .builder()
         * .username("john")
         * .password("{noop}test123")
         * .roles("EMPLOYEE")
         * .build();
         * 
         * UserDetails mary = User
         * .builder()
         * .username("mary")
         * .password("{noop}test123")
         * .roles("EMPLOYEE", "MANAGER")
         * .build();
         * 
         * UserDetails susan = User
         * .builder()
         * .username("susan")
         * .password("{noop}test123")
         * .roles("EMPLOYEE", "MANAGER", "ADMIN")
         * .build();
         * 
         * return new InMemoryUserDetailsManager(john, mary, susan);
         * }
         */
}
