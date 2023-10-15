package br.com.luizmaciel.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

// Tô dizendo que vou criar um controller (rotas) é a primeira camadade acesso do usuário a aplicação
@RestController
// Nome do controller (rotas)
@RequestMapping("/users")
public class UserController {
    // Coletando todos os metodos do repository e instanciando a classe através do
    // Autowired
    @Autowired
    private IUserRepository userRepository;

    // Tipo da requisição HTTP
    @PostMapping("/")
    // RequestBody estou dizendo que o meu parametro vai vim do corpo da requisição
    public ResponseEntity createUser(@RequestBody UserModel userModel) {
        var user = this.userRepository.findByUsername(userModel.getUsername());

        if (user != null) {
            // mensagem de erro de já existir usuário
            // Status code
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já existe");
        }

        var passwordHashed = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
        userModel.setPassword(passwordHashed);

        var userCreated = this.userRepository.save(userModel);
        return ResponseEntity.status(HttpStatus.OK).body(userCreated);

        // MVN SPRING-BOOT:RUN
    }
}
