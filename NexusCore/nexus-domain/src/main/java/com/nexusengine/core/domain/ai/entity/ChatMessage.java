package com.nexusengine.core.domain.ai.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.nexusengine.core.domain.customer.entity.Member;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "chat_message", schema = "ai")
public class ChatMessage {
        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

private Long id;

        @Column(name = "member_id")

private Long memberId;
        @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", insertable = false, updatable = false)

private Member member;

        @Column(name = "conversation_id", nullable = false)

private String sessionId;

        @Column(name = "role", nullable = false)

private String role;

        @Column(name = "content", nullable = false)

private String content;

        @Column(name = "model_name")

private String model;

        @Column(name = "token_count")

private Integer tokensUsed;

        @Column(name = "created_at")

private OffsetDateTime createTime;
}
