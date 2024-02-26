package com.compassuol.challenge.msuser.jwt;

import com.compassuol.challenge.msuser.application.service.UserService;
import com.compassuol.challenge.msuser.domain.Usuario;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = userService.getUserByEmail(email);
        if (usuario == null) {
            throw new EntityNotFoundException("Usuario não encontrado com o email: " + email);
        }
        return new JwtUserDetails(usuario);
    }

    public JwtToken getTokenAuthenticated(String email) {
        return JwtUtils.createToken(email);
    }
}
