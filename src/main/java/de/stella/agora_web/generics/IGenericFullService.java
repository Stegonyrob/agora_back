package de.stella.agora_web.generics;

import de.stella.agora_web.messages.Message;
import de.stella.agora_web.posts.controller.dto.PostDTO;
import de.stella.agora_web.posts.model.Post;
import java.util.List;
import org.springframework.lang.NonNull;

public interface IGenericFullService<T, DTO> extends IGenericGetService<T> {
  List<T> getAll();

  T getByName(String name) throws Exception;

  public T save();

  public T update(@NonNull Long id, DTO obj) throws Exception;

  public Message delete(@NonNull Long id) throws Exception;

  List<Post> findPostsByUserId(Long userId);

  Object get(long l);

  T getById(@NonNull Long id);
  T create(DTO dto);
  Post create(PostDTO postDTO);
}
