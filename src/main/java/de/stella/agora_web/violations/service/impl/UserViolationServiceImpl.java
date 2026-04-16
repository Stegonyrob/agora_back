package de.stella.agora_web.violations.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.model.User.SanctionStatus;
import de.stella.agora_web.user.model.User.SanctionType;
import de.stella.agora_web.user.repository.UserRepository;
import de.stella.agora_web.violations.controller.dto.UserViolationDTO;
import de.stella.agora_web.violations.service.IUserViolationService;

@Service
public class UserViolationServiceImpl implements IUserViolationService {

    // Simulación de persistencia en memoria (reemplazar por JPA en el futuro)
    private final ConcurrentHashMap<Long, List<UserViolationDTO>> userViolations = new ConcurrentHashMap<>();

    @Autowired
    private UserRepository userRepository;

    @Override
    public void registerViolation(UserViolationDTO violationDTO) {
        if (violationDTO == null) {
            throw new NullPointerException("violationDTO");
        }
        userViolations.computeIfAbsent(violationDTO.getUserId(), k -> new ArrayList<>()).add(violationDTO);
        evaluateAndApplySanction(violationDTO.getUserId());
    }

    @Override
    public List<UserViolationDTO> getViolationsByUser(Long userId) {
        if (userId == null) {
            throw new NullPointerException("userId");
        }
        return userViolations.getOrDefault(userId, new ArrayList<UserViolationDTO>());
    }

    @Override
    public int countViolationsByUser(Long userId) {
        if (userId == null) {
            throw new NullPointerException("userId");
        }
        return userViolations.getOrDefault(userId, List.of()).size();
    }

    @Override
    public void evaluateAndApplySanction(Long userId) {
        List<UserViolationDTO> violations = getViolationsByUser(userId);
        int total = violations.size();
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return;
        }

        // Lógica simple: 1 aviso, 3 = suspensión 1 semana, 5 = suspensión 1 mes, 7+ = expulsión
        if (total >= 7) {
            user.setSanctionStatus(SanctionStatus.EXPELLED);
            user.setSanctionType(SanctionType.EXPELLED);
            user.setSanctionExpiration(null);
        } else if (total >= 5) {
            user.setSanctionStatus(SanctionStatus.SUSPENDED);
            user.setSanctionType(SanctionType.SUSPENSION_1MONTH);
            user.setSanctionExpiration(LocalDateTime.now().plusMonths(1));
        } else if (total >= 3) {
            user.setSanctionStatus(SanctionStatus.SUSPENDED);
            user.setSanctionType(SanctionType.SUSPENSION_1WEEK);
            user.setSanctionExpiration(LocalDateTime.now().plusWeeks(1));
        } else if (total >= 1) {
            user.setSanctionStatus(SanctionStatus.WARNING);
            user.setSanctionType(SanctionType.WARNING);
            user.setSanctionExpiration(null);
        } else {
            user.setSanctionStatus(SanctionStatus.NONE);
            user.setSanctionType(SanctionType.NONE);
            user.setSanctionExpiration(null);
        }
        userRepository.save(user);
    }

    @Override
    public void removeSanction(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return;
        }
        user.setSanctionStatus(SanctionStatus.NONE);
        user.setSanctionType(SanctionType.NONE);
        user.setSanctionExpiration(null);
        userRepository.save(user);
    }
}
