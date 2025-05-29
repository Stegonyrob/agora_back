package de.stella.agora_web.captcha.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import de.stella.agora_web.captcha.service.ICaptchaService;

@Service
public class CaptchaServiceImpl implements ICaptchaService {

    @Value("${recaptcha.secret}")
    private String recaptchaSecret;

    private static final String VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    @Override
    public boolean verify(String token) {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("secret", recaptchaSecret);
        params.add("response", token);

        ResponseEntity<Map> response = restTemplate.postForEntity(VERIFY_URL, params, Map.class);
        Map body = response.getBody();
        return (Boolean) body.get("success");
    }
}