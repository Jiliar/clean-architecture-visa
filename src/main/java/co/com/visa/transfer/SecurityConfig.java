package co.com.visa.transfer;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import co.com.visa.transfer.helpers.EnvVariables;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        EnvVariables env = new EnvVariables();
            auth
                .inMemoryAuthentication()
                .withUser(env.SEC_USER1)
                    .password("{noop}"+env.getUserPass())
                    .roles(env.SEC_ROL1)
                    .and()
                .withUser(env.SEC_USER2)
                    .password("{noop}"+env.getAdminPass())
                    .roles(env.SEC_ROL2);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }
}
