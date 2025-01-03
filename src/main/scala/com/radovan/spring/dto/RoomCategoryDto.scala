package com.radovan.spring.dto

import scala.beans.BeanProperty

@SerialVersionUID(1L)
class RoomCategoryDto extends Serializable{

  @BeanProperty var roomCategoryId: Integer = _

  @BeanProperty var name: String = _

  @BeanProperty var price: Float = _

  @BeanProperty var wifi: Short = _

  @BeanProperty var wc: Short = _

  @BeanProperty var tv: Short = _

  @BeanProperty var bar:  Short= _

  @BeanProperty var roomsIds: Array[Integer] = _
}
