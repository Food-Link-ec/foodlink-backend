package com.foodlink.infrastructure.adapter.input.rest.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class UsuarioAutenticado implements Authentication {

    private final UUID usuarioId;
    private final String email;
    private final String tipoUsuario;
    private final Collection<GrantedAuthority> authorities;
    private boolean autenticado = true;

    private UsuarioAutenticado(UUID usuarioId, String email, String tipoUsuario, Collection<GrantedAuthority> authorities) {
        this.usuarioId = usuarioId;
        this.email = email;
        this.tipoUsuario = tipoUsuario;
        this.authorities = authorities;
    }

    public static UsuarioAutenticado of(UUID usuarioId, String email, String rol, String tipoUsuario) {
        return new UsuarioAutenticado(usuarioId, email, tipoUsuario, List.of(new SimpleGrantedAuthority(rol)));
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this;
    }

    @Override
    public boolean isAuthenticated() {
        return autenticado;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) {
        this.autenticado = isAuthenticated;
    }

    @Override
    public String getName() {
        return email;
    }
}
