package id.nuryono.adnan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.nuryono.adnan.databinding.ItemListBinding
import id.nuryono.adnan.model.Items

class EsportAdapter: RecyclerView.Adapter<EsportAdapter.ViewHolder>() {

    // membuat empyt list dari model Items
    private var esport = emptyList<Items>()

    // membuat listener untuk mengetahui position items yang diclick
    private var listener : ((List<Items>, Int) -> Unit)? = null

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
                    listener(esport, adapterPosition)
                }
            }
        }

    }

    // deklarasi views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    // menampilkan data
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
         holder.bindItem(esport[position], listener)
    }

    // menghitung jumlah items
    override fun getItemCount(): Int = esport.size

    // function untuk listen click pada setiap item di adapter
    fun onClick(listener: ( List<Items>, Int) -> Unit) {
        this.listener = listener
    }

    // function untuk set data
    fun setData(esport: List<Items>) {
        this.esport = esport
        notifyDataSetChanged()
    }

}