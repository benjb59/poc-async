package fr.insee.pocasync.producer.controller;

import fr.insee.pocasync.producer.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(path = "/create")
    public String createUser(@RequestParam(required = false) String username) {
        return userService.createUser(username);
    }

    @GetMapping(path = "/all")
    public String getUsers() {
        return userService.queryUser().map(userEntity -> userEntity.getUsername()).collect(Collectors.joining(" : "));
    }
}
