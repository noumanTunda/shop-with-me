package com.tundalabs.store.controllers;

import com.tundalabs.store.entities.Message;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class MessageController {
    @RequestMapping()
    public Message sayHello(){
        return new Message("Hello Guest!");
    }
}
