--
-- PostgreSQL database dump
--

\restrict 0z4dbL8gXoVvAGLddPsTdSp7iFnbUVvvdubntb1tOZgr0xH9gXgfSDZjXjuyWV1

-- Dumped from database version 18.3
-- Dumped by pg_dump version 18.3

-- Started on 2026-04-21 18:44:27 -05

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
-- TOC entry 2 (class 3079 OID 17247)
-- Name: pgcrypto; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS pgcrypto WITH SCHEMA public;


--
-- TOC entry 3730 (class 0 OID 0)
-- Dependencies: 2
-- Name: EXTENSION pgcrypto; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION pgcrypto IS 'cryptographic functions';


--
-- TOC entry 916 (class 1247 OID 17306)
-- Name: canal_pedido; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.canal_pedido AS ENUM (
    'LOCAL',
    'DELIVERY'
);


ALTER TYPE public.canal_pedido OWNER TO postgres;

--
-- TOC entry 922 (class 1247 OID 17324)
-- Name: estado_alerta; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.estado_alerta AS ENUM (
    'ACTIVA',
    'RESUELTA',
    'IGNORADA'
);


ALTER TYPE public.estado_alerta OWNER TO postgres;

--
-- TOC entry 919 (class 1247 OID 17312)
-- Name: estado_pedido; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.estado_pedido AS ENUM (
    'PENDIENTE',
    'EN_PROCESO',
    'LISTO',
    'DESPACHADO',
    'CANCELADO'
);


ALTER TYPE public.estado_pedido OWNER TO postgres;

--
-- TOC entry 925 (class 1247 OID 17332)
-- Name: estado_producto; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.estado_producto AS ENUM (
    'ACTIVO',
    'INACTIVO',
    'DESCONTINUADO'
);


ALTER TYPE public.estado_producto OWNER TO postgres;

--
-- TOC entry 910 (class 1247 OID 17286)
-- Name: rol_usuario; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.rol_usuario AS ENUM (
    'ADMINISTRADOR',
    'OPERARIO',
    'GERENTE'
);


ALTER TYPE public.rol_usuario OWNER TO postgres;

--
-- TOC entry 913 (class 1247 OID 17294)
-- Name: tipo_movimiento; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.tipo_movimiento AS ENUM (
    'ENTRADA',
    'SALIDA',
    'AJUSTE',
    'MERMA',
    'DEVOLUCION'
);


ALTER TYPE public.tipo_movimiento OWNER TO postgres;

--
-- TOC entry 277 (class 1255 OID 17688)
-- Name: fn_actualizar_total_pedido(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.fn_actualizar_total_pedido() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    UPDATE pedidos
    SET total = (
        SELECT COALESCE(SUM(subtotal), 0)
        FROM pedido_detalle
        WHERE pedido_id = COALESCE(NEW.pedido_id, OLD.pedido_id)
    )
    WHERE id = COALESCE(NEW.pedido_id, OLD.pedido_id);
    RETURN NEW;
END;
$$;


ALTER FUNCTION public.fn_actualizar_total_pedido() OWNER TO postgres;

--
-- TOC entry 276 (class 1255 OID 17686)
-- Name: fn_alerta_stock(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.fn_alerta_stock() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    IF NEW.stock_actual <= NEW.punto_pedido AND
       NOT EXISTS (
           SELECT 1 FROM alertas_stock
           WHERE producto_id = NEW.id AND estado = 'ACTIVA'
       )
    THEN
        INSERT INTO alertas_stock (producto_id, stock_al_generar, punto_pedido_ref)
        VALUES (NEW.id, NEW.stock_actual, NEW.punto_pedido);
    END IF;
    RETURN NEW;
END;
$$;


ALTER FUNCTION public.fn_alerta_stock() OWNER TO postgres;

--
-- TOC entry 275 (class 1255 OID 17683)
-- Name: fn_set_updated_at(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.fn_set_updated_at() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN NEW.updated_at = NOW(); RETURN NEW; END;
$$;


ALTER FUNCTION public.fn_set_updated_at() OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 228 (class 1259 OID 17494)
-- Name: alertas_stock; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.alertas_stock (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    producto_id uuid NOT NULL,
    stock_al_generar integer NOT NULL,
    punto_pedido_ref integer NOT NULL,
    estado public.estado_alerta DEFAULT 'ACTIVA'::public.estado_alerta NOT NULL,
    resuelta_por uuid,
    fecha_generada timestamp without time zone DEFAULT now() NOT NULL,
    fecha_resuelta timestamp without time zone
);


ALTER TABLE public.alertas_stock OWNER TO postgres;

--
-- TOC entry 235 (class 1259 OID 17605)
-- Name: auditoria; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.auditoria (
    id bigint NOT NULL,
    usuario_id uuid,
    accion character varying(100) NOT NULL,
    tabla character varying(100),
    registro_id character varying(100),
    detalle jsonb,
    ip character varying(45),
    fecha timestamp without time zone DEFAULT now() NOT NULL
);


ALTER TABLE public.auditoria OWNER TO postgres;

--
-- TOC entry 234 (class 1259 OID 17604)
-- Name: auditoria_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.auditoria_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.auditoria_id_seq OWNER TO postgres;

--
-- TOC entry 3731 (class 0 OID 0)
-- Dependencies: 234
-- Name: auditoria_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.auditoria_id_seq OWNED BY public.auditoria.id;


--
-- TOC entry 221 (class 1259 OID 17340)
-- Name: categorias; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.categorias (
    id integer NOT NULL,
    nombre character varying(120) NOT NULL,
    descripcion text,
    padre_id integer,
    activa boolean DEFAULT true NOT NULL
);


ALTER TABLE public.categorias OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 17339)
-- Name: categorias_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.categorias_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.categorias_id_seq OWNER TO postgres;

--
-- TOC entry 3732 (class 0 OID 0)
-- Dependencies: 220
-- Name: categorias_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.categorias_id_seq OWNED BY public.categorias.id;


--
-- TOC entry 233 (class 1259 OID 17593)
-- Name: configuracion; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.configuracion (
    clave character varying(100) NOT NULL,
    valor text NOT NULL,
    descripcion text,
    updated_at timestamp without time zone DEFAULT now() NOT NULL
);


ALTER TABLE public.configuracion OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 17468)
-- Name: inventario_movimientos; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.inventario_movimientos (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    producto_id uuid NOT NULL,
    usuario_id uuid NOT NULL,
    tipo public.tipo_movimiento NOT NULL,
    cantidad integer NOT NULL,
    stock_antes integer NOT NULL,
    stock_despues integer NOT NULL,
    costo_unitario numeric(10,2),
    motivo character varying(300),
    referencia character varying(100),
    fecha timestamp without time zone DEFAULT now() NOT NULL,
    CONSTRAINT inventario_movimientos_cantidad_check CHECK ((cantidad > 0))
);


ALTER TABLE public.inventario_movimientos OWNER TO postgres;

--
-- TOC entry 236 (class 1259 OID 17622)
-- Name: notificaciones; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.notificaciones (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    usuario_id uuid NOT NULL,
    titulo character varying(200) NOT NULL,
    mensaje text NOT NULL,
    leida boolean DEFAULT false NOT NULL,
    tipo character varying(50) DEFAULT 'INFO'::character varying NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL
);


ALTER TABLE public.notificaciones OWNER TO postgres;

--
-- TOC entry 231 (class 1259 OID 17548)
-- Name: pedido_detalle; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pedido_detalle (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    pedido_id uuid NOT NULL,
    producto_id uuid NOT NULL,
    cantidad integer NOT NULL,
    precio_unitario numeric(10,2) NOT NULL,
    subtotal numeric(12,2) GENERATED ALWAYS AS (((cantidad)::numeric * precio_unitario)) STORED,
    CONSTRAINT pedido_detalle_cantidad_check CHECK ((cantidad > 0))
);


ALTER TABLE public.pedido_detalle OWNER TO postgres;

--
-- TOC entry 230 (class 1259 OID 17519)
-- Name: pedidos; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pedidos (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    numero integer NOT NULL,
    usuario_id uuid NOT NULL,
    canal public.canal_pedido NOT NULL,
    estado public.estado_pedido DEFAULT 'PENDIENTE'::public.estado_pedido NOT NULL,
    prioridad smallint DEFAULT 5 NOT NULL,
    cliente_nombre character varying(200),
    cliente_telefono character varying(20),
    cliente_dir text,
    observaciones text,
    total numeric(12,2) DEFAULT 0 NOT NULL,
    fecha_ingreso timestamp without time zone DEFAULT now() NOT NULL,
    fecha_despacho timestamp without time zone,
    CONSTRAINT pedidos_prioridad_check CHECK (((prioridad >= 1) AND (prioridad <= 10)))
);


ALTER TABLE public.pedidos OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 17518)
-- Name: pedidos_numero_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.pedidos_numero_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.pedidos_numero_seq OWNER TO postgres;

--
-- TOC entry 3733 (class 0 OID 0)
-- Dependencies: 229
-- Name: pedidos_numero_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.pedidos_numero_seq OWNED BY public.pedidos.numero;


--
-- TOC entry 232 (class 1259 OID 17571)
-- Name: predicciones_demanda; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.predicciones_demanda (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    producto_id uuid NOT NULL,
    semana_inicio date NOT NULL,
    semana_fin date NOT NULL,
    cantidad_predicha integer NOT NULL,
    cantidad_real integer,
    error_porcentaje numeric(6,2),
    confianza numeric(5,2),
    modelo_version character varying(50) DEFAULT 'v1.0-linreg'::character varying NOT NULL,
    generado_en timestamp without time zone DEFAULT now() NOT NULL
);


ALTER TABLE public.predicciones_demanda OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 17421)
-- Name: productos; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.productos (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    sku character varying(50),
    codigo_barras character varying(50),
    nombre character varying(250) NOT NULL,
    descripcion text,
    marca character varying(100),
    unidad_medida character varying(30) DEFAULT 'UNIDAD'::character varying NOT NULL,
    categoria_id integer NOT NULL,
    proveedor_id integer NOT NULL,
    precio_costo numeric(10,2) NOT NULL,
    precio_venta numeric(10,2) NOT NULL,
    stock_actual integer DEFAULT 0 NOT NULL,
    stock_minimo integer DEFAULT 10 NOT NULL,
    stock_maximo integer DEFAULT 500 NOT NULL,
    punto_pedido integer DEFAULT 30 NOT NULL,
    estado public.estado_producto DEFAULT 'ACTIVO'::public.estado_producto NOT NULL,
    imagen_url text,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    updated_at timestamp without time zone DEFAULT now() NOT NULL,
    CONSTRAINT productos_precio_costo_check CHECK ((precio_costo >= (0)::numeric)),
    CONSTRAINT productos_precio_venta_check CHECK ((precio_venta >= (0)::numeric)),
    CONSTRAINT productos_stock_actual_check CHECK ((stock_actual >= 0))
);


ALTER TABLE public.productos OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 17360)
-- Name: proveedores; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.proveedores (
    id integer NOT NULL,
    nombre character varying(200) NOT NULL,
    ruc character varying(11),
    contacto character varying(150),
    telefono character varying(20),
    email character varying(150),
    direccion text,
    lead_time_dias integer DEFAULT 3 NOT NULL,
    activo boolean DEFAULT true NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL
);


ALTER TABLE public.proveedores OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 17359)
-- Name: proveedores_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.proveedores_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.proveedores_id_seq OWNER TO postgres;

--
-- TOC entry 3734 (class 0 OID 0)
-- Dependencies: 222
-- Name: proveedores_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.proveedores_id_seq OWNED BY public.proveedores.id;


--
-- TOC entry 237 (class 1259 OID 17645)
-- Name: reportes; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.reportes (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    usuario_id uuid NOT NULL,
    tipo character varying(80) NOT NULL,
    formato character varying(10) NOT NULL,
    parametros jsonb,
    ruta_archivo text,
    created_at timestamp without time zone DEFAULT now() NOT NULL
);


ALTER TABLE public.reportes OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 17400)
-- Name: sesiones; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sesiones (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    usuario_id uuid NOT NULL,
    token_hash character varying(255) NOT NULL,
    expira_en timestamp without time zone NOT NULL,
    revocado boolean DEFAULT false NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL
);


ALTER TABLE public.sesiones OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 17378)
-- Name: usuarios; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.usuarios (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    nombre character varying(150) NOT NULL,
    apellido character varying(150) NOT NULL,
    email character varying(150) NOT NULL,
    password_hash character varying(255) DEFAULT 'PENDING_HASH'::character varying NOT NULL,
    rol public.rol_usuario DEFAULT 'OPERARIO'::public.rol_usuario NOT NULL,
    activo boolean DEFAULT true NOT NULL,
    ultimo_login timestamp without time zone,
    created_at timestamp without time zone DEFAULT now() NOT NULL
);


ALTER TABLE public.usuarios OWNER TO postgres;

--
-- TOC entry 3482 (class 2604 OID 17608)
-- Name: auditoria id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.auditoria ALTER COLUMN id SET DEFAULT nextval('public.auditoria_id_seq'::regclass);


--
-- TOC entry 3442 (class 2604 OID 17343)
-- Name: categorias id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categorias ALTER COLUMN id SET DEFAULT nextval('public.categorias_id_seq'::regclass);


--
-- TOC entry 3471 (class 2604 OID 17523)
-- Name: pedidos numero; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pedidos ALTER COLUMN numero SET DEFAULT nextval('public.pedidos_numero_seq'::regclass);


--
-- TOC entry 3444 (class 2604 OID 17363)
-- Name: proveedores id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.proveedores ALTER COLUMN id SET DEFAULT nextval('public.proveedores_id_seq'::regclass);


--
-- TOC entry 3529 (class 2606 OID 17507)
-- Name: alertas_stock alertas_stock_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.alertas_stock
    ADD CONSTRAINT alertas_stock_pkey PRIMARY KEY (id);


--
-- TOC entry 3551 (class 2606 OID 17616)
-- Name: auditoria auditoria_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.auditoria
    ADD CONSTRAINT auditoria_pkey PRIMARY KEY (id);


--
-- TOC entry 3497 (class 2606 OID 17353)
-- Name: categorias categorias_nombre_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categorias
    ADD CONSTRAINT categorias_nombre_key UNIQUE (nombre);


--
-- TOC entry 3499 (class 2606 OID 17351)
-- Name: categorias categorias_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categorias
    ADD CONSTRAINT categorias_pkey PRIMARY KEY (id);


--
-- TOC entry 3549 (class 2606 OID 17603)
-- Name: configuracion configuracion_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.configuracion
    ADD CONSTRAINT configuracion_pkey PRIMARY KEY (clave);


--
-- TOC entry 3527 (class 2606 OID 17483)
-- Name: inventario_movimientos inventario_movimientos_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.inventario_movimientos
    ADD CONSTRAINT inventario_movimientos_pkey PRIMARY KEY (id);


--
-- TOC entry 3556 (class 2606 OID 17639)
-- Name: notificaciones notificaciones_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.notificaciones
    ADD CONSTRAINT notificaciones_pkey PRIMARY KEY (id);


--
-- TOC entry 3541 (class 2606 OID 17560)
-- Name: pedido_detalle pedido_detalle_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pedido_detalle
    ADD CONSTRAINT pedido_detalle_pkey PRIMARY KEY (id);


--
-- TOC entry 3537 (class 2606 OID 17542)
-- Name: pedidos pedidos_numero_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pedidos
    ADD CONSTRAINT pedidos_numero_key UNIQUE (numero);


--
-- TOC entry 3539 (class 2606 OID 17540)
-- Name: pedidos pedidos_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pedidos
    ADD CONSTRAINT pedidos_pkey PRIMARY KEY (id);


--
-- TOC entry 3545 (class 2606 OID 17585)
-- Name: predicciones_demanda predicciones_demanda_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.predicciones_demanda
    ADD CONSTRAINT predicciones_demanda_pkey PRIMARY KEY (id);


--
-- TOC entry 3547 (class 2606 OID 17587)
-- Name: predicciones_demanda predicciones_demanda_producto_id_semana_inicio_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.predicciones_demanda
    ADD CONSTRAINT predicciones_demanda_producto_id_semana_inicio_key UNIQUE (producto_id, semana_inicio);


--
-- TOC entry 3518 (class 2606 OID 17457)
-- Name: productos productos_codigo_barras_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.productos
    ADD CONSTRAINT productos_codigo_barras_key UNIQUE (codigo_barras);


--
-- TOC entry 3520 (class 2606 OID 17453)
-- Name: productos productos_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.productos
    ADD CONSTRAINT productos_pkey PRIMARY KEY (id);


--
-- TOC entry 3522 (class 2606 OID 17455)
-- Name: productos productos_sku_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.productos
    ADD CONSTRAINT productos_sku_key UNIQUE (sku);


--
-- TOC entry 3501 (class 2606 OID 17375)
-- Name: proveedores proveedores_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.proveedores
    ADD CONSTRAINT proveedores_pkey PRIMARY KEY (id);


--
-- TOC entry 3503 (class 2606 OID 17377)
-- Name: proveedores proveedores_ruc_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.proveedores
    ADD CONSTRAINT proveedores_ruc_key UNIQUE (ruc);


--
-- TOC entry 3558 (class 2606 OID 17658)
-- Name: reportes reportes_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reportes
    ADD CONSTRAINT reportes_pkey PRIMARY KEY (id);


--
-- TOC entry 3510 (class 2606 OID 17413)
-- Name: sesiones sesiones_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sesiones
    ADD CONSTRAINT sesiones_pkey PRIMARY KEY (id);


--
-- TOC entry 3512 (class 2606 OID 17415)
-- Name: sesiones sesiones_token_hash_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sesiones
    ADD CONSTRAINT sesiones_token_hash_key UNIQUE (token_hash);


--
-- TOC entry 3505 (class 2606 OID 17399)
-- Name: usuarios usuarios_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_email_key UNIQUE (email);


--
-- TOC entry 3507 (class 2606 OID 17397)
-- Name: usuarios usuarios_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_pkey PRIMARY KEY (id);


--
-- TOC entry 3530 (class 1259 OID 17671)
-- Name: idx_alertas_estado; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_alertas_estado ON public.alertas_stock USING btree (estado);


--
-- TOC entry 3531 (class 1259 OID 17672)
-- Name: idx_alertas_producto; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_alertas_producto ON public.alertas_stock USING btree (producto_id);


--
-- TOC entry 3552 (class 1259 OID 17680)
-- Name: idx_auditoria_fecha; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_auditoria_fecha ON public.auditoria USING btree (fecha DESC);


--
-- TOC entry 3553 (class 1259 OID 17679)
-- Name: idx_auditoria_usuario; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_auditoria_usuario ON public.auditoria USING btree (usuario_id);


--
-- TOC entry 3523 (class 1259 OID 17669)
-- Name: idx_mov_fecha; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_mov_fecha ON public.inventario_movimientos USING btree (fecha DESC);


--
-- TOC entry 3524 (class 1259 OID 17668)
-- Name: idx_mov_producto; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_mov_producto ON public.inventario_movimientos USING btree (producto_id);


--
-- TOC entry 3525 (class 1259 OID 17670)
-- Name: idx_mov_tipo; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_mov_tipo ON public.inventario_movimientos USING btree (tipo);


--
-- TOC entry 3554 (class 1259 OID 17682)
-- Name: idx_notif_usuario; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_notif_usuario ON public.notificaciones USING btree (usuario_id, leida);


--
-- TOC entry 3532 (class 1259 OID 17674)
-- Name: idx_pedidos_canal; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_pedidos_canal ON public.pedidos USING btree (canal);


--
-- TOC entry 3533 (class 1259 OID 17673)
-- Name: idx_pedidos_estado; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_pedidos_estado ON public.pedidos USING btree (estado);


--
-- TOC entry 3534 (class 1259 OID 17675)
-- Name: idx_pedidos_fecha; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_pedidos_fecha ON public.pedidos USING btree (fecha_ingreso DESC);


--
-- TOC entry 3535 (class 1259 OID 17676)
-- Name: idx_pedidos_prioridad; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_pedidos_prioridad ON public.pedidos USING btree (prioridad);


--
-- TOC entry 3542 (class 1259 OID 17677)
-- Name: idx_pred_producto; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_pred_producto ON public.predicciones_demanda USING btree (producto_id);


--
-- TOC entry 3543 (class 1259 OID 17678)
-- Name: idx_pred_semana; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_pred_semana ON public.predicciones_demanda USING btree (semana_inicio DESC);


--
-- TOC entry 3513 (class 1259 OID 17664)
-- Name: idx_productos_categoria; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_productos_categoria ON public.productos USING btree (categoria_id);


--
-- TOC entry 3514 (class 1259 OID 17666)
-- Name: idx_productos_estado; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_productos_estado ON public.productos USING btree (estado);


--
-- TOC entry 3515 (class 1259 OID 17665)
-- Name: idx_productos_proveedor; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_productos_proveedor ON public.productos USING btree (proveedor_id);


--
-- TOC entry 3516 (class 1259 OID 17667)
-- Name: idx_productos_sku; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_productos_sku ON public.productos USING btree (sku);


--
-- TOC entry 3508 (class 1259 OID 17681)
-- Name: idx_sesiones_usuario; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_sesiones_usuario ON public.sesiones USING btree (usuario_id);


--
-- TOC entry 3574 (class 2620 OID 17687)
-- Name: productos trg_alerta_stock; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trg_alerta_stock AFTER UPDATE OF stock_actual ON public.productos FOR EACH ROW EXECUTE FUNCTION public.fn_alerta_stock();


--
-- TOC entry 3577 (class 2620 OID 17685)
-- Name: configuracion trg_config_updated_at; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trg_config_updated_at BEFORE UPDATE ON public.configuracion FOR EACH ROW EXECUTE FUNCTION public.fn_set_updated_at();


--
-- TOC entry 3575 (class 2620 OID 17684)
-- Name: productos trg_productos_updated_at; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trg_productos_updated_at BEFORE UPDATE ON public.productos FOR EACH ROW EXECUTE FUNCTION public.fn_set_updated_at();


--
-- TOC entry 3576 (class 2620 OID 17689)
-- Name: pedido_detalle trg_total_pedido; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trg_total_pedido AFTER INSERT OR DELETE OR UPDATE ON public.pedido_detalle FOR EACH ROW EXECUTE FUNCTION public.fn_actualizar_total_pedido();


--
-- TOC entry 3565 (class 2606 OID 17508)
-- Name: alertas_stock alertas_stock_producto_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.alertas_stock
    ADD CONSTRAINT alertas_stock_producto_id_fkey FOREIGN KEY (producto_id) REFERENCES public.productos(id);


--
-- TOC entry 3566 (class 2606 OID 17513)
-- Name: alertas_stock alertas_stock_resuelta_por_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.alertas_stock
    ADD CONSTRAINT alertas_stock_resuelta_por_fkey FOREIGN KEY (resuelta_por) REFERENCES public.usuarios(id);


--
-- TOC entry 3571 (class 2606 OID 17617)
-- Name: auditoria auditoria_usuario_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.auditoria
    ADD CONSTRAINT auditoria_usuario_id_fkey FOREIGN KEY (usuario_id) REFERENCES public.usuarios(id);


--
-- TOC entry 3559 (class 2606 OID 17354)
-- Name: categorias categorias_padre_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categorias
    ADD CONSTRAINT categorias_padre_id_fkey FOREIGN KEY (padre_id) REFERENCES public.categorias(id);


--
-- TOC entry 3563 (class 2606 OID 17484)
-- Name: inventario_movimientos inventario_movimientos_producto_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.inventario_movimientos
    ADD CONSTRAINT inventario_movimientos_producto_id_fkey FOREIGN KEY (producto_id) REFERENCES public.productos(id);


--
-- TOC entry 3564 (class 2606 OID 17489)
-- Name: inventario_movimientos inventario_movimientos_usuario_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.inventario_movimientos
    ADD CONSTRAINT inventario_movimientos_usuario_id_fkey FOREIGN KEY (usuario_id) REFERENCES public.usuarios(id);


--
-- TOC entry 3572 (class 2606 OID 17640)
-- Name: notificaciones notificaciones_usuario_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.notificaciones
    ADD CONSTRAINT notificaciones_usuario_id_fkey FOREIGN KEY (usuario_id) REFERENCES public.usuarios(id) ON DELETE CASCADE;


--
-- TOC entry 3568 (class 2606 OID 17561)
-- Name: pedido_detalle pedido_detalle_pedido_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pedido_detalle
    ADD CONSTRAINT pedido_detalle_pedido_id_fkey FOREIGN KEY (pedido_id) REFERENCES public.pedidos(id) ON DELETE CASCADE;


--
-- TOC entry 3569 (class 2606 OID 17566)
-- Name: pedido_detalle pedido_detalle_producto_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pedido_detalle
    ADD CONSTRAINT pedido_detalle_producto_id_fkey FOREIGN KEY (producto_id) REFERENCES public.productos(id);


--
-- TOC entry 3567 (class 2606 OID 17543)
-- Name: pedidos pedidos_usuario_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pedidos
    ADD CONSTRAINT pedidos_usuario_id_fkey FOREIGN KEY (usuario_id) REFERENCES public.usuarios(id);


--
-- TOC entry 3570 (class 2606 OID 17588)
-- Name: predicciones_demanda predicciones_demanda_producto_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.predicciones_demanda
    ADD CONSTRAINT predicciones_demanda_producto_id_fkey FOREIGN KEY (producto_id) REFERENCES public.productos(id);


--
-- TOC entry 3561 (class 2606 OID 17458)
-- Name: productos productos_categoria_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.productos
    ADD CONSTRAINT productos_categoria_id_fkey FOREIGN KEY (categoria_id) REFERENCES public.categorias(id);


--
-- TOC entry 3562 (class 2606 OID 17463)
-- Name: productos productos_proveedor_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.productos
    ADD CONSTRAINT productos_proveedor_id_fkey FOREIGN KEY (proveedor_id) REFERENCES public.proveedores(id);


--
-- TOC entry 3573 (class 2606 OID 17659)
-- Name: reportes reportes_usuario_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reportes
    ADD CONSTRAINT reportes_usuario_id_fkey FOREIGN KEY (usuario_id) REFERENCES public.usuarios(id);


--
-- TOC entry 3560 (class 2606 OID 17416)
-- Name: sesiones sesiones_usuario_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sesiones
    ADD CONSTRAINT sesiones_usuario_id_fkey FOREIGN KEY (usuario_id) REFERENCES public.usuarios(id) ON DELETE CASCADE;


-- Completed on 2026-04-21 18:44:27 -05

--
-- PostgreSQL database dump complete
--

\unrestrict 0z4dbL8gXoVvAGLddPsTdSp7iFnbUVvvdubntb1tOZgr0xH9gXgfSDZjXjuyWV1

