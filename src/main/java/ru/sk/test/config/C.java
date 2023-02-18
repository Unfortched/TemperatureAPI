package ru.sk.test.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Configuration
@Slf4j
public class C implements CommandLineRunner {

    public String version;

    public C() throws IOException, ParserConfigurationException, SAXException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                .newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        org.w3c.dom.Document document = null;
        document = documentBuilder.parse(new File("./pom.xml"));
        this.version = document.getDocumentElement().getElementsByTagName("version").item(0).getTextContent();
    }

    @Value("${db.driver}")
    public String DB_DRIVER;
    @Value("${db.url}")
    public String DB_URL;
    @Value("${db.login}")
    public String DB_LOGIN;
    @Value("${db.password}")
    public String DB_PASSWORD;

    @Override
    public void run(String... args) throws Exception {
        List<String> names = new ArrayList<>();
        List<String> values = new ArrayList<>();
        names.add("version");
        values.add(this.version);

        for (Field field : this.getClass().getFields()) {
            if (field.isAnnotationPresent(Value.class)) {
                Value annotation = field.getDeclaredAnnotation(Value.class);
                String name = annotation.value();
                name = name.substring("${".length());
                name = name.substring(0, name.length() - "}".length());
                Object value = field.get(this);
                if ("NULL".equals(value)) {
                    field.set(this, value = null);
                }
                names.add(name);
                values.add(String.valueOf(value));
            }
        }

        int sizeColumnName = names.stream().mapToInt(String::length).max().orElse(0) + 1;
        int sizeColumnValue = values.stream().mapToInt(String::length).max().orElse(0) + 1;

        String format = "\n" + "%-" + sizeColumnName + "s" + " " + "%-" + sizeColumnValue + "s" + "";

        StringBuilder stringBuilder = new StringBuilder("PROPERTIES")
                .append(String.format(format, "name", "value"));

        int n = names.size();

        for (int i = 0; i < n; i++) {
            String name = names.get(i);
            String value = values.get(i);
            stringBuilder.append(String.format(format, name, value));
        }

        log.info(stringBuilder.toString());
    }
}
