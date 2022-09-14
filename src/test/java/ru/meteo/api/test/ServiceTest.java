package ru.meteo.api.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Value;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PS. Мало тестов с Mockito, так как фантазии перестает хватать в рабочее и вечернее время :)
 */

@ExtendWith(MockitoExtension.class)
public class ServiceTest {

    @Mock
    private MapDB mapDB;

    @InjectMocks
    private Service service;

    @Test
    public void serviceTest() {

        Answer<Stream> answer = new Answer<Stream>() {
            public Stream answer(InvocationOnMock invocation) throws Throwable {
                return Stream.of("Paris/France", "Moscow/Russia");
            }
        };

        Mockito.when(mapDB.getCache()).thenAnswer(answer);
        assertNull(service.getDataByTimestamp("Paris/France", 0L));
    }
}
