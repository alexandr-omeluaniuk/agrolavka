package ss.martin.core.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ss.martin.core")
public class TestMain {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(TestMain.class, args);
    }
}
