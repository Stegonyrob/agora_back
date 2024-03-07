package de.stella.agora_web.generics;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;


    public interface IGenericRepository<T, ID extends Serializable> {

    List<T> findAllByIdIn(Collection<ID> ids);

    
}
