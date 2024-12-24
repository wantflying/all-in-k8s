#!/bin/bash
kubectl -n happy exec -it redis-leader-0 -- redis-cli -a 123456  -h redis-leader-0 -p 6379 config set cluster-announce-ip 192.168.123.136
kubectl -n happy exec -it redis-leader-0 -- redis-cli -a 123456  -h redis-leader-0 -p 6379 config set cluster-announce-port 31000
kubectl -n happy exec -it redis-leader-0 -- redis-cli -a 123456  -h redis-leader-0 -p 6379 config set cluster-announce-bus-port 32000
kubectl -n happy exec -it redis-leader-0 -- redis-cli -a 123456  -h redis-leader-0 -p 6379 config rewrite

kubectl -n happy exec -it redis-leader-0 -- redis-cli -a 123456  -h redis-leader-1 -p 6379 config set cluster-announce-ip 192.168.123.136
kubectl -n happy exec -it redis-leader-0 -- redis-cli -a 123456  -h redis-leader-1 -p 6379 config set cluster-announce-port 31001
kubectl -n happy exec -it redis-leader-0 -- redis-cli -a 123456  -h redis-leader-1 -p 6379 config set cluster-announce-bus-port 32001
kubectl -n happy exec -it redis-leader-0 -- redis-cli -a 123456  -h redis-leader-1 -p 6379 config rewrite

kubectl -n happy exec -it redis-leader-0 -- redis-cli -a 123456  -h redis-leader-2 -p 6379 config set cluster-announce-ip 192.168.123.136
kubectl -n happy exec -it redis-leader-0 -- redis-cli -a 123456  -h redis-leader-2 -p 6379 config set cluster-announce-port 31002
kubectl -n happy exec -it redis-leader-0 -- redis-cli -a 123456  -h redis-leader-2 -p 6379 config set cluster-announce-bus-port 32002
kubectl -n happy exec -it redis-leader-0 -- redis-cli -a 123456  -h redis-leader-2 -p 6379 config rewrite

kubectl -n happy exec -it redis-leader-0 -- redis-cli -a 123456  -h redis-follower-0 -p 6379 config set cluster-announce-ip 192.168.123.136
kubectl -n happy exec -it redis-leader-0 -- redis-cli -a 123456  -h redis-follower-0 -p 6379 config set cluster-announce-port 31100
kubectl -n happy exec -it redis-leader-0 -- redis-cli -a 123456  -h redis-follower-0 -p 6379 config set cluster-announce-bus-port 32100
kubectl -n happy exec -it redis-leader-0 -- redis-cli -a 123456  -h redis-follower-0 -p 6379 config rewrite

kubectl -n happy exec -it redis-leader-0 -- redis-cli -a 123456  -h redis-follower-1 -p 6379 config set cluster-announce-ip 192.168.123.136
kubectl -n happy exec -it redis-leader-0 -- redis-cli -a 123456  -h redis-follower-1 -p 6379 config set cluster-announce-port 31101
kubectl -n happy exec -it redis-leader-0 -- redis-cli -a 123456  -h redis-follower-1 -p 6379 config set cluster-announce-bus-port 32101
kubectl -n happy exec -it redis-leader-0 -- redis-cli -a 123456  -h redis-follower-1 -p 6379 config rewrite

kubectl -n happy exec -it redis-leader-0 -- redis-cli -a 123456  -h redis-follower-2 -p 6379 config set cluster-announce-ip 192.168.123.136
kubectl -n happy exec -it redis-leader-0 -- redis-cli -a 123456  -h redis-follower-2 -p 6379 config set cluster-announce-port 31102
kubectl -n happy exec -it redis-leader-0 -- redis-cli -a 123456  -h redis-follower-2 -p 6379 config set cluster-announce-bus-port 32102
kubectl -n happy exec -it redis-leader-0 -- redis-cli -a 123456  -h redis-follower-2 -p 6379 config rewrite

kubectl -n happy exec -it redis-leader-0 -- redis-cli -a 123456  -h redis-follower-2 -p 6379 cluster nodes

