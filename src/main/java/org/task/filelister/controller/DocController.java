package org.task.filelister.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.file.*;
import java.util.zip.*;

@RestController
@RequestMapping("/doc")
public class DocController {

    //TODO put them into the application property for better managing
    private static final String OUTPUT_DIR = "build/docs/javadoc";
    private static final String ZIP_PATH = "build/docs/javadoc.zip";
    private static final String SOURCE_DIR = "src/main/java";
    private static final String PACKAGE_ROOT = "org.task.filelister";

    /**
     * Dinamically generates the javadoc files
     */
    private void ensureJavadocGenerated() throws IOException, InterruptedException {

        //needed for external dependencies like spring because otherwise javadoc doesn't recognize them
        String classpath = System.getProperty("java.class.path");

        Path indexHtml = Paths.get(OUTPUT_DIR, "index.html");
        if (Files.exists(indexHtml)) return;

        ProcessBuilder pb = new ProcessBuilder(
                "javadoc",
                "-d", OUTPUT_DIR,
                "-sourcepath", SOURCE_DIR,
                "-subpackages", PACKAGE_ROOT,
                "-classpath", classpath

        );
        pb.redirectErrorStream(true);
        Process process = pb.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            reader.lines().forEach(System.out::println);
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new IOException("Javadoc generation failed with exit code " + exitCode);
        }
    }

    /**
     * Gets javadoc files into a zip
     *
     * @return zip of files
     */
    @GetMapping("/download")
    public ResponseEntity<Object> downloadZip() throws IOException, InterruptedException {
        try {
            ensureJavadocGenerated();
        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body("Error during javadoc generation: " + ex);

        }

        // Create ZIP
        Path zipFile = Paths.get(ZIP_PATH);
        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipFile))) {
            Path sourceDir = Paths.get(OUTPUT_DIR);
            Files.walk(sourceDir)
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(sourceDir.relativize(path).toString());
                        try {
                            zos.putNextEntry(zipEntry);
                            Files.copy(path, zos);
                            zos.closeEntry();
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    });
        }
        InputStreamResource resource = new InputStreamResource(new FileInputStream(zipFile.toFile()));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=javadoc.zip")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    /**
     * Gets javadoc index.html
     *
     * @return html site of javadoc
     */
    @GetMapping
    public ResponseEntity<Object> viewJavadoc() throws IOException, InterruptedException {
        try {
            ensureJavadocGenerated();
        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body("Error during javadoc generation: " + ex);

        }

        File index = new File(OUTPUT_DIR, "index.html");
        if (!index.exists()) {
            throw new FileNotFoundException("index.html not found after generation");
        }

        InputStreamResource resource = new InputStreamResource(new FileInputStream(index));
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(resource);
    }
}