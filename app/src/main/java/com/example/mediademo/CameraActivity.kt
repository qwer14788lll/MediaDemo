package com.example.mediademo

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.File

class CameraActivity : AppCompatActivity() {
    private val takePhoto = 1
    private val fromAlbum = 2

    //输出的图片
    private lateinit var outPutImage: File

    //图片的地址
    private lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        takePhontoButton.setOnClickListener {
            //创建File文件对象，指定存储位置和文件名
            //externalCacheDir：APP关联的缓存目录
            outPutImage = File(externalCacheDir, "output_image.jpg")
            if (outPutImage.exists()) {
                outPutImage.delete()
            }
            outPutImage.createNewFile()

            imageUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(this, "com.example.mediademo.fileprovider", outPutImage)
            } else {
                Uri.fromFile(outPutImage)
            }

            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            startActivityForResult(intent, takePhoto)
        }
        fromAlbumBtn.setOnClickListener {
            //打开文件选择器
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            //开启类别分类
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            //指定类型为图片类型
            intent.type = "image/*"
            startActivityForResult(intent, fromAlbum)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            takePhoto -> {
                if (resultCode == Activity.RESULT_OK) {
                    val bitmap =
                        BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri))
                    imageView.setImageBitmap(rotateIfRequired(bitmap))
                }
            }
            fromAlbum -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    data.data?.let {
                        imageView.setImageBitmap(getBitmapFromUri(it))
                    }
                }
            }
        }
    }

    /**
     * Uri转Bitmap
     */
    private fun getBitmapFromUri(uri: Uri) = contentResolver.openFileDescriptor(uri, "r")
        ?.use { BitmapFactory.decodeFileDescriptor(it.fileDescriptor) }


    /**
     * 判断图片是否需要旋转
     */
    private fun rotateIfRequired(bitmap: Bitmap): Bitmap {
        val exif = ExifInterface(outPutImage.path)
        return when (exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270)
            else -> bitmap
        }
    }

    /**
     * 旋转图片
     */
    private fun rotateBitmap(bitmap: Bitmap, degree: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        val rotateBitmap =
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        //回收不再需要的bitmap对象
        bitmap.recycle()
        return rotateBitmap
    }
}