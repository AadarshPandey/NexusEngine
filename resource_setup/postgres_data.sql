--
-- PostgreSQL database cluster dump
--

\restrict MK2NqAq6cAGq6jDRAMeeAmVG4Vw3obkgDCddRTyajHQxDVurYX1YqknvbXuA49s

SET default_transaction_read_only = off;

SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;

--
-- Roles
--

CREATE ROLE postgres;
ALTER ROLE postgres WITH SUPERUSER INHERIT CREATEROLE CREATEDB LOGIN REPLICATION BYPASSRLS PASSWORD 'SCRAM-SHA-256$4096:P6fmIV8yvO1ycO6ZiWasRQ==$qYbMIwdrYBDWnAXUxWoVoaVl43CykMi+V2DKreCe7J4=:uBp4WwyQdVu+TSEhoeriKXQUjSFQGxgAift7sP18Zr4=';

--
-- User Configurations
--








\unrestrict MK2NqAq6cAGq6jDRAMeeAmVG4Vw3obkgDCddRTyajHQxDVurYX1YqknvbXuA49s

--
-- Databases
--

--
-- Database "template1" dump
--

\connect template1

--
-- PostgreSQL database dump
--

\restrict 7u2FVydQVs2AZT9AtlI7EKeu771ualI6BFcGTB6zgaHHQEr0tFSfnbnZqieIRRr

-- Dumped from database version 17.11 (Debian 17.11-1.pgdg12+2)
-- Dumped by pg_dump version 17.11 (Debian 17.11-1.pgdg12+2)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- PostgreSQL database dump complete
--

\unrestrict 7u2FVydQVs2AZT9AtlI7EKeu771ualI6BFcGTB6zgaHHQEr0tFSfnbnZqieIRRr

--
-- Database "nexuscore" dump
--

--
-- PostgreSQL database dump
--

\restrict 3N866ErZhZRDDO8oMCygVchKsc2aqnqacdAspyWESIZ5nEEnvD48mfCuDc7Myef

-- Dumped from database version 17.11 (Debian 17.11-1.pgdg12+2)
-- Dumped by pg_dump version 17.11 (Debian 17.11-1.pgdg12+2)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: nexuscore; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE nexuscore WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'en_US.utf8';


ALTER DATABASE nexuscore OWNER TO postgres;

\unrestrict 3N866ErZhZRDDO8oMCygVchKsc2aqnqacdAspyWESIZ5nEEnvD48mfCuDc7Myef
\connect nexuscore
\restrict 3N866ErZhZRDDO8oMCygVchKsc2aqnqacdAspyWESIZ5nEEnvD48mfCuDc7Myef

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: ai; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA ai;


ALTER SCHEMA ai OWNER TO postgres;

--
-- Name: pms; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA pms;


ALTER SCHEMA pms OWNER TO postgres;

--
-- Name: integration; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA integration;


ALTER SCHEMA integration OWNER TO postgres;

--
-- Name: marketing; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA marketing;


ALTER SCHEMA marketing OWNER TO postgres;

--
-- Name: migration; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA migration;


ALTER SCHEMA migration OWNER TO postgres;

--
-- Name: orders; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA orders;


ALTER SCHEMA orders OWNER TO postgres;

--
-- Name: public; Type: SCHEMA; Schema: -; Owner: postgres
--

-- *not* creating schema, since initdb creates it


ALTER SCHEMA public OWNER TO postgres;

--
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA public IS '';


--
-- Name: system; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA system;


ALTER SCHEMA system OWNER TO postgres;

--
-- Name: users; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA users;


ALTER SCHEMA users OWNER TO postgres;

--
-- Name: hstore; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS hstore WITH SCHEMA public;


--
-- Name: EXTENSION hstore; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION hstore IS 'data type for storing sets of (key, value) pairs';


--
-- Name: uuid-ossp; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS "uuid-ossp" WITH SCHEMA public;


--
-- Name: EXTENSION "uuid-ossp"; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION "uuid-ossp" IS 'generate universally unique identifiers (UUIDs)';


--
-- Name: vector; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS vector WITH SCHEMA public;


--
-- Name: EXTENSION vector; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION vector IS 'vector data type and ivfflat and hnsw access methods';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: ai_chat_conversation; Type: TABLE; Schema: ai; Owner: postgres
--

CREATE TABLE ai.ai_chat_conversation (
    id bigint NOT NULL,
    member_id bigint,
    session_id character varying(100) NOT NULL,
    role character varying(20) NOT NULL,
    content text NOT NULL,
    model character varying(50) DEFAULT 'gemini-pro'::character varying,
    tokens_used integer,
    create_time timestamp without time zone DEFAULT now()
);


ALTER TABLE ai.ai_chat_conversation OWNER TO postgres;

--
-- Name: TABLE ai_chat_conversation; Type: COMMENT; Schema: ai; Owner: postgres
--

COMMENT ON TABLE ai.ai_chat_conversation IS 'Persisted AI chat history for analytics and context';


--
-- Name: ai_chat_conversation_id_seq; Type: SEQUENCE; Schema: ai; Owner: postgres
--

CREATE SEQUENCE ai.ai_chat_conversation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE ai.ai_chat_conversation_id_seq OWNER TO postgres;

--
-- Name: ai_chat_conversation_id_seq; Type: SEQUENCE OWNED BY; Schema: ai; Owner: postgres
--

ALTER SEQUENCE ai.ai_chat_conversation_id_seq OWNED BY ai.ai_chat_conversation.id;


--
-- Name: pms_product_embedding; Type: TABLE; Schema: ai; Owner: postgres
--

CREATE TABLE ai.pms_product_embedding (
    product_id bigint NOT NULL,
    embedding public.vector(1536)
);


ALTER TABLE ai.pms_product_embedding OWNER TO postgres;

--
-- Name: vector_store; Type: TABLE; Schema: ai; Owner: postgres
--

CREATE TABLE ai.vector_store (
    id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    content text,
    metadata json,
    embedding public.vector(1536)
);


ALTER TABLE ai.vector_store OWNER TO postgres;

--
-- Name: pms_brand; Type: TABLE; Schema: pms; Owner: postgres
--

CREATE TABLE pms.pms_brand (
    id bigint NOT NULL,
    name character varying(255),
    show_status integer,
    logo character varying(255)
);


ALTER TABLE pms.pms_brand OWNER TO postgres;

--
-- Name: pms_brand_id_seq; Type: SEQUENCE; Schema: pms; Owner: postgres
--

CREATE SEQUENCE pms.pms_brand_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pms.pms_brand_id_seq OWNER TO postgres;

--
-- Name: pms_brand_id_seq; Type: SEQUENCE OWNED BY; Schema: pms; Owner: postgres
--

ALTER SEQUENCE pms.pms_brand_id_seq OWNED BY pms.pms_brand.id;


--
-- Name: pms_comment; Type: TABLE; Schema: pms; Owner: postgres
--

CREATE TABLE pms.pms_comment (
    id bigint NOT NULL,
    product_id bigint,
    member_nick_name character varying(255),
    product_name character varying(255),
    star integer,
    member_ip character varying(255),
    create_time timestamp without time zone,
    show_status integer,
    
    likes_count integer,
    dislike_count integer,
    dislike_count integer,
    pics character varying(255),
    member_icon character varying(255),
    reply_count integer,
    content character varying(255)
);


ALTER TABLE pms.pms_comment OWNER TO postgres;

--
-- Name: pms_comment_id_seq; Type: SEQUENCE; Schema: pms; Owner: postgres
--

CREATE SEQUENCE pms.pms_comment_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pms.pms_comment_id_seq OWNER TO postgres;

--
-- Name: pms_comment_id_seq; Type: SEQUENCE OWNED BY; Schema: pms; Owner: postgres
--

ALTER SEQUENCE pms.pms_comment_id_seq OWNED BY pms.pms_comment.id;


--
-- Name: pms_comment_reply; Type: TABLE; Schema: pms; Owner: postgres
--

CREATE TABLE pms.pms_comment_reply (
    id bigint NOT NULL,
    comment_id bigint,
    member_nick_name character varying(255),
    member_icon character varying(255),
    content character varying(255),
    create_time timestamp without time zone,
    type integer
);


ALTER TABLE pms.pms_comment_reply OWNER TO postgres;

--
-- Name: pms_comment_reply_id_seq; Type: SEQUENCE; Schema: pms; Owner: postgres
--

CREATE SEQUENCE pms.pms_comment_reply_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pms.pms_comment_reply_id_seq OWNER TO postgres;

--
-- Name: pms_comment_reply_id_seq; Type: SEQUENCE OWNED BY; Schema: pms; Owner: postgres
--

ALTER SEQUENCE pms.pms_comment_reply_id_seq OWNED BY pms.pms_comment_reply.id;


--
-- Name: pms_product; Type: TABLE; Schema: pms; Owner: postgres
--

CREATE TABLE pms.pms_product (
    id bigint NOT NULL,
    brand_id bigint,
    product_category_id bigint,
    product_attribute_category_id bigint,
    name character varying(255),
    pic character varying(255),
    product_sn character varying(255),
    delete_status integer,
    publish_status integer,
    new_status integer,
    recommend_status integer,
    verify_status integer,
    sort integer,
    sale integer,
    sub_title character varying(255),
    unit character varying(255),
    weight numeric(19,2),
    service_ids character varying(255),
    keywords character varying(255),
    promotion_start_time timestamp without time zone,
    promotion_end_time timestamp without time zone,
    promotion_per_limit integer,
    promotion_type integer,
    brand_name character varying(255),
    product_category_name character varying(255),
    description text,
    detail_html text,
    vendor_id bigint,
    detail_title character varying(255),
    detail_desc text
);


ALTER TABLE pms.pms_product OWNER TO postgres;

--
-- Name: pms_product_attribute; Type: TABLE; Schema: pms; Owner: postgres
--

CREATE TABLE pms.pms_product_attribute (
    id bigint NOT NULL,
    product_attribute_category_id bigint,
    name character varying(255),
    select_type integer,
    input_type integer,
    input_list character varying(255),
    sort integer,
    filter_type integer,
    search_type integer,
    related_status integer,
    hand_add_status integer,
    type integer
);


ALTER TABLE pms.pms_product_attribute OWNER TO postgres;

--
-- Name: pms_product_attribute_category; Type: TABLE; Schema: pms; Owner: postgres
--

CREATE TABLE pms.pms_product_attribute_category (
    id bigint NOT NULL,
    name character varying(255),
    attribute_count integer,
    param_count integer
);


ALTER TABLE pms.pms_product_attribute_category OWNER TO postgres;

--
-- Name: pms_product_attribute_category_id_seq; Type: SEQUENCE; Schema: pms; Owner: postgres
--

CREATE SEQUENCE pms.pms_product_attribute_category_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pms.pms_product_attribute_category_id_seq OWNER TO postgres;

--
-- Name: pms_product_attribute_category_id_seq; Type: SEQUENCE OWNED BY; Schema: pms; Owner: postgres
--

ALTER SEQUENCE pms.pms_product_attribute_category_id_seq OWNED BY pms.pms_product_attribute_category.id;


--
-- Name: pms_product_attribute_id_seq; Type: SEQUENCE; Schema: pms; Owner: postgres
--

CREATE SEQUENCE pms.pms_product_attribute_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pms.pms_product_attribute_id_seq OWNER TO postgres;

--
-- Name: pms_product_attribute_id_seq; Type: SEQUENCE OWNED BY; Schema: pms; Owner: postgres
--

ALTER SEQUENCE pms.pms_product_attribute_id_seq OWNED BY pms.pms_product_attribute.id;


--
-- Name: pms_product_attribute_value; Type: TABLE; Schema: pms; Owner: postgres
--

CREATE TABLE pms.pms_product_attribute_value (
    id bigint NOT NULL,
    product_id bigint,
    product_attribute_id bigint,
    value character varying(255)
);


ALTER TABLE pms.pms_product_attribute_value OWNER TO postgres;

--
-- Name: pms_product_attribute_value_id_seq; Type: SEQUENCE; Schema: pms; Owner: postgres
--

CREATE SEQUENCE pms.pms_product_attribute_value_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pms.pms_product_attribute_value_id_seq OWNER TO postgres;

--
-- Name: pms_product_attribute_value_id_seq; Type: SEQUENCE OWNED BY; Schema: pms; Owner: postgres
--

ALTER SEQUENCE pms.pms_product_attribute_value_id_seq OWNED BY pms.pms_product_attribute_value.id;


--
-- Name: pms_product_category; Type: TABLE; Schema: pms; Owner: postgres
--

CREATE TABLE pms.pms_product_category (
    id bigint NOT NULL,
    parent_id bigint,
    name character varying(255),
    level integer,
    product_count integer,
    product_unit character varying(255),
    nav_status integer,
    show_status integer,
    sort integer,
    icon character varying(255),
    keywords character varying(255),
    description character varying(255)
);


ALTER TABLE pms.pms_product_category OWNER TO postgres;

--
-- Name: pms_product_category_attribute_relation; Type: TABLE; Schema: pms; Owner: postgres
--

CREATE TABLE pms.pms_product_category_attribute_relation (
    id bigint NOT NULL,
    product_category_id bigint,
    product_attribute_id bigint
);


ALTER TABLE pms.pms_product_category_attribute_relation OWNER TO postgres;

--
-- Name: pms_product_category_attribute_relation_id_seq; Type: SEQUENCE; Schema: pms; Owner: postgres
--

CREATE SEQUENCE pms.pms_product_category_attribute_relation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pms.pms_product_category_attribute_relation_id_seq OWNER TO postgres;

--
-- Name: pms_product_category_attribute_relation_id_seq; Type: SEQUENCE OWNED BY; Schema: pms; Owner: postgres
--

ALTER SEQUENCE pms.pms_product_category_attribute_relation_id_seq OWNED BY pms.pms_product_category_attribute_relation.id;


--
-- Name: pms_product_category_id_seq; Type: SEQUENCE; Schema: pms; Owner: postgres
--

CREATE SEQUENCE pms.pms_product_category_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pms.pms_product_category_id_seq OWNER TO postgres;

--
-- Name: pms_product_category_id_seq; Type: SEQUENCE OWNED BY; Schema: pms; Owner: postgres
--

ALTER SEQUENCE pms.pms_product_category_id_seq OWNED BY pms.pms_product_category.id;


--
-- Name: pms_product_spend_discount; Type: TABLE; Schema: pms; Owner: postgres
--

CREATE TABLE pms.pms_product_spend_discount (
    id bigint NOT NULL,
    product_id bigint,
    full_price numeric(19,2),
    reduce_price numeric(19,2)
);


ALTER TABLE pms.pms_product_spend_discount OWNER TO postgres;

--
-- Name: pms_product_spend_discount_id_seq; Type: SEQUENCE; Schema: pms; Owner: postgres
--

CREATE SEQUENCE pms.pms_product_spend_discount_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pms.pms_product_spend_discount_id_seq OWNER TO postgres;

--
-- Name: pms_product_spend_discount_id_seq; Type: SEQUENCE OWNED BY; Schema: pms; Owner: postgres
--

ALTER SEQUENCE pms.pms_product_spend_discount_id_seq OWNED BY pms.pms_product_spend_discount.id;


--
-- Name: pms_product_id_seq; Type: SEQUENCE; Schema: pms; Owner: postgres
--

CREATE SEQUENCE pms.pms_product_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pms.pms_product_id_seq OWNER TO postgres;

--
-- Name: pms_product_id_seq; Type: SEQUENCE OWNED BY; Schema: pms; Owner: postgres
--

ALTER SEQUENCE pms.pms_product_id_seq OWNED BY pms.pms_product.id;


--
-- Name: pms_product_volume_discount; Type: TABLE; Schema: pms; Owner: postgres
--

CREATE TABLE pms.pms_product_volume_discount (
    id bigint NOT NULL,
    product_id bigint,
    count integer,
    discount numeric(19,2),
    price numeric(19,2)
);


ALTER TABLE pms.pms_product_volume_discount OWNER TO postgres;

--
-- Name: pms_product_volume_discount_id_seq; Type: SEQUENCE; Schema: pms; Owner: postgres
--

CREATE SEQUENCE pms.pms_product_volume_discount_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pms.pms_product_volume_discount_id_seq OWNER TO postgres;

--
-- Name: pms_product_volume_discount_id_seq; Type: SEQUENCE OWNED BY; Schema: pms; Owner: postgres
--

ALTER SEQUENCE pms.pms_product_volume_discount_id_seq OWNED BY pms.pms_product_volume_discount.id;


--
-- Name: pms_product_gallery; Type: TABLE; Schema: pms; Owner: postgres
--

CREATE TABLE pms.pms_product_gallery (
    id bigint NOT NULL,
    product_id bigint NOT NULL,
    media_type character varying(50),
    media_url character varying(255) NOT NULL,
    sort_order integer
);


ALTER TABLE pms.pms_product_gallery OWNER TO postgres;

--
-- Name: pms_product_gallery_id_seq; Type: SEQUENCE; Schema: pms; Owner: postgres
--

CREATE SEQUENCE pms.pms_product_gallery_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pms.pms_product_gallery_id_seq OWNER TO postgres;

--
-- Name: pms_product_gallery_id_seq; Type: SEQUENCE OWNED BY; Schema: pms; Owner: postgres
--

ALTER SEQUENCE pms.pms_product_gallery_id_seq OWNED BY pms.pms_product_gallery.id;


--
-- Name: pms_product_audit_log; Type: TABLE; Schema: pms; Owner: postgres
--

CREATE TABLE pms.pms_product_audit_log (
    id bigint NOT NULL,
    product_id bigint,
    price_old numeric(19,2),
    price_new numeric(19,2),
    sale_price_old numeric(19,2),
    sale_price_new numeric(19,2),
    operate_man character varying(255),
    create_time timestamp without time zone
);


ALTER TABLE pms.pms_product_audit_log OWNER TO postgres;

--
-- Name: pms_product_audit_log_id_seq; Type: SEQUENCE; Schema: pms; Owner: postgres
--

CREATE SEQUENCE pms.pms_product_audit_log_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pms.pms_product_audit_log_id_seq OWNER TO postgres;

--
-- Name: pms_product_audit_log_id_seq; Type: SEQUENCE OWNED BY; Schema: pms; Owner: postgres
--

ALTER SEQUENCE pms.pms_product_audit_log_id_seq OWNED BY pms.pms_product_audit_log.id;


--
-- Name: pms_product_approval_history; Type: TABLE; Schema: pms; Owner: postgres
--

CREATE TABLE pms.pms_product_approval_history (
    id bigint NOT NULL,
    product_id bigint,
    create_time timestamp without time zone,
    reviewer_name character varying(255),
    status integer,
    detail character varying(255)
);


ALTER TABLE pms.pms_product_approval_history OWNER TO postgres;

--
-- Name: pms_product_approval_history_id_seq; Type: SEQUENCE; Schema: pms; Owner: postgres
--

CREATE SEQUENCE pms.pms_product_approval_history_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pms.pms_product_approval_history_id_seq OWNER TO postgres;

--
-- Name: pms_product_approval_history_id_seq; Type: SEQUENCE OWNED BY; Schema: pms; Owner: postgres
--

ALTER SEQUENCE pms.pms_product_approval_history_id_seq OWNED BY pms.pms_product_approval_history.id;


--
-- Name: pms_review; Type: TABLE; Schema: pms; Owner: postgres
--

CREATE TABLE pms.pms_review (
    id bigint NOT NULL,
    product_id bigint,
    member_id bigint,
    parent_id bigint,
    rating integer,
    content character varying(255),
    like_count integer,
    dislike_count integer,
    status integer,
    created_time timestamp without time zone
);


ALTER TABLE pms.pms_review OWNER TO postgres;

--
-- Name: pms_review_id_seq; Type: SEQUENCE; Schema: pms; Owner: postgres
--

CREATE SEQUENCE pms.pms_review_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pms.pms_review_id_seq OWNER TO postgres;

--
-- Name: pms_review_id_seq; Type: SEQUENCE OWNED BY; Schema: pms; Owner: postgres
--

ALTER SEQUENCE pms.pms_review_id_seq OWNED BY pms.pms_review.id;


--
-- Name: pms_review_attachments; Type: TABLE; Schema: pms; Owner: postgres
--

CREATE TABLE pms.pms_review_attachments (
    id bigint NOT NULL,
    review_id bigint,
    media_type character varying(255),
    media_url character varying(255),
    thumbnail_url character varying(255),
    sort_order integer
);


ALTER TABLE pms.pms_review_attachments OWNER TO postgres;

--
-- Name: pms_review_attachments_id_seq; Type: SEQUENCE; Schema: pms; Owner: postgres
--

CREATE SEQUENCE pms.pms_review_attachments_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pms.pms_review_attachments_id_seq OWNER TO postgres;

--
-- Name: pms_review_attachments_id_seq; Type: SEQUENCE OWNED BY; Schema: pms; Owner: postgres
--

ALTER SEQUENCE pms.pms_review_attachments_id_seq OWNED BY pms.pms_review_attachments.id;


--
-- Name: pms_sku_stock; Type: TABLE; Schema: pms; Owner: postgres
--

CREATE TABLE pms.pms_sku_stock (
    id bigint NOT NULL,
    product_id bigint,
    sku_code character varying(255),
    price numeric(19,2),
    stock integer,
    low_stock integer,
    pic character varying(255),
    sale integer,
    promotion_price numeric(19,2),
    lock_stock integer,
    sku_attributes jsonb
);


ALTER TABLE pms.pms_sku_stock OWNER TO postgres;

--
-- Name: pms_sku_stock_id_seq; Type: SEQUENCE; Schema: pms; Owner: postgres
--

CREATE SEQUENCE pms.pms_sku_stock_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE pms.pms_sku_stock_id_seq OWNER TO postgres;

--
-- Name: pms_sku_stock_id_seq; Type: SEQUENCE OWNED BY; Schema: pms; Owner: postgres
--

ALTER SEQUENCE pms.pms_sku_stock_id_seq OWNED BY pms.pms_sku_stock.id;


--
-- Name: outbox_event; Type: TABLE; Schema: integration; Owner: postgres
--

CREATE TABLE integration.outbox_event (
    id bigint NOT NULL,
    aggregate_type character varying(255) NOT NULL,
    aggregate_id character varying(255) NOT NULL,
    type character varying(255) NOT NULL,
    payload text,
    status character varying(255) NOT NULL,
    retry_count integer DEFAULT 0,
    created_at timestamp without time zone
);


ALTER TABLE integration.outbox_event OWNER TO postgres;

--
-- Name: outbox_event_id_seq; Type: SEQUENCE; Schema: integration; Owner: postgres
--

CREATE SEQUENCE integration.outbox_event_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE integration.outbox_event_id_seq OWNER TO postgres;

--
-- Name: outbox_event_id_seq; Type: SEQUENCE OWNED BY; Schema: integration; Owner: postgres
--

ALTER SEQUENCE integration.outbox_event_id_seq OWNED BY integration.outbox_event.id;


--
-- Name: sms_coupon; Type: TABLE; Schema: marketing; Owner: postgres
--

CREATE TABLE marketing.sms_coupon (
    id bigint NOT NULL,
    type integer,
    name character varying(255),
    count integer,
    amount numeric(19,2),
    per_limit integer,
    min_order_amount numeric(19,2),
    start_time timestamp without time zone,
    end_time timestamp without time zone,
    use_type integer,
    receive_count integer,
    code character varying(255),
    max_discount_amount numeric(10,2) DEFAULT NULL::numeric,
    version integer DEFAULT 0,
    stackable boolean DEFAULT false,
    is_public boolean DEFAULT false
);


ALTER TABLE marketing.sms_coupon OWNER TO postgres;

--
-- Name: COLUMN sms_coupon.max_discount_amount; Type: COMMENT; Schema: marketing; Owner: postgres
--

COMMENT ON COLUMN marketing.sms_coupon.max_discount_amount IS 'Maximum discount for percentage coupons (cap)';


--
-- Name: COLUMN sms_coupon.version; Type: COMMENT; Schema: marketing; Owner: postgres
--

COMMENT ON COLUMN marketing.sms_coupon.version IS 'Optimistic lock version for stock management';


--
-- Name: COLUMN sms_coupon.stackable; Type: COMMENT; Schema: marketing; Owner: postgres
--

COMMENT ON COLUMN marketing.sms_coupon.stackable IS 'Whether this coupon can be combined with other coupons';


--
-- Name: COLUMN sms_coupon.is_public; Type: COMMENT; Schema: marketing; Owner: postgres
--

COMMENT ON COLUMN marketing.sms_coupon.is_public IS 'Whether coupon can be claimed by entering a code';


--
--




--
--



--
--



--
--

    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



--
--



--
-- Name: sms_coupon_history; Type: TABLE; Schema: marketing; Owner: postgres
--

CREATE TABLE marketing.sms_coupon_history (
    id bigint NOT NULL,
    coupon_id bigint,
    member_id bigint,
    coupon_code character varying(255),
    member_nickname character varying(255),
    get_type integer,
    create_time timestamp without time zone,
    use_status integer,
    use_time timestamp without time zone,
    order_id bigint,
    order_sn character varying(255)
);


ALTER TABLE marketing.sms_coupon_history OWNER TO postgres;

--
-- Name: sms_coupon_history_id_seq; Type: SEQUENCE; Schema: marketing; Owner: postgres
--

CREATE SEQUENCE marketing.sms_coupon_history_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE marketing.sms_coupon_history_id_seq OWNER TO postgres;

--
-- Name: sms_coupon_history_id_seq; Type: SEQUENCE OWNED BY; Schema: marketing; Owner: postgres
--

ALTER SEQUENCE marketing.sms_coupon_history_id_seq OWNED BY marketing.sms_coupon_history.id;


--
-- Name: sms_coupon_id_seq; Type: SEQUENCE; Schema: marketing; Owner: postgres
--

CREATE SEQUENCE marketing.sms_coupon_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE marketing.sms_coupon_id_seq OWNER TO postgres;

--
-- Name: sms_coupon_id_seq; Type: SEQUENCE OWNED BY; Schema: marketing; Owner: postgres
--

ALTER SEQUENCE marketing.sms_coupon_id_seq OWNED BY marketing.sms_coupon.id;


--
-- Name: sms_coupon_product_category_relation; Type: TABLE; Schema: marketing; Owner: postgres
--

CREATE TABLE marketing.sms_coupon_product_category_relation (
    id bigint NOT NULL,
    coupon_id bigint,
    product_category_id bigint,
    product_category_name character varying(255),
    parent_category_name character varying(255)
);


ALTER TABLE marketing.sms_coupon_product_category_relation OWNER TO postgres;

--
-- Name: sms_coupon_product_category_relation_id_seq; Type: SEQUENCE; Schema: marketing; Owner: postgres
--

CREATE SEQUENCE marketing.sms_coupon_product_category_relation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE marketing.sms_coupon_product_category_relation_id_seq OWNER TO postgres;

--
-- Name: sms_coupon_product_category_relation_id_seq; Type: SEQUENCE OWNED BY; Schema: marketing; Owner: postgres
--

ALTER SEQUENCE marketing.sms_coupon_product_category_relation_id_seq OWNED BY marketing.sms_coupon_product_category_relation.id;


--
-- Name: sms_coupon_product_relation; Type: TABLE; Schema: marketing; Owner: postgres
--

CREATE TABLE marketing.sms_coupon_product_relation (
    id bigint NOT NULL,
    coupon_id bigint,
    product_id bigint,
    product_name character varying(255),
    product_sn character varying(255)
);


ALTER TABLE marketing.sms_coupon_product_relation OWNER TO postgres;

--
-- Name: sms_coupon_product_relation_id_seq; Type: SEQUENCE; Schema: marketing; Owner: postgres
--

CREATE SEQUENCE marketing.sms_coupon_product_relation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE marketing.sms_coupon_product_relation_id_seq OWNER TO postgres;

--
-- Name: sms_coupon_product_relation_id_seq; Type: SEQUENCE OWNED BY; Schema: marketing; Owner: postgres
--

ALTER SEQUENCE marketing.sms_coupon_product_relation_id_seq OWNED BY marketing.sms_coupon_product_relation.id;


--
-- Name: sms_flash_promotion; Type: TABLE; Schema: marketing; Owner: postgres
--

CREATE TABLE marketing.sms_flash_promotion (
    id bigint NOT NULL,
    title character varying(200) NOT NULL,
    start_date date NOT NULL,
    end_date date NOT NULL,
    status integer DEFAULT 0,
    create_time timestamp without time zone DEFAULT now()
);


ALTER TABLE marketing.sms_flash_promotion OWNER TO postgres;

--
-- Name: TABLE sms_flash_promotion; Type: COMMENT; Schema: marketing; Owner: postgres
--

COMMENT ON TABLE marketing.sms_flash_promotion IS 'Flash promotion event definition';


--
-- Name: sms_flash_promotion_id_seq; Type: SEQUENCE; Schema: marketing; Owner: postgres
--

CREATE SEQUENCE marketing.sms_flash_promotion_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE marketing.sms_flash_promotion_id_seq OWNER TO postgres;

--
-- Name: sms_flash_promotion_id_seq; Type: SEQUENCE OWNED BY; Schema: marketing; Owner: postgres
--

ALTER SEQUENCE marketing.sms_flash_promotion_id_seq OWNED BY marketing.sms_flash_promotion.id;


--
-- Name: sms_flash_promotion_log; Type: TABLE; Schema: marketing; Owner: postgres
--

CREATE TABLE marketing.sms_flash_promotion_log (
    id bigint NOT NULL,
    member_id bigint NOT NULL,
    product_id bigint NOT NULL,
    flash_promotion_id bigint,
    session_id bigint,
    order_id bigint,
    quantity integer DEFAULT 1,
    status character varying(50) DEFAULT 'PENDING'::character varying,
    create_time timestamp without time zone DEFAULT now()
);


ALTER TABLE marketing.sms_flash_promotion_log OWNER TO postgres;

--
-- Name: sms_flash_promotion_log_id_seq; Type: SEQUENCE; Schema: marketing; Owner: postgres
--

CREATE SEQUENCE marketing.sms_flash_promotion_log_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE marketing.sms_flash_promotion_log_id_seq OWNER TO postgres;

--
-- Name: sms_flash_promotion_log_id_seq; Type: SEQUENCE OWNED BY; Schema: marketing; Owner: postgres
--

ALTER SEQUENCE marketing.sms_flash_promotion_log_id_seq OWNED BY marketing.sms_flash_promotion_log.id;


--
-- Name: sms_flash_promotion_product_relation; Type: TABLE; Schema: marketing; Owner: postgres
--

CREATE TABLE marketing.sms_flash_promotion_product_relation (
    id bigint NOT NULL,
    flash_promotion_id bigint NOT NULL,
    flash_promotion_session_id bigint NOT NULL,
    product_id bigint NOT NULL,
    flash_promotion_price numeric(10,2) NOT NULL,
    flash_promotion_count integer NOT NULL,
    flash_promotion_limit integer DEFAULT 1,
    sort integer DEFAULT 0
);


ALTER TABLE marketing.sms_flash_promotion_product_relation OWNER TO postgres;

--
-- Name: TABLE sms_flash_promotion_product_relation; Type: COMMENT; Schema: marketing; Owner: postgres
--

COMMENT ON TABLE marketing.sms_flash_promotion_product_relation IS 'Products in a specific flash session with flash pricing';


--
-- Name: sms_flash_promotion_product_relation_id_seq; Type: SEQUENCE; Schema: marketing; Owner: postgres
--

CREATE SEQUENCE marketing.sms_flash_promotion_product_relation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE marketing.sms_flash_promotion_product_relation_id_seq OWNER TO postgres;

--
-- Name: sms_flash_promotion_product_relation_id_seq; Type: SEQUENCE OWNED BY; Schema: marketing; Owner: postgres
--

ALTER SEQUENCE marketing.sms_flash_promotion_product_relation_id_seq OWNED BY marketing.sms_flash_promotion_product_relation.id;


--
-- Name: sms_flash_promotion_session; Type: TABLE; Schema: marketing; Owner: postgres
--

CREATE TABLE marketing.sms_flash_promotion_session (
    id bigint NOT NULL,
    name character varying(200) NOT NULL,
    start_time time without time zone NOT NULL,
    end_time time without time zone NOT NULL,
    status integer DEFAULT 0,
    create_time timestamp without time zone DEFAULT now()
);


ALTER TABLE marketing.sms_flash_promotion_session OWNER TO postgres;

--
-- Name: TABLE sms_flash_promotion_session; Type: COMMENT; Schema: marketing; Owner: postgres
--

COMMENT ON TABLE marketing.sms_flash_promotion_session IS 'Time slots within a flash promotion day';


--
-- Name: sms_flash_promotion_session_id_seq; Type: SEQUENCE; Schema: marketing; Owner: postgres
--

CREATE SEQUENCE marketing.sms_flash_promotion_session_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE marketing.sms_flash_promotion_session_id_seq OWNER TO postgres;

--
-- Name: sms_flash_promotion_session_id_seq; Type: SEQUENCE OWNED BY; Schema: marketing; Owner: postgres
--

ALTER SEQUENCE marketing.sms_flash_promotion_session_id_seq OWNED BY marketing.sms_flash_promotion_session.id;


--
-- Name: flyway_schema_history; Type: TABLE; Schema: migration; Owner: postgres
--

CREATE TABLE migration.flyway_schema_history (
    installed_rank integer NOT NULL,
    version character varying(50),
    description character varying(200) NOT NULL,
    type character varying(20) NOT NULL,
    script character varying(1000) NOT NULL,
    checksum integer,
    installed_by character varying(100) NOT NULL,
    installed_on timestamp without time zone DEFAULT now() NOT NULL,
    execution_time integer NOT NULL,
    success boolean NOT NULL
);


ALTER TABLE migration.flyway_schema_history OWNER TO postgres;

--
-- Name: oms_cart_item; Type: TABLE; Schema: orders; Owner: postgres
--

CREATE TABLE orders.oms_cart_item (
    id bigint NOT NULL,
    product_id bigint,
    product_sku_id bigint,
    member_id bigint,
    quantity integer,
    price numeric(19,2),
    product_pic character varying(255),
    product_name character varying(255),
    product_sub_title character varying(255),
    product_sku_code character varying(255),
    member_nickname character varying(255),
    create_date timestamp without time zone,
    modify_date timestamp without time zone,
    delete_status integer,
    product_category_id bigint,
    product_brand character varying(255),
    product_sn character varying(255),
    product_attr character varying(255)
);


ALTER TABLE orders.oms_cart_item OWNER TO postgres;

--
-- Name: oms_cart_item_id_seq; Type: SEQUENCE; Schema: orders; Owner: postgres
--

CREATE SEQUENCE orders.oms_cart_item_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE orders.oms_cart_item_id_seq OWNER TO postgres;

--
-- Name: oms_cart_item_id_seq; Type: SEQUENCE OWNED BY; Schema: orders; Owner: postgres
--

ALTER SEQUENCE orders.oms_cart_item_id_seq OWNED BY orders.oms_cart_item.id;


--
-- Name: oms_company_address; Type: TABLE; Schema: orders; Owner: postgres
--

CREATE TABLE orders.oms_company_address (
    id bigint NOT NULL,
    address_name character varying(255),
    send_status integer,
    receive_status integer,
    name character varying(255),
    phone character varying(255),
    province character varying(255),
    city character varying(255),
    region character varying(255),
    detail_address character varying(255)
);


ALTER TABLE orders.oms_company_address OWNER TO postgres;

--
-- Name: oms_company_address_id_seq; Type: SEQUENCE; Schema: orders; Owner: postgres
--

CREATE SEQUENCE orders.oms_company_address_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE orders.oms_company_address_id_seq OWNER TO postgres;

--
-- Name: oms_company_address_id_seq; Type: SEQUENCE OWNED BY; Schema: orders; Owner: postgres
--

ALTER SEQUENCE orders.oms_company_address_id_seq OWNED BY orders.oms_company_address.id;


--
-- Name: oms_order; Type: TABLE; Schema: orders; Owner: postgres
--

CREATE TABLE orders.oms_order (
    id bigint NOT NULL,
    member_id bigint,
    coupon_id bigint,
    order_sn character varying(255),
    create_time timestamp without time zone,
    member_username character varying(255),
    total_amount numeric(19,2),
    pay_amount numeric(19,2),
    freight_amount numeric(19,2),
    promotion_amount numeric(19,2),
    points_discount_amount numeric(19,2),
    coupon_amount numeric(19,2),
    discount_amount numeric(19,2),
    pay_type integer,
    source_type integer,
    status integer,
    order_type integer,
    delivery_company character varying(255),
    delivery_sn character varying(255),
    auto_confirm_day integer,
    earned_points integer,
    promotion_info character varying(255),
    bill_type integer,
    bill_header character varying(255),
    bill_content character varying(255),
    bill_receiver_phone character varying(255),
    bill_receiver_email character varying(255),
    receiver_name character varying(255),
    receiver_phone character varying(255),
    receiver_post_code character varying(255),
    receiver_province character varying(255),
    receiver_city character varying(255),
    receiver_region character varying(255),
    receiver_detail_address character varying(255),
    confirm_status integer,
    delete_status integer,
    used_points integer,
    payment_id character varying(255),
    payment_time timestamp without time zone,
    delivery_time timestamp without time zone,
    receive_time timestamp without time zone,
    comment_time timestamp without time zone,
    modify_time timestamp without time zone,
    vendor_id bigint
);


ALTER TABLE orders.oms_order OWNER TO postgres;

--
-- Name: oms_order_id_seq; Type: SEQUENCE; Schema: orders; Owner: postgres
--

CREATE SEQUENCE orders.oms_order_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE orders.oms_order_id_seq OWNER TO postgres;

--
-- Name: oms_order_id_seq; Type: SEQUENCE OWNED BY; Schema: orders; Owner: postgres
--

ALTER SEQUENCE orders.oms_order_id_seq OWNED BY orders.oms_order.id;


--
-- Name: oms_order_item; Type: TABLE; Schema: orders; Owner: postgres
--

CREATE TABLE orders.oms_order_item (
    id bigint NOT NULL,
    order_id bigint,
    order_sn character varying(255),
    product_id bigint,
    product_pic character varying(255),
    product_name character varying(255),
    product_brand character varying(255),
    product_sn character varying(255),
    product_price numeric(19,2),
    product_quantity integer,
    product_sku_id bigint,
    product_sku_code character varying(255),
    product_category_id bigint,
    promotion_name character varying(255),
    promotion_amount numeric(19,2),
    coupon_amount numeric(19,2),
    points_discount_amount numeric(19,2),
    real_amount numeric(19,2),
    earned_points integer,
    product_attr character varying(255)
);


ALTER TABLE orders.oms_order_item OWNER TO postgres;

--
-- Name: oms_order_item_id_seq; Type: SEQUENCE; Schema: orders; Owner: postgres
--

CREATE SEQUENCE orders.oms_order_item_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE orders.oms_order_item_id_seq OWNER TO postgres;

--
-- Name: oms_order_item_id_seq; Type: SEQUENCE OWNED BY; Schema: orders; Owner: postgres
--

ALTER SEQUENCE orders.oms_order_item_id_seq OWNED BY orders.oms_order_item.id;


--
-- Name: oms_order_operate_history; Type: TABLE; Schema: orders; Owner: postgres
--

CREATE TABLE orders.oms_order_operate_history (
    id bigint NOT NULL,
    order_id bigint,
    operate_man character varying(255),
    create_time timestamp without time zone,
    order_status integer,
);


ALTER TABLE orders.oms_order_operate_history OWNER TO postgres;

--
-- Name: oms_order_operate_history_id_seq; Type: SEQUENCE; Schema: orders; Owner: postgres
--

CREATE SEQUENCE orders.oms_order_operate_history_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE orders.oms_order_operate_history_id_seq OWNER TO postgres;

--
-- Name: oms_order_operate_history_id_seq; Type: SEQUENCE OWNED BY; Schema: orders; Owner: postgres
--

ALTER SEQUENCE orders.oms_order_operate_history_id_seq OWNED BY orders.oms_order_operate_history.id;


--
-- Name: oms_order_return_apply; Type: TABLE; Schema: orders; Owner: postgres
--

CREATE TABLE orders.oms_order_return_apply (
    id bigint NOT NULL,
    order_id bigint,
    company_address_id bigint,
    product_id bigint,
    order_sn character varying(255),
    create_time timestamp without time zone,
    member_username character varying(255),
    return_amount numeric(19,2),
    return_name character varying(255),
    return_phone character varying(255),
    status integer,
    handle_time timestamp without time zone,
    product_pic character varying(255),
    product_name character varying(255),
    product_brand character varying(255),
    product_attr character varying(255),
    product_count integer,
    product_price numeric(19,2),
    product_real_price numeric(19,2),
    reason character varying(255),
    description character varying(255),
    proof_pics character varying(255),
    handle_man character varying(255),
    receive_man character varying(255),
    receive_time timestamp without time zone,
);


ALTER TABLE orders.oms_order_return_apply OWNER TO postgres;

--
-- Name: oms_order_return_apply_id_seq; Type: SEQUENCE; Schema: orders; Owner: postgres
--

CREATE SEQUENCE orders.oms_order_return_apply_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE orders.oms_order_return_apply_id_seq OWNER TO postgres;

--
-- Name: oms_order_return_apply_id_seq; Type: SEQUENCE OWNED BY; Schema: orders; Owner: postgres
--

ALTER SEQUENCE orders.oms_order_return_apply_id_seq OWNED BY orders.oms_order_return_apply.id;


--
-- Name: oms_order_return_reason; Type: TABLE; Schema: orders; Owner: postgres
--

CREATE TABLE orders.oms_order_return_reason (
    id bigint NOT NULL,
    name character varying(255),
    sort integer,
    status integer,
    create_time timestamp without time zone
);


ALTER TABLE orders.oms_order_return_reason OWNER TO postgres;

--
-- Name: oms_order_return_reason_id_seq; Type: SEQUENCE; Schema: orders; Owner: postgres
--

CREATE SEQUENCE orders.oms_order_return_reason_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE orders.oms_order_return_reason_id_seq OWNER TO postgres;

--
-- Name: oms_order_return_reason_id_seq; Type: SEQUENCE OWNED BY; Schema: orders; Owner: postgres
--

ALTER SEQUENCE orders.oms_order_return_reason_id_seq OWNED BY orders.oms_order_return_reason.id;


--
-- Name: oms_order_setting; Type: TABLE; Schema: orders; Owner: postgres
--

CREATE TABLE orders.oms_order_setting (
    id bigint NOT NULL,
    flash_order_overtime integer,
    normal_order_overtime integer,
    confirm_overtime integer,
    finish_overtime integer,
    comment_overtime integer
);


ALTER TABLE orders.oms_order_setting OWNER TO postgres;

--
-- Name: oms_order_setting_id_seq; Type: SEQUENCE; Schema: orders; Owner: postgres
--

CREATE SEQUENCE orders.oms_order_setting_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE orders.oms_order_setting_id_seq OWNER TO postgres;

--
-- Name: oms_order_setting_id_seq; Type: SEQUENCE OWNED BY; Schema: orders; Owner: postgres
--

ALTER SEQUENCE orders.oms_order_setting_id_seq OWNED BY orders.oms_order_setting.id;


--
-- Name: oms_payment_transaction; Type: TABLE; Schema: orders; Owner: postgres
--

CREATE TABLE orders.oms_payment_transaction (
    id bigint NOT NULL,
    order_id bigint NOT NULL,
    transaction_id character varying(255) NOT NULL,
    transaction_type character varying(50) NOT NULL,
    amount numeric(38,2) NOT NULL,
    status character varying(50) NOT NULL,
    webhook_payload jsonb,
    create_time timestamp without time zone DEFAULT now()
);


ALTER TABLE orders.oms_payment_transaction OWNER TO postgres;

--
-- Name: oms_payment_transaction_id_seq; Type: SEQUENCE; Schema: orders; Owner: postgres
--

ALTER TABLE orders.oms_payment_transaction ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME orders.oms_payment_transaction_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: ai_chat_conversation; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ai_chat_conversation (
    id bigint NOT NULL,
    member_id bigint,
    session_id character varying(100) NOT NULL,
    role character varying(20) NOT NULL,
    content text NOT NULL,
    model character varying(50) DEFAULT 'gemini-pro'::character varying,
    tokens_used integer,
    create_time timestamp without time zone DEFAULT now()
);


ALTER TABLE public.ai_chat_conversation OWNER TO postgres;

--
-- Name: TABLE ai_chat_conversation; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.ai_chat_conversation IS 'Persisted AI chat history for analytics and context';


--
-- Name: ai_chat_conversation_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.ai_chat_conversation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.ai_chat_conversation_id_seq OWNER TO postgres;

--
-- Name: ai_chat_conversation_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.ai_chat_conversation_id_seq OWNED BY public.ai_chat_conversation.id;


--
-- Name: flyway_schema_history; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.flyway_schema_history (
    installed_rank integer NOT NULL,
    version character varying(50),
    description character varying(200) NOT NULL,
    type character varying(20) NOT NULL,
    script character varying(1000) NOT NULL,
    checksum integer,
    installed_by character varying(100) NOT NULL,
    installed_on timestamp without time zone DEFAULT now() NOT NULL,
    execution_time integer NOT NULL,
    success boolean NOT NULL
);


ALTER TABLE public.flyway_schema_history OWNER TO postgres;

--
-- Name: oms_cart_item; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.oms_cart_item (
    id bigint NOT NULL,
    product_id bigint,
    product_sku_id bigint,
    member_id bigint,
    quantity integer,
    price numeric(19,2),
    product_pic character varying(255),
    product_name character varying(255),
    product_sub_title character varying(255),
    product_sku_code character varying(255),
    member_nickname character varying(255),
    create_date timestamp without time zone,
    modify_date timestamp without time zone,
    delete_status integer,
    product_category_id bigint,
    product_brand character varying(255),
    product_sn character varying(255),
    product_attr character varying(255)
);


ALTER TABLE public.oms_cart_item OWNER TO postgres;

--
-- Name: oms_cart_item_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.oms_cart_item_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.oms_cart_item_id_seq OWNER TO postgres;

--
-- Name: oms_cart_item_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.oms_cart_item_id_seq OWNED BY public.oms_cart_item.id;


--
-- Name: oms_company_address; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.oms_company_address (
    id bigint NOT NULL,
    address_name character varying(255),
    send_status integer,
    receive_status integer,
    name character varying(255),
    phone character varying(255),
    province character varying(255),
    city character varying(255),
    region character varying(255),
    detail_address character varying(255)
);


ALTER TABLE public.oms_company_address OWNER TO postgres;

--
-- Name: oms_company_address_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.oms_company_address_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.oms_company_address_id_seq OWNER TO postgres;

--
-- Name: oms_company_address_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.oms_company_address_id_seq OWNED BY public.oms_company_address.id;


--
-- Name: oms_order; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.oms_order (
    id bigint NOT NULL,
    member_id bigint,
    coupon_id bigint,
    order_sn character varying(255),
    create_time timestamp without time zone,
    member_username character varying(255),
    total_amount numeric(19,2),
    pay_amount numeric(19,2),
    freight_amount numeric(19,2),
    promotion_amount numeric(19,2),
    points_discount_amount numeric(19,2),
    coupon_amount numeric(19,2),
    discount_amount numeric(19,2),
    pay_type integer,
    source_type integer,
    status integer,
    order_type integer,
    delivery_company character varying(255),
    delivery_sn character varying(255),
    auto_confirm_day integer,
    earned_points integer,
    promotion_info character varying(255),
    bill_type integer,
    bill_header character varying(255),
    bill_content character varying(255),
    bill_receiver_phone character varying(255),
    bill_receiver_email character varying(255),
    receiver_name character varying(255),
    receiver_phone character varying(255),
    receiver_post_code character varying(255),
    receiver_province character varying(255),
    receiver_city character varying(255),
    receiver_region character varying(255),
    receiver_detail_address character varying(255),
    confirm_status integer,
    delete_status integer,
    used_points integer,
    payment_id character varying(255),
    payment_time timestamp without time zone,
    delivery_time timestamp without time zone,
    receive_time timestamp without time zone,
    comment_time timestamp without time zone,
    modify_time timestamp without time zone,
    vendor_id bigint
);


ALTER TABLE public.oms_order OWNER TO postgres;

--
-- Name: oms_order_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.oms_order_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.oms_order_id_seq OWNER TO postgres;

--
-- Name: oms_order_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.oms_order_id_seq OWNED BY public.oms_order.id;


--
-- Name: oms_order_item; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.oms_order_item (
    id bigint NOT NULL,
    order_id bigint,
    order_sn character varying(255),
    product_id bigint,
    product_pic character varying(255),
    product_name character varying(255),
    product_brand character varying(255),
    product_sn character varying(255),
    product_price numeric(19,2),
    product_quantity integer,
    product_sku_id bigint,
    product_sku_code character varying(255),
    product_category_id bigint,
    promotion_name character varying(255),
    promotion_amount numeric(19,2),
    coupon_amount numeric(19,2),
    points_discount_amount numeric(19,2),
    real_amount numeric(19,2),
    earned_points integer,
    product_attr character varying(255)
);


ALTER TABLE public.oms_order_item OWNER TO postgres;

--
-- Name: oms_order_item_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.oms_order_item_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.oms_order_item_id_seq OWNER TO postgres;

--
-- Name: oms_order_item_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.oms_order_item_id_seq OWNED BY public.oms_order_item.id;


--
-- Name: oms_order_operate_history; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.oms_order_operate_history (
    id bigint NOT NULL,
    order_id bigint,
    operate_man character varying(255),
    create_time timestamp without time zone,
    order_status integer,
);


ALTER TABLE public.oms_order_operate_history OWNER TO postgres;

--
-- Name: oms_order_operate_history_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.oms_order_operate_history_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.oms_order_operate_history_id_seq OWNER TO postgres;

--
-- Name: oms_order_operate_history_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.oms_order_operate_history_id_seq OWNED BY public.oms_order_operate_history.id;


--
-- Name: oms_order_return_apply; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.oms_order_return_apply (
    id bigint NOT NULL,
    order_id bigint,
    company_address_id bigint,
    product_id bigint,
    order_sn character varying(255),
    create_time timestamp without time zone,
    member_username character varying(255),
    return_amount numeric(19,2),
    return_name character varying(255),
    return_phone character varying(255),
    status integer,
    handle_time timestamp without time zone,
    product_pic character varying(255),
    product_name character varying(255),
    product_brand character varying(255),
    product_attr character varying(255),
    product_count integer,
    product_price numeric(19,2),
    product_real_price numeric(19,2),
    reason character varying(255),
    description character varying(255),
    proof_pics character varying(255),
    handle_man character varying(255),
    receive_man character varying(255),
    receive_time timestamp without time zone,
);


ALTER TABLE public.oms_order_return_apply OWNER TO postgres;

--
-- Name: oms_order_return_apply_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.oms_order_return_apply_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.oms_order_return_apply_id_seq OWNER TO postgres;

--
-- Name: oms_order_return_apply_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.oms_order_return_apply_id_seq OWNED BY public.oms_order_return_apply.id;


--
-- Name: oms_order_return_reason; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.oms_order_return_reason (
    id bigint NOT NULL,
    name character varying(255),
    sort integer,
    status integer,
    create_time timestamp without time zone
);


ALTER TABLE public.oms_order_return_reason OWNER TO postgres;

--
-- Name: oms_order_return_reason_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.oms_order_return_reason_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.oms_order_return_reason_id_seq OWNER TO postgres;

--
-- Name: oms_order_return_reason_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.oms_order_return_reason_id_seq OWNED BY public.oms_order_return_reason.id;


--
-- Name: oms_order_setting; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.oms_order_setting (
    id bigint NOT NULL,
    flash_order_overtime integer,
    normal_order_overtime integer,
    confirm_overtime integer,
    finish_overtime integer,
    comment_overtime integer
);


ALTER TABLE public.oms_order_setting OWNER TO postgres;

--
-- Name: oms_order_setting_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.oms_order_setting_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.oms_order_setting_id_seq OWNER TO postgres;

--
-- Name: oms_order_setting_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.oms_order_setting_id_seq OWNED BY public.oms_order_setting.id;


--
-- Name: oms_payment_transaction; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.oms_payment_transaction (
    id bigint NOT NULL,
    order_id bigint NOT NULL,
    transaction_id character varying(255) NOT NULL,
    transaction_type character varying(50) NOT NULL,
    amount numeric(38,2) NOT NULL,
    status character varying(50) NOT NULL,
    webhook_payload jsonb,
    create_time timestamp without time zone DEFAULT now()
);


ALTER TABLE public.oms_payment_transaction OWNER TO postgres;

--
-- Name: oms_payment_transaction_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.oms_payment_transaction ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.oms_payment_transaction_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: outbox_event; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.outbox_event (
    id bigint NOT NULL,
    aggregate_type character varying(255) NOT NULL,
    aggregate_id character varying(255) NOT NULL,
    type character varying(255) NOT NULL,
    payload text,
    status character varying(255) NOT NULL,
    retry_count integer DEFAULT 0,
    created_at timestamp without time zone
);


ALTER TABLE public.outbox_event OWNER TO postgres;

--
-- Name: outbox_event_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.outbox_event_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.outbox_event_id_seq OWNER TO postgres;

--
-- Name: outbox_event_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.outbox_event_id_seq OWNED BY public.outbox_event.id;


--
-- Name: pms_brand; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pms_brand (
    id bigint NOT NULL,
    name character varying(255),
    show_status integer,
    logo character varying(255)
);


ALTER TABLE public.pms_brand OWNER TO postgres;

--
-- Name: pms_brand_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.pms_brand_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.pms_brand_id_seq OWNER TO postgres;

--
-- Name: pms_brand_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.pms_brand_id_seq OWNED BY public.pms_brand.id;


--
-- Name: pms_comment; Type: TABLE; Schema: public; Owner: postgres
--




--
-- Name: pms_comment_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--



--
-- Name: pms_comment_reply; Type: TABLE; Schema: public; Owner: postgres
--




--
-- Name: pms_comment_reply_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--



--
-- Name: pms_product; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pms_product (
    id bigint NOT NULL,
    brand_id bigint,
    product_category_id bigint,
    product_attribute_category_id bigint,
    name character varying(255),
    pic character varying(255),
    product_sn character varying(255),
    delete_status integer,
    publish_status integer,
    new_status integer,
    recommend_status integer,
    verify_status integer,
    sale integer,2),
    keywords character varying(255),
    brand_name character varying(255),
    description text,
    vendor_id bigint
);


ALTER TABLE public.pms_product OWNER TO postgres;

--
-- Name: pms_product_attribute; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pms_product_attribute (
    id bigint NOT NULL,
    product_attribute_category_id bigint,
    name character varying(255),
    select_type integer,
    input_type integer,
    input_list character varying(255),
    sort integer,
    type integer
);


ALTER TABLE public.pms_product_attribute OWNER TO postgres;

--
-- Name: pms_product_attribute_category; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pms_product_attribute_category (
    id bigint NOT NULL,
    name character varying(255),
    attribute_count integer,
    param_count integer
);


ALTER TABLE public.pms_product_attribute_category OWNER TO postgres;

--
-- Name: pms_product_attribute_category_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.pms_product_attribute_category_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.pms_product_attribute_category_id_seq OWNER TO postgres;

--
-- Name: pms_product_attribute_category_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.pms_product_attribute_category_id_seq OWNED BY public.pms_product_attribute_category.id;


--
-- Name: pms_product_attribute_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.pms_product_attribute_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.pms_product_attribute_id_seq OWNER TO postgres;

--
-- Name: pms_product_attribute_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.pms_product_attribute_id_seq OWNED BY public.pms_product_attribute.id;


--
-- Name: pms_product_attribute_value; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pms_product_attribute_value (
    id bigint NOT NULL,
    product_id bigint,
    product_attribute_id bigint,
    value character varying(255)
);


ALTER TABLE public.pms_product_attribute_value OWNER TO postgres;

--
-- Name: pms_product_attribute_value_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.pms_product_attribute_value_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.pms_product_attribute_value_id_seq OWNER TO postgres;

--
-- Name: pms_product_attribute_value_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.pms_product_attribute_value_id_seq OWNED BY public.pms_product_attribute_value.id;


--
-- Name: pms_product_category; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pms_product_category (
    id bigint NOT NULL,
    parent_id bigint,
    name character varying(255),
    level integer,
    product_count integer,
    product_unit character varying(255),
    nav_status integer,
    show_status integer,
    sort integer,
    icon character varying(255),
    keywords character varying(255),
    description character varying(255)
);


ALTER TABLE public.pms_product_category OWNER TO postgres;

--
-- Name: pms_product_category_attribute_relation; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pms_product_category_attribute_relation (
    id bigint NOT NULL,
    product_category_id bigint,
    product_attribute_id bigint
);


ALTER TABLE public.pms_product_category_attribute_relation OWNER TO postgres;

--
-- Name: pms_product_category_attribute_relation_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.pms_product_category_attribute_relation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.pms_product_category_attribute_relation_id_seq OWNER TO postgres;

--
-- Name: pms_product_category_attribute_relation_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.pms_product_category_attribute_relation_id_seq OWNED BY public.pms_product_category_attribute_relation.id;


--
-- Name: pms_product_category_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.pms_product_category_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.pms_product_category_id_seq OWNER TO postgres;

--
-- Name: pms_product_category_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.pms_product_category_id_seq OWNED BY public.pms_product_category.id;


--
-- Name: pms_product_embedding; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pms_product_embedding (
    product_id bigint NOT NULL,
    embedding public.vector(1536)
);


ALTER TABLE public.pms_product_embedding OWNER TO postgres;

--
-- Name: pms_product_spend_discount; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pms_product_spend_discount (
    id bigint NOT NULL,
    product_id bigint,
    full_price numeric(19,2),
    reduce_price numeric(19,2)
);


ALTER TABLE public.pms_product_spend_discount OWNER TO postgres;

--
-- Name: pms_product_spend_discount_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.pms_product_spend_discount_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.pms_product_spend_discount_id_seq OWNER TO postgres;

--
-- Name: pms_product_spend_discount_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.pms_product_spend_discount_id_seq OWNED BY public.pms_product_spend_discount.id;


--
-- Name: pms_product_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.pms_product_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.pms_product_id_seq OWNER TO postgres;

--
-- Name: pms_product_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.pms_product_id_seq OWNED BY public.pms_product.id;


--
-- Name: pms_product_volume_discount; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pms_product_volume_discount (
    id bigint NOT NULL,
    product_id bigint,
    count integer,
    discount numeric(19,2),
    price numeric(19,2)
);


ALTER TABLE public.pms_product_volume_discount OWNER TO postgres;

--
-- Name: pms_product_volume_discount_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.pms_product_volume_discount_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.pms_product_volume_discount_id_seq OWNER TO postgres;

--
-- Name: pms_product_volume_discount_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.pms_product_volume_discount_id_seq OWNED BY public.pms_product_volume_discount.id;


--
-- Name: pms_product_gallery; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pms_product_gallery (
    id bigint NOT NULL,
    product_id bigint NOT NULL,
    media_type character varying(50),
    media_url character varying(255) NOT NULL,
    sort_order integer
);


ALTER TABLE public.pms_product_gallery OWNER TO postgres;

--
-- Name: pms_product_gallery_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.pms_product_gallery_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.pms_product_gallery_id_seq OWNER TO postgres;

--
-- Name: pms_product_gallery_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.pms_product_gallery_id_seq OWNED BY public.pms_product_gallery.id;


--
-- Name: pms_product_audit_log; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pms_product_audit_log (
    id bigint NOT NULL,
    product_id bigint,
    price_old numeric(19,2),
    price_new numeric(19,2),
    sale_price_old numeric(19,2),
    sale_price_new numeric(19,2),
    operate_man character varying(255),
    create_time timestamp without time zone
);


ALTER TABLE public.pms_product_audit_log OWNER TO postgres;

--
-- Name: pms_product_audit_log_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.pms_product_audit_log_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.pms_product_audit_log_id_seq OWNER TO postgres;

--
-- Name: pms_product_audit_log_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.pms_product_audit_log_id_seq OWNED BY public.pms_product_audit_log.id;


--
-- Name: pms_product_approval_history; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pms_product_approval_history (
    id bigint NOT NULL,
    product_id bigint,
    create_time timestamp without time zone,
    reviewer_name character varying(255),
    status integer,
    detail character varying(255)
);


ALTER TABLE public.pms_product_approval_history OWNER TO postgres;

--
-- Name: pms_product_approval_history_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.pms_product_approval_history_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.pms_product_approval_history_id_seq OWNER TO postgres;

--
-- Name: pms_product_approval_history_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.pms_product_approval_history_id_seq OWNED BY public.pms_product_approval_history.id;


--
-- Name: pms_review; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pms_review (
    id bigint NOT NULL,
    product_id bigint,
    member_id bigint,
    parent_id bigint,
    rating integer,
    content character varying(255),
    like_count integer,
    dislike_count integer,
    status integer,
    created_time timestamp without time zone
);


ALTER TABLE public.pms_review OWNER TO postgres;

--
-- Name: pms_review_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.pms_review_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.pms_review_id_seq OWNER TO postgres;

--
-- Name: pms_review_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.pms_review_id_seq OWNED BY public.pms_review.id;


--
-- Name: pms_review_attachments; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pms_review_attachments (
    id bigint NOT NULL,
    review_id bigint,
    media_type character varying(255),
    media_url character varying(255),
    thumbnail_url character varying(255),
    sort_order integer
);


ALTER TABLE public.pms_review_attachments OWNER TO postgres;

--
-- Name: pms_review_attachments_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.pms_review_attachments_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.pms_review_attachments_id_seq OWNER TO postgres;

--
-- Name: pms_review_attachments_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.pms_review_attachments_id_seq OWNED BY public.pms_review_attachments.id;


--
-- Name: pms_sku_stock; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pms_sku_stock (
    id bigint NOT NULL,
    product_id bigint,
    sku_code character varying(255),
    price numeric(19,2),
    stock integer,
    low_stock integer,
    pic character varying(255),
    sale integer,
    promotion_price numeric(19,2),
    lock_stock integer,
    sku_attributes jsonb
);


ALTER TABLE public.pms_sku_stock OWNER TO postgres;

--
-- Name: pms_sku_stock_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.pms_sku_stock_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.pms_sku_stock_id_seq OWNER TO postgres;

--
-- Name: pms_sku_stock_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.pms_sku_stock_id_seq OWNED BY public.pms_sku_stock.id;


--
-- Name: sms_coupon; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sms_coupon (
    id bigint NOT NULL,
    type integer,
    name character varying(255),
    count integer,
    amount numeric(19,2),
    per_limit integer,
    min_order_amount numeric(19,2),
    start_time timestamp without time zone,
    end_time timestamp without time zone,
    use_type integer,
    receive_count integer,
    code character varying(255),
    max_discount_amount numeric(10,2) DEFAULT NULL::numeric,
    version integer DEFAULT 0,
    stackable boolean DEFAULT false,
    is_public boolean DEFAULT false
);


ALTER TABLE public.sms_coupon OWNER TO postgres;

--
-- Name: COLUMN sms_coupon.max_discount_amount; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sms_coupon.max_discount_amount IS 'Maximum discount for percentage coupons (cap)';


--
-- Name: COLUMN sms_coupon.version; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sms_coupon.version IS 'Optimistic lock version for stock management';


--
-- Name: COLUMN sms_coupon.stackable; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sms_coupon.stackable IS 'Whether this coupon can be combined with other coupons';


--
-- Name: COLUMN sms_coupon.is_public; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.sms_coupon.is_public IS 'Whether coupon can be claimed by entering a code';


--
--




--
--



--
--



--
--

    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



--
--



--
-- Name: sms_coupon_history; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sms_coupon_history (
    id bigint NOT NULL,
    coupon_id bigint,
    member_id bigint,
    coupon_code character varying(255),
    member_nickname character varying(255),
    get_type integer,
    create_time timestamp without time zone,
    use_status integer,
    use_time timestamp without time zone,
    order_id bigint,
    order_sn character varying(255)
);


ALTER TABLE public.sms_coupon_history OWNER TO postgres;

--
-- Name: sms_coupon_history_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.sms_coupon_history_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.sms_coupon_history_id_seq OWNER TO postgres;

--
-- Name: sms_coupon_history_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.sms_coupon_history_id_seq OWNED BY public.sms_coupon_history.id;


--
-- Name: sms_coupon_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.sms_coupon_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.sms_coupon_id_seq OWNER TO postgres;

--
-- Name: sms_coupon_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.sms_coupon_id_seq OWNED BY public.sms_coupon.id;


--
-- Name: sms_coupon_product_category_relation; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sms_coupon_product_category_relation (
    id bigint NOT NULL,
    coupon_id bigint,
    product_category_id bigint,
    product_category_name character varying(255),
    parent_category_name character varying(255)
);


ALTER TABLE public.sms_coupon_product_category_relation OWNER TO postgres;

--
-- Name: sms_coupon_product_category_relation_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.sms_coupon_product_category_relation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.sms_coupon_product_category_relation_id_seq OWNER TO postgres;

--
-- Name: sms_coupon_product_category_relation_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.sms_coupon_product_category_relation_id_seq OWNED BY public.sms_coupon_product_category_relation.id;


--
-- Name: sms_coupon_product_relation; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sms_coupon_product_relation (
    id bigint NOT NULL,
    coupon_id bigint,
    product_id bigint,
    product_name character varying(255),
    product_sn character varying(255)
);


ALTER TABLE public.sms_coupon_product_relation OWNER TO postgres;

--
-- Name: sms_coupon_product_relation_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.sms_coupon_product_relation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.sms_coupon_product_relation_id_seq OWNER TO postgres;

--
-- Name: sms_coupon_product_relation_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.sms_coupon_product_relation_id_seq OWNED BY public.sms_coupon_product_relation.id;


--
-- Name: sms_flash_promotion; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sms_flash_promotion (
    id bigint NOT NULL,
    title character varying(200) NOT NULL,
    start_date date NOT NULL,
    end_date date NOT NULL,
    status integer DEFAULT 0,
    create_time timestamp without time zone DEFAULT now()
);


ALTER TABLE public.sms_flash_promotion OWNER TO postgres;

--
-- Name: TABLE sms_flash_promotion; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.sms_flash_promotion IS 'Flash promotion event definition';


--
-- Name: sms_flash_promotion_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.sms_flash_promotion_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.sms_flash_promotion_id_seq OWNER TO postgres;

--
-- Name: sms_flash_promotion_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.sms_flash_promotion_id_seq OWNED BY public.sms_flash_promotion.id;


--
-- Name: sms_flash_promotion_log; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sms_flash_promotion_log (
    id bigint NOT NULL,
    member_id bigint NOT NULL,
    product_id bigint NOT NULL,
    flash_promotion_id bigint,
    session_id bigint,
    order_id bigint,
    quantity integer DEFAULT 1,
    status character varying(50) DEFAULT 'PENDING'::character varying,
    create_time timestamp without time zone DEFAULT now()
);


ALTER TABLE public.sms_flash_promotion_log OWNER TO postgres;

--
-- Name: sms_flash_promotion_log_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.sms_flash_promotion_log_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.sms_flash_promotion_log_id_seq OWNER TO postgres;

--
-- Name: sms_flash_promotion_log_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.sms_flash_promotion_log_id_seq OWNED BY public.sms_flash_promotion_log.id;


--
-- Name: sms_flash_promotion_product_relation; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sms_flash_promotion_product_relation (
    id bigint NOT NULL,
    flash_promotion_id bigint NOT NULL,
    flash_promotion_session_id bigint NOT NULL,
    product_id bigint NOT NULL,
    flash_promotion_price numeric(10,2) NOT NULL,
    flash_promotion_count integer NOT NULL,
    flash_promotion_limit integer DEFAULT 1,
    sort integer DEFAULT 0
);


ALTER TABLE public.sms_flash_promotion_product_relation OWNER TO postgres;

--
-- Name: TABLE sms_flash_promotion_product_relation; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.sms_flash_promotion_product_relation IS 'Products in a specific flash session with flash pricing';


--
-- Name: sms_flash_promotion_product_relation_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.sms_flash_promotion_product_relation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.sms_flash_promotion_product_relation_id_seq OWNER TO postgres;

--
-- Name: sms_flash_promotion_product_relation_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.sms_flash_promotion_product_relation_id_seq OWNED BY public.sms_flash_promotion_product_relation.id;


--
-- Name: sms_flash_promotion_session; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sms_flash_promotion_session (
    id bigint NOT NULL,
    name character varying(200) NOT NULL,
    start_time time without time zone NOT NULL,
    end_time time without time zone NOT NULL,
    status integer DEFAULT 0,
    create_time timestamp without time zone DEFAULT now()
);


ALTER TABLE public.sms_flash_promotion_session OWNER TO postgres;

--
-- Name: TABLE sms_flash_promotion_session; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.sms_flash_promotion_session IS 'Time slots within a flash promotion day';


--
-- Name: sms_flash_promotion_session_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.sms_flash_promotion_session_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.sms_flash_promotion_session_id_seq OWNER TO postgres;

--
-- Name: sms_flash_promotion_session_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.sms_flash_promotion_session_id_seq OWNED BY public.sms_flash_promotion_session.id;


--
-- Name: sys_banner; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sys_banner (
    id bigint NOT NULL,
    name character varying(255),
    type integer,
    pic character varying(255),
    start_time timestamp without time zone,
    end_time timestamp without time zone,
    status integer,
    click_count integer,
    order_count integer,
    url character varying(255),
    sort integer
);


ALTER TABLE public.sys_banner OWNER TO postgres;

--
-- Name: sys_banner_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.sys_banner_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.sys_banner_id_seq OWNER TO postgres;

--
-- Name: sys_banner_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.sys_banner_id_seq OWNED BY public.sys_banner.id;


--
-- Name: ums_admin; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ums_admin (
    id bigint NOT NULL,
    username character varying(255),
    password character varying(255),
    icon character varying(255),
    email character varying(255),
    nick_name character varying(255),
    create_time timestamp without time zone,
    login_time timestamp without time zone,
    status integer,
    vendor_id bigint
);


ALTER TABLE public.ums_admin OWNER TO postgres;

--
-- Name: ums_admin_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.ums_admin_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.ums_admin_id_seq OWNER TO postgres;

--
-- Name: ums_admin_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.ums_admin_id_seq OWNED BY public.ums_admin.id;


--
-- Name: ums_admin_login_log; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ums_admin_login_log (
    id bigint NOT NULL,
    admin_id bigint,
    create_time timestamp without time zone,
    ip character varying(255),
    address character varying(255),
    user_agent character varying(255)
);


ALTER TABLE public.ums_admin_login_log OWNER TO postgres;

--
-- Name: ums_admin_login_log_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.ums_admin_login_log_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.ums_admin_login_log_id_seq OWNER TO postgres;

--
-- Name: ums_admin_login_log_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.ums_admin_login_log_id_seq OWNED BY public.ums_admin_login_log.id;


--
-- Name: ums_admin_role_relation; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ums_admin_role_relation (
    id bigint NOT NULL,
    admin_id bigint,
    role_id bigint
);


ALTER TABLE public.ums_admin_role_relation OWNER TO postgres;

--
-- Name: ums_admin_role_relation_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.ums_admin_role_relation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.ums_admin_role_relation_id_seq OWNER TO postgres;

--
-- Name: ums_admin_role_relation_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.ums_admin_role_relation_id_seq OWNED BY public.ums_admin_role_relation.id;


--
-- Name: ums_member; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ums_member (
    id bigint NOT NULL,
    username character varying(255),
    password character varying(255),
    nickname character varying(255),
    phone character varying(255),
    email character varying(255),
    status integer,
    create_time timestamp without time zone,
    icon character varying(255),
    gender integer,
    birthday timestamp without time zone,
    personalized_signature character varying(255),
    source_type integer,
    points integer,
    bonus_draws_remaining integer,
    lifetime_points integer
);


ALTER TABLE public.ums_member OWNER TO postgres;

--
-- Name: ums_member_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.ums_member_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.ums_member_id_seq OWNER TO postgres;

--
-- Name: ums_member_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.ums_member_id_seq OWNED BY public.ums_member.id;


--
-- Name: ums_member_login_log; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ums_member_login_log (
    id bigint NOT NULL,
    member_id bigint,
    create_time timestamp without time zone,
    ip character varying(255),
    city character varying(255),
    login_type integer,
    province character varying(255)
);


ALTER TABLE public.ums_member_login_log OWNER TO postgres;

--
-- Name: ums_member_login_log_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.ums_member_login_log_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.ums_member_login_log_id_seq OWNER TO postgres;

--
-- Name: ums_member_login_log_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.ums_member_login_log_id_seq OWNED BY public.ums_member_login_log.id;


--
-- Name: ums_member_product_category_relation; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ums_member_product_category_relation (
    id bigint NOT NULL,
    member_id bigint,
    product_category_id bigint
);


ALTER TABLE public.ums_member_product_category_relation OWNER TO postgres;

--
-- Name: ums_member_product_category_relation_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.ums_member_product_category_relation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.ums_member_product_category_relation_id_seq OWNER TO postgres;

--
-- Name: ums_member_product_category_relation_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.ums_member_product_category_relation_id_seq OWNED BY public.ums_member_product_category_relation.id;


--
-- Name: ums_member_receive_address; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ums_member_receive_address (
    id bigint NOT NULL,
    member_id bigint,
    name character varying(255),
    phone_number character varying(255),
    default_status integer,
    post_code character varying(255),
    province character varying(255),
    city character varying(255),
    region character varying(255),
    detail_address character varying(255)
);


ALTER TABLE public.ums_member_receive_address OWNER TO postgres;

--
-- Name: ums_member_receive_address_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.ums_member_receive_address_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.ums_member_receive_address_id_seq OWNER TO postgres;

--
-- Name: ums_member_receive_address_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.ums_member_receive_address_id_seq OWNED BY public.ums_member_receive_address.id;


--
-- Name: ums_menu; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ums_menu (
    id bigint NOT NULL,
    parent_id bigint,
    create_time timestamp without time zone,
    title character varying(255),
    level integer,
    sort integer,
    name character varying(255),
    icon character varying(255),
    hidden integer
);


ALTER TABLE public.ums_menu OWNER TO postgres;

--
-- Name: ums_menu_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.ums_menu_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.ums_menu_id_seq OWNER TO postgres;

--
-- Name: ums_menu_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.ums_menu_id_seq OWNED BY public.ums_menu.id;


--
-- Name: ums_resource; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ums_resource (
    id bigint NOT NULL,
    create_time timestamp without time zone,
    name character varying(255),
    url character varying(255),
    description character varying(255),
    category_id bigint
);


ALTER TABLE public.ums_resource OWNER TO postgres;

--
-- Name: ums_resource_category; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ums_resource_category (
    id bigint NOT NULL,
    create_time timestamp without time zone,
    name character varying(255),
    sort integer
);


ALTER TABLE public.ums_resource_category OWNER TO postgres;

--
-- Name: ums_resource_category_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.ums_resource_category_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.ums_resource_category_id_seq OWNER TO postgres;

--
-- Name: ums_resource_category_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.ums_resource_category_id_seq OWNED BY public.ums_resource_category.id;


--
-- Name: ums_resource_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.ums_resource_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.ums_resource_id_seq OWNER TO postgres;

--
-- Name: ums_resource_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.ums_resource_id_seq OWNED BY public.ums_resource.id;


--
-- Name: ums_role; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ums_role (
    id bigint NOT NULL,
    name character varying(255),
    description character varying(255),
    admin_count integer,
    create_time timestamp without time zone,
    status integer,
    sort integer
);


ALTER TABLE public.ums_role OWNER TO postgres;

--
-- Name: ums_role_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.ums_role_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.ums_role_id_seq OWNER TO postgres;

--
-- Name: ums_role_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.ums_role_id_seq OWNED BY public.ums_role.id;


--
-- Name: ums_role_menu_relation; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ums_role_menu_relation (
    id bigint NOT NULL,
    role_id bigint,
    menu_id bigint
);


ALTER TABLE public.ums_role_menu_relation OWNER TO postgres;

--
-- Name: ums_role_menu_relation_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.ums_role_menu_relation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.ums_role_menu_relation_id_seq OWNER TO postgres;

--
-- Name: ums_role_menu_relation_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.ums_role_menu_relation_id_seq OWNED BY public.ums_role_menu_relation.id;


--
-- Name: ums_role_resource_relation; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ums_role_resource_relation (
    id bigint NOT NULL,
    role_id bigint,
    resource_id bigint
);


ALTER TABLE public.ums_role_resource_relation OWNER TO postgres;

--
-- Name: ums_role_resource_relation_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.ums_role_resource_relation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.ums_role_resource_relation_id_seq OWNER TO postgres;

--
-- Name: ums_role_resource_relation_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.ums_role_resource_relation_id_seq OWNED BY public.ums_role_resource_relation.id;


--
-- Name: vector_store; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.vector_store (
    id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    content text,
    metadata json,
    embedding public.vector(1536)
);


ALTER TABLE public.vector_store OWNER TO postgres;

--
-- Name: sys_banner; Type: TABLE; Schema: system; Owner: postgres
--

CREATE TABLE system.sys_banner (
    id bigint NOT NULL,
    name character varying(255),
    type integer,
    pic character varying(255),
    start_time timestamp without time zone,
    end_time timestamp without time zone,
    status integer,
    click_count integer,
    order_count integer,
    url character varying(255),
    sort integer
);


ALTER TABLE system.sys_banner OWNER TO postgres;

--
-- Name: sys_banner_id_seq; Type: SEQUENCE; Schema: system; Owner: postgres
--

CREATE SEQUENCE system.sys_banner_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE system.sys_banner_id_seq OWNER TO postgres;

--
-- Name: sys_banner_id_seq; Type: SEQUENCE OWNED BY; Schema: system; Owner: postgres
--

ALTER SEQUENCE system.sys_banner_id_seq OWNED BY system.sys_banner.id;


--
-- Name: ums_admin; Type: TABLE; Schema: users; Owner: postgres
--

CREATE TABLE users.ums_admin (
    id bigint NOT NULL,
    username character varying(255),
    password character varying(255),
    icon character varying(255),
    email character varying(255),
    nick_name character varying(255),
    create_time timestamp without time zone,
    login_time timestamp without time zone,
    status integer,
    vendor_id bigint
);


ALTER TABLE users.ums_admin OWNER TO postgres;

--
-- Name: ums_admin_id_seq; Type: SEQUENCE; Schema: users; Owner: postgres
--

CREATE SEQUENCE users.ums_admin_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE users.ums_admin_id_seq OWNER TO postgres;

--
-- Name: ums_admin_id_seq; Type: SEQUENCE OWNED BY; Schema: users; Owner: postgres
--

ALTER SEQUENCE users.ums_admin_id_seq OWNED BY users.ums_admin.id;


--
-- Name: ums_admin_login_log; Type: TABLE; Schema: users; Owner: postgres
--

CREATE TABLE users.ums_admin_login_log (
    id bigint NOT NULL,
    admin_id bigint,
    create_time timestamp without time zone,
    ip character varying(255),
    address character varying(255),
    user_agent character varying(255)
);


ALTER TABLE users.ums_admin_login_log OWNER TO postgres;

--
-- Name: ums_admin_login_log_id_seq; Type: SEQUENCE; Schema: users; Owner: postgres
--

CREATE SEQUENCE users.ums_admin_login_log_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE users.ums_admin_login_log_id_seq OWNER TO postgres;

--
-- Name: ums_admin_login_log_id_seq; Type: SEQUENCE OWNED BY; Schema: users; Owner: postgres
--

ALTER SEQUENCE users.ums_admin_login_log_id_seq OWNED BY users.ums_admin_login_log.id;


--
-- Name: ums_admin_role_relation; Type: TABLE; Schema: users; Owner: postgres
--

CREATE TABLE users.ums_admin_role_relation (
    id bigint NOT NULL,
    admin_id bigint,
    role_id bigint
);


ALTER TABLE users.ums_admin_role_relation OWNER TO postgres;

--
-- Name: ums_admin_role_relation_id_seq; Type: SEQUENCE; Schema: users; Owner: postgres
--

CREATE SEQUENCE users.ums_admin_role_relation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE users.ums_admin_role_relation_id_seq OWNER TO postgres;

--
-- Name: ums_admin_role_relation_id_seq; Type: SEQUENCE OWNED BY; Schema: users; Owner: postgres
--

ALTER SEQUENCE users.ums_admin_role_relation_id_seq OWNED BY users.ums_admin_role_relation.id;


--
-- Name: ums_member; Type: TABLE; Schema: users; Owner: postgres
--

CREATE TABLE users.ums_member (
    id bigint NOT NULL,
    username character varying(255),
    password character varying(255),
    nickname character varying(255),
    phone character varying(255),
    email character varying(255),
    status integer,
    create_time timestamp without time zone,
    icon character varying(255),
    gender integer,
    birthday timestamp without time zone,
    personalized_signature character varying(255),
    source_type integer,
    points integer,
    bonus_draws_remaining integer,
    lifetime_points integer
);


ALTER TABLE users.ums_member OWNER TO postgres;

--
-- Name: ums_member_id_seq; Type: SEQUENCE; Schema: users; Owner: postgres
--

CREATE SEQUENCE users.ums_member_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE users.ums_member_id_seq OWNER TO postgres;

--
-- Name: ums_member_id_seq; Type: SEQUENCE OWNED BY; Schema: users; Owner: postgres
--

ALTER SEQUENCE users.ums_member_id_seq OWNED BY users.ums_member.id;


--
-- Name: ums_member_login_log; Type: TABLE; Schema: users; Owner: postgres
--

CREATE TABLE users.ums_member_login_log (
    id bigint NOT NULL,
    member_id bigint,
    create_time timestamp without time zone,
    ip character varying(255),
    city character varying(255),
    login_type integer,
    province character varying(255)
);


ALTER TABLE users.ums_member_login_log OWNER TO postgres;

--
-- Name: ums_member_login_log_id_seq; Type: SEQUENCE; Schema: users; Owner: postgres
--

CREATE SEQUENCE users.ums_member_login_log_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE users.ums_member_login_log_id_seq OWNER TO postgres;

--
-- Name: ums_member_login_log_id_seq; Type: SEQUENCE OWNED BY; Schema: users; Owner: postgres
--

ALTER SEQUENCE users.ums_member_login_log_id_seq OWNED BY users.ums_member_login_log.id;


--
-- Name: ums_member_product_category_relation; Type: TABLE; Schema: users; Owner: postgres
--

CREATE TABLE users.ums_member_product_category_relation (
    id bigint NOT NULL,
    member_id bigint,
    product_category_id bigint
);


ALTER TABLE users.ums_member_product_category_relation OWNER TO postgres;

--
-- Name: ums_member_product_category_relation_id_seq; Type: SEQUENCE; Schema: users; Owner: postgres
--

CREATE SEQUENCE users.ums_member_product_category_relation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE users.ums_member_product_category_relation_id_seq OWNER TO postgres;

--
-- Name: ums_member_product_category_relation_id_seq; Type: SEQUENCE OWNED BY; Schema: users; Owner: postgres
--

ALTER SEQUENCE users.ums_member_product_category_relation_id_seq OWNED BY users.ums_member_product_category_relation.id;


--
-- Name: ums_member_receive_address; Type: TABLE; Schema: users; Owner: postgres
--

CREATE TABLE users.ums_member_receive_address (
    id bigint NOT NULL,
    member_id bigint,
    name character varying(255),
    phone_number character varying(255),
    default_status integer,
    post_code character varying(255),
    province character varying(255),
    city character varying(255),
    region character varying(255),
    detail_address character varying(255)
);


ALTER TABLE users.ums_member_receive_address OWNER TO postgres;

--
-- Name: ums_member_receive_address_id_seq; Type: SEQUENCE; Schema: users; Owner: postgres
--

CREATE SEQUENCE users.ums_member_receive_address_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE users.ums_member_receive_address_id_seq OWNER TO postgres;

--
-- Name: ums_member_receive_address_id_seq; Type: SEQUENCE OWNED BY; Schema: users; Owner: postgres
--

ALTER SEQUENCE users.ums_member_receive_address_id_seq OWNED BY users.ums_member_receive_address.id;


--
-- Name: ums_menu; Type: TABLE; Schema: users; Owner: postgres
--

CREATE TABLE users.ums_menu (
    id bigint NOT NULL,
    parent_id bigint,
    create_time timestamp without time zone,
    title character varying(255),
    level integer,
    sort integer,
    name character varying(255),
    icon character varying(255),
    hidden integer
);


ALTER TABLE users.ums_menu OWNER TO postgres;

--
-- Name: ums_menu_id_seq; Type: SEQUENCE; Schema: users; Owner: postgres
--

CREATE SEQUENCE users.ums_menu_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE users.ums_menu_id_seq OWNER TO postgres;

--
-- Name: ums_menu_id_seq; Type: SEQUENCE OWNED BY; Schema: users; Owner: postgres
--

ALTER SEQUENCE users.ums_menu_id_seq OWNED BY users.ums_menu.id;


--
-- Name: ums_resource; Type: TABLE; Schema: users; Owner: postgres
--

CREATE TABLE users.ums_resource (
    id bigint NOT NULL,
    create_time timestamp without time zone,
    name character varying(255),
    url character varying(255),
    description character varying(255),
    category_id bigint
);


ALTER TABLE users.ums_resource OWNER TO postgres;

--
-- Name: ums_resource_category; Type: TABLE; Schema: users; Owner: postgres
--

CREATE TABLE users.ums_resource_category (
    id bigint NOT NULL,
    create_time timestamp without time zone,
    name character varying(255),
    sort integer
);


ALTER TABLE users.ums_resource_category OWNER TO postgres;

--
-- Name: ums_resource_category_id_seq; Type: SEQUENCE; Schema: users; Owner: postgres
--

CREATE SEQUENCE users.ums_resource_category_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE users.ums_resource_category_id_seq OWNER TO postgres;

--
-- Name: ums_resource_category_id_seq; Type: SEQUENCE OWNED BY; Schema: users; Owner: postgres
--

ALTER SEQUENCE users.ums_resource_category_id_seq OWNED BY users.ums_resource_category.id;


--
-- Name: ums_resource_id_seq; Type: SEQUENCE; Schema: users; Owner: postgres
--

CREATE SEQUENCE users.ums_resource_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE users.ums_resource_id_seq OWNER TO postgres;

--
-- Name: ums_resource_id_seq; Type: SEQUENCE OWNED BY; Schema: users; Owner: postgres
--

ALTER SEQUENCE users.ums_resource_id_seq OWNED BY users.ums_resource.id;


--
-- Name: ums_role; Type: TABLE; Schema: users; Owner: postgres
--

CREATE TABLE users.ums_role (
    id bigint NOT NULL,
    name character varying(255),
    description character varying(255),
    admin_count integer,
    create_time timestamp without time zone,
    status integer,
    sort integer
);


ALTER TABLE users.ums_role OWNER TO postgres;

--
-- Name: ums_role_id_seq; Type: SEQUENCE; Schema: users; Owner: postgres
--

CREATE SEQUENCE users.ums_role_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE users.ums_role_id_seq OWNER TO postgres;

--
-- Name: ums_role_id_seq; Type: SEQUENCE OWNED BY; Schema: users; Owner: postgres
--

ALTER SEQUENCE users.ums_role_id_seq OWNED BY users.ums_role.id;


--
-- Name: ums_role_menu_relation; Type: TABLE; Schema: users; Owner: postgres
--

CREATE TABLE users.ums_role_menu_relation (
    id bigint NOT NULL,
    role_id bigint,
    menu_id bigint
);


ALTER TABLE users.ums_role_menu_relation OWNER TO postgres;

--
-- Name: ums_role_menu_relation_id_seq; Type: SEQUENCE; Schema: users; Owner: postgres
--

CREATE SEQUENCE users.ums_role_menu_relation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE users.ums_role_menu_relation_id_seq OWNER TO postgres;

--
-- Name: ums_role_menu_relation_id_seq; Type: SEQUENCE OWNED BY; Schema: users; Owner: postgres
--

ALTER SEQUENCE users.ums_role_menu_relation_id_seq OWNED BY users.ums_role_menu_relation.id;


--
-- Name: ums_role_resource_relation; Type: TABLE; Schema: users; Owner: postgres
--

CREATE TABLE users.ums_role_resource_relation (
    id bigint NOT NULL,
    role_id bigint,
    resource_id bigint
);


ALTER TABLE users.ums_role_resource_relation OWNER TO postgres;

--
-- Name: ums_role_resource_relation_id_seq; Type: SEQUENCE; Schema: users; Owner: postgres
--

CREATE SEQUENCE users.ums_role_resource_relation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE users.ums_role_resource_relation_id_seq OWNER TO postgres;

--
-- Name: ums_role_resource_relation_id_seq; Type: SEQUENCE OWNED BY; Schema: users; Owner: postgres
--

ALTER SEQUENCE users.ums_role_resource_relation_id_seq OWNED BY users.ums_role_resource_relation.id;


--
-- Name: ai_chat_conversation id; Type: DEFAULT; Schema: ai; Owner: postgres
--

ALTER TABLE ONLY ai.ai_chat_conversation ALTER COLUMN id SET DEFAULT nextval('ai.ai_chat_conversation_id_seq'::regclass);


--
-- Name: pms_brand id; Type: DEFAULT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_brand ALTER COLUMN id SET DEFAULT nextval('pms.pms_brand_id_seq'::regclass);


--
-- Name: pms_comment id; Type: DEFAULT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_comment ALTER COLUMN id SET DEFAULT nextval('pms.pms_comment_id_seq'::regclass);


--
-- Name: pms_comment_reply id; Type: DEFAULT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_comment_reply ALTER COLUMN id SET DEFAULT nextval('pms.pms_comment_reply_id_seq'::regclass);


--
-- Name: pms_product id; Type: DEFAULT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_product ALTER COLUMN id SET DEFAULT nextval('pms.pms_product_id_seq'::regclass);


--
-- Name: pms_product_attribute id; Type: DEFAULT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_product_attribute ALTER COLUMN id SET DEFAULT nextval('pms.pms_product_attribute_id_seq'::regclass);


--
-- Name: pms_product_attribute_category id; Type: DEFAULT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_product_attribute_category ALTER COLUMN id SET DEFAULT nextval('pms.pms_product_attribute_category_id_seq'::regclass);


--
-- Name: pms_product_attribute_value id; Type: DEFAULT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_product_attribute_value ALTER COLUMN id SET DEFAULT nextval('pms.pms_product_attribute_value_id_seq'::regclass);


--
-- Name: pms_product_category id; Type: DEFAULT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_product_category ALTER COLUMN id SET DEFAULT nextval('pms.pms_product_category_id_seq'::regclass);


--
-- Name: pms_product_category_attribute_relation id; Type: DEFAULT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_product_category_attribute_relation ALTER COLUMN id SET DEFAULT nextval('pms.pms_product_category_attribute_relation_id_seq'::regclass);


--
-- Name: pms_product_spend_discount id; Type: DEFAULT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_product_spend_discount ALTER COLUMN id SET DEFAULT nextval('pms.pms_product_spend_discount_id_seq'::regclass);


--
-- Name: pms_product_volume_discount id; Type: DEFAULT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_product_volume_discount ALTER COLUMN id SET DEFAULT nextval('pms.pms_product_volume_discount_id_seq'::regclass);


--
-- Name: pms_product_gallery id; Type: DEFAULT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_product_gallery ALTER COLUMN id SET DEFAULT nextval('pms.pms_product_gallery_id_seq'::regclass);


--
-- Name: pms_product_audit_log id; Type: DEFAULT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_product_audit_log ALTER COLUMN id SET DEFAULT nextval('pms.pms_product_audit_log_id_seq'::regclass);


--
-- Name: pms_product_approval_history id; Type: DEFAULT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_product_approval_history ALTER COLUMN id SET DEFAULT nextval('pms.pms_product_approval_history_id_seq'::regclass);


--
-- Name: pms_review id; Type: DEFAULT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_review ALTER COLUMN id SET DEFAULT nextval('pms.pms_review_id_seq'::regclass);


--
-- Name: pms_review_attachments id; Type: DEFAULT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_review_attachments ALTER COLUMN id SET DEFAULT nextval('pms.pms_review_attachments_id_seq'::regclass);


--
-- Name: pms_sku_stock id; Type: DEFAULT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_sku_stock ALTER COLUMN id SET DEFAULT nextval('pms.pms_sku_stock_id_seq'::regclass);


--
-- Name: outbox_event id; Type: DEFAULT; Schema: integration; Owner: postgres
--

ALTER TABLE ONLY integration.outbox_event ALTER COLUMN id SET DEFAULT nextval('integration.outbox_event_id_seq'::regclass);


--
-- Name: sms_coupon id; Type: DEFAULT; Schema: marketing; Owner: postgres
--

ALTER TABLE ONLY marketing.sms_coupon ALTER COLUMN id SET DEFAULT nextval('marketing.sms_coupon_id_seq'::regclass);


--
--



--
-- Name: sms_coupon_history id; Type: DEFAULT; Schema: marketing; Owner: postgres
--

ALTER TABLE ONLY marketing.sms_coupon_history ALTER COLUMN id SET DEFAULT nextval('marketing.sms_coupon_history_id_seq'::regclass);


--
-- Name: sms_coupon_product_category_relation id; Type: DEFAULT; Schema: marketing; Owner: postgres
--

ALTER TABLE ONLY marketing.sms_coupon_product_category_relation ALTER COLUMN id SET DEFAULT nextval('marketing.sms_coupon_product_category_relation_id_seq'::regclass);


--
-- Name: sms_coupon_product_relation id; Type: DEFAULT; Schema: marketing; Owner: postgres
--

ALTER TABLE ONLY marketing.sms_coupon_product_relation ALTER COLUMN id SET DEFAULT nextval('marketing.sms_coupon_product_relation_id_seq'::regclass);


--
-- Name: sms_flash_promotion id; Type: DEFAULT; Schema: marketing; Owner: postgres
--

ALTER TABLE ONLY marketing.sms_flash_promotion ALTER COLUMN id SET DEFAULT nextval('marketing.sms_flash_promotion_id_seq'::regclass);


--
-- Name: sms_flash_promotion_log id; Type: DEFAULT; Schema: marketing; Owner: postgres
--

ALTER TABLE ONLY marketing.sms_flash_promotion_log ALTER COLUMN id SET DEFAULT nextval('marketing.sms_flash_promotion_log_id_seq'::regclass);


--
-- Name: sms_flash_promotion_product_relation id; Type: DEFAULT; Schema: marketing; Owner: postgres
--

ALTER TABLE ONLY marketing.sms_flash_promotion_product_relation ALTER COLUMN id SET DEFAULT nextval('marketing.sms_flash_promotion_product_relation_id_seq'::regclass);


--
-- Name: sms_flash_promotion_session id; Type: DEFAULT; Schema: marketing; Owner: postgres
--

ALTER TABLE ONLY marketing.sms_flash_promotion_session ALTER COLUMN id SET DEFAULT nextval('marketing.sms_flash_promotion_session_id_seq'::regclass);


--
-- Name: oms_cart_item id; Type: DEFAULT; Schema: orders; Owner: postgres
--

ALTER TABLE ONLY orders.oms_cart_item ALTER COLUMN id SET DEFAULT nextval('orders.oms_cart_item_id_seq'::regclass);


--
-- Name: oms_company_address id; Type: DEFAULT; Schema: orders; Owner: postgres
--

ALTER TABLE ONLY orders.oms_company_address ALTER COLUMN id SET DEFAULT nextval('orders.oms_company_address_id_seq'::regclass);


--
-- Name: oms_order id; Type: DEFAULT; Schema: orders; Owner: postgres
--

ALTER TABLE ONLY orders.oms_order ALTER COLUMN id SET DEFAULT nextval('orders.oms_order_id_seq'::regclass);


--
-- Name: oms_order_item id; Type: DEFAULT; Schema: orders; Owner: postgres
--

ALTER TABLE ONLY orders.oms_order_item ALTER COLUMN id SET DEFAULT nextval('orders.oms_order_item_id_seq'::regclass);


--
-- Name: oms_order_operate_history id; Type: DEFAULT; Schema: orders; Owner: postgres
--

ALTER TABLE ONLY orders.oms_order_operate_history ALTER COLUMN id SET DEFAULT nextval('orders.oms_order_operate_history_id_seq'::regclass);


--
-- Name: oms_order_return_apply id; Type: DEFAULT; Schema: orders; Owner: postgres
--

ALTER TABLE ONLY orders.oms_order_return_apply ALTER COLUMN id SET DEFAULT nextval('orders.oms_order_return_apply_id_seq'::regclass);


--
-- Name: oms_order_return_reason id; Type: DEFAULT; Schema: orders; Owner: postgres
--

ALTER TABLE ONLY orders.oms_order_return_reason ALTER COLUMN id SET DEFAULT nextval('orders.oms_order_return_reason_id_seq'::regclass);


--
-- Name: oms_order_setting id; Type: DEFAULT; Schema: orders; Owner: postgres
--

ALTER TABLE ONLY orders.oms_order_setting ALTER COLUMN id SET DEFAULT nextval('orders.oms_order_setting_id_seq'::regclass);


--
-- Name: ai_chat_conversation id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ai_chat_conversation ALTER COLUMN id SET DEFAULT nextval('public.ai_chat_conversation_id_seq'::regclass);


--
-- Name: oms_cart_item id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.oms_cart_item ALTER COLUMN id SET DEFAULT nextval('public.oms_cart_item_id_seq'::regclass);


--
-- Name: oms_company_address id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.oms_company_address ALTER COLUMN id SET DEFAULT nextval('public.oms_company_address_id_seq'::regclass);


--
-- Name: oms_order id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.oms_order ALTER COLUMN id SET DEFAULT nextval('public.oms_order_id_seq'::regclass);


--
-- Name: oms_order_item id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.oms_order_item ALTER COLUMN id SET DEFAULT nextval('public.oms_order_item_id_seq'::regclass);


--
-- Name: oms_order_operate_history id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.oms_order_operate_history ALTER COLUMN id SET DEFAULT nextval('public.oms_order_operate_history_id_seq'::regclass);


--
-- Name: oms_order_return_apply id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.oms_order_return_apply ALTER COLUMN id SET DEFAULT nextval('public.oms_order_return_apply_id_seq'::regclass);


--
-- Name: oms_order_return_reason id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.oms_order_return_reason ALTER COLUMN id SET DEFAULT nextval('public.oms_order_return_reason_id_seq'::regclass);


--
-- Name: oms_order_setting id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.oms_order_setting ALTER COLUMN id SET DEFAULT nextval('public.oms_order_setting_id_seq'::regclass);


--
-- Name: outbox_event id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.outbox_event ALTER COLUMN id SET DEFAULT nextval('public.outbox_event_id_seq'::regclass);


--
-- Name: pms_brand id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pms_brand ALTER COLUMN id SET DEFAULT nextval('public.pms_brand_id_seq'::regclass);


--
-- Name: pms_comment id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pms_comment ALTER COLUMN id SET DEFAULT nextval('public.pms_comment_id_seq'::regclass);


--
-- Name: pms_comment_reply id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pms_comment_reply ALTER COLUMN id SET DEFAULT nextval('public.pms_comment_reply_id_seq'::regclass);


--
-- Name: pms_product id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pms_product ALTER COLUMN id SET DEFAULT nextval('public.pms_product_id_seq'::regclass);


--
-- Name: pms_product_attribute id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pms_product_attribute ALTER COLUMN id SET DEFAULT nextval('public.pms_product_attribute_id_seq'::regclass);


--
-- Name: pms_product_attribute_category id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pms_product_attribute_category ALTER COLUMN id SET DEFAULT nextval('public.pms_product_attribute_category_id_seq'::regclass);


--
-- Name: pms_product_attribute_value id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pms_product_attribute_value ALTER COLUMN id SET DEFAULT nextval('public.pms_product_attribute_value_id_seq'::regclass);


--
-- Name: pms_product_category id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pms_product_category ALTER COLUMN id SET DEFAULT nextval('public.pms_product_category_id_seq'::regclass);


--
-- Name: pms_product_category_attribute_relation id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pms_product_category_attribute_relation ALTER COLUMN id SET DEFAULT nextval('public.pms_product_category_attribute_relation_id_seq'::regclass);


--
-- Name: pms_product_spend_discount id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pms_product_spend_discount ALTER COLUMN id SET DEFAULT nextval('public.pms_product_spend_discount_id_seq'::regclass);


--
-- Name: pms_product_volume_discount id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pms_product_volume_discount ALTER COLUMN id SET DEFAULT nextval('public.pms_product_volume_discount_id_seq'::regclass);


--
-- Name: pms_product_gallery id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pms_product_gallery ALTER COLUMN id SET DEFAULT nextval('public.pms_product_gallery_id_seq'::regclass);


--
-- Name: pms_product_audit_log id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pms_product_audit_log ALTER COLUMN id SET DEFAULT nextval('public.pms_product_audit_log_id_seq'::regclass);


--
-- Name: pms_product_approval_history id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pms_product_approval_history ALTER COLUMN id SET DEFAULT nextval('public.pms_product_approval_history_id_seq'::regclass);


--
-- Name: pms_review id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pms_review ALTER COLUMN id SET DEFAULT nextval('public.pms_review_id_seq'::regclass);


--
-- Name: pms_review_attachments id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pms_review_attachments ALTER COLUMN id SET DEFAULT nextval('public.pms_review_attachments_id_seq'::regclass);


--
-- Name: pms_sku_stock id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pms_sku_stock ALTER COLUMN id SET DEFAULT nextval('public.pms_sku_stock_id_seq'::regclass);


--
-- Name: sms_coupon id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sms_coupon ALTER COLUMN id SET DEFAULT nextval('public.sms_coupon_id_seq'::regclass);


--
--



--
-- Name: sms_coupon_history id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sms_coupon_history ALTER COLUMN id SET DEFAULT nextval('public.sms_coupon_history_id_seq'::regclass);


--
-- Name: sms_coupon_product_category_relation id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sms_coupon_product_category_relation ALTER COLUMN id SET DEFAULT nextval('public.sms_coupon_product_category_relation_id_seq'::regclass);


--
-- Name: sms_coupon_product_relation id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sms_coupon_product_relation ALTER COLUMN id SET DEFAULT nextval('public.sms_coupon_product_relation_id_seq'::regclass);


--
-- Name: sms_flash_promotion id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sms_flash_promotion ALTER COLUMN id SET DEFAULT nextval('public.sms_flash_promotion_id_seq'::regclass);


--
-- Name: sms_flash_promotion_log id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sms_flash_promotion_log ALTER COLUMN id SET DEFAULT nextval('public.sms_flash_promotion_log_id_seq'::regclass);


--
-- Name: sms_flash_promotion_product_relation id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sms_flash_promotion_product_relation ALTER COLUMN id SET DEFAULT nextval('public.sms_flash_promotion_product_relation_id_seq'::regclass);


--
-- Name: sms_flash_promotion_session id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sms_flash_promotion_session ALTER COLUMN id SET DEFAULT nextval('public.sms_flash_promotion_session_id_seq'::regclass);


--
-- Name: sys_banner id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sys_banner ALTER COLUMN id SET DEFAULT nextval('public.sys_banner_id_seq'::regclass);


--
-- Name: ums_admin id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ums_admin ALTER COLUMN id SET DEFAULT nextval('public.ums_admin_id_seq'::regclass);


--
-- Name: ums_admin_login_log id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ums_admin_login_log ALTER COLUMN id SET DEFAULT nextval('public.ums_admin_login_log_id_seq'::regclass);


--
-- Name: ums_admin_role_relation id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ums_admin_role_relation ALTER COLUMN id SET DEFAULT nextval('public.ums_admin_role_relation_id_seq'::regclass);


--
-- Name: ums_member id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ums_member ALTER COLUMN id SET DEFAULT nextval('public.ums_member_id_seq'::regclass);


--
-- Name: ums_member_login_log id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ums_member_login_log ALTER COLUMN id SET DEFAULT nextval('public.ums_member_login_log_id_seq'::regclass);


--
-- Name: ums_member_product_category_relation id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ums_member_product_category_relation ALTER COLUMN id SET DEFAULT nextval('public.ums_member_product_category_relation_id_seq'::regclass);


--
-- Name: ums_member_receive_address id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ums_member_receive_address ALTER COLUMN id SET DEFAULT nextval('public.ums_member_receive_address_id_seq'::regclass);


--
-- Name: ums_menu id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ums_menu ALTER COLUMN id SET DEFAULT nextval('public.ums_menu_id_seq'::regclass);


--
-- Name: ums_resource id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ums_resource ALTER COLUMN id SET DEFAULT nextval('public.ums_resource_id_seq'::regclass);


--
-- Name: ums_resource_category id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ums_resource_category ALTER COLUMN id SET DEFAULT nextval('public.ums_resource_category_id_seq'::regclass);


--
-- Name: ums_role id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ums_role ALTER COLUMN id SET DEFAULT nextval('public.ums_role_id_seq'::regclass);


--
-- Name: ums_role_menu_relation id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ums_role_menu_relation ALTER COLUMN id SET DEFAULT nextval('public.ums_role_menu_relation_id_seq'::regclass);


--
-- Name: ums_role_resource_relation id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ums_role_resource_relation ALTER COLUMN id SET DEFAULT nextval('public.ums_role_resource_relation_id_seq'::regclass);


--
-- Name: sys_banner id; Type: DEFAULT; Schema: system; Owner: postgres
--

ALTER TABLE ONLY system.sys_banner ALTER COLUMN id SET DEFAULT nextval('system.sys_banner_id_seq'::regclass);


--
-- Name: ums_admin id; Type: DEFAULT; Schema: users; Owner: postgres
--

ALTER TABLE ONLY users.ums_admin ALTER COLUMN id SET DEFAULT nextval('users.ums_admin_id_seq'::regclass);


--
-- Name: ums_admin_login_log id; Type: DEFAULT; Schema: users; Owner: postgres
--

ALTER TABLE ONLY users.ums_admin_login_log ALTER COLUMN id SET DEFAULT nextval('users.ums_admin_login_log_id_seq'::regclass);


--
-- Name: ums_admin_role_relation id; Type: DEFAULT; Schema: users; Owner: postgres
--

ALTER TABLE ONLY users.ums_admin_role_relation ALTER COLUMN id SET DEFAULT nextval('users.ums_admin_role_relation_id_seq'::regclass);


--
-- Name: ums_member id; Type: DEFAULT; Schema: users; Owner: postgres
--

ALTER TABLE ONLY users.ums_member ALTER COLUMN id SET DEFAULT nextval('users.ums_member_id_seq'::regclass);


--
-- Name: ums_member_login_log id; Type: DEFAULT; Schema: users; Owner: postgres
--

ALTER TABLE ONLY users.ums_member_login_log ALTER COLUMN id SET DEFAULT nextval('users.ums_member_login_log_id_seq'::regclass);


--
-- Name: ums_member_product_category_relation id; Type: DEFAULT; Schema: users; Owner: postgres
--

ALTER TABLE ONLY users.ums_member_product_category_relation ALTER COLUMN id SET DEFAULT nextval('users.ums_member_product_category_relation_id_seq'::regclass);


--
-- Name: ums_member_receive_address id; Type: DEFAULT; Schema: users; Owner: postgres
--

ALTER TABLE ONLY users.ums_member_receive_address ALTER COLUMN id SET DEFAULT nextval('users.ums_member_receive_address_id_seq'::regclass);


--
-- Name: ums_menu id; Type: DEFAULT; Schema: users; Owner: postgres
--

ALTER TABLE ONLY users.ums_menu ALTER COLUMN id SET DEFAULT nextval('users.ums_menu_id_seq'::regclass);


--
-- Name: ums_resource id; Type: DEFAULT; Schema: users; Owner: postgres
--

ALTER TABLE ONLY users.ums_resource ALTER COLUMN id SET DEFAULT nextval('users.ums_resource_id_seq'::regclass);


--
-- Name: ums_resource_category id; Type: DEFAULT; Schema: users; Owner: postgres
--

ALTER TABLE ONLY users.ums_resource_category ALTER COLUMN id SET DEFAULT nextval('users.ums_resource_category_id_seq'::regclass);


--
-- Name: ums_role id; Type: DEFAULT; Schema: users; Owner: postgres
--

ALTER TABLE ONLY users.ums_role ALTER COLUMN id SET DEFAULT nextval('users.ums_role_id_seq'::regclass);


--
-- Name: ums_role_menu_relation id; Type: DEFAULT; Schema: users; Owner: postgres
--

ALTER TABLE ONLY users.ums_role_menu_relation ALTER COLUMN id SET DEFAULT nextval('users.ums_role_menu_relation_id_seq'::regclass);


--
-- Name: ums_role_resource_relation id; Type: DEFAULT; Schema: users; Owner: postgres
--

ALTER TABLE ONLY users.ums_role_resource_relation ALTER COLUMN id SET DEFAULT nextval('users.ums_role_resource_relation_id_seq'::regclass);


--
-- Data for Name: ai_chat_conversation; Type: TABLE DATA; Schema: ai; Owner: postgres
--

COPY ai.ai_chat_conversation (id, member_id, session_id, role, content, model, tokens_used, create_time) FROM stdin;
1	\N	session_19cu1ho5v1c	user	hi	\N	\N	2026-09-26 16:52:17.461177
2	\N	session_fhz3uwf2o45	user	hi	\N	\N	2026-09-26 11:58:25.439595
3	\N	session_fhz3uwf2o45	user	hi	\N	\N	2026-09-26 12:12:38.07357
4	\N	session_fhz3uwf2o45	assistant	Hello! Welcome to NexusCore. How can I assist you today?	\N	\N	2026-09-26 12:12:39.761713
\.


--
-- Data for Name: pms_product_embedding; Type: TABLE DATA; Schema: ai; Owner: postgres
--

COPY ai.pms_product_embedding (product_id, embedding) FROM stdin;
\.


--
-- Data for Name: vector_store; Type: TABLE DATA; Schema: ai; Owner: postgres
--

COPY ai.vector_store (id, content, metadata, embedding) FROM stdin;
\.


--
-- Data for Name: pms_brand; Type: TABLE DATA; Schema: pms; Owner: postgres
--

COPY pms.pms_brand (id, name, show_status, logo) FROM stdin;
3	Aashirvaad	1	http://localhost:9000/nexus-media/public/pms/brands/3/logo.svg
5	Allen Solly	1	http://localhost:9000/nexus-media/public/pms/brands/5/logo.svg
7	Amul	1	http://localhost:9000/nexus-media/public/pms/brands/7/logo.svg
8	Apollo Tyres	1	http://localhost:9000/nexus-media/public/pms/brands/8/logo.svg
10	Arihant	1	http://localhost:9000/nexus-media/public/pms/brands/10/logo.svg
11	Bajaj	1	http://localhost:9000/nexus-media/public/pms/brands/11/logo.svg
13	Bosch	1	http://localhost:9000/nexus-media/public/pms/brands/13/logo.svg
15	Britannia	1	http://localhost:9000/nexus-media/public/pms/brands/15/logo.svg
16	Butterfly	1	http://localhost:9000/nexus-media/public/pms/brands/16/logo.svg
18	Castrol	1	http://localhost:9000/nexus-media/public/pms/brands/18/logo.svg
20	Coca-Cola	1	http://localhost:9000/nexus-media/public/pms/brands/20/logo.svg
21	Cosco	1	http://localhost:9000/nexus-media/public/pms/brands/21/logo.svg
23	Crossword	1	http://localhost:9000/nexus-media/public/pms/brands/23/logo.svg
25	Decathlon	1	http://localhost:9000/nexus-media/public/pms/brands/25/logo.svg
26	Dell	1	http://localhost:9000/nexus-media/public/pms/brands/26/logo.svg
28	Dyson	1	http://localhost:9000/nexus-media/public/pms/brands/28/logo.svg
30	Forest Essentials	1	http://localhost:9000/nexus-media/public/pms/brands/30/logo.svg
32	Google	1	http://localhost:9000/nexus-media/public/pms/brands/32/logo.svg
33	Gulf Oil	1	http://localhost:9000/nexus-media/public/pms/brands/33/logo.svg
35	Havells	1	http://localhost:9000/nexus-media/public/pms/brands/35/logo.svg
37	IKEA	1	http://localhost:9000/nexus-media/public/pms/brands/37/logo.svg
38	ITC	1	http://localhost:9000/nexus-media/public/pms/brands/38/logo.svg
40	Kellogg's	1	http://localhost:9000/nexus-media/public/pms/brands/40/logo.svg
42	LG	1	http://localhost:9000/nexus-media/public/pms/brands/42/logo.svg
44	Lava	1	http://localhost:9000/nexus-media/public/pms/brands/44/logo.svg
45	Lenovo	1	http://localhost:9000/nexus-media/public/pms/brands/45/logo.svg
47	Logitech	1	http://localhost:9000/nexus-media/public/pms/brands/47/logo.svg
49	MTR	1	http://localhost:9000/nexus-media/public/pms/brands/49/logo.svg
51	Manyavar	1	http://localhost:9000/nexus-media/public/pms/brands/51/logo.svg
52	Maybelline	1	http://localhost:9000/nexus-media/public/pms/brands/52/logo.svg
54	Michelin	1	http://localhost:9000/nexus-media/public/pms/brands/54/logo.svg
56	Minimalist	1	http://localhost:9000/nexus-media/public/pms/brands/56/logo.svg
57	Moleskine	1	http://localhost:9000/nexus-media/public/pms/brands/57/logo.svg
59	Motul	1	http://localhost:9000/nexus-media/public/pms/brands/59/logo.svg
61	Neutrogena	1	http://localhost:9000/nexus-media/public/pms/brands/61/logo.svg
62	Nike	1	http://localhost:9000/nexus-media/public/pms/brands/62/logo.svg
1	3M	1	http://localhost:9000/nexus-media/public/pms/brands/1/logo.svg
2	ASUS	1	http://localhost:9000/nexus-media/public/pms/brands/2/logo.svg
4	Adidas	1	http://localhost:9000/nexus-media/public/pms/brands/4/logo.svg
6	Amar Chitra Katha	1	http://localhost:9000/nexus-media/public/pms/brands/6/logo.svg
9	Apple	1	http://localhost:9000/nexus-media/public/pms/brands/9/logo.svg
12	Biotique	1	http://localhost:9000/nexus-media/public/pms/brands/12/logo.svg
14	Bosch India	1	http://localhost:9000/nexus-media/public/pms/brands/14/logo.svg
17	CEAT	1	http://localhost:9000/nexus-media/public/pms/brands/17/logo.svg
19	Clinique	1	http://localhost:9000/nexus-media/public/pms/brands/19/logo.svg
22	Crompton	1	http://localhost:9000/nexus-media/public/pms/brands/22/logo.svg
24	Dabur	1	http://localhost:9000/nexus-media/public/pms/brands/24/logo.svg
27	Dove	1	http://localhost:9000/nexus-media/public/pms/brands/27/logo.svg
65	Noise	1	http://localhost:9000/nexus-media/public/pms/brands/65/logo.svg
66	Nothing	1	http://localhost:9000/nexus-media/public/pms/brands/66/logo.svg
68	O'Reilly	1	http://localhost:9000/nexus-media/public/pms/brands/68/logo.svg
70	Parle	1	http://localhost:9000/nexus-media/public/pms/brands/70/logo.svg
71	Patanjali	1	http://localhost:9000/nexus-media/public/pms/brands/71/logo.svg
73	Penguin Random House	1	http://localhost:9000/nexus-media/public/pms/brands/73/logo.svg
75	Philips	1	http://localhost:9000/nexus-media/public/pms/brands/75/logo.svg
76	Plum	1	http://localhost:9000/nexus-media/public/pms/brands/76/logo.svg
78	Prestige	1	http://localhost:9000/nexus-media/public/pms/brands/78/logo.svg
80	Ray-Ban	1	http://localhost:9000/nexus-media/public/pms/brands/80/logo.svg
81	Raymond	1	http://localhost:9000/nexus-media/public/pms/brands/81/logo.svg
83	S. Chand	1	http://localhost:9000/nexus-media/public/pms/brands/83/logo.svg
85	SS (Sareen Sports)	1	http://localhost:9000/nexus-media/public/pms/brands/85/logo.svg
86	Samsung	1	http://localhost:9000/nexus-media/public/pms/brands/86/logo.svg
88	TVS	1	http://localhost:9000/nexus-media/public/pms/brands/88/logo.svg
89	Tata	1	http://localhost:9000/nexus-media/public/pms/brands/89/logo.svg
91	The Ordinary	1	http://localhost:9000/nexus-media/public/pms/brands/91/logo.svg
93	Unilever	1	http://localhost:9000/nexus-media/public/pms/brands/93/logo.svg
94	Uniqlo	1	http://localhost:9000/nexus-media/public/pms/brands/94/logo.svg
96	Van Heusen	1	http://localhost:9000/nexus-media/public/pms/brands/96/logo.svg
98	W for Woman	1	http://localhost:9000/nexus-media/public/pms/brands/98/logo.svg
99	Wilson	1	http://localhost:9000/nexus-media/public/pms/brands/99/logo.svg
101	Yonex	1	http://localhost:9000/nexus-media/public/pms/brands/101/logo.svg
103	Zebronics	1	http://localhost:9000/nexus-media/public/pms/brands/103/logo.svg
29	FabIndia	1	http://localhost:9000/nexus-media/public/pms/brands/29/logo.svg
31	Fortune	1	http://localhost:9000/nexus-media/public/pms/brands/31/logo.svg
34	H&M	1	http://localhost:9000/nexus-media/public/pms/brands/34/logo.svg
36	Hawkins	1	http://localhost:9000/nexus-media/public/pms/brands/36/logo.svg
39	KENT	1	http://localhost:9000/nexus-media/public/pms/brands/39/logo.svg
41	L'Oréal	1	http://localhost:9000/nexus-media/public/pms/brands/41/logo.svg
43	Lakmé	1	http://localhost:9000/nexus-media/public/pms/brands/43/logo.svg
46	Levi's	1	http://localhost:9000/nexus-media/public/pms/brands/46/logo.svg
48	MRF	1	http://localhost:9000/nexus-media/public/pms/brands/48/logo.svg
50	Mamaearth	1	http://localhost:9000/nexus-media/public/pms/brands/50/logo.svg
53	McGraw Hill	1	http://localhost:9000/nexus-media/public/pms/brands/53/logo.svg
55	Micromax	1	http://localhost:9000/nexus-media/public/pms/brands/55/logo.svg
58	Mother Dairy	1	http://localhost:9000/nexus-media/public/pms/brands/58/logo.svg
60	Nestlé	1	http://localhost:9000/nexus-media/public/pms/brands/60/logo.svg
63	Nivea	1	http://localhost:9000/nexus-media/public/pms/brands/63/logo.svg
64	Nivia	1	http://localhost:9000/nexus-media/public/pms/brands/64/logo.svg
67	Nykaa	1	http://localhost:9000/nexus-media/public/pms/brands/67/logo.svg
69	OnePlus	1	http://localhost:9000/nexus-media/public/pms/brands/69/logo.svg
72	Pearson	1	http://localhost:9000/nexus-media/public/pms/brands/72/logo.svg
74	Peter England	1	http://localhost:9000/nexus-media/public/pms/brands/74/logo.svg
77	Portronics	1	http://localhost:9000/nexus-media/public/pms/brands/77/logo.svg
79	Puma	1	http://localhost:9000/nexus-media/public/pms/brands/79/logo.svg
82	Rupa Publications	1	http://localhost:9000/nexus-media/public/pms/brands/82/logo.svg
84	SG (Sanspareils Greenlands)	1	http://localhost:9000/nexus-media/public/pms/brands/84/logo.svg
87	Sony	1	http://localhost:9000/nexus-media/public/pms/brands/87/logo.svg
90	The North Face	1	http://localhost:9000/nexus-media/public/pms/brands/90/logo.svg
92	Under Armour	1	http://localhost:9000/nexus-media/public/pms/brands/92/logo.svg
95	Usha	1	http://localhost:9000/nexus-media/public/pms/brands/95/logo.svg
97	Vector X	1	http://localhost:9000/nexus-media/public/pms/brands/97/logo.svg
100	Wonderchef	1	http://localhost:9000/nexus-media/public/pms/brands/100/logo.svg
102	Zara	1	http://localhost:9000/nexus-media/public/pms/brands/102/logo.svg
104	boAt	1	http://localhost:9000/nexus-media/public/pms/brands/104/logo.svg
\.


--
-- Data for Name: pms_comment; Type: TABLE DATA; Schema: pms; Owner: postgres
--



--
-- Data for Name: pms_comment_reply; Type: TABLE DATA; Schema: pms; Owner: postgres
--



--
-- Data for Name: pms_product; Type: TABLE DATA; Schema: pms; Owner: postgres
--

COPY pms.pms_product (id, brand_id, product_category_id, product_attribute_category_id, name, pic, product_sn, delete_status, publish_status, new_status, recommend_status, verify_status, sort, sale, sub_title, unit, weight, service_ids, keywords, note, promotion_start_time, promotion_end_time, promotion_per_limit, promotion_type, brand_name, product_category_name, description, detail_html, vendor_id, detail_title, detail_desc) FROM stdin;
4	44	4	8	Tab Pro 11	http://localhost:9000/nexus-media/public/pms/products/4/main.svg	P004	0	1	1	1	1	0	\N
150	88	43	41	Portable Tyre Inflator	http://localhost:9000/nexus-media/public/pms/products/150/main.svg	P150	0	1	1	1	1	97	\N
10	69	4	8	Pad 3	http://localhost:9000/nexus-media/public/pms/products/10/main.svg	P010	0	1	1	1	1	0	\N
7	26	5	3	Inspiron 16 Plus	http://localhost:9000/nexus-media/public/pms/products/7/main.svg	P007	0	1	1	1	1	0	\N
8	45	5	3	ThinkPad E16	http://localhost:9000/nexus-media/public/pms/products/8/main.svg	P008	0	1	1	1	1	0	\N
9	2	5	3	Vivobook S 16	http://localhost:9000/nexus-media/public/pms/products/9/main.svg	P009	0	1	1	1	1	0	\N
15	47	6	4	MX Brio Webcam	http://localhost:9000/nexus-media/public/pms/products/15/main.svg	P015	0	1	1	1	1	0	\N
14	104	7	2	Lunar Pro Smartwatch	http://localhost:9000/nexus-media/public/pms/products/14/main.svg	P014	0	1	1	1	1	0	\N
17	42	8	5	UltraGear 27GS60F	http://localhost:9000/nexus-media/public/pms/products/17/main.svg	P017	0	1	1	1	1	0	\N
18	86	8	5	Odyssey G6 27	http://localhost:9000/nexus-media/public/pms/products/18/main.svg	P018	0	1	1	1	1	0	\N
2	86	3	6	Galaxy S26	http://localhost:9000/nexus-media/public/pms/products/2/main.svg	P002	0	1	1	1	1	0	\N
3	32	3	6	Pixel 11	http://localhost:9000/nexus-media/public/pms/products/3/main.svg	P003	0	1	1	1	1	0	\N
19	103	9	9	Transformer Wireless Keyboard	http://localhost:9000/nexus-media/public/pms/products/19/main.svg	P019	0	1	1	1	1	0	\N
20	46	11	12	511 Slim Jeans	http://localhost:9000/nexus-media/public/pms/products/20/main.svg	P020	0	1	1	1	1	0	\N
5	69	3	6	OnePlus 14	http://localhost:9000/nexus-media/public/pms/products/5/main.svg	P005	0	1	1	1	1	0	\N
22	74	11	12	Regular Formal Shirt	http://localhost:9000/nexus-media/public/pms/products/22/main.svg	P022	0	1	1	1	1	0	\N
23	81	11	12	Classic Formal Trousers	http://localhost:9000/nexus-media/public/pms/products/23/main.svg	P023	0	1	1	1	1	0	\N
24	94	12	11	Supima Cotton T-Shirt	http://localhost:9000/nexus-media/public/pms/products/24/main.svg	P024	0	1	1	1	1	0	\N
25	102	12	11	Relaxed Fit Blouse	http://localhost:9000/nexus-media/public/pms/products/25/main.svg	P025	0	1	1	1	1	0	\N
26	34	12	11	Ribbed Midi Dress	http://localhost:9000/nexus-media/public/pms/products/26/main.svg	P026	0	1	1	1	1	0	\N
34	80	13	13	Aviator Classic	http://localhost:9000/nexus-media/public/pms/products/34/main.svg	P034	0	1	1	1	1	0	\N
35	96	13	13	Leather Finish Belt	http://localhost:9000/nexus-media/public/pms/products/35/main.svg	P035	0	1	1	1	1	0	\N
36	94	13	13	Round Mini Shoulder Bag	http://localhost:9000/nexus-media/public/pms/products/36/main.svg	P036	0	1	1	1	1	0	\N
37	62	13	13	Heritage Backpack	http://localhost:9000/nexus-media/public/pms/products/37/main.svg	P037	0	1	1	1	1	0	\N
38	4	13	13	Classic 3S Cap	http://localhost:9000/nexus-media/public/pms/products/38/main.svg	P038	0	1	1	1	1	0	\N
30	62	14	14	Air Max 270	http://localhost:9000/nexus-media/public/pms/products/30/main.svg	P030	0	1	1	1	1	0	\N
32	79	14	14	Velocity Nitro 3	http://localhost:9000/nexus-media/public/pms/products/32/main.svg	P032	0	1	1	1	1	0	\N
39	37	18	17	MALM 6-Drawer Chest	http://localhost:9000/nexus-media/public/pms/products/39/main.svg	P039	0	1	1	1	1	0	\N
41	37	18	17	BILLY Bookcase	http://localhost:9000/nexus-media/public/pms/products/41/main.svg	P041	0	1	1	1	1	0	\N
33	90	14	14	VECTIV Enduris	http://localhost:9000/nexus-media/public/pms/products/33/main.svg	P033	0	1	1	1	1	0	\N
12	66	2	7	Ear (3)	http://localhost:9000/nexus-media/public/pms/products/12/main.svg	P012	0	1	1	1	1	0	\N
75	67	21	22	ManGlow Beard Wash	http://localhost:9000/nexus-media/public/pms/products/75/main.svg	P075	0	1	1	1	1	0	\N
43	28	19	18	V15 Detect	http://localhost:9000/nexus-media/public/pms/products/43/main.svg	P043	0	1	1	1	1	0	\N
48	42	19	18	6.5kg Front Load Washer	http://localhost:9000/nexus-media/public/pms/products/48/main.svg	P048	0	1	1	1	1	0	\N
49	13	19	18	8kg Front Load Washer	http://localhost:9000/nexus-media/public/pms/products/49/main.svg	P049	0	1	1	1	1	0	\N
76	76	21	22	Green Tea Shave Gel	http://localhost:9000/nexus-media/public/pms/products/76/main.svg	P076	0	1	1	1	1	0	\N
66	41	22	23	True Match Foundation	http://localhost:9000/nexus-media/public/pms/products/66/main.svg	P066	0	1	1	1	1	0	\N
67	52	22	23	Fit Me Foundation	http://localhost:9000/nexus-media/public/pms/products/67/main.svg	P067	0	1	1	1	1	0	\N
68	43	22	23	9to5 Primer + Matte Lip Color	http://localhost:9000/nexus-media/public/pms/products/68/main.svg	P068	0	1	1	1	1	0	\N
69	67	22	23	SkinGenius Foundation	http://localhost:9000/nexus-media/public/pms/products/69/main.svg	P069	0	1	1	1	1	0	\N
45	78	17	19	Air Fryer Pro	http://localhost:9000/nexus-media/public/pms/products/45/main.svg	P045	0	1	1	1	1	0	\N
46	36	17	19	Contura Pressure Cooker	http://localhost:9000/nexus-media/public/pms/products/46/main.svg	P046	0	1	1	1	1	0	\N
47	39	17	19	Grand RO Water Purifier	http://localhost:9000/nexus-media/public/pms/products/47/main.svg	P047	0	1	1	1	1	0	\N
71	27	23	24	Intense Repair Shampoo	http://localhost:9000/nexus-media/public/pms/products/71/main.svg	P071	0	1	1	1	1	0	\N
81	25	26	26	500 Adjustable Dumbbell	http://localhost:9000/nexus-media/public/pms/products/81/main.svg	P081	0	1	1	1	1	0	\N
82	64	26	26	Hex Dumbbell 10kg	http://localhost:9000/nexus-media/public/pms/products/82/main.svg	P082	0	1	1	1	1	0	\N
77	92	29	28	Charged Assert 10	http://localhost:9000/nexus-media/public/pms/products/77/main.svg	P077	0	1	1	1	1	0	\N
78	25	29	28	Kalenji Run Comfort	http://localhost:9000/nexus-media/public/pms/products/78/main.svg	P078	0	1	1	1	1	0	\N
79	64	29	28	Running Shoes Racer	http://localhost:9000/nexus-media/public/pms/products/79/main.svg	P079	0	1	1	1	1	0	\N
80	97	29	28	Sprint Running Shoes	http://localhost:9000/nexus-media/public/pms/products/80/main.svg	P080	0	1	1	1	1	0	\N
72	41	23	24	Absolut Repair Shampoo	http://localhost:9000/nexus-media/public/pms/products/72/main.svg	P072	0	1	1	1	1	0	\N
73	50	23	24	Onion Hair Oil	http://localhost:9000/nexus-media/public/pms/products/73/main.svg	P073	0	1	1	1	1	0	\N
58	91	24	21	Niacinamide 10% + Zinc 1%	http://localhost:9000/nexus-media/public/pms/products/58/main.svg	P058	0	1	1	1	1	0	\N
59	63	24	21	Soft Moisturizing Cream	http://localhost:9000/nexus-media/public/pms/products/59/main.svg	P059	0	1	1	1	1	0	\N
60	19	24	21	Moisture Surge 100H	http://localhost:9000/nexus-media/public/pms/products/60/main.svg	P060	0	1	1	1	1	0	\N
61	61	24	21	Hydro Boost Water Gel	http://localhost:9000/nexus-media/public/pms/products/61/main.svg	P061	0	1	1	1	1	0	\N
53	16	16	16	Rapid 3 Burner Gas Stove	http://localhost:9000/nexus-media/public/pms/products/53/main.svg	P053	0	1	1	1	1	0	\N
85	25	27	27	Basic Yoga Mat	http://localhost:9000/nexus-media/public/pms/products/85/main.svg	P085	0	1	1	1	1	0	\N
88	99	28	29	Pro Staff 97 Tennis Racket	http://localhost:9000/nexus-media/public/pms/products/88/main.svg	P088	0	1	1	1	1	0	\N
89	101	28	29	Astrox 100ZZ	http://localhost:9000/nexus-media/public/pms/products/89/main.svg	P089	0	1	1	1	1	0	\N
90	85	28	29	Skyer Cricket Bat	http://localhost:9000/nexus-media/public/pms/products/90/main.svg	P090	0	1	1	1	1	0	\N
96	73	31	34	The Midnight Library	http://localhost:9000/nexus-media/public/pms/products/96/main.svg	P096	0	1	1	1	1	0	\N
97	73	31	34	1984	http://localhost:9000/nexus-media/public/pms/products/97/main.svg	P097	0	1	1	1	1	0	\N
98	82	31	34	The Silent Patient	http://localhost:9000/nexus-media/public/pms/products/98/main.svg	P098	0	1	1	1	1	0	\N
99	6	31	34	Mahabharata	http://localhost:9000/nexus-media/public/pms/products/99/main.svg	P099	0	1	1	1	1	0	\N
109	57	32	33	Classic Notebook Large	http://localhost:9000/nexus-media/public/pms/products/109/main.svg	P109	0	1	1	1	1	0	\N
110	23	32	33	Premium Ruled Notebook	http://localhost:9000/nexus-media/public/pms/products/110/main.svg	P110	0	1	1	1	1	0	\N
112	83	32	33	Graph Paper Notebook	http://localhost:9000/nexus-media/public/pms/products/112/main.svg	P112	0	1	1	1	1	0	\N
113	10	32	33	Exam Planner Notebook	http://localhost:9000/nexus-media/public/pms/products/113/main.svg	P113	0	1	1	1	1	0	\N
114	73	32	33	Reading Journal	http://localhost:9000/nexus-media/public/pms/products/114/main.svg	P114	0	1	1	1	1	0	\N
100	73	33	31	Atomic Habits	http://localhost:9000/nexus-media/public/pms/products/100/main.svg	P100	0	1	1	1	1	0	\N
101	53	33	31	The Lean Startup	http://localhost:9000/nexus-media/public/pms/products/101/main.svg	P101	0	1	1	1	1	0	\N
120	15	36	38	Good Day Cashew Cookies	http://localhost:9000/nexus-media/public/pms/products/120/main.svg	P120	0	1	1	1	1	0	\N
121	70	36	38	Hide & Seek Biscuits	http://localhost:9000/nexus-media/public/pms/products/121/main.svg	P121	0	1	1	1	1	0	\N
122	38	36	38	Sunfeast Dark Fantasy	http://localhost:9000/nexus-media/public/pms/products/122/main.svg	P122	0	1	1	1	1	0	\N
115	60	38	37	Nescafé Classic Coffee	http://localhost:9000/nexus-media/public/pms/products/115/main.svg	P115	0	1	1	1	1	0	\N
116	20	38	37	Coca-Cola Original	http://localhost:9000/nexus-media/public/pms/products/116/main.svg	P116	0	1	1	1	1	0	\N
117	7	38	37	Amul Taaza Milk	http://localhost:9000/nexus-media/public/pms/products/117/main.svg	P117	0	1	1	1	1	0	\N
118	89	38	37	Tata Tea Gold	http://localhost:9000/nexus-media/public/pms/products/118/main.svg	P118	0	1	1	1	1	0	\N
119	58	38	37	Toned Milk	http://localhost:9000/nexus-media/public/pms/products/119/main.svg	P119	0	1	1	1	1	0	\N
102	72	33	31	Sapiens Study Guide	http://localhost:9000/nexus-media/public/pms/products/102/main.svg	P102	0	1	1	1	1	0	\N
104	68	34	32	Designing Data-Intensive Applications	http://localhost:9000/nexus-media/public/pms/products/104/main.svg	P104	0	1	1	1	1	0	\N
105	53	34	32	Cracking the Coding Interview	http://localhost:9000/nexus-media/public/pms/products/105/main.svg	P105	0	1	1	1	1	0	\N
106	72	34	32	Java Programming Complete	http://localhost:9000/nexus-media/public/pms/products/106/main.svg	P106	0	1	1	1	1	0	\N
125	40	37	39	Corn Flakes	http://localhost:9000/nexus-media/public/pms/products/125/main.svg	P125	0	1	1	1	1	0	\N
129	31	39	36	Sunflower Oil	http://localhost:9000/nexus-media/public/pms/products/129/main.svg	P129	0	1	1	1	1	0	\N
130	71	39	36	Dalia	http://localhost:9000/nexus-media/public/pms/products/130/main.svg	P130	0	1	1	1	1	0	\N
132	7	39	36	Pure Ghee	http://localhost:9000/nexus-media/public/pms/products/132/main.svg	P132	0	1	1	1	1	0	\N
138	18	41	43	Magnatec 5W-30	http://localhost:9000/nexus-media/public/pms/products/138/main.svg	P138	0	1	1	1	1	0	\N
139	59	41	43	8100 X-Cess 5W-40	http://localhost:9000/nexus-media/public/pms/products/139/main.svg	P139	0	1	1	1	1	0	\N
141	1	41	43	Perfect-It Rubbing Compound	http://localhost:9000/nexus-media/public/pms/products/141/main.svg	P141	0	1	1	1	1	0	\N
151	18	41	43	Engine Flush	http://localhost:9000/nexus-media/public/pms/products/151/main.svg	P151	0	1	1	1	1	0	\N
142	54	42	42	Energy XM2+ Tyre	http://localhost:9000/nexus-media/public/pms/products/142/main.svg	P142	0	1	1	1	1	0	\N
143	48	42	42	Perfinza Tyre	http://localhost:9000/nexus-media/public/pms/products/143/main.svg	P143	0	1	1	1	1	0	\N
144	8	42	42	Alnac 4G Tyre	http://localhost:9000/nexus-media/public/pms/products/144/main.svg	P144	0	1	1	1	1	0	\N
145	17	42	42	Milaze Tyre	http://localhost:9000/nexus-media/public/pms/products/145/main.svg	P145	0	1	1	1	1	0	\N
134	1	43	41	Car Care Microfiber Cloth	http://localhost:9000/nexus-media/public/pms/products/134/main.svg	P134	0	1	1	1	1	0	\N
135	14	43	41	Car Air Filter	http://localhost:9000/nexus-media/public/pms/products/135/main.svg	P135	0	1	1	1	1	0	\N
136	88	43	41	Car Battery Charger	http://localhost:9000/nexus-media/public/pms/products/136/main.svg	P136	0	1	1	1	1	0	\N
137	17	43	41	Tubeless Valve Kit	http://localhost:9000/nexus-media/public/pms/products/137/main.svg	P137	0	1	1	1	1	0	\N
146	88	42	42	Eco Grip 2W Tyre	http://localhost:9000/nexus-media/public/pms/products/146/main.svg	P146	0	1	1	1	1	0	\N
11	77	2	7	Muffs M2 Wireless Headphones	http://localhost:9000/nexus-media/public/pms/products/11/main.svg	P011	0	1	1	1	1	0	\N
21	5	11	12	Slim Fit Oxford Shirt	http://localhost:9000/nexus-media/public/pms/products/21/main.svg	P021	0	1	1	1	1	0	\N
31	4	14	14	Ultraboost Light	http://localhost:9000/nexus-media/public/pms/products/31/main.svg	P031	0	1	1	1	1	0	\N
54	22	16	16	Steel Essential Kettle	http://localhost:9000/nexus-media/public/pms/products/54/main.svg	P054	0	1	1	1	1	0	\N
42	75	17	19	Airfryer 3000 Series	http://localhost:9000/nexus-media/public/pms/products/42/main.svg	P042	0	1	1	1	1	0	\N
40	37	18	17	LACK Coffee Table	http://localhost:9000/nexus-media/public/pms/products/40/main.svg	P040	0	1	1	1	1	0	\N
74	63	21	22	Men Sensitive Shaving Gel	http://localhost:9000/nexus-media/public/pms/products/74/main.svg	P074	0	1	1	1	1	0	\N
64	30	24	21	Facial Tonic Mist	http://localhost:9000/nexus-media/public/pms/products/64/main.svg	P064	0	1	1	1	1	0	\N
124	24	36	38	Real Fruit Drink	http://localhost:9000/nexus-media/public/pms/products/124/main.svg	P124	0	1	1	1	1	0	\N
128	89	37	39	Sampann Poha	http://localhost:9000/nexus-media/public/pms/products/128/main.svg	P128	0	1	1	1	1	0	\N
13	65	2	7	Buds X Prime	http://localhost:9000/nexus-media/public/pms/products/13/main.svg	P013	0	1	1	1	1	0	\N
16	87	2	7	WH-1000XM6	http://localhost:9000/nexus-media/public/pms/products/16/main.svg	P016	0	1	1	1	1	0	\N
6	55	3	6	IN Note 3	http://localhost:9000/nexus-media/public/pms/products/6/main.svg	P006	0	1	1	1	1	0	\N
27	29	12	11	Cotton Kurta	http://localhost:9000/nexus-media/public/pms/products/27/main.svg	P027	0	1	1	1	1	0	\N
28	51	12	11	Embroidered Kurta	http://localhost:9000/nexus-media/public/pms/products/28/main.svg	P028	0	1	1	1	1	0	\N
29	98	12	11	Straight Kurta Set	http://localhost:9000/nexus-media/public/pms/products/29/main.svg	P029	0	1	1	1	1	0	\N
55	100	16	16	Granite Fry Pan	http://localhost:9000/nexus-media/public/pms/products/55/main.svg	P055	0	1	1	1	1	0	\N
56	78	16	16	Omega Deluxe Pan	http://localhost:9000/nexus-media/public/pms/products/56/main.svg	P056	0	1	1	1	1	0	\N
57	36	16	16	Futura Nonstick Tawa	http://localhost:9000/nexus-media/public/pms/products/57/main.svg	P057	0	1	1	1	1	0	\N
44	13	17	19	Serie 4 Dishwasher	http://localhost:9000/nexus-media/public/pms/products/44/main.svg	P044	0	1	1	1	1	0	\N
50	11	19	18	Maharaja Tower Fan	http://localhost:9000/nexus-media/public/pms/products/50/main.svg	P050	0	1	1	1	1	0	\N
51	35	19	18	Aqua Plus Water Heater	http://localhost:9000/nexus-media/public/pms/products/51/main.svg	P051	0	1	1	1	1	0	\N
52	95	19	18	Mist Air Icy Fan	http://localhost:9000/nexus-media/public/pms/products/52/main.svg	P052	0	1	1	1	1	0	\N
70	12	22	23	Bio Coconut Whitening Cream	http://localhost:9000/nexus-media/public/pms/products/70/main.svg	P070	0	1	1	1	1	0	\N
62	50	24	21	Vitamin C Face Serum	http://localhost:9000/nexus-media/public/pms/products/62/main.svg	P062	0	1	1	1	1	0	\N
63	56	24	21	2% Salicylic Acid Serum	http://localhost:9000/nexus-media/public/pms/products/63/main.svg	P063	0	1	1	1	1	0	\N
65	76	24	21	Green Tea Face Wash	http://localhost:9000/nexus-media/public/pms/products/65/main.svg	P065	0	1	1	1	1	0	\N
84	97	26	26	Power Training Gloves	http://localhost:9000/nexus-media/public/pms/products/84/main.svg	P084	0	1	1	1	1	0	\N
83	21	26	26	Resistance Tube Set	http://localhost:9000/nexus-media/public/pms/products/83/main.svg	P083	0	1	1	1	1	0	\N
86	64	27	27	TPE Yoga Mat	http://localhost:9000/nexus-media/public/pms/products/86/main.svg	P086	0	1	1	1	1	0	\N
87	21	27	27	Yoga Block Pair	http://localhost:9000/nexus-media/public/pms/products/87/main.svg	P087	0	1	1	1	1	0	\N
92	25	28	29	Quechua Hiking Backpack 40L	http://localhost:9000/nexus-media/public/pms/products/92/main.svg	P092	0	1	1	1	1	0	\N
1	9	3	6	iPhone 17	http://localhost:9000/nexus-media/public/pms/products/1/main.svg	P001	0	1	1	1	1	14	\N
93	64	28	29	Storm Football	http://localhost:9000/nexus-media/public/pms/products/93/main.svg	P093	0	1	1	1	1	0	\N
94	21	28	29	Club Volleyball	http://localhost:9000/nexus-media/public/pms/products/94/main.svg	P094	0	1	1	1	1	0	\N
95	99	28	29	NBA Official Basketball	http://localhost:9000/nexus-media/public/pms/products/95/main.svg	P095	0	1	1	1	1	0	\N
91	84	28	29	Players English Willow Bat	http://localhost:9000/nexus-media/public/pms/products/91/main.svg	P091	0	1	1	1	1	0	\N
111	82	32	33	Executive Journal	http://localhost:9000/nexus-media/public/pms/products/111/main.svg	P111	0	1	1	1	1	0	\N
107	83	34	32	Data Structures in C	http://localhost:9000/nexus-media/public/pms/products/107/main.svg	P107	0	1	1	1	1	0	\N
108	10	34	32	Python for Competitive Programming	http://localhost:9000/nexus-media/public/pms/products/108/main.svg	P108	0	1	1	1	1	0	\N
103	68	34	32	Clean Code	http://localhost:9000/nexus-media/public/pms/products/103/main.svg	P103	0	1	1	1	1	0	\N
123	60	36	38	Munch Chocolate Bar	http://localhost:9000/nexus-media/public/pms/products/123/main.svg	P123	0	1	1	1	1	0	\N
126	49	37	39	3 Minute Breakfast Poha	http://localhost:9000/nexus-media/public/pms/products/126/main.svg	P126	0	1	1	1	1	0	\N
131	93	39	36	Kissan Tomato Ketchup	http://localhost:9000/nexus-media/public/pms/products/131/main.svg	P131	0	1	1	1	1	0	\N
140	33	41	43	Formula G 5W-30	http://localhost:9000/nexus-media/public/pms/products/140/main.svg	P140	0	1	1	1	1	0	\N
149	1	43	41	12V Car Vacuum Cleaner	http://localhost:9000/nexus-media/public/pms/products/149/main.svg	P149	0	1	1	1	1	0	\N
127	3	37	39	Multigrain Atta	http://localhost:9000/nexus-media/public/pms/products/127/main.svg	P127	0	1	1	1	1	0	\N
133	58	39	36	Paneer Fresh	http://localhost:9000/nexus-media/public/pms/products/133/main.svg	P133	0	1	1	1	1	0	\N
147	14	42	42	Platinum Spark Plug	http://localhost:9000/nexus-media/public/pms/products/147/main.svg	P147	0	1	1	1	1	0	\N
148	17	42	42	Car Brake Pad Set	http://localhost:9000/nexus-media/public/pms/products/148/main.svg	P148	0	1	1	1	1	0	\N
152	59	41	43	Chain Clean	http://localhost:9000/nexus-media/public/pms/products/152/main.svg	P152	0	1	1	1	1	1	\N
\.


--
-- Data for Name: pms_product_attribute; Type: TABLE DATA; Schema: pms; Owner: postgres
--

COPY pms.pms_product_attribute (id, product_attribute_category_id, name, select_type, input_type, input_list, sort, type) FROM stdin;
34	9	Layout	0	1	Standard	0	1
35	9	Switch Type	0	1	Premium Material	0	1
36	11	Size	0	1	L,M,S,XL,L,M,XL	0	0
37	11	Color	0	1	Black,Standard	0	0
38	11	Material	0	1	Premium Material	0	1
39	11	Fit	0	1	Standard	0	1
40	12	Size	0	1	L,M,S,XL	0	0
41	12	Color	0	1	Black	0	0
42	12	Material	0	1	Premium Material	0	1
43	12	Fit	0	1	Standard	0	1
44	13	Color	0	1	Black,Silver,Standard	0	0
45	13	Size	0	1	L,M,S,L,M,XL,Standard	0	0
46	13	Material	0	1	Premium Material	0	1
47	14	Size (US)	0	1	Standard	0	0
48	14	Color	0	1	Black,White	0	0
49	14	Material	0	1	Premium Material	0	1
50	14	Shoe Type	0	1	Premium Material	0	1
51	16	Size	0	1	24cm,26cm,28cm,Standard	0	0
52	16	Capacity	0	1	1.5L,2L,Standard	0	0
53	16	Material	0	1	Premium Material	0	1
54	16	Color	0	1	Standard	0	1
55	17	Color	0	1	Black,Black,Silver	0	0
56	17	Material	0	1	Premium Material	0	1
57	17	Dimensions	0	1	Standard Size	0	1
58	18	Capacity	0	1	10L,15L,6.5kg,8kg,Standard	0	0
59	18	Color	0	1	Black,Silver,Standard	0	0
60	18	Power Consumption	0	1	Standard	0	1
61	19	Capacity	0	1	12-place,14-place,3L,5L,4L,6L,Standard	0	0
62	19	Color	0	1	Black,Standard	0	0
63	19	Power Consumption	0	1	Standard	0	1
64	21	Skin Type	0	1	Premium Material	0	1
65	21	Ingredient	0	1	Premium Material	0	1
66	21	Pack Size	0	1	Standard	0	0
67	21	SPF	0	1	Standard	0	1
68	22	Pack Size	0	1	Standard	0	0
69	22	Skin Type	0	1	Premium Material	0	1
70	22	Ingredient	0	1	Premium Material	0	1
71	23	Shade	0	1	110 Porcelain,120 Classic Ivory,130 Buff Beige,220 Natural Beige,Standard	0	0
72	23	Finish	0	1	Matte Finish	0	1
73	23	Skin Type	0	1	Premium Material	0	1
74	24	Pack Size	0	1	Standard	0	0
75	24	Hair Type	0	1	Premium Material	0	1
76	24	Ingredient	0	1	Premium Material	0	1
77	26	Weight	0	1	Standard	0	1
78	26	Material	0	1	Premium Material	0	1
79	26	Size	0	1	Standard Size	0	1
80	26	Color	0	1	Standard	0	1
81	27	Color	0	1	Black,Silver	0	0
82	27	Material	0	1	Premium Material	0	1
83	27	Size	0	1	Standard Size	0	1
84	28	Size (US)	0	1	Standard	0	0
85	28	Color	0	1	Black,White	0	0
86	28	Cushioning	0	1	Standard	0	1
87	28	Shoe Type	0	1	Premium Material	0	1
88	29	Size	0	1	10,8,9,L,M,XL,Standard	0	0
89	29	Material	0	1	Premium Material	0	1
90	29	Color	0	1	Black,Black,Silver,Standard	0	0
91	29	Sport Type	0	1	Premium Material	0	1
92	31	Format	0	1	Hardcover,Kindle,Paperback	0	0
93	31	Language	0	1	Standard	0	1
94	31	Publication Year	0	1	Standard	0	1
95	31	Author	0	1	Standard	0	1
96	32	Format	0	1	Hardcover,Kindle,Paperback	0	0
97	32	Language	0	1	Standard	0	1
98	32	Publication Year	0	1	Standard	0	1
99	32	Author	0	1	Standard	0	1
100	33	Size	0	1	A5,A6,Standard	0	0
101	33	Color	0	1	Black,Black,Silver,Standard	0	0
102	33	Paper Type	0	1	Premium Material	0	1
103	33	Page Count	0	1	Standard	0	1
1	2	Color	0	1	Black,Silver	0	0
2	2	Strap Size	0	1	Standard Size	0	1
3	2	Display Size	0	1	Standard Size	0	1
4	2	GPS	0	1	Yes	0	1
5	2	Water Resistance	0	1	Yes	0	1
6	3	RAM	0	1	16GB,32GB	0	0
7	3	Storage	0	1	1TB,512GB	0	0
8	3	Color	0	1	Black,Silver	0	0
9	3	Processor	0	1	Latest Gen Processor	0	1
10	3	Screen Size	0	1	Standard Size	0	1
11	4	Color	0	1	Black	0	0
12	4	Resolution	0	1	4K UHD	0	1
13	4	Sensor Type	0	1	Premium Material	0	1
14	4	Connectivity	0	1	Standard	0	1
15	5	Size (inches)	0	1	Standard	0	0
16	5	Resolution	0	1	2560x1440,3840x2160	0	0
17	5	Refresh Rate	0	1	Standard	0	1
18	5	Panel Type	0	1	Premium Material	0	1
19	6	Color	0	1	Black,Silver	0	0
20	6	Storage	0	1	128GB,256GB	0	0
21	6	RAM	0	1	Standard	0	1
22	6	Screen Size	0	1	Standard Size	0	1
23	6	Camera	0	1	Advanced Dual Camera	0	1
24	7	Color	0	1	Black,Black,Silver	0	0
25	7	Connectivity	0	1	Standard	0	1
26	7	ANC	0	1	Yes	0	1
27	7	Battery Life	0	1	24 Hours	0	1
28	8	Color	0	1	Black,Silver	0	0
29	8	Storage	0	1	128GB,256GB	0	0
30	8	RAM	0	1	Standard	0	1
31	8	Screen Size	0	1	Standard Size	0	1
32	9	Color	0	1	Black,Silver	0	0
33	9	Connectivity	0	1	Standard	0	1
130	1	Color	1	1	Black,White	0	0
131	1	Storage	1	1	64GB,128GB	0	1
104	34	Format	0	1	Hardcover,Kindle,Paperback	0	0
105	34	Language	0	1	Standard	0	1
106	34	Publication Year	0	1	Standard	0	1
107	34	Author	0	1	Standard	0	1
108	36	Pack Size	0	1	Standard	0	0
109	36	Vegetarian	0	1	Standard	0	1
110	36	Brand Origin	0	1	Standard	0	1
111	37	Pack Size	0	1	Standard	0	0
112	37	Beverage Type	0	1	Premium Material	0	1
113	37	Flavor	0	1	Standard	0	1
114	38	Pack Size	0	1	Standard	0	0
115	38	Flavor	0	1	Standard	0	1
116	38	Vegetarian	0	1	Standard	0	1
117	39	Pack Size	0	1	Standard	0	0
118	39	Ingredients	0	1	Premium Material	0	1
119	39	Vegetarian	0	1	Standard	0	1
120	41	Color	0	1	Black,Black,Silver,Standard	0	0
121	41	Size	0	1	100ml,30ml,50ml,Standard	0	0
122	41	Material	0	1	Premium Material	0	1
123	41	Compatibility	0	1	Standard	0	1
124	42	Size	0	1	195/65 R15,205/55 R16,215/60 R17,Standard	0	0
125	42	Vehicle Compatibility	0	1	Standard	0	1
126	42	Part Type	0	1	Premium Material	0	1
127	43	Pack Size	0	1	Standard	0	0
128	43	Product Type	0	1	Premium Material	0	1
129	43	Suitable For	0	1	Standard	0	1
132	1	TestAttr	0	1	\N	0	0
\.


--
-- Data for Name: pms_product_attribute_category; Type: TABLE DATA; Schema: pms; Owner: postgres
--

COPY pms.pms_product_attribute_category (id, name, attribute_count, param_count) FROM stdin;
2	Laptop Specs	1	4
3	Audio Specs	3	2
4	Shoe Sizes	1	3
5	Apparel Sizes	2	2
6	Beauty Shades	2	3
7	Book Editions	1	3
8	Grocery Pack	2	2
9	Display Specs	1	3
10	General Variants	0	0
1	Smartphone Specs	2	1
\.


--
-- Data for Name: pms_product_attribute_value; Type: TABLE DATA; Schema: pms; Owner: postgres
--

COPY pms.pms_product_attribute_value (id, product_id, product_attribute_id, value) FROM stdin;
1	4	28	Black,Silver

2	4	29	128GB,256GB

3	4	30	Standard

4	4	31	Standard Size

5	10	28	Black,Silver

6	10	29	128GB,256GB

7	10	30	Standard

8	10	31	Standard Size

9	20	40	L,M,S,XL

10	20	41	Black

11	20	42	Premium Material

12	20	43	Standard

13	19	32	Black,Silver

14	19	33	Standard

15	19	34	Standard

16	19	35	Premium Material

17	24	36	L,M,XL

18	24	37	Standard

19	24	38	Premium Material

20	24	39	Standard

21	17	15	Standard

22	17	16	2560x1440,3840x2160

23	17	17	Standard

24	17	18	Premium Material

25	2	19	Black,Silver

26	2	20	128GB,256GB

27	2	21	Standard

28	2	22	Standard Size

29	2	23	Advanced Dual Camera

30	3	19	Black,Silver

31	3	20	128GB,256GB

32	3	21	Standard

33	3	22	Standard Size

34	3	23	Advanced Dual Camera

35	30	47	Standard

36	30	48	Black,White

37	30	49	Premium Material

38	30	50	Premium Material

39	25	36	L,M,S,XL

40	25	37	Black

41	25	38	Premium Material

42	25	39	Standard

43	32	47	Standard

44	32	48	Black,White

45	32	49	Premium Material

46	32	50	Premium Material

47	18	15	Standard

48	18	16	2560x1440,3840x2160

49	18	17	Standard

50	18	18	Premium Material

51	12	24	Black,Silver

52	12	25	Standard

53	12	26	Yes

54	12	27	24 Hours

55	33	47	Standard

56	33	48	Black,White

57	33	49	Premium Material

58	33	50	Premium Material

59	13	24	Black

60	13	25	Standard

61	13	26	Yes

62	13	27	24 Hours

63	16	24	Black,Silver

64	16	25	Standard

65	16	26	Yes

66	16	27	24 Hours

67	39	55	Black

68	39	56	Premium Material

69	39	57	Standard Size

70	41	55	Black,Silver

71	41	56	Premium Material

72	41	57	Standard Size

73	22	40	L,M,S,XL

74	22	41	Black

75	22	42	Premium Material

76	22	43	Standard

77	23	40	L,M,S,XL

78	23	41	Black

79	23	42	Premium Material

80	23	43	Standard

81	34	44	Black,Silver

82	34	45	Standard

83	34	46	Premium Material

84	35	44	Standard

85	35	45	L,M,S

86	35	46	Premium Material

87	36	44	Black,Silver

88	36	45	Standard

89	36	46	Premium Material

90	14	1	Black,Silver

91	14	2	Standard Size

92	14	3	Standard Size

93	14	4	Yes

94	14	5	Yes

95	7	6	16GB,32GB

96	7	7	1TB,512GB

97	7	8	Black,Silver

98	7	9	Latest Gen Processor

99	7	10	Standard Size

100	15	11	Black

101	15	12	4K UHD

102	15	13	Premium Material

103	15	14	Standard

104	5	19	Black,Silver

105	5	20	128GB,256GB

106	5	21	Standard

107	5	22	Standard Size

108	5	23	Advanced Dual Camera

109	26	36	L,M,S,XL

110	26	37	Black

111	26	38	Premium Material

112	26	39	Standard

113	27	36	L,M,S,XL

114	27	37	Black

115	27	38	Premium Material

116	27	39	Standard

117	58	64	Premium Material

118	58	65	Premium Material

119	58	66	Standard

120	58	67	Standard

121	75	68	Standard

122	75	69	Premium Material

123	75	70	Premium Material

124	76	68	Standard

125	76	69	Premium Material

126	76	70	Premium Material

127	66	71	110 Porcelain,120 Classic Ivory,130 Buff Beige,220 Natural Beige

128	66	72	Matte Finish

129	66	73	Premium Material

130	59	64	Premium Material

131	59	65	Premium Material

132	59	66	Standard

133	59	67	Standard

134	60	64	Premium Material

135	60	65	Premium Material

136	60	66	Standard

137	60	67	Standard

138	67	71	110 Porcelain,120 Classic Ivory,130 Buff Beige,220 Natural Beige

139	67	72	Matte Finish

140	67	73	Premium Material

141	68	71	110 Porcelain,120 Classic Ivory,130 Buff Beige,220 Natural Beige

142	68	72	Matte Finish

143	68	73	Premium Material

144	69	71	110 Porcelain,120 Classic Ivory,130 Buff Beige,220 Natural Beige

145	69	72	Matte Finish

146	69	73	Premium Material

147	45	61	4L,6L

148	45	62	Standard

149	45	63	Standard

150	46	61	3L,5L

151	46	62	Standard

152	46	63	Standard

153	47	61	Standard

154	47	62	Black

155	47	63	Standard

156	71	74	Standard

157	71	75	Premium Material

158	71	76	Premium Material

159	72	74	Standard

160	72	75	Premium Material

161	72	76	Premium Material

162	73	74	Standard

163	73	75	Premium Material

164	73	76	Premium Material

165	81	77	Standard

166	81	78	Premium Material

167	81	79	Standard Size

168	81	80	Standard

169	82	77	Standard

170	82	78	Premium Material

171	82	79	Standard Size

172	82	80	Standard

173	77	84	Standard

174	77	85	Black,White

175	77	86	Standard

176	77	87	Premium Material

177	78	84	Standard

178	78	85	Black,White

179	78	86	Standard

180	78	87	Premium Material

181	79	84	Standard

182	79	85	Black,White

183	79	86	Standard

184	79	87	Premium Material

185	80	84	Standard

186	80	85	Black,White

187	80	86	Standard

188	80	87	Premium Material

189	53	51	Standard

190	53	52	Standard

191	53	53	Premium Material

192	53	54	Standard

193	43	58	Standard

194	43	59	Standard

195	43	60	Standard

196	48	58	6.5kg,8kg

197	48	59	Standard

198	48	60	Standard

199	49	58	6.5kg,8kg

200	49	59	Standard

201	49	60	Standard

202	61	64	Premium Material

203	61	65	Premium Material

204	61	66	Standard

205	61	67	Standard

206	62	64	Premium Material

207	62	65	Premium Material

208	62	66	Standard

209	62	67	Standard

210	63	64	Premium Material

211	63	65	Premium Material

212	63	66	Standard

213	63	67	Standard

214	100	92	Hardcover,Kindle,Paperback

215	100	93	Standard

216	100	94	Standard

217	100	95	Standard

218	104	96	Hardcover,Kindle,Paperback

219	104	97	Standard

220	104	98	Standard

221	104	99	Standard

222	105	96	Hardcover,Kindle,Paperback

223	105	97	Standard

224	105	98	Standard

225	105	99	Standard

226	106	96	Hardcover,Kindle,Paperback

227	106	97	Standard

228	106	98	Standard

229	106	99	Standard

230	109	100	Standard

231	109	101	Black

232	109	102	Premium Material

233	109	103	Standard

234	110	100	Standard

235	110	101	Black,Silver

236	110	102	Premium Material

237	110	103	Standard

238	112	100	Standard

239	112	101	Black

240	112	102	Premium Material

241	112	103	Standard

242	96	104	Hardcover,Kindle,Paperback

243	96	105	Standard

244	96	106	Standard

245	96	107	Standard

246	97	104	Hardcover,Kindle,Paperback

247	97	105	Standard

248	97	106	Standard

249	97	107	Standard

250	98	104	Hardcover,Kindle,Paperback

251	98	105	Standard

252	98	106	Standard

253	98	107	Standard

254	99	104	Hardcover,Kindle,Paperback

255	99	105	Standard

256	99	106	Standard

257	99	107	Standard

258	85	81	Black,Silver

259	85	82	Premium Material

260	85	83	Standard Size

261	115	111	Standard

262	115	112	Premium Material

263	115	113	Standard

264	116	111	Standard

265	116	112	Premium Material

266	116	113	Standard

267	117	111	Standard

268	117	112	Premium Material

269	117	113	Standard

270	118	111	Standard

271	118	112	Premium Material

272	118	113	Standard

273	120	114	Standard

274	120	115	Standard

275	120	116	Standard

276	121	114	Standard

277	121	115	Standard

278	121	116	Standard

279	122	114	Standard

280	122	115	Standard

281	122	116	Standard

282	88	88	Standard

283	88	89	Premium Material

284	88	90	Black

285	88	91	Premium Material

286	89	88	Standard

287	89	89	Premium Material

288	89	90	Black

289	89	91	Premium Material

290	90	88	10,8,9

291	90	89	Premium Material

292	90	90	Standard

293	90	91	Premium Material

294	101	92	Hardcover,Kindle,Paperback

295	101	93	Standard

296	101	94	Standard

297	101	95	Standard

298	102	92	Hardcover,Kindle,Paperback

299	102	93	Standard

300	102	94	Standard

301	102	95	Standard

302	113	100	A5,A6

303	113	101	Standard

304	113	102	Premium Material

305	113	103	Standard

306	114	100	Standard

307	114	101	Black

308	114	102	Premium Material

309	114	103	Standard

310	142	124	195/65 R15,205/55 R16,215/60 R17

311	142	125	Standard

312	142	126	Premium Material

313	143	124	195/65 R15,205/55 R16,215/60 R17

314	143	125	Standard

315	143	126	Premium Material

316	144	124	195/65 R15,205/55 R16,215/60 R17

317	144	125	Standard

318	144	126	Premium Material

319	145	124	195/65 R15,205/55 R16,215/60 R17

320	145	125	Standard

321	145	126	Premium Material

322	146	124	195/65 R15,205/55 R16,215/60 R17

323	146	125	Standard

324	146	126	Premium Material

325	125	117	Standard

326	125	118	Premium Material

327	125	119	Standard

328	138	127	Standard

329	138	128	Premium Material

330	138	129	Standard

331	139	127	Standard

332	139	128	Premium Material

333	139	129	Standard

334	141	127	Standard

335	141	128	Premium Material

336	141	129	Standard

337	151	127	Standard

338	151	128	Premium Material

339	151	129	Standard

340	152	127	Standard

341	152	128	Premium Material

342	152	129	Standard

343	1	19	Black,Silver

344	1	20	128GB,256GB

345	1	21	Standard

346	1	22	Standard Size

347	1	23	Advanced Dual Camera

348	11	24	Black,Silver

349	11	25	Standard

350	11	26	Yes

351	11	27	24 Hours

352	21	40	L,M,S,XL

353	21	41	Black

354	21	42	Premium Material

355	21	43	Standard

356	31	47	Standard

357	31	48	Black,White

358	31	49	Premium Material

359	31	50	Premium Material

360	54	51	Standard

361	54	52	1.5L,2L

362	54	53	Premium Material

363	54	54	Standard

364	40	55	Black,Silver

365	40	56	Premium Material

366	40	57	Standard Size

367	42	61	4L,6L

368	42	62	Standard

369	42	63	Standard

370	64	64	Premium Material

371	64	65	Premium Material

372	64	66	Standard

373	64	67	Standard

374	74	68	Standard

375	74	69	Premium Material

376	74	70	Premium Material

377	129	108	Standard

378	129	109	Standard

379	129	110	Standard

380	130	108	Standard

381	130	109	Standard

382	130	110	Standard

383	124	114	Standard

384	124	115	Standard

385	124	116	Standard

386	126	117	Standard

387	126	118	Premium Material

388	126	119	Standard

389	127	117	Standard

390	127	118	Premium Material

391	127	119	Standard

392	134	120	Standard

393	134	121	100ml,30ml,50ml

394	134	122	Premium Material

395	134	123	Standard

396	135	120	Black

397	135	121	Standard

398	135	122	Premium Material

399	135	123	Standard

400	136	120	Black

401	136	121	Standard

402	136	122	Premium Material

403	136	123	Standard

404	137	120	Black

405	137	121	Standard

406	137	122	Premium Material

407	137	123	Standard

408	86	81	Black,Silver

409	86	82	Premium Material

410	86	83	Standard Size

411	107	96	Hardcover,Kindle,Paperback

412	107	97	Standard

413	107	98	Standard

414	107	99	Standard

415	111	100	Standard

416	111	101	Black,Silver

417	111	102	Premium Material

418	111	103	Standard

419	119	111	Standard

420	119	112	Premium Material

421	119	113	Standard

422	123	114	Standard

423	123	115	Standard

424	123	116	Standard

425	128	117	Standard

426	128	118	Premium Material

427	128	119	Standard

428	140	127	Standard

429	140	128	Premium Material

430	140	129	Standard

431	8	6	16GB,32GB

432	8	7	1TB,512GB

433	8	8	Black,Silver

434	8	9	Latest Gen Processor

435	8	10	Standard Size

436	6	19	Black,Silver

437	6	20	128GB,256GB

438	6	21	Standard

439	6	22	Standard Size

440	6	23	Advanced Dual Camera

441	37	44	Black,Silver

442	37	45	Standard

443	37	46	Premium Material

444	38	44	Standard

445	38	45	L,M,XL

446	38	46	Premium Material

447	55	51	24cm,26cm,28cm

448	55	52	Standard

449	55	53	Premium Material

450	55	54	Standard

451	56	51	24cm,26cm,28cm

452	56	52	Standard

453	56	53	Premium Material

454	56	54	Standard

455	50	58	Standard

456	50	59	Black,Silver

457	50	60	Standard

458	51	58	10L,15L

459	51	59	Standard

460	51	60	Standard

461	52	58	Standard

462	52	59	Black,Silver

463	52	60	Standard

464	44	61	12-place,14-place

465	44	62	Standard

466	44	63	Standard

467	65	64	Premium Material

468	65	65	Premium Material

469	65	66	Standard

470	65	67	Standard

471	84	77	Standard

472	84	78	Premium Material

473	84	79	Standard Size

474	84	80	Standard

475	83	77	Standard

476	83	78	Premium Material

477	83	79	Standard Size

478	83	80	Standard

479	87	81	Black,Silver

480	87	82	Premium Material

481	87	83	Standard Size

482	92	88	Standard

483	92	89	Premium Material

484	92	90	Black,Silver

485	92	91	Premium Material

486	93	88	Standard

487	93	89	Premium Material

488	93	90	Black

489	93	91	Premium Material

490	94	88	Standard

491	94	89	Premium Material

492	94	90	Black

493	94	91	Premium Material

494	108	96	Hardcover,Kindle,Paperback

495	108	97	Standard

496	108	98	Standard

497	108	99	Standard

498	103	96	Hardcover,Kindle,Paperback

499	103	97	Standard

500	103	98	Standard

501	103	99	Standard

502	132	108	Standard

503	132	109	Standard

504	132	110	Standard

505	133	108	Standard

506	133	109	Standard

507	133	110	Standard

508	131	108	Standard

509	131	109	Standard

510	131	110	Standard

511	150	120	Black

512	150	121	Standard

513	150	122	Premium Material

514	150	123	Standard

515	149	120	Black,Silver

516	149	121	Standard

517	149	122	Premium Material

518	149	123	Standard

519	9	6	16GB,32GB

520	9	7	1TB,512GB

521	9	8	Black,Silver

522	9	9	Latest Gen Processor

523	9	10	Standard Size

524	28	36	L,M,S,XL

525	28	37	Black

526	28	38	Premium Material

527	28	39	Standard

528	29	36	L,M,S,XL

529	29	37	Black

530	29	38	Premium Material

531	29	39	Standard

532	57	51	24cm,26cm,28cm

533	57	52	Standard

534	57	53	Premium Material

535	57	54	Standard

536	70	71	Standard

537	70	72	Matte Finish

538	70	73	Premium Material

539	95	88	L,M,XL

540	95	89	Premium Material

541	95	90	Standard

542	95	91	Premium Material

543	91	88	10,8,9

544	91	89	Premium Material

545	91	90	Standard

546	91	91	Premium Material

547	147	124	Standard

548	147	125	Standard

549	147	126	Premium Material

550	148	124	Standard

551	148	125	Standard

552	148	126	Premium Material

\.


--
-- Data for Name: pms_product_category; Type: TABLE DATA; Schema: pms; Owner: postgres
--

COPY pms.pms_product_category (id, parent_id, name, level, product_count, product_unit, nav_status, show_status, sort, icon, keywords, description) FROM stdin;
1	0	Electronics	0	10	\N	1	1	\N	http://localhost:9000/nexus-media/public/pms/categories/1/icon.svg	\N	\N

2	1	Headphones & Earbuds	1	10	\N	1	1	\N	http://localhost:9000/nexus-media/public/pms/categories/1/subcategories/2/icon.svg	\N	\N

3	1	Smartphones	1	10	\N	1	1	\N	http://localhost:9000/nexus-media/public/pms/categories/1/subcategories/3/icon.svg	\N	\N

4	1	Tablets	1	10	\N	1	1	\N	http://localhost:9000/nexus-media/public/pms/categories/1/subcategories/4/icon.svg	\N	\N

5	1	Laptops	1	10	\N	1	1	\N	http://localhost:9000/nexus-media/public/pms/categories/1/subcategories/5/icon.svg	\N	\N

6	1	Cameras	1	10	\N	1	1	\N	http://localhost:9000/nexus-media/public/pms/categories/1/subcategories/6/icon.svg	\N	\N

7	1	Smartwatches	1	10	\N	1	1	\N	http://localhost:9000/nexus-media/public/pms/categories/1/subcategories/7/icon.svg	\N	\N

8	1	Monitors	1	10	\N	1	1	\N	http://localhost:9000/nexus-media/public/pms/categories/1/subcategories/8/icon.svg	\N	\N

9	1	Keyboards & Mice	1	10	\N	1	1	\N	http://localhost:9000/nexus-media/public/pms/categories/1/subcategories/9/icon.svg	\N	\N

10	0	Fashion	0	10	\N	1	1	\N	http://localhost:9000/nexus-media/public/pms/categories/10/icon.svg	\N	\N

11	10	Men's Clothing	1	10	\N	1	1	\N	http://localhost:9000/nexus-media/public/pms/categories/10/subcategories/11/icon.svg	\N	\N

12	10	Women's Clothing	1	10	\N	1	1	\N	http://localhost:9000/nexus-media/public/pms/categories/10/subcategories/12/icon.svg	\N	\N

13	10	Accessories	1	10	\N	1	1	\N	http://localhost:9000/nexus-media/public/pms/categories/10/subcategories/13/icon.svg	\N	\N

14	10	Footwear	1	10	\N	1	1	\N	http://localhost:9000/nexus-media/public/pms/categories/10/subcategories/14/icon.svg	\N	\N

15	0	Home & Kitchen	0	10	\N	1	1	\N	http://localhost:9000/nexus-media/public/pms/categories/15/icon.svg	\N	\N

16	15	Kitchenware	1	10	\N	1	1	\N	http://localhost:9000/nexus-media/public/pms/categories/15/subcategories/16/icon.svg	\N	\N

17	15	Kitchen Appliances	1	10	\N	1	1	\N	http://localhost:9000/nexus-media/public/pms/categories/15/subcategories/17/icon.svg	\N	\N

18	15	Furniture	1	10	\N	1	1	\N	http://localhost:9000/nexus-media/public/pms/categories/15/subcategories/18/icon.svg	\N	\N

19	15	Home Appliances	1	10	\N	1	1	\N	http://localhost:9000/nexus-media/public/pms/categories/15/subcategories/19/icon.svg	\N	\N

20	0	Beauty & Personal Care	0	10	\N	1	1	\N	http://localhost:9000/nexus-media/public/pms/categories/20/icon.svg	\N	\N

21	20	Grooming	1	10	\N	1	1	\N	http://localhost:9000/nexus-media/public/pms/categories/20/subcategories/21/icon.svg	\N	\N

22	20	Makeup	1	10	\N	1	1	\N	http://localhost:9000/nexus-media/public/pms/categories/20/subcategories/22/icon.svg	\N	\N

23	20	Hair Care	1	10	\N	1	1	\N	http://localhost:9000/nexus-media/public/pms/categories/20/subcategories/23/icon.svg	\N	\N

24	20	Skincare	1	10	\N	1	1	\N	http://localhost:9000/nexus-media/public/pms/categories/20/subcategories/24/icon.svg	\N	\N

25	0	Sports & Fitness	0	10	\N	1	1	\N	http://localhost:9000/nexus-media/public/pms/categories/25/icon.svg	\N	\N

26	25	Gym Equipment	1	10	\N	1	1	\N	http://localhost:9000/nexus-media/public/pms/categories/25/subcategories/26/icon.svg	\N	\N

27	25	Yoga	1	10	\N	1	1	\N	http://localhost:9000/nexus-media/public/pms/categories/25/subcategories/27/icon.svg	\N	\N

28	25	Outdoor	1	10	\N	1	1	\N	http://localhost:9000/nexus-media/public/pms/categories/25/subcategories/28/icon.svg	\N	\N

29	25	Running	1	10	\N	1	1	\N	http://localhost:9000/nexus-media/public/pms/categories/25/subcategories/29/icon.svg	\N	\N

30	0	Books & Stationery	0	10	\N	1	1	\N	http://localhost:9000/nexus-media/public/pms/categories/30/icon.svg	\N	\N

31	30	Fiction	1	10	\N	1	1	\N	http://localhost:9000/nexus-media/public/pms/categories/30/subcategories/31/icon.svg	\N	\N

32	30	Notebooks & Supplies	1	10	\N	1	1	\N	http://localhost:9000/nexus-media/public/pms/categories/30/subcategories/32/icon.svg	\N	\N

33	30	Non-Fiction	1	10	\N	1	1	\N	http://localhost:9000/nexus-media/public/pms/categories/30/subcategories/33/icon.svg	\N	\N

34	30	Technology	1	10	\N	1	1	\N	http://localhost:9000/nexus-media/public/pms/categories/30/subcategories/34/icon.svg	\N	\N

35	0	Grocery	0	10	\N	1	1	\N	http://localhost:9000/nexus-media/public/pms/categories/35/icon.svg	\N	\N

36	35	Snacks	1	10	\N	1	1	\N	http://localhost:9000/nexus-media/public/pms/categories/35/subcategories/36/icon.svg	\N	\N

37	35	Breakfast	1	10	\N	1	1	\N	http://localhost:9000/nexus-media/public/pms/categories/35/subcategories/37/icon.svg	\N	\N

38	35	Beverages	1	10	\N	1	1	\N	http://localhost:9000/nexus-media/public/pms/categories/35/subcategories/38/icon.svg	\N	\N

39	35	Pantry	1	10	\N	1	1	\N	http://localhost:9000/nexus-media/public/pms/categories/35/subcategories/39/icon.svg	\N	\N

40	0	Automotive	0	10	\N	1	1	\N	http://localhost:9000/nexus-media/public/pms/categories/40/icon.svg	\N	\N

41	40	Car Care	1	10	\N	1	1	\N	http://localhost:9000/nexus-media/public/pms/categories/40/subcategories/41/icon.svg	\N	\N

42	40	Replacement Parts	1	10	\N	1	1	\N	http://localhost:9000/nexus-media/public/pms/categories/40/subcategories/42/icon.svg	\N	\N

43	40	Car Accessories	1	10	\N	1	1	\N	http://localhost:9000/nexus-media/public/pms/categories/40/subcategories/43/icon.svg	\N	\N

\.


--
-- Data for Name: pms_product_category_attribute_relation; Type: TABLE DATA; Schema: pms; Owner: postgres
--

COPY pms.pms_product_category_attribute_relation (id, product_category_id, product_attribute_id) FROM stdin;
1	2	1

2	2	2

3	2	3

4	2	4

5	2	5

6	3	6

7	3	7

8	3	8

9	3	9

10	3	10

11	4	11

12	4	12

13	4	13

14	4	14

15	5	15

16	5	16

17	5	17

18	5	18

19	6	19

20	6	20

21	6	21

22	6	22

23	6	23

24	7	24

25	7	25

26	7	26

27	7	27

28	8	28

29	8	29

30	8	30

31	8	31

32	9	32

33	9	33

34	9	34

35	9	35

36	11	36

37	11	37

38	11	38

39	11	39

40	12	40

41	12	41

42	12	42

43	12	43

44	13	44

45	13	45

46	13	46

47	14	47

48	14	48

49	14	49

50	14	50

51	16	51

52	16	52

53	16	53

54	16	54

55	17	55

56	17	56

57	17	57

58	18	58

59	18	59

60	18	60

61	19	61

62	19	62

63	19	63

64	21	64

65	21	65

66	21	66

67	21	67

68	22	68

69	22	69

70	22	70

71	23	71

72	23	72

73	23	73

74	24	74

75	24	75

76	24	76

77	26	77

78	26	78

79	26	79

80	26	80

81	27	81

82	27	82

83	27	83

84	28	84

85	28	85

86	28	86

87	28	87

88	29	88

89	29	89

90	29	90

91	29	91

92	31	92

93	31	93

94	31	94

95	31	95

96	32	96

97	32	97

98	32	98

99	32	99

100	33	100

101	33	101

102	33	102

103	33	103

104	34	104

105	34	105

106	34	106

107	34	107

108	36	108

109	36	109

110	36	110

111	37	111

112	37	112

113	37	113

114	38	114

115	38	115

116	38	116

117	39	117

118	39	118

119	39	119

120	41	120

121	41	121

122	41	122

123	41	123

124	42	124

125	42	125

126	42	126

127	43	127

128	43	128

129	43	129

\.


--
-- Data for Name: pms_product_spend_discount; Type: TABLE DATA; Schema: pms; Owner: postgres
--

COPY pms.pms_product_spend_discount (id, product_id, full_price, reduce_price) FROM stdin;
\.


--
-- Data for Name: pms_product_volume_discount; Type: TABLE DATA; Schema: pms; Owner: postgres
--

COPY pms.pms_product_volume_discount (id, product_id, count, discount, price) FROM stdin;
\.


--
-- Data for Name: pms_product_gallery; Type: TABLE DATA; Schema: pms; Owner: postgres
--

COPY pms.pms_product_gallery (id, product_id, media_type, media_url, sort_order) FROM stdin;
1	1	image	http://localhost:9000/nexus-media/public/pms/products/1-iphone-17/main.webp	0

2	2	image	http://localhost:9000/nexus-media/public/pms/products/2-galaxy-s25/main.webp	0

3	3	image	http://localhost:9000/nexus-media/public/pms/products/3-macbook-pro-m5/main.webp	0

4	4	image	http://localhost:9000/nexus-media/public/pms/products/4-air-max-2026/main.webp	0

5	5	image	http://localhost:9000/nexus-media/public/pms/products/5-ultraboost-26/main.webp	0

6	6	image	http://localhost:9000/nexus-media/public/pms/products/6-clean-code/main.webp	0

7	7	image	http://localhost:9000/nexus-media/public/pms/products/7-coca-cola-zero/main.webp	0

8	8	image	http://localhost:9000/nexus-media/public/pms/products/8-supima-cotton-t-shirt/main.webp	0

9	9	image	http://localhost:9000/nexus-media/public/pms/products/9-wh-1000xm6/main.webp	0

10	10	image	http://localhost:9000/nexus-media/public/pms/products/10-v15-detect-absolute/main.webp	0

\.


--
-- Data for Name: pms_product_audit_log; Type: TABLE DATA; Schema: pms; Owner: postgres
--

COPY pms.pms_product_audit_log (id, product_id, price_old, price_new, sale_price_old, sale_price_new, operate_man, create_time) FROM stdin;
\.


--
-- Data for Name: pms_product_approval_history; Type: TABLE DATA; Schema: pms; Owner: postgres
--

COPY pms.pms_product_approval_history (id, product_id, create_time, reviewer_name, status, detail) FROM stdin;
\.


--
-- Data for Name: pms_review; Type: TABLE DATA; Schema: pms; Owner: postgres
--

COPY pms.pms_review (id, product_id, member_id, parent_id, rating, content, like_count, dislike_count, status, created_time) FROM stdin;
\.


--
-- Data for Name: pms_review_attachments; Type: TABLE DATA; Schema: pms; Owner: postgres
--

COPY pms.pms_review_attachments (id, review_id, media_type, media_url, thumbnail_url, sort_order) FROM stdin;
\.


--
-- Data for Name: pms_sku_stock; Type: TABLE DATA; Schema: pms; Owner: postgres
--

COPY pms.pms_sku_stock (id, product_id, sku_code, price, stock, low_stock, pic, sale, promotion_price, lock_stock, sku_attributes) FROM stdin;
2	1	APPLE-IP17-BLK-256	56570.00	19	10	http://localhost:9000/nexus-media/public/pms/skus/APPLE-IP17-BLK-256/main.svg	0	\N	12	[{"key": "Color", "value": "Black"}, {"key": "Storage", "value": "256GB"}]

1	1	APPLE-IP17-BLK-128	52380.00	114	10	http://localhost:9000/nexus-media/public/pms/skus/APPLE-IP17-BLK-128/main.svg	0	\N	1	[{"key": "Color", "value": "Black"}, {"key": "Storage", "value": "128GB"}]

394	150	TVS-PTI-STD	4640.00	6	10	http://localhost:9000/nexus-media/public/pms/skus/TVS-PTI-STD/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}]

41	11	PORT-MUFFM2-BLK	18160.00	75	10	http://localhost:9000/nexus-media/public/pms/skus/PORT-MUFFM2-BLK/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}]

226	79	NIVIA-RACER-WHT-9	6930.00	60	10	http://localhost:9000/nexus-media/public/pms/skus/NIVIA-RACER-WHT-9/main.svg	0	\N	0	[{"key": "Color", "value": "White"}, {"key": "Size (US)", "value": "9"}]

23	6	MICRO-INN3-SLV-128	36600.00	42	10	http://localhost:9000/nexus-media/public/pms/skus/MICRO-INN3-SLV-128/main.svg	0	\N	0	[{"key": "Color", "value": "Silver"}, {"key": "Storage", "value": "128GB"}]

24	6	MICRO-INN3-SLV-256	39470.00	35	10	http://localhost:9000/nexus-media/public/pms/skus/MICRO-INN3-SLV-256/main.svg	0	\N	0	[{"key": "Color", "value": "Silver"}, {"key": "Storage", "value": "256GB"}]

25	7	DELL-IN16P-16-512-BLK	110200.00	92	10	http://localhost:9000/nexus-media/public/pms/skus/DELL-IN16P-16-512-BLK/main.svg	0	\N	0	[{"key": "RAM", "value": "16GB"}, {"key": "Color", "value": "Black"}, {"key": "Storage", "value": "512GB"}]

43	12	NOTHING-EAR3-BLK	6790.00	38	10	http://localhost:9000/nexus-media/public/pms/skus/NOTHING-EAR3-BLK/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}]

26	7	DELL-IN16P-16-1TB-SLV	127830.00	40	10	http://localhost:9000/nexus-media/public/pms/skus/DELL-IN16P-16-1TB-SLV/main.svg	0	\N	0	[{"key": "RAM", "value": "16GB"}, {"key": "Color", "value": "Silver"}, {"key": "Storage", "value": "1TB"}]

28	7	DELL-IN16P-32-1TB-SLV	138850.00	90	10	http://localhost:9000/nexus-media/public/pms/skus/DELL-IN16P-32-1TB-SLV/main.svg	0	\N	0	[{"key": "RAM", "value": "32GB"}, {"key": "Color", "value": "Silver"}, {"key": "Storage", "value": "1TB"}]

29	8	LENOVO-TPE16-16-512-BLK	109820.00	69	10	http://localhost:9000/nexus-media/public/pms/skus/LENOVO-TPE16-16-512-BLK/main.svg	0	\N	0	[{"key": "RAM", "value": "16GB"}, {"key": "Color", "value": "Black"}, {"key": "Storage", "value": "512GB"}]

30	8	LENOVO-TPE16-16-1TB-SLV	127390.00	13	10	http://localhost:9000/nexus-media/public/pms/skus/LENOVO-TPE16-16-1TB-SLV/main.svg	0	\N	0	[{"key": "RAM", "value": "16GB"}, {"key": "Color", "value": "Silver"}, {"key": "Storage", "value": "1TB"}]

31	8	LENOVO-TPE16-32-1TB-BLK	136180.00	76	10	http://localhost:9000/nexus-media/public/pms/skus/LENOVO-TPE16-32-1TB-BLK/main.svg	0	122560.00	0	[{"key": "RAM", "value": "32GB"}, {"key": "Color", "value": "Black"}, {"key": "Storage", "value": "1TB"}]

32	8	LENOVO-TPE16-32-1TB-SLV	138370.00	30	10	http://localhost:9000/nexus-media/public/pms/skus/LENOVO-TPE16-32-1TB-SLV/main.svg	0	\N	0	[{"key": "RAM", "value": "32GB"}, {"key": "Color", "value": "Silver"}, {"key": "Storage", "value": "1TB"}]

34	9	ASUS-VBS16-16-1TB-SLV	71830.00	87	10	http://localhost:9000/nexus-media/public/pms/skus/ASUS-VBS16-16-1TB-SLV/main.svg	0	\N	0	[{"key": "RAM", "value": "16GB"}, {"key": "Color", "value": "Silver"}, {"key": "Storage", "value": "1TB"}]

35	9	ASUS-VBS16-32-1TB-BLK	76780.00	81	10	http://localhost:9000/nexus-media/public/pms/skus/ASUS-VBS16-32-1TB-BLK/main.svg	0	\N	0	[{"key": "RAM", "value": "32GB"}, {"key": "Color", "value": "Black"}, {"key": "Storage", "value": "1TB"}]

36	9	ASUS-VBS16-32-1TB-SLV	78020.00	25	10	http://localhost:9000/nexus-media/public/pms/skus/ASUS-VBS16-32-1TB-SLV/main.svg	0	71780.00	0	[{"key": "RAM", "value": "32GB"}, {"key": "Color", "value": "Silver"}, {"key": "Storage", "value": "1TB"}]

37	10	ONEPLUS-OPPAD3-BLK-128	42770.00	38	10	http://localhost:9000/nexus-media/public/pms/skus/ONEPLUS-OPPAD3-BLK-128/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}, {"key": "Storage", "value": "128GB"}]

38	10	ONEPLUS-OPPAD3-BLK-256	46190.00	58	10	http://localhost:9000/nexus-media/public/pms/skus/ONEPLUS-OPPAD3-BLK-256/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}, {"key": "Storage", "value": "256GB"}]

40	10	ONEPLUS-OPPAD3-SLV-256	47050.00	12	10	http://localhost:9000/nexus-media/public/pms/skus/ONEPLUS-OPPAD3-SLV-256/main.svg	0	41400.00	0	[{"key": "Color", "value": "Silver"}, {"key": "Storage", "value": "256GB"}]

45	13	NOISE-BXPR-BLK	31000.00	76	10	http://localhost:9000/nexus-media/public/pms/skus/NOISE-BXPR-BLK/main.svg	0	25420.00	0	[{"key": "Color", "value": "Black"}]

46	14	BOAT-LUNPRO-BLK	19420.00	26	10	http://localhost:9000/nexus-media/public/pms/skus/BOAT-LUNPRO-BLK/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}]

47	14	BOAT-LUNPRO-SLV	19810.00	101	10	http://localhost:9000/nexus-media/public/pms/skus/BOAT-LUNPRO-SLV/main.svg	0	\N	0	[{"key": "Color", "value": "Silver"}]

48	15	LOGI-MXBRIO-STD	9150.00	22	10	http://localhost:9000/nexus-media/public/pms/skus/LOGI-MXBRIO-STD/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}]

50	16	SONY-WH6-SLV	35490.00	101	10	http://localhost:9000/nexus-media/public/pms/skus/SONY-WH6-SLV/main.svg	0	\N	0	[{"key": "Color", "value": "Silver"}]

51	17	LG-27GS6-27-QHD	42180.00	97	10	http://localhost:9000/nexus-media/public/pms/skus/LG-27GS6-27-QHD/main.svg	0	\N	0	[{"key": "Resolution", "value": "2560x1440"}, {"key": "Size (inches)", "value": "27"}]

52	17	LG-27GS6-32-4K	52300.00	12	10	http://localhost:9000/nexus-media/public/pms/skus/LG-27GS6-32-4K/main.svg	0	\N	0	[{"key": "Resolution", "value": "3840x2160"}, {"key": "Size (inches)", "value": "32"}]

54	18	SAMSUNG-ODG6-32-4K	13650.00	19	10	http://localhost:9000/nexus-media/public/pms/skus/SAMSUNG-ODG6-32-4K/main.svg	0	11190.00	0	[{"key": "Resolution", "value": "3840x2160"}, {"key": "Size (inches)", "value": "32"}]

55	19	ZEBRO-TWK-BLK	6440.00	85	10	http://localhost:9000/nexus-media/public/pms/skus/ZEBRO-TWK-BLK/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}]

56	19	ZEBRO-TWK-SLV	6560.00	43	10	http://localhost:9000/nexus-media/public/pms/skus/ZEBRO-TWK-SLV/main.svg	0	\N	0	[{"key": "Color", "value": "Silver"}]

57	20	LEVIS-511SL-S-BLK	4570.00	67	10	http://localhost:9000/nexus-media/public/pms/skus/LEVIS-511SL-S-BLK/main.svg	0	\N	0	[{"key": "Size", "value": "S"}, {"key": "Color", "value": "Black"}]

58	20	LEVIS-511SL-M-BLK	4570.00	35	10	http://localhost:9000/nexus-media/public/pms/skus/LEVIS-511SL-M-BLK/main.svg	0	3880.00	0	[{"key": "Size", "value": "M"}, {"key": "Color", "value": "Black"}]

59	20	LEVIS-511SL-L-BLK	4570.00	85	10	http://localhost:9000/nexus-media/public/pms/skus/LEVIS-511SL-L-BLK/main.svg	0	\N	0	[{"key": "Size", "value": "L"}, {"key": "Color", "value": "Black"}]

61	21	ALLENS-ASOXR-S-BLK	1220.00	67	10	http://localhost:9000/nexus-media/public/pms/skus/ALLENS-ASOXR-S-BLK/main.svg	0	\N	0	[{"key": "Size", "value": "S"}, {"key": "Color", "value": "Black"}]

62	21	ALLENS-ASOXR-M-BLK	1220.00	116	10	http://localhost:9000/nexus-media/public/pms/skus/ALLENS-ASOXR-M-BLK/main.svg	0	\N	0	[{"key": "Size", "value": "M"}, {"key": "Color", "value": "Black"}]

63	21	ALLENS-ASOXR-L-BLK	1220.00	121	10	http://localhost:9000/nexus-media/public/pms/skus/ALLENS-ASOXR-L-BLK/main.svg	0	1040.00	0	[{"key": "Size", "value": "L"}, {"key": "Color", "value": "Black"}]

64	21	ALLENS-ASOXR-XL-BLK	1220.00	14	10	http://localhost:9000/nexus-media/public/pms/skus/ALLENS-ASOXR-XL-BLK/main.svg	0	\N	0	[{"key": "Size", "value": "XL"}, {"key": "Color", "value": "Black"}]

66	22	PETEREN-PEFSH-M-BLK	2580.00	58	10	http://localhost:9000/nexus-media/public/pms/skus/PETEREN-PEFSH-M-BLK/main.svg	0	\N	0	[{"key": "Size", "value": "M"}, {"key": "Color", "value": "Black"}]

67	22	PETEREN-PEFSH-L-BLK	2580.00	5	10	http://localhost:9000/nexus-media/public/pms/skus/PETEREN-PEFSH-L-BLK/main.svg	0	2190.00	0	[{"key": "Size", "value": "L"}, {"key": "Color", "value": "Black"}]

68	22	PETEREN-PEFSH-XL-BLK	2580.00	98	10	http://localhost:9000/nexus-media/public/pms/skus/PETEREN-PEFSH-XL-BLK/main.svg	0	\N	0	[{"key": "Size", "value": "XL"}, {"key": "Color", "value": "Black"}]

69	23	RAYMOND-RFT-S-BLK	6050.00	99	10	http://localhost:9000/nexus-media/public/pms/skus/RAYMOND-RFT-S-BLK/main.svg	0	\N	0	[{"key": "Size", "value": "S"}, {"key": "Color", "value": "Black"}]

71	23	RAYMOND-RFT-L-BLK	6050.00	137	10	http://localhost:9000/nexus-media/public/pms/skus/RAYMOND-RFT-L-BLK/main.svg	0	\N	0	[{"key": "Size", "value": "L"}, {"key": "Color", "value": "Black"}]

72	23	RAYMOND-RFT-XL-BLK	6050.00	133	10	http://localhost:9000/nexus-media/public/pms/skus/RAYMOND-RFT-XL-BLK/main.svg	0	4960.00	0	[{"key": "Size", "value": "XL"}, {"key": "Color", "value": "Black"}]

73	24	UNIQLO-UQT-M	6270.00	131	10	http://localhost:9000/nexus-media/public/pms/skus/UNIQLO-UQT-M/main.svg	0	\N	0	[{"key": "Size", "value": "M"}]

74	24	UNIQLO-UQT-L	6270.00	30	10	http://localhost:9000/nexus-media/public/pms/skus/UNIQLO-UQT-L/main.svg	0	\N	0	[{"key": "Size", "value": "L"}]

75	24	UNIQLO-UQT-XL	6270.00	22	10	http://localhost:9000/nexus-media/public/pms/skus/UNIQLO-UQT-XL/main.svg	0	\N	0	[{"key": "Size", "value": "XL"}]

77	25	ZARA-ZRBL-M-BLK	970.00	119	10	http://localhost:9000/nexus-media/public/pms/skus/ZARA-ZRBL-M-BLK/main.svg	0	\N	0	[{"key": "Size", "value": "M"}, {"key": "Color", "value": "Black"}]

78	25	ZARA-ZRBL-L-BLK	970.00	140	10	http://localhost:9000/nexus-media/public/pms/skus/ZARA-ZRBL-L-BLK/main.svg	0	\N	0	[{"key": "Size", "value": "L"}, {"key": "Color", "value": "Black"}]

79	25	ZARA-ZRBL-XL-BLK	970.00	88	10	http://localhost:9000/nexus-media/public/pms/skus/ZARA-ZRBL-XL-BLK/main.svg	0	\N	0	[{"key": "Size", "value": "XL"}, {"key": "Color", "value": "Black"}]

80	26	HM-HMD-S-BLK	2500.00	121	10	http://localhost:9000/nexus-media/public/pms/skus/HM-HMD-S-BLK/main.svg	0	\N	0	[{"key": "Size", "value": "S"}, {"key": "Color", "value": "Black"}]

81	26	HM-HMD-M-BLK	2500.00	116	10	http://localhost:9000/nexus-media/public/pms/skus/HM-HMD-M-BLK/main.svg	0	2250.00	0	[{"key": "Size", "value": "M"}, {"key": "Color", "value": "Black"}]

82	26	HM-HMD-L-BLK	2500.00	101	10	http://localhost:9000/nexus-media/public/pms/skus/HM-HMD-L-BLK/main.svg	0	\N	0	[{"key": "Size", "value": "L"}, {"key": "Color", "value": "Black"}]

84	27	FABIND-FBKRT-S-BLK	2710.00	159	10	http://localhost:9000/nexus-media/public/pms/skus/FABIND-FBKRT-S-BLK/main.svg	0	\N	0	[{"key": "Size", "value": "S"}, {"key": "Color", "value": "Black"}]

85	27	FABIND-FBKRT-M-BLK	2710.00	84	10	http://localhost:9000/nexus-media/public/pms/skus/FABIND-FBKRT-M-BLK/main.svg	0	2220.00	0	[{"key": "Size", "value": "M"}, {"key": "Color", "value": "Black"}]

86	27	FABIND-FBKRT-L-BLK	2710.00	74	10	http://localhost:9000/nexus-media/public/pms/skus/FABIND-FBKRT-L-BLK/main.svg	0	\N	0	[{"key": "Size", "value": "L"}, {"key": "Color", "value": "Black"}]

88	28	MANYAVAR-MVKRT-S-BLK	2550.00	121	10	http://localhost:9000/nexus-media/public/pms/skus/MANYAVAR-MVKRT-S-BLK/main.svg	0	\N	0	[{"key": "Size", "value": "S"}, {"key": "Color", "value": "Black"}]

137	46	HAWKINS-CONT-5L	6850.00	67	10	http://localhost:9000/nexus-media/public/pms/skus/HAWKINS-CONT-5L/main.svg	0	\N	0	[{"key": "Capacity", "value": "5L"}]

91	28	MANYAVAR-MVKRT-XL-BLK	2550.00	92	10	http://localhost:9000/nexus-media/public/pms/skus/MANYAVAR-MVKRT-XL-BLK/main.svg	0	\N	0	[{"key": "Size", "value": "XL"}, {"key": "Color", "value": "Black"}]

92	29	WFW-WKS-S-BLK	3480.00	96	10	http://localhost:9000/nexus-media/public/pms/skus/WFW-WKS-S-BLK/main.svg	0	\N	0	[{"key": "Size", "value": "S"}, {"key": "Color", "value": "Black"}]

93	29	WFW-WKS-M-BLK	3480.00	98	10	http://localhost:9000/nexus-media/public/pms/skus/WFW-WKS-M-BLK/main.svg	0	\N	0	[{"key": "Size", "value": "M"}, {"key": "Color", "value": "Black"}]

94	29	WFW-WKS-L-BLK	3480.00	63	10	http://localhost:9000/nexus-media/public/pms/skus/WFW-WKS-L-BLK/main.svg	0	3130.00	0	[{"key": "Size", "value": "L"}, {"key": "Color", "value": "Black"}]

95	29	WFW-WKS-XL-BLK	3480.00	81	10	http://localhost:9000/nexus-media/public/pms/skus/WFW-WKS-XL-BLK/main.svg	0	\N	0	[{"key": "Size", "value": "XL"}, {"key": "Color", "value": "Black"}]

96	30	NIKE-AM270-BLK-8	9820.00	76	10	http://localhost:9000/nexus-media/public/pms/skus/NIKE-AM270-BLK-8/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}, {"key": "Size (US)", "value": "8"}]

98	30	NIKE-AM270-WHT-8	9820.00	62	10	http://localhost:9000/nexus-media/public/pms/skus/NIKE-AM270-WHT-8/main.svg	0	\N	0	[{"key": "Color", "value": "White"}, {"key": "Size (US)", "value": "8"}]

99	30	NIKE-AM270-WHT-9	9820.00	33	10	http://localhost:9000/nexus-media/public/pms/skus/NIKE-AM270-WHT-9/main.svg	0	9030.00	0	[{"key": "Color", "value": "White"}, {"key": "Size (US)", "value": "9"}]

100	31	ADIDAS-UBLGT-BLK-8	8260.00	150	10	http://localhost:9000/nexus-media/public/pms/skus/ADIDAS-UBLGT-BLK-8/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}, {"key": "Size (US)", "value": "8"}]

101	31	ADIDAS-UBLGT-BLK-9	8260.00	118	10	http://localhost:9000/nexus-media/public/pms/skus/ADIDAS-UBLGT-BLK-9/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}, {"key": "Size (US)", "value": "9"}]

103	31	ADIDAS-UBLGT-WHT-9	8260.00	108	10	http://localhost:9000/nexus-media/public/pms/skus/ADIDAS-UBLGT-WHT-9/main.svg	0	6770.00	0	[{"key": "Color", "value": "White"}, {"key": "Size (US)", "value": "9"}]

106	32	PUMA-VN3-WHT-8	2840.00	70	10	http://localhost:9000/nexus-media/public/pms/skus/PUMA-VN3-WHT-8/main.svg	0	\N	0	[{"key": "Color", "value": "White"}, {"key": "Size (US)", "value": "8"}]

107	32	PUMA-VN3-WHT-9	2840.00	145	10	http://localhost:9000/nexus-media/public/pms/skus/PUMA-VN3-WHT-9/main.svg	0	\N	0	[{"key": "Color", "value": "White"}, {"key": "Size (US)", "value": "9"}]

109	33	TNF-VEND-BLK-9	2540.00	62	10	http://localhost:9000/nexus-media/public/pms/skus/TNF-VEND-BLK-9/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}, {"key": "Size (US)", "value": "9"}]

110	33	TNF-VEND-WHT-8	2540.00	18	10	http://localhost:9000/nexus-media/public/pms/skus/TNF-VEND-WHT-8/main.svg	0	\N	0	[{"key": "Color", "value": "White"}, {"key": "Size (US)", "value": "8"}]

111	33	TNF-VEND-WHT-9	2540.00	61	10	http://localhost:9000/nexus-media/public/pms/skus/TNF-VEND-WHT-9/main.svg	0	\N	0	[{"key": "Color", "value": "White"}, {"key": "Size (US)", "value": "9"}]

112	34	RAYBAN-AVCL-BLK	960.00	93	10	http://localhost:9000/nexus-media/public/pms/skus/RAYBAN-AVCL-BLK/main.svg	0	860.00	0	[{"key": "Color", "value": "Black"}]

113	34	RAYBAN-AVCL-SLV	980.00	151	10	http://localhost:9000/nexus-media/public/pms/skus/RAYBAN-AVCL-SLV/main.svg	0	\N	0	[{"key": "Color", "value": "Silver"}]

114	35	VANH-VHBLT-S	6090.00	145	10	http://localhost:9000/nexus-media/public/pms/skus/VANH-VHBLT-S/main.svg	0	\N	0	[{"key": "Size", "value": "S"}]

116	35	VANH-VHBLT-L	6090.00	141	10	http://localhost:9000/nexus-media/public/pms/skus/VANH-VHBLT-L/main.svg	0	\N	0	[{"key": "Size", "value": "L"}]

117	36	UNIQLO-RMSB-BLK	5250.00	41	10	http://localhost:9000/nexus-media/public/pms/skus/UNIQLO-RMSB-BLK/main.svg	0	4720.00	0	[{"key": "Color", "value": "Black"}]

118	36	UNIQLO-RMSB-SLV	5350.00	22	10	http://localhost:9000/nexus-media/public/pms/skus/UNIQLO-RMSB-SLV/main.svg	0	\N	0	[{"key": "Color", "value": "Silver"}]

119	37	NIKE-HBPK-BLK	1150.00	120	10	http://localhost:9000/nexus-media/public/pms/skus/NIKE-HBPK-BLK/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}]

121	38	ADIDAS-3SCAP-M	3310.00	88	10	http://localhost:9000/nexus-media/public/pms/skus/ADIDAS-3SCAP-M/main.svg	0	2910.00	0	[{"key": "Size", "value": "M"}]

122	38	ADIDAS-3SCAP-L	3310.00	139	10	http://localhost:9000/nexus-media/public/pms/skus/ADIDAS-3SCAP-L/main.svg	0	\N	0	[{"key": "Size", "value": "L"}]

123	38	ADIDAS-3SCAP-XL	3310.00	70	10	http://localhost:9000/nexus-media/public/pms/skus/ADIDAS-3SCAP-XL/main.svg	0	\N	0	[{"key": "Size", "value": "XL"}]

124	39	IKEA-MALM6-STD	17780.00	140	10	http://localhost:9000/nexus-media/public/pms/skus/IKEA-MALM6-STD/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}]

126	40	IKEA-LACK-SLV	21960.00	85	10	http://localhost:9000/nexus-media/public/pms/skus/IKEA-LACK-SLV/main.svg	0	18010.00	0	[{"key": "Color", "value": "Silver"}]

4	1	APPLE-IP17-SLV-256	57620.00	5	10	http://localhost:9000/nexus-media/public/pms/skus/APPLE-IP17-SLV-256/main.svg	0	47250.00	0	[{"key": "Color", "value": "Silver"}, {"key": "Storage", "value": "256GB"}]

127	41	IKEA-BILLY-BLK	14620.00	32	10	http://localhost:9000/nexus-media/public/pms/skus/IKEA-BILLY-BLK/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}]

128	41	IKEA-BILLY-SLV	14910.00	89	10	http://localhost:9000/nexus-media/public/pms/skus/IKEA-BILLY-SLV/main.svg	0	\N	0	[{"key": "Color", "value": "Silver"}]

129	42	PHILIPS-AF3K-4L	15620.00	75	10	http://localhost:9000/nexus-media/public/pms/skus/PHILIPS-AF3K-4L/main.svg	0	\N	0	[{"key": "Capacity", "value": "4L"}]

130	42	PHILIPS-AF3K-6L	15620.00	137	10	http://localhost:9000/nexus-media/public/pms/skus/PHILIPS-AF3K-6L/main.svg	0	14060.00	0	[{"key": "Capacity", "value": "6L"}]

132	44	BOSCH-BOSDW-12P	9130.00	118	10	http://localhost:9000/nexus-media/public/pms/skus/BOSCH-BOSDW-12P/main.svg	0	\N	0	[{"key": "Capacity", "value": "12-place"}]

133	44	BOSCH-BOSDW-14P	9130.00	32	10	http://localhost:9000/nexus-media/public/pms/skus/BOSCH-BOSDW-14P/main.svg	0	\N	0	[{"key": "Capacity", "value": "14-place"}]

134	45	PRESTIGE-AFPRO-4L	23130.00	70	10	http://localhost:9000/nexus-media/public/pms/skus/PRESTIGE-AFPRO-4L/main.svg	0	\N	0	[{"key": "Capacity", "value": "4L"}]

136	46	HAWKINS-CONT-3L	6850.00	76	10	http://localhost:9000/nexus-media/public/pms/skus/HAWKINS-CONT-3L/main.svg	0	\N	0	[{"key": "Capacity", "value": "3L"}]

140	48	LG-LGW65-8KG	14170.00	61	10	http://localhost:9000/nexus-media/public/pms/skus/LG-LGW65-8KG/main.svg	0	\N	0	[{"key": "Capacity", "value": "8kg"}]

141	49	BOSCH-BOS8-6.5KG	4660.00	138	10	http://localhost:9000/nexus-media/public/pms/skus/BOSCH-BOS8-6.5KG/main.svg	0	\N	0	[{"key": "Capacity", "value": "6.5kg"}]

142	49	BOSCH-BOS8-8KG	4660.00	19	10	http://localhost:9000/nexus-media/public/pms/skus/BOSCH-BOS8-8KG/main.svg	0	\N	0	[{"key": "Capacity", "value": "8kg"}]

143	50	BAJAJ-MTF-BLK	44600.00	67	10	http://localhost:9000/nexus-media/public/pms/skus/BAJAJ-MTF-BLK/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}]

144	50	BAJAJ-MTF-SLV	45500.00	76	10	http://localhost:9000/nexus-media/public/pms/skus/BAJAJ-MTF-SLV/main.svg	0	40040.00	0	[{"key": "Color", "value": "Silver"}]

146	51	HAVELLS-AQP-15L	7550.00	11	10	http://localhost:9000/nexus-media/public/pms/skus/HAVELLS-AQP-15L/main.svg	0	\N	0	[{"key": "Capacity", "value": "15L"}]

147	52	USHA-MAF-BLK	45620.00	133	10	http://localhost:9000/nexus-media/public/pms/skus/USHA-MAF-BLK/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}]

148	52	USHA-MAF-SLV	46530.00	144	10	http://localhost:9000/nexus-media/public/pms/skus/USHA-MAF-SLV/main.svg	0	41880.00	0	[{"key": "Color", "value": "Silver"}]

149	53	BUTTERFLY-RB3-STD	6360.00	13	10	http://localhost:9000/nexus-media/public/pms/skus/BUTTERFLY-RB3-STD/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}]

151	54	CROMPTON-SEK-2L	2220.00	75	10	http://localhost:9000/nexus-media/public/pms/skus/CROMPTON-SEK-2L/main.svg	0	\N	0	[{"key": "Capacity", "value": "2L"}]

152	55	WONDERCHEF-GFP-24CM	5200.00	137	10	http://localhost:9000/nexus-media/public/pms/skus/WONDERCHEF-GFP-24CM/main.svg	0	\N	0	[{"key": "Size", "value": "24cm"}]

153	55	WONDERCHEF-GFP-26CM	5200.00	63	10	http://localhost:9000/nexus-media/public/pms/skus/WONDERCHEF-GFP-26CM/main.svg	0	4420.00	0	[{"key": "Size", "value": "26cm"}]

154	55	WONDERCHEF-GFP-28CM	5200.00	44	10	http://localhost:9000/nexus-media/public/pms/skus/WONDERCHEF-GFP-28CM/main.svg	0	\N	0	[{"key": "Size", "value": "28cm"}]

156	56	PRESTIGE-ODP-26CM	4820.00	33	10	http://localhost:9000/nexus-media/public/pms/skus/PRESTIGE-ODP-26CM/main.svg	0	\N	0	[{"key": "Size", "value": "26cm"}]

157	56	PRESTIGE-ODP-28CM	4820.00	52	10	http://localhost:9000/nexus-media/public/pms/skus/PRESTIGE-ODP-28CM/main.svg	0	4340.00	0	[{"key": "Size", "value": "28cm"}]

158	57	HAWKINS-FNT-24CM	6970.00	94	10	http://localhost:9000/nexus-media/public/pms/skus/HAWKINS-FNT-24CM/main.svg	0	\N	0	[{"key": "Size", "value": "24cm"}]

159	57	HAWKINS-FNT-26CM	6970.00	93	10	http://localhost:9000/nexus-media/public/pms/skus/HAWKINS-FNT-26CM/main.svg	0	\N	0	[{"key": "Size", "value": "26cm"}]

161	58	ORDINARY-NOZ-STD	1230.00	141	10	http://localhost:9000/nexus-media/public/pms/skus/ORDINARY-NOZ-STD/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}]

162	59	NIVEA-NSOFT-30ML	2150.00	124	10	http://localhost:9000/nexus-media/public/pms/skus/NIVEA-NSOFT-30ML/main.svg	0	1940.00	0	[{"key": "Pack Size", "value": "30ml"}]

163	59	NIVEA-NSOFT-50ML	2150.00	24	10	http://localhost:9000/nexus-media/public/pms/skus/NIVEA-NSOFT-50ML/main.svg	0	\N	0	[{"key": "Pack Size", "value": "50ml"}]

164	60	CLINIQUE-MS100-30ML	900.00	115	10	http://localhost:9000/nexus-media/public/pms/skus/CLINIQUE-MS100-30ML/main.svg	0	\N	0	[{"key": "Pack Size", "value": "30ml"}]

165	60	CLINIQUE-MS100-50ML	900.00	97	10	http://localhost:9000/nexus-media/public/pms/skus/CLINIQUE-MS100-50ML/main.svg	0	\N	0	[{"key": "Pack Size", "value": "50ml"}]

167	61	NEUTRO-HWB-30ML	2340.00	65	10	http://localhost:9000/nexus-media/public/pms/skus/NEUTRO-HWB-30ML/main.svg	0	\N	0	[{"key": "Pack Size", "value": "30ml"}]

168	61	NEUTRO-HWB-50ML	2340.00	127	10	http://localhost:9000/nexus-media/public/pms/skus/NEUTRO-HWB-50ML/main.svg	0	\N	0	[{"key": "Pack Size", "value": "50ml"}]

169	61	NEUTRO-HWB-100ML	2340.00	95	10	http://localhost:9000/nexus-media/public/pms/skus/NEUTRO-HWB-100ML/main.svg	0	\N	0	[{"key": "Pack Size", "value": "100ml"}]

170	62	MAMAEARTH-VCFS-STD	2170.00	48	10	http://localhost:9000/nexus-media/public/pms/skus/MAMAEARTH-VCFS-STD/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}]

171	63	MINIMAL-SA2-STD	1840.00	70	10	http://localhost:9000/nexus-media/public/pms/skus/MINIMAL-SA2-STD/main.svg	0	1620.00	0	[{"key": "Color", "value": "Black"}]

173	64	FOREST-FTM-50ML	1450.00	106	10	http://localhost:9000/nexus-media/public/pms/skus/FOREST-FTM-50ML/main.svg	0	\N	0	[{"key": "Pack Size", "value": "50ml"}]

174	64	FOREST-FTM-100ML	1450.00	149	10	http://localhost:9000/nexus-media/public/pms/skus/FOREST-FTM-100ML/main.svg	0	\N	0	[{"key": "Pack Size", "value": "100ml"}]

175	65	PLUM-GTFW-30ML	2300.00	125	10	http://localhost:9000/nexus-media/public/pms/skus/PLUM-GTFW-30ML/main.svg	0	1960.00	0	[{"key": "Pack Size", "value": "30ml"}]

176	65	PLUM-GTFW-50ML	2300.00	150	10	http://localhost:9000/nexus-media/public/pms/skus/PLUM-GTFW-50ML/main.svg	0	\N	0	[{"key": "Pack Size", "value": "50ml"}]

178	66	LOREAL-TMatch-110	700.00	134	10	http://localhost:9000/nexus-media/public/pms/skus/LOREAL-TMatch-110/main.svg	0	\N	0	[{"key": "Shade", "value": "110 Porcelain"}]

179	66	LOREAL-TMatch-120	700.00	115	10	http://localhost:9000/nexus-media/public/pms/skus/LOREAL-TMatch-120/main.svg	0	\N	0	[{"key": "Shade", "value": "120 Classic Ivory"}]

180	66	LOREAL-TMatch-130	700.00	151	10	http://localhost:9000/nexus-media/public/pms/skus/LOREAL-TMatch-130/main.svg	0	620.00	0	[{"key": "Shade", "value": "130 Buff Beige"}]

181	66	LOREAL-TMatch-220	700.00	42	10	http://localhost:9000/nexus-media/public/pms/skus/LOREAL-TMatch-220/main.svg	0	\N	0	[{"key": "Shade", "value": "220 Natural Beige"}]

183	67	MAYBELLINE-FMF-120	1660.00	137	10	http://localhost:9000/nexus-media/public/pms/skus/MAYBELLINE-FMF-120/main.svg	0	\N	0	[{"key": "Shade", "value": "120 Classic Ivory"}]

184	67	MAYBELLINE-FMF-130	1660.00	27	10	http://localhost:9000/nexus-media/public/pms/skus/MAYBELLINE-FMF-130/main.svg	0	1490.00	0	[{"key": "Shade", "value": "130 Buff Beige"}]

185	67	MAYBELLINE-FMF-220	1660.00	122	10	http://localhost:9000/nexus-media/public/pms/skus/MAYBELLINE-FMF-220/main.svg	0	\N	0	[{"key": "Shade", "value": "220 Natural Beige"}]

186	68	LAKME-9T5-110	2180.00	44	10	http://localhost:9000/nexus-media/public/pms/skus/LAKME-9T5-110/main.svg	0	\N	0	[{"key": "Shade", "value": "110 Porcelain"}]

188	68	LAKME-9T5-130	2180.00	69	10	http://localhost:9000/nexus-media/public/pms/skus/LAKME-9T5-130/main.svg	0	\N	0	[{"key": "Shade", "value": "130 Buff Beige"}]

189	68	LAKME-9T5-220	2180.00	121	10	http://localhost:9000/nexus-media/public/pms/skus/LAKME-9T5-220/main.svg	0	1960.00	0	[{"key": "Shade", "value": "220 Natural Beige"}]

190	69	NYKAA-SKGEN-110	1710.00	34	10	http://localhost:9000/nexus-media/public/pms/skus/NYKAA-SKGEN-110/main.svg	0	\N	0	[{"key": "Shade", "value": "110 Porcelain"}]

192	69	NYKAA-SKGEN-130	1710.00	158	10	http://localhost:9000/nexus-media/public/pms/skus/NYKAA-SKGEN-130/main.svg	0	\N	0	[{"key": "Shade", "value": "130 Buff Beige"}]

193	69	NYKAA-SKGEN-220	1710.00	76	10	http://localhost:9000/nexus-media/public/pms/skus/NYKAA-SKGEN-220/main.svg	0	1540.00	0	[{"key": "Shade", "value": "220 Natural Beige"}]

194	70	BIOTIQUE-BCC-30ML	2310.00	97	10	http://localhost:9000/nexus-media/public/pms/skus/BIOTIQUE-BCC-30ML/main.svg	0	\N	0	[{"key": "Pack Size", "value": "30ml"}]

195	70	BIOTIQUE-BCC-50ML	2310.00	155	10	http://localhost:9000/nexus-media/public/pms/skus/BIOTIQUE-BCC-50ML/main.svg	0	\N	0	[{"key": "Pack Size", "value": "50ml"}]

196	70	BIOTIQUE-BCC-100ML	2310.00	130	10	http://localhost:9000/nexus-media/public/pms/skus/BIOTIQUE-BCC-100ML/main.svg	0	\N	0	[{"key": "Pack Size", "value": "100ml"}]

198	71	DOVE-DIR-50ML	760.00	140	10	http://localhost:9000/nexus-media/public/pms/skus/DOVE-DIR-50ML/main.svg	0	680.00	0	[{"key": "Pack Size", "value": "50ml"}]

199	71	DOVE-DIR-100ML	760.00	121	10	http://localhost:9000/nexus-media/public/pms/skus/DOVE-DIR-100ML/main.svg	0	\N	0	[{"key": "Pack Size", "value": "100ml"}]

200	72	LOREAL-ARS-30ML	1840.00	22	10	http://localhost:9000/nexus-media/public/pms/skus/LOREAL-ARS-30ML/main.svg	0	\N	0	[{"key": "Pack Size", "value": "30ml"}]

201	72	LOREAL-ARS-50ML	1840.00	51	10	http://localhost:9000/nexus-media/public/pms/skus/LOREAL-ARS-50ML/main.svg	0	\N	0	[{"key": "Pack Size", "value": "50ml"}]

203	73	MAMAEARTH-OHO-30ML	770.00	132	10	http://localhost:9000/nexus-media/public/pms/skus/MAMAEARTH-OHO-30ML/main.svg	0	\N	0	[{"key": "Pack Size", "value": "30ml"}]

204	73	MAMAEARTH-OHO-50ML	770.00	76	10	http://localhost:9000/nexus-media/public/pms/skus/MAMAEARTH-OHO-50ML/main.svg	0	\N	0	[{"key": "Pack Size", "value": "50ml"}]

205	73	MAMAEARTH-OHO-100ML	770.00	95	10	http://localhost:9000/nexus-media/public/pms/skus/MAMAEARTH-OHO-100ML/main.svg	0	\N	0	[{"key": "Pack Size", "value": "100ml"}]

206	74	NIVEA-MSG-30ML	1570.00	122	10	http://localhost:9000/nexus-media/public/pms/skus/NIVEA-MSG-30ML/main.svg	0	\N	0	[{"key": "Pack Size", "value": "30ml"}]

208	74	NIVEA-MSG-100ML	1570.00	94	10	http://localhost:9000/nexus-media/public/pms/skus/NIVEA-MSG-100ML/main.svg	0	\N	0	[{"key": "Pack Size", "value": "100ml"}]

209	75	NYKAA-MBW-30ML	2440.00	69	10	http://localhost:9000/nexus-media/public/pms/skus/NYKAA-MBW-30ML/main.svg	0	\N	0	[{"key": "Pack Size", "value": "30ml"}]

210	75	NYKAA-MBW-50ML	2440.00	135	10	http://localhost:9000/nexus-media/public/pms/skus/NYKAA-MBW-50ML/main.svg	0	\N	0	[{"key": "Pack Size", "value": "50ml"}]

211	75	NYKAA-MBW-100ML	2440.00	55	10	http://localhost:9000/nexus-media/public/pms/skus/NYKAA-MBW-100ML/main.svg	0	2000.00	0	[{"key": "Pack Size", "value": "100ml"}]

212	76	PLUM-GTSG-30ML	320.00	110	10	http://localhost:9000/nexus-media/public/pms/skus/PLUM-GTSG-30ML/main.svg	0	\N	0	[{"key": "Pack Size", "value": "30ml"}]

214	76	PLUM-GTSG-100ML	320.00	146	10	http://localhost:9000/nexus-media/public/pms/skus/PLUM-GTSG-100ML/main.svg	0	\N	0	[{"key": "Pack Size", "value": "100ml"}]

215	77	UA-CA10-BLK-8	8630.00	44	10	http://localhost:9000/nexus-media/public/pms/skus/UA-CA10-BLK-8/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}, {"key": "Size (US)", "value": "8"}]

216	77	UA-CA10-BLK-9	8630.00	159	10	http://localhost:9000/nexus-media/public/pms/skus/UA-CA10-BLK-9/main.svg	0	7590.00	0	[{"key": "Color", "value": "Black"}, {"key": "Size (US)", "value": "9"}]

217	77	UA-CA10-WHT-8	8630.00	105	10	http://localhost:9000/nexus-media/public/pms/skus/UA-CA10-WHT-8/main.svg	0	\N	0	[{"key": "Color", "value": "White"}, {"key": "Size (US)", "value": "8"}]

219	78	DECATHLON-KRC-BLK-8	2480.00	55	10	http://localhost:9000/nexus-media/public/pms/skus/DECATHLON-KRC-BLK-8/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}, {"key": "Size (US)", "value": "8"}]

220	78	DECATHLON-KRC-BLK-9	2480.00	62	10	http://localhost:9000/nexus-media/public/pms/skus/DECATHLON-KRC-BLK-9/main.svg	0	2230.00	0	[{"key": "Color", "value": "Black"}, {"key": "Size (US)", "value": "9"}]

221	78	DECATHLON-KRC-WHT-8	2480.00	157	10	http://localhost:9000/nexus-media/public/pms/skus/DECATHLON-KRC-WHT-8/main.svg	0	\N	0	[{"key": "Color", "value": "White"}, {"key": "Size (US)", "value": "8"}]

222	78	DECATHLON-KRC-WHT-9	2480.00	147	10	http://localhost:9000/nexus-media/public/pms/skus/DECATHLON-KRC-WHT-9/main.svg	0	\N	0	[{"key": "Color", "value": "White"}, {"key": "Size (US)", "value": "9"}]

224	79	NIVIA-RACER-BLK-9	6930.00	12	10	http://localhost:9000/nexus-media/public/pms/skus/NIVIA-RACER-BLK-9/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}, {"key": "Size (US)", "value": "9"}]

225	79	NIVIA-RACER-WHT-8	6930.00	25	10	http://localhost:9000/nexus-media/public/pms/skus/NIVIA-RACER-WHT-8/main.svg	0	6240.00	0	[{"key": "Color", "value": "White"}, {"key": "Size (US)", "value": "8"}]

5	2	SAMSUNG-GS26-BLK-128	68520.00	50	10	http://localhost:9000/nexus-media/public/pms/skus/SAMSUNG-GS26-BLK-128/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}, {"key": "Storage", "value": "128GB"}]

227	80	VECTORX-SPRINT-BLK-8	4130.00	116	10	http://localhost:9000/nexus-media/public/pms/skus/VECTORX-SPRINT-BLK-8/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}, {"key": "Size (US)", "value": "8"}]

229	80	VECTORX-SPRINT-WHT-8	4130.00	72	10	http://localhost:9000/nexus-media/public/pms/skus/VECTORX-SPRINT-WHT-8/main.svg	0	3510.00	0	[{"key": "Color", "value": "White"}, {"key": "Size (US)", "value": "8"}]

230	80	VECTORX-SPRINT-WHT-9	4130.00	62	10	http://localhost:9000/nexus-media/public/pms/skus/VECTORX-SPRINT-WHT-9/main.svg	0	\N	0	[{"key": "Color", "value": "White"}, {"key": "Size (US)", "value": "9"}]

231	81	DECATHLON-ADJDB-STD	4500.00	118	10	http://localhost:9000/nexus-media/public/pms/skus/DECATHLON-ADJDB-STD/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}]

232	82	NIVIA-HD10-STD	2120.00	46	10	http://localhost:9000/nexus-media/public/pms/skus/NIVIA-HD10-STD/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}]

233	83	COSCO-RTSET-STD	7480.00	129	10	http://localhost:9000/nexus-media/public/pms/skus/COSCO-RTSET-STD/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}]

235	84	VECTORX-PTG-L	6750.00	22	10	http://localhost:9000/nexus-media/public/pms/skus/VECTORX-PTG-L/main.svg	0	\N	0	[{"key": "Size", "value": "L"}]

236	84	VECTORX-PTG-XL	6750.00	83	10	http://localhost:9000/nexus-media/public/pms/skus/VECTORX-PTG-XL/main.svg	0	\N	0	[{"key": "Size", "value": "XL"}]

237	85	DECATHLON-BYM-BLK	5020.00	21	10	http://localhost:9000/nexus-media/public/pms/skus/DECATHLON-BYM-BLK/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}]

239	86	NIVIA-TPEM-BLK	1970.00	32	10	http://localhost:9000/nexus-media/public/pms/skus/NIVIA-TPEM-BLK/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}]

240	86	NIVIA-TPEM-SLV	2010.00	86	10	http://localhost:9000/nexus-media/public/pms/skus/NIVIA-TPEM-SLV/main.svg	0	\N	0	[{"key": "Color", "value": "Silver"}]

241	87	COSCO-YBP-BLK	3270.00	98	10	http://localhost:9000/nexus-media/public/pms/skus/COSCO-YBP-BLK/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}]

242	87	COSCO-YBP-SLV	3330.00	66	10	http://localhost:9000/nexus-media/public/pms/skus/COSCO-YBP-SLV/main.svg	0	\N	0	[{"key": "Color", "value": "Silver"}]

243	88	WILSON-PS97-STD	7760.00	143	10	http://localhost:9000/nexus-media/public/pms/skus/WILSON-PS97-STD/main.svg	0	6980.00	0	[{"key": "Color", "value": "Black"}]

245	90	SS-SSKYER-S8	1770.00	47	10	http://localhost:9000/nexus-media/public/pms/skus/SS-SSKYER-S8/main.svg	0	\N	0	[{"key": "Size", "value": "8"}]

246	90	SS-SSKYER-S9	1770.00	142	10	http://localhost:9000/nexus-media/public/pms/skus/SS-SSKYER-S9/main.svg	0	\N	0	[{"key": "Size", "value": "9"}]

247	90	SS-SSKYER-S10	1770.00	39	10	http://localhost:9000/nexus-media/public/pms/skus/SS-SSKYER-S10/main.svg	0	1560.00	0	[{"key": "Size", "value": "10"}]

248	91	SG-SGP-S8	6600.00	42	10	http://localhost:9000/nexus-media/public/pms/skus/SG-SGP-S8/main.svg	0	\N	0	[{"key": "Size", "value": "8"}]

249	91	SG-SGP-S9	6600.00	129	10	http://localhost:9000/nexus-media/public/pms/skus/SG-SGP-S9/main.svg	0	\N	0	[{"key": "Size", "value": "9"}]

251	92	DECATHLON-QHB40-BLK	1510.00	31	10	http://localhost:9000/nexus-media/public/pms/skus/DECATHLON-QHB40-BLK/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}]

252	92	DECATHLON-QHB40-SLV	1540.00	45	10	http://localhost:9000/nexus-media/public/pms/skus/DECATHLON-QHB40-SLV/main.svg	0	1390.00	0	[{"key": "Color", "value": "Silver"}]

253	93	NIVIA-STORM-STD	1910.00	76	10	http://localhost:9000/nexus-media/public/pms/skus/NIVIA-STORM-STD/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}]

255	95	WILSON-NBAOB-M	2370.00	69	10	http://localhost:9000/nexus-media/public/pms/skus/WILSON-NBAOB-M/main.svg	0	\N	0	[{"key": "Size", "value": "M"}]

256	95	WILSON-NBAOB-L	2370.00	108	10	http://localhost:9000/nexus-media/public/pms/skus/WILSON-NBAOB-L/main.svg	0	1940.00	0	[{"key": "Size", "value": "L"}]

257	95	WILSON-NBAOB-XL	2370.00	86	10	http://localhost:9000/nexus-media/public/pms/skus/WILSON-NBAOB-XL/main.svg	0	\N	0	[{"key": "Size", "value": "XL"}]

258	96	PRH-MNL-PB	1240.00	202	10	http://localhost:9000/nexus-media/public/pms/skus/PRH-MNL-PB/main.svg	0	\N	0	[{"key": "Format", "value": "Paperback"}]

259	96	PRH-MNL-HC	1270.00	97	10	http://localhost:9000/nexus-media/public/pms/skus/PRH-MNL-HC/main.svg	0	\N	0	[{"key": "Format", "value": "Hardcover"}]

261	97	PRH-1984-PB	1440.00	166	10	http://localhost:9000/nexus-media/public/pms/skus/PRH-1984-PB/main.svg	0	1300.00	0	[{"key": "Format", "value": "Paperback"}]

262	97	PRH-1984-HC	1480.00	224	10	http://localhost:9000/nexus-media/public/pms/skus/PRH-1984-HC/main.svg	0	\N	0	[{"key": "Format", "value": "Hardcover"}]

263	97	PRH-1984-KD	1380.00	160	10	http://localhost:9000/nexus-media/public/pms/skus/PRH-1984-KD/main.svg	0	\N	0	[{"key": "Format", "value": "Kindle"}]

264	98	RUPA-TSP-PB	1770.00	21	10	http://localhost:9000/nexus-media/public/pms/skus/RUPA-TSP-PB/main.svg	0	\N	0	[{"key": "Format", "value": "Paperback"}]

265	98	RUPA-TSP-HC	1820.00	116	10	http://localhost:9000/nexus-media/public/pms/skus/RUPA-TSP-HC/main.svg	0	1490.00	0	[{"key": "Format", "value": "Hardcover"}]

267	99	ACK-MHB-PB	430.00	91	10	http://localhost:9000/nexus-media/public/pms/skus/ACK-MHB-PB/main.svg	0	\N	0	[{"key": "Format", "value": "Paperback"}]

104	32	PUMA-VN3-BLK-8	2840.00	9	10	http://localhost:9000/nexus-media/public/pms/skus/PUMA-VN3-BLK-8/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}, {"key": "Size (US)", "value": "8"}]

268	99	ACK-MHB-HC	450.00	173	10	http://localhost:9000/nexus-media/public/pms/skus/ACK-MHB-HC/main.svg	0	\N	0	[{"key": "Format", "value": "Hardcover"}]

269	99	ACK-MHB-KD	420.00	243	10	http://localhost:9000/nexus-media/public/pms/skus/ACK-MHB-KD/main.svg	0	\N	0	[{"key": "Format", "value": "Kindle"}]

270	100	PRH-AH-PB	1500.00	103	10	http://localhost:9000/nexus-media/public/pms/skus/PRH-AH-PB/main.svg	0	1230.00	0	[{"key": "Format", "value": "Paperback"}]

272	100	PRH-AH-KD	1440.00	87	10	http://localhost:9000/nexus-media/public/pms/skus/PRH-AH-KD/main.svg	0	\N	0	[{"key": "Format", "value": "Kindle"}]

273	101	MCGRAW-TLS-PB	440.00	173	10	http://localhost:9000/nexus-media/public/pms/skus/MCGRAW-TLS-PB/main.svg	0	\N	0	[{"key": "Format", "value": "Paperback"}]

274	101	MCGRAW-TLS-HC	450.00	166	10	http://localhost:9000/nexus-media/public/pms/skus/MCGRAW-TLS-HC/main.svg	0	370.00	0	[{"key": "Format", "value": "Hardcover"}]

275	101	MCGRAW-TLS-KD	420.00	179	10	http://localhost:9000/nexus-media/public/pms/skus/MCGRAW-TLS-KD/main.svg	0	\N	0	[{"key": "Format", "value": "Kindle"}]

277	102	PEARSON-SSG-HC	1700.00	249	10	http://localhost:9000/nexus-media/public/pms/skus/PEARSON-SSG-HC/main.svg	0	\N	0	[{"key": "Format", "value": "Hardcover"}]

278	102	PEARSON-SSG-KD	1580.00	118	10	http://localhost:9000/nexus-media/public/pms/skus/PEARSON-SSG-KD/main.svg	0	\N	0	[{"key": "Format", "value": "Kindle"}]

279	103	OREILLY-CC-PB	1600.00	56	10	http://localhost:9000/nexus-media/public/pms/skus/OREILLY-CC-PB/main.svg	0	1440.00	0	[{"key": "Format", "value": "Paperback"}]

280	103	OREILLY-CC-HC	1650.00	110	10	http://localhost:9000/nexus-media/public/pms/skus/OREILLY-CC-HC/main.svg	0	\N	0	[{"key": "Format", "value": "Hardcover"}]

281	103	OREILLY-CC-KD	1540.00	160	10	http://localhost:9000/nexus-media/public/pms/skus/OREILLY-CC-KD/main.svg	0	\N	0	[{"key": "Format", "value": "Kindle"}]

283	104	OREILLY-DDIA-HC	1310.00	141	10	http://localhost:9000/nexus-media/public/pms/skus/OREILLY-DDIA-HC/main.svg	0	1110.00	0	[{"key": "Format", "value": "Hardcover"}]

285	105	MCGRAW-CCIT-PB	1220.00	29	10	http://localhost:9000/nexus-media/public/pms/skus/MCGRAW-CCIT-PB/main.svg	0	\N	0	[{"key": "Format", "value": "Paperback"}]

287	105	MCGRAW-CCIT-KD	1180.00	81	10	http://localhost:9000/nexus-media/public/pms/skus/MCGRAW-CCIT-KD/main.svg	0	\N	0	[{"key": "Format", "value": "Kindle"}]

289	106	PEARSON-JPC-HC	1560.00	108	10	http://localhost:9000/nexus-media/public/pms/skus/PEARSON-JPC-HC/main.svg	0	\N	0	[{"key": "Format", "value": "Hardcover"}]

290	106	PEARSON-JPC-KD	1450.00	121	10	http://localhost:9000/nexus-media/public/pms/skus/PEARSON-JPC-KD/main.svg	0	\N	0	[{"key": "Format", "value": "Kindle"}]

291	107	SCHAND-DSC-PB	1670.00	199	10	http://localhost:9000/nexus-media/public/pms/skus/SCHAND-DSC-PB/main.svg	0	\N	0	[{"key": "Format", "value": "Paperback"}]

292	107	SCHAND-DSC-HC	1720.00	50	10	http://localhost:9000/nexus-media/public/pms/skus/SCHAND-DSC-HC/main.svg	0	1410.00	0	[{"key": "Format", "value": "Hardcover"}]

293	107	SCHAND-DSC-KD	1600.00	176	10	http://localhost:9000/nexus-media/public/pms/skus/SCHAND-DSC-KD/main.svg	0	\N	0	[{"key": "Format", "value": "Kindle"}]

295	108	ARIHANT-PYCP-HC	900.00	202	10	http://localhost:9000/nexus-media/public/pms/skus/ARIHANT-PYCP-HC/main.svg	0	\N	0	[{"key": "Format", "value": "Hardcover"}]

296	108	ARIHANT-PYCP-KD	840.00	96	10	http://localhost:9000/nexus-media/public/pms/skus/ARIHANT-PYCP-KD/main.svg	0	\N	0	[{"key": "Format", "value": "Kindle"}]

297	109	MOLESKINE-MKNL-STD	560.00	139	10	http://localhost:9000/nexus-media/public/pms/skus/MOLESKINE-MKNL-STD/main.svg	0	490.00	0	[{"key": "Color", "value": "Black"}]

298	110	CROSSWORD-PRN-BLK	910.00	45	10	http://localhost:9000/nexus-media/public/pms/skus/CROSSWORD-PRN-BLK/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}]

300	111	RUPA-EXJ-BLK	560.00	115	10	http://localhost:9000/nexus-media/public/pms/skus/RUPA-EXJ-BLK/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}]

301	111	RUPA-EXJ-SLV	580.00	187	10	http://localhost:9000/nexus-media/public/pms/skus/RUPA-EXJ-SLV/main.svg	0	490.00	0	[{"key": "Color", "value": "Silver"}]

302	112	SCHAND-GPN-STD	280.00	70	10	http://localhost:9000/nexus-media/public/pms/skus/SCHAND-GPN-STD/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}]

303	113	ARIHANT-EPN-A6	1050.00	163	10	http://localhost:9000/nexus-media/public/pms/skus/ARIHANT-EPN-A6/main.svg	0	\N	0	[{"key": "Size", "value": "A6"}]

304	113	ARIHANT-EPN-A5	1050.00	233	10	http://localhost:9000/nexus-media/public/pms/skus/ARIHANT-EPN-A5/main.svg	0	\N	0	[{"key": "Size", "value": "A5"}]

306	115	NESTLE-NCF-100G	320.00	109	10	http://localhost:9000/nexus-media/public/pms/skus/NESTLE-NCF-100G/main.svg	0	290.00	0	[{"key": "Pack Size", "value": "100g"}]

307	115	NESTLE-NCF-200G	320.00	121	10	http://localhost:9000/nexus-media/public/pms/skus/NESTLE-NCF-200G/main.svg	0	\N	0	[{"key": "Pack Size", "value": "200g"}]

308	115	NESTLE-NCF-500G	320.00	26	10	http://localhost:9000/nexus-media/public/pms/skus/NESTLE-NCF-500G/main.svg	0	\N	0	[{"key": "Pack Size", "value": "500g"}]

309	116	COKE-COKE-300ML	890.00	216	10	http://localhost:9000/nexus-media/public/pms/skus/COKE-COKE-300ML/main.svg	0	\N	0	[{"key": "Pack Size", "value": "300ml"}]

311	116	COKE-COKE-1.25L	1090.00	8	10	http://localhost:9000/nexus-media/public/pms/skus/COKE-COKE-1.25L/main.svg	0	\N	0	[{"key": "Pack Size", "value": "1.25L"}]

105	32	PUMA-VN3-BLK-9	2840.00	16	10	http://localhost:9000/nexus-media/public/pms/skus/PUMA-VN3-BLK-9/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}, {"key": "Size (US)", "value": "9"}]

312	116	COKE-COKE-2.25L	1290.00	118	10	http://localhost:9000/nexus-media/public/pms/skus/COKE-COKE-2.25L/main.svg	0	\N	0	[{"key": "Pack Size", "value": "2.25L"}]

313	117	AMUL-ATMLK-500ML	650.00	22	10	http://localhost:9000/nexus-media/public/pms/skus/AMUL-ATMLK-500ML/main.svg	0	\N	0	[{"key": "Pack Size", "value": "500ml"}]

314	117	AMUL-ATMLK-1L	770.00	102	10	http://localhost:9000/nexus-media/public/pms/skus/AMUL-ATMLK-1L/main.svg	0	\N	0	[{"key": "Pack Size", "value": "1L"}]

316	118	TATA-TTG-200G	420.00	246	10	http://localhost:9000/nexus-media/public/pms/skus/TATA-TTG-200G/main.svg	0	\N	0	[{"key": "Pack Size", "value": "200g"}]

317	118	TATA-TTG-500G	420.00	129	10	http://localhost:9000/nexus-media/public/pms/skus/TATA-TTG-500G/main.svg	0	\N	0	[{"key": "Pack Size", "value": "500g"}]

318	119	MOTHERDAIRY-TDMLK-500ML	480.00	168	10	http://localhost:9000/nexus-media/public/pms/skus/MOTHERDAIRY-TDMLK-500ML/main.svg	0	\N	0	[{"key": "Pack Size", "value": "500ml"}]

319	119	MOTHERDAIRY-TDMLK-1L	570.00	14	10	http://localhost:9000/nexus-media/public/pms/skus/MOTHERDAIRY-TDMLK-1L/main.svg	0	520.00	0	[{"key": "Pack Size", "value": "1L"}]

321	120	BRITANNIA-GDC-100G	340.00	35	10	http://localhost:9000/nexus-media/public/pms/skus/BRITANNIA-GDC-100G/main.svg	0	\N	0	[{"key": "Pack Size", "value": "100g"}]

322	120	BRITANNIA-GDC-200G	340.00	78	10	http://localhost:9000/nexus-media/public/pms/skus/BRITANNIA-GDC-200G/main.svg	0	\N	0	[{"key": "Pack Size", "value": "200g"}]

323	120	BRITANNIA-GDC-500G	340.00	12	10	http://localhost:9000/nexus-media/public/pms/skus/BRITANNIA-GDC-500G/main.svg	0	\N	0	[{"key": "Pack Size", "value": "500g"}]

324	121	PARLE-HSB-100G	190.00	199	10	http://localhost:9000/nexus-media/public/pms/skus/PARLE-HSB-100G/main.svg	0	170.00	0	[{"key": "Pack Size", "value": "100g"}]

326	121	PARLE-HSB-500G	190.00	55	10	http://localhost:9000/nexus-media/public/pms/skus/PARLE-HSB-500G/main.svg	0	\N	0	[{"key": "Pack Size", "value": "500g"}]

327	122	ITC-SDF-100G	440.00	40	10	http://localhost:9000/nexus-media/public/pms/skus/ITC-SDF-100G/main.svg	0	\N	0	[{"key": "Pack Size", "value": "100g"}]

328	122	ITC-SDF-200G	440.00	39	10	http://localhost:9000/nexus-media/public/pms/skus/ITC-SDF-200G/main.svg	0	370.00	0	[{"key": "Pack Size", "value": "200g"}]

329	122	ITC-SDF-500G	440.00	212	10	http://localhost:9000/nexus-media/public/pms/skus/ITC-SDF-500G/main.svg	0	\N	0	[{"key": "Pack Size", "value": "500g"}]

330	123	NESTLE-MUNCH-100G	410.00	89	10	http://localhost:9000/nexus-media/public/pms/skus/NESTLE-MUNCH-100G/main.svg	0	\N	0	[{"key": "Pack Size", "value": "100g"}]

332	123	NESTLE-MUNCH-500G	410.00	111	10	http://localhost:9000/nexus-media/public/pms/skus/NESTLE-MUNCH-500G/main.svg	0	\N	0	[{"key": "Pack Size", "value": "500g"}]

334	124	DABUR-REAL-1L	650.00	12	10	http://localhost:9000/nexus-media/public/pms/skus/DABUR-REAL-1L/main.svg	0	\N	0	[{"key": "Pack Size", "value": "1L"}]

335	124	DABUR-REAL-2L	750.00	154	10	http://localhost:9000/nexus-media/public/pms/skus/DABUR-REAL-2L/main.svg	0	\N	0	[{"key": "Pack Size", "value": "2L"}]

336	125	KELLOGG-KCF-100G	300.00	41	10	http://localhost:9000/nexus-media/public/pms/skus/KELLOGG-KCF-100G/main.svg	0	\N	0	[{"key": "Pack Size", "value": "100g"}]

338	125	KELLOGG-KCF-500G	300.00	199	10	http://localhost:9000/nexus-media/public/pms/skus/KELLOGG-KCF-500G/main.svg	0	\N	0	[{"key": "Pack Size", "value": "500g"}]

339	126	MTR-MPOHA-100G	640.00	21	10	http://localhost:9000/nexus-media/public/pms/skus/MTR-MPOHA-100G/main.svg	0	\N	0	[{"key": "Pack Size", "value": "100g"}]

340	126	MTR-MPOHA-200G	640.00	73	10	http://localhost:9000/nexus-media/public/pms/skus/MTR-MPOHA-200G/main.svg	0	\N	0	[{"key": "Pack Size", "value": "200g"}]

341	126	MTR-MPOHA-500G	640.00	157	10	http://localhost:9000/nexus-media/public/pms/skus/MTR-MPOHA-500G/main.svg	0	\N	0	[{"key": "Pack Size", "value": "500g"}]

343	127	AASHIRVAAD-MGA-200G	80.00	203	10	http://localhost:9000/nexus-media/public/pms/skus/AASHIRVAAD-MGA-200G/main.svg	0	\N	0	[{"key": "Pack Size", "value": "200g"}]

344	127	AASHIRVAAD-MGA-500G	80.00	230	10	http://localhost:9000/nexus-media/public/pms/skus/AASHIRVAAD-MGA-500G/main.svg	0	\N	0	[{"key": "Pack Size", "value": "500g"}]

345	128	TATA-SPOHA-100G	900.00	107	10	http://localhost:9000/nexus-media/public/pms/skus/TATA-SPOHA-100G/main.svg	0	\N	0	[{"key": "Pack Size", "value": "100g"}]

346	128	TATA-SPOHA-200G	900.00	61	10	http://localhost:9000/nexus-media/public/pms/skus/TATA-SPOHA-200G/main.svg	0	810.00	0	[{"key": "Pack Size", "value": "200g"}]

347	128	TATA-SPOHA-500G	900.00	119	10	http://localhost:9000/nexus-media/public/pms/skus/TATA-SPOHA-500G/main.svg	0	\N	0	[{"key": "Pack Size", "value": "500g"}]

349	129	FORTUNE-FSO-1L	560.00	202	10	http://localhost:9000/nexus-media/public/pms/skus/FORTUNE-FSO-1L/main.svg	0	\N	0	[{"key": "Pack Size", "value": "1L"}]

350	129	FORTUNE-FSO-2L	650.00	62	10	http://localhost:9000/nexus-media/public/pms/skus/FORTUNE-FSO-2L/main.svg	0	\N	0	[{"key": "Pack Size", "value": "2L"}]

351	130	PATANJALI-DALIA-100G	640.00	91	10	http://localhost:9000/nexus-media/public/pms/skus/PATANJALI-DALIA-100G/main.svg	0	580.00	0	[{"key": "Pack Size", "value": "100g"}]

352	130	PATANJALI-DALIA-200G	640.00	223	10	http://localhost:9000/nexus-media/public/pms/skus/PATANJALI-DALIA-200G/main.svg	0	\N	0	[{"key": "Pack Size", "value": "200g"}]

6	2	SAMSUNG-GS26-BLK-256	74000.00	110	10	http://localhost:9000/nexus-media/public/pms/skus/SAMSUNG-GS26-BLK-256/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}, {"key": "Storage", "value": "256GB"}]

354	131	UNILEVER-KTK-500ML	180.00	168	10	http://localhost:9000/nexus-media/public/pms/skus/UNILEVER-KTK-500ML/main.svg	0	\N	0	[{"key": "Pack Size", "value": "500ml"}]

355	131	UNILEVER-KTK-1L	220.00	226	10	http://localhost:9000/nexus-media/public/pms/skus/UNILEVER-KTK-1L/main.svg	0	200.00	0	[{"key": "Pack Size", "value": "1L"}]

356	131	UNILEVER-KTK-2L	250.00	232	10	http://localhost:9000/nexus-media/public/pms/skus/UNILEVER-KTK-2L/main.svg	0	\N	0	[{"key": "Pack Size", "value": "2L"}]

357	132	AMUL-PGHEE-500ML	380.00	50	10	http://localhost:9000/nexus-media/public/pms/skus/AMUL-PGHEE-500ML/main.svg	0	\N	0	[{"key": "Pack Size", "value": "500ml"}]

358	132	AMUL-PGHEE-1L	460.00	139	10	http://localhost:9000/nexus-media/public/pms/skus/AMUL-PGHEE-1L/main.svg	0	\N	0	[{"key": "Pack Size", "value": "1L"}]

360	133	MOTHERDAIRY-PANEER-100G	430.00	49	10	http://localhost:9000/nexus-media/public/pms/skus/MOTHERDAIRY-PANEER-100G/main.svg	0	370.00	0	[{"key": "Pack Size", "value": "100g"}]

361	133	MOTHERDAIRY-PANEER-200G	430.00	101	10	http://localhost:9000/nexus-media/public/pms/skus/MOTHERDAIRY-PANEER-200G/main.svg	0	\N	0	[{"key": "Pack Size", "value": "200g"}]

362	133	MOTHERDAIRY-PANEER-500G	430.00	239	10	http://localhost:9000/nexus-media/public/pms/skus/MOTHERDAIRY-PANEER-500G/main.svg	0	\N	0	[{"key": "Pack Size", "value": "500g"}]

364	134	3M-MFC-50ML	7080.00	38	10	http://localhost:9000/nexus-media/public/pms/skus/3M-MFC-50ML/main.svg	0	6510.00	0	[{"key": "Pack Size", "value": "50ml"}]

365	134	3M-MFC-100ML	7080.00	113	10	http://localhost:9000/nexus-media/public/pms/skus/3M-MFC-100ML/main.svg	0	\N	0	[{"key": "Pack Size", "value": "100ml"}]

366	135	BOSCHI-CAF-STD	7210.00	11	10	http://localhost:9000/nexus-media/public/pms/skus/BOSCHI-CAF-STD/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}]

367	136	TVS-CBC-STD	1970.00	143	10	http://localhost:9000/nexus-media/public/pms/skus/TVS-CBC-STD/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}]

368	137	CEAT-TVK-STD	8640.00	154	10	http://localhost:9000/nexus-media/public/pms/skus/CEAT-TVK-STD/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}]

370	139	MOTUL-XCESS-STD	7590.00	53	10	http://localhost:9000/nexus-media/public/pms/skus/MOTUL-XCESS-STD/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}]

371	140	GULF-FG530-STD	6640.00	115	10	http://localhost:9000/nexus-media/public/pms/skus/GULF-FG530-STD/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}]

372	141	3M-PRIT-30ML	8130.00	122	10	http://localhost:9000/nexus-media/public/pms/skus/3M-PRIT-30ML/main.svg	0	\N	0	[{"key": "Pack Size", "value": "30ml"}]

373	141	3M-PRIT-50ML	8130.00	49	10	http://localhost:9000/nexus-media/public/pms/skus/3M-PRIT-50ML/main.svg	0	6670.00	0	[{"key": "Pack Size", "value": "50ml"}]

375	142	MICHELIN-XM2P-R15	2850.00	59	10	http://localhost:9000/nexus-media/public/pms/skus/MICHELIN-XM2P-R15/main.svg	0	\N	0	[{"key": "Size", "value": "195/65 R15"}]

376	142	MICHELIN-XM2P-R16	2850.00	20	10	http://localhost:9000/nexus-media/public/pms/skus/MICHELIN-XM2P-R16/main.svg	0	\N	0	[{"key": "Size", "value": "205/55 R16"}]

377	142	MICHELIN-XM2P-R17	2850.00	116	10	http://localhost:9000/nexus-media/public/pms/skus/MICHELIN-XM2P-R17/main.svg	0	\N	0	[{"key": "Size", "value": "215/60 R17"}]

378	143	MRF-PERF-R15	8280.00	41	10	http://localhost:9000/nexus-media/public/pms/skus/MRF-PERF-R15/main.svg	0	7620.00	0	[{"key": "Size", "value": "195/65 R15"}]

380	143	MRF-PERF-R17	8280.00	121	10	http://localhost:9000/nexus-media/public/pms/skus/MRF-PERF-R17/main.svg	0	\N	0	[{"key": "Size", "value": "215/60 R17"}]

381	144	APOLLO-ALN4-R15	5830.00	144	10	http://localhost:9000/nexus-media/public/pms/skus/APOLLO-ALN4-R15/main.svg	0	\N	0	[{"key": "Size", "value": "195/65 R15"}]

9	3	GOOGLE-PX11-BLK-128	54640.00	89	10	http://localhost:9000/nexus-media/public/pms/skus/GOOGLE-PX11-BLK-128/main.svg	0	49180.00	0	[{"key": "Color", "value": "Black"}, {"key": "Storage", "value": "128GB"}]

16	4	LAVA-TAB11-SLV-256	27700.00	17	10	http://localhost:9000/nexus-media/public/pms/skus/LAVA-TAB11-SLV-256/main.svg	0	\N	0	[{"key": "Color", "value": "Silver"}, {"key": "Storage", "value": "256GB"}]

22	6	MICRO-INN3-BLK-256	38750.00	114	10	http://localhost:9000/nexus-media/public/pms/skus/MICRO-INN3-BLK-256/main.svg	0	31780.00	0	[{"key": "Color", "value": "Black"}, {"key": "Storage", "value": "256GB"}]

27	7	DELL-IN16P-32-1TB-BLK	136640.00	76	10	http://localhost:9000/nexus-media/public/pms/skus/DELL-IN16P-32-1TB-BLK/main.svg	0	122980.00	0	[{"key": "RAM", "value": "32GB"}, {"key": "Color", "value": "Black"}, {"key": "Storage", "value": "1TB"}]

3	1	APPLE-IP17-SLV-128	53430.00	83	10	http://localhost:9000/nexus-media/public/pms/skus/APPLE-IP17-SLV-128/main.svg	0	\N	0	[{"key": "Color", "value": "Silver"}, {"key": "Storage", "value": "128GB"}]

33	9	ASUS-VBS16-16-512-BLK	61920.00	36	10	http://localhost:9000/nexus-media/public/pms/skus/ASUS-VBS16-16-512-BLK/main.svg	0	\N	0	[{"key": "RAM", "value": "16GB"}, {"key": "Color", "value": "Black"}, {"key": "Storage", "value": "512GB"}]

39	10	ONEPLUS-OPPAD3-SLV-128	43630.00	38	10	http://localhost:9000/nexus-media/public/pms/skus/ONEPLUS-OPPAD3-SLV-128/main.svg	0	\N	0	[{"key": "Color", "value": "Silver"}, {"key": "Storage", "value": "128GB"}]

44	12	NOTHING-EAR3-SLV	6930.00	9	10	http://localhost:9000/nexus-media/public/pms/skus/NOTHING-EAR3-SLV/main.svg	0	\N	0	[{"key": "Color", "value": "Silver"}]

49	16	SONY-WH6-BLK	34990.00	8	10	http://localhost:9000/nexus-media/public/pms/skus/SONY-WH6-BLK/main.svg	0	31490.00	0	[{"key": "Color", "value": "Black"}]

53	18	SAMSUNG-ODG6-27-QHD	11010.00	58	10	http://localhost:9000/nexus-media/public/pms/skus/SAMSUNG-ODG6-27-QHD/main.svg	0	\N	0	[{"key": "Resolution", "value": "2560x1440"}, {"key": "Size (inches)", "value": "27"}]

60	20	LEVIS-511SL-XL-BLK	4570.00	120	10	http://localhost:9000/nexus-media/public/pms/skus/LEVIS-511SL-XL-BLK/main.svg	0	\N	0	[{"key": "Size", "value": "XL"}, {"key": "Color", "value": "Black"}]

65	22	PETEREN-PEFSH-S-BLK	2580.00	98	10	http://localhost:9000/nexus-media/public/pms/skus/PETEREN-PEFSH-S-BLK/main.svg	0	\N	0	[{"key": "Size", "value": "S"}, {"key": "Color", "value": "Black"}]

70	23	RAYMOND-RFT-M-BLK	6050.00	98	10	http://localhost:9000/nexus-media/public/pms/skus/RAYMOND-RFT-M-BLK/main.svg	0	\N	0	[{"key": "Size", "value": "M"}, {"key": "Color", "value": "Black"}]

76	25	ZARA-ZRBL-S-BLK	970.00	100	10	http://localhost:9000/nexus-media/public/pms/skus/ZARA-ZRBL-S-BLK/main.svg	0	820.00	0	[{"key": "Size", "value": "S"}, {"key": "Color", "value": "Black"}]

83	26	HM-HMD-XL-BLK	2500.00	150	10	http://localhost:9000/nexus-media/public/pms/skus/HM-HMD-XL-BLK/main.svg	0	\N	0	[{"key": "Size", "value": "XL"}, {"key": "Color", "value": "Black"}]

87	27	FABIND-FBKRT-XL-BLK	2710.00	38	10	http://localhost:9000/nexus-media/public/pms/skus/FABIND-FBKRT-XL-BLK/main.svg	0	\N	0	[{"key": "Size", "value": "XL"}, {"key": "Color", "value": "Black"}]

89	28	MANYAVAR-MVKRT-M-BLK	2550.00	93	10	http://localhost:9000/nexus-media/public/pms/skus/MANYAVAR-MVKRT-M-BLK/main.svg	0	\N	0	[{"key": "Size", "value": "M"}, {"key": "Color", "value": "Black"}]

90	28	MANYAVAR-MVKRT-L-BLK	2550.00	149	10	http://localhost:9000/nexus-media/public/pms/skus/MANYAVAR-MVKRT-L-BLK/main.svg	0	2300.00	0	[{"key": "Size", "value": "L"}, {"key": "Color", "value": "Black"}]

97	30	NIKE-AM270-BLK-9	9820.00	95	10	http://localhost:9000/nexus-media/public/pms/skus/NIKE-AM270-BLK-9/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}, {"key": "Size (US)", "value": "9"}]

102	31	ADIDAS-UBLGT-WHT-8	8260.00	23	10	http://localhost:9000/nexus-media/public/pms/skus/ADIDAS-UBLGT-WHT-8/main.svg	0	\N	0	[{"key": "Color", "value": "White"}, {"key": "Size (US)", "value": "8"}]

108	33	TNF-VEND-BLK-8	2540.00	133	10	http://localhost:9000/nexus-media/public/pms/skus/TNF-VEND-BLK-8/main.svg	0	2160.00	0	[{"key": "Color", "value": "Black"}, {"key": "Size (US)", "value": "8"}]

383	144	APOLLO-ALN4-R17	5830.00	78	10	http://localhost:9000/nexus-media/public/pms/skus/APOLLO-ALN4-R17/main.svg	0	\N	0	[{"key": "Size", "value": "215/60 R17"}]

385	145	CEAT-MILAZE-R16	11610.00	130	10	http://localhost:9000/nexus-media/public/pms/skus/CEAT-MILAZE-R16/main.svg	0	\N	0	[{"key": "Size", "value": "205/55 R16"}]

386	145	CEAT-MILAZE-R17	11610.00	126	10	http://localhost:9000/nexus-media/public/pms/skus/CEAT-MILAZE-R17/main.svg	0	\N	0	[{"key": "Size", "value": "215/60 R17"}]

387	146	TVS-EGRIP-R15	5380.00	132	10	http://localhost:9000/nexus-media/public/pms/skus/TVS-EGRIP-R15/main.svg	0	4410.00	0	[{"key": "Size", "value": "195/65 R15"}]

388	146	TVS-EGRIP-R16	5380.00	147	10	http://localhost:9000/nexus-media/public/pms/skus/TVS-EGRIP-R16/main.svg	0	\N	0	[{"key": "Size", "value": "205/55 R16"}]

390	147	BOSCHI-PSP-STD	3840.00	66	10	http://localhost:9000/nexus-media/public/pms/skus/BOSCHI-PSP-STD/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}]

391	148	CEAT-CBPS-STD	5420.00	103	10	http://localhost:9000/nexus-media/public/pms/skus/CEAT-CBPS-STD/main.svg	0	4610.00	0	[{"key": "Color", "value": "Black"}]

392	149	3M-CVC-BLK	3760.00	32	10	http://localhost:9000/nexus-media/public/pms/skus/3M-CVC-BLK/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}]

393	149	3M-CVC-SLV	3830.00	25	10	http://localhost:9000/nexus-media/public/pms/skus/3M-CVC-SLV/main.svg	0	\N	0	[{"key": "Color", "value": "Silver"}]

396	151	CASTROL-EF-50ML	2770.00	43	10	http://localhost:9000/nexus-media/public/pms/skus/CASTROL-EF-50ML/main.svg	0	2270.00	0	[{"key": "Pack Size", "value": "50ml"}]

397	151	CASTROL-EF-100ML	2770.00	144	10	http://localhost:9000/nexus-media/public/pms/skus/CASTROL-EF-100ML/main.svg	0	\N	0	[{"key": "Pack Size", "value": "100ml"}]

398	152	MOTUL-CCLEAN-30ML	13020.00	67	10	http://localhost:9000/nexus-media/public/pms/skus/MOTUL-CCLEAN-30ML/main.svg	0	\N	0	[{"key": "Pack Size", "value": "30ml"}]

399	152	MOTUL-CCLEAN-50ML	13020.00	114	10	http://localhost:9000/nexus-media/public/pms/skus/MOTUL-CCLEAN-50ML/main.svg	0	\N	0	[{"key": "Pack Size", "value": "50ml"}]

115	35	VANH-VHBLT-M	6090.00	60	10	http://localhost:9000/nexus-media/public/pms/skus/VANH-VHBLT-M/main.svg	0	\N	0	[{"key": "Size", "value": "M"}]

120	37	NIKE-HBPK-SLV	1170.00	73	10	http://localhost:9000/nexus-media/public/pms/skus/NIKE-HBPK-SLV/main.svg	0	\N	0	[{"key": "Color", "value": "Silver"}]

125	40	IKEA-LACK-BLK	21530.00	80	10	http://localhost:9000/nexus-media/public/pms/skus/IKEA-LACK-BLK/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}]

131	43	DYSON-V15D-STD	10510.00	25	10	http://localhost:9000/nexus-media/public/pms/skus/DYSON-V15D-STD/main.svg	0	\N	0	\N

135	45	PRESTIGE-AFPRO-6L	23130.00	30	10	http://localhost:9000/nexus-media/public/pms/skus/PRESTIGE-AFPRO-6L/main.svg	0	18970.00	0	[{"key": "Capacity", "value": "6L"}]

7	2	SAMSUNG-GS26-SLV-128	69890.00	107	10	http://localhost:9000/nexus-media/public/pms/skus/SAMSUNG-GS26-SLV-128/main.svg	0	\N	0	[{"key": "Color", "value": "Silver"}, {"key": "Storage", "value": "128GB"}]

8	2	SAMSUNG-GS26-SLV-256	75370.00	85	10	http://localhost:9000/nexus-media/public/pms/skus/SAMSUNG-GS26-SLV-256/main.svg	0	\N	0	[{"key": "Color", "value": "Silver"}, {"key": "Storage", "value": "256GB"}]

10	3	GOOGLE-PX11-BLK-256	59010.00	50	10	http://localhost:9000/nexus-media/public/pms/skus/GOOGLE-PX11-BLK-256/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}, {"key": "Storage", "value": "256GB"}]

11	3	GOOGLE-PX11-SLV-128	55730.00	110	10	http://localhost:9000/nexus-media/public/pms/skus/GOOGLE-PX11-SLV-128/main.svg	0	\N	0	[{"key": "Color", "value": "Silver"}, {"key": "Storage", "value": "128GB"}]

12	3	GOOGLE-PX11-SLV-256	60100.00	99	10	http://localhost:9000/nexus-media/public/pms/skus/GOOGLE-PX11-SLV-256/main.svg	0	\N	0	[{"key": "Color", "value": "Silver"}, {"key": "Storage", "value": "256GB"}]

13	4	LAVA-TAB11-BLK-128	25180.00	75	10	http://localhost:9000/nexus-media/public/pms/skus/LAVA-TAB11-BLK-128/main.svg	0	20650.00	0	[{"key": "Color", "value": "Black"}, {"key": "Storage", "value": "128GB"}]

14	4	LAVA-TAB11-BLK-256	27190.00	52	10	http://localhost:9000/nexus-media/public/pms/skus/LAVA-TAB11-BLK-256/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}, {"key": "Storage", "value": "256GB"}]

15	4	LAVA-TAB11-SLV-128	25680.00	20	10	http://localhost:9000/nexus-media/public/pms/skus/LAVA-TAB11-SLV-128/main.svg	0	\N	0	[{"key": "Color", "value": "Silver"}, {"key": "Storage", "value": "128GB"}]

17	5	ONEPLUS-OP14-BLK-128	40530.00	29	10	http://localhost:9000/nexus-media/public/pms/skus/ONEPLUS-OP14-BLK-128/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}, {"key": "Storage", "value": "128GB"}]

18	5	ONEPLUS-OP14-BLK-256	43780.00	115	10	http://localhost:9000/nexus-media/public/pms/skus/ONEPLUS-OP14-BLK-256/main.svg	0	40280.00	0	[{"key": "Color", "value": "Black"}, {"key": "Storage", "value": "256GB"}]

19	5	ONEPLUS-OP14-SLV-128	41340.00	48	10	http://localhost:9000/nexus-media/public/pms/skus/ONEPLUS-OP14-SLV-128/main.svg	0	\N	0	[{"key": "Color", "value": "Silver"}, {"key": "Storage", "value": "128GB"}]

20	5	ONEPLUS-OP14-SLV-256	44590.00	112	10	http://localhost:9000/nexus-media/public/pms/skus/ONEPLUS-OP14-SLV-256/main.svg	0	\N	0	[{"key": "Color", "value": "Silver"}, {"key": "Storage", "value": "256GB"}]

21	6	MICRO-INN3-BLK-128	35880.00	90	10	http://localhost:9000/nexus-media/public/pms/skus/MICRO-INN3-BLK-128/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}, {"key": "Storage", "value": "128GB"}]

42	11	PORT-MUFFM2-SLV	18530.00	35	10	http://localhost:9000/nexus-media/public/pms/skus/PORT-MUFFM2-SLV/main.svg	0	\N	0	[{"key": "Color", "value": "Silver"}]

286	105	MCGRAW-CCIT-HC	1260.00	211	10	http://localhost:9000/nexus-media/public/pms/skus/MCGRAW-CCIT-HC/main.svg	0	\N	0	[{"key": "Format", "value": "Hardcover"}]

150	54	CROMPTON-SEK-1.5L	1740.00	8	10	http://localhost:9000/nexus-media/public/pms/skus/CROMPTON-SEK-1.5L/main.svg	0	\N	0	[{"key": "Capacity", "value": "1.5L"}]

155	56	PRESTIGE-ODP-24CM	4820.00	98	10	http://localhost:9000/nexus-media/public/pms/skus/PRESTIGE-ODP-24CM/main.svg	0	\N	0	[{"key": "Size", "value": "24cm"}]

160	57	HAWKINS-FNT-28CM	6970.00	33	10	http://localhost:9000/nexus-media/public/pms/skus/HAWKINS-FNT-28CM/main.svg	0	\N	0	[{"key": "Size", "value": "28cm"}]

166	60	CLINIQUE-MS100-100ML	900.00	94	10	http://localhost:9000/nexus-media/public/pms/skus/CLINIQUE-MS100-100ML/main.svg	0	810.00	0	[{"key": "Pack Size", "value": "100ml"}]

172	64	FOREST-FTM-30ML	1450.00	75	10	http://localhost:9000/nexus-media/public/pms/skus/FOREST-FTM-30ML/main.svg	0	\N	0	[{"key": "Pack Size", "value": "30ml"}]

177	65	PLUM-GTFW-100ML	2300.00	41	10	http://localhost:9000/nexus-media/public/pms/skus/PLUM-GTFW-100ML/main.svg	0	\N	0	[{"key": "Pack Size", "value": "100ml"}]

182	67	MAYBELLINE-FMF-110	1660.00	79	10	http://localhost:9000/nexus-media/public/pms/skus/MAYBELLINE-FMF-110/main.svg	0	\N	0	[{"key": "Shade", "value": "110 Porcelain"}]

187	68	LAKME-9T5-120	2180.00	107	10	http://localhost:9000/nexus-media/public/pms/skus/LAKME-9T5-120/main.svg	0	\N	0	[{"key": "Shade", "value": "120 Classic Ivory"}]

191	69	NYKAA-SKGEN-120	1710.00	8	10	http://localhost:9000/nexus-media/public/pms/skus/NYKAA-SKGEN-120/main.svg	0	\N	0	[{"key": "Shade", "value": "120 Classic Ivory"}]

197	71	DOVE-DIR-30ML	760.00	152	10	http://localhost:9000/nexus-media/public/pms/skus/DOVE-DIR-30ML/main.svg	0	\N	0	[{"key": "Pack Size", "value": "30ml"}]

202	72	LOREAL-ARS-100ML	1840.00	149	10	http://localhost:9000/nexus-media/public/pms/skus/LOREAL-ARS-100ML/main.svg	0	1620.00	0	[{"key": "Pack Size", "value": "100ml"}]

207	74	NIVEA-MSG-50ML	1570.00	12	10	http://localhost:9000/nexus-media/public/pms/skus/NIVEA-MSG-50ML/main.svg	0	1290.00	0	[{"key": "Pack Size", "value": "50ml"}]

213	76	PLUM-GTSG-50ML	320.00	10	10	http://localhost:9000/nexus-media/public/pms/skus/PLUM-GTSG-50ML/main.svg	0	\N	0	[{"key": "Pack Size", "value": "50ml"}]

218	77	UA-CA10-WHT-9	8630.00	115	10	http://localhost:9000/nexus-media/public/pms/skus/UA-CA10-WHT-9/main.svg	0	\N	0	[{"key": "Color", "value": "White"}, {"key": "Size (US)", "value": "9"}]

223	79	NIVIA-RACER-BLK-8	6930.00	57	10	http://localhost:9000/nexus-media/public/pms/skus/NIVIA-RACER-BLK-8/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}, {"key": "Size (US)", "value": "8"}]

228	80	VECTORX-SPRINT-BLK-9	4130.00	30	10	http://localhost:9000/nexus-media/public/pms/skus/VECTORX-SPRINT-BLK-9/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}, {"key": "Size (US)", "value": "9"}]

138	47	KENT-KGR-STD	22540.00	111	10	http://localhost:9000/nexus-media/public/pms/skus/KENT-KGR-STD/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}]

139	48	LG-LGW65-6.5KG	14170.00	66	10	http://localhost:9000/nexus-media/public/pms/skus/LG-LGW65-6.5KG/main.svg	0	12040.00	0	[{"key": "Capacity", "value": "6.5kg"}]

145	51	HAVELLS-AQP-10L	7550.00	75	10	http://localhost:9000/nexus-media/public/pms/skus/HAVELLS-AQP-10L/main.svg	0	\N	0	[{"key": "Capacity", "value": "10L"}]

250	91	SG-SGP-S10	6600.00	81	10	http://localhost:9000/nexus-media/public/pms/skus/SG-SGP-S10/main.svg	0	\N	0	[{"key": "Size", "value": "10"}]

234	84	VECTORX-PTG-M	6750.00	23	10	http://localhost:9000/nexus-media/public/pms/skus/VECTORX-PTG-M/main.svg	0	6080.00	0	[{"key": "Size", "value": "M"}]

238	85	DECATHLON-BYM-SLV	5120.00	31	10	http://localhost:9000/nexus-media/public/pms/skus/DECATHLON-BYM-SLV/main.svg	0	4510.00	0	[{"key": "Color", "value": "Silver"}]

244	89	YONEX-A100Z-STD	3670.00	101	10	http://localhost:9000/nexus-media/public/pms/skus/YONEX-A100Z-STD/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}]

254	94	COSCO-CVB-STD	2460.00	117	10	http://localhost:9000/nexus-media/public/pms/skus/COSCO-CVB-STD/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}]

260	96	PRH-MNL-KD	1190.00	46	10	http://localhost:9000/nexus-media/public/pms/skus/PRH-MNL-KD/main.svg	0	\N	0	[{"key": "Format", "value": "Kindle"}]

266	98	RUPA-TSP-KD	1700.00	168	10	http://localhost:9000/nexus-media/public/pms/skus/RUPA-TSP-KD/main.svg	0	\N	0	[{"key": "Format", "value": "Kindle"}]

271	100	PRH-AH-HC	1540.00	47	10	http://localhost:9000/nexus-media/public/pms/skus/PRH-AH-HC/main.svg	0	\N	0	[{"key": "Format", "value": "Hardcover"}]

276	102	PEARSON-SSG-PB	1650.00	72	10	http://localhost:9000/nexus-media/public/pms/skus/PEARSON-SSG-PB/main.svg	0	\N	0	[{"key": "Format", "value": "Paperback"}]

282	104	OREILLY-DDIA-PB	1270.00	51	10	http://localhost:9000/nexus-media/public/pms/skus/OREILLY-DDIA-PB/main.svg	0	\N	0	[{"key": "Format", "value": "Paperback"}]

284	104	OREILLY-DDIA-KD	1220.00	221	10	http://localhost:9000/nexus-media/public/pms/skus/OREILLY-DDIA-KD/main.svg	0	\N	0	[{"key": "Format", "value": "Kindle"}]

288	106	PEARSON-JPC-PB	1510.00	216	10	http://localhost:9000/nexus-media/public/pms/skus/PEARSON-JPC-PB/main.svg	0	1330.00	0	[{"key": "Format", "value": "Paperback"}]

294	108	ARIHANT-PYCP-PB	870.00	182	10	http://localhost:9000/nexus-media/public/pms/skus/ARIHANT-PYCP-PB/main.svg	0	\N	0	[{"key": "Format", "value": "Paperback"}]

299	110	CROSSWORD-PRN-SLV	930.00	58	10	http://localhost:9000/nexus-media/public/pms/skus/CROSSWORD-PRN-SLV/main.svg	0	\N	0	[{"key": "Color", "value": "Silver"}]

305	114	PRH-RDJ-STD	700.00	135	10	http://localhost:9000/nexus-media/public/pms/skus/PRH-RDJ-STD/main.svg	0	\N	0	[{"key": "Color", "value": "Black"}]

310	116	COKE-COKE-750ML	890.00	122	10	http://localhost:9000/nexus-media/public/pms/skus/COKE-COKE-750ML/main.svg	0	730.00	0	[{"key": "Pack Size", "value": "750ml"}]

315	118	TATA-TTG-100G	420.00	94	10	http://localhost:9000/nexus-media/public/pms/skus/TATA-TTG-100G/main.svg	0	380.00	0	[{"key": "Pack Size", "value": "100g"}]

320	119	MOTHERDAIRY-TDMLK-2L	670.00	183	10	http://localhost:9000/nexus-media/public/pms/skus/MOTHERDAIRY-TDMLK-2L/main.svg	0	\N	0	[{"key": "Pack Size", "value": "2L"}]

325	121	PARLE-HSB-200G	190.00	6	10	http://localhost:9000/nexus-media/public/pms/skus/PARLE-HSB-200G/main.svg	0	\N	0	[{"key": "Pack Size", "value": "200g"}]

331	123	NESTLE-MUNCH-200G	410.00	49	10	http://localhost:9000/nexus-media/public/pms/skus/NESTLE-MUNCH-200G/main.svg	0	\N	0	[{"key": "Pack Size", "value": "200g"}]

333	124	DABUR-REAL-500ML	540.00	100	10	http://localhost:9000/nexus-media/public/pms/skus/DABUR-REAL-500ML/main.svg	0	460.00	0	[{"key": "Pack Size", "value": "500ml"}]

337	125	KELLOGG-KCF-200G	300.00	7	10	http://localhost:9000/nexus-media/public/pms/skus/KELLOGG-KCF-200G/main.svg	0	260.00	0	[{"key": "Pack Size", "value": "200g"}]

342	127	AASHIRVAAD-MGA-100G	80.00	47	10	http://localhost:9000/nexus-media/public/pms/skus/AASHIRVAAD-MGA-100G/main.svg	0	70.00	0	[{"key": "Pack Size", "value": "100g"}]

348	129	FORTUNE-FSO-500ML	470.00	144	10	http://localhost:9000/nexus-media/public/pms/skus/FORTUNE-FSO-500ML/main.svg	0	\N	0	[{"key": "Pack Size", "value": "500ml"}]

353	130	PATANJALI-DALIA-500G	640.00	168	10	http://localhost:9000/nexus-media/public/pms/skus/PATANJALI-DALIA-500G/main.svg	0	\N	0	[{"key": "Pack Size", "value": "500g"}]

359	132	AMUL-PGHEE-2L	540.00	92	10	http://localhost:9000/nexus-media/public/pms/skus/AMUL-PGHEE-2L/main.svg	0	\N	0	[{"key": "Pack Size", "value": "2L"}]

363	134	3M-MFC-30ML	7080.00	34	10	http://localhost:9000/nexus-media/public/pms/skus/3M-MFC-30ML/main.svg	0	\N	0	[{"key": "Pack Size", "value": "30ml"}]

369	138	CASTROL-MAG530-STD	2580.00	125	10	http://localhost:9000/nexus-media/public/pms/skus/CASTROL-MAG530-STD/main.svg	0	2120.00	0	[{"key": "Color", "value": "Black"}]

374	141	3M-PRIT-100ML	8130.00	23	10	http://localhost:9000/nexus-media/public/pms/skus/3M-PRIT-100ML/main.svg	0	\N	0	[{"key": "Pack Size", "value": "100ml"}]

379	143	MRF-PERF-R16	8280.00	150	10	http://localhost:9000/nexus-media/public/pms/skus/MRF-PERF-R16/main.svg	0	\N	0	[{"key": "Size", "value": "205/55 R16"}]

382	144	APOLLO-ALN4-R16	5830.00	112	10	http://localhost:9000/nexus-media/public/pms/skus/APOLLO-ALN4-R16/main.svg	0	4780.00	0	[{"key": "Size", "value": "205/55 R16"}]

384	145	CEAT-MILAZE-R15	11610.00	10	10	http://localhost:9000/nexus-media/public/pms/skus/CEAT-MILAZE-R15/main.svg	0	\N	0	[{"key": "Size", "value": "195/65 R15"}]

389	146	TVS-EGRIP-R17	5380.00	156	10	http://localhost:9000/nexus-media/public/pms/skus/TVS-EGRIP-R17/main.svg	0	\N	0	[{"key": "Size", "value": "215/60 R17"}]

395	151	CASTROL-EF-30ML	2770.00	111	10	http://localhost:9000/nexus-media/public/pms/skus/CASTROL-EF-30ML/main.svg	0	\N	0	[{"key": "Pack Size", "value": "30ml"}]

400	152	MOTUL-CCLEAN-100ML	13020.00	119	10	http://localhost:9000/nexus-media/public/pms/skus/MOTUL-CCLEAN-100ML/main.svg	0	11980.00	0	[{"key": "Pack Size", "value": "100ml"}]

\.


--
-- Data for Name: outbox_event; Type: TABLE DATA; Schema: integration; Owner: postgres
--

COPY integration.outbox_event (id, aggregate_type, aggregate_id, type, payload, status, retry_count, created_at) FROM stdin;
1	Order	1	CancelOrder	7200000	SENT	0	2026-09-25 14:16:43.13
2	Order	2	CancelOrder	7200000	SENT	0	2026-09-25 14:25:35.798
3	Order	3	CancelOrder	7200000	SENT	0	2026-09-25 14:31:52.474
4	Order	4	CancelOrder	7200000	SENT	0	2026-09-25 23:02:40.41
5	Order	5	CancelOrder	7200000	SENT	0	2026-09-25 23:10:06.271
6	Order	6	CancelOrder	7200000	SENT	0	2026-09-27 12:59:38.227357
7	Order	7	CancelOrder	7200000	SENT	0	2026-09-27 13:14:12.133665
8	Order	8	CancelOrder	7200000	SENT	0	2026-09-27 13:24:14.183497
\.


--
-- Data for Name: sms_coupon; Type: TABLE DATA; Schema: marketing; Owner: postgres
--

COPY marketing.sms_coupon (id, type, name, count, amount, per_limit, min_order_amount, start_time, end_time, use_type, receive_count, code, max_discount_amount, version, stackable, is_public) FROM stdin;
1	0	Welcome Promo	\N	99	10.00	1	0.00	2026-09-25 12:50:41.059784	2026-10-25 12:50:41.059784	0	\N	\N	WELCOME10
\.


--
--



--
-- Data for Name: sms_coupon_history; Type: TABLE DATA; Schema: marketing; Owner: postgres
--

COPY marketing.sms_coupon_history (id, coupon_id, member_id, coupon_code, member_nickname, get_type, create_time, use_status, use_time, order_id, order_sn) FROM stdin;
\.


--
-- Data for Name: sms_coupon_product_category_relation; Type: TABLE DATA; Schema: marketing; Owner: postgres
--

COPY marketing.sms_coupon_product_category_relation (id, coupon_id, product_category_id) FROM stdin;
\.


--
-- Data for Name: sms_coupon_product_relation; Type: TABLE DATA; Schema: marketing; Owner: postgres
--

COPY marketing.sms_coupon_product_relation (id, coupon_id, product_id) FROM stdin;
\.


--
-- Data for Name: sms_flash_promotion; Type: TABLE DATA; Schema: marketing; Owner: postgres
--

COPY marketing.sms_flash_promotion (id, title, start_date, end_date, status, create_time) FROM stdin;
\.


--
-- Data for Name: sms_flash_promotion_log; Type: TABLE DATA; Schema: marketing; Owner: postgres
--

COPY marketing.sms_flash_promotion_log (id, member_id, product_id, flash_promotion_id, session_id, order_id, quantity, status, create_time) FROM stdin;
\.


--
-- Data for Name: sms_flash_promotion_product_relation; Type: TABLE DATA; Schema: marketing; Owner: postgres
--

COPY marketing.sms_flash_promotion_product_relation (id, flash_promotion_id, flash_promotion_session_id, product_id, flash_promotion_price, flash_promotion_count, flash_promotion_limit) FROM stdin;
\.


--
-- Data for Name: sms_flash_promotion_session; Type: TABLE DATA; Schema: marketing; Owner: postgres
--

COPY marketing.sms_flash_promotion_session (id, name, start_time, end_time, status, create_time) FROM stdin;
\.


--
-- Data for Name: flyway_schema_history; Type: TABLE DATA; Schema: migration; Owner: postgres
--

COPY migration.flyway_schema_history (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) FROM stdin;
1	1	baseline	SQL	V1__baseline.sql	146656653	postgres	2026-09-25 11:17:39.702335	93	t

2	2	seed full pms	SQL	V2__seed_full_catalog.sql	-734177831	postgres	2026-09-25 12:13:07.03409	58	t

3	3	membership and admin	SQL	V3__membership_and_admin.sql	17031760	postgres	2026-09-25 12:13:07.158114	2	t

4	3.1	product desc text	SQL	V3_1__product_desc_text.sql	1882706475	postgres	2026-09-25 12:13:07.165683	1	t
5	4	english menus	SQL	V4__english_menus.sql	1654041005	postgres	2026-09-25 12:13:07.170771	4	t

6	5	english resources	SQL	V5__english_resources.sql	1611983688	postgres	2026-09-25 12:13:07.178479	6	t

7	6	coupon enhancements	SQL	V6__coupon_enhancements.sql	-1620614981	postgres	2026-09-25 12:13:07.189167	7	t

8	7	flash sale tables	SQL	V7__flash_sale_tables.sql	1184604928	postgres	2026-09-25 12:13:07.200742	6	t

9	8	ai chat history	SQL	V8__ai_chat_history.sql	-929212253	postgres	2026-09-25 12:13:07.210667	2	t

10	9	coupon improvements	SQL	V9__coupon_improvements.sql	-1303713297	postgres	2026-09-25 12:49:00.385874	14	t

\.


--
-- Data for Name: oms_cart_item; Type: TABLE DATA; Schema: orders; Owner: postgres
--

COPY orders.oms_cart_item (id, product_id, product_sku_id, member_id, quantity, price, product_pic, product_name, product_sku_code, create_date, modify_date, delete_status, product_category_id, product_brand, product_sn, product_attr) FROM stdin;
19	1	2	1	1	56570.00	http://localhost:9000/nexus-media/public/pms/skus/APPLE-IP17-BLK-256/main.svg	iPhone 17 (Black, 256GB)	\N	APPLE-IP17-BLK-256	Customer One	2026-09-27 13:23:30.71045	1	\N
21	1	3	1	1	53430.00	http://localhost:9000/nexus-media/public/pms/skus/APPLE-IP17-SLV-128/main.svg	iPhone 17 (Silver, 128GB)	\N	APPLE-IP17-SLV-128	Customer One	2026-09-27 13:23:37.33667	1	\N
4	150	\N	1	97	4640.00	http://localhost:9000/nexus-media/public/pms/products/150/main.svg	Portable Tyre Inflator	\N	\N	Customer One	2026-09-25 14:24:42.129	1	\N
5	150	\N	1	1	4640.00	http://localhost:9000/nexus-media/public/pms/products/150/main.svg	Portable Tyre Inflator	\N	\N	Customer One	2026-09-25 14:26:31.164	1	\N
8	1	4	1	4	57620.00	http://localhost:9000/nexus-media/public/pms/skus/APPLE-IP17-SLV-256/main.svg	iPhone 17 (Silver, 256GB)	\N	\N	Customer One	2026-09-25 23:09:55.831	1	\N
20	1	4	1	2	57620.00	http://localhost:9000/nexus-media/public/pms/skus/APPLE-IP17-SLV-256/main.svg	iPhone 17 (Silver, 256GB)	\N	APPLE-IP17-SLV-256	Customer One	2026-09-27 13:23:34.42814	1	\N
6	1	2	1	4	56570.00	http://localhost:9000/nexus-media/public/pms/skus/APPLE-IP17-BLK-256/main.svg	iPhone 17 (Black, 256GB)	\N	\N	Customer One	2026-09-25 14:31:38.587	1	\N
18	1	1	1	2	52380.00	http://localhost:9000/nexus-media/public/pms/skus/APPLE-IP17-BLK-128/main.svg	iPhone 17 (Black, 128GB)	\N	APPLE-IP17-BLK-128	Customer One	2026-09-27 13:23:26.192193	1	\N
17	1	1	1	1	52380.00	http://localhost:9000/nexus-media/public/pms/skus/APPLE-IP17-BLK-128/main.svg	iPhone 17 (Black, 128GB)	\N	APPLE-IP17-BLK-128	Customer One	2026-09-27 13:13:34.827662	1	\N
9	1	\N	1	12	56570.00	http://localhost:9000/nexus-media/public/pms/skus/APPLE-IP17-BLK-256/main.svg	iPhone 17 (Black, 256GB)	\N	\N	Customer One	2026-09-27 12:58:37.549046	1	\N
11	1	\N	1	21	56570.00	http://localhost:9000/nexus-media/public/pms/skus/APPLE-IP17-BLK-256/main.svg	iPhone 17 (Black, 256GB)	\N	\N	Customer One	2026-09-27 13:01:54.033302	1	\N
10	1	\N	1	2	56570.00	http://localhost:9000/nexus-media/public/pms/skus/APPLE-IP17-BLK-256/main.svg	iPhone 17 (Black, 256GB)	\N	\N	Customer One	2026-09-27 13:01:30.656942	1	\N
16	1	2	1	12	56570.00	http://localhost:9000/nexus-media/public/pms/skus/APPLE-IP17-BLK-256/main.svg	iPhone 17 (Black, 256GB)	\N	APPLE-IP17-BLK-256	Customer One	2026-09-27 13:13:31.86371	1	\N
12	1	2	1	1	56570.00	http://localhost:9000/nexus-media/public/pms/skus/APPLE-IP17-BLK-256/main.svg	iPhone 17 (Black, 256GB)	\N	APPLE-IP17-BLK-256	Customer One	2026-09-27 13:09:21.208676	1	\N
13	1	4	1	2	57620.00	http://localhost:9000/nexus-media/public/pms/skus/APPLE-IP17-SLV-256/main.svg	iPhone 17 (Silver, 256GB)	\N	APPLE-IP17-SLV-256	Customer One	2026-09-27 13:09:25.557142	1	\N
7	1	2	1	20	56570.00	http://localhost:9000/nexus-media/public/pms/skus/APPLE-IP17-BLK-256/main.svg	iPhone 17 (Black, 256GB)	\N	\N	Customer One	2026-09-25 23:02:21.602	1	\N
15	1	1	1	22	52380.00	http://localhost:9000/nexus-media/public/pms/skus/APPLE-IP17-BLK-128/main.svg	iPhone 17 (Black, 128GB)	\N	APPLE-IP17-BLK-128	Customer One	2026-09-27 13:12:53.444645	1	\N
14	1	2	1	18	56570.00	http://localhost:9000/nexus-media/public/pms/skus/APPLE-IP17-BLK-256/main.svg	iPhone 17 (Black, 256GB)	\N	APPLE-IP17-BLK-256	Customer One	2026-09-27 13:12:49.042026	1	\N
\.


--
-- Data for Name: oms_company_address; Type: TABLE DATA; Schema: orders; Owner: postgres
--

COPY orders.oms_company_address (id, address_name, send_status, receive_status, name, phone, province, city, region, detail_address) FROM stdin;
1	Main Warehouse	1	1	Admin	1234567890	State	City	Region	123 Main St
\.


--
-- Data for Name: oms_order; Type: TABLE DATA; Schema: orders; Owner: postgres
--

COPY orders.oms_order (id, member_id, coupon_id, order_sn, create_time, member_username, total_amount, pay_amount, freight_amount, promotion_amount, points_discount_amount, coupon_amount, discount_amount, pay_type, source_type, status, order_type, delivery_company, delivery_sn, auto_confirm_day, earned_points, promotion_info, bill_type, bill_header, bill_content, bill_receiver_phone, bill_receiver_email, receiver_name, receiver_phone, receiver_post_code, receiver_province, receiver_city, receiver_region, receiver_detail_address, note, confirm_status, delete_status, used_points, payment_id, payment_time, delivery_time, receive_time, comment_time, modify_time, vendor_id) FROM stdin;
6	1	\N	202609270102000001	2026-09-27 12:59:38.215765	customer1	678840.00	679039.00	199.00	0.00	0.00	0.00	2	0
7	1	\N	202609270102000002	2026-09-27 13:14:12.125836	customer1	731220.00	731419.00	199.00	0.00	0.00	0.00	2	0
8	1	\N	202609270102000003	2026-09-27 13:24:14.169148	customer1	330000.00	330199.00	199.00	0.00	0.00	0.00	2	0
\.


--
-- Data for Name: oms_order_item; Type: TABLE DATA; Schema: orders; Owner: postgres
--

COPY orders.oms_order_item (id, order_id, order_sn, product_id, product_pic, product_name, product_brand, product_sn, product_price, product_quantity, product_sku_id, product_sku_code, product_category_id, promotion_name, promotion_amount, coupon_amount, points_discount_amount, real_amount, earned_points, product_attr) FROM stdin;
6	6	202609270102000001	1	http://localhost:9000/nexus-media/public/pms/skus/APPLE-IP17-BLK-256/main.svg	iPhone 17 (Black, 256GB)	\N	\N	56570.00	12	\N	\N	No promotion	0.00
7	7	202609270102000002	1	http://localhost:9000/nexus-media/public/pms/skus/APPLE-IP17-BLK-128/main.svg	iPhone 17 (Black, 128GB)	\N	\N	52380.00	1	1	APPLE-IP17-BLK-128	No promotion	0.00
8	7	202609270102000002	1	http://localhost:9000/nexus-media/public/pms/skus/APPLE-IP17-BLK-256/main.svg	iPhone 17 (Black, 256GB)	\N	\N	56570.00	12	2	APPLE-IP17-BLK-256	No promotion	0.00
9	8	202609270102000003	1	http://localhost:9000/nexus-media/public/pms/skus/APPLE-IP17-BLK-256/main.svg	iPhone 17 (Black, 256GB)	\N	\N	56570.00	1	2	APPLE-IP17-BLK-256	No promotion	0.00
10	8	202609270102000003	1	http://localhost:9000/nexus-media/public/pms/skus/APPLE-IP17-SLV-128/main.svg	iPhone 17 (Silver, 128GB)	\N	\N	53430.00	1	3	APPLE-IP17-SLV-128	No promotion	0.00
11	8	202609270102000003	1	http://localhost:9000/nexus-media/public/pms/skus/APPLE-IP17-SLV-256/main.svg	iPhone 17 (Silver, 256GB)	\N	\N	57620.00	2	4	APPLE-IP17-SLV-256	No promotion	0.00
12	8	202609270102000003	1	http://localhost:9000/nexus-media/public/pms/skus/APPLE-IP17-BLK-128/main.svg	iPhone 17 (Black, 128GB)	\N	\N	52380.00	2	1	APPLE-IP17-BLK-128	No promotion	0.00
\.


--
-- Data for Name: oms_order_operate_history; Type: TABLE DATA; Schema: orders; Owner: postgres
--

COPY orders.oms_order_operate_history (id, order_id, operate_man, create_time, order_status, note) FROM stdin;
\.


--
-- Data for Name: oms_order_return_apply; Type: TABLE DATA; Schema: orders; Owner: postgres
--

COPY orders.oms_order_return_apply (id, order_id, company_address_id, product_id, order_sn, create_time, member_username, return_amount, return_name, return_phone, status, handle_time, product_pic, product_name, product_brand, product_attr, product_count, product_price, product_real_price, reason, description, proof_pics, handle_note, handle_man, receive_man, receive_time, receive_note) FROM stdin;
2	8	1	1	202609270102000003	2026-09-27 13:25:33.65216	customer1	28810.00	Customer One		2	2026-09-27 13:27:01.141076	iPhone 17 (Silver, 256GB)	1
\.


--
-- Data for Name: oms_order_return_reason; Type: TABLE DATA; Schema: orders; Owner: postgres
--

COPY orders.oms_order_return_reason (id, name, sort, status, create_time) FROM stdin;
1	Defective product	1	1	2026-09-25 13:35:28.423835
2	Wrong item sent	2	1	2026-09-25 13:35:28.423835
3	Changed my mind	3	1	2026-09-25 13:35:28.423835
4	Quality not as expected	4	1	2026-09-25 13:35:28.423835
\.


--
-- Data for Name: oms_order_setting; Type: TABLE DATA; Schema: orders; Owner: postgres
--

COPY orders.oms_order_setting (id, flash_order_overtime, normal_order_overtime, confirm_overtime, finish_overtime, comment_overtime) FROM stdin;
1	60	120	15	7	7

\.


--
-- Data for Name: oms_payment_transaction; Type: TABLE DATA; Schema: orders; Owner: postgres
--

COPY orders.oms_payment_transaction (id, order_id, transaction_id, transaction_type, amount, status, webhook_payload, create_time) FROM stdin;
\.


--
-- Data for Name: ai_chat_conversation; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.ai_chat_conversation (id, member_id, session_id, role, content, model, tokens_used, create_time) FROM stdin;
1	\N	session_1grztm8x3dn	user	hi	\N	\N	2026-10-02 13:46:37.545096
\.


--
-- Data for Name: flyway_schema_history; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.flyway_schema_history (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) FROM stdin;
1	1	baseline	SQL	V1__baseline.sql	146656653	postgres	2026-09-27 01:38:11.920816	110	t

2	2	seed full pms	SQL	V2__seed_full_catalog.sql	-734177831	postgres	2026-09-27 01:38:12.089311	108	t

3	3	membership and admin	SQL	V3__membership_and_admin.sql	17031760	postgres	2026-09-27 01:38:12.283447	7	t

4	3.1	product desc text	SQL	V3_1__product_desc_text.sql	1882706475	postgres	2026-09-27 01:38:12.299541	3	t
5	4	english menus	SQL	V4__english_menus.sql	1654041005	postgres	2026-09-27 01:38:12.309145	7	t

6	5	english resources	SQL	V5__english_resources.sql	1611983688	postgres	2026-09-27 01:38:12.32246	9	t

7	6	coupon enhancements	SQL	V6__coupon_enhancements.sql	-1620614981	postgres	2026-09-27 01:38:12.337257	9	t

8	7	flash sale tables	SQL	V7__flash_sale_tables.sql	1184604928	postgres	2026-09-27 01:38:12.352003	11	t

9	8	ai chat history	SQL	V8__ai_chat_history.sql	-929212253	postgres	2026-09-27 01:38:12.368953	5	t

10	9	coupon improvements	SQL	V9__coupon_improvements.sql	-1303713297	postgres	2026-09-27 01:38:12.378824	3	t

\.


--
-- Data for Name: oms_cart_item; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.oms_cart_item (id, product_id, product_sku_id, member_id, quantity, price, product_pic, product_name, product_sub_title, product_sku_code, member_nickname, create_date, modify_date, delete_status, product_category_id, product_brand, product_sn, product_attr) FROM stdin;
\.


--
-- Data for Name: oms_company_address; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.oms_company_address (id, address_name, send_status, receive_status, name, phone, province, city, region, detail_address) FROM stdin;
\.


--
-- Data for Name: oms_order; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.oms_order (id, member_id, coupon_id, order_sn, create_time, member_username, total_amount, pay_amount, freight_amount, promotion_amount, points_discount_amount, coupon_amount, discount_amount, pay_type, source_type, status, order_type, delivery_company, delivery_sn, auto_confirm_day, earned_points, promotion_info, bill_type, bill_header, bill_content, bill_receiver_phone, bill_receiver_email, receiver_name, receiver_phone, receiver_post_code, receiver_province, receiver_city, receiver_region, receiver_detail_address, note, confirm_status, delete_status, used_points, payment_id, payment_time, delivery_time, receive_time, comment_time, modify_time, vendor_id) FROM stdin;
\.


--
-- Data for Name: oms_order_item; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.oms_order_item (id, order_id, order_sn, product_id, product_pic, product_name, product_brand, product_sn, product_price, product_quantity, product_sku_id, product_sku_code, product_category_id, promotion_name, promotion_amount, coupon_amount, points_discount_amount, real_amount, earned_points, product_attr) FROM stdin;
\.


--
-- Data for Name: oms_order_operate_history; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.oms_order_operate_history (id, order_id, operate_man, create_time, order_status, note) FROM stdin;
\.


--
-- Data for Name: oms_order_return_apply; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.oms_order_return_apply (id, order_id, company_address_id, product_id, order_sn, create_time, member_username, return_amount, return_name, return_phone, status, handle_time, product_pic, product_name, product_brand, product_attr, product_count, product_price, product_real_price, reason, description, proof_pics, handle_note, handle_man, receive_man, receive_time, receive_note) FROM stdin;
\.


--
-- Data for Name: oms_order_return_reason; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.oms_order_return_reason (id, name, sort, status, create_time) FROM stdin;
1	Defective product	1	1	2026-09-27 03:29:44.075979
2	Wrong item received	2	1	2026-09-27 03:29:44.075979
3	Changed my mind	3	1	2026-09-27 03:29:44.075979
\.


--
-- Data for Name: oms_order_setting; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.oms_order_setting (id, flash_order_overtime, normal_order_overtime, confirm_overtime, finish_overtime, comment_overtime) FROM stdin;
\.


--
-- Data for Name: oms_payment_transaction; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.oms_payment_transaction (id, order_id, transaction_id, transaction_type, amount, status, webhook_payload, create_time) FROM stdin;
\.


--
-- Data for Name: outbox_event; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.outbox_event (id, aggregate_type, aggregate_id, type, payload, status, retry_count, created_at) FROM stdin;
\.


--
-- Data for Name: pms_brand; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.pms_brand (id, name, show_status, logo) FROM stdin;
1	Apple	1	\N
2	Samsung	1	\N
3	Google	1	\N
4	Sony	1	\N
5	OnePlus	1	\N
6	Nothing	1	\N
7	Dell	1	\N
8	Lenovo	1	\N
9	ASUS	1	\N
10	Logitech	1	\N
11	boAt	1	\N
12	Noise	1	\N
13	Portronics	1	\N
14	Zebronics	1	\N
15	Lava	1	\N
16	Micromax	1	\N
17	Nike	1	\N
18	Adidas	1	\N
19	Puma	1	\N
20	Levi's	1	\N
21	Uniqlo	1	\N
22	Zara	1	\N
23	H&M	1	\N
24	Ray-Ban	1	\N
25	The North Face	1	\N
26	FabIndia	1	\N
27	Manyavar	1	\N
28	W for Woman	1	\N
29	Allen Solly	1	\N
30	Peter England	1	\N
31	Raymond	1	\N
32	Van Heusen	1	\N
33	IKEA	1	\N
34	Philips	1	\N
35	Dyson	1	\N
36	LG	1	\N
37	Bosch	1	\N
38	Prestige	1	\N
39	Hawkins	1	\N
40	KENT	1	\N
41	Bajaj	1	\N
42	Havells	1	\N
43	Usha	1	\N
44	Butterfly	1	\N
45	Crompton	1	\N
46	Wonderchef	1	\N
47	L'Oréal	1	\N
48	Maybelline	1	\N
49	Nivea	1	\N
50	Clinique	1	\N
51	The Ordinary	1	\N
52	Dove	1	\N
53	Neutrogena	1	\N
54	Lakmé	1	\N
55	Mamaearth	1	\N
56	Minimalist	1	\N
57	Forest Essentials	1	\N
58	Plum	1	\N
59	Nykaa	1	\N
60	Biotique	1	\N
61	Under Armour	1	\N
62	Decathlon	1	\N
63	Wilson	1	\N
64	Yonex	1	\N
65	Nivia	1	\N
66	Cosco	1	\N
67	SS	1	\N
68	SG	1	\N
69	Vector X	1	\N
70	Penguin Random House	1	\N
71	O'Reilly	1	\N
72	McGraw Hill	1	\N
73	Pearson	1	\N
74	Moleskine	1	\N
75	Rupa Publications	1	\N
76	S. Chand	1	\N
77	Arihant	1	\N
78	Amar Chitra Katha	1	\N
79	Crossword	1	\N
80	Nestlé	1	\N
81	Coca-Cola	1	\N
82	Kellogg's	1	\N
83	Unilever	1	\N
84	Britannia	1	\N
85	Tata	1	\N
86	Amul	1	\N
87	Parle	1	\N
88	ITC	1	\N
89	Dabur	1	\N
90	Patanjali	1	\N
91	Mother Dairy	1	\N
92	Aashirvaad	1	\N
93	Fortune	1	\N
94	MTR	1	\N
95	Michelin	1	\N
96	Castrol	1	\N
97	3M	1	\N
98	Motul	1	\N
99	CEAT	1	\N
100	MRF	1	\N
101	Apollo Tyres	1	\N
102	TVS	1	\N
103	Bosch India	1	\N
104	Gulf Oi	1	\N
\.


--
-- Data for Name: pms_comment; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: pms_comment_reply; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: pms_product; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.pms_product (id, brand_id, product_category_id, product_attribute_category_id, name, pic, product_sn, delete_status, publish_status, new_status, recommend_status, verify_status, sort, sale, sub_title, unit, weight, service_ids, keywords, note, promotion_start_time, promotion_end_time, promotion_per_limit, promotion_type, brand_name, product_category_name, description, detail_html, detail_title, detail_desc, vendor_id) FROM stdin;
1	1	2	1	iPhone 17	http://localhost:9000/nexus-media/public/pms/products/1-iphone-17/main.webp	APP-IPH-1	0	1	1	1	1	0	500.00
2	2	2	1	Galaxy S25	http://localhost:9000/nexus-media/public/pms/products/2-galaxy-s25/main.webp	SAM-GAL-2	0	1	1	1	1	0	500.00
3	1	3	2	MacBook Pro M5	http://localhost:9000/nexus-media/public/pms/products/3-macbook-pro-m5/main.webp	APP-MAC-3	0	1	1	1	1	0	500.00
4	17	13	4	Air Max 2026	http://localhost:9000/nexus-media/public/pms/products/4-air-max-2026/main.webp	NIK-AIR-4	0	1	1	1	1	0	500.00
5	18	13	4	Ultraboost 26	http://localhost:9000/nexus-media/public/pms/products/5-ultraboost-26/main.webp	ADI-ULT-5	0	1	1	1	1	0	500.00
6	73	33	7	Clean Code	http://localhost:9000/nexus-media/public/pms/products/6-clean-code/main.webp	PEA-CLE-6	0	1	1	1	1	0	500.00
7	81	36	8	Coca-Cola Zero	http://localhost:9000/nexus-media/public/pms/products/7-coca-cola-zero/main.webp	COC-COC-7	0	1	1	1	1	0	500.00
8	21	11	5	Supima Cotton T-Shirt	http://localhost:9000/nexus-media/public/pms/products/8-supima-cotton-t-shirt/main.webp	UNI-SUP-8	0	1	1	1	1	0	500.00
9	4	5	3	WH-1000XM6	http://localhost:9000/nexus-media/public/pms/products/9-wh-1000xm6/main.webp	SON-WH--9	0	1	1	1	1	0	500.00
10	35	18	10	V15 Detect Absolute	http://localhost:9000/nexus-media/public/pms/products/10-v15-detect-absolute/main.webp	DYS-V15-10	0	1	1	1	1	0	500.00
\.


--
-- Data for Name: pms_product_attribute; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.pms_product_attribute (id, product_attribute_category_id, name, select_type, input_type, input_list, sort, type) FROM stdin;
1	1	Color	1	1	Value1,Value2	0	1	1	1	0	0

2	1	Storage	1	1	Value1,Value2	0	1	1	1	0	0

3	2	RAM	1	1	Value1,Value2	0	1	1	1	0	0

4	2	Storage	1	1	Value1,Value2	0	1	1	1	0	0

5	2	Color	1	1	Value1,Value2	0	1	1	1	0	0

6	3	Color	1	1	Value1,Value2	0	1	1	1	0	0

7	4	Size (US)	1	1	Value1,Value2	0	1	1	1	0	0

8	4	Color	1	1	Value1,Value2	0	1	1	1	0	0

9	5	Size	1	1	Value1,Value2	0	1	1	1	0	0

10	5	Color	1	1	Value1,Value2	0	1	1	1	0	0

11	6	Shade	1	1	Value1,Value2	0	1	1	1	0	0

12	7	Format	1	1	Value1,Value2	0	1	1	1	0	0

13	8	Pack Size	1	1	Value1,Value2	0	1	1	1	0	0

14	9	Size (inches)	1	1	Value1,Value2	0	1	1	1	0	0

15	9	Resolution	1	1	Value1,Value2	0	1	1	1	0	0

16	10	Color	1	1	Value1,Value2	0	1	1	1	0	0

\.


--
-- Data for Name: pms_product_attribute_category; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.pms_product_attribute_category (id, name, attribute_count, param_count) FROM stdin;
1	Smartphone Specs	2	0
2	Laptop Specs	2	0
3	Audio Specs	2	0
4	Shoe Sizes	2	0
5	Apparel Sizes	2	0
6	Beauty Shades	2	0
7	Book Editions	2	0
8	Grocery Pack	2	0
9	Display Specs	2	0
10	General Variants	2	0
\.


--
-- Data for Name: pms_product_attribute_value; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.pms_product_attribute_value (id, product_id, product_attribute_id, value) FROM stdin;
1	1	1	Black

2	1	2	128GB

3	1	1	Black

4	1	2	256GB

5	1	1	Silver

6	1	2	128GB

7	1	1	Silver

8	1	2	256GB

9	2	1	Phantom Black

10	2	2	256GB

11	2	1	Phantom Black

12	2	2	512GB

13	2	1	Cream

14	2	2	256GB

15	2	1	Cream

16	2	2	512GB

17	3	3	16GB

18	3	4	512GB

19	3	5	Space Gray

20	3	3	16GB

21	3	4	1TB

22	3	5	Space Gray

23	3	3	32GB

24	3	4	512GB

25	3	5	Space Gray

26	3	3	32GB

27	3	4	1TB

28	3	5	Space Gray

29	4	7	8

30	4	8	Black

31	4	7	8

32	4	8	White

33	4	7	9

34	4	8	Black

35	4	7	9

36	4	8	White

37	4	7	10

38	4	8	Black

39	4	7	10

40	4	8	White

41	5	7	7

42	5	8	Core Black

43	5	7	8

44	5	8	Core Black

45	5	7	9

46	5	8	Core Black

47	6	12	Paperback

48	6	12	Hardcover

49	6	12	Kindle

50	7	13	300ml

51	7	13	750ml

52	7	13	1.25L

53	7	13	2.25L

54	8	9	S

55	8	10	White

56	8	9	S

57	8	10	Black

58	8	9	M

59	8	10	White

60	8	9	M

61	8	10	Black

62	8	9	L

63	8	10	White

64	8	9	L

65	8	10	Black

66	8	9	XL

67	8	10	White

68	8	9	XL

69	8	10	Black

70	9	6	Black

71	9	6	Silver

72	9	6	Blue

73	10	16	Yellow/Nickel

\.


--
-- Data for Name: pms_product_category; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.pms_product_category (id, parent_id, name, level, product_count, product_unit, nav_status, show_status, sort, icon, keywords, description) FROM stdin;
1	0	Electronics	0	0	piece	1	1	0	\N	electronics	Electronics items

2	1	Smartphones	1	0	piece	1	1	0	\N	smartphones	Smartphones items

3	1	Laptops	1	0	piece	1	1	0	\N	laptops	Laptops items

4	1	Tablets	1	0	piece	1	1	0	\N	tablets	Tablets items

5	1	Headphones & Earbuds	1	0	piece	1	1	0	\N	headphones & earbuds	Headphones & Earbuds items

6	1	Smartwatches	1	0	piece	1	1	0	\N	smartwatches	Smartwatches items

7	1	Cameras	1	0	piece	1	1	0	\N	cameras	Cameras items

8	1	Monitors	1	0	piece	1	1	0	\N	monitors	Monitors items

9	1	Keyboards & Mice	1	0	piece	1	1	0	\N	keyboards & mice	Keyboards & Mice items

10	0	Fashion	0	0	piece	1	1	0	\N	fashion	Fashion items

11	10	Men's Clothing	1	0	piece	1	1	0	\N	men's clothing	Men's Clothing items

12	10	Women's Clothing	1	0	piece	1	1	0	\N	women's clothing	Women's Clothing items

13	10	Footwear	1	0	piece	1	1	0	\N	footwear	Footwear items

14	10	Accessories	1	0	piece	1	1	0	\N	accessories	Accessories items

15	0	Home & Kitchen	0	0	piece	1	1	0	\N	home & kitchen	Home & Kitchen items

16	15	Furniture	1	0	piece	1	1	0	\N	furniture	Furniture items

17	15	Kitchen Appliances	1	0	piece	1	1	0	\N	kitchen appliances	Kitchen Appliances items

18	15	Home Appliances	1	0	piece	1	1	0	\N	home appliances	Home Appliances items

19	15	Kitchenware	1	0	piece	1	1	0	\N	kitchenware	Kitchenware items

20	0	Beauty & Personal Care	0	0	piece	1	1	0	\N	beauty & personal care	Beauty & Personal Care items

21	20	Skincare	1	0	piece	1	1	0	\N	skincare	Skincare items

22	20	Hair Care	1	0	piece	1	1	0	\N	hair care	Hair Care items

23	20	Makeup	1	0	piece	1	1	0	\N	makeup	Makeup items

24	20	Grooming	1	0	piece	1	1	0	\N	grooming	Grooming items

25	0	Sports & Fitness	0	0	piece	1	1	0	\N	sports & fitness	Sports & Fitness items

26	25	Running	1	0	piece	1	1	0	\N	running	Running items

27	25	Gym Equipment	1	0	piece	1	1	0	\N	gym equipment	Gym Equipment items

28	25	Yoga	1	0	piece	1	1	0	\N	yoga	Yoga items

29	25	Outdoor	1	0	piece	1	1	0	\N	outdoor	Outdoor items

30	0	Books & Stationery	0	0	piece	1	1	0	\N	books & stationery	Books & Stationery items

31	30	Fiction	1	0	piece	1	1	0	\N	fiction	Fiction items

32	30	Non-Fiction	1	0	piece	1	1	0	\N	non-fiction	Non-Fiction items

33	30	Technology	1	0	piece	1	1	0	\N	technology	Technology items

34	30	Notebooks & Supplies	1	0	piece	1	1	0	\N	notebooks & supplies	Notebooks & Supplies items

35	0	Grocery	0	0	piece	1	1	0	\N	grocery	Grocery items

36	35	Beverages	1	0	piece	1	1	0	\N	beverages	Beverages items

37	35	Snacks	1	0	piece	1	1	0	\N	snacks	Snacks items

38	35	Breakfast	1	0	piece	1	1	0	\N	breakfast	Breakfast items

39	35	Pantry	1	0	piece	1	1	0	\N	pantry	Pantry items

40	0	Automotive	0	0	piece	1	1	0	\N	automotive	Automotive items

41	40	Car Accessories	1	0	piece	1	1	0	\N	car accessories	Car Accessories items

42	40	Car Care	1	0	piece	1	1	0	\N	car care	Car Care items

43	40	Replacement Parts	1	0	piece	1	1	0	\N	replacement parts	Replacement Parts items

\.


--
-- Data for Name: pms_product_category_attribute_relation; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.pms_product_category_attribute_relation (id, product_category_id, product_attribute_id) FROM stdin;
\.


--
-- Data for Name: pms_product_embedding; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.pms_product_embedding (product_id, embedding) FROM stdin;
\.


--
-- Data for Name: pms_product_spend_discount; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.pms_product_spend_discount (id, product_id, full_price, reduce_price) FROM stdin;
\.


--
-- Data for Name: pms_product_volume_discount; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.pms_product_volume_discount (id, product_id, count, discount, price) FROM stdin;
\.


--
-- Data for Name: pms_product_gallery; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.pms_product_gallery (id, product_id, media_type, media_url, sort_order) FROM stdin;
1	1	image	http://localhost:9000/nexus-media/public/pms/products/1-iphone-17/main.webp	0

2	2	image	http://localhost:9000/nexus-media/public/pms/products/2-galaxy-s25/main.webp	0

3	3	image	http://localhost:9000/nexus-media/public/pms/products/3-macbook-pro-m5/main.webp	0

4	4	image	http://localhost:9000/nexus-media/public/pms/products/4-air-max-2026/main.webp	0

5	5	image	http://localhost:9000/nexus-media/public/pms/products/5-ultraboost-26/main.webp	0

6	6	image	http://localhost:9000/nexus-media/public/pms/products/6-clean-code/main.webp	0

7	7	image	http://localhost:9000/nexus-media/public/pms/products/7-coca-cola-zero/main.webp	0

8	8	image	http://localhost:9000/nexus-media/public/pms/products/8-supima-cotton-t-shirt/main.webp	0

9	9	image	http://localhost:9000/nexus-media/public/pms/products/9-wh-1000xm6/main.webp	0

10	10	image	http://localhost:9000/nexus-media/public/pms/products/10-v15-detect-absolute/main.webp	0

\.


--
-- Data for Name: pms_product_audit_log; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.pms_product_audit_log (id, product_id, price_old, price_new, sale_price_old, sale_price_new, operate_man, create_time) FROM stdin;
\.


--
-- Data for Name: pms_product_approval_history; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.pms_product_approval_history (id, product_id, create_time, reviewer_name, status, detail) FROM stdin;
\.


--
-- Data for Name: pms_review; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.pms_review (id, product_id, member_id, parent_id, rating, content, like_count, dislike_count, status, created_time) FROM stdin;
\.


--
-- Data for Name: pms_review_attachments; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.pms_review_attachments (id, review_id, media_type, media_url, thumbnail_url, sort_order) FROM stdin;
\.


--
-- Data for Name: pms_sku_stock; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.pms_sku_stock (id, product_id, sku_code, price, stock, low_stock, pic, sale, promotion_price, lock_stock, sku_attributes) FROM stdin;
1	1	APP-IPH-1-BLA-128	81851.00	12	5	http://localhost:9000/nexus-media/public/pms/skus/APP-IPH-1-BLA-128/main.webp	0	\N	\N	[{"key": "Color", "value": "Black"}, {"key": "Storage", "value": "128GB"}]

2	1	APP-IPH-1-BLA-256	81114.00	51	5	http://localhost:9000/nexus-media/public/pms/skus/APP-IPH-1-BLA-256/main.webp	0	\N	\N	[{"key": "Color", "value": "Black"}, {"key": "Storage", "value": "256GB"}]

3	1	APP-IPH-1-SIL-128	80023.00	10	5	http://localhost:9000/nexus-media/public/pms/skus/APP-IPH-1-SIL-128/main.webp	0	\N	\N	[{"key": "Color", "value": "Silver"}, {"key": "Storage", "value": "128GB"}]

4	1	APP-IPH-1-SIL-256	82600.00	26	5	http://localhost:9000/nexus-media/public/pms/skus/APP-IPH-1-SIL-256/main.webp	0	\N	\N	[{"key": "Color", "value": "Silver"}, {"key": "Storage", "value": "256GB"}]

5	2	SAM-GAL-2-PHA-256	75967.00	22	5	http://localhost:9000/nexus-media/public/pms/skus/SAM-GAL-2-PHA-256/main.webp	0	\N	\N	[{"key": "Color", "value": "Phantom Black"}, {"key": "Storage", "value": "256GB"}]

6	2	SAM-GAL-2-PHA-512	76304.00	82	5	http://localhost:9000/nexus-media/public/pms/skus/SAM-GAL-2-PHA-512/main.webp	0	\N	\N	[{"key": "Color", "value": "Phantom Black"}, {"key": "Storage", "value": "512GB"}]

7	2	SAM-GAL-2-CRE-256	79629.00	17	5	http://localhost:9000/nexus-media/public/pms/skus/SAM-GAL-2-CRE-256/main.webp	0	\N	\N	[{"key": "Color", "value": "Cream"}, {"key": "Storage", "value": "256GB"}]

8	2	SAM-GAL-2-CRE-512	76532.00	39	5	http://localhost:9000/nexus-media/public/pms/skus/SAM-GAL-2-CRE-512/main.webp	0	\N	\N	[{"key": "Color", "value": "Cream"}, {"key": "Storage", "value": "512GB"}]

9	3	APP-MAC-3-16G-512-SPA	154564.00	38	5	http://localhost:9000/nexus-media/public/pms/skus/APP-MAC-3-16G-512-SPA/main.webp	0	\N	\N	[{"key": "RAM", "value": "16GB"}, {"key": "Storage", "value": "512GB"}, {"key": "Color", "value": "Space Gray"}]

10	3	APP-MAC-3-16G-1TB-SPA	152879.00	42	5	http://localhost:9000/nexus-media/public/pms/skus/APP-MAC-3-16G-1TB-SPA/main.webp	0	\N	\N	[{"key": "RAM", "value": "16GB"}, {"key": "Storage", "value": "1TB"}, {"key": "Color", "value": "Space Gray"}]

11	3	APP-MAC-3-32G-512-SPA	150761.00	63	5	http://localhost:9000/nexus-media/public/pms/skus/APP-MAC-3-32G-512-SPA/main.webp	0	\N	\N	[{"key": "RAM", "value": "32GB"}, {"key": "Storage", "value": "512GB"}, {"key": "Color", "value": "Space Gray"}]

12	3	APP-MAC-3-32G-1TB-SPA	154147.00	31	5	http://localhost:9000/nexus-media/public/pms/skus/APP-MAC-3-32G-1TB-SPA/main.webp	0	\N	\N	[{"key": "RAM", "value": "32GB"}, {"key": "Storage", "value": "1TB"}, {"key": "Color", "value": "Space Gray"}]

13	4	NIK-AIR-4-8-BLA	12279.00	73	5	http://localhost:9000/nexus-media/public/pms/skus/NIK-AIR-4-8-BLA/main.webp	0	\N	\N	[{"key": "Size (US)", "value": "8"}, {"key": "Color", "value": "Black"}]

14	4	NIK-AIR-4-8-WHI	16734.00	23	5	http://localhost:9000/nexus-media/public/pms/skus/NIK-AIR-4-8-WHI/main.webp	0	\N	\N	[{"key": "Size (US)", "value": "8"}, {"key": "Color", "value": "White"}]

15	4	NIK-AIR-4-9-BLA	12283.00	82	5	http://localhost:9000/nexus-media/public/pms/skus/NIK-AIR-4-9-BLA/main.webp	0	\N	\N	[{"key": "Size (US)", "value": "9"}, {"key": "Color", "value": "Black"}]

16	4	NIK-AIR-4-9-WHI	13567.00	93	5	http://localhost:9000/nexus-media/public/pms/skus/NIK-AIR-4-9-WHI/main.webp	0	\N	\N	[{"key": "Size (US)", "value": "9"}, {"key": "Color", "value": "White"}]

17	4	NIK-AIR-4-10-BLA	12140.00	93	5	http://localhost:9000/nexus-media/public/pms/skus/NIK-AIR-4-10-BLA/main.webp	0	\N	\N	[{"key": "Size (US)", "value": "10"}, {"key": "Color", "value": "Black"}]

18	4	NIK-AIR-4-10-WHI	15736.00	73	5	http://localhost:9000/nexus-media/public/pms/skus/NIK-AIR-4-10-WHI/main.webp	0	\N	\N	[{"key": "Size (US)", "value": "10"}, {"key": "Color", "value": "White"}]

19	5	ADI-ULT-5-7-COR	17246.00	92	5	http://localhost:9000/nexus-media/public/pms/skus/ADI-ULT-5-7-COR/main.webp	0	\N	\N	[{"key": "Size (US)", "value": "7"}, {"key": "Color", "value": "Core Black"}]

20	5	ADI-ULT-5-8-COR	15404.00	75	5	http://localhost:9000/nexus-media/public/pms/skus/ADI-ULT-5-8-COR/main.webp	0	\N	\N	[{"key": "Size (US)", "value": "8"}, {"key": "Color", "value": "Core Black"}]

21	5	ADI-ULT-5-9-COR	15237.00	60	5	http://localhost:9000/nexus-media/public/pms/skus/ADI-ULT-5-9-COR/main.webp	0	\N	\N	[{"key": "Size (US)", "value": "9"}, {"key": "Color", "value": "Core Black"}]

22	6	PEA-CLE-6-PAP	3429.00	15	5	http://localhost:9000/nexus-media/public/pms/skus/PEA-CLE-6-PAP/main.webp	0	\N	\N	[{"key": "Format", "value": "Paperback"}]

23	6	PEA-CLE-6-HAR	4763.00	65	5	http://localhost:9000/nexus-media/public/pms/skus/PEA-CLE-6-HAR/main.webp	0	\N	\N	[{"key": "Format", "value": "Hardcover"}]

24	6	PEA-CLE-6-KIN	5471.00	93	5	http://localhost:9000/nexus-media/public/pms/skus/PEA-CLE-6-KIN/main.webp	0	\N	\N	[{"key": "Format", "value": "Kindle"}]

25	7	COC-COC-7-300	3738.00	81	5	http://localhost:9000/nexus-media/public/pms/skus/COC-COC-7-300/main.webp	0	\N	\N	[{"key": "Pack Size", "value": "300ml"}]

26	7	COC-COC-7-750	3147.00	42	5	http://localhost:9000/nexus-media/public/pms/skus/COC-COC-7-750/main.webp	0	\N	\N	[{"key": "Pack Size", "value": "750ml"}]

27	7	COC-COC-7-1.2	2891.00	21	5	http://localhost:9000/nexus-media/public/pms/skus/COC-COC-7-1.2/main.webp	0	\N	\N	[{"key": "Pack Size", "value": "1.25L"}]

28	7	COC-COC-7-2.2	1211.00	10	5	http://localhost:9000/nexus-media/public/pms/skus/COC-COC-7-2.2/main.webp	0	\N	\N	[{"key": "Pack Size", "value": "2.25L"}]

29	8	UNI-SUP-8-S-WHI	2281.00	78	5	http://localhost:9000/nexus-media/public/pms/skus/UNI-SUP-8-S-WHI/main.webp	0	\N	\N	[{"key": "Size", "value": "S"}, {"key": "Color", "value": "White"}]

30	8	UNI-SUP-8-S-BLA	4276.00	78	5	http://localhost:9000/nexus-media/public/pms/skus/UNI-SUP-8-S-BLA/main.webp	0	\N	\N	[{"key": "Size", "value": "S"}, {"key": "Color", "value": "Black"}]

31	8	UNI-SUP-8-M-WHI	4499.00	65	5	http://localhost:9000/nexus-media/public/pms/skus/UNI-SUP-8-M-WHI/main.webp	0	\N	\N	[{"key": "Size", "value": "M"}, {"key": "Color", "value": "White"}]

32	8	UNI-SUP-8-M-BLA	2641.00	20	5	http://localhost:9000/nexus-media/public/pms/skus/UNI-SUP-8-M-BLA/main.webp	0	\N	\N	[{"key": "Size", "value": "M"}, {"key": "Color", "value": "Black"}]

33	8	UNI-SUP-8-L-WHI	5380.00	92	5	http://localhost:9000/nexus-media/public/pms/skus/UNI-SUP-8-L-WHI/main.webp	0	\N	\N	[{"key": "Size", "value": "L"}, {"key": "Color", "value": "White"}]

34	8	UNI-SUP-8-L-BLA	3220.00	23	5	http://localhost:9000/nexus-media/public/pms/skus/UNI-SUP-8-L-BLA/main.webp	0	\N	\N	[{"key": "Size", "value": "L"}, {"key": "Color", "value": "Black"}]

35	8	UNI-SUP-8-XL-WHI	5464.00	44	5	http://localhost:9000/nexus-media/public/pms/skus/UNI-SUP-8-XL-WHI/main.webp	0	\N	\N	[{"key": "Size", "value": "XL"}, {"key": "Color", "value": "White"}]

36	8	UNI-SUP-8-XL-BLA	3594.00	53	5	http://localhost:9000/nexus-media/public/pms/skus/UNI-SUP-8-XL-BLA/main.webp	0	\N	\N	[{"key": "Size", "value": "XL"}, {"key": "Color", "value": "Black"}]

37	9	SON-WH--9-BLA	29341.00	44	5	http://localhost:9000/nexus-media/public/pms/skus/SON-WH--9-BLA/main.webp	0	\N	\N	[{"key": "Color", "value": "Black"}]

38	9	SON-WH--9-SIL	29771.00	78	5	http://localhost:9000/nexus-media/public/pms/skus/SON-WH--9-SIL/main.webp	0	\N	\N	[{"key": "Color", "value": "Silver"}]

39	9	SON-WH--9-BLU	25922.00	47	5	http://localhost:9000/nexus-media/public/pms/skus/SON-WH--9-BLU/main.webp	0	\N	\N	[{"key": "Color", "value": "Blue"}]

40	10	DYS-V15-10-YEL	56206.00	18	5	http://localhost:9000/nexus-media/public/pms/skus/DYS-V15-10-YEL/main.webp	0	\N	\N	[{"key": "Color", "value": "Yellow/Nickel"}]

\.


--
-- Data for Name: sms_coupon; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sms_coupon (id, type, name, count, amount, per_limit, min_order_amount, start_time, end_time, use_type, receive_count, code, max_discount_amount, version, stackable, is_public) FROM stdin;
\.


--
--



--
-- Data for Name: sms_coupon_history; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sms_coupon_history (id, coupon_id, member_id, coupon_code, member_nickname, get_type, create_time, use_status, use_time, order_id, order_sn) FROM stdin;
\.


--
-- Data for Name: sms_coupon_product_category_relation; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sms_coupon_product_category_relation (id, coupon_id, product_category_id, product_category_name, parent_category_name) FROM stdin;
\.


--
-- Data for Name: sms_coupon_product_relation; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sms_coupon_product_relation (id, coupon_id, product_id, product_name, product_sn) FROM stdin;
\.


--
-- Data for Name: sms_flash_promotion; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sms_flash_promotion (id, title, start_date, end_date, status, create_time) FROM stdin;
\.


--
-- Data for Name: sms_flash_promotion_log; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sms_flash_promotion_log (id, member_id, product_id, flash_promotion_id, session_id, order_id, quantity, status, create_time) FROM stdin;
\.


--
-- Data for Name: sms_flash_promotion_product_relation; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sms_flash_promotion_product_relation (id, flash_promotion_id, flash_promotion_session_id, product_id, flash_promotion_price, flash_promotion_count, flash_promotion_limit, sort) FROM stdin;
\.


--
-- Data for Name: sms_flash_promotion_session; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sms_flash_promotion_session (id, name, start_time, end_time, status, create_time) FROM stdin;
\.


--
-- Data for Name: sys_banner; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sys_banner (id, name, type, pic, start_time, end_time, status, click_count, order_count, url, note, sort) FROM stdin;
1	Electronics Summer Sale	1	http://localhost:9000/nexus-media/public/marketing/banners/electronics-sale.webp	2024-01-01 00:00:00	2026-12-31 23:59:59	1	0	0	/category/electronics	Electronics Banner	0
2	Fashion Week	1	http://localhost:9000/nexus-media/public/marketing/banners/fashion-week.webp	2024-01-01 00:00:00	2026-12-31 23:59:59	1	0	0	/category/fashion	Fashion Banner	1
\.


--
-- Data for Name: ums_admin; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.ums_admin (id, username, password, icon, email, nick_name, note, create_time, login_time, status, vendor_id) FROM stdin;
3	samsung_admin	$2a$10$yBHlid11MRH9e6vDsETdJufyIgz/AASiNSUJdsirodt8IUr16xWDO	\N	samsung@nexusengine.com	Samsung Admin	Brand Admin	2026-09-27 01:38:12.288861	\N	1	2
4	nike_admin	$2a$10$yBHlid11MRH9e6vDsETdJufyIgz/AASiNSUJdsirodt8IUr16xWDO	\N	nike@nexusengine.com	Nike Admin	Brand Admin	2026-09-27 01:38:12.288861	\N	1	3
\.


--
-- Data for Name: ums_admin_login_log; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.ums_admin_login_log (id, admin_id, create_time, ip, address, user_agent) FROM stdin;
\.


--
-- Data for Name: ums_admin_role_relation; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.ums_admin_role_relation (id, admin_id, role_id) FROM stdin;
1	3	2

2	4	2

\.


--
-- Data for Name: ums_member; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.ums_member (id, username, password, nickname, phone, email, status, create_time, icon, gender, birthday, personalized_signature, source_type, points, bonus_draws_remaining, lifetime_points) FROM stdin;
\.


--
-- Data for Name: ums_member_login_log; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.ums_member_login_log (id, member_id, create_time, ip, city, login_type, province) FROM stdin;
\.


--
-- Data for Name: ums_member_product_category_relation; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.ums_member_product_category_relation (id, member_id, product_category_id) FROM stdin;
\.


--
-- Data for Name: ums_member_receive_address; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.ums_member_receive_address (id, member_id, name, phone_number, default_status, post_code, province, city, region, detail_address) FROM stdin;
\.


--
-- Data for Name: ums_menu; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.ums_menu (id, parent_id, create_time, title, level, sort, name, icon, hidden) FROM stdin;
\.


--
-- Data for Name: ums_resource; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.ums_resource (id, create_time, name, url, description, category_id) FROM stdin;
\.


--
-- Data for Name: ums_resource_category; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.ums_resource_category (id, create_time, name, sort) FROM stdin;
\.


--
-- Data for Name: ums_role; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.ums_role (id, name, description, admin_count, create_time, status, sort) FROM stdin;
2	Product Manager	Manage products for a specific brand	2	2026-09-27 01:38:12.288861	1	1
3	Order Manager	Manage orders	0	2026-09-27 01:38:12.288861	1	2
\.


--
-- Data for Name: ums_role_menu_relation; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.ums_role_menu_relation (id, role_id, menu_id) FROM stdin;
\.


--
-- Data for Name: ums_role_resource_relation; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.ums_role_resource_relation (id, role_id, resource_id) FROM stdin;
\.


--
-- Data for Name: vector_store; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.vector_store (id, content, metadata, embedding) FROM stdin;
\.


--
-- Data for Name: sys_banner; Type: TABLE DATA; Schema: system; Owner: postgres
--

COPY system.sys_banner (id, name, type, pic, start_time, end_time, status, click_count, order_count, url, note, sort) FROM stdin;
1	Electronics Sale	0	http://localhost:9000/nexus-media/public/marketing/banners/electronics-sale.svg	2024-01-01 00:00:00	2027-01-01 00:00:00	1	0	0		Electronics banner	1
2	Fashion Week	0	http://localhost:9000/nexus-media/public/marketing/banners/fashion-week.svg	2024-01-01 00:00:00	2027-01-01 00:00:00	1	0	0		Fashion banner	2
3	Special Discount	0	http://localhost:9000/nexus-media/public/marketing/banners/special-discount.svg	2024-01-01 00:00:00	2027-01-01 00:00:00	1	0	0		Special discount banner	3
\.


--
-- Data for Name: ums_admin; Type: TABLE DATA; Schema: users; Owner: postgres
--

COPY users.ums_admin (id, username, password, icon, email, nick_name, note, create_time, login_time, status, vendor_id) FROM stdin;
3	samsung_admin	$2a$10$yBHlid11MRH9e6vDsETdJufyIgz/AASiNSUJdsirodt8IUr16xWDO	\N	samsung@nexusengine.com	Samsung Admin	Brand Admin	2026-09-25 12:13:07.161346	\N	1	2
4	nike_admin	$2a$10$yBHlid11MRH9e6vDsETdJufyIgz/AASiNSUJdsirodt8IUr16xWDO	\N	nike@nexusengine.com	Nike Admin	Brand Admin	2026-09-25 12:13:07.161346	\N	1	3
1	admin	$2a$10$UDwbUxLbBpgXBAvRlR1pS.LTz.Cx9bXm93F3bf8qaqxok6HpU5ZmC	/assets/default-avatar.png	admin@nexusengine.com	System Administrator	System Administrator	2018-10-08 13:32:47	2019-04-20 12:43:33	1	\N
\.


--
-- Data for Name: ums_admin_login_log; Type: TABLE DATA; Schema: users; Owner: postgres
--

COPY users.ums_admin_login_log (id, admin_id, create_time, ip, address, user_agent) FROM stdin;
1	1	2026-09-25 12:23:54.171	172.18.0.1	\N	\N

2	1	2026-09-25 12:24:10.301	172.18.0.1	\N	\N

3	1	2026-09-25 13:15:06.248	172.18.0.1	\N	\N

4	1	2026-09-25 13:46:39.749	172.18.0.1	\N	\N

5	1	2026-09-25 22:28:57.906	172.18.0.1	\N	\N

6	3	2026-09-25 22:54:01.732	172.18.0.1	\N	\N

7	1	2026-09-26 11:56:17.255427	172.18.0.3	\N	\N

8	1	2026-09-27 02:34:44.803221	172.18.0.1	\N	\N

9	1	2026-09-27 02:34:48.735188	172.18.0.1	\N	\N

10	1	2026-09-27 02:35:50.083465	172.18.0.1	\N	\N

11	1	2026-09-27 02:36:34.05214	172.18.0.1	\N	\N

12	1	2026-09-27 02:38:27.528223	172.18.0.1	\N	\N

13	1	2026-09-27 02:45:04.720348	172.18.0.1	\N	\N

14	1	2026-09-27 02:45:55.011739	172.18.0.1	\N	\N

15	1	2026-09-27 02:48:17.119091	172.18.0.1	\N	\N

16	1	2026-09-27 02:48:50.525834	172.18.0.1	\N	\N

17	1	2026-09-27 02:50:39.09046	172.18.0.1	\N	\N

18	1	2026-09-27 02:51:38.507918	172.18.0.1	\N	\N

19	1	2026-09-27 02:54:11.92585	172.18.0.1	\N	\N

20	1	2026-09-27 03:01:41.340752	172.18.0.1	\N	\N

21	1	2026-09-27 03:40:33.962386	172.18.0.1	\N	\N

\.


--
-- Data for Name: ums_admin_role_relation; Type: TABLE DATA; Schema: users; Owner: postgres
--

COPY users.ums_admin_role_relation (id, admin_id, role_id) FROM stdin;
1	3	2

2	4	2

3	1	1

\.


--
-- Data for Name: ums_member; Type: TABLE DATA; Schema: users; Owner: postgres
--

COPY users.ums_member (id, username, password, nickname, phone, email, status, create_time, icon, gender, birthday, personalized_signature, source_type, points, bonus_draws_remaining, lifetime_points) FROM stdin;
1	customer1	$2a$10$yBHlid11MRH9e6vDsETdJufyIgz/AASiNSUJdsirodt8IUr16xWDO	Customer One	\N	\N	1	2026-09-25 12:27:48.208213	\N	\N	\N	\N	\N	\N	\N	\N
\.


--
-- Data for Name: ums_member_login_log; Type: TABLE DATA; Schema: users; Owner: postgres
--

COPY users.ums_member_login_log (id, member_id, create_time, ip, city, login_type, province) FROM stdin;
\.


--
-- Data for Name: ums_member_product_category_relation; Type: TABLE DATA; Schema: users; Owner: postgres
--

COPY users.ums_member_product_category_relation (id, member_id, product_category_id) FROM stdin;
\.


--
-- Data for Name: ums_member_receive_address; Type: TABLE DATA; Schema: users; Owner: postgres
--

COPY users.ums_member_receive_address (id, member_id, name, phone_number, default_status, post_code, province, city, region, detail_address) FROM stdin;
1	1	Customer One	9876543210	1	110001	Delhi	New Delhi	Central	123 Fake Street, Connaught Place

\.


--
-- Data for Name: ums_menu; Type: TABLE DATA; Schema: users; Owner: postgres
--

COPY users.ums_menu (id, parent_id, create_time, title, level, sort, name, icon, hidden) FROM stdin;
\.


--
-- Data for Name: ums_resource; Type: TABLE DATA; Schema: users; Owner: postgres
--

COPY users.ums_resource (id, create_time, name, url, description, category_id) FROM stdin;
\.


--
-- Data for Name: ums_resource_category; Type: TABLE DATA; Schema: users; Owner: postgres
--

COPY users.ums_resource_category (id, create_time, name, sort) FROM stdin;
\.


--
-- Data for Name: ums_role; Type: TABLE DATA; Schema: users; Owner: postgres
--

COPY users.ums_role (id, name, description, admin_count, create_time, status, sort) FROM stdin;
2	Product Manager	Manage products for a specific brand	2	2026-09-25 12:13:07.161346	1	1
3	Order Manager	Manage orders	0	2026-09-25 12:13:07.161346	1	2
1	Super Admin	Has all permissions	0	2018-09-30 15:46:11	1	0
\.


--
-- Data for Name: ums_role_menu_relation; Type: TABLE DATA; Schema: users; Owner: postgres
--

COPY users.ums_role_menu_relation (id, role_id, menu_id) FROM stdin;
\.


--
-- Data for Name: ums_role_resource_relation; Type: TABLE DATA; Schema: users; Owner: postgres
--

COPY users.ums_role_resource_relation (id, role_id, resource_id) FROM stdin;
\.


--
-- Name: ai_chat_conversation_id_seq; Type: SEQUENCE SET; Schema: ai; Owner: postgres
--

SELECT pg_catalog.setval('ai.ai_chat_conversation_id_seq', 4, true);


--
-- Name: pms_brand_id_seq; Type: SEQUENCE SET; Schema: pms; Owner: postgres
--

SELECT pg_catalog.setval('pms.pms_brand_id_seq', 105, true);


--
-- Name: pms_comment_id_seq; Type: SEQUENCE SET; Schema: pms; Owner: postgres
--

SELECT pg_catalog.setval('pms.pms_comment_id_seq', 1, false);


--
-- Name: pms_comment_reply_id_seq; Type: SEQUENCE SET; Schema: pms; Owner: postgres
--

SELECT pg_catalog.setval('pms.pms_comment_reply_id_seq', 1, false);


--
-- Name: pms_product_attribute_category_id_seq; Type: SEQUENCE SET; Schema: pms; Owner: postgres
--

SELECT pg_catalog.setval('pms.pms_product_attribute_category_id_seq', 11, true);


--
-- Name: pms_product_attribute_id_seq; Type: SEQUENCE SET; Schema: pms; Owner: postgres
--

SELECT pg_catalog.setval('pms.pms_product_attribute_id_seq', 132, true);


--
-- Name: pms_product_attribute_value_id_seq; Type: SEQUENCE SET; Schema: pms; Owner: postgres
--

SELECT pg_catalog.setval('pms.pms_product_attribute_value_id_seq', 74, true);


--
-- Name: pms_product_category_attribute_relation_id_seq; Type: SEQUENCE SET; Schema: pms; Owner: postgres
--

SELECT pg_catalog.setval('pms.pms_product_category_attribute_relation_id_seq', 129, true);


--
-- Name: pms_product_category_id_seq; Type: SEQUENCE SET; Schema: pms; Owner: postgres
--

SELECT pg_catalog.setval('pms.pms_product_category_id_seq', 44, true);


--
-- Name: pms_product_spend_discount_id_seq; Type: SEQUENCE SET; Schema: pms; Owner: postgres
--

SELECT pg_catalog.setval('pms.pms_product_spend_discount_id_seq', 1, false);


--
-- Name: pms_product_id_seq; Type: SEQUENCE SET; Schema: pms; Owner: postgres
--

SELECT pg_catalog.setval('pms.pms_product_id_seq', 11, true);


--
-- Name: pms_product_volume_discount_id_seq; Type: SEQUENCE SET; Schema: pms; Owner: postgres
--

SELECT pg_catalog.setval('pms.pms_product_volume_discount_id_seq', 1, false);


--
-- Name: pms_product_gallery_id_seq; Type: SEQUENCE SET; Schema: pms; Owner: postgres
--

SELECT pg_catalog.setval('pms.pms_product_gallery_id_seq', 11, true);


--
-- Name: pms_product_audit_log_id_seq; Type: SEQUENCE SET; Schema: pms; Owner: postgres
--

SELECT pg_catalog.setval('pms.pms_product_audit_log_id_seq', 1, false);


--
-- Name: pms_product_approval_history_id_seq; Type: SEQUENCE SET; Schema: pms; Owner: postgres
--

SELECT pg_catalog.setval('pms.pms_product_approval_history_id_seq', 1, false);


--
-- Name: pms_review_id_seq; Type: SEQUENCE SET; Schema: pms; Owner: postgres
--

SELECT pg_catalog.setval('pms.pms_review_id_seq', 1, false);


--
-- Name: pms_review_attachments_id_seq; Type: SEQUENCE SET; Schema: pms; Owner: postgres
--

SELECT pg_catalog.setval('pms.pms_review_attachments_id_seq', 1, false);


--
-- Name: pms_sku_stock_id_seq; Type: SEQUENCE SET; Schema: pms; Owner: postgres
--

SELECT pg_catalog.setval('pms.pms_sku_stock_id_seq', 41, true);


--
-- Name: outbox_event_id_seq; Type: SEQUENCE SET; Schema: integration; Owner: postgres
--

SELECT pg_catalog.setval('integration.outbox_event_id_seq', 8, true);


--
--



--
-- Name: sms_coupon_history_id_seq; Type: SEQUENCE SET; Schema: marketing; Owner: postgres
--

SELECT pg_catalog.setval('marketing.sms_coupon_history_id_seq', 1, true);


--
-- Name: sms_coupon_id_seq; Type: SEQUENCE SET; Schema: marketing; Owner: postgres
--

SELECT pg_catalog.setval('marketing.sms_coupon_id_seq', 1, true);


--
-- Name: sms_coupon_product_category_relation_id_seq; Type: SEQUENCE SET; Schema: marketing; Owner: postgres
--

SELECT pg_catalog.setval('marketing.sms_coupon_product_category_relation_id_seq', 1, false);


--
-- Name: sms_coupon_product_relation_id_seq; Type: SEQUENCE SET; Schema: marketing; Owner: postgres
--

SELECT pg_catalog.setval('marketing.sms_coupon_product_relation_id_seq', 1, false);


--
-- Name: sms_flash_promotion_id_seq; Type: SEQUENCE SET; Schema: marketing; Owner: postgres
--

SELECT pg_catalog.setval('marketing.sms_flash_promotion_id_seq', 1, false);


--
-- Name: sms_flash_promotion_log_id_seq; Type: SEQUENCE SET; Schema: marketing; Owner: postgres
--

SELECT pg_catalog.setval('marketing.sms_flash_promotion_log_id_seq', 1, false);


--
-- Name: sms_flash_promotion_product_relation_id_seq; Type: SEQUENCE SET; Schema: marketing; Owner: postgres
--

SELECT pg_catalog.setval('marketing.sms_flash_promotion_product_relation_id_seq', 1, false);


--
-- Name: sms_flash_promotion_session_id_seq; Type: SEQUENCE SET; Schema: marketing; Owner: postgres
--

SELECT pg_catalog.setval('marketing.sms_flash_promotion_session_id_seq', 1, false);


--
-- Name: oms_cart_item_id_seq; Type: SEQUENCE SET; Schema: orders; Owner: postgres
--

SELECT pg_catalog.setval('orders.oms_cart_item_id_seq', 21, true);


--
-- Name: oms_company_address_id_seq; Type: SEQUENCE SET; Schema: orders; Owner: postgres
--

SELECT pg_catalog.setval('orders.oms_company_address_id_seq', 1, false);


--
-- Name: oms_order_id_seq; Type: SEQUENCE SET; Schema: orders; Owner: postgres
--

SELECT pg_catalog.setval('orders.oms_order_id_seq', 8, true);


--
-- Name: oms_order_item_id_seq; Type: SEQUENCE SET; Schema: orders; Owner: postgres
--

SELECT pg_catalog.setval('orders.oms_order_item_id_seq', 12, true);


--
-- Name: oms_order_operate_history_id_seq; Type: SEQUENCE SET; Schema: orders; Owner: postgres
--

SELECT pg_catalog.setval('orders.oms_order_operate_history_id_seq', 1, false);


--
-- Name: oms_order_return_apply_id_seq; Type: SEQUENCE SET; Schema: orders; Owner: postgres
--

SELECT pg_catalog.setval('orders.oms_order_return_apply_id_seq', 2, true);


--
-- Name: oms_order_return_reason_id_seq; Type: SEQUENCE SET; Schema: orders; Owner: postgres
--

SELECT pg_catalog.setval('orders.oms_order_return_reason_id_seq', 1, false);


--
-- Name: oms_order_setting_id_seq; Type: SEQUENCE SET; Schema: orders; Owner: postgres
--

SELECT pg_catalog.setval('orders.oms_order_setting_id_seq', 1, false);


--
-- Name: oms_payment_transaction_id_seq; Type: SEQUENCE SET; Schema: orders; Owner: postgres
--

SELECT pg_catalog.setval('orders.oms_payment_transaction_id_seq', 1, false);


--
-- Name: ai_chat_conversation_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.ai_chat_conversation_id_seq', 1, true);


--
-- Name: oms_cart_item_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.oms_cart_item_id_seq', 1, false);


--
-- Name: oms_company_address_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.oms_company_address_id_seq', 1, false);


--
-- Name: oms_order_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.oms_order_id_seq', 1, false);


--
-- Name: oms_order_item_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.oms_order_item_id_seq', 1, false);


--
-- Name: oms_order_operate_history_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.oms_order_operate_history_id_seq', 1, false);


--
-- Name: oms_order_return_apply_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.oms_order_return_apply_id_seq', 1, false);


--
-- Name: oms_order_return_reason_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.oms_order_return_reason_id_seq', 3, true);


--
-- Name: oms_order_setting_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.oms_order_setting_id_seq', 1, false);


--
-- Name: oms_payment_transaction_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.oms_payment_transaction_id_seq', 1, false);


--
-- Name: outbox_event_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.outbox_event_id_seq', 1, false);


--
-- Name: pms_brand_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.pms_brand_id_seq', 105, true);


--
-- Name: pms_comment_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.pms_comment_id_seq', 1, false);


--
-- Name: pms_comment_reply_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.pms_comment_reply_id_seq', 1, false);


--
-- Name: pms_product_attribute_category_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.pms_product_attribute_category_id_seq', 11, true);


--
-- Name: pms_product_attribute_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.pms_product_attribute_id_seq', 17, true);


--
-- Name: pms_product_attribute_value_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.pms_product_attribute_value_id_seq', 74, true);


--
-- Name: pms_product_category_attribute_relation_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.pms_product_category_attribute_relation_id_seq', 1, false);


--
-- Name: pms_product_category_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.pms_product_category_id_seq', 44, true);


--
-- Name: pms_product_spend_discount_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.pms_product_spend_discount_id_seq', 1, false);


--
-- Name: pms_product_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.pms_product_id_seq', 11, true);


--
-- Name: pms_product_volume_discount_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.pms_product_volume_discount_id_seq', 1, false);


--
-- Name: pms_product_gallery_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.pms_product_gallery_id_seq', 11, true);


--
-- Name: pms_product_audit_log_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.pms_product_audit_log_id_seq', 1, false);


--
-- Name: pms_product_approval_history_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.pms_product_approval_history_id_seq', 1, false);


--
-- Name: pms_review_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.pms_review_id_seq', 1, false);


--
-- Name: pms_review_attachments_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.pms_review_attachments_id_seq', 1, false);


--
-- Name: pms_sku_stock_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.pms_sku_stock_id_seq', 41, true);


--
--



--
-- Name: sms_coupon_history_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.sms_coupon_history_id_seq', 1, false);


--
-- Name: sms_coupon_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.sms_coupon_id_seq', 1, false);


--
-- Name: sms_coupon_product_category_relation_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.sms_coupon_product_category_relation_id_seq', 1, false);


--
-- Name: sms_coupon_product_relation_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.sms_coupon_product_relation_id_seq', 1, false);


--
-- Name: sms_flash_promotion_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.sms_flash_promotion_id_seq', 1, false);


--
-- Name: sms_flash_promotion_log_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.sms_flash_promotion_log_id_seq', 1, false);


--
-- Name: sms_flash_promotion_product_relation_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.sms_flash_promotion_product_relation_id_seq', 1, false);


--
-- Name: sms_flash_promotion_session_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.sms_flash_promotion_session_id_seq', 1, false);


--
-- Name: sys_banner_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.sys_banner_id_seq', 3, true);


--
-- Name: ums_admin_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.ums_admin_id_seq', 5, true);


--
-- Name: ums_admin_login_log_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.ums_admin_login_log_id_seq', 1, false);


--
-- Name: ums_admin_role_relation_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.ums_admin_role_relation_id_seq', 2, true);


--
-- Name: ums_member_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.ums_member_id_seq', 1, false);


--
-- Name: ums_member_login_log_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.ums_member_login_log_id_seq', 1, false);


--
-- Name: ums_member_product_category_relation_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.ums_member_product_category_relation_id_seq', 1, false);


--
-- Name: ums_member_receive_address_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.ums_member_receive_address_id_seq', 1, false);


--
-- Name: ums_menu_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.ums_menu_id_seq', 1, false);


--
-- Name: ums_resource_category_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.ums_resource_category_id_seq', 1, false);


--
-- Name: ums_resource_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.ums_resource_id_seq', 1, false);


--
-- Name: ums_role_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.ums_role_id_seq', 4, true);


--
-- Name: ums_role_menu_relation_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.ums_role_menu_relation_id_seq', 1, false);


--
-- Name: ums_role_resource_relation_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.ums_role_resource_relation_id_seq', 1, false);


--
-- Name: sys_banner_id_seq; Type: SEQUENCE SET; Schema: system; Owner: postgres
--

SELECT pg_catalog.setval('system.sys_banner_id_seq', 3, true);


--
-- Name: ums_admin_id_seq; Type: SEQUENCE SET; Schema: users; Owner: postgres
--

SELECT pg_catalog.setval('users.ums_admin_id_seq', 5, true);


--
-- Name: ums_admin_login_log_id_seq; Type: SEQUENCE SET; Schema: users; Owner: postgres
--

SELECT pg_catalog.setval('users.ums_admin_login_log_id_seq', 21, true);


--
-- Name: ums_admin_role_relation_id_seq; Type: SEQUENCE SET; Schema: users; Owner: postgres
--

SELECT pg_catalog.setval('users.ums_admin_role_relation_id_seq', 3, true);


--
-- Name: ums_member_id_seq; Type: SEQUENCE SET; Schema: users; Owner: postgres
--

SELECT pg_catalog.setval('users.ums_member_id_seq', 1, true);


--
-- Name: ums_member_login_log_id_seq; Type: SEQUENCE SET; Schema: users; Owner: postgres
--

SELECT pg_catalog.setval('users.ums_member_login_log_id_seq', 1, false);


--
-- Name: ums_member_product_category_relation_id_seq; Type: SEQUENCE SET; Schema: users; Owner: postgres
--

SELECT pg_catalog.setval('users.ums_member_product_category_relation_id_seq', 1, false);


--
-- Name: ums_member_receive_address_id_seq; Type: SEQUENCE SET; Schema: users; Owner: postgres
--

SELECT pg_catalog.setval('users.ums_member_receive_address_id_seq', 1, true);


--
-- Name: ums_menu_id_seq; Type: SEQUENCE SET; Schema: users; Owner: postgres
--

SELECT pg_catalog.setval('users.ums_menu_id_seq', 1, false);


--
-- Name: ums_resource_category_id_seq; Type: SEQUENCE SET; Schema: users; Owner: postgres
--

SELECT pg_catalog.setval('users.ums_resource_category_id_seq', 1, false);


--
-- Name: ums_resource_id_seq; Type: SEQUENCE SET; Schema: users; Owner: postgres
--

SELECT pg_catalog.setval('users.ums_resource_id_seq', 1, false);


--
-- Name: ums_role_id_seq; Type: SEQUENCE SET; Schema: users; Owner: postgres
--

SELECT pg_catalog.setval('users.ums_role_id_seq', 4, true);


--
-- Name: ums_role_menu_relation_id_seq; Type: SEQUENCE SET; Schema: users; Owner: postgres
--

SELECT pg_catalog.setval('users.ums_role_menu_relation_id_seq', 1, false);


--
-- Name: ums_role_resource_relation_id_seq; Type: SEQUENCE SET; Schema: users; Owner: postgres
--

SELECT pg_catalog.setval('users.ums_role_resource_relation_id_seq', 1, false);


--
-- Name: ai_chat_conversation ai_chat_conversation_pkey; Type: CONSTRAINT; Schema: ai; Owner: postgres
--

ALTER TABLE ONLY ai.ai_chat_conversation
    ADD CONSTRAINT ai_chat_conversation_pkey PRIMARY KEY (id);


--
-- Name: pms_product_embedding pms_product_embedding_pkey; Type: CONSTRAINT; Schema: ai; Owner: postgres
--

ALTER TABLE ONLY ai.pms_product_embedding
    ADD CONSTRAINT pms_product_embedding_pkey PRIMARY KEY (product_id);


--
-- Name: vector_store vector_store_pkey; Type: CONSTRAINT; Schema: ai; Owner: postgres
--

ALTER TABLE ONLY ai.vector_store
    ADD CONSTRAINT vector_store_pkey PRIMARY KEY (id);


--
-- Name: pms_brand pms_brand_pkey; Type: CONSTRAINT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_brand
    ADD CONSTRAINT pms_brand_pkey PRIMARY KEY (id);


--
-- Name: pms_comment pms_comment_pkey; Type: CONSTRAINT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_comment
    ADD CONSTRAINT pms_comment_pkey PRIMARY KEY (id);


--
-- Name: pms_comment_reply pms_comment_reply_pkey; Type: CONSTRAINT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_comment_reply
    ADD CONSTRAINT pms_comment_reply_pkey PRIMARY KEY (id);


--
-- Name: pms_product_attribute_category pms_product_attribute_category_pkey; Type: CONSTRAINT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_product_attribute_category
    ADD CONSTRAINT pms_product_attribute_category_pkey PRIMARY KEY (id);


--
-- Name: pms_product_attribute pms_product_attribute_pkey; Type: CONSTRAINT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_product_attribute
    ADD CONSTRAINT pms_product_attribute_pkey PRIMARY KEY (id);


--
-- Name: pms_product_attribute_value pms_product_attribute_value_pkey; Type: CONSTRAINT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_product_attribute_value
    ADD CONSTRAINT pms_product_attribute_value_pkey PRIMARY KEY (id);


--
-- Name: pms_product_category_attribute_relation pms_product_category_attribute_relation_pkey; Type: CONSTRAINT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_product_category_attribute_relation
    ADD CONSTRAINT pms_product_category_attribute_relation_pkey PRIMARY KEY (id);


--
-- Name: pms_product_category pms_product_category_pkey; Type: CONSTRAINT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_product_category
    ADD CONSTRAINT pms_product_category_pkey PRIMARY KEY (id);


--
-- Name: pms_product_spend_discount pms_product_spend_discount_pkey; Type: CONSTRAINT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_product_spend_discount
    ADD CONSTRAINT pms_product_spend_discount_pkey PRIMARY KEY (id);


--
-- Name: pms_product_volume_discount pms_product_volume_discount_pkey; Type: CONSTRAINT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_product_volume_discount
    ADD CONSTRAINT pms_product_volume_discount_pkey PRIMARY KEY (id);


--
-- Name: pms_product_gallery pms_product_gallery_pkey; Type: CONSTRAINT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_product_gallery
    ADD CONSTRAINT pms_product_gallery_pkey PRIMARY KEY (id);


--
-- Name: pms_product_audit_log pms_product_audit_log_pkey; Type: CONSTRAINT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_product_audit_log
    ADD CONSTRAINT pms_product_audit_log_pkey PRIMARY KEY (id);


--
-- Name: pms_product pms_product_pkey; Type: CONSTRAINT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_product
    ADD CONSTRAINT pms_product_pkey PRIMARY KEY (id);


--
-- Name: pms_product_approval_history pms_product_approval_history_pkey; Type: CONSTRAINT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_product_approval_history
    ADD CONSTRAINT pms_product_approval_history_pkey PRIMARY KEY (id);


--
-- Name: pms_review_attachments pms_review_attachments_pkey; Type: CONSTRAINT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_review_attachments
    ADD CONSTRAINT pms_review_attachments_pkey PRIMARY KEY (id);


--
-- Name: pms_review pms_review_pkey; Type: CONSTRAINT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_review
    ADD CONSTRAINT pms_review_pkey PRIMARY KEY (id);


--
-- Name: pms_sku_stock pms_sku_stock_pkey; Type: CONSTRAINT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_sku_stock
    ADD CONSTRAINT pms_sku_stock_pkey PRIMARY KEY (id);


--
-- Name: outbox_event outbox_event_pkey; Type: CONSTRAINT; Schema: integration; Owner: postgres
--

ALTER TABLE ONLY integration.outbox_event
    ADD CONSTRAINT outbox_event_pkey PRIMARY KEY (id);


--
--



--
-- Name: sms_coupon_history sms_coupon_history_pkey; Type: CONSTRAINT; Schema: marketing; Owner: postgres
--

ALTER TABLE ONLY marketing.sms_coupon_history
    ADD CONSTRAINT sms_coupon_history_pkey PRIMARY KEY (id);


--
-- Name: sms_coupon sms_coupon_pkey; Type: CONSTRAINT; Schema: marketing; Owner: postgres
--

ALTER TABLE ONLY marketing.sms_coupon
    ADD CONSTRAINT sms_coupon_pkey PRIMARY KEY (id);


--
-- Name: sms_coupon_product_category_relation sms_coupon_product_category_relation_pkey; Type: CONSTRAINT; Schema: marketing; Owner: postgres
--

ALTER TABLE ONLY marketing.sms_coupon_product_category_relation
    ADD CONSTRAINT sms_coupon_product_category_relation_pkey PRIMARY KEY (id);


--
-- Name: sms_coupon_product_relation sms_coupon_product_relation_pkey; Type: CONSTRAINT; Schema: marketing; Owner: postgres
--

ALTER TABLE ONLY marketing.sms_coupon_product_relation
    ADD CONSTRAINT sms_coupon_product_relation_pkey PRIMARY KEY (id);


--
-- Name: sms_flash_promotion_log sms_flash_promotion_log_pkey; Type: CONSTRAINT; Schema: marketing; Owner: postgres
--

ALTER TABLE ONLY marketing.sms_flash_promotion_log
    ADD CONSTRAINT sms_flash_promotion_log_pkey PRIMARY KEY (id);


--
-- Name: sms_flash_promotion sms_flash_promotion_pkey; Type: CONSTRAINT; Schema: marketing; Owner: postgres
--

ALTER TABLE ONLY marketing.sms_flash_promotion
    ADD CONSTRAINT sms_flash_promotion_pkey PRIMARY KEY (id);


--
-- Name: sms_flash_promotion_product_relation sms_flash_promotion_product_r_flash_promotion_id_flash_prom_key; Type: CONSTRAINT; Schema: marketing; Owner: postgres
--

ALTER TABLE ONLY marketing.sms_flash_promotion_product_relation
    ADD CONSTRAINT sms_flash_promotion_product_r_flash_promotion_id_flash_prom_key UNIQUE (flash_promotion_id, flash_promotion_session_id, product_id);


--
-- Name: sms_flash_promotion_product_relation sms_flash_promotion_product_relation_pkey; Type: CONSTRAINT; Schema: marketing; Owner: postgres
--

ALTER TABLE ONLY marketing.sms_flash_promotion_product_relation
    ADD CONSTRAINT sms_flash_promotion_product_relation_pkey PRIMARY KEY (id);


--
-- Name: sms_flash_promotion_session sms_flash_promotion_session_pkey; Type: CONSTRAINT; Schema: marketing; Owner: postgres
--

ALTER TABLE ONLY marketing.sms_flash_promotion_session
    ADD CONSTRAINT sms_flash_promotion_session_pkey PRIMARY KEY (id);


--
-- Name: flyway_schema_history flyway_schema_history_pk; Type: CONSTRAINT; Schema: migration; Owner: postgres
--

ALTER TABLE ONLY migration.flyway_schema_history
    ADD CONSTRAINT flyway_schema_history_pk PRIMARY KEY (installed_rank);


--
-- Name: oms_cart_item oms_cart_item_pkey; Type: CONSTRAINT; Schema: orders; Owner: postgres
--

ALTER TABLE ONLY orders.oms_cart_item
    ADD CONSTRAINT oms_cart_item_pkey PRIMARY KEY (id);


--
-- Name: oms_company_address oms_company_address_pkey; Type: CONSTRAINT; Schema: orders; Owner: postgres
--

ALTER TABLE ONLY orders.oms_company_address
    ADD CONSTRAINT oms_company_address_pkey PRIMARY KEY (id);


--
-- Name: oms_order_item oms_order_item_pkey; Type: CONSTRAINT; Schema: orders; Owner: postgres
--

ALTER TABLE ONLY orders.oms_order_item
    ADD CONSTRAINT oms_order_item_pkey PRIMARY KEY (id);


--
-- Name: oms_order_operate_history oms_order_operate_history_pkey; Type: CONSTRAINT; Schema: orders; Owner: postgres
--

ALTER TABLE ONLY orders.oms_order_operate_history
    ADD CONSTRAINT oms_order_operate_history_pkey PRIMARY KEY (id);


--
-- Name: oms_order oms_order_pkey; Type: CONSTRAINT; Schema: orders; Owner: postgres
--

ALTER TABLE ONLY orders.oms_order
    ADD CONSTRAINT oms_order_pkey PRIMARY KEY (id);


--
-- Name: oms_order_return_apply oms_order_return_apply_pkey; Type: CONSTRAINT; Schema: orders; Owner: postgres
--

ALTER TABLE ONLY orders.oms_order_return_apply
    ADD CONSTRAINT oms_order_return_apply_pkey PRIMARY KEY (id);


--
-- Name: oms_order_return_reason oms_order_return_reason_pkey; Type: CONSTRAINT; Schema: orders; Owner: postgres
--

ALTER TABLE ONLY orders.oms_order_return_reason
    ADD CONSTRAINT oms_order_return_reason_pkey PRIMARY KEY (id);


--
-- Name: oms_order_setting oms_order_setting_pkey; Type: CONSTRAINT; Schema: orders; Owner: postgres
--

ALTER TABLE ONLY orders.oms_order_setting
    ADD CONSTRAINT oms_order_setting_pkey PRIMARY KEY (id);


--
-- Name: oms_payment_transaction oms_payment_transaction_pkey; Type: CONSTRAINT; Schema: orders; Owner: postgres
--

ALTER TABLE ONLY orders.oms_payment_transaction
    ADD CONSTRAINT oms_payment_transaction_pkey PRIMARY KEY (id);


--
-- Name: oms_payment_transaction uk_oms_payment_transaction_id; Type: CONSTRAINT; Schema: orders; Owner: postgres
--

ALTER TABLE ONLY orders.oms_payment_transaction
    ADD CONSTRAINT uk_oms_payment_transaction_id UNIQUE (transaction_id);


--
-- Name: ai_chat_conversation ai_chat_conversation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ai_chat_conversation
    ADD CONSTRAINT ai_chat_conversation_pkey PRIMARY KEY (id);


--
-- Name: flyway_schema_history flyway_schema_history_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.flyway_schema_history
    ADD CONSTRAINT flyway_schema_history_pk PRIMARY KEY (installed_rank);


--
-- Name: oms_cart_item oms_cart_item_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.oms_cart_item
    ADD CONSTRAINT oms_cart_item_pkey PRIMARY KEY (id);


--
-- Name: oms_company_address oms_company_address_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.oms_company_address
    ADD CONSTRAINT oms_company_address_pkey PRIMARY KEY (id);


--
-- Name: oms_order_item oms_order_item_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.oms_order_item
    ADD CONSTRAINT oms_order_item_pkey PRIMARY KEY (id);


--
-- Name: oms_order_operate_history oms_order_operate_history_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.oms_order_operate_history
    ADD CONSTRAINT oms_order_operate_history_pkey PRIMARY KEY (id);


--
-- Name: oms_order oms_order_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.oms_order
    ADD CONSTRAINT oms_order_pkey PRIMARY KEY (id);


--
-- Name: oms_order_return_apply oms_order_return_apply_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.oms_order_return_apply
    ADD CONSTRAINT oms_order_return_apply_pkey PRIMARY KEY (id);


--
-- Name: oms_order_return_reason oms_order_return_reason_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.oms_order_return_reason
    ADD CONSTRAINT oms_order_return_reason_pkey PRIMARY KEY (id);


--
-- Name: oms_order_setting oms_order_setting_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.oms_order_setting
    ADD CONSTRAINT oms_order_setting_pkey PRIMARY KEY (id);


--
-- Name: oms_payment_transaction oms_payment_transaction_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.oms_payment_transaction
    ADD CONSTRAINT oms_payment_transaction_pkey PRIMARY KEY (id);


--
-- Name: outbox_event outbox_event_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.outbox_event
    ADD CONSTRAINT outbox_event_pkey PRIMARY KEY (id);


--
-- Name: pms_brand pms_brand_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pms_brand
    ADD CONSTRAINT pms_brand_pkey PRIMARY KEY (id);


--
-- Name: pms_comment pms_comment_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pms_comment
    ADD CONSTRAINT pms_comment_pkey PRIMARY KEY (id);


--
-- Name: pms_comment_reply pms_comment_reply_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pms_comment_reply
    ADD CONSTRAINT pms_comment_reply_pkey PRIMARY KEY (id);


--
-- Name: pms_product_attribute_category pms_product_attribute_category_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pms_product_attribute_category
    ADD CONSTRAINT pms_product_attribute_category_pkey PRIMARY KEY (id);


--
-- Name: pms_product_attribute pms_product_attribute_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pms_product_attribute
    ADD CONSTRAINT pms_product_attribute_pkey PRIMARY KEY (id);


--
-- Name: pms_product_attribute_value pms_product_attribute_value_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pms_product_attribute_value
    ADD CONSTRAINT pms_product_attribute_value_pkey PRIMARY KEY (id);


--
-- Name: pms_product_category_attribute_relation pms_product_category_attribute_relation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pms_product_category_attribute_relation
    ADD CONSTRAINT pms_product_category_attribute_relation_pkey PRIMARY KEY (id);


--
-- Name: pms_product_category pms_product_category_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pms_product_category
    ADD CONSTRAINT pms_product_category_pkey PRIMARY KEY (id);


--
-- Name: pms_product_embedding pms_product_embedding_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pms_product_embedding
    ADD CONSTRAINT pms_product_embedding_pkey PRIMARY KEY (product_id);


--
-- Name: pms_product_spend_discount pms_product_spend_discount_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pms_product_spend_discount
    ADD CONSTRAINT pms_product_spend_discount_pkey PRIMARY KEY (id);


--
-- Name: pms_product_volume_discount pms_product_volume_discount_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pms_product_volume_discount
    ADD CONSTRAINT pms_product_volume_discount_pkey PRIMARY KEY (id);


--
-- Name: pms_product_gallery pms_product_gallery_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pms_product_gallery
    ADD CONSTRAINT pms_product_gallery_pkey PRIMARY KEY (id);


--
-- Name: pms_product_audit_log pms_product_audit_log_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pms_product_audit_log
    ADD CONSTRAINT pms_product_audit_log_pkey PRIMARY KEY (id);


--
-- Name: pms_product pms_product_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pms_product
    ADD CONSTRAINT pms_product_pkey PRIMARY KEY (id);


--
-- Name: pms_product_approval_history pms_product_approval_history_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pms_product_approval_history
    ADD CONSTRAINT pms_product_approval_history_pkey PRIMARY KEY (id);


--
-- Name: pms_review_attachments pms_review_attachments_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pms_review_attachments
    ADD CONSTRAINT pms_review_attachments_pkey PRIMARY KEY (id);


--
-- Name: pms_review pms_review_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pms_review
    ADD CONSTRAINT pms_review_pkey PRIMARY KEY (id);


--
-- Name: pms_sku_stock pms_sku_stock_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pms_sku_stock
    ADD CONSTRAINT pms_sku_stock_pkey PRIMARY KEY (id);


--
--



--
-- Name: sms_coupon_history sms_coupon_history_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sms_coupon_history
    ADD CONSTRAINT sms_coupon_history_pkey PRIMARY KEY (id);


--
-- Name: sms_coupon sms_coupon_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sms_coupon
    ADD CONSTRAINT sms_coupon_pkey PRIMARY KEY (id);


--
-- Name: sms_coupon_product_category_relation sms_coupon_product_category_relation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sms_coupon_product_category_relation
    ADD CONSTRAINT sms_coupon_product_category_relation_pkey PRIMARY KEY (id);


--
-- Name: sms_coupon_product_relation sms_coupon_product_relation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sms_coupon_product_relation
    ADD CONSTRAINT sms_coupon_product_relation_pkey PRIMARY KEY (id);


--
-- Name: sms_flash_promotion_log sms_flash_promotion_log_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sms_flash_promotion_log
    ADD CONSTRAINT sms_flash_promotion_log_pkey PRIMARY KEY (id);


--
-- Name: sms_flash_promotion sms_flash_promotion_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sms_flash_promotion
    ADD CONSTRAINT sms_flash_promotion_pkey PRIMARY KEY (id);


--
-- Name: sms_flash_promotion_product_relation sms_flash_promotion_product_r_flash_promotion_id_flash_prom_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sms_flash_promotion_product_relation
    ADD CONSTRAINT sms_flash_promotion_product_r_flash_promotion_id_flash_prom_key UNIQUE (flash_promotion_id, flash_promotion_session_id, product_id);


--
-- Name: sms_flash_promotion_product_relation sms_flash_promotion_product_relation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sms_flash_promotion_product_relation
    ADD CONSTRAINT sms_flash_promotion_product_relation_pkey PRIMARY KEY (id);


--
-- Name: sms_flash_promotion_session sms_flash_promotion_session_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sms_flash_promotion_session
    ADD CONSTRAINT sms_flash_promotion_session_pkey PRIMARY KEY (id);


--
-- Name: sys_banner sys_banner_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sys_banner
    ADD CONSTRAINT sys_banner_pkey PRIMARY KEY (id);


--
-- Name: oms_payment_transaction uk_oms_payment_transaction_id; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.oms_payment_transaction
    ADD CONSTRAINT uk_oms_payment_transaction_id UNIQUE (transaction_id);


--
-- Name: ums_admin_login_log ums_admin_login_log_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ums_admin_login_log
    ADD CONSTRAINT ums_admin_login_log_pkey PRIMARY KEY (id);


--
-- Name: ums_admin ums_admin_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ums_admin
    ADD CONSTRAINT ums_admin_pkey PRIMARY KEY (id);


--
-- Name: ums_admin_role_relation ums_admin_role_relation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ums_admin_role_relation
    ADD CONSTRAINT ums_admin_role_relation_pkey PRIMARY KEY (id);


--
-- Name: ums_member_login_log ums_member_login_log_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ums_member_login_log
    ADD CONSTRAINT ums_member_login_log_pkey PRIMARY KEY (id);


--
-- Name: ums_member ums_member_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ums_member
    ADD CONSTRAINT ums_member_pkey PRIMARY KEY (id);


--
-- Name: ums_member_product_category_relation ums_member_product_category_relation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ums_member_product_category_relation
    ADD CONSTRAINT ums_member_product_category_relation_pkey PRIMARY KEY (id);


--
-- Name: ums_member_receive_address ums_member_receive_address_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ums_member_receive_address
    ADD CONSTRAINT ums_member_receive_address_pkey PRIMARY KEY (id);


--
-- Name: ums_menu ums_menu_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ums_menu
    ADD CONSTRAINT ums_menu_pkey PRIMARY KEY (id);


--
-- Name: ums_resource_category ums_resource_category_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ums_resource_category
    ADD CONSTRAINT ums_resource_category_pkey PRIMARY KEY (id);


--
-- Name: ums_resource ums_resource_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ums_resource
    ADD CONSTRAINT ums_resource_pkey PRIMARY KEY (id);


--
-- Name: ums_role_menu_relation ums_role_menu_relation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ums_role_menu_relation
    ADD CONSTRAINT ums_role_menu_relation_pkey PRIMARY KEY (id);


--
-- Name: ums_role ums_role_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ums_role
    ADD CONSTRAINT ums_role_pkey PRIMARY KEY (id);


--
-- Name: ums_role_resource_relation ums_role_resource_relation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ums_role_resource_relation
    ADD CONSTRAINT ums_role_resource_relation_pkey PRIMARY KEY (id);


--
-- Name: vector_store vector_store_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vector_store
    ADD CONSTRAINT vector_store_pkey PRIMARY KEY (id);


--
-- Name: sys_banner sys_banner_pkey; Type: CONSTRAINT; Schema: system; Owner: postgres
--

ALTER TABLE ONLY system.sys_banner
    ADD CONSTRAINT sys_banner_pkey PRIMARY KEY (id);


--
-- Name: ums_admin_login_log ums_admin_login_log_pkey; Type: CONSTRAINT; Schema: users; Owner: postgres
--

ALTER TABLE ONLY users.ums_admin_login_log
    ADD CONSTRAINT ums_admin_login_log_pkey PRIMARY KEY (id);


--
-- Name: ums_admin ums_admin_pkey; Type: CONSTRAINT; Schema: users; Owner: postgres
--

ALTER TABLE ONLY users.ums_admin
    ADD CONSTRAINT ums_admin_pkey PRIMARY KEY (id);


--
-- Name: ums_admin_role_relation ums_admin_role_relation_pkey; Type: CONSTRAINT; Schema: users; Owner: postgres
--

ALTER TABLE ONLY users.ums_admin_role_relation
    ADD CONSTRAINT ums_admin_role_relation_pkey PRIMARY KEY (id);


--
-- Name: ums_member_login_log ums_member_login_log_pkey; Type: CONSTRAINT; Schema: users; Owner: postgres
--

ALTER TABLE ONLY users.ums_member_login_log
    ADD CONSTRAINT ums_member_login_log_pkey PRIMARY KEY (id);


--
-- Name: ums_member ums_member_pkey; Type: CONSTRAINT; Schema: users; Owner: postgres
--

ALTER TABLE ONLY users.ums_member
    ADD CONSTRAINT ums_member_pkey PRIMARY KEY (id);


--
-- Name: ums_member_product_category_relation ums_member_product_category_relation_pkey; Type: CONSTRAINT; Schema: users; Owner: postgres
--

ALTER TABLE ONLY users.ums_member_product_category_relation
    ADD CONSTRAINT ums_member_product_category_relation_pkey PRIMARY KEY (id);


--
-- Name: ums_member_receive_address ums_member_receive_address_pkey; Type: CONSTRAINT; Schema: users; Owner: postgres
--

ALTER TABLE ONLY users.ums_member_receive_address
    ADD CONSTRAINT ums_member_receive_address_pkey PRIMARY KEY (id);


--
-- Name: ums_menu ums_menu_pkey; Type: CONSTRAINT; Schema: users; Owner: postgres
--

ALTER TABLE ONLY users.ums_menu
    ADD CONSTRAINT ums_menu_pkey PRIMARY KEY (id);


--
-- Name: ums_resource_category ums_resource_category_pkey; Type: CONSTRAINT; Schema: users; Owner: postgres
--

ALTER TABLE ONLY users.ums_resource_category
    ADD CONSTRAINT ums_resource_category_pkey PRIMARY KEY (id);


--
-- Name: ums_resource ums_resource_pkey; Type: CONSTRAINT; Schema: users; Owner: postgres
--

ALTER TABLE ONLY users.ums_resource
    ADD CONSTRAINT ums_resource_pkey PRIMARY KEY (id);


--
-- Name: ums_role_menu_relation ums_role_menu_relation_pkey; Type: CONSTRAINT; Schema: users; Owner: postgres
--

ALTER TABLE ONLY users.ums_role_menu_relation
    ADD CONSTRAINT ums_role_menu_relation_pkey PRIMARY KEY (id);


--
-- Name: ums_role ums_role_pkey; Type: CONSTRAINT; Schema: users; Owner: postgres
--

ALTER TABLE ONLY users.ums_role
    ADD CONSTRAINT ums_role_pkey PRIMARY KEY (id);


--
-- Name: ums_role_resource_relation ums_role_resource_relation_pkey; Type: CONSTRAINT; Schema: users; Owner: postgres
--

ALTER TABLE ONLY users.ums_role_resource_relation
    ADD CONSTRAINT ums_role_resource_relation_pkey PRIMARY KEY (id);


--
-- Name: idx_chat_member; Type: INDEX; Schema: ai; Owner: postgres
--

CREATE INDEX idx_chat_member ON ai.ai_chat_conversation USING btree (member_id);


--
-- Name: idx_chat_session; Type: INDEX; Schema: ai; Owner: postgres
--

CREATE INDEX idx_chat_session ON ai.ai_chat_conversation USING btree (session_id);


--
-- Name: idx_pms_product_publish_status; Type: INDEX; Schema: pms; Owner: postgres
--

CREATE INDEX idx_pms_product_publish_status ON pms.pms_product USING btree (publish_status, delete_status);


--
-- Name: idx_coupon_code; Type: INDEX; Schema: marketing; Owner: postgres
--

CREATE INDEX idx_coupon_code ON marketing.sms_coupon USING btree (code) WHERE (code IS NOT NULL);


--
-- Name: idx_coupon_history_member_status; Type: INDEX; Schema: marketing; Owner: postgres
--

CREATE INDEX idx_coupon_history_member_status ON marketing.sms_coupon_history USING btree (member_id, use_status);


--
-- Name: idx_coupon_id; Type: INDEX; Schema: marketing; Owner: postgres
--



--
-- Name: idx_flash_log_member; Type: INDEX; Schema: marketing; Owner: postgres
--

CREATE INDEX idx_flash_log_member ON marketing.sms_flash_promotion_log USING btree (member_id, product_id);


--
-- Name: idx_fpp_session; Type: INDEX; Schema: marketing; Owner: postgres
--

CREATE INDEX idx_fpp_session ON marketing.sms_flash_promotion_product_relation USING btree (flash_promotion_id, flash_promotion_session_id);


--
-- Name: flyway_schema_history_s_idx; Type: INDEX; Schema: migration; Owner: postgres
--

CREATE INDEX flyway_schema_history_s_idx ON migration.flyway_schema_history USING btree (success);


--
-- Name: idx_oms_order_create_time_status; Type: INDEX; Schema: orders; Owner: postgres
--

CREATE INDEX idx_oms_order_create_time_status ON orders.oms_order USING btree (create_time, status);


--
-- Name: idx_oms_order_return_status; Type: INDEX; Schema: orders; Owner: postgres
--

CREATE INDEX idx_oms_order_return_status ON orders.oms_order_return_apply USING btree (status);


--
-- Name: idx_oms_order_status; Type: INDEX; Schema: orders; Owner: postgres
--

CREATE INDEX idx_oms_order_status ON orders.oms_order USING btree (status);


--
-- Name: idx_oms_payment_order_id; Type: INDEX; Schema: orders; Owner: postgres
--

CREATE INDEX idx_oms_payment_order_id ON orders.oms_payment_transaction USING btree (order_id);


--
-- Name: flyway_schema_history_s_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX flyway_schema_history_s_idx ON public.flyway_schema_history USING btree (success);


--
-- Name: idx_chat_member; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_chat_member ON public.ai_chat_conversation USING btree (member_id);


--
-- Name: idx_chat_session; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_chat_session ON public.ai_chat_conversation USING btree (session_id);


--
-- Name: idx_coupon_code; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_coupon_code ON public.sms_coupon USING btree (code) WHERE (code IS NOT NULL);


--
-- Name: idx_coupon_history_member_status; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_coupon_history_member_status ON public.sms_coupon_history USING btree (member_id, use_status);


--
-- Name: idx_coupon_id; Type: INDEX; Schema: public; Owner: postgres
--



--
-- Name: idx_flash_log_member; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_flash_log_member ON public.sms_flash_promotion_log USING btree (member_id, product_id);


--
-- Name: idx_fpp_session; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_fpp_session ON public.sms_flash_promotion_product_relation USING btree (flash_promotion_id, flash_promotion_session_id);


--
-- Name: idx_oms_order_create_time_status; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_oms_order_create_time_status ON public.oms_order USING btree (create_time, status);


--
-- Name: idx_oms_order_return_status; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_oms_order_return_status ON public.oms_order_return_apply USING btree (status);


--
-- Name: idx_oms_order_status; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_oms_order_status ON public.oms_order USING btree (status);


--
-- Name: idx_oms_payment_order_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_oms_payment_order_id ON public.oms_payment_transaction USING btree (order_id);


--
-- Name: idx_pms_product_publish_status; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_pms_product_publish_status ON public.pms_product USING btree (publish_status, delete_status);


--
-- Name: idx_ums_member_create_time; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_ums_member_create_time ON public.ums_member USING btree (create_time);


--
-- Name: spring_ai_vector_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX spring_ai_vector_index ON public.vector_store USING hnsw (embedding public.vector_cosine_ops);


--
-- Name: idx_ums_member_create_time; Type: INDEX; Schema: users; Owner: postgres
--

CREATE INDEX idx_ums_member_create_time ON users.ums_member USING btree (create_time);


--
-- Name: ai_chat_conversation fk_ai_chat_conversation_member_id; Type: FK CONSTRAINT; Schema: ai; Owner: postgres
--

ALTER TABLE ONLY ai.ai_chat_conversation
    ADD CONSTRAINT fk_ai_chat_conversation_member_id FOREIGN KEY (member_id) REFERENCES users.ums_member(id);


--
-- Name: pms_product_embedding fk_pms_product_embedding_product_id; Type: FK CONSTRAINT; Schema: ai; Owner: postgres
--

ALTER TABLE ONLY ai.pms_product_embedding
    ADD CONSTRAINT fk_pms_product_embedding_product_id FOREIGN KEY (product_id) REFERENCES pms.pms_product(id);


--
-- Name: pms_comment fk_pms_comment_product_id; Type: FK CONSTRAINT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_comment
    ADD CONSTRAINT fk_pms_comment_product_id FOREIGN KEY (product_id) REFERENCES pms.pms_product(id);


--
-- Name: pms_comment_reply fk_pms_comment_reply_comment_id; Type: FK CONSTRAINT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_comment_reply
    ADD CONSTRAINT fk_pms_comment_reply_comment_id FOREIGN KEY (comment_id) REFERENCES pms.pms_comment(id);


--
-- Name: pms_product_attribute_value fk_pms_product_attribute_value_product_attribute_id; Type: FK CONSTRAINT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_product_attribute_value
    ADD CONSTRAINT fk_pms_product_attribute_value_product_attribute_id FOREIGN KEY (product_attribute_id) REFERENCES pms.pms_product_attribute(id);


--
-- Name: pms_product_attribute_value fk_pms_product_attribute_value_product_id; Type: FK CONSTRAINT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_product_attribute_value
    ADD CONSTRAINT fk_pms_product_attribute_value_product_id FOREIGN KEY (product_id) REFERENCES pms.pms_product(id);


--
-- Name: pms_product fk_pms_product_brand_id; Type: FK CONSTRAINT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_product
    ADD CONSTRAINT fk_pms_product_brand_id FOREIGN KEY (brand_id) REFERENCES pms.pms_brand(id);


--
-- Name: pms_product_category_attribute_relation fk_pms_product_category_attribute_relation_product_attribute_id; Type: FK CONSTRAINT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_product_category_attribute_relation
    ADD CONSTRAINT fk_pms_product_category_attribute_relation_product_attribute_id FOREIGN KEY (product_attribute_id) REFERENCES pms.pms_product_attribute(id);


--
-- Name: pms_product_category_attribute_relation fk_pms_product_category_attribute_relation_product_category_id; Type: FK CONSTRAINT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_product_category_attribute_relation
    ADD CONSTRAINT fk_pms_product_category_attribute_relation_product_category_id FOREIGN KEY (product_category_id) REFERENCES pms.pms_product_category(id);


--
-- Name: pms_product_spend_discount fk_pms_product_spend_discount_product_id; Type: FK CONSTRAINT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_product_spend_discount
    ADD CONSTRAINT fk_pms_product_spend_discount_product_id FOREIGN KEY (product_id) REFERENCES pms.pms_product(id);


--
-- Name: pms_product_volume_discount fk_pms_product_volume_discount_product_id; Type: FK CONSTRAINT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_product_volume_discount
    ADD CONSTRAINT fk_pms_product_volume_discount_product_id FOREIGN KEY (product_id) REFERENCES pms.pms_product(id);


--
-- Name: pms_product_gallery fk_pms_product_gallery_product_id; Type: FK CONSTRAINT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_product_gallery
    ADD CONSTRAINT fk_pms_product_gallery_product_id FOREIGN KEY (product_id) REFERENCES pms.pms_product(id);


--
-- Name: pms_product_audit_log fk_pms_product_audit_log_product_id; Type: FK CONSTRAINT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_product_audit_log
    ADD CONSTRAINT fk_pms_product_audit_log_product_id FOREIGN KEY (product_id) REFERENCES pms.pms_product(id);


--
-- Name: pms_product fk_pms_product_product_category_id; Type: FK CONSTRAINT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_product
    ADD CONSTRAINT fk_pms_product_product_category_id FOREIGN KEY (product_category_id) REFERENCES pms.pms_product_category(id);


--
-- Name: pms_product_approval_history fk_pms_product_approval_history_product_id; Type: FK CONSTRAINT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_product_approval_history
    ADD CONSTRAINT fk_pms_product_approval_history_product_id FOREIGN KEY (product_id) REFERENCES pms.pms_product(id);


--
-- Name: pms_review_attachments fk_pms_review_attachments_review_id; Type: FK CONSTRAINT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_review_attachments
    ADD CONSTRAINT fk_pms_review_attachments_review_id FOREIGN KEY (review_id) REFERENCES pms.pms_review(id);


--
-- Name: pms_review fk_pms_review_member_id; Type: FK CONSTRAINT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_review
    ADD CONSTRAINT fk_pms_review_member_id FOREIGN KEY (member_id) REFERENCES users.ums_member(id);


--
-- Name: pms_review fk_pms_review_parent_id; Type: FK CONSTRAINT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_review
    ADD CONSTRAINT fk_pms_review_parent_id FOREIGN KEY (parent_id) REFERENCES pms.pms_review(id);


--
-- Name: pms_review fk_pms_review_product_id; Type: FK CONSTRAINT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_review
    ADD CONSTRAINT fk_pms_review_product_id FOREIGN KEY (product_id) REFERENCES pms.pms_product(id);


--
-- Name: pms_sku_stock fk_pms_sku_stock_product_id; Type: FK CONSTRAINT; Schema: pms; Owner: postgres
--

ALTER TABLE ONLY pms.pms_sku_stock
    ADD CONSTRAINT fk_pms_sku_stock_product_id FOREIGN KEY (product_id) REFERENCES pms.pms_product(id);


--
--



--
--



--
-- Name: sms_coupon_history fk_sms_coupon_history_coupon_id; Type: FK CONSTRAINT; Schema: marketing; Owner: postgres
--

ALTER TABLE ONLY marketing.sms_coupon_history
    ADD CONSTRAINT fk_sms_coupon_history_coupon_id FOREIGN KEY (coupon_id) REFERENCES marketing.sms_coupon(id);


--
-- Name: sms_coupon_history fk_sms_coupon_history_member_id; Type: FK CONSTRAINT; Schema: marketing; Owner: postgres
--

ALTER TABLE ONLY marketing.sms_coupon_history
    ADD CONSTRAINT fk_sms_coupon_history_member_id FOREIGN KEY (member_id) REFERENCES users.ums_member(id);


--
-- Name: sms_coupon_history fk_sms_coupon_history_order_id; Type: FK CONSTRAINT; Schema: marketing; Owner: postgres
--

ALTER TABLE ONLY marketing.sms_coupon_history
    ADD CONSTRAINT fk_sms_coupon_history_order_id FOREIGN KEY (order_id) REFERENCES orders.oms_order(id);


--
-- Name: sms_coupon_product_category_relation fk_sms_coupon_product_category_relation_coupon_id; Type: FK CONSTRAINT; Schema: marketing; Owner: postgres
--

ALTER TABLE ONLY marketing.sms_coupon_product_category_relation
    ADD CONSTRAINT fk_sms_coupon_product_category_relation_coupon_id FOREIGN KEY (coupon_id) REFERENCES marketing.sms_coupon(id);


--
-- Name: sms_coupon_product_category_relation fk_sms_coupon_product_category_relation_product_category_id; Type: FK CONSTRAINT; Schema: marketing; Owner: postgres
--

ALTER TABLE ONLY marketing.sms_coupon_product_category_relation
    ADD CONSTRAINT fk_sms_coupon_product_category_relation_product_category_id FOREIGN KEY (product_category_id) REFERENCES pms.pms_product_category(id);


--
-- Name: sms_coupon_product_relation fk_sms_coupon_product_relation_coupon_id; Type: FK CONSTRAINT; Schema: marketing; Owner: postgres
--

ALTER TABLE ONLY marketing.sms_coupon_product_relation
    ADD CONSTRAINT fk_sms_coupon_product_relation_coupon_id FOREIGN KEY (coupon_id) REFERENCES marketing.sms_coupon(id);


--
-- Name: sms_coupon_product_relation fk_sms_coupon_product_relation_product_id; Type: FK CONSTRAINT; Schema: marketing; Owner: postgres
--

ALTER TABLE ONLY marketing.sms_coupon_product_relation
    ADD CONSTRAINT fk_sms_coupon_product_relation_product_id FOREIGN KEY (product_id) REFERENCES pms.pms_product(id);


--
-- Name: sms_flash_promotion_log fk_sms_flash_promotion_log_flash_promotion_id; Type: FK CONSTRAINT; Schema: marketing; Owner: postgres
--

ALTER TABLE ONLY marketing.sms_flash_promotion_log
    ADD CONSTRAINT fk_sms_flash_promotion_log_flash_promotion_id FOREIGN KEY (flash_promotion_id) REFERENCES marketing.sms_flash_promotion(id);


--
-- Name: sms_flash_promotion_log fk_sms_flash_promotion_log_member_id; Type: FK CONSTRAINT; Schema: marketing; Owner: postgres
--

ALTER TABLE ONLY marketing.sms_flash_promotion_log
    ADD CONSTRAINT fk_sms_flash_promotion_log_member_id FOREIGN KEY (member_id) REFERENCES users.ums_member(id);


--
-- Name: sms_flash_promotion_log fk_sms_flash_promotion_log_order_id; Type: FK CONSTRAINT; Schema: marketing; Owner: postgres
--

ALTER TABLE ONLY marketing.sms_flash_promotion_log
    ADD CONSTRAINT fk_sms_flash_promotion_log_order_id FOREIGN KEY (order_id) REFERENCES orders.oms_order(id);


--
-- Name: sms_flash_promotion_log fk_sms_flash_promotion_log_product_id; Type: FK CONSTRAINT; Schema: marketing; Owner: postgres
--

ALTER TABLE ONLY marketing.sms_flash_promotion_log
    ADD CONSTRAINT fk_sms_flash_promotion_log_product_id FOREIGN KEY (product_id) REFERENCES pms.pms_product(id);


--
-- Name: sms_flash_promotion_log fk_sms_flash_promotion_log_session_id; Type: FK CONSTRAINT; Schema: marketing; Owner: postgres
--

ALTER TABLE ONLY marketing.sms_flash_promotion_log
    ADD CONSTRAINT fk_sms_flash_promotion_log_session_id FOREIGN KEY (session_id) REFERENCES marketing.sms_flash_promotion_session(id);


--
-- Name: sms_flash_promotion_product_relation fk_sms_flash_promotion_product_relation_flash_promotion_id; Type: FK CONSTRAINT; Schema: marketing; Owner: postgres
--

ALTER TABLE ONLY marketing.sms_flash_promotion_product_relation
    ADD CONSTRAINT fk_sms_flash_promotion_product_relation_flash_promotion_id FOREIGN KEY (flash_promotion_id) REFERENCES marketing.sms_flash_promotion(id);


--
-- Name: sms_flash_promotion_product_relation fk_sms_flash_promotion_product_relation_flash_promotion_session; Type: FK CONSTRAINT; Schema: marketing; Owner: postgres
--

ALTER TABLE ONLY marketing.sms_flash_promotion_product_relation
    ADD CONSTRAINT fk_sms_flash_promotion_product_relation_flash_promotion_session FOREIGN KEY (flash_promotion_session_id) REFERENCES marketing.sms_flash_promotion_session(id);


--
-- Name: sms_flash_promotion_product_relation fk_sms_flash_promotion_product_relation_product_id; Type: FK CONSTRAINT; Schema: marketing; Owner: postgres
--

ALTER TABLE ONLY marketing.sms_flash_promotion_product_relation
    ADD CONSTRAINT fk_sms_flash_promotion_product_relation_product_id FOREIGN KEY (product_id) REFERENCES pms.pms_product(id);


--
-- Name: sms_flash_promotion_product_relation sms_flash_promotion_product_rel_flash_promotion_session_id_fkey; Type: FK CONSTRAINT; Schema: marketing; Owner: postgres
--

ALTER TABLE ONLY marketing.sms_flash_promotion_product_relation
    ADD CONSTRAINT sms_flash_promotion_product_rel_flash_promotion_session_id_fkey FOREIGN KEY (flash_promotion_session_id) REFERENCES marketing.sms_flash_promotion_session(id);


--
-- Name: sms_flash_promotion_product_relation sms_flash_promotion_product_relation_flash_promotion_id_fkey; Type: FK CONSTRAINT; Schema: marketing; Owner: postgres
--

ALTER TABLE ONLY marketing.sms_flash_promotion_product_relation
    ADD CONSTRAINT sms_flash_promotion_product_relation_flash_promotion_id_fkey FOREIGN KEY (flash_promotion_id) REFERENCES marketing.sms_flash_promotion(id);


--
-- Name: oms_cart_item fk_oms_cart_item_member_id; Type: FK CONSTRAINT; Schema: orders; Owner: postgres
--

ALTER TABLE ONLY orders.oms_cart_item
    ADD CONSTRAINT fk_oms_cart_item_member_id FOREIGN KEY (member_id) REFERENCES users.ums_member(id);


--
-- Name: oms_cart_item fk_oms_cart_item_product_category_id; Type: FK CONSTRAINT; Schema: orders; Owner: postgres
--

ALTER TABLE ONLY orders.oms_cart_item
    ADD CONSTRAINT fk_oms_cart_item_product_category_id FOREIGN KEY (product_category_id) REFERENCES pms.pms_product_category(id);


--
-- Name: oms_cart_item fk_oms_cart_item_product_id; Type: FK CONSTRAINT; Schema: orders; Owner: postgres
--

ALTER TABLE ONLY orders.oms_cart_item
    ADD CONSTRAINT fk_oms_cart_item_product_id FOREIGN KEY (product_id) REFERENCES pms.pms_product(id);


--
-- Name: oms_cart_item fk_oms_cart_item_product_sku_id; Type: FK CONSTRAINT; Schema: orders; Owner: postgres
--

ALTER TABLE ONLY orders.oms_cart_item
    ADD CONSTRAINT fk_oms_cart_item_product_sku_id FOREIGN KEY (product_sku_id) REFERENCES pms.pms_sku_stock(id);


--
-- Name: oms_order fk_oms_order_coupon_id; Type: FK CONSTRAINT; Schema: orders; Owner: postgres
--

ALTER TABLE ONLY orders.oms_order
    ADD CONSTRAINT fk_oms_order_coupon_id FOREIGN KEY (coupon_id) REFERENCES marketing.sms_coupon(id);


--
-- Name: oms_order_item fk_oms_order_item_order; Type: FK CONSTRAINT; Schema: orders; Owner: postgres
--

ALTER TABLE ONLY orders.oms_order_item
    ADD CONSTRAINT fk_oms_order_item_order FOREIGN KEY (order_id) REFERENCES orders.oms_order(id);


--
-- Name: oms_order_item fk_oms_order_item_order_id; Type: FK CONSTRAINT; Schema: orders; Owner: postgres
--

ALTER TABLE ONLY orders.oms_order_item
    ADD CONSTRAINT fk_oms_order_item_order_id FOREIGN KEY (order_id) REFERENCES orders.oms_order(id);


--
-- Name: oms_order_item fk_oms_order_item_product; Type: FK CONSTRAINT; Schema: orders; Owner: postgres
--

ALTER TABLE ONLY orders.oms_order_item
    ADD CONSTRAINT fk_oms_order_item_product FOREIGN KEY (product_id) REFERENCES pms.pms_product(id);


--
-- Name: oms_order_item fk_oms_order_item_product_category_id; Type: FK CONSTRAINT; Schema: orders; Owner: postgres
--

ALTER TABLE ONLY orders.oms_order_item
    ADD CONSTRAINT fk_oms_order_item_product_category_id FOREIGN KEY (product_category_id) REFERENCES pms.pms_product_category(id);


--
-- Name: oms_order_item fk_oms_order_item_product_id; Type: FK CONSTRAINT; Schema: orders; Owner: postgres
--

ALTER TABLE ONLY orders.oms_order_item
    ADD CONSTRAINT fk_oms_order_item_product_id FOREIGN KEY (product_id) REFERENCES pms.pms_product(id);


--
-- Name: oms_order_item fk_oms_order_item_product_sku_id; Type: FK CONSTRAINT; Schema: orders; Owner: postgres
--

ALTER TABLE ONLY orders.oms_order_item
    ADD CONSTRAINT fk_oms_order_item_product_sku_id FOREIGN KEY (product_sku_id) REFERENCES pms.pms_sku_stock(id);


--
-- Name: oms_order fk_oms_order_member; Type: FK CONSTRAINT; Schema: orders; Owner: postgres
--

ALTER TABLE ONLY orders.oms_order
    ADD CONSTRAINT fk_oms_order_member FOREIGN KEY (member_id) REFERENCES users.ums_member(id);


--
-- Name: oms_order fk_oms_order_member_id; Type: FK CONSTRAINT; Schema: orders; Owner: postgres
--

ALTER TABLE ONLY orders.oms_order
    ADD CONSTRAINT fk_oms_order_member_id FOREIGN KEY (member_id) REFERENCES users.ums_member(id);


--
-- Name: oms_order_operate_history fk_oms_order_operate_history_order_id; Type: FK CONSTRAINT; Schema: orders; Owner: postgres
--

ALTER TABLE ONLY orders.oms_order_operate_history
    ADD CONSTRAINT fk_oms_order_operate_history_order_id FOREIGN KEY (order_id) REFERENCES orders.oms_order(id);


--
-- Name: oms_order_return_apply fk_oms_order_return_apply_company_address_id; Type: FK CONSTRAINT; Schema: orders; Owner: postgres
--

ALTER TABLE ONLY orders.oms_order_return_apply
    ADD CONSTRAINT fk_oms_order_return_apply_company_address_id FOREIGN KEY (company_address_id) REFERENCES orders.oms_company_address(id);


--
-- Name: oms_order_return_apply fk_oms_order_return_apply_order_id; Type: FK CONSTRAINT; Schema: orders; Owner: postgres
--

ALTER TABLE ONLY orders.oms_order_return_apply
    ADD CONSTRAINT fk_oms_order_return_apply_order_id FOREIGN KEY (order_id) REFERENCES orders.oms_order(id);


--
-- Name: oms_order_return_apply fk_oms_order_return_apply_product_id; Type: FK CONSTRAINT; Schema: orders; Owner: postgres
--

ALTER TABLE ONLY orders.oms_order_return_apply
    ADD CONSTRAINT fk_oms_order_return_apply_product_id FOREIGN KEY (product_id) REFERENCES pms.pms_product(id);


--
-- Name: oms_payment_transaction fk_oms_payment_transaction_order_id; Type: FK CONSTRAINT; Schema: orders; Owner: postgres
--

ALTER TABLE ONLY orders.oms_payment_transaction
    ADD CONSTRAINT fk_oms_payment_transaction_order_id FOREIGN KEY (order_id) REFERENCES orders.oms_order(id);


--
-- Name: sms_flash_promotion_product_relation sms_flash_promotion_product_rel_flash_promotion_session_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sms_flash_promotion_product_relation
    ADD CONSTRAINT sms_flash_promotion_product_rel_flash_promotion_session_id_fkey FOREIGN KEY (flash_promotion_session_id) REFERENCES public.sms_flash_promotion_session(id);


--
-- Name: sms_flash_promotion_product_relation sms_flash_promotion_product_relation_flash_promotion_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sms_flash_promotion_product_relation
    ADD CONSTRAINT sms_flash_promotion_product_relation_flash_promotion_id_fkey FOREIGN KEY (flash_promotion_id) REFERENCES public.sms_flash_promotion(id);


--
-- Name: ums_admin_login_log fk_ums_admin_login_log_admin_id; Type: FK CONSTRAINT; Schema: users; Owner: postgres
--

ALTER TABLE ONLY users.ums_admin_login_log
    ADD CONSTRAINT fk_ums_admin_login_log_admin_id FOREIGN KEY (admin_id) REFERENCES users.ums_admin(id);


--
-- Name: ums_admin_role_relation fk_ums_admin_role_relation_admin_id; Type: FK CONSTRAINT; Schema: users; Owner: postgres
--

ALTER TABLE ONLY users.ums_admin_role_relation
    ADD CONSTRAINT fk_ums_admin_role_relation_admin_id FOREIGN KEY (admin_id) REFERENCES users.ums_admin(id);


--
-- Name: ums_admin_role_relation fk_ums_admin_role_relation_role_id; Type: FK CONSTRAINT; Schema: users; Owner: postgres
--

ALTER TABLE ONLY users.ums_admin_role_relation
    ADD CONSTRAINT fk_ums_admin_role_relation_role_id FOREIGN KEY (role_id) REFERENCES users.ums_role(id);


--
-- Name: ums_member_login_log fk_ums_member_login_log_member_id; Type: FK CONSTRAINT; Schema: users; Owner: postgres
--

ALTER TABLE ONLY users.ums_member_login_log
    ADD CONSTRAINT fk_ums_member_login_log_member_id FOREIGN KEY (member_id) REFERENCES users.ums_member(id);


--
-- Name: ums_member_product_category_relation fk_ums_member_product_category_relation_member_id; Type: FK CONSTRAINT; Schema: users; Owner: postgres
--

ALTER TABLE ONLY users.ums_member_product_category_relation
    ADD CONSTRAINT fk_ums_member_product_category_relation_member_id FOREIGN KEY (member_id) REFERENCES users.ums_member(id);


--
-- Name: ums_member_product_category_relation fk_ums_member_product_category_relation_product_category_id; Type: FK CONSTRAINT; Schema: users; Owner: postgres
--

ALTER TABLE ONLY users.ums_member_product_category_relation
    ADD CONSTRAINT fk_ums_member_product_category_relation_product_category_id FOREIGN KEY (product_category_id) REFERENCES pms.pms_product_category(id);


--
-- Name: ums_member_receive_address fk_ums_member_receive_address_member; Type: FK CONSTRAINT; Schema: users; Owner: postgres
--

ALTER TABLE ONLY users.ums_member_receive_address
    ADD CONSTRAINT fk_ums_member_receive_address_member FOREIGN KEY (member_id) REFERENCES users.ums_member(id);


--
-- Name: ums_member_receive_address fk_ums_member_receive_address_member_id; Type: FK CONSTRAINT; Schema: users; Owner: postgres
--

ALTER TABLE ONLY users.ums_member_receive_address
    ADD CONSTRAINT fk_ums_member_receive_address_member_id FOREIGN KEY (member_id) REFERENCES users.ums_member(id);


--
-- Name: ums_menu fk_ums_menu_parent_id; Type: FK CONSTRAINT; Schema: users; Owner: postgres
--

ALTER TABLE ONLY users.ums_menu
    ADD CONSTRAINT fk_ums_menu_parent_id FOREIGN KEY (parent_id) REFERENCES users.ums_menu(id);


--
-- Name: ums_resource fk_ums_resource_category_id; Type: FK CONSTRAINT; Schema: users; Owner: postgres
--

ALTER TABLE ONLY users.ums_resource
    ADD CONSTRAINT fk_ums_resource_category_id FOREIGN KEY (category_id) REFERENCES users.ums_resource_category(id);


--
-- Name: ums_role_menu_relation fk_ums_role_menu_relation_menu_id; Type: FK CONSTRAINT; Schema: users; Owner: postgres
--

ALTER TABLE ONLY users.ums_role_menu_relation
    ADD CONSTRAINT fk_ums_role_menu_relation_menu_id FOREIGN KEY (menu_id) REFERENCES users.ums_menu(id);


--
-- Name: ums_role_menu_relation fk_ums_role_menu_relation_role_id; Type: FK CONSTRAINT; Schema: users; Owner: postgres
--

ALTER TABLE ONLY users.ums_role_menu_relation
    ADD CONSTRAINT fk_ums_role_menu_relation_role_id FOREIGN KEY (role_id) REFERENCES users.ums_role(id);


--
-- Name: ums_role_resource_relation fk_ums_role_resource_relation_resource_id; Type: FK CONSTRAINT; Schema: users; Owner: postgres
--

ALTER TABLE ONLY users.ums_role_resource_relation
    ADD CONSTRAINT fk_ums_role_resource_relation_resource_id FOREIGN KEY (resource_id) REFERENCES users.ums_resource(id);


--
-- Name: ums_role_resource_relation fk_ums_role_resource_relation_role_id; Type: FK CONSTRAINT; Schema: users; Owner: postgres
--

ALTER TABLE ONLY users.ums_role_resource_relation
    ADD CONSTRAINT fk_ums_role_resource_relation_role_id FOREIGN KEY (role_id) REFERENCES users.ums_role(id);


--
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE USAGE ON SCHEMA public FROM PUBLIC;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

\unrestrict 3N866ErZhZRDDO8oMCygVchKsc2aqnqacdAspyWESIZ5nEEnvD48mfCuDc7Myef

--
-- Database "postgres" dump
--

\connect postgres

--
-- PostgreSQL database dump
--

\restrict t5UhMQEUEl4O8FFglM6ZFhBqhR7YvPG7TSmWo5brcuybwOTPdEUUxePDaYQmFrc

-- Dumped from database version 17.11 (Debian 17.11-1.pgdg12+2)
-- Dumped by pg_dump version 17.11 (Debian 17.11-1.pgdg12+2)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- PostgreSQL database dump complete
--

\unrestrict t5UhMQEUEl4O8FFglM6ZFhBqhR7YvPG7TSmWo5brcuybwOTPdEUUxePDaYQmFrc

--
-- PostgreSQL database cluster dump complete
--

