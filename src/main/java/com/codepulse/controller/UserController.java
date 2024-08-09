package com.codepulse.controller;

import com.codepulse.security.CustomerUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/home")
    public String home(Authentication authentication, Model model) {
        CustomerUserDetails userDetails = (CustomerUserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        String firstName = userDetails.getFirstName();
        String lastName = userDetails.getLastName();
        String gender = userDetails.getGender();
        String phoneNumber = userDetails.getPhoneNumber();

        model.addAttribute("Email", email);
        model.addAttribute("FirstName", firstName);
        model.addAttribute("LastName", lastName);
        model.addAttribute("Gender", gender);
        model.addAttribute("PhoneNumber", phoneNumber);

        return "user/home";
    }
}

