package com.project.demo.logic.entity.session;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRespository extends JpaRepository<Session, Long> {

}
