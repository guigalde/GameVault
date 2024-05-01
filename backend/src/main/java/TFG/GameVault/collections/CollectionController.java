package TFG.GameVault.collections;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    public List<CollectionDto> findAllByUserId(@PathVariable Integer userId, @PathVariable Integer page, @RequestPart String searchTerm, @RequestPart String orderBy){
        return cs.findAllByUserIdAndSearchTerm(userId, searchTerm, page, orderBy).stream().map(cs::toCollectionDto).collect(Collectors.toList());
    }
    
    @PostMapping("/collections/create/{userId}")
    public CollectionDto createCollection(@PathVariable Integer userId, @RequestPart CollectionDto collectionDto){
        return cs.toCollectionDto(cs.saveCollection(cs.toCollection(collectionDto, userId)));
    }

    @DeleteMapping("/collections/delete/{collectionId}")
    public void deleteCollection(@PathVariable Integer collectionId){
        cs.deleteCollection(collectionId);
    }

    
}
