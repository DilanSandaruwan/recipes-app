package com.gtp01.group01.android.recipesmobileapp.feature.view_recipe.recipe_details

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gtp01.group01.android.recipesmobileapp.R
/**
 * Adapter for the RecyclerView to display a list of instructions.
 *
 * @param context The context in which the adapter is used.
 * @param instructions The array of instructions to be displayed.
 */
class InstructionAdapter(private val context: Context, private var instructions: Array<String>) :
    RecyclerView.Adapter<InstructionAdapter.ViewHolder>() {
    /**
     * Called when RecyclerView needs a new [ViewHolder] of the given type to represent an item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.instructions_list_item, parent, false)
        return ViewHolder(view)
    }
    /**
     * Called by RecyclerView to display the data at the specified position.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(instructions[position])
    }
    /**
     * Returns the total number of items in the data set held by the adapter.
     */
    override fun getItemCount(): Int = instructions.size
    /**
     * Updates the list of instructions with new data and notifies the adapter of the change.
     *
     * @param newInstructions The new array of instructions to be displayed.
     */
    fun updateInstructions(newInstructions: Array<String>) {
        instructions = newInstructions
        notifyDataSetChanged()
    }
    /**
     * The ViewHolder class represents each item in the RecyclerView.
     *
     * @param itemView The view for each item in the RecyclerView.
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textIngredientName: TextView = itemView.findViewById(R.id.tvInstructions)
        /**
         * Binds the instruction data to the ViewHolder.
         *
         * @param instruction The instruction to be displayed.
         */
        fun bind(ingredient: String) {
            textIngredientName.text = ingredient
        }
    }
}
