package com.radovan.spring.repository

import com.radovan.spring.entity.RoomCategoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
trait RoomCategoryRepository extends JpaRepository[RoomCategoryEntity, Integer]{

}
