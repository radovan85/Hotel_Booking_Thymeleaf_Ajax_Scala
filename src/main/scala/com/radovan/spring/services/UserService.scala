package com.radovan.spring.services

import com.radovan.spring.dto.UserDto

trait UserService {

  def getUserById(userId:Integer):UserDto

  def listAll:Array[UserDto]

  def getUserByEmail(email:String):UserDto

  def getCurrentUser:UserDto

  def isAdmin:Boolean
}
