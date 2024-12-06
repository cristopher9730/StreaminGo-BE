package com.project.demo.rest.session;

import com.project.demo.logic.entity.session.Session;
import com.project.demo.logic.entity.session.SessionRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import static com.project.demo.logic.helper.CodeHelper.generateResetCode;

@RestController
@RequestMapping("/sessions")
public class SessionController {

    @Autowired
    private SessionRespository sessionRespository;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'SUPER_ADMIN')")
    public List<Session> getAllSessions(){
       return sessionRespository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'SUPER_ADMIN')")
    public Session createSession(){
        Session newSession = new Session();
        String generatedSessionCode = generateResetCode(16);
        newSession.setSessionCode(generatedSessionCode);

        return sessionRespository.save(newSession);
    }
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteSession(@PathVariable Long id){
        sessionRespository.deleteById(id);
    }
}
