package org.example.http_threads;
import okhttp3.OkHttpClient;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class ThreadsControllerWithAsyncAndConnectionPool {

    private static final int MAX_THREADS = 4000;

    private final ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREADS);

    static StopWatch stopWatch;
    private final RestTemplate restTemplate;

    public ThreadsControllerWithAsyncAndConnectionPool() {
        OkHttpClient okHttpClient = new OkHttpClient();
        OkHttp3ClientHttpRequestFactory requestFactory = new OkHttp3ClientHttpRequestFactory(okHttpClient);
        this.restTemplate = new RestTemplate(requestFactory);
    }

    @GetMapping("/threads/async_http_pool")
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
            String url = "http://localhost:8080/threads/async_http_pool?number=" + number;
            String content = restTemplate.getForObject(url, String.class);
            System.out.println(content);
        } catch (Throwable e) {
            String message = "Error calling endpoint for request " + number;
            System.out.println(message);
            System.out.println(e);
        }
    }
}


