package org.emmek.beu2w3p.controller;

import org.emmek.beu2w3p.entities.Event;
import org.emmek.beu2w3p.entities.User;
import org.emmek.beu2w3p.exceptions.BadRequestException;
import org.emmek.beu2w3p.exceptions.NotFoundException;
import org.emmek.beu2w3p.payloads.UserDTO;
import org.emmek.beu2w3p.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("")
    public Page<User> getUsers(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(defaultValue = "id") String sort) {
        return userService.getUsers(page, size, sort);
    }

    @GetMapping("/me")
    public User getProfile(@AuthenticationPrincipal User user) {
        return user;
    }

    @GetMapping("/me/events/{id}/book")
    public Event bookEvent(@AuthenticationPrincipal User user, @PathVariable long id) {
        return userService.bookEvent(user, id);
    }

    @GetMapping("/me/events/{id}/unbook")
    public Event unBookEvent(@AuthenticationPrincipal User user, @PathVariable long id) {
        return userService.unBookEvent(user, id);
    }

    @GetMapping("/{userId}/events/{eventId}/book")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Event bookEvent(@PathVariable long userId, @PathVariable long eventId) {
        return userService.bookEvent(userId, eventId);
    }

    @GetMapping("/{userId}/events/{eventId}/unbook")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Event unBookEvent(@PathVariable long userId, @PathVariable long eventId) {
        return userService.unBookEvent(userId, eventId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User getUserById(@PathVariable long id) {
        try {
            return userService.findById(id);
        } catch (Exception e) {
            throw new NotFoundException(id);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User findByIdAndUpdate(@PathVariable long id, @RequestBody @Validated UserDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        }
        return userService.findByIdAndUpdate(id, body);
    }

    @GetMapping("/{id}/set-admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User setAdmin(@PathVariable long id) {
        return userService.setAdmin(id);
    }
}
