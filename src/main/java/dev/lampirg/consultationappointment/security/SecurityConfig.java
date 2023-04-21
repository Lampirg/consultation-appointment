package dev.lampirg.consultationappointment.security;

import dev.lampirg.consultationappointment.data.student.StudentRepository;
import dev.lampirg.consultationappointment.data.teacher.TeacherRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    // TODO: split methods for students and teachers into different classes

    @Bean
    public PasswordEncoder encoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public UserDetailsService studentDetailService(StudentRepository studentRepository) {
        return new PersonDetailService<>(studentRepository);
    }

    @Bean
    public UserDetailsService teacherDetailService(TeacherRepository teacherRepository) {
        return new PersonDetailService<>(teacherRepository);
    }

    @Bean
    public AuthenticationProvider studentAuthenticationProvider(UserDetailsService studentDetailService) throws Exception {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(studentDetailService);
        authenticationProvider.setPasswordEncoder(encoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationProvider teacherAuthenticationProvider(UserDetailsService teacherDetailService) throws Exception {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(teacherDetailService);
        authenticationProvider.setPasswordEncoder(encoder());
        return authenticationProvider;
    }

    @Bean
    public SecurityFilterChain studentFilterChain(HttpSecurity http, AuthenticationProvider studentAuthenticationProvider) throws Exception {
        // @formatter:off
        http
                .authenticationProvider(studentAuthenticationProvider)
                .authorizeHttpRequests()
                    .anyRequest().hasRole("'USER'")
                    .and()
                .formLogin()
                    .loginPage("/student/login")
                    .failureUrl("/student/login?hasError=true")
                    .usernameParameter("email")
                    .loginProcessingUrl("/student/login")
                    .permitAll()
                    .and()
                .logout()
                    .logoutUrl("/student/logout")
                    .logoutSuccessUrl("/student/login")
                    .and()
                .httpBasic();
        // @formatter:on
        return http.build();
    }

//    @Bean
//    public SecurityFilterChain teacherFilterChain(HttpSecurity http, AuthenticationProvider teacherAuthenticationProvider) throws Exception {
//        http
//                .authenticationProvider(teacherAuthenticationProvider)
//                .authorizeHttpRequests()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .usernameParameter("email")
//                .loginPage("/teacher/login")
//                .loginProcessingUrl("/teacher/login")
//                .permitAll()
//                .and()
//                .logout()
//                .logoutUrl("/teacher/logout")
//                .and().httpBasic();
//        return http.build();
//    }


}
