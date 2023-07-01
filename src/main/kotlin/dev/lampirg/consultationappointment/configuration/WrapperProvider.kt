package dev.lampirg.consultationappointment.configuration

import dev.lampirg.consultationappointment.web.fetch.ConsultationPatternListWrapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode

@Configuration
class WrapperProvider {
    @Bean
    @Scope(scopeName = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
    fun wrapper(): ConsultationPatternListWrapper = ConsultationPatternListWrapper()
}