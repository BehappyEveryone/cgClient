package com.example.chatground2.view.detailForum

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.chatground2.Api.IpAddress
import com.example.chatground2.Model.Constants
import com.example.chatground2.Model.DAO.Model
import com.example.chatground2.Model.DTO.ForumDto
import com.example.chatground2.Model.DTO.UserDto
import com.example.chatground2.adapter.adapterContract.CommentsAdapterContract
import com.google.gson.Gson
import java.text.DateFormat

class DetailForumPresenter(
    private val context: Context,
    val view: DetailForumContract.IDetailForumView
) : DetailForumContract.IDetailForumPresenter, DetailForumContract.Listener {

    private var model: Model = Model(context)

    private var imagePath: String? = null

    private val sp: SharedPreferences =
        context.getSharedPreferences(Constants.SHARED_PREFERENCE, Context.MODE_PRIVATE)
    private val gson = Gson()

    private var c: Cursor? = null

    override var adapterModel: CommentsAdapterContract.Model? = null
    override var adapterView: CommentsAdapterContract.View? = null
        set(value) {//커스텀 접근자
            field = value
            field?.onReplyCameraClick = { onPathCheck(it) }
            field?.onReplySendClick = { position, content ->
                onReplySendClick(position, content)
            }
        }

    override fun onReplySendClick(position: Int, content: String) {

    }

    override fun onCameraClick() {
        onPathCheck(imagePath)
    }

    override fun closeCursor() {
        if (c != null) {
            c?.close()
        }
    }

    override fun onPathCheck(imagePath: String?) {
        if (imagePath.isNullOrEmpty())//비었으면
        {
            view.createDialog()
        } else {
            view.deleteDialog()
        }
    }

    override fun deleteImage() {
        imagePath = null
        adapterModel?.setReplyImagePathString(null)
    }

    override fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    context as Activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {//이전에 이미 권한이 거부되었을 때 설명
                view.toastMessage("권한을 허가해주십시오.")
                setupPermissions()
            } else {//최초로 권한 요청
                makeRequest()
            }
        } else {
            view.openGallery()
        }
    }

    override fun galleryResult(data: Intent?) {
        val currentImageUrl: Uri? = data?.data
        val path = getPath(currentImageUrl!!)
        if (!path.isNullOrEmpty()) {
            imagePath = path
            view.setCameraImage(path)
            adapterModel?.setReplyImagePathString(path)
        }
    }

    private fun getPath(uri: Uri): String? {
        val pro: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        c = context.contentResolver.query(uri, pro, null, null, null)
        val index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c?.moveToFirst()
        return index?.let { c?.getString(it) }
    }

    private fun setupPermissions() {
        //스토리지 읽기 퍼미션을 permission 변수에 담는다
        val permission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            100
        )
    }

    override fun detailForum(idx: Int) {
        view.progressVisible(true)

        val hashMap = HashMap<String, Any>()
        hashMap["idx"] = idx
        model.detailForum(hashMap, this)
    }

    override fun deleteForum(idx: Int) {
        view.progressVisible(true)

        val hashMap = HashMap<String, Any>()
        hashMap["idx"] = idx
        hashMap["userId"] = getUser()._id
        model.deleteForum(hashMap, this)
    }

    override fun modifyForum(idx: Int) {
        view.enterModifyForum(idx)
    }

    override fun onDetailForumSuccess(forumDto: ForumDto?) {
        forumDto?.let {
            if (it.user._id == getUser()._id) {
                view.setDeleteForumVisible(true)
                view.setModifyForumVisible(true)
            }
            view.setTitleText(it.title)
            view.setContentText(it.content)
            view.setDateText(DateFormat.getDateInstance(DateFormat.LONG).format(it.birth))
            view.setCommentNumText("댓글 : ${it.comments?.size.toString()} 개")
            view.setRecommendText("추천 : ${it.recommend} 개")
            view.setRecommendButtonText(it.recommend.toString())
            it.user.profile?.let { it1 -> view.setProfileImage(it1) }
            view.setNicknameText(it.user.nickname)

            it.imageUrl?.let { array ->
                for (i in array.indices) {
                    when (i) {
                        0 -> view.setImage0(IpAddress.BaseURL + array[i])
                        1 -> view.setImage1(IpAddress.BaseURL + array[i])
                        2 -> view.setImage2(IpAddress.BaseURL + array[i])
                        3 -> view.setImage3(IpAddress.BaseURL + array[i])
                        4 -> view.setImage4(IpAddress.BaseURL + array[i])
                    }
                }
            }
        }
        view.progressVisible(false)
    }

    override fun onDetailForumFailure() {
        view.progressVisible(false)
        view.toastMessage("해당 글을 찾을 수 없습니다.")
        view.finishActivity()
    }

    override fun onDeleteForumSuccess() {
        view.progressVisible(false)
        view.toastMessage("게시글 삭제 성공")
        view.finishActivity()
    }

    override fun onDeleteForumFailure() {
        view.progressVisible(false)
        view.toastMessage("게시글 삭제 실패")
    }

    override fun onFailure() {
        view.progressVisible(false)
        view.toastMessage("통신 에러")
    }

    private fun getUser(): UserDto {
        val json = sp.getString("User", "")
        return gson.fromJson(json, UserDto::class.java)
    }
}