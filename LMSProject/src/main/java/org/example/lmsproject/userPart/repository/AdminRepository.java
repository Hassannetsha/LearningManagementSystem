package org.example.lmsproject.userPart.repository;

import org.example.lmsproject.userPart.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin,Long> {
}
