package TFG.GameVault.videogame;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class VideogameService {
    
    private final VideogameRepository videogameRepository;

    public Videogame saveGame(Videogame videogame){
        if(!videogameRepository.findByName(videogame.getName()).isPresent()){
            return videogameRepository.save(videogame);
        }else{
            return null;
        }
        
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

    public List<Videogame> filterGames(GamesFilter filter, Pageable pageable) {
        Specification<Videogame> spec = Specification.where(null);

        if (filter.getPlatform() != null && !filter.getPlatform().isEmpty()) {
            spec = spec.and(VideogameSpecifications.hasPlatform(filter.getPlatform()));
        }
        if (filter.getPublisher() != null && !filter.getPublisher().isEmpty()) {
            spec = spec.and(VideogameSpecifications.hasPublisher(filter.getPublisher()));
        }
        if (filter.getMinReleaseDate() != null) {
            spec = spec.and(VideogameSpecifications.filterByDate(filter.getMinReleaseDate(), filter.getMaxReleaseDate()));
        }
        if (filter.getSearchTerms() != null && !filter.getSearchTerms().isEmpty()) {
            spec = spec.and(VideogameSpecifications.search(filter.getSearchTerms()));
        }

        return videogameRepository.findAll(spec, pageable).toList();
    }
}
