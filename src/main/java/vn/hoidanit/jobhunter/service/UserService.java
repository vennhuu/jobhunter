package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.CreateUserDTO;
import vn.hoidanit.jobhunter.domain.dto.FetchUserDTO;
import vn.hoidanit.jobhunter.domain.dto.Meta;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.dto.UpdateUserDTO;
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

        List<FetchUserDTO> listDTO= pageUser.getContent()
                                    .stream()
                                    .map(user -> this.fetchUserByIdDTO(user))
                                    .collect(Collectors.toList());

        rs.setMeta(mt);
        rs.setResult(listDTO);
        return rs ;
    }

    public User getUserByEmail( String email ) {
        return this.userRepository.findUserByEmail(email) ;
    }

    public boolean existByEmail(String email) {
        return this.userRepository.existsByEmail(email) ;
    }

    public CreateUserDTO handleCreateUserDTO (User user) {
        CreateUserDTO createUserDTO = new CreateUserDTO() ;
        createUserDTO.setName(user.getName());
        createUserDTO.setEmail(user.getEmail());
        createUserDTO.setGender(user.getGender().toString());
        createUserDTO.setAddress(user.getAddress());
        createUserDTO.setAge(user.getAge());
        createUserDTO.setCreatedAt(user.getCreatedAt());
        return createUserDTO ;
    }

    public boolean existsId( long id) {
        return this.userRepository.existsById(id) ;
    }

    public UpdateUserDTO handleUpdateUserDTO (User user) {
        UpdateUserDTO updateUserDTO = new UpdateUserDTO() ;
        updateUserDTO.setId(user.getId());
        updateUserDTO.setName(user.getName());
        updateUserDTO.setGender(user.getGender().toString());
        updateUserDTO.setAddress(user.getAddress());
        updateUserDTO.setAge(user.getAge());
        updateUserDTO.setUpdatedAt(user.getUpdateAt());
        return updateUserDTO ;
    }
    
    public FetchUserDTO fetchUserByIdDTO ( User user ) {
        FetchUserDTO fetchUserByIdDTO = new FetchUserDTO() ; 
        fetchUserByIdDTO.setId(user.getId());
        fetchUserByIdDTO.setEmail(user.getEmail());
        fetchUserByIdDTO.setName(user.getName());
        fetchUserByIdDTO.setGender(user.getGender().toString());
        fetchUserByIdDTO.setAddress(user.getAddress());
        fetchUserByIdDTO.setAge(user.getAge());
        fetchUserByIdDTO.setUpdatedAt(user.getUpdateAt());
        fetchUserByIdDTO.setCreatedAt(user.getCreatedAt());
        return fetchUserByIdDTO ;
    }

    public void updateUserToken ( String token , String email ) {
        User user  = this.getUserByEmail(email) ;
        if ( user != null ) {
            user.setRefreshToken(token);
            this.saveUser(user) ;
        }
    }

    public User getUserByRefreshTokenAndEmail ( String token , String email ) {
        return this.userRepository.findByRefreshTokenAndEmail(token, email) ;
    }
}
