package com.radovan.spring.services.impl

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.{NoteDto, ReservationDto}
import com.radovan.spring.exceptions.InstanceUndefinedException
import com.radovan.spring.repository.{NoteRepository, ReservationRepository, RoomRepository}
import com.radovan.spring.services.{GuestService, ReservationService, RoomService, UserService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.sql.Timestamp
import java.time.{LocalDateTime, ZoneId, ZonedDateTime}
import java.time.format.DateTimeFormatter
import scala.jdk.CollectionConverters._

@Service
class ReservationServiceImpl extends ReservationService {

  private var reservationRepository: ReservationRepository = _
  private var roomRepository: RoomRepository = _
  private var tempConverter: TempConverter = _
  private var roomService: RoomService = _
  private var userService: UserService = _
  private var noteRepository: NoteRepository = _
  private var guestService: GuestService = _
  private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
  private val zoneId = ZoneId.of("UTC")


  @Autowired
  private def initialize(reservationRepository: ReservationRepository, roomRepository: RoomRepository, tempConverter: TempConverter,
                         roomService: RoomService, userService: UserService, noteRepository: NoteRepository,
                         guestService: GuestService): Unit = {
    this.reservationRepository = reservationRepository
    this.roomRepository = roomRepository
    this.tempConverter = tempConverter
    this.roomService = roomService
    this.userService = userService
    this.noteRepository = noteRepository
    this.guestService = guestService
  }

  @Transactional
  override def addReservation(reservation: ReservationDto, roomId: Integer): ReservationDto = {
    val room = roomService.getRoomById(roomId)
    reservation.setPrice(room.getPrice * reservation.getNumberOfNights)
    val checkInDate = LocalDateTime.parse(reservation.getCheckInDateStr, formatter).atZone(zoneId)
    val checkOutDate = LocalDateTime.parse(reservation.getCheckOutDateStr, formatter).atZone(zoneId)
    val reservationEntity = tempConverter.reservationDtoToEntity(reservation)
    reservationEntity.setCheckInDate(Timestamp.valueOf(checkInDate.toLocalDateTime))
    reservationEntity.setCheckOutDate(Timestamp.valueOf(checkOutDate.toLocalDateTime))
    reservationEntity.setRoom(tempConverter.roomDtoToEntity(room))
    reservationEntity.setCreatedAt(tempConverter.getCurrentUTCTimestamp)
    reservationEntity.setUpdatedAt(tempConverter.getCurrentUTCTimestamp)
    val storedReservation = reservationRepository.save(reservationEntity)

    val authUser = userService.getCurrentUser
    val note = new NoteDto
    note.setSubject("Reservation Created")
    val text = "User " + authUser.getFirstName + " " + authUser.getLastName + " reserved the room "
    +storedReservation.getRoom.getRoomNumber + ". Check-in is scheduled for " + reservation.getCheckInDateStr
    note.setText(text)
    val noteEntity = tempConverter.noteDtoToEntity(note)
    noteEntity.setCreatedAt(tempConverter.getCurrentUTCTimestamp)
    noteRepository.save(noteEntity)
    tempConverter.reservationEntityToDto(storedReservation)
  }

  @Transactional(readOnly = true)
  override def getReservationById(reservationId: Integer): ReservationDto = {
    val reservationEntity = reservationRepository.findById(reservationId)
      .orElseThrow(() => new InstanceUndefinedException(new Error("The reservation has not been found!")))
    tempConverter.reservationEntityToDto(reservationEntity)
  }

  @Transactional
  override def deleteReservation(reservationId: Integer): Unit = {
    val reservation = getReservationById(reservationId)
    val room = roomService.getRoomById(reservation.getRoomId)
    val guest = guestService.getGuestById(reservation.getGuestId)
    val reservationUser = userService.getUserById(guest.getUserId)

    val note = new NoteDto
    note.setSubject("Reservation Canceled")

    val text = if (userService.isAdmin) {
      s"Reservation for user ${reservationUser.getFirstName} ${reservationUser.getLastName} " +
        s"for room No ${room.getRoomNumber} scheduled for ${reservation.getCheckInDateStr} " +
        s"has been cancelled by Administrator"
    } else {
      s"Reservation for user ${reservationUser.getFirstName} ${reservationUser.getLastName} " +
        s"for room No ${room.getRoomNumber} scheduled for ${reservation.getCheckInDateStr} " +
        s"has been cancelled by user"
    }

    note.setText(text)

    val noteEntity = tempConverter.noteDtoToEntity(note)
    noteEntity.setCreatedAt(tempConverter.getCurrentUTCTimestamp)

    noteRepository.save(noteEntity)
    reservationRepository.deleteById(reservationId)
    reservationRepository.flush()
  }

  @Transactional(readOnly = true)
  override def listAll: Array[ReservationDto] = {
    val allReservations = reservationRepository.findAll().asScala
    allReservations.collect {
      case reservationEntity => tempConverter.reservationEntityToDto(reservationEntity)
    }.toArray
  }

  @Transactional(readOnly = true)
  override def listAllByGuestId(guestId: Integer): Array[ReservationDto] = {
    val allReservations = reservationRepository.findAllByGuestId(guestId).asScala
    allReservations.collect {
      case reservationEntity => tempConverter.reservationEntityToDto(reservationEntity)
    }.toArray
  }

  @Transactional(readOnly = true)
  override def listAllByRoomId(roomId: Integer): Array[ReservationDto] = {
    val allReservations = reservationRepository.findAllByRoomId(roomId).asScala
    allReservations.collect {
      case reservationEntity => tempConverter.reservationEntityToDto(reservationEntity)
    }.toArray
  }

  @Transactional(readOnly = true)
  def listAllActive: Array[ReservationDto] = {
    val currentDateTime = Timestamp.valueOf(ZonedDateTime.now(zoneId).toLocalDateTime)
    val activeReservations = reservationRepository.findAllActiveReservations(currentDateTime).asScala
    activeReservations.map(reservation => tempConverter.reservationEntityToDto(reservation)).toArray
  }


  @Transactional(readOnly = true)
  def listAllExpired: Array[ReservationDto] = {
    val currentDateTime = Timestamp.valueOf(ZonedDateTime.now(zoneId).toLocalDateTime)
    val expiredReservations = reservationRepository.findAllExpiredReservations(currentDateTime).asScala
    expiredReservations.collect {
      case reservationEntity => tempConverter.reservationEntityToDto(reservationEntity)
    }.toArray
  }


  @Transactional(readOnly = true)
  def isAvailable(roomId: Integer, checkInDate: Timestamp, checkOutDate: Timestamp): Boolean = {
    // Brojanje preklapajućih rezervacija
    val overlappingCount = reservationRepository.countOverlappingReservations(roomId, checkInDate, checkOutDate)
    // Ako nema preklapanja, soba je dostupna
    overlappingCount == 0
  }


  @Transactional
  override def updateReservation(reservation: ReservationDto, reservationId: Integer): ReservationDto = {
    val optionalReservationEntity = reservationRepository.findById(reservationId)

    if (optionalReservationEntity.isPresent) {
      val reservationEntity = optionalReservationEntity.get
      val updatedEntity = tempConverter.reservationDtoToEntity(reservation)

      // Postavljanje polja
      updatedEntity.setReservationId(reservationEntity.getReservationId)
      updatedEntity.setCreatedAt(reservationEntity.getCreatedAt)
      updatedEntity.setUpdatedAt(tempConverter.getCurrentUTCTimestamp)
      updatedEntity.setCheckInDate(reservationEntity.getCheckInDate)
      updatedEntity.setCheckOutDate(reservationEntity.getCheckOutDate)

      // Čuvanje ažurirane rezervacije
      val updatedReservation = reservationRepository.saveAndFlush(updatedEntity)

      // Kreiranje beleške
      val note = new NoteDto
      note.setSubject("Reservation Updated")
      val text =
        s"Reservation ${reservation.getReservationId} scheduled for ${reservation.getCheckInDateStr} has been switched to room ${updatedReservation.getRoom.getRoomNumber}"
      note.setText(text)

      val noteEntity = tempConverter.noteDtoToEntity(note)
      noteEntity.setCreatedAt(tempConverter.getCurrentUTCTimestamp)
      noteRepository.save(noteEntity)

      // Povratak ažurirane rezervacije kao DTO
      tempConverter.reservationEntityToDto(updatedReservation)
    } else {
      null
    }
  }

  @Transactional
  override def deleteAllByRoomId(roomId: Integer): Unit = {
    reservationRepository.deleteAllByRoomId(roomId)
    reservationRepository.flush()
  }


}
