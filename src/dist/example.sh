#!/bin/sh
# © mirabilos Ⓕ CC0 or MirBSD or Apache 2 or MIT

java=${JAVA:-java}

test -n "$jar" || for x in extract-tool-*-cli.jar; do
	if test -n "$jar"; then
		echo >&2 "E: multiple CLI JAR versions found"
		ls -l extract-tool-*-cli.jar | sed 's/^/N: /' >&2
		exit 1
	fi
	jar=$x
done
test -s "$jar" || echo >&2 "E: no CLI JAR found"

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
test -n "$dbjdbc" || if test x"$db" = x"org.postgresql.Driver"; then
	dbjdbc=~/.m2/repository/org/postgresql/postgresql/42.7.3/postgresql-42.7.3.jar
fi
test -s "$dbjdbc" || echo >&2 "W: cannot find JDBC driver"

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
    -jar "$jar" \
    -c /dev/null \
    -J "$dbjdbc" \
    "${1:-$(dirname "$0")/example.jsn}"
