package com.radovan.spring.repository

import com.radovan.spring.entity.GuestEntity
import org.springframework.data.jpa.repository.{JpaRepository, Query}
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
trait GuestRepository extends JpaRepository[GuestEntity, Integer]{

  @Query(value = "select * from guests where user_id = :userId", nativeQuery = true)
  def findByUserId(@Param("userId") userId: Integer): Option[GuestEntity]
}
