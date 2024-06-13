package TFG.GameVault.user;


import org.springframework.web.bind.annotation.RestController;

import TFG.GameVault.API_Consumers.Steam_consumer;
import TFG.GameVault.DTOs.CredentialsDto;
import TFG.GameVault.DTOs.PersonalVideogameBasicInfo;
import TFG.GameVault.DTOs.PersonalVideogameInfoDto;
import TFG.GameVault.DTOs.SignUpDto;
import TFG.GameVault.DTOs.UserDto;
import TFG.GameVault.DTOs.VideogameNewsDto;
import TFG.GameVault.config.UserAuthenticationProvider;
import TFG.GameVault.personal_videogame.PersonalVideogame;
import TFG.GameVault.personal_videogame.PersonalVideogameService;
import io.micrometer.core.ipc.http.HttpSender.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService us;

    @Autowired
    private Steam_consumer steamConsumer;
    
    @Autowired
    private final UserAuthenticationProvider userAuthenticationProvider;

    @Autowired
    private PersonalVideogameService ps;

        @PostMapping("/login")
        public ResponseEntity<UserDto> login(@RequestBody @Valid CredentialsDto credentialsDto) {
            try{
                UserDto userDto = us.login(credentialsDto);
                userDto.setToken(userAuthenticationProvider.createToken(userDto));
                return ResponseEntity.ok(userDto);
            } catch (Exception e) {
                return ResponseEntity.badRequest().build();
            }
        }

        @PostMapping("/register")
        public ResponseEntity<UserDto> register(@RequestBody @Valid SignUpDto user) {
            try{
                UserDto createdUser = us.register(user);
                createdUser.setToken(userAuthenticationProvider.createToken(createdUser));
                return ResponseEntity.created(URI.create("/users/" + createdUser.getId())).body(createdUser);
            } catch (Exception e) {
                return ResponseEntity.badRequest().build();
            }
        }

        @PutMapping("/users/{id}")
        public ResponseEntity<UserDto> updateUser(@PathVariable Integer id, @RequestBody @Valid SignUpDto userDto) {
            try{
                UserDto updatedUser = us.updateUser(id, userDto);
                updatedUser.setToken(userAuthenticationProvider.createToken(updatedUser));
                return ResponseEntity.ok(updatedUser);
            } catch (Exception e) {
                return ResponseEntity.badRequest().build();
            }
        }

        @DeleteMapping("/users/{id}")
        public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
            try{
                us.deleteUser(id);
                String message = "User successfully deleted";
                return ResponseEntity.ok(message);
            } catch (Exception e) {
                return ResponseEntity.badRequest().build();
            }
        }

        @GetMapping("/users/usernames")
        public ResponseEntity<List<String>> findUsernames() {
            return ResponseEntity.ok(us.findUsernames());
        }
        
        @GetMapping("/users/emails")
        public ResponseEntity<List<String>> findEmails() {
            return ResponseEntity.ok(us.findEmails());
        }

        @PostMapping("/user/gameNews/{userId}")
        public ResponseEntity<String> setNews(@RequestBody List<PersonalVideogameInfoDto> games, @PathVariable Integer userId) {
            try{
                List<Integer> gameIds = games.stream().map(game -> game.getSteamId()).toList();
                us.setGamesNews(userId, gameIds);
                return ResponseEntity.ok().body("Games news set successfully");
            }catch(Exception e){
                return ResponseEntity.badRequest().body("Error setting games news");
            }
        }
        

        @GetMapping("/user/getNews/{userId}")
        public ResponseEntity<?> getNews(@PathVariable Integer userId) {
            try{
                User user = us.findById(userId);
                if(user.getSteamGamesNewsIds() == null || user.getSteamGamesNewsIds().isEmpty()){
                    return ResponseEntity.ok().body(new ArrayList<VideogameNewsDto>());
                }else{
                    List<String> appIds = Arrays.stream(user.getSteamGamesNewsIds().split(",")).map(appId -> appId.trim()).toList();
                    List<VideogameNewsDto> news = new ArrayList<>();
                    for(String appId: appIds){
                        PersonalVideogame pv = ps.frindByUserIdAndSteamId(userId, Integer.valueOf(appId));
                        List<VideogameNewsDto> appNews = steamConsumer.getNews(appId);
                        appNews.forEach(newsDto -> newsDto.setGameName(pv.getVideogame().getName()));
                        if(news != null){
                            news.addAll(appNews);
                        }
                    }
                    return ResponseEntity.ok().body(news);
                }
            }catch(Exception e){
                return ResponseEntity.badRequest().body("Error getting games news");
            }
        }

        @GetMapping("/user/clearNewsPreferences/{userId}")
        public ResponseEntity<String> clearNewsPreferences(@PathVariable Integer userId) {
            try{
                User user = us.findById(userId);
                user.setSteamGamesNewsIds("");
                us.saveUser(user);
                return ResponseEntity.ok().body("News preferences cleared successfully");
            }catch(Exception e){
                return ResponseEntity.badRequest().body("Error clearing news preferences");
            }
        }
        
    
}
