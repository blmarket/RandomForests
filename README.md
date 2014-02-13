Decision Tree - Random Forests
------------------------------

scala Random Forests implementation using spark

It's not a production-ready code yet(see Known Issues below this doc)

## Prerequisites

* sbt ( >= 0.12.0, recommends 0.13)
* Good Internet Connection!  
sbt will download every requirements in build.sbt

## How to install

* run sbt
* type run
* sbt will do the rest

## Shut Up and show me example

    $ sbt run net.blmarket.TestTree
    will generate binary decision tree and error rate

## Known Issues

* It's not a Random Forests, it's yet a single decision tree.
  * I'll implement building multiple trees and voting process soon.
* It's slow.
  * Due to lack of knowledge of scala and spark, my code do some stupid.
  * Be paitient.
* It doesn't support missing values.
  * Will support Double.NaN values.

## Used Tools

* IntelliJ 13 CE
  * Used to edit most of the codes
