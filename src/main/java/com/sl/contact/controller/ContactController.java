package com.sl.contact.controller;

import com.sl.contact.entity.Contact;
import com.sl.contact.entity.JsonMsg;
import com.sl.contact.exception.NullIdException;
import com.sl.contact.service.ContactService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;


/**
 * @author 舒露
 */
@RestController
public class ContactController {
    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);
    private final ContactService contactService;

    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/contacts")
    public List<Contact> findAll() {
        logger.debug("Find All Contacts");
        return contactService.findAllContact();
    }

    @GetMapping("/contact/{id}")
    public JsonMsg findOne(@PathVariable String id) {
        logger.debug("Find One Contacts");
        try {
            return new JsonMsg(contactService.findOne(id), "success", 200);
        } catch (NullIdException e) {
            return new JsonMsg(null, e.getMessage(), 404);
        }
    }

    @PostMapping("/contact")
    public JsonMsg save(Contact contact) {
        logger.debug("save One Contact");
        try {
            contact.setId(UUID.randomUUID().toString().replace("-", ""));
            contactService.saveContact(contact);
            return new JsonMsg(contact, "add success", 200);
        } catch (Exception e) {
            return new JsonMsg(null, e.getMessage(), 404);
        }
    }

    @PatchMapping("/contact")
    public void updateContact(Contact contact, HttpServletResponse response) {
        try {
            contactService.updateContact(contact);
            logger.debug("update One Contact success");
        } catch (NullIdException e) {
            logger.debug("update fail:" + e.getMessage());
            response.setStatus(404);
        }
    }

    @DeleteMapping("/contact/{id}")
    public void del(@PathVariable String id, HttpServletResponse response) {
        try {
            contactService.delContact(id);
            logger.debug("delete success");
        } catch (NullIdException e) {
            logger.debug("delete fail:" + e.getMessage());
            response.setStatus(404);
        }
    }
}
