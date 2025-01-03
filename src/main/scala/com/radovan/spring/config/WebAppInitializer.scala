package com.radovan.spring.config

import jakarta.servlet.ServletContext
import org.springframework.web.WebApplicationInitializer
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext
import org.springframework.web.servlet.DispatcherServlet

class WebAppInitializer extends WebApplicationInitializer {

  override def onStartup(servletContext: ServletContext): Unit = {
    val webContext = new AnnotationConfigWebApplicationContext
    webContext.register(classOf[SpringMvcConfiguration])
    val initializer = servletContext.addServlet("Spring Initializer", new DispatcherServlet(webContext))
    initializer.setLoadOnStartup(1)
    initializer.addMapping("/")
  }
}