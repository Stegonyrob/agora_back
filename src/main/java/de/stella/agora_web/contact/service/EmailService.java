package de.stella.agora_web.contact.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarCorreo(String nombre, String correo, String mensaje) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("tu_correo_de_destino@gmail.com");
        message.setSubject("Nuevo mensaje de contacto");
        message.setText("Nombre: " + nombre + "\nCorreo: " + correo + "\nMensaje: " + mensaje);

        mailSender.send(message);
    }
}