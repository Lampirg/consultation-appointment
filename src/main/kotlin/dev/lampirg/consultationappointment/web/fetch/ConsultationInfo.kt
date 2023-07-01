package dev.lampirg.consultationappointment.web.fetch

import jakarta.persistence.Embeddable
import lombok.Data
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import java.time.LocalTime

@Data
@Embeddable
class ConsultationInfo(
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    var date: LocalDate,
    var classroom: String,
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    var startTime: LocalTime,
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    var endTime: LocalTime
) {
    constructor() : this(LocalDate.MIN, "", LocalTime.MIN, LocalTime.MIN)
}