package TFG.GameVault.user;

import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class MessagesController {

    @GetMapping("/messages")
    public ResponseEntity<List<String>> getMethodName() {
        return ResponseEntity.ok(Arrays.asList("fist","second"));
    }
    
    
}
