#!/usr/bin/env bash

CDIR=`dirname $0`
ODIR="`pwd`/html"

echo "CDIR: $CDIR"
echo "ODIR: $ODIR"

rm -rf "$ODIR"
mkdir -p "$ODIR"

echo "Copying images to ODIR..."
cp "$CDIR/"*.png "$ODIR/"

echo "Generating HTML from Markdown..."
for MD in "$CDIR/"*.md
do
	BN=`basename $MD`
	BBN=`basename $MD .md`
	HBN="$BBN.html"
	echo "  $BN -> $HBN"
	echo "<html><head><link rel=\"stylesheet\" href=\"styles.css\"><meta charset=\"UTF-8\"></head><body>" > "$ODIR/$HBN"
	perl -pe 's/\[(.*?)\]\((.*?)\.md\)/[\1](\2.html)/g' < "$MD" | markdown --html4tags >> "$ODIR/$HBN"
	echo "</body></html>" >> "$ODIR/$HBN"
done

echo "Generating CSS styles..."
echo "img {max-width:100%;display: block;margin-left:auto;margin-right:auto;border:thick double #32a1ce}" > "$ODIR/styles.css"


