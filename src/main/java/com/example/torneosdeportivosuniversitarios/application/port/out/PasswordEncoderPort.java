package com.example.torneosdeportivosuniversitarios.application.port.out;

/**
 * Puerto de salida para encriptar passwords. La aplicacion no depende de Spring Security:
 * la implementacion con BCrypt vive en infrastructure/security.
 */
public interface PasswordEncoderPort {

    String encode(String rawPassword);

    boolean matches(String rawPassword, String encodedPassword);
}
