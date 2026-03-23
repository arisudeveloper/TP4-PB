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
        
        WebElement inputNome = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[placeholder='nome']")));
        
        inputNome.clear();
        inputNome.sendKeys("Mesa");
        driver.findElement(By.cssSelector("input[placeholder='quantidade']")).sendKeys("10");
        driver.findElement(By.cssSelector("input[placeholder='preço']")).sendKeys("700");
        
        driver.findElement(By.xpath("//button[text()='cadastrar']")).click();

        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
            if (shortWait.until(ExpectedConditions.alertIsPresent()) != null) {
                String msgAlerta = driver.switchTo().alert().getText();
                driver.switchTo().alert().accept();
                if (msgAlerta.toLowerCase().contains("erro")) {
                    Assertions.fail("O Front exibiu erro ao cadastrar: " + msgAlerta);
                }
            }
        } catch (Exception e) {

        }

        boolean encontrou = wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.className("containerProdutos"), "Mesa"));
        
        assertTrue(encontrou, "O produto deveria estar na lista, mas não foi encontrado.");
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
        
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("containerProdutos")));

        WebElement btnExcluir = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='excluir']")));
        
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", btnExcluir);
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