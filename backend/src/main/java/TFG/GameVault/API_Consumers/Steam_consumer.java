package TFG.GameVault.API_Consumers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import TFG.GameVault.DTOs.VideogameNewsDto;


@Component
public class Steam_consumer {

    @Value("${steam.api.key}")
    String apiKey;

    public List<Map<String,?>> getGames(String steamId) throws UnirestException{
        String url = "http://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key="+apiKey+"&steamid="+steamId+"&format=json&include_appinfo=true";
        try{
            HttpResponse<JsonNode> response = Unirest.get(url).asJson();
            JSONArray games = response.getBody().getObject().getJSONObject("response").getJSONArray("games");
            List<Map<String,?>> gamesMap = new ArrayList<>();
            for(int i = 0; i<games.length(); i++){
                Map<String,Object> gameMap = new HashMap<>();
                gameMap.put("appid", Integer.valueOf(games.getJSONObject(i).get("appid").toString()));
                gameMap.put("name", games.getJSONObject(i).get("name"));
                gameMap.put("playtime", Integer.valueOf(games.getJSONObject(i).get("playtime_forever").toString()));
                gamesMap.add(gameMap);
            }
            return gamesMap;
        }catch(UnirestException e){
            System.out.println(e);
            throw e;
        }
    }

    public List<VideogameNewsDto> getNews(String appId) throws  UnirestException{
        String url = "https://api.steampowered.com/ISteamNews/GetNewsForApp/v0002/?appid="+appId+"&maxlength=4294967295&count=3&format=json";
        try{
            List<VideogameNewsDto> newsList = new ArrayList<>();
            HttpResponse<JsonNode> response = Unirest.get(url).asJson();
            JSONArray news = response.getBody().getObject().getJSONObject("appnews").getJSONArray("newsitems");
            if(news.length() == 0){
                return null;
            }
            for(int i=0; i<news.length(); i++){
                VideogameNewsDto newsDto = new VideogameNewsDto();
                newsDto.setNewsTitle(news.getJSONObject(i).get("title").toString());
                newsDto.setNewsContent(news.getJSONObject(i).get("contents").toString());
                newsDto.setNewsUrl(news.getJSONObject(i).get("url").toString());
                newsList.add(newsDto);
            }
            return newsList;
        }catch(UnirestException e){
            System.out.println(e);
            throw e;
        }
    }
    
}
