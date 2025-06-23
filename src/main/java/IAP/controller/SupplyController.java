package IAP.controller;

import IAP.model.SupplyRequest;
import jakarta.validation.constraints.Null;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/supply")
public class SupplyController {

    public SupplyController() {
        
    }

//    @GetMapping('/list')
//    public ResponseEntity<List<SupplyRequest>> listSupplyRequests(){
//        return new List<SupplyRequest>();
//    }

}
