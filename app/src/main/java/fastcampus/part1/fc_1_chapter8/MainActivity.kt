package fastcampus.part1.fc_1_chapter8

import android.Manifest.permission.*
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import fastcampus.part1.fc_1_chapter8.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.loadImageButton.setOnClickListener {
            checkPermission()
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

        // 권한이 여러개일 경우 arrayOf() 사용한다.
        ActivityCompat.requestPermissions(this, arrayOf(READ_EXTERNAL_STORAGE), REQUEST_READ_EXTERNAL_STORAGE)
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
        Toast.makeText(this, "이미지를 가져올 예정", Toast.LENGTH_SHORT).show()
    }

    private fun requestReadExternalStorage() {

    }

    companion object {
        const val REQUEST_READ_EXTERNAL_STORAGE = 100
    }
}