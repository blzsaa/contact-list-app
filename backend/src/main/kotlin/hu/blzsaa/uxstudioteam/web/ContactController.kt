package hu.blzsaa.uxstudioteam.web

import hu.blzsaa.uxstudioteam.data.ContactCreationDto
import hu.blzsaa.uxstudioteam.data.ContactDto
import hu.blzsaa.uxstudioteam.service.ContactCrudService
import mu.KotlinLogging
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestBody
import java.util.UUID
import javax.validation.Valid

private val logger = KotlinLogging.logger {}

@CrossOrigin(origins = ["http://localhost:5000"])
@RestController
@RequestMapping("contacts")
class ContactController(private val contactCrudService: ContactCrudService) {
    @PostMapping
    fun createContact(@Valid @RequestBody contactCreationDto: ContactCreationDto): ContactDto {
        logger.info { "incoming create contact request with $contactCreationDto" }
        val createContact = contactCrudService.createContact(contactCreationDto)
        logger.info { "returning: $createContact" }
        return createContact
    }

    @GetMapping
    fun listContacts(): List<ContactDto> {
        logger.info { "incoming list contact request" }
        val listContacts = contactCrudService.listContacts()
        logger.info { "returning $listContacts" }
        return listContacts
    }

    @DeleteMapping("{id}")
    fun deleteContact(@PathVariable("id") id: UUID) {
        logger.info { "incoming delete request for contact with id: $id" }
        contactCrudService.deleteContact(id)
        logger.info { "successfully deleted" }
    }

    @PostMapping("{id}")
    fun editContact(@PathVariable("id") id: UUID, @RequestBody @Valid contactCreationDto: ContactCreationDto): ContactDto {
        logger.info { "incoming update request for contact with id: $id, to update to: $contactCreationDto" }
        val editContact = contactCrudService.editContact(id, contactCreationDto)
        logger.info { "update was successful, output is: $editContact" }
        return editContact
    }
}
