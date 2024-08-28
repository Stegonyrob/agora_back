package de.stella.agora_web.banned.services;

import de.stella.agora_web.banned.model.Banned;

public interface IBannedService {
  public boolean isUserBanned(Long userId);

  public void banUser(
    Long userId,
    String banReason,
    boolean dataRetained,
    boolean euDataProtection
  );

  public void unbanUser(Long userId);

  public void unbanAllUsers();

  public int countInfractionsByUser(Long userId);

  Banned findByUserId(Long userId);
  void banUser(Long userId);
}
