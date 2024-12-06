package com.project.demo.rest.auth;

import com.project.demo.logic.entity.auth.AuthenticationService;
import com.project.demo.logic.entity.auth.JwtService;
import com.project.demo.logic.entity.passwordResetRequests.PasswordResetRequest;
import com.project.demo.logic.entity.passwordResetRequests.PasswordResetRequestRepository;
import com.project.demo.logic.entity.rol.Role;
import com.project.demo.logic.entity.rol.RoleEnum;
import com.project.demo.logic.entity.rol.RoleRepository;
import com.project.demo.logic.entity.user.LoginResponse;
import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
import com.project.demo.logic.helper.CodeHelper;
import com.project.demo.logic.helper.EmailHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RequestMapping("/auth")
@RestController
public class AuthRestController {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    PasswordResetRequestRepository passwordResetRequestRepository;

    private final AuthenticationService authenticationService;
    private final JwtService jwtService;
    private final EmailHelper emailHelper;

    public AuthRestController(JwtService jwtService, AuthenticationService authenticationService, EmailHelper emailHelper) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.emailHelper = emailHelper;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody User user) {
        User authenticatedUser = authenticationService.authenticate(user);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        Optional<User> foundedUser = userRepository.findByEmail(user.getEmail());

        foundedUser.ifPresent(loginResponse::setAuthUser);

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.USER);

        if (optionalRole.isEmpty()) {
            return null;
        }
        user.setRole(optionalRole.get());
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/passwordResetRequest")
    public PasswordResetRequest addPasswordResetRequest(@RequestBody User user){

        Optional<User> foundedUser = userRepository.findByEmail(user.getEmail());

        if(foundedUser.isEmpty()){
            return null;
        }

        PasswordResetRequest passwordResetRequest = new PasswordResetRequest();
        passwordResetRequest.setResetCode(CodeHelper.generateResetCode(16));
        passwordResetRequest.setExpirationDate(LocalDateTime.now().plusMinutes(30));

        foundedUser.get().setPassword("");
        passwordResetRequest.setUser(foundedUser.get());

        PasswordResetRequest newPasswordResetRequest = passwordResetRequestRepository.save(passwordResetRequest);

        String url = "http://localhost:4200/resetPassword/" + newPasswordResetRequest.getResetCode();

        newPasswordResetRequest.setResetCode(null);

        emailHelper.sendEmail(foundedUser.get().getEmail(),"Cambio de contraseña","Para realizar el cambio de su contraseña ingrese al siguiente link: " + url);

        return newPasswordResetRequest;


    }

    @PostMapping("passwordReset/{code}")
    public User passwordReset(@PathVariable String code, @RequestBody User user) {
        Optional<PasswordResetRequest> foundRequest = passwordResetRequestRepository.findByResetCode(code);

        if(foundRequest.isEmpty())
            return null;

        if(foundRequest.get().isExpired()){
            return null;
        }

        return userRepository.findById(foundRequest.get().getUser().getId())
                .map(existingUser -> {
                    existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
                    User updateUser = userRepository.save(existingUser);
                    updateUser.setPassword("");
                    return updateUser;
                })
                .orElseGet(() -> null);
    }

}