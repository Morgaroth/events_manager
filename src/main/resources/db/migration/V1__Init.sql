CREATE TABLE times (
  start TIMESTAMPTZ NOT NULL,
  "end"   TIMESTAMPTZ NOT NULL,
  kind  VARCHAR     NOT NULL,
  state VARCHAR     NOT NULL,
  tags  TEXT ARRAY  NOT NULL,
  id    UUID        NOT NULL PRIMARY KEY
)
