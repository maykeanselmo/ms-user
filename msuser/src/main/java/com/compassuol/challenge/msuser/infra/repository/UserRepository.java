package com.compassuol.challenge.msuser.infra.repository;

import com.compassuol.challenge.msuser.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Usuario, Long> {

    Usuario findByEmail(String email);


    Usuario findByCpf(String cpf);
}
