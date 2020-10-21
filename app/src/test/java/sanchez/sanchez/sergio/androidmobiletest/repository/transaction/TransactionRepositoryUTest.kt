package sanchez.sanchez.sergio.androidmobiletest.repository.transaction

import android.os.Build
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.squareup.moshi.Moshi
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import sanchez.sanchez.sergio.androidmobiletest.persistence.api.exception.RepoNoResultException
import sanchez.sanchez.sergio.androidmobiletest.persistence.api.transaction.ITransactionRepository
import sanchez.sanchez.sergio.androidmobiletest.persistence.api.transaction.TransactionRepositoryImpl
import sanchez.sanchez.sergio.androidmobiletest.persistence.db.repository.transaction.ITransactionDBRepository
import sanchez.sanchez.sergio.androidmobiletest.persistence.db.repository.transaction.TransactionDBRepositoryImpl
import sanchez.sanchez.sergio.androidmobiletest.persistence.db.room.AppRoomDatabase
import sanchez.sanchez.sergio.androidmobiletest.persistence.network.repository.transaction.ITransactionNetworkRepository
import sanchez.sanchez.sergio.androidmobiletest.persistence.network.repository.transaction.TransactionNetworkRepositoryImpl
import sanchez.sanchez.sergio.androidmobiletest.persistence.network.serder.DateJsonAdapter
import sanchez.sanchez.sergio.androidmobiletest.persistence.network.service.ITransactionService
import java.io.File
import java.net.InetAddress

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class TransactionRepositoryUTest {

    private lateinit var mockServer : MockWebServer
    private lateinit var database: AppRoomDatabase
    private lateinit var transactionNetworkRepository: ITransactionNetworkRepository
    private lateinit var transactionDBRepository: ITransactionDBRepository
    private lateinit var transactionRepository: ITransactionRepository

    @Test
    fun test_repository_001_transactions_sorted_by_date_desc_retrieved_successfully(){
        runBlocking {

            mockServer.enqueue(
                MockResponse().apply {
                    status = "HTTP/1.1 200 OK"
                    setBody(getJson("json/network/transaction/test_repository_001_transactions_sorted_by_date_desc_retrieved_successfully.json"))
                })

            val userTransactions = transactionRepository.findTransactionsByUserOrderByDateDesc()

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
    fun test_repository_002_transactions_cannot_be_retrieved_the_first_time_from_the_cache() {
        runBlocking {

            mockServer.enqueue(
                MockResponse().apply {
                    status = "HTTP/1.1 200 OK"
                    setBody(getJson("json/network/transaction/test_repository_002_transactions_cannot_be_retrieved_the_first_time_from_the_cache.json"))
                })

            try {
                transactionRepository.findTransactionsByUserOrderByDateDesc()

            } catch (ex: Exception) {
                // Check No Result Exception
                assertThat(ex).isInstanceOf(RepoNoResultException::class.java)
            }

        }
    }

    @Test
    fun test_repository_003_transactions_can_be_retrieved_the_second_time_from_cache_in_order() {
        runBlocking {

            mockServer.enqueue(
                MockResponse().apply {
                    status = "HTTP/1.1 200 OK"
                    setBody(getJson("json/network/transaction/test_repository_003_transactions_can_be_retrieved_the_second_time_from_cache.json"))
                })

            transactionRepository.findTransactionsByUserOrderByDateDesc()

            mockServer.enqueue(
                MockResponse().apply {
                    status = "HTTP/1.1 500 Internal Server Error"
                })

            val userTransactions = transactionRepository.findTransactionsByUserOrderByDateDesc()

            assertThat(userTransactions).isNotEmpty
            assertThat(userTransactions).hasSize(3)
            assertThat(userTransactions[0].date).isNotNull
            assertThat(userTransactions[1].date).isNotNull
            assertThat(userTransactions[2].date).isNotNull
            assertThat(userTransactions[0].date).isAfter(userTransactions[1].date)
            assertThat(userTransactions[1].date).isAfter(userTransactions[2].date)
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


    /**
     * Setup
     */
    @Before
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

        // Create Spy for network repository
        transactionNetworkRepository = Mockito.spy(
            TransactionNetworkRepositoryImpl(
            transactionService, TransactionNetworkMapper()
        )
        )

        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            AppRoomDatabase::class.java).build()

        // Create Spy for DB repository
        transactionDBRepository = Mockito.spy(TransactionDBRepositoryImpl(
            database.transactionDAO(),
            TransactionDBMapper()
        ))

        transactionRepository = TransactionRepositoryImpl(
            transactionNetworkRepository, transactionDBRepository
        )

    }

    /**
     * Tear Down
     */
    @After
    @Throws
    fun tearDown() {
        database.close()
        mockServer.shutdown()
    }
}