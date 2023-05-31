package com.fitness.fitnessCru.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.model.RecipeHowToMakeModel

class RecipeHowToMakeAdapter(
    var recipeHowToMakeModel: ArrayList<RecipeHowToMakeModel>,
    var context: Context?
) :
    RecyclerView.Adapter<RecipeHowToMakeAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var planText: TextView = itemView.findViewById(R.id.plan_text)
        var image: ImageView = itemView.findViewById(R.id.circle_icon)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecipeHowToMakeAdapter.ViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recipe_rv_how_to_make_item, parent, false)
        return RecipeHowToMakeAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeHowToMakeAdapter.ViewHolder, position: Int) {
        holder.planText.text = recipeHowToMakeModel[position].planText.toString()
        Glide.with(context!!)
            .load(recipeHowToMakeModel[position].image)
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(holder.image)
    }

    override fun getItemCount(): Int {
        return recipeHowToMakeModel.size
    }
}