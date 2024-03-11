package TFG.GameVault.game;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GameService {
    
    private final GameRepository gameRepository;

    public Game saveGame(Game game){
        return gameRepository.save(game);
    }

    public Game updateGame(Game game){
        return gameRepository.save(game);
    }

    public void deleteGame(Integer id){
        gameRepository.deleteById(id);
    }

    public Game getGame(Integer id){
        return gameRepository.findById(id).orElse(null);
    }

    public List<Game> getGames(){
        return gameRepository.findAll();
    }
}
