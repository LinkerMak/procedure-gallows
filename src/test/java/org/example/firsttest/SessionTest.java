package org.example.firsttest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import printers.OutputPrinter;
import session.Session;
import wordsrepository.WordsRepository;
import writers.Input;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SessionTest {

    @Mock
    private Input input;

    @Mock
    private OutputPrinter printer;

    @Mock
    private WordsRepository repository;

    @InjectMocks
    private Session session;

    @Test
    public void shouldQuitOnQuitCommand(){
        when(input.nextLine()).thenReturn("В");

        session.create();

        verify(printer).printSessionQuit();
    }
}
