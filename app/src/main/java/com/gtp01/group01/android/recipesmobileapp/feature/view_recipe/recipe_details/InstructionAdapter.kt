package com.gtp01.group01.android.recipesmobileapp.feature.view_recipe.recipe_details

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gtp01.group01.android.recipesmobileapp.R

class InstructionAdapter (private val context: Context, private var instructions: Array<String>) : RecyclerView.Adapter<InstructionAdapter.ViewHolder>(){



        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.instructions_list_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(instructions[position])
        }

        override fun getItemCount(): Int = instructions.size

        fun updateInstructions(newInstructions: Array<String>) {
            instructions = newInstructions
            notifyDataSetChanged()
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val textIngredientName: TextView = itemView.findViewById(R.id.tvInstructions)

            fun bind(ingredient: String) {
                textIngredientName.text = ingredient
            }
        }
    }
