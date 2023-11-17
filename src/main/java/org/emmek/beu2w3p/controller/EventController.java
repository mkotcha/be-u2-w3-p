package org.emmek.beu2w3p.controller;

import org.emmek.beu2w3p.entities.Event;
import org.emmek.beu2w3p.exceptions.BadRequestException;
import org.emmek.beu2w3p.payloads.EventPostDTO;
import org.emmek.beu2w3p.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/events")
public class EventController {
    @Autowired
    private EventService eventService;

    @GetMapping("")
    public Page<Event> getEvents(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 @RequestParam(defaultValue = "id") String sort) {
        return eventService.findAll(page, size, sort);
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Event createEvent(@RequestBody EventPostDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        } else {
            return eventService.save(body);
        }
    }

    @GetMapping("/{id}")
    public Event getEventById(@PathVariable long id) {
        return eventService.findById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Event updateEventById(@PathVariable long id, @RequestBody EventPostDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        } else {
            return eventService.updateById(id, body);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteEventById(@PathVariable long id) {
        eventService.deleteById(id);
    }

    @PostMapping("/{id}/picture")
    public String uploadExample(@PathVariable long id, @RequestParam("picture") MultipartFile body) throws IOException {
        return eventService.uploadPicture(id, body);
    }


}
