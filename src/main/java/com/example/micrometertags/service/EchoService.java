package com.example.micrometertags.service;

import com.example.micrometertags.ExtraTag;
import com.example.micrometertags.ExtraTagsPropagation;
import io.micrometer.core.annotation.Timed;
import org.springframework.stereotype.Service;

@Service
public class EchoService {
    @Timed("echo")
    public String echo(@ExtraTag("message") String message, boolean reverse) {
        ExtraTagsPropagation.add("reversed", reverse);
        if (reverse) {
            return new StringBuilder(message).reverse().toString();
        }
        else {
            return message;
        }
    }
}
