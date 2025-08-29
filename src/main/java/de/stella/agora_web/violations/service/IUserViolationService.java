package de.stella.agora_web.violations.service;

import java.util.List;

import de.stella.agora_web.violations.controller.dto.UserViolationDTO;

public interface IUserViolationService {

    void registerViolation(UserViolationDTO violationDTO);

    List<UserViolationDTO> getViolationsByUser(Long userId);

    int countViolationsByUser(Long userId);
    // Lógica de sanción automática

    void evaluateAndApplySanction(Long userId);

    void removeSanction(Long userId);
}
