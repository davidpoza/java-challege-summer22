CREATE TABLE "prices_tbl" (
	"id"	INTEGER NOT NULL,
	"date"	TEXT,
	"amount"	REAL NOT NULL,
	"discount"	REAL,
	"product_id"	INTEGER NOT NULL,
	PRIMARY KEY("id" AUTOINCREMENT),
	FOREIGN KEY("product_id") REFERENCES "products_tbl"("id") ON DELETE CASCADE
);

CREATE TABLE "products_tbl" (
	"id"	INTEGER NOT NULL,
	"name"	TEXT,
	"url"	TEXT,
	"brand"	TEXT,
	PRIMARY KEY("id" AUTOINCREMENT)
);