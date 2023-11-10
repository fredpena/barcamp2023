package dev.fredpena.barcamp.security;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import dev.fredpena.barcamp.views.login.LoginView;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
@Order(2)
public class SecurityConfiguration extends VaadinWebSecurity {

//    https://www.toptal.com/spring/spring-security-tutorial
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(
                authorize -> authorize.requestMatchers(new AntPathRequestMatcher("/images/*.png")).permitAll());


        // Icons from the line-awesome addon
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(new AntPathRequestMatcher("/line-awesome/**/*.svg")).permitAll());

        super.configure(http);
        setLoginView(http, LoginView.class);
    }




    @Override
    public void configure(WebSecurity web) throws Exception {
        // Customize your WebSecurity configuration.
        super.configure(web);
    }

}
