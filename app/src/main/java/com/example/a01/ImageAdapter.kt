package com.example.a01

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.a01.categories.BloodActivity
import com.example.a01.categories.BooksActivity
import com.example.a01.categories.ClothesActivity
import com.example.a01.categories.FoodActivity
import com.example.a01.categories.MedicineActivity

class ImageAdapter(private val imageList: ArrayList<Int>, private val viewPager2: ViewPager2) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView);
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.image_container, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.imageView.setImageResource(imageList[position])
        if (position == imageList.size-1){
            viewPager2.post(runnable)
        }

        val imageRes = imageList[position]
        holder.imageView.setImageResource(imageRes)

        // Set OnClickListener for the image
        holder.itemView.setOnClickListener {
            // Open a different activity based on the selected image
            when (imageRes) {
                R.drawable.food -> {
                    val intent = Intent(holder.itemView.context, FoodActivity::class.java)
                    holder.itemView.context.startActivity(intent)
                }
                R.drawable.cloths -> {
                    val intent = Intent(holder.itemView.context, ClothesActivity::class.java)
                    holder.itemView.context.startActivity(intent)
                }
                R.drawable.books -> {
                    val intent = Intent(holder.itemView.context, BooksActivity::class.java)
                    holder.itemView.context.startActivity(intent)
                }
                R.drawable.blood -> {
                    val intent = Intent(holder.itemView.context, BloodActivity::class.java)
                    holder.itemView.context.startActivity(intent)
                }
                R.drawable.medicine -> {
                    val intent = Intent(holder.itemView.context, MedicineActivity::class.java)
                    holder.itemView.context.startActivity(intent)
                }
                // Add more cases for other images if needed
                else -> {
                    // Handle the default case or do nothing
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    private val runnable = Runnable {
        imageList.addAll(imageList)
        notifyDataSetChanged()
    }
}