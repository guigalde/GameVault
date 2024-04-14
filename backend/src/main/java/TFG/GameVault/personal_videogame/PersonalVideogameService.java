package TFG.GameVault.personal_videogame;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import TFG.GameVault.DTOs.PersonalVideogameDto;
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

    @Transactional
    public PersonalVideogame savePersonalVideogame(PersonalVideogame personalVideogame){
        return personalVideogameRepository.save(personalVideogame);
    }

    @Transactional
    public PersonalVideogame findGameById(Integer id){
        return personalVideogameRepository.findById(id).orElse(null);
    }

    @Transactional
    public List<PersonalVideogame> findGamesByUser(User user){
        return personalVideogameRepository.findByUser(user);
    }

    @Transactional
    public void deleteGame(Integer id){
        personalVideogameRepository.deleteById(id);
    }

    public PersonalVideogame fromDTO(PersonalVideogameDto dto, Integer user_id){
        User user = userService.findByUsername(user_id);
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

}
