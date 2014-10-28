#!/bin/bash

TEMP=`getopt -o a --longoptions mvn-home:,user:,host:,hdfs-cmd:,ba-dir:,ba-jar:,lib-dir:,lib-hdfs:,data-dir:,data-hdfs:,conf-dir:,conf-hdfs: -n 'copy-files.sh' -- "$@"`

if [ $? != 0 ] ; then echo "Terminating..." >&2 ; exit 1 ; fi

eval set -- "$TEMP"

unset MVN_HOME
unset USER
unset HOST
unset HDFS_CMD
unset BIOALGORITHM_PROJECT_DIR
unset BIOALGORITHM_JAR
unset LIB_TMP_DIR
unset LIB_HDFS_DIR
unset DATA_TMP_DIR
unset DATA_TMP_DIR
unset CONF_HDFS_DIR
unset CONF_HDFS_DIR

while true ; do
        case "$1" in
                --mvn-home) echo asd;MVN_HOME=$2 ; shift 2 ;;
                --user) USER=$2 ; shift 2 ;;
                --host) HOST=$2 ; shift 2 ;;
                --hdfs-cmd) HDFS_CMD=$2 ; shift 2 ;;
                --ba-dir) BIOALGORITHM_PROJECT_DIR=$2 ; shift 2 ;;
                --ba-jar) BIOALGORITHM_JAR=$2 ; shift 2 ;;
                --lib-dir) LIB_TMP_DIR=$2 ; shift 2 ;;
                --lib-hdfs) LIB_HDFS_DIR=$2 ; shift 2 ;;
                --data-dir) DATA_TMP_DIR=$2 ; shift 2 ;;
                --data-hdfs) DATA_HDFS_DIR=$2 ; shift 2 ;;
                --conf-dir) CONF_TMP_DIR=$2 ; shift 2 ;;
                --conf-hdfs) CONF_HDFS_DIR=$2 ; shift 2 ;;
                --) shift ; break ;;
                *) echo "Internal error!" ; exit 1 ;;
        esac
done

function usage {
  echo "Usage: --mvn-home Maven home dir --user Remote user --host Remote host --hdfs-cmd HDFS command on Hadoop --ba-dir Biohadoop algorithms project dir --ba-jar Biohadoop algorithms JAR file name --lib-dir Temporary lib dir on Hadoop --lib-hdfs Lib dir in HDFS --data-dir Temporary data dir on Hadoop --data-hdfs Data dir in HDFS --conf-dir Temporary conf dir on Hadoop --conf-hdfs Conf dir in HDFS"
  exit 1
}

function build {
  echo "Building Biohadoop algorithms with Maven"
  $MVN_HOME/bin/mvn -f $BIOALGORITHM_PROJECT_DIR clean install
  if [ "$?" -ne 0 ]
  then
    echo "Error while building Biohadoop algorithms"
    exit 1
  fi
}

function copyLibRemote {
  echo "Copying lib to remote FS"
  ssh $USER@$HOST "mkdir -p $LIB_TMP_DIR"
  ssh $USER@$HOST "rm $LIB_TMP_DIR/$BIOALGORITHM_JAR"
  
  scp -r $BIOALGORITHM_PROJECT_DIR/target/$BIOALGORITHM_JAR $USER@$HOST:$LIB_TMP_DIR

  echo "Copying libs from remote FS to remote HDFS"
  ssh $USER@$HOST "$HDFS_CMD dfs -mkdir -p $LIB_HDFS_DIR"
  ssh $USER@$HOST "$HDFS_CMD dfs -copyFromLocal -f $LIB_TMP_DIR/$BIOALGORITHM_JAR $LIB_HDFS_DIR/"
  echo "Copying libs from remote FS to remote HDFS - OK"
}

function copyDataRemote {
  echo "Copying data to remote FS"
  ssh $USER@$HOST "mkdir -p $DATA_TMP_DIR"
  scp -r $BIOALGORITHM_PROJECT_DIR/data/* $USER@$HOST:$DATA_TMP_DIR

  echo "Copying data from remote FS to remote HDFS"
  ssh $USER@$HOST "$HDFS_CMD dfs -mkdir -p $DATA_HDFS_DIR"
  ssh $USER@$HOST "$HDFS_CMD dfs -copyFromLocal -f $DATA_TMP_DIR/* $DATA_HDFS_DIR"
  echo "Copying data from remote FS to remote HDFS - OK"
}

function copyConfRemote {
  echo "Copying configs to remote FS"
  ssh $USER@$HOST "mkdir -p $CONF_TMP_DIR"
  scp -r $BIOALGORITHM_PROJECT_DIR/conf/* $USER@$HOST:$CONF_TMP_DIR

  echo "Copying configs from remote FS to remote HDFS"
  ssh $USER@$HOST "$HDFS_CMD dfs -mkdir -p $CONF_HDFS_DIR"
  ssh $USER@$HOST "$HDFS_CMD dfs -copyFromLocal -f $CONF_TMP_DIR/* $CONF_HDFS_DIR"
  echo "Copying configs from remote FS to remote HDFS - OK"
}

echo $MVN_HOME
echo $USER
echo $HOST
echo $HDFS_CMD
echo $BIOALGORITHM_PROJECT_DIR
echo $BIOALGORITHM_JAR
echo $LIB_TMP_DIR
echo $LIB_HDFS_DIR
echo $DATA_TMP_DIR
echo $DATA_HDFS_DIR
echo $CONF_TMP_DIR
echo $CONF_HDFS_DIR

if [ -z "$MVN_HOME" ] | [ -z "$USER" ] | [ -z "$HOST" ] | [ -z "$HDFS_CMD" ] | [ -z "$BIOALGORITHM_PROJECT_DIR" ] | [ -z "$BIOALGORITHM_JAR" ] | [ -z "$LIB_TMP_DIR" ] | [ -z "$LIB_HDFS_DIR" ] | [ -z "$DATA_TMP_DIR" ] | [ -z "$DATA_HDFS_DIR" ] | [ -z "$CONF_TMP_DIR" ] | [ -z "$CONF_HDFS_DIR" ]
then
  usage
  exit 1
fi

# Build and copy to remote
build
copyLibRemote
copyDataRemote
copyConfRemote

