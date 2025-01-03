package com.radovan.spring.dto

import java.lang
import scala.beans.BeanProperty

@SerialVersionUID(1L)
class GuestDto extends Serializable{

  @BeanProperty var guestId: Integer = _

  @BeanProperty var phoneNumber: String = _

  @BeanProperty var idNumber: lang.Long = _

  @BeanProperty var userId: Integer = _

  @BeanProperty var reservationsIds: Array[Integer] = _
}
