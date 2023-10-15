package br.com.luizmaciel.todolist.user;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

// Getters e Setters de forma automatizada
@Data
// @Getters só getters
// @Setters só setters
// Criando tabela no BD em memoria
@Entity(name = "tb_users")
public class UserModel {
    // Criando ID da tabela do tipo UUID e colocando para ser gerado automaticamente
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    // Atributos de um usuário
    // Campo username tem que ser unico não pode se repetir
    @Column(unique = true)
    private String username;
    private String password;
    private String name;
    // Hora em que os dados foram inseridos no BD
    @CreationTimestamp
    private LocalDateTime createdAt;
}
