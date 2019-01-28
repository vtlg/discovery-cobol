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
    
-- DROP INDEX public.index_tbl_artefato_02;

CREATE INDEX index_tbl_artefato_02
    ON public.tbl_artefato USING btree
    (ts_fim_vigencia, co_tipo_artefato COLLATE pg_catalog."default" varchar_ops, no_nome_artefato COLLATE pg_catalog."default" varchar_ops)
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
    
    
-- View: public.vw_artefato

-- DROP VIEW public.vw_artefato;

CREATE OR REPLACE VIEW public.vw_artefato AS
 SELECT DISTINCT t1.co_artefato,
    t1.no_nome_artefato,
    t1.no_nome_exibicao,
    t1.no_nome_interno,
    t1.co_ambiente,
    t1.co_sistema,
    t1.co_tipo_artefato,
    t1.de_identificador,
    t1.de_hash,
    t1.de_descricao_usuario,
    t1.de_descricao_artefato,
    t1.ic_processo_critico,
    t1.ic_inclusao_manual,
    t1.ts_inicio_vigencia,
    t1.ts_ultima_modificacao,
    count(t2.*) AS count_relacionamento,
    count(t2.*) FILTER (WHERE t2.co_tipo_relacionamento::text = 'INTERFACE'::text) AS count_relacionamento_interface,
    count(t2.*) FILTER (WHERE t2.co_tipo_relacionamento::text = 'NORMAL'::text) AS count_relacionamento_normal,
    count(t2.*) FILTER (WHERE t2.co_tipo_relacionamento::text = 'CONTROL-M'::text) AS count_relacionamento_control_m
   FROM tbl_artefato t1
     LEFT JOIN tbl_relacionamento_artefato t2 ON (t2.co_artefato = t1.co_artefato OR t2.co_artefato_pai = t1.co_artefato) AND t2.ts_fim_vigencia IS NULL
  WHERE t1.ts_fim_vigencia IS NULL
  GROUP BY t1.co_artefato, t1.no_nome_artefato, t1.no_nome_exibicao, t1.no_nome_interno, t1.co_ambiente, t1.co_sistema, t1.co_tipo_artefato, t1.de_identificador, t1.de_hash, t1.de_descricao_usuario, t1.de_descricao_artefato, t1.ic_processo_critico, t1.ic_inclusao_manual, t1.ts_inicio_vigencia, t1.ts_ultima_modificacao;

ALTER TABLE public.vw_artefato
    OWNER TO postgres;


-- View: public.vw_relacionamento

-- DROP VIEW public.vw_relacionamento;

CREATE OR REPLACE VIEW public.vw_relacionamento AS
 SELECT t1.co_relacionamento AS rel_co_relacionamento,
    t1.co_tipo_relacionamento AS rel_co_tipo_relacionamento,
    t1.ic_inclusao_manual AS rel_ic_inclusao_manual,
    t1.ic_inclusao_malha AS rel_ic_inclusao_malha,
    t4.nu_sequencial AS atr_nu_sequencial,
    t4.co_tipo_atributo AS atr_co_tipo_atributo,
    t4.co_tabela AS atr_co_tabela,
    t4.co_externo AS atr_co_externo,
    t4.de_valor AS atr_de_valor,
    t4.ic_editavel AS atr_ic_editavel,
    t4.ic_opcional AS atr_ic_opcional,
    t2.co_artefato AS asc_co_artefato,
    t2.no_nome_artefato AS asc_no_nome_artefato,
    t2.no_nome_exibicao AS asc_no_nome_exibicao,
    t2.no_nome_interno AS asc_no_nome_interno,
    t2.co_ambiente AS asc_co_ambiente,
    t2.co_sistema AS asc_co_sistema,
    t2.co_tipo_artefato AS asc_co_tipo_artefato,
    t2.de_identificador AS asc_de_identificador,
    t2.ic_inclusao_manual AS asc_ic_inclusao_manual,
    t2.ic_processo_critico AS asc_ic_processo_critico,
    t3.co_artefato AS desc_co_artefato,
    t3.no_nome_artefato AS desc_no_nome_artefato,
    t3.no_nome_exibicao AS desc_no_nome_exibicao,
    t3.no_nome_interno AS desc_no_nome_interno,
    t3.co_ambiente AS desc_co_ambiente,
    t3.co_sistema AS desc_co_sistema,
    t3.co_tipo_artefato AS desc_co_tipo_artefato,
    t3.de_identificador AS desc_de_identificador,
    t3.ic_inclusao_manual AS desc_ic_inclusao_manual,
    t3.ic_processo_critico AS desc_ic_processo_critico
   FROM tbl_relacionamento_artefato t1
     LEFT JOIN tbl_artefato t3 ON t3.co_artefato = t1.co_artefato
     LEFT JOIN tbl_artefato t2 ON t2.co_artefato = t1.co_artefato_pai
     LEFT JOIN tbl_atributo t4 ON t4.co_externo = t1.co_relacionamento AND t4.co_tabela::text = 'RELACIONAMENTO_ARTEFATO'::text
  WHERE t1.ts_fim_vigencia IS NULL AND t2.ts_fim_vigencia IS NULL AND t3.ts_fim_vigencia IS NULL;

ALTER TABLE public.vw_relacionamento
    OWNER TO postgres;

