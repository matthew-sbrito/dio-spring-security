package com.techsoft.api.authentication.service;

import com.techsoft.api.authentication.domain.Role;
import com.techsoft.api.authentication.repository.RoleRepository;
import com.techsoft.api.common.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService extends AbstractService<Role, Object> {

    @Autowired
    RoleService(RoleRepository roleRepository) {
        super(roleRepository, Role.class);
    }
}
