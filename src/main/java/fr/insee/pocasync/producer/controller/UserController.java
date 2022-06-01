package fr.insee.pocasync.producer.controller;

import fr.insee.pocasync.producer.domain.UserDTO;
import fr.insee.pocasync.producer.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(path = "/create")
    public Mono<String> createUser(@RequestParam String username) {
        return userService.createUser(username);
    }

    @GetMapping(path = "/all")
    public Flux<String> getUsers() {
        return userService.queryUser().map(UserDTO::toPrintableString);
    }
}
