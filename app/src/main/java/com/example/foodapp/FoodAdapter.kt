package com.example.foodapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodapp.databinding.ItemFoodBinding
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

class FoodAdapter(
    private val data: ArrayList<Food>,
    private val FoodEvents: foodEvents
) :
    RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    inner class FoodViewHolder(itemView: View, private val context: Context) :
        RecyclerView.ViewHolder(itemView) {
        val imageMain = itemView.findViewById<ImageView>(R.id.item_img_main)
        val txtFoodName = itemView.findViewById<TextView>(R.id.item_txt_foodName)
        val txtFoodCompany = itemView.findViewById<TextView>(R.id.item_txt_company)
        val txtFoodPrice = itemView.findViewById<TextView>(R.id.item_txt_price)
        val txtNumberOfRating = itemView.findViewById<TextView>(R.id.item_number_of_rating)
        val ratingBar = itemView.findViewById<RatingBar>(R.id.item_ratingBar)

        //set data
        fun bindData(position: Int) {
            txtFoodName.text = data[position].foodName
            txtFoodCompany.text = data[position].foodCompany
            txtFoodPrice.text = data[position].foodPrice + " VIP"
            txtNumberOfRating.text = "Rating(" + data[position].foodNumberOfRating.toString() + ")"
            ratingBar.rating = data[position].rating

            //loading image from the internet with Glide
            Glide.with(context)
                .load(data[position].urlImage)
                .transform(RoundedCornersTransformation(16, 4))
                .into(imageMain)

            itemView.setOnClickListener {
                FoodEvents.onFoodClicked(data[adapterPosition], adapterPosition)
            }
            itemView.setOnLongClickListener {
                FoodEvents.onFoodLongClick(data[adapterPosition], adapterPosition)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false)
        return FoodViewHolder(view, parent.context)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        holder.bindData(position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    //create a function for adding a food
    fun addFood(newFood: Food) {
        //add food to list
        data.add(0, newFood)
        notifyItemInserted(0)
    }

    //create function for remove food
    fun removeFood(oldFood: Food, oldPosition: Int) {
        //remove item from list:
        data.remove(oldFood)
        notifyItemRemoved(oldPosition)
    }

    //create function for update food
    fun updateFood(newFood: Food, position: Int) {
        //update item from list
        data.set(position, newFood)
        notifyItemChanged(position)
    }

    //create function for data search
    fun setData(newList: ArrayList<Food>) {
        //set a new data to list
        data.clear()
        data.addAll(newList)

        notifyDataSetChanged()
    }

    interface foodEvents {
        //1. create a interface in adapter
        //2. get an object of interface in args adapter
        //3. fill (call) object of interface with your data
        //4. implementation in MainActivity

        fun onFoodClicked(food: Food, position: Int)
        fun onFoodLongClick(food: Food, pos: Int)
    }
}