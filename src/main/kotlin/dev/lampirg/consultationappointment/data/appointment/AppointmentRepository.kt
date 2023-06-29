package dev.lampirg.consultationappointment.data.appointment

import org.springframework.data.jpa.repository.JpaRepository

interface AppointmentRepository : JpaRepository<Appointment?, Long?>