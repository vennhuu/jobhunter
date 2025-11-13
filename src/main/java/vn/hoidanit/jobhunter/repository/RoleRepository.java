package vn.hoidanit.jobhunter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import vn.hoidanit.jobhunter.domain.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> , JpaSpecificationExecutor<Role>{
    public List<Role> findByIdIn( List<Long> id) ;

    public boolean existsByName(String name ) ;

    public boolean existsById( long id) ;

    public Role findByName(String name);
}
