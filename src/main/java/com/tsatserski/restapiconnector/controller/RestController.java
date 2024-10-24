package com.tsatserski.restapiconnector.controller;

import com.tsatserski.restapiconnector.model.ContactItem;
import com.tsatserski.restapiconnector.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/api")
public class RestController {

    private final ContactService contactService;

    @Autowired
    public RestController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/contacts")
    public ResponseEntity<List<ContactItem>> getContacts() {
        return ResponseEntity.ok(contactService.getAllContacts());
    }
}