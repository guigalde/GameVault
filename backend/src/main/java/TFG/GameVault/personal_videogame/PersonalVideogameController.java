package TFG.GameVault.personal_videogame;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import TFG.GameVault.DTOs.PersonalVideogameBasicInfo;
import TFG.GameVault.DTOs.PersonalVideogameDto;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping("/api")
public class PersonalVideogameController {

    @Autowired
    private final PersonalVideogameService pvService;

    @PostMapping("/addPersonalVideogame/{user_id}")
    public ResponseEntity<String> addPersonalVideogame(@PathVariable Integer user_id, @RequestBody PersonalVideogameDto personalVideogame){
        try{
            pvService.savePersonalVideogame(pvService.fromDTO(personalVideogame, user_id));
            return ResponseEntity.ok("Game added successfully");
        }catch(Exception e){
            return ResponseEntity.badRequest().body(null);
        }
        
    }

    @GetMapping("/myGamesNames/{user_id}")
    public ResponseEntity<List<PersonalVideogameBasicInfo>> listPersonalVideogameNames(@PathVariable Integer user_id){
        try{
            return ResponseEntity.ok(pvService.findAllByUser(user_id).stream().map(pv -> pvService.toBasicInfo(pv)).toList());
        }catch(Exception e){
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/listMyGames/{user_id}/{page}")
    public ResponseEntity<List<Object>> listPersonalVideogame(@PathVariable Integer user_id,@PathVariable Integer page, @RequestBody PersonalVideogameFilter filter){
        try{
            if(user_id == null || page == null){
                return ResponseEntity.badRequest().body(null);
            }
            Pageable pageable = PageRequest.of(page, 50, Direction.DESC, "timePlayed");
            Page<PersonalVideogame> videogames = pvService.applyFilters(user_id, filter, pageable);
            List<Object> response = List.of(videogames.getContent().stream().map(pv -> pvService.toInfoDto(pv)).collect(java.util.stream.Collectors.toList()), videogames.getTotalPages());
            return ResponseEntity.ok(response);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/personalVideogame/{game_id}/{user_id}")
    public ResponseEntity<?> getPersonalVideogame(@PathVariable Integer game_id, @PathVariable Integer user_id){
        try{
            PersonalVideogame pv = pvService.findById(game_id, user_id);
            if(pv == null){
                return ResponseEntity.badRequest().body("Game not found");
            }else{
                return ResponseEntity.ok(pvService.toInfoDto(pv));
            }
        }catch(Exception e){
            return ResponseEntity.badRequest().body("Something went wrong");
        }
    }

    @DeleteMapping("/deletePersonalVideogame/{game_id}/{user_id}")
    public ResponseEntity<String> deletePersonalVideogame(@PathVariable Integer game_id, @PathVariable Integer user_id){
        
            pvService.deletePersonalVideogame(game_id, user_id);
            return ResponseEntity.ok("Game deleted successfully");
        
    }

    @PostMapping("/personalVideogame/update/{game_id}/{user_id}")
    public ResponseEntity<String> postMethodName(@PathVariable Integer game_id, @PathVariable Integer user_id, @RequestBody PersonalVideogameDto personalVideogame) {
        try{
            PersonalVideogame pv = pvService.findById(game_id, user_id);
            if(pv == null){
                return ResponseEntity.badRequest().body("Game not found");
            }
            pvService.updatePersonalVideogame(pv, personalVideogame);
            return ResponseEntity.ok("Game updated successfully");
        }catch(Exception e){
            return ResponseEntity.badRequest().body("Error updating game information");
        }
    }
    
    @PostMapping("/personalVideogame/syncWithSteam/{user_id}/{steamId}")
    public ResponseEntity<?> syncWithSteam(@PathVariable Integer user_id, @PathVariable String steamId){ 
        if(steamId == null || user_id == null){
            return ResponseEntity.badRequest().body("Steam ID or user ID not provided");
        }else{
            
                List<PersonalVideogame> steamGames = pvService.getGamesBySteamData(user_id, steamId);
                if (steamGames.size()>0) {
                    return ResponseEntity.ok("Collection synced with steam successfully");
                }else{
                    return ResponseEntity.badRequest().body("Problem retrieving steam games");
                }
            
        }
    }
    

    
}
