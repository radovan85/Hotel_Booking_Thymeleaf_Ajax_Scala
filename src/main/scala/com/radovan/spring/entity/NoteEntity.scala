package com.radovan.spring.entity

import jakarta.persistence.{Column, Entity, GeneratedValue, GenerationType, Id, Table}

import java.sql.Timestamp
import scala.beans.BeanProperty

@Entity
@Table(name = "notes")
@SerialVersionUID(1L)
class NoteEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "note_id")
  @BeanProperty var noteId:Integer = _

  @Column(nullable = false, length = 40)
  @BeanProperty var subject:String = _

  @Column(nullable = false, length = 255)
  @BeanProperty var text:String = _

  @Column(name = "created", nullable = false)
  @BeanProperty var createdAt:Timestamp = _

}
