package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecification;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import com.turkraft.springfilter.parser.FilterParser;
import com.turkraft.springfilter.parser.node.FilterNode;

import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResCreateResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import vn.hoidanit.jobhunter.repository.ResumeRepository;
import vn.hoidanit.jobhunter.util.SecurityUtil;

@Service
public class ResumeService {

    @Autowired
    FilterBuilder fb;

    @Autowired
    private FilterParser filterParser;

    @Autowired
    private FilterSpecificationConverter filterSpecificationConverter;


    private final ResumeRepository resumeRepository ;

    public ResumeService(ResumeRepository resumeRepository) {
        this.resumeRepository = resumeRepository;
    }

    public Resume save( Resume resume) {
        return this.resumeRepository.save(resume) ;
    }

    public Resume findById( long id ) {
        Optional<Resume> optionalResume = this.resumeRepository.findById(id) ; 
        if ( optionalResume.isPresent() ) {
            return optionalResume.get() ;
        }
        return null ;
    }

    public ResCreateResumeDTO createResume( Resume resume) {
        ResCreateResumeDTO res = new ResCreateResumeDTO() ;
        res.setId(resume.getId());
        res.setCreatedAt(resume.getCreatedAt());
        res.setCreatedBy(resume.getCreatedBy());
        return res ;
    }

    public ResUpdateResumeDTO updateResume( Resume resumePostMan ) {
        ResUpdateResumeDTO res = new ResUpdateResumeDTO() ;

        res.setUpdatedAt(resumePostMan.getUpdatedAt());
        res.setUpdatedBy(resumePostMan.getUpdatedBy());

        return res ;
    }

    public boolean existsById( long id ) {
        return this.resumeRepository.existsById(id) ;
    }

    public void deleteResumeById( long id ) {
        this.resumeRepository.deleteById(id) ;
    }

    public ResResumeDTO getResumeConvertResumeDTOById( Resume resume ) {

        ResResumeDTO rs = new ResResumeDTO() ;
        ResResumeDTO.UserResume userResume = new ResResumeDTO.UserResume() ;
        ResResumeDTO.JobResume jobResume = new ResResumeDTO.JobResume() ;

        rs.setId(resume.getId());
        rs.setEmail(resume.getEmail());
        rs.setUrl(resume.getUrl());
        rs.setStatus(resume.getStatus().toString());
        rs.setCreatedAt(resume.getCreatedAt());
        rs.setUpdatedAt(resume.getUpdatedAt());
        rs.setCreatedBy(resume.getCreatedBy());
        rs.setUpdatedBy(resume.getUpdatedBy());
        if ( resume.getUser() != null ) {
            userResume.setId(resume.getUser().getId());
            userResume.setName(resume.getUser().getName());
        }
        if ( resume.getJob() != null ) {
            rs.setCompany(resume.getJob().getCompany().getName());
            jobResume.setId(resume.getJob().getId());
            jobResume.setName(resume.getJob().getName());
        }
        rs.setUserResume(userResume);
        rs.setJobResume(jobResume);

        return rs;
    }

    public ResultPaginationDTO getAllResume(Specification<Resume> spec , Pageable pageable) {
        Page<Resume> pageResume = this.resumeRepository.findAll(spec, pageable) ;
        ResultPaginationDTO rs = new ResultPaginationDTO() ;
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta() ;

        mt.setPage(pageable.getPageNumber() + 1); // đg ở trang bnhiu
        mt.setPageSize(pageable.getPageSize()); // lấy tối đa bnhiu phần tử

        mt.setPages(pageResume.getTotalPages()); // tổng số trang
        mt.setTotal(pageResume.getTotalElements()); // tổng số phần tử

        List<ResResumeDTO> listDTO= pageResume.getContent()
                                    .stream()
                                    .map(resume -> this.getResumeConvertResumeDTOById(resume))
                                    .collect(Collectors.toList());

        rs.setMeta(mt);
        rs.setResult(listDTO);
        return rs ;
    }

    public ResultPaginationDTO fetchResumeByUser(Pageable pageable) {
        // query builder
        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        FilterNode node = filterParser.parse("email='" + email + "'");
        FilterSpecification<Resume> spec = filterSpecificationConverter.convert(node);
        Page<Resume> pageResume = this.resumeRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageResume.getTotalPages());
        mt.setTotal(pageResume.getTotalElements());

        rs.setMeta(mt);

        // remove sensitive data
        List<ResResumeDTO> listResume = pageResume.getContent()
                .stream()
                .map(item -> this.getResumeConvertResumeDTOById(item))
                .collect(Collectors.toList());

        rs.setResult(listResume);

        return rs;
    }
}
