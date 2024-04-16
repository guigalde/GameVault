package TFG.GameVault.personal_videogame;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
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
            pvService.savePersonalVideogame(pvService.fromDTO(personalVideogame, user_id));
            return ResponseEntity.ok("Game added successfully");
        
    }

    @PostMapping("/listPersonalVideogames/{user_id}/{page}")
    public ResponseEntity<List<Object>> listPersonalVideogame(@PathVariable Integer user_id,@PathVariable Integer page, @RequestBody PersonalVideogameFilter filter){
        Pageable pageable = PageRequest.of(page, 50, Direction.DESC, "timePlayed");
        return ResponseEntity.ok(pvService.applyFilters(user_id, filter, pageable));
    }

    
}
