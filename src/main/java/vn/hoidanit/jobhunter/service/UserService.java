package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.Meta;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository ; 
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser (User user) {
        return this.userRepository.save(user) ;
    }

    public void deleteUser(long id) {
        this.userRepository.deleteById(id);
    }

    public User getUserById (long id) {
        Optional<User> userOptional = this.userRepository.findById(id) ; 
        if ( userOptional.isPresent() ) {
            return userOptional.get() ;
        }
        return null ;
    }

    public ResultPaginationDTO getAllUser (Specification<User> spec , Pageable pageable) {
        Page<User> pageUser = this.userRepository.findAll(spec , pageable) ;
        ResultPaginationDTO rs = new ResultPaginationDTO() ;
        Meta mt = new Meta() ;

        mt.setPage(pageable.getPageNumber() + 1); // đg ở trang bnhiu
        mt.setPageSize(pageable.getPageSize()); // lấy tối đa bnhiu phần tử

        mt.setPages(pageUser.getTotalPages()); // tổng số trang
        mt.setTotal(pageUser.getTotalElements()); // tổng số phần tử

        rs.setMeta(mt);
        rs.setResult(pageUser.getContent());
        return rs ;
    }

    public User getUserByEmail( String email ) {
        return this.userRepository.findUserByEmail(email) ;
    }

    
}
