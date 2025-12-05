package vn.vennhuu.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.vennhuu.jobhunter.domain.Permission;
import vn.vennhuu.jobhunter.domain.response.ResultPaginationDTO;
import vn.vennhuu.jobhunter.service.PermissionService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import vn.vennhuu.jobhunter.util.annotation.APIMessage;
import vn.vennhuu.jobhunter.util.error.InvalidException;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.turkraft.springfilter.boot.Filter;

@RestController
@RequestMapping("/api/v1")
public class PermissionController {

    private final PermissionService permissionService ;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/permissions")
    @APIMessage("Create a new permission")
    public ResponseEntity<Permission> handleCreatePermission( @RequestBody Permission permissionPostMan) throws InvalidException{

        if ( this.permissionService.existsByApiPathAndMethodAndModule(
            permissionPostMan.getApiPath(), 
            permissionPostMan.getMethod(), 
            permissionPostMan.getModule())){
                throw new InvalidException("Permission đã tồn tại") ;
        }
        Permission permission = this.permissionService.handleSavePermission(permissionPostMan) ;
        return ResponseEntity.status(HttpStatus.CREATED).body(permission) ;
    }

    @PutMapping("/permissions")
    @APIMessage("Update a permission")
    public ResponseEntity<Permission> handleUpdatePermission (@RequestBody Permission permissionPostMan)throws InvalidException {
        Permission updPermission = this.permissionService.getPermissionById(permissionPostMan.getId()) ;
        if ( !this.permissionService.existsById(permissionPostMan.getId()) ) {
            if ( this.permissionService.isSameName(updPermission))
            throw new InvalidException("Permission đã tồn tại") ;
        }
        if ( this.permissionService.existsByApiPathAndMethodAndModule(
            permissionPostMan.getApiPath(),
            permissionPostMan.getMethod(),
            permissionPostMan.getModule())){
                throw new InvalidException("Permission đã tồn tại") ;
        }
        
        return ResponseEntity.status(HttpStatus.CREATED).body(this.permissionService.handleUpdatePermission(permissionPostMan, updPermission)) ;
    }

    @GetMapping("/permissions")
    @APIMessage("Get all permission")
    public ResponseEntity<ResultPaginationDTO> handleGetAllPermission( @Filter Specification<Permission> spec , Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(this.permissionService.getAllPermission(spec, pageable));
    }

    @DeleteMapping("/permissions/{id}")
    @APIMessage("Delete a permission")
    public ResponseEntity<Permission> handleDeletePermission( @PathVariable("id") long id) throws InvalidException {
        if ( !this.permissionService.existsById(id)) {
            throw new InvalidException("Không tồn tại id này") ;
        }
        this.permissionService.handleDeleteAPermissionById(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
    

}
