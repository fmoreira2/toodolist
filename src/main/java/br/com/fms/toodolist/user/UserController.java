package br.com.fms.toodolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody UserModel user) {

        var userlocal = this.userRepository.findByUserName(user.getUserName());

        if (userlocal != null) {
            return ResponseEntity.badRequest().body("Usuário já cadastrado");
        }

        var hash = BCrypt.withDefaults()
                   .hashToString(12, user.getPassword().toCharArray());
        //set password com o hash
        user.setPassword(hash);
        var userCreate = this.userRepository.save(user);
        return ResponseEntity.created(null).body(userCreate);
    }

    @GetMapping("/list")
    public Iterable<UserModel> list() {

        var users = this.userRepository.findAll();

        return users;
    }

}
