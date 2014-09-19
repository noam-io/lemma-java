[![Build Status](https://travis-ci.org/noam-io/lemma-java.svg?branch=master)](https://travis-ci.org/noam-io/lemma-java)
## Java Lemma Repository
------------------------

This repository three modules for connecting to Noam from Java in various ways.

* **lemma-lib** - A pure [Java](http://www.java.com/) library
* **processing-library** - A packaged library for [Processing](http://processing.org/) wrapping the Java library
* **max-external** - A thin wrapper around the Java library creating a bridge for use in [Max](http://cycling74.com/products/max/) patches


### Getting started

This repository can built using [Maven](http://maven.apache.org/). The quickest way to install Maven on OSX is [Homebrew](http://brew.sh/).

##### Install Homebrew if necessary

Follow the [instructions](http://brew.sh/), or use the following one liner:

        ruby -e "$(curl -fsSL https://raw.github.com/mxcl/homebrew/go)"
        
##### Install Maven

Follow the [instructions](http://maven.apache.org/download.cgi), or use homebrew:

        brew install maven
        
##### Build 

Clone the repo.  

        cd <project directory>
        mvn clean package
        
This will create zip archives under the "target/" directory of each module containing all the dependencies, examples, and installation instructions.

See the individual module readme files for details.

* lemma-lib/[README.md](lemma-lib/README.md)
* processing-library/[README.md](processing-library/README.md)
* max-external/[README.md](max-external/README.md)

