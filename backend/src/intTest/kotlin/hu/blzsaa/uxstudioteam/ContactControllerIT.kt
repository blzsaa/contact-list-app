package hu.blzsaa.uxstudioteam

import hu.blzsaa.uxstudioteam.data.Contact
import hu.blzsaa.uxstudioteam.repository.ContactRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import java.util.UUID

private const val API_CONTACTS = "/contacts"
private const val API_CONTACTS_ID = "/contacts/{id}"

private const val CONTACT1_AS_JSON = """
            {
                "name": "n1",
                "emailAddress": "asd@asd.com",
                "phoneNumber": "+36701234567"
            }
        """

private const val CONTACT2_AS_JSON = """
            {
                "name": "n2",
                "emailAddress": "asd2@asd.com",
                "phoneNumber": "+36701234568"
            }
        """

@SpringBootTest
internal class ContactControllerIT {
    @Autowired
    private lateinit var wac: WebApplicationContext
    @Autowired
    private lateinit var repository: ContactRepository

    private lateinit var mockMvc: MockMvc

    @BeforeEach
    internal fun setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build()
    }

    @AfterEach
    internal fun tearDown() {
        repository.deleteAll()
    }

    @Test
    internal fun `should create contact`() {
        addContact(CONTACT1_AS_JSON)
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.name").value("n1"))
                .andExpect(jsonPath("$.emailAddress").value("asd@asd.com"))
                .andExpect(jsonPath("$.phoneNumber").value("+36701234567"))

        assertThat(repository.findAll()).hasSize(1)
        val actual = repository.findAll().first()
        val expected = Contact(id = UUID.randomUUID(), name = "n1", emailAddress = "asd@asd.com", phoneNumber = "+36701234567")
        assertThat(actual)
                .isEqualToIgnoringGivenFields(expected, "id")
    }

    @Test
    internal fun `should not let create contact with invalid email address`() {
        addContact(CONTACT1_AS_JSON.replace("@", ""))
                .andExpect(status().isBadRequest)

        assertThat(repository.findAll()).isEmpty()
    }

    @Test
    internal fun `should list contacts`() {
        // given
        addContact(CONTACT1_AS_JSON)
        addContact(CONTACT2_AS_JSON)

        // when + then
        mockMvc.perform(get(API_CONTACTS))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.[0].name").value("n1"))
                .andExpect(jsonPath("$.[1].name").value("n2"))
    }

    @Test
    internal fun `deleting the second contact should not modify the first one`() {
        // given
        addContact(CONTACT1_AS_JSON)
        addContact(CONTACT2_AS_JSON)
        val id = repository.findAll().findLast { c -> c.name == "n2" }!!.id

        // when
        mockMvc.perform(delete(API_CONTACTS_ID, id))
                .andExpect(status().isOk)

        // then
        assertThat(repository.findAll()).hasSize(1)
        mockMvc.perform(get(API_CONTACTS))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.[0].name").value("n1"))
    }

    @Test
    internal fun `editing should overwrite all fields`() {
        // given
        addContact(CONTACT1_AS_JSON)

        val id = repository.findAll().findLast { c -> c.name == "n1" }!!.id

        // when
        mockMvc.perform(post(API_CONTACTS_ID, id).contentType(APPLICATION_JSON).content(CONTACT2_AS_JSON))
                .andExpect(status().isOk).andExpect(jsonPath("$.name").value("n2"))

        // then
        assertThat(repository.findAll()).hasSize(1)
        assertThat(repository.findAll().first().emailAddress).isEqualTo("asd2@asd.com")
    }

    private fun addContact(contact: String) =
            mockMvc.perform(post(API_CONTACTS).contentType(APPLICATION_JSON).content(contact)
                    .accept(APPLICATION_JSON))
}
