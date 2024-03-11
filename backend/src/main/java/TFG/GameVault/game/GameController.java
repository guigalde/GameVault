package TFG.GameVault.game;

import org.springframework.stereotype.Controller;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class GameController {
    
    private final GameService gameService;
}
