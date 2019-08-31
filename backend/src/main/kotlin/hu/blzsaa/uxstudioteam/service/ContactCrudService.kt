package hu.blzsaa.uxstudioteam.service

import hu.blzsaa.uxstudioteam.data.Contact
import hu.blzsaa.uxstudioteam.data.ContactCreationDto
import hu.blzsaa.uxstudioteam.data.ContactDto
import hu.blzsaa.uxstudioteam.helper.UuidGenerator
import hu.blzsaa.uxstudioteam.repository.ContactRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ContactCrudService(private val repository: ContactRepository, private val uuidGenerator: UuidGenerator) {
    fun createContact(contactCreationDto: ContactCreationDto): ContactDto {
        val contact1 = transform(uuidGenerator.randomUUID(), contactCreationDto)
        val saved = repository.save(contact1)
        return transform(saved)
    }

    fun listContacts(): List<ContactDto> {
        return repository.findAll().map { c -> transform(c) }
    }

    fun deleteContact(id: UUID) {
        return repository.deleteById(id)
    }

    fun editContact(id: UUID, contactCreationDto: ContactCreationDto): ContactDto {
        val updatedContact = transform(id, contactCreationDto)
        val saved = repository.save(updatedContact)
        return transform(saved)
    }

    private fun transform(c: Contact) =
            ContactDto(c.id, c.name, c.phoneNumber, c.emailAddress)

    private fun transform(id: UUID, contactCreationDto: ContactCreationDto) =
            Contact(id, contactCreationDto.name, contactCreationDto.phoneNumber, contactCreationDto.emailAddress)
}
