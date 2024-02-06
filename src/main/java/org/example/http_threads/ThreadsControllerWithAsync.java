package org.example.http_threads;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class ThreadsControllerWithAsync {

    private static final int MAX_THREADS = 4000;
    static StopWatch stopWatch;
    private final ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREADS);

    @GetMapping("/threads/async")
    public String doGet(@RequestParam("number") int number) {
        try {
            if(number == 1) {
                stopWatch = new StopWatch();
                stopWatch.start();
            }
            System.out.println("Servlet no. " + number + " called.");
            if (number < MAX_THREADS) {
                executorService.submit(() -> callAnotherEndpoint(number + 1));
                return "Processing request " + number;
            } else {
                stopWatch.stop();
                return String.format("Max threads reached. Time spent: %sSECONDS", stopWatch.getTotalTimeSeconds());
            }
        } catch (Throwable e) {
            String message = "Error processing request " + number;
            System.out.println(message);
            System.out.println(e);
            return message;
        }
    }

    private void callAnotherEndpoint(int number) {
        try {
            String url = "http://localhost:8080/threads/async?number=" + number;
            RestTemplate restTemplate = new RestTemplate();
            String content = restTemplate.getForObject(url, String.class);
            System.out.println(content);
        } catch (Throwable e) {
            String message = "Error calling endpoint for request " + number;
            System.out.println(message);
            System.out.println(e);
        }
    }
}



