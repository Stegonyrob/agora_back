package de.stella.agora_web.encryptations;

public interface IEncryptFacade {
    
    String encode(String type, String data);
    String decode(String type, String data);

}
