package TFG.GameVault.API_Consumers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Component;

import com.api.igdb.apicalypse.APICalypse;
import com.api.igdb.apicalypse.Sort;
import com.api.igdb.exceptions.RequestException;
import com.api.igdb.request.IGDBWrapper;
import com.api.igdb.request.ProtoRequestKt;
import com.api.igdb.request.TwitchAuthenticator;
import com.api.igdb.utils.Endpoints;
import com.api.igdb.utils.TwitchToken;

import proto.Company;
import proto.Game;
import proto.GameResult;
import proto.InvolvedCompany;

@Component
public class IGDB_consumer {
    
    String userId = "tc0ofgmcuu0urgnudg7g3bgpyj1yzg";
    String userKey= "6as0obusv47dli8cd7yslwkq70a9bc";

     public  Map<Game, Map<String,List<String>>> getGames(){
        TwitchAuthenticator tAuth = TwitchAuthenticator.INSTANCE;
        TwitchToken token = tAuth.requestTwitchToken(userId, userKey);
        IGDBWrapper wrapper = IGDBWrapper.INSTANCE;
        Map<Game, Map<String, List<String>>> res = new HashMap<>();
        List<Game> games = new ArrayList<>();
        wrapper.setCredentials(userId, token.getAccess_token());
        
        APICalypse apicalypse = new APICalypse()
            .fields("name, genres, platforms, summary, first_release_date, involved_companies, cover;");
        try{
            games = ProtoRequestKt.games(wrapper, apicalypse);
        }catch(RequestException e){
            throw new RuntimeException("Error getting games from IGDB");
        }
        for(Game game: games){
            Map<String, List<String>> gameDetails = new HashMap<>();
            gameDetails = getGameDetails(game, wrapper, "all");
            res.put(game, gameDetails);
        }
        
        return res;
     }


//TODO: Refactor this method so it adds everything without a switch, returning the Map created a little above

     public Map<String,List<String>> getGameDetails(Game game, IGDBWrapper wrapper, String type){
        Map<String, List<String>> gameDetails = new HashMap<>();

            List<Long> genresIds = game.getGenresList().stream().map(g->g.getId()).toList();
            List<String> genreNames = new ArrayList<>();
            for(Long id: genresIds){
                APICalypse apicalypseGenres = new APICalypse().fields("name").where("id = "+id);
                try{
                    genreNames.add(ProtoRequestKt.genres(wrapper, apicalypseGenres).get(0).getName());
                }catch(RequestException e){
                    throw new RuntimeException("Error getting genres from IGDB");
                }
            
            }
        
            List<Long> platformsIds = game.getPlatformsList().stream().map(p->p.getId()).toList();
            List<String> platformNames = new ArrayList<>();
            for(Long id: platformsIds){
                APICalypse apicalypsePlatforms = new APICalypse().fields("name").where("id = "+id);
                try{
                    platformNames.add(ProtoRequestKt.platforms(wrapper, apicalypsePlatforms).get(0).getName());
                }catch(RequestException e){
                    throw new RuntimeException("Error getting platforms from IGDB");
                }
            }
            

            List<Long> involverdCompaniesIDs = game.getInvolvedCompaniesList().stream().map(c->c.getCompany().getId()).toList();
            List<InvolvedCompany> involvedCompanies = new ArrayList<>();
            for(Long id: involverdCompaniesIDs){
                APICalypse apicalypseInvolvedCompanies = new APICalypse().fields("company, developer, publisher;").where("id = "+id);
                try{
                    involvedCompanies.add(ProtoRequestKt.involvedCompanies(wrapper, apicalypseInvolvedCompanies).get(0));
                }catch(RequestException e){
                    throw new RuntimeException("Error getting companies from IGDB");
                }
            }
            List<String> developers = new ArrayList<>();
            List<String> publishers = new ArrayList<>();

            for(InvolvedCompany company: involvedCompanies){
                boolean isDeveloper = company.getDeveloper();
                boolean isPublisher = company.getPublisher();

                if (isDeveloper || isPublisher) {
                    APICalypse apicalypseCompany = new APICalypse().fields("name;").where("id = "+company.getId());
                    if(company.getDeveloper()){
                        try{
                            developers.add(ProtoRequestKt.companies(wrapper, apicalypseCompany).get(0).getName());
                        }catch(RequestException e){
                            throw new RuntimeException("No developer found for this game");
                        }
                    }else if( company.getPublisher()){
                        try{ 
                            publishers.add(ProtoRequestKt.companies(wrapper, apicalypseCompany).get(0).getName());
                        }catch(RequestException e){
                            throw new RuntimeException("No publisher found for this game");
                        }
                    }else{
                        throw new RuntimeException("Error getting companies from IGDB");
                    }
            
                }
            }

            Long id = game.getCover().getId();
            List<String> cover = new ArrayList<>();
            APICalypse apicalypseCovers = new APICalypse().fields("url;").where("id = "+id);
            try{
                cover.add(ProtoRequestKt.covers(wrapper, apicalypseCovers).get(0).getUrl());
            }catch(RequestException e){
                cover.add("No cover found for this game");
            }
                
            
            gameDetails.put("genres", genreNames);
            gameDetails.put("platforms", platformNames);
            gameDetails.put("developers", developers);
            gameDetails.put("publishers", publishers);
            gameDetails.put("cover", cover);

            return gameDetails;
     }  

    
}
