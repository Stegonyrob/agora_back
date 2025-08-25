package de.stella.agora_web.user.service;

public interface ITotpService {

    public String generateTotpSecret();

    public boolean validateTotpCode(String base64Secret, String code);
}
