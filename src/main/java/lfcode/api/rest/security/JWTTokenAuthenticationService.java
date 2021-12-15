package lfcode.api.rest.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lfcode.api.rest.ApplicationContextLoad;
import lfcode.api.rest.models.UserModel;
import lfcode.api.rest.repositories.UserRepository;

@Service
@Component
public class JWTTokenAuthenticationService {
	
	/** Tempo de validade do TOKEN - 120 dias*/
	private static final long EXPIRATION_TIME = 86400000;

	/** senha unica para compor a autenticação  e ajudar na segurança*/
	private static final String SECRET = "Fernando_Oliveira_TOKEN";

	/** prefixo padrão de token*/
	private static final String TOKEN_PREFIX = "Bearer";

	
	private static final String HEADER_STRING = "Authorization";
	
	
	
	/** Gerando token de autenticação e adicionando ao cabeçalho e resposta HTTP*/
	public void addAuthentication(HttpServletResponse response, String username) throws IOException {
		
		/** Montagem do token */
		String JWT = Jwts.builder() /** Chama o gerador de token*/
				.setSubject(username) /** Adiciona o usuario*/
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME ))/* tempo de expiração*/
				.signWith(SignatureAlgorithm.HS512, SECRET).compact(); /* compactação e algoritimo de geração de senha*/
		
		/** Junta o token com o prefixo */
		String token = TOKEN_PREFIX + " " + JWT;
		
		/** Adiciona no cabeçalho http */
		response.addHeader(HEADER_STRING, token);
		
		ApplicationContextLoad.getApplicationContext()
		.getBean(UserRepository.class).updateTokenUser(JWT, username);
		
		/** Adiciona token como resposta body http */
		response.getWriter().write("{\"Authorization\": \""+token+"\"}");
		
	}
	/** Retorna o usuario valido com token ou caso ñ  seja valido retorna nuul */
	public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response ) {
		
		/* Pega o token enviado no cabeçalho http */
		String token = request.getHeader(HEADER_STRING);
		
		try {
			
		if (token != null) {
			
			String tokenClear = token.replace(TOKEN_PREFIX, "").trim();
			
			/* faz a validação do token do user na requisição*/
			String user = Jwts.parser().setSigningKey(SECRET)
								.parseClaimsJws(tokenClear)
								.getBody().getSubject();
			if(user != null) {
				
				UserModel userModel = ApplicationContextLoad.getApplicationContext()
						.getBean(UserRepository.class).findUserByLogin(user);
				
				if (userModel != null) {
					
					if (tokenClear.equalsIgnoreCase(userModel.getToken())) {
					
					return new UsernamePasswordAuthenticationToken(
							userModel.getLogin(),
							userModel.getPassword(),
							userModel.getAuthorities());
					}
				}
			
			}
		}
		}catch (io.jsonwebtoken.ExpiredJwtException e){
			try {
				response.getOutputStream().println("Your Token is expired, login again!");
			} catch (IOException e1) { }
			
		}
		
			/** Liberando o Cors*/
			releaseCors(response);

			return null; /* não autorizado*/
		}
	private void releaseCors(HttpServletResponse response) {
		
		if (response.getHeader("Access-Control-Allow-Origin") == null) {
			response.addHeader("Access-Control-Allow-Origin", "*");		
		}
		
		if (response.getHeader("Access-Control-Allow-Headers") == null) {
			response.addHeader("Access-Control-Allow-Headers", "*");		
		}
		
		if (response.getHeader("Access-Control-Request-Headers") == null) {
			response.addHeader("Access-Control-Request-Headers", "*");		
		}
		
		if (response.getHeader("Access-Control-Allow-Methods") == null) {
			response.addHeader("Access-Control-Allow-Methods", "*");		
		}
	
		
	}
	}		

