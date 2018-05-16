# --- !Ups

create table "properties" (
  "id" bigserial primary key,
  "address" varchar not null,
  "postcode" varchar not null,
  "latitude" double precision not null,
  "longitude" double precision not null,
  "bedroom_count" int,
  "surface" double precision
);

create table "prices" (
  "id" bigserial primary key  ,
  "price" double precision not null,
  "date" date not null,
  "property_id" bigint
);

alter table "prices" add constraint "property_fk" foreign key ("property_id") references "properties"("id") on update restrict on delete cascade ;

# --- !Downs

drop table if exists "properties" ;
drop table if exists "prices" ;
