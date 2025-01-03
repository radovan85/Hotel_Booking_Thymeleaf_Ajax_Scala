package com.radovan.spring.controllers

import com.radovan.spring.dto.{ReservationDto, RoomCategoryDto, RoomDto}
import com.radovan.spring.services.{GuestService, NoteService, ReservationService, RoomCategoryService, RoomService, UserService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.{GetMapping, ModelAttribute, PathVariable, PostMapping, RequestMapping}

@Controller
@RequestMapping(value = Array("/admin"))
class AdminController {

  private var guestService:GuestService = _
  private var userService:UserService = _
  private var roomCategoryService:RoomCategoryService = _
  private var roomService:RoomService = _
  private var noteService:NoteService = _
  private var reservationService:ReservationService = _

  @Autowired
  private def initialize(guestService: GuestService,userService: UserService,roomCategoryService: RoomCategoryService,
                         roomService: RoomService,noteService: NoteService,reservationService: ReservationService):Unit = {
    this.guestService = guestService
    this.userService = userService
    this.roomCategoryService = roomCategoryService
    this.roomService = roomService
    this.noteService = noteService
    this.reservationService = reservationService
  }

  @GetMapping(value = Array("/allGuests"))
  def allGuests(map: ModelMap): String = {
    val allUsers = userService.listAll
    val allGuests = guestService.listAll
    map.put("allUsers", allUsers)
    map.put("allGuests", allGuests)
    map.put("recordsPerPage", 6.asInstanceOf[Integer])
    "fragments/guestList :: fragmentContent"
  }

  @GetMapping(value = Array("/guestDetails/{guestId}"))
  def getGuestDetails(@PathVariable("guestId") guestId: Integer, map: ModelMap): String = {
    val guest = guestService.getGuestById(guestId)
    val user = userService.getUserById(guest.getUserId)
    map.put("user", user)
    map.put("guest", guest)
    "fragments/guestDetails :: fragmentContent"
  }

  @GetMapping(value = Array("/deleteGuest/{guestId}"))
  def deleteGuest(@PathVariable("guestId") guestId: Integer): String = {
    guestService.deleteGuest(guestId)
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/allRoomCategories"))
  def listAllCategories(map: ModelMap): String = {
    val allCategories = roomCategoryService.listAll
    map.put("allCategories", allCategories)
    map.put("recordsPerPage", 5.asInstanceOf[Integer])
    "fragments/roomCategoryList :: fragmentContent"
  }

  @GetMapping(value = Array("/createRoomCategory"))
  def renderCategoryForm(map: ModelMap): String = {
    val roomCategory = new RoomCategoryDto
    map.put("roomCategory", roomCategory)
    "fragments/roomCategoryForm :: fragmentContent"
  }

  @PostMapping(value = Array("/createRoomCategory"))
  def storeRoomCategory(@ModelAttribute("roomCategory") roomCategory: RoomCategoryDto): String = {
    roomCategoryService.addCategory(roomCategory)
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/categoryDetails/{categoryId}"))
  def getCategoryDetails(@PathVariable("categoryId") categoryId: Integer, map: ModelMap): String = {
    val roomCategory = roomCategoryService.getCategoryById(categoryId)
    map.put("roomCategory", roomCategory)
    "fragments/roomCategoryDetails :: fragmentContent"
  }

  @GetMapping(value = Array("/updateRoomCategory/{categoryId}"))
  def renderCategoryUpdateForm(@PathVariable("categoryId") categoryId: Integer, map: ModelMap): String = {
    val roomCategory = new RoomCategoryDto
    val currentCategory = roomCategoryService.getCategoryById(categoryId)
    map.put("roomCategory", roomCategory)
    map.put("currentCategory", currentCategory)
    "fragments/updateRoomCategory :: fragmentContent"
  }

  @GetMapping(value = Array("/deleteRoomCategory/{categoryId}"))
  def deleteCategory(@PathVariable("categoryId") categoryId: Integer): String = {
    roomCategoryService.deleteCategory(categoryId)
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/allRooms"))
  def listAllRooms(map: ModelMap): String = {
    val allRooms = roomService.listAll
    val allCategories = roomCategoryService.listAll
    map.put("allRooms", allRooms)
    map.put("allCategories", allCategories)
    map.put("recordsPerPage", 6.asInstanceOf[Integer])
    "fragments/roomList :: fragmentContent"
  }

  @GetMapping(value = Array("/createRoom"))
  def renderRoomForm(map: ModelMap): String = {
    val room = new RoomDto
    val allCategories = roomCategoryService.listAll
    map.put("room", room)
    map.put("allCategories", allCategories)
    "fragments/roomForm :: fragmentContent"
  }

  @PostMapping(value = Array("/createRoom"))
  def storeRoom(@ModelAttribute("room") room: RoomDto): String = {
    roomService.addRoom(room)
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/roomDetails/{roomId}"))
  def getRoomDetails(@PathVariable("roomId") roomId: Integer, map: ModelMap): String = {
    val room = roomService.getRoomById(roomId)
    val category = roomCategoryService.getCategoryById(room.getRoomCategoryId)
    map.put("room", room)
    map.put("category", category)
    "fragments/roomDetails :: fragmentContent"
  }

  @GetMapping(value = Array("/updateRoom/{roomId}"))
  def renderRoomUpdateForm(@PathVariable("roomId") roomId: Integer, map: ModelMap): String = {
    val room = new RoomDto
    val currentRoom = roomService.getRoomById(roomId)
    val allCategories = roomCategoryService.listAll
    map.put("room", room)
    map.put("currentRoom", currentRoom)
    map.put("allCategories", allCategories)
    "fragments/updateRoom :: fragmentContent"
  }

  @GetMapping(value = Array("/deleteRoom/{roomId}"))
  def deleteRoom(@PathVariable("roomId") roomId: Integer): String = {
    roomService.deleteRoom(roomId)
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/allNotes")) def listAllNotes(map: ModelMap): String = {
    val allNotes = noteService.listAll
    map.put("allNotes", allNotes)
    map.put("recordsPerPage", 10.asInstanceOf[Integer])
    "fragments/noteList :: fragmentContent"
  }

  @GetMapping(value = Array("/allNotesToday"))
  def listAllNotesForToday(map: ModelMap): String = {
    val allNotes = noteService.listAllForToday
    map.put("allNotes", allNotes)
    map.put("recordsPerPage", 10.asInstanceOf[Integer])
    "fragments/noteList :: fragmentContent"
  }

  @GetMapping(value = Array("/noteDetails/{noteId}"))
  def getNoteDetails(@PathVariable("noteId") noteId: Integer, map: ModelMap): String = {
    val note = noteService.getNoteById(noteId)
    map.put("note", note)
    "fragments/noteDetails :: fragmentContent"
  }

  @GetMapping(value = Array("/deleteNote/{noteId}"))
  def deleteNote(@PathVariable("noteId") noteId: Integer): String = {
    noteService.deleteNote(noteId)
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/deleteAllNotes"))
  def deleteAllNotes: String = {
    noteService.deleteAllNotes
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/allReservations"))
  def listAllReservations(map: ModelMap): String = {
    val allReservations = reservationService.listAll
    val allGuests = guestService.listAll
    val allUsers = userService.listAll
    map.put("allReservations", allReservations)
    map.put("allGuests", allGuests)
    map.put("allUsers", allUsers)
    map.put("recordsPerPage", 6.asInstanceOf[Integer])
    "fragments/reservationList :: fragmentContent"
  }

  @GetMapping(value = Array("/allActiveReservations"))
  def listAllActiveReservations(map: ModelMap): String = {
    val allReservations = reservationService.listAllActive
    val allGuests = guestService.listAll
    val allUsers = userService.listAll
    map.put("allReservations", allReservations)
    map.put("allGuests", allGuests)
    map.put("allUsers", allUsers)
    map.put("recordsPerPage", 6.asInstanceOf[Integer])
    "fragments/reservationList :: fragmentContent"
  }

  @GetMapping(value = Array("/allExpiredReservations"))
  def listAllExpiredReservations(map: ModelMap): String = {
    val allReservations = reservationService.listAllExpired
    val allGuests = guestService.listAll
    val allUsers = userService.listAll
    map.put("allReservations", allReservations)
    map.put("allGuests", allGuests)
    map.put("allUsers", allUsers)
    map.put("recordsPerPage", 6.asInstanceOf[Integer])
    "fragments/reservationList :: fragmentContent"
  }

  @GetMapping(value = Array("/reservationDetails/{reservationId}"))
  def getReservationDetails(@PathVariable("reservationId") reservationId: Integer, map: ModelMap): String = {
    val reservation = reservationService.getReservationById(reservationId)
    val room = roomService.getRoomById(reservation.getRoomId)
    val category = roomCategoryService.getCategoryById(room.getRoomCategoryId)
    val guest = guestService.getGuestById(reservation.getGuestId)
    val user = userService.getUserById(guest.getUserId)
    map.put("room", room)
    map.put("category", category)
    map.put("reservation", reservation)
    map.put("user", user)
    "fragments/reservationDetails :: fragmentContent"
  }

  @GetMapping(value = Array("/switchReservationRoom/{reservationId}"))
  def changeReservationRoom(@PathVariable("reservationId") reservationId: Integer, map: ModelMap): String = {
    val reservation = new ReservationDto
    val currentReservation = reservationService.getReservationById(reservationId)
    val currentRoom = roomService.getRoomById(currentReservation.getRoomId)
    val availableRooms = roomService.listAllByCategoryId(currentRoom.getRoomCategoryId)
    val category = roomCategoryService.getCategoryById(currentRoom.getRoomCategoryId)
    val filteredRooms = availableRooms.filterNot(_.getRoomId == currentRoom.getRoomId)
    map.put("reservation", reservation)
    map.put("currentReservation", currentReservation)
    map.put("currentRoom", currentRoom)
    map.put("availableRooms", filteredRooms)
    map.put("category", category)
    "fragments/switchRoomForm :: fragmentContent"
  }


  @PostMapping(value = Array("/updateReservation/{reservationId}"))
  def updateReservation(@ModelAttribute reservation: ReservationDto, @PathVariable("reservationId") reservationId: Integer): String = {
    reservationService.updateReservation(reservation, reservationId)
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/deleteReservation/{reservationId}"))
  def deleteReservation(@PathVariable("reservationId") reservationId: Integer): String = {
    reservationService.deleteReservation(reservationId)
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/roomNumberError"))
  def roomError = "fragments/roomNumberError :: fragmentContent"
}
