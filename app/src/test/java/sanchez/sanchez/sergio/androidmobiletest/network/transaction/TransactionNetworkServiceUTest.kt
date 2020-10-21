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
import sanchez.sanchez.sergio.androidmobiletest.persistence.network.serder.DateJsonAdapter
import sanchez.sanchez.sergio.androidmobiletest.persistence.network.service.ITransactionService
import java.io.File
import java.net.InetAddress


/**
 * Test for Transaction Network Service
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(MockitoJUnitRunner::class)
class TransactionNetworkServiceUTest {

    @Test
    fun test_network_001_two_transactions_with_valid_date_format(){
        runBlocking {

            mockServer.enqueue(
                MockResponse().apply {
                    status = "HTTP/1.1 200 OK"
                    setBody(getJson("json/network/transaction/test_network_001_two_transactions_with_valid_date_format.json"))
                })

            val userTransactions = transactionService.getUserTransactions()

            assertThat(userTransactions).isNotEmpty
            assertThat(userTransactions).hasSize(2)
            assertThat(userTransactions[0].date).isNotNull
            assertThat(userTransactions[1].date).isNotNull
        }
    }

    @Test
    fun test_network_002_amount_can_be_negative_or_positive(){
        runBlocking {

            mockServer.enqueue(
                MockResponse().apply {
                    status = "HTTP/1.1 200 OK"
                    setBody(getJson("json/network/transaction/test_network_002_amount_can_be_negative_or_positive.json"))
                })

            val userTransactions = transactionService.getUserTransactions()

            assertThat(userTransactions).isNotEmpty
            assertThat(userTransactions).hasSize(2)
            assertThat(userTransactions[0].amount).isLessThan(0.0f)
            assertThat(userTransactions[1].amount).isGreaterThan(0.0f)
        }
    }

    @Test
    fun test_network_003_transactions_may_or_may_not_have_a_description(){
        runBlocking {

            mockServer.enqueue(
                MockResponse().apply {
                    status = "HTTP/1.1 200 OK"
                    setBody(getJson("json/network/transaction/test_network_003_transactions_may_or_may_not_have_a_description.json"))
                })

            val userTransactions = transactionService.getUserTransactions()

            assertThat(userTransactions).isNotEmpty
            assertThat(userTransactions).hasSize(3)
            assertThat(userTransactions[0].description).isNotEmpty
            assertThat(userTransactions[1].description).isEmpty()
            assertThat(userTransactions[2].description).isNull()
        }
    }


    @Test
    fun test_network_004_transaction_fee_can_be_null_or_a_negative_value(){
        runBlocking {

            mockServer.enqueue(
                MockResponse().apply {
                    status = "HTTP/1.1 200 OK"
                    setBody(getJson("json/network/transaction/test_network_004_transaction_fee_can_be_null_or_a_negative_value.json"))
                })

            val userTransactions = transactionService.getUserTransactions()

            assertThat(userTransactions).isNotEmpty
            assertThat(userTransactions).hasSize(2)
            assertThat(userTransactions[0].fee).isNotNull
            assertThat(userTransactions[0].fee).isLessThan(0.0f)
            assertThat(userTransactions[1].fee).isNull()
        }
    }

    @Test
    fun test_network_005_transactions_can_have_repeated_ids(){
        runBlocking {

            mockServer.enqueue(
                MockResponse().apply {
                    status = "HTTP/1.1 200 OK"
                    setBody(getJson("json/network/transaction/test_network_005_transactions_can_have_repeated_ids.json"))
                })

            val userTransactions = transactionService.getUserTransactions()

            assertThat(userTransactions).isNotEmpty
            assertThat(userTransactions).hasSize(3)
            assertThat(userTransactions[0].id).isNotNull
            assertThat(userTransactions[1].id).isNotNull
            assertThat(userTransactions[2].id).isNotNull
            assertThat(userTransactions[0].id).isEqualTo(userTransactions[1].id)
            assertThat(userTransactions[2].id).isNotEqualTo(userTransactions[0].id)
            assertThat(userTransactions[2].id).isNotEqualTo(userTransactions[1].id)
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
        lateinit var transactionService : ITransactionService


        @JvmStatic
        @BeforeClass
        @Throws
        fun setup() {
            // Initialize mock webserver
            mockServer = MockWebServer().also {
                // Start the local server
                it.start(InetAddress.getByName("127.0.0.1"), 63637)
            }

            transactionService = Retrofit.Builder()
                .addConverterFactory(
                    MoshiConverterFactory.create(
                        Moshi.Builder()
                    .add(DateJsonAdapter())
                    .build()))
                .baseUrl(mockServer.url("").toString())
                .client(OkHttpClient.Builder().build())
                .build()
                .create(ITransactionService::class.java)

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