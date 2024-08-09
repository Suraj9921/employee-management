package com.codepulse.controller;

import com.codepulse.dto.UserUpdateDTO;
import com.codepulse.model.User;
import com.codepulse.security.CustomerUserDetails;
import com.codepulse.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AdminController {

    private UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

//    @GetMapping("/dashboard")
//    public String listRegisteredUsers(Authentication authentication, Model model){
//        CustomerUserDetails userDetails = (CustomerUserDetails) authentication.getPrincipal();
//        String name = userDetails.getName();
//        List<User> users = userService.findAllByRole("USER");
//        model.addAttribute("users", users);
//        model.addAttribute("name", name);
//        return "admin/dashboard";
//    }

    @GetMapping("/dashboard")
    public String listRegisteredUsers(Authentication authentication, Model model){
        CustomerUserDetails userDetails = (CustomerUserDetails) authentication.getPrincipal();
        String firstName = userDetails.getFirstName();
        String lastName = userDetails.getLastName();
        String name = firstName + " " + lastName;
        List<User> users = userService.findAllByRole("USER");
        model.addAttribute("users", users);
        model.addAttribute("name", name);
        return "admin/dashboard";
    }

//    @GetMapping("/dashboard/update/{id}")
//    public String showUpdateUser(@PathVariable int id, Model model){
//        User existing = userService.findById(id);
//        model.addAttribute("user", existing);
//        return "admin/updateuser";
//    }

    @GetMapping("/dashboard/update/{id}")
    public String showUpdateUser(@PathVariable int id, Model model){
        User user = userService.findById(id);
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO();
        userUpdateDTO.setId(user.getId());
        userUpdateDTO.setFirstName(user.getFirstName());
        userUpdateDTO.setLastName(user.getLastName());
        userUpdateDTO.setEmail(user.getEmail());
        userUpdateDTO.setPhoneNumber(user.getPhoneNumber());
        userUpdateDTO.setGender(user.getGender());
        model.addAttribute("user", userUpdateDTO);
        return "admin/updateuser";
    }

    @PostMapping("/dashboard/update/{id}")
    public String updateUser(@Valid @ModelAttribute("user") UserUpdateDTO userUpdateDTO,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("user", userUpdateDTO);
            return "admin/updateuser";
        }

        // Convert DTO to User entity
        User existingUser = userService.findById(userUpdateDTO.getId());
        existingUser.setFirstName(userUpdateDTO.getFirstName());
        existingUser.setLastName(userUpdateDTO.getLastName());
        existingUser.setEmail(userUpdateDTO.getEmail());
        existingUser.setPhoneNumber(userUpdateDTO.getPhoneNumber());
        existingUser.setGender(userUpdateDTO.getGender());

        userService.updateUser(existingUser);
        return "redirect:/dashboard";
    }

//    @PostMapping("/dashboard/update/{id}")
//    public String updateUser(@Valid @ModelAttribute("user") User user,BindingResult result, Model model){
//        if (result.hasErrors()) {
//            model.addAttribute("user", user);
//            return "admin/updateuser";
//        }
//        userService.updateUser(user);
//        return "redirect:/dashboard";
//    }

//    @PostMapping("/update/{id}")
//    public String updateUser(@Validated(UpdateGroup.class) @ModelAttribute("user") User user,
//                             BindingResult result, Model model) {
//        if (result.hasErrors()) {
//            model.addAttribute("user", user);
//            return "admin/updateuser";
//        }
//        userService.updateUser(user);
//        return "redirect:/dashboard";
//    }

    @GetMapping("/dashboard/delete/{id}")
    public String deleteUser(@PathVariable int id){
        userService.deleteUserById(id);
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard/adduser")
    public String showAddUser(Model model){
        User user = new User();
        model.addAttribute("user", user);
        return "admin/adduser";
    }

    @PostMapping("/dashboard/adduser/save")
    public String addUser(@Valid @ModelAttribute("user") User user, BindingResult result, Model model){
        User existing = userService.findByEmail(user.getEmail());
        if (existing != null) {
            result.rejectValue("email", null, "There is already an account registered with that email");
        }
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "admin/adduser";
        }
        userService.saveUser(user);
        return "redirect:/dashboard/adduser?success";
    }

    @GetMapping("/dashboard/search")
    public String search(@RequestParam("name") String name, Model model){
        List<User> users = userService.searchUser("USER", name);
        model.addAttribute("users", users);
        return "admin/dashboard";
    }
}

