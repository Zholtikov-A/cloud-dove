package com.zholtikov.cloud_dove.dal;

import com.zholtikov.cloud_dove.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query(value = "SELECT user_id FROM clouddove.users WHERE username = ?1", nativeQuery = true)
    List<Long> getIdByUsername(String username);


    @Modifying
    @Query(value = "UPDATE clouddove.users SET status = ?2 WHERE user_id = ?1", nativeQuery = true)
    @Transactional
    void changeUserStatus(Long id, String status);

}

