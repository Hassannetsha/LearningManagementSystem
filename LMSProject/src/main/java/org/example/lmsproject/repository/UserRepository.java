package org.example.lmsproject.repository;

import org.example.lmsproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// import java.util.List;
import java.util.Optional;

// import java.util.function.Function;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserById(Long id);

    Optional<User> findByUsername(String username);
}
