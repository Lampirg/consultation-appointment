package dev.lampirg.consultationappointment.security

import dev.lampirg.consultationappointment.data.teacher.TeacherRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer.AuthorizationManagerRequestMatcherRegistry
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
class TeacherSecurityConfig @Autowired constructor(private val passwordEncoder: PasswordEncoder) {
    @Bean
    fun teacherDetailService(teacherRepository: TeacherRepository): UserDetailsService {
        return PersonDetailService(teacherRepository)
    }

    @Bean
    fun teacherAuthenticationProvider(teacherDetailService: UserDetailsService): AuthenticationProvider {
        val authenticationProvider = DaoAuthenticationProvider()
        authenticationProvider.setUserDetailsService(teacherDetailService)
        authenticationProvider.setPasswordEncoder(passwordEncoder)
        return authenticationProvider
    }

    @Bean
    fun teacherFilterChain(
        http: HttpSecurity,
        teacherAuthenticationProvider: AuthenticationProvider
    ): SecurityFilterChain {
        http
            .securityMatcher("/teacher/**")
            .authenticationProvider(teacherAuthenticationProvider)
            .authorizeHttpRequests { authorizeRequests ->
                authorizeRequests.requestMatchers(
                    "/teacher/**"
                ).hasRole("TEACHER")
            }
            .formLogin { formLogin ->
                formLogin
                    .loginPage("/teacher/login")
                    .failureUrl("/teacher/login?hasError=true")
                    .usernameParameter("email")
                    .loginProcessingUrl("/teacher/login")
                    .defaultSuccessUrl("/teacher/profile")
                    .permitAll()
            }
            .logout { logout ->
                logout
                    .logoutUrl("/teacher/logout")
                    .logoutSuccessUrl("/home")
            }
            .httpBasic {}
        return http.build()
    }
}