package com.midziklabs.advertisement.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;

import com.midziklabs.advertisement.service.FileStorageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileStorageServiceTests {

    private FileStorageService fileStorageService;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        fileStorageService = new FileStorageService(tempDir.toString());
    }

    @Test
    void testStoreFile() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Hello, World!".getBytes());

        String storedFileName = fileStorageService.storeFile(file);

        Path storedFilePath = tempDir.resolve(storedFileName);
        
        assertTrue(Files.exists(storedFilePath), "File should be stored in the directory.");
    }

    @Test
    void testStoreFileWithInvalidPath() {
        MockMultipartFile file = new MockMultipartFile("file", "../test.txt", "text/plain", "Hello".getBytes());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            fileStorageService.storeFile(file);
        });

        assertTrue(exception.getMessage().contains("Invalid file path sequence"));
    }

    @Test
    void testLoadFile() throws IOException {
        String fileName = "test.txt";
        Path filePath = tempDir.resolve(fileName);
        Files.createFile(filePath);

        Path loadedPath = fileStorageService.loadFile(fileName);
        assertEquals(filePath, loadedPath, "Loaded path should match the expected file path.");
    }
}
