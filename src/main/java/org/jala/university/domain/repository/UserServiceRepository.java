package org.jala.university.domain.repository;

import org.jala.university.domain.entity.UserService;

import java.util.List;

public interface UserServiceRepository {

    List<UserService> findByUserId(int userId);

    void updateUserService(UserService service);

    void deleteUserService(
            Long userServiceId,
            Long userId
    );
}
