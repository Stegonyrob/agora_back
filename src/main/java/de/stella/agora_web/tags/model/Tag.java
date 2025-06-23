package de.stella.agora_web.tags.model;

import java.util.List;

import de.stella.agora_web.posts.model.Post;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "archived")
    private Boolean archived = false;

    @ManyToMany(mappedBy = "tags")
    private List<Post> posts;

    public Tag() {
        this.archived = false;

    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
