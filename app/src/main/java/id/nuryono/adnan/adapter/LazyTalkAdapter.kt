package id.nuryono.adnan.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.nuryono.adnan.R
import id.nuryono.adnan.databinding.ItemListBinding
import id.nuryono.adnan.model.Items

class LazyTalkAdapter: RecyclerView.Adapter<LazyTalkAdapter.ViewHolder>() {
    private var lazy = emptyList<Items>()
    private var listener: ((List<Items>, Int) -> Unit)? = null

    inner class ViewHolder(private var binding: ItemListBinding): RecyclerView.ViewHolder(binding.root) {

        // function untuk bind items di adapter
        fun bindItem(items: Items, listener: ((List<Items>, Int) -> Unit)?) {
            binding.tvTitle.text = items.title
            binding.tvDate.text = items.time
            binding.tvAuthor.text = items.author
            binding.tvDescription.text = items.desc

            // load image dari internet
            Glide.with(itemView).load(items.thumb).placeholder(android.R.color.darker_gray).into(binding.ivGames)

            // onClick setiap itemView pada adapter
            itemView.setOnClickListener {
                if(listener != null) {
                    listener(lazy, adapterPosition)
                }
            }
        }

    }

    // deklarasi views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder(ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    // menampilkan items
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(lazy[position], listener)
    }

    // menghitung jumlah items yang akan di buat pada adapter
    override fun getItemCount(): Int = lazy.size

    // function untuk listen setiap click pada adapter
    fun onClick(listener: ((List<Items>, Int) -> Unit)?) {
        this.listener = listener
    }

    // function untuk set data
    fun setData(lazy: List<Items>) {
        this.lazy = lazy
        notifyDataSetChanged()
    }
}