import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    @GetMapping("/api/hello")
    public String hello() {
        return "Hello, World!";
    }

    @PostMapping("/api/data")
    public String postData(@RequestBody String data) {
        return "Data received: " + data;
    }
}