package hu.blzsaa.uxstudioteam.data

import java.util.UUID
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import javax.validation.constraints.Email

@Document
data class Contact(@Id val id: UUID, val name: String, val phoneNumber: String, val emailAddress: String)

data class ContactCreationDto(val name: String, val phoneNumber: String, @get: Email() val emailAddress: String)

data class ContactDto(val id: UUID, val name: String, val phoneNumber: String, @get: Email() val emailAddress: String)
