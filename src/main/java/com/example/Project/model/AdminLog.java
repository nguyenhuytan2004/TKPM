package com.example.Project.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "admin_logs")
@Getter
@Setter
@NoArgsConstructor
public class AdminLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer  id;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private User admin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActionType actionType;

    @ManyToOne
    @JoinColumn(name = "target_tool_id", nullable = false)
    private Tool targetTool;

    public enum ActionType {
        ENABLE_TOOL, DISABLE_TOOL, ADD_TOOL, DELETE_TOOL, SET_PREMIUM_TOOL, UNSET_PREMIUM_TOOL
    }
}
