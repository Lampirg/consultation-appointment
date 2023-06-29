package dev.lampirg.consultationappointment.data.teacher

import dev.lampirg.consultationappointment.data.model.PersonRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface TeacherRepository : PersonRepository<Teacher> {
    @Query("select t from DatePeriod t where t.id = ?1")
    fun findDatePeriodById(datePeriodId: Long): Optional<DatePeriod>
}