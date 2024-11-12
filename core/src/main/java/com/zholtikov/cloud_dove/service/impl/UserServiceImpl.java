package com.zholtikov.cloud_dove.service.impl;

import com.zholtikov.cloud_dove.dal.UserRepository;
import com.zholtikov.cloud_dove.dto.EMailDto;
import com.zholtikov.cloud_dove.dto.UserInputDto;
import com.zholtikov.cloud_dove.enums.UserRole;
import com.zholtikov.cloud_dove.enums.UserStatus;
import com.zholtikov.cloud_dove.exception.EntityNotFoundException;
import com.zholtikov.cloud_dove.kafka.producer.KafkaProducer;
import com.zholtikov.cloud_dove.model.User;
import com.zholtikov.cloud_dove.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    KafkaProducer kafkaProducer;

    @Override
    public User createUser(UserInputDto userInputDto) {
        String hashedPassword = passwordEncoder.encode(userInputDto.getPassword());
        User newUser = new User(userInputDto.getEmail(), userInputDto.getUsername(), hashedPassword, userInputDto.getName(), UserStatus.ACTIVE, UserRole.USER);

        EMailDto email = new EMailDto();
        email.setTo(userInputDto.getEmail());
        email.setSubject("Welcome to Cloud Dove service!");
        email.setContent("Dear " + userInputDto.getName() + ", we're pleased to welcome you at Cloud Dove service!");
        kafkaProducer.sendEMail("mail-topic", email);
        return userRepository.save(newUser);
    }

    @Override
    public User findUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            log.info("Not found: users's id " + id);
            throw new EntityNotFoundException("Not found: users's id " + id);
        }
        return optionalUser.get();
    }

    @Override
    public Long getUsersIdIfExist(String username) {
        List<Long> userIds = userRepository.getIdByUsername(username);
        if (userIds.isEmpty()) {
            log.info("Not found: users's name " + username);
            throw new EntityNotFoundException("Not found: users's name " + username);
        }
        return userIds.getFirst();
    }


}
