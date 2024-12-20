package org.example.lmsproject.userPart.repository;

import org.example.lmsproject.userPart.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request,Long> {
    Request findByid(Long user_id);
}
