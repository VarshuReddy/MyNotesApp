package com.example.mynotesapp

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.room.RoomDatabase
import com.example.mynotesapp.Adapter.NotesAdapter
import com.example.mynotesapp.Database.NoteDatabase
import com.example.mynotesapp.Models.Note
import com.example.mynotesapp.Models.NoteViewModel
import com.example.mynotesapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() ,NotesAdapter.NotesClickListener,PopupMenu.OnMenuItemClickListener{

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: NoteDatabase
    private lateinit var viewModel: NoteViewModel
    private lateinit var adapter: NotesAdapter
    private lateinit var selectedNote: Note

    private  val updateNote=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
        if(result.resultCode == Activity.RESULT_OK){

            val note =result.data?.getSerializableExtra("note") as? Note

            if (note!=null){
                viewModel.updateNote(note)
            }

        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(NoteViewModel::class.java)

        viewModel.allNotes.observe(this, { list ->

            list?.let {
                adapter.UpdateList(list)
            }
        })

        database = NoteDatabase.getDatabase(this)
    }

    fun init() {

        binding.rV.setHasFixedSize(true)
        binding.rV.layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
        adapter = NotesAdapter(this, this)
        binding.rV.adapter = adapter

        val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result ->

            if (result.resultCode == Activity.RESULT_OK) {

                val note = result.data?.getSerializableExtra("note") as? Note

                if (note != null) {
                    viewModel.insertNote(note)
                }
            }
        }
        binding.fabAdd.setOnClickListener {

            val intent = Intent(this, AddNote::class.java)
            getContent.launch(intent)

        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newtext: String?): Boolean {

                if (newtext != null) {
                    adapter.filterList(newtext)
                }
                return true
            }

        })

    }

    override fun onItemClicked(note: Note) {
        val intent =Intent(this@MainActivity,AddNote::class.java)
        intent.putExtra("currentNote",note)
        updateNote.launch(intent)
    }

    override fun onLongItemClicked(note: Note, cardView: CardView) {
        selectedNote=note
        popUpDisplay(cardView)
    }

    private fun popUpDisplay(cardView: CardView) {
        val popup =PopupMenu(this,cardView)
        popup.setOnMenuItemClickListener(this@MainActivity)
        popup.inflate(R.menu.popup_menu)
        popup.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.delete){
            viewModel.deleteNote(selectedNote)
            return true
        }
        return false
    }

}