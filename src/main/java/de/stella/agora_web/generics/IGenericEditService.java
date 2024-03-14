package de.stella.agora_web.generics;

public interface IGenericEditService<T,R> {
    
    R save(T type) throws Exception;
    R update(T type, Long id);

}
