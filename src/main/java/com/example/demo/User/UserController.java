package com.example.demo.User;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/user")
public class UserController {
    @Autowired private UserRepository repo;

    @NoArgsConstructor
    @AllArgsConstructor
    private class Response {
        public enum Code {
            OK,
            USER_NOT_FOUND,
            USER_ALREADY_EXISTS
        }
        public Code code;
        public String msg;
        public Optional<User> user;

        public Response UserNotFound (int id) {
            this.code = Code.USER_NOT_FOUND;
            this.msg = "User with id \"" + id + "\" not found";
            this.user = null;
            return this;
        }

        public Response UserAlreadyExists (User user) {
            this.code = Code.USER_ALREADY_EXISTS;
            this.msg = "User with id \"" + user.getId() + "\" already exists";
            this.user = Optional.of(user);
            return this;
        }
    };

    @GetMapping
    public List<User> getUsers () {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Response getUser (@PathVariable int id) {
        Optional<User> user = repo.findById(id);
        if (user.isPresent())
            return new Response(Response.Code.OK, "", user);
        return new Response().UserNotFound(id);
    }

    @PostMapping
    public Response addUser (@RequestBody User user) {
        Optional<User> found_user = repo.findById(user.getId());
        if (found_user.isPresent())
            return new Response().UserAlreadyExists(found_user.get());
        repo.saveAndFlush(user);
        return new Response(
            Response.Code.OK,
            "User added successfully",
            Optional.of(user)
        );
    }

    @DeleteMapping("/{id}")
    public Response deleteUser (@PathVariable int id) {
        Optional<User> user = repo.findById(id);
        if (user.isPresent()) {
            repo.deleteById(id);
            return new Response(
                Response.Code.OK,
                "User deleted successfully",
                user
            );
        }
        return new Response().UserNotFound(id);
    }

    @PutMapping
    public Response updateUser (@RequestBody User user) {
        Optional<User> found_user = repo.findById(user.getId());
        if (found_user.isPresent()) {
            repo.saveAndFlush(user);
            return new Response(
                Response.Code.OK,
                "User updated successfully",
                null
            );
        }
        return new Response().UserNotFound(user.getId());
    }
}
