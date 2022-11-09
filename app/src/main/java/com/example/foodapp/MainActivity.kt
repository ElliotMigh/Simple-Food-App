package com.example.foodapp

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.databinding.ActivityMainBinding
import com.example.foodapp.databinding.DialogAddNewItemBinding
import com.example.foodapp.databinding.DialogDeleteItemBinding
import com.example.foodapp.databinding.DialogUpdateBinding

class MainActivity : AppCompatActivity(), FoodAdapter.foodEvents {
    private lateinit var binding: ActivityMainBinding
    lateinit var myAdapter: FoodAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val foodList = arrayListOf<Food>(
            Food(
                "Cheese burger",
                "Burger king",
                "$12",
                90,
                4.5f,
                "https://wallpaperaccess.com/full/2004483.jpg"
            ),
            Food("Pizza", "Gook", "$20", 120, 3.2f, "https://wallpaperaccess.com/full/866645.jpg"),
            Food(
                "Fried Chicken",
                "KFC",
                "$10",
                500,
                4.9f,
                "https://media-cdn.grubhub.com/image/upload/d_search:browse-images:default.jpg/w_1200,q_auto:low,fl_lossy,dpr_2.0,c_fill,f_auto,h_800,g_auto/i0wyjobl8xn5zsnuhz4k"
            ),
            Food(
                "French Fries",
                "Curly cuts",
                "$2",
                200,
                3.8f,
                "https://upload.wikimedia.org/wikipedia/commons/8/83/French_Fries.JPG"
            ),
            Food(
                "Burger",
                "Mac Donald's",
                "$1",
                1000,
                4.0f,
                "https://imageio.forbes.com/specials-images/dam/imageserve/a3efc4000e5c449a8bc413a2087a59c9/0x0.jpg?format=jpg&width=1200"
            ),
            Food(
                "Fish",
                "Salamon",
                "$19",
                40,
                3.2f,
                "https://www.nordsee.com/fileadmin/_processed_/3/d/csm_Grillen_jeden_Fisch_perfekt_Grillen_99fda99108.jpg"
            ),
            Food(
                "Pasta",
                "Bariila",
                "$14",
                30,
                4.3f,
                "https://www.tastingtable.com/img/gallery/the-real-reason-you-should-always-finish-your-pasta-in-the-sauce/l-intro-1653233952.jpg"
            ),
            Food(
                "English break fast",
                "EE2",
                "$5",
                20,
                3.0f,
                "https://dailydish.co.uk/wp-content/uploads/2021/12/vegan-full-english-breakfast-baked-beans.jpg"
            ),
            Food("Hot Dog", "Masud", "$1", 90, 3.8f, "https://wallpaperaccess.com/full/350720.jpg"),
            Food(
                "Caesar salad",
                "Miami+",
                "$9",
                12,
                3.5f,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/2/23/Caesar_salad_%282%29.jpg/800px-Caesar_salad_%282%29.jpg"
            ),
            Food(
                "Lasania",
                "Babataher",
                "$6",
                66,
                3.1f,
                "https://wallpapercave.com/wp/wp3030132.jpg"
            ),
            Food("Sushi", "Chao", "$90", 23, 3.0f, "https://wallpaperaccess.com/full/286316.jpg")
        )
        myAdapter = FoodAdapter(foodList.clone() as ArrayList<Food>, this)
        binding.recyclerMain.adapter = myAdapter
        binding.recyclerMain.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        //adding food
        binding.btnAddNewFood.setOnClickListener {
            //Create alert dialog
            val dialog = AlertDialog.Builder(this).create()
            val dialogBinding = DialogAddNewItemBinding.inflate(layoutInflater)
            dialog.setView(dialogBinding.root)
            dialog.setCancelable(true)
            dialog.show()

            //click on done button on dialog_add_new_item.xml
            dialogBinding.dialogBtnDone.setOnClickListener {
                if (dialogBinding.etDialogFoodName.length() > 0 &&
                    dialogBinding.etDialogFoodCompany.length() > 0 &&
                    dialogBinding.etDialogFoodPrice.length() > 0
                ) {
                    val txtFoodName = dialogBinding.etDialogFoodName.text.toString()
                    val txtFoodCompany = dialogBinding.etDialogFoodCompany.text.toString()
                    val txtFoodPrice = dialogBinding.etDialogFoodPrice.text.toString()

                    //set random for number of rating & Rating star
                    val numberOfFoodRating: Int = (1..150).random()
                    val ratingBarStar: Float = (0..5).random().toFloat()
                    //set random for image
                    val randomForUrl = (0..11).random()
                    val urlPic = foodList[randomForUrl].urlImage

                    //create a new food
                    val newFood = Food(
                        txtFoodName,
                        txtFoodCompany,
                        txtFoodPrice,
                        numberOfFoodRating,
                        ratingBarStar,
                        urlPic
                    )
                    myAdapter.addFood(newFood)

                    //dismiss alert dialog for close
                    dialog.dismiss()
                    binding.recyclerMain.scrollToPosition(0)
                    Toast.makeText(this, "Done! ;)", Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(this, "You have not entered anything!!!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        //edit text search
        binding.etSearch.addTextChangedListener { editTextInput ->
            if (editTextInput!!.isNotEmpty()) {
                //filter data
                val cloneList = foodList.clone() as ArrayList<Food>
                val filteredList = cloneList.filter { foodItem ->
                    foodItem.foodName.contains(editTextInput)
                }
                myAdapter.setData(ArrayList(filteredList))
            } else {
                //show all data
                myAdapter.setData(foodList.clone() as ArrayList<Food>)
            }
        }
    }

    override fun onFoodClicked(food: Food, position: Int) {
        val dialog = AlertDialog.Builder(this).create()
        val updateDialogBinding = DialogUpdateBinding.inflate(layoutInflater)
        dialog.setView(updateDialogBinding.root)
        dialog.setCancelable(true)
        dialog.show()
        //data
        updateDialogBinding.etDialogFoodName.setText(food.foodName)
        updateDialogBinding.etDialogFoodCompany.setText(food.foodCompany)
        updateDialogBinding.etDialogFoodPrice.setText(food.foodPrice)

        //click on  button in dialog update
        updateDialogBinding.dialogUpdateBtnCancel.setOnClickListener {
            dialog.dismiss()
        }
        updateDialogBinding.dialogUpdateBtnDone.setOnClickListener {

            if (updateDialogBinding.etDialogFoodName.length() > 0 &&
                updateDialogBinding.etDialogFoodCompany.length() > 0 &&
                updateDialogBinding.etDialogFoodPrice.length() > 0
            ) {


                val txtFoodName = updateDialogBinding.etDialogFoodName.text.toString()
                val txtFoodCompany = updateDialogBinding.etDialogFoodCompany.text.toString()
                val txtFoodPrice = updateDialogBinding.etDialogFoodPrice.text.toString()

                //create a new food
                val newFood = Food(
                    txtFoodName,
                    txtFoodCompany,
                    txtFoodPrice,
                    food.foodNumberOfRating,
                    food.rating,
                    food.urlImage
                )
                //update food
                myAdapter.updateFood(newFood, position)

                dialog.dismiss()

            } else {
                Toast.makeText(this, "You have not entered anything!!!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onFoodLongClick(food: Food, pos: Int) {
        val dialog = AlertDialog.Builder(this).create()
        val dialogDeleteBinding = DialogDeleteItemBinding.inflate(layoutInflater)
        dialog.setView(dialogDeleteBinding.root)
        dialog.setCancelable(true)
        dialog.show()

        //click cancel button
        dialogDeleteBinding.btnDialogCancel.setOnClickListener {
            dialog.dismiss()
        }
        //click sure button
        dialogDeleteBinding.btnDialogSure.setOnClickListener {
            dialog.dismiss()
            myAdapter.removeFood(food, pos)
        }
    }
}