package org.jala.university.domain.repository;

import org.jala.university.domain.entity.Service;
import java.util.List;

public interface ServiceRepository {

    List<Service> findByUserId(int userId);

    void updateUserService(Service service);
}
