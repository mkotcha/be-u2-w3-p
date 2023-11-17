package org.emmek.beu2w3p.services;

import org.emmek.beu2w3p.entities.Event;
import org.emmek.beu2w3p.entities.User;
import org.emmek.beu2w3p.exceptions.NotFoundException;
import org.emmek.beu2w3p.exceptions.ParticipatingException;
import org.emmek.beu2w3p.payloads.EventPostDTO;
import org.emmek.beu2w3p.reposittories.EventRepository;
import org.emmek.beu2w3p.reposittories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;


    public Event save(EventPostDTO body) {
        Event event = new Event();
        event.setTitle(body.title());
        event.setDescription(body.description());
        event.setDate(LocalDate.parse(body.date()));
        event.setLocation(body.location());
        event.setMaxParticipants(body.maxParticipants());
        return eventRepository.save(event);
    }

    public Event findById(long id) {
        return eventRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    Page<Event> getEvents(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return eventRepository.findAll(pageable);
    }

    Page<Event> findByUsers(int page, int size, String sort, User user) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return eventRepository.findByUsers(user, pageable);
    }

    public Event bookEvent(long eventId, long userId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(eventId));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(userId));

        if (event.getUsers().contains(user)) {
            throw new ParticipatingException(event, user);
        } else {
            if (event.getUsers().size() < event.getMaxParticipants()) {
                event.getUsers().add(user);
                return eventRepository.save(event);
            } else {
                throw new ParticipatingException("SOLD OUT!");
            }
        }
    }

    public Event unBookEvent(long eventId, long userId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(eventId));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(userId));

        if (event.getUsers().contains(user)) {
            event.getUsers().remove(user);
            return eventRepository.save(event);
        } else {
            throw new ParticipatingException("NOT PARTICIPATING!");
        }
    }

  
}
