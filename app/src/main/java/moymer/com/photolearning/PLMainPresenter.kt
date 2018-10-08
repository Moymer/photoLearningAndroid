package moymer.com.photolearning

import android.Manifest
import android.content.Context
import android.net.Uri
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import moymer.com.utils.ImageUtils
import java.io.File

class PLMainPresenter : PLMainContract.Presenter {

    private var mMainFragmentView: PLMainContract.View? = null
    private var mFilePath: String? = null

    override fun takeView(view: PLMainContract.View) {
        mMainFragmentView = view
    }

    override fun dropView() {
        mMainFragmentView = null
    }

    override fun validatePermission(plMainActivity: PLMainActivity) {
        Dexter.withActivity(plMainActivity)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(
                            response: PermissionGrantedResponse?) {
                        mMainFragmentView?.launchCamera()
                    }

                    override fun onPermissionRationaleShouldBeShown(
                            permission: PermissionRequest?,
                            token: PermissionToken?) {
                        mMainFragmentView?.permissionDialog(token)

                    }

                    override fun onPermissionDenied(
                            response: PermissionDeniedResponse?) {
                        mMainFragmentView?.permissionDenied()
                    }
                })
                .check()
    }

    override fun processCapturedPhoto(context: Context?, currentPhotoPath: String) : String? {
        context?.let {
            val cursor = it.contentResolver.query(Uri.parse(currentPhotoPath),
                    Array(1) { android.provider.MediaStore.Images.ImageColumns.DATA },
                    null, null, null)
            cursor.moveToFirst()
            val photoPath = cursor.getString(0)
            cursor.close()
            val file = File(photoPath)
            val uri = Uri.fromFile(file)

            return ImageUtils.resizeAndCropUserImage(uri, it, photoPath, mFilePath)
        } ?: return ""
    }

    override fun setFilePath(path: String?) {
        mFilePath = path
    }
}