package sanchez.sanchez.sergio.androidmobiletest.network.transaction

import com.squareup.moshi.Moshi
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import sanchez.sanchez.sergio.androidmobiletest.persistence.network.exception.NetworkErrorException
import sanchez.sanchez.sergio.androidmobiletest.persistence.network.exception.NetworkForbiddenException
import sanchez.sanchez.sergio.androidmobiletest.persistence.network.exception.NetworkNoResultException
import sanchez.sanchez.sergio.androidmobiletest.persistence.network.repository.transaction.ITransactionNetworkRepository
import sanchez.sanchez.sergio.androidmobiletest.persistence.network.repository.transaction.TransactionNetworkRepositoryImpl
import sanchez.sanchez.sergio.androidmobiletest.persistence.network.serder.DateJsonAdapter
import sanchez.sanchez.sergio.androidmobiletest.persistence.network.service.ITransactionService
import java.io.File
import java.net.InetAddress


/**
 * Test for Transaction Network Repository
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(MockitoJUnitRunner::class)
class TransactionNetworkRepositoryUTest {

    @Test
    fun test_network_repository_001_discard_transactions_with_invalid_date(){
        runBlocking {

            mockServer.enqueue(
                MockResponse().apply {
                    status = "HTTP/1.1 200 OK"
                    setBody(getJson("json/network/transaction/test_network_repository_001_discard_transactions_with_invalid_date.json"))
                })


            val userTransactions = transactionNetworkRepository.findTransactionsByUserOrderByDateDesc()

            assertThat(userTransactions).isNotEmpty
            assertThat(userTransactions).hasSize(2)
            assertThat(userTransactions[0].date).isNotNull
            assertThat(userTransactions[1].date).isNotNull

        }
    }

    @Test
    fun test_network_repository_002_discard_transactions_with_duplicate_id(){
        runBlocking {

            mockServer.enqueue(
                MockResponse().apply {
                    status = "HTTP/1.1 200 OK"
                    setBody(getJson("json/network/transaction/test_network_repository_002_discard_transactions_with_duplicate_id.json"))
                })

            val userTransactions = transactionNetworkRepository.findTransactionsByUserOrderByDateDesc()

            assertThat(userTransactions).isNotEmpty
            assertThat(userTransactions).hasSize(3)
            assertThat(userTransactions[0].id).isNotNull
            assertThat(userTransactions[1].id).isNotNull
            assertThat(userTransactions[2].id).isNotNull
            assertThat(userTransactions[0].id).isNotEqualTo(userTransactions[1].id)
            assertThat(userTransactions[0].id).isNotEqualTo(userTransactions[2].id)
            assertThat(userTransactions[1].id).isNotEqualTo(userTransactions[0].id)
            assertThat(userTransactions[1].id).isNotEqualTo(userTransactions[2].id)
            assertThat(userTransactions[2].id).isNotEqualTo(userTransactions[0].id)
            assertThat(userTransactions[2].id).isNotEqualTo(userTransactions[1].id)

        }
    }

    @Test
    fun test_network_repository_003_transactions_are_sorted_by_date_in_descending_order(){
        runBlocking {

            mockServer.enqueue(
                MockResponse().apply {
                    status = "HTTP/1.1 200 OK"
                    setBody(getJson("json/network/transaction/test_network_repository_003_transactions_are_sorted_by_date_in_descending_order.json"))
                })

            val userTransactions = transactionNetworkRepository.findTransactionsByUserOrderByDateDesc()

            assertThat(userTransactions).isNotEmpty
            assertThat(userTransactions).hasSize(3)
            assertThat(userTransactions[0].date).isNotNull
            assertThat(userTransactions[1].date).isNotNull
            assertThat(userTransactions[2].date).isNotNull
            assertThat(userTransactions[0].date).isAfter(userTransactions[1].date)
            assertThat(userTransactions[1].date).isAfter(userTransactions[2].date)

        }
    }

    @Test
    fun test_network_repository_004_transactions_will_have_a_total_field_with_the_sum_of_the_amount_with_the_fee(){
        runBlocking {

            mockServer.enqueue(
                MockResponse().apply {
                    status = "HTTP/1.1 200 OK"
                    setBody(getJson("json/network/transaction/test_network_repository_004_transactions_will_have_a_total_field_with_the_sum_of_the_amount_with_the_fee.json"))
                })

            val userTransactions = transactionNetworkRepository.findTransactionsByUserOrderByDateDesc()

            assertThat(userTransactions).isNotEmpty
            assertThat(userTransactions).hasSize(3)
            // First Transaction only has a negative amount
            assertThat(userTransactions[0].amount).isNotNull
            assertThat(userTransactions[0].amount).isLessThan(0.0f)
            assertThat(userTransactions[0].fee).isNull()
            assertThat(userTransactions[0].total).isEqualTo(userTransactions[0].amount)
            // Second Transaction only has a positive amount
            assertThat(userTransactions[1].amount).isNotNull
            assertThat(userTransactions[1].amount).isGreaterThan(0.0f)
            assertThat(userTransactions[1].fee).isNull()
            assertThat(userTransactions[1].total).isEqualTo(userTransactions[1].amount)
            // Last Transaction has amount and fee
            assertThat(userTransactions[2].amount).isNotNull
            assertThat(userTransactions[2].amount).isLessThan(0.0f)
            assertThat(userTransactions[2].fee).isNotNull
            assertThat(userTransactions[2].fee).isLessThan(0.0f)
            assertThat(userTransactions[2].total).isEqualTo(userTransactions[2].amount + userTransactions[2].fee!!)

        }
    }

    @Test
    fun test_network_repository_005_no_transactions_found(){
        runBlocking {

            mockServer.enqueue(
                MockResponse().apply {
                    status = "HTTP/1.1 200 OK"
                    setBody(getJson("json/network/transaction/test_network_repository_005_no_transactions_found.json"))
                })

            try {
                transactionNetworkRepository.findTransactionsByUserOrderByDateDesc()

            } catch (ex: NetworkException) {

                // Check No Result Exception
                assertThat(ex).isInstanceOf(NetworkNoResultException::class.java)

            }
        }
    }

    @Test
    fun test_network_repository_006_internal_server_error(){
        runBlocking {

            mockServer.enqueue(
                MockResponse().apply {
                    status = "HTTP/1.1 500 Internal Server Error"
                })

            try {
                transactionNetworkRepository.findTransactionsByUserOrderByDateDesc()

            } catch (ex: NetworkException) {

                // Check Error Exception
                assertThat(ex).isInstanceOf(NetworkErrorException::class.java)

            }
        }
    }

    @Test
    fun test_network_repository_007_forbidden_access(){
        runBlocking {

            mockServer.enqueue(
                MockResponse().apply {
                    status = "HTTP/1.1 403 Forbidden"
                })

            try {
                transactionNetworkRepository.findTransactionsByUserOrderByDateDesc()

            } catch (ex: NetworkException) {

                // Check Forbidden Exception
                assertThat(ex).isInstanceOf(NetworkForbiddenException::class.java)

            }
        }
    }


    /**
     * Private Methods
     */

    /**
     * Helper function which will loadByDate JSON from
     * the path specified
     *
     * @param path : Path of JSON file
     * @return json : JSON from file at given path
     */
    private fun getJson(path: String) : String {
        // Load the JSON response
        val uri = this.javaClass.classLoader!!.getResource(path)
        val file = File(uri.path)
        return String(file.readBytes())
    }


    companion object {

        @JvmStatic
        lateinit var mockServer : MockWebServer

        @JvmStatic
        lateinit var transactionNetworkRepository : ITransactionNetworkRepository


        @JvmStatic
        @BeforeClass
        @Throws
        fun setup() {
            // Initialize mock webserver
            mockServer = MockWebServer().also {
                // Start the local server
                it.start(InetAddress.getByName("127.0.0.1"), 63637)
            }

            val transactionService = Retrofit.Builder()
                .addConverterFactory(
                    MoshiConverterFactory.create(
                        Moshi.Builder()
                    .add(DateJsonAdapter())
                    .build()))
                .baseUrl(mockServer.url("").toString())
                .client(OkHttpClient.Builder().build())
                .build()
                .create(ITransactionService::class.java)


            transactionNetworkRepository = TransactionNetworkRepositoryImpl(
                transactionService, TransactionNetworkMapper()
            )

        }

        @JvmStatic
        @AfterClass
        @Throws
        fun tearDown() {
            // We're done with tests, shut it down
            mockServer.shutdown()
        }
    }
}