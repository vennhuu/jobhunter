package vn.vennhuu.jobhunter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import vn.vennhuu.jobhunter.domain.Job;
import vn.vennhuu.jobhunter.domain.Skill;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> , JpaSpecificationExecutor<Job> {
    
    public boolean existsById(long id) ;

    List<Job> findBySkillsIn(List<Skill> skills);

}
