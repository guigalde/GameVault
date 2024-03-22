package TFG.GameVault.videogame;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

import TFG.GameVault.API_Consumers.IGDB_consumer;




@Controller
@AllArgsConstructor
@RequestMapping("/api")
public class VideogameController {
    
    private final VideogameService vgService;
    private final IGDB_consumer igdbConsumer;

    @GetMapping("/populateVideogames")
    public ResponseEntity<String> populate() {
        List<Videogame> games = igdbConsumer.getGames("0");
        games.addAll(igdbConsumer.getGames("500"));
        games.addAll(igdbConsumer.getGames("1000"));
        games.addAll(igdbConsumer.getGames("1500"));
        for(Videogame game: games){
            try{
                game = vgService.saveGame(game);
            }catch(Exception e){
                System.out.println("Error saving game: "+game.getName());
            }
        }
        
        return ResponseEntity.ok(games.size() + " games loaded successfully!");
    }
    
}
