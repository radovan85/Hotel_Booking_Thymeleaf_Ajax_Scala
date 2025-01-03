package com.radovan.spring.dto

import scala.beans.BeanProperty

@SerialVersionUID(1L)
class UserDto extends Serializable {

  @BeanProperty var id: Integer = _

  @BeanProperty var firstName: String = _

  @BeanProperty var lastName: String = _

  @BeanProperty var email: String = _

  @BeanProperty var password: String = _

  @BeanProperty var enabled: Short = _

  @BeanProperty var rolesIds: Array[Integer] = _
}
