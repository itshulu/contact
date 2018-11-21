package com.sl.contact.controller;

import com.sl.contact.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author 舒露
 */
@Controller
public class FrameController {
    private final ContactService contactService;

    @Autowired
    public FrameController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("contactsList", contactService.findAllContact());
        return "index";
    }
}
