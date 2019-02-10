#!/usr/bin/make
SRC  := $(wildcard ./things/*.scad)
DEST := $(SRC:.scad=.stl)

all: $(DEST)

print-vars:
	@echo "SRC  = $(SRC)"
	@echo "DEST = $(DEST)"

%.stl: %.scad
	@printf "Generating $@ ... "
	@openscad ./$< -o ./$@
	@printf "Done.\n"
