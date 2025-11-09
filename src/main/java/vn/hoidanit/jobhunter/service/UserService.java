package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResCreateUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResUpdateUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository ; 
    private final CompanyService companyService ; 
    private final RoleService roleService ;
    
    public UserService(UserRepository userRepository , CompanyService companyService , RoleService roleService ) {
        this.userRepository = userRepository;
        this.companyService = companyService ;
        this.roleService = roleService ;
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
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta() ;

        mt.setPage(pageable.getPageNumber() + 1); // đg ở trang bnhiu
        mt.setPageSize(pageable.getPageSize()); // lấy tối đa bnhiu phần tử

        mt.setPages(pageUser.getTotalPages()); // tổng số trang
        mt.setTotal(pageUser.getTotalElements()); // tổng số phần tử

        List<ResUserDTO> listDTO= pageUser.getContent()
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

    public ResCreateUserDTO handleCreateUserDTO (User user) {
        ResCreateUserDTO createUserDTO = new ResCreateUserDTO() ;
        createUserDTO.setName(user.getName());
        createUserDTO.setEmail(user.getEmail());
        createUserDTO.setGender(user.getGender().toString());
        createUserDTO.setAddress(user.getAddress());
        createUserDTO.setAge(user.getAge());
        createUserDTO.setCreatedAt(user.getCreatedAt());
        if ( user.getCompany() != null ) {
            ResCreateUserDTO.CompanyUser company = new ResCreateUserDTO.CompanyUser() ;
            long id = user.getCompany().getId() ;
            Company com = this.companyService.getCompanyById(id) ;
            company.setId(id);
            company.setName(com.getName());
            createUserDTO.setCompany(company) ;
        }
        else {
            createUserDTO.setCompany(null);
        }

        if ( user.getRole() != null ) {
            ResCreateUserDTO.RoleUser role = new ResCreateUserDTO.RoleUser() ;
            long id = user.getRole().getId() ;
            Role r = this.roleService.getRoleById(id) ;
            role.setId(id);
            role.setName(r.getName());
            createUserDTO.setRole(role) ;
        }
        else {
            createUserDTO.setRole(null);
        }
        return createUserDTO ;
    }

    public boolean existsId( long id) {
        return this.userRepository.existsById(id) ;
    }

    public ResUpdateUserDTO handleUpdateUserDTO (User user) {
        ResUpdateUserDTO updateUserDTO = new ResUpdateUserDTO() ;
        updateUserDTO.setId(user.getId());
        updateUserDTO.setName(user.getName());
        updateUserDTO.setGender(user.getGender().toString());
        updateUserDTO.setAddress(user.getAddress());
        updateUserDTO.setAge(user.getAge());
        updateUserDTO.setUpdatedAt(user.getUpdatedAt());
        if( user.getCompany() != null ) {
            if ( !this.companyService.existById(user.getId())) {
                updateUserDTO.setCompany(null);
            }
            else {
                ResUpdateUserDTO.CompanyUser companyUser = new ResUpdateUserDTO.CompanyUser() ;
                long id = user.getCompany().getId() ;
                Company com = this.companyService.getCompanyById(id) ;
                companyUser.setId(id);
                companyUser.setName(com.getName());
                updateUserDTO.setCompany(companyUser);
            }
            
        }
        if( user.getRole() != null ) {
            ResUpdateUserDTO.RoleUser roleUser = new ResUpdateUserDTO.RoleUser() ;
            long id = user.getRole().getId() ;
            Role r = this.roleService.getRoleById(id) ;
            roleUser.setId(id);
            roleUser.setName(r.getName());
            updateUserDTO.setRole(roleUser);
        }
        return updateUserDTO ;
    }
    
    public ResUserDTO fetchUserByIdDTO ( User user ) {
        ResUserDTO fetchUserByIdDTO = new ResUserDTO() ; 
        fetchUserByIdDTO.setId(user.getId());
        fetchUserByIdDTO.setEmail(user.getEmail());
        fetchUserByIdDTO.setName(user.getName());
        fetchUserByIdDTO.setGender(user.getGender().toString());
        fetchUserByIdDTO.setAddress(user.getAddress());
        fetchUserByIdDTO.setAge(user.getAge());
        fetchUserByIdDTO.setUpdatedAt(user.getUpdatedAt());
        fetchUserByIdDTO.setCreatedAt(user.getCreatedAt());
        if (user.getCompany() != null) {
            ResUserDTO.CompanyUser com = new ResUserDTO.CompanyUser();
            com.setId(user.getCompany().getId());
            com.setName(user.getCompany().getName());
            fetchUserByIdDTO.setCompany(com);
        } else {
            fetchUserByIdDTO.setCompany(null);
        }

        if (user.getRole() != null) {
            ResUserDTO.RoleUser r = new ResUserDTO.RoleUser();
            r.setId(user.getRole().getId());
            r.setName(user.getRole().getName());
            fetchUserByIdDTO.setRole(r);
        } else {
            fetchUserByIdDTO.setRole(null);
        }
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
