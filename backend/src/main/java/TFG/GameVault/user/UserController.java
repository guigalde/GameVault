package TFG.GameVault.user;


import org.springframework.web.bind.annotation.RestController;

import TFG.GameVault.DTOs.CredentialsDto;
import TFG.GameVault.DTOs.SignUpDto;
import TFG.GameVault.DTOs.UserDto;
import TFG.GameVault.config.UserAuthenticationProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.net.URI;
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



@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService us;
    
    @Autowired
    private final UserAuthenticationProvider userAuthenticationProvider;

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
        public ResponseEntity<UserDto> updateUser(@PathVariable Integer id, @RequestBody @Valid UserDto userDto) {
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
    
}
