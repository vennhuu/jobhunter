package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResCreateJobDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResUpdateJobDTO;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.SkillRepository;

@Service
public class JobService {
    private final JobRepository jobRepository ;
    private final SkillRepository skillRepository ;

    public JobService(JobRepository jobRepository , SkillRepository skillRepository) {
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository ;
    }
    
    public Job handleSaveJob( Job job) {
        return this.jobRepository.save(job) ;
    } 

    public Job findById ( long id ) {
        Optional<Job> optionalJob = this.jobRepository.findById(id) ; 
        if ( optionalJob.isPresent() ) {
            return optionalJob.get() ;
        }
        return null ;
    }

    public boolean existById(long id) {
        return this.jobRepository.existsById(id) ;
    }

    public Job handleSaveListJob( Job job ) {
        List<Skill> listSkill= job.getSkills()
                                    .stream()
                                    .map(skill -> this.skillRepository.findById(skill.getId()).orElse(null))
                                    .collect(Collectors.toList());
        job.setSkills(listSkill);
        return this.jobRepository.save(job);
    }

    public Job handleUpdate( Job job ) {
        Job updJob = this.findById(job.getId()) ;
        updJob.setId(job.getId());
        updJob.setName(job.getName());
        updJob.setLocation(job.getLocation());
        updJob.setSalary(job.getSalary());
        updJob.setQuantity(job.getQuantity());
        updJob.setLevel(job.getLevel());
        updJob.setDescription(job.getDescription());
        updJob.setActive(job.isActive());
        if ( job.getSkills() != null ) {
            List<Skill> listSkill= job.getSkills()
                                    .stream()
                                    .map(skill -> this.skillRepository.findById(skill.getId()).orElse(null))
                                    .collect(Collectors.toList());
            updJob.setSkills(listSkill);
        }
        return this.handleSaveJob(updJob) ;
    }

    public void handleDeleteJob( long id ) {
        this.jobRepository.deleteById(id);
    }

    public ResultPaginationDTO getAllJob( Specification<Job> spec , Pageable pageable ) {
        Page<Job> pageJob = this.jobRepository.findAll(spec, pageable) ;
        ResultPaginationDTO rs = new ResultPaginationDTO() ;
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta() ;

        mt.setPage(pageJob.getNumber() + 1 );
        mt.setPageSize(pageJob.getSize());

        mt.setPages(pageJob.getTotalPages());
        mt.setTotal(pageJob.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pageJob.getContent());
        return rs;

    }

    public ResCreateJobDTO handleSaveConvertResCreateJobDTO(Job j) {
        
        if ( j.getSkills() != null ) {
            List<Long> reqSkill = j.getSkills()
                                    .stream()
                                    .map(x -> x.getId())
                                    .collect(Collectors.toList());
            List<Skill> dbSkill = this.skillRepository.findByIdIn(reqSkill) ;
            j.setSkills(dbSkill); 
        }
        Job job = this.handleSaveJob(j) ;

        ResCreateJobDTO res = new ResCreateJobDTO() ;
        res.setId(job.getId());
        res.setName(job.getName());
        res.setLocation(job.getLocation());
        res.setSalary(job.getSalary());
        res.setQuantity(job.getQuantity());
        res.setLevel(job.getLevel());
        res.setStartDate(job.getStartDate());
        res.setEndDate(job.getEndDate());
        res.setId(job.getId());
        res.setCreatedAt(job.getCreatedAt());
        res.setCreatedBy(job.getCreatedBy());
        res.setActive(job.isActive());
        if( job.getSkills() != null ) {
            List<String> listSkill = job.getSkills()
                                        .stream()
                                        .map(x -> x.getName())
                                        .collect(Collectors.toList());
            res.setNameSkill(listSkill);
        }
        return res ;
    }

    public ResUpdateJobDTO handleUpdateConvertResUpdateJobDTO( Job j ) {
        if ( j.getSkills() != null ) {
            List<Long> reqSkill = j.getSkills()
                                    .stream()
                                    .map(x -> x.getId())
                                    .collect(Collectors.toList());
            List<Skill> dbSkill = this.skillRepository.findByIdIn(reqSkill) ;
            j.setSkills(dbSkill); 
        }
        Job job = this.handleSaveJob(j) ;

        ResUpdateJobDTO res = new ResUpdateJobDTO() ;
        res.setId(job.getId());
        res.setName(job.getName());
        res.setLocation(job.getLocation());
        res.setSalary(job.getSalary());
        res.setQuantity(job.getQuantity());
        res.setLevel(job.getLevel());
        res.setStartDate(job.getStartDate());
        res.setEndDate(job.getEndDate());
        res.setId(job.getId());
        res.setUpdatedAt(job.getUpdatedAt());
        res.setUpdatedBy(job.getUpdatedBy());
        res.setActive(job.isActive());
        if( job.getSkills() != null ) {
            List<String> listSkill = job.getSkills()
                                        .stream()
                                        .map(x -> x.getName())
                                        .collect(Collectors.toList());
            res.setNameSkill(listSkill);
        }
        return res ;
    }
}
