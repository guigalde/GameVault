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
    
    private final UserAuthenticationProvider userAuthenticationProvider = new UserAuthenticationProvider();

        @PostMapping("/login")
        public ResponseEntity<UserDto> login(@RequestBody @Valid CredentialsDto credentialsDto) {
            UserDto userDto = us.login(credentialsDto);
            userDto.setToken(userAuthenticationProvider.createToken(userDto));
            return ResponseEntity.ok(userDto);
        }

        @PostMapping("/register")
        public ResponseEntity<UserDto> register(@RequestBody @Valid SignUpDto user) {
            UserDto createdUser = us.register(user);
            createdUser.setToken(userAuthenticationProvider.createToken(createdUser));
            return ResponseEntity.created(URI.create("/users/" + createdUser.getId())).body(createdUser);
        }

        @PutMapping("/users/{id}")
        public ResponseEntity<UserDto> updateUser(@PathVariable Integer id, @RequestBody @Valid UserDto userDto) {
            return ResponseEntity.ok(us.updateUser(id, userDto));
        }

        @DeleteMapping("/users/{id}")
        public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
            us.deleteUser(id);
            String message = "User successfully deleted";
            return ResponseEntity.ok(message);
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
