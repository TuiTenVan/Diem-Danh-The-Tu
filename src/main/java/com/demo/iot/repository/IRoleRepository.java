package com.demo.iot.repository;

import com.demo.iot.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<Role,Integer> {
    Optional<Role> findRoleByName(String roleName);

    @Query("SELECT r FROM Role r " +
            "WHERE LOWER(r.name) " +
            "LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Role> findRole(@Param("name") String name, Pageable pageable);
}