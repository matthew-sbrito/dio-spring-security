package com.techsoft.api.authentication.repository;

import com.techsoft.api.authentication.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
