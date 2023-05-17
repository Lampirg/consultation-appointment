package dev.lampirg.consultationappointment.security;

import dev.lampirg.consultationappointment.data.student.StudentRepository;
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
public class StudentSecurityConfig {

    private PasswordEncoder passwordEncoder;

    public StudentSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public UserDetailsService studentDetailService(StudentRepository studentRepository) {
        return new PersonDetailService<>(studentRepository);
    }


    @Bean
    public AuthenticationProvider studentAuthenticationProvider(UserDetailsService studentDetailService) throws Exception {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(studentDetailService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }


    @Bean
    public SecurityFilterChain studentFilterChain(HttpSecurity http, AuthenticationProvider studentAuthenticationProvider) throws Exception {
        // @formatter:off
        http
                .securityMatcher("/student/**")
                .authenticationProvider(studentAuthenticationProvider)
                .authorizeHttpRequests()
                    .requestMatchers("/student/**").hasRole("STUDENT")
                    .and()
                .formLogin()
                    .loginPage("/student/login")
                    .failureUrl("/student/login?hasError=true")
                    .usernameParameter("email")
                    .loginProcessingUrl("/student/login")
                    .defaultSuccessUrl("/student/profile")
                    .permitAll()
                    .and()
                .logout()
                    .logoutUrl("/student/logout")
                    .logoutSuccessUrl("/home")
                    .and()
                .httpBasic();
        // @formatter:on
        return http.build();
    }


}
