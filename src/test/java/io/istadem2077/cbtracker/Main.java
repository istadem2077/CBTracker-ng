package io.istadem2077.cbtracker;

import static io.istadem2077.cbtracker.ElementsList.*;
import static io.istadem2077.cbtracker.TgMessage.*;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void LoginMySat(WebDriver driver, String email, String password, WebDriverWait wait) throws InterruptedException {
        driver.get("https://mysat.collegeboard.org/");
        driver.navigate().refresh();

        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(signInBtnId))).click();
        }
        catch (TimeoutException timeoutException) {
            if (driver.getTitle().equals("My SAT Home Page")){
                return;
            }
        }

        Thread.sleep(2000);
        WebElement ElementIdpUsername = driver.findElement(By.cssSelector(signInEmailId));
        ElementIdpUsername.clear();
        ElementIdpUsername.sendKeys(email);

        Thread.sleep(2000);

        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(signInEmailSubmitId))).click();
        }
        catch (ElementClickInterceptedException interceptedException) {
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#onetrust-accept-btn-handler"))).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(signInEmailSubmitId))).click();
        }

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(signInPasswordId)));

        driver.findElement(By.cssSelector(signInPasswordId)).clear();
        driver.findElement(By.cssSelector(signInPasswordId)).sendKeys(password);
        driver.findElement(By.cssSelector(signInPasswordSubmitId)).click();

    }

    public static void SatReg(WebDriver driver, WebDriverWait wait) throws InterruptedException {
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(satRegBtnId))).click();

        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(getStartedBtnId))).click();
        }
        catch (TimeoutException timeoutException) {
            System.out.println("reloading");
            driver.get("https://mysat.collegeboard.org/dashboard");
            SatReg(driver, wait);
        }
        catch (ElementClickInterceptedException interceptedException) {
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#onetrust-accept-btn-handler"))).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(getStartedBtnId))).click();
        }

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(gradeDateConfirmId))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(gradeConfirmId))).click();

        Thread.sleep(5000);

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(continueToDemosId))).click();
        Thread.sleep(5000);
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(saveDemosContinueId))).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(getStartedBtnScrollId))).click();
        driver.findElement(By.cssSelector(tosScrollboxId)).sendKeys(Keys.END);

        Thread.sleep(5000);

        driver.findElement(By.cssSelector(tosAcceptCss)).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(tosContinueId))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(selectLocationNextId))).click();
    }

    public static void ChooseTestDate(String TestDate,WebDriver driver, WebDriverWait wait) {
        ((JavascriptExecutor)driver).executeScript("window.scrollTo(0,600);");
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(String.format(testDateButtonId, TestDate)))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(testDateContinueId))).click();
    }

    public static int FindTestCenter(WebDriver driver, WebDriverWait wait) throws InterruptedException {
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(selectDiffCenterId))).click();

        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(selectDiffCenterId))).click();
        }
        catch (Exception _){

        }

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(findCentersId))).click();
        Thread.sleep(2000);
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(toggleButtonId))).click();
        return driver.findElement(By.cssSelector(availSchoolsCountCss)).getText().charAt(11);
    }

    public static void RefreshTestCenter(WebDriver driver, WebDriverWait wait) throws InterruptedException {
        Thread.sleep(2000);

        if (driver.getTitle().equals("SAT Registration")){
            driver.navigate().refresh();
        }

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(getStartedBtnScrollId))).click();
        FindTestCenter(driver, wait);
    }

    public static int CheckSchools(int Counter, int PrevCounter, String TestDate, WebDriver driver) throws IOException, InterruptedException {
        List<String> MessageList = new ArrayList<>();
        MessageList.add(String.format("%s\nLast update: %s %s\n\n", TestDate, LocalTime.now(), LocalDate.now()));
        StringBuilder Message = new StringBuilder();

        if (Counter > 0) {
            while (driver.findElement(By.id("undefined_next")).getAttribute("aria-disabled").equals("false")) {
                List<WebElement> table = (driver.findElement(By.className("cb-table")).findElement(By.tagName("tbody")).findElements(By.tagName("tr")));
                for (WebElement tr : table) {
                    if (tr.findElement(By.className("test-center-name")).getText().equals("Nakhchivan State University"))
                        continue;
                    MessageList.add(String.format("%s : %s\n", tr.findElement(By.className("test-center-name")).getText(), tr.findElement(By.className("seat-label")).getText()));
                }
                driver.findElement(By.className("cb-right")).click();
            }
            List<WebElement> table = (driver.findElement(By.className("cb-table")).findElement(By.tagName("tbody")).findElements(By.tagName("tr")));
            for (WebElement tr : table) {
                if (tr.findElement(By.className("test-center-name")).getText().equals("Nakhchivan State University"))
                    continue;
                MessageList.add(String.format("%s : %s\n", tr.findElement(By.className("test-center-name")).getText(), tr.findElement(By.className("seat-label")).getText()));
            }
            if (MessageList.size() == 1){
                return Counter;
            }

            for (String Msg : MessageList){
                Message.append(Msg); // concat everything into one message
            }

            if (PrevCounter == 0){
                Notify("976908358", Message.toString()); // Arif
                Notify("5670908383", Message.toString()); // My chat
                Notify("584098198", Message.toString());  // Mansur
                Notify("1278150481", Message.toString()); // Rafail
            }

        }
        return Counter;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        FirefoxOptions opt = new FirefoxOptions();
//        opt.addArguments("--disable-dev-shm-usage");
//        opt.addArguments("--no-sandbox");
//        opt.addArguments("--disable-browser-side-navigation");
//        opt.addArguments("--start-maximized");
//        ChromeDriverService srv = new ChromeDriverService(new File(".\\chromedriver.exe"));
        WebDriver driver = new FirefoxDriver(opt);
        WebDriverWait wdw = new WebDriverWait(driver, Duration.ofSeconds(60));

        String email = args[0];
        String pass = args[1];
        String TestDate = args[2];

        while (true) {
            int PrevCounter = 0;
            try {
                System.out.println("LoginMySAT");
                LoginMySat(driver, email, pass, wdw);
                CBError("Logged%20In");
                SatReg(driver, wdw);
                ChooseTestDate(TestDate, driver, wdw);
                int SchoolCount = FindTestCenter(driver, wdw);
                PrevCounter = CheckSchools(SchoolCount, PrevCounter, TestDate, driver);
                while (true) {
                    try {
                        System.out.println("refresh");
                        RefreshTestCenter(driver, wdw);
                        //noinspection DataFlowIssue
                        PrevCounter = CheckSchools(SchoolCount, PrevCounter, TestDate, driver);
                    }
                    catch (TimeoutException timeoutException) {
//                        CBError("TimeoutException at %s".formatted(LocalTime.now().toString()));
                        System.out.println("Timeout");
                        break;
                    }
                    catch (NoSuchWindowException noSuchWindowException) {
                        return;
                    }
                    catch (WebDriverException webDriverException) {
//                        CBError("WebDriverException at %s".formatted(LocalTime.now().toString()));
                        return;
                    }
                    catch (Exception e) {
//                        CBError("Exception at %s".formatted(LocalTime.now().toString()));
                        driver.quit();
                        return;
                    }
                }
                PrevCounter = 0;
            }
            catch (TimeoutException timeoutException) {
//                CBError("Timeout at %s".formatted(LocalTime.now().toString()));
                System.out.println("Timeout outer");
            }
            catch (NoSuchWindowException noSuchWindowException) {
                return;
            }
            catch (WebDriverException webDriverException) {
//                CBError("WebDriverException at %s".formatted(LocalTime.now().toString()));
                return;
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("https://api.telegram.org/bot6412450545:AAH0N0coNpBcQdXPzGdGTyPvBGBTs1_IwAI/sendMessage?chat_id=5670908383&parse_mode=Markdown&text=Logged In".charAt(137));
//                CBError("Exception at %s".formatted(LocalTime.now().toString()));
                driver.quit();
                return;
            }
        }
    }
}