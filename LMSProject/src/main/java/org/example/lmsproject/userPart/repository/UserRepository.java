package org.example.lmsproject.userPart.repository;

import org.example.lmsproject.userPart.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByid(Long id);

    Optional<User> findByusername(String username);
}
