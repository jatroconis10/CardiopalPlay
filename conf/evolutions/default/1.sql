# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table brazalete (
  id                        bigserial not null,
  paciente_id               bigint,
  constraint uq_brazalete_paciente_id unique (paciente_id),
  constraint pk_brazalete primary key (id))
;

create table cita (
  id                        bigserial not null,
  paciente_id               bigint,
  constraint pk_cita primary key (id))
;

create table consejo (
  id                        bigserial not null,
  descripcion               varchar(255),
  paciente_id               bigint,
  medico_id                 bigint,
  constraint pk_consejo primary key (id))
;

create table emergencia (
  id                        bigserial not null,
  fecha                     timestamp,
  lat                       float,
  lon                       float,
  paciente_id               bigint,
  constraint pk_emergencia primary key (id))
;

create table entrada (
  id                        bigserial not null,
  tipo                      integer,
  descripcion               varchar(255),
  historial_id              bigint,
  constraint ck_entrada_tipo check (tipo in (0,1,2)),
  constraint pk_entrada primary key (id))
;

create table historial (
  id                        bigserial not null,
  paciente_id               bigint,
  constraint uq_historial_paciente_id unique (paciente_id),
  constraint pk_historial primary key (id))
;

create table medicion_estres (
  id                        bigserial not null,
  nivel_estres              integer,
  fecha                     timestamp,
  historial_id              bigint,
  constraint ck_medicion_estres_nivel_estres check (nivel_estres in (0,1,2)),
  constraint pk_medicion_estres primary key (id))
;

create table medicion_frecuencia (
  id                        bigserial not null,
  fecha                     timestamp,
  latidos_pmin              integer,
  historial_id              bigint,
  constraint pk_medicion_frecuencia primary key (id))
;

create table medicion_presion (
  id                        bigserial not null,
  presion_diastolica        integer,
  presion_sistolica         integer,
  fecha                     timestamp,
  estado                    varchar(255),
  historial_id              bigint,
  constraint pk_medicion_presion primary key (id))
;

create table medico (
  id                        bigserial not null,
  nombres                   varchar(255),
  apellidos                 varchar(255),
  especializacion           integer,
  constraint ck_medico_especializacion check (especializacion in (0,1)),
  constraint pk_medico primary key (id))
;

create table paciente (
  id                        bigserial not null,
  nombres                   varchar(255),
  apellidos                 varchar(255),
  constraint pk_paciente primary key (id))
;

alter table brazalete add constraint fk_brazalete_paciente_1 foreign key (paciente_id) references paciente (id);
create index ix_brazalete_paciente_1 on brazalete (paciente_id);
alter table cita add constraint fk_cita_paciente_2 foreign key (paciente_id) references paciente (id);
create index ix_cita_paciente_2 on cita (paciente_id);
alter table consejo add constraint fk_consejo_paciente_3 foreign key (paciente_id) references paciente (id);
create index ix_consejo_paciente_3 on consejo (paciente_id);
alter table consejo add constraint fk_consejo_medico_4 foreign key (medico_id) references medico (id);
create index ix_consejo_medico_4 on consejo (medico_id);
alter table emergencia add constraint fk_emergencia_paciente_5 foreign key (paciente_id) references paciente (id);
create index ix_emergencia_paciente_5 on emergencia (paciente_id);
alter table entrada add constraint fk_entrada_historial_6 foreign key (historial_id) references historial (id);
create index ix_entrada_historial_6 on entrada (historial_id);
alter table historial add constraint fk_historial_paciente_7 foreign key (paciente_id) references paciente (id);
create index ix_historial_paciente_7 on historial (paciente_id);
alter table medicion_estres add constraint fk_medicion_estres_historial_8 foreign key (historial_id) references historial (id);
create index ix_medicion_estres_historial_8 on medicion_estres (historial_id);
alter table medicion_frecuencia add constraint fk_medicion_frecuencia_histori_9 foreign key (historial_id) references historial (id);
create index ix_medicion_frecuencia_histori_9 on medicion_frecuencia (historial_id);
alter table medicion_presion add constraint fk_medicion_presion_historial_10 foreign key (historial_id) references historial (id);
create index ix_medicion_presion_historial_10 on medicion_presion (historial_id);



# --- !Downs

drop table if exists brazalete cascade;

drop table if exists cita cascade;

drop table if exists consejo cascade;

drop table if exists emergencia cascade;

drop table if exists entrada cascade;

drop table if exists historial cascade;

drop table if exists medicion_estres cascade;

drop table if exists medicion_frecuencia cascade;

drop table if exists medicion_presion cascade;

drop table if exists medico cascade;

drop table if exists paciente cascade;

