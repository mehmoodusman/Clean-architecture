package com.mehmood.memorynotes.framework

import android.content.Context
import com.mehmood.core.data.Note
import com.mehmood.core.repository.NoteDataSource
import com.mehmood.memorynotes.framework.db.DatabaseService
import com.mehmood.memorynotes.framework.db.NoteEntity

class RoomNoteDataSource(context: Context): NoteDataSource {
    val noteDao = DatabaseService.getInstance(context).noteDao()

    override suspend fun add(note: Note) = noteDao.addNoteEntity(NoteEntity.fromNote(note))

    override suspend fun get(id: Long) = noteDao.getNoteEntity(id)?.toNote()

    override suspend fun getAll() = noteDao.getAllNoteEntities().map { it.toNote() }

    override suspend fun remove(note: Note) = noteDao.deleteNoteEntity(NoteEntity.fromNote(note))
}