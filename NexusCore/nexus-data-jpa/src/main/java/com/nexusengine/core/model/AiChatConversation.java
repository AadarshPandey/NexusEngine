package com.nexusengine.core.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "ai_chat_conversation")
public class AiChatConversation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id")
    private Long memberId;
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "member_id", insertable = false, updatable = false)
    private UmsMember member;

    @Column(name = "session_id", nullable = false)
    private String sessionId;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "model")
    private String model;

    @Column(name = "tokens_used")
    private Integer tokensUsed;

    @Column(name = "create_time")
    private LocalDateTime createTime;
}
