package sanchez.sanchez.sergio.androidmobiletest.persistence.network.utils

/**
 * Auth Hash Strategy
 */
interface IAuthHashStrategy {

    /**
     * Generate Hash
     * @param timestamp
     */
    fun generateHash(timestamp: String): String
}