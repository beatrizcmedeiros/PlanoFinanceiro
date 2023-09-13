--
-- PostgreSQL database dump
--

-- Dumped from database version 14.8
-- Dumped by pg_dump version 14.8

-- Started on 2023-07-10 07:27:12

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 217 (class 1259 OID 49897)
-- Name: categoria_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.categoria_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.categoria_id_seq OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 212 (class 1259 OID 49846)
-- Name: categoria; Type: TABLE; Schema: public; Owner: dba
--

CREATE TABLE public.categoria (
    codigo bigint DEFAULT nextval('public.categoria_id_seq'::regclass) NOT NULL,
    descricao character varying(255) NOT NULL
);


ALTER TABLE public.categoria OWNER TO dba;

--
-- TOC entry 218 (class 1259 OID 49898)
-- Name: despesa_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.despesa_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.despesa_id_seq OWNER TO postgres;

--
-- TOC entry 213 (class 1259 OID 49851)
-- Name: despesa; Type: TABLE; Schema: public; Owner: dba
--

CREATE TABLE public.despesa (
    codigo bigint DEFAULT nextval('public.despesa_id_seq'::regclass) NOT NULL,
    descricao character varying(255) NOT NULL,
    cod_categoria bigint NOT NULL
);


ALTER TABLE public.despesa OWNER TO dba;

--
-- TOC entry 216 (class 1259 OID 49894)
-- Name: forma_pagamento_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.forma_pagamento_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.forma_pagamento_id_seq OWNER TO postgres;

--
-- TOC entry 211 (class 1259 OID 49841)
-- Name: forma_pagamento; Type: TABLE; Schema: public; Owner: dba
--

CREATE TABLE public.forma_pagamento (
    codigo bigint DEFAULT nextval('public.forma_pagamento_id_seq'::regclass) NOT NULL,
    descricao character varying(255) NOT NULL
);


ALTER TABLE public.forma_pagamento OWNER TO dba;

--
-- TOC entry 219 (class 1259 OID 49899)
-- Name: investimento_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.investimento_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.investimento_id_seq OWNER TO postgres;

--
-- TOC entry 214 (class 1259 OID 49886)
-- Name: investimento; Type: TABLE; Schema: public; Owner: dba
--

CREATE TABLE public.investimento (
    codigo bigint DEFAULT nextval('public.investimento_id_seq'::regclass) NOT NULL,
    objetivo character varying(255) NOT NULL,
    estrategia character varying(255) NOT NULL,
    nome character varying(255) NOT NULL,
    valor_investido double precision NOT NULL,
    posicao double precision NOT NULL,
    rendimento_bruto double precision NOT NULL,
    rentabilidade double precision NOT NULL,
    vencimento date NOT NULL
);


ALTER TABLE public.investimento OWNER TO dba;

--
-- TOC entry 220 (class 1259 OID 49914)
-- Name: orcamento; Type: TABLE; Schema: public; Owner: dba
--

CREATE TABLE public.orcamento (
    mes_ano character varying(7) NOT NULL,
    cod_despesa bigint NOT NULL,
    data_despesa date NOT NULL,
    data_pagamento date NOT NULL,
    cod_forma_pagamento bigint NOT NULL,
    valor double precision NOT NULL,
    situacao character varying(4)
);


ALTER TABLE public.orcamento OWNER TO dba;

--
-- TOC entry 215 (class 1259 OID 49893)
-- Name: renda_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.renda_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.renda_id_seq OWNER TO postgres;

--
-- TOC entry 209 (class 1259 OID 49826)
-- Name: renda; Type: TABLE; Schema: public; Owner: dba
--

CREATE TABLE public.renda (
    codigo bigint DEFAULT nextval('public.renda_id_seq'::regclass) NOT NULL,
    descricao character varying(255) NOT NULL
);


ALTER TABLE public.renda OWNER TO dba;

--
-- TOC entry 210 (class 1259 OID 49831)
-- Name: renda_mensal; Type: TABLE; Schema: public; Owner: dba
--

CREATE TABLE public.renda_mensal (
    cod_renda bigint NOT NULL,
    data date NOT NULL,
    valor double precision NOT NULL
);


ALTER TABLE public.renda_mensal OWNER TO dba;

--
-- TOC entry 3357 (class 0 OID 49846)
-- Dependencies: 212
-- Data for Name: categoria; Type: TABLE DATA; Schema: public; Owner: dba
--

COPY public.categoria (codigo, descricao) FROM stdin;
2	Lazer
3	Automóvel
4	Alimentação
5	Serviços
6	Moradia
7	Contas
8	Saúde
\.


--
-- TOC entry 3358 (class 0 OID 49851)
-- Dependencies: 213
-- Data for Name: despesa; Type: TABLE DATA; Schema: public; Owner: dba
--

COPY public.despesa (codigo, descricao, cod_categoria) FROM stdin;
2	Cinema	2
3	Gasolina	3
4	Restaurante	4
5	Internet	5
6	Aluguel	6
7	Restaurante	4
8	Internet	5
9	Aluguel	6
10	Supermercado	4
11	Energia Elétrica	7
12	Academia	8
\.


--
-- TOC entry 3356 (class 0 OID 49841)
-- Dependencies: 211
-- Data for Name: forma_pagamento; Type: TABLE DATA; Schema: public; Owner: dba
--

COPY public.forma_pagamento (codigo, descricao) FROM stdin;
2	CD
3	CC
4	Dinheiro
5	Transferência
6	Boleto
\.


--
-- TOC entry 3359 (class 0 OID 49886)
-- Dependencies: 214
-- Data for Name: investimento; Type: TABLE DATA; Schema: public; Owner: dba
--

COPY public.investimento (codigo, objetivo, estrategia, nome, valor_investido, posicao, rendimento_bruto, rentabilidade, vencimento) FROM stdin;
2	Viagem	Pós-fixado	CDB Original Jan/2026	2000	2789.45	789.45	39.47	2026-01-14
3	Comprar notebook	Multimercado	Western Asset FIM	4000	7561.34	3561.34	89.03	2023-12-15
4	Aposentadoria	Previdência Privada	Previdência XXI	10000	11500	1500	15	2050-06-30
5	Fundo de emergência	Tesouro Direto	Tesouro Selic	5000	5100	100	2	2024-05-10
6	Comprar carro	Renda Fixa	LCI Banco X	8000	8300	300	3.75	2025-09-20
7	Estudos	Ações	Apple Inc.	3500	4200	700	20	2026-06-20
8	Férias	Pós-fixado	CDB Original Jan/2025	1500	1800	300	20	2025-07-10
9	Comprar imóvel	Fundos Imobiliários	FIIB11	15000	16500	1500	10	2024-08-16
10	Educação dos filhos	Previdência Privada	Previdência Infinita	7000	9500	2500	35.71	2040-11-30
11	Diversificação	Ações	Amazon.com Inc.	6000	7200	1200	20	2029-03-04
\.


--
-- TOC entry 3365 (class 0 OID 49914)
-- Dependencies: 220
-- Data for Name: orcamento; Type: TABLE DATA; Schema: public; Owner: dba
--

COPY public.orcamento (mes_ano, cod_despesa, data_despesa, data_pagamento, cod_forma_pagamento, valor, situacao) FROM stdin;
06/2023	2	2023-06-05	2023-06-05	2	14.5	Paga
06/2023	3	2023-06-06	2023-06-10	3	254.43	\N
07/2023	4	2023-06-07	2023-07-05	4	75.9	Paga
09/2023	5	2023-06-08	2023-09-03	5	99.99	\N
08/2023	6	2023-06-09	2023-08-01	6	1200	Paga
08/2023	7	2023-06-07	2023-08-02	4	75.9	Paga
06/2023	8	2023-06-08	2023-06-02	5	99.99	\N
06/2023	9	2023-06-09	2023-06-03	6	1200	\N
07/2023	10	2023-06-10	2023-07-04	3	150	Paga
08/2023	11	2023-06-11	2023-08-19	6	120	\N
06/2023	12	2023-06-12	2023-06-15	2	50	Paga
\.


--
-- TOC entry 3354 (class 0 OID 49826)
-- Dependencies: 209
-- Data for Name: renda; Type: TABLE DATA; Schema: public; Owner: dba
--

COPY public.renda (codigo, descricao) FROM stdin;
2	Salário
3	Bolsa de pesquisa
4	Prestação de serviço
5	Prêmio de venda
6	Renda de aluguel
7	Bonificação
8	Comissão de vendas
9	Prêmio de produtividade
10	Reembolso de despesas
11	Consultoria
\.


--
-- TOC entry 3355 (class 0 OID 49831)
-- Dependencies: 210
-- Data for Name: renda_mensal; Type: TABLE DATA; Schema: public; Owner: dba
--

COPY public.renda_mensal (cod_renda, data, valor) FROM stdin;
2	2023-06-02	4
3	2023-05-05	1.3
4	2023-01-15	750
5	2023-04-20	2.5
6	2023-03-10	1.2
7	2023-02-07	500
8	2023-07-10	1
9	2023-06-25	500
10	2023-05-18	300
11	2023-04-03	900
\.


--
-- TOC entry 3371 (class 0 OID 0)
-- Dependencies: 217
-- Name: categoria_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.categoria_id_seq', 8, true);


--
-- TOC entry 3372 (class 0 OID 0)
-- Dependencies: 218
-- Name: despesa_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.despesa_id_seq', 12, true);


--
-- TOC entry 3373 (class 0 OID 0)
-- Dependencies: 216
-- Name: forma_pagamento_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.forma_pagamento_id_seq', 6, true);


--
-- TOC entry 3374 (class 0 OID 0)
-- Dependencies: 219
-- Name: investimento_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.investimento_id_seq', 11, true);


--
-- TOC entry 3375 (class 0 OID 0)
-- Dependencies: 215
-- Name: renda_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.renda_id_seq', 11, true);


--
-- TOC entry 3204 (class 2606 OID 49850)
-- Name: categoria categoria_pkey; Type: CONSTRAINT; Schema: public; Owner: dba
--

ALTER TABLE ONLY public.categoria
    ADD CONSTRAINT categoria_pkey PRIMARY KEY (codigo);


--
-- TOC entry 3206 (class 2606 OID 49855)
-- Name: despesa despesa_pkey; Type: CONSTRAINT; Schema: public; Owner: dba
--

ALTER TABLE ONLY public.despesa
    ADD CONSTRAINT despesa_pkey PRIMARY KEY (codigo);


--
-- TOC entry 3202 (class 2606 OID 49845)
-- Name: forma_pagamento forma_pagamento_pkey; Type: CONSTRAINT; Schema: public; Owner: dba
--

ALTER TABLE ONLY public.forma_pagamento
    ADD CONSTRAINT forma_pagamento_pkey PRIMARY KEY (codigo);


--
-- TOC entry 3208 (class 2606 OID 49892)
-- Name: investimento investimento_pkey; Type: CONSTRAINT; Schema: public; Owner: dba
--

ALTER TABLE ONLY public.investimento
    ADD CONSTRAINT investimento_pkey PRIMARY KEY (codigo);


--
-- TOC entry 3210 (class 2606 OID 49945)
-- Name: orcamento orcamento_pkey; Type: CONSTRAINT; Schema: public; Owner: dba
--

ALTER TABLE ONLY public.orcamento
    ADD CONSTRAINT orcamento_pkey PRIMARY KEY (mes_ano, cod_despesa);


--
-- TOC entry 3200 (class 2606 OID 49835)
-- Name: renda_mensal renda_mensal_pkey; Type: CONSTRAINT; Schema: public; Owner: dba
--

ALTER TABLE ONLY public.renda_mensal
    ADD CONSTRAINT renda_mensal_pkey PRIMARY KEY (cod_renda, data);


--
-- TOC entry 3198 (class 2606 OID 49830)
-- Name: renda renda_pkey; Type: CONSTRAINT; Schema: public; Owner: dba
--

ALTER TABLE ONLY public.renda
    ADD CONSTRAINT renda_pkey PRIMARY KEY (codigo);


--
-- TOC entry 3212 (class 2606 OID 49856)
-- Name: despesa cod_categoria_fkey; Type: FK CONSTRAINT; Schema: public; Owner: dba
--

ALTER TABLE ONLY public.despesa
    ADD CONSTRAINT cod_categoria_fkey FOREIGN KEY (cod_categoria) REFERENCES public.categoria(codigo);


--
-- TOC entry 3213 (class 2606 OID 49919)
-- Name: orcamento cod_despesa; Type: FK CONSTRAINT; Schema: public; Owner: dba
--

ALTER TABLE ONLY public.orcamento
    ADD CONSTRAINT cod_despesa FOREIGN KEY (cod_despesa) REFERENCES public.despesa(codigo);


--
-- TOC entry 3214 (class 2606 OID 49924)
-- Name: orcamento cod_forma_pagamento; Type: FK CONSTRAINT; Schema: public; Owner: dba
--

ALTER TABLE ONLY public.orcamento
    ADD CONSTRAINT cod_forma_pagamento FOREIGN KEY (cod_forma_pagamento) REFERENCES public.forma_pagamento(codigo);


--
-- TOC entry 3211 (class 2606 OID 49836)
-- Name: renda_mensal cod_renda_fkey; Type: FK CONSTRAINT; Schema: public; Owner: dba
--

ALTER TABLE ONLY public.renda_mensal
    ADD CONSTRAINT cod_renda_fkey FOREIGN KEY (cod_renda) REFERENCES public.renda(codigo);


-- Completed on 2023-07-10 07:27:12

--
-- PostgreSQL database dump complete
--

