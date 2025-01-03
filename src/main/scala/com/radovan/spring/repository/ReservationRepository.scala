package com.radovan.spring.repository

import com.radovan.spring.entity.ReservationEntity
import org.springframework.data.jpa.repository.{JpaRepository, Modifying, Query}
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

import java.sql.Timestamp
import java.util

@Repository
trait ReservationRepository extends JpaRepository[ReservationEntity, Integer] {

  @Query(value = "select * from reservations where guest_id = :guestId", nativeQuery = true)
  def findAllByGuestId(@Param("guestId") guestId: Integer): util.List[ReservationEntity]

  @Query(value = "select * from reservations where room_id = :roomId", nativeQuery = true)
  def findAllByRoomId(@Param("roomId") roomId: Integer): util.List[ReservationEntity]

  @Query(
    value = "SELECT * FROM reservations WHERE check_out > :currentDateTime",
    nativeQuery = true
  )
  def findAllActiveReservations(currentDateTime: Timestamp): util.List[ReservationEntity]

  @Query(
    value = "SELECT * FROM reservations WHERE check_out < :currentDateTime",
    nativeQuery = true
  )
  def findAllExpiredReservations(currentDateTime: Timestamp): util.List[ReservationEntity]

  @Query(
    value =
      """
        SELECT COUNT(*) FROM reservations
        WHERE room_id = :roomId
        AND (:checkInDate < check_out AND :checkOutDate > check_in)
      """,
    nativeQuery = true
  )
  def countOverlappingReservations(roomId: Integer, checkInDate: Timestamp, checkOutDate: Timestamp): Long

  @Modifying
  @Query(
    value =
      """
      DELETE FROM reservations WHERE room_id = :roomId
    """,
    nativeQuery = true
  )
  def deleteAllByRoomId(roomId: Integer): Unit
}
