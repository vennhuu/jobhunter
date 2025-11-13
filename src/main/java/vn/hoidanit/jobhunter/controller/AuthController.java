package vn.hoidanit.jobhunter.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.request.ReqLoginDTO;
import vn.hoidanit.jobhunter.domain.response.ResLoginDTO;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.SecurityUtil;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import vn.hoidanit.jobhunter.domain.response.ResCreateUserDTO;
import vn.hoidanit.jobhunter.util.annotation.APIMessage;
import vn.hoidanit.jobhunter.util.error.InvalidException;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final PasswordEncoder passwordEncoder ; 
    @Value("${venn.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    public AuthController(UserService userService, AuthenticationManagerBuilder authenticationManagerBuilder,
            SecurityUtil securityUtil , PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.passwordEncoder = passwordEncoder ;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody ReqLoginDTO DTOUser) {
        // Nạp input gồm username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                DTOUser.getUsername(), DTOUser.getPassword());
        // xác thực người dùng => cần viết hàm loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResLoginDTO res = new ResLoginDTO();

        User user = this.userService.getUserByEmail(DTOUser.getUsername());

        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(user.getId(), user.getEmail(), user.getName() , user.getRole());
        res.setUser(userLogin);

        // create a token
        String access_token = this.securityUtil.createAccessToken(authentication.getName(), res);
        res.setAccess_token(access_token);

        // create refresh token
        String refresh_token = this.securityUtil.createRefreshToken(DTOUser.getUsername(), res);

        // update user
        this.userService.updateUserToken(refresh_token, DTOUser.getUsername());

        // set cookies
        ResponseCookie resCookies = ResponseCookie
                .from("refresh_token", refresh_token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, resCookies.toString())
                .body(res);
    }

    @GetMapping("/auth/account")
    @APIMessage("fetch account")
    public ResponseEntity<ResLoginDTO.UserGetAccount> getAccount() {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";

        User user = this.userService.getUserByEmail(email);

        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(user.getId(), user.getEmail() , user.getName() , user.getRole()) ;

        ResLoginDTO.UserGetAccount userGetAccount = new ResLoginDTO.UserGetAccount(userLogin) ;

        return ResponseEntity.ok(userGetAccount);
    }

    @GetMapping("/auth/refresh")
    @APIMessage("Get user by refresh token")
    public ResponseEntity<ResLoginDTO> getRefreshToken(
            @CookieValue(name = "refresh_token" , defaultValue = "abc") String refresh_token ) throws InvalidException {
        
        if ( refresh_token.equals("abc")) {
            throw new InvalidException("Bạn không có refresh_token lên cookies") ;
        } 
        // check valid
        Jwt decodedToken = this.securityUtil.checkValidRefreshToken(refresh_token) ;
        String email = decodedToken.getSubject() ;

        // check user by token + email
        User currentUser = this.userService.getUserByRefreshTokenAndEmail(refresh_token, email) ;
        if ( currentUser == null ) {
            throw new InvalidException("Refresh Token k hợp lệ") ;
        }
        
        ResLoginDTO res = new ResLoginDTO();

        User user = this.userService.getUserByEmail(email);

        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(user.getId(), user.getEmail(), user.getName() , user.getRole());
        res.setUser(userLogin);
        // create a token
        String access_token = this.securityUtil.createAccessToken(email, res);
        res.setAccess_token(access_token);

        // create refresh token
        String new_refresh_token = this.securityUtil.createRefreshToken(email, res);

        // update user
        this.userService.updateUserToken(refresh_token, email);

        // set cookies
        ResponseCookie resCookies = ResponseCookie
                .from("refresh_token", new_refresh_token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, resCookies.toString())
                .body(res);
    }

    @PostMapping("/auth/logout")
    @APIMessage("Logout User")
    public ResponseEntity<Void> handleLogout() throws InvalidException {
        
        String email = SecurityUtil.getCurrentUserLogin().orElse("");

        if ( email.equals("")) {
            throw new InvalidException("Access token k hợp lệ") ;
        }
        
        // update user
        this.userService.updateUserToken(null, email);

        ResponseCookie deleteCookie = ResponseCookie.from("refresh_token", "")
                .path("/")
                .httpOnly(true)
                .maxAge(0) // remove cookie immediately
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .build();
    }

    @PostMapping("/auth/register")
    public ResponseEntity<ResCreateUserDTO> handleLogout(@Valid @RequestBody User userPostMan) throws InvalidException {
        if ( this.userService.existByEmail(userPostMan.getEmail())) {
            throw new InvalidException ("Email đã tồn tại") ;
        }
        String hashPassword = this.passwordEncoder.encode(userPostMan.getPassword()) ;
        userPostMan.setPassword(hashPassword);

        User user = this.userService.saveUser(userPostMan) ;
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.handleCreateUserDTO(user));
    }
    
}
