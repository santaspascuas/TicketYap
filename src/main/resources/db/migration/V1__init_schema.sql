CREATE TABLE IF NOT EXISTS public.barcode_registro
(
    id serial NOT NULL,
    hash_barcode character varying(64) NOT NULL,
    contenido character varying(500),
    id_entrada_tmp integer NOT NULL,
    id_usuario integer NOT NULL,
    fecha_registro timestamp without time zone DEFAULT now(),
    CONSTRAINT barcode_registro_pkey PRIMARY KEY (id),
    CONSTRAINT barcode_registro_hash_key UNIQUE (hash_barcode)
);

CREATE TABLE IF NOT EXISTS public.batch_job_instance
(
    job_instance_id bigint NOT NULL,
    version bigint,
    job_name character varying(100) NOT NULL,
    job_key character varying(32) NOT NULL,
    CONSTRAINT batch_job_instance_pkey PRIMARY KEY (job_instance_id),
    CONSTRAINT job_inst_un UNIQUE (job_name, job_key)
);

CREATE TABLE IF NOT EXISTS public.batch_job_execution
(
    job_execution_id bigint NOT NULL,
    version bigint,
    job_instance_id bigint NOT NULL,
    create_time timestamp without time zone NOT NULL,
    start_time timestamp without time zone,
    end_time timestamp without time zone,
    status character varying(10),
    exit_code character varying(2500),
    exit_message character varying(2500),
    last_updated timestamp without time zone,
    CONSTRAINT batch_job_execution_pkey PRIMARY KEY (job_execution_id)
);

CREATE TABLE IF NOT EXISTS public.batch_job_execution_context
(
    job_execution_id bigint NOT NULL,
    short_context character varying(2500) NOT NULL,
    serialized_context text,
    CONSTRAINT batch_job_execution_context_pkey PRIMARY KEY (job_execution_id)
);

CREATE TABLE IF NOT EXISTS public.batch_job_execution_params
(
    job_execution_id bigint NOT NULL,
    parameter_name character varying(100) NOT NULL,
    parameter_type character varying(100) NOT NULL,
    parameter_value character varying(2500),
    identifying character(1) NOT NULL
);

CREATE TABLE IF NOT EXISTS public.batch_step_execution
(
    step_execution_id bigint NOT NULL,
    version bigint NOT NULL,
    step_name character varying(100) NOT NULL,
    job_execution_id bigint NOT NULL,
    create_time timestamp without time zone NOT NULL,
    start_time timestamp without time zone,
    end_time timestamp without time zone,
    status character varying(10),
    commit_count bigint,
    read_count bigint,
    filter_count bigint,
    write_count bigint,
    read_skip_count bigint,
    write_skip_count bigint,
    process_skip_count bigint,
    rollback_count bigint,
    exit_code character varying(2500),
    exit_message character varying(2500),
    last_updated timestamp without time zone,
    CONSTRAINT batch_step_execution_pkey PRIMARY KEY (step_execution_id)
);

CREATE TABLE IF NOT EXISTS public.batch_step_execution_context
(
    step_execution_id bigint NOT NULL,
    short_context character varying(2500) NOT NULL,
    serialized_context text,
    CONSTRAINT batch_step_execution_context_pkey PRIMARY KEY (step_execution_id)
);

CREATE TABLE IF NOT EXISTS public.carrito
(
    id_carrito serial NOT NULL,
    id_usuario integer NOT NULL,
    fecha_agregado timestamp without time zone NOT NULL DEFAULT now(),
    fecha_modificado timestamp without time zone NOT NULL DEFAULT now(),
    estado character varying(20) NOT NULL DEFAULT 'ACTIVO',
    CONSTRAINT carrito_pkey PRIMARY KEY (id_carrito)
);

CREATE TABLE IF NOT EXISTS public.carrito_item
(
    id_item serial NOT NULL,
    id_carrito integer NOT NULL,
    id_entrada integer NOT NULL,
    fecha_agregado timestamp without time zone NOT NULL DEFAULT now(),
    CONSTRAINT carrito_item_pkey PRIMARY KEY (id_item),
    CONSTRAINT carrito_item_id_carrito_id_entrada_key UNIQUE (id_carrito, id_entrada)
);

CREATE TABLE IF NOT EXISTS public.conversacion
(
    id serial NOT NULL,
    id_entrada integer,
    id_comprador integer,
    id_vendedor integer,
    fecha_creacion timestamp without time zone DEFAULT now(),
    CONSTRAINT conversacion_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.entrada
(
    id_entrada serial NOT NULL,
    id_evento integer NOT NULL,
    id_vendedor integer NOT NULL,
    precio_venta numeric(10, 2) NOT NULL,
    estado_entrada character varying(20) DEFAULT 'disponible',
    fecha_publicacion date DEFAULT CURRENT_DATE,
    iduuid uuid,
    id_pdf integer,
    CONSTRAINT entrada_pkey PRIMARY KEY (id_entrada)
);

CREATE TABLE IF NOT EXISTS public.entrada_pdf_imagen
(
    id_entrada serial NOT NULL,
    path_imagen character varying(100) NOT NULL,
    id_pdf integer NOT NULL,
    fecha_publicacion date DEFAULT CURRENT_DATE,
    CONSTRAINT entrada_pdf_imagen_pkey PRIMARY KEY (id_entrada),
    CONSTRAINT entrada_pdf_imagen_path_imagen_key UNIQUE (path_imagen)
);

CREATE TABLE IF NOT EXISTS public.entrada_pdf_tmp
(
    id_entrada serial NOT NULL,
    nombre_pdf character varying(255),
    id_usuario integer NOT NULL,
    hash character(64) NOT NULL,
    num_pagin integer,
    fecha_publicacion date,
    CONSTRAINT entrada_pdf_tmp_pkey PRIMARY KEY (id_entrada),
    CONSTRAINT entrada_pdf_tmp_hash_key UNIQUE (hash)
);

CREATE TABLE IF NOT EXISTS public.evento_imagen
(
    id_evento_imagen serial NOT NULL,
    path_imagen character varying(100) NOT NULL,
    evento_id integer NOT NULL,
    fecha_publicacion date DEFAULT CURRENT_DATE,
    CONSTRAINT evento_imagen_pkey PRIMARY KEY (id_evento_imagen),
    CONSTRAINT evento_imagen_path_imagen_key UNIQUE (path_imagen)
);

CREATE TABLE IF NOT EXISTS public.localizacion
(
    localizacion_id serial NOT NULL,
    nombre character varying(150) NOT NULL,
    calle character varying(150) NOT NULL,
    municipio character varying(100) NOT NULL,
    localidad character varying(100) NOT NULL,
    codigo_postal character varying(10) NOT NULL,
    CONSTRAINT localizacion_pkey PRIMARY KEY (localizacion_id)
);

CREATE TABLE IF NOT EXISTS public.tipo_evento
(
    tipo_evento_id serial NOT NULL,
    nombre character varying(50) NOT NULL,
    CONSTRAINT tipo_evento_pkey PRIMARY KEY (tipo_evento_id),
    CONSTRAINT tipo_evento_nombre_key UNIQUE (nombre)
);

CREATE TABLE IF NOT EXISTS public.eventos
(
    evento_id serial NOT NULL,
    nombre_evento character varying(150) NOT NULL,
    tipo_evento_id integer NOT NULL,
    localizacion_id integer NOT NULL,
    fecha_evento timestamp without time zone NOT NULL,
    descripcion character varying(150),
    webident uuid,
    CONSTRAINT eventos_pkey PRIMARY KEY (evento_id)
);

CREATE TABLE IF NOT EXISTS public.mensaje
(
    id serial NOT NULL,
    id_conversacion integer,
    id_emisor integer,
    contenido text NOT NULL,
    fecha_envio timestamp without time zone DEFAULT now(),
    leido boolean DEFAULT false,
    CONSTRAINT mensaje_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.oferta
(
    id_oferta serial NOT NULL,
    id_entrada integer NOT NULL,
    id_comprador integer NOT NULL,
    precio_oferta numeric(10, 2) NOT NULL,
    estado_oferta character varying(20) NOT NULL DEFAULT 'PENDIENTE',
    fecha_oferta timestamp without time zone NOT NULL DEFAULT now(),
    fecha_expiracion timestamp without time zone,
    mensaje character varying(500),
    CONSTRAINT oferta_pkey PRIMARY KEY (id_oferta)
);

CREATE TABLE IF NOT EXISTS public.refresh_token
(
    id uuid NOT NULL,
    token character varying(100) NOT NULL,
    user_id integer NOT NULL,
    created_at timestamp without time zone DEFAULT now(),
    expires_at timestamp without time zone NOT NULL,
    revoked boolean DEFAULT false,
    CONSTRAINT refresh_token_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.usuario
(
    id_usuario serial NOT NULL,
    nombre character varying(100) NOT NULL,
    email character varying(100) NOT NULL,
    contrasena character varying(100) NOT NULL,
    numero_cuenta character varying(34) NOT NULL,
    fecha_registro timestamp without time zone DEFAULT now(),
    role character varying(100),
    CONSTRAINT usuario_pkey PRIMARY KEY (id_usuario),
    CONSTRAINT usuario_email_key UNIQUE (email)
);

-- Foreign keys

ALTER TABLE IF EXISTS public.barcode_registro
    ADD CONSTRAINT fk_barcode_entrada_tmp FOREIGN KEY (id_entrada_tmp)
    REFERENCES public.entrada_pdf_tmp (id_entrada) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE IF EXISTS public.barcode_registro
    ADD CONSTRAINT fk_barcode_usuario FOREIGN KEY (id_usuario)
    REFERENCES public.usuario (id_usuario) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE IF EXISTS public.batch_job_execution
    ADD CONSTRAINT job_inst_exec_fk FOREIGN KEY (job_instance_id)
    REFERENCES public.batch_job_instance (job_instance_id);

ALTER TABLE IF EXISTS public.batch_job_execution_context
    ADD CONSTRAINT job_exec_ctx_fk FOREIGN KEY (job_execution_id)
    REFERENCES public.batch_job_execution (job_execution_id);

CREATE INDEX IF NOT EXISTS batch_job_execution_context_pkey
    ON public.batch_job_execution_context(job_execution_id);

ALTER TABLE IF EXISTS public.batch_job_execution_params
    ADD CONSTRAINT job_exec_params_fk FOREIGN KEY (job_execution_id)
    REFERENCES public.batch_job_execution (job_execution_id);

ALTER TABLE IF EXISTS public.batch_step_execution
    ADD CONSTRAINT job_exec_step_fk FOREIGN KEY (job_execution_id)
    REFERENCES public.batch_job_execution (job_execution_id);

ALTER TABLE IF EXISTS public.batch_step_execution_context
    ADD CONSTRAINT step_exec_ctx_fk FOREIGN KEY (step_execution_id)
    REFERENCES public.batch_step_execution (step_execution_id);

CREATE INDEX IF NOT EXISTS batch_step_execution_context_pkey
    ON public.batch_step_execution_context(step_execution_id);

ALTER TABLE IF EXISTS public.carrito
    ADD CONSTRAINT carrito_id_usuario_fkey FOREIGN KEY (id_usuario)
    REFERENCES public.usuario (id_usuario) ON DELETE CASCADE;

ALTER TABLE IF EXISTS public.carrito_item
    ADD CONSTRAINT carrito_item_id_carrito_fkey FOREIGN KEY (id_carrito)
    REFERENCES public.carrito (id_carrito) ON DELETE CASCADE;

ALTER TABLE IF EXISTS public.carrito_item
    ADD CONSTRAINT carrito_item_id_entrada_fkey FOREIGN KEY (id_entrada)
    REFERENCES public.entrada (id_entrada) ON DELETE CASCADE;

ALTER TABLE IF EXISTS public.conversacion
    ADD CONSTRAINT conversacion_id_comprador_fkey FOREIGN KEY (id_comprador)
    REFERENCES public.usuario (id_usuario) ON DELETE CASCADE;

CREATE INDEX IF NOT EXISTS idx_conversacion_comprador ON public.conversacion(id_comprador);

ALTER TABLE IF EXISTS public.conversacion
    ADD CONSTRAINT conversacion_id_entrada_fkey FOREIGN KEY (id_entrada)
    REFERENCES public.entrada (id_entrada) ON DELETE CASCADE;

ALTER TABLE IF EXISTS public.conversacion
    ADD CONSTRAINT conversacion_id_vendedor_fkey FOREIGN KEY (id_vendedor)
    REFERENCES public.usuario (id_usuario) ON DELETE CASCADE;

CREATE INDEX IF NOT EXISTS idx_conversacion_vendedor ON public.conversacion(id_vendedor);

ALTER TABLE IF EXISTS public.entrada
    ADD CONSTRAINT fk_entrada_entradatmp FOREIGN KEY (id_pdf)
    REFERENCES public.entrada_pdf_tmp (id_entrada) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE IF EXISTS public.entrada
    ADD CONSTRAINT fk_entrada_evento_evento FOREIGN KEY (id_evento)
    REFERENCES public.eventos (evento_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE IF EXISTS public.entrada
    ADD CONSTRAINT fk_entrada_vendedor_vender FOREIGN KEY (id_vendedor)
    REFERENCES public.usuario (id_usuario) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE IF EXISTS public.entrada_pdf_imagen
    ADD CONSTRAINT fk_entrada_imagen_pdf FOREIGN KEY (id_pdf)
    REFERENCES public.entrada_pdf_tmp (id_entrada) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE IF EXISTS public.evento_imagen
    ADD CONSTRAINT fk_imagen_evento_to_evento FOREIGN KEY (evento_id)
    REFERENCES public.eventos (evento_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE IF EXISTS public.eventos
    ADD CONSTRAINT fk_localizacion_id FOREIGN KEY (localizacion_id)
    REFERENCES public.localizacion (localizacion_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE IF EXISTS public.eventos
    ADD CONSTRAINT fk_tipoevento_id FOREIGN KEY (tipo_evento_id)
    REFERENCES public.tipo_evento (tipo_evento_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE IF EXISTS public.mensaje
    ADD CONSTRAINT mensaje_id_conversacion_fkey FOREIGN KEY (id_conversacion)
    REFERENCES public.conversacion (id) ON DELETE CASCADE;

CREATE INDEX IF NOT EXISTS idx_mensaje_conversacion ON public.mensaje(id_conversacion);

ALTER TABLE IF EXISTS public.mensaje
    ADD CONSTRAINT mensaje_id_emisor_fkey FOREIGN KEY (id_emisor)
    REFERENCES public.usuario (id_usuario) ON DELETE CASCADE;

CREATE INDEX IF NOT EXISTS idx_mensaje_emisor ON public.mensaje(id_emisor);

ALTER TABLE IF EXISTS public.oferta
    ADD CONSTRAINT oferta_id_comprador_fkey FOREIGN KEY (id_comprador)
    REFERENCES public.usuario (id_usuario);

ALTER TABLE IF EXISTS public.oferta
    ADD CONSTRAINT oferta_id_entrada_fkey FOREIGN KEY (id_entrada)
    REFERENCES public.entrada (id_entrada) ON DELETE CASCADE;

ALTER TABLE IF EXISTS public.refresh_token
    ADD CONSTRAINT fk_usuario_token_user FOREIGN KEY (user_id)
    REFERENCES public.usuario (id_usuario) ON UPDATE CASCADE ON DELETE CASCADE;
