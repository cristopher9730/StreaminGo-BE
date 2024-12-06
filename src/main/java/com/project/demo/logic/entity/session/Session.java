package com.project.demo.logic.entity.session;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "session")
public class Session {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;
    @Column(nullable = false)
    private String sessionCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSessionCode() {
        return sessionCode;
    }

    public void setSessionCode(String invitationUrl) {
        this.sessionCode = invitationUrl;
    }
}
