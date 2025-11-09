package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.PermissionRepository;
import vn.hoidanit.jobhunter.repository.RoleRepository;

@Service
public class RoleService {
    
    private final RoleRepository roleRepository ;
    private final PermissionRepository permissionRepository ;

    public RoleService(RoleRepository roleRepository , PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository ;
    }

    public Role getRoleById( long id ) {
        Optional<Role> roleOptional = this.roleRepository.findById(id) ;
        if ( roleOptional.isPresent()) {
            return roleOptional.get() ;
        }
        return null ;
    }
    
    public Role handleSaveRole(Role role) {
        return this.roleRepository.save(role) ;
    }

    public boolean existsByName( String name ) {
        return this.roleRepository.existsByName(name) ;
    }

    public Role handleCreateNewRole( Role role ) {
        if ( role.getPermissions() != null ) {
            List<Long> reqPermission = role.getPermissions()
                                    .stream()
                                    .map(x -> x.getId())
                                    .collect(Collectors.toList());
            List<Permission> dbPermission = this.permissionRepository.findByIdIn(reqPermission) ;
            
            role.setPermissions(dbPermission); 
        }
        Role r = this.handleSaveRole(role) ;
        return r ;
    }

    public boolean existsById( long id ) {
        return this.roleRepository.existsById(id) ;
    }

    public Role handleUpdateRole( Role role , Role roleInDB) {
        if ( role != null ) {
            roleInDB.setId(role.getId()) ;
            if ( role.getName() != null ) {
                roleInDB.setName(role.getName()) ;
            }
            if ( role.getDescription() != null ) {
                roleInDB.setDescription(role.getDescription()) ;
            }
            if ( role.isActive()) {
                roleInDB.setActive(role.isActive()) ;
            }
            if ( role.getCreatedAt() != null ) {
                roleInDB.setCreatedAt(role.getCreatedAt()) ;
            }
            if ( role.getCreatedBy() != null ) {
                roleInDB.setCreatedBy(role.getCreatedBy()) ;
            }
            if ( role.getPermissions() != null ) {
                List<Long> reqPermissions = role.getPermissions()
                                            .stream()
                                            .map(x -> x.getId())
                                            .collect(Collectors.toList()); 
                List<Permission> listPermission = this.permissionRepository.findByIdIn(reqPermissions) ;
                roleInDB.setPermissions(listPermission);
            }
            this.handleSaveRole(roleInDB) ;

        }
        return roleInDB ;
    }

    public ResultPaginationDTO getAllRole( Specification<Role> spec , Pageable pageable ) {
        Page<Role> pageRole = this.roleRepository.findAll(spec, pageable) ;
        ResultPaginationDTO rs = new ResultPaginationDTO() ;
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta() ;

        mt.setPage(pageRole.getNumber() + 1 );
        mt.setPageSize(pageRole.getSize());

        mt.setPages(pageRole.getTotalPages());
        mt.setTotal(pageRole.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pageRole.getContent());
        return rs;
    }

    public void handleDeleteRoleById( long id ) {
        this.roleRepository.deleteById(id);
    }
    
}
