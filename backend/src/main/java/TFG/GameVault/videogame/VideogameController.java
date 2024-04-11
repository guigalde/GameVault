package TFG.GameVault.videogame;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import TFG.GameVault.API_Consumers.IGDB_consumer;
import TFG.GameVault.DTOs.VideogameDto;





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
    
    @PostMapping("/videogames/{page}")
    public ResponseEntity<List<Object>> getGames(@RequestBody GamesFilter filter, @PathVariable Integer page){
        
            Pageable pageable = PageRequest.of(page, 50, Direction.DESC, "releaseDate");
            return ResponseEntity.ok(vgService.filterGames(filter, pageable));
        
    }

    @GetMapping("/videogames/platforms")
    public ResponseEntity<Set<String>> getPlatforms(){
        return ResponseEntity.ok(vgService.getPlatforms());
    }
    
    @GetMapping("/videogames/genres")
    public ResponseEntity<Set<String>> getGenres(){
        return ResponseEntity.ok(vgService.getGenres());
    }
    
    @GetMapping("/videogames/publishers")
    public ResponseEntity<Set<String>> getPublishers(){
        return ResponseEntity.ok(vgService.getPublishers());
    }
    
}
