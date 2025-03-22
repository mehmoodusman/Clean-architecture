package com.mehmood.memorynotes.framework.di

import android.app.Application
import com.mehmood.core.repository.NoteRepository
import com.mehmood.memorynotes.framework.RoomNoteDataSource
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @Provides
    fun provideRepository(app: Application) = NoteRepository(RoomNoteDataSource(app))
}