#!/bin/bash

cd target/
unzip processing-lemma-lib-bin.zip
rm -rf $HOME/Documents/Processing/libraries/lemma
mv processing-lemma-lib/lemma $HOME/Documents/Processing/libraries
rm -rf target/processing-lemma-lib
