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
class PatternSchedule(
    val consultationPattern: ConsultationPattern
) {
    @Id
    @GeneratedValue
    var id: Long? = null
}