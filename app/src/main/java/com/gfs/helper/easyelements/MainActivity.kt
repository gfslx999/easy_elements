package com.gfs.helper.easyelements

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import com.gfs.helper.elements.model.MultiImageConfigModel
import com.gfs.helper.elements.popup.MultiImagesPopupWindow
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private val mMultiImagePopupWindow by lazy { MultiImagesPopupWindow(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnHello = findViewById<Button>(R.id.btn_hello)
        val btnAdd = findViewById<Button>(R.id.btn_add)
        val btnDelete = findViewById<Button>(R.id.btn_delete)
        val imageList = listOf(
            "https://img2.woyaogexing.com/2022/10/19/0d46b2b78daa55c0!360x640.jpg",
            "https://img2.woyaogexing.com/2022/10/19/e147e9b0c1358e1f!360x640.jpg",
            "https://img2.woyaogexing.com/2022/10/19/3f12c781eae6e013!360x640.jpg"
        )
        val imageList2 = listOf(
            "https://img2.woyaogexing.com/2022/10/19/ce6bc74995356d92!360x640.jpg",
            "https://img2.woyaogexing.com/2022/10/19/4075b1c5e1bfb57e!360x640.jpg",
            "https://img2.woyaogexing.com/2022/10/19/3f12c781eae6e013!360x640.jpg",
//            R.mipmap.ic_launcher,
            "https://img0.baidu.com/it/u=4203773040,2236419112&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1666544400&t=c474fc672fc33527f8acf96d96edf80e",
            "https://i0.hdslb.com/bfs/article/10b29b8e5f9aa60d68b29d1679e125e399e0d3cd.jpg@942w_531h_progressive.webp"
        )
        btnHello.setOnClickListener {
            try {
                if (mMultiImagePopupWindow.currentImageListSize == 0) {
                    mMultiImagePopupWindow.showPopupWindowAndSetData(it, imageList2)
                } else {
                    mMultiImagePopupWindow.showPopupWindowNoSetData(it)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        btnAdd.setOnClickListener {
            mMultiImagePopupWindow.insertData("https://img2.woyaogexing.com/2022/10/19/ee71245d156a13e1!360x640.jpg", 0)
        }

        btnDelete.setOnClickListener {
            mMultiImagePopupWindow.deleteData(mMultiImagePopupWindow.currentImageListSize - 1)
        }
    }
}