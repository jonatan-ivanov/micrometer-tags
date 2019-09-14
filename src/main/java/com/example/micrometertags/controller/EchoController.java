package com.example.micrometertags.controller;

import com.example.micrometertags.service.EchoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EchoController {
    @Autowired private EchoService echoService;

    @GetMapping("/echo/{message}")
    public String echo(
        @PathVariable("message") String message,
        @RequestParam(value = "reverse", required = false, defaultValue = "false") boolean reverse) {
        return echoService.echo(message, reverse);
    }
}
