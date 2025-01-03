package com.radovan.spring.dto

import scala.beans.BeanProperty

@SerialVersionUID(1L)
class RoleDto extends Serializable{

  @BeanProperty var id: Integer = _

  @BeanProperty var role: String = _

  @BeanProperty var usersIds: Array[Integer] = _
}
