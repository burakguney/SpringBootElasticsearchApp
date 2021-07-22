package com.example.elastic.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import com.example.elastic.entity.User;
import com.example.elastic.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
class UserController {

    private final UserRepository repository;

    @PostConstruct
    public void init() {
        User user = new User();
        user.setId("K0001");
        user.setName("Burak");
        user.setSurname("Güney");
        user.setAddress("Edirne");

        repository.save(user);

        User user1 = new User();
        user1.setId("K0002");
        user1.setName("Beyza");
        user1.setSurname("Güney");
        user1.setAddress("Edirne");

        repository.save(user1);

        User user2 = new User();
        user2.setId("K0003");
        user2.setName("Mete");
        user2.setSurname("Güney");
        user2.setAddress("Edirne");

        repository.save(user2);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        try {
            List<User> items = new ArrayList<>();

            repository.findAll().forEach(items::add);

            if (items.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(items, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<User> getById(@PathVariable("id") String id) {
        Optional<User> existingItemOptional = repository.findById(id);

        if (existingItemOptional.isPresent()) {
            return new ResponseEntity<>(existingItemOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/elastic/{search}")
    public ResponseEntity<List<User>> getUser(@PathVariable String search) {
        List<User> users = repository.findByNameLikeOrSurnameLike(search, search);
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User item) {
        try {
            User savedItem = repository.save(item);
            return new ResponseEntity<>(savedItem, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<User> update(@PathVariable("id") String id, @RequestBody User item) {
        Optional<User> existingItemOptional = repository.findById(id);
        if (existingItemOptional.isPresent()) {
            User existingItem = existingItemOptional.get();
            System.out
                    .println("TODO for developer - update logic is unique to entity and must be implemented manually.");
            // existingItem.setSomeField(item.getSomeField());
            return new ResponseEntity<>(repository.save(existingItem), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") String id) {
        try {
            repository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }
}
