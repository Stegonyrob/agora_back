package de.stella.agora_web.tags.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagSummaryDTO {

    private Long id;
    private String name;
    private boolean archived;
}
