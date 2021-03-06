-- apply changes
create table dsequence_definition (
  id                            uuid not null,
  namespace                     varchar(99) not null,
  name                          varchar(99) not null,
  start                         bigint not null,
  padding                       smallint,
  max                           bigint,
  prefix                        varchar(255),
  suffix                        varchar(255),
  constraint uq_dsequence_definition_namespace_name unique (namespace,name),
  constraint pk_dsequence_definition primary key (id)
);

