#!/usr/bin/env bash
set -euo pipefail

CONTAINER="${ZENON_PG_CONTAINER:-zenon-postgres}"
DB_USER="${POSTGRES_USER:-zenon}"
DB_NAME="${POSTGRES_DB:-zenon_bank}"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

if ! docker ps --format '{{.Names}}' | grep -qx "$CONTAINER"; then
  echo "Container '$CONTAINER' is not running. Start with: docker compose up -d"
  exit 1
fi

docker exec -i "$CONTAINER" psql -v ON_ERROR_STOP=1 -U "$DB_USER" -d "$DB_NAME" \
  < "$SCRIPT_DIR/init/01-create-transactions.sql"

echo "Schema applied on $CONTAINER ($DB_NAME)."
