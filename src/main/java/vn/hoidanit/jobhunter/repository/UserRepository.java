package vn.hoidanit.jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import vn.hoidanit.jobhunter.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User , Long> , JpaSpecificationExecutor<User>{
    public User findUserByEmail(String email) ;

    public boolean existsByEmail(String email);

    public boolean existsById( long id ) ;

    public User findByRefreshTokenAndEmail(String token , String email ) ;
    
}
