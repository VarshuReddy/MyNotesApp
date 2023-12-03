package com.example.mynotesapp.Models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.mynotesapp.Database.NoteDatabase
import com.example.mynotesapp.Database.NotesRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application):AndroidViewModel(application) {

    private  val repository : NotesRepo

    val allNotes :LiveData<List<Note>>

    init{
        val dao =NoteDatabase.getDatabase(application).getNoteDao()
        repository= NotesRepo(dao)
        allNotes =repository.allNotes

    }

    fun deleteNote(note: Note)=viewModelScope.launch(Dispatchers.IO){
        repository.delete(note)
    }
    fun insertNote(note: Note)=viewModelScope.launch(Dispatchers.IO){
        repository.insert(note)
    }
    fun updateNote(note: Note)=viewModelScope.launch(Dispatchers.IO){
        repository.update(note)
    }

}