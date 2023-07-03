#!/bin/bash

num_execution=$1

properties_file=$PWD/src/main/resources/app.properties
cat >$properties_file <<EOL
db_port=3306
app_port=3000
browser=chrome
headless_browser=true
num_execution_flaky_test_suite=${num_execution}
wait=2000
EOL

./run.sh check_suite_flakiness