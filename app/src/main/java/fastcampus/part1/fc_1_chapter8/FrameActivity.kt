package fastcampus.part1.fc_1_chapter8

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import fastcampus.part1.fc_1_chapter8.databinding.ActivityFrameBinding

class FrameActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFrameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFrameBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val images = (intent.getStringArrayExtra("images") ?: emptyArray()).map { uriString -> FrameItem(Uri.parse(uriString))}
        val frameAdapter = FrameAdapter(images)

        binding.viewPager.adapter = frameAdapter
    }
}