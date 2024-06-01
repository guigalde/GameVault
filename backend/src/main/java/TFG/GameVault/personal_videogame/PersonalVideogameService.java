package TFG.GameVault.personal_videogame;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import TFG.GameVault.API_Consumers.IGDB_consumer;
import TFG.GameVault.DTOs.PersonalVideogameBasicInfo;
import TFG.GameVault.DTOs.PersonalVideogameDto;
import TFG.GameVault.DTOs.PersonalVideogameInfoDto;
import TFG.GameVault.DTOs.VideogameDto;
import TFG.GameVault.collections.Collection;
import TFG.GameVault.collections.CollectionRepository;
import TFG.GameVault.user.User;
import TFG.GameVault.user.UserService;
import TFG.GameVault.videogame.Videogame;
import TFG.GameVault.videogame.VideogameService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import TFG.GameVault.API_Consumers.Steam_consumer;

@Service
@AllArgsConstructor
public class PersonalVideogameService {

    @Autowired
    private final PersonalVideogameRepository personalVideogameRepository;

    @Autowired
    private final UserService userService;

    @Autowired
    private final VideogameService videogameService;

    @Autowired
    private final CollectionRepository collectionRepository;

    @Autowired
    private final IGDB_consumer igdbConsumer;

    @Autowired
    private final Steam_consumer steamConsumer;


    @Transactional
    public PersonalVideogame savePersonalVideogame(PersonalVideogame personalVideogame){
        return personalVideogameRepository.save(personalVideogame);
    }

    @Transactional
    public PersonalVideogame findGameById(Integer id){
        return personalVideogameRepository.findById(id).orElse(null);
    }

    @Transactional
    public void deleteGame(Integer id){
        personalVideogameRepository.deleteById(id);
    }

    @Transactional 
    public List<PersonalVideogame> findAllByUser(Integer user_id){
        return personalVideogameRepository.findAllByUser_Id(user_id);
    }


    public PersonalVideogame fromDTO(PersonalVideogameDto dto, Integer user_id){
        User user = userService.findById(user_id);
        Videogame videogame = videogameService.getGame(dto.getVideogameId());

        PersonalVideogame personalVideogame = new PersonalVideogame();
        personalVideogame.setUser(user);
        personalVideogame.setVideogame(videogame);
        personalVideogame.setAcquiredOn(dto.getAcquiredOn());
        personalVideogame.setCompletedOn(dto.getCompletedOn());
        personalVideogame.setCompletionTime(dto.getCompletionTime());
        personalVideogame.setMark(dto.getMark());
        personalVideogame.setNotes(dto.getNotes());
        personalVideogame.setPlatform(dto.getPlatform());
        personalVideogame.setTimePlayed(dto.getTimePlayed());

        return personalVideogame;
    }

    public PersonalVideogameInfoDto toInfoDto(PersonalVideogame personalVideogame){
        LocalDate acquiredOn = personalVideogame.getAcquiredOn();
        LocalDate completedOn = personalVideogame.getCompletedOn();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        PersonalVideogameInfoDto dto = new PersonalVideogameInfoDto();
        VideogameDto videogameDto = videogameService.transformToDTO(personalVideogame.getVideogame());
        dto.setId(personalVideogame.getId());
        dto.setCompletionTime(personalVideogame.getCompletionTime());
        dto.setMark(personalVideogame.getMark());
        dto.setNotes(personalVideogame.getNotes());
        dto.setPlatform(personalVideogame.getPlatform());
        dto.setTimePlayed(personalVideogame.getTimePlayed());
        dto.setVideogame(videogameDto);
        
        if(acquiredOn!=null){
            String acquiredOnString = acquiredOn.format(formatter);
            dto.setAcquiredOn(acquiredOnString);
        }
        if(completedOn!=null){
            String completedOnString = completedOn.format(formatter);
            dto.setCompletedOn(completedOnString);

        }

        return dto;
    }

    public PersonalVideogameDto toDto(PersonalVideogame personalVideogame){
        PersonalVideogameDto dto = new PersonalVideogameDto();
        dto.setId(personalVideogame.getId());
        dto.setAcquiredOn(personalVideogame.getAcquiredOn());
        dto.setCompletedOn(personalVideogame.getCompletedOn());
        dto.setCompletionTime(personalVideogame.getCompletionTime());
        dto.setMark(personalVideogame.getMark());
        dto.setNotes(personalVideogame.getNotes());
        dto.setPlatform(personalVideogame.getPlatform());
        dto.setTimePlayed(personalVideogame.getTimePlayed());
        dto.setVideogameId(personalVideogame.getVideogame().getId());

        return dto;
    }

    public PersonalVideogameBasicInfo toBasicInfo(PersonalVideogame personalVideogame){
        return new PersonalVideogameBasicInfo(personalVideogame.getId(), personalVideogame.getVideogame().getName());
    }

    @Transactional
    public Page<PersonalVideogame> applyFilters(Integer userId, PersonalVideogameFilter filter, Pageable page){
        Specification<PersonalVideogame> spec = Specification.where(PersonalVideogameSpecifications.user(userId));
        if(filter != null){
            spec = spec.and(PersonalVideogameSpecifications.completed(filter.getCompleted()))
                .and(PersonalVideogameSpecifications.hasPlatform(filter.getPlatform()))
                .and(PersonalVideogameSpecifications.markInInterval(filter.getMinMark(), filter.getMaxMark()))
                .and(PersonalVideogameSpecifications.timePlayedInInterval(filter.getMinTimePlayed(), filter.getMaxTimePlayed()))
                .and(PersonalVideogameSpecifications.hasGenre(filter.getGenre()))
                .and(PersonalVideogameSpecifications.hasPublisher(filter.getPublisher()))
                .and(PersonalVideogameSpecifications.search(filter.getSearchTerms()))
                .and(PersonalVideogameSpecifications.filterByDate(filter.getMinReleaseDate(), filter.getMaxReleaseDate()))
                .and(PersonalVideogameSpecifications.isInCollection(filter.getCollection()));
            
            if(page!= null && filter.getMarkSort()!=null && filter.getMarkSort()==true){
                page = PageRequest.of(page.getPageNumber(), page.getPageSize(), Direction.ASC, "mark");
            }else if(page!= null && filter.getMarkSort()!=null && filter.getMarkSort()==false){
                page = PageRequest.of(page.getPageNumber(), page.getPageSize(), Direction.DESC, "mark");
            }else if(page!= null && filter.getTimePlayedSort()!=null && filter.getTimePlayedSort()==true){
                page = PageRequest.of(page.getPageNumber(), page.getPageSize(), Direction.ASC, "timePlayed");
            }else if(page!= null && filter.getTimePlayedSort()!=null && filter.getTimePlayedSort()==false){
                page = PageRequest.of(page.getPageNumber(), page.getPageSize(), Direction.DESC, "timePlayed");
            }
        }
        Page<PersonalVideogame>  personalVideogames = personalVideogameRepository.findAll(spec, page);

        return personalVideogames;
    }

    @Transactional
    public PersonalVideogame findById(Integer game_id, Integer user_id) {

        PersonalVideogame personalVideogame = personalVideogameRepository.findById(game_id).orElse(null);
        if(personalVideogame.getUser().getId() == user_id && personalVideogame != null){
            return personalVideogame;
        }else{
            return null;
        }
    }
    
    @Transactional
    public void deletePersonalVideogame(Integer game_id, Integer user_id) {
        PersonalVideogame personalVideogame = personalVideogameRepository.findById(game_id).orElse(null);
        if(personalVideogame.getUser().getId() == user_id && personalVideogame != null){
            collectionRepository.findAllByUser_Id(user_id).forEach(collection -> {
                collection.getCollectionGames().remove(personalVideogame);
                collectionRepository.save(collection);
            });
            personalVideogameRepository.deleteById(game_id);
        }
    }

    @Transactional
    public PersonalVideogame updatePersonalVideogame(PersonalVideogame pv, PersonalVideogameDto personalVideogame) {
        pv.setAcquiredOn(personalVideogame.getAcquiredOn());
        pv.setCompletedOn(personalVideogame.getCompletedOn());
        pv.setCompletionTime(personalVideogame.getCompletionTime());
        pv.setMark(personalVideogame.getMark());
        pv.setNotes(personalVideogame.getNotes());
        pv.setPlatform(personalVideogame.getPlatform());
        pv.setTimePlayed(personalVideogame.getTimePlayed());
        PersonalVideogame pv2 = personalVideogameRepository.save(pv);
        return pv2;
    }

    @Transactional
    public PersonalVideogame findByUserAndVideogameId(Integer gameId, Integer userId){
        PersonalVideogame pv = personalVideogameRepository.findByUserIdAndVideogameId(gameId, userId);
        return pv;
    }

    
    public List<PersonalVideogame> getGamesBySteamData(Integer userId, String steamId) {
        List<Map<String,?>> games = new ArrayList<>();
        try {
            games = steamConsumer.getGames(steamId);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }

        List<PersonalVideogame> gamesToAdd = new ArrayList<>();
        Set<String> existingGameNames = new HashSet<>();
        Set<PersonalVideogame> existingGames = new HashSet<>();

        List<PersonalVideogame> personalGames = personalVideogameRepository.findAllByUser_Id(userId);
        for (PersonalVideogame pg : personalGames) {
            if (pg.getVideogame()!= null && pg.getVideogame().getName()!= null) {
                existingGameNames.add(pg.getVideogame().getName());
                existingGames.add(pg);
            }
        }

        Collection collection = new Collection();
        if(collectionRepository.findByName("Steam")==null){
            collection.setName("Steam");
            collection.setUser(userService.findById(userId));
            collection.setCreationDate(LocalDate.now());
            collection.setLastUpdate(LocalDate.now());
            collectionRepository.save(collection);
        }else{
            collection = collectionRepository.findByName("Steam");
            collection.setLastUpdate(LocalDate.now());
        }
        

        for (Map<String,?> game : games) {
            String name = (String) game.get("name");

            if (!existingGameNames.contains(name)) { 
                Videogame vg = igdbConsumer.searchGame(name);
                if (vg == null) {
                    continue;
                } else {
                    Videogame existingVideogame = videogameService.getGameByName(vg.getName());
                    if (existingVideogame != null) {
                        vg = existingVideogame;
                    } else {
                        vg = videogameService.saveGame(vg);
                    }
                    PersonalVideogame pg = new PersonalVideogame();
                    pg.setUser(userService.findById(userId));
                    pg.setVideogame(vg);
                    pg.setPlatform("Steam");
                    float playtimeHours = ((Integer) game.get("playtime")).floatValue() / 60f;
                    pg.setTimePlayed(playtimeHours);
                    gamesToAdd.add(pg);
                    existingGameNames.add(name);
                }
            }else{
                for (PersonalVideogame pg : existingGames) {
                    if (pg.getVideogame().getName().equals(name)) {
                        float playtimeHours = ((Integer) game.get("playtime")).floatValue() / 60f;
                        pg.setTimePlayed(playtimeHours);
                        gamesToAdd.add(pg);
                    }
                }
            }
        }

        List<PersonalVideogame> gamesToRemove = new ArrayList<>();
        for (PersonalVideogame pv : gamesToAdd) {
            try {
                personalVideogameRepository.save(pv);
            } catch (Exception e) {
                System.out.println(e);
                gamesToRemove.add(pv);
            }
        }
        gamesToAdd.removeAll(gamesToRemove);

        collection.setCollectionGames(gamesToAdd);
        collectionRepository.save(collection);

        return gamesToAdd;
    }

}
