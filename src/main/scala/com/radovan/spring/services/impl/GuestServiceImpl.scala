package com.radovan.spring.services.impl

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.GuestDto
import com.radovan.spring.entity.{RoleEntity, UserEntity}
import com.radovan.spring.exceptions.{ExistingInstanceException, InstanceUndefinedException}
import com.radovan.spring.repository.{GuestRepository, RoleRepository, UserRepository}
import com.radovan.spring.services.GuestService
import com.radovan.spring.utils.RegistrationForm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.collection.mutable.ArrayBuffer
import scala.jdk.CollectionConverters._


@Service
class GuestServiceImpl extends GuestService{

  private var guestRepository:GuestRepository = _
  private var userRepository:UserRepository = _
  private var roleRepository:RoleRepository = _
  private var passwordEncoder:BCryptPasswordEncoder = _
  private var tempConverter:TempConverter = _

  @Autowired
  private def initialize(guestRepository: GuestRepository,userRepository: UserRepository,
                         roleRepository: RoleRepository,passwordEncoder: BCryptPasswordEncoder,
                         tempConverter: TempConverter):Unit = {
    this.guestRepository = guestRepository
    this.userRepository = userRepository
    this.roleRepository = roleRepository
    this.passwordEncoder = passwordEncoder
    this.tempConverter = tempConverter
  }

  @Transactional
  override def addGuest(guest: GuestDto): GuestDto = {
    val storedGuest = guestRepository.save(tempConverter.guestDtoToEntity(guest))
    tempConverter.guestEntityToDto(storedGuest)
  }

  @Transactional(readOnly = true)
  override def getGuestById(guestId: Integer): GuestDto = {
    val guestEntity = guestRepository.findById(guestId)
      .orElseThrow(() => new InstanceUndefinedException(new Error("The guest has not been found!")))
    tempConverter.guestEntityToDto(guestEntity)
  }

  @Transactional(readOnly = true)
  override def getGuestByUserId(userId: Integer): GuestDto = {
    val guestEntity = guestRepository.findByUserId(userId)
      .getOrElse(throw new InstanceUndefinedException(new Error("The guest has not been found!")))
    tempConverter.guestEntityToDto(guestEntity)
  }

  @Transactional
  override def deleteGuest(guestId: Integer): Unit = {
    getGuestById(guestId)
    guestRepository.deleteById(guestId)
    guestRepository.flush()
  }

  @Transactional(readOnly = true)
  override def listAll: Array[GuestDto] = {
    val allGuests = guestRepository.findAll().asScala
    allGuests.collect{
      case guestEntity => tempConverter.guestEntityToDto(guestEntity)
    }.toArray
  }

  @Transactional
  override def storeGuest(form: RegistrationForm): GuestDto = {
    val user = form.getUser
    val testUserOptional = userRepository.findByEmail(user.getEmail)
    testUserOptional match {
      case Some(_) => throw new ExistingInstanceException(new Error("This email exists already!"))
      case None =>
    }
    val roleEntity = roleRepository.findByRole("ROLE_USER").getOrElse {
      roleRepository.save(new RoleEntity("ROLE_USER"))
    }
    user.setPassword(passwordEncoder.encode(user.getPassword))
    user.setEnabled(1.asInstanceOf[Short])
    val roles = new ArrayBuffer[RoleEntity]()
    roles += roleEntity
    val userEntity = tempConverter.userDtoToEntity(user)
    userEntity.setRoles(roles.asJava)
    val storedUser = userRepository.save(userEntity)
    val users = new ArrayBuffer[UserEntity]()
    users += storedUser
    roleEntity.setUsers(users.asJava)
    roleRepository.saveAndFlush(roleEntity)
    val guest = form.getGuest
    guest.setUserId(storedUser.getId)
    val storedGuest = guestRepository.save(tempConverter.guestDtoToEntity(guest))
    tempConverter.guestEntityToDto(storedGuest)
  }
}
