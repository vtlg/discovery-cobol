-- SEQUENCE: public.seq_artefato_co_artefato

-- DROP SEQUENCE public.seq_artefato_co_artefato;

CREATE SEQUENCE public.seq_artefato_co_artefato;

ALTER SEQUENCE public.seq_artefato_co_artefato
    OWNER TO postgres;
    
    
-- SEQUENCE: public.seq_atributo_nu_sequencial

-- DROP SEQUENCE public.seq_atributo_nu_sequencial;

CREATE SEQUENCE public.seq_atributo_nu_sequencial;

ALTER SEQUENCE public.seq_atributo_nu_sequencial
    OWNER TO postgres;
    
    
-- SEQUENCE: public.seq_relacionamento_co_relacionamento

-- DROP SEQUENCE public.seq_relacionamento_co_relacionamento;

CREATE SEQUENCE public.seq_relacionamento_co_relacionamento;

ALTER SEQUENCE public.seq_relacionamento_co_relacionamento
    OWNER TO postgres;
    
-- Table: public.tbl_artefato

-- DROP TABLE public.tbl_artefato;

CREATE TABLE public.tbl_artefato
(
    co_artefato bigint NOT NULL DEFAULT nextval('seq_artefato_co_artefato'::regclass),
    no_nome_artefato character varying(300) COLLATE pg_catalog."default" NOT NULL,
    no_nome_exibicao character varying(300) COLLATE pg_catalog."default" NOT NULL,
    no_nome_interno character varying(300) COLLATE pg_catalog."default" DEFAULT ' '::character varying,
    co_ambiente character varying(15) COLLATE pg_catalog."default" NOT NULL,
    co_sistema character varying(15) COLLATE pg_catalog."default" NOT NULL,
    co_tipo_artefato character varying(100) COLLATE pg_catalog."default",
    de_identificador character varying(500) COLLATE pg_catalog."default",
    de_hash character varying(256) COLLATE pg_catalog."default" NOT NULL DEFAULT ' '::character varying,
    de_descricao_usuario text COLLATE pg_catalog."default" NOT NULL DEFAULT ' '::text,
    de_descricao_artefato text COLLATE pg_catalog."default" NOT NULL DEFAULT ' '::text,
    ic_processo_critico boolean NOT NULL DEFAULT false,
    ic_inclusao_manual boolean NOT NULL DEFAULT false,
    ts_inicio_vigencia timestamp without time zone NOT NULL DEFAULT now(),
    ts_ultima_modificacao timestamp without time zone NOT NULL DEFAULT now(),
    ts_fim_vigencia timestamp without time zone,
    CONSTRAINT tbl_artefato_pkey PRIMARY KEY (co_artefato),
    CONSTRAINT unique_tbl_artefato UNIQUE (no_nome_artefato, co_ambiente, co_tipo_artefato, co_sistema, ts_fim_vigencia)

)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.tbl_artefato
    OWNER to postgres;

-- Index: index_tbl_artefato_01

-- DROP INDEX public.index_tbl_artefato_01;

CREATE INDEX index_tbl_artefato_01
    ON public.tbl_artefato USING btree
    (co_sistema COLLATE pg_catalog."default" varchar_ops, co_ambiente COLLATE pg_catalog."default" varchar_ops, co_tipo_artefato COLLATE pg_catalog."default" varchar_ops, no_nome_artefato COLLATE pg_catalog."default" varchar_pattern_ops)
    TABLESPACE pg_default;
    
-- Table: public.tbl_atributo

-- DROP TABLE public.tbl_atributo;

CREATE TABLE public.tbl_atributo
(
    nu_sequencial bigint NOT NULL DEFAULT nextval('seq_atributo_nu_sequencial'::regclass),
    co_tipo_atributo character varying(100) COLLATE pg_catalog."default",
    co_tabela character varying(100) COLLATE pg_catalog."default",
    co_externo bigint,
    de_valor text COLLATE pg_catalog."default" DEFAULT ' '::text,
    ic_editavel boolean NOT NULL DEFAULT false,
    ic_opcional boolean NOT NULL DEFAULT true,
    CONSTRAINT tbl_atributo_pkey PRIMARY KEY (nu_sequencial)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.tbl_atributo
    OWNER to postgres;
    
-- Table: public.tbl_relacionamento_artefato

-- DROP TABLE public.tbl_relacionamento_artefato;

CREATE TABLE public.tbl_relacionamento_artefato
(
    co_relacionamento bigint NOT NULL DEFAULT nextval('seq_relacionamento_co_relacionamento'::regclass),
    co_tipo_relacionamento character varying(100) COLLATE pg_catalog."default" NOT NULL DEFAULT 'NORMAL'::character varying,
    co_artefato bigint,
    co_artefato_pai bigint,
    co_artefato_anterior bigint,
    co_artefato_posterior bigint,
    co_artefato_primeiro bigint,
    co_artefato_ultimo bigint,
    ic_inclusao_manual boolean NOT NULL DEFAULT false,
    ic_inclusao_malha boolean NOT NULL DEFAULT false,
    ts_inicio_vigencia timestamp without time zone NOT NULL DEFAULT now(),
    ts_fim_vigencia timestamp without time zone,
    CONSTRAINT tbl_relacionamento_artefato_pkey PRIMARY KEY (co_relacionamento),
    CONSTRAINT unique_tbl_relacionamento_artefato UNIQUE (co_artefato, co_artefato_pai, co_artefato_anterior, co_artefato_posterior, co_artefato_primeiro, co_artefato_ultimo)

)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.tbl_relacionamento_artefato
    OWNER to postgres;
    
-- Table: public.tbl_tipo

-- DROP TABLE public.tbl_tipo;

CREATE TABLE public.tbl_tipo
(
    co_tabela character varying(100) COLLATE pg_catalog."default",
    co_tipo character varying(100) COLLATE pg_catalog."default" NOT NULL,
    de_tipo character varying(500) COLLATE pg_catalog."default",
    ic_pesquisavel boolean NOT NULL DEFAULT true,
    ic_exibir_atributo boolean NOT NULL DEFAULT true,
    ic_exibir_grafo boolean NOT NULL DEFAULT true,
    co_cor character varying(30) COLLATE pg_catalog."default" NOT NULL DEFAULT '#000000'::character varying,
    co_cor_borda character varying(30) COLLATE pg_catalog."default",
    nu_largura_borda integer NOT NULL DEFAULT 1,
    CONSTRAINT tbl_tipo_pkey PRIMARY KEY (co_tipo)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.tbl_tipo
    OWNER to postgres;