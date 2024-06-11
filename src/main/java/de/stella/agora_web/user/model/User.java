package de.stella.agora_web.user.model;

import java.util.Set;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.roles.model.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table (name = "users")
public class User {

   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    @Column (name = "id_user")
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String nickname;
    private String relationship;
    private String email;
    private String password;
   
    public User() {
    }
    public User(Long id, String username, String password, String firstName, String lastName, String nickname, String relationship, String email, Set<Role> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nickname = nickname;
        this.relationship = relationship;
        this.email = email;
        this.roles = roles;
    }


    public boolean hasRole(String role) {
        return false;
       
    }

@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
@JsonManagedReference
private Set<Post> posts;

@ManyToMany(fetch = FetchType.LAZY)
@JoinTable(name = "roles_users", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
@JsonManagedReference
private Set<Role> roles;


public GrantedAuthority getRole() {

    return getRole();
   
}

public void setId(long long1) {
 return;
}

public User orElseThrow(Object object) {
    return object == null ? null : this;
}

public boolean isPresent() {
   return true;
}

public User get() {
    return this;
}

}


