#!/bin/sh

java=java

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
    -Djdbc.driver="${db:-org.postgresql.Driver}" \
    -Djdbc.url="${dburl:-jdbc:postgresql://localhost:5432/hellophpworld}" \
    -Djdbc.username="${dbuser:-hellophpworld}" \
    -Djdbc.password="${dbpass:-P68ntEvJQbhI}" \
    -jar extract-tool-*-cli.jar \
    -c /dev/null \
    -J ~/.m2/repository/org/postgresql/postgresql/42.7.3/postgresql-42.7.3.jar \
    "${1:-$(dirname "$0")/example.jsn}"
