package br.com.fms.toodolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.fms.toodolist.user.IUserRepository;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // pegar a autenticação do header
        var authorization = request.getHeader("Authorization");
        // remove o prefixo Basic
        var authEncode = authorization.substring("Basic".length()).trim();
        // decodifica o base64
        byte[] authDecode = Base64.getDecoder().decode(authEncode);
        // converte para string
        String auth = new String(authDecode);
        // separa o usuario e senha
        String[] userPass = auth.split(":");
        String user = userPass[0];
        String pass = userPass[1];
        // validar usuario
        var userLocal = this.userRepository.findByUserName(user);
        if (userLocal == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }else{
            //validar senha
            if(!BCrypt.verifyer().verify(pass.toCharArray(), userLocal.getPassword()).verified){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            //se tudo ok, segue o fluxo
            filterChain.doFilter(request, response);
        }

    }

}
