package com.nexusengine.core.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "pms_comment_reply", schema = "catalog")
public class PmsCommentReply implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "comment_id")
    private Long commentId;

    @com.fasterxml.jackson.annotation.JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", insertable = false, updatable = false)
    private PmsComment parentComment;

    @Column(name = "reply_comment_id")
    private Long replyCommentId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reply_comment_id", insertable = false, updatable = false)
    private PmsComment childComment;
}
