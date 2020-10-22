package sanchez.sanchez.sergio.androidmobiletest.network.character

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
import sanchez.sanchez.sergio.androidmobiletest.persistence.network.service.ICharactersService
import java.io.File
import java.net.InetAddress


/**
 * Test for Character Network Service
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(MockitoJUnitRunner::class)
class CharacterNetworkServiceUTest {

    @Test
    fun test_network_001_get_two_characters_from_start(){
        runBlocking {

            mockServer.enqueue(
                MockResponse().apply {
                    status = "HTTP/1.1 200 OK"
                    setBody(getJson("json/network/character/test_network_001_get_two_character_from_start.json"))
                })

            val offset = 0
            val limit = 2

            val characters = charactersService.getCharacterList(offset, limit)

            assertThat(characters.code).isEqualTo("200")
            assertThat(characters.status).isEqualTo("Ok")
            assertThat(characters.data.offset).isEqualTo(offset)
            assertThat(characters.data.count).isEqualTo(limit)
            assertThat(characters.data.limit).isEqualTo(limit)
            assertThat(characters.data.total).isGreaterThan(limit)
            assertThat(characters.data.results).isNotEmpty
            assertThat(characters.data.results).hasSize(limit)
        }
    }

    @Test
    fun test_network_002_get_characters_order_by_name_asc(){
        runBlocking {

            mockServer.enqueue(
                MockResponse().apply {
                    status = "HTTP/1.1 200 OK"
                    setBody(getJson("json/network/character/test_network_002_get_characters_order_by_name_asc.json"))
                })

            val offset = 0
            val limit = 2

            val characters = charactersService.getCharacterList(offset, limit)

            assertThat(characters.code).isEqualTo("200")
            assertThat(characters.status).isEqualTo("Ok")
            assertThat(characters.data.results).isNotEmpty
            assertThat(characters.data.results).hasSize(limit)
            assertThat(characters.data.results[0].name).isLessThan(characters.data.results[1].name)

        }
    }

    @Test
    fun test_network_003_character_has_thumbnail_with_path_and_extension(){
        runBlocking {
            mockServer.enqueue(
                MockResponse().apply {
                    status = "HTTP/1.1 200 OK"
                    setBody(getJson("json/network/character/test_network_003_character_has_thumbnail_with_path_and_extension.json"))
                })

            val offset = 0
            val limit = 1
            val characters = charactersService.getCharacterList(offset, limit)

            assertThat(characters.code).isEqualTo("200")
            assertThat(characters.status).isEqualTo("Ok")
            assertThat(characters.data.results).isNotEmpty
            assertThat(characters.data.results).hasSize(limit)
            assertThat(characters.data.results[0].thumbnail).isNotNull
            assertThat(characters.data.results[0].thumbnail.path).isNotEmpty
            assertThat(characters.data.results[0].thumbnail.extension).isNotEmpty
        }
    }

    @Test
    fun test_network_004_character_has_empty_description(){
        runBlocking {
            mockServer.enqueue(
                MockResponse().apply {
                    status = "HTTP/1.1 200 OK"
                    setBody(getJson("json/network/character/test_network_004_character_has_empty_description.json"))
                })


            val offset = 0
            val limit = 2
            val characters = charactersService.getCharacterList(offset, limit)

            assertThat(characters.code).isEqualTo("200")
            assertThat(characters.status).isEqualTo("Ok")
            assertThat(characters.data.results).isNotEmpty
            assertThat(characters.data.results).hasSize(limit)
            assertThat(characters.data.results[0].description).isEmpty()
            assertThat(characters.data.results[1].description).isNotEmpty
        }
    }

    @Test
    fun test_network_005_modified_date_is_parsed_as_null_when_is_invalid(){
        runBlocking {
            mockServer.enqueue(
                MockResponse().apply {
                    status = "HTTP/1.1 200 OK"
                    setBody(getJson("json/network/character/test_network_005_modified_date_is_parsed_as_null_when_is_invalid.json"))
                })


            val offset = 0
            val limit = 1
            val characters = charactersService.getCharacterList(offset, limit)

            assertThat(characters.code).isEqualTo("200")
            assertThat(characters.status).isEqualTo("Ok")
            assertThat(characters.data.results).isNotEmpty
            assertThat(characters.data.results).hasSize(limit)
            assertThat(characters.data.results[0].modified).isNull()
        }
    }

    @Test
    fun test_network_006_can_get_character_detail(){
        runBlocking {

            mockServer.enqueue(
                MockResponse().apply {
                    status = "HTTP/1.1 200 OK"
                    setBody(getJson("json/network/character/test_network_006_can_get_character_detail.json"))
                })

            val characterId = 1011334L

            val characterDetail = charactersService.getCharacterDetail(characterId)
            assertThat(characterDetail.code).isEqualTo("200")
            assertThat(characterDetail.status).isEqualTo("Ok")
            assertThat(characterDetail.data.count).isEqualTo(1)
            assertThat(characterDetail.data.results).isNotEmpty
            assertThat(characterDetail.data.results).hasSize(1)
            assertThat(characterDetail.data.results[0].id).isEqualTo(characterId)
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
        lateinit var charactersService : ICharactersService


        @JvmStatic
        @BeforeClass
        @Throws
        fun setup() {
            // Initialize mock webserver
            mockServer = MockWebServer().also {
                // Start the local server
                it.start(InetAddress.getByName("127.0.0.1"), 63637)
            }

            charactersService = Retrofit.Builder()
                .addConverterFactory(
                    MoshiConverterFactory.create(Moshi.Builder()
                    .add(DateJsonAdapter())
                    .build()))
                .baseUrl(mockServer.url("").toString())
                .client(OkHttpClient.Builder().build())
                .build()
                .create(ICharactersService::class.java)

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