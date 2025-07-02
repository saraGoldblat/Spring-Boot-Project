package com.example.recipes.repository;

import com.example.recipes.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository למשתמשים
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * מחפש משתמש לפי שם משתמש
     */
    Optional<User> findByUsername(String username);
    
    /**
     * מחפש משתמש לפי אימייל
     */
    Optional<User> findByEmail(String email);
    
    /**
     * בודק אם קיים משתמש עם שם משתמש זה
     */
    boolean existsByUsername(String username);
    
    /**
     * בודק אם קיים משתמש עם אימייל זה
     */
    boolean existsByEmail(String email);
    
    /**
     * מחפש משתמש לפי שם משתמש או אימייל
     */
    @Query("SELECT u FROM User u WHERE u.username = :usernameOrEmail OR u.email = :usernameOrEmail")
    Optional<User> findByUsernameOrEmail(@Param("usernameOrEmail") String usernameOrEmail);
    
    /**
     * מחפש משתמשים פעילים בלבד
     */
    @Query("SELECT u FROM User u WHERE u.isActive = true")
    java.util.List<User> findActiveUsers();
}
