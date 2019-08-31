package hu.blzsaa.uxstudioteam.service

import hu.blzsaa.uxstudioteam.data.Contact
import hu.blzsaa.uxstudioteam.data.ContactCreationDto
import hu.blzsaa.uxstudioteam.data.ContactDto
import hu.blzsaa.uxstudioteam.helper.UuidGenerator
import hu.blzsaa.uxstudioteam.repository.ContactRepository
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

internal class ContactCrudServiceTest {
    private val contactRepository = mockk<ContactRepository>(relaxed = true)
    private val uuidGenerator = mockk<UuidGenerator>(relaxed = true)
    private val underTest = ContactCrudService(contactRepository, uuidGenerator)

    private val testUuid = UUID.fromString("bb0902c4-dbd6-4702-a638-2e39d3fb5887")
    private val contactCreationDto = ContactCreationDto("name1", "phoneNumber1", "emailAddress1")
    private val contactDto1 = ContactDto(testUuid, "name1", "phoneNumber1", "emailAddress1")
    private val contactDto2 = ContactDto(testUuid, "name2", "phoneNumber2", "emailAddress2")
    private val contact1 = Contact(testUuid, "name1", "phoneNumber1", "emailAddress1")
    private val contact2 = Contact(testUuid, "name2", "phoneNumber2", "emailAddress2")

    @BeforeEach
    internal fun setUp() {
        clearMocks(contactRepository, uuidGenerator)
        every { uuidGenerator.randomUUID() } returns testUuid
    }

    @Test
    fun `createContact should create contact then save it`() {
        // given
        every { contactRepository.save(contact1) } returns contact2

        // when
        val actual = underTest.createContact(contactCreationDto)

        // then
        assertThat(actual).isEqualTo(contactDto2)
    }

    @Test
    fun `listContacts should transform all contacts to dto`() {
        // given
        every { contactRepository.findAll() } returns listOf(contact1, contact2)

        // when
val actual = underTest.listContacts()

        // then
        assertThat(actual).isEqualTo(listOf(contactDto1, contactDto2))
    }

    @Test
    fun `deleteContact should delete by id`() {
        // when
        underTest.deleteContact(testUuid)

        // then
        verify { contactRepository.deleteById(testUuid) }
    }

    @Test
    fun `editContact should update existing contact then transform updated values to dto`() {
        // given
        every { contactRepository.save(contact1) } returns contact2

        // when
        val actual = underTest.editContact(testUuid, contactCreationDto)

        // then
        assertThat(actual).isEqualTo(contactDto2)
    }
}
