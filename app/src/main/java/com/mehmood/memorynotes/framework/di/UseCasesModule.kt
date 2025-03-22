package com.mehmood.memorynotes.framework.di

import com.mehmood.core.repository.NoteRepository
import com.mehmood.core.usecase.*
import com.mehmood.memorynotes.framework.UseCases
import dagger.Module
import dagger.Provides

@Module
class UseCasesModule {
    @Provides
    fun getUseCases(repository: NoteRepository) = UseCases(
        AddNote(repository),
        GetAllNotes(repository),
        GetNote(repository),
        RemoveNote(repository),
        GetWordCount()
    )
}