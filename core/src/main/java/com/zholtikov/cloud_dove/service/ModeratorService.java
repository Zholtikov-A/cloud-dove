package com.zholtikov.cloud_dove.service;

import com.zholtikov.cloud_dove.enums.UserStatus;

public interface ModeratorService {
    void changeStatus(String username, UserStatus status);
}
