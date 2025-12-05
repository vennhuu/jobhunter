package vn.vennhuu.jobhunter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import vn.vennhuu.jobhunter.domain.Permission;
import vn.vennhuu.jobhunter.domain.Role;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> , JpaSpecificationExecutor<Permission> {

    public boolean existsByApiPathAndMethodAndModule(String apiPath , String method , String module) ;

    public boolean existsById( long id ) ;

    public List<Permission> findByIdIn( List<Long> id) ;
}
