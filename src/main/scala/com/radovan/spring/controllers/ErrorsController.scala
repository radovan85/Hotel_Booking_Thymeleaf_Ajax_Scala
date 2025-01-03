package com.radovan.spring.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import com.radovan.spring.exceptions.ExistingInstanceException
import com.radovan.spring.exceptions.InstanceUndefinedException
import com.radovan.spring.exceptions.SuspendedUserException


@ControllerAdvice
class ErrorsController {

  @ExceptionHandler(Array(classOf[ExistingInstanceException]))
  def handleExistingInstanceException(error: Error) = new ResponseEntity[String](error.getMessage, HttpStatus.CONFLICT)

  @ExceptionHandler(Array(classOf[SuspendedUserException]))
  def handleSuspendedUserException(error: Error): ResponseEntity[String] = {
    SecurityContextHolder.clearContext()
    new ResponseEntity[String](error.getMessage, HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS)
  }

  @ExceptionHandler(Array(classOf[InstanceUndefinedException]))
  def handleInstanceUndefinedException(error: Error) = new ResponseEntity[String](error.getMessage, HttpStatus.UNPROCESSABLE_ENTITY)
}