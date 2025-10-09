package vn.hoidanit.jobhunter.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.service.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
public class UserController {

    private final UserService userService ; 
    
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public User createNewUser(@RequestBody User postManUser) {

        User user = this.userService.saveUser(postManUser);
        return user ;
    }

    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        this.userService.deleteUser(id);
        return "deleteUser" ;
    }

    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable("id") long id) {
        User user = this.userService.getUserById(id) ;
        return user;
    }

    @GetMapping("/user")
    public List<User> getAllUser() {
        List<User> allUser = this.userService.getAllUser();
        return allUser;
    }
    @GetMapping("/a")
    public String getMethodName() {
        return "Hello world";
    }

    @PutMapping("/user")
    public User updateUser( @RequestBody User postManUser) {
        User updateUser = this.userService.getUserById(postManUser.getId()) ; 
        if ( updateUser!= null) {
            updateUser.setName(postManUser.getName());
            updateUser.setEmail(postManUser.getEmail());
            updateUser.setPassword(postManUser.getPassword());
            this.userService.saveUser(updateUser) ;
        }
        
        return updateUser;
    }
    
}
