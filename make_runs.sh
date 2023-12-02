#!/bin/sh

# look for folders named Year{} and for each, copy RunConfigTemplate.xml to .idea/runConfigurations/Run_{}.xml
# the replace {YEAR} in the file with the year number

for i in Year*; do
    echo "Found module $i"
    year=$(echo "$i" | sed 's/Year//')
    cp "RunConfigTemplate.xml" ".idea/runConfigurations/Run_$year.xml"
    sed -i '' "s/{YEAR}/$year/g" ".idea/runConfigurations/Run_$year.xml"
done