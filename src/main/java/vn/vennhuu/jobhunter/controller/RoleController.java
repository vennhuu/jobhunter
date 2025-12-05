package vn.vennhuu.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.vennhuu.jobhunter.domain.Permission;
import vn.vennhuu.jobhunter.domain.Role;
import vn.vennhuu.jobhunter.domain.response.ResultPaginationDTO;
import vn.vennhuu.jobhunter.service.RoleService;
import vn.vennhuu.jobhunter.util.annotation.APIMessage;
import vn.vennhuu.jobhunter.util.error.InvalidException;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.turkraft.springfilter.boot.Filter;



@RestController
@RequestMapping("/api/v1")
public class RoleController {
    
    private final RoleService roleService ;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/roles")
    @APIMessage("Create a new role")
    public ResponseEntity<Role> handleCreatePermission( @RequestBody Role rolePostMan) throws InvalidException{

        if ( this.roleService.existsByName(rolePostMan.getName())) {
            throw new InvalidException("Tên role đã tồn tại") ;
        }

        Role role = this.roleService.handleCreateNewRole(rolePostMan) ;
        return ResponseEntity.status(HttpStatus.CREATED).body(role) ;
    }

    @PutMapping("/roles")
    @APIMessage("Update a role")
    public ResponseEntity<Role> handleUpdatePermission( @RequestBody Role rolePostMan) throws InvalidException {
        //TODO: process PUT request
        if ( !this.roleService.existsById(rolePostMan.getId())) {
            throw new InvalidException("Không tồn tại id này") ;
        }

        // if ( this.roleService.existsByName(rolePostMan.getName())) {
        //     throw new InvalidException("Tên role đã tồn tại") ;
        // }
        Role r = this.roleService.getRoleById(rolePostMan.getId()) ;
        Role updRole = this.roleService.handleUpdateRole(rolePostMan, r) ;
        return ResponseEntity.status(HttpStatus.OK).body(updRole) ;
    }

    @GetMapping("/roles")
    @APIMessage("Get all role")
    public ResponseEntity<ResultPaginationDTO> handleGetAllRole(@Filter Specification<Role> spec , Pageable pageable  ) {
        return ResponseEntity.status(HttpStatus.OK).body(this.roleService.getAllRole(spec, pageable)) ;
    }

    @DeleteMapping("/roles/{id}")
    @APIMessage("Delete a role")
    public ResponseEntity<Role> handleDeleteRole( @PathVariable("id") long id) throws InvalidException {
        if ( !this.roleService.existsById(id)) {
            throw new InvalidException("Không tồn tại id này") ;
        }
        this.roleService.handleDeleteRoleById(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
    

    @GetMapping("/roles/{id}")
    @APIMessage("Get role by id")
    public ResponseEntity<Role> handleGetRoleById(@PathVariable("id") long id) throws InvalidException {

        Role role = this.roleService.getRoleById(id) ;
        if ( role == null ) {
            throw new InvalidException("Không tồn tại role này") ;
        }

        return ResponseEntity.ok(role);
    }
    
}
