package victor.training.ddd;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

//@EnableWebSecurity

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

   @Override
   protected void configure(HttpSecurity http) throws Exception {
      http
          .csrf().disable() // not needed for REST endppoints
          .authorizeRequests().anyRequest().authenticated()
          .and()
          .httpBasic().and()
          .formLogin().permitAll()
      ;
   }

   @Bean
   public UserDetailsService userDetailsService() {
      UserDetails userDetails = User.withDefaultPasswordEncoder().username("user").password("user").roles("USER").build();
      UserDetails adminDetails = User.withDefaultPasswordEncoder().username("admin").password("admin").roles("ADMIN").build();
      return new InMemoryUserDetailsManager(userDetails, adminDetails);
   }
}
