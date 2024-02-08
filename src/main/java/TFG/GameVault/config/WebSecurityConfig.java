package TFG.GameVault.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import TFG.GameVault.user.UserService;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

  @Autowired
  UserService userService;

  private final UserAuthenticationEntryPoint userAuthenticationEntryPoint;
  private final UserAuthenticationProvider userAuthProvider;
  private final WebConfig webConfig;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

      http.exceptionHandling(handling -> handling.authenticationEntryPoint(userAuthenticationEntryPoint))
              .addFilterBefore(webConfig.corsFilter(), CorsFilter.class)
              .addFilterBefore(new JwtAuthFilter(userAuthProvider), BasicAuthenticationFilter.class)
              .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
              .authorizeHttpRequests((requests) -> requests
              .requestMatchers(HttpMethod.POST, "/login", "/register", "/").permitAll()
              .anyRequest().authenticated());
    
    return http.build();
  }

}
