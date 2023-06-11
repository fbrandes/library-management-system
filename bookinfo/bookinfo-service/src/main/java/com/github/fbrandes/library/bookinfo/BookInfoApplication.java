package com.github.fbrandes.library.bookinfo;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.ws.rs.core.Application;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;

@OpenAPIDefinition(
        info = @Info(
                title = "Book API",
                version = "0.0.1",
                contact = @Contact(
                        name = "Book API Support",
                        url = "http://example.com/contact",
                        email = "techsupport@example.com"),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0.html"))
)
@Slf4j
@QuarkusMain
public class BookInfoApplication extends Application {
    public static void main(String[] args) {
        log.info("Starting BookInfo Service");
        Quarkus.run(args);
    }
}
