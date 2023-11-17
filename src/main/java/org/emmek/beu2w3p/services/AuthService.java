package org.emmek.beu2w3p.services;

import org.emmek.beu2w3p.config.EmailSender;
import org.emmek.beu2w3p.entities.Role;
import org.emmek.beu2w3p.entities.User;
import org.emmek.beu2w3p.exceptions.BadRequestException;
import org.emmek.beu2w3p.exceptions.UnauthorizedException;
import org.emmek.beu2w3p.payloads.UserLoginDTO;
import org.emmek.beu2w3p.payloads.UserPostDTO;
import org.emmek.beu2w3p.reposittories.UserRepository;
import org.emmek.beu2w3p.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AuthService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    EmailSender emailSender;
    @Autowired
    private UserService usersService;
    @Autowired
    private JWTTools jwtTools;
    @Autowired
    private PasswordEncoder bcrypt;

    public String authenticateUser(UserLoginDTO body) {
        User user = usersService.findByEmail(body.email());
        if (bcrypt.matches(body.password(), user.getPassword())) {
            return jwtTools.createToken(user);
        } else {
            throw new UnauthorizedException("wrong password");
        }
    }

    public User registerUser(UserPostDTO body) throws IOException {
        userRepository.findByEmail(body.email()).ifPresent(a -> {
            throw new BadRequestException("User with email " + a.getEmail() + " already exists");
        });
        User user = new User();
        user.setPassword(bcrypt.encode(body.password()));
        user.setName(body.name());
        user.setSurname(body.surname());
        user.setEmail(body.email());
        user.setRole(Role.USER);
        User savedUser = userRepository.save(user);
        emailSender.sendRegistrationEmail(savedUser);
        return savedUser;
    }
}