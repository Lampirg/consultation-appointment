package dev.lampirg.consultationappointment.data.model

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface PersonRepository<T : Person?> : JpaRepository<T, Int?> {
    fun findByEmail(email: String?): T
}