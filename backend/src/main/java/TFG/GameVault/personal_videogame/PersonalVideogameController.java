package TFG.GameVault.personal_videogame;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import TFG.GameVault.DTOs.PersonalVideogameDto;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping("/api")
public class PersonalVideogameController {

    private final PersonalVideogameService pvService;

    @PostMapping("/addPersonalVideogame/{user_id}")
    public ResponseEntity<String> addPersonalVideogame(@PathVariable Integer user_id, @RequestBody PersonalVideogameDto personalVideogame){
        try{
            pvService.savePersonalVideogame(pvService.fromDTO(personalVideogame, user_id));
            return ResponseEntity.ok("Game added successfully");
        }catch(Exception e){
            return ResponseEntity.badRequest().body("Error adding game");
        }
    }

    
}
