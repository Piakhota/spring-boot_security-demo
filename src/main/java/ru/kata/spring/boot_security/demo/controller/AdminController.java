package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showAll(ModelMap model) {
        List<User> allUsers = userService.getAll();
        model.addAttribute("allUsers", allUsers);
        return "all-users";
    }

    @GetMapping("/addNewUser")
    public String addNew(ModelMap model) {
        User user = new User();
        model.addAttribute("user", user);
        return "user-info";
    }

    @PatchMapping("/updateInfo")
    public String update(@RequestParam("id") Long id, ModelMap model) {
        User user = userService.get(id);
        model.addAttribute("user", user);
        return "user-info";
    }

    @PostMapping("/saveUser")
    public String save(@ModelAttribute("user") User user) {
        if (user.getId() == null) {
            user.setRoles(Set.of(new Role(1L, "ROLE_USER")));
        }
        userService.save(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/deleteUser")
    public String delete(@RequestParam("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}
