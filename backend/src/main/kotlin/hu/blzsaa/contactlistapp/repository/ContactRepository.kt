package hu.blzsaa.contactlistapp.repository

import hu.blzsaa.contactlistapp.data.Contact
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ContactRepository : CrudRepository<Contact, UUID>
