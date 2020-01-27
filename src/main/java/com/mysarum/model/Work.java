package com.mysarum.model;


import com.mysarum.service.UserService;
import com.mysarum.service.WorkService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "WORK")
public class Work {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "WORK_ID")
    private int id;
    @Column(name = "PLAYER_ID")
    private int playerId;
    @Column(name = "`timestamp`")
    private LocalDateTime timestamp;

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public static void refreshWorkState(WorkService workService, UserService userService, User user) {
        if (workService.findByPlayerId(user.getId()) != null) {
            LocalDateTime currentTime = workService.findByPlayerId(user.getId()).getTimestamp();

            boolean result = (currentTime.plusMinutes(30).compareTo(currentTime = LocalDateTime.now()) <= -1);
            if (result) {
                workService.deleteWork(user.getId());
                user.setOnWork(false);
                userService.saveStats(user);
            }
        }
    }
}