package com.konka.fileproviderdemo

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileOutputStream

/**
 * FileProviderDemo
 *
 * @author anter
 * @date 2019-09-10
 */
class Util {
    companion object {
        /**
         * Request this app's all denied permissions
         */
        fun requestAppPermissions(activity: Activity, requestCode: Int) {
            val TAG = "Permission"
            val pm = activity.packageManager
            val deniedPermissions = ArrayList<String>()
            try {
                val info = pm.getPackageInfo(activity.packageName, PackageManager.GET_PERMISSIONS)
                val permissions = info.requestedPermissions ?: return
                for (p in permissions) {
                    Log.e(TAG, "p = $p")
                    if (pm.checkPermission(
                            p,
                            activity.packageName
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        deniedPermissions.add(p)
                        Log.e(TAG, "denied p = $p")
                    }
                }

                val pArray = deniedPermissions.toTypedArray()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && pArray.isNotEmpty()) {
                    activity.requestPermissions(pArray, requestCode)
                }
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

        }

        fun copyAssetsFileToLocal(context: Context, fullFileName: String) {
            val assetManager = context.assets
            val files = assetManager.list("")
            val desDirPath =
                Environment.getExternalStorageDirectory().absolutePath + File.separator + "shared_files"
            val desDir = File(desDirPath)
            files?.forEach {
                if (it.equals(fullFileName)) {
                    if (!desDir.exists() && desDir.mkdirs()) {
                        System.out.println("Create dir success")
                    }
                    val desFilePath = desDirPath + File.separator + fullFileName
                    val `in` = assetManager.open(fullFileName)
                    val out = FileOutputStream(desFilePath)

                    val buffer = ByteArray(1024)
                    var read: Int

                    do {
                        read = `in`.read(buffer)
                        if (read == -1) {
                            break
                        }
                        out.write(buffer, 0, read)

                    } while (true)

                    `in`.close()
                    out.flush()
                    out.close()
                }
            }
        }
    }
}