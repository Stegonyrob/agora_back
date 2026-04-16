package de.stella.agora_web.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SanctionInfoResponse {

    private String sanctionType;
    private String sanctionEnd;
    private String message;
}
