package de.stella.agora_web.captcha.service;

public interface ICaptchaService {
    boolean verify(String token);
}