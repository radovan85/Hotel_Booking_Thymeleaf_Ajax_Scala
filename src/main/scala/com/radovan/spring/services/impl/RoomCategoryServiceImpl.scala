package com.radovan.spring.services.impl

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.RoomCategoryDto
import com.radovan.spring.exceptions.InstanceUndefinedException
import com.radovan.spring.repository.RoomCategoryRepository
import com.radovan.spring.services.RoomCategoryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.jdk.CollectionConverters._

@Service
class RoomCategoryServiceImpl extends RoomCategoryService {

  private var categoryRepository: RoomCategoryRepository = _
  private var tempConverter: TempConverter = _

  @Autowired
  private def initialize(categoryRepository: RoomCategoryRepository, tempConverter: TempConverter): Unit = {
    this.categoryRepository = categoryRepository
    this.tempConverter = tempConverter
  }

  @Transactional
  override def addCategory(category: RoomCategoryDto): RoomCategoryDto = {
    val storedCategory = categoryRepository.save(tempConverter.roomCategoryDtoToEntity(category))
    tempConverter.roomCategoryEntityToDto(storedCategory)
  }

  @Transactional(readOnly = true)
  override def getCategoryById(categoryId: Integer): RoomCategoryDto = {
    val categoryEntity = categoryRepository.findById(categoryId)
      .orElseThrow(() => new InstanceUndefinedException(new Error("The category has not been found!")))
    tempConverter.roomCategoryEntityToDto(categoryEntity)
  }

  @Transactional
  override def deleteCategory(categoryId: Integer): Unit = {
    getCategoryById(categoryId)
    categoryRepository.deleteById(categoryId)
    categoryRepository.flush()
  }

  @Transactional(readOnly = true)
  override def listAll: Array[RoomCategoryDto] = {
    val allCategories = categoryRepository.findAll().asScala
    allCategories.collect {
      case categoryEntity => tempConverter.roomCategoryEntityToDto(categoryEntity)
    }.toArray
  }
}
