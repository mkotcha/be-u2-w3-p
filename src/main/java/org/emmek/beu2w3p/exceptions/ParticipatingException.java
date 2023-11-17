package org.emmek.beu2w3p.exceptions;

import org.emmek.beu2w3p.entities.Event;
import org.emmek.beu2w3p.entities.User;

public class ParticipatingException extends RuntimeException {
    public ParticipatingException(String message) {
        super(message);
    }

    public ParticipatingException(Event event, User user) {
        super("User " + user.getName() + " is already participating in event " + event.getTitle());
    }
}
