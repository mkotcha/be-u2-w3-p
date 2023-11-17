package org.emmek.beu2w3p.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserDTO(

        @NotNull(message = "email cannot be null")
        @NotEmpty(message = "email cannot be empty")
        @Pattern(regexp = "^[\\w.-]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Email not valid")
        String email,
        @NotNull(message = "name cannot be null")
        @NotEmpty(message = "name cannot be empty")
        @Size(min = 3, max = 30, message = "name must be between 3 e 30 chars")
        String name,
        @NotNull(message = "surname cannot be null")
        @NotEmpty(message = "surname cannot be empty")
        @Size(min = 3, max = 30, message = "surname must be between 3 e 30 chars")
        String surname

) {
}
