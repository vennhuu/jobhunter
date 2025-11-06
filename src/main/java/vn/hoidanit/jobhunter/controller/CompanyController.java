package vn.hoidanit.jobhunter.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.CompanyService;
import vn.hoidanit.jobhunter.util.error.InvalidException;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import vn.hoidanit.jobhunter.util.annotation.APIMessage;

@RestController
@RequestMapping("/api/v1")
public class CompanyController {
    
    private final CompanyService companyService ;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    @APIMessage("create a new company")
    public ResponseEntity<Company> createCompany(@Valid @RequestBody Company postManCompany) {
        Company newCompany = this.companyService.saveCompany(postManCompany) ;
        return ResponseEntity.status(HttpStatus.CREATED).body(newCompany);
    }
    
    @GetMapping("/companies")
    @APIMessage("fetch all company")
    public ResponseEntity<ResultPaginationDTO> getAllCompany(
        @Filter Specification<Company> spec , 
        Pageable pageable
    ) {
        return ResponseEntity.ok(this.companyService.getAllCompany(spec , pageable));
    }

    @PutMapping("/companies")
    @APIMessage("update a company")
    public ResponseEntity<Company> updateCompany( @RequestBody Company postManCompany ) {
        //TODO: process PUT request
        Company updCompany = this.companyService.getCompanyById(postManCompany.getId()) ;
        if ( updCompany != null ) {
            updCompany.setName(postManCompany.getName());
            updCompany.setDescription(postManCompany.getDescription());
            updCompany.setAddress(postManCompany.getAddress());
            updCompany.setLogo(postManCompany.getLogo());
            this.companyService.saveCompany(updCompany) ;
        }
        return ResponseEntity.status(HttpStatus.OK).body(updCompany);
    }

    @DeleteMapping("/companies/{id}")
    @APIMessage("delete a company")
    public ResponseEntity<Void> deleteCompany (@PathVariable ("id") long id){
        this.companyService.deleteCompany(id);
        return ResponseEntity.ok(null) ;
    }


    
}
