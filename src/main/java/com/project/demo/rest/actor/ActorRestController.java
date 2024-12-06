package com.project.demo.rest.actor;

import com.project.demo.logic.entity.actor.Actor;
import com.project.demo.logic.entity.actor.ActorRepository;
import com.project.demo.logic.entity.casting.Casting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/actors")
public class ActorRestController {

    @Autowired
    private ActorRepository ActorRepository;

    @GetMapping
    public List<Actor> getAllActors() {
        return ActorRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public Actor addActor(@RequestBody Actor actor) {
        return ActorRepository.save(actor);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public Actor getActorById(@PathVariable Long id) {
        return ActorRepository.findById(id).orElseThrow(RuntimeException::new);
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/filterByName/{name}")
    public List<Actor> getActorById(@PathVariable String name) {
        return ActorRepository.findActorsWithCharacterInName(name);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public Actor updateActor(@PathVariable Long id, @RequestBody Actor actor) {
        return ActorRepository.findById(id)
                .map(existingActor -> {
                    existingActor.setName(actor.getName());
                    existingActor.setLastname(actor.getLastname());
                    existingActor.setNationality(actor.getNationality());
                    existingActor.setBirth(actor.getBirth());
                    existingActor.setCasting(actor.getCasting());
                    return ActorRepository.save(existingActor);
                })
                .orElseGet(() -> {
                    actor.setId(id);
                    return ActorRepository.save(actor);
                });
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public void deleteActor(@PathVariable Long id) {
        ActorRepository.deleteById(id);
    }
}
