package org.example.http_threads;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ThreadsControllerWithoutAsync {

    @GetMapping("/threads/noasync")
    public String doGet(@RequestParam("number") int number) {
        try {
            System.out.println("Servlet no. " + number + " called.");
            String url = "http://localhost:8080/threads/noasync?number=" + (number + 1);
            RestTemplate restTemplate = new RestTemplate();
            String content = restTemplate.getForObject(url, String.class);
            return "OK: " + content;
        } catch (Throwable e) {
            String message = "Reached " + number + " of connections";
            System.out.println(message);
            System.out.println(e);
            return message;
        }
    }
}



