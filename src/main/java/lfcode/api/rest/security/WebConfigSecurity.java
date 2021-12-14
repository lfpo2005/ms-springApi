package lfcode.api.rest.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lfcode.api.rest.services.ImplUserDetailsService;

@Configuration
@EnableWebSecurity
public class WebConfigSecurity extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private ImplUserDetailsService implUserDetailsService;
	
	/** Configura as solicitatções de acesso via http*/
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		/** Ativa a proteção contra user ñ estão validados por TOKEN */		
		http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
				
		/** Ativando a pagina principal*/		
		.disable().authorizeRequests().antMatchers("/").permitAll()
		.antMatchers("/index").permitAll()
		
		/** libere cors para todas as chamadas */
		.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()		
		
		/** URL de Logout- Redireciona apos o user deslogar*/
		.anyRequest().authenticated().and().logout().logoutSuccessUrl("/index")
		
		/*Mapeia a URL de Logout e invalida user*/
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		
		/** Filtra as req de login para a autenticação */
		.and().addFilterBefore(new JWTLoginFilter("/login", authenticationManager()), UsernamePasswordAuthenticationFilter.class)

		/** Filtra demais req para verificar TOKEN JWT no HEADER HTTP*/
		.addFilterBefore(new JWTApiAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);		

	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		/** Service ira consulta user no db */
		auth.userDetailsService(implUserDetailsService)
		/** padrão de cliptocrafia de senha*/
		.passwordEncoder(new BCryptPasswordEncoder());
	}

}
