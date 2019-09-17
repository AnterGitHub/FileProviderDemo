package com.konka.fileproviderdemo

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider.getUriForFile
import com.konka.fileproviderdemo.Util.Companion.copyAssetsFileToLocal
import java.io.File


class MainActivity : AppCompatActivity() {
    companion object {
        private val TAG = "MainActivity"
        private val AUTHORITY = "com.konka.fileproviderdemo"
        private val fileDir =
            Environment.getExternalStorageDirectory().absolutePath + File.separator + "shared_files"
    }

    private lateinit var mainTv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainTv = findViewById(R.id.mainBtn)
        Util.requestAppPermissions(this@MainActivity, 1024)

        Thread(Runnable {
            // 或者手动拷贝文件到 file_provider_test.jpg sdcard/shared_files 下
            copyAssetsFileToLocal(this, "file_provider_test.jpg")
        }).start()

        findViewById<TextView>(R.id.mainBtn).setOnClickListener {
            openFile(this@MainActivity, "file_provider_test.jpg")
        }
    }

    fun openFile(context: Context, fileName: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val file = File(fileDir, fileName)

        var uri = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Uri.fromFile(file)
        } else {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            getUriForFile(context, AUTHORITY, file)
        }
        intent.setDataAndType(uri, MapTable.getMIMEType(fileName))
        startActivity(Intent.createChooser(intent, null))
        Log.d(TAG, "URI = ${getUriForFile(context, AUTHORITY, file)}")

    }

}
