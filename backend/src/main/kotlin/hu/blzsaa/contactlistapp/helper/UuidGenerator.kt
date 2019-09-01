package hu.blzsaa.contactlistapp.helper

import org.springframework.stereotype.Component
import java.util.UUID

@Component
class UuidGenerator {
    fun randomUUID(): UUID = UUID.randomUUID()
}
