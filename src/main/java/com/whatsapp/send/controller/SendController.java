package com.whatsapp.send.controller;


import com.whatsapp.send.model.MessageBody;
import com.whatsapp.send.service.SendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/messages")
public class SendController {

    Logger logger = LoggerFactory.getLogger(SendController.class);

    @Autowired
    SendService service;

    @GetMapping
    public String test(){
        return "App is Up and Running....";
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/send")
    public String sendSimpleMessage(@RequestBody MessageBody messageBody){
        logger.info("Received request");
        String response = service.messageService(messageBody);
        return response.equals("SUCCESS")?"Message Sent Successfully":response;
    }


}
