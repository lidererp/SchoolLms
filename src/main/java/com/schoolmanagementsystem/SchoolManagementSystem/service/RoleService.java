package com.schoolmanagementsystem.SchoolManagementSystem.service;

import com.schoolmanagementsystem.SchoolManagementSystem.entity.Role;
import com.schoolmanagementsystem.SchoolManagementSystem.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleService {


    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role getRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));
    }

    @Transactional
    public Role updateRole(Long id, Role updatedRole) {
        Role role = getRoleById(id);
        role.setRoleName(updatedRole.getRoleName());
        return roleRepository.save(role);
    }

    @Transactional
    public void deleteRole(Long id) {
        Role role = getRoleById(id);
        roleRepository.delete(role);
    }


}
