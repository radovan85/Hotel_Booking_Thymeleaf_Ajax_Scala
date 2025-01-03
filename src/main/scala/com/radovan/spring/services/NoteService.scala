package com.radovan.spring.services

import com.radovan.spring.dto.NoteDto

trait NoteService {

  def getNoteById(noteId:Integer):NoteDto

  def deleteNote(noteId:Integer):Unit

  def listAll:Array[NoteDto]

  def listAllForToday:Array[NoteDto]

  def deleteAllNotes:Unit
}
