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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import TFG.GameVault.API_Consumers.IGDB_consumer;
import TFG.GameVault.DTOs.VideogameDto;
import TFG.GameVault.personal_videogame.PersonalVideogame;
import TFG.GameVault.personal_videogame.PersonalVideogameService;





@Controller
@AllArgsConstructor
@RequestMapping("/api")
public class VideogameController {
    
    private final VideogameService vgService;
    private final IGDB_consumer igdbConsumer;
    private final PersonalVideogameService pvgService;

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
            return ResponseEntity.ok(vgService.filterGames(filter, pageable, null));
        
    }

    @PostMapping("/wishlist/{page}/{userId}")
    public ResponseEntity<List<Object>> getWishlist(@RequestBody GamesFilter filter, @PathVariable Integer page, @PathVariable Integer userId){
        
            Pageable pageable = PageRequest.of(page, 50, Direction.DESC, "releaseDate");
            return ResponseEntity.ok(vgService.filterGames(filter, pageable, userId));
        
    }
    
    @DeleteMapping("/deleteFromWishlist/{userId}/{gameId}")
        public ResponseEntity<String> deleteFromWishlist(@PathVariable Integer userId, @PathVariable Integer gameId) {
            try{
                vgService.deleteFromWishlist(userId, gameId);
                String message = "Game successfully deleted from wishlist";
                return ResponseEntity.ok(message);
            }catch(Exception e){
                String message = "Error deleting game from wishlist";
                return ResponseEntity.badRequest().body(message);
            }
        }

        @PostMapping("/addToWishlist/{userId}/{gameId}")
        public ResponseEntity<String> addToWishlist(@PathVariable Integer userId, @PathVariable Integer gameId) {
            try{
                PersonalVideogame pv = pvgService.findByUserAndVideogameId(gameId, userId);
                if(pv != null) return ResponseEntity.badRequest().body("Game already in wishlist");
                vgService.addToWishlist(userId, gameId);
                String message = "Game successfully added to wishlist";
                return ResponseEntity.ok(message);
            }catch(Exception e){
                String message = "Error adding game to wishlist";
                return ResponseEntity.badRequest().body(message);
            }
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

    @GetMapping("/videogame/{id}")
    public ResponseEntity<VideogameDto> getGame(@PathVariable Integer id){
        return ResponseEntity.ok(vgService.transformToDTO(vgService.getGame(id)));
    }
    
}
