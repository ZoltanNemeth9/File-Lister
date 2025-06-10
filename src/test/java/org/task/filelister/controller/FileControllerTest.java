package org.task.filelister.controller;

import org.task.filelister.entity.HistoryEntity;
import org.task.filelister.repository.HistoryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileControllerTest {

    @Mock
    private HistoryRepository historyRepository;

    @InjectMocks
    private FileController fileController;

    public FileControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUniqueWithExtension() throws Exception {
        Path tempDir = Files.createTempDirectory("testDir");
        Files.createFile(tempDir.resolve("file1.txt"));
        Files.createFile(tempDir.resolve("file2.log"));

        List<String> result = fileController.getUnique(tempDir.toString(), ".txt");
        assertEquals(1, result.size());
        assertTrue(result.contains("file1.txt"));

        verify(historyRepository, times(1)).save(any(HistoryEntity.class));
    }

    @Test
    void testGetUniqueWithoutExtension() throws Exception {
        Path tempDir = Files.createTempDirectory("testDir");
        Files.createFile(tempDir.resolve("file1.txt"));
        Files.createFile(tempDir.resolve("file2.txt"));

        List<String> result = fileController.getUnique(tempDir.toString(), null);
        assertEquals(2, result.size());

        verify(historyRepository, times(1)).save(any(HistoryEntity.class));
    }

    @Test
    void testGetUniqueInvalidFolder() {
        assertThrows(IllegalArgumentException.class, () -> {
            fileController.getUnique("invalid-folder", null);
        });
        verify(historyRepository, never()).save(any());
    }
}