CREATE TABLE "genre" (
  "id" int PRIMARY KEY,
  "name" varchar NOT NULL
);

CREATE TABLE "rating" (
  "id" int PRIMARY KEY,
  "name" varchar NOT NULL
);

CREATE TABLE "films" (
  "id" int PRIMARY KEY,
  "name" varchar NOT NULL,
  "description" varchar,
  "releaseDate" date,
  "duration" int,
  "genre_id" int,
  "rating_id" int
);

CREATE TABLE "users" (
  "id" int PRIMARY KEY,
  "login" varchar NOT NULL,
  "name" varchar,
  "email" varchar NOT NULL,
  "birthday" date
);

CREATE TABLE "likes" (
  "id" int PRIMARY KEY,
  "film_id" int,
  "user_id" int
);

CREATE TABLE "friends" (
  "id" int PRIMARY KEY,
  "userOne_id" int,
  "userTwo_id" int,
  "confirmed" bool
);

ALTER TABLE "films" ADD FOREIGN KEY ("genre_id") REFERENCES "genre" ("id");

ALTER TABLE "films" ADD FOREIGN KEY ("rating_id") REFERENCES "rating" ("id");

ALTER TABLE "likes" ADD FOREIGN KEY ("film_id") REFERENCES "films" ("id");

ALTER TABLE "likes" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");

ALTER TABLE "friends" ADD FOREIGN KEY ("userOne_id") REFERENCES "users" ("id");

ALTER TABLE "friends" ADD FOREIGN KEY ("userTwo_id") REFERENCES "users" ("id");
