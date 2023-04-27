package fastcampus.part1.fc_1_chapter8

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fastcampus.part1.fc_1_chapter8.databinding.ItemImageBinding
import fastcampus.part1.fc_1_chapter8.databinding.ItemLoadMoreBinding

class ImageAdapter(private val itemClickListener: ItemClickListener) : ListAdapter<ImageItems, RecyclerView.ViewHolder>(
    //  ListAdapter를 사용했을 때 DiffUtil을 사용해주어야 한다. -> 아이템이 변경되었는지 알아서 확인
    object : DiffUtil.ItemCallback<ImageItems>() {
        override fun areItemsTheSame(oldItem: ImageItems, newItem: ImageItems): Boolean {
            // 참조의 동등성 : ===
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: ImageItems, newItem: ImageItems): Boolean {
            // == : equals()
            return oldItem == newItem
        }
    }
) {

    override fun getItemCount(): Int {
        val originSize = currentList.size
        // footer 추가하기 위해서
        return if(originSize == 0) 0 else originSize.inc()
    }

    override fun getItemViewType(position: Int): Int {
        // 2가지 이상이 되면 아이템 타입을 체킹 해줘야 한다.
        // footer와 다른 것들 viewType 다르게 설정
        return if (itemCount.dec() == position) ITEM_LOAD_MORE else ITEM_IMAGE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        return when(viewType) {
            ITEM_IMAGE -> {
                val binding = ItemImageBinding.inflate(inflater, parent, false)
                ImageViewHolder(binding)
            }
            else -> {
                val binding = ItemLoadMoreBinding.inflate(inflater, parent, false)
                LoadMoreViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // sealed class 이면 자동으로 2개가 선언됨
//        when(val item = currentList[position]) {
//            is ImageItems.Image -> {}
//            ImageItems.LoadMore -> {}
//        }

        when(holder) {
            is ImageViewHolder -> {
                holder.bind(currentList[position] as ImageItems.Image)
            }
            is LoadMoreViewHolder -> {
                holder.bind(itemClickListener)
            }
        }
    }

    interface ItemClickListener {
        fun onLoadMoreClick()
    }

    companion object {
        const val ITEM_IMAGE = 0
        const val ITEM_LOAD_MORE = 1
    }
}

class ImageViewHolder(private val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ImageItems.Image) {
        binding.previewImageView.setImageURI(item.uri)
    }
}

class LoadMoreViewHolder(binding: ItemLoadMoreBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(itemCLickListener: ImageAdapter.ItemClickListener) {
        itemView.setOnClickListener { itemCLickListener.onLoadMoreClick() }
    }
}