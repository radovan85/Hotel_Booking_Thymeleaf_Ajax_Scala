package com.radovan.spring.services.impl

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.NoteDto
import com.radovan.spring.exceptions.InstanceUndefinedException
import com.radovan.spring.repository.NoteRepository
import com.radovan.spring.services.NoteService

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.sql.Timestamp
import java.time.{ZoneId, ZonedDateTime}
import scala.jdk.CollectionConverters._

@Service
class NoteServiceImpl extends NoteService {

  private var noteRepository:NoteRepository = _
  private var tempConverter:TempConverter = _

  @Autowired
  private def initialize(noteRepository: NoteRepository,tempConverter: TempConverter):Unit = {
    this.noteRepository = noteRepository
    this.tempConverter = tempConverter
  }

  @Transactional(readOnly = true)
  override def getNoteById(noteId: Integer): NoteDto = {
    val noteEntity = noteRepository.findById(noteId)
      .orElseThrow(() => new InstanceUndefinedException(new Error("The note has not been found!")))
    tempConverter.noteEntityToDto(noteEntity)
  }

  @Transactional
  override def deleteNote(noteId: Integer): Unit = {
    getNoteById(noteId)
    noteRepository.deleteById(noteId)
    noteRepository.flush()
  }

  @Transactional(readOnly = true)
  override def listAll: Array[NoteDto] = {
    val allNotes = noteRepository.findAll().asScala
    allNotes.collect{
      case noteEntity => tempConverter.noteEntityToDto(noteEntity)
    }.toArray
  }

  @Transactional(readOnly = true)
  def listAllForToday: Array[NoteDto] = {
    // Postavljanje vremenskog opsega
    val now = ZonedDateTime.now(ZoneId.of("UTC"))
    val startOfDay = Timestamp.valueOf(now.toLocalDate.atStartOfDay)
    val endOfDay = Timestamp.valueOf(now.toLocalDate.atTime(23, 59, 59, 999000000))

    // Pozivanje metode iz repository-ja sa start i end vremenskim opsegom
    val noteEntities = noteRepository.findNotesForToday(startOfDay, endOfDay).asScala

    // Mapiranje rezultata u NoteDto
    noteEntities.collect{
      case noteEntity => tempConverter.noteEntityToDto(noteEntity)
    }.toArray
  }

  @Transactional
  override def deleteAllNotes: Unit = {
    noteRepository.deleteAll()
    noteRepository.flush()
  }
}
