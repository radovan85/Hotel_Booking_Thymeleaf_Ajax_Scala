package com.radovan.spring.converter

import com.radovan.spring.dto.{GuestDto, NoteDto, ReservationDto, RoleDto, RoomCategoryDto, RoomDto, UserDto}
import com.radovan.spring.entity.{GuestEntity, NoteEntity, ReservationEntity, RoleEntity, RoomCategoryEntity, RoomEntity, UserEntity}
import com.radovan.spring.repository.{GuestRepository, ReservationRepository, RoleRepository, RoomCategoryRepository, RoomRepository, UserRepository}
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import java.sql.Timestamp
import java.text.DecimalFormat
import java.time.{Instant, ZoneId}
import java.time.format.DateTimeFormatter
import scala.collection.mutable.ArrayBuffer
import scala.jdk.CollectionConverters._


@Component
class TempConverter {

  private var userRepository: UserRepository = _
  private var roleRepository: RoleRepository = _
  private var roomCategoryRepository: RoomCategoryRepository = _
  private var roomRepository: RoomRepository = _
  private var guestRepository: GuestRepository = _
  private var reservationRepository: ReservationRepository = _
  private var mapper: ModelMapper = _
  private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
  private val zoneId = ZoneId.of("UTC")
  private val decfor = new DecimalFormat("0.00")

  @Autowired
  private def initialize(userRepository: UserRepository, roleRepository: RoleRepository, roomCategoryRepository: RoomCategoryRepository,
                         roomRepository: RoomRepository, guestRepository: GuestRepository, reservationRepository: ReservationRepository,
                         mapper: ModelMapper): Unit = {
    this.userRepository = userRepository
    this.roleRepository = roleRepository
    this.roomCategoryRepository = roomCategoryRepository
    this.roomRepository = roomRepository
    this.guestRepository = guestRepository
    this.reservationRepository = reservationRepository
    this.mapper = mapper
  }

  def guestEntityToDto(guest: GuestEntity): GuestDto = {
    val returnValue = mapper.map(guest, classOf[GuestDto])
    val userOption = Option(guest.getUser)
    if (userOption.isDefined) {
      returnValue.setUserId(userOption.get.getId)
    }

    val reservationsOption = Option(guest.getReservations)
    val reservationsIds = new ArrayBuffer[Integer]()
    reservationsOption match {
      case Some(reservations) => {
        reservations.forEach(reservation => reservationsIds += reservation.getReservationId)
      }
      case None =>
    }

    returnValue.setReservationsIds(reservationsIds.toArray)
    returnValue
  }

  def guestDtoToEntity(guestDto: GuestDto): GuestEntity = {
    val returnValue = mapper.map(guestDto, classOf[GuestEntity])
    val userIdOption = Option(guestDto.getUserId)
    userIdOption match {
      case Some(userId) => {
        val userEntity = userRepository.findById(userId).orElse(null)
        if (userEntity != null) {
          returnValue.setUser(userEntity)
        }
      }
      case None =>
    }

    val reservationsIdsOption = Option(guestDto.getReservationsIds)
    val reservations = new ArrayBuffer[ReservationEntity]()
    reservationsIdsOption match {
      case Some(reservationsIds) =>
        reservationsIds.foreach(reservationId => {
          val reservationEntity = reservationRepository.findById(reservationId).orElse(null)
          if (reservationEntity != null) {
            reservations += reservationEntity
          }
        })
      case None =>
    }

    returnValue.setReservations(reservations.asJava)
    returnValue
  }

  def noteEntityToDto(note: NoteEntity): NoteDto = {
    val returnValue = mapper.map(note, classOf[NoteDto])
    val createdAtOption = Option(note.getCreatedAt)
    createdAtOption match {
      case Some(createdAt) =>
        val createdAtUTC = createdAt.toLocalDateTime.atZone(zoneId)
        val createdAtStr = createdAtUTC.format(formatter)
        returnValue.setCreatedAtStr(createdAtStr)
      case None =>
    }

    returnValue
  }

  def noteDtoToEntity(noteDto: NoteDto): NoteEntity = {
    mapper.map(noteDto, classOf[NoteEntity])
  }

  def reservationEntityToDto(reservation: ReservationEntity): ReservationDto = {
    val returnValue = mapper.map(reservation, classOf[ReservationDto])
    val roomOption = Option(reservation.getRoom)
    if (roomOption.isDefined) {
      returnValue.setRoomId(roomOption.get.getRoomId)
    }

    val guestOption = Option(reservation.getGuest)
    if (guestOption.isDefined) {
      returnValue.setGuestId(guestOption.get.getGuestId)
    }

    val checkInDateOption = Option(reservation.getCheckInDate)
    checkInDateOption match {
      case Some(checkInDate) =>
        val checkInDateUTC = checkInDate.toLocalDateTime.atZone(zoneId)
        val checkInDateStr = checkInDateUTC.format(formatter)
        returnValue.setCheckInDateStr(checkInDateStr)
      case None =>
    }

    val checkOutDateOption = Option(reservation.getCheckOutDate)
    checkOutDateOption match {
      case Some(checkOutDate) =>
        val checkOutDateUTC = checkOutDate.toLocalDateTime.atZone(zoneId)
        val checkOutDateStr = checkOutDateUTC.format(formatter)
        returnValue.setCheckOutDateStr(checkOutDateStr)
      case None =>
    }

    val createdAtOption = Option(reservation.getCreatedAt)
    createdAtOption match {
      case Some(createdAt) =>
        val createdAtUTC = createdAt.toLocalDateTime.atZone(zoneId)
        val createdAtStr = createdAtUTC.format(formatter)
        returnValue.setCreatedAtStr(createdAtStr)
      case None =>
    }

    val updatedAtOption = Option(reservation.getUpdatedAt)
    updatedAtOption match {
      case Some(updatedAt) =>
        val updatedAtUTC = updatedAt.toLocalDateTime.atZone(zoneId)
        val updatedAtStr = updatedAtUTC.format(formatter)
        returnValue.setUpdatedAtStr(updatedAtStr)
      case None =>
    }

    returnValue
  }

  def reservationDtoToEntity(reservationDto: ReservationDto): ReservationEntity = {
    val returnValue = mapper.map(reservationDto, classOf[ReservationEntity])
    val roomIdOption = Option(reservationDto.getRoomId)
    roomIdOption match {
      case Some(roomId) =>
        val roomEntity = roomRepository.findById(roomId).orElse(null)
        if (roomEntity != null) {
          returnValue.setRoom(roomEntity)
        }
      case None =>
    }

    val guestIdOption = Option(reservationDto.getGuestId)
    guestIdOption match {
      case Some(guestId) =>
        val guestEntity = guestRepository.findById(guestId).orElse(null)
        if (guestEntity != null) {
          returnValue.setGuest(guestEntity)
        }
      case None =>
    }

    returnValue
  }

  def roomCategoryEntityToDto(roomCategory: RoomCategoryEntity): RoomCategoryDto = {
    val returnValue = mapper.map(roomCategory, classOf[RoomCategoryDto])
    val wifiOption = Option(roomCategory.getWifi)
    if (wifiOption.isDefined) returnValue.setWifi(wifiOption.get.asInstanceOf[Short])

    val wcOption = Option(roomCategory.getWc)
    if (wcOption.isDefined) returnValue.setWc(wcOption.get.asInstanceOf[Short])

    val tvOption = Option(roomCategory.getTv)
    if (tvOption.isDefined) returnValue.setTv(tvOption.get.asInstanceOf[Short])

    val barOption = Option(roomCategory.getBar)
    if (barOption.isDefined) returnValue.setBar(barOption.get.asInstanceOf[Short])

    val roomsOption = Option(roomCategory.getRooms)
    val roomsIds = new ArrayBuffer[Integer]()
    roomsOption match {
      case Some(rooms) => rooms.forEach(roomEntity => roomsIds += roomEntity.getRoomId)
      case None =>
    }

    returnValue.setRoomsIds(roomsIds.toArray)
    returnValue.setPrice(decfor.format(roomCategory.getPrice).toFloat)
    returnValue
  }

  def roomCategoryDtoToEntity(roomCategoryDto: RoomCategoryDto): RoomCategoryEntity = {
    val returnValue = mapper.map(roomCategoryDto, classOf[RoomCategoryEntity])
    val wifiOption = Option(roomCategoryDto.getWifi)
    wifiOption match {
      case Some(wifi) => returnValue.setWifi(wifi.asInstanceOf[Byte])
      case None =>
    }

    val wcOption = Option(roomCategoryDto.getWifi)
    wcOption match {
      case Some(wc) => returnValue.setWc(wc.asInstanceOf[Byte])
      case None =>
    }

    val tvOption = Option(roomCategoryDto.getTv)
    tvOption match {
      case Some(tv) => returnValue.setTv(tv.asInstanceOf[Byte])
      case None =>
    }

    val barOption = Option(roomCategoryDto.getBar)
    barOption match {
      case Some(bar) => returnValue.setBar(bar.asInstanceOf[Byte])
      case None =>
    }

    val roomsIdsOption = Option(roomCategoryDto.getRoomsIds)
    val rooms = new ArrayBuffer[RoomEntity]()
    roomsIdsOption match {
      case Some(roomsIds) =>
        roomsIds.foreach(roomId => {
          val roomEntity = roomRepository.findById(roomId).orElse(null)
          if (roomEntity != null) rooms += roomEntity
        })
      case None =>
    }

    returnValue.setRooms(rooms.asJava)
    returnValue.setPrice(decfor.format(returnValue.getPrice).toFloat)
    returnValue
  }

  def roomEntityToDto(room: RoomEntity): RoomDto = {
    val returnValue = mapper.map(room, classOf[RoomDto])
    val categoryOption = Option(room.getRoomCategory)
    if (categoryOption.isDefined) returnValue.setRoomCategoryId(categoryOption.get.getRoomCategoryId)
    val reservationsOption = Option(room.getReservations)
    val reservationsIds = new ArrayBuffer[Integer]()
    reservationsOption match {
      case Some(reservations) =>
        reservations.forEach(reservationEntity => reservationsIds += reservationEntity.getReservationId)
      case None =>
    }

    returnValue.setReservationsIds(reservationsIds.toArray)
    returnValue
  }

  def roomDtoToEntity(roomDto: RoomDto): RoomEntity = {
    val returnValue = mapper.map(roomDto, classOf[RoomEntity])
    val categoryIdOption = Option(roomDto.getRoomCategoryId)
    categoryIdOption match {
      case Some(categoryId) =>
        val categoryEntity = roomCategoryRepository.findById(categoryId).orElse(null)
        if (categoryEntity != null) returnValue.setRoomCategory(categoryEntity)
      case None =>
    }

    val reservationsIdsOption = Option(roomDto.getReservationsIds)
    val reservations = new ArrayBuffer[ReservationEntity]()
    reservationsIdsOption match {
      case Some(reservationsIds) =>
        reservationsIds.foreach(reservationId => {
          val reservationEntity = reservationRepository.findById(reservationId).orElse(null)
          if (reservationEntity != null) reservations += reservationEntity
        })
      case None =>
    }

    returnValue.setReservations(reservations.asJava)
    returnValue
  }

  def roleEntityToDto(roleEntity: RoleEntity): RoleDto = {
    val returnValue = mapper.map(roleEntity, classOf[RoleDto])
    val usersOption = Option(roleEntity.getUsers)
    val usersIds = new ArrayBuffer[Integer]()
    usersOption match {
      case Some(users) =>
        users.forEach(userEntity => usersIds += userEntity.getId)
      case None =>
    }

    returnValue.setUsersIds(usersIds.toArray)
    returnValue
  }

  def roleDtoToEntity(roleDto: RoleDto): RoleEntity = {
    val returnValue = mapper.map(roleDto, classOf[RoleEntity])
    val usersIdsOption = Option(roleDto.getUsersIds)
    val users = new ArrayBuffer[UserEntity]()
    usersIdsOption match {
      case Some(usersIds) =>
        usersIds.foreach(userId => {
          val userEntity = userRepository.findById(userId).orElse(null)
          if (userEntity != null) users += userEntity
        })
      case None =>
    }

    returnValue.setUsers(users.asJava)
    returnValue
  }

  def userEntityToDto(userEntity: UserEntity): UserDto = {
    val returnValue = mapper.map(userEntity, classOf[UserDto])
    val enabledOption = Option(userEntity.getEnabled)
    if (enabledOption.isDefined) returnValue.setEnabled(enabledOption.get.asInstanceOf[Short])
    val rolesOption = Option(userEntity.getRoles)
    val rolesIds = new ArrayBuffer[Integer]()
    rolesOption match {
      case Some(roles) => roles.forEach(roleEntity => rolesIds += roleEntity.getId)
      case None =>
    }

    returnValue.setRolesIds(rolesIds.toArray)
    returnValue
  }

  def userDtoToEntity(userDto: UserDto): UserEntity = {
    val returnValue = mapper.map(userDto, classOf[UserEntity])
    val enabledOption = Option(userDto.getEnabled)
    if (enabledOption.isDefined) returnValue.setEnabled(enabledOption.get.asInstanceOf[Byte])
    val rolesIdsOption = Option(userDto.getRolesIds)
    val roles = new ArrayBuffer[RoleEntity]()
    rolesIdsOption match {
      case Some(rolesIds) =>
        rolesIds.foreach(roleId => {
          val roleEntity = roleRepository.findById(roleId).orElse(null)
          if (roleEntity != null) roles += roleEntity
        })
      case None =>
    }

    returnValue.setRoles(roles.asJava)
    returnValue
  }

  def getCurrentUTCTimestamp:Timestamp = {
    val currentTime = Instant.now.atZone(zoneId)
    Timestamp.valueOf(currentTime.toLocalDateTime)
  }
}
