package vn.hoidanit.jobhunter.controller;


import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import vn.hoidanit.jobhunter.service.SkillService;
import vn.hoidanit.jobhunter.util.annotation.APIMessage;
import vn.hoidanit.jobhunter.util.error.InvalidException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import com.turkraft.springfilter.boot.Filter;




@RestController
@RequestMapping("/api/v1")
public class SkillController {

    private final SkillService skillService ;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping("/skills")
    @APIMessage("Create a new skill")
    public ResponseEntity<Skill> createNewSkill(@Valid @RequestBody Skill postManSkill) throws InvalidException {
        String name = postManSkill.getName() ;
        if ( this.skillService.existByName(name)){
            throw new InvalidException("Đã tồn tại kĩ năng này") ;
        }
        Skill skill = this.skillService.handleSaveSkill(postManSkill) ;
        return ResponseEntity.status(HttpStatus.CREATED).body(skill) ;
    }

    @PutMapping("/skills")
    @APIMessage("Update a skill")
    public ResponseEntity<Skill> updateNewSkill(@Valid @RequestBody Skill postManSkill) throws InvalidException {

        if ( !this.skillService.existById(postManSkill.getId())) {
            throw new InvalidException("id không tồn tại") ;
        }
        String name = postManSkill.getName() ;
        if ( this.skillService.existByName(name)){
            throw new InvalidException("Đã tồn tại kĩ năng này") ;
        }

        Skill updSkill = this.skillService.handleUpdateSkill(postManSkill) ;
        return ResponseEntity.status(HttpStatus.OK).body(updSkill) ;
    }

    @DeleteMapping("/skills/{id}")
    @APIMessage("Delete a skill")
    public ResponseEntity<Void> deleteSkill(@PathVariable("id") long id) throws InvalidException {
        if ( !this.skillService.existById(id)) {
            throw new InvalidException("id không tồn tại") ;
        }
        this.skillService.deleteById(id) ;
        return ResponseEntity.status(HttpStatus.OK).body(null) ;
    }

    @GetMapping("/skills")
    @APIMessage("Get all skills")
    public ResponseEntity<ResultPaginationDTO> getAllSkill(@Filter Specification<Skill> spec , Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(this.skillService.getAllSkill(spec, pageable));
    }

    @GetMapping("/skills/{id}")
    public ResponseEntity<Skill> getSkillById(@PathVariable("id") long id) throws InvalidException {
        if ( !this.skillService.existById(id)) {
            throw new InvalidException("id không tồn tại") ;
        }
        Skill skill = this.skillService.findById(id) ;

        return ResponseEntity.status(HttpStatus.OK).body(skill) ; 
    }
    
    
    
}
