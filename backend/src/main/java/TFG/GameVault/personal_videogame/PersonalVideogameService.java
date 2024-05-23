package TFG.GameVault.personal_videogame;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import TFG.GameVault.DTOs.PersonalVideogameBasicInfo;
import TFG.GameVault.DTOs.PersonalVideogameDto;
import TFG.GameVault.DTOs.PersonalVideogameInfoDto;
import TFG.GameVault.DTOs.VideogameDto;
import TFG.GameVault.collections.CollectionRepository;
import TFG.GameVault.collections.CollectionService;
import TFG.GameVault.user.User;
import TFG.GameVault.user.UserService;
import TFG.GameVault.videogame.Videogame;
import TFG.GameVault.videogame.VideogameService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

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
    public List<Object> applyFilters(Integer userId, PersonalVideogameFilter filter, Pageable page){
        List<Object> res = new ArrayList<>();
        Specification<PersonalVideogame> spec = Specification.where(PersonalVideogameSpecifications.user(userId));
        if(filter != null){
            spec = spec.and(PersonalVideogameSpecifications.completed(filter.getCompleted()))
                .and(PersonalVideogameSpecifications.hasPlatform(filter.getPlatform()))
                .and(PersonalVideogameSpecifications.markInInterval(filter.getMinMark(), filter.getMaxMark()))
                .and(PersonalVideogameSpecifications.timePlayedInInterval(filter.getMinTimePlayed(), filter.getMaxTimePlayed()))
                .and(PersonalVideogameSpecifications.hasGenre(filter.getGenre()))
                .and(PersonalVideogameSpecifications.hasPublisher(filter.getPublisher()))
                .and(PersonalVideogameSpecifications.search(filter.getSearchTerms()))
                .and(PersonalVideogameSpecifications.filterByDate(filter.getMinReleaseDate(), filter.getMaxReleaseDate()));
            
            if(filter.getMarkSort()!=null &&filter.getMarkSort()==true){
                page = PageRequest.of(page.getPageNumber(), page.getPageSize(), Direction.ASC, "mark");
            }else if(filter.getMarkSort()!=null && filter.getMarkSort()==false){
                page = PageRequest.of(page.getPageNumber(), page.getPageSize(), Direction.DESC, "mark");
            }else if(filter.getTimePlayedSort()!=null && filter.getTimePlayedSort()==true){
                page = PageRequest.of(page.getPageNumber(), page.getPageSize(), Direction.ASC, "timePlayed");
            }else if(filter.getTimePlayedSort()!=null && filter.getTimePlayedSort()==false){
                page = PageRequest.of(page.getPageNumber(), page.getPageSize(), Direction.DESC, "timePlayed");
            }
        }
        Page<PersonalVideogame>  personalVideogames = personalVideogameRepository.findAll(spec, page);
        List<PersonalVideogameInfoDto> personalVideogameDtos = new ArrayList<>();
        for (PersonalVideogame personalVideogame : personalVideogames) {
            personalVideogameDtos.add(toInfoDto(personalVideogame));
        }
        res.add(personalVideogameDtos);
        res.add(personalVideogames.getTotalPages());

        return res;
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

}
