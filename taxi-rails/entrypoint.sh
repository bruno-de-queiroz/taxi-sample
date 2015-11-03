#!/bin/bash

set -e

if [ -f './tmp/pids/server.pid' ]; then
	rm ./tmp/pids/server.pid
fi

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

rake db:create
if [ $? -eq 0 ]; then
	rake db:migrate
	if [ $? -eq 0 ]; then
		rake test
		if [ $? -eq 0 ]; then
			exec "$@"
		fi
	fi
fi