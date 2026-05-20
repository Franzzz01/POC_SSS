package com.shopwave.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Generic file-based JSON repository. Replaces any database.
 * Data is stored in JSON files in the ./data directory.
 */
@Repository
@Slf4j
public class FileRepository {

    private final String dataPath;
    private final ObjectMapper mapper;

    public FileRepository(@Value("${shopwave.data.path:./data}") String dataPath) {
        this.dataPath = dataPath;
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());

        // Create data directory if it doesn't exist
        File dir = new File(dataPath);
        if (!dir.exists()) {
            dir.mkdirs();
            log.info("Created data directory at: {}", dir.getAbsolutePath());
        }
    }

    public <T> List<T> readAll(String filename, TypeReference<List<T>> typeRef) {
        File file = new File(dataPath, filename);
        if (!file.exists()) return new ArrayList<>();
        try {
            return mapper.readValue(file, typeRef);
        } catch (IOException e) {
            log.error("Error reading file {}: {}", filename, e.getMessage());
            return new ArrayList<>();
        }
    }

    public <T> void writeAll(String filename, List<T> data) {
        File file = new File(dataPath, filename);
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
        } catch (IOException e) {
            log.error("Error writing file {}: {}", filename, e.getMessage());
            throw new RuntimeException("Failed to save data", e);
        }
    }

    public <T> Optional<T> findOne(String filename, TypeReference<List<T>> typeRef, Predicate<T> predicate) {
        return readAll(filename, typeRef).stream().filter(predicate).findFirst();
    }

    public <T> void save(String filename, TypeReference<List<T>> typeRef, T item,
                         Predicate<T> matchPredicate, boolean replaceExisting) {
        List<T> all = readAll(filename, typeRef);
        if (replaceExisting) {
            all.removeIf(matchPredicate);
        }
        all.add(item);
        writeAll(filename, all);
    }

    public <T> boolean delete(String filename, TypeReference<List<T>> typeRef, Predicate<T> predicate) {
        List<T> all = readAll(filename, typeRef);
        boolean removed = all.removeIf(predicate);
        if (removed) writeAll(filename, all);
        return removed;
    }

    public <T> void update(String filename, TypeReference<List<T>> typeRef,
                           Predicate<T> predicate, Function<T, T> updater) {
        List<T> all = readAll(filename, typeRef);
        List<T> updated = all.stream().map(item -> predicate.test(item) ? updater.apply(item) : item).toList();
        writeAll(filename, new ArrayList<>(updated));
    }
}
