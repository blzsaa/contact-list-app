package hu.blzsaa.contactlistapp.data

import java.util.UUID
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

@Document
data class Contact(@Id val id: UUID, val name: String, val phoneNumber: String, val emailAddress: String)

data class ContactCreationDto(@get: NotBlank val name: String, @get:Pattern(regexp = "^[+]36\\d{9}\$") val phoneNumber: String, @get: [Email NotBlank] val emailAddress: String)

data class ContactDto(@get: NotBlank val id: UUID, val name: String, @get:Pattern(regexp = "^[+]36\\d{9}\$") val phoneNumber: String, @get: [Email NotBlank] val emailAddress: String)
