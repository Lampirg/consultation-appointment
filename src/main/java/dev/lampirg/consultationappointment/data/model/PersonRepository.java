package dev.lampirg.consultationappointment.data.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface PersonRepository<T extends Person> extends JpaRepository<T, Integer> {
    T findByEmail(String email);
}