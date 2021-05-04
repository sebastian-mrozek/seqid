-- apply changes
alter table dsequence_definition add column length smallint;
alter table dsequence_definition add column max bigint;
alter table dsequence_definition add column prefix varchar(255);
alter table dsequence_definition add column suffix varchar(255);

