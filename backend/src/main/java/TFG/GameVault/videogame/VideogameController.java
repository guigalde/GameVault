package TFG.GameVault.videogame;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

import TFG.GameVault.API_Consumers.IGDB_consumer;

import org.springframework.web.bind.annotation.RequestParam;





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
    
    @GetMapping("/videogames")

    public ResponseEntity<List<Videogame>> getGames(@RequestParam GamesFilter filter, Integer pageNumber){
        try{
            Pageable pageable = PageRequest.of(pageNumber, 10, Direction.ASC, "release_date");
            return ResponseEntity.ok(vgService.filterGames(filter, pageable));
        }catch(Exception e){
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    
}
