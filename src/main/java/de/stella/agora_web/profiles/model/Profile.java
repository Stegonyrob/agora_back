package de.stella.agora_web.profiles.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.stella.agora_web.replys.model.Reply;
import de.stella.agora_web.user.model.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "profiles")
public class Profile {

    @Id
    @Column(name = "id_profile")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id_user")
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private User user;

    private String email;
    private String firstName;
    private String firstLastName;
    private String secondLastName;
    private String address;
    private String city;
    private String province;
    private String postalCode;
    private String numberPhone;

    @Column
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "reply", joinColumns = @JoinColumn(name = "profile_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
    Set<Reply> favorites;

    private String fitsLastName;
 // Private constructor to force the use of Builder
 private Profile(Builder builder) {
    this.id = builder.id;
    this.user = builder.user;
    this.email = builder.email;
    this.firstName = builder.firstName;
    this.fitsLastName = builder.fitsLastName;
    this.secondLastName = builder.secondLastName;
    this.address = builder.address;
    this.city = builder.city;
    this.province = builder.province;
    this.postalCode = builder.postalCode;
}

public Profile() {
 return;
}

// Static inner class for building instances of Profile
public static class Builder {
    private Long id;
    private User user;
    private String email;
    private String firstName;
    private String fitsLastName;
    private String secondLastName;
    private String address;
    private String city;
    private String province;
    private String postalCode;
    private String firstLastName;
    private String numberPhone;


    public Builder id(Long id) {
        this.id = id;
        return this;
    }

    public Builder user(User user) {
        this.user = user;
        return this;
    }

    public Builder email(String email) {
        this.email = email;
        return this;
    }



    public Profile build() {
        return new Profile(this);
    }

    public Builder firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public Builder firstLastName(String firstLastName) {
        this.firstLastName = firstLastName;
        return this;
    }

    public Builder secondLastName(String secondLastName) {
        this.secondLastName = secondLastName;
        return this;
    }

    public Builder address(String address) {
        this.address = address;
        return this;
    }

    public Builder postalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public Builder numberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
        return this;
    }

    public Builder city(String city) {
        this.city = city;
        return this;
    }

    public Builder province(String province) {
        this.province = province;
        return this;
    }


}
    public void toggleFavorite(Reply reply) {
        if (favorites.contains(reply)) {
            favorites.remove(reply);
        } else {
            favorites.add(reply);
        }
    }

    public static Builder builder() {
        return new Builder();
    }



}