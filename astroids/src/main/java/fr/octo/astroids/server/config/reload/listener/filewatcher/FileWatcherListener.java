package fr.octo.astroids.server.config.reload.listener.filewatcher;

import fr.octo.astroids.server.config.reload.FileSystemWatcher;

import java.nio.file.Path;
import java.nio.file.WatchEvent;

/**
 *  All classes that implement this class will be called when a file changes (create or new).
 */
public interface FileWatcherListener {

    void setFileSystemWatcher(FileSystemWatcher fileSystemWatcher);

    boolean support(Path file, WatchEvent.Kind kind);

    void onChange(String parentFolder, Path file, WatchEvent.Kind kind);
}
