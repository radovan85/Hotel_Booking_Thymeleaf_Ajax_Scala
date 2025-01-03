package com.radovan.spring.entity

import jakarta.persistence.{Column, Entity, FetchType, GeneratedValue, GenerationType, Id, JoinColumn, OneToMany, OneToOne, Table}

import java.util
import scala.beans.BeanProperty

@Entity
@Table(name = "guests")
@SerialVersionUID(1L)
class GuestEntity extends Serializable{

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "guest_id")
  @BeanProperty var guestId:Integer = _

  @Column(name = "phone_number", nullable = false, length = 15)
  @BeanProperty var phoneNumber:String = _

  @Column(name = "id_number", nullable = false)
  @BeanProperty var idNumber:Long = _

  @OneToOne(fetch = FetchType.EAGER, orphanRemoval = true)
  @JoinColumn(name = "user_id")
  @BeanProperty var user:UserEntity = _

  @OneToMany(mappedBy = "guest", fetch = FetchType.EAGER, orphanRemoval = true)
  @BeanProperty var reservations:util.List[ReservationEntity] = _
}
