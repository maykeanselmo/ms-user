package com.compassuol.challenge.msuser.infra.repository;

import com.compassuol.challenge.msuser.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
