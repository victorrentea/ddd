//package victor.training.ddd;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//
////@EnableWebSecurity
//
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//@Configuration
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//   @Override
//   protected void configure(HttpSecurity http) throws Exception {
//      http
//          .csrf().disable() // not needed for REST endppoints
//          .authorizeRequests()
////            .mvcMatchers("/api/admin/**").hasRole("ADMIN")
////            .mvcMatchers(HttpMethod.POST,"/api/orders").hasRole("ADMIN")
////            .mvcMatchers(HttpMethod.POST,"/api/orders/*").hasRole("USEr")
//            .anyRequest().authenticated()
//          .and()
////          .addFilterBefore(new MyFilter(), BasicAuthenticationFilter.class)
//          .httpBasic().and()
//          .formLogin().permitAll()
//
//      ;
//   }
//
//   @Bean
//   public UserDetailsService userDetailsService() {
//      UserDetails userDetails = User.withDefaultPasswordEncoder().username("user").password("user").roles("USER").build();
//      UserDetails adminDetails = User.withDefaultPasswordEncoder().username("admin").password("admin").roles("ADMIN").build();
//      return new InMemoryUserDetailsManager(userDetails, adminDetails);
//   }
//}
