package de.stella.agora_web.admin.service;

public interface IAdminTotpService {
    String generateTotpSecret(Long adminId);
    String getAdminTotpSecret(Long adminId);
    boolean validateAdminTotp(Long adminId, String totpCode);
    boolean enableTotpForAdmin(Long adminId, String totpCode);
    boolean disableTotpForAdmin(Long adminId);
    boolean isAdminTotpEnabled(Long adminId);
    String regenerateAdminTotpSecret(Long adminId);
    String generateTotpQrUrl(Long adminId, String appName);
}
