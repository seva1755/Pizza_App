package com.example.pizza.controller;

import com.example.pizza.entity.User;
import com.example.pizza.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Контроллер для приложения
 */
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    /**
     * Получение списка всех пользователей при логине с ролью администратора
     */
    @GetMapping("/admin")
    public List<User> findAll(){
        return userService.findAll();
    }

    /**
     * Получение пользователя по его id.
     */
    @GetMapping("/admin/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        Optional<User> userData = userService.findById(id);
            return userData.map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Главная страница пользователя. Возвращает его данные из базы
     */
    @GetMapping("/user")
    public ResponseEntity<User> getCurrentUserById() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Создание нового пользователя
     */
    @PostMapping("/admin")
    public ResponseEntity<User> createUser(@RequestBody User user){
        System.out.println(user.getRoles());
        try {
            User _user = userService.save(
                    new User(
                            user.getUsername(), encoder.encode(user.getPassword()),user.getFirstName(),
                            user.getLastName(), user.getAge(), user.getRoles()));
            return new ResponseEntity<>(_user, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Обновление пользователя по заданному id
     */
    @PutMapping("/admin/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        Optional<User> userData = userService.findById(id);
        if (userData.isPresent()) {
            User _user = userData.get();
            _user.setId(user.getId());
            _user.setUsername(user.getUsername());
            _user.setPassword(encoder.encode(user.getPassword()));
            _user.setFirstName(user.getFirstName());
            _user.setLastName(user.getLastName());
            _user.setAge(user.getAge());
            _user.setRoles(user.getRoles());
            return new ResponseEntity<>(userService.updateUser(_user), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Удаление пользователя по заданному id
     */
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<HttpStatus> deleteUserById(@PathVariable("id") long id){
        try {
            userService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
