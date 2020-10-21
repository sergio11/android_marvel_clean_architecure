package sanchez.sanchez.sergio.androidmobiletest.persistence.network.utils.impl

import sanchez.sanchez.sergio.androidmobiletest.persistence.network.utils.IAuthHashStrategy
import sanchez.sanchez.sergio.androidmobiletest.utils.IApplicationAware
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * Auth Hash Strategy Impl
 * @param applicationAware
 */
class AuthHashStrategyImpl(private val applicationAware: IApplicationAware): IAuthHashStrategy {

    /**
     * Generate Hash
     * @param timestamp
     */
    override fun generateHash(timestamp: String): String =
        try {
            val value = timestamp + applicationAware.getApiPrivateKey() + applicationAware.getApiPublicKey()
            val digest = MessageDigest.getInstance("MD5").apply {
                reset()
                update(value.toByteArray())
            }
            val a = digest.digest()
            val len = a.size
            val sb = StringBuilder(len shl 1)
            for (anA in a) {
                sb.append(Character.forDigit(anA.toInt() and 0xf0 shr 4, 16))
                sb.append(Character.forDigit(anA.toInt() and 0x0f, 16))
            }
           sb.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            ""
        }

}