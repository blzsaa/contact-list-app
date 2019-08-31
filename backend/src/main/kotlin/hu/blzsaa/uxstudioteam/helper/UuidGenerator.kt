package hu.blzsaa.uxstudioteam.helper

import org.springframework.stereotype.Component
import java.util.UUID

@Component
class UuidGenerator {
    fun randomUUID(): UUID = UUID.randomUUID()
}
