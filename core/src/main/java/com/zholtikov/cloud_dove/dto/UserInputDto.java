package com.zholtikov.cloud_dove.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public class UserInputDto {
    @Email(message = "Invalid email!")
    @Schema(name = "email", example = "nickname@mail.com")
    private String email;
    @Pattern(regexp = "[^ ]*", message = "There must be no space symbols in username!")
    @NotEmpty(message = "username may not be empty!")
    @Schema(name = "username", example = "YourNickname", description = "Username may not be empty and may not contain space symbols")
    private String username;
    @Pattern(regexp = "[^ ]*", message = "There must be no space symbols in password!")
    @NotEmpty(message = "password may not be empty!")
    private String password;
    @NotEmpty(message = "Name may not be empty!")
    @Schema(name = "name", example = "Firstname Lastname", description = "Displayed name. May not be empty")
    private String name;

    public @Email(message = "Invalid email!") String getEmail() {
        return email;
    }

    public void setEmail(@Email(message = "Invalid email!") String email) {
        this.email = email;
    }

    public @Pattern(regexp = "[^ ]*", message = "There must be no space symbols in username!") @NotEmpty(message = "username may not be empty!") String getUsername() {
        return username;
    }

    public void setUsername(@Pattern(regexp = "[^ ]*", message = "There must be no space symbols in username!") @NotEmpty(message = "username may not be empty!") String username) {
        this.username = username;
    }

    public @Pattern(regexp = "[^ ]*", message = "There must be no space symbols in password!") @NotEmpty(message = "password may not be empty!") String getPassword() {
        return password;
    }

    public void setPassword(@Pattern(regexp = "[^ ]*", message = "There must be no space symbols in password!") @NotEmpty(message = "password may not be empty!") String password) {
        this.password = password;
    }

    public @NotEmpty(message = "Name may not be empty!") String getName() {
        return name;
    }

    public void setName(@NotEmpty(message = "Name may not be empty!") String name) {
        this.name = name;
    }
}

