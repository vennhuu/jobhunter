package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.Meta;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.CompanyRepository;

@Service
public class CompanyService {
    
    private final CompanyRepository companyRepository ;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company saveCompany(Company company) {
        return this.companyRepository.save(company) ;
    }
    
    public ResultPaginationDTO getAllCompany(Specification<Company> spec , Pageable pageable) {
        Page<Company> pageCompany = this.companyRepository.findAll(spec , pageable) ;
        ResultPaginationDTO rs = new ResultPaginationDTO() ;
        Meta mt = new Meta() ;

        mt.setPage(pageCompany.getNumber() + 1); // đg ở trang bnhiu
        mt.setPageSize(pageCompany.getSize()); // lấy tối đa bnhiu phần tử

        mt.setPages(pageCompany.getTotalPages()); // tổng số trang
        mt.setTotal(pageCompany.getTotalElements()); // tổng số phần tử

        rs.setMeta(mt);
        rs.setResult(pageCompany.getContent());
        return rs ;
    }

    public Company getCompanyById(long id) {
        return this.companyRepository.getCompanyById(id) ;
    }

    public void deleteCompany( long id ) {
        this.companyRepository.deleteById(id);
    }
}
