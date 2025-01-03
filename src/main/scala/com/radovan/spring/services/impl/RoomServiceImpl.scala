package com.radovan.spring.services.impl

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.RoomDto
import com.radovan.spring.exceptions.{ExistingInstanceException, InstanceUndefinedException}
import com.radovan.spring.repository.RoomRepository
import com.radovan.spring.services.{ReservationService, RoomCategoryService, RoomService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.jdk.CollectionConverters._

@Service
class RoomServiceImpl extends RoomService {

  private var roomRepository: RoomRepository = _
  private var categoryService: RoomCategoryService = _
  private var tempConverter: TempConverter = _
  private var reservationService:ReservationService = _

  @Autowired
  private def initialize(roomRepository: RoomRepository, categoryService: RoomCategoryService,
                         tempConverter: TempConverter,reservationService: ReservationService): Unit = {
    this.roomRepository = roomRepository
    this.categoryService = categoryService
    this.tempConverter = tempConverter
    this.reservationService = reservationService
  }


  @Transactional
  override def addRoom(room: RoomDto): RoomDto = {
    val roomCategory = categoryService.getCategoryById(room.getRoomCategoryId)
    val existingRoomOption = roomRepository.findByRoomNumber(room.getRoomNumber)
    existingRoomOption match {
      case Some(existingRoom) =>
        if (existingRoom.getRoomId != room.getRoomId) {
          throw new ExistingInstanceException(new Error("This room number exists already!"))
        }
        if(existingRoom.getRoomCategory.getRoomCategoryId!=room.getRoomCategoryId){
        val allReservations = reservationService.listAllByRoomId(existingRoom.getRoomId)
        if(allReservations.length > 0) {
          throw new InstanceUndefinedException(new Error("There are reservations associated with this room.\nSwitching the room category will affect the reservation prices.\nPlease update or delete the existing reservations before proceeding with this operation."))
        }}
      case None =>
    }

    room.setPrice(roomCategory.getPrice)
    val savedRoom = roomRepository.save(tempConverter.roomDtoToEntity(room))
    tempConverter.roomEntityToDto(savedRoom)
  }

  @Transactional(readOnly = true)
  override def getRoomById(roomId: Integer): RoomDto = {
    val roomEntity = roomRepository.findById(roomId)
      .orElseThrow(() => new InstanceUndefinedException(new Error("The room has not been found!")))
    tempConverter.roomEntityToDto(roomEntity)
  }

  @Transactional
  override def deleteRoom(roomId: Integer): Unit = {
    getRoomById(roomId)
    roomRepository.deleteById(roomId)
    roomRepository.flush()
  }

  @Transactional(readOnly = true)
  override def listAll: Array[RoomDto] = {
    val allRooms = roomRepository.findAll().asScala
    allRooms.collect {
      case roomEntity => tempConverter.roomEntityToDto(roomEntity)
    }.toArray
  }

  @Transactional(readOnly = true)
  override def listAllByCategoryId(categoryId: Integer): Array[RoomDto] = {
    val allRooms = roomRepository.findAllByCategoryId(categoryId).asScala
    allRooms.collect {
      case roomEntity => tempConverter.roomEntityToDto(roomEntity)
    }.toArray
  }

  @Transactional
  override def deleteAllByCategoryId(categoryId: Integer): Unit = {
    roomRepository.deleteAllByCategoryId(categoryId)
    roomRepository.flush()
  }
}
