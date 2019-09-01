package hu.blzsaa.contactlistapp.e2e

import io.github.bonigarcia.wdm.WebDriverManager
import org.assertj.core.api.Assertions.assertThat
import org.awaitility.Awaitility.await
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions

@TestInstance(TestInstance.Lifecycle.PER_CLASS)

internal class ContactListAppE2E {

    private lateinit var driver: WebDriver

    @BeforeAll
    fun beforeAll() {
        WebDriverManager.chromedriver().setup()
        driver = ChromeDriver(ChromeOptions().addArguments("--headless"))
    }

    @BeforeEach
    internal fun setUp() {
        driver.get("http://localhost:5000")
    }

    @AfterEach
    internal fun deleteAllContacts() {
        clearInputs()
        await().until {
            skipHeaderRow()
                    .forEach { e ->
                        e.findElement(By.name("edit")).click()
                        e.findElement(By.name("delete")).click()
                    }
            driver.findElements(By.tagName("tr")).size == 1
        }
    }

    private fun clearInputs() {
        awaitUntilInputIsCleared("name")
        awaitUntilInputIsCleared("phoneNumber")
        awaitUntilInputIsCleared("emailAddress")
    }

    private fun awaitUntilInputIsCleared(id: String) {
        await().until {
            driver.findElement(By.id(id)).clear()
            driver.findElement(By.id(id)).getAttribute("value").isEmpty()
        }
    }

    @AfterAll
    fun afterAll() {
        driver.quit()
    }

    @Test
    internal fun `should be able to add contact`() {
        // when
        createContact()

        // then
        val newItem = driver.findElements(By.tagName("tr")).last()
        assertThat(getContactInformation(newItem)).isEqualTo(listOf("name", "+36123456789", "email@email.com"))
    }

    @Test
    internal fun `should be able to modify contact`() {
        // given
        createContact()
        val newItem = driver.findElements(By.tagName("tr")).last()

        // when
        newItem.findElement(By.name("edit")).click()
        newItem.findElements(By.tagName("input"))[0].clear()
        newItem.findElements(By.tagName("input"))[0].sendKeys("name2")
        newItem.findElement(By.name("save")).click()

        // then
        assertThat(getContactInformation(newItem)).isEqualTo(listOf("name2", "+36123456789", "email@email.com"))
    }

    private fun skipHeaderRow() = driver.findElements(By.tagName("tr")).drop(1)

    private fun getContactInformation(newItem: WebElement): List<String> {
        return newItem.findElements(By.tagName("input")).map { input -> input.getAttribute("value") }
    }

    private fun createContact(name: String = "name") {
        driver.findElement(By.id("name")).sendKeys(name)
        driver.findElement(By.id("phoneNumber")).sendKeys("+36123456789")
        driver.findElement(By.id("emailAddress")).sendKeys("email@email.com")
        driver.findElement(By.id("create-contact-button")).click()
        clearInputs()
    }
}
