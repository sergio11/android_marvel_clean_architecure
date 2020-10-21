package sanchez.sanchez.sergio.androidmobiletest.utils

interface IApplicationAware {

    /**
     * Get API Public Key
     */
    fun getApiPublicKey(): String

    /**
     * Get API Private Key
     */
    fun getApiPrivateKey(): String

}