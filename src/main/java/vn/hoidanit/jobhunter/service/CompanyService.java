package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.CompanyRepository;
import vn.hoidanit.jobhunter.repository.UserRepository;

@Service
public class CompanyService {
    
    private final CompanyRepository companyRepository ;
    private final UserRepository userRepository ;

    public CompanyService(CompanyRepository companyRepository , UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository ;
    }

    public Company saveCompany(Company company) {
        return this.companyRepository.save(company) ;
    }
    
    public ResultPaginationDTO getAllCompany(Specification<Company> spec , Pageable pageable) {
        Page<Company> pageCompany = this.companyRepository.findAll(spec , pageable) ;
        ResultPaginationDTO rs = new ResultPaginationDTO() ;
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta() ;

        mt.setPage(pageCompany.getNumber() + 1); // đg ở trang bnhiu
        mt.setPageSize(pageCompany.getSize()); // lấy tối đa bnhiu phần tử

        mt.setPages(pageCompany.getTotalPages()); // tổng số trang
        mt.setTotal(pageCompany.getTotalElements()); // tổng số phần tử

        rs.setMeta(mt);
        rs.setResult(pageCompany.getContent());
        return rs ;
    }

    public Company getCompanyById (long id) {
        Optional<Company> companyOptional = this.companyRepository.findById(id) ; 
        if ( companyOptional.isPresent() ) {
            return companyOptional.get() ;
        }
        return null ;
    }

    public void deleteCompany( long id ) {
        Optional<Company> optionalCompany = this.companyRepository.findById(id) ;
        if ( optionalCompany.isPresent() ) {
            Company com = optionalCompany.get() ;
            List<User> listUsers = this.userRepository.findByCompany(com) ; 
            this.userRepository.deleteAll(listUsers);
        }
        this.companyRepository.deleteById(id);
    }

    public boolean existById(long id ) {
        return this.companyRepository.existsById(id) ;
    }
}
