package br.com.luizmaciel.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.luizmaciel.todolist.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {
    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var serverletPath = request.getServletPath();

        if (serverletPath.startsWith("/tasks/")) {
            // Pegar header de autenticação (usuário e senha)
            var authorization = request.getHeader("Authorization");
            // Formatando header
            var authEncoded = authorization.substring("Basic".length()).trim();
            // Decodeficando ele de base 64 para byte
            byte[] authDecoded = Base64.getDecoder().decode(authEncoded);
            // Transformando de byte para string
            var authString = new String(authDecoded);
            // Formatando a string
            String[] crendentials = authString.split(":");
            var username = crendentials[0];
            var password = crendentials[1];
            // Validar usuário
            var user = this.userRepository.findByUsername(username);
            if (user == null) {
                response.sendError(401);// Usuário não autorizado porque não possui nenhum username no sistema
            } else {
                // Validar password (verificar se o password é igual ao password que está no
                // banco)
                var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                if (passwordVerify.verified) {
                    request.setAttribute("idUser", user.getId());
                    filterChain.doFilter(request, response);// usuário permitido para seguir o fluxo
                } else {
                    response.sendError(401);// Usuário não autorizado porque o password está errado
                }
            }
        } else {
            filterChain.doFilter(request, response);// usuário permitido para seguir o fluxo
        }
    }
}
