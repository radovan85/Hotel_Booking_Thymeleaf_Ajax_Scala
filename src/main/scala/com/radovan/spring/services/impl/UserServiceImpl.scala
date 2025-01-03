package com.radovan.spring.services.impl

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.UserDto
import com.radovan.spring.exceptions.InstanceUndefinedException
import com.radovan.spring.repository.{RoleRepository, UserRepository}
import com.radovan.spring.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.jdk.CollectionConverters._

@Service
class UserServiceImpl extends UserService {

  private var userRepository: UserRepository = _
  private var roleRepository: RoleRepository = _
  private var tempConverter: TempConverter = _

  @Autowired
  private def initialize(userRepository: UserRepository, roleRepository: RoleRepository,
                         tempConverter: TempConverter): Unit = {
    this.userRepository = userRepository
    this.roleRepository = roleRepository
    this.tempConverter = tempConverter
  }

  @Transactional(readOnly = true)
  override def getUserById(userId: Integer): UserDto = {
    val userEntity = userRepository.findById(userId)
      .orElseThrow(() => new InstanceUndefinedException(new Error("The user has not been found!")))
    tempConverter.userEntityToDto(userEntity)
  }

  @Transactional(readOnly = true)
  override def listAll: Array[UserDto] = {
    val allUsers = userRepository.findAll().asScala
    allUsers.collect {
      case userEntity => tempConverter.userEntityToDto(userEntity)
    }.toArray
  }

  @Transactional(readOnly = true)
  override def getUserByEmail(email: String): UserDto = {
    val userEntity = userRepository.findByEmail(email).getOrElse {
      throw new InstanceUndefinedException(new Error("The user has not been found!"))
    }
    tempConverter.userEntityToDto(userEntity)
  }

  @Transactional(readOnly = true)
  override def getCurrentUser: UserDto = {
    val authentication = SecurityContextHolder.getContext.getAuthentication
    if (authentication.isAuthenticated) {
      val currentUsername = authentication.getName
      userRepository.findByEmail(currentUsername)
        .map(tempConverter.userEntityToDto)
        .getOrElse(throw new InstanceUndefinedException(new Error("Invalid user!")))
    } else {
      throw new InstanceUndefinedException(new Error("Invalid user!"))
    }
  }

  @Transactional(readOnly = true)
  override def isAdmin: Boolean = {
    val authUser = getCurrentUser
    roleRepository.findByRole("ADMIN") match {
      case Some(role) => authUser.getRolesIds.contains(role.getId)
      case None => false
    }
  }
}
