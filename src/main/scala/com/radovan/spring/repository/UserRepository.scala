package com.radovan.spring.repository

import com.radovan.spring.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
trait UserRepository extends JpaRepository[UserEntity, Integer]{

  def findByEmail(email:String):Option[UserEntity]
}
