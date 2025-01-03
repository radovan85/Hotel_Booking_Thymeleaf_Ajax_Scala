package com.radovan.spring.config

import org.modelmapper.ModelMapper
import org.modelmapper.convention.MatchingStrategies
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect
import org.thymeleaf.spring6.SpringTemplateEngine
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver
import org.thymeleaf.spring6.view.ThymeleafViewResolver


@Configuration
@EnableWebMvc
@ComponentScan(basePackages = Array("com.radovan.spring"))
class SpringMvcConfiguration extends WebMvcConfigurer {
  @Bean def templateResolver: SpringResourceTemplateResolver = {
    val returnValue = new SpringResourceTemplateResolver
    returnValue.setPrefix("/WEB-INF/templates/")
    returnValue.setSuffix(".html")
    returnValue.setCacheable(false)
    returnValue
  }

  @Bean def templateEngine: SpringTemplateEngine = {
    val returnValue = new SpringTemplateEngine
    returnValue.setTemplateResolver(templateResolver)
    returnValue.setEnableSpringELCompiler(true)
    returnValue.addDialect(springSecurityDialect)
    returnValue
  }

  override def configureViewResolvers(registry: ViewResolverRegistry): Unit = {
    val resolver = new ThymeleafViewResolver
    resolver.setTemplateEngine(templateEngine)
    registry.viewResolver(resolver)
  }

  override def addResourceHandlers(registry: ResourceHandlerRegistry): Unit = {
    registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/")
    registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/")
    registry.addResourceHandler("/images/**").addResourceLocations("classpath:/static/images/")
  }

  @Bean def getMapper: ModelMapper = {
    val returnValue = new ModelMapper
    returnValue.getConfiguration.setAmbiguityIgnored(true).setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
    returnValue.getConfiguration.setMatchingStrategy(MatchingStrategies.STRICT)
    returnValue
  }

  @Bean def springSecurityDialect = new SpringSecurityDialect
}
