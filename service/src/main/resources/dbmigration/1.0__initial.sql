-- apply changes
create table dsequence_definition (
  id                            uuid not null,
  namespace                     varchar(99) not null,
  name                          varchar(99) not null,
  start                         bigint not null,
  constraint pk_dsequence_definition primary key (id)
);

create index ix_dsequence_definition_namespace_name on dsequence_definition (namespace,name);