package com.zholtikov.cloud_dove.service.impl;

import com.zholtikov.cloud_dove.dal.UserRepository;
import com.zholtikov.cloud_dove.enums.UserStatus;
import com.zholtikov.cloud_dove.kafka.producer.KafkaProducer;
import com.zholtikov.cloud_dove.service.ModeratorService;
import com.zholtikov.cloud_dove.service.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ModeratorServiceImpl implements ModeratorService {
    @Autowired
    KafkaProducer kafkaProducer;

    private final UserRepository userRepository;
    private final UserService userService;

    public ModeratorServiceImpl(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }
@Override
    public void changeStatus(String username, UserStatus status) {
        Long id = userService.getUsersIdIfExist(username);
        userRepository.changeUserStatus(id, status.toString());
    }

}
