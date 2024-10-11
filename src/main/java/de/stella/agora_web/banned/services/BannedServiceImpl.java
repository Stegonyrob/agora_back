package de.stella.agora_web.banned.services;

import de.stella.agora_web.banned.model.Banned;
import de.stella.agora_web.banned.repository.BannedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BannedServiceImpl implements IBannedService {

  @Autowired
  private BannedRepository bannedRepository;

  @Override
  public Banned findByUserId(Long userId) {
    return bannedRepository.findByUserId(userId);
  }

  @Override
  public void banUser(Long userId) {
    // Lógica para banear al usuario
    Banned banned = new Banned();
    banned.setUserId(userId);
    bannedRepository.save(banned);
  }

  @Override
  public void unbanUser(Long userId) {
    // Lógica para desbanear al usuario
    Banned banned = bannedRepository.findByUserId(userId);
    if (banned != null) {
      bannedRepository.delete(banned);
    }
  }

  @Override
  public boolean isUserBanned(Long userId) {
    return bannedRepository.existsByUserId(userId);
  }

  @Override
  public void banUser(
    Long userId,
    String banReason,
    boolean dataRetained,
    boolean euDataProtection
  ) {
    Banned banned = new Banned();
    banned.setUserId(userId);
    banned.setBanReason(banReason);
    banned.setDataRetained(dataRetained);
    banned.setEuDataProtection(euDataProtection);
    bannedRepository.save(banned);
  }

  @Override
  public void unbanAllUsers() {
    bannedRepository.deleteAll();
  }

  @Override
  public int countInfractionsByUser(Long userId) {
    // Cuenta el n mero de infracciones del usuario
    return bannedRepository.countByUserId(userId);
  }
}
