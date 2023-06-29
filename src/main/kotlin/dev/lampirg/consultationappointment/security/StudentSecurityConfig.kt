package dev.lampirg.consultationappointment.security

import dev.lampirg.consultationappointment.data.student.StudentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
class StudentSecurityConfig @Autowired constructor(private val passwordEncoder: PasswordEncoder) {
    @Bean
    fun studentDetailService(studentRepository: StudentRepository): UserDetailsService {
        return PersonDetailService(studentRepository)
    }

    @Bean
    fun studentAuthenticationProvider(studentDetailService: UserDetailsService): AuthenticationProvider {
        val authenticationProvider = DaoAuthenticationProvider()
        authenticationProvider.setUserDetailsService(studentDetailService)
        authenticationProvider.setPasswordEncoder(passwordEncoder)
        return authenticationProvider
    }

    @Bean
    fun studentFilterChain(
        http: HttpSecurity,
        studentAuthenticationProvider: AuthenticationProvider
    ): SecurityFilterChain {
        http
            .securityMatcher("/student/**")
            .authenticationProvider(studentAuthenticationProvider)
            .authorizeHttpRequests {
                it
                    .requestMatchers("/student/**").hasRole("STUDENT")
            }
            .formLogin {
                it
                    .loginPage("/student/login")
                    .failureUrl("/student/login?hasError=true")
                    .usernameParameter("email")
                    .loginProcessingUrl("/student/login")
                    .defaultSuccessUrl("/student/profile")
                    .permitAll()
            }
            .logout {
                it
                    .logoutUrl("/student/logout")
                    .logoutSuccessUrl("/home")
            }
            .httpBasic {}
        return http.build()
    }
}