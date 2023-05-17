package dev.lampirg.consultationappointment.security;

import dev.lampirg.consultationappointment.data.teacher.TeacherRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class TeacherSecurityConfig {

    private PasswordEncoder passwordEncoder;

    public TeacherSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public UserDetailsService teacherDetailService(TeacherRepository teacherRepository) {
        return new PersonDetailService<>(teacherRepository);
    }


    @Bean
    public AuthenticationProvider teacherAuthenticationProvider(UserDetailsService teacherDetailService) throws Exception {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(teacherDetailService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

    @Bean
    public SecurityFilterChain teacherFilterChain(HttpSecurity http, AuthenticationProvider teacherAuthenticationProvider) throws Exception {
        // @formatter:off
        http
                .securityMatcher("/teacher/**")
                .authenticationProvider(teacherAuthenticationProvider)
                .authorizeHttpRequests()
                    .requestMatchers("/teacher/**").hasRole("TEACHER")
                    .and()
                .formLogin()
                    .loginPage("/teacher/login")
                    .failureUrl("/teacher/login?hasError=true")
                    .usernameParameter("email")
                    .loginProcessingUrl("/teacher/login")
                    .defaultSuccessUrl("/teacher/profile")
                    .permitAll()
                    .and()
                .logout()
                    .logoutUrl("/teacher/logout")
                    .logoutSuccessUrl("/home")
                    .and()
                .httpBasic();
        // @formatter:on
        return http.build();
    }
}
