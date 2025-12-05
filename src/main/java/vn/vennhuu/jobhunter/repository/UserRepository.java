package vn.vennhuu.jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import vn.vennhuu.jobhunter.domain.Company;
import vn.vennhuu.jobhunter.domain.User;
import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User , Long> , JpaSpecificationExecutor<User>{
    public User findUserByEmail(String email) ;

    public boolean existsByEmail(String email);

    public boolean existsById( long id ) ;

    public User findByRefreshTokenAndEmail(String token , String email ) ;

    public List<User> findByCompany(Company company);

    // public Company findUserById( long id ) ; 
    
}
