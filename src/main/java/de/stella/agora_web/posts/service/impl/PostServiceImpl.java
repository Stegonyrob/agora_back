package de.stella.agora_web.posts.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import de.stella.agora_web.comment.controller.dto.CommentDTO;
import de.stella.agora_web.comment.model.Comment;
import de.stella.agora_web.comment.repository.CommentRepository;
import de.stella.agora_web.posts.controller.dto.PostDTO;
import de.stella.agora_web.posts.controller.dto.PostSummaryDTO;
import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.posts.model.PostLove;
import de.stella.agora_web.posts.repository.PostLoveRepository;
import de.stella.agora_web.posts.repository.PostRepository;
import de.stella.agora_web.posts.service.IPostService;
import de.stella.agora_web.replies.controller.dto.ReplyDTO;
import de.stella.agora_web.replies.model.Reply;
import de.stella.agora_web.replies.repository.ReplyRepository;
import de.stella.agora_web.tags.model.Tag;
import de.stella.agora_web.tags.repository.TagRepository;
import de.stella.agora_web.tags.service.ITagService;
import de.stella.agora_web.user.exceptions.UserNotFoundException;
import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.service.impl.UserServiceImpl;

@Service
public class PostServiceImpl implements IPostService {

    private final PostRepository postRepository;
    private final PostLoveRepository postLoveRepository;
    private final UserServiceImpl userService;
    private final ITagService tagService;
    private final TagRepository tagRepository;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;

    public PostServiceImpl(PostRepository postRepository, PostLoveRepository postLoveRepository, UserServiceImpl userService, ITagService tagService,
            CommentRepository commentRepository, ReplyRepository replyRepository, TagRepository tagRepository) {
        this.postRepository = postRepository;
        this.postLoveRepository = postLoveRepository;
        this.userService = userService;
        this.tagService = tagService;
        this.commentRepository = commentRepository;
        this.replyRepository = replyRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Post createPost(PostDTO postDTO, Long userId) {
        Optional<User> user = userService.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }

        Post post = new Post();
        post.setUser(user.get());
        post.setTitle(postDTO.getTitle());
        post.setMessage(postDTO.getMessage());

        List<Tag> tags = new ArrayList<>();
        for (String tagName : postDTO.getTags()) {
            Tag tag = tagService.getTagByName(tagName);
            if (tag == null) {
                tag = tagService.createTag(tagName);
            }
            tags.add(tag);
        }

        List<String> hashtags = tagService.extractHashtags(post.getMessage());
        for (String hashtag : hashtags) {
            Tag tag = tagService.getTagByName(hashtag);
            if (tag == null) {
                tag = tagService.createTag(hashtag);
            }
            tags.add(tag);
        }

        post.setTags(tags);

        return postRepository.save(post);
    }

    @Override
    public void archivePost(Long id) {
        Post post = postRepository.findById(id).orElseThrow();
        post.setArchived(true);
        for (Comment comment : post.getComments()) {
            comment.setArchived(true);
            for (Reply reply : comment.getReplies()) {
                reply.setArchived(true);
            }
        }
        for (Tag tag : post.getTags()) {
            tag.getPosts().remove(post);
        }
        postRepository.save(post);
    }

    @Override
    public void unArchivePost(Long id) {
        Post post = postRepository.findById(id).orElseThrow();
        post.setArchived(false);
        for (Comment comment : post.getComments()) {
            comment.setArchived(false);
            for (Reply reply : comment.getReplies()) {
                reply.setArchived(false);
            }
        }
        for (Tag tag : post.getTags()) {
            tag.getPosts().add(post);
        }
        postRepository.save(post);
    }

    @Override
    public Post getById(Long id) {
        return postRepository.findById(id).orElseThrow();
    }

    @Override
    public Post updatePost(PostDTO postDTO, Long postId) {
        Post existingPost = postRepository.findById(postId).orElseThrow();
        existingPost.setTitle(postDTO.getTitle());
        existingPost.setMessage(postDTO.getMessage());

        existingPost.getTags().clear();

        List<Tag> tags = new ArrayList<>();
        for (String tagName : postDTO.getTags()) {
            Tag tag = tagService.getTagByName(tagName);
            if (tag == null) {
                tag = tagService.createTag(tagName);
            }
            tags.add(tag);
        }

        List<String> hashtags = tagService.extractHashtags(postDTO.getMessage());
        for (String hashtag : hashtags) {
            Tag tag = tagService.getTagByName(hashtag);
            if (tag == null) {
                tag = tagService.createTag(hashtag);
            }
            tags.add(tag);
        }

        existingPost.setTags(tags);

        return postRepository.save(existingPost);
    }

    @Override
    public List<Comment> getCommentsByPostId(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        return post.getComments();
    }

    @Override
    public List<Reply> getRepliesByCommentId(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        return comment.getReplies();
    }

    @Override
    public void createComment(Long postId, CommentDTO commentDTO) {
        Post post = postRepository.findById(postId).orElseThrow();
        Comment comment = new Comment();
        comment.setMessage(commentDTO.getMessage());
        comment.setUser(userService.getUserById(commentDTO.getUserId()));
        comment.setPost(post);
        commentRepository.save(comment);
    }

    @Override
    public void createReply(Long commentId, ReplyDTO replyDTO) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        Reply reply = new Reply();
        reply.setMessage(replyDTO.getMessage());
        reply.setUser(userService.getUserById(replyDTO.getUserId()));
        reply.setComment(comment);
        replyRepository.save(reply);
    }

    @Override
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    public void deleteReply(Long replyId) {
        replyRepository.deleteById(replyId);
    }

    @Override
    public Post save(PostDTO postDTO) {
        Post post = new Post();
        post.setTitle(postDTO.getTitle());
        post.setMessage(postDTO.getMessage());
        post.setUser(userService.getUserById(postDTO.getUserId()));

        List<Tag> tags = new ArrayList<>();
        for (String tagName : postDTO.getTags()) {
            Tag tag = tagService.getTagByName(tagName);
            if (tag == null) {
                tag = tagService.createTag(tagName);
            }
            tags.add(tag);
        }

        List<String> hashtags = tagService.extractHashtags(postDTO.getMessage());
        for (String hashtag : hashtags) {
            Tag tag = tagService.getTagByName(hashtag);
            if (tag == null) {
                tag = tagService.createTag(hashtag);
            }
            tags.add(tag);
        }

        post.setTags(tags);

        return postRepository.save(post);
    }

    @Override
    public List<String> extractHashtags(String message) {
        List<String> hashtags = new ArrayList<>();
        Pattern pattern = Pattern.compile("#(\\w+)");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            hashtags.add(matcher.group(1));
        }
        return hashtags;
    }

    @Override
    public Tag getTagByName(String tagName) {
        List<Tag> tags = tagRepository.findByName(tagName);
        if (tags.isEmpty()) {
            throw new NoSuchElementException("Tag with name " + tagName + " not found");
        }
        return tags.get(0);
    }

    @Override
    public Tag createTag(String tagName) {
        Tag tag = new Tag();
        tag.setName(tagName);
        return tagRepository.save(tag);
    }

    @Override
    public List<Tag> getTagsByPostId(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        return post.getTags();
    }

    @Override
    public List<Post> getPostsByUserId(Long userId) {
        return postRepository.findByUser_Id(userId);
    }

    @Override
    public Post update(PostDTO postDTO, Long id) {
        Post post = postRepository.findById(id).orElseThrow();

        post.setTitle(postDTO.getTitle());
        post.setMessage(postDTO.getMessage());

        // Remove existing tags
        post.getTags().clear();

        // Add tags from tag list
        List<Tag> tags = new ArrayList<>();
        for (String tagName : postDTO.getTags()) {
            Tag tag = tagService.getTagByName(tagName);
            if (tag == null) {
                tag = tagService.createTag(tagName);
            }
            tags.add(tag);
        }

        // Add tags from hashtags in post message
        List<String> hashtags = tagService.extractHashtags(postDTO.getMessage());
        for (String hashtag : hashtags) {
            Tag tag = tagService.getTagByName(hashtag);
            if (tag == null) {
                tag = tagService.createTag(hashtag);
            }
            tags.add(tag);
        }

        post.setTags(tags);

        return postRepository.save(post);
    }

    public static PostDTO toPostDTO(Post post) {
        return PostDTO.builder().id(post.getId()).userId(post.getUser() != null ? post.getUser().getId() : null)
                .title(post.getTitle()).message(post.getMessage())
                .tags(post.getTags() != null ? post.getTags().stream().map(Tag::getName).toList() : new ArrayList<>())
                // Si tienes tags, comentarios, etc., mapea aquí también
                .build();
    }

    // --- Lógica de "loves" usando la tabla auxiliar ---
    @Override
    public void lovePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId).orElseThrow();
        User user = userService.getUserById(userId);

        boolean exists = postLoveRepository.existsByPostIdAndProfileUserId(postId, userId);
        if (!exists) {
            PostLove postLove = new PostLove();
            postLove.setPost(post);
            postLove.setProfile(user.getProfile());
            postLoveRepository.save(postLove);
        }
    }

    @Override
    public void unlovePost(Long postId, Long userId) {
        PostLove postLove = postLoveRepository.findByPostIdAndProfileUserId(postId, userId)
                .orElseThrow(() -> new NoSuchElementException("No love found for this post and user"));
        postLoveRepository.delete(postLove);
    }

    @Override
    public Integer getLoveCount(Long postId) {
        return postLoveRepository.countByPostId(postId);
    }

    @Override
    public void save(Post post) {
        postRepository.save(post);
    }

    @Override
    public Page<PostSummaryDTO> getAllPostsWithCounts(Pageable pageable) {
        return postRepository.findAllWithCounts(pageable);
    }
}
