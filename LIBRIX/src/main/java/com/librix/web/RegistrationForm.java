package com.librix.web;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegistrationForm {
    @NotBlank @Size(max = 160)
    private String fullName;
    @NotBlank @Email @Size(max = 190)
    private String email;
    @NotBlank @Size(min = 6, max = 80)
    private String username;
    @NotBlank @Size(min = 8, max = 72)
    private String password;
    @NotBlank
    private String confirmPassword;
    @Size(max = 40)
    private String phone;

    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getConfirmPassword() { return confirmPassword; }
    public String getPhone() { return phone; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email) { this.email = email; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
    public void setPhone(String phone) { this.phone = phone; }
}
