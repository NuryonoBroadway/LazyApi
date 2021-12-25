package id.nuryono.adnan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.nuryono.adnan.databinding.ItemListBinding
import id.nuryono.adnan.model.Items

class GameReviewAdapter : RecyclerView.Adapter<GameReviewAdapter.ViewHolder>() {

    // membuat empyt list dari model Items
    private var games = emptyList<Items>()

    // membuat listener untuk mengetahui position items yang diclick
    private var listener : ((List<Items>, Int) -> Unit)? = null


    inner class ViewHolder(private var binding: ItemListBinding): RecyclerView.ViewHolder(binding.root) {

        // function untuk bind items di adapter
        fun bindItem(game: Items, listener: ((List<Items>, Int) -> Unit)?) {
            binding.tvTitle.text = game.title
            binding.tvDate.text = game.time
            binding.tvAuthor.text = game.author
            binding.tvDescription.text = game.desc

            // load image dari internet
            Glide.with(itemView).load(game.thumb).placeholder(android.R.color.darker_gray).into(binding.ivGames)

            // onClick setiap itemView pada adapter
            itemView.setOnClickListener {
                if(listener != null) {
                    listener(games, adapterPosition)
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
        holder.bindItem(games[position], listener)
    }

    // menghitung jumlah items yang akan dibuat di adapter
    override fun getItemCount(): Int = games.size

    // function untuk listen click pada setiap item di adapter
    fun onClick(listener: ( List<Items>, Int ) -> Unit) {
        this.listener = listener
    }

    // function untuk set data
    fun setData(games: List<Items>) {
        this.games = games
        notifyDataSetChanged()
    }
}