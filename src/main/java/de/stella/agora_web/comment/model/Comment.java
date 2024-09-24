package de.stella.agora_web.comment.model;

import java.time.LocalDateTime;
import java.util.List;

import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.replies.model.Reply;
import de.stella.agora_web.tags.model.Tag;
import de.stella.agora_web.user.model.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "comments")
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "title")
  private String title;

  @Column(name = "message ", length = 1000)
  private String message;

  @Column(name = "creation_date")
  private LocalDateTime creationDate;

  @Column(name = "archived")
  private Boolean archived;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id")
  private Post post;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToMany
  @JoinTable(name = "comment_tags", joinColumns = @JoinColumn(name = "comment_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
  private List<Tag> tags;

  public Comment() {
  }

  public Comment(Long id, String title, String message, LocalDateTime creationDate, User author, Post post) {
    this.id = id;
    this.title = title;
    this.message = message;
    this.creationDate = creationDate;
    this.user = author;
    this.post = post;
  }

  public void setAuthor(User author) {
    this.user = author;
  }

  public void setPost(Post post) {
    this.post = post;
  }

  @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Reply> replies;

  public List<Reply> getReplies() {
    return replies;
  }
}
// Para implementar un sistema de moderación de comentarios en tu aplicación
// Agora Web, podemos seguir estos pasos:
// 1. Implementar un servicio de moderación de lenguaje:
// - Necesitarás utilizar una biblioteca de detección de lenguaje natural como
// NLTK o spaCy.
// - Estas herramientas pueden ayudarte a identificar palabras ofensivas,
// racismo, violencia de género, etc.
// 2. Crear un servicio de moderación:
// - Implementa un servicio que reciba el texto del comentario y lo procese
// usando la biblioteca de detección de lenguaje.
// - Este servicio devolverá un resultado booleano indicando si el comentario
// está apropiado o no.
// 3. Integrar el servicio de moderación en el controlador:
// - Modifica el método `createComment` en `CommentController.java` para incluir
// la moderación del lenguaje.
// 4. Implementar un sistema de censura:
// - Usa el servicio de moderación para verificar todos los comentarios antes de
// publicarlos.
// - Si un comentario es rechazado por la moderación, puedes guardarlo como un
// comentario censurado usando el servicio `ICensuredCommentService`.
// Aquí te muestro cómo podrías implementar estos pasos:
// 1. Instalar una biblioteca de detección de lenguaje natural:
// ```bash
// mvn dependency:resolve
// ```
// Añade esta dependencia al archivo `pom.xml`:
// ```xml
// <dependency>
// <groupId>org.apache.opennlp</groupId>
// <artifactId>opennlp-tools</artifactId>
// <version>2.4.0</version>
// </dependency>
// ```
// 2. Crea un servicio de moderación de lenguaje:
// ```java
// @Service
// public class LanguageModerationService {
// private final OpenNLPMaxentTagger tagger;
// public LanguageModerationService() throws IOException {
// String[] models = {"models/en-token.bin", "models/en-pos-maxent.bin"};
// tagger = new OpenNLPMaxentTagger(models);
// }
// public boolean isCommentApproved(String comment) {
// // Aquí implementa la lógica de moderación
// // Por ejemplo, puedes buscar palabras específicas o usar el modelo para
// analizar el contexto
// // Este es solo un ejemplo básico y deberías expandirlo según tus necesidades
// String[] words = comment.split("\\s+");
// for (String word : words) {
// if (word.toLowerCase().contains("badword")) {
// return false; // Comentario contiene una palabra maliciosa
// }
// }
// return true; // Comentario está apropiado
// }
// }
// ```
// 3. Modifica el controlador para incluir la moderación:
// ```java
// @RestController
// @RequestMapping(path = "${api-endpoint}/")
// public class CommentController {
// private final ICommentService commentService;
// private final LanguageModerationService moderationService;
// public CommentController(ICommentService commentService,
// LanguageModerationService moderationService) {
// this.commentService = commentService;
// this.moderationService = moderationService;
// }
// @PostMapping("/comments/create")
// @PreAuthorize("hasRole('USER','ADMIN')")
// public ResponseEntity<Comment> createComment(
// @RequestBody CommentDTO commentDTO
// ) {
// String commentText = commentDTO.getMessage();
// if (!moderationService.isCommentApproved(commentText)) {
// // Si el comentario no está apropiado, lo censura
// CensuredComment censoredComment = new CensuredComment(commentDTO.getPostId(),
// commentDTO.getUserId());
// censoredComment.setReason("Comentario no apropiado por moderación de
// lenguaje");
// ICensuredCommentService censuredCommentService = // Obtén la referencia al
// servicio de censurados
// censuredCommentService.save(censoredComment);
// return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
// }
// Comment comment = commentService.createComment(commentDTO, null);
// return ResponseEntity.status(HttpStatus.CREATED).body(comment);
// }
// }
// ```
// 4. Actualiza el servicio de censurados para manejar los comentarios
// censurados:
// ```java
// @Service
// public class CensuredCommentService implements ICensuredCommentService {
// @Autowired
// private CensuredCommentRepository repository;
// @Override
// public List<CensuredComment> findAll() {
// return repository.findAll();
// }
// @Override
// public CensuredComment findById(Long id) {
// Optional<CensuredComment> optionalCensuredComment = repository.findById(id);
// return optionalCensuredComment.orElse(null);
// }
// @Override
// public CensuredComment save(CensuredComment censuredComment) {
// return repository.save(censuredComment);
// }
// @Override
// public void delete(Long id) {
// repository.deleteById(id);
// }
// public List<CensuredComment> findByUserId(Long userId) {
// return repository.findByUserId(userId);
// }
// }
// ```
// Estos pasos te proporcionan una base sólida para implementar un sistema de
// moderación de lenguaje en tu aplicación Agora Web.
// Recuerda que necesitarás expandir y mejorar este sistema según tus requisitos
// específicos y las políticas de moderación que desees implementar.
// Para mejorar aún más la moderación, podrías considerar:
// 1. Usar modelos de machine learning más avanzados como TensorFlow o PyTorch.
// 2. Integrar APIs de detección de lenguaje natural como Google Cloud Natural
// Language API o IBM Watson Tone Analyzer.
// 3. Implementar un sistema de reportes de usuarios para marcar contenido
// ofensivo.
// 4. Crear un panel de administración para revisar y aprobar manualmente
// comentarios censurados.
// Recuerda también que la moderación automática puede no capturar todo, así que
// siempre mantén un equipo humano revisando y actualizando las reglas de
// moderación regularmente.
