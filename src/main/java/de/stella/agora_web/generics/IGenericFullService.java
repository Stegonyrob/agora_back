package de.stella.agora_web.generics;

import java.util.List;

import org.springframework.lang.NonNull;

import de.stella.agora_web.messages.Message;
import de.stella.agora_web.posts.model.Post;

public interface IGenericFullService<T, DTO> extends IGenericGetService<T> {
    List<T> getAll();

    T getByName(String name) throws Exception;

    public T save(DTO obj);

    public T update(@NonNull Long id, DTO obj) throws Exception;

    public Message delete(@NonNull Long id) throws Exception;

    List<Post> findPostsByUserId(Long userId);
}