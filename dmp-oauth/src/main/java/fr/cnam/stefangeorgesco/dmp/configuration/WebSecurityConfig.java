package fr.cnam.stefangeorgesco.dmp.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;
import java.util.List;

@Configuration
public class WebSecurityConfig {

	@Value("${frontend.url}")
	private String frontEndUrl;
	
	@Value("${keycloak.principal-attribute}")
	private String principalClaimName;

	@Bean
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());
        jwtAuthenticationConverter.setPrincipalClaimName(principalClaimName);
		
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().cors()
				.configurationSource(request -> {
					CorsConfiguration config = new CorsConfiguration();
					config.setAllowedOriginPatterns(Collections.singletonList(frontEndUrl));
					config.setAllowedMethods(Collections.singletonList("*"));
					config.setAllowCredentials(true);
					config.setAllowedHeaders(Collections.singletonList("*"));
					config.setExposedHeaders(List.of("Authorization"));
					config.setMaxAge(3600L);
					return config;
				}).and().csrf().disable()
				.authorizeHttpRequests()
						.mvcMatchers(HttpMethod.POST, "/doctor").hasRole("ADMIN")
						.mvcMatchers(HttpMethod.POST, "/patient-file").hasRole("DOCTOR")
						.mvcMatchers(HttpMethod.GET, "/doctor/details").hasRole("DOCTOR")
						.mvcMatchers(HttpMethod.PUT, "/doctor/details").hasRole("DOCTOR")
						.mvcMatchers(HttpMethod.GET, "/patient-file/details").hasRole("PATIENT")
						.mvcMatchers(HttpMethod.PUT, "/patient-file/details").hasRole("PATIENT")
						.mvcMatchers(HttpMethod.GET, "/patient-file/details/correspondence").hasRole("PATIENT")
						.mvcMatchers(HttpMethod.GET, "/patient-file/details/item").hasRole("PATIENT")
						.mvcMatchers(HttpMethod.PUT, "/patient-file/{id}/referring-doctor").hasRole("ADMIN")
						.mvcMatchers(HttpMethod.GET, "/doctor/{id}").authenticated()
						.mvcMatchers(HttpMethod.GET, "/patient-file/{id}").hasAnyRole("ADMIN", "DOCTOR")
						.mvcMatchers(HttpMethod.DELETE, "/doctor/{id}").hasRole("ADMIN")
						.mvcMatchers(HttpMethod.GET, "/doctor").hasAnyRole("ADMIN", "DOCTOR")
						.mvcMatchers(HttpMethod.GET, "/patient-file").hasAnyRole("ADMIN", "DOCTOR")
						.mvcMatchers(HttpMethod.DELETE, "/patient-file/{id}").hasRole("ADMIN")
						.mvcMatchers(HttpMethod.GET, "/patient-file/{id}/correspondence").hasRole("DOCTOR")
						.mvcMatchers(HttpMethod.POST, "/patient-file/{id}/correspondence").hasRole("DOCTOR")
						.mvcMatchers(HttpMethod.POST, "/patient-file/{id}/item").hasRole("DOCTOR")
						.mvcMatchers(HttpMethod.GET, "/patient-file/{id}/item").hasRole("DOCTOR")
						.mvcMatchers(HttpMethod.PUT, "/patient-file/{patientFileId}/item/{itemId}").hasRole("DOCTOR")
						.mvcMatchers(HttpMethod.DELETE, "/patient-file/{patientFileId}/item/{itemId}").hasRole("DOCTOR")
						.mvcMatchers(HttpMethod.DELETE,
								"/patient-file/{patientFileId}/correspondence/{correspondenceId}").hasRole("DOCTOR")
						.mvcMatchers(HttpMethod.GET, "/specialty/{id}").hasRole("ADMIN")
						.mvcMatchers(HttpMethod.GET, "/specialty").hasRole("ADMIN")
						.mvcMatchers(HttpMethod.GET, "/disease/{id}").hasRole("DOCTOR")
						.mvcMatchers(HttpMethod.GET, "/disease").hasRole("DOCTOR")
						.mvcMatchers(HttpMethod.GET, "/medical-act/{id}").hasRole("DOCTOR")
						.mvcMatchers(HttpMethod.GET, "/medical-act").hasRole("DOCTOR")
						.anyRequest().permitAll()
						.and().oauth2ResourceServer().jwt().jwtAuthenticationConverter(jwtAuthenticationConverter);
		
		return http.build();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
