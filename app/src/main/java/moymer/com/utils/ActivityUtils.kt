package moymer.com.utils

class ActivityUtils {

    companion object {
        /**
         * The `fragment` is added to the container view with id `frameId`. The operation is
         * performed by the `fragmentManager`.
         *
         */
        fun addFragmentToActivity(fragmentManager: android.support.v4.app.FragmentManager,
                                  fragment: android.support.v4.app.Fragment, frameId: Int) {
            checkNotNull(fragmentManager)
            checkNotNull(fragment)
            val transaction = fragmentManager.beginTransaction()
            transaction.add(frameId, fragment)
            transaction.commit()
        }


        fun addFragmentToActivity(fragmentManager: android.app.FragmentManager,
                                  fragment: android.app.Fragment, frameId: Int) {
            checkNotNull(fragmentManager)
            checkNotNull(fragment)
            val transaction = fragmentManager.beginTransaction()
            transaction.add(frameId, fragment)
            transaction.commit()
        }
    }
}