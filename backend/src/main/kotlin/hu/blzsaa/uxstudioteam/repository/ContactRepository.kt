package hu.blzsaa.uxstudioteam.repository

import hu.blzsaa.uxstudioteam.data.Contact
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ContactRepository : CrudRepository<Contact, UUID>
