package vn.vennhuu.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.vennhuu.jobhunter.domain.Skill;
import vn.vennhuu.jobhunter.domain.response.ResultPaginationDTO;
import vn.vennhuu.jobhunter.repository.SkillRepository;

@Service
public class SkillService {

    private final SkillRepository skillRepository ;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public Skill handleSaveSkill (Skill skill) {
        return this.skillRepository.save(skill) ;
    }
    
    public boolean existByName(String name) {
        return this.skillRepository.existsByName(name) ;
    }
    
    public Skill handleUpdateSkill ( Skill skill ) {
        Skill updSkill = this.findById(skill.getId()) ;
        if ( updSkill != null) {
            updSkill.setName(skill.getName()) ;
            this.handleSaveSkill(updSkill) ;
        }

        return updSkill ;
    }

    public Skill findById( long id ) {
        Optional<Skill> skill = this.skillRepository.findById(id) ;
        if ( skill.isPresent() ) {
            return skill.get() ;
        }
        return null ;
    }

    public boolean existById(long id) {
        return this.skillRepository.existsById(id) ;
    }

    public ResultPaginationDTO getAllSkill(Specification<Skill> spec , Pageable pageable) {
        Page<Skill> pageSkill = this.skillRepository.findAll(spec, pageable) ;
        ResultPaginationDTO rs = new ResultPaginationDTO() ;
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta() ;

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageSkill.getTotalPages());
        mt.setTotal(pageSkill.getTotalElements());

        List<Skill> allSkill = this.skillRepository.findAll() ;

        rs.setMeta(mt);
        rs.setResult(allSkill);
        return rs ;
    }

    public void deleteById( long id ) {
        Optional<Skill> optionalSkill = this.skillRepository.findById(id) ;
        Skill currentSkill = optionalSkill.get() ;
        currentSkill.getJobs().forEach(job -> job.getSkills().remove(currentSkill));

        this.skillRepository.delete(currentSkill);
    }
    
   
}
