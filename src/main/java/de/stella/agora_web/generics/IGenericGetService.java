package de.stella.agora_web.generics;

import lombok.NonNull;

public interface IGenericGetService<T> {

    T getById(@NonNull Long id) throws Exception;
}