package de.stella.agora_web.tags.dto;

import java.util.List;

public class TagListDTO {

    private List<TagSummaryDTO> tags;

    public List<TagSummaryDTO> getTags() {
        return tags;
    }

    public void setTags(List<TagSummaryDTO> tags) {
        this.tags = tags;
    }
}
