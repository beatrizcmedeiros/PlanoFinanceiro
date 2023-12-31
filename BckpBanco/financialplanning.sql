PGDMP         !        
        {            financialplanning    14.8    14.8 '    (           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            )           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            *           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            +           1262    49799    financialplanning    DATABASE     q   CREATE DATABASE financialplanning WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'Portuguese_Brazil.1252';
 !   DROP DATABASE financialplanning;
                dba    false            �            1259    49897    categoria_id_seq    SEQUENCE     y   CREATE SEQUENCE public.categoria_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 '   DROP SEQUENCE public.categoria_id_seq;
       public          postgres    false            �            1259    49846 	   categoria    TABLE     �   CREATE TABLE public.categoria (
    codigo bigint DEFAULT nextval('public.categoria_id_seq'::regclass) NOT NULL,
    descricao character varying(255) NOT NULL
);
    DROP TABLE public.categoria;
       public         heap    dba    false    217            �            1259    49898    despesa_id_seq    SEQUENCE     w   CREATE SEQUENCE public.despesa_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 %   DROP SEQUENCE public.despesa_id_seq;
       public          postgres    false            �            1259    49851    despesa    TABLE     �   CREATE TABLE public.despesa (
    codigo bigint DEFAULT nextval('public.despesa_id_seq'::regclass) NOT NULL,
    descricao character varying(255) NOT NULL,
    cod_categoria bigint NOT NULL
);
    DROP TABLE public.despesa;
       public         heap    dba    false    218            �            1259    49894    forma_pagamento_id_seq    SEQUENCE        CREATE SEQUENCE public.forma_pagamento_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 -   DROP SEQUENCE public.forma_pagamento_id_seq;
       public          postgres    false            �            1259    49841    forma_pagamento    TABLE     �   CREATE TABLE public.forma_pagamento (
    codigo bigint DEFAULT nextval('public.forma_pagamento_id_seq'::regclass) NOT NULL,
    descricao character varying(255) NOT NULL
);
 #   DROP TABLE public.forma_pagamento;
       public         heap    dba    false    216            �            1259    49899    investimento_id_seq    SEQUENCE     |   CREATE SEQUENCE public.investimento_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 *   DROP SEQUENCE public.investimento_id_seq;
       public          postgres    false            �            1259    49886    investimento    TABLE     �  CREATE TABLE public.investimento (
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
     DROP TABLE public.investimento;
       public         heap    dba    false    219            �            1259    49914 	   orcamento    TABLE     !  CREATE TABLE public.orcamento (
    mes_ano character varying(7) NOT NULL,
    cod_despesa bigint NOT NULL,
    data_despesa date NOT NULL,
    data_pagamento date NOT NULL,
    cod_forma_pagamento bigint NOT NULL,
    valor double precision NOT NULL,
    situacao character varying(4)
);
    DROP TABLE public.orcamento;
       public         heap    dba    false            �            1259    49893    renda_id_seq    SEQUENCE     u   CREATE SEQUENCE public.renda_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 #   DROP SEQUENCE public.renda_id_seq;
       public          postgres    false            �            1259    49826    renda    TABLE     �   CREATE TABLE public.renda (
    codigo bigint DEFAULT nextval('public.renda_id_seq'::regclass) NOT NULL,
    descricao character varying(255) NOT NULL
);
    DROP TABLE public.renda;
       public         heap    dba    false    215            �            1259    49831    renda_mensal    TABLE     �   CREATE TABLE public.renda_mensal (
    cod_renda bigint NOT NULL,
    data date NOT NULL,
    valor double precision NOT NULL
);
     DROP TABLE public.renda_mensal;
       public         heap    dba    false                      0    49846 	   categoria 
   TABLE DATA           6   COPY public.categoria (codigo, descricao) FROM stdin;
    public          dba    false    212   �,                 0    49851    despesa 
   TABLE DATA           C   COPY public.despesa (codigo, descricao, cod_categoria) FROM stdin;
    public          dba    false    213   -                 0    49841    forma_pagamento 
   TABLE DATA           <   COPY public.forma_pagamento (codigo, descricao) FROM stdin;
    public          dba    false    211   �-                 0    49886    investimento 
   TABLE DATA           �   COPY public.investimento (codigo, objetivo, estrategia, nome, valor_investido, posicao, rendimento_bruto, rentabilidade, vencimento) FROM stdin;
    public          dba    false    214   �-       %          0    49914 	   orcamento 
   TABLE DATA           }   COPY public.orcamento (mes_ano, cod_despesa, data_despesa, data_pagamento, cod_forma_pagamento, valor, situacao) FROM stdin;
    public          dba    false    220   �/                 0    49826    renda 
   TABLE DATA           2   COPY public.renda (codigo, descricao) FROM stdin;
    public          dba    false    209   �0                 0    49831    renda_mensal 
   TABLE DATA           >   COPY public.renda_mensal (cod_renda, data, valor) FROM stdin;
    public          dba    false    210   51       ,           0    0    categoria_id_seq    SEQUENCE SET     >   SELECT pg_catalog.setval('public.categoria_id_seq', 8, true);
          public          postgres    false    217            -           0    0    despesa_id_seq    SEQUENCE SET     =   SELECT pg_catalog.setval('public.despesa_id_seq', 12, true);
          public          postgres    false    218            .           0    0    forma_pagamento_id_seq    SEQUENCE SET     D   SELECT pg_catalog.setval('public.forma_pagamento_id_seq', 6, true);
          public          postgres    false    216            /           0    0    investimento_id_seq    SEQUENCE SET     B   SELECT pg_catalog.setval('public.investimento_id_seq', 11, true);
          public          postgres    false    219            0           0    0    renda_id_seq    SEQUENCE SET     ;   SELECT pg_catalog.setval('public.renda_id_seq', 11, true);
          public          postgres    false    215            �           2606    49850    categoria categoria_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.categoria
    ADD CONSTRAINT categoria_pkey PRIMARY KEY (codigo);
 B   ALTER TABLE ONLY public.categoria DROP CONSTRAINT categoria_pkey;
       public            dba    false    212            �           2606    49855    despesa despesa_pkey 
   CONSTRAINT     V   ALTER TABLE ONLY public.despesa
    ADD CONSTRAINT despesa_pkey PRIMARY KEY (codigo);
 >   ALTER TABLE ONLY public.despesa DROP CONSTRAINT despesa_pkey;
       public            dba    false    213            �           2606    49845 $   forma_pagamento forma_pagamento_pkey 
   CONSTRAINT     f   ALTER TABLE ONLY public.forma_pagamento
    ADD CONSTRAINT forma_pagamento_pkey PRIMARY KEY (codigo);
 N   ALTER TABLE ONLY public.forma_pagamento DROP CONSTRAINT forma_pagamento_pkey;
       public            dba    false    211            �           2606    49892    investimento investimento_pkey 
   CONSTRAINT     `   ALTER TABLE ONLY public.investimento
    ADD CONSTRAINT investimento_pkey PRIMARY KEY (codigo);
 H   ALTER TABLE ONLY public.investimento DROP CONSTRAINT investimento_pkey;
       public            dba    false    214            �           2606    49945    orcamento orcamento_pkey 
   CONSTRAINT     h   ALTER TABLE ONLY public.orcamento
    ADD CONSTRAINT orcamento_pkey PRIMARY KEY (mes_ano, cod_despesa);
 B   ALTER TABLE ONLY public.orcamento DROP CONSTRAINT orcamento_pkey;
       public            dba    false    220    220            �           2606    49835    renda_mensal renda_mensal_pkey 
   CONSTRAINT     i   ALTER TABLE ONLY public.renda_mensal
    ADD CONSTRAINT renda_mensal_pkey PRIMARY KEY (cod_renda, data);
 H   ALTER TABLE ONLY public.renda_mensal DROP CONSTRAINT renda_mensal_pkey;
       public            dba    false    210    210            ~           2606    49830    renda renda_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.renda
    ADD CONSTRAINT renda_pkey PRIMARY KEY (codigo);
 :   ALTER TABLE ONLY public.renda DROP CONSTRAINT renda_pkey;
       public            dba    false    209            �           2606    49856    despesa cod_categoria_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.despesa
    ADD CONSTRAINT cod_categoria_fkey FOREIGN KEY (cod_categoria) REFERENCES public.categoria(codigo);
 D   ALTER TABLE ONLY public.despesa DROP CONSTRAINT cod_categoria_fkey;
       public          dba    false    212    213    3204            �           2606    49919    orcamento cod_despesa    FK CONSTRAINT     ~   ALTER TABLE ONLY public.orcamento
    ADD CONSTRAINT cod_despesa FOREIGN KEY (cod_despesa) REFERENCES public.despesa(codigo);
 ?   ALTER TABLE ONLY public.orcamento DROP CONSTRAINT cod_despesa;
       public          dba    false    220    3206    213            �           2606    49924    orcamento cod_forma_pagamento    FK CONSTRAINT     �   ALTER TABLE ONLY public.orcamento
    ADD CONSTRAINT cod_forma_pagamento FOREIGN KEY (cod_forma_pagamento) REFERENCES public.forma_pagamento(codigo);
 G   ALTER TABLE ONLY public.orcamento DROP CONSTRAINT cod_forma_pagamento;
       public          dba    false    3202    211    220            �           2606    49836    renda_mensal cod_renda_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.renda_mensal
    ADD CONSTRAINT cod_renda_fkey FOREIGN KEY (cod_renda) REFERENCES public.renda(codigo);
 E   ALTER TABLE ONLY public.renda_mensal DROP CONSTRAINT cod_renda_fkey;
       public          dba    false    209    3198    210               Z   x�3��I�J-�2�t,-��=��,5�˄�1'375�$���Ë�L9�S��2/�/�2���/JL�L�2�t�*)��N<�+%�+F��� �*@         �   x�]ʽ�0���)� ��/e�"D-�)�"K��.�P��bqѽ�Y\]���T����]d���![��������R���%�GG�_��r��g��љ�k��`������ߤnf�d,�2$8�@���v�b0�         <   x�3�tv�2�tv�2�t���H�,��2�)J�+NK-:�*/93�ˌ�)?'�$�+F��� |��         �  x���ˎ�0��'O�p��8�e/S)� 肍'q;G�ؕ�V��A,lG<A^��:�Y�XNt>��s|Fu�l�gO��MuV�%y���F��2o�����\¼d��?S�)ϓVv8:刱�~��	�O���vmD�~�ΐ��z$���-e��,�l^�e��Q.(�I���ڌ�P���3v�Ӣ"[�g���r�k���\�z-*e�X"as2�%�&:<\;����,Y�ӣ�m?�[��(ydE1cN���%�m�V9g�6�"��ޭ�T��dU$TY(�'-c&!)��`I	w~<u��b�5��a={MӦAO��Eg��"6V��~5�ի���j�p� e���M���|��U�'�`���Ðn�4K�/��*^�YIEy���]wjU�g�({�C���ט=U�A�"�L�%g�ro1�X�Y;�{�Ozq7��֤�����rq���,�,O��I��$���      %   �   x�e�K!D�x(~�D*�ͬr�D"Q'���zմ�GĘ �>��|�@2�����ش�2%��3Np<���b�ɪ�51_춸��:	"_�6е�d�H}N�l��������v�P��#*�E�Z���^���,�{a���-�fZ�a��u�]��~�Gp�} �V�         �   x�M�;�@D��)rD����4�5��&�l�("q��N��i��if�!?#��N�b�hH�a������D)v�{�����xl;�=���
�(�ڜ5���5����b՟m�)l�]M�Z�أ'(�&��b�F�I�m��T���Jd�� >u�I�         _   x�=���0�3ޅȘд�t�9~~E+q�d��tN�D�Z{,Fb6�G�*����Q8҃�EXr.+g�z.p}��'����i���Z�vm����_     