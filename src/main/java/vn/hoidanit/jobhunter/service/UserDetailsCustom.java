package vn.hoidanit.jobhunter.service;

import java.util.Collections;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;



@Component("userDetailService")
public class UserDetailsCustom implements UserDetailsService{

    private final UserService userService ; 

    public UserDetailsCustom(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO Auto-generated method stub
        vn.hoidanit.jobhunter.domain.User user = this.userService.getUserByEmail(username) ;

        if ( user == null ) {
            throw new UsernameNotFoundException("Username/password không hợp lệ") ;
        }

        return new User (
            user.getEmail() ,
            user.getPassword() , 
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
    
}
