#!/usr/bin/env bash
set -euo pipefail
rm -rf build/work
mkdir -p build/work
cd build/work
jar xf ../../baseline/MagicSMP.jar
jar xf ../../guard/target/magicsmp-sell-guard-2.0.jar
rm -f META-INF/*.SF META-INF/*.RSA META-INF/*.DSA
jar cf ../MagicSMP.jar .
