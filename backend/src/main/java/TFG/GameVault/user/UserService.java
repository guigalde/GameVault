package TFG.GameVault.user;

import java.nio.CharBuffer;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import TFG.GameVault.DTOs.CredentialsDto;
import TFG.GameVault.DTOs.SignUpDto;
import TFG.GameVault.DTOs.UserDto;
import TFG.GameVault.exceptions.AppException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository ur;
    @Autowired
    private RoleRepository rr;

    public final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<User> findAllUsers(){
        return (List<User>) ur.findAll();
    }

    @Transactional
    public void saveUser(User user){
        ur.save(user);
    }

    @Transactional
    public void deleteUser(Integer id){
        User user = ur.findById(id).orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
        ur.delete(user);
    }

    
    @Transactional(readOnly = true)
    public User findByUsername(String username){
        User user = ur.findByUsername(username).orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
        return user;
    }

    public UserDto findByUsernameDto(String username) {
        User user = findByUsername(username);
        return toUserDto(user);
    }

    public UserDto toUserDto(User user){

        UserDto userDto = new UserDto(user.getId(),user.getUsername(),user.getEmail(), user.getRole().getRoleName().toString(), null);
        return userDto;
    }

    public User fromRegisterToUser(SignUpDto signup){

        Optional<Role> role = rr.findById(2);
        User user = new User(signup.getUsername(), passwordEncoder.encode(CharBuffer.wrap(signup.getPassword())), signup.getEmail(), role.get(), null);
        return user;
    }

    public UserDto login(CredentialsDto credentialsDto){
        User user = ur.findByUsername(credentialsDto.getUsername())
            .orElseThrow(()-> new AppException("Unknown user", HttpStatus.NOT_FOUND));
        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.getPassword()), user.getPassword())){
            return toUserDto(user);
        }

        throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public UserDto updateUser(Integer id, UserDto userDto){
        User user = ur.findById(id).orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        ur.save(user);
        return toUserDto(user);
    }

    @Transactional
    public UserDto register(SignUpDto signup){
        
        Optional<User> optionalUser = ur.findByUsername(signup.getUsername());
        if(optionalUser.isPresent()){
            throw new AppException("User already exists", HttpStatus.BAD_REQUEST);
        }

        User user = fromRegisterToUser(signup);

        User savedUser = ur.save(user);

        return toUserDto(savedUser);
    }

    @Transactional(readOnly = true)
    public List<String> findUsernames(){
        return ur.findUsernames();
    }

    @Transactional(readOnly = true)
    public List<String> findEmails(){
        return ur.findEmails();
    }

    public User findByUsername(Integer user_id) {
        return ur.findById(user_id).orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
    }
}
