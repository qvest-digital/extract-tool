#!/bin/sh
# © mirabilos Ⓕ CC0 or MirBSD or Apache 2 or MIT

java=java

if test -z "$db$dburl$dbuser$dbpass"; then
	if test -z "$PGHOST$PGPORT$PGDATABASE$PGUSER$PGPASSWORD"; then
		echo >&2 "I: using fallback test-database parameters"
		PGHOST=localhost
		PGPORT=5432
		PGDATABASE=hellophpworld
		PGUSER=hellophpworld
		PGPASSWORD=P68ntEvJQbhI
	fi
	db=org.postgresql.Driver
	dburl=jdbc:postgresql://$PGHOST:$PGPORT/$PGDATABASE
	dbuser=$PGUSER
	dbpass=$PGPASSWORD
fi

isjava11() {
	$java "$@" --source 11 /dev/stdin >/dev/null 2>&1 <<'	EOF'
		class x {
			public static void main(String[] args) {
				System.exit(0);
			}
		}
	EOF
}

j11='--add-opens java.base/java.lang=ALL-UNNAMED'

if isjava11 $j11; then
	java="$java $j11"
fi

exec $java \
    -Djdbc.driver="$db" \
    -Djdbc.url="$dburl" \
    -Djdbc.username="$dbuser" \
    -Djdbc.password="$dbpass" \
    -jar extract-tool-*-cli.jar \
    -c /dev/null \
    -J ~/.m2/repository/org/postgresql/postgresql/42.7.3/postgresql-42.7.3.jar \
    "${1:-$(dirname "$0")/example.jsn}"
