package com.example.chatground2.view.detailForum

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.example.chatground2.Model.Constants
import com.example.chatground2.R
import com.example.chatground2.view.writeForum.WriteForumActivity
import com.example.chatground2.view.writeForum.WriteForumContract
import com.example.chatground2.view.writeForum.WriteForumPresenter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_forum.*
import kotlinx.android.synthetic.main.activity_detail_forum.DF_camera
import kotlinx.android.synthetic.main.activity_detail_forum.DF_deleteButton
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_write_forum.*
import kotlinx.android.synthetic.main.activity_write_forum.backButton

class DetailForumActivity : AppCompatActivity(), DetailForumContract.IDetailForumView,
    View.OnClickListener {

    private var presenter: DetailForumPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_forum)

        initialize()
        presenter?.detailForum(intent.getIntExtra("idx", -1))
    }

    private fun initialize() {
        presenter = DetailForumPresenter(this, this)

        backButton.setOnClickListener(this)
        DF_camera.setOnClickListener(this)
        DF_deleteButton.setOnClickListener(this)
        DF_commentSend.setOnClickListener(this)
        DF_modifyButton.setOnClickListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        presenter?.closeCursor()
    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.backButton -> onBackPressed()
            R.id.DF_camera -> presenter?.onCameraClick()
            R.id.DF_commentSend -> onBackPressed()
            R.id.DF_deleteButton -> presenter?.deleteForum(intent.getIntExtra("idx", -1))
            R.id.DF_modifyButton -> presenter?.modifyForum(intent.getIntExtra("idx", -1))
        }
    }

    override fun setDateText(text: String) {
        DF_date.text = text
    }

    override fun setTitleText(text: String) {
        DF_title.text = text
    }

    override fun setContentText(text: String) {
        DF_content.text = text
    }

    override fun setProfileImage(path: String) {
        Picasso.get().load(path).into(DF_profile)
    }

    override fun setNicknameText(text: String) {
        DF_nickname.text = text
    }

    override fun setCommentNumText(text: String) {
        DF_commentNum.text = text
    }

    override fun setRecommendText(text: String) {
        DF_recommendNum.text = text
    }

    override fun setRecommendButtonText(text: String) {
        DF_recommend.text = text
    }

    override fun setImage0(path: String) {
        DF_image0.visibility = View.VISIBLE
        Picasso.get().load(path).into(DF_image0)
    }

    override fun setImage1(path: String) {
        DF_image1.visibility = View.VISIBLE
        Picasso.get().load(path).into(DF_image1)
    }

    override fun setImage2(path: String) {
        DF_image2.visibility = View.VISIBLE
        Picasso.get().load(path).into(DF_image2)
    }

    override fun setImage3(path: String) {
        DF_image3.visibility = View.VISIBLE
        Picasso.get().load(path).into(DF_image3)
    }

    override fun setImage4(path: String) {
        DF_image4.visibility = View.VISIBLE
        Picasso.get().load(path).into(DF_image4)
    }

    override fun progressVisible(boolean: Boolean) {
        if (boolean) {
            DF_progressbar.visibility = View.VISIBLE
        } else {
            DF_progressbar.visibility = View.INVISIBLE
        }
    }

    override fun openGallery() {
        val intent: Intent = Intent(Intent.ACTION_PICK)
        intent.type = android.provider.MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, Constants.OPEN_GALLERY)
    }

    override fun createDialog() {
        val items = arrayOf("이미지")
        val dialog =
            AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert)
        dialog.setTitle("이미지 첨부")
            .setItems(items) { _, which ->
                val selected: String = items[which]
                if (selected == "이미지") {
                    presenter?.checkCameraPermission()
                }
            }
            .create()
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Constants.OPEN_GALLERY -> {
                    presenter?.galleryResult(data)
                }
            }
        } else {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_LONG).show();
        }
    }

    override fun setCameraImage(path: String) {
        Picasso.get().load(path).into(DF_camera)
    }

    override fun setDeleteForumVisible(boolean: Boolean) {
        if (boolean) {
            DF_deleteButton.visibility = View.VISIBLE
        } else {
            DF_deleteButton.visibility = View.GONE
        }
    }

    override fun setModifyForumVisible(boolean: Boolean) {
        if (boolean) {
            DF_modifyButton.visibility = View.VISIBLE
        } else {
            DF_modifyButton.visibility = View.GONE
        }
    }

    override fun deleteDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("알림")
        builder.setMessage("이미지를 지우시겠습니까?")
        builder.setNegativeButton("취소", null)
        builder.setPositiveButton("삭제") { _, _ ->
            presenter?.deleteImage()
        }
        builder.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            100 -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                        toastMessage("권한이 거부되었습니다.")
                    }
                    return
                }
            }
        }
    }

    override fun enterModifyForum(idx: Int) {
        startActivityForResult(Intent(this, WriteForumActivity::class.java).putExtra("idx",idx), Constants.MODIFY_FORUM)
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    override fun finishActivity() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun toastMessage(text: String) = Toast.makeText(this, text, Toast.LENGTH_LONG).show()

}