package com.haiyue.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloWorldController {

    @Autowired
    HelloService helloService;

    @GetMapping("/hello") // http://domain/hello
    public String helloWorld(@RequestParam String name) {
        return this.helloService.hello(name);
    }

    @GetMapping("/greeting")
    public String greeting(@RequestParam(required = false, defaultValue = "Alice") String name,
                           @RequestHeader("User-Agent") String userAgent) {
        return "Hello, " + name + "!, user-agent: " + userAgent;
    }

    @PostMapping("/greeting")
    public String greetingPost(@RequestBody(required = false) String body, @RequestParam String name) {
        return "Body: " + body + ", name: " + name;
    }

    @PostMapping("/addPerson") // HTML template
    public Person addPerson(@RequestBody Person person) {
        return person;
    }
    // object in memory -> JSON/text/... serialization
    // JSON/text -> object in memory ... deserialization

    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName());
    }
}
