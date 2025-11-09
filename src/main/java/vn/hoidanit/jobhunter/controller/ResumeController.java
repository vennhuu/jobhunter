package vn.hoidanit.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResCreateResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import vn.hoidanit.jobhunter.service.ResumeService;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.annotation.APIMessage;
import vn.hoidanit.jobhunter.util.error.InvalidException;

import org.springframework.web.bind.annotation.PostMapping;

import vn.hoidanit.jobhunter.service.JobService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1")
public class ResumeController {
    
    private final ResumeService resumeService ;
    private final UserService userService ;
    private final JobService jobService ;

    public ResumeController(ResumeService resumeService , UserService userService , JobService jobService ) {
        this.resumeService = resumeService;
        this.userService = userService ;
        this.jobService = jobService ;
    }

    @PostMapping("/resumes")
    @APIMessage("Tạo mới CV")
        public ResponseEntity<ResCreateResumeDTO> createNewResume(@Valid @RequestBody Resume resumePostMan) throws InvalidException{

        if ( resumePostMan.getUser() == null || resumePostMan.getJob() == null) {
            throw new InvalidException("CV này không biết cụ thể là của ai ....") ;
        }
        if ( !this.userService.existsId(resumePostMan.getUser().getId()) ) {
            throw new InvalidException("Không tồn tại người dùng này") ;
        }

        if ( !this.jobService.existById(resumePostMan.getJob().getId()) ) {
            throw new InvalidException("Không tồn tại Job này") ;
        }


        this.resumeService.save(resumePostMan) ;
        ResCreateResumeDTO res = this.resumeService.createResume(resumePostMan) ;
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }


    @PutMapping("/resumes")
    @APIMessage("Update CV")
    public ResponseEntity<ResUpdateResumeDTO> updateResume(@RequestBody Resume resumePostMan) throws InvalidException {

        if ( !this.resumeService.existsById(resumePostMan.getId()) ) {
            throw new InvalidException("Không tồn tại CV này") ;
        }
        Resume rs = this.resumeService.findById(resumePostMan.getId()) ;

        rs.setId(resumePostMan.getId());

        if (resumePostMan.getEmail() != null) {
            rs.setEmail(resumePostMan.getEmail());
        }
        if (resumePostMan.getUrl() != null) {
            rs.setUrl(resumePostMan.getUrl());
        }
        if (resumePostMan.getStatus() != null) {
            rs.setStatus(resumePostMan.getStatus());
        }
        if (resumePostMan.getJob() != null) {
            rs.setJob(resumePostMan.getJob());
        }
        if (resumePostMan.getUser() != null) {
            rs.setUser(resumePostMan.getUser());
        }
        if (resumePostMan.getCreatedAt() != null) {
            rs.setCreatedAt(resumePostMan.getCreatedAt());
        }
        if (resumePostMan.getCreatedBy() != null) {
            rs.setCreatedBy(resumePostMan.getCreatedBy());
        }

        this.resumeService.save(rs) ;
        return ResponseEntity.status(HttpStatus.OK).body(this.resumeService.updateResume(rs));
    }

    @DeleteMapping("/resumes/{id}")
    @APIMessage("Delete a CV")
    public ResponseEntity<Void> deleteResume(@PathVariable("id") long id) throws InvalidException{
        if ( !this.resumeService.existsById(id)) {
            throw new InvalidException("Không tồn tại CV này") ;
        }
        this.resumeService.deleteResumeById( id ) ;
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/resumes/{id}")
    @APIMessage("Get CV by Id")
    public ResponseEntity<ResResumeDTO> getResumeById(@PathVariable("id") long id) throws InvalidException{
        Resume resume = this.resumeService.findById(id) ;
        if ( !this.resumeService.existsById(id)) {
            throw new InvalidException("Không tồn tại CV này") ;
        }
        ResResumeDTO rs = this.resumeService.getResumeConvertResumeDTOById(resume) ;

        return ResponseEntity.status(HttpStatus.OK).body(rs);
    }
    
    @GetMapping("/resumes")
    @APIMessage("Get All CV")
    public ResponseEntity<ResultPaginationDTO> getAllResumes(@Filter Specification<Resume> spec , Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(this.resumeService.getAllResume(spec, pageable));
    }
    
    @PostMapping("/resumes/by-user")
    @APIMessage("Get list resumes by user")
    public ResponseEntity<ResultPaginationDTO> fetchResumeByUser(Pageable pageable) {

        return ResponseEntity.ok().body(this.resumeService.fetchResumeByUser(pageable));
    }
}
