package TFG.GameVault.collections;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import TFG.GameVault.DTOs.CollectionBasicInfo;
import TFG.GameVault.DTOs.CollectionDto;
import TFG.GameVault.DTOs.PersonalVideogameInfoDto;
import TFG.GameVault.personal_videogame.PersonalVideogame;
import TFG.GameVault.personal_videogame.PersonalVideogameService;
import TFG.GameVault.user.User;
import TFG.GameVault.user.UserService;
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
        LocalDate creationDate = collection.getCreationDate();
        LocalDate lastUpdate = collection.getLastUpdate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String creationDateString = creationDate.format(formatter);
        String lastUpdateString = lastUpdate.format(formatter);

        return new CollectionDto(collection.getId(), collection.getName(), collection.getDescription()==null? "": collection.getDescription(), collection.getCreationDate(), collection.getLastUpdate(),
             creationDateString, lastUpdateString,collection.getCollectionGames().stream().map(pvs::toInfoDto).collect(Collectors.toList()));
    }

    public Collection toCollection(CollectionDto collectionDto, Integer user_id){
        User user = us.findById(user_id);
        List <PersonalVideogame> collectionGames = new ArrayList<>();
        for(PersonalVideogameInfoDto pvgDto : collectionDto.getCollectionGames()){
            PersonalVideogame pvg = pvs.findGameById(pvgDto.getId());
            if(pvg != null){
                collectionGames.add(pvg);
            }
        }
        return new Collection(collectionDto.getName(), collectionDto.getDescription(), collectionDto.getCreationDate(), collectionDto.getLastUpdate(),
             collectionGames, user);
    }

    @Transactional
    public Collection findById(Integer id){
        return cr.findById(id).orElse(null);
    }

    @Transactional
    public void deleteCollection(Integer id, Integer userId){
        Collection collection = cr.findById(id).orElse(null);
        if(collection != null && collection.getUser().getId() == userId){
            cr.delete(collection);
        }
    }

    @Transactional
    public Page<Collection> findAllByUserIdAndSearchTerm(Integer userId, String searchTerm, Integer pageNumber, String orderBy){
        
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

        Page<Collection> collections = cr.findAll(spec, pageable);
        return collections;
    }

    public List<Collection> findAllByUserId(Integer userId) {
        return cr.findAllByUser_Id(userId);
    }

    public CollectionBasicInfo toBasicInfo(Collection collection){
        return new CollectionBasicInfo(collection.getId(), collection.getName());
    }

    @Transactional
    public void addGameToCollection(Integer collectionId, Integer gameId){
        Collection collection = cr.findById(collectionId).orElse(null);
        PersonalVideogame pvg = pvs.findGameById(gameId);
        if(collection != null && pvg != null){
            collection.getCollectionGames().add(pvg);
            cr.save(collection);
        }
    }

    @Transactional
    public void removeGameFromCollections(PersonalVideogame pvg, Integer userId){
        List<Collection> collections = cr.findAllByUser_Id(userId);
        for(Collection collection : collections){
            collection.getCollectionGames().remove(pvg);
            cr.save(collection);
        }
    }

    @Transactional

    public void removeGameFromCollection(Integer collectionId, Integer gameId){
        Collection collection = cr.findById(collectionId).orElse(null);
        if(collection != null){
            List<PersonalVideogame> games = collection.getCollectionGames();
            Boolean resultOfRemove= games.removeIf(g -> g.getId() == gameId);
            if(resultOfRemove){
                cr.save(collection);
            }else{
                throw new IllegalArgumentException("Game not found in collection");
            }
        }else{
            throw new IllegalArgumentException("Collection not found");
        }
    }


}
