package de.stella.agora_web.avatar.module;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.stella.agora_web.profiles.model.Profile;
import de.stella.agora_web.user.model.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_image")
    private Long id;

    @Column(name = "image_name")
    private String imageName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(cascade = { CascadeType.ALL, CascadeType.REFRESH }, fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = true)
    private Profile profile;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id_user")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user;
}