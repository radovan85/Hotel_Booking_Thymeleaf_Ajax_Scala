package com.radovan.spring.repository

import com.radovan.spring.entity.NoteEntity
import org.springframework.data.jpa.repository.{JpaRepository, Query}
import org.springframework.stereotype.Repository

import java.util
import java.sql.Timestamp

@Repository
trait NoteRepository extends JpaRepository[NoteEntity, Integer]{

  @Query(
    nativeQuery = true,
    value =
      """
        SELECT * FROM notes
        WHERE created >= :startOfDay AND created <= :endOfDay
      """
  )
  def findNotesForToday(startOfDay: Timestamp, endOfDay: Timestamp): util.List[NoteEntity]

}
