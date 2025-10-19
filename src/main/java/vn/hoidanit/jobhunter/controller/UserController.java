package vn.hoidanit.jobhunter.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.annotation.APIMessage;
import vn.hoidanit.jobhunter.util.error.InvalidException;


@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService ; 
    private final PasswordEncoder passwordEncoder ; 
    
    public UserController(UserService userService , PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder ;
    }

    @PostMapping("/users")
    public ResponseEntity<User> createNewUser(@RequestBody User postManUser) {

        String hashPassWord = this.passwordEncoder.encode(postManUser.getPassword()) ; 
        postManUser.setPassword(hashPassWord);
        User user = this.userService.saveUser(postManUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") long id) throws InvalidException{
        if ( id >= 1500 ) {
            throw new InvalidException("Id không hợp lệ") ;
        }
        this.userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body("Phuoc") ;
    } 

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") long id) {
        User user = this.userService.getUserById(id) ;
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping("/users")
    @APIMessage("fetch all users")
    public ResponseEntity<ResultPaginationDTO> getAllUser(
        @Filter Specification<User> spec , 
        Pageable pageable
    ) {

        return ResponseEntity.status(HttpStatus.OK).body(this.userService.getAllUser(spec , pageable));
    }

    @PutMapping("/users")
    public ResponseEntity<User> updateUser( @RequestBody User postManUser) {
        User updateUser = this.userService.getUserById(postManUser.getId()) ; 
        if ( updateUser!= null) {
            updateUser.setName(postManUser.getName());
            updateUser.setEmail(postManUser.getEmail());
            updateUser.setPassword(postManUser.getPassword());
            this.userService.saveUser(updateUser) ;
        }
        
        return ResponseEntity.status(HttpStatus.OK).body(updateUser);
    }
    
}
