package com.radovan.spring.services

import com.radovan.spring.dto.ReservationDto

import java.sql.Timestamp

trait ReservationService {

  def addReservation(reservation:ReservationDto,roomId:Integer):ReservationDto

  def getReservationById(reservationId:Integer):ReservationDto

  def deleteReservation(reservationId:Integer):Unit

  def listAll:Array[ReservationDto]

  def listAllByGuestId(guestId:Integer):Array[ReservationDto]

  def listAllByRoomId(roomId:Integer):Array[ReservationDto]

  def listAllActive:Array[ReservationDto]

  def listAllExpired:Array[ReservationDto]

  def isAvailable(roomId:Integer,checkInDate:Timestamp,checkOutDate:Timestamp):Boolean

  def updateReservation(reservation:ReservationDto,reservationId:Integer):ReservationDto

  def deleteAllByRoomId(roomId:Integer):Unit
}
