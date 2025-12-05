package vn.vennhuu.jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import vn.vennhuu.jobhunter.domain.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> , JpaSpecificationExecutor<Company> {
    public Company getCompanyById(long id) ;

    public boolean existsById(long id) ;
    
}
