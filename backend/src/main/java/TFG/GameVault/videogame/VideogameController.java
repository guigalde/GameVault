package TFG.GameVault.videogame;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import lombok.AllArgsConstructor;
import proto.Game;

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
        Map<Game, Map<String,List<String>>> gamesMap = igdbConsumer.getGames();
        Set<Game> games = gamesMap.keySet();
        for(Game game: games){
            Videogame vg = new Videogame();
            vg.setName(game.getName());
            vg.setDescription(game.getSummary());
            vg.setImage(gamesMap.get(game).get("cover").stream().map(c->"https:"+ c).collect(Collectors.joining(", ")));
            vg.setPlatforms(gamesMap.get(game).get("platforms").stream().collect(Collectors.joining(", ")));
            vg.setGenres(gamesMap.get(game).get("genres").stream().collect(Collectors.joining(", ")));
            vg.setDeveloper(gamesMap.get(game).get("developers").stream().collect(Collectors.joining(", ")));
            vg.setPublisher(gamesMap.get(game).get("publishers").stream().collect(Collectors.joining(", ")));
            vg.setReleaseDate(Instant
            .ofEpochSecond( game.getFirstReleaseDate().getSeconds() , game.getFirstReleaseDate().getNanos() ) 
            .atZone( ZoneId.of( "America/Montreal" ) ) 
            .toLocalDate());
            
            vgService.saveGame(vg);
        }
        
        return ResponseEntity.ok("50 games loaded successfully!");
    }
    
}
