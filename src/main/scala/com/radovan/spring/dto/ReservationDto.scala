package com.radovan.spring.dto

import java.time.{Instant, LocalDateTime, ZoneId, ZonedDateTime}
import java.time.format.DateTimeFormatter
import scala.beans.BeanProperty

@SerialVersionUID(1L)
class ReservationDto extends Serializable{

  @BeanProperty var reservationId: Integer = _

  @BeanProperty var roomId: Integer = _

  @BeanProperty var guestId: Integer = _

  @BeanProperty var checkInDateStr: String = _

  @BeanProperty var checkOutDateStr: String = _

  @BeanProperty var createdAtStr: String = _

  @BeanProperty var updatedAtStr: String = _

  @BeanProperty var price: Float = _

  @BeanProperty var numberOfNights: Integer = _

  import java.time.Instant
  import java.time.LocalDateTime
  import java.time.ZoneId
  import java.time.ZonedDateTime
  import java.time.format.DateTimeFormatter

  def possibleCancel: Boolean = {
    var returnValue = false
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    val currentTime = Instant.now.atZone(ZoneId.of("UTC"))
    val currentTimeLocal = currentTime.toLocalDateTime
    val cancelDate = currentTimeLocal.plusDays(1)
    val checkInDateTime = LocalDateTime.parse(checkInDateStr, formatter).atZone(ZoneId.of("UTC"))
    if (cancelDate.isBefore(checkInDateTime.toLocalDateTime)) returnValue = true
    returnValue
  }
}
