package hu.blzsaa.contactlistapp.web

import hu.blzsaa.contactlistapp.data.ContactCreationDto
import hu.blzsaa.contactlistapp.data.ContactDto
import hu.blzsaa.contactlistapp.service.ContactCrudService
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

internal class ContactControllerTest {
    private val service = mockk<ContactCrudService>(relaxed = true)
    private val underTest = ContactController(service)

    private val testUuid = UUID.fromString("bb0902c4-dbd6-4702-a638-2e39d3fb5887")
    private val contactCreationDto = ContactCreationDto("name1", "phoneNumber1", "emailAddress1")
    private val contactDto1 = ContactDto(testUuid, "name1", "phoneNumber1", "emailAddress1")
    private val contactDto2 = ContactDto(testUuid, "name2", "phoneNumber2", "emailAddress2")

    @BeforeEach
    internal fun setUp() {
        clearMocks(service)
    }

    @Test
    internal fun `createContact should call service`() {
        // given
        every { service.createContact(contactCreationDto) } returns contactDto1

        // when
        val actual = underTest.createContact(contactCreationDto)

        // then
        assertThat(actual).isEqualTo(contactDto1)
    }
    @Test
    internal fun `updateContact should call service`() {
        // given
        every { service.editContact(testUuid, contactCreationDto) } returns contactDto1

        // when
        val actual = underTest.editContact(testUuid, contactCreationDto)

        // then
        assertThat(actual).isEqualTo(contactDto1)
    }

    @Test
    internal fun `listContacts should call service`() {
        // given
        every { service.listContacts() } returns listOf(contactDto1, contactDto2)

        // when
        val actual = underTest.listContacts()

        // then
        assertThat(actual).isEqualTo(listOf(contactDto1, contactDto2))
    }

    @Test
    internal fun `deleteContact should call service`() {
        // when
        underTest.deleteContact(testUuid)

        // then
        verify { service.deleteContact(testUuid) }
    }
}
