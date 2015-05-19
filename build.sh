#!/bin/sh
OUTDIR="../jd-cli/"
rm -rf jd-cli.jar tmp/* $OUTDIR/* &&
mkdir -p tmp $OUTDIR &&
javac -d tmp -cp "lib/*" `find jd-cli jd-lib | grep '\.java'` &&
jar cvfm jd-cli.jar MANIFEST.MF -C tmp . &&
cp jd-cli.jar lib/* $OUTDIR &&
rm -rf jd-cli.jar tmp
