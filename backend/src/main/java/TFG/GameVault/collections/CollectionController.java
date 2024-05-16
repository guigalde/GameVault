package TFG.GameVault.collections;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;

import TFG.GameVault.DTOs.CollectionBasicInfo;
import TFG.GameVault.DTOs.CollectionDto;
import TFG.GameVault.DTOs.VideogameDto;
import lombok.AllArgsConstructor;



@Controller
@AllArgsConstructor
@RequestMapping("/api")
public class CollectionController {

    @Autowired
    private CollectionService cs;

    @PostMapping("/collections/{userId}/{page}")
    public ResponseEntity<List<Object>> findAllByUserId(@PathVariable Integer userId, @PathVariable Integer page,
     @RequestPart(required = false) String searchTerm, @RequestPart(required = false) String orderBy){
        searchTerm = searchTerm == null ? "" : searchTerm;
        orderBy = orderBy == null ? "creationDate" : orderBy;
        Page<Collection> collections = cs.findAllByUserIdAndSearchTerm(userId, searchTerm, page, orderBy);
        List<Object> res = List.of(collections.getContent().stream().map(cs::toCollectionDto).collect(Collectors.toList()), collections.getTotalPages());
        return ResponseEntity.ok(res);
    }
    
    @PostMapping("/collections/create/{userId}")
    //Cambiar
    public ResponseEntity<?> createCollection(@PathVariable Integer userId, @RequestBody CollectionDto collectionDto){
        try{
            collectionDto.setCreationDate(LocalDate.now());
            collectionDto.setLastUpdate(LocalDate.now());
            cs.saveCollection(cs.toCollection(collectionDto, userId));
            return ResponseEntity.ok("Collection created successfully");
        }catch(Exception e){
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/collections/delete/{collectionId}")
    public ResponseEntity<?> deleteCollection(@PathVariable Integer collectionId){
        try{
            cs.deleteCollection(collectionId);
            return ResponseEntity.ok("Collection deleted successfully");
        }catch(Exception e){
            return ResponseEntity.badRequest().body(null);
        }
        
        
    }

    @GetMapping("/collections/{userId}")
    public ResponseEntity<List<CollectionBasicInfo>> findAllByUserId(@PathVariable Integer userId){
        List<Collection> collections = cs.findAllByUserId(userId);
        return ResponseEntity.ok(collections.stream().map(cs::toBasicInfo).collect(Collectors.toList()));
    }

    @PostMapping("/collections/addGame/{collectionId}/{gameId}")
    public ResponseEntity<String> addGameToCollection(@PathVariable Integer collectionId, @PathVariable Integer gameId){
        try{
            Collection collection = cs.findById(collectionId);
            if(collection.getCollectionGames().stream().anyMatch(g -> g.getId() == gameId)){
                return ResponseEntity.badRequest().body("Game already in collection");
            }
            cs.addGameToCollection(collectionId, gameId);
            return ResponseEntity.ok("Game added to collection successfully");
        }catch(Exception e){
            return ResponseEntity.badRequest().body("Error adding game to collection");
        }
    }
    
}
