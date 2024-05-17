package TFG.GameVault.personal_videogame;

import java.util.List;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import TFG.GameVault.DTOs.PersonalVideogameBasicInfo;
import TFG.GameVault.DTOs.PersonalVideogameDto;
import TFG.GameVault.DTOs.PersonalVideogameInfoDto;
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
            ResponseEntity<List<Object>> response = ResponseEntity.ok(pvService.applyFilters(user_id, filter, pageable));
            return response;
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

    
}
