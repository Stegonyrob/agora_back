package de.stella.agora_web.tags.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.stella.agora_web.posts.model.Post;
import de.stella.agora_web.posts.repository.PostRepository;
import de.stella.agora_web.replys.model.Reply;
import de.stella.agora_web.replys.repository.ReplyRepository;
import de.stella.agora_web.tags.module.Tag;
import de.stella.agora_web.tags.repository.TagRepository;
import de.stella.agora_web.tags.service.ITagService;

@Service
public class TagServiceImpl implements ITagService {
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    @Override
    public Tag getTagById(Long id) {
        return tagRepository.findById(id).orElse(null);
    }

    @Override
    public Tag createTag(String name) {
        Tag tag = new Tag();
        tag.setName(name);
        return tagRepository.save(tag);
    }

    public List<String> extractHashtags(String text) {
        List<String> hashtags = new ArrayList<>();
        Pattern pattern = Pattern.compile("#(\\w+)");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            hashtags.add(matcher.group(1));
        }
        return hashtags;
    }

    @Override
    public void addTagToPost(Long postId, Long tagId) {
        Post post = postRepository.findById(postId).orElse(null);
        Tag tag = tagRepository.findById(tagId).orElse(null);
        if (post != null && tag != null) {
            post.getTags().add(tag);
            postRepository.save(post);
        }
    }

    @Override
    public void removeTagFromPost(Long postId, Long tagId) {
        Post post = postRepository.findById(postId).orElse(null);
        Tag tag = tagRepository.findById(tagId).orElse(null);
        if (post != null && tag != null) {
            post.getTags().remove(tag);
            postRepository.save(post);
        }
    }

    @Override
    public Tag getTagByName(String tagName) {
        return (Tag) tagRepository.findByName(tagName);
    }

    @Override
    public void addTagToReply(Long replyId, Long tagId) {
        Reply reply = replyRepository.findById(replyId).orElse(null);
        Tag tag = tagRepository.findById(tagId).orElse(null);
        if (reply != null && tag != null) {
            reply.getTags().add(tag);
            postRepository.save(reply);
        }
    }

    @Override
    public void removeTagFromReply(Long replyId, Long tagId) {
        Reply reply = replyRepository.findById(replyId).orElse(null);
        Tag tag = tagRepository.findById(tagId).orElse(null);
        if (reply != null && tag != null) {
            reply.getTags().remove(tag);
            replyRepository.save(reply);
        }
    }
}