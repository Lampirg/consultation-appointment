package dev.lampirg.consultationappointment.data.teacher;

import dev.lampirg.consultationappointment.data.appointment.Appointment;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class DatePeriod extends AbstractPersistable<Long> {

    @NotEmpty
    @Pattern(regexp = "\\d{1,2}-\\d{3}")
    private String classroom;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;

    @NotNull
    private Duration unoccupiedTime;

    @OneToMany(orphanRemoval = true, mappedBy = "appointmentPeriod", fetch = FetchType.EAGER)
    private Set<Appointment> appointments = new HashSet<>();

    public DatePeriod(String classroom, LocalDateTime startTime, LocalDateTime endTime) {
        this.classroom = classroom;
        this.startTime = startTime;
        this.endTime = endTime;
        unoccupiedTime = Duration.between(startTime, endTime);
    }
}
