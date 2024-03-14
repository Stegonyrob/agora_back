package de.stella.agora_web.generics;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@MappedSuperclass
public abstract class GenericEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    protected Long id;

 
}
