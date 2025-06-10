package org.task.filelister.controller;

import org.task.filelister.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.task.filelister.entity.HistoryEntity;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class FileController {

    @Autowired
    private HistoryRepository historyRepository;

    /**
     * Gets unique filenames of folders
     *
     * @param folder  the main path to list the files
     * @param extension optional extension filter
     * @return list of filenames
     */
    @GetMapping("/getUnique")
    public List<String> getUnique(@RequestParam String folder,
                                  @RequestParam(required = false) String extension) {
        File dir = new File(folder);
        Set<String> results = new HashSet<>();

        if (!dir.exists() || !dir.isDirectory()) {
            throw new IllegalArgumentException("Invalid folder path: " + folder);
        }

        try {
            //Have to be honest I didn't know Files had traversing file structure streams
            // but I really liked this approach (found online)
            // I had issues with bypassing/skipping illegal access exceptions elegantly
            // It skips not visitable files and directories
            Files.walkFileTree( Paths.get(folder), new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    if (extension == null || file.toString().endsWith(extension)) {
                        results.add(file.getFileName().toString());
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    if (Files.isReadable(dir)) {
                        return FileVisitResult.CONTINUE;
                    } else {
                        return FileVisitResult.SKIP_SUBTREE;
                    }
                }
            });

            String osUser = System.getProperty("user.name");
            historyRepository.save(new HistoryEntity(osUser, LocalDateTime.now(), folder));

            return results.stream().toList();
        } catch (IOException e) {
            throw new RuntimeException("Error reading folder", e);
        }
    }
}