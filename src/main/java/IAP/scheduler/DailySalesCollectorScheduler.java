package IAP.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Component
public class DailySalesCollectorScheduler {

    private final RestTemplate restTemplate;

    public DailySalesCollectorScheduler() {
        this.restTemplate = new RestTemplate();
    }

    // Cron: second minute hour day month weekday
    // @Scheduled(cron = "0 0 8 * * *") // Every day at 8:00 AM
    @Scheduled(cron = "0/15 * * * * *") //
    public void getDailySalesFromBranches() {
        String branchSaleUrl = "http://localhost:8080/api/sales";

        try {
//            ResponseEntity<String> response = restTemplate.getForEntity(targetUrl, String.class);
            System.out.println(LocalDateTime.now().toString() + " | hello from DailySalesCollectorScheduler");
        } catch (Exception e) {
            System.err.println("Scheduler failed: " + e.getMessage());
        }
    }
}