package vn.hoidanit.jobhunter.service;


import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.PermissionRepository;

@Service
public class PermissionService {
    
    private final PermissionRepository permissionRepository ;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public Permission getPermissionById( long id ) {
        Optional<Permission> optionalPermission = this.permissionRepository.findById(id) ; 
        if ( optionalPermission.isPresent()) {
            return optionalPermission.get() ;
        }
        return null ;
    }

    public Permission handleSavePermission( Permission permission ) {
        return this.permissionRepository.save(permission) ;
    }

    public boolean existsByApiPathAndMethodAndModule(String apiPath , String method , String module) {
        return this.permissionRepository.existsByApiPathAndMethodAndModule(apiPath , method , module) ;
    }

    public boolean existsById( long id ) {
        return this.permissionRepository.existsById(id) ;
    }

    public Permission handleUpdatePermission(Permission permission , Permission permissionInDB) {

        if ( permission != null ) {

            permissionInDB.setId(permission.getId());
            if ( permission.getName() != null ) {
                permissionInDB.setName(permission.getName());
            }
            if ( permission.getApiPath() != null ) {
                permissionInDB.setApiPath(permission.getApiPath());
            }
            if ( permission.getMethod() != null ) {
                permissionInDB.setMethod(permission.getMethod());
            }
            if ( permission.getModule() != null ) {
                permissionInDB.setModule(permission.getModule());
            }
            if ( permission.getCreatedAt() != null ) {
                permissionInDB.setCreatedAt(permission.getCreatedAt());
            }
            if ( permission.getCreatedBy() != null ) {
                permissionInDB.setCreatedBy(permission.getCreatedBy());
            }

            this.handleSavePermission(permissionInDB) ;
        }
        return permissionInDB ;
    }

    public ResultPaginationDTO getAllPermission (Specification<Permission> spec , Pageable pageable ) {
        Page<Permission> pagePermission = this.permissionRepository.findAll(spec, pageable) ;
        ResultPaginationDTO rs = new ResultPaginationDTO() ;
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta() ;

        mt.setPage(pagePermission.getNumber() + 1 );
        mt.setPageSize(pagePermission.getSize());

        mt.setPages(pagePermission.getTotalPages());
        mt.setTotal(pagePermission.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pagePermission.getContent());
        return rs;

    }

    public void handleDeleteAPermissionById ( long id ) {
        Optional<Permission> optionalPermission = this.permissionRepository.findById(id) ;
        Permission currentPermission = optionalPermission.get() ;
        currentPermission.getRoles().forEach(role -> role.getPermissions().remove(currentPermission));

        this.permissionRepository.delete(currentPermission);
    }

    public boolean isSameName(Permission p) {
        Permission permissionDB = this.getPermissionById(p.getId()) ;
        if ( permissionDB != null ) {
            if ( permissionDB.getName().equals(p.getName())) {
                return true ;
            }
        }
        return false;
    }
}
