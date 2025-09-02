package br.com.fiap.swiftarena.user;

import br.com.fiap.swiftarena.submission.Submission;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@Table(name = "users")
public class User implements UserDetails {

    @Id @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    private String avatarUrl;

    @OneToMany(mappedBy = "user")
    private List<Submission> submissions;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.toString()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }
}