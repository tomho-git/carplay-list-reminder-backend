package com.carplayPackage.repository;

import com.carplayPackage.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByUsernameOrEmail(String username, String email);
    
    List<User> findByIsActiveOrderByCreatedAtDesc(Boolean isActive);
    
    @Query("SELECT u FROM User u WHERE u.firstName LIKE %:name% OR u.lastName LIKE %:name% OR u.username LIKE %:name%")
    List<User> findByNameContaining(@Param("name") String name);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
}