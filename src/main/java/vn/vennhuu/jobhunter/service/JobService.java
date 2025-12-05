package vn.vennhuu.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.vennhuu.jobhunter.domain.Company;
import vn.vennhuu.jobhunter.domain.Job;
import vn.vennhuu.jobhunter.domain.Skill;
import vn.vennhuu.jobhunter.domain.response.ResultPaginationDTO;
import vn.vennhuu.jobhunter.domain.response.job.ResCreateJobDTO;
import vn.vennhuu.jobhunter.domain.response.job.ResUpdateJobDTO;
import vn.vennhuu.jobhunter.repository.CompanyRepository;
import vn.vennhuu.jobhunter.repository.JobRepository;
import vn.vennhuu.jobhunter.repository.SkillRepository;

@Service
public class JobService {
    private final JobRepository jobRepository ;
    private final SkillRepository skillRepository ;
    private final CompanyRepository companyRepository ;

    public JobService(JobRepository jobRepository , SkillRepository skillRepository , CompanyRepository companyRepository) {
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository ;
        this.companyRepository = companyRepository ;
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

    public ResUpdateJobDTO handleUpdateConvertResUpdateJobDTO( Job j , Job jobInDB ) {
        if ( j.getSkills() != null ) {
            List<Long> reqSkill = j.getSkills()
                                    .stream()
                                    .map(x -> x.getId())
                                    .collect(Collectors.toList());
            List<Skill> dbSkill = this.skillRepository.findByIdIn(reqSkill) ;
            j.setSkills(dbSkill); 
            jobInDB.setSkills(dbSkill) ;
        }
        if ( j.getCompany() != null ) {
            Optional<Company> cOptional = this.companyRepository.findById(j.getCompany().getId()) ;
            if ( cOptional.isPresent()) {
                jobInDB.setCompany(cOptional.get()) ;
            }
        }
        // update correct info
        jobInDB.setName(j.getName());
        jobInDB.setSalary(j.getSalary());
        jobInDB.setQuantity(j.getQuantity());
        jobInDB.setLocation(j.getLocation());
        jobInDB.setLevel(j.getLevel());
        jobInDB.setStartDate(j.getStartDate());
        jobInDB.setEndDate(j.getEndDate());
        jobInDB.setActive(j.isActive());

        // update job
        Job currentJob = this.jobRepository.save(jobInDB);

        ResUpdateJobDTO res = new ResUpdateJobDTO() ;
        res.setId(currentJob.getId());
        res.setName(currentJob.getName());
        res.setLocation(currentJob.getLocation());
        res.setSalary(currentJob.getSalary());
        res.setQuantity(currentJob.getQuantity());
        res.setLevel(currentJob.getLevel());
        res.setStartDate(currentJob.getStartDate());
        res.setEndDate(currentJob.getEndDate());
        res.setId(currentJob.getId());
        res.setUpdatedAt(currentJob.getUpdatedAt());
        res.setUpdatedBy(currentJob.getUpdatedBy());
        res.setActive(currentJob.isActive());
        if( currentJob.getSkills() != null ) {
            List<String> listSkill = currentJob.getSkills()
                                        .stream()
                                        .map(x -> x.getName())
                                        .collect(Collectors.toList());
            res.setNameSkill(listSkill);
        }
        return res ;
    }
}
