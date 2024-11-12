package com.zholtikov.cloud_dove.service;

import com.zholtikov.cloud_dove.dto.UserInputDto;
import com.zholtikov.cloud_dove.enums.SortState;
import com.zholtikov.cloud_dove.enums.UserStatus;
import com.zholtikov.cloud_dove.model.PictureMeta;
import com.zholtikov.cloud_dove.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface UserService {
    User createUser(UserInputDto userInputDto);
    User findUserById(Long id);
    Long getUsersIdIfExist(String username);
}
