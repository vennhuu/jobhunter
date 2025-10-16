package vn.hoidanit.jobhunter.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.error.InvalidException;


@RestController
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
    public ResponseEntity<List<User>> getAllUser() {
        List<User> allUser = this.userService.getAllUser();
        return ResponseEntity.status(HttpStatus.OK).body(allUser);
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
