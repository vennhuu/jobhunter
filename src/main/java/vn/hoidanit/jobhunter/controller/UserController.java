package vn.hoidanit.jobhunter.controller;


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
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.ResCreateUserDTO;
import vn.hoidanit.jobhunter.domain.dto.FetchUserDTO;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.dto.ResUpdateUserDTO;
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
    @APIMessage("Create a new user")
    public ResponseEntity<ResCreateUserDTO> createNewUser(@Valid @RequestBody User postManUser) throws InvalidException {

        boolean existEmail = this.userService.existByEmail(postManUser.getEmail()) ;
        if ( existEmail ) {
            throw new InvalidException("Email " + postManUser.getEmail() + " đã tồn tại") ;
        }
        String hashPassWord = this.passwordEncoder.encode(postManUser.getPassword()) ; 
        postManUser.setPassword(hashPassWord);
        User user = this.userService.saveUser(postManUser);
        ResCreateUserDTO createUserDTO = this.userService.handleCreateUserDTO(user) ;
        return ResponseEntity.status(HttpStatus.CREATED).body(createUserDTO);
    }

    @DeleteMapping("/users/{id}")
    @APIMessage("Delete a user")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) throws InvalidException{
        if ( !this.userService.existsId(id) ) {
            throw new InvalidException("Id không tồn tại") ;
        }
        this.userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(null) ;
    } 

    @GetMapping("/users/{id}")
    @APIMessage("fetch user with ID")
    public ResponseEntity<FetchUserDTO> getUserById(@PathVariable("id") long id) throws InvalidException {
        
        User user = this.userService.getUserById(id) ;
        if ( user == null ) {
            throw new InvalidException("Không tồn tại người dùng này") ;
        }
        FetchUserDTO fetchUserByIdDTO = this.userService.fetchUserByIdDTO(user) ;
        return ResponseEntity.status(HttpStatus.OK).body(fetchUserByIdDTO);
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
    @APIMessage("Update a user")
    public ResponseEntity<ResUpdateUserDTO> updateUser( @RequestBody User postManUser) throws InvalidException {
        User updateUser = this.userService.getUserById(postManUser.getId()) ; 
        if ( !this.userService.existsId(postManUser.getId())) {
            throw new InvalidException("Id không tồn tại") ;
        }
        if ( updateUser!= null) {
            updateUser.setName(postManUser.getName());
            updateUser.setGender(postManUser.getGender());
            updateUser.setAge(postManUser.getAge());
            updateUser.setAddress(postManUser.getAddress());
            this.userService.saveUser(updateUser) ;
        }
        ResUpdateUserDTO updateUserDTO = this.userService.handleUpdateUserDTO(updateUser) ;
        
        return ResponseEntity.status(HttpStatus.OK).body(updateUserDTO);
    }
    
}
