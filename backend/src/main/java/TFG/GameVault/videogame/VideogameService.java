package TFG.GameVault.videogame;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import TFG.GameVault.DTOs.VideogameDto;
import TFG.GameVault.user.User;
import TFG.GameVault.user.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class VideogameService {
    
    @Autowired
    private final VideogameRepository videogameRepository;

    @Autowired
    private final UserRepository ur;

    @Transactional
    public Videogame saveGame(Videogame videogame){
        if(!videogameRepository.findByName(videogame.getName()).isPresent()){
            return videogameRepository.save(videogame);
        }else{
            return null;
        }
        
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
        LocalDate date = videogame.getReleaseDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dateString = date.format(formatter);
        return new VideogameDto(videogame.getId(), videogame.getName(), videogame.getDescription(), videogame.getImage(),
         videogame.getPlatforms(), videogame.getGenres(), videogame.getDeveloper(), videogame.getPublisher(),
         dateString);
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

    public Specification<Videogame> getSpec(GamesFilter filter){
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

        return spec;
    }

    @Transactional
    public List<Object> filterGames(GamesFilter filter, Pageable pageable, Integer userId) {
        Specification<Videogame> spec = getSpec(filter);
        if(userId != null){
            spec = spec.and(VideogameSpecifications.isWhishlistedBy(userId));
        }
        Page<Videogame> page = videogameRepository.findAll(spec, pageable);
        List<VideogameDto> games = page.getContent().stream()
            .map(this::transformToDTO)
            .collect(Collectors.toList());

        Integer totalPages = page.getTotalPages();
        List<Object> res = Arrays.asList(games, totalPages);

        return res;
    }

    public void deleteFromWishlist(Integer userId, Integer gameId) {
        Videogame game = videogameRepository.findById(gameId).get();
        List<User> users = game.getUsersWhishlited();
        users.removeIf(user->user.getId().equals(userId));
        game.setUsersWhishlited(users);
        videogameRepository.save(game);
    }

    public void addToWishlist(Integer userId, Integer gameId) {
        User user = ur.findById(userId).get();
        Videogame game = videogameRepository.findById(gameId).get();
        if(!game.getUsersWhishlited().contains(user)){
            List<User> users = game.getUsersWhishlited();
            users.add(user);
            game.setUsersWhishlited(users);
            videogameRepository.save(game);
        }
    }
}
