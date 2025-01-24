package com.msouza.client.jwt;


import com.msouza.client.entity.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class JwtUserDetails extends User {

    private Usuario usuario;

    public JwtUserDetails(Usuario usuario) {
        super(usuario.getUsername(), usuario.getPassword(), AuthorityUtils.NO_AUTHORITIES);
        this.usuario = usuario;
    }

    public Long getId() {
        return this.usuario.getId();
    }
}
