package org.emmek.beu2w3p.services;

import org.emmek.beu2w3p.entities.Event;
import org.emmek.beu2w3p.entities.User;
import org.emmek.beu2w3p.exceptions.NotFoundException;
import org.emmek.beu2w3p.exceptions.ParticipatingException;
import org.emmek.beu2w3p.payloads.UserDTO;
import org.emmek.beu2w3p.reposittories.EventRepository;
import org.emmek.beu2w3p.reposittories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    EventRepository eventRepository;

    public Page<User> getUsers(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return userRepository.findAll(pageable);
    }

    public User findById(long id) throws NotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User with email " + email + " not found!"));
    }


    public User findByIdAndUpdate(long id, UserDTO body) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
        user.setEmail(body.email());
        user.setName(body.name());
        user.setSurname(body.surname());
        return userRepository.save(user);
    }

    public User setAdmin(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
        user.setRole("ADMIN");
        return userRepository.save(user);
    }

    public User setUser(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
        user.setRole("USER");
        return userRepository.save(user);
    }

    public Event bookEvent(User user, long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(eventId));
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

    public Event bookEvent(long userId, long eventId) {
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

    public Event unBookEvent(User user, long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(eventId));
        if (event.getUsers().contains(user)) {
            event.getUsers().remove(user);
            return eventRepository.save(event);
        } else {
            throw new ParticipatingException("NOT PARTICIPATING!");
        }
    }

    public Event unBookEvent(long userId, long eventId) {
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
