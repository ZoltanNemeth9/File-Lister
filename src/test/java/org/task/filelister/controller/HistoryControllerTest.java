package org.task.filelister.controller;

import org.task.filelister.entity.HistoryEntity;
import org.task.filelister.repository.HistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HistoryControllerTest {

    @Mock
    private HistoryRepository historyRepository;

    @InjectMocks
    private HistoryController historyController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetHistoryFilteredByWho() {
        HistoryEntity h1 = new HistoryEntity("evillilly", LocalDateTime.now(), "/home/evillilly");
        HistoryEntity h2 = new HistoryEntity("evillilly", LocalDateTime.now(), "/home/evillilly/secretFolder");

        when(historyRepository.findAll()).thenReturn(List.of(h1, h2));

        List<HistoryEntity> result = historyController.getHistory("evillilly", null, null);
        assertEquals(2, result.size());
        assertEquals("evillilly", result.get(0).getWho());
    }

    @Test
    void testGetHistoryFilteredByWhat() {
        HistoryEntity h1 = new HistoryEntity("evillilly", LocalDateTime.now(), "/home/evillilly/gondor");
        HistoryEntity h2 = new HistoryEntity("evillilly", LocalDateTime.now(), "/home/evillilly/westfold");

        when(historyRepository.findAll()).thenReturn(List.of(h1, h2));

        List<HistoryEntity> result = historyController.getHistory(null, null, "gondor");
        assertEquals(1, result.size());
        assertTrue(result.get(0).getWhat().contains("gondor"));
    }

    @Test
    void testGetHistoryNoFilters() {
        HistoryEntity h1 = new HistoryEntity("evillilly", LocalDateTime.now(), "/home/evillilly");
        when(historyRepository.findAll()).thenReturn(List.of(h1));

        List<HistoryEntity> result = historyController.getHistory(null, null, null);
        assertEquals(1, result.size());
    }
}
