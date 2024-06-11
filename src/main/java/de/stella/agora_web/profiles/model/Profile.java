package de.stella.agora_web.profiles.model;

import java.util.Set;

import de.stella.agora_web.replys.model.Reply;
import de.stella.agora_web.user.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Profile {

    private Long id;
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
    private Set<Reply> favorites;

    public void toggleFavorite(Reply reply) {
        if (favorites.contains(reply)) {
            favorites.remove(reply);
        } else {
            favorites.add(reply);
        }
    }

    public static class Builder {
        private Long id;
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
        private Set<Reply> favorites;

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

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder province(String province) {
            this.province = province;
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

        public Builder favorites(Set<Reply> favorites) {
            this.favorites = favorites;
            return this;
        }

        public Profile build() {
            Profile profile = new Profile();
            profile.setId(id);
            profile.setUser(user);
            profile.setEmail(email);
            profile.setFirstName(firstName);
            profile.setFirstLastName(firstLastName);
            profile.setSecondLastName(secondLastName);
            profile.setAddress(address);
            profile.setCity(city);
            profile.setProvince(province);
            profile.setPostalCode(postalCode);
            profile.setNumberPhone(numberPhone);
            profile.setFavorites(favorites);
            return profile;
        }
    }

    public static Profile.Builder builder() {
        return new Profile.Builder();
    }
}