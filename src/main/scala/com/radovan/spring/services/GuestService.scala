package com.radovan.spring.services

import com.radovan.spring.dto.GuestDto
import com.radovan.spring.utils.RegistrationForm

trait GuestService {

  def addGuest(guest:GuestDto):GuestDto

  def getGuestById(guestId:Integer):GuestDto

  def getGuestByUserId(userId:Integer):GuestDto

  def deleteGuest(guestId:Integer):Unit

  def listAll:Array[GuestDto]

  def storeGuest(form:RegistrationForm):GuestDto
}
