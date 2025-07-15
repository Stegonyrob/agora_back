package de.stella.agora_web.user.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class UserFieldTest {

    @Test
    void testPhoneAndTotpSecretFields() {
        User user = new User();
        String phone = "+34123456789";
        String secret = "SECRETBASE32";
        user.setPhone(phone);
        user.setTotpSecret(secret);
        assertEquals(phone, user.getPhone());
        assertEquals(secret, user.getTotpSecret());
    }
}
