package dev.lampirg.consultationappointment.data.schedule

import dev.lampirg.consultationappointment.web.fetch.ConsultationPattern
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter

@Entity
@Getter
@Setter
@NoArgsConstructor
open class PatternSchedule(
    open val consultationPattern: ConsultationPattern
) {
    @Id
    @GeneratedValue
    open var id: Long? = null
}