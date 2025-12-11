import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// filepath: JavaProject/src/test/java/com/marketplace/service/FileStorageServiceTest.java
package com.marketplace.service;




class FileStorageServiceTest {

    private FileStorageService fileStorageService;

    @Mock
    private MultipartFile multipartFile;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        fileStorageService = new FileStorageService();
        Files.createDirectories(Path.of("uploads/test"));
    }

    @Test
    void testSaveFile_Success() throws IOException {
        // Arrange
        String originalFilename = "testfile.txt";
        String subDir = "test";
        byte[] fileContent = "Sample content".getBytes();
        InputStream inputStream = new ByteArrayInputStream(fileContent);

        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getOriginalFilename()).thenReturn(originalFilename);
        when(multipartFile.getInputStream()).thenReturn(inputStream);

        // Act
        String result = fileStorageService.saveFile(multipartFile, subDir);

        // Assert
        assertNotNull(result);
        assertTrue(result.startsWith("/uploads/test/"));
        assertTrue(result.endsWith("_testfile.txt"));

        Path savedFilePath = Path.of("uploads", subDir, result.substring(result.lastIndexOf("/") + 1));
        assertTrue(Files.exists(savedFilePath));
        assertEquals("Sample content", Files.readString(savedFilePath));

        // Cleanup
        Files.deleteIfExists(savedFilePath);
    }

    @Test
    void testSaveFile_EmptyFile() {
        // Arrange
        when(multipartFile.isEmpty()).thenReturn(true);

        // Act
        String result = fileStorageService.saveFile(multipartFile, "test");

        // Assert
        assertNull(result);
        verify(multipartFile, never()).getOriginalFilename();
        verify(multipartFile, never()).getInputStream();
    }

    @Test
    void testSaveFile_PathTraversalAttack() {
        // Arrange
        String originalFilename = "../testfile.txt";
        String subDir = "test";

        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getOriginalFilename()).thenReturn(originalFilename);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> 
            fileStorageService.saveFile(multipartFile, subDir)
        );
        assertEquals("Cannot store file outside current directory.", exception.getMessage());
    }

    @Test
    void testSaveFile_IOException() throws IOException {
        // Arrange
        String originalFilename = "testfile.txt";
        String subDir = "test";

        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getOriginalFilename()).thenReturn(originalFilename);
        when(multipartFile.getInputStream()).thenThrow(new IOException("Failed to read file"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> 
            fileStorageService.saveFile(multipartFile, subDir)
        );
        assertEquals("Failed to store file.", exception.getMessage());
    }
}