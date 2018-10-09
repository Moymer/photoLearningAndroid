package moymer.com.photolearning

import android.content.Context
import com.karumi.dexter.PermissionToken
import com.moymer.spoken.di.qualifiers.utils.BasePresenter
import com.moymer.spoken.di.qualifiers.utils.BaseView
import java.io.File

interface PLMainContract {

    interface View: BaseView<Presenter> {
        fun launchCamera()
        fun permissionDialog(token: PermissionToken?)
        fun permissionDenied()
    }

    interface Presenter: BasePresenter<View> {
        fun validatePermission(plMainActivity: PLMainActivity)
        fun processCapturedPhoto(context: Context?, currentPhotoPath: String) : String?
        fun setFilePath(path: String?)

    }
}