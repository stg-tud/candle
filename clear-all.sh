#!/bin/bash

./bin/shutdown.sh
rm logs/*
rm data/player/*
rm data/events/*
./bin/startup.sh

