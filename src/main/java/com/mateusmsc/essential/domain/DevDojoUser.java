package com.mateusmsc.essential.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
// Classe do lombok para poder buildar as coisas
@Builder
//Garantir q o objeto faz parte do UserDetails
public class DevDojoUser implements UserDetails {
    //Reflete o modelo no banco de dados
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

//    @JsonProperty
    // utilizado para mapear do request para o atributo
    @NotEmpty(message = "The anime name cannot be empty or null")
    private String name;
    private String username;
    private String password;
    private String authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(authorities.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}