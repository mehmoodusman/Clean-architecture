package com.mehmood.core.usecase

import com.mehmood.core.data.Note
import com.mehmood.core.repository.NoteRepository


class AddNote(private val noteRepository: NoteRepository) {
    suspend operator fun invoke(note: Note) = noteRepository.addNote(note)
}