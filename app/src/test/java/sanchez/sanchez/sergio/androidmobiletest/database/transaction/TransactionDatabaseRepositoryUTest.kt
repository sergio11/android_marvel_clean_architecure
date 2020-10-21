package sanchez.sanchez.sergio.androidmobiletest.database.transaction

import android.os.Build
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import sanchez.sanchez.sergio.androidmobiletest.persistence.db.repository.exception.DBNoResultException
import sanchez.sanchez.sergio.androidmobiletest.persistence.db.repository.transaction.ITransactionDBRepository
import sanchez.sanchez.sergio.androidmobiletest.persistence.db.repository.transaction.TransactionDBRepositoryImpl
import sanchez.sanchez.sergio.androidmobiletest.persistence.db.room.AppRoomDatabase
import java.util.*

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class TransactionDatabaseRepositoryUTest {

    private lateinit var database: AppRoomDatabase
    private lateinit var transactionDBRepository: ITransactionDBRepository

    @Test
    fun test_database_repository_001_no_cached_user_transactions() {
        runBlocking {
            try {
                withContext(IO) {
                    transactionDBRepository.findCurrentAuthUserOrderByDateDesc()
                }
            } catch (ex: Exception) {
                // Check No Result Exception
                assertThat(ex.cause).isInstanceOf(DBNoResultException::class.java)
                assertThat(ex).isInstanceOf(DBNoResultException::class.java)
            }
        }
    }

    @Test
    fun test_database_repository_002_transactions_are_cached_successfully() {

        runBlocking {

            val userTransactions = listOf(
                Transaction(id = 4734, date = Date(), amount = -193.38f, total = -193.38f),
                Transaction(id = 4735, date = Date(), amount = -50.00f, fee = -3.45f, total = -53.45f)
            )

            val userTransactionsInDB = withContext(IO) {
                transactionDBRepository.saveCurrentAuthUserTransactions(userTransactions)
                transactionDBRepository.findCurrentAuthUserOrderByDateDesc()
            }

            assertThat(userTransactionsInDB).isNotEmpty
            assertThat(userTransactionsInDB).hasSize(2)

            assertThat(userTransactionsInDB[0].id).isEqualTo(userTransactions[0].id)
            assertThat(userTransactionsInDB[0].date).isEqualTo(userTransactions[0].date)
            assertThat(userTransactionsInDB[0].amount).isEqualTo(userTransactions[0].amount)
            assertThat(userTransactionsInDB[0].description).isEqualTo(userTransactions[0].description)
            assertThat(userTransactionsInDB[0].fee).isEqualTo(userTransactions[0].fee)
            assertThat(userTransactionsInDB[0].total).isEqualTo(userTransactions[0].total)

            assertThat(userTransactionsInDB[1].id).isEqualTo(userTransactions[1].id)
            assertThat(userTransactionsInDB[1].date).isEqualTo(userTransactions[1].date)
            assertThat(userTransactionsInDB[1].amount).isEqualTo(userTransactions[1].amount)
            assertThat(userTransactionsInDB[1].description).isEqualTo(userTransactions[1].description)
            assertThat(userTransactionsInDB[1].fee).isEqualTo(userTransactions[1].fee)
            assertThat(userTransactionsInDB[1].total).isEqualTo(userTransactions[1].total)
        }

    }


    /**
     * Setup
     */
    @Before
    @Throws
    fun setup() {

        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            AppRoomDatabase::class.java).build()

        transactionDBRepository = TransactionDBRepositoryImpl(
            database.transactionDAO(),
            TransactionDBMapper()
        )

    }

    /**
     * Tear Down
     */
    @After
    @Throws
    fun tearDown() {
        database.close()
    }
}