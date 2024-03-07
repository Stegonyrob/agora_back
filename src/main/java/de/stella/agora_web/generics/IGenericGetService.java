package de.stella.agora_web.generics;

public interface IGenericGetService<T> {

    T getById(Long id) throws Exception;
    
}
