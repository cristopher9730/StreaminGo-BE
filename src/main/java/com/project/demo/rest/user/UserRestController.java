package com.project.demo.rest.user;
import com.project.demo.logic.entity.passwordResetRequests.PasswordResetRequest;
import com.project.demo.logic.entity.passwordResetRequests.PasswordResetRequestRepository;
import com.project.demo.logic.entity.rol.Role;
import com.project.demo.logic.entity.rol.RoleEnum;
import com.project.demo.logic.entity.rol.RoleRepository;
import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserRestController {
    @Autowired
    private UserRepository UserRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordResetRequestRepository passwordResetRequestRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public List<User> getAllUsers() {
        return UserRepository.findAllUsersWithUserRole();
    }
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    @PostMapping
    public User addUser(@RequestBody User user) {
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.USER);

        if (optionalRole.isEmpty()) {
            return null;
        }

        var users = new User();
        user.setName(user.getName());
        user.setEmail(user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(optionalRole.get());

        return UserRepository.save(user);
    }
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return UserRepository.findById(id).orElseThrow(RuntimeException::new);
    }
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    @GetMapping("/filterByName/{name}")
    public ResponseEntity<List<User>> getUserByName(@PathVariable String name) {
        List<User> users = UserRepository.findUsersWithCharacterInName(name);
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return UserRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setName(user.getName());
                    existingUser.setLastname(user.getLastname());
                    existingUser.setEmail(user.getEmail());
                    return UserRepository.save(existingUser);
                })
                .orElseGet(() -> {
                    user.setId(id);
                    return UserRepository.save(user);
                });
    }
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        // Encontrar el usuario por ID
        User user = UserRepository.findById(id).orElse(null);

        // Obtener las solicitudes de restablecimiento de contraseña para el usuario
        List<PasswordResetRequest> resetRequests = passwordResetRequestRepository.findByUserId(id);

        // Eliminar las solicitudes de restablecimiento de contraseña
        for (PasswordResetRequest request : resetRequests) {
            passwordResetRequestRepository.delete(request);
        }

        // Eliminar el usuario
        UserRepository.deleteById(id);
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public User authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/me")
    public Optional<User> updateUser(@RequestBody User user) {

        return UserRepository.findById(user.getId())
                .map(existingUser -> {
                    existingUser.setName(user.getName());
                    existingUser.setLastname(user.getLastname());
                    return UserRepository.save(existingUser);
                });
    }
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/me")
    public Optional<User> updatePassword(@RequestBody User user){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User profile = (User) authentication.getPrincipal();

        return UserRepository.findById(profile.getId()).map(existingUser -> {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            return UserRepository.save(existingUser);
        });
    }

    @GetMapping("/count-by-month")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getUserCountByMonth() {
        List<Object[]> results = UserRepository.countUsersByMonth();
        
        // Convert the results to a List of Maps
        List<Map<String, Object>> userCounts = results.stream()
                .map(record -> {
                    if (record.length >= 3) {  // Ensure the array has at least 3 elements
                        return Map.of(
                                "year", record[0],
                                "month", record[1],
                                "count", record[2]
                        );
                    } else {
                        return Map.of(
                                "year", null,
                                "month", null,
                                "count", null
                        );
                    }
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(userCounts);
    }
}


