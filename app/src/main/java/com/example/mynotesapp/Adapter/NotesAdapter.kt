package com.example.mynotesapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.mynotesapp.Models.Note
import com.example.mynotesapp.R
import kotlin.random.Random

class NotesAdapter(private val context: Context, val listener: NotesClickListener) :
    RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    private val NotesList= ArrayList<Note>()
    private val FullList = ArrayList<Note>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        )

    }

    override fun getItemCount(): Int {
        return NotesList.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = NotesList[position]
        holder.title.text = currentNote.title
        holder.title.isSelected = true

        holder.tvNotes.text = currentNote.note
        holder.date.text = currentNote.date
        holder.date.isSelected = true

        holder.notes_layout.setCardBackgroundColor(
            holder.itemView.resources.getColor(
                RandomColor(),
                null
            )
        )

        holder.notes_layout.setOnClickListener {
            listener.onItemClicked(NotesList[holder.adapterPosition])
        }

        holder.notes_layout.setOnLongClickListener {
            listener.onLongItemClicked(NotesList[holder.adapterPosition], holder.notes_layout)
            true
        }


    }

    fun UpdateList(newList: List<Note>) {
        FullList.clear()
        FullList.addAll(newList)

        NotesList.clear()
        NotesList.addAll(FullList)
        notifyDataSetChanged()
    }

    fun filterList(search: String) {

        NotesList.clear()

        for (item in FullList) {
            if (item.title?.lowercase()?.contains(search.lowercase()) == true ||
                item.note?.lowercase()?.contains(search.lowercase()) == true
            ) {
                NotesList.add(item)
            }
        }
        notifyDataSetChanged()
    }


    fun RandomColor(): Int {
        val list = ArrayList<Int>()
        list.add(R.color.Note1)
        list.add(R.color.Note2)
        list.add(R.color.Note3)
        list.add(R.color.Note4)
        list.add(R.color.Note5)
        list.add(R.color.Note6)

        //based on the seed it will create a random
        val seed = System.currentTimeMillis().toInt()
        val random = Random(seed).nextInt(list.size)

        return list[random]

    }

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val notes_layout = itemView.findViewById<CardView>(R.id.card_layout)
        val title = itemView.findViewById<TextView>(R.id.tvTitle)
        val tvNotes = itemView.findViewById<TextView>(R.id.tvNote)
        val date = itemView.findViewById<TextView>(R.id.tvDate)

    }

    interface NotesClickListener {

        fun onItemClicked(note: Note)
        fun onLongItemClicked(note: Note, cardView: CardView)

    }

}



