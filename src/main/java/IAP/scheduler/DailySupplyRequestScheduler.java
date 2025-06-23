package IAP.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Component
public class DailySupplyRequestScheduler {

    private final RestTemplate restTemplate;

    public DailySupplyRequestScheduler() {
        this.restTemplate = new RestTemplate();
    }

    // Cron: second minute hour day month weekday
    // @Scheduled(cron = "0 0 8 * * *") // Every day at 8:00 AM
    @Scheduled(cron = "0 * * * * *") //
    public void getDailySupplyRequests() {
        String targetUrl = "https://www.iana.org/assignments/media-types/application/vnd.api+json"; // example URL

        try {
//            ResponseEntity<String> response = restTemplate.getForEntity(targetUrl, String.class);
            System.out.println(LocalDateTime.now().toString() + " | hello from DailySupplyRequestScheduler");
        } catch (Exception e) {
            System.err.println("Scheduler failed: " + e.getMessage());
        }
    }
}
