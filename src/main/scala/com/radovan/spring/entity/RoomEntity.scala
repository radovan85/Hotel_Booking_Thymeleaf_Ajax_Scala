package com.radovan.spring.entity

import jakarta.persistence.{Column, Entity, FetchType, GeneratedValue, GenerationType, Id, JoinColumn, ManyToOne, OneToMany, Table}

import java.util
import scala.beans.BeanProperty

@Entity
@Table(name = "rooms")
@SerialVersionUID(1L)
class RoomEntity extends Serializable{

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "room_id")
  @BeanProperty var roomId:Integer = _

  @Column(name = "room_number", nullable = false,unique = true)
  @BeanProperty var roomNumber:Integer = _

  @Column(nullable = false)
  @BeanProperty var price:Float = _

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "category_id")
  @BeanProperty var roomCategory:RoomCategoryEntity = _

  @OneToMany(mappedBy = "room", fetch = FetchType.EAGER, orphanRemoval = true)
  @BeanProperty var reservations:util.List[ReservationEntity] = _
}
