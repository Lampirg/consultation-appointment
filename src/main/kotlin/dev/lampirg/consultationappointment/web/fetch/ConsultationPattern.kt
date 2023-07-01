package dev.lampirg.consultationappointment.web.fetch

import dev.lampirg.consultationappointment.data.teacher.Teacher
import jakarta.persistence.Embeddable
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import lombok.Data
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

@Data
@Embeddable
class ConsultationPattern(
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    var until: LocalDate,
    var fromDate: LocalDate,
    var consultationInfo: ConsultationInfo,
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    var teacher: Teacher? = null
) {
    constructor() : this(LocalDate.MIN, LocalDate.MIN, ConsultationInfo())
}