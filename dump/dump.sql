CREATE TABLE "films" (
  "id" int PRIMARY KEY,
  "name" varchar,
  "description" varchar,
  "release_date" date,
  "duration" int,
  "rating" int
);

CREATE TABLE "users" (
  "id" int PRIMARY KEY,
  "login" varchar,
  "name" varchar,
  "email" varchar,
  "birthday" date
);

CREATE TABLE "likes" (
  "film_id" int,
  "user_id" int
);

CREATE TABLE "friendship" (
  "id" int,
  "friend_id" int,
  "is_confirmed" boolean
);

CREATE TABLE "genres" (
  "id" int,
  "name" varchar
);

CREATE TABLE "film_genre" (
  "film_id" int,
  "genre_id" int
);

CREATE TABLE "ratings" (
  "id" int,
  "name" varchar
);

ALTER TABLE "likes" ADD FOREIGN KEY ("film_id") REFERENCES "films" ("id");

ALTER TABLE "likes" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");

ALTER TABLE "friendship" ADD FOREIGN KEY ("id") REFERENCES "users" ("id");

ALTER TABLE "friendship" ADD FOREIGN KEY ("friend_id") REFERENCES "users" ("id");

ALTER TABLE "film_genre" ADD FOREIGN KEY ("film_id") REFERENCES "films" ("id");

ALTER TABLE "film_genre" ADD FOREIGN KEY ("genre_id") REFERENCES "genres" ("id");

ALTER TABLE "films" ADD FOREIGN KEY ("rating") REFERENCES "ratings" ("id");
