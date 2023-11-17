package org.emmek.beu2w3p.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Cloudinary cloudinary;

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
    
    public Page<Event> findAll(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return eventRepository.findAll(pageable);
    }

    public String uploadPicture(long id, MultipartFile body) throws IOException {
        Event event = eventRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
        String url = (String) cloudinary.uploader().upload(body.getBytes(), ObjectUtils.emptyMap()).get("public_id");
        event.setPicture(url);
        eventRepository.save(event);
        return url;
    }

    public Event updateById(long id, EventPostDTO body) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
        event.setTitle(body.title());
        event.setDescription(body.description());
        event.setDate(LocalDate.parse(body.date()));
        event.setLocation(body.location());
        event.setMaxParticipants(body.maxParticipants());
        return eventRepository.save(event);
    }

    public void deleteById(long id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
        eventRepository.delete(event);
    }
}
