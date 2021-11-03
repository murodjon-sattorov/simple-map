package uz.murodjon_sattorov.yotoqxona_task.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.murodjon_sattorov.yotoqxona_task.Model
import uz.murodjon_sattorov.yotoqxona_task.R
import uz.murodjon_sattorov.yotoqxona_task.databinding.ImageItemBinding

/**
 * Created by <a href="mailto: sattorovmurodjon43@gmail.com">Murodjon Sattorov</a>
 *
 * @author Murodjon
 * @date 10/26/2021
 * @project Yotoqxona-task
 */
class ImagesAdapter : RecyclerView.Adapter<ImagesAdapter.ViewHolder>() {

    private var data = ArrayList<Model>()
    fun addData(d: List<Model>) {
        data.clear()
        data.addAll(d)
    }

    inner class ViewHolder(var binding: ImageItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(model: Model) {
            Glide.with(binding.imageView.context).load(model.images).into(binding.imageView)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ImageItemBinding.bind(
                LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false)
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }



}