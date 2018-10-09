package moymer.com.photolearning

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.karumi.dexter.PermissionToken
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_plmain.*
import java.io.File


class PLMainFragment : PLMainContract.View, Fragment() {

    companion object {
        private const val TAKE_PHOTO_REQUEST: Int = 828
    }

    private val mPresenter by lazy {
        PLMainPresenter()
    }

    private var mCurrentPhotoPath = ""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        mPresenter.takeView(this)

        arguments?.let { bundle ->
            takeIf { bundle.containsKey("path") }?.apply {
                mPresenter.setFilePath(bundle.getString("path"))
            }
        }

        return inflater.inflate(R.layout.fragment_plmain, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fab_capture.setOnClickListener {
            activity?.let { act ->
                if (act is PLMainActivity) {
                    mPresenter.validatePermission(act)
                }
            }
        }
    }

    override fun onDestroy() {
        mPresenter.dropView()
        super.onDestroy()
    }

    override fun launchCamera() {
        context?.let {
            val values = ContentValues(1)
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
            val fileUri = it.contentResolver
                    .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            values)
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (intent.resolveActivity(it.packageManager) != null) {
                mCurrentPhotoPath = fileUri.toString()
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                        or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                startActivityForResult(intent, TAKE_PHOTO_REQUEST)
            }
        }

    }

    override fun permissionDialog(token: PermissionToken?) {
        context?.let { it ->
            AlertDialog.Builder(it)
                    .setMessage(
                            "permissao")
                    .setNegativeButton(
                            android.R.string.cancel
                    ) { dialog, _ ->
                        dialog.dismiss()
                        token?.cancelPermissionRequest()
                    }
                    .setPositiveButton(android.R.string.ok
                    ) { dialog, _ ->
                        dialog.dismiss()
                        token?.continuePermissionRequest()
                    }
                    .setOnDismissListener {
                        token?.cancelPermissionRequest()
                    }
                    .show()
        }
    }

    override fun permissionDenied() {
        Snackbar.make(main_container,
                "permissao negada",
                Snackbar.LENGTH_LONG)
                .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int,
                                  data: Intent?) {
        if (resultCode == Activity.RESULT_OK
                && requestCode == TAKE_PHOTO_REQUEST) {

            val processCapturedPhoto = mPresenter.processCapturedPhoto(context, mCurrentPhotoPath)
            processCapturedPhoto?.let { processPhotoPath ->
                takeIf { processPhotoPath.isNotEmpty() }?.apply {


                    Picasso.get().load(File(processPhotoPath)).into(imgv_photo)
                }
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
