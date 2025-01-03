package com.radovan.spring.security

import com.radovan.spring.services.impl.UserDetailsImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.{HttpSecurity, WebSecurity}
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import com.radovan.spring.security.handler.LoginSuccessHandler
import org.springframework.security.config.annotation.web.configurers.{AuthorizeHttpRequestsConfigurer, CsrfConfigurer, FormLoginConfigurer, LogoutConfigurer}


@Configuration
@EnableWebSecurity class SecurityConfiguration {
  @Bean def authenticationManager: AuthenticationManager = {
    val authProvider = new DaoAuthenticationProvider
    authProvider.setUserDetailsService(userDetailsService)
    authProvider.setPasswordEncoder(passwordEncoder)
    new ProviderManager(authProvider)
  }

  @Bean
  @throws[Exception]
  protected def configure(http: HttpSecurity): SecurityFilterChain = {
    http.formLogin((fl: FormLoginConfigurer[HttpSecurity]) => fl.loginPage("/login").successHandler(new LoginSuccessHandler).loginProcessingUrl("/login").usernameParameter("email").passwordParameter("password").permitAll)
    http.logout((logout: LogoutConfigurer[HttpSecurity]) => logout.permitAll.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login?logout"))
    http.csrf((csrf: CsrfConfigurer[HttpSecurity]) => csrf.disable)
    http.authorizeHttpRequests((authorize: AuthorizeHttpRequestsConfigurer[HttpSecurity]#AuthorizationManagerRequestMatcherRegistry) => authorize.requestMatchers("/login", "/loginErrorPage").anonymous.requestMatchers("/registerComplete", "/registerFail", "/register").anonymous.requestMatchers("/loginPassConfirm", "/", "/home", "/contactInfo").permitAll.requestMatchers("/admin/**").hasAuthority("ADMIN").requestMatchers("/guests").hasAuthority("ROLE_USER").anyRequest.authenticated)
    http.build
  }

  @Bean
  def userDetailsService = new UserDetailsImpl

  @Bean
  def passwordEncoder = new BCryptPasswordEncoder

  @Bean
  def resourcesCustomizer: WebSecurityCustomizer = (web: WebSecurity) => web.ignoring.requestMatchers("/resources/**", "/static/**", "/images/**", "/css/**", "/js/**")
}