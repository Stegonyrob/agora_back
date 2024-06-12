package de.stella.agora_web.profiles.model;

import java.util.Set;

import de.stella.agor

import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.replys.model.Reply;
import de.stella.agora_web.user.model.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

// Constructor con parámetros
    public Profile(Long id, String email, String firstName, String firstLastName, String secondLastName, String address, String city, String province, String postalCode, String numberPhone) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.firstLastName = firstLastName;
        this.secondLastName = secondLastName;
        this.address = address;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
        this.numberPhone = numberPhone;
    }



    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }



    public String getRelationship() {
        return relationship;
    }
}
=======
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;