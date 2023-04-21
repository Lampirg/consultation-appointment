package dev.lampirg.consultationappointment.data.admin;

import dev.lampirg.consultationappointment.data.model.PersonRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends PersonRepository<Admin> {
}