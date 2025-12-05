package vn.vennhuu.jobhunter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    @CrossOrigin
    public ResponseEntity<String> getHelloWorld() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Hello World");
    }
}
