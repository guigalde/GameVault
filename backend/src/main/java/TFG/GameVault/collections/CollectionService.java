package TFG.GameVault.collections;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.checkerframework.checker.units.qual.A;
import org.hibernate.mapping.Join;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import TFG.GameVault.DTOs.CollectionDto;
import TFG.GameVault.DTOs.PersonalVideogameDto;
import TFG.GameVault.personal_videogame.PersonalVideogame;
import TFG.GameVault.personal_videogame.PersonalVideogameService;
import TFG.GameVault.user.User;
import TFG.GameVault.user.UserService;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CollectionService {
    
    @Autowired
    private CollectionRepository cr;

    @Autowired
    private UserService us;

    @Autowired
    private PersonalVideogameService pvs;

    @Transactional
    public Collection saveCollection(Collection collection){
        return cr.save(collection);
    }

    public CollectionDto toCollectionDto(Collection collection){
        return new CollectionDto(collection.getName(), collection.getDescription(), collection.getCreationDate(), collection.getLastUpdate(),
             collection.getCollectionGames().stream().map(pvs::toDto).collect(Collectors.toList()));
    }

    public Collection toCollection(CollectionDto collectionDto, Integer user_id){
        User user = us.findById(user_id);
        List <PersonalVideogame> collectionGames = new ArrayList<>();
        for(PersonalVideogameDto pvgDto : collectionDto.getCollectionGames()){
            collectionGames.add(pvs.fromDTO(pvgDto, user_id));
        }
        return new Collection(collectionDto.getName(), collectionDto.getDescription(), collectionDto.getCreationDate(), collectionDto.getLastUpdate(),
             collectionGames, user);
    }

    @Transactional
    public Collection findById(Integer id){
        return cr.findById(id).orElse(null);
    }

    @Transactional
    public void deleteCollection(Integer id){
        Collection collection = cr.findById(id).orElse(null);
        if(collection != null){
            cr.delete(collection);
        }
    }

    @Transactional
    public List<Collection> findAllByUserIdAndSearchTerm(Integer userId, String searchTerm, Integer pageNumber, String orderBy){
        
        Specification<Collection> spec = (root, query, criteriaBuilder) -> {
            return criteriaBuilder.and(
                criteriaBuilder.equal(root.get("user").get("id"), userId)
            );
        };
        if(searchTerm != null && !searchTerm.isEmpty()){
            spec = spec.and((root, query, criteriaBuilder) -> {
                return criteriaBuilder.like(root.get("name"), "%" + searchTerm + "%");
            });
        }
        Pageable pageable = null;
        switch (orderBy) {
            case "nameAsc":
                pageable = PageRequest.of(pageNumber, 20, Direction.ASC, "name");
                break;
            case "nameDesc":
                pageable = PageRequest.of(pageNumber, 20, Direction.DESC, "name");
                break;
            case "creationDateAsc":
                pageable = PageRequest.of(pageNumber, 20, Direction.ASC, "creationDate");
                break;
            case "creationDateDesc":
                pageable = PageRequest.of(pageNumber, 20, Direction.DESC, "creationDate");
                break;
            case "lastUpdateAsc":
                pageable = PageRequest.of(pageNumber, 20, Direction.ASC, "lastUpdate");
                break;
            case "lastUpdateDesc":
                pageable = PageRequest.of(pageNumber, 20, Direction.DESC, "lastUpdate");
                break;
            default:
                pageable = PageRequest.of(pageNumber, 20, Direction.DESC, "lastUpdate");
                break;
        }

        return cr.findAll(spec, pageable).getContent();
    }


}
