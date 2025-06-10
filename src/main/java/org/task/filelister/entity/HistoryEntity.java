package org.task.filelister.entity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class HistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String who;
    private LocalDateTime whenRequested;
    private String what;

    public HistoryEntity(String who, LocalDateTime whenRequested, String what) {
        this.who = who;
        this.whenRequested = whenRequested;
        this.what = what;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public LocalDateTime getWhenRequested() {
        return whenRequested;
    }

    public void setWhenRequested(LocalDateTime whenRequested) {
        this.whenRequested = whenRequested;
    }

    public String getWhat() {
        return what;
    }

    public void setWhat(String what) {
        this.what = what;
    }
}
