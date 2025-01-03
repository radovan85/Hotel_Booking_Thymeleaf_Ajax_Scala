package com.radovan.spring.security.handler

import jakarta.servlet.ServletException
import jakarta.servlet.http.{HttpServletRequest, HttpServletResponse}
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

import java.io.IOException

@Component
class LoginSuccessHandler extends AuthenticationSuccessHandler {

  @throws[IOException]
  @throws[ServletException]
  override def onAuthenticationSuccess(httpServletRequest: HttpServletRequest, httpServletResponse: HttpServletResponse, authentication: Authentication): Unit = {

  }
}
