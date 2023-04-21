package dev.lampirg.consultationappointment.data.teacher;

import dev.lampirg.consultationappointment.data.model.PersonRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface TeacherRepository extends PersonRepository<Teacher> {
    Page<Teacher> findByLastNameStartingWith(String lastName, Pageable pageable);
}