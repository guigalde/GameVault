package TFG.GameVault.videogame;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class VideogameService {
    
    private final VideogameRepository videogameRepository;

    public Videogame saveGame(Videogame videogame){
        return videogameRepository.save(videogame);
    }

    public Videogame updateGame(Videogame videogame){
        return videogameRepository.save(videogame);
    }

    public void deleteGame(Integer id){
        videogameRepository.deleteById(id);
    }

    public Videogame getGame(Integer id){
        return videogameRepository.findById(id).orElse(null);
    }

    public List<Videogame> getGames(){
        return videogameRepository.findAll();
    }
}
