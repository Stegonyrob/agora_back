package de.stella.agora_web.roles.model;

import java.util.Set;

import de.stella.agora_web.user.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;




    @Entity
    @Table(name = "roles")
    @Data
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class Role {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id_role")
        private Long id;

        private String name;

        @ManyToMany(mappedBy = "roles")
        Set<User> users;
    

    
    public String getRole() {
       return this.name; 
    }
}
