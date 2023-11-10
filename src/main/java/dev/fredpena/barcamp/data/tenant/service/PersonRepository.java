package dev.fredpena.barcamp.data.tenant.service;


import dev.fredpena.barcamp.data.tenant.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PersonRepository
        extends
            JpaRepository<Person, Long>,
            JpaSpecificationExecutor<Person> {

}
