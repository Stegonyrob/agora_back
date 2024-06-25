package de.stella.agora_web.auth;

import lombok.Getter;
import lombok.Setter;

/**
 * Clase que representa un objeto de transferencia de datos para realizar el
 * registro de un usuario. Contiene los campos necesarios para registrar un
 * usuario, que son el nombre de usuario y la contraseña. Los getters y setters
 * permiten la configuración y obtención de estos valores.
 */
@Getter
@Setter
public class SignUpDTO {

    /**
     * Nombre de usuario del usuario que se va a registrar.
     */
    private String username;

    /**
     * Contraseña del usuario que se va a registrar.
     */
    private String password;

    /**
     * Constructor sin argumentos. Se utiliza principalmente para la serialización
     * de objetos.
     */
    public SignUpDTO() {

    }

    /**
     * Constructor que inicializa los campos username y password con los valores
     * pasados como argumentos.
     * 
     * @param username Nombre de usuario del usuario que se va a registrar.
     * @param password Contraseña del usuario que se va a registrar.
     */
    public SignUpDTO(String username, String password) {
        super();
        this.username = username;
        this.password = password;
    }

    /**
     * Getter para el nombre de usuario.
     * 
     * @return El nombre de usuario del usuario registrado.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter para el nombre de usuario.
     * 
     * @param username El nuevo nombre de usuario del usuario registrado.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter para la contraseña.
     * 
     * @return La contraseña del usuario registrado.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter para la contraseña.
     * 
     * @param password La nueva contraseña del usuario registrado.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return getFirstName();
    }

    public String getLastName1() {
        return getLastName1();
    }

    public String getLastName2() {
        return getLastName2();
    }

    public String getRelationship() {
        return getRelationship();
    }

    public String getEmail() {
        return getEmail();
    }

    public String getCity() {
        return getCity();
    }

}
