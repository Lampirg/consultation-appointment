package dev.lampirg.consultationappointment.data.teacher

import dev.lampirg.consultationappointment.data.appointment.Appointment
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.OneToMany
import jakarta.validation.constraints.NotNull
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter
import lombok.ToString
import org.springframework.data.jpa.domain.AbstractPersistable
import java.time.Duration
import java.time.LocalDateTime

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
class DatePeriod(
    var classroom: String,
    var startTime: LocalDateTime,
    var endTime: LocalDateTime,
    var unoccupiedTime: Duration = Duration.between(startTime, endTime),
    @OneToMany(orphanRemoval = true, mappedBy = "appointmentPeriod", fetch = FetchType.EAGER)
    var appointments: Set<Appointment> = HashSet()
) : AbstractPersistable<Long>()