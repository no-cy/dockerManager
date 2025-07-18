package com.example.dockermanager.application.user.service;

import com.example.dockermanager.application.auth.dto.SocialUserInfo;
import com.example.dockermanager.domain.user.entity.User;
import com.example.dockermanager.infrastructure.db.jpa.user.UserRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class UserService {

    UserRepository userRepository;

    public Long findOrCreateUser(SocialUserInfo userInfo) {
        Optional<User> user = userRepository.findByEmail(userInfo.getEmail());

        if (user.isPresent()) {
            return user.get().getUserId();
        } else {
            User newUser = new User(userInfo.getName(), userInfo.getEmail());
            userRepository.save(newUser);
            return newUser.getUserId();
        }
    }
}
