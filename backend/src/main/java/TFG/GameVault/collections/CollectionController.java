package TFG.GameVault.collections;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;

import TFG.GameVault.DTOs.CollectionDto;
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

    
}
