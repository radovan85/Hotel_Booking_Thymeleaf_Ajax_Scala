package com.radovan.spring.controllers

import com.radovan.spring.exceptions.InstanceUndefinedException
import com.radovan.spring.services.GuestService
import com.radovan.spring.utils.RegistrationForm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.{GetMapping, ModelAttribute, PostMapping}

import java.security.Principal

@Controller
class MainController {

  @Autowired
  private var guestService:GuestService = _

  @GetMapping(value = Array("/"))
  def indexPage: String = "index"

  @GetMapping(value = Array("/home"))
  def homePage: String = "fragments/homePage :: fragmentContent"

  @GetMapping(value = Array("/login"))
  def login: String = "fragments/login :: fragmentContent"

  @PostMapping(value = Array("/loginPassConfirm"))
  def confirmLoginPass(principal: Principal): String = {
    Option(principal) match {
      case Some(_) => "fragments/homePage :: fragmentContent"
      case None =>
        throw new InstanceUndefinedException(new Error("Invalid user"))
    }
  }

  @GetMapping(value = Array("/loginErrorPage"))
  def logError(map: ModelMap): String = {
    map.put("alert", "Invalid username or password!")
    "fragments/login :: fragmentContent"
  }

  @PostMapping(value = Array("/loggedout"))
  def logout: String = {
    val context = SecurityContextHolder.getContext
    context.setAuthentication(null)
    SecurityContextHolder.clearContext()
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/contactInfo"))
  def getContactInfo: String = "fragments/contact :: fragmentContent"

  @GetMapping(value = Array("/register"))
  def userRegistration(map: ModelMap): String = {
    val registerForm = new RegistrationForm
    map.put("registerForm", registerForm)
    "fragments/registration :: fragmentContent"
  }

  @PostMapping(value = Array("/register"))
  def registerUser(@ModelAttribute("registerForm") registerForm: RegistrationForm): String = {
    guestService.storeGuest(registerForm)
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/registerComplete"))
  def registrationCompleted: String = "fragments/registration_completed :: fragmentContent"

  @GetMapping(value = Array("/registerFail"))
  def registrationFailed: String = "fragments/registration_failed :: fragmentContent"
}
