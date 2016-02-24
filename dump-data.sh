#!/bin/bash

FILE="data-$(date +%Y-%m-%d-%H-%M-%S).tbz2"
tar cvjf $FILE data

