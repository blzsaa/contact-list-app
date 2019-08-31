package hu.blzsaa.uxstudioteam.web

import hu.blzsaa.uxstudioteam.data.ContactCreationDto
import hu.blzsaa.uxstudioteam.data.ContactDto
import hu.blzsaa.uxstudioteam.service.ContactCrudService
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

@CrossOrigin(origins = ["http://localhost:5000"])
@RestController
@RequestMapping("contacts")
class ContactController(private val contactCrudService: ContactCrudService) {
    @PostMapping
    fun createContact(@Valid @RequestBody contactCreationDto: ContactCreationDto): ContactDto {
        return contactCrudService.createContact(contactCreationDto)
    }

    @GetMapping
    fun listContacts(): List<ContactDto> {
        return contactCrudService.listContacts()
    }

    @DeleteMapping("{id}")
    fun deleteContact(@PathVariable("id") id: UUID) {
        return contactCrudService.deleteContact(id)
    }

    @PostMapping("{id}")
    fun editContact(@PathVariable("id") id: UUID, @RequestBody contactCreationDto: ContactCreationDto): ContactDto {
        return contactCrudService.editContact(id, contactCreationDto)
    }
}
