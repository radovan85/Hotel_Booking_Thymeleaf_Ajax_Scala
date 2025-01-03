package com.radovan.spring.services

import com.radovan.spring.dto.RoomCategoryDto

trait RoomCategoryService {

  def addCategory(category:RoomCategoryDto):RoomCategoryDto

  def getCategoryById(categoryId:Integer):RoomCategoryDto

  def deleteCategory(categoryId:Integer):Unit

  def listAll:Array[RoomCategoryDto]
}
