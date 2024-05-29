package TFG.GameVault.personal_videogame;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import TFG.GameVault.collections.Collection;

public class PersonalVideogameSpecifications {
    
    public static Specification<PersonalVideogame> markInInterval(Float minMark, Float maxMark){
        return (personalVideogame, cq, cb) ->{
            if (minMark == null && maxMark == null) {
                return cb.conjunction();
            }else if(minMark == null){
                return cb.lessThanOrEqualTo(personalVideogame.get("mark"), maxMark);
            }else if(maxMark == null){
                return cb.greaterThanOrEqualTo(personalVideogame.get("mark"), minMark);
            }else{
                return cb.between(personalVideogame.get("mark"), minMark, maxMark);
            }
        };
    }

    public static Specification<PersonalVideogame> timePlayedInInterval(Float minTimePlayed, Float maxTimePlayed){
        return (personalVideogame, cq, cb) ->{
            if (minTimePlayed == null && maxTimePlayed == null) {
                return cb.conjunction();
            }else if(minTimePlayed == null){
                return cb.lessThanOrEqualTo(personalVideogame.get("timePlayed"), maxTimePlayed);
            }else if(maxTimePlayed == null){
                return cb.greaterThanOrEqualTo(personalVideogame.get("timePlayed"), minTimePlayed);
            }else{
                return cb.between(personalVideogame.get("timePlayed"), minTimePlayed, maxTimePlayed);
            }
        };
    }

    public static Specification<PersonalVideogame> completed(Boolean completed){
        return (personalVideogame, cq, cb) ->{
            if (completed == null) {
                return cb.conjunction();
            }else if(completed == true){
                return cb.isNotNull(personalVideogame.get("completionTime"));
            }else{
                return cb.isNull(personalVideogame.get("completionTime"));
            }
            
        };
    }

    public static Specification<PersonalVideogame> user(Integer userId){
        return (personalVideogame, cq, cb) ->{
            return cb.equal(personalVideogame.get("user").get("id"), userId);
        };
    }

    public static Specification<PersonalVideogame> hasPlatform(String platform){
        return (personalVideogame, cq, cb) ->{
            if (platform == null || platform.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(personalVideogame.get("videogame").get("platforms")), "%"+platform.toLowerCase()+"%");
        };
    }

    public static Specification<PersonalVideogame> hasPublisher(String publisher){
        return (personalVideogame, cq, cb) ->{
            if (publisher == null || publisher.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(personalVideogame.get("videogame").get("publisher")), "%"+publisher.toLowerCase()+"%");
        };
    }

    public static Specification<PersonalVideogame> filterByDate(LocalDate minReleaseDate, LocalDate maxReleaseDate){
        return (personalVideogame, cq, cb) ->{
            if (minReleaseDate == null && maxReleaseDate == null) {
                return cb.conjunction();
            }else if(minReleaseDate == null){
                return cb.lessThanOrEqualTo(personalVideogame.get("videogame").get("releaseDate"), maxReleaseDate);
            }else if(maxReleaseDate == null){
                return cb.greaterThanOrEqualTo(personalVideogame.get("videogame").get("releaseDate"), minReleaseDate);
            }else{
                return cb.between(personalVideogame.get("videogame").get("releaseDate"), minReleaseDate, maxReleaseDate);
            }
        };
    }

    public static Specification<PersonalVideogame> hasGenre(String genre){
        return (personalVideogame, cq, cb) ->{
            if (genre == null || genre.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(personalVideogame.get("videogame").get("genres")), "%"+genre.toLowerCase()+"%");
        };
    }
    
    public static Specification<PersonalVideogame> search(String searchTerms){
        return (personalVideogame, cq, cb) ->{
            if (searchTerms == null || searchTerms.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(personalVideogame.get("videogame").get("name")), "%" + searchTerms.toLowerCase() + "%");
        };
    }

    public static Specification<PersonalVideogame> isInCollection(Collection collection){
        return (personalVideogame, cq, cb) ->{
            if (collection == null) {
                return cb.conjunction();
            }
            return cb.isMember(collection, personalVideogame.get("collections"));
        };
    }
}
