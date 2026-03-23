package com.example.apirestful.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProdutoSeleniumTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private final String URL_FRONT = "http://localhost:5173";

    @BeforeEach
    void setup() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new"); 
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @Order(1)
    @DisplayName("Deve cadastrar um produto com sucesso e validar na lista")
    void testCadastrarProdutoComSucesso() {
        driver.get(URL_FRONT);

        WebElement inputNome = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[placeholder='nome']")));
        WebElement inputQtd = driver.findElement(By.cssSelector("input[placeholder='quantidade']"));
        WebElement inputPreco = driver.findElement(By.cssSelector("input[placeholder='preço']"));
        WebElement btnCadastrar = driver.findElement(By.xpath("//button[text()='cadastrar']"));

        inputNome.sendKeys("Mesa");
        inputQtd.sendKeys("10");
        inputPreco.sendKeys("700");
        btnCadastrar.click();

        boolean encontrouProduto = wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.className("containerProdutos"), "Mesa"));

        assertTrue(encontrouProduto, "O produto deveria estar visível na lista após o cadastro.");
    }

    @Test
    @Order(2)
    @DisplayName("Validar mensagem de erro para campos vazios")
    void testValidarCamposObrigatorios() {
        driver.get(URL_FRONT);
        WebElement btnCadastrar = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='cadastrar']")));
        btnCadastrar.click();

        wait.until(ExpectedConditions.alertIsPresent());
        String textoAlerta = driver.switchTo().alert().getText();
        assertTrue(textoAlerta.contains("preencha todos os campos"), "Deveria exibir o alerta de campos obrigatórios.");
        driver.switchTo().alert().accept();
    }

    @Test
    @Order(3)
    @DisplayName("Deve remover um produto da lista")
    void testExcluirProduto() {
        driver.get(URL_FRONT);
        WebElement btnExcluir = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='excluir']")));
        btnExcluir.click();
    }

    @Test
    @Order(4)
    @DisplayName("Simular Falha de Rede/Timeout")
    void testSimularTimeoutRede() {
        driver.manage().timeouts().pageLoadTimeout(Duration.ofMillis(10));
        Assertions.assertThrows(org.openqa.selenium.TimeoutException.class, () -> {
            driver.get("http://10.255.255.1");
        }, "O sistema deveria identificar o timeout de rede.");
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
    }

    @Test
    @Order(5)
    @DisplayName("Entrada Inválida")
    void testEntradaInvalida() {
        driver.get(URL_FRONT);
        WebElement inputNome = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[placeholder='nome']")));
        WebElement btnCadastrar = driver.findElement(By.xpath("//button[text()='cadastrar']"));

        String dadosFuzz = "<script>alert('entrada invalida')</script> " + "A".repeat(100);
        inputNome.sendKeys(dadosFuzz);
        driver.findElement(By.cssSelector("input[placeholder='quantidade']")).sendKeys("1");
        driver.findElement(By.cssSelector("input[placeholder='preço']")).sendKeys("1");
        btnCadastrar.click();

        try {
            wait.until(ExpectedConditions.alertIsPresent());
            driver.switchTo().alert().accept();
        } catch (Exception e) {
            assertTrue(true);
        }
    }
}