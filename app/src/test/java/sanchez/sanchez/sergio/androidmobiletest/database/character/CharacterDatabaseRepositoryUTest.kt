package sanchez.sanchez.sergio.androidmobiletest.database.character

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
import sanchez.sanchez.sergio.androidmobiletest.domain.models.Character
import sanchez.sanchez.sergio.androidmobiletest.persistence.db.repository.character.CharacterDBRepositoryImpl
import sanchez.sanchez.sergio.androidmobiletest.persistence.db.repository.character.ICharacterDBRepository
import sanchez.sanchez.sergio.androidmobiletest.persistence.db.repository.exception.DBNoResultException
import sanchez.sanchez.sergio.androidmobiletest.persistence.db.room.AppRoomDatabase
import sanchez.sanchez.sergio.androidmobiletest.persistence.db.room.mapper.CharacterDBMapper
import java.util.*

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class CharacterDatabaseRepositoryUTest {

    private lateinit var database: AppRoomDatabase
    private lateinit var characterDBRepository: ICharacterDBRepository

    @Test
    fun test_database_repository_001_no_cached_characters() {
        runBlocking {
            try {
                withContext(IO) {
                    characterDBRepository.findCharactersOrderByNameAsc()
                }
            } catch (ex: Exception) {
                // Check No Result Exception
                assertThat(ex.cause).isInstanceOf(DBNoResultException::class.java)
                assertThat(ex).isInstanceOf(DBNoResultException::class.java)
            }
        }
    }

    @Test
    fun test_database_repository_002_characters_are_cached_successfully() {

        runBlocking {

            val characterList = listOf(
                Character(
                    id = 1011334,
                    name = "3-D Man",
                    modified = Date(),
                    thumbnail = "http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784.jpg",
                    comics = 2,
                    series = 3,
                    events = 5
                ),
                Character(
                    id = 1017100,
                    name = "A-Bomb (HAS)",
                    modified = Date(),
                    thumbnail = "http://i.annihil.us/u/prod/marvel/i/mg/3/20/5232158de5b16.jpg",
                    comics = 2,
                    series = 3,
                    events = 5
                )
            )

            val charactersInDB = withContext(IO) {
                characterDBRepository.saveCharacters(characterList)
                characterDBRepository.findCharactersOrderByNameAsc()
            }

            assertThat(charactersInDB).isNotEmpty
            assertThat(charactersInDB).hasSize(2)

            assertThat(charactersInDB[0].id).isEqualTo(characterList[0].id)
            assertThat(charactersInDB[0].name).isEqualTo(characterList[0].name)
            assertThat(charactersInDB[0].modified).isEqualTo(characterList[0].modified)
            assertThat(charactersInDB[0].thumbnail).isEqualTo(characterList[0].thumbnail)
            assertThat(charactersInDB[0].comics).isEqualTo(characterList[0].comics)
            assertThat(charactersInDB[0].series).isEqualTo(characterList[0].series)
            assertThat(charactersInDB[0].events).isEqualTo(characterList[0].events)

            assertThat(charactersInDB[1].id).isEqualTo(characterList[1].id)
            assertThat(charactersInDB[1].name).isEqualTo(characterList[1].name)
            assertThat(charactersInDB[1].modified).isEqualTo(characterList[1].modified)
            assertThat(charactersInDB[1].thumbnail).isEqualTo(characterList[1].thumbnail)
            assertThat(charactersInDB[1].comics).isEqualTo(characterList[1].comics)
            assertThat(charactersInDB[1].series).isEqualTo(characterList[1].series)
            assertThat(charactersInDB[1].events).isEqualTo(characterList[1].events)
        }

    }

    @Test
    fun test_database_repository_003_cached_characters_can_be_deleted_successfully() {
        runBlocking {

            val characterList = listOf(
                Character(
                    id = 1011334,
                    name = "3-D Man",
                    modified = Date(),
                    thumbnail = "http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784.jpg",
                    comics = 2,
                    series = 3,
                    events = 5
                ),
                Character(
                    id = 1017100,
                    name = "A-Bomb (HAS)",
                    modified = Date(),
                    thumbnail = "http://i.annihil.us/u/prod/marvel/i/mg/3/20/5232158de5b16.jpg",
                    comics = 2,
                    series = 3,
                    events = 5
                )
            )

            try {
                withContext(IO) {
                    characterDBRepository.saveCharacters(characterList)
                    characterDBRepository.deleteAll()
                    characterDBRepository.findCharactersOrderByNameAsc()
                }
            } catch (ex: Exception) {
                // Check No Result Exception
                assertThat(ex).isInstanceOf(DBNoResultException::class.java)
            }

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

        characterDBRepository = CharacterDBRepositoryImpl(
            database.characterDAO(),
            CharacterDBMapper()
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