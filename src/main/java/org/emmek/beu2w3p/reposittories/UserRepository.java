package org.emmek.beu2w3p.reposittories;

import org.emmek.beu2w3p.entities.Event;
import org.emmek.beu2w3p.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findById(long id);

    public Optional<User> findByEmail(String email);

    public Page<User> findByEvents(Event event, Pageable pageable);


}
