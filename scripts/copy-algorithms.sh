#!/bin/bash

# Set Maven home directory
if [ "$#" -eq 1 ]
then
  MVN_HOME=$1
fi

# Test for parameters
if [ -z $MVN_HOME ]
then
  echo "Environment variable MVN_HOME (Maven home directory) must be set. As an alternative, provide MVN_HOME as an argument"
  echo "    e.g. copy-files.sh /opt/maven"
  exit 1
fi

# Set dir
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# Set destination properties
DEST_USER=root
DEST_IP=172.17.0.100

# Set Biohadoop algorithms home directory
BIOHADOOP_ALGORITHMS_PROJECT_HOME=$DIR/..

# Set Biohadoop algorithms version
BIOHADOOP_ALGORITHMS_CURRENT=biohadoop-algorithms*.jar

# Set remote dirs
LIB_TMP_DIR=/tmp/lib
LIB_HDFS_DIR=/biohadoop/lib
DATA_TMP_DIR=/tmp/data
DATA_HDFS_DIR=/biohadoop/data
CONF_TMP_DIR=/tmp/conf
CONF_HDFS_DIR=/biohadoop/conf

function build {
  echo "Building algorithms with Maven"
  $MVN_HOME/bin/mvn -f $BIOHADOOP_ALGORITHMS_PROJECT_HOME clean install
}

function copyLibRemote {
  echo "Copying lib to remote FS"
  ssh $DEST_USER@$DEST_IP "mkdir -p $LIB_TMP_DIR"
  ssh $DEST_USER@$DEST_IP "rm $LIB_TMP_DIR/$BIOHADOOP_ALGORITHMS_CURRENT"
  
  scp -r $BIOHADOOP_ALGORITHMS_PROJECT_HOME/target/$BIOHADOOP_ALGORITHMS_CURRENT $DEST_USER@$DEST_IP:$LIB_TMP_DIR

  echo "Copying libs from remote FS to remote HDFS"
  ssh $DEST_USER@$DEST_IP "/opt/hadoop/current/bin/hdfs dfs -mkdir -p $LIB_HDFS_DIR"
  ssh $DEST_USER@$DEST_IP "/opt/hadoop/current/bin/hdfs dfs -copyFromLocal -f $LIB_TMP_DIR/$BIOHADOOP_ALGORITHMS_CURRENT $LIB_HDFS_DIR/$BIOHADOOP_ALGORITHMS_CURRENT"
  echo "Copying libs from remote FS to remote HDFS - OK"
}

function copyDataRemote {
  echo "Copying data to remote FS"
  ssh $DEST_USER@$DEST_IP "mkdir -p $DATA_TMP_DIR"
  scp -r $BIOHADOOP_ALGORITHMS_PROJECT_HOME/data/* $DEST_USER@$DEST_IP:$DATA_TMP_DIR

  echo "Copying data from remote FS to remote HDFS"
  ssh $DEST_USER@$DEST_IP "/opt/hadoop/current/bin/hdfs dfs -mkdir -p $DATA_HDFS_DIR"
  ssh $DEST_USER@$DEST_IP "/opt/hadoop/current/bin/hdfs dfs -copyFromLocal -f $DATA_TMP_DIR/* $DATA_HDFS_DIR"
  echo "Copying data from remote FS to remote HDFS - OK"
}

function copyConfRemote {
  echo "Copying configs to remote FS"
  ssh $DEST_USER@$DEST_IP "mkdir -p $CONF_TMP_DIR"
  scp -r $BIOHADOOP_ALGORITHMS_PROJECT_HOME/conf/* $DEST_USER@$DEST_IP:$CONF_TMP_DIR

  echo "Copying configs from remote FS to remote HDFS"
  ssh $DEST_USER@$DEST_IP "/opt/hadoop/current/bin/hdfs dfs -mkdir -p $CONF_HDFS_DIR"
  ssh $DEST_USER@$DEST_IP "/opt/hadoop/current/bin/hdfs dfs -copyFromLocal -f $CONF_TMP_DIR/* $CONF_HDFS_DIR"
  echo "Copying configs from remote FS to remote HDFS - OK"
}

# Build and copy to remote
build
copyLibRemote
copyDataRemote
copyConfRemote
