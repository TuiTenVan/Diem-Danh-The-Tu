package com.demo.iot.controller;

import com.demo.iot.dto.request.RoleRequest;
import com.demo.iot.dto.response.ApiResponse;
import com.demo.iot.dto.response.RoleResponse;
import com.demo.iot.service.IRoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("${api.prefix}/role")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    IRoleService roleService;

    @GetMapping("")
    @PreAuthorize("@requiredPermission.checkPermission('GET_ALL_ROLES')")
    public ResponseEntity<?> getAllRoles(@RequestParam(value = "page", defaultValue = "0") int page,
                                         @RequestParam(value = "size", defaultValue = "10") int size,
                                         @RequestParam(value = "name", required = false) String name) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RoleResponse> roleResponses = roleService.getAllRoles(name, pageable);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Roles retrieved successfully")
                .data(roleResponses)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{roleId}")
    @PreAuthorize("@requiredPermission.checkPermission('GET_ROLE')")
    public ResponseEntity<?> getRoleId(@PathVariable("roleId") Integer roleId) {
        RoleResponse roleResponse = roleService.getRoleId(roleId);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(roleResponse)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("")
    @PreAuthorize("@requiredPermission.checkPermission('CREATE_ROLE')")
    public ResponseEntity<?> createRole(@RequestBody RoleRequest roleRequest) {
        RoleResponse roleResponse = roleService.createRole(roleRequest);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .data(roleResponse.getId())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@requiredPermission.checkPermission('UPDATE_ROLE')")
    public ResponseEntity<?> updateRole(@PathVariable Integer id,
                                        @RequestBody RoleRequest roleRequest) {
        RoleResponse roleResponse = roleService.updateRole(id, roleRequest);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Role updated successfully")
                .data(roleResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{ids}")
    @PreAuthorize("@requiredPermission.checkPermission('DELETE_ROLES')")
    public ResponseEntity<?> deleteRole(@PathVariable("ids") List<Integer> ids) {
        roleService.deleteRole(ids);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data("success")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/assignment/{permissionIds}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> assignPermissions(@RequestParam Integer roleId,
                                               @PathVariable List<Integer> permissionIds) {
        RoleResponse roleResponse = roleService.assignPermissionToRole(roleId, permissionIds);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(roleResponse)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/unassignment/{permissionIds}")
    @PreAuthorize("@requiredPermission.checkPermission('UNASSIGN_PERMISSION_FROM_ROLE')")
    public ResponseEntity<?> unassignPermissions(@RequestParam Integer roleId,
                                                 @PathVariable List<Integer> permissionIds) {
        RoleResponse roleResponse = roleService.unassignPermissionFromRole(roleId, permissionIds);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(roleResponse)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
