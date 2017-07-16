package com.macro.dev.repositories;

import com.macro.dev.models.LutUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * LutUser repository for CRUD operations.
 */
public interface UserRepository extends JpaRepository<LutUser,Long> {
    LutUser findByUsername(String username);
}
