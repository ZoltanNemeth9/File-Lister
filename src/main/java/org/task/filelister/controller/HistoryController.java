package org.task.filelister.controller;

import org.task.filelister.entity.HistoryEntity;
import org.task.filelister.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class HistoryController {

    @Autowired
    private HistoryRepository historyRepository;

    /**
     *
     * Gives back the log history of the requests
     *
     * @param who the user filter
     * @param when the date filter
     * @param what the folder filter
     * @return list of history entities
     */
    @GetMapping("/history")
    public List<HistoryEntity> getHistory(@RequestParam(required = false) String who,
                                    @RequestParam(required = false) String when,
                                    @RequestParam(required = false) String what) {
        List<HistoryEntity> all = historyRepository.findAll();

        return all.stream()
                .filter(h -> who == null || h.getWho().equalsIgnoreCase(who))
                .filter(h -> when == null || h.getWhenRequested().toString().contains(when))
                .filter(h -> what == null || h.getWhat().contains(what))
                .toList();
    }

    //TODO DTO class implementation
}