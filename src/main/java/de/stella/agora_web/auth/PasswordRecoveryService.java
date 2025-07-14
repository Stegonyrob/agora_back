package de.stella.agora_web.auth;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import de.stella.agora_web.admin.model.Admin;
import de.stella.agora_web.admin.repository.AdminRepository;
import de.stella.agora_web.auth.model.PasswordResetToken;
import de.stella.agora_web.auth.repository.PasswordResetTokenRepository;
import de.stella.agora_web.notification.service.NotificationService;
import de.stella.agora_web.profiles.model.Profile;
import de.stella.agora_web.profiles.repository.ProfileRepository;
import de.stella.agora_web.user.model.User;
import de.stella.agora_web.user.persistence.IUserDAO;

@Service
public class PasswordRecoveryService {

    @Autowired
    private PasswordResetTokenRepository tokenRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private IUserDAO userDAO;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${frontend.reset-password-url:http://localhost:4200/reset-password}")
    private String resetPasswordUrl;

    public void sendRecoveryEmail(String email) {
        // Buscar usuario o admin
        Optional<User> userOpt = userDAO.findByEmail(email);
        Optional<Admin> adminOpt = adminRepository.findByEmail(email);
        Optional<Profile> profileOpt = profileRepository.findByEmail(email);
        if (userOpt.isEmpty() && adminOpt.isEmpty() && profileOpt.isEmpty()) {
            // No existe el email
            return;
        }
        // Generar token y guardar
        String token = UUID.randomUUID().toString();
        LocalDateTime expiry = LocalDateTime.now().plusHours(2);
        tokenRepository.deleteByEmail(email); // Solo uno activo por email
        PasswordResetToken resetToken = new PasswordResetToken(email, token, expiry);
        tokenRepository.save(resetToken);
        // Enviar email
        String link = resetPasswordUrl + "?token=" + token;
        String subject = "Recuperación de contraseña";
        String body = "Para restablecer tu contraseña, haz clic en el siguiente enlace: " + link + "\n\nEste enlace expirará en 2 horas.";
        notificationService.sendEmailNotification(email, subject, body);
    }

    public void resetPassword(String token, String newPassword) {
        Optional<PasswordResetToken> tokenOpt = tokenRepository.findByToken(token);
        if (tokenOpt.isEmpty()) {
            throw new IllegalArgumentException("Token inválido");
        }
        PasswordResetToken resetToken = tokenOpt.get();
        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(resetToken);
            throw new IllegalArgumentException("Token expirado");
        }
        String email = resetToken.getEmail();
        // Buscar usuario o admin
        Optional<User> userOpt = userDAO.findByEmail(email);
        Optional<Admin> adminOpt = adminRepository.findByEmail(email);
        Optional<Profile> profileOpt = profileRepository.findByEmail(email);
        boolean changed = false;
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            userDAO.save(user);
            changed = true;
        }
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            admin.setEmail(email); // No password field in Admin, adjust if needed
            // Si Admin tiene password, agregar lógica aquí
            changed = true;
        }
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            profile.setPassword(passwordEncoder.encode(newPassword));
            profileRepository.save(profile);
            changed = true;
        }
        tokenRepository.delete(resetToken);
        if (!changed) {
            throw new IllegalArgumentException("No se pudo actualizar la contraseña");
        }
    }
}
