package com.radovan.spring.controllers

import com.radovan.spring.dto.{ReservationDto, RoomDto}
import com.radovan.spring.services.{GuestService, ReservationService, RoomCategoryService, RoomService, UserService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.{GetMapping, ModelAttribute, PathVariable, PostMapping, RequestMapping}

import java.sql.Timestamp
import java.time.{Instant, LocalDateTime, ZoneId}
import java.time.format.DateTimeFormatter
import scala.collection.mutable.ArrayBuffer
import scala.util.control.Breaks._

@Controller
@RequestMapping(value = Array("/guests"))
class GuestController {

  private var userService:UserService = _
  private var guestService:GuestService = _
  private var reservationService:ReservationService = _
  private var roomCategoryService:RoomCategoryService = _
  private var roomService:RoomService = _
  private val zoneId = ZoneId.of("UTC")

  @Autowired
  private def initialize(userService: UserService,guestService: GuestService,reservationService: ReservationService,
                         roomCategoryService: RoomCategoryService,roomService: RoomService):Unit = {
    this.userService = userService
    this.guestService = guestService
    this.reservationService = reservationService
    this.roomCategoryService = roomCategoryService
    this.roomService = roomService
  }

  @GetMapping(value = Array("/allUserReservations"))
  def getAllUserReservations(map: ModelMap): String = {
    val user = userService.getCurrentUser
    val guest = guestService.getGuestByUserId(user.getId)
    val allReservations = reservationService.listAllByGuestId(guest.getGuestId)
    val allRooms = roomService.listAll
    val allCategories = roomCategoryService.listAll
    map.put("allReservations", allReservations)
    map.put("allRooms", allRooms)
    map.put("allCategories", allCategories)
    map.put("recordsPerPage", 6.asInstanceOf[Integer])
    "fragments/userReservationList :: fragmentContent"
  }

  @GetMapping(Array("/createReservation/{checkInDateStr}/{numberOfNights}"))
  def renderReservationForm(
                             @PathVariable("checkInDateStr") checkInDateStrParam: String,
                             @PathVariable("numberOfNights") numberOfNights: Integer,
                             map: ModelMap
                           ): String = {

    val reservation = new ReservationDto
    val user = userService.getCurrentUser
    val guest = guestService.getGuestByUserId(user.getId)

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    val checkInDateStr = s"$checkInDateStrParam 14:00"
    val allCategories = roomCategoryService.listAll
    val availableRooms = new ArrayBuffer[RoomDto]()

    val checkInDateZoned = LocalDateTime.parse(checkInDateStr, formatter).atZone(zoneId)
    val checkInDate = checkInDateZoned.toLocalDateTime
    var checkOutDate = checkInDate.plusDays(numberOfNights.longValue()).minusHours(2)

    val checkOutDateStr = checkOutDate.format(formatter)

    val checkInStamp = Timestamp.valueOf(checkInDate)
    val checkOutStamp = Timestamp.valueOf(checkOutDate)

    // Provera dostupnosti soba
    for (category <- allCategories) {
      val rooms = roomService.listAllByCategoryId(category.getRoomCategoryId) // Vraća Array[RoomDto]
      breakable {
        for (roomDto <- rooms) {
          if (reservationService.isAvailable(roomDto.getRoomId, checkInStamp, checkOutStamp)) {
            availableRooms += roomDto
            break // Izlaz iz unutrašnje petlje
          }
        }
      }
    }

    map.put("checkInDateStr", checkInDateStr)
    map.put("checkOutDateStr", checkOutDateStr)
    map.put("checkInDate", checkInDate)
    map.put("checkOutDate", checkOutDate)
    map.put("numberOfNights", numberOfNights)
    map.put("allCategories", allCategories)
    map.put("availableRooms", availableRooms.toArray)
    map.put("guestId", guest.getGuestId)
    map.put("reservation", reservation)
    "fragments/reservationForm :: fragmentContent"
  }


  @PostMapping(value = Array("/createReservation/{roomId}"))
  def storeReservation(@ModelAttribute("reservation") reservation: ReservationDto, @PathVariable("roomId") roomId: Integer): String = {
    reservationService.addReservation(reservation, roomId)
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/bookReservation"))
  def bookReservation(map: ModelMap): String = {
    val reservation = new ReservationDto
    val currentTime = Instant.now.atZone(zoneId)
    val today = currentTime.toLocalDate
    val maxDate = today.plusYears(1)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val todayStr = today.format(formatter)
    val maxDateStr = maxDate.format(formatter)
    map.put("reservation", reservation)
    map.put("todayStr", todayStr)
    map.put("maxDateStr", maxDateStr)
    "fragments/reservationBooking :: fragmentContent"
  }

  @GetMapping(value = Array("/deleteReservation/{reservationId}"))
  def deleteReservation(@PathVariable("reservationId") reservationId: Integer): String = {
    reservationService.deleteReservation(reservationId)
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/accountDetails"))
  def getAccountDetails(map: ModelMap): String = {
    val authUser = userService.getCurrentUser
    val guest = guestService.getGuestByUserId(authUser.getId)
    map.put("guest", guest)
    map.put("user", authUser)
    "fragments/guestDetails :: fragmentContent"
  }

}
