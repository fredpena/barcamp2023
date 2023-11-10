package dev.fredpena.barcamp.data.common.service;

import dev.fredpena.barcamp.data.common.entity.CommonUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CommonUserRepository extends JpaRepository<CommonUser, String>, JpaSpecificationExecutor<CommonUser> {

}
