package com.example.ieltspreparation.presentation.dashboard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ieltspreparation.R
class VocabularyListAdapter(private var data: ArrayList<VocabularyModel>, internal var context: Context) :
    RecyclerView.Adapter<VocabularyListAdapter.ViewHolder>() {
    var onItemClick:((VocabularyModel) -> Unit)? = null
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val wordE: TextView
        val rigthIV: TextView
        val card: CardView

        init {
            wordE = itemView.findViewById(R.id.cardwordName)
            rigthIV = itemView.findViewById(R.id.cardwordRIv)
            card = itemView.findViewById(R.id.wordCard)
        }

        fun bind(vocabulary: VocabularyModel) {
            wordE.text = vocabulary.wordE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.vocabulary_list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
        holder.card.setOnClickListener {
            onItemClick?.invoke(item)
        }
    }
    fun searchDataList(searchList:List<VocabularyModel>){
        data = searchList as ArrayList<VocabularyModel>
        notifyDataSetChanged()
    }
}