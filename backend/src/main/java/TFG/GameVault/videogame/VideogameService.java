package TFG.GameVault.videogame;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import TFG.GameVault.DTOs.VideogameDto;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class VideogameService {
    
    private final VideogameRepository videogameRepository;

    @Transactional
    public Videogame saveGame(Videogame videogame){
        if(!videogameRepository.findByName(videogame.getName()).isPresent()){
            return videogameRepository.save(videogame);
        }else{
            return null;
        }
        
    }

    @Transactional
    public Videogame updateGame(Videogame videogame){
        return videogameRepository.save(videogame);
    }

    @Transactional
    public void deleteGame(Integer id){
        videogameRepository.deleteById(id);
    }

    @Transactional
    public Videogame getGame(Integer id){
        return videogameRepository.findById(id).orElse(null);
    }

    @Transactional
    public VideogameDto transformToDTO(Videogame videogame){
        return new VideogameDto(videogame.getName(), videogame.getDescription(), videogame.getImage(), videogame.getPlatforms(), videogame.getGenres(), videogame.getDeveloper(), videogame.getPublisher(), videogame.getReleaseDate());
    }

    @Transactional
    public Set<String> getGenres(){
        return videogameRepository.getGenres().stream()
            .flatMap(g -> Arrays.stream(g.split(",")))
            .map(String::trim)
            .collect(Collectors.toSet());
    }

    @Transactional
    public Set<String> getPlatforms(){
        return videogameRepository.getPlatforms().stream()
            .flatMap(p -> Arrays.stream(p.split(",")))
            .map(String::trim)
            .collect(Collectors.toSet());
    }

    @Transactional
    public Set<String> getPublishers(){
        return videogameRepository.getPublishers().stream()
            .flatMap(p -> Arrays.stream(p.split(",")))
            .map(String::trim)
            .collect(Collectors.toSet());
    }

    @Transactional
    public List<Object> filterGames(GamesFilter filter, Pageable pageable) {
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

        if (filter.getGenre() != null && !filter.getGenre().isEmpty()) {
            spec = spec.and(VideogameSpecifications.hasGenre(filter.getGenre()));
        }
        Page<Videogame> page = videogameRepository.findAll(spec, pageable);
        List<VideogameDto> games = page.getContent().stream()
            .map(this::transformToDTO)
            .collect(Collectors.toList());

        Integer totalPages = page.getTotalPages();
        List<Object> res = Arrays.asList(games, totalPages);

        return res;
    }
}
