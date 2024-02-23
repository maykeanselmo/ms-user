package com.compassuol.challenge.msuser.jwt;

import com.compassuol.challenge.msuser.domain.Usuario;
import org.springframework.security.core.userdetails.User;


import java.util.ArrayList;

public class JwtUserDetails extends User  {
    private Usuario usuario;

    public JwtUserDetails(Usuario usuario) {
        super(usuario.getEmail(), usuario.getPassword(), new ArrayList<>());
        this.usuario = usuario;
    }


    public Long getId() {
        return this.usuario.getId();
    }

}
