#!/bin/bash

set -e

HOSTS=($( env | grep _TCP_ADDR | cut -d = -f 1))

if [ ! -z "$HOSTS" ]; then
	echo -n "Waiting for linked containers to start..."
	for ((i = 0 ; i < "${#HOSTS[@]}" ; i++ ));
	do
		IP=($(env | grep "${HOSTS[$i]}" | cut -d = -f 2))
		if [ ! -z "${IP[0]}" ]; then
			PORT=($(env | grep "${HOSTS[$i]//_TCP_ADDR/_TCP_PORT}" | cut -d = -f 2))
			if [ ! -z "${PORT[0]}" ]; then
				while ! (exec 3<>/dev/tcp/${IP[0]}/${PORT[0]}) &>/dev/null; do
					echo -n "."
					sleep 1
				done
			fi
		fi
	done
	echo -n "LINKS: OK"
	echo
fi

mvn test
if [ $? -eq 0 ]; then
	exec "$@"
fi