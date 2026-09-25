-- V8__ai_chat_history.sql

CREATE TABLE ai_chat_conversation (
    id              BIGSERIAL PRIMARY KEY,
    member_id       BIGINT,
    session_id      VARCHAR(100)    NOT NULL,
    role            VARCHAR(20)     NOT NULL,   -- 'user' or 'assistant'
    content         TEXT            NOT NULL,
    model           VARCHAR(50)     DEFAULT 'gemini-pro',
    tokens_used     INTEGER,
    create_time     TIMESTAMP       DEFAULT NOW()
);
CREATE INDEX idx_chat_session ON ai_chat_conversation(session_id);
CREATE INDEX idx_chat_member ON ai_chat_conversation(member_id);
COMMENT ON TABLE ai_chat_conversation IS 'Persisted AI chat history for analytics and context';
