package org.task.filelister.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/generate")
public class GenerateController {

    private static final String BASE_DIR = "generated"; // Root directory for creation

    /**
     *
     * Generates a file structure using a  binaryTree like traverse, that was my original idea during the personal interview
     * before misdirected myself with the ls approach
     *
     * @param structure a json with a specific template
     * @return positive response
     */
    @PostMapping
    public ResponseEntity<String> generateStructure(@RequestBody Map<String, Object> structure) {
        try {
            File root = new File(BASE_DIR);
            if (!root.exists()) root.mkdirs();

            //I would go with Queue/LinkedList approach here but I've read that Deque is faster
            Deque<Node> queue = new ArrayDeque<>();
            queue.push(new Node(BASE_DIR, structure));

            while (!queue.isEmpty()) {
                Node current = queue.pop();

                for (Map.Entry<String, Object> entry : current.data.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    String newPath = current.path + File.separator + key;

                    if (value instanceof Map<?, ?> nestedMap) {
                        File folder = new File(newPath);
                        //this way we can make sure it only creates it if it doesn't exist
                        if (!folder.exists()) folder.mkdirs();
                        queue.push(new Node(newPath, (Map<String, Object>) nestedMap));
                    } else if (value instanceof String filename) {
                        File file = new File(current.path + File.separator + filename);
                        try (FileWriter writer = new FileWriter(file)) {
                            writer.write("Generated test contant");
                        }
                    }
                }
            }

            return ResponseEntity.ok("Structure created in ./" + BASE_DIR);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    private static class Node {
        String path;
        Map<String, Object> data;

        Node(String path, Map<String, Object> data) {
            this.path = path;
            this.data = data;
        }
    }
}
