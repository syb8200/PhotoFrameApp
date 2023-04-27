package fastcampus.part1.fc_1_chapter8

import android.Manifest.permission.*
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import fastcampus.part1.fc_1_chapter8.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var imageAdapter : ImageAdapter
    private val imageLoadLauncher = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uriList ->
        updateImages(uriList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.toolBar.apply {
            title = "사진 가져오기"
            setSupportActionBar(this)
        }


        binding.loadImageButton.setOnClickListener {
            checkPermission()
        }

        binding.navigateFrameActivityButton.setOnClickListener {
            navigateToFrameActivity()
        }

        initRecyclerView()
    }

    // 메뉴가 생기게 된다.
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    // +버튼 눌렀을 때 수행하는 기능
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_add -> {
                checkPermission()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun navigateToFrameActivity() {
        val images = imageAdapter.currentList.filterIsInstance<ImageItems.Image>().map { it.uri.toString() }.toTypedArray()
        val intent = Intent(this, FrameActivity::class.java)
            .putExtra("images", images)
        startActivity(intent)
    }

    private fun initRecyclerView() {
        imageAdapter = ImageAdapter(object : ImageAdapter.ItemClickListener {
            override fun onLoadMoreClick() {
                checkPermission()
            }
        })

        // 어댑터 선언 및 layoutManager 지정
        binding.imageRecyclerView.apply {
            adapter = imageAdapter
            layoutManager = GridLayoutManager(context, 2)
        }
    }

    private fun checkPermission() {
        when {
            ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED -> {
                // 권한이 허락되었을 때 실행
                loadImage()
            }
            // 권한이 허락되지 않았을 때 실행(PERMISSION_DENIED) : 팝업을 보여줄지말지 체킹 필요
            shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE) -> {
                showPermissionInfoDialog()
            }
            else -> {
                requestReadExternalStorage()
            }
        }
    }

    private fun showPermissionInfoDialog() {
        AlertDialog.Builder(this).apply {
            setMessage("이미지를 가져오기 위해서 외부 저장소 읽기 권한이 필요합니다.")
            setNegativeButton("취소", null)
            setPositiveButton("동의") { _, _ ->
                requestReadExternalStorage()
            }
        }.show()
    }

    private fun loadImage() {
        // 이미지 타입의 모든 파일을 갖고 오겠다. (MIME 타입)
        imageLoadLauncher.launch("image/*")
    }

    private fun requestReadExternalStorage() {
        // 권한이 여러개일 경우 arrayOf() 사용한다.
        ActivityCompat.requestPermissions(this, arrayOf(READ_EXTERNAL_STORAGE), REQUEST_READ_EXTERNAL_STORAGE)
    }

    private fun updateImages(uriList: List<Uri>) {
        Log.i("updateImages", "$uriList")
        val images = uriList.map { ImageItems.Image(it) }
        // 기존에 있던 것에서 추가됨 (매번 새롭게 갱신되지 않음)
        val updatedImages = imageAdapter.currentList.toMutableList().apply {
            addAll(images)
        }
         imageAdapter.submitList(updatedImages) // submitList : 데이터 변경사항 알아서 처리
    }

    // Permissions Allow 누른 다음 다시 버튼 누르지 않도록 편의성 제공
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode) {
            REQUEST_READ_EXTERNAL_STORAGE -> {
                val resultCode = grantResults.firstOrNull() ?: PackageManager.PERMISSION_DENIED // NPE 개선
                if (resultCode == PackageManager.PERMISSION_GRANTED) {
                    loadImage()
                }
            }
        }
    }

    companion object {
        const val REQUEST_READ_EXTERNAL_STORAGE = 100
    }
}