package fastcampus.part1.fc_1_chapter8

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.tabs.TabLayoutMediator
import fastcampus.part1.fc_1_chapter8.databinding.ActivityFrameBinding

class FrameActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFrameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFrameBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.toolBar.apply {
            title = "나만의 앨범"
            setSupportActionBar(this)
        }
        // 뒤로가기
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val images = (intent.getStringArrayExtra("images") ?: emptyArray()).map { uriString -> FrameItem(Uri.parse(uriString))}
        val frameAdapter = FrameAdapter(images)

        binding.viewPager.adapter = frameAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            binding.viewPager.currentItem = tab.position
        }.attach()
    }

    // 뒤로가기 눌렀을 때 수행하는 기능
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            // 안드로이드에서 기본적으로 사용하는 id
            android.R.id.home -> {
                finish()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
}