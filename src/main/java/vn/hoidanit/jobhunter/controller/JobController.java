package vn.hoidanit.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.service.JobService;
import vn.hoidanit.jobhunter.util.annotation.APIMessage;
import vn.hoidanit.jobhunter.util.error.InvalidException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResCreateJobDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResUpdateJobDTO;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/v1")
public class JobController {

    private final JobService jobService ;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }
    
    @PostMapping("/jobs")
    @APIMessage("Create a new job")
    public ResponseEntity<ResCreateJobDTO> createNewJob(@RequestBody Job postManJob) throws InvalidException{

        // List<Skill> listSkill = postManJob.getSkills() ;
        
        // postManJob.setSkills(listSkill)
        
        return ResponseEntity.status(HttpStatus.CREATED).body(this.jobService.handleSaveConvertResCreateJobDTO(postManJob));
    }

    @PutMapping("/jobs")
    @APIMessage("Update a job")
    public ResponseEntity<ResUpdateJobDTO> updateJob(@RequestBody Job postManJob) throws InvalidException {
        if ( !this.jobService.existById(postManJob.getId()) ) {
            throw new InvalidException("Id không tồn tại") ;
        }

        return ResponseEntity.status(HttpStatus.OK).body(this.jobService.handleUpdateConvertResUpdateJobDTO(postManJob)) ;
    }

    @DeleteMapping("/jobs/{id}")
    @APIMessage("Delete a job")
    public ResponseEntity<Void> deleteJob(@PathVariable("id") long id) throws InvalidException {
        if ( !this.jobService.existById(id) ) {
            throw new InvalidException("Id không tồn tại") ;
        }
        this.jobService.handleDeleteJob(id);
        
        return ResponseEntity.status(HttpStatus.OK).body(null) ;
    }
    
    @GetMapping("/jobs")
    @APIMessage("Get all jobs")
    public ResponseEntity<ResultPaginationDTO> getAllJob(@Filter Specification<Job> spec , Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(this.jobService.getAllJob(spec, pageable));
    }

    @GetMapping("/jobs/{id}")
    @APIMessage("Get job by id")
    public ResponseEntity<Job> getJobById(@PathVariable("id") long id) throws InvalidException {
        if ( !this.jobService.existById(id) ) {
            throw new InvalidException("Id không tồn tại") ;
        }
        
        return ResponseEntity.status(HttpStatus.OK).body(this.jobService.findById(id));
    }
    
    
  
}
