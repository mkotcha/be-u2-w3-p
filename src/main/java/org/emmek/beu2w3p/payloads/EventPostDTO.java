package org.emmek.beu2w3p.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EventPostDTO(
        @NotNull(message = "Title cannot be null")
        @NotEmpty(message = "Title cannot be empty")
        @Size(min = 3, max = 30, message = "Title must be between 3 e 30 chars")
        String title,
        @NotNull(message = "Description cannot be null")
        @NotEmpty(message = "Description cannot be empty")
        @Size(min = 3, max = 30, message = "Description must be between 3 e 30 chars")
        String description,
        @NotNull(message = "Location cannot be null")
        @NotEmpty(message = "Location cannot be empty")
        @Size(min = 3, max = 30, message = "Location must be between 3 e 30 chars")
        String location,

        @NotNull(message = "Date cannot be null")
        @NotEmpty(message = "Date cannot be empty")
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Date not valid (yyyy-mm-dd)")
        String date,

        @NotNull(message = "Max participants cannot be null")
        int maxParticipants

) {
}
