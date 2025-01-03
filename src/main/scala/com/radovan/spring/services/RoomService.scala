package com.radovan.spring.services

import com.radovan.spring.dto.RoomDto

trait RoomService {

  def addRoom(room:RoomDto):RoomDto

  def getRoomById(roomId:Integer):RoomDto

  def deleteRoom(roomId:Integer):Unit

  def listAll:Array[RoomDto]

  def listAllByCategoryId(categoryId:Integer):Array[RoomDto]

  def deleteAllByCategoryId(categoryId:Integer):Unit

}
