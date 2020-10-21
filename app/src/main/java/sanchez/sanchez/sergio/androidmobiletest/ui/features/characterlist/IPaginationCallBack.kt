package sanchez.sanchez.sergio.androidmobiletest.ui.features.characterlist

interface IPaginationCallBack {

    /**
     * on Load Next Page
     */
    fun onLoadNextPage()

    /**
     * Is Pagination Enabled
     */
    fun isPaginationEnabled(): Boolean
}